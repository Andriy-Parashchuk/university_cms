package com.foxminded.parashchuk.university.dao;

import com.foxminded.parashchuk.university.models.Teacher;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

/**Class with call to teachers table in DB. */
@Component
public class TeacherDao {
  private static final Logger log = LoggerFactory.getLogger(TeacherDao.class);
  private JdbcTemplate jdbcTemplate;
  
  public TeacherDao(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }
  
  RowMapper<Teacher> rowMapper = (resultSet, rowNum) -> {
    Teacher teacher = new Teacher(
        resultSet.getInt("teacher_id"), 
        resultSet.getString("first_name"),
        resultSet.getString("last_name"));
    
    teacher.setAudience(resultSet.getInt("audience"));
    teacher.setDepartment(resultSet.getString("department"));
    
    return teacher;
  };
  
  /**Get all teachers from table in DB.*/
  public List<Teacher> getAllTeachers() {
    String sql = "select teacher_id, first_name, last_name, audience, department "
        + "from teachers order by teacher_id";
    return jdbcTemplate.query(sql, rowMapper);
  }
  
  /**Save new teacher to table by Teacher object.*/
  public int createTeacher(Teacher teacher) {
    if (teacher == null) {
      throw new IllegalArgumentException("Teacher can not be a null");
    } else {
      String sql = "insert into teachers (first_name, last_name, audience, department) "
          + "values (?, ?, ?, ?)";
      return jdbcTemplate.update(
          sql, 
          teacher.getFirstName(),
          teacher.getLastName(), 
          teacher.getAudience(), 
          teacher.getDepartment());
    }
  }
  
  /**Get one teacher from table in DB by id.*/
  public Optional<Teacher> getTeacherById(int id) {
    String sql = "select teacher_id, first_name, last_name, audience, department "
        + "from teachers where teacher_id = ?";
    Teacher teacher = null;
    try {
      teacher = jdbcTemplate.queryForObject(sql, new Object[]{id}, rowMapper);
    } catch (DataAccessException exception) {
      log.info(String.format("Teacher with id %d not found.", id));
    }
    return Optional.ofNullable(teacher);
  }
  
  /**Update teacher by existing id in table and teacher object for overwriting.
   * Return 1 if overwriting was successful*/
  public int updateTeacherById(Teacher teacher, int id) {
    String sql = "update teachers set first_name = ?, last_name = ?, audience = ?, department = ?"
        + "where teacher_id = ?";
    return jdbcTemplate.update(
        sql, 
        teacher.getFirstName(), 
        teacher.getLastName(), 
        teacher.getAudience(), 
        teacher.getDepartment(),
        id);
  }
  
  /**Delete teacher by id from table in DB.
   * Return 1 if deleting was successful*/
  public int deleteTeacherById(int id) {
    return jdbcTemplate.update("delete from teachers where teacher_id = ?", id);
  }
}
