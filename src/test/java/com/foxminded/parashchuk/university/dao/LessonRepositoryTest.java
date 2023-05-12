package com.foxminded.parashchuk.university.dao;

import com.foxminded.parashchuk.university.config.TestPersistenceConfig;
import com.foxminded.parashchuk.university.models.Lesson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@EnableAutoConfiguration
@ContextConfiguration(classes = {TestPersistenceConfig.class})
@Sql(value = {"classpath:jdbc/groups.sql", "classpath:jdbc/teachers.sql", 
    "classpath:jdbc/lessons.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class LessonRepositoryTest {
  
  @Autowired
  private LessonRepository dao;

  
  @Test 
  void getAllLessons_ReturnListWithLessons_whenDbIsNotEmpty() {
    List<Lesson> expected = Arrays.asList(
        new Lesson(1, "Math", 2, 1, LocalDateTime.of(2023, 02, 10, 10, 30, 00), 305), 
        new Lesson(2, "Biology", 1, 2, LocalDateTime.of(2023, 02, 11, 12, 00, 00), 203));

    List<Lesson> lessons = dao.findAllByOrderById();
    assertEquals(expected, lessons);
  }
  
  @Test 
  void createLesson_shouldAddNewLessonWithIncrementedIdToDb_whenGetLesson() {
    Lesson newLesson = new Lesson(3, "New lesson", 2, 2, 
        LocalDateTime.of(2023, 02, 11, 12, 00, 00), 999);
    List<Lesson> expected = Arrays.asList(
        new Lesson(1, "Math", 2, 1, LocalDateTime.of(2023, 02, 10, 10, 30, 00), 305), 
        new Lesson(2, "Biology", 1, 2, LocalDateTime.of(2023, 02, 11, 12, 00, 00), 203),
        newLesson);

    assertEquals(newLesson, dao.save(newLesson));
    assertEquals(expected, dao.findAllByOrderById());
  }
  
  @Test 
  void createLesson_shouldThrowIllegalArgsException_whenGetNull() {
    assertThrows(InvalidDataAccessApiUsageException.class, () -> dao.save(null));
  }
  
  @Test
  void getTeacherById_shouldReturnTeacher_whenGetTeachersExistsId() {
    Lesson expected = new Lesson(1, "Math", 2, 1,
            LocalDateTime.of(2023, 02, 10, 10, 30, 00), 305);
    Lesson lesson = dao.findById(1).orElse(null);
    
    assertEquals(expected, lesson);
  }
  
  @Test
  void getLessonById_shouldThrowException_whenProvidedIdDoesNotExists() {
    assertEquals(Optional.empty(), dao.findById(12));
  }
  
  
  @Test 
  void updateLessonById_shouldUpdateLesson_whenGetExistsLessonIdAndParameters() {
    Lesson expected = new Lesson(1, "updated", 1, 2, 
        LocalDateTime.of(2023, 03, 12, 9, 00, 00), 403);
 
    assertEquals(expected, dao.save(new Lesson(1, "updated", 1, 2,
        LocalDateTime.of(2023, 03, 12, 9, 00, 00), 403)));
    assertEquals(Optional.of(expected), dao.findById(1));
  }

  @Test 
  void deleteLessonById_shouldDeleteLesson_whenGetExistsLessonId() {    
    List<Lesson> expected = Arrays.asList(
        new Lesson(1, "Math", 2, 1, LocalDateTime.of(2023, 02, 10, 10, 30, 00), 305), 
        new Lesson(2, "Biology", 1, 2, LocalDateTime.of(2023, 02, 11, 12, 00, 00), 203));

    List<Lesson> lessons = dao.findAllByOrderById();
    
    assertEquals(expected, lessons);
    
    dao.deleteById(2);
    
    lessons = dao.findAllByOrderById();
    assertEquals(Arrays.asList(new Lesson(1, "Math", 2, 1, 
        LocalDateTime.of(2023, 02, 10, 10, 30, 00), 305)), lessons);
  }
  
  @Test 
  void deleteLessonById_shouldThrowException_whenLessonDoesNot_Exists() {
    assertThrows(EmptyResultDataAccessException.class, () -> dao.deleteById(12));
  }
}
