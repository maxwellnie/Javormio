package io.github.maxwellnie.javormio.extension.accelerate;

/**
 * <p>实例工厂</p>
 *
 * @author Maxwell Nie
 */
public interface InstanceFactory {
    /**
     * <p>使用索引创建实例</p>
     *
     * @param constructorIndex 构造函数索引
     * @param <T>              实例类型
     * @return T
     */
    default <T> T produce(int constructorIndex) {
        throw new UnsupportedOperationException();
    }

    /**
     * <p>使用索引和参数创建实例</p>
     * <p>此方法在本框架中无实现，请谨慎使用<p/>
     *
     * @param constructorIndex 构造函数索引
     * @param args             构造函数参数
     * @param <T>              实例类型
     * @return T
     */
    default <T> T produce(int constructorIndex, Object... args) {
        throw new UnsupportedOperationException();
    }
}
