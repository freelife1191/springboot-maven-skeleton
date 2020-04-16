package com.project.config.datasource;

// import com.atomikos.icatch.jta.UserTransactionImp;
// import com.atomikos.icatch.jta.UserTransactionManager;

/**
 * 멀티DB 트랜잭션 설정
 */
// @Slf4j
// @Configuration
public class XaDataManagerConfig {
    /*
    @Bean("userTransaction")
    public UserTransaction userTransaction() throws Throwable {
        UserTransactionImp userTransactionImp = new UserTransactionImp();
        userTransactionImp.setTransactionTimeout(3600);
        return userTransactionImp;
    }

    @Bean(name = "atomikosTransactionManager", initMethod = "init", destroyMethod = "close")
    public TransactionManager atomikosTransactionManager() throws Throwable {
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        userTransactionManager.setForceShutdown(false);
        return userTransactionManager;
    }

    @Bean(name = "multiTxManager")
    @DependsOn({"userTransaction", "atomikosTransactionManager"})
    public PlatformTransactionManager transactionManager(
            @Qualifier("atomikosTransactionManager")  TransactionManager transactionManager,
            @Qualifier("userTransaction") UserTransaction userTransaction
    ) throws Throwable {
        log.info("========= transactionManager =========");
        AtomikosJtaPlatform.transaction = userTransaction;
        AtomikosJtaPlatform.transactionManager = transactionManager;
        return new JtaTransactionManager(userTransaction, transactionManager);
    }
    */
}
