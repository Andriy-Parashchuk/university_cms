package com.foxminded.parashchuk.university.config;

import com.foxminded.parashchuk.university.dao.GroupDao;
import com.foxminded.parashchuk.university.dao.LessonDao;
import com.foxminded.parashchuk.university.dao.StudentDao;
import com.foxminded.parashchuk.university.dao.TeacherDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
public class TestConfig {
  @Bean
  DataSource dataSource() {
    return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
  }
  
  @Bean
  JdbcTemplate jdbcTemplate() {
    return new JdbcTemplate(dataSource());
  }

  @Bean
  GroupDao groupDao() {
    return new GroupDao();
  }

  @Bean
  TeacherDao teacherDao() {
    return new TeacherDao();
  }
  
  @Bean
  StudentDao studentDao() {
    return new StudentDao();
  }
  
  @Bean
  LessonDao lessonDao() {
    return new LessonDao();
  }


}
