package com.maxwellnie.velox.sql.core.ext.template.engine.parser.impl;

import com.maxwellnie.velox.sql.core.annotation.dao.SQL;
import com.maxwellnie.velox.sql.core.ext.template.Param;
import com.maxwellnie.velox.sql.core.ext.template.SqlTemplateInfo;
import com.maxwellnie.velox.sql.core.ext.template.TemplateException;
import com.maxwellnie.velox.sql.core.ext.template.ValueReaderException;
import com.maxwellnie.velox.sql.core.ext.template.engine.parser.TemplateParser;
import com.maxwellnie.velox.sql.core.natives.database.mapping.param.ParamTypeMapping;
import com.maxwellnie.velox.sql.core.natives.database.sql.SqlType;
import com.maxwellnie.velox.sql.core.natives.database.table.TableInfo;
import com.maxwellnie.velox.sql.core.natives.stream.Node;
import com.maxwellnie.velox.sql.core.natives.type.convertor.TypeConvertor;
import com.maxwellnie.velox.sql.core.natives.type.convertor.TypeConvertorManager;
import com.maxwellnie.velox.sql.core.utils.base.StringUtils;
import com.maxwellnie.velox.sql.core.utils.base.TypeUtils;
import com.maxwellnie.velox.sql.core.utils.reflect.ReflectionUtils;
import com.maxwellnie.velox.sql.core.utils.reflect.property.ValueReader;
import com.maxwellnie.velox.sql.core.utils.reflect.property.impl.base.*;
import com.maxwellnie.velox.sql.core.utils.reflect.property.impl.meta.MetaProperty;
import com.maxwellnie.velox.sql.core.utils.reflect.property.impl.meta.NodeMetaProperty;

import java.lang.reflect.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Maxwell Nie
 */
public class DefaultTemplateParser implements TemplateParser {
    /**
     * 这是一个非常简单的模板解析器，包括${0},#{0},${a},#{a},${a.b},#{a.b}这几种模式
     *
     * @param template
     * @param method
     * @param tableInfo
     * @return SqlTemplateInfo
     */
    @Override
    public SqlTemplateInfo parse(String template, Method method, TableInfo tableInfo) {
        SqlTemplateInfo sqlTemplateInfo = new SqlTemplateInfo();
        String nativeSql = template;
        String regex = "[#+].*?}|[$].*?}";
        SQL sql = method.getAnnotation(SQL.class);
        boolean batch = sql.sqlType().equals(SqlType.BATCH_DELETE) || sql.sqlType().equals(SqlType.BATCH_INSERT) || sql.sqlType().equals(SqlType.BATCH);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(template);
        List<Node<ValueReader>> linkedWays = new LinkedList<>();
        sqlTemplateInfo.setSqlParamWays(linkedWays);
        List<KeyIndex> keyIndexes = new LinkedList<>();
        sqlTemplateInfo.setTemplateSql(template);
        Map<String, ClassInfo> paramClasses = new LinkedHashMap<>();
        parseParamClasses(method, paramClasses, batch);
        sqlTemplateInfo.setParameterTypeMapping(getParamTypeMapping(paramClasses, batch));
        while (matcher.find()) {
            String indexKey = matcher.group();
            Matcher indexMatcher = Pattern.compile("(.*)\\{\\d+}").matcher(indexKey);
            Matcher nameMatcher = Pattern.compile("(.*)\\{[A-Za-z0-9]+}|(.*)\\{(.*)\\.(.*)}").matcher(indexKey);
            String index = isMatch(indexMatcher);
            if (StringUtils.isNotNullOrEmpty(index)) {
                indexTypeParamHandle(index, indexKey, keyIndexes, linkedWays, sqlTemplateInfo, paramClasses, batch);
            } else if (StringUtils.isNotNullOrEmpty((index = isMatch(nameMatcher)))) {
                nameTypeParamHandle(index, indexKey, keyIndexes, linkedWays, sqlTemplateInfo, paramClasses, batch);
            } else {
                throw new TemplateException("The template has a error expression.");
            }
            nativeSql = nativeSql.replace(indexKey, "?");
        }
        sqlTemplateInfo.setNaiveSql(nativeSql);
        return sqlTemplateInfo;
    }

