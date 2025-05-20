package io.github.maxwellnie.javormio.spring.boot;

import io.github.maxwellnie.javormio.common.java.jdbc.datasource.DynamicDataSource;
import io.github.maxwellnie.javormio.common.java.jdbc.datasource.SpringDynamicDataSource;
import io.github.maxwellnie.javormio.core.Context;
import io.github.maxwellnie.javormio.core.ContextBuilder;
import io.github.maxwellnie.javormio.core.SpringContextBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author Maxwell Nie
 */
@Configuration
@ConditionalOnClass({DataSource.class, DynamicDataSource.class, Context.class, ContextBuilder.class})
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@ConfigurationProperties(prefix = "javormio")
public class JavormioAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public DynamicDataSource dynamicDataSource(DataSource dataSource) {
        DynamicDataSource dynamicDataSource = new SpringDynamicDataSource("default");
        dynamicDataSource.register("default", dataSource);
        return dynamicDataSource;
    }
    @Bean
    @ConditionalOnMissingBean
    public ContextBuilder contextBuilder(DynamicDataSource dynamicDataSource) {
        return new SpringContextBuilder(dynamicDataSource);
    }
    @Bean
    @ConditionalOnMissingBean
    public Context sqlContext(ContextBuilder contextBuilder) {
        return contextBuilder.build();
    }
}
