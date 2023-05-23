package com.foxminded.parashchuk.university.service;

import com.foxminded.parashchuk.university.dao.LessonRepository;
import com.foxminded.parashchuk.university.models.Lesson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

/**Class contains requests for LessonDao class.*/

@Service
public class LessonService {
  @Autowired
  private LessonRepository dao;

  private static final Logger log = LoggerFactory.getLogger(LessonService.class);


  /**Get all lessons from table in DB.*/
  public List<Lesson> getAllLessons() {
    log.info("Get all data from Lessons table.");
    return dao.findAllByOrderById();
  }

  /**Save new Lesson to table by lesson object.*/
  public Lesson createLesson(Lesson lesson) {
    if (lesson == null) {
      log.error("Lesson can not be a null");
      throw new IllegalArgumentException("Lesson can not be a null");
    } else {
      log.info("Create new Lesson with name {}.", lesson.getName());
      return dao.save(lesson);
    }
  }

  /**Get one lesson from table in DB by id.*/
  public Lesson getLessonById(int id) {
    log.info("Get Lesson with id {}.", id);
    Lesson lesson = dao.findById(id).orElse(null);
    if (lesson == null) {
      log.error("Lesson with id {} is not found.", id);
      throw new NoSuchElementException(String.format("Lesson with id %d is not found.", id));
    }
    return lesson;
  }

  /**Update lesson by existing id in table.*/
  public Lesson updateLessonById(Lesson lesson) {
    log.info("Update Lesson with id {}.", lesson.getId());
    Lesson checkedLesson = getLessonById(lesson.getId());
    if (checkedLesson == null){
      log.error("Lesson with id {} is not found.", lesson.getId());
      throw new NoSuchElementException(String.format("Lesson with id %d is not found.", lesson.getId()));
    }
    return dao.save(lesson);
  }

  /**Delete lesson by id from table in DB.*/
  public void deleteLessonById(int id) {
    log.info("Delete Lesson with id {}.", id);
    dao.deleteById(id);
  }
}
