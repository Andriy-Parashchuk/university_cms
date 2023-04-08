package com.foxminded.parashchuk.university.controller;

import com.foxminded.parashchuk.university.models.Lesson;
import com.foxminded.parashchuk.university.service.LessonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

/**Class for connecting UI with Lesson model.*/

@Controller
@RequestMapping("/lessons")
public class LessonController {
  @Autowired
  private LessonService service;
  private static final Logger log = LoggerFactory.getLogger(LessonController.class);

  /**Return all Lessons from database and show them to UI.*/
  @GetMapping("/all")
  public String getAllLessons(Model model){
    List<Lesson> lessons = service.getAllLessons();
    model.addAttribute("lessons", lessons);
    log.info("All data from lessons was transfer to web-page");
    return "all_lessons";
  }

  /**Get id from field on the page and show edit page for this Lesson.*/
  @PostMapping("/all")
  public String findLessonById(@RequestParam(defaultValue="0") int id, Model model){
    model.addAttribute("id", id);
    log.info("Request for finding lesson by id {}", id);
    return "redirect:/lessons/" + id;
  }

  /**Return Lesson with id in path from database and show it to edit page.*/
  @GetMapping("/{lessonId}")
  public String lessonEditForm(Model model, @PathVariable String lessonId, RedirectAttributes redirectAttributes){
    try{
      Lesson lesson = service.getLessonById(Integer.parseInt(lessonId));
      log.info("Show edit form for lesson with id {}", lessonId);
      model.addAttribute("lesson", lesson);
      return "edit/lesson_edit";
    } catch (NoSuchElementException exception) {
      log.error("Lesson with id {} does not exists.", lessonId);
      redirectAttributes.addFlashAttribute("danger_message",
              "Lesson with id " + lessonId + " does not exists.");
      return "redirect:/lessons/all";
    }
  }

  /**Get new info from fields on the page for edit existing Lesson.*/
  @PostMapping("/{lessonId}")
  public String lessonEdit(@RequestParam String name, @RequestParam(defaultValue="0") int teacherId,
                           @RequestParam(defaultValue="0") int groupId,
                           @RequestParam(defaultValue="0") int audience,
                           @RequestParam String time, @PathVariable String lessonId,
                           RedirectAttributes redirectAttributes){
    try{
      service.updateLessonById(lessonId, name, teacherId, groupId, time, audience);
      log.info("Lesson with id {} was updated.", lessonId);
      redirectAttributes.addFlashAttribute("success_message",
              "Lesson with id " + lessonId + " was updated.");
      return "redirect:/lessons/all";
    } catch (DataIntegrityViolationException e){
      log.error("Group with id {} or teacher with id {} does not exists for changing reference for lesson.",
              groupId, teacherId);
      redirectAttributes.addFlashAttribute("danger_message",
              "Group with this id (" + groupId + ") or teacher with id (" + teacherId +
                      ") does not exists.");
      return "redirect:/lessons/" + lessonId;
    }

  }

  /**Show page for creating new Lesson.*/
  @GetMapping("/new")
  public String lessonCreateForm(Model model){
    log.info("Show form for add new lesson.");
    return "create/lesson_new";
  }

  /**Get info from fields on the page for creating new Lesson.*/
  @PostMapping("/new")
  public String lessonCreate(@RequestParam String name, @RequestParam(defaultValue="0") int teacherId,
                             @RequestParam(defaultValue="0") int groupId,
                             @RequestParam(defaultValue="0") int audience,
                             @RequestParam String time, RedirectAttributes redirectAttributes){
    try{
      Lesson lesson = new Lesson(0, name, teacherId, groupId, LocalDateTime.parse(time), audience);
      service.createLesson(lesson);
      log.info("New lesson was created");
      redirectAttributes.addFlashAttribute("success_message",
              "New lesson was created.");
    } catch (DataIntegrityViolationException e){
      log.error("Group with id {} or teacher with id {} does not exists for changing reference for lesson.",
              groupId, teacherId);
      redirectAttributes.addFlashAttribute("danger_message",
              "Group with this id (" + groupId + ") or teacher with id (" + teacherId +
                      ") does not exists.");
      return "redirect:/lessons/new";
    }
    return "redirect:/lessons/all";
  }

  /**Delete Lesson by existing id.*/
  @GetMapping("/delete/{lessonId}")
  public String deleteLesson(@PathVariable String lessonId, RedirectAttributes redirectAttributes){
    try {
      service.deleteLessonById(Integer.parseInt(lessonId));
      redirectAttributes.addFlashAttribute("success_message",
              "Lesson with id " + lessonId + " was deleted.");
      log.info("Lesson with id {} was deleted.", lessonId);
      return "redirect:/lessons/all";
    } catch (NoSuchElementException e){
      log.error("Lesson with id {} does not exists.", lessonId);
      redirectAttributes.addFlashAttribute("danger_message",
              "Lesson with id " + lessonId + " does not exists.");
      return "redirect:/lessons/all";
    }
  }
}
