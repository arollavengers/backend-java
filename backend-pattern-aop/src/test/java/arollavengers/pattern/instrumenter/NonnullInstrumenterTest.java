package arollavengers.pattern.instrumenter;

import static org.fest.assertions.api.Assertions.assertThat;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.Loader;
import javassist.NotFoundException;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class NonnullInstrumenterTest {

    private ClassPool classPool;

    @Before
    public void setUp() {
        // make sure to have a fresh new class pool for each test
        classPool = new ClassPool();
        classPool.appendSystemPath();
    }

    @Test
    public void annotated_method_can_be_invoked_with_non_null_parameter()
            throws
            IllegalAccessException,
            InstantiationException,
            NotFoundException,
            CannotCompileException,
            InvocationTargetException,
            NoSuchMethodException, NoSuchFieldException
    {
        Class klazz = instrumentAndGetClass();
        Object underTest = klazz.newInstance();
        invokeAnnotatedMethod(klazz, underTest, 1, null, "bob");

        assertThat(klazz.getField("when").getInt(underTest)).isEqualTo(1);
        assertThat(klazz.getField("what").get(underTest)).isNull();
        assertThat(klazz.getField("who").get(underTest)).isEqualTo("bob");
    }

    @Test
    public void annotated_method_cannot_be_invoked_with_null_parameter()
            throws
            IllegalAccessException,
            InstantiationException,
            NotFoundException,
            CannotCompileException,
            InvocationTargetException,
            NoSuchMethodException, NoSuchFieldException
    {
        Class klazz = instrumentAndGetClass();
        Object underTest = klazz.newInstance();

        try {
            invokeAnnotatedMethod(klazz, underTest, 1, null, null);
            Assert.fail("An exception should have been raised");
        }
        catch (InvocationTargetException ite) {
            assertThat(ite.getCause()).isNotNull();
            assertThat(ite.getCause().getClass().getName()).isEqualTo("java.lang.IllegalArgumentException");
        }
    }

    private Class instrumentAndGetClass() throws NotFoundException, CannotCompileException {
        CtClass clazz = classPool.get(ClassUnderTest.class.getName());
        boolean instrumented = new NonnullInstrumenter().instrument(clazz);
        assertThat(instrumented).isTrue();

        // make sure to load the class within an other classloader
        // to prevent classes with the same to be loaded in the same classloader
        // which is not possible
        ClassLoader classLoader = new Loader(classPool);
        Class aClass = clazz.toClass(classLoader);
        assertThat(aClass.getName()).isEqualTo("arollavengers.pattern.instrumenter.NonnullInstrumenterTest$ClassUnderTest");
        return aClass;
    }

    private Object invokeAnnotatedMethod(Class aClass, Object underTest, Object... args)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
    {
        return aClass.getMethod("annotatedMethod", int.class, Integer.class, String.class).invoke(underTest, args);
    }

    public static class ClassUnderTest {
        public int when;

        public Integer what;

        @Nonnull
        public String who;

        public void annotatedMethod(@Nonnull int when, Integer what, @Nonnull String who) {
            this.when = when;
            this.what = what;
            this.who = who;
        }
    }

}
