package com.foxminded.parashchuk.university.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode (callSuper = true)
public class Student extends User {
  public Student(int id, String firstName, String lastName, Group group) {
    super(id, firstName, lastName);
    this.group = group;
  }

  private Group group;

}
