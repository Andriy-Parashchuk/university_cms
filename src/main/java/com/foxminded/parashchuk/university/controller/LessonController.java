package com.foxminded.parashchuk.university.controller;

import com.foxminded.parashchuk.university.dto.LessonDTO;
import com.foxminded.parashchuk.university.service.LessonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
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
    List<LessonDTO> lessons = service.getAllLessons();
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
      LessonDTO lessonDTO = service.getLessonById(Integer.parseInt(lessonId));
      log.info("Show edit form for lesson with id {}", lessonId);
      model.addAttribute("lesson", lessonDTO);
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
  public String lessonEdit(@Valid LessonDTO lessonDTO, BindingResult bindingResult, @PathVariable String lessonId,
                           Model model, RedirectAttributes redirectAttributes){
    lessonDTO.setId(Integer.parseInt(lessonId));
    model.addAttribute("lesson", lessonDTO);
    if (bindingResult.hasErrors()){
      Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
      model.mergeAttributes(errorsMap);
      log.error("Some fields get errors: {}", bindingResult.getModel());
      return "edit/lesson_edit";
    }
    try{
      service.updateLessonById(lessonDTO);
      log.info("Lesson with id {} was updated.", lessonId);
      redirectAttributes.addFlashAttribute("success_message",
              "Lesson with id " + lessonId + " was updated.");
      return "redirect:/lessons/all";
    } catch (DataIntegrityViolationException e){
      log.error("Group with id {} or teacher with id {} does not exists for changing reference for lesson.",
              lessonDTO.getGroupId(), lessonDTO.getTeacherId());
      redirectAttributes.addFlashAttribute("danger_message",
              "Group with this id (" + lessonDTO.getGroupId() + ") or teacher with id (" +
                      lessonDTO.getTeacherId() + ") does not exists.");
      return "redirect:/lessons/" + lessonId;
    }

  }

  /**Show page for creating new Lesson.*/
  @GetMapping("/new")
  public String lessonCreateForm(Model model){
    model.addAttribute("lesson", new LessonDTO());
    log.info("Show form for add new lesson.");
    return "create/lesson_new";
  }

  /**Get info from fields on the page for creating new Lesson.*/
  @PostMapping("/new")
  public String lessonCreate(@Valid LessonDTO lessonDTO, BindingResult bindingResult, Model model,
                             RedirectAttributes redirectAttributes){
    model.addAttribute("lesson", lessonDTO);
    if (bindingResult.hasErrors()) {
      Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
      model.mergeAttributes(errorsMap);
      log.error("Some fields get errors: {}", bindingResult.getModel());
      return "create/lesson_new";
    }
    try{
      LessonDTO savedLesson = service.createLesson(lessonDTO);
      log.info("New lesson was created with name {}", savedLesson.getName());
      redirectAttributes.addFlashAttribute("success_message",
              "New lesson was created.");
    } catch (DataIntegrityViolationException e){
      log.error("Group with id {} or teacher with id {} does not exists for changing reference for lesson.",
              lessonDTO.getGroupId(), lessonDTO.getTeacherId());
      model.addAttribute("danger_message",
              "Group with this id (" + lessonDTO.getGroupId() + ") or teacher with id (" +
                      lessonDTO.getTeacherId() + ") does not exists.");
      return "create/lesson_new";
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
    } catch (EmptyResultDataAccessException e){
      log.error("Lesson with id {} does not exists.", lessonId);
      redirectAttributes.addFlashAttribute("danger_message",
              "Lesson with id " + lessonId + " does not exists.");
      return "redirect:/lessons/all";
    }
  }
}
