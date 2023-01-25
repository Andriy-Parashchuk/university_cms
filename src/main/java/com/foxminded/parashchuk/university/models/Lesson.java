package com.foxminded.parashchuk.university.models;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Lesson {
  private String name;
  private Teacher teacher;
  private Group group;
  private LocalDateTime time;
  private int audience;
}
