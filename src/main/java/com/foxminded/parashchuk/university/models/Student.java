package com.foxminded.parashchuk.university.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@EqualsAndHashCode (callSuper = true)
public class Student extends User {
  private int id;
  private String firstName;
  private String lastName;
  private int groupId;
  
  public Student(int id, String firstName, String lastName, int groupId) {
    super(id, firstName, lastName);
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.groupId = groupId;
  }

}
