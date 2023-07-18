package com.foxminded.parashchuk.university.api;

import com.foxminded.parashchuk.university.dto.StudentDTO;
import com.foxminded.parashchuk.university.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**Class for connecting UI with Student model via REST.*/
@RestController
@RequestMapping("/api/students")
public class StudentApiController {

  @Autowired
  private StudentService service;
  private static final Logger log = LoggerFactory.getLogger(StudentApiController.class);

  /**Return all Students from database and transfer via REST.*/
  @GetMapping
  public List<StudentDTO> getAll(){
    log.info("All data from students was transfer to REST");
    return service.getAllStudents();
  }

  /**Return Student with id in path from database and show it to edit page.*/
  @GetMapping("/{studentId}")
  public StudentDTO getById(@PathVariable String studentId){
    log.info("Show edit form for student with id {} for REST api", studentId);
    return service.getStudentById(Integer.parseInt(studentId));
  }

  /**Get info from fields on the page for creating new Student.*/
  @PostMapping()
  @PreAuthorize("hasRole('university_admin') or hasRole('university_teacher')")
  public ResponseEntity<StudentDTO> create(@Valid @RequestBody StudentDTO studentDTO){
    StudentDTO savedStudent = service.createStudent(studentDTO);
    log.info("New student was created with firstname {}, lastname {}",
            savedStudent.getFirstName(), savedStudent.getLastName());
    return new ResponseEntity<>(savedStudent, HttpStatus.CREATED);
  }

  /**Get new info from fields on the page for edit existing Student.*/
  @PutMapping("/{studentId}")
  @PreAuthorize("hasRole('university_admin') or hasRole('university_teacher')")
  public ResponseEntity<StudentDTO> update(@PathVariable String studentId, @Valid @RequestBody StudentDTO studentDTO){
    studentDTO.setId(Integer.parseInt(studentId));
    StudentDTO updateStudent = service.updateStudentById(studentDTO);
    log.info("Student with id {} was updated via REST.", studentId);
    return ResponseEntity.ok(updateStudent);
  }

  /**Delete Student by existing id.*/
  @DeleteMapping("/{studentId}")
  @PreAuthorize("hasRole('university_admin')")
  public ResponseEntity<String> delete(@PathVariable String studentId){
    service.deleteStudentById(Integer.parseInt(studentId));
    log.info("Student with id {} was deleted via REST.", studentId);
    return ResponseEntity.noContent().build();
  }

  /**Handler for Validation Exception.*/
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Map<String, String> handleValidationExceptions(
          MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach(error -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    log.error("Something gone wrong {}", errors);
    return errors;
  }

  /**Handler for IntegrityViolation Exception.*/
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(DataIntegrityViolationException.class)
  public Map<String, String> handleIntegrityException() {
    Map<String, String> errors = new HashMap<>();
    errors.put("error", "This group does not exists for set reference for student.");
    log.error("Something gone wrong {}", errors);
    return errors;
  }

  /**Handler for Deleting and Not Exists Exceptions.*/
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({NoSuchElementException.class, EmptyResultDataAccessException.class})
  public Map<String, String> handleNoElementException() {
    Map<String, String> errors = new HashMap<>();
    errors.put("error", "This student does not exists.");
    log.error("Something gone wrong {}", errors);
    return errors;
  }

}
