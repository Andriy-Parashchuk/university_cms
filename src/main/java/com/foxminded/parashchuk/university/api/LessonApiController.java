package com.foxminded.parashchuk.university.api;

import com.foxminded.parashchuk.university.dto.LessonDTO;
import com.foxminded.parashchuk.university.service.LessonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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

/**Class for connecting UI with Lesson model via REST api.*/
@RestController
@RequestMapping("/lessons_api")
public class LessonApiController {

  @Autowired
  private LessonService service;

  private static final Logger log = LoggerFactory.getLogger(LessonApiController.class);

  /**Return all Students from database and transfer via REST.*/
  @GetMapping("/all")
  public List<LessonDTO> showAllLessons(){
    log.info("All data from lessons was transfer to REST");
    return service.getAllLessons();
  }


  /**Get id from field on the page and show edit page for this Lesson.*/
  @PostMapping("/all")
  public LessonDTO findLessonById(@RequestParam(defaultValue = "0") int id){
    log.info("Request for finding lesson by id {} for REST api", id);
    return service.getLessonById(id);
  }

  /**Get info from fields on the page for creating new Lesson.*/
  @PostMapping("/new")
  public ResponseEntity<String> lessonCreate(@Valid @RequestBody LessonDTO lessonDTO){
    LessonDTO savedLesson = service.createLesson(lessonDTO);
    log.info("New lesson was created with name {}", savedLesson.getName());
    return ResponseEntity.ok("New lesson was created successfully.");
  }

  /**Return Lesson with id in path from database and show it to edit page.*/
  @GetMapping("/{lessonId}")
  public LessonDTO studentEditForm(@PathVariable String lessonId){
    log.info("Show edit form for student with id {} for REST api", lessonId);
    return service.getLessonById(Integer.parseInt(lessonId));
  }

  /**Get new info from fields on the page for update existing Lesson.*/
  @PutMapping("/{lessonId}")
  public ResponseEntity<String> lessonUpdate(@PathVariable String lessonId, @Valid @RequestBody LessonDTO lessonDTO){
    lessonDTO.setId(Integer.parseInt(lessonId));
    service.updateLessonById(lessonDTO);
    log.info("Lesson with id {} was updated via REST.", lessonId);
    return ResponseEntity.ok("Lesson was updated successfully.");
  }

  /**Delete Lesson by existing id.*/
  @DeleteMapping("/{lessonId}")
  public ResponseEntity<String> deleteLesson(@PathVariable String lessonId){
      service.deleteLessonById(Integer.parseInt(lessonId));
      log.info("Lesson with id {} was deleted via REST.", lessonId);
      return ResponseEntity.ok("Lesson was deleted successfully.");
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
  @ExceptionHandler({EmptyResultDataAccessException.class, NoSuchElementException.class})
  public Map<String, String> handleDeleteException() {
    Map<String, String> errors = new HashMap<>();
    errors.put("error", "This lesson does not exists.");
    log.error("Something gone wrong {}", errors);
    return errors;
  }

  /**Handler for IntegrityViolation Exception.*/
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(DataIntegrityViolationException.class)
  public Map<String, String> handleIntegrityException() {
    Map<String, String> errors = new HashMap<>();
    errors.put("error", "This group or teacher does not exists for set reference for lesson.");
    log.error("Something gone wrong {}", errors);
    return errors;
  }
}
