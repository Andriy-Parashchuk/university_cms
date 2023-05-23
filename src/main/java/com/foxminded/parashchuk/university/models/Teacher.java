package com.foxminded.parashchuk.university.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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

  @NotBlank(message = "Firstname is mandatory")
  @Size(min = 2, max = 20, message = "Firstname size should be between 2 and 20")
  @Column(name = "first_name")
  private String firstName;

  @NotBlank(message = "Lastname is mandatory")
  @Size(min = 2, max = 20, message = "Lastname size should be between 2 and 20")
  @Column(name = "last_name")
  private String lastName;

  @Transient
  @Email(regexp = "^[\\w-\\.]+@([\\w-])+\\.[\\w-]{3,6}", message = "Please enter a valid email")
  @Size(min = 5, message = "Email size should be greater then 5")
  private String email;

  @NotNull(message = "Audience is mandatory")
  private int audience;

  @NotBlank(message = "Department is mandatory")
  private String department;
  
  public Teacher(int id, String firstName, String lastName, String email) {
    super(id, firstName, lastName, email);
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
  }
  
}
