package com.foxminded.parashchuk.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.foxminded.parashchuk.university.config.TestConfig;
import com.foxminded.parashchuk.university.models.Teacher;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;


@SpringBootTest
@ContextConfiguration(classes = {TestConfig.class})
@Sql(value = {"classpath:jdbc/teachers.sql"}, 
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class TeacherDaoTest {
  
  @Autowired
  private TeacherDao dao;
  
  @Test 
  void getAllTeachers_ReturnListWithTeachers_whenDbIsNotEmpty() {
    Teacher teacher1 = new Teacher(1, "Chris", "Martin");
    Teacher teacher2 = new Teacher(2, "Mari", "Osvald");
    teacher1.setAudience(203);
    teacher1.setDepartment("Biology");
    
    teacher2.setAudience(304);
    teacher2.setDepartment("Math");
    
    List<Teacher> expected = Arrays.asList(teacher1, teacher2);

    List<Teacher> teachers = dao.getAllTeachers();
    assertEquals(expected, teachers);
  }
  
  @Test 
  void createTeacher_shouldAddNewTeacherWithIncrementedIdToDb_whenGetTeacher() {
    Teacher teacher1 = new Teacher(1, "Chris", "Martin");
    Teacher teacher2 = new Teacher(2, "Mari", "Osvald");
    teacher1.setAudience(203);
    teacher1.setDepartment("Biology");
    
    teacher2.setAudience(304);
    teacher2.setDepartment("Math");
    
    Teacher newTeacher = new Teacher(3, "new", "teacher");
 
    List<Teacher> expected = Arrays.asList(teacher1, teacher2, newTeacher);

    dao.createTeacher(newTeacher);
    assertEquals(expected, dao.getAllTeachers());
  }
  
  @Test 
  void createTeacher_shouldThrowIllegalArgsException_whenGetNull() {
    assertThrows(IllegalArgumentException.class, () -> dao.createTeacher(null));
  }
  
  @Test
  void getTeacherById_shouldReturnTeacher_whenGetTeachersExistsId() {
    Teacher expected = new Teacher(1, "Chris", "Martin");
    expected.setAudience(203);
    expected.setDepartment("Biology");
    
    Teacher teacher = dao.getTeacherById(1).get();
    
    assertEquals(expected, teacher);
  }
  
  @Test
  void getTeacherById_shouldReturnNull_whenProvidedIdDoesNotExists() {
    assertFalse(dao.getTeacherById(12).isPresent());
  }
  
  @Test 
  void updateTeacherById_shouldUpdateTeacher_whenGetExistsTeacherIdAndParameters() {
    Teacher expected = new Teacher(1, "updated", "teacher");
 
    assertEquals(1, dao.updateTeacherById(new Teacher(1, "updated", "teacher"), 1));
    assertEquals(expected, dao.getTeacherById(1).get());
  }
  
  @Test 
  void updateTeacherById_shouldReturnZero_whenProvidedIdDoesNotExists() {
    assertEquals(0, dao.updateTeacherById(new Teacher(1, "updated", "teacher"), 9));
  }
  
  @Test 
  void deleteTeacherById_shouldDeleteTeacher_whenGetExistsTeacherId() {
    Teacher teacher1 = new Teacher(1, "Chris", "Martin");
    Teacher teacher2 = new Teacher(2, "Mari", "Osvald");
    teacher1.setAudience(203);
    teacher1.setDepartment("Biology");
    
    teacher2.setAudience(304);
    teacher2.setDepartment("Math");
    
    List<Teacher> expected = Arrays.asList(teacher1, teacher2);
        
    List<Teacher> teachers = dao.getAllTeachers();
    
    assertEquals(expected, teachers);
    
    assertEquals(1, dao.deleteTeacherById(2));
    
    teachers = dao.getAllTeachers();
    assertEquals(Arrays.asList(teacher1), teachers);
  }
  
  @Test 
  void deleteTeacherById_shouldReturnZero_whenProvidedIdDoesNotExists() {
    assertEquals(0, dao.deleteTeacherById(12));
  }
}
