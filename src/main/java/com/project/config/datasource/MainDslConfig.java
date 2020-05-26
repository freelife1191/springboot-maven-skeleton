package com.project.config.datasource;

import com.zaxxer.hikari.HikariDataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.RenderQuotedNames;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * MariaDB 설정 샘플
 * Created by KMS on 14/04/2020.
 */
// @Configuration
public class MainDslConfig {

    /*
    public static final String DATASOURCE = "mainDataSource";

    @Primary
    @Bean(DATASOURCE)
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public DataSource dataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Primary
    @Bean("txManager")
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Primary
    @Bean("mainDsl")
    @DependsOn(DATASOURCE)
    DSLContext dslContext() throws Exception {
        DefaultConfiguration jooqConfiguration = new DefaultConfiguration();
        jooqConfiguration.settings()
                .withRenderQuotedNames(RenderQuotedNames.EXPLICIT_DEFAULT_UNQUOTED);
        jooqConfiguration.setDataSource(dataSource());
        jooqConfiguration.setSQLDialect(SQLDialect.MARIADB);
        // .withRenderNameCase(RenderNameCase.LOWER_IF_UNQUOTED);               // Defaults to AS_IS
        jooqConfiguration.setExecuteListenerProvider( new DefaultExecuteListenerProvider(new JOOQToSpringExceptionTransformer()) );
        return new DefaultDSLContext(jooqConfiguration);
    }
    */

}
