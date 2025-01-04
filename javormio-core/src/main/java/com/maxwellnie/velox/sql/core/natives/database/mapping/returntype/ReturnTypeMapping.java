package com.maxwellnie.velox.sql.core.natives.database.mapping.returntype;

/**
 * 返回值映射
 */
public class ReturnTypeMapping {
    //返回值类型
    Class<?> type;
    //返回值类型映射
    ReturnTypeInnerMapping returnTypeInnerMapping;
    boolean hasJoin;
    boolean isReturnManyObject;

    public ReturnTypeMapping() {
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public ReturnTypeInnerMapping getTypeMapping() {
        return returnTypeInnerMapping;
    }

    public void setTypeMapping(ReturnTypeInnerMapping returnTypeInnerMapping) {
        this.returnTypeInnerMapping = returnTypeInnerMapping;
    }

    public boolean isHasJoin() {
        return hasJoin;
    }

    public void setHasJoin(boolean hasJoin) {
        this.hasJoin = hasJoin;
    }

    public boolean isReturnManyObject() {
        return isReturnManyObject;
    }

    public void setReturnManyObject(boolean returnManyObject) {
        isReturnManyObject = returnManyObject;
    }

    @Override
    public String toString() {
        return "ReturnTypeMapping{" +
                "type=" + type +
                ", returnTypeInnerMapping=" + returnTypeInnerMapping +
                ", hasJoin=" + hasJoin +
                ", isReturnManyObject=" + isReturnManyObject +
                '}';
    }
}
