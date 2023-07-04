package com.foxminded.parashchuk.university.controller;

import com.foxminded.parashchuk.university.dto.LessonDTO;
import com.foxminded.parashchuk.university.exceptions.LessonsNotFoundExceptions;
import com.foxminded.parashchuk.university.service.ScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;


/**Class for connecting UI with Schedule model.*/
@Controller
@RequestMapping("/schedule")
public class ScheduleController {
  @Autowired
  private ScheduleService service;
  private static final Logger log = LoggerFactory.getLogger(ScheduleController.class);

  /**Show form for finding schedule.*/
  @GetMapping("/")
  public String showScheduleForm(){
    log.info("Open page with choose schedule");
    return "schedule_form";
  }

  /**Get data from fields and show all lessons for group or teacher by day/month.*/
  @PostMapping("/")
  public String getChosenSchedule(@RequestParam String type, @RequestParam(defaultValue="0") int id,
                                  @RequestParam String period, @RequestParam String time,
                                  Model model, RedirectAttributes redirectAttributes) {
    List<LessonDTO> lessons;
    LocalDate date;
    try{
      date = LocalDateTime.parse(time).toLocalDate();
    } catch (DateTimeParseException e) {
      redirectAttributes.addFlashAttribute("danger_message",
              "Please choose date correctly.");
      return "redirect:/schedule/";
    }
    try {
      if (Objects.equals(type, "student")) {
        if (Objects.equals(period, "day")) {
          lessons = service.getLessonsStudentDay(id, date);
          model.addAttribute("info", "student for day");
        } else {
          lessons = service.getLessonsStudentMonth(id, date);
          model.addAttribute("info", "student for month");
        }
      } else {
        if (Objects.equals(period, "day")) {
          lessons = service.getLessonsTeacherDay(id, date);
          model.addAttribute("info", "teacher for day");
        } else {
          lessons = service.getLessonsTeacherMonth(id, date);
          model.addAttribute("info", "teacher for month");
        }
      }
      model.addAttribute("lessons", lessons);
      return "all_lessons";
    } catch (LessonsNotFoundExceptions e) {
      redirectAttributes.addFlashAttribute("danger_message",
              "Lessons for this user on " + date + " not found.");
      return "redirect:/schedule/";
    } catch (NoSuchElementException exception) {
      redirectAttributes.addFlashAttribute("danger_message",
              "Student or teacher with id " + id + " does not exists.");
      return "redirect:/schedule/";
    }
  }
}
