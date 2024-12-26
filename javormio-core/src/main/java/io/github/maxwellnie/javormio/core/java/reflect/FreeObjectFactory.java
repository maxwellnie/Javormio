package io.github.maxwellnie.javormio.core.java.reflect;

/**
 * @author Maxwell Nie
 */
public class FreeObjectFactory implements ObjectFactory<Object> {
    @Override
    public Object produce() throws ReflectionException {
        throw new ReflectionException("It isn't supported.");
    }

    public <T> T produce(Class<?> clazz) throws ReflectionException {
        try {
            return (T) clazz.getConstructor().newInstance();
        } catch (Throwable e) {
            throw new ReflectionException("The constructor you are supporting demands parameters.Please use produce(Class.class, Object[].class).", e, false, true);
        }
    }

    public <T> T produce(Class<?> clazz, Object[] args) throws ReflectionException {
        try {
            if (args == null || args.length == 0) {
                return produce(clazz);
            }
            Class<?>[] types = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                if (args[i] == null)
                    throw new IllegalArgumentException("The parameter is null at " + i + ".");
                types[i] = args[i].getClass();
            }
            return (T) clazz.getConstructor(types).newInstance(args);
        } catch (Throwable e) {
            throw new ReflectionException("Please check your parameters and ensure they are complete.", e, false, true);
        }
    }

    public <T> T produce(Class<?> clazz, Class<?>[] parameters, Object[] args) throws ReflectionException {
        try {
            if ((parameters == null || parameters.length == 0) || (args == null || args.length == 0))
                return produce(clazz);
            return (T) clazz.getConstructor(parameters).newInstance(args);
        } catch (Throwable e) {
            throw new ReflectionException("Please check your parameters and ensure they are complete.", e, false, true);
        }
    }
}
