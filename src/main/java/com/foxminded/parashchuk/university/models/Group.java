package com.foxminded.parashchuk.university.models;

import java.util.Objects;

/**Model class for Group.*/
public class Group {
  private String name;
  
  public Group(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Group other = (Group) obj;
    return Objects.equals(name, other.name);
  }
  

}
