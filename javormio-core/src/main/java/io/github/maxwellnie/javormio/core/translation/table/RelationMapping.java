package io.github.maxwellnie.javormio.core.translation.table;

import io.github.maxwellnie.javormio.common.java.table.BaseMetaTableInfo;
import io.github.maxwellnie.javormio.common.java.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.common.java.table.primary.KeyGenerator;
import io.github.maxwellnie.javormio.common.java.table.primary.PrimaryInfo;

/**
 * @author Maxwell Nie
 */
public class RelationMapping<M> extends BaseMetaTableInfo<M>{
    protected BaseMetaTableInfo<M> masterInfo;
    protected SlaveInfo<M, ?>[] slaveInfos;
    protected boolean mappingAllColumnsToMaster;

    public RelationMapping(BaseMetaTableInfo<M> masterTableInfo, SlaveInfo<M, ?>[] slaveInfos, boolean mappingAllColumnsToMaster) {
        super(masterTableInfo.tableName, masterTableInfo.defaultDataSourceName, masterTableInfo.mappingClass, masterTableInfo.instanceInvoker);
        this.masterInfo = masterTableInfo;
        this.slaveInfos = slaveInfos;
        this.mappingAllColumnsToMaster = mappingAllColumnsToMaster;
    }

    public BaseMetaTableInfo<M> getMasterInfo() {
        return masterInfo;
    }

    public boolean isMappingAllColumnsToMaster() {
        return mappingAllColumnsToMaster;
    }

    public void setMappingAllColumnsToMaster(boolean mappingAllColumnsToMaster) {
        this.mappingAllColumnsToMaster = mappingAllColumnsToMaster;
    }

    public void setMasterInfo(BaseMetaTableInfo<M> masterInfo) {
        this.masterInfo = masterInfo;
    }

    public SlaveInfo<M, ?>[] getSlaveInfos() {
        return slaveInfos;
    }

    public void setSlaveInfos(SlaveInfo<M, ?>[] slaveInfos) {
        this.slaveInfos = slaveInfos;
    }

    @Override
    public ColumnInfo<M, ?>[] getColumnInfos() {
        return this.masterInfo.getColumnInfos();
    }

    @Override
    public PrimaryInfo<M, ?>[] getPrimaryInfos() {
        return this.masterInfo.getPrimaryInfos();
    }

    @Override
    public KeyGenerator<M, ?>[] getAllKeyGenerators() {
        return this.masterInfo.getAllKeyGenerators();
    }
}
