package com.foxminded.parashchuk.university.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ToString
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class TeacherDTO extends UserDTO{

  @NotNull(message = "Audience is mandatory.")
  private int audience;

  @NotBlank(message = "Department is mandatory.")
  private String department;

  public TeacherDTO(int id, String firstName, String lastName, String email) {
    super(id, firstName, lastName, email);
  }
}
