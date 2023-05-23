package com.foxminded.parashchuk.university.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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

  @NotBlank(message = "Name is mandatory")
  @Size(min = 2, max = 20, message = "Name size should be between 2 and 20")
  private String name;

  @NotNull(message = "Teacher id is mandatory")
  @Column(name = "teacher_id")
  private int teacherId;

  @NotNull(message = "Group id is mandatory")
  @Column(name = "group_id")
  private int groupId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @NotNull(message = "Time is mandatory")
  @Column(name = "\"time\"")
  private LocalDateTime time;

  @NotNull(message = "Audience is mandatory")
  private int audience;
  

}