    /**
     * 处理索引类型的参数
     *
     * @param index
     * @param indexKey
     * @param keyIndexes
     * @param linkedWays
     * @param sqlTemplateInfo
     * @param paramTypes
     * @param batch
     */
    private void indexTypeParamHandle(String index, String indexKey, List<KeyIndex> keyIndexes, List<Node<ValueReader>> linkedWays, SqlTemplateInfo sqlTemplateInfo, Map<String, ClassInfo> paramTypes, boolean batch) {
        KeyIndex keyIndex = new KeyIndex(index, indexKey, true);
        keyIndexes.add(keyIndex);
        Node<ValueReader> linkedNode = new Node<>(null, null, null);
        linkedWays.add(linkedNode);
        findSqlParamWays(sqlTemplateInfo.getParameterTypeMapping(), paramTypes, linkedNode, keyIndex, batch);
    }

    /**
     * 处理名称类型的参数
     *
     * @param index
     * @param indexKey
     * @param keyIndexes
     * @param linkedWays
     * @param sqlTemplateInfo
     * @param paramTypes
     * @param batch
     */
    private void nameTypeParamHandle(String index, String indexKey, List<KeyIndex> keyIndexes, List<Node<ValueReader>> linkedWays, SqlTemplateInfo sqlTemplateInfo, Map<String, ClassInfo> paramTypes, boolean batch) {
        KeyIndex keyIndex = new KeyIndex(index, indexKey, false);
        keyIndexes.add(keyIndex);
        Node<ValueReader> linkedNode = new Node<>(null, null, null);
        linkedWays.add(linkedNode);
        findSqlParamWays(sqlTemplateInfo.getParameterTypeMapping(), paramTypes, linkedNode, keyIndex, batch);
    }

