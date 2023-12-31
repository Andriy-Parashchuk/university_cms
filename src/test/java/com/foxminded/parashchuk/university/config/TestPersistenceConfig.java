package com.foxminded.parashchuk.university.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class TestPersistenceConfig {

  @Bean
  public DataSource dataSource() {
    return DataSourceBuilder.create()
            .url("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE") // In-memory H2 database
            .username("sa")
            .password("")
            .build();
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
    LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
    emf.setDataSource(dataSource);
    emf.setPackagesToScan("com.foxminded.parashchuk.university.models");
    emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    emf.setJpaProperties(hibernateProperties());
    return emf;
  }

  @Bean
  public Properties hibernateProperties() {
    Properties properties = new Properties();
    properties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
    properties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
    return properties;
  }

  @Bean
  public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
    JpaTransactionManager manager = new JpaTransactionManager();
    manager.setEntityManagerFactory(emf);
    return manager;
  }
}
