package com.foxminded.parashchuk.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.foxminded.parashchuk.university.exceptions.LessonsNotFoundExceptions;
import com.foxminded.parashchuk.university.models.Group;
import com.foxminded.parashchuk.university.models.Lesson;
import com.foxminded.parashchuk.university.models.Student;
import com.foxminded.parashchuk.university.models.Teacher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {
  Teacher teacher1 = new Teacher(1, "Mark", "Robinson");
  Teacher teacher2 = new Teacher(2, "Elizabeth", "Miller"); 
  
  Group group1 = new Group(1, "first");
  Group group2 = new Group(2, "second");
  
  Student student1 = new Student(3, "Tony", "McMillan", group1.getId());
  Student student2 = new Student(4, "Tomas", "Stivenson", group2.getId());
  
  LocalDateTime time1 = LocalDateTime.of(2023, 2, 12, 15, 40);
  LocalDateTime time2 = LocalDateTime.of(2023, 3, 11, 10, 40);
  LocalDateTime time3 = LocalDateTime.of(2023, 2, 26, 12, 40);
  
  List<Lesson> lessons = Arrays.asList(
      new Lesson(1, "Bio", teacher1.getId(), group1.getId(), time1, 22),
      new Lesson(2, "Geo", teacher2.getId(), group2.getId(), time2, 22),
      new Lesson(3, "Physics", teacher1.getId(), group1.getId(), time3, 22),
      new Lesson(4, "Philosophy", teacher2.getId(), group1.getId(), time1, 22),
      new Lesson(5, "Chemistry", teacher1.getId(), group2.getId(), time2, 22));

  @InjectMocks
  ScheduleService schedule;

  @Mock
  LessonService lessonService;
  
  @Test
  void getLessonsTeacherDay_shouldReturnListWithLessons_whenTeacherSearchLessonsAtDay()
          throws LessonsNotFoundExceptions {
    when(lessonService.getAllLessons()).thenReturn(lessons);
    List<Lesson> expected = Arrays.asList(
            new Lesson(1, "Bio", teacher1.getId(), group1.getId(), time1, 22));
    assertEquals(expected, schedule.getLessonsTeacherDay(teacher1.getId(), LocalDate.of(2023, 2, 12)));
  }

  @Test
  void getLessonsTeacherDay_shouldThrowException_whenLessonsAreNotFound(){
    when(lessonService.getAllLessons()).thenReturn(lessons);
    assertThrows(LessonsNotFoundExceptions.class,
            () -> schedule.getLessonsTeacherDay(teacher1.getId(), LocalDate.of(2023, 2, 14)));
  }
  
  @Test
  void getLessonsTeacherMonth_shouldReturnListWithLessons_whenTeacherSearchLessonsAtMonth()
          throws LessonsNotFoundExceptions {
    when(lessonService.getAllLessons()).thenReturn(lessons);

    List<Lesson> expected = Arrays.asList(
            new Lesson(1,"Bio", teacher1.getId(), group1.getId(), time1, 22),
            new Lesson(3,"Physics", teacher1.getId(), group1.getId(), time3, 22));
    assertEquals(expected, schedule.getLessonsTeacherMonth(teacher1.getId(), LocalDate.of(2023, 2, 12)));
  }

  @Test
  void getLessonsTeacherMonth_shouldThrowException_whenLessonsAreNotFound(){
    when(lessonService.getAllLessons()).thenReturn(lessons);
    assertThrows(LessonsNotFoundExceptions.class,
            () -> schedule.getLessonsTeacherMonth(teacher2.getId(), LocalDate.of(2023, 5, 26)));
  }

  @Test
  void getLessonsStudentDay_shouldReturnListWithLessons_whenStudentSearchLessonsAtDay()
          throws LessonsNotFoundExceptions {
    when(lessonService.getAllLessons()).thenReturn(lessons);
    List<Lesson> expected = Arrays.asList(
        new Lesson(2, "Geo", teacher2.getId(), group2.getId(), time2, 22),
        new Lesson(5, "Chemistry", teacher1.getId(), group2.getId(), time2, 22));
    assertEquals(expected, schedule.getLessonsStudentDay(student2.getId(), LocalDate.of(2023, 3, 11)));
  }

  @Test
  void getLessonsStudentDay_shouldThrowException_whenLessonsAreNotFound(){
    when(lessonService.getAllLessons()).thenReturn(lessons);
    assertThrows(LessonsNotFoundExceptions.class,
            () -> schedule.getLessonsStudentDay(student2.getId(), LocalDate.of(2023, 3, 14)));
  }

  @Test
  void getLessonsStudentMonth_shouldReturnListWithLessons_whenStudentSearchLessonsAtMonth()
          throws LessonsNotFoundExceptions {
    when(lessonService.getAllLessons()).thenReturn(lessons);
    List<Lesson> expected = Arrays.asList(
        new Lesson(1, "Bio", teacher1.getId(), group1.getId(), time1, 22),
        new Lesson(3, "Physics", teacher1.getId(), group1.getId(), time3, 22),
        new Lesson(4, "Philosophy", teacher2.getId(), group1.getId(), time1, 22));
    assertEquals(expected, schedule.getLessonsStudentMonth(student1.getId(), LocalDate.of(2023, 2, 12)));
  }

  @Test
  void getLessonsStudentMonth_shouldThrowException_whenLessonsAreNotFound(){
    when(lessonService.getAllLessons()).thenReturn(lessons);
    assertThrows(LessonsNotFoundExceptions.class,
            () -> schedule.getLessonsStudentMonth(student1.getId(), LocalDate.of(2023, 4, 12)));
  }

}
