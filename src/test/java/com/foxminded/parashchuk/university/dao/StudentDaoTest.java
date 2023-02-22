package com.foxminded.parashchuk.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.foxminded.parashchuk.university.config.TestConfig;
import com.foxminded.parashchuk.university.models.Student;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@ContextConfiguration(classes = {TestConfig.class})
@Sql(value = {"classpath:jdbc/groups.sql", "classpath:jdbc/students.sql"}, 
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class StudentDaoTest {
  
  @Autowired
  private StudentDao dao;
  
  @Test 
  void getAllStdeunts_ReturnListWithStudents_whenDbIsNotEmpty() {
    List<Student> expected = Arrays.asList(
        new Student(1, "Chris", "Martin", 1), 
        new Student(2, "Mari", "Osvald", 2));
    List<Student> students = dao.getAllStudents();
    assertEquals(expected, students);
  }
  
  @Test 
  void createStudent_shouldAddNewStudentWithIncrementedIdToDb_whenGetStudent() {
    Student student = new Student(0, "New", "Student", 1);
    List<Student> expected = Arrays.asList(
        new Student(1, "Chris", "Martin", 1), 
        new Student(2, "Mari", "Osvald", 2),
        new Student(3, "New", "Student", 1));
    
    dao.createStudent(student);
    
    assertEquals(expected, dao.getAllStudents());
  }
  
  @Test 
  void saveStudent_shouldThrowIllegalArgsException_whenGetNull() {
    assertThrows(IllegalArgumentException.class, () -> dao.createStudent(null));
  }
  
  @Test
  void getStudentById_shouldReturnStudent_whenGetStudentsExistsId() {
    Student student = dao.getStudentById(1).get();
    Student expected = new Student(1, "Chris", "Martin", 1);
    
    assertEquals(expected, student);
  }
  
  @Test
  void getStudentById_shouldReturnNull_whenProvidedIdDoesNotExists() {
    assertFalse(dao.getStudentById(12).isPresent());
  }
  
  @Test 
  void updateStudentById_shouldUpdateStudent_whenGetExistsStudentIdAndParameters() {
    Student expected = new Student(1, "Updated", "Student", 2);
 
    assertEquals(1, dao.updateStudentById(new Student(0, "Updated", "Student", 2), 1));
    assertEquals(expected, dao.getStudentById(1).get());
  }
  
  @Test 
  void updateStudentById_shouldReturnZero_whenProvidedIdDoesNotExists() {
    assertEquals(0, dao.updateStudentById(new Student(0, "Updated", "Student", 2), 9));
  }
  
  @Test 
  void deleteStudentById_shouldDeleteStudent_whenGetExistsStudentId() {
    List<Student> expected = Arrays.asList(
        new Student(1, "Chris", "Martin", 1), 
        new Student(2, "Mari", "Osvald", 2));
        
    List<Student> students = dao.getAllStudents();
    assertEquals(expected, students);
    
    assertEquals(1, dao.deleteStudentById(2));
    
    students = dao.getAllStudents();
    assertEquals(Arrays.asList(new Student(1, "Chris", "Martin", 1)), students);
  }
  
  @Test 
  void deleteStudentById_shouldReturnZero_whenProvidedIdDoesNotExists() {
    assertEquals(0, dao.deleteStudentById(12));
  }
  
}
