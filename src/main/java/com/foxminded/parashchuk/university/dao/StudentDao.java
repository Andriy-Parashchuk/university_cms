package com.foxminded.parashchuk.university.dao;

import com.foxminded.parashchuk.university.models.Student;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

/**Class with call to students table in DB. */
@Component
public class StudentDao {
  private static final Logger log = LoggerFactory.getLogger(StudentDao.class);
  private JdbcTemplate jdbcTemplate;
  
  public StudentDao(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }
  
  RowMapper<Student> rowMapper = (resultSet, rowNum) -> {
    return new Student(
        resultSet.getInt("student_id"), 
        resultSet.getString("first_name"),
        resultSet.getString("last_name"),
        resultSet.getInt("group_id"));
  };
  
  /**Get all students from table in DB.*/
  public List<Student> getAllStudents() {
    String sql = "select student_id, first_name, last_name, group_id "
        + "from students order by student_id";
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
    String sql = "select student_id, first_name, last_name, group_id "
        + "from students where student_id = ?";
    Student student = null;
    try {
      student = jdbcTemplate.queryForObject(sql, new Object[]{id}, rowMapper);
    } catch (DataAccessException exception) {
      log.info(String.format("Student with id %d not found.", id));
    }
    return Optional.ofNullable(student);
  }
  
  /**Update student by existing id in table and student object for overwriting.
   * Return 1 if overwriting was successful*/
  public int updateStudentById(Student student, int id) {
    String sql = "update students set first_name = ?, last_name = ?, group_id = ? "
        + "where student_id = ?"; 
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
    return jdbcTemplate.update("delete from students where student_id = ?", id);
  }
}
