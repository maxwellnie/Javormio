package io.github.maxwellnie.javormio.core.translation.table;

import io.github.maxwellnie.javormio.common.java.table.BaseMetaTableInfo;
import io.github.maxwellnie.javormio.common.java.table.column.ColumnInfo;

/**
 * @author Maxwell Nie
 */
public class SlaveInfo<M, S>{
    protected BaseMetaTableInfo<M> masterTableInfo;
    protected BaseMetaTableInfo<S> slaveTableInfo;
    protected ColumnInfo<S, ?> slaveKey;
    protected ColumnInfo<M, ?> masterKey;
    protected String joinType;

    public BaseMetaTableInfo<M> getMasterTableInfo() {
        return masterTableInfo;
    }

    public void setMasterTableInfo(BaseMetaTableInfo<M> masterTableInfo) {
        this.masterTableInfo = masterTableInfo;
    }

    public BaseMetaTableInfo<S> getSlaveTableInfo() {
        return slaveTableInfo;
    }

    public void setSlaveTableInfo(BaseMetaTableInfo<S> slaveTableInfo) {
        this.slaveTableInfo = slaveTableInfo;
    }

    public ColumnInfo<S, ?> getSlaveKey() {
        return slaveKey;
    }

    public void setSlaveKey(ColumnInfo<S, ?> slaveKey) {
        this.slaveKey = slaveKey;
    }

    public ColumnInfo<M, ?> getMasterKey() {
        return masterKey;
    }

    public void setMasterKey(ColumnInfo<M, ?> masterKey) {
        this.masterKey = masterKey;
    }

    public String getJoinType() {
        return joinType;
    }

    public void setJoinType(String joinType) {
        this.joinType = joinType;
    }
}