    /**
     * 获取参数
     *
     * @param paramTypes
     * @param batch
     * @return ParamTypeMapping
     */
    private void parseParamClasses(Method method, Map<String, ClassInfo> paramTypes, boolean batch) {
        int index = 0;
        for (Parameter parameter : method.getParameters()) {
            Class<?> type = parameter.getType();
            Type beanClass = type;
            if (batch) {
                if (TypeUtils.isCollection((Class<?>) beanClass)) {
                    beanClass = parameter.getParameterizedType();
                    if (beanClass instanceof ParameterizedType) {
                        beanClass = ((ParameterizedType) beanClass).getActualTypeArguments()[0];
                    }
                } else if (TypeUtils.isArray((Class<?>) beanClass)) {
                    beanClass = parameter.getType().getComponentType();
                } else
                    throw new TemplateException("The parameter type must be collection or array.");
            }
            String name;
            String key;
            TypeConvertor<?> convertor = null;
            if (parameter.isAnnotationPresent(Param.class)) {
                Param param = parameter.getAnnotation(Param.class);
                name = param.value();
                Class<? extends TypeConvertor> convertorClass = param.convertor();
                if (!convertorClass.equals(TypeConvertor.class)) {
                    try {
                        convertor = ReflectionUtils.newInstance(convertorClass);
                    } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                             IllegalAccessException e) {
                        throw new TemplateException(e);
                    }
                }
                key = parameter.getName();
            } else {
                key = name = parameter.getName();
            }
            paramTypes.put(name, new ClassInfo((Class<?>) beanClass, type, convertor, key, index++));
        }
    }

    /**
     * 递归查找参数链路
     *
     * @param rootTypeMapping
     * @param paramTypes
     * @param linkedNode
     * @param keyIndex
     * @param batch
     */
    private void findSqlParamWays(ParamTypeMapping rootTypeMapping, Map<String, ClassInfo> paramTypes, Node<ValueReader> linkedNode, KeyIndex keyIndex, boolean batch) {
        if (keyIndex.indexType) {
            int index = Integer.parseInt(keyIndex.key.substring(2, keyIndex.key.length() - 1));
            Iterator<String> keys = paramTypes.keySet().iterator();
            String key = null;
            int i = -1;
            for (; i < index && keys.hasNext(); i++) {
                key = keys.next();
            }
            if (i == index) {
                if (rootTypeMapping.getInnerTypeMappings() == null)
                    return;
                ParamTypeMapping sqlParamTypeMapping = rootTypeMapping.getInnerTypeMappings().get(String.valueOf(index));
                if (sqlParamTypeMapping == null)
                    throw new IllegalArgumentException("The template has a error expression.The key " + key + " isn't matching argument [" + index + "].");
                if (sqlParamTypeMapping.isBasicType())
                    sqlParamTypeMapping = rootTypeMapping;
                linkedNode.setValue(new ValueReader((configuration -> {
                    try {
                        return new ValueReader.Value(configuration.autoRead(), false, false);
                    } catch (Throwable e) {
                        throw new ValueReaderException(e);
                    }
                }), sqlParamTypeMapping.getPropertyDefined(), i, keyIndex.key));
            } else {
                throw new TemplateException("The template has a error expression.It isn't matching consistent argument.");
            }
        } else {
            String key = keyIndex.key;
            String[] ways = key.substring(2, key.length() - 1).split("\\.", -2);
            if (ways.length == 0)
                throw new TemplateException("The template has a error expression.The key " + key + " isn't matching argument.");
            boolean found = false;
            sortWays(ways);
            for (String paramKey : paramTypes.keySet()) {
                if (paramKey.equals(ways[0])) {
                    ways[0] = String.valueOf(paramTypes.get(paramKey).index);
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new TemplateException("Param name [" + ways[0] + "] not found.It may be a bug.");
            }
            findSqlParamsWaysByNameKey(linkedNode, ways, 0, rootTypeMapping, null, batch);
        }
    }

    /**
     * 排序链路关键字
     *
     * @param ways
     */
    private void sortWays(String[] ways) {
        for (int i = 0; i < ways.length - 1; i++) {
            for (int j = i; j < ways.length - 1; j++) {
                if (StringUtils.isNullOrEmpty(ways[j]) && j < ways.length - 1) {
                    ways[j] = ways[j + 1];
                    ways[j + 1] = null;
                }
            }
        }
    }

    /**
     * 递归查找参数链路
     *
     * @param linkedNode
     * @param ways
     * @param index
     * @param rootTypeMapping
     * @param paramType
     * @param batch
     */
    private void findSqlParamsWaysByNameKey(Node<ValueReader> linkedNode, String[] ways, int index, ParamTypeMapping rootTypeMapping, Class<?> paramType, boolean batch) {
        if (index > ways.length - 1 || ways[index] == null)
            return;
        if (paramType != null) {
            ParamTypeMapping sqlParamTypeMapping = getParamTypeMapping(paramType);
            if (rootTypeMapping != null) {
                Map<Object, ParamTypeMapping> paramTypeMappingMap = new LinkedHashMap<>();
                paramTypeMappingMap.put("0", sqlParamTypeMapping);
                rootTypeMapping.setInnerTypeMappings(paramTypeMappingMap);
            }
            rootTypeMapping = sqlParamTypeMapping;
        }
        if (rootTypeMapping == null)
            return;
        Node<ValueReader> next;
        if (index == 0)
            next = linkedNode;
        else
            next = new Node<>(null, linkedNode, null);
        if (rootTypeMapping.isArray()) {
            Class<?> componentType = rootTypeMapping.getType().getComponentType();
            next.setValue(new ValueReader((configuration -> {
                try {
                    return new ValueReader.Value(configuration.autoRead(), false, false);
                } catch (Throwable e) {
                    throw new ValueReaderException(e);
                }
            }), rootTypeMapping.getPropertyDefined(), Integer.parseInt(ways[index]), ways[index]));
            if (index != 0)
                linkedNode.setNext(next);
            findSqlParamsWaysByNameKey(next, ways, index + 1, null, componentType, batch);
        } else if (rootTypeMapping.isCollection()) {
            Object param = ways[index];
            if (!TypeUtils.isMap(rootTypeMapping.getType()))
                param = Integer.parseInt(ways[index]);
            next.setValue(new ValueReader((configuration -> {
                if (configuration.getBean() == null) {
                    return getInnerValue(ways, index, configuration);
                } else {
                    try {
                        return new ValueReader.Value(configuration.autoRead(), false, false);
                    } catch (Throwable e) {
                        throw new ValueReaderException(e);
                    }
                }
            }), rootTypeMapping.getPropertyDefined(), param, ways[index]));
            if (index != 0)
                linkedNode.setNext(next);
        } else {
            ParamTypeMapping sqlParamTypeMapping = rootTypeMapping.getInnerTypeMappings().get(ways[index]);
            if (sqlParamTypeMapping == null)
                throw new TemplateException("The template has a error expression.The key " + ways[index] + " isn't matching argument.");
            next.setValue(new ValueReader((configuration -> {
                try {
                    return new ValueReader.Value(configuration.autoRead(), false, false);
                } catch (Throwable e) {
                    throw new ValueReaderException(e);
                }
            }), sqlParamTypeMapping.getPropertyDefined(), ways[index], ways[index]));
            if (index != 0)
                linkedNode.setNext(next);
            findSqlParamsWaysByNameKey(next, ways, index + 1, sqlParamTypeMapping, null, false);
        }
    }

    /**
     * 获取值值读取器的值
     *
     * @param ways
     * @param index
     * @param configuration
     * @return ValueReader.Value
     */
    private ValueReader.Value getInnerValue(String[] ways, int index, ValueReader.Configuration configuration) {
        Node<ValueReader> childLinkedNode = new Node<>(null, null, null);
        ParamTypeMapping childParamTypeMapping = new ParamTypeMapping();
        findSqlParamsWaysByNameKey(childLinkedNode, ways, index + 1, childParamTypeMapping, configuration.getBean().getClass(), false);
        Object tempBean = configuration.autoRead();
        childParamTypeMapping = childParamTypeMapping.getChild("0");
        if (childParamTypeMapping != null) {
            MetaProperty<ParamTypeMapping> metaProperty = new MetaProperty<>(childParamTypeMapping, tempBean, null);
            MetaProperty.Result<ParamTypeMapping, ValueReader> result = metaProperty.searchByWay(childLinkedNode);
            return result.getNode().getValue().read(tempBean);
        } else {
            try {
                return new ValueReader.Value(configuration.autoRead(), true, false);
            } catch (Throwable e) {
                throw new ValueReaderException(e);
            }
        }
    }

    /**
     * 获取参数类型映射
     *
     * @param classMap
     * @param batch
     * @return ParamTypeMapping
     */
    private ParamTypeMapping getParamTypeMapping(Map<String, ClassInfo> classMap, boolean batch) {
        ParamTypeMapping rootParamTypeMapping = new ParamTypeMapping();
        Map<Object, ParamTypeMapping> innerMap = new LinkedHashMap<>();
        rootParamTypeMapping.setInnerTypeMappings(innerMap);
        rootParamTypeMapping.setPropertyDefined(ArrayProperty.INSTANCE);
        rootParamTypeMapping.setBatchParam(batch);
        rootParamTypeMapping.setType(Object[].class);
        for (String key : classMap.keySet()) {
            ClassInfo classInfo = classMap.get(key);
            if (classInfo == null)
                throw new TemplateException("The template has a error expression.The key " + key + " isn't matching argument.");
            ParamTypeMapping child = getParamTypeMapping(classInfo, batch);
            child.setParentTypeMapping(rootParamTypeMapping);
            child.setArgumentIndex(classInfo.index);
            String indexStr = String.valueOf(classInfo.index);
            innerMap.put(indexStr, child);
            if (batch)
                rootParamTypeMapping.setBatchProperty(indexStr);
        }
        return rootParamTypeMapping;
    }

    /**
     * 获取参数类型映射
     *
     * @param paramType
     * @param batch
     * @return ParamTypeMapping
     */
    private ParamTypeMapping getParamTypeMapping(ClassInfo paramType, boolean batch) {
        if (TypeUtils.isBasic(paramType.originalClass)) {
            ParamTypeMapping child = new ParamTypeMapping();
            child.setType(paramType.originalClass);
            TypeConvertor<?> typeConvertor = paramType.typeConvertor;
            if (typeConvertor == null)
                typeConvertor = TypeConvertorManager.getConvertor(paramType.originalClass);
            child.setTypeConvertor(typeConvertor);
            return child;
        }
        ParamTypeMapping child = new ParamTypeMapping();
        child.setType(paramType.originalClass);
        setPropertyDefined(child);
        if (child.isComplexType()) {
            Map<Object, ParamTypeMapping> innerMap = new LinkedHashMap<>();
            child.setInnerTypeMappings(innerMap);
            for (Field field : ReflectionUtils.getAllFields(paramType.originalClass)) {
                ParamTypeMapping inner = getParamTypeMapping(child, field.getType());
                if (inner != null)
                    innerMap.put(field.getName(), inner);
            }
        } else if (batch) {
            Map<Object, ParamTypeMapping> innerMap = new LinkedHashMap<>();
            child.setInnerTypeMappings(innerMap);
            ParamTypeMapping inner = getParamTypeMapping(child, paramType.beanClass);
            if (inner != null)
                innerMap.put("0", inner);
            child.setBatchProperty("0");
        }
        return child;
    }

    /**
     * 获取参数类型映射
     *
     * @param paramType
     * @return ParamTypeMapping
     */
    private ParamTypeMapping getParamTypeMapping(Class<?> paramType) {
        return getParamTypeMapping(new ClassInfo(paramType, null), false);
    }

    /**
     * 设置属性定义
     *
     * @param paramTypeMapping
     */
    private void setPropertyDefined(ParamTypeMapping paramTypeMapping) {
        Class<?> type = paramTypeMapping.getType();
        if (TypeUtils.isArray(type))
            paramTypeMapping.setPropertyDefined(ArrayProperty.INSTANCE);
        else if (TypeUtils.isCollection(type)) {
            if (TypeUtils.isMap(type))
                paramTypeMapping.setPropertyDefined(MapProperty.INSTANCE);
            else if (TypeUtils.isList(type))
                paramTypeMapping.setPropertyDefined(ListProperty.INSTANCE);
            else if (TypeUtils.isSet(type))
                paramTypeMapping.setPropertyDefined(SetProperty.INSTANCE);
            else
                paramTypeMapping.setPropertyDefined(SetProperty.INSTANCE);
        } else {
            paramTypeMapping.setComplexType(true);
            paramTypeMapping.setPropertyDefined(new BeanProperty(ReflectionUtils.getMetaFieldsMap(type)));
        }
    }

    /**
     * 获取参数类型映射
     *
     * @param paramTypeMapping
     * @param clazz
     * @return ParamTypeMapping
     */
    private ParamTypeMapping getParamTypeMapping(ParamTypeMapping paramTypeMapping, Class<?> clazz) {
        if (TypeUtils.isBasic(clazz))
            return null;
        ParamTypeMapping child = new ParamTypeMapping();
        child.setParentTypeMapping(paramTypeMapping);
        child.setType(clazz);
        setPropertyDefined(child);
        if (child.isComplexType()) {
            Map<Object, ParamTypeMapping> innerMap = new LinkedHashMap<>();
            child.setInnerTypeMappings(innerMap);
            for (Field field : ReflectionUtils.getAllFields(clazz)) {
                innerMap.put(field.getName(), getParamTypeMapping(clazz));
            }
        }
        return child;
    }

    /**
     * 匹配字符串
     *
     * @param matcher
     * @return String
     */
    private String isMatch(Matcher matcher) {
        if (matcher.find())
            return matcher.group();
        return null;
    }

    /**
     * 键索引
     */
    static class KeyIndex {
        String key;
        String regex;
        boolean indexType;

        public KeyIndex(String key, String regex, boolean indexType) {
            this.key = key;
            this.regex = regex;
            this.indexType = indexType;
        }
    }

    /**
     * 存储类信息
     */
    static class ClassInfo {
        Class<?> originalClass;
        Class<?> beanClass;
        TypeConvertor<?> typeConvertor;
        String key;
        int index;

        public ClassInfo(Class<?> originalClass, Class<?> beanClass) {
            this.originalClass = originalClass;
            this.beanClass = beanClass;
        }

        public ClassInfo(Class<?> originalClass, Class<?> beanClass, TypeConvertor<?> typeConvertor, String key, int index) {
            this.originalClass = originalClass;
            this.beanClass = beanClass;
            this.typeConvertor = typeConvertor;
            this.key = key;
            this.index = index;
        }
    }
}
