package com.foxminded.parashchuk.university.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**Model class for Lesson.*/
public class Schedule {

  private List<Lesson> lessons;
  
  /**Check all Lessons for teacher for one day.*/
  public List<Lesson> getLessonsTeacherDay(Teacher teacher, LocalDate date) {
    List<Lesson> result = new ArrayList<>();
    for (Lesson l : this.lessons) {
      if (l.getTeacher().equals(teacher) && l.getTime().toLocalDate().equals(date)) {
        result.add(l);
      }
    }
    return result; 
  }
  
  /**Check all Lessons for teacher by the end of the month.*/
  public List<Lesson> getLessonsTeacherMonth(Teacher teacher, LocalDate date) {
    List<Lesson> result = new ArrayList<>();
    for (Lesson l : this.lessons) {
      if (l.getTeacher().equals(teacher) 
          && l.getTime().toLocalDate().getYear() == (date.getYear()) 
          && l.getTime().toLocalDate().getMonth() == (date.getMonth())) {
        result.add(l);
      }
    }
    return result; 
  }
  
  /**Check all Lessons for student for one day.*/
  public List<Lesson> getLessonsStudentDay(Student student, LocalDate date) {
    List<Lesson> result = new ArrayList<>();
    for (Lesson l : this.lessons) {
      if (l.getGroup().equals(student.getGroup()) && l.getTime().toLocalDate().equals(date)) {
        result.add(l);
      }
    }
    return result; 
  }
  
  /**Check all Lessons for student by the end of the month.*/
  public List<Lesson> getLessonsStudentMonth(Student student, LocalDate date) {
    List<Lesson> result = new ArrayList<>();
    for (Lesson l : this.lessons) {
      if (l.getGroup().equals(student.getGroup())  
          && l.getTime().toLocalDate().getYear() == (date.getYear()) 
          && l.getTime().toLocalDate().getMonth() == (date.getMonth())) {
        result.add(l);
      }
    }
    return result; 
  }

  public List<Lesson> getLessons() {
    return lessons;
  }

  public void setLessons(List<Lesson> lessons) {
    this.lessons = lessons;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(lessons);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Schedule other = (Schedule) obj;
    return Objects.equals(lessons, other.lessons);
  }
}
