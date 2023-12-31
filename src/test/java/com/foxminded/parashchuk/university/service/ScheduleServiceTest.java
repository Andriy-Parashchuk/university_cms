package com.foxminded.parashchuk.university.service;

import com.foxminded.parashchuk.university.dto.GroupDTO;
import com.foxminded.parashchuk.university.dto.LessonDTO;
import com.foxminded.parashchuk.university.dto.StudentDTO;
import com.foxminded.parashchuk.university.dto.TeacherDTO;
import com.foxminded.parashchuk.university.exceptions.LessonsNotFoundExceptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {
  TeacherDTO teacher1 = new TeacherDTO(1, "Mark", "Robinson", "mark_rob@testmail.com");
  TeacherDTO teacher2 = new TeacherDTO(2, "Elizabeth", "Miller", "eliza_miller@testmail.com");
  
  GroupDTO group1 = new GroupDTO(1, "first");
  GroupDTO group2 = new GroupDTO(2, "second");
  
  StudentDTO student1 = new StudentDTO(3, "Tony", "McMillan", group1.getId(), "tony@testmail.com");
  StudentDTO student2 = new StudentDTO(4, "Tomas", "Stivenson", group2.getId(), "tom@testmail.com");
  
  LocalDateTime time1 = LocalDateTime.of(2023, 2, 12, 15, 40);
  LocalDateTime time2 = LocalDateTime.of(2023, 3, 11, 10, 40);
  LocalDateTime time3 = LocalDateTime.of(2023, 2, 26, 12, 40);

  List<LessonDTO> lessons = Arrays.asList(
          new LessonDTO(1, "Bio", teacher1.getId(), group1.getId(), time1, 22),
          new LessonDTO(2, "Geo", teacher2.getId(), group2.getId(), time2, 22),
          new LessonDTO(3, "Physics", teacher1.getId(), group1.getId(), time3, 22),
          new LessonDTO(4, "Philosophy", teacher2.getId(), group1.getId(), time1, 22),
          new LessonDTO(5, "Chemistry", teacher1.getId(), group2.getId(), time2, 22));

  @InjectMocks
  ScheduleService schedule;

  @Mock
  LessonService lessonService;

  @Mock
  StudentService studentService;

  @Test
  void getLessonsTeacherDay_shouldReturnListWithLessons_whenTeacherSearchLessonsAtDay()
          throws LessonsNotFoundExceptions {
    when(lessonService.getAllLessons()).thenReturn(lessons);
    List<LessonDTO> expected = Arrays.asList(
            new LessonDTO(1, "Bio", teacher1.getId(), group1.getId(), time1, 22));
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

    List<LessonDTO> expected = Arrays.asList(
            new LessonDTO(1,"Bio", teacher1.getId(), group1.getId(), time1, 22),
            new LessonDTO(3,"Physics", teacher1.getId(), group1.getId(), time3, 22));
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
    when(studentService.getStudentById(4)).thenReturn(student2);
    List<LessonDTO> expected = Arrays.asList(
        new LessonDTO(2, "Geo", teacher2.getId(), group2.getId(), time2, 22),
        new LessonDTO(5, "Chemistry", teacher1.getId(), group2.getId(), time2, 22));
    assertEquals(expected, schedule.getLessonsStudentDay(student2.getId(), LocalDate.of(2023, 3, 11)));
  }

  @Test
  void getLessonsStudentDay_shouldThrowException_whenLessonsAreNotFound(){
    when(lessonService.getAllLessons()).thenReturn(lessons);
    when(studentService.getStudentById(4)).thenReturn(student2);

    assertThrows(LessonsNotFoundExceptions.class,
            () -> schedule.getLessonsStudentDay(student2.getId(), LocalDate.of(2023, 3, 14)));
  }

  @Test
  void getLessonsStudentMonth_shouldReturnListWithLessons_whenStudentSearchLessonsAtMonth()
          throws LessonsNotFoundExceptions {
    when(lessonService.getAllLessons()).thenReturn(lessons);
    when(studentService.getStudentById(3)).thenReturn(student1);

    List<LessonDTO> expected = Arrays.asList(
        new LessonDTO(1, "Bio", teacher1.getId(), group1.getId(), time1, 22),
        new LessonDTO(3, "Physics", teacher1.getId(), group1.getId(), time3, 22),
        new LessonDTO(4, "Philosophy", teacher2.getId(), group1.getId(), time1, 22));
    assertEquals(expected, schedule.getLessonsStudentMonth(student1.getId(), LocalDate.of(2023, 2, 12)));
  }

  @Test
  void getLessonsStudentMonth_shouldThrowException_whenLessonsAreNotFound(){
    when(lessonService.getAllLessons()).thenReturn(lessons);
    when(studentService.getStudentById(3)).thenReturn(student1);

    assertThrows(LessonsNotFoundExceptions.class,
            () -> schedule.getLessonsStudentMonth(student1.getId(), LocalDate.of(2023, 4, 12)));
  }

}
