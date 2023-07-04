package com.foxminded.parashchuk.university.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;


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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Group group = (Group) o;
    return id == group.id && name.equals(group.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }

  public Group(int id, String name){
    this.id = id;
    this.name = name;
  }
  
}
