package arollavengers.pattern.instrumenter;

import arollavengers.pattern.javassist.DirsClassPath;
import arollavengers.pattern.javassist.JarsClassPath;
import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class InstrumentClasses {

    private final ClassPool classPool;
    private final String classdir;
    private List<Instrumenter> instrumenterList = new ArrayList<Instrumenter>();

    public InstrumentClasses(File classdir) throws NotFoundException {
        this.classdir = classdir.getAbsolutePath();
        this.classPool = new ClassPool();
        this.classPool.appendSystemPath();
        this.classPool.appendClassPath(this.classdir);
    }

    public InstrumentClasses useSelfPath() {
        this.classPool.appendClassPath(new ClassClassPath(getClass()));
        return this;
    }
    public InstrumentClasses useClassPath(String classpath) {
        System.out.println("InstrumentClasses, Classpath: " + classpath);
        this.classPool.appendClassPath(new JarsClassPath().initFromClasspath(classpath));
        this.classPool.appendClassPath(new DirsClassPath().initFromClasspath(classpath));
        return this;
    }

    public InstrumentClasses useDefaultInstrumenters() {
        instrumenterList.add(new CanBeInvokedOnlyOnceInstrumenter());
        return this;
    }

    public void instrument(String className)
            throws NotFoundException, CannotCompileException, IOException
    {
        System.out.println("Analysing => " + className);
        String cleanedName = className.replaceAll("\\.class$", "").replace('/', '.');
        if (cleanedName.startsWith(".")) {
            cleanedName = cleanedName.substring(1);
        }

        CtClass ctClass = classPool.get(cleanedName);

        List<String> instrumented = new ArrayList<String>();
        for (Instrumenter instrumenter : instrumenterList) {
            try {
                if (instrumenter.instrument(ctClass)) {
                    instrumented.add(instrumenter.getName());
                }
            }
            catch (CannotCompileException ce) {
                throw new CannotCompileException(
                        "Failed to compile instrumentation " + instrumenter.getName() + " on class " + cleanedName, ce);
            }
        }
        if (!instrumented.isEmpty()) {
            ctClass.writeFile(classdir);
            System.out.println("  Class instrumented with " + instrumented);
        }
    }
}
