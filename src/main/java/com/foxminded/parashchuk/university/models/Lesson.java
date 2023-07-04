package com.foxminded.parashchuk.university.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "lessons")
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Lesson {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String name;

  @Column(name = "teacher_id")
  private int teacherId;

  @ManyToOne
  @JoinColumn(name = "teacher_id", insertable = false, updatable = false)
  private Teacher teacher;

  @Column(name = "group_id")
  private int groupId;

  @ManyToOne
  @JoinColumn(name = "group_id", insertable = false, updatable = false)
  private Group group;

  @Column(name = "\"time\"")
  private LocalDateTime time;

  private int audience;

  public Lesson(int id, String name, int teacherId, int groupId, LocalDateTime time, int audience) {
    this.id = id;
    this.name = name;
    this.teacherId = teacherId;
    this.groupId = groupId;
    this.time = time;
    this.audience = audience;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Lesson lesson = (Lesson) o;
    return id == lesson.id && teacherId == lesson.teacherId && groupId == lesson.groupId && audience == lesson.audience && name.equals(lesson.name) && time.equals(lesson.time);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, teacherId, groupId, time, audience);
  }
}
