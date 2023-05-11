package com.foxminded.parashchuk.university.dao;

import com.foxminded.parashchuk.university.models.Lesson;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**Class with call to lessons table in DB. */
public interface LessonRepository extends CrudRepository<Lesson, Integer> {

  List<Lesson> findAllByOrderById();

}
