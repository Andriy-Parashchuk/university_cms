package com.foxminded.parashchuk.university.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupDTO {

  private int id;

  @Size(min = 2, max = 20, message = "Name size should be between 2 and 20")
  private String name;

}
