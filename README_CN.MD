# 介绍

Javormio是一个致力于简化SQL构建并提高良好扩展性的ORM框架

## 特性

- 数据库适配性：Javormio对于SQL语句处理的扩展点面面俱到，允许用户修改SQL构建逻辑以适应不同的数据库
- 易扩展：Javormio各个模块依赖性低，模块间耦合度低，便于扩展
- 支持代码处理：Javormio提供了基于Java Annotation Processor的代码处理扩展，用户可以基于注解来处理源代码，以实现对SQL语句的定制化构建
- 轻量：Javormio的体积小，依赖少，便于集成到项目中
- 高性能：应用FlexibleSql扩展后，Javormio的对象构建将不依赖于反射，与直接应用JDBC操作数据库的性能相近
- 灵活SQL：Javormio支持FlexibleSql扩展，用户可以使用lambda与链式调用来构建SQL和映射结果集到Java对象
- 事务支持：Javormio支持在对Table数据查询、修改、删除时的事务控制
- 缓存：Javormio支持缓存，用户可以基于缓存来提高查询性能（需要扩展支持）
- Mapper：Javormio支持Mapper，用户可以使用Mapper对象来操作数据库（需要扩展支持）

### 数据库适配性

目前，Javormio官方数据库方言扩展仅支持MySQL数据库，后续将支持更多数据库

### 易扩展

- Javormio各个组件的获取均依赖于Context，通过重写Context相关方法可对各个组件进行增强
  例如，我们可以通过重写getSqlExecutor，实现执行SQL前打印SQL语句
- SqlExecutorExtension.java

```java
public class SqlExecutorExtension implements SqlExecutor {
    protected SqlExecutor sqlExecutor;
    protected static final Logger logger = LoggerFactory.getLogger(SqlExecutorExtension.class);

    public SqlExecutorExtension(SqlExecutor sqlExecutor) {
        this.sqlExecutor = sqlExecutor;
    }

    @Override
    public <T> StatementWrapper run(ExecutorContext<T> executorParameters) throws ConvertException {
        ExecutableSql sql = executorParameters.getExecutableSql();
        logger.info("SQL: " + sql.getSql(), "PARAMS:" + sql.getParameters());
        return sqlExecutor.run(executorParameters);
    }
}
```

- ExtensionContext.java

```java
public class ExtensionContext extends Context {
    @Override
    public SqlExecutor getSqlExecutor() {
        return new SqlExecutorExtension(super.getSqlExecutor());
    }
}
```

- 结果

```text
[INFO] SQL: SELECT * FROM user WHERE id = ? PARAMS:[[1, null]]
```

### 支持代码处理

需要启用javormio-source-code-processor扩展

#### 介绍

javormio-source-code-processor扩展是Javormio框架的一个扩展插件，基于SPI机制与Java Annotation Processor功能在编译期处理源代码

#### 架构

```text
|----CoreProcessor--(核心处理器，基于SPI机制，按照需要处理的注解来分发任务到专属处理器)<--
|                                                                              |
|                                                                              |扩展               
|                                                                              |
|----ExtensionProcessor--(专属处理器，能够处理核心处理器找到的带有具体注解的元素)-------


@SPIPlugin--扩展处理器注解，用于标识专属处理器可用的注解和最低支持的Java版本


CoreProcessor.properties---(核心配置文件，用于配置专属处理器的实现类、Java Annotation Processor的其他支持选项、支持的最低Java版本)
```

#### 快速开始

1. 添加依赖

```xml

<dependency>
    <groupId>com.javormio</groupId>
    <artifactId>javormio-processor</artifactId>
    <version>1.0.0.SNAPSHOT</version>
</dependency>
```

2. 添加注解

```java
public @interface Annotation1 {
    String value();
}
```

3. 添加处理器

##### 这里的value()为专属处理器所处理的注解，sourceVersion为最低支持的Java版本

```java
import io.github.maxwellnie.javormio.source.code.processor.CustomProcessor;
import io.github.maxwellnie.javormio.source.code.processor.ExtensionProcessor;
import io.github.maxwellnie.javormio.source.code.processor.SPIPlugin;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;
import java.util.Set;


@SPIPlugin(value = Annotation1.class, sourceVersion = SourceVersion.RELEASE_8)
public class Annotation1Processor implements CustomProcessor {
    @Override
    public void handle(Set<? extends Element> element, ProcessingEnvironment processingEnv, RoundEnvironment roundEnv) {
        for (Element element : elements) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE,
                    "@Annotation1: " + element.getSimpleName() + ", element);
        }
    }
}
```

4. 添加配置文件CoreProcessor.properties

```java
extension-plugin-classes=Annotation1Processor
```

##### 当然，我们也可以添加多个专属处理器

```java
extension-plugin-classes=Annotation1Processor,Annotation2Processor
```

6. 运行

```shell
mvn clean compile
```

