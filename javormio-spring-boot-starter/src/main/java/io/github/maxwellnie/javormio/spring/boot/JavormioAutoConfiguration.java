package io.github.maxwellnie.javormio.spring.boot;

import io.github.maxwellnie.javormio.common.java.jdbc.datasource.DataBaseModel;
import io.github.maxwellnie.javormio.common.java.jdbc.datasource.DataBaseModelManager;
import io.github.maxwellnie.javormio.common.java.jdbc.datasource.SpringDataBaseModelManager;
import io.github.maxwellnie.javormio.common.java.sql.dialect.DefaultDialect;
import io.github.maxwellnie.javormio.common.java.sql.dialect.Dialect;
import io.github.maxwellnie.javormio.core.Context;
import io.github.maxwellnie.javormio.core.ContextBuilder;
import io.github.maxwellnie.javormio.core.SpringContextBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
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
@ConditionalOnClass({DataSource.class, DataBaseModelManager.class, Context.class, ContextBuilder.class})
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@ConfigurationProperties(prefix = "javormio")
public class JavormioAutoConfiguration {
    private final Log log = LogFactory.getLog(JavormioAutoConfiguration.class);
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean({DataSource.class, Dialect.class})
    public DataBaseModelManager dynamicDataSource(DataSource dataSource, Dialect dialect) {
        DataBaseModelManager dataBaseModelManager = new SpringDataBaseModelManager("default");
        dataBaseModelManager.register("default", new DataBaseModel(dataSource, dialect));
        return dataBaseModelManager;
    }
    @Bean
    @ConditionalOnMissingBean
    public DataBaseModelManager dynamicDataSource() {
        log.warn("Default datasource is not appointed. Javormio can be occurring exceptions.");
        return new SpringDataBaseModelManager("default");
    }
    @Bean
    @ConditionalOnMissingBean
    public Dialect dialect(){
        log.warn("Custom database dialect is not injected into Javormio. Check your source code to avoid program occurring exceptions.");
        return new DefaultDialect();
    }
    @Bean
    @ConditionalOnMissingBean
    public ContextBuilder contextBuilder(DataBaseModelManager dataBaseModelManager) {
        return new SpringContextBuilder(dataBaseModelManager);
    }
    @Bean
    @ConditionalOnMissingBean
    public Context sqlContext(ContextBuilder contextBuilder) {
        return contextBuilder.build();
    }
}
