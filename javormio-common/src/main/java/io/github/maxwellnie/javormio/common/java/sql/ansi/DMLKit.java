package io.github.maxwellnie.javormio.common.java.sql.ansi;

/**
 * @author Maxwell Nie
 */
public interface DMLKit {
    StringBuilder pageBuild(StringBuilder sqlFragment, long offset, long limit, boolean isPrepared);
}
