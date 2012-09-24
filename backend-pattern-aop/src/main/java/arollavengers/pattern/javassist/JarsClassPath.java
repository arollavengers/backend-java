package arollavengers.pattern.javassist;

import javassist.ClassPath;
import javassist.NotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class JarsClassPath implements ClassPath {

    private List<Jar> jars;
    private boolean verbose = true;

    public JarsClassPath() {
    }

    public JarsClassPath initFromClasspath(String classpath) {
        jars = new ArrayList<Jar>();
        for (String string : classpath.split("[:;]")) {
            String trimmed = string.trim();
            if(!trimmed.isEmpty())
                jars.add(new Jar(trimmed));
        }
        return this;
    }

    @Override
    public InputStream openClassfile(String classname) throws NotFoundException {
        String adjustedClassname = adjustClassname(classname);
        for (Jar jar : jars) {
            try {
                InputStream stream = jar.openStream(adjustedClassname);
                if (stream != null) {
                    return stream;
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        throw new NotFoundException(classname);
    }

    private String adjustClassname(String classname) {
        return classname.replace('.', '/') + ".class";
    }

    @Override
    public URL find(String classname) {
        if(verbose)
            System.out.println("JarsClassPath.find(" + classname + ")");
        String adjustedClassname = adjustClassname(classname);
        for (Jar jar : jars) {
            try {
                if(verbose)
                    System.out.print("> " + jar.path + ": ");
                URL url = jar.url(adjustedClassname);
                if (url != null) {
                    if(verbose)
                        System.out.println("OK!");
                    return url;
                }
                if(verbose)
                System.out.println("KO");
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void close() {
        for (Jar jar : jars) {
            jar.close();
        }
    }

    public static class Jar {
        private final String path;
        private JarFile jarFile;
        private boolean inError;

        public Jar(String path) {
            this.path = path;
        }

        public JarFile open() {
            if (jarFile == null) {
                try {
                    jarFile = new JarFile(path);
                }
                catch (IOException e) {
                    inError = true;
                }
            }
            return jarFile;
        }

        public URL url(String adjustedClassname) throws MalformedURLException {
            if(inError)
                return null;
            JarFile jar = open();
            if(inError)
                return null;

            JarEntry entry = jar.getJarEntry(adjustedClassname);
            if (entry == null) {
                return null;
            }
            return new URL("jar:file:" + path + "!/" + adjustedClassname);
        }

        public InputStream openStream(String adjustedClassname) throws IOException {
            if(inError)
                return null;
            JarFile jar = open();
            if(inError)
                return null;

            JarEntry entry = jar.getJarEntry(adjustedClassname);
            if (entry == null) {
                return null;
            }
            return jar.getInputStream(entry);
        }

        public void close() {
            try {
                if (jarFile != null) {
                    jarFile.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
