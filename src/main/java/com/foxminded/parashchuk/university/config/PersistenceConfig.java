package com.foxminded.parashchuk.university.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
public class PersistenceConfig {
  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
    LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
    emf.setPersistenceXmlLocation("classpath:META-INF/persistence.xml");
    emf.setPersistenceUnitName("com.foxminded.parashchuk.university.persistence");
    emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    return emf;
  }

  @Bean
  public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
    JpaTransactionManager manager = new JpaTransactionManager();
    manager.setEntityManagerFactory(emf);
    return manager;
  }

}
