package com.mountain.orm.mybatis.plus.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

/**
 * mybatis sql 日志拦截器, 用于打印SQL相关信息（例如：实际执行SQL语句，SQL执行时间，查询记录数等）
 * <p>
 * 关于 @Intercepts注解说明：
 * 为了让拦截器能够精确地拦截特定的方法，需要使用 @Intercepts 注解来声明拦截的方法和参数类型
 * 该注解包含一个参数，即一个 @Signature 类型的数组，用于指定要拦截的方法。每个 @Signature 注解表示一个要拦截的方法签名，其中包括以下属性：
 * <p>
 * type：被拦截的目标类或接口。在这里，StatementHandler.class 表示拦截 MyBatis 中的 StatementHandler 类。
 * method：被拦截的方法名。可以通过字符串指定方法名或使用方法引用。
 * args：被拦截方法的参数类型数组。用于指定被拦截方法的参数类型及顺序。
 * 在以下代码中的注解示例中，拦截器指定了对 StatementHandler 类中的三个方法进行拦截，分别是：
 * <p>
 * query(Statement.class, ResultHandler.class)：拦截 StatementHandler 类中的 query 方法，该方法有两个参数，分别是 Statement 和 ResultHandler。
 * update(Statement.class)：拦截 StatementHandler 类中的 update 方法，该方法有一个参数，即 Statement。
 * batch(Statement.class)：拦截 StatementHandler 类中的 batch 方法，该方法也有一个参数，即 Statement。
 * 通过使用 @Intercepts 注解和 @Signature 注解，可以精确地指定要拦截的方法和参数类型，从而实现对特定方法的拦截和处理。
 *
 * @author zt
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "spring.profiles", name = "active", havingValue = "local", matchIfMissing = false)
@Intercepts({@Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class}), @Signature(type = StatementHandler.class, method = "update", args = {Statement.class}), @Signature(type = StatementHandler.class, method = "batch", args = {Statement.class})})
public class MybatisLogSqlInterceptor implements Interceptor {

    /**
     * 定义一个包含需要添加单引号括起来的参数类型集合。
     */
    private static final Set<String> NEED_BRACKETS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("String", "Date", "Time", "LocalDate", "LocalTime", "LocalDateTime", "BigDecimal", "Timestamp")));

    /**
     * MyBatis的配置对象。
     */
    private Configuration configuration = null;

    /**
     * 拦截器的核心方法，用于拦截并处理SQL语句执行前后的逻辑。
     *
     * @param invocation Invocation 类是 MyBatis 框架提供的一个接口，定义了用于描述方法执行的信息和操作的方法。
     *                   在方法拦截时，MyBatis 将被拦截的方法封装成 Invocation 对象，并作为参数传递给拦截器。
     *                   <p>
     *                   Invocation 接口中定义了以下几个重要的方法：
     *                   <p>
     *                   Object getTarget()：获取目标对象，即被拦截的对象。
     *                   Method getMethod()：获取被拦截的方法对象。
     *                   Object[] getArgs()：获取被拦截方法的参数列表。
     *                   Object proceed() throws Throwable：继续执行被拦截的方法。
     *                   Object proceed(Object[] args) throws Throwable：继续执行被拦截的方法，并使用指定的参数列表。
     *                   Object getThis()：获取代理对象，即拦截器生成的代理对象。
     *                   通过 Invocation 对象，我们可以获取被拦截方法的相关信息，如目标对象、方法名称、参数列表等。拦截器可以根据这些信息对方法进行额外的处理，比如记录日志、性能监控、权限验证等。最后，通过调用 proceed() 方法，可以继续执行被拦截的方法。
     *                   <p>
     *                   在代码中，Invocation invocation 参数被用于执行被拦截方法，并在方法执行前后进行一些额外的操作。
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object target = invocation.getTarget();
        long startTime = System.currentTimeMillis();
        // 初始化行数为 1
        int lines = 1;
        // 默认状态为 "失败"
        String status = "failed";
        try {
            // 执行原始方法，并获取返回结果
            Object proceed = invocation.proceed();
            // 如果返回结果为集合，则统计行数
            if (proceed instanceof Collection<?>) {
                lines = ((List<?>) proceed).size();
            }
            // 执行成功，将状态设置为 "成功"
            status = "success";
            return proceed;
        } finally {
            // 计算 SQL 执行耗时
            long sqlCost = System.currentTimeMillis() - startTime;
            // 获取 SQL 语句
            String sql = this.getSql(target);
            // 打印日志
            log.info("\u001B[38;5;220mSQL 执行结果：{}. SQL 信息：{}\u001B[0m", status, sql);
            log.info("\u001B[38;5;220m耗时：{} mm. Total：{}\u001B[0m", sqlCost, lines);
        }
    }

    /**
     * 获取 SQL 语句
     *
     * @param target 获取目标对象，即被拦截的对象。
     * @return 实际执行SQL语句
     */
    private String getSql(Object target) {
        try {
            // 获取 StatementHandler 对象
            StatementHandler statementHandler = (StatementHandler) target;
            // 获取 BoundSql 对象
            BoundSql boundSql = statementHandler.getBoundSql();
            if (configuration == null) {
                // 通过反射获取 Configuration 对象
                final ParameterHandler parameterHandler = statementHandler.getParameterHandler();
                this.configuration = (Configuration) FieldUtils.readField(parameterHandler, "configuration", true);
            }
            // 格式化 SQL 语句并返回
            return formatSql(boundSql, configuration);
        } catch (Exception e) {
            // 异常处理，打印警告日志
            log.warn("获取 SQL 语句失败：{}", target, e);
            return "无法解析的 SQL 语句";
        }
    }

    /**
     * 格式化 SQL 语句
     *
     * @param boundSql      绑定的 SQL 对象，包含 SQL 语句和参数信息
     * @param configuration MyBatis 的配置信息对象，用于获取配置信息
     * @return 格式化后的 SQL 字符串
     */
    private String formatSql(BoundSql boundSql, Configuration configuration) {
        // 获取原始 SQL 语句
        String sql = boundSql.getSql();
        // 获取参数映射列表
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        // 获取参数对象
        Object parameterObject = boundSql.getParameterObject();
        // 判断是否为空
        if (StringUtils.isEmpty(sql) || Objects.isNull(configuration)) {
            return "";
        }

        // 获取 TypeHandlerRegistry 对象
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();

        // 移除 SQL 字符串中的空格、换行符等
        sql = sql.replaceAll("[\n\r ]+", " ");

        // 过滤掉输出参数的参数映射
        if (parameterMappings == null) {
            return sql;
        }
        parameterMappings = parameterMappings.stream().filter(it -> it.getMode() != ParameterMode.OUT).collect(Collectors.toList());

        // 使用 StringBuilder 保存格式化后的 SQL
        final StringBuilder result = new StringBuilder(sql);

        // 解析问号并替换参数
        for (int i = result.length(); i > 0; i--) {
            if (result.charAt(i - 1) != '?') {
                continue;
            }
            ParameterMapping parameterMapping = parameterMappings.get(parameterMappings.size() - 1);
            Object value;
            String propertyName = parameterMapping.getProperty();
            // 判断绑定的附加参数中是否有对应的属性名
            if (boundSql.hasAdditionalParameter(propertyName)) {
                value = boundSql.getAdditionalParameter(propertyName);
            } else if (parameterObject == null) {
                value = null;
            } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                value = parameterObject;
            } else {
                // 使用 MetaObject 获取属性值
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                value = metaObject.getValue(propertyName);
            }
            if (value != null) {
                // 判断参数类型，如果是需要添加括号的类型，则添加单引号
                String type = value.getClass().getSimpleName();
                if (NEED_BRACKETS.contains(type)) {
                    result.replace(i - 1, i, "'" + value + "'");
                } else {
                    result.replace(i - 1, i, value.toString());
                }
            } else {
                // 参数值为空时，替换为 "null"
                result.replace(i - 1, i, "null");
            }
            // 移除已处理的参数映射
            parameterMappings.remove(parameterMappings.size() - 1);
        }
        return result.toString();
    }
}