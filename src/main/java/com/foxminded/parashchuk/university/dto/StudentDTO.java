package com.foxminded.parashchuk.university.dto;


import com.foxminded.parashchuk.university.models.Group;
import lombok.*;

import javax.validation.constraints.NotNull;

@ToString
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class StudentDTO extends UserDTO{

  @NotNull(message = "Group id is mandatory")
  private int groupId;

  public StudentDTO(int id, String firstName, String lastName, int groupId, String email) {
    super(id, firstName, lastName, email);
    this.groupId = groupId;
  }
}
