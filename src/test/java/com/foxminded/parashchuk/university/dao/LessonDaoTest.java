package com.foxminded.parashchuk.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.foxminded.parashchuk.university.config.TestConfig;
import com.foxminded.parashchuk.university.models.Lesson;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;


@SpringBootTest
@ContextConfiguration(classes = {TestConfig.class})
@Sql(value = {"classpath:jdbc/groups.sql", "classpath:jdbc/teachers.sql", 
    "classpath:jdbc/lessons.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class LessonDaoTest {
  
  @Autowired
  private LessonDao dao;

  
  @Test 
  void getAllLessons_ReturnListWithLessons_whenDbIsNotEmpty() {
    List<Lesson> expected = Arrays.asList(
        new Lesson(1, "Math", 2, 1, LocalDateTime.of(2023, 02, 10, 10, 30, 00), 305), 
        new Lesson(2, "Biology", 1, 2, LocalDateTime.of(2023, 02, 11, 12, 00, 00), 203));

    List<Lesson> lessons = dao.getAllLessons();
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

    dao.createLesson(newLesson);
    assertEquals(expected, dao.getAllLessons());
  }
  
  @Test 
  void createLesson_shouldThrowIllegalArgsException_whenGetNull() {
    assertThrows(IllegalArgumentException.class, () -> dao.createLesson(null));
  }
  
  @Test
  void getTeacherById_shouldReturnTeacher_whenGetTeachersExistsId() {
    Lesson expected = new Lesson(1, "Math", 2, 1, LocalDateTime.of(2023, 02, 10, 10, 30, 00), 305); 
    Lesson lesson = dao.getLessonById(1).get();
    
    assertEquals(expected, lesson);
  }
  
  @Test
  void getLessonById_shouldReturnNull_whenProvidedIdDoesNotExists() {
    assertFalse(dao.getLessonById(12).isPresent());
  }
  
  
  @Test 
  void updateLessonById_shouldUpdateLesson_whenGetExistsLessonIdAndParameters() {
    Lesson expected = new Lesson(1, "updated", 1, 2, 
        LocalDateTime.of(2023, 03, 12, 9, 00, 00), 403);
 
    assertEquals(1, dao.updateLessonById(new Lesson(1, "updated", 1, 2, 
        LocalDateTime.of(2023, 03, 12, 9, 00, 00), 403), 1));
    assertEquals(expected, dao.getLessonById(1).get());
  }
  
  @Test 
  void updateLessonById_shouldThrowException_whenLessonDoesNot_Exists() {

    Lesson lesson = new Lesson(1, "updated", 1, 2,
            LocalDateTime.of(2023, 03, 12, 9, 00, 00), 403);
    assertThrows(NoSuchElementException.class, () -> dao.updateLessonById(lesson, 5));
  }
  
  @Test 
  void deleteLessonById_shouldDeleteLesson_whenGetExistsLessonId() {    
    List<Lesson> expected = Arrays.asList(
        new Lesson(1, "Math", 2, 1, LocalDateTime.of(2023, 02, 10, 10, 30, 00), 305), 
        new Lesson(2, "Biology", 1, 2, LocalDateTime.of(2023, 02, 11, 12, 00, 00), 203));

    List<Lesson> lessons = dao.getAllLessons();
    
    assertEquals(expected, lessons);
    
    assertEquals(1, dao.deleteLessonById(2));
    
    lessons = dao.getAllLessons();
    assertEquals(Arrays.asList(new Lesson(1, "Math", 2, 1, 
        LocalDateTime.of(2023, 02, 10, 10, 30, 00), 305)), lessons);
  }
  
  @Test 
  void deleteLessonById_shouldThrowException_whenLessonDoesNot_Exists() {
    assertThrows(NoSuchElementException.class, () -> dao.deleteLessonById(12));
  }
}
