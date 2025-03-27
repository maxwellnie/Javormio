package io.github.maxwellnie.javormio.framework.common.java.proxy;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Adaptor for Java version.
 *
 * @author Maxwell Nie
 * @since 1.0
 */
public class JavaVersionAdaptor {
    /**
     * JDK9+ introduces a novel method named "privateLookupIn" to handle PRIVATE and PROTECTED methods.
     *
     * @since 1.0
     */
    public static final Method highJavaVersionLookUpMethod;
    /**
     * JDK8 it is necessary to use an invisible constructor to instantiate the LookUp class to handle PRIVATE and PROTECTED methods.
     *
     * @since 1.0
     */
    public static final Constructor<MethodHandles.Lookup> java8LookupConstructor;
    public static int ALL_METHODS = MethodHandles.Lookup.PUBLIC | MethodHandles.Lookup.PRIVATE | MethodHandles.Lookup.PROTECTED | MethodHandles.Lookup.PACKAGE;

    //since 1.0
    static {
        //java9+
        Method privateLookupIn;
        try {
            privateLookupIn = MethodHandles.class.getMethod("privateLookupIn", Class.class, MethodHandles.Lookup.class);
        } catch (NoSuchMethodException e) {
            privateLookupIn = null;
        }
        highJavaVersionLookUpMethod = privateLookupIn;
        //java8
        Constructor<MethodHandles.Lookup> lookup = null;
        if (highJavaVersionLookUpMethod == null) {
            try {
                lookup = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
                lookup.setAccessible(true);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (Exception e) {
                lookup = null;
            }
        }
        java8LookupConstructor = lookup;
    }

    /**
     * @param method default method
     * @return MethodHandle
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @since 1.0
     */
    private static MethodHandle getHighJavaVersionDefaultMethodHandle(Method method)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final Class<?> declaringClass = method.getDeclaringClass();
        return ((MethodHandles.Lookup) highJavaVersionLookUpMethod.invoke(null, declaringClass, MethodHandles.lookup())).findSpecial(
                declaringClass, method.getName(), MethodType.methodType(method.getReturnType(), method.getParameterTypes()),
                declaringClass);
    }

    /**
     * @param method default method
     * @return MethodHandle
     * @throws IllegalAccessException    illegal access
     * @throws InstantiationException    instantiation
     * @throws InvocationTargetException invocation target
     * @since 1.0
     */
    private static MethodHandle getJava8DefaultMethodHandle(Method method)
            throws IllegalAccessException, InstantiationException, InvocationTargetException {
        final Class<?> declaringClass = method.getDeclaringClass();
        return java8LookupConstructor.newInstance(declaringClass, MethodHandles.Lookup.PRIVATE | MethodHandles.Lookup.PROTECTED).unreflectSpecial(method, declaringClass);
    }

    /**
     * Get a method handle of the virtual method.<br/>
     * oops! this method must be a public and virtual method.
     *
     * @param method virtual method
     * @return MethodHandle
     * @throws Throwable about reflect exception
     * @since 1.0
     */
    public static MethodHandle getVirtualMethodHandle(Method method) throws Throwable {
        final Class<?> declaringClass = method.getDeclaringClass();
        if (highJavaVersionLookUpMethod == null)
            return java8LookupConstructor.newInstance(declaringClass, ALL_METHODS).findVirtual(declaringClass, method.getName(), MethodType.methodType(method.getReturnType(), method.getParameterTypes()));
        else
            return ((MethodHandles.Lookup) highJavaVersionLookUpMethod.invoke(null, declaringClass, MethodHandles.lookup())).findVirtual(
                    declaringClass, method.getName(), MethodType.methodType(method.getReturnType(), method.getParameterTypes()));
    }

    /**
     * Get a method handle of the virtual method.<br/>
     * oops! this method must be a public and virtual method.
     *
     * @param declaringClass declaring class of virtual method
     * @param methodName     method name
     * @param returnType     return type
     * @param parameterTypes parameter types
     * @return MethodHandle
     * @throws Throwable about reflect exception
     * @since 1.0
     */
    public static MethodHandle getVirtualMethodHandle(Class<?> declaringClass, String methodName, Class<?> returnType, Class<?>[] parameterTypes) throws Throwable {
        if (highJavaVersionLookUpMethod == null)
            return java8LookupConstructor.newInstance(declaringClass, ALL_METHODS).findVirtual(declaringClass, methodName, MethodType.methodType(returnType, parameterTypes));
        else
            return ((MethodHandles.Lookup) highJavaVersionLookUpMethod.invoke(null, declaringClass, MethodHandles.lookup())).findVirtual(
                    declaringClass, methodName, MethodType.methodType(returnType, parameterTypes));
    }

    /**
     * Handle default method.
     *
     * @param method default method
     * @param proxy  target object
     * @param args   arguments
     * @return Object
     * @throws Throwable about reflect exception
     * @since 1.0
     */
    public static Object handleDefaultMethod(Method method, Object proxy, Object[] args) throws Throwable {
        MethodHandle methodHandle = getJava8DefaultMethodHandle(method);
        if (highJavaVersionLookUpMethod == null)
            return methodHandle.bindTo(proxy).invokeWithArguments(args);
        else
            return getHighJavaVersionDefaultMethodHandle(method).bindTo(proxy).invokeWithArguments(args);
    }
}
