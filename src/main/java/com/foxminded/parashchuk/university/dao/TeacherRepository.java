package com.foxminded.parashchuk.university.dao;

import com.foxminded.parashchuk.university.models.Teacher;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**Class with call to teachers table in DB. */
public interface TeacherRepository extends CrudRepository<Teacher, Integer> {

  List<Teacher> findAllByOrderById();

}
