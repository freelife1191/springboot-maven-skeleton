package com.project.config;

/**
 * 멀티DB 서브DB 설정
 */
//@Configuration
//@EnableTransactionManagement
public class SubDbConfig {
    /*
    @Value("${spring.second.datasource.xa-data-source-class-name}") String dataSourceClassName;
    @Value("${spring.second.datasource.xa-properties.user}") String user;
    @Value("${spring.second.datasource.xa-properties.password}") String password;
    @Value("${spring.second.datasource.xa-properties.server-name}") String serverName;
    @Value("${spring.second.datasource.xa-properties.port-number}") String portNumber;
    @Value("${spring.second.datasource.xa-properties.database-name}") String databaseName;

    public static final String DATASOURCE = "ds2DataSource";

    @Bean(name = DATASOURCE)
    public DataSource dataSource() {
        AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
        ds.setUniqueResourceName(DATASOURCE);
        ds.setXaDataSourceClassName(dataSourceClassName);

        Properties p = new Properties();
        p.setProperty("user", user);
        p.setProperty("password", password);
        p.setProperty("serverName", serverName);
        p.setProperty("portNumber", portNumber);
        p.setProperty("databaseName", databaseName);
        p.setProperty("serverTimezone", "UTC");
        ds.setXaProperties (p);

        return ds;
    }

    @Bean(name="secondDsl")
    @DependsOn({DATASOURCE})
    DSLContext dslContext() throws Exception {
        DefaultConfiguration jooqConfiguration = new DefaultConfiguration();
        jooqConfiguration.set( SQLDialect.MARIADB );
        jooqConfiguration.set( dataSource()  );
        jooqConfiguration.set( new DefaultExecuteListenerProvider(new JOOQToSpringExceptionTransformer()) );
        DSLContext dslContext = new DefaultDSLContext(jooqConfiguration);
        return dslContext;
    }
    */
}
