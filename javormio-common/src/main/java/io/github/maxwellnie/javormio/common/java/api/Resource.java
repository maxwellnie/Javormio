package io.github.maxwellnie.javormio.common.java.api;

import java.io.Serializable;

/**
 * 标识这是一个可序列化的资源
 *
 * @author Maxwell Nie
 */
public interface Resource extends Serializable {
    long serialVersionUID = Constants.SERIAL_VERSION_UID;
}
