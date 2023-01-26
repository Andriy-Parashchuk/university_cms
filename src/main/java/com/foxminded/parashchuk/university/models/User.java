package com.foxminded.parashchuk.university.models;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class User {
  private int id;
  private String firstName;
  private String lastName;
}
