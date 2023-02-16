package com.foxminded.parashchuk.university.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@EqualsAndHashCode (callSuper = true)
@NoArgsConstructor
public class Teacher extends User {
  private int id;
  private String firstName;
  private String lastName;
  private int audience;
  private String department;
  
  public Teacher(int id, String firstName, String lastName) {
    super(id, firstName, lastName);
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
  }
  
}
