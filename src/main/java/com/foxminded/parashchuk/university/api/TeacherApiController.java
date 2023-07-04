package com.foxminded.parashchuk.university.api;

import com.foxminded.parashchuk.university.dto.TeacherDTO;
import com.foxminded.parashchuk.university.service.TeacherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;


/**Class REST api for Teacher model.*/
@RestController
@RequestMapping("/api/teachers")
public class TeacherApiController {

  @Autowired
  private TeacherService service;
  private static final Logger log = LoggerFactory.getLogger(TeacherApiController.class);


  /**Return all Teacher from database and show them to UI.*/
  @GetMapping
  public List<TeacherDTO> getAll(){
    log.info("All data from teachers was transfer to REST api");
    return service.getAllTeachers();
  }

  /**Return Teacher with id in path from database and show it to edit page.*/
  @GetMapping("/{teacherId}")
  public TeacherDTO getById(@PathVariable String teacherId){
    log.info("Show edit form for teacher with id {} for REST api", teacherId);
    return service.getTeacherById(Integer.parseInt(teacherId));
  }

  /**Get info from fields on the page for creating new Teacher.*/
  @PostMapping
  public ResponseEntity<TeacherDTO> create(@Valid @RequestBody TeacherDTO teacherDTO){
    TeacherDTO savedTeacher = service.createTeacher(teacherDTO);
    log.info("New teacher was created with firstname {}, lastname {}",
            savedTeacher.getFirstName(), savedTeacher.getLastName());
    return new ResponseEntity<>(savedTeacher, HttpStatus.CREATED);
  }

  /**Get new info from fields on the page for edit existing Teacher.*/
  @PutMapping("/{teacherId}")
  public ResponseEntity<TeacherDTO> update(@PathVariable String teacherId, @Valid @RequestBody TeacherDTO teacherDTO){
    teacherDTO.setId(Integer.parseInt(teacherId));
    TeacherDTO updatedTeacher = service.updateTeacherById(teacherDTO);
    log.info("Teacher with id {} was updated via REST.", teacherId);
    return ResponseEntity.ok(updatedTeacher);
  }

  /**Delete Teacher by existing id.*/
  @DeleteMapping("/{teacherId}")
  public ResponseEntity<String> delete(@PathVariable String teacherId){
    service.deleteTeacherById(Integer.parseInt(teacherId));
    log.info("Teacher with id {} was deleted via REST.", teacherId);
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

  /**Handler for Deleting and Not Exists Exceptions.*/
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({NoSuchElementException.class, EmptyResultDataAccessException.class})
  public Map<String, String> handleNoElementException() {
    Map<String, String> errors = new HashMap<>();
    errors.put("error", "This teacher does not exists.");
    log.error("Something gone wrong {}", errors);
    return errors;
  }

}
