package com.foxminded.parashchuk.university.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ToString
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class UserDTO {

  private int id;

  @Size(min = 2, max = 20, message = "Firstname size should be between 2 and 20.")
  @NotNull(message = "Firstname is mandatory")
  private String firstName;

  @NotNull(message = "Lastname is mandatory")
  @Size(min = 2, max = 20, message = "Lastname size should be between 2 and 20.")
  private String lastName;

  @Email(regexp = "^[\\w-\\.]+@([\\w-])+\\.[\\w-]{3,6}", message = "Please enter a valid email.")
  @NotNull(message = "Email is mandatory.")
  private String email;

}
