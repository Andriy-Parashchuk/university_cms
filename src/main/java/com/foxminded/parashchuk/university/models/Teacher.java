package com.foxminded.parashchuk.university.models;

import java.util.Objects;

/**Model class for Teacher.*/
public class Teacher extends User {
  private int audience;
  private String department;

  public Teacher(int id, String firstName, String lastName) {
    super(id, firstName, lastName);
  }
  
  public int getAudience() {
    return audience;
  }

  public void setAudience(int audience) {
    this.audience = audience;
  }

  public String getDepartment() {
    return department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + Objects.hash(audience, department);
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
    Teacher other = (Teacher) obj;
    return audience == other.audience && Objects.equals(department, other.department);
  }
  
}
