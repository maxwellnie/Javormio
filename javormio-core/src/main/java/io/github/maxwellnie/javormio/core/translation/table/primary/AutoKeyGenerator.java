package io.github.maxwellnie.javormio.core.translation.table.primary;

import io.github.maxwellnie.javormio.common.java.api.BugException;
import io.github.maxwellnie.javormio.common.java.api.JavormioException;
import io.github.maxwellnie.javormio.common.java.proxy.MethodInvocationException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Maxwell Nie
 */
public class AutoKeyGenerator<E> implements KeyGenerator<E, Integer>{
    private PrimaryInfo<E, Integer> primaryInfo;

    @Override
    public void beforeInsert(E target) {

    }

    @Override
    public void beforeInsert(Collection<E> ts) {

    }

    @Override
    public void afterInsert(E target, Statement statement) throws SQLException {
        try {
            if (statement == null)
                throw new IllegalArgumentException();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet !=null && resultSet.next())
                primaryInfo.getMetaField().set(target, resultSet.getInt(1));
        } catch (MethodInvocationException e) {
            throw new PrimaryException(e);
        }
    }

    @Override
    public void afterInsert(List<E> ts, Statement statement) throws SQLException {
        try {
            if (statement == null)
                throw new IllegalArgumentException();
            ResultSet resultSet = statement.getGeneratedKeys();
            int[] generatedKeys = new int[ts.size()];
            int i = 0;
            while (resultSet !=null && resultSet.next()) {
                generatedKeys[i++] = resultSet.getInt(1);
            }
            if (i != ts.size()){
                throw new BugException("Bug: The number of inserted rows does not match the number of inserted objects.");
            }else {
                for (int j = 0; j < ts.size(); j++) {
                    E target = ts.get(j);
                    if (target != null)
                        primaryInfo.getMetaField().set(target, generatedKeys[j]);
                }
            }
        } catch (MethodInvocationException e) {
            throw new PrimaryException(e);
        }
    }

    @Override
    public void setPrimaryInfo(PrimaryInfo<E, Integer> primaryInfo) {
        this.primaryInfo = primaryInfo;
    }

    @Override
    public PrimaryInfo<E, Integer> getPrimaryInfo() {
        return this.primaryInfo;
    }

    @Override
    public boolean isAcceptGenerateKeys() {
        return true;
    }
}
