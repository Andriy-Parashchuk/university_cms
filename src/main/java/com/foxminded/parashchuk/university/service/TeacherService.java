package com.foxminded.parashchuk.university.service;

import com.foxminded.parashchuk.university.dao.TeacherRepository;
import com.foxminded.parashchuk.university.dto.TeacherDTO;
import com.foxminded.parashchuk.university.models.Teacher;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**Class contains requests for TeacherRepository class.*/
@Service
public class TeacherService {

  @Autowired
  private TeacherRepository dao;
  @Autowired
  private ModelMapper mapper;

  private static final Logger log = LoggerFactory.getLogger(TeacherService.class);


  /**Get all teachers from table in DB.*/
  public List<TeacherDTO> getAllTeachers() {
    log.info("Get all data from Teachers table.");
    return dao.findAllByOrderById().stream()
            .map(teacher -> mapper.map(teacher, TeacherDTO.class))
            .collect(Collectors.toList());
  }

  /**Save new teacher to table by Teacher object.*/
  public TeacherDTO createTeacher(TeacherDTO teacherDTO) {
    if (teacherDTO == null) {
      log.error("Teacher can not be a null");
      throw new IllegalArgumentException("Teacher can not be a null");
    } else {
      Teacher teacher = mapper.map(teacherDTO, Teacher.class);
      log.info("Create new Teacher with firstname {} and surname {}.",
              teacher.getFirstName(), teacher.getLastName());
      return mapper.map(dao.save(teacher), TeacherDTO.class);
    }
  }

  /**Get one teacher from table in DB by id.*/
  public TeacherDTO getTeacherById(int id) {
    log.info("Get Teacher with id {}.", id);
    Teacher teacher = dao.findById(id).orElse(null);
    if (teacher == null) {
      log.error("Teacher with id {} is not found.", id);
      throw new NoSuchElementException(String.format("Teacher with id %d is not found.", id));
    }
    return mapper.map(teacher, TeacherDTO.class);
  }

  /**Update teacher by existing id in table.*/
  public TeacherDTO updateTeacherById(TeacherDTO teacher) {
    log.info("Update Teacher with id {}.", teacher.getId());
    TeacherDTO checkedTeacher = getTeacherById(teacher.getId());
    if (checkedTeacher == null){
      log.error("Teacher with id {} is not found.", teacher.getId());
      throw new NoSuchElementException(String.format("Teacher with id %d is not found.", teacher.getId()));
    }
    return mapper.map(dao.save(mapper.map(teacher, Teacher.class)), TeacherDTO.class);
  }

  /**Delete teacher by id from table in DB.*/
  public void deleteTeacherById(int id) {
    log.info("Delete Teacher with id {}.", id);
    dao.deleteById(id);
  }
}
