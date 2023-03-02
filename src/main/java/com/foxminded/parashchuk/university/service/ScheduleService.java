package com.foxminded.parashchuk.university.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.foxminded.parashchuk.university.models.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.foxminded.parashchuk.university.models.Lesson;
import com.foxminded.parashchuk.university.models.Teacher;

/**In this class all lessons from DB filtered by teacher or group and time (week or month).*/
@Service
public class ScheduleService {
  @Autowired
  private LessonService lessonService;

  /**Check all Lessons for teacher for one day.*/
  public List<Lesson> getLessonsTeacherDay(Teacher teacher, LocalDate date) {
    List<Lesson> lessons = lessonService.getAllLessons();
    List<Lesson> result = new ArrayList<>();
    for (Lesson l : lessons) {
      if (l.getTeacherId() == teacher.getId() && l.getTime().toLocalDate().equals(date)) {
        result.add(l);
      }
    }
    return result;
  }

  /**Check all Lessons for teacher by the end of the month.*/
  public List<Lesson> getLessonsTeacherMonth(Teacher teacher, LocalDate date) {
    List<Lesson> lessons = lessonService.getAllLessons();
    List<Lesson> result = new ArrayList<>();
    for (Lesson l : lessons) {
      if (l.getTeacherId() == teacher.getId()
          && l.getTime().toLocalDate().getYear() == (date.getYear())
          && l.getTime().toLocalDate().getMonth() == (date.getMonth())) {
        result.add(l);
      }
    }
    return result;
  }

  /**Check all Lessons for student for one day.*/
  public List<Lesson> getLessonsStudentDay(Student student, LocalDate date) {
    List<Lesson> lessons = lessonService.getAllLessons();
    List<Lesson> result = new ArrayList<>();
    for (Lesson l : lessons) {
      if (l.getGroupId() == student.getGroupId() && l.getTime().toLocalDate().equals(date)) {
        result.add(l);
      }
    }
    return result;
  }

  /**Check all Lessons for student by the end of the month.*/
  public List<Lesson> getLessonsStudentMonth(Student student, LocalDate date) {
    List<Lesson> lessons = lessonService.getAllLessons();
    List<Lesson> result = new ArrayList<>();
    for (Lesson l : lessons) {
      if (l.getGroupId() == student.getGroupId()
          && l.getTime().toLocalDate().getYear() == (date.getYear())
          && l.getTime().toLocalDate().getMonth() == (date.getMonth())) {
        result.add(l);
      }
    }
    return result;
  }

  
}
