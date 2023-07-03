package com.foxminded.parashchuk.university.api;


import com.foxminded.parashchuk.university.dto.LessonDTO;
import com.foxminded.parashchuk.university.exceptions.LessonsNotFoundExceptions;
import com.foxminded.parashchuk.university.service.ScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

/**Class for connecting UI with Schedule model via REST api.*/
@RestController
@RequestMapping("/api/schedule")
public class ScheduleApiController {

  @Autowired
  ScheduleService service;

  private static final Logger log = LoggerFactory.getLogger(ScheduleApiController.class);

  /**Get data from fields and show all lessons for group or teacher by day/month.*/
  @PostMapping()
  public List<LessonDTO> getChosenSchedule(@RequestParam String type, @RequestParam(defaultValue = "0") int id,
                                           @RequestParam String period, @RequestParam String time)
          throws LessonsNotFoundExceptions {
    LocalDate date = LocalDateTime.parse(time).toLocalDate();

    if (Objects.equals(type, "student")) {
      if (Objects.equals(period, "day")) {
        return service.getLessonsStudentDay(id, date);
      } else {
        return service.getLessonsStudentMonth(id, date);
      }
    } else {
      if (Objects.equals(period, "day")) {
        return service.getLessonsTeacherDay(id, date);
      } else {
        return service.getLessonsTeacherMonth(id, date);
      }
    }
  }

  /**Handler for Time Parse Exception.*/
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(DateTimeParseException.class)
  public Map<String, String> handleDateParseExceptions() {
    Map<String, String> errors = new HashMap<>();
    errors.put("error", "Please choose date correctly.");
    log.error("Something gone wrong {}", errors);
    return errors;
  }

  /**Handler for Not Exists Lessons Exceptions.*/
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(LessonsNotFoundExceptions.class)
  public Map<String, String> handleLessonException() {
    Map<String, String> errors = new HashMap<>();
    errors.put("error", "Lessons for this user for this date are not found.");
    log.error("Something gone wrong {}", errors);
    return errors;
  }


  /**Handler for Not Existing User Exception.*/
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(NoSuchElementException.class)
  public Map<String, String> handleNoElementException() {
    Map<String, String> errors = new HashMap<>();
    errors.put("error", "User with this id does not exist.");
    log.error("Something gone wrong {}", errors);
    return errors;
  }

}
