package com.foxminded.parashchuk.university.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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

  @NotBlank(message = "Firstname is mandatory")
  @Size(min = 2, max = 20, message = "Firstname size should be between 2 and 20")
  @Column(name = "first_name")
  private String firstName;

  @NotBlank(message = "Lastname is mandatory")
  @Size(min = 2, max = 20, message = "Lastname size should be between 2 and 20")
  @Column(name = "last_name")
  private String lastName;

  @Email(regexp = "^[\\w-\\.]+@([\\w-])+\\.[\\w-]{3,6}", message = "Please enter a valid email")
  @Size(min = 5, message = "Email size should be greater then 5")
  @Transient
  private String email;

  @NotNull(message = "Group id is mandatory")
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
