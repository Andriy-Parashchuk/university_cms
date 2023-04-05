package com.foxminded.parashchuk.university.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.foxminded.parashchuk.university.exceptions.LessonsNotFoundExceptions;
import com.foxminded.parashchuk.university.models.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.foxminded.parashchuk.university.models.Lesson;
import com.foxminded.parashchuk.university.models.Teacher;

/**In this class all lessons from DB filtered by teacher or group and time (week or month).*/
@Service
public class ScheduleService {
  private static final Logger log = LoggerFactory.getLogger(ScheduleService.class);

  @Autowired
  private LessonService lessonService;

  @Autowired
  private StudentService studentService;

  /**Check all Lessons for teacher for one day.*/
  public List<Lesson> getLessonsTeacherDay(int teacherId, LocalDate date) throws LessonsNotFoundExceptions {
    log.info("Get all lessons for teacher with id {} for day by LocalDate {}.", teacherId, date);
    List<Lesson> lessons = lessonService.getAllLessons();
    List<Lesson> result = new ArrayList<>();
    for (Lesson l : lessons) {
      if (l.getTeacherId() == teacherId && l.getTime().toLocalDate().equals(date)) {
        result.add(l);
      }
    }
    if (result.isEmpty()){
      log.error("Lessons for teacher with id {} for day by LocalDate {} is not found", teacherId, date);
      throw new LessonsNotFoundExceptions(
              String.format("Lessons for teacher with id %d for day by LocalDate %s is not found",
                      teacherId, date));
    } else {
      return result;
    }
  }

  /**Check all Lessons for teacher by the end of the month.*/
  public List<Lesson> getLessonsTeacherMonth(int teacherId, LocalDate date) throws LessonsNotFoundExceptions {
    log.info("Get all lessons for teacher with id {} for month by LocalDate {}.", teacherId, date);
    List<Lesson> lessons = lessonService.getAllLessons();
    List<Lesson> result = new ArrayList<>();
    for (Lesson l : lessons) {
      if (l.getTeacherId() == teacherId
          && l.getTime().toLocalDate().getYear() == (date.getYear())
          && l.getTime().toLocalDate().getMonth() == (date.getMonth())) {
        result.add(l);
      }
    }
    if (result.isEmpty()){
      log.error("Lessons for teacher with id {} for month by LocalDate {} is not found", teacherId, date);
      throw new LessonsNotFoundExceptions(
              String.format("Lessons for teacher with id %d for month by LocalDate %s is not found",
                      teacherId, date));
    } else {
      return result;
    }
  }

  /**Check all Lessons for student for one day.*/
  public List<Lesson> getLessonsStudentDay(int studentId, LocalDate date) throws LessonsNotFoundExceptions {
    log.info("Get all lessons for student with id {} for day by LocalDate {}.", studentId, date);
    Student student = studentService.getStudentById(studentId);
    List<Lesson> lessons = lessonService.getAllLessons();
    List<Lesson> result = new ArrayList<>();
    for (Lesson l : lessons) {
      if (l.getGroupId() == student.getGroupId() && l.getTime().toLocalDate().equals(date)) {
        result.add(l);
      }
    }
    if (result.isEmpty()){
      log.error("Lessons for student with id {} for day by LocalDate {} is not found", student.getId(), date);
      throw new LessonsNotFoundExceptions(
              String.format("Lessons for student with id %d for day by LocalDate %s is not found",
                      student.getId(), date));
    } else {
      return result;
    }
  }

  /**Check all Lessons for student by the end of the month.*/
  public List<Lesson> getLessonsStudentMonth(int studentId, LocalDate date) throws LessonsNotFoundExceptions {
    log.info("Get all lessons for student with id {} for month by LocalDate {}.", studentId, date);
    Student student = studentService.getStudentById(studentId);
    List<Lesson> lessons = lessonService.getAllLessons();
    List<Lesson> result = new ArrayList<>();
    for (Lesson l : lessons) {
      if (l.getGroupId() == student.getGroupId()
          && l.getTime().toLocalDate().getYear() == (date.getYear())
          && l.getTime().toLocalDate().getMonth() == (date.getMonth())) {
        result.add(l);
      }
    }
    if (result.isEmpty()){
      log.error("Lessons for student with id {} for month by LocalDate {} is not found", student.getId(), date);
      throw new LessonsNotFoundExceptions(
              String.format("Lessons for student with id %d for month by LocalDate %s is not found",
                      student.getId(), date));
    } else {
      return result;
    }
  }

  
}
