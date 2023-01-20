package com.foxminded.parashchuk.university.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class ScheduleTest {
  Teacher teacher1 = new Teacher(1, "Mark", "Robinson"); 
  Teacher teacher2 = new Teacher(2, "Elizabeth", "Miller"); 
  
  Group group1 = new Group("first");
  Group group2 = new Group("second");
  
  Student student1 = new Student(3, "Tony", "McMillan", group1);
  Student student2 = new Student(4, "Tomas", "Stivenson", group2);
  
  LocalDateTime time1 = LocalDateTime.of(2023, 2, 12, 15, 40);
  LocalDateTime time2 = LocalDateTime.of(2023, 3, 11, 10, 40);
  LocalDateTime time3 = LocalDateTime.of(2023, 2, 26, 12, 40);
  
  List<Lesson> lessons = Arrays.asList(
      new Lesson("Bio", teacher1, group1, time1, 22),
      new Lesson("Geo", teacher2, group2, time2, 22),
      new Lesson("Phisics", teacher1, group1, time3, 22),
      new Lesson("Philosophy", teacher2, group1, time1, 22),
      new Lesson("Chemistry", teacher1, group2, time2, 22));
  
  Schedule schedule = new Schedule();
  
  
  @Test
  void getLessonsTeacherDay_shouldReturnListWithLessons_whenTeacherSearchLessonsAtDay() {
    schedule.setLessons(lessons);
    List<Lesson> expected = Arrays.asList(new Lesson("Bio", teacher1, group1, time1, 22));
    assertEquals(expected, schedule.getLessonsTeacherDay(teacher1, LocalDate.of(2023, 2, 12)));
  }
  
  @Test
  void getLessonsTeacherMonth_shouldReturnListWithLessons_whenTeacherSearchLessonsAtMonth() {
    schedule.setLessons(lessons);
    List<Lesson> expected = Arrays.asList(
        new Lesson("Bio", teacher1, group1, time1, 22),
        new Lesson("Phisics", teacher1, group1, time3, 22));
    assertEquals(expected, schedule.getLessonsTeacherMonth(teacher1, LocalDate.of(2023, 2, 12)));
  }
  
  @Test
  void getLessonsStudentDay_shouldReturnListWithLessons_whenStudentSearchLessonsAtDay() {
    schedule.setLessons(lessons);
    List<Lesson> expected = Arrays.asList(
        new Lesson("Geo", teacher2, group2, time2, 22),
        new Lesson("Chemistry", teacher1, group2, time2, 22));
    assertEquals(expected, schedule.getLessonsStudentDay(student2, LocalDate.of(2023, 3, 11)));
  }
  
  @Test
  void getLessonsStudentMonth_shouldReturnListWithLessons_whenStudentSearchLessonsAtMonth() {
    schedule.setLessons(lessons);
    List<Lesson> expected = Arrays.asList(
        new Lesson("Bio", teacher1, group1, time1, 22),
        new Lesson("Phisics", teacher1, group1, time3, 22),
        new Lesson("Philosophy", teacher2, group1, time1, 22));
    assertEquals(expected, schedule.getLessonsStudentMonth(student1, LocalDate.of(2023, 2, 12)));
  }

}
