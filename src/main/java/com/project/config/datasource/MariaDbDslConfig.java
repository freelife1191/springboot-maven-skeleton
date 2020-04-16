package com.project.config.datasource;

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
        jooqConfiguration.set( SQLDialect.MARIADB );
        jooqConfiguration.set(dataSource());
        jooqConfiguration.set( new DefaultExecuteListenerProvider(new JOOQToSpringExceptionTransformer()) );
        return new DefaultDSLContext(jooqConfiguration);
    }
    */
}
