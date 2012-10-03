package arollavengers.pattern.javassist;

import javassist.ClassPath;
import javassist.NotFoundException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class DirsClassPath implements ClassPath {

    private List<Dir> dirs;
    private boolean verbose;

    public DirsClassPath() {
    }

    public DirsClassPath initFromClasspath(String classpath) {
        dirs = new ArrayList<Dir>();
        for (String string : classpath.split("[:;]")) {
            String trimmed = string.trim();
            if (!trimmed.isEmpty() && new File(trimmed).isDirectory()) {
                dirs.add(new Dir(trimmed));
            }
        }
        return this;
    }

    @Override
    public InputStream openClassfile(String classname) throws NotFoundException {
        String adjustedClassname = adjustClassname(classname);
        for (Dir dir : dirs) {
            try {
                InputStream stream = dir.openStream(adjustedClassname);
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
        if (verbose) {
            System.out.println("JarsClassPath.find(" + classname + ")");
        }
        String adjustedClassname = adjustClassname(classname);
        for (Dir dir : dirs) {
            try {
                if (verbose) {
                    System.out.print("> " + dir.path + ": ");
                }
                URL url = dir.url(adjustedClassname);
                if (url != null) {
                    if (verbose) {
                        System.out.println("OK!");
                    }
                    return url;
                }
                if (verbose) {
                    System.out.println("KO");
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void close() {
    }

    public static class Dir {
        private final String path;

        public Dir(String path) {
            this.path = path;
        }

        public URL url(String adjustedClassname) throws MalformedURLException {
            File file = new File(path + "/" + adjustedClassname);
            return file.toURI().toURL();
        }

        public InputStream openStream(String adjustedClassname) throws IOException {
            File file = new File(path + "/" + adjustedClassname);
            if(file.exists())
                return new FileInputStream(file);
            else
                return null;
        }
    }
}
