package com.foxminded.parashchuk.university.controller;

import com.foxminded.parashchuk.university.models.Student;
import com.foxminded.parashchuk.university.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.NoSuchElementException;

/**Class for connecting UI with Student model.*/
@Controller
@RequestMapping("/students")
public class StudentController {
  @Autowired
  private StudentService service;
  private static final Logger log = LoggerFactory.getLogger(StudentController.class);

  /**Return all Lessons from database and show them to UI.*/
  @GetMapping("/all")
  public String showAllStudents(Model model){
    List<Student> students = service.getAllStudents();
    model.addAttribute("students", students);
    log.info("All data from students was transfer to web-page");
    return "all_students";
  }

  /**Get id from field on the page and show edit page for this Lesson.*/
  @PostMapping("/all")
  public String findStudentById(@RequestParam(defaultValue="0") int id, Model model){
    model.addAttribute("id", id);
    log.info("Request for finding student by id {}", id);
    return "redirect:/students/" + id;
  }

  /**Return Lesson with id in path from database and show it to edit page.*/
  @GetMapping("/{studentId}")
  public String studentEditForm(Model model, @PathVariable String studentId, RedirectAttributes redirectAttributes){
    try{
      Student student = service.getStudentById(Integer.parseInt(studentId));
      log.info("Show edit form for student with id {}", studentId);
      model.addAttribute("student", student);
      return "edit/student_edit";
    } catch (NoSuchElementException exception) {
      log.error("Student with id {} does not exists.", studentId);
      redirectAttributes.addFlashAttribute("danger_message",
              "Student with id " + studentId + " does not exists.");
      return "redirect:/students/all";
    }
  }

  /**Get new info from fields on the page for edit existing Lesson.*/
  @PostMapping("/{studentId}")
  public String studentEdit(@RequestParam String firstName, @RequestParam String lastName,
                            @RequestParam(defaultValue="0") int groupId, @PathVariable String studentId,
                            RedirectAttributes redirectAttributes){
    try {
      Student student = service.getStudentById(Integer.parseInt(studentId));
      student.setFirstName(firstName);
      student.setLastName(lastName);
      student.setGroupId(groupId);
      service.updateStudentById(student, Integer.parseInt(studentId));
      log.info("Student with id {} was updated.", studentId);
      redirectAttributes.addFlashAttribute("success_message",
              "Student with id " + studentId + " was updated.");
    } catch (DataIntegrityViolationException e){
      log.error("Group with id {} does not exists for changing reference for student.", groupId);
      redirectAttributes.addFlashAttribute("danger_message",
              "Group with this id (" + groupId + ") does not exists.");
      return "redirect:/students/" + studentId;
    }
    return "redirect:/students/all";
  }


  /**Show page for creating new Lesson.*/
  @GetMapping("/new")
  public String studentCreateForm(){
    log.info("Show form for add new student.");
    return "create/student_new";
  }

  /**Get info from fields on the page for creating new Lesson.*/
  @PostMapping("/new")
  public String studentCreate(@RequestParam String firstName, @RequestParam String lastName,
                            @RequestParam(defaultValue="0") int groupId,
                            RedirectAttributes redirectAttributes){
    try{
      Student student = new Student(0, firstName, lastName, groupId);
      log.info("New student was created");
      service.createStudent(student);
      redirectAttributes.addFlashAttribute("success_message",
              "New student was created.");
    } catch (DataIntegrityViolationException e){
      log.error("Group with id {} does not exists for changing reference for student.", groupId);
      redirectAttributes.addFlashAttribute("danger_message",
              "Group with this id (" + groupId + ") does not exists.");
      return "redirect:/students/new";
    }
    return "redirect:/students/all";
  }

  /**Delete Lesson by existing id.*/
  @GetMapping("/delete/{studentId}")
  public String deleteStudent(Model model, @PathVariable String studentId, RedirectAttributes redirectAttributes){
    try {
      service.deleteStudentById(Integer.parseInt(studentId));
      redirectAttributes.addFlashAttribute("success_message",
              "Student with id " + studentId + " was deleted.");
      log.info("Student with id {} was deleted.", studentId);
      return "redirect:/students/all";
    } catch (NoSuchElementException e){
      log.error("Student with id {} does not exists.", studentId);
      redirectAttributes.addFlashAttribute("danger_message",
              "Student with id " + studentId + " does not exists.");
      return "redirect:/students/all";
    }
  }
}
