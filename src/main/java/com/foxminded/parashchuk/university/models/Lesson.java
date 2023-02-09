package com.foxminded.parashchuk.university.models;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
@AllArgsConstructor
public class Lesson {
  private int id;
  private String name;
  private int teacherId;
  private int groupId;
  private LocalDateTime time;
  private int audience;
}
