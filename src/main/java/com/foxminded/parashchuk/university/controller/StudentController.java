package com.foxminded.parashchuk.university.controller;

import com.foxminded.parashchuk.university.dto.StudentDTO;
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
import java.util.Map;
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
    List<StudentDTO> students = service.getAllStudents();
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
      StudentDTO studentDTO = service.getStudentById(Integer.parseInt(studentId));
      log.info("Show edit form for student with id {}", studentId);
      model.addAttribute("student", studentDTO);
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
  public String studentEdit(@PathVariable String studentId, @Valid StudentDTO studentDTO, BindingResult bindingResult,
                            Model model, RedirectAttributes redirectAttributes){
    studentDTO.setId(Integer.parseInt(studentId));
    model.addAttribute("student", studentDTO);
    if (bindingResult.hasErrors()){
      Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
      model.mergeAttributes(errorsMap);
      log.error("Some fields get errors: {}", bindingResult.getModel());
      return "edit/student_edit";
    }
    try {
      studentDTO.setId(Integer.parseInt(studentId));
      service.updateStudentById(studentDTO);
      log.info("Student with id {} was updated.", studentId);
      redirectAttributes.addFlashAttribute("success_message",
              "Student with id " + studentId + " was updated.");
    } catch (DataIntegrityViolationException e){
      log.error("Group with id {} does not exists for changing reference for student.", studentDTO.getGroupId());
      redirectAttributes.addFlashAttribute("danger_message",
              "Group with this id (" + studentDTO.getGroupId() + ") does not exists.");
      return "redirect:/students/" + studentId;
    }
    return "redirect:/students/all";
  }


  /**Show page for creating new Lesson.*/
  @GetMapping("/new")
  public String studentCreateForm(Model model){
    model.addAttribute("student", new StudentDTO());
    log.info("Show form for add new student.");
    return "create/student_new";
  }

  /**Get info from fields on the page for creating new Lesson.*/
  @PostMapping("/new")
  public String studentCreate(@Valid StudentDTO studentDTO, BindingResult bindingResult,
                            Model model,
                            RedirectAttributes redirectAttributes){
    model.addAttribute("student", studentDTO);
    if (bindingResult.hasErrors()){
      Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
      model.mergeAttributes(errorsMap);
      log.error("Some fields get errors: {}", bindingResult.getModel());
      return "create/student_new";
    }
    try{
      StudentDTO savedStudent = service.createStudent(studentDTO);
      log.info("New student was created with name {} {}", savedStudent.getFirstName(), savedStudent.getLastName());
      redirectAttributes.addFlashAttribute("success_message",
              "New student was created.");
    } catch (DataIntegrityViolationException e){
      log.error("Group with id {} does not exists for changing reference for student.", studentDTO.getGroupId());
      model.addAttribute("danger_message",
              "Group with this id (" + studentDTO.getGroupId() + ") does not exists.");
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
