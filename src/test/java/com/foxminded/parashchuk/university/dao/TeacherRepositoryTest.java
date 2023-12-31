package com.foxminded.parashchuk.university.dao;

import com.foxminded.parashchuk.university.config.TestPersistenceConfig;
import com.foxminded.parashchuk.university.models.Lesson;
import com.foxminded.parashchuk.university.models.Teacher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@DataJpaTest
@EnableAutoConfiguration
@ContextConfiguration(classes = {TestPersistenceConfig.class})
@Sql(value = {"classpath:jdbc/teachers.sql"}, 
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class TeacherRepositoryTest {
  
  @Autowired
  private TeacherRepository dao;
  
  @Test 
  void getAllTeachers_ReturnListWithTeachers_whenDbIsNotEmpty() {
    Teacher teacher1 = new Teacher(1, "Chris", "Martin", "test@test.test");
    Teacher teacher2 = new Teacher(2, "Mari", "Osvald", "test@test.test");
    teacher1.setAudience(203);
    teacher1.setDepartment("Biology");
    teacher1.setLessons(new ArrayList<Lesson>());

    teacher2.setAudience(304);
    teacher2.setDepartment("Math");
    teacher2.setLessons(new ArrayList<Lesson>());

    List<Teacher> expected = Arrays.asList(teacher1, teacher2);

    List<Teacher> teachers = dao.findAllByOrderById();
    assertEquals(expected, teachers);
  }
  
  @Test 
  void createTeacher_shouldAddNewTeacherWithIncrementedIdToDb_whenGetTeacher() {
    Teacher teacher1 = new Teacher(1, "Chris", "Martin", "test@test.test");
    Teacher teacher2 = new Teacher(2, "Mari", "Osvald", "test@test.test");
    teacher1.setAudience(203);
    teacher1.setDepartment("Biology");
    teacher1.setLessons(new ArrayList<Lesson>());

    teacher2.setAudience(304);
    teacher2.setDepartment("Math");
    teacher2.setLessons(new ArrayList<Lesson>());
    
    Teacher newTeacher = new Teacher(3, "new", "teacher", "test@test.test");
    newTeacher.setAudience(305);
    newTeacher.setDepartment("Philosophy");
    newTeacher.setLessons(new ArrayList<Lesson>());

    List<Teacher> expected = Arrays.asList(teacher1, teacher2, newTeacher);

    assertEquals(newTeacher, dao.save(newTeacher));
    assertEquals(expected, dao.findAllByOrderById());
  }
  
  @Test 
  void createTeacher_shouldThrowIllegalArgsException_whenGetNull() {
    assertThrows(InvalidDataAccessApiUsageException.class, () -> dao.save(null));
  }
  
  @Test
  void getTeacherById_shouldReturnTeacher_whenGetTeachersExistsId() {
    Teacher expected = new Teacher(1, "Chris", "Martin", "test@test.test");
    expected.setAudience(203);
    expected.setDepartment("Biology");
    expected.setLessons(new ArrayList<Lesson>());

    Teacher teacher = dao.findById(1).orElse(null);
    
    assertEquals(expected, teacher);
  }
  
  @Test
  void getTeacherById_shouldThrowException_whenProvidedIdDoesNotExists() {
    assertEquals(Optional.empty(), dao.findById(12));
  }
  
  @Test 
  void updateTeacherById_shouldUpdateTeacher_whenGetExistsTeacherIdAndParameters() {
    Teacher expected = new Teacher(1, "updated", "teacher", "test@test.test");
 
    assertEquals(expected, dao.save(new Teacher(1, "updated", "teacher", "test@test.test")));
    assertEquals(Optional.of(expected), dao.findById(1));
  }

  @Test 
  void deleteTeacherById_shouldDeleteTeacher_whenGetExistsTeacherId() {
    Teacher teacher1 = new Teacher(1, "Chris", "Martin", "test@test.test");
    Teacher teacher2 = new Teacher(2, "Mari", "Osvald", "test@test.test");
    teacher1.setAudience(203);
    teacher1.setDepartment("Biology");
    teacher1.setLessons(new ArrayList<Lesson>());

    teacher2.setAudience(304);
    teacher2.setDepartment("Math");
    teacher2.setLessons(new ArrayList<Lesson>());

    List<Teacher> expected = Arrays.asList(teacher1, teacher2);
        
    List<Teacher> teachers = dao.findAllByOrderById();
    
    assertEquals(expected, teachers);
    
    dao.deleteById(2);
    
    teachers = dao.findAllByOrderById();
    assertEquals(Arrays.asList(teacher1), teachers);
  }
  
  @Test 
  void deleteTeacherById_shouldThrowException_whenTeacherDoesNotExists() {
    assertThrows(EmptyResultDataAccessException.class, () -> dao.deleteById(12));
  }
}
