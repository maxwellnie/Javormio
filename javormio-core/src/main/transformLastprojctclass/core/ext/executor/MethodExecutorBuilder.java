package com.maxwellnie.velox.sql.core.ext.executor;

import com.maxwellnie.velox.sql.core.meta.MetaData;
import com.maxwellnie.velox.sql.core.natives.database.sql.row.RowSql;
import com.maxwellnie.velox.sql.core.proxy.dao.executor.MethodExecutor;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Maxwell Nie
 */
public class MethodExecutorBuilder {
    private QuickCustomMethodExecutor quickCustomMethodExecutor;
    public Builder init(String name){
        quickCustomMethodExecutor = new QuickCustomMethodExecutor(name);
        return new Builder();
    }
    public class Builder {
        public Builder targetMethodExecutor(MethodExecutor targetMethodExecutor){
            quickCustomMethodExecutor.setTargetMethodExecutor(targetMethodExecutor);
            return this;
        }
        public Builder sql(String sql){
            quickCustomMethodExecutor.setSql(sql);
            return this;
        }
        public Builder paramsType(Class<?>[] paramsType){
            quickCustomMethodExecutor.setParamsType(paramsType);
            return this;
        }
        public Builder check(Consumer<Object[]> check){
            quickCustomMethodExecutor.setCheck(check);
            return this;
        }
        public Builder buildRowSql(Function<MetaData, RowSql> buildRowSql){
            quickCustomMethodExecutor.setBuildRowSql(buildRowSql);
            return this;
        }
        public MethodExecutorBuilder ok(){
            return MethodExecutorBuilder.this;
        }
    }
    public MethodExecutor build(){
        return quickCustomMethodExecutor;
    }
}
