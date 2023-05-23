package com.foxminded.parashchuk.university.service;

import com.foxminded.parashchuk.university.dao.TeacherRepository;
import com.foxminded.parashchuk.university.models.Teacher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

/**Class contains requests for TeacherRepository class.*/
@Service
public class TeacherService {

  @Autowired
  private TeacherRepository dao;

  private static final Logger log = LoggerFactory.getLogger(TeacherService.class);


  /**Get all teachers from table in DB.*/
  public List<Teacher> getAllTeachers() {
    log.info("Get all data from Teachers table.");
    return dao.findAllByOrderById();
  }

  /**Save new teacher to table by Teacher object.*/
  public Teacher createTeacher(Teacher teacher) {
    if (teacher == null) {
      log.error("Teacher can not be a null");
      throw new IllegalArgumentException("Teacher can not be a null");
    } else {
      log.info("Create new Teacher with firstname {} and surname {}.",
              teacher.getFirstName(), teacher.getLastName());
      return dao.save(teacher);
    }
  }

  /**Get one teacher from table in DB by id.*/
  public Teacher getTeacherById(int id) {
    log.info("Get Teacher with id {}.", id);
    Teacher teacher = dao.findById(id).orElse(null);
    if (teacher == null) {
      log.error("Teacher with id {} is not found.", id);
      throw new NoSuchElementException(String.format("Teacher with id %d is not found.", id));
    }
    return teacher;
  }

  /**Update teacher by existing id in table.*/
  public Teacher updateTeacherById(Teacher teacher) {
    log.info("Update Teacher with id {}.", teacher.getId());
    Teacher checkedTeacher = getTeacherById(teacher.getId());
    if (checkedTeacher == null){
      log.error("Teacher with id {} is not found.", teacher.getId());
      throw new NoSuchElementException(String.format("Teacher with id %d is not found.", teacher.getId()));
    }
    return dao.save(teacher);
  }

  /**Delete teacher by id from table in DB.*/
  public void deleteTeacherById(int id) {
    log.info("Delete Teacher with id {}.", id);
    dao.deleteById(id);
  }
}
