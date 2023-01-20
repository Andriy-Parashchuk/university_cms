package com.foxminded.parashchuk.university.models;

import java.util.Objects;

/**Model class for User.*/
public class User {
  private int id;
  private String firstName;
  private String lastName;
  
  /**Constructor which contain id, firstName and lastName for any User.*/
  public User(int id, String firstName, String lastName) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
  }
  
  public int getId() {
    return id;
  }
  
  public void setId(int id) {
    this.id = id;
  }
  
  public String getFirstName() {
    return firstName;
  }
  
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }
  
  public String getLastName() {
    return lastName;
  }
  
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  @Override
  public int hashCode() {
    return Objects.hash(firstName, id, lastName);
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
    User other = (User) obj;
    return Objects.equals(firstName, other.firstName) && id == other.id 
        && Objects.equals(lastName, other.lastName);
  }
}
