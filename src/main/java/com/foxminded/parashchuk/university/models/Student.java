package com.foxminded.parashchuk.university.models;

import java.util.Objects;

/**Model class for Student.*/
public class Student extends User {
  private Group group;

  public Student(int id, String firstName, String lastName, Group group) {
    super(id, firstName, lastName);
    this.setGroup(group);
  }

  public Group getGroup() {
    return group;
  }

  public void setGroup(Group group) {
    this.group = group;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + Objects.hash(group);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Student other = (Student) obj;
    return Objects.equals(group, other.group);
  }

}
