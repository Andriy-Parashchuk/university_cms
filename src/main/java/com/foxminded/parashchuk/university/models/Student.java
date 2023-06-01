package com.foxminded.parashchuk.university.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "students")
@ToString
@Getter
@Setter
@EqualsAndHashCode (callSuper = true)
@NoArgsConstructor
public class Student extends User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  private String email;

  @Column(name = "group_id")
  private int groupId;
  
  public Student(int id, String firstName, String lastName, int groupId, String email) {
    super(id, firstName, lastName, email);
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.groupId = groupId;
    this.email = email;
  }
  

}