7. 运行结果

```shell
[INFO] --- javormio-processor:1.0.0.SNAPSHOT:process (default) @ javormio-processor-demo ---
[INFO] @Annotation1: Annotation1Processor
```

### 轻量性

核心部分仅依赖sl4j与JDBCDriver，依赖少，体积小

### 性能

### 基

### Mapper

### Flexible SQL

由于结果集映射不依赖反射而是基于lambda和表元对象，性能与直接使用JDBC操作数据库性能相近，以下为jmeter压测结果：

- 小数据量大量请求(服务器常态)

| 标签              | 样本      | 单位请求大小  | 平均值 | 最小值 | 最大值 | 成功率 | 吞吐量 | 总耗时 | 内存占用 |
|-----------------|---------|---------|-----|-----|-----|-----|-----|-----|------|
| Flexible SQL    | 1*10000 | 21bytes |     |     |     |     |     |     |      |
| Mybatis-Plus    | 1*10000 | 21bytes |     |     |     |     |     |     |      |
| Mybatis         | 1*10000 | 21bytes |     |     |     |     |     |     |      |
| Hibernate       | 1*10000 | 21bytes |     |     |     |     |     |     |      |
| Javromio-Mapper | 1*10000 | 21bytes |     |     |     |     |     |     |      |

- 大数据量单个请求(服务器低负载)

| 标签              | 样本   | 单位请求大小 | 平均值 | 最小值 | 最大值 | 成功率 | 吞吐量 | 总耗时 | 内存占用 |
|-----------------|------|--------|-----|-----|-----|-----|-----|-----|------|
| Flexible SQL    | 10*1 | 15MB   |     |     |     |     |     |     |      |
| Mybatis-Plus    | 10*1 | 15MB   |     |     |     |     |     |     |      |
| Mybatis         | 10*1 | 15MB   |     |     |     |     |     |     |      |
| Hibernate       | 10*1 | 15MB   |     |     |     |     |     |     |      |
| Javromio-Mapper | 10*1 | 15MB   |     |     |     |     |     |     |      |

- 大数据量大量请求(服务器超高负载)

| 标签              | 样本量     | 单位请求大小 | 平均值 | 最小值 | 最大值 | 成功率 | 吞吐量 | 总耗时 | 内存占用 |
|-----------------|---------|--------|-----|-----|-----|-----|-----|-----|------|
| Flexible SQL    | 1*10000 | 15MB   |     |     |     |     |     |     |      |
| Mybatis-Plus    | 1*10000 | 15MB   |     |     |     |     |     |     |      |
| Mybatis         | 1*10000 | 15MB   |     |     |     |     |     |     |      |
| Hibernate       | 1*10000 | 15MB   |     |     |     |     |     |     |      |
| Javromio-Mapper | 1*10000 | 15MB   |     |     |     |     |     |     |      |

### 灵活SQL

需要启用javormio-flexible-sql扩展

#### 介绍

javormio-flexible-sql扩展是Javormio框架的一个扩展插件，基于Java Annotation
Processor功能在编译期处理源代码，构建数据库表对应的元数据，用于构建SQL和映射结果集到Java对象，并且支持链式调用和lambda来构建SQL，使SQL构建与结果集映射变得更简单

#### 架构

```text
|---------表元对象生成器(MetaTableHandler)-----使用Java Annotation Processor和Velocity ----------
|                                                                                              |生成
|                                                                                              |
|---------表元对象(BaseMetaTableInfo)---------携带表的各信息与列信息---------------------------- <-
|                                                                                          
|                                                                                          
|---------列信息(ExpressionColumnInfo)--------携带列信息并且可以使用ExpressionApi生成SQL表达式，且getter、setter为显式对象调用     
|               
|               
|---------数据库操作对象(Operator)-----------QueryBuilder、UpdateBuilder、DeleteBuilder对象，使用表元对象来构建数据库操作对象
|
|
|---------数据库操作对象(Query、Update、Delete)----------------可用于操作数据库、获取结果集
|
|                                                                                          
|---------SQL表达式对象(Expression)-----------------各种表达式的封装，使用表元对象来构建SQL表达式 
|
|
|---------SQL表达式构建器(SqlExpressionBuilder)-------SQL表达式构建器，使用列信息对象来构建SQL表达式
|
|
|---------SQL构建器(SqlBuilder)-----------------拼接SQL语句，使其成为可转化为可执行的SQL对象
|
|
|---------执行结果对象(ExecuteResult)---------执行结果对象，用于封装执行结果，使用ObjectMap来映射结果集、Collector把转换的对象收集起来
| 
|
|---------结果集取值工具(Tool)----------------用于辅助获取结果集的列数据                                                                       
```

#### 快速开始

1. 添加依赖

