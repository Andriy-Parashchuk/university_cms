package com.foxminded.parashchuk.university.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.parashchuk.university.dto.StudentDTO;
import com.foxminded.parashchuk.university.service.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(StudentApiController.class)
class StudentApiControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private StudentService service;

  @Autowired
  @InjectMocks
  private StudentApiController controller;


  private final StudentDTO firstStudent = new StudentDTO(
          1, "first", "student", 1,"test@test.test");
  private final StudentDTO secondStudent = new StudentDTO(
          2, "second", "student", 1, "test@test.test");

  private final List<StudentDTO> students = Arrays.asList(firstStudent, secondStudent);


  @Test
  void getAllStudents_shouldReturnAllData_whenDbIsNotEmpty() throws Exception {
    when(service.getAllStudents()).thenReturn(students);
    this.mockMvc.perform(get("/students_api/all"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[0].firstName", is("first")))
            .andExpect(jsonPath("$[0].lastName", is("student")))
            .andExpect(jsonPath("$[0].groupId", is(1)))
            .andExpect(jsonPath("$[0].email", is("test@test.test")))
            .andExpect(jsonPath("$[1].id", is(2)))
            .andExpect(jsonPath("$[1].firstName", is("second")))
            .andExpect(jsonPath("$[1].lastName", is("student")))
            .andExpect(jsonPath("$[1].groupId", is(1)))
            .andExpect(jsonPath("$[1].email", is("test@test.test")))
            .andDo(print());

    verify(service, times(1)).getAllStudents();
  }


  @Test
  void findStudentById_shouldReturnStudent_whenStudentIsExists() throws Exception {
    when(service.getStudentById(1)).thenReturn(firstStudent);
    this.mockMvc.perform(post("/students_api/all")
                    .param("id", "1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.firstName", is("first")))
            .andExpect(jsonPath("$.lastName", is("student")))
            .andExpect(jsonPath("$.groupId", is(1)))
            .andExpect(jsonPath("$.email", is("test@test.test")))
            .andDo(print());

    verify(service, times(1)).getStudentById(1);
  }


  @Test
  void findStudentById_shouldReturnError_whenStudentDoesNotExists() throws Exception {
    when(service.getStudentById(1)).thenThrow(NoSuchElementException.class);
    this.mockMvc.perform(post("/students_api/all")
                    .param("id", "1"))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.error", is("This student does not exists.")))
            .andDo(print());

    verify(service, times(1)).getStudentById(1);
  }

  @Test
  void studentEditForm_shouldReturnStudent_whenStudentIsExists() throws Exception {
    when(service.getStudentById(1)).thenReturn(firstStudent);
    this.mockMvc.perform(get("/students_api/1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.firstName", is("first")))
            .andExpect(jsonPath("$.lastName", is("student")))
            .andExpect(jsonPath("$.groupId", is(1)))
            .andExpect(jsonPath("$.email", is("test@test.test")))
            .andDo(print());

    verify(service, times(1)).getStudentById(1);
  }

  @Test
  void studentEditForm_shouldReturnError_whenStudentDoesNotExists() throws Exception {
    when(service.getStudentById(1)).thenThrow(NoSuchElementException.class);
    this.mockMvc.perform(get("/students_api/1"))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.error", is("This student does not exists.")))
            .andDo(print());

    verify(service, times(1)).getStudentById(1);
  }

  @Test
  void studentCreate_shouldReturnSuccessString_whenGetRequiredData() throws Exception {
    when(service.createStudent(firstStudent)).thenReturn(firstStudent);
    this.mockMvc.perform(post("/students_api/new").contentType("application/json")
                    .content(objectMapper.writeValueAsString(firstStudent)))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("New student was created successfully.")))
            .andDo(print());

    verify(service, times(1)).createStudent(firstStudent);
  }

  @Test
  void studentCreate_shouldReturnIntegrityError_whenGetNotCorrectDataForGroup() throws Exception {
    when(service.createStudent(firstStudent)).thenThrow(DataIntegrityViolationException.class);
    this.mockMvc.perform(post("/students_api/new").contentType("application/json")
                    .content(objectMapper.writeValueAsString(firstStudent)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error", is("This group does not exists for set reference for student.")))
            .andDo(print());
  }

  @Test
  void studentCreate_shouldReturnValidationError_whenGetNotCorrectData() throws Exception {
    StudentDTO studentDTO = new StudentDTO(0, "", "", 0, "");

    this.mockMvc.perform(post("/students_api/new").contentType("application/json")
                    .content(objectMapper.writeValueAsString(studentDTO)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.firstName", is("Firstname size should be between 2 and 20.")))
            .andExpect(jsonPath("$.lastName", is("Lastname size should be between 2 and 20.")))
            .andExpect(jsonPath("$.email", is("Please enter a valid email.")))
            .andDo(print());
  }


  @Test
  void studentUpdate_shouldReturnSuccessString_whenGetRequiredData() throws Exception {
    StudentDTO studentDTO = new StudentDTO(1, "updated", "teacher", 1, "test@test.test");

    when(service.updateStudentById(studentDTO)).thenReturn(studentDTO);
    this.mockMvc.perform(put("/students_api/1").contentType("application/json")
                    .content(objectMapper.writeValueAsString(studentDTO)))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Student was updated successfully.")))
            .andDo(print());

    verify(service, times(1)).updateStudentById(studentDTO);
  }

  @Test
  void studentUpdate_shouldReturnBadRequest_whenGetNotValidData() throws Exception {
    StudentDTO studentDTO = new StudentDTO(0, "", "", 0, "");

    this.mockMvc.perform(put("/students_api/1").contentType("application/json")
                    .content(objectMapper.writeValueAsString(studentDTO)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.firstName", is("Firstname size should be between 2 and 20.")))
            .andExpect(jsonPath("$.lastName", is("Lastname size should be between 2 and 20.")))
            .andExpect(jsonPath("$.email", is("Please enter a valid email.")))
            .andDo(print());
  }

  @Test
  void studentUpdate_shouldReturnIntegrityError_whenGetNotCorrectDataForGroup() throws Exception {
    when(service.updateStudentById(firstStudent)).thenThrow(DataIntegrityViolationException.class);

    this.mockMvc.perform(put("/students_api/1").contentType("application/json")
                    .content(objectMapper.writeValueAsString(firstStudent)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error", is("This group does not exists for set reference for student.")))
            .andDo(print());
  }

  @Test
  void studentDelete_shouldReturnSuccessString_whenGetExistingId() throws Exception {
    this.mockMvc.perform(delete("/students_api/1").contentType("application/json"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Student was deleted successfully.")))
            .andDo(print());
  }

  @Test
  void studentDelete_shouldReturnBadRequest_whenIdDoesNotExists() throws Exception {
    doThrow(EmptyResultDataAccessException.class).when(service).deleteStudentById(1);
    this.mockMvc.perform(delete("/students_api/1").contentType("application/json"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error", is("This student does not exists.")))

            .andDo(print());
  }

}
