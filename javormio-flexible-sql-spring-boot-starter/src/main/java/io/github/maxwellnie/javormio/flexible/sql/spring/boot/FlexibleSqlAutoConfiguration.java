package io.github.maxwellnie.javormio.flexible.sql.spring.boot;

import io.github.maxwellnie.javormio.core.Context;
import io.github.maxwellnie.javormio.flexible.sql.plugin.FlexibleSqlContext;
import io.github.maxwellnie.javormio.flexible.sql.spring.QueryKit;
import io.github.maxwellnie.javormio.spring.boot.JavormioAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import javax.sql.DataSource;

/**
 * @author Maxwell Nie
 */
@Configuration
@ConditionalOnClass({DataSource.class, Context.class, FlexibleSqlContext.class})
@AutoConfigureAfter(JavormioAutoConfiguration.class)
public class FlexibleSqlAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public FlexibleSqlContext flexibleSqlContext(Context context){
        return new FlexibleSqlContext(context);
    }
    @Bean
    @ConditionalOnMissingBean
    public QueryKit queryKit(FlexibleSqlContext flexibleSqlContext){
        QueryKit queryKit = new QueryKit();
        queryKit.setFlexibleSqlContext(flexibleSqlContext);
        return queryKit;
    }
}
