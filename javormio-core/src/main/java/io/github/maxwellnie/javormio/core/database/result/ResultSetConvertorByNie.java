package io.github.maxwellnie.javormio.core.database.result;

import io.github.maxwellnie.javormio.core.java.reflect.ObjectFactory;
import io.github.maxwellnie.javormio.core.java.reflect.Reflection;
import io.github.maxwellnie.javormio.core.java.reflect.ReflectionException;
import io.github.maxwellnie.javormio.core.java.reflect.property.impl.meta.MetaProperty;
import io.github.maxwellnie.javormio.core.utils.ReflectionUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author Maxwell Nie
 */
public class ResultSetConvertorByNie implements ResultSetConvertor{
    private Object simpleConvert(ResultSet resultSet, TypeMapping typeMapping) throws ConvertException {
        if(resultSet == null || typeMapping == null)
            throw new ConvertException("resultSet or typeMapping is null");
        else {
            try {
                Reflection<?> reflection = ReflectionUtils.getReflection(typeMapping.getType());
                ObjectFactory<?> entityObjectFactory = reflection.getObjectFactory();
                Object parent = null;
                MetaProperty metaProperty = null;
                boolean isEntity = typeMapping.isComplex() && typeMapping.isEntity();
                if (!isEntity){
                    parent = entityObjectFactory.produce();
                    metaProperty = typeMapping.getMetaProperty();
                    typeMapping = typeMapping
                            .getChildren()
                                .values()
                                    .iterator()
                                        .next();
                }
                int rowIndex = 0;
                if (resultSet.isClosed())
                    throw new ConvertException("resultSet is closed.");
                while (!resultSet.next()){
                    Object instance = entityObjectFactory.produce();
                    int columnIndex = 1;
                    for (Map.Entry<String, TypeMapping> child : typeMapping.getChildren().entrySet()){
                        String fieldName = child.getKey();
                        TypeMapping childTypeMapping = child.getValue();
                        Object columnValue = childTypeMapping.getTypeHandler().getValue(resultSet, columnIndex);
                        if(columnValue != null)
                            childTypeMapping.getMetaProperty().getProperty().setValue(instance, fieldName, columnValue);
                    }
                    if (parent != null && !isEntity){
                        switch (metaProperty.getPropertyType()){
                            case BASIC:
                                throw new ConvertException("The type ["+metaProperty.getType()+"] is not support.");
                            case ARRAY:
                            case LIST:
                            case MAP:
                                parent = metaProperty.getProperty().setValue(parent, rowIndex, instance);
                                break;
                            case SET:
                                parent = metaProperty.getProperty().setValue(parent, null, instance);
                                break;
                        }
                    }else {
                        parent = instance;
                    }
                    rowIndex++;
                }
                return parent;
            } catch (SQLException | NoSuchMethodException | ReflectionException e) {
                throw new ConvertException(e);
            }

        }
    }
    private Object multipleTableConvert(ResultSet resultSet, TypeMapping typeMapping) {
        return null;
    }
    @Override
    public Object convert(ResultSet resultSet, TypeMapping typeMapping, boolean multipleTable) throws ConvertException{
        if(multipleTable)
            return multipleTableConvert(resultSet, typeMapping);
        return simpleConvert(resultSet, typeMapping);
    }

    @Override
    public Object convert(List<ResultSet> resultSet, TypeMapping typeMapping, boolean multipleTable) throws ConvertException {
        return null;
    }
}
