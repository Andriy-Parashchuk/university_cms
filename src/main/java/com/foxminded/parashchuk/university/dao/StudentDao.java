package com.foxminded.parashchuk.university.dao;

import com.foxminded.parashchuk.university.models.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

/**Class with call to students table in DB. */
@Repository
@Transactional
public class StudentDao {
  private static final Logger log = LoggerFactory.getLogger(StudentDao.class);

  @PersistenceContext
  private EntityManager entityManager;
  
  /**Get all students from table in DB.*/
  public List<Student> getAllStudents() {
    log.info("Get all data from Students table.");
    return entityManager.createQuery("SELECT s FROM Student s order by s.id", Student.class).getResultList();
  }
  
  /**Save new student to table by Student object.*/
  public Student createStudent(Student student) {
    if (student == null) {
      log.error("Student can not be a null");
      throw new IllegalArgumentException("Student can not be a null");
    } else {
      log.info("Create new Student with firstname {} and surname {}.", student.getFirstName(), student.getLastName());
      entityManager.persist(student);
      return student;
    }
  }
  
  /**Get one student from table in DB by id.*/
  public Student getStudentById(int id) {
    log.info("Get Student with id {}.", id);
    Student student = entityManager.find(Student.class, id);
    if (student == null) {
      log.error("Student with id {} not found.", id);
      throw new NoSuchElementException(String.format("Student with id %d is not found.", id));
    }
    return student;
  }
  
  /**Update student by existing id in table and student object for overwriting.*/
  public Student updateStudentById(Student student) {
    log.info("Update Student with id {}.", student.getId());
    Student checkedStudent = entityManager.find(Student.class, student.getId());
    if (checkedStudent == null) {
      log.error("Student with id {} is not found.", student.getId());
      throw new NoSuchElementException(String.format("Student with id %d is not found.", student.getId()));
    }
    return entityManager.merge(student);
  }
  
  /**Delete student by id from table in DB.*/
  public void deleteStudentById(int id) {
    log.info("Delete Student with id {}.", id);
    Student student = entityManager.find(Student.class, id);
    if (student == null) {
      log.error("Student with id {} is not found.", id);
      throw new NoSuchElementException(String.format("Student with id %d is not found.", id));
    }
    entityManager.remove(student);
  }
}
