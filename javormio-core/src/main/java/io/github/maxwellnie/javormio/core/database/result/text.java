package io.github.maxwellnie.javormio.core.database.result;

import com.maxwellnie.velox.sql.core.natives.database.mapping.returntype.ReturnTypeMapping;
import io.github.maxwellnie.javormio.core.java.reflect.Reflection;
import io.github.maxwellnie.javormio.core.utils.ReflectionUtils;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Maxwell Nie
 * @author yurongqi
 * 将ResultSet转换为Java实体对象
 */
public class text implements ResultSetConvertor{
    method stack1 = new method<>();
    method stack2 = new method<>();
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
     *
     * @param resultSet 数据库查询结果集，包含从数据库中查询到的数据
     * @param TypeMapping 返回类型映射，用于指导如何将数据库数据映射到业务对象
     * @return 返回转换后的对象，如果转换过程中遇到错误或者无法转换，则返回null或者空对象
     */
    @lombok.SneakyThrows
    public Object simpleConvert(ResultSet resultSet, TypeMapping TypeMapping){
        Class<?> mappingType = TypeMapping.getType();

        List<Object> resultList = new ArrayList<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        while (!resultSet.isClosed() && resultSet.next()) {
//            Reflection<?> reflection = ReflectionUtils.getReflection(mappingType);
            Object instance = mappingType.newInstance();
            for (int i = 1; i <= columnCount; i++){
                String columnName = metaData.getColumnName(i);
                Object columnValue = resultSet.getObject(i);
                try {
                    Field field = mappingType.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(instance, columnValue);
                } catch (NoSuchFieldException e) {
                    // 如果字段不存在，可以忽略或记录日志
                    System.out.println("Field " + columnName + " not found in class " + mappingType.getName());
                }
                resultList.add(instance);
            }

        }

        return resultList;
    }

}
