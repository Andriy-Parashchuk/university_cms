package com.foxminded.parashchuk.university.dao;

import com.foxminded.parashchuk.university.models.Teacher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

/**Class with call to teachers table in DB. */
@Repository
@Transactional
public class TeacherDao {
  private static final Logger log = LoggerFactory.getLogger(TeacherDao.class);

  @PersistenceContext
  private EntityManager entityManager;
  
  /**Get all teachers from table in DB.*/
  public List<Teacher> getAllTeachers() {
    log.info("Get all data from Teachers table.");
    return entityManager.createQuery("SELECT t FROM Teacher t order by t.id", Teacher.class).getResultList();
  }
  
  /**Save new teacher to table by Teacher object.*/
  public Teacher createTeacher(Teacher teacher) {
    if (teacher == null) {
      log.error("Teacher can not be a null");
      throw new IllegalArgumentException("Teacher can not be a null");
    } else {
      log.info("Create new Teacher with firstname {} and surname {}.", teacher.getFirstName(), teacher.getLastName());
      return entityManager.merge(teacher);
    }
  }
  
  /**Get one teacher from table in DB by id.*/
  public Teacher getTeacherById(int id) {
    log.info("Get Teacher with id {}.", id);
    Teacher teacher = entityManager.find(Teacher.class, id);
    if (teacher == null){
      log.error("Teacher with id {} is not found.", id);
      throw new NoSuchElementException(String.format("Teacher with id %d is not found.", id));
    }
    return teacher;
  }
  
  /**Update teacher by existing id in table and teacher object for overwriting.*/
  public Teacher updateTeacherById(Teacher teacher) {
    log.info("Update Teacher with id {}.", teacher.getId());
    Teacher checkedTeacher = entityManager.find(Teacher.class, teacher.getId());
    if (checkedTeacher == null){
      log.error("Teacher with id {} is not found.", teacher.getId());
      throw new NoSuchElementException(String.format("Teacher with id %d is not found.", teacher.getId()));
    }
    return entityManager.merge(teacher);
  }
  
  /**Delete teacher by id from table in DB.*/
  public void deleteTeacherById(int id) {
    log.info("Delete Teacher with id {}.", id);
    Teacher teacher = entityManager.find(Teacher.class, id);
    if (teacher == null){
      log.error("Teacher with id {} is not found.", id);
      throw new NoSuchElementException(String.format("Teacher with id %d is not found.", id));
    }
    entityManager.remove(teacher);
  }
}
