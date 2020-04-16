package com.project.config.datasource;

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
        jooqConfiguration.set( SQLDialect.MYSQL );
        jooqConfiguration.set(dataSource());
        jooqConfiguration.set( new DefaultExecuteListenerProvider(new JOOQToSpringExceptionTransformer()) );
        return new DefaultDSLContext(jooqConfiguration);
    }
    */
}
