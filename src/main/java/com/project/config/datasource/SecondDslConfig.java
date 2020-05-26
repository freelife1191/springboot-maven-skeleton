package com.project.config.datasource;

import com.project.config.properties.SecondDataSourceProperties;
import com.mysql.cj.jdbc.MysqlXADataSource;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.RenderQuotedNames;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * SubDB 설정 샘플
 * Created by KMS on 13/04/2020.
 */
// @Slf4j
// @Configuration
// @DependsOn("multiTxManager")
// @EnableConfigurationProperties(SecondDataSourceProperties.class)
public class SecondDslConfig {

    /*
    private final SecondDataSourceProperties properties;

    public SecondDSLConfig(SecondDataSourceProperties properties) {
        this.properties = properties;
    }

    public static final String DATASOURCE = "secondDataSource";

    @Bean(DATASOURCE)
    public DataSource dataSource() throws SQLException {
        MysqlXADataSource mysqlXADataSource = new MysqlXADataSource();
        mysqlXADataSource.setUrl(properties.getUrl());
        mysqlXADataSource.setUser(properties.getUsername());
        mysqlXADataSource.setPassword(properties.getPassword());
        mysqlXADataSource.setPinGlobalTxToPhysicalConnection(true);

        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setUniqueResourceName(DATASOURCE);
        xaDataSource.setXaDataSource(mysqlXADataSource);
        xaDataSource.setXaDataSourceClassName(properties.getXaDataSourceClassName());
        return xaDataSource;
    }

    @Bean("secondDsl")
    @DependsOn(DATASOURCE)
    DSLContext dslContext() throws Exception {
        DefaultConfiguration jooqConfiguration = new DefaultConfiguration();
        jooqConfiguration.settings()
                .withRenderQuotedNames(RenderQuotedNames.EXPLICIT_DEFAULT_UNQUOTED);
        jooqConfiguration.setDataSource(dataSource());
        jooqConfiguration.setSQLDialect(SQLDialect.MYSQL);
        // .withRenderNameCase(RenderNameCase.LOWER_IF_UNQUOTED);               // Defaults to AS_IS
        jooqConfiguration.setExecuteListenerProvider( new DefaultExecuteListenerProvider(new JOOQToSpringExceptionTransformer()) );
        return new DefaultDSLContext(jooqConfiguration);
    }
    */
}
