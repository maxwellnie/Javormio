package io.github.maxwellnie.javormio.core.database.result;
import io.github.maxwellnie.javormio.core.database.result.method.method;
import io.github.maxwellnie.javormio.core.java.reflect.ObjectFactory;
import io.github.maxwellnie.javormio.core.java.reflect.Reflection;
import io.github.maxwellnie.javormio.core.java.reflect.property.impl.meta.MetaProperty;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Maxwell Nie
 * @author yurongqi
 * 将ResultSet转换为Java实体对象
 */
public class ResultSetCOnvertorByQise implements ResultSetConvertor{
//    ArrayList<Reflection<?>> typeMappingsReflectionList = new ArrayList<>();
    method m = new method();
    @lombok.SneakyThrows
    @Override
    public Object convert(ResultSet resultSet, TypeMapping typeMapping, boolean multipleTable) {
        if (multipleTable){

        }else{
            if(resultSet.getRow()<=100){
                return simpleConvert(resultSet, typeMapping);
            } else{

            }
        }
        return null;
    }

    @Override
    public Object convert(List<ResultSet> resultSet, TypeMapping typeMapping, boolean multipleTable) {
        return null;
    }

    /**
     * 简单转换ResultSet对象为指定的对象类型
     * 此方法用于根据ResultSet对象和返回类型映射，将数据转换为用户指定的业务对象
     * 主要用于简化数据转换过程，提高代码复用性和灵活性
     * 递归来进行数据的转换，当数据为复杂类型时，将会递归调用该方法
     * 通过调用singleSetValue方法将数据设置到对象中，返回单一对象，然后通过setRowleValue方法进行递归处理将一行数据进行
     * 赋值到对象中，直到所有数据处理完成。最后通过遍历resultSet将所有的赋值给parent中，最后返回parent。
     *
     * @param resultSet 数据库查询结果集，包含从数据库中查询到的数据
     * @param typeMapping 返回类型映射，用于指导如何将数据库数据映射到业务对象
     * @return 返回转换后的对象，如果转换过程中遇到错误或者无法转换，则返回null或者空对象
     */

    public Object simpleConvert(ResultSet resultSet, TypeMapping typeMapping) throws ConvertException{
        int rowIndex = 0;
        Object parent = null;
        MetaProperty metaProperty = null;
        Reflection<?> reflection = m.getReflection(typeMapping);
        try{
            if (resultSet.isClosed())
                throw new ConvertException("resultSet is closed.");
            while (resultSet.next()) {
                ObjectFactory<?> objectFactory = reflection.getObjectFactory();
                Object rowData = setRowValue(resultSet, typeMapping, objectFactory, rowIndex);
                parent = metaProperty.getProperty().setValue(parent, null, rowData);
                rowIndex++;
            }
        }catch (Throwable e){
            throw new ConvertException("Convert error.", e);
        }
        return parent;
    }
    /**
     * 将ResultSet中的值设置到对象中,将resultSet中的单一数据赋值给单一对象属性，
     *
     * @param resultSet 数据库查询结果集
     * @param typeMapping 类型映射对象，包含列名和类型处理器等信息
     * @param objectFactory 对象工厂，用于创建或获取对象
     * @param columnIndex 结果集中的列索引
     * @return 返回更新后的对象或对象工厂
     */
    @SneakyThrows
    public Object setSingleValue(ResultSet resultSet, TypeMapping typeMapping, ObjectFactory<?> objectFactory, int columnIndex){
        String columnName = typeMapping.getColumnName();
        Object columnValue = typeMapping.getTypeHandler().getValue(resultSet, columnIndex);
        Object singleData = typeMapping.getMetaProperty().getProperty().setValue(objectFactory, columnName, columnValue);
        return singleData;
    }
    /**
     * 根据ResultSet设置角色值
     * 该方法通过递归方式处理结果集中的数据，并根据类型映射信息将数据转换为相应的类型
     *
     * @param resultSet ResultSet对象，包含从数据库查询到的数据
     * @param typeMapping 类型映射对象，描述了数据库表结构与Java对象的映射关系
     * @param objectFactory 对象工厂，用于创建Java对象实例
     * @param rowIndex 结果集中的列索引，指示当前处理的列
     * @return Object 返回转换后的对象实例
     */
    public Object setRowValue(ResultSet resultSet,TypeMapping typeMapping,ObjectFactory<?> objectFactory,int rowIndex){
        Object data = null;
        ArrayList<TypeMapping> premetaList = new ArrayList<>();
        for (Map.Entry<String, TypeMapping> child : typeMapping.getChildren().entrySet()){
            premetaList.add(child.getValue());
        }
        for (TypeMapping index : premetaList){
            if(!index.isEntity()){//跳出递归
                data = setSingleValue(resultSet, index, objectFactory, rowIndex);
            }else{
                try {
                    Reflection<?> reflection = m.getReflection(index);
                    ObjectFactory<?> objectFactory1 = null;
                    objectFactory1 = reflection.getObjectFactory();//新创建的对象
                    setRowValue(resultSet, index, objectFactory1, rowIndex);//实体类递归
                    setSingleValue(resultSet, index, objectFactory1, rowIndex);//设置新创建的对象
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }

            }
        }
        return data;
    }
}