```xml

<dependency>
    <groupId>com.javormio</groupId>
    <artifactId>javormio-source-code-processor</artifactId>
    <version>1.0.0.SNAPSHOT</version>
</dependency>
<dependency>
<groupId>com.javormio</groupId>
<artifactId>javormio-flexible-sql</artifactId>
<version>1.0.0.SNAPSHOT</version>
</dependency>
```

2. 添加CoreProcessor.properties

```text
extension-plugin-classes=MetaTableHandler
```

3. 添加表对象

```java

@Table(name = "user")
public class User {
    @Column(name = "id")
    public int id;
    @Column(name = "name")
    public String name;
    @Column(name = "age")
    public int age;
}
```

4. 生成代码

```shell
mvn clean compile
```

5. 使用

```java
public class Test {
    private FlexibleContext context;
    private MetaUser tableUser;

    public Test(FlexibleContext context, MetaUser tableUser) {
        this.context = context;
        this.tableUser = tableUser;
    }

    public void test() {
        Set<User> users = QueryBuilder.from(tableUser, "a", context)
                .aliasColumns((a) ->
                        a.alias(tableUser.id, "id_alias")
                                .alias(tableUser.name, "name_alias")
                )
                .excludeColumns(tableUser.id)
                .toSql()
                .selectToResultSet()
                .mapTo(User::new, (executionResults, user) -> {
                    user.setName(executionResults.getColumnValue(tableUser.name));
                    return user;
                })
                .collect(Collectors.toSet());
        System.out.println(users);
    }
}
```

#### SQL构建

1. 列别名

```java
QueryBuilder.from(tableUser, "a",context)
        .

aliasColumns((a) ->
        a.

alias(tableUser.id, "id_alias")
                        .

alias(tableUser.name, "name_alias")
        );
```

2. 排除列(建议在join之后where之前进行)

```java
QueryBuilder.from(tableUser, "a",context).

excludeColumns(tableUser.id);
```

3. 表关联

```java
QueryBuilder.from(tableUser, "a",context)
        .

join(tableUser2, "b"，tableUser.roleId.eq(tableRole.id));
        QueryBuilder.

from(tableUser, "a",context)
        .

joinOn(tableUser2, "b"，tableUser.roleId.eq(tableRole.id));
```

4. where条件

```java
QueryBuilder.from(tableUser, "a",context)
        .

where(tableUser.id.eq(1))
        .

and(tableUser.name.eq("test"))
        .

or(tableUser.age.gt(18))
        .

and(tableUser.age.lt(20))
        .

or(tableUser.age.between(18, 20))
        .

and(tableUser.age.in(18, 19,20))
        .

or(tableUser.age.notIn(18, 19,20))
```

5. 获取SQL语句

```java
Query query = QueryBuilder.from(tableUser, "a", context).toSql();
ExecutableSql executableSql = query.getExecutableSql();
String rowSql = executableSql.getSqlList()[0];
```

#### 结果集映射与收集

1. 在生成Sql后(调用toSql()后)，可以指定查询的结果为ResultSet：

```java
ResultSetExecuteResult resultSetExecuteResult = QueryBuilder.from(tableUser, "a", context).toSql().selectToResultSet();
```

此时，返回的对象是ResultSet，我们可使用mapTo方法将ResultSet映射为对象

- mapTo方法，使用如下代码，可将ResultSet映射为User

```java
MapExecuteResult<User, Tool> userMapExecuteResult = QueryBuilder.from(tableUser, "a", context).toSql().selectToResultSet().mapTo(User::new, (executionResults, user) -> {
    user.setName(executionResults.getColumnValue(tableUser.name));
    return user;
});
```

- collect方法，对于映射好的对象，我们必须对其进行收集，可以使用Collector来指示这些对象的收集方式，比如：

```java
Set<User> users = QueryBuilder.from(tableUser, "a", context).toSql().selectToResultSet().mapTo(User::new, (executionResults, user) -> {
    user.setName(executionResults.getColumnValue(tableUser.name));
    return user;
}).collect(Collectors.toSet());
```

这样，我们就将获取的ResultSet映射为User对象，并收集为Set集合了。或者，我们可以使用collectToMap，将ResultSet映射为Map，并收集为Map

```java
List<Map<String, Obejct>> users = QueryBuilder.from(tableUser, "a", context).toSql().selectToResultSet().collectToMap();
```

2. 当然我们也可以使其返回from方法指定的表对象对应实体类对象

- 使用collectToEntity方法后直接收集User对象

```java
List<User> users = QueryBuilder.from(tableUser, "a", context).toSql().selectToEntity().collect(Collectors.toList());
```

- 使用collectToEntity方法后将User对象转换为UserVO对象

```java
List<UserVO> users = QueryBuilder.from(tableUser, "a", context).toSql().selectToEntity().mapTo(UserVO::new, (user, userVO) -> {
    userVO.setName(user.getName());
    userVO.setAge(user.getAge());
    return userVO;
}).collect(Collectors.toList());
```