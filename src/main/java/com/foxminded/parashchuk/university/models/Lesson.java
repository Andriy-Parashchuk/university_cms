package com.foxminded.parashchuk.university.models;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Lesson {
  private int id;
  private String name;
  private int teacherId;
  private int groupId;
  private LocalDateTime time;
  private int audience;
  

}
