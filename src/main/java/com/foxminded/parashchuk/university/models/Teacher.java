package com.foxminded.parashchuk.university.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "teachers")
@ToString
@Getter
@Setter
@EqualsAndHashCode (callSuper = true)
@NoArgsConstructor
public class Teacher extends User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
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
