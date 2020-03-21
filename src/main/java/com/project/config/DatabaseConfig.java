package com.project.config;

/**
 * 멀티DB Database 설정
 */
//@Configuration
//@EnableTransactionManagement
public class DatabaseConfig {
    /*
    @Bean(name="originalDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.original.datasource")
    public DataSource articleDataSource() {
        return DataSourceBuilder.create().build();
    }
    @Primary
    @Bean(name = "txManager")
    public PlatformTransactionManager transactionManager (@Qualifier("originalDataSource") DataSource dataSource){
        return  new DataSourceTransactionManager(dataSource);
    }


    @Bean(name ="originalDsl")
    @DependsOn({"originalDataSource"})
    DSLContext dslContext() throws Exception {
        DefaultConfiguration jooqConfiguration = new DefaultConfiguration();
        jooqConfiguration.set( SQLDialect.MARIADB );
        jooqConfiguration.set( articleDataSource()  );
        jooqConfiguration.set( new DefaultExecuteListenerProvider(new JOOQToSpringExceptionTransformer()) );
        DSLContext dslContext = new DefaultDSLContext(jooqConfiguration);
        return dslContext;
    }
    */
}
