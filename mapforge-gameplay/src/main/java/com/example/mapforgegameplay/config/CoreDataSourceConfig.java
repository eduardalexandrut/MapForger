package com.example.mapforgegameplay.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.example.mapforgegameplay.repository.core",
        entityManagerFactoryRef = "coreEntityManagerFactory",
        transactionManagerRef = "coreTransactionManager"
)
public class CoreDataSourceConfig {

    @Bean(name = "coreDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.core")
    public DataSource coreDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "coreEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean coreEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("coreDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.example.mapforgegameplay.model.core")
                .persistenceUnit("core")
                .properties(Map.of(
                        "hibernate.hbm2ddl.auto", "none",   // never touch MapForge schema
                        "hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect"
                ))
                .build();
    }

    @Bean(name = "coreTransactionManager")
    public PlatformTransactionManager coreTransactionManager(
            @Qualifier("coreEntityManagerFactory") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}