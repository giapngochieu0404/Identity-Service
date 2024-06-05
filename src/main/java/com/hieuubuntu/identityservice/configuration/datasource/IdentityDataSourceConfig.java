package com.hieuubuntu.identityservice.configuration.datasource;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = IdentityDataSourceConfig.REPOSITORY_BASE_PACKAGES,
        entityManagerFactoryRef = IdentityDataSourceConfig.ENTITY_MANAGER,
        transactionManagerRef = IdentityDataSourceConfig.TRANSACTION_MANAGER)
public class IdentityDataSourceConfig {
    public static final String DATA_SOURCE_PROPERTIES = "IdentitySourceProperties";
    public static final String DATA_SOURCE_NAME = "IdentityDataSource";
    public static final String ENTITY_MANAGER = "IdentityEntityManager";
    public static final String TRANSACTION_MANAGER = "IdentityTransactionManager";
    public static final String[] ENTITY_BASE_PACKAGES = {"com.hieuubuntu.identityservice.entity"};
    public static final String REPOSITORY_BASE_PACKAGES = "com.hieuubuntu.identityservice.repositories";

    @Primary
    @Bean(name = DATA_SOURCE_PROPERTIES)
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties identityDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean(name = DATA_SOURCE_NAME)
    @ConfigurationProperties("spring.datasource.hikari")
    public DataSource identityDataSource(
            @Qualifier(DATA_SOURCE_PROPERTIES) DataSourceProperties identitySourceProperties) {
        return identitySourceProperties.initializeDataSourceBuilder().build();
    }

    @Primary
    @Bean(name = ENTITY_MANAGER)
    public LocalContainerEntityManagerFactoryBean identityEntityManager(
            @Qualifier(DATA_SOURCE_NAME) DataSource dataSource) {
        var entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setDataSource(dataSource);
        entityManagerFactory.setPackagesToScan(ENTITY_BASE_PACKAGES);
        entityManagerFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        return entityManagerFactory;
    }

    @Primary
    @Bean(TRANSACTION_MANAGER)
    public PlatformTransactionManager identityTransactionManager(
            @Qualifier(ENTITY_MANAGER) LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        var transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory.getObject());
        return transactionManager;
    }
}
