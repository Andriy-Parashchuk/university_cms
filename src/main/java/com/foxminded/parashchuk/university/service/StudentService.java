package com.foxminded.parashchuk.university.service;

import com.foxminded.parashchuk.university.dao.StudentRepository;
import com.foxminded.parashchuk.university.models.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

/**Class contains requests for StudentRepository class.*/
@Service
public class StudentService {
  @Autowired
  private StudentRepository dao;

  private static final Logger log = LoggerFactory.getLogger(StudentService.class);


  /**Get all students from table in DB.*/
  public List<Student> getAllStudents() {
    log.info("Get all data from Student table.");
    return dao.findAllByOrderById();
  }

  /**Save new student to table by Student object.*/
  public Student createStudent(Student student) {
    if (student == null) {
      log.error("Student can not be a null");
      throw new IllegalArgumentException("Student can not be a null");
    } else {
      log.info("Create new Student with firstname {} and surname {}.",
              student.getFirstName(), student.getLastName());
      return dao.save(student);
    }
  }

  /**Get one student from table in DB by id.*/
  public Student getStudentById(int id) {
    log.info("Get Student with id {}.", id);
    Student student = dao.findById(id).orElse(null);
    if (student == null) {
      log.error("Student with id {} is not found.", id);
      throw new NoSuchElementException(String.format("Student with id %d is not found.", id));
    }
    return student;
  }

  /**Update student by existing id in table.*/
  public Student updateStudentById(String id, String firstName, String lastName, int groupId) {
    int studentId = Integer.parseInt(id);
    Student student = getStudentById(studentId);
    student.setFirstName(firstName);
    student.setLastName(lastName);
    student.setGroupId(groupId);
    log.info("Update Student with id {}.", student.getId());
    Student checkedStudent = getStudentById(student.getId());
    if (checkedStudent == null){
      log.error("Student with id {} is not found.", student.getId());
      throw new NoSuchElementException(String.format("Student with id %d is not found.", student.getId()));
    }
    return dao.save(student);
  }

  /**Delete student by id from table in DB.*/
  public void deleteStudentById(int id) {
    log.info("Delete Student with id {}.", id);
    dao.deleteById(id);
  }
}
