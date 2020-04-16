package com.project.config.datasource;

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
        jooqConfiguration.set( SQLDialect.MARIADB );
        jooqConfiguration.set(dataSource());
        jooqConfiguration.set( new DefaultExecuteListenerProvider(new JOOQToSpringExceptionTransformer()) );
        return new DefaultDSLContext(jooqConfiguration);
    }
    */

}
