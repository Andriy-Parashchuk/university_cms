package com.foxminded.parashchuk.university.dao;

import com.foxminded.parashchuk.university.models.Student;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**Class with call to students table in DB. */
public interface StudentRepository extends CrudRepository<Student, Integer> {

  List<Student> findAllByOrderById();

}
