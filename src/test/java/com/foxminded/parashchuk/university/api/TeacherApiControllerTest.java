package com.foxminded.parashchuk.university.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.parashchuk.university.dto.TeacherDTO;
import com.foxminded.parashchuk.university.service.TeacherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
@WebMvcTest(TeacherApiController.class)
class TeacherApiControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private TeacherService service;

  @Autowired
  @InjectMocks
  private TeacherApiController controller;


  private final TeacherDTO firstTeacher = new TeacherDTO(
          1, "first", "teacher", "test@test.test");
  private final TeacherDTO secondTeacher = new TeacherDTO(
          2, "second", "teacher", "test@test.test");

  private final List<TeacherDTO> teachers = Arrays.asList(firstTeacher, secondTeacher);



  @Test
  void getAllTeachers_shouldReturnAllData_whenDbIsNotEmpty() throws Exception {
    when(service.getAllTeachers()).thenReturn(teachers);
    this.mockMvc.perform(get("/api/teachers"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[0].firstName", is("first")))
            .andExpect(jsonPath("$[0].lastName", is("teacher")))
            .andExpect(jsonPath("$[0].email", is("test@test.test")))
            .andExpect(jsonPath("$[1].id", is(2)))
            .andExpect(jsonPath("$[1].firstName", is("second")))
            .andExpect(jsonPath("$[1].lastName", is("teacher")))
            .andExpect(jsonPath("$[1].email", is("test@test.test")))
            .andDo(print());

    verify(service, times(1)).getAllTeachers();
  }

  @Test
  void teacherEditForm_shouldReturnTeacher_whenTeacherIsExists() throws Exception {
    when(service.getTeacherById(1)).thenReturn(firstTeacher);
    this.mockMvc.perform(get("/api/teachers/1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.firstName", is("first")))
            .andExpect(jsonPath("$.lastName", is("teacher")))
            .andExpect(jsonPath("$.email", is("test@test.test")))
            .andDo(print());

    verify(service, times(1)).getTeacherById(1);
  }

  @Test
  void teacherEditForm_shouldReturnError_whenTeacherDoesNotExists() throws Exception {
    when(service.getTeacherById(1)).thenThrow(NoSuchElementException.class);
    this.mockMvc.perform(get("/api/teachers/1"))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.error", is("This teacher does not exists.")))
            .andDo(print());

    verify(service, times(1)).getTeacherById(1);
  }

  @Test
  void teacherCreate_shouldReturnSuccessString_whenGetRequiredData() throws Exception {
    firstTeacher.setDepartment("bio");
    firstTeacher.setAudience(1);

    when(service.createTeacher(firstTeacher)).thenReturn(firstTeacher);
    this.mockMvc.perform(post("/api/teachers").contentType("application/json")
                    .content(objectMapper.writeValueAsString(firstTeacher)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.firstName", is("first")))
            .andExpect(jsonPath("$.lastName", is("teacher")))
            .andExpect(jsonPath("$.email", is("test@test.test")))
            .andExpect(jsonPath("$.department", is("bio")))
            .andExpect(jsonPath("$.audience", is(1)))
            .andDo(print());

    verify(service, times(1)).createTeacher(firstTeacher);
  }

  @Test
  void teacherCreate_shouldReturnValidationError_whenGetNotCorrectData() throws Exception {
    TeacherDTO teacherDTO = new TeacherDTO(0, "", "", "");
    teacherDTO.setDepartment("");
    teacherDTO.setAudience(1);
    this.mockMvc.perform(post("/api/teachers").contentType("application/json")
                    .content(objectMapper.writeValueAsString(teacherDTO)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.firstName", is("Firstname size should be between 2 and 20.")))
            .andExpect(jsonPath("$.lastName", is("Lastname size should be between 2 and 20.")))
            .andExpect(jsonPath("$.email", is("Please enter a valid email.")))
            .andExpect(jsonPath("$.department", is("Department is mandatory.")))
            .andDo(print());
  }


  @Test
  void teacherUpdate_shouldReturnSuccessString_whenGetRequiredData() throws Exception {
    TeacherDTO teacherDTO = new TeacherDTO(1, "updated", "teacher", "test@test.test");
    teacherDTO.setDepartment("bio");
    teacherDTO.setAudience(1);

    when(service.updateTeacherById(teacherDTO)).thenReturn(teacherDTO);
    this.mockMvc.perform(put("/api/teachers/1").contentType("application/json")
                    .content(objectMapper.writeValueAsString(teacherDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.firstName", is("updated")))
            .andExpect(jsonPath("$.lastName", is("teacher")))
            .andExpect(jsonPath("$.email", is("test@test.test")))
            .andExpect(jsonPath("$.department", is("bio")))
            .andExpect(jsonPath("$.audience", is(1)))
            .andDo(print());

    verify(service, times(1)).updateTeacherById(teacherDTO);
  }

  @Test
  void teacherUpdate_shouldReturnBadRequest_whenGetNotValidData() throws Exception {
    TeacherDTO teacherDTO = new TeacherDTO(1, "", "", "");
    teacherDTO.setDepartment("");
    teacherDTO.setAudience(1);

    this.mockMvc.perform(put("/api/teachers/1").contentType("application/json")
                    .content(objectMapper.writeValueAsString(teacherDTO)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.firstName", is("Firstname size should be between 2 and 20.")))
            .andExpect(jsonPath("$.lastName", is("Lastname size should be between 2 and 20.")))
            .andExpect(jsonPath("$.email", is("Please enter a valid email.")))
            .andExpect(jsonPath("$.department", is("Department is mandatory.")))
            .andDo(print());
  }

  @Test
  void teacherDelete_shouldReturnSuccessString_whenGetExistingId() throws Exception {
    doNothing().when(service).deleteTeacherById(1);
    this.mockMvc.perform(delete("/api/teachers/1").contentType("application/json"))
            .andExpect(status().isNoContent())
            .andDo(print());
  }

  @Test
  void teacherDelete_shouldReturnBadRequest_whenIdDoesNotExists() throws Exception {
    doThrow(EmptyResultDataAccessException.class).when(service).deleteTeacherById(1);
    this.mockMvc.perform(delete("/api/teachers/1").contentType("application/json"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error", is("This teacher does not exists.")))

            .andDo(print());
  }

}
