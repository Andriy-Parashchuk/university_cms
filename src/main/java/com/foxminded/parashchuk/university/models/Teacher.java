package com.foxminded.parashchuk.university.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode (callSuper = true)
public class Teacher extends User {
  public Teacher(int id, String firstName, String lastName) {
    super(id, firstName, lastName);
  }
  
  private int audience;
  private String department;


}
