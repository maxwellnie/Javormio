package io.github.maxwellnie.javormio.core.database.result;
import io.github.maxwellnie.javormio.core.java.reflect.ObjectFactory;
import io.github.maxwellnie.javormio.core.java.reflect.Reflection;
import io.github.maxwellnie.javormio.core.java.reflect.property.impl.meta.MetaProperty;
import io.github.maxwellnie.javormio.core.utils.ReflectionUtils;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Maxwell Nie
 * @author yurongqi
 * 将ResultSet转换为Java实体对象
 */
public class ResultSetCOnvertorByQise implements ResultSetConvertor{
    ArrayList<TypeMapping> premetaList = new ArrayList<>();
    ArrayList<TypeMapping> lastmetaList = new ArrayList<>();
    @lombok.SneakyThrows
    @Override
    public Object convert(ResultSet resultSet, TypeMapping typeMapping, boolean multipleTable) {

        if (multipleTable){

        }else{
                return simpleConvert(resultSet, typeMapping);
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
     *
     * @param resultSet 数据库查询结果集，包含从数据库中查询到的数据
     * @param typeMapping 返回类型映射，用于指导如何将数据库数据映射到业务对象
     * @return 返回转换后的对象，如果转换过程中遇到错误或者无法转换，则返回null或者空对象
     */
    @lombok.SneakyThrows
    public Object simpleConvert(ResultSet resultSet, TypeMapping typeMapping){
        Reflection<?> reflection = ReflectionUtils.getReflection(typeMapping.getType());
        MetaProperty metaProperty = typeMapping.getMetaProperty();
        Object data = null;
//        int columCount = 0; //列数
        for (Map.Entry<String, TypeMapping> child : typeMapping.getChildren().entrySet()){
            premetaList.add(child.getValue());
 //           columCount++;
        }
        if (resultSet.isClosed())
            throw new ConvertException("resultSet is closed.");
        while (resultSet.next()) {
            int columnIndex = 0;
            ObjectFactory<?> objectFactory = reflection.getObjectFactory();
            for (TypeMapping index : premetaList){
                if(!typeMapping.isEntity()){
                    data = setValue(resultSet, index, objectFactory, columnIndex, metaProperty);
                    columnIndex++;
                }else{
                    simpleConvert(resultSet,index);
                }

                }
        }
        return data;
    }
    /**
     * 将ResultSet中的值设置到对象中,将resultSet中的单一数据赋值给单一对象属性，
     *
     * @param resultSet 数据库查询结果集
     * @param typeMapping 类型映射对象，包含列名和类型处理器等信息
     * @param objectFactory 对象工厂，用于创建或获取对象
     * @param columnIndex 结果集中的列索引
     * @param metaProperty 元属性，描述对象中的属性信息
     * @return 返回更新后的对象或对象工厂
     */
    @SneakyThrows
    public Object setValue(ResultSet resultSet, TypeMapping typeMapping, ObjectFactory<?> objectFactory, int columnIndex ,MetaProperty metaProperty){
        Object parent = null;
        String columnName = typeMapping.getColumnName();
        Object columnValue = typeMapping.getTypeHandler().getValue(resultSet, columnIndex);
        if (columnValue != null)
            typeMapping.getMetaProperty().getProperty().setValue(objectFactory, columnName, columnValue);
        if (parent != null ) {
            switch (metaProperty.getPropertyType()) {
                case ARRAY:
                case LIST:
                case MAP:
                    parent = metaProperty.getProperty().setValue(objectFactory, columnName, columnValue);
                    break;
                case SET:
                    parent = metaProperty.getProperty().setValue(objectFactory, null, columnValue);
                    break;
                default:
                    throw new ConvertException("The type [" + metaProperty.getType() + "] is not support.");
            }
        } else {
            parent = objectFactory;
        }
        return parent;
    }
}
