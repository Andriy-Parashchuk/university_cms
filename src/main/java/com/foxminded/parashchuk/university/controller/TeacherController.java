package com.foxminded.parashchuk.university.controller;

import com.foxminded.parashchuk.university.models.Teacher;
import com.foxminded.parashchuk.university.service.TeacherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.NoSuchElementException;

/**Class for connecting UI with Teacher model.*/
@Controller
@RequestMapping("/teachers")
public class TeacherController {
  @Autowired
  private TeacherService service;
  private static final Logger log = LoggerFactory.getLogger(TeacherController.class);

  /**Return all Teacher from database and show them to UI.*/
  @GetMapping("/all")
  public String showAllTeachers(Model model){
    List<Teacher> teachers = service.getAllTeachers();
    model.addAttribute("teachers", teachers);
    log.info("All data from teachers was transfer to web-page");
    return "all_teachers";
  }

  /**Get id from field on the page and show edit page for this Teacher.*/
  @PostMapping("/all")
  public String findTeacherById(@RequestParam(defaultValue="0") int id, Model model){
    model.addAttribute("id", id);
    log.info("Request for finding teacher by id {}", id);
    return "redirect:/teachers/" + id;
  }

  /**Return Lesson with id in path from database and show it to edit page.*/
  @GetMapping("/{teacherId}")
  public String teacherEditForm(Model model, @PathVariable String teacherId, RedirectAttributes redirectAttributes){
    try{
      Teacher teacher = service.getTeacherById(Integer.parseInt(teacherId));
      log.info("Show edit form for teacher with id {}", teacherId);
      model.addAttribute("teacher", teacher);
      return "edit/teacher_edit";
    } catch (NoSuchElementException exception) {
      log.error("Teacher with id {} does not exists.", teacherId);
      redirectAttributes.addFlashAttribute("danger_message",
              "Teacher with id " + teacherId + " does not exists.");
      return "redirect:/teachers/all";
    }
  }

  /**Show page for creating new Teacher.*/
  @GetMapping("/new")
  public String teacherCreateForm(){
    log.info("Show form for add new teacher.");
    return "create/teacher_new";
  }

  /**Get new info from fields on the page for edit existing Teacher.*/
  @PostMapping("/{teacherId}")
  public String teacherEdit(@RequestParam String firstName, @RequestParam String lastName,
                            @RequestParam(defaultValue="0") int audience,
                            @RequestParam(defaultValue="") String department,
                            @PathVariable String teacherId, RedirectAttributes redirectAttributes){

    service.updateTeacherById(teacherId, firstName, lastName, audience, department);
    log.info("Teacher with id {} was updated.", teacherId);
    redirectAttributes.addFlashAttribute("success_message",
            "Teacher with id " + teacherId + " was updated.");
    return "redirect:/teachers/all";
  }

  /**Get info from fields on the page for creating new Teacher.*/
  @PostMapping("/new")
  public String teacherCreate(@RequestParam String firstName, @RequestParam String lastName,
                              @RequestParam(defaultValue="0") int audience,
                              @RequestParam(defaultValue="") String department,
                              RedirectAttributes redirectAttributes){
    Teacher teacher = new Teacher(0, firstName, lastName);
    teacher.setAudience(audience);
    teacher.setDepartment(department);
    service.createTeacher(teacher);
    log.info("New teacher was created");
    redirectAttributes.addFlashAttribute("success_message",
            "New teacher was created");
    return "redirect:/teachers/all";
  }


  /**Delete Teacher by existing id.*/
  @GetMapping("/delete/{teacherId}")
  public String deleteTeacher(@PathVariable String teacherId, RedirectAttributes redirectAttributes){
    try {
      service.deleteTeacherById(Integer.parseInt(teacherId));
      redirectAttributes.addFlashAttribute("success_message",
              "Teacher with id "+ teacherId + " was deleted.");
      log.info("Teacher with id {} was deleted.", teacherId);
      return "redirect:/teachers/all";
    } catch (NoSuchElementException e){
      log.error("Teacher with id {} does not exists.", teacherId);
      redirectAttributes.addFlashAttribute("danger_message",
              "Teacher with id " + teacherId + " does not exists.");
      return "redirect:/teachers/all";
    }
  }
}
