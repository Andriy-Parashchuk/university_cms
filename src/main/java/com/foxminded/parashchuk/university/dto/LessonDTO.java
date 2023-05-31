package com.foxminded.parashchuk.university.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LessonDTO {

  private int id;

  @Size(min = 2, max = 20, message = "Name size should be between 2 and 20")
  private String name;

  @NotNull(message = "Teacher id is mandatory")
  private int teacherId;

  @NotNull(message = "Group id is mandatory")
  private int groupId;

  @NotNull(message = "Time is mandatory")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime time;

  @NotNull(message = "Audience is mandatory")
  private int audience;


}
