package com.foxminded.parashchuk.university.service;

import com.foxminded.parashchuk.university.dao.StudentRepository;
import com.foxminded.parashchuk.university.dto.StudentDTO;
import com.foxminded.parashchuk.university.models.Student;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**Class contains requests for StudentRepository class.*/
@Service
public class StudentService {
  private final StudentRepository dao;

  private final ModelMapper mapper;

  @Autowired
  public StudentService(ModelMapper mapper, StudentRepository dao){
    this.mapper = mapper;
    this.dao = dao;
    mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
  }

  private static final Logger log = LoggerFactory.getLogger(StudentService.class);


  /**Get all students from table in DB.*/
  public List<StudentDTO> getAllStudents() {
    log.info("Get all data from Student table.");
    return dao.findAllByOrderById().stream()
            .map(student -> mapper.map(student, StudentDTO.class))
            .collect(Collectors.toList());
  }

  /**Save new student to table by Student object.*/
  public StudentDTO createStudent(StudentDTO studentDTO) {
    if (studentDTO == null) {
      log.error("Student can not be a null");
      throw new IllegalArgumentException("Student can not be a null");
    } else {
      Student student = mapper.map(studentDTO, Student.class);
      log.info("Create new Student with firstname {} and surname {}.",
              student.getFirstName(), student.getLastName());
      return mapper.map(dao.save(student), StudentDTO.class);
    }
  }

  /**Get one student from table in DB by id.*/
  public StudentDTO getStudentById(int id) {

    log.info("Get Student with id {}.", id);
    Student student = dao.findById(id).orElse(null);
    if (student == null) {
      log.error("Student with id {} is not found.", id);
      throw new NoSuchElementException(String.format("Student with id %d is not found.", id));
    }
    return mapper.map(student, StudentDTO.class);
  }

  /**Update student by existing id in table.*/
  public StudentDTO updateStudentById(StudentDTO studentDTO) {
    log.info("Update Student with id {}.", studentDTO.getId());
    StudentDTO checkedStudent = getStudentById(studentDTO.getId());
    if (checkedStudent == null){
      log.error("Student with id {} is not found.", studentDTO.getId());
      throw new NoSuchElementException(String.format("Student with id %d is not found.", studentDTO.getId()));
    }
    return mapper.map(dao.save(mapper.map(studentDTO, Student.class)), StudentDTO.class);
  }

  /**Delete student by id from table in DB.*/
  public void deleteStudentById(int id) {
    log.info("Delete Student with id {}.", id);
    dao.deleteById(id);
  }
}
