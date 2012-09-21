package arollavengers.pattern.instrumenter;

import static org.fest.assertions.api.Assertions.assertThat;

import arollavengers.pattern.annotation.CanBeInvokedOnlyOnce;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.Loader;
import javassist.NotFoundException;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class CanBeInvokedOnlyOnceInstrumenterTest {

    private ClassPool classPool;

    @Before
    public void setUp() {
        // make sure to have a fresh new class pool for each test
        classPool = new ClassPool();
        classPool.appendSystemPath();
    }

    @Test
    public void annotated_method_can_be_invoked_at_least_once()
            throws
            NotFoundException,
            CannotCompileException,
            IllegalAccessException,
            InstantiationException,
            IOException, NoSuchMethodException, InvocationTargetException
    {
        Class aClass = instrumentAndGetClass();

        // Cannot be cast due to the different classloader usage => the class loaded is not the same
        // as the compiler has seen while compiling the test class
        Object underTest = /*(ClassUnderTest)*/aClass.newInstance();

        invokeAnnotatedMethod(aClass, underTest);
    }

    @Test
    public void annotated_method_can_only_be_invoked_once()
            throws
            NotFoundException,
            CannotCompileException,
            IllegalAccessException,
            InstantiationException,
            IOException, NoSuchMethodException, InvocationTargetException
    {
        Class aClass = instrumentAndGetClass();

        // Cannot be cast due to the different classloader usage => the class loaded is not the same
        // as the compiler has seen while compiling the test class
        Object underTest = /*(ClassUnderTest)*/aClass.newInstance();

        invokeAnnotatedMethod(aClass, underTest);

        try {
            invokeAnnotatedMethod(aClass, underTest);
            Assert.fail("An exception should have been raised");
        }
        catch (InvocationTargetException ite) {
            assertThat(ite.getCause()).isNotNull();
            assertThat(ite.getCause().getClass().getName()).isEqualTo("arollavengers.pattern.annotation.CanBeInvokedOnlyOnce$MethodAlreadyInvokedException");
        }
    }

    private Class instrumentAndGetClass() throws NotFoundException, CannotCompileException {
        CtClass clazz = classPool.get(ClassUnderTest.class.getName());
        boolean instrumented = new CanBeInvokedOnlyOnceInstrumenter().instrument(clazz);
        assertThat(instrumented).isTrue();

        // make sure to load the class within an other classloader
        // to prevent classes with the same to be loaded in the same classloader
        // which is not possible
        ClassLoader classLoader = new Loader(classPool);
        Class aClass = clazz.toClass(classLoader);
        assertThat(aClass.getName()).isEqualTo("arollavengers.pattern.instrumenter.CanBeInvokedOnlyOnceInstrumenterTest$ClassUnderTest");
        return aClass;
    }

    private Object invokeAnnotatedMethod(Class aClass, Object underTest)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
    {
        return aClass.getMethod("annotatedMethod", int.class).invoke(underTest, 1);
    }

    public static class ClassUnderTest {
        private int field1;
        private int field2;

        public void nonAnnotatedMethod(int value) {
            this.field1 = value;
        }

        @CanBeInvokedOnlyOnce
        public void annotatedMethod(int value) {
            this.field2 = value;
        }
    }
}
