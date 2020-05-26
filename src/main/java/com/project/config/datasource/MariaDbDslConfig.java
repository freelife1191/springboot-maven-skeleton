package com.project.config.datasource;

import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.RenderQuotedNames;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.mariadb.jdbc.MariaDbDataSource;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.sql.DataSource;
import java.sql.SQLException;
/**
 * Created by KMS on 13/04/2020.
 */
// @Slf4j
// @Configuration
// @DependsOn("multiTxManager")
// @EnableConfigurationProperties(MariaDbDataSourceProperties.class)
public class MariaDbDslConfig {

    /*
    private final MariaDbDataSourceProperties properties;

    public MariaDbDslConfig(MariaDbDataSourceProperties properties) {
        this.properties = properties;
    }

    public static final String DATASOURCE = "mariaDbDataSource";

    @Primary
    @Bean(DATASOURCE)
    public DataSource dataSource() throws SQLException {
        MariaDbDataSource mariaDbDataSource = new MariaDbDataSource();
        mariaDbDataSource.setUrl(properties.getUrl());
        mariaDbDataSource.setUser(properties.getUsername());
        mariaDbDataSource.setPassword(properties.getPassword());

        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(mariaDbDataSource);
        xaDataSource.setUniqueResourceName(DATASOURCE);
        xaDataSource.setXaDataSourceClassName(properties.getXaDataSourceClassName());
        return xaDataSource;
    }

    @Primary
    @Bean("mariaDbDsl")
    @DependsOn(DATASOURCE)
    DSLContext MariaDbDsl() throws Exception {
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
