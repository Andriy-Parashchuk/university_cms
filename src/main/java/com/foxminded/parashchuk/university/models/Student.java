package com.foxminded.parashchuk.university.models;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "students")
@ToString
@Getter
@Setter
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

  @ManyToOne
  @JoinColumn(name = "group_id", insertable = false, updatable = false)
  private Group group;

  public Student(int id, String firstName, String lastName, int groupId, String email) {
    super(id, firstName, lastName, email);
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.groupId = groupId;
    this.email = email;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    Student student = (Student) o;
    return id == student.id && groupId == student.groupId && firstName.equals(student.firstName) && lastName.equals(student.lastName) && email.equals(student.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), id, firstName, lastName, email, groupId);
  }



}
