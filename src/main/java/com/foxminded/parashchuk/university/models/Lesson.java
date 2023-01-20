package com.foxminded.parashchuk.university.models;

import java.time.LocalDateTime;
import java.util.Objects;

/**Model class for Lesson.*/
public class Lesson {
  private String name;
  private Teacher teacher;
  private Group group;
  private LocalDateTime time;
  private int audience;
  
  /**Constructor which contain name, teacher, group time and audience for Lesson.*/
  public Lesson(String name, Teacher teacher, Group group, LocalDateTime time, int audience) {
    this.name = name;
    this.teacher = teacher;
    this.group = group;
    this.time = time;
    this.audience = audience;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public Teacher getTeacher() {
    return teacher;
  }
  
  public void setTeacher(Teacher teacher) {
    this.teacher = teacher;
  }
  
  public Group getGroup() {
    return group;
  }
  
  public void setGroup(Group group) {
    this.group = group;
  }
  
  public LocalDateTime getTime() {
    return time;
  }
  
  public void setTime(LocalDateTime time) {
    this.time = time;
  }
  
  public int getAudience() {
    return audience;
  }
  
  public void setAudience(int audience) {
    this.audience = audience;
  }

  @Override
  public int hashCode() {
    return Objects.hash(audience, group, name, teacher, time);
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
    Lesson other = (Lesson) obj;
    return audience == other.audience && Objects.equals(group, other.group) 
        && Objects.equals(name, other.name)
        && Objects.equals(teacher, other.teacher) && Objects.equals(time, other.time);
  }
  
}
