package com.foxminded.parashchuk.university.controller;

import com.foxminded.parashchuk.university.models.Student;
import com.foxminded.parashchuk.university.service.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(StudentController.class)
@AutoConfigureMockMvc
class StudentControllerTest {

  @MockBean
  private StudentService service;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  @InjectMocks
  private StudentController controller;


  @Test
  void showAllStudents_shouldTransferAllDataToTemplate_whenGetDataFromService() throws Exception {
    List<Student> expected = Arrays.asList(
            new Student(1, "Chris", "Martin", 1, "test@test.test"),
            new Student(2, "Mari", "Oswald", 2, "test@test.test"));
    when(service.getAllStudents()).thenReturn(expected);
    this.mockMvc.perform(get("/students/all"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(model().attribute("students",expected))
            .andExpect(view().name("all_students"));
  }

  @Test
  void findStudentById_shouldFindStudentAndRedirectItToEditPage_whenGetId() throws Exception {
    this.mockMvc.perform(post("/students/all")
                    .param("id", "1"))
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/students/1"));
  }

  @Test
  void studentEditForm_shouldShowEditFormForStudent_whenGetIdFromPath() throws Exception {
    Student student =  new Student(1, "Chris", "Martin", 1, "test@test.test");
    when(service.getStudentById(1)).thenReturn(student);
    this.mockMvc.perform(get("/students/1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(model().attribute("student", student))
            .andExpect(view().name("edit/student_edit"));
  }

  @Test
  void studentEditForm_shouldRedirectToMainStudentsPageWithDangerMessage_whenGetNotExistedId() throws Exception {
    when(service.getStudentById(3)).thenThrow(NoSuchElementException.class);
    this.mockMvc.perform(get("/students/3"))
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(flash().attribute("danger_message", "Student with id 3 does not exists."))
            .andExpect(redirectedUrl("/students/all"));
  }

  @Test
  void studentCreateForm_shouldShowFormForCreateNewStudent() throws Exception {
    this.mockMvc.perform(get("/students/new"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("create/student_new"));
  }

  @Test
  void studentEdit_shouldTransferDataToService_whenGetNeededParameters() throws Exception {
    this.mockMvc.perform(post("/students/1")
                    .param("firstName", "Updated")
                    .param("lastName", "Student")
                    .param("groupId", "2")
                    .param("email", "test@test.test"))
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(flash().attribute("success_message", "Student with id 1 was updated."))
            .andExpect(redirectedUrl("/students/all"));
  }

  @Test
  void studentEdit_shouldShowErrors_whenGetInValidParameters() throws Exception {
    this.mockMvc.perform(post("/students/1")
                    .param("firstName", "")
                    .param("lastName", "")
                    .param("groupId", "0")
                    .param("email", ""))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Firstname is mandatory")))
            .andExpect(content().string(containsString("Firstname size should be between 2 and 20")))
            .andExpect(content().string(containsString("Lastname is mandatory")))
            .andExpect(content().string(containsString("Lastname size should be between 2 and 20")))
            .andExpect(content().string(containsString("Please enter a valid email")))
            .andExpect(content().string(containsString("Email size should be greater then 5")))
            .andExpect(view().name("edit/student_edit"));
  }

  @Test
  void studentEdit_shouldThrowExceptionAndRedirectToMainStudentPage_whenGetNotExistedGroup() throws Exception {
    when(service.getStudentById(1)).thenReturn(new Student(1, "Chris", "Martin", 1, "test@test.test"));
    Student student = new Student(1, "Updated", "Student", 2, "test@test.test");

    doThrow(DataIntegrityViolationException.class).when(service).updateStudentById(student);

    this.mockMvc.perform(post("/students/1")
                    .param("firstName", "Updated")
                    .param("lastName", "Student")
                    .param("groupId", "2")
                    .param("email", "test@test.test"))
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(flash().attribute("danger_message", "Group with this id (2) does not exists."))
            .andExpect(redirectedUrl("/students/1"));
  }

  @Test
  void studentCreate_shouldTransferDataToService_whenGetNeededParameters() throws Exception {
    Student student = new Student(0, "Chris", "Martin", 1, "test@test.test");
    when(service.createStudent(student)).thenReturn(student);
    this.mockMvc.perform(post("/students/new")
                    .param("firstName", "Chris")
                    .param("lastName", "Martin")
                    .param("groupId", "1")
                    .param("email", "test@test.test"))
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(flash().attribute("success_message", "New student was created."))
            .andExpect(redirectedUrl("/students/all"));
  }

  @Test
  void studentCreate_shouldShowErrors_whenGetInValidParameters() throws Exception {
    this.mockMvc.perform(post("/students/new")
                    .param("firstName", "")
                    .param("lastName", "")
                    .param("groupId", "1")
                    .param("email", ""))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Firstname is mandatory")))
            .andExpect(content().string(containsString("Firstname size should be between 2 and 20")))
            .andExpect(content().string(containsString("Lastname is mandatory")))
            .andExpect(content().string(containsString("Lastname size should be between 2 and 20")))
            .andExpect(content().string(containsString("Please enter a valid email")))
            .andExpect(content().string(containsString("Email size should be greater then 5")))
            .andExpect(view().name("create/student_new"));
  }
  @Test
  void studentCreate_shouldThrowExceptionAndRedirectToNewStudentPage_whenGetNotExistedGroup() throws Exception {
    doThrow(DataIntegrityViolationException.class).when(service).createStudent(
            new Student(0, "Chris", "Martin", 1, "test@test.test"));
    this.mockMvc.perform(post("/students/new")
                    .param("firstName", "Chris")
                    .param("lastName", "Martin")
                    .param("groupId", "1")
                    .param("email", "test@test.test"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(model().attribute("danger_message", "Group with this id (1) does not exists."))
            .andExpect(view().name("create/student_new"));
  }

  @Test
  void deleteStudent_shouldRedirectToMainStudentsPageWithSuccessMessage_whenGetExistedId() throws Exception {
    this.mockMvc.perform(get("/students/delete/1"))
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(flash().attribute("success_message", "Student with id 1 was deleted."))
            .andExpect(redirectedUrl("/students/all"));
  }

  @Test
  void deleteStudent_shouldRedirectToMainStudentPageWithDangerMessage_whenGetNotExistedId() throws Exception {
    doThrow(EmptyResultDataAccessException.class).when(service).deleteStudentById(1);

    this.mockMvc.perform(get("/students/delete/1"))
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(flash().attribute("danger_message", "Student with id 1 does not exists."))
            .andExpect(redirectedUrl("/students/all"));
  }

}
