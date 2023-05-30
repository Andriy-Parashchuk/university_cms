package com.foxminded.parashchuk.university.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;


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

  @OneToMany(mappedBy = "groupId", cascade = CascadeType.ALL)
  private List<Student> students;

  @OneToMany(mappedBy = "groupId", cascade = CascadeType.ALL)
  private List<Lesson> lessons;

  public Group(int id, String name){
    this.id = id;
    this.name = name;
  }
  
}
