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
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

/**
 * @author Maxwell Nie
 * @author yurongqi
 * 将ResultSet转换为Java实体对象
 */
public class ResultSetCOnvertorByQise implements ResultSetConvertor{
    ArrayList<TypeMapping> lastmetaList = new ArrayList<>();
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
     *
     * 通过调用singleSetValue方法将数据设置到对象中，返回单一对象，然后通过setRowleValue方法进行递归处理将一行数据进行
     * 赋值到对象中，直到所有数据处理完成。最后通过遍历resultSet将所有的赋值给parent中，最后返回parent。
     *
     * @param resultSet 数据库查询结果集，包含从数据库中查询到的数据
     * @param typeMapping 返回类型映射，用于指导如何将数据库数据映射到业务对象
     * @return 返回转换后的对象，如果转换过程中遇到错误或者无法转换，则返回null或者空对象
     */
    @lombok.SneakyThrows
    public Object simpleConvert(ResultSet resultSet, TypeMapping typeMapping){
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        List<RecursiveTask<Object>> tasks = new ArrayList<>();
        Object parent = null;
        MetaProperty metaProperty = null;
        Reflection<?> reflection = ReflectionUtils.getReflection(typeMapping.getType());
        if (resultSet.isClosed())
            throw new ConvertException("resultSet is closed.");
        while (resultSet.next()) {
            int columnIndex = 0;
            ObjectFactory<?> objectFactory = reflection.getObjectFactory();
            Object rowData = setRowValue(resultSet, typeMapping, objectFactory, columnIndex);
            parent = metaProperty.getProperty().setValue(parent, null, rowData);
/*            RecursiveTask<Object> task = new RecursiveTask<Object>() {
                @Override
                protected Object compute() {
                    int columInde = 0;
                    return setRowValue(resultSet, typeMapping, objectFactory, columInde);
                }
            };
            tasks.add(task);
            forkJoinPool.execute(task);*/
            columnIndex++;
        }
/*        List<Object> results = tasks.stream()
                .map(RecursiveTask::join)
                .collect(Collectors.toList());
            return  results;*/
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
     * @param columnIndex 结果集中的列索引，指示当前处理的列
     * @return Object 返回转换后的对象实例
     */
    public Object setRowValue(ResultSet resultSet,TypeMapping typeMapping,ObjectFactory<?> objectFactory,int columnIndex){
        Object data = null;
        ArrayList<TypeMapping> premetaList = new ArrayList<>();
        for (Map.Entry<String, TypeMapping> child : typeMapping.getChildren().entrySet()){
            premetaList.add(child.getValue());
        }
        for (TypeMapping index : premetaList){
            if(!index.isEntity()){//跳出递归
                data = setSingleValue(resultSet, index, objectFactory, columnIndex);
            }else{
                setRowValue(resultSet,index,objectFactory,columnIndex);//实体类递归
            }
        }
        return data;
    }

/*
    @lombok.SneakyThrows
    public Object simpleConvert(ResultSet resultSet, TypeMapping typeMapping) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        List<RecursiveTask<Object>> tasks = new ArrayList<>();
        Reflection<?> reflection = ReflectionUtils.getReflection(typeMapping.getType());
        if (resultSet.isClosed())
            throw new ConvertException("resultSet is closed.");

        // 单线程读取 ResultSet 的每一行数据
        while (resultSet.next()) {
            int columnIndex = 0;
            ObjectFactory<?> objectFactory = reflection.getObjectFactory();
            Object rowData = setRowValue(resultSet, typeMapping, objectFactory, columnIndex);
            columnIndex++;

            // 将每一行数据传递给 ForkJoinPool 进行并行处理
            RecursiveTask<Object> task = new RecursiveTask<Object>() {
                @Override
                protected Object compute() {
                    // 这里可以进行更复杂的对象转换逻辑
                    return rowData;
                }
            };
            tasks.add(task);
            forkJoinPool.execute(task);
        }

        // 等待所有任务完成并收集结果
        List<Object> results = tasks.stream()
                .map(RecursiveTask::join)
                .collect(Collectors.toList());

        // 根据具体需求返回结果，这里假设返回一个包含所有结果的列表
        return results;
    }
    @lombok.SneakyThrows
    public Object simpleConvert(ResultSet resultSet, TypeMapping typeMapping) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        List<RecursiveTask<Object>> tasks = new ArrayList<>();
        Reflection<?> reflection = ReflectionUtils.getReflection(typeMapping.getType());
        if (resultSet.isClosed())
            throw new ConvertException("resultSet is closed.");

        // 分页处理，假设每页 100 条记录
        int pageSize = 100;
        List<Object> allResults = new ArrayList<>();

        while (resultSet.next()) {
            List<Object> pageResults = new ArrayList<>();
            for (int i = 0; i < pageSize && resultSet.next(); i++) {
                int columnIndex = 0;
                ObjectFactory<?> objectFactory = reflection.getObjectFactory();
                Object rowData = setRowValue(resultSet, typeMapping, objectFactory, columnIndex);
                columnIndex++;
                pageResults.add(rowData);
            }

            // 将每一页的数据传递给 ForkJoinPool 进行并行处理
            RecursiveTask<Object> task = new RecursiveTask<Object>() {
                @Override
                protected Object compute() {
                    // 这里可以进行更复杂的对象转换逻辑
                    return pageResults;
                }
            };
            tasks.add(task);
            forkJoinPool.execute(task);
        }

        // 等待所有任务完成并收集结果
        List<Object> results = tasks.stream()
                .map(RecursiveTask::join)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        // 根据具体需求返回结果，这里假设返回一个包含所有结果的列表
        return results;
    }*/
/*@lombok.SneakyThrows
public Object simpleConvert(ResultSet resultSet, TypeMapping typeMapping) {
    ForkJoinPool forkJoinPool = new ForkJoinPool();
    List<RecursiveTask<Object>> tasks = new ArrayList<>();
    Reflection<?> reflection = ReflectionUtils.getReflection(typeMapping.getType());
    if (resultSet.isClosed())
        throw new ConvertException("resultSet is closed.");

    // 单线程读取 ResultSet 的每一行数据
    List<Object> allRowData = new ArrayList<>();
    while (resultSet.next()) {
        int columnIndex = 0;
        ObjectFactory<?> objectFactory = reflection.getObjectFactory();
        Object rowData = setRowValue(resultSet, typeMapping, objectFactory, columnIndex);
        columnIndex++;
        allRowData.add(rowData);
    }

    // 将每一行数据传递给 ForkJoinPool 进行并行处理
    for (Object rowData : allRowData) {
        RecursiveTask<Object> task = new RecursiveTask<Object>() {
            @Override
            protected Object compute() {
                // 这里可以进行更复杂的对象转换逻辑
                return rowData;
            }
        };
        tasks.add(task);
        forkJoinPool.execute(task);
    }

    // 等待所有任务完成并收集结果
    List<Object> results = tasks.stream()
            .map(RecursiveTask::join)
            .collect(Collectors.toList());

    // 根据具体需求返回结果，这里假设返回一个包含所有结果的列表
    return results;
}*/
}
