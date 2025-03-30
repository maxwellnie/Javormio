package io.github.maxwellnie.javormio.common.java.reflect;

/**
 * @author Maxwell Nie
 */
public class FreeObjectFactory implements ObjectFactory<Object> {
    @Override
    public Object produce() throws ReflectiveOperationException {
        throw new ReflectiveOperationException("It isn't supported.");
    }

    public <T> T produce(Class<?> clazz) throws ReflectiveOperationException {
        try {
            return (T) clazz.getConstructor().newInstance();
        } catch (Throwable e) {
            throw new ReflectiveOperationException("The constructor you are supporting demands parameters.Please use produce(Class.class, Object[].class).", e);
        }
    }

    public <T> T produce(Class<?> clazz, Object[] args) throws ReflectiveOperationException {
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
            throw new ReflectiveOperationException("Please check your parameters and ensure they are complete.", e);
        }
    }

    public <T> T produce(Class<?> clazz, Class<?>[] parameters, Object[] args) throws ReflectiveOperationException {
        try {
            if ((parameters == null || parameters.length == 0) || (args == null || args.length == 0))
                return produce(clazz);
            return (T) clazz.getConstructor(parameters).newInstance(args);
        } catch (Throwable e) {
            throw new ReflectiveOperationException("Please check your parameters and ensure they are complete.", e);
        }
    }
}
