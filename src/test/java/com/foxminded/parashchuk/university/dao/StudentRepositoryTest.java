package com.foxminded.parashchuk.university.dao;

import com.foxminded.parashchuk.university.config.TestPersistenceConfig;
import com.foxminded.parashchuk.university.models.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@EnableAutoConfiguration
@ContextConfiguration(classes = {TestPersistenceConfig.class})
@Sql(value = {"classpath:jdbc/groups.sql", "classpath:jdbc/students.sql"}, 
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class StudentRepositoryTest {
  
  @Autowired
  private StudentRepository dao;
  
  @Test 
  void getAllStudents_ReturnListWithStudents_whenDbIsNotEmpty() {
    List<Student> expected = Arrays.asList(
        new Student(1, "Chris", "Martin", 1, "test@test.test"),
        new Student(2, "Mari", "Osvald", 2, "test@test.test"));
    List<Student> students = dao.findAllByOrderById();
    assertEquals(expected, students);
  }
  
  @Test 
  void createStudent_shouldAddNewStudentWithIncrementedIdToDb_whenGetStudent() {
    Student student = new Student(0, "New", "Student", 1, "test@test.test");
    List<Student> expected = Arrays.asList(
        new Student(1, "Chris", "Martin", 1, "test@test.test"),
        new Student(2, "Mari", "Osvald", 2, "test@test.test"),
        new Student(3, "New", "Student", 1, "test@test.test"));
    
    assertEquals(student, dao.save(student));
    assertEquals(expected, dao.findAllByOrderById());
  }
  
  @Test 
  void saveStudent_shouldThrowIllegalArgsException_whenGetNull() {
    assertThrows(InvalidDataAccessApiUsageException.class, () -> dao.save(null));
  }
  
  @Test
  void getStudentById_shouldReturnStudent_whenGetStudentsExistsId() {
    Student student = dao.findById(1).orElse(null);
    Student expected = new Student(1, "Chris", "Martin", 1, "test@test.test");
    
    assertEquals(expected, student);
  }
  
  @Test
  void getStudentById_shouldThrowException_whenProvidedIdDoesNotExists() {
    assertEquals(Optional.empty(), dao.findById(12));
  }
  
  @Test 
  void updateStudentById_shouldUpdateStudent_whenGetExistsStudentIdAndParameters() {
    Student expected = new Student(1, "Updated", "Student", 2, "test@test.test");
 
    assertEquals(expected, dao.save(new Student(1, "Updated", "Student", 2, "test@test.test")));
    assertEquals(Optional.of(expected), dao.findById(1));
  }
  
  @Test 
  void deleteStudentById_shouldDeleteStudent_whenGetExistsStudentId() {
    List<Student> expected = Arrays.asList(
        new Student(1, "Chris", "Martin", 1, "test@test.test"),
        new Student(2, "Mari", "Osvald", 2, "test@test.test"));
        
    List<Student> students = dao.findAllByOrderById();
    assertEquals(expected, students);
    
    dao.deleteById(2);
    
    students = dao.findAllByOrderById();
    assertEquals(Arrays.asList(new Student(1, "Chris", "Martin", 1, "test@test.test")), students);
  }
  
  @Test 
  void deleteStudentById_shouldThrowException_whenStudentDoesNotExists() {
    assertThrows(EmptyResultDataAccessException.class, () -> dao.deleteById(12));
  }
  
}
