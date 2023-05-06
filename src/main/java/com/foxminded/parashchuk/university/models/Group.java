package com.foxminded.parashchuk.university.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Table(name = "groups")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Group {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String name;
  
}
