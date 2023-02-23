package com.foxminded.parashchuk.university.models;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Schedule {

  private List<Lesson> lessons;
  

}
