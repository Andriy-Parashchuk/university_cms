package com.foxminded.parashchuk.university.dao;

import com.foxminded.parashchuk.university.models.Lesson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

/**Class with call to lessons table in DB. */
@Repository
@Transactional
public class LessonDao {
  private static final Logger log = LoggerFactory.getLogger(LessonDao.class);

  @PersistenceContext
  private EntityManager entityManager;

  /**Get all lessons from table in DB.*/
  public List<Lesson> getAllLessons() {
    log.info("Get all data from Lessons table.");
    return entityManager.createQuery("SELECT l FROM Lesson l order by l.id", Lesson.class).getResultList();

  }
  
  /**Save new Lesson to table by lesson object.*/
  public void createLesson(Lesson lesson) {
    if (lesson == null) {
      log.error("Teacher can not be a null");
      throw new IllegalArgumentException("Teacher can not be a null");
    } else {
      log.info("Create new Lesson with name {}.", lesson.getName());
      entityManager.merge(lesson);
    }
  }
  
  /**Get one lesson from table in DB by id.*/
  public Lesson getLessonById(int id) {
    log.info("Get Lesson with id {}.", id);
    Lesson lesson = entityManager.find(Lesson.class, id);
    if (lesson == null) {
      log.error("Lesson with id {} is not found.", id);
      throw new NoSuchElementException(String.format("Lesson with id %d is not found.", id));
    }
    return lesson;
  }
  
  /**Update lesson by existing id in table and lesson object for overwriting.
   * Return 1 if overwriting was successful*/
  public void updateLessonById(Lesson lesson) {
    log.info("Update Lesson with id {}.", lesson.getId());
    Lesson checkedLesson = entityManager.find(Lesson.class, lesson.getId());
    if (checkedLesson == null) {
      log.error("Lesson with id {} is not found.", lesson.getId());
      throw new NoSuchElementException(String.format("Lesson with id %d is not found.", lesson.getId()));
    }
    entityManager.merge(lesson);
  }
  
  /**Delete lesson by id from table in DB.
   * Return 1 if deleting was successful*/
  public void deleteLessonById(int id) {
    log.info("Delete Lesson with id {}.", id);
    Lesson lesson = entityManager.find(Lesson.class, id);
    if (lesson == null) {
      log.error("Lesson with id {} is not found.", id);
      throw new NoSuchElementException(String.format("Lesson with id %d is not found.", id));
    }
    entityManager.remove(lesson);
  }
}
