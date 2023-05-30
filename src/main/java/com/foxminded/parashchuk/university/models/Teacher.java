package com.foxminded.parashchuk.university.models;

import lombok.*;

import javax.persistence.*;
import java.util.List;

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

  private String email;

  private int audience;

  private String department;

  @OneToMany(mappedBy = "teacherId", cascade = CascadeType.ALL)
  private List<Lesson> lessons;
  
  public Teacher(int id, String firstName, String lastName, String email) {
    super(id, firstName, lastName, email);
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
  }
  
}
