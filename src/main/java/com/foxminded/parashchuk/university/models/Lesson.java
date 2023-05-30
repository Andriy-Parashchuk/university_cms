package com.foxminded.parashchuk.university.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

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

  @Column(name = "group_id")
  private int groupId;

  @Column(name = "\"time\"")
  private LocalDateTime time;

  private int audience;
  

}
