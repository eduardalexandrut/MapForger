package com.example.mapforgegameplay.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.example.mapforgegameplay.repository.gameplay",
        entityManagerFactoryRef = "gameplayEntityManagerFactory",
        transactionManagerRef = "gameplayTransactionManager"
)
public class GameplayDataSourceConfig {

    @Primary
    @Bean(name = "gameplayDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.gameplay")
    public DataSource gameplayDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "gameplayEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean gameplayEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("gameplayDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.example.mapforgegameplay.model.gameplay")
                .persistenceUnit("gameplay")
                .properties(Map.of(
                        "hibernate.hbm2ddl.auto", "update",
                        "hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect"
                ))
                .build();
    }

    @Primary
    @Bean(name = "gameplayTransactionManager")
    public PlatformTransactionManager gameplayTransactionManager(
            @Qualifier("gameplayEntityManagerFactory") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}