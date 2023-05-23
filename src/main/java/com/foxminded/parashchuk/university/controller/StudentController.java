package com.foxminded.parashchuk.university.controller;

import com.foxminded.parashchuk.university.models.Student;
import com.foxminded.parashchuk.university.service.StudentService;
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
  public String studentEdit(@PathVariable String studentId, @Valid Student student, BindingResult bindingResult,
                            RedirectAttributes redirectAttributes){
    if (bindingResult.hasErrors()){
      log.error("Some fields get errors: {}", bindingResult.getModel());
      return "edit/student_edit";
    }
    try {
      student.setId(Integer.parseInt(studentId));
      service.updateStudentById(student);
      log.info("Student with id {} was updated.", studentId);
      redirectAttributes.addFlashAttribute("success_message",
              "Student with id " + studentId + " was updated.");
    } catch (DataIntegrityViolationException e){
      log.error("Group with id {} does not exists for changing reference for student.", student.getGroupId());
      redirectAttributes.addFlashAttribute("danger_message",
              "Group with this id (" + student.getGroupId() + ") does not exists.");
      return "redirect:/students/" + studentId;
    }
    return "redirect:/students/all";
  }


  /**Show page for creating new Lesson.*/
  @GetMapping("/new")
  public String studentCreateForm(Student student){
    log.info("Show form for add new student.");
    return "create/student_new";
  }

  /**Get info from fields on the page for creating new Lesson.*/
  @PostMapping("/new")
  public String studentCreate(@Valid Student student, BindingResult bindingResult,
                            Model model,
                            RedirectAttributes redirectAttributes){
    if (bindingResult.hasErrors()){
      log.error("Some fields get errors: {}", bindingResult.getModel());
      return "create/student_new";
    }
    try{
      Student savedStudent = service.createStudent(student);
      log.info("New student was created with name {} {}", savedStudent.getFirstName(), savedStudent.getLastName());
      redirectAttributes.addFlashAttribute("success_message",
              "New student was created.");
    } catch (DataIntegrityViolationException e){
      log.error("Group with id {} does not exists for changing reference for student.", student.getGroupId());
      model.addAttribute("danger_message",
              "Group with this id (" + student.getGroupId() + ") does not exists.");
      return "create/student_new";
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
    } catch (EmptyResultDataAccessException e){
      log.error("Student with id {} does not exists.", studentId);
      redirectAttributes.addFlashAttribute("danger_message",
              "Student with id " + studentId + " does not exists.");
      return "redirect:/students/all";
    }
  }
}
