package com.foxminded.parashchuk.university.dao;

import com.foxminded.parashchuk.university.models.Student;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**Class with call to students table in DB. */
@Repository
public class StudentDao {
  private static final Logger log = LoggerFactory.getLogger(StudentDao.class);
  
  private JdbcTemplate jdbcTemplate;
  
  @Autowired
  public StudentDao(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }
  
  RowMapper<Student> rowMapper = BeanPropertyRowMapper.newInstance(Student.class);
  
  /**Get all students from table in DB.*/
  public List<Student> getAllStudents() {
    String sql = "select id, first_name, last_name, group_id "
        + "from students order by id";
    return jdbcTemplate.query(sql, rowMapper);
  }
  
  /**Save new student to table by Student object.*/
  public int createStudent(Student student) {
    if (student == null) {
      throw new IllegalArgumentException("Student can not be a null");
    } else {
      String sql = "insert into students (first_name, last_name, group_id) "
          + "values (?, ?, ?)";
      return jdbcTemplate.update(
          sql, 
          student.getFirstName(),
          student.getLastName(),
          student.getGroupId());
    }
  }
  
  /**Get one student from table in DB by id.*/
  public Optional<Student> getStudentById(int id) {
    String sql = "select id, first_name, last_name, group_id "
        + "from students where id = ?";
    Student student = null;
    try {
      student = jdbcTemplate.queryForObject(sql, new Object[]{id}, rowMapper);
    } catch (DataAccessException exception) {
      log.error(String.format("Student with id %d not found.", id));
    }
    return Optional.ofNullable(student);
  }
  
  /**Update student by existing id in table and student object for overwriting.
   * Return 1 if overwriting was successful*/
  public int updateStudentById(Student student, int id) {
    String sql = "update students set first_name = ?, last_name = ?, group_id = ? "
        + "where id = ?"; 
    return jdbcTemplate.update(
        sql, 
        student.getFirstName(), 
        student.getLastName(),
        student.getGroupId(),
        id);
  }
  
  /**Delete student by id from table in DB.
   * Return 1 if deleting was successful*/
  public int deleteStudentById(int id) {
    return jdbcTemplate.update("delete from students where id = ?", id);
  }
}
