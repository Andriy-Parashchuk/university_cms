package com.foxminded.parashchuk.university.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.parashchuk.university.dto.LessonDTO;
import com.foxminded.parashchuk.university.service.LessonService;
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

import java.time.LocalDateTime;
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
@WebMvcTest(LessonApiController.class)
class LessonApiControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private LessonService service;

  @Autowired
  @InjectMocks
  private LessonApiController controller;


  private final LessonDTO firstLesson = new LessonDTO(1, "Math", 2, 1,
                LocalDateTime.of(2023, 02, 10, 10, 30, 00), 305);

  private final LessonDTO secondLesson = new LessonDTO(2, "Biology", 1, 2,
          LocalDateTime.of(2023, 02, 11, 12, 00, 00), 203);

  private final List<LessonDTO> lessons = Arrays.asList(firstLesson, secondLesson);


  @Test
  void getAllLessons_shouldReturnAllData_whenDbIsNotEmpty() throws Exception {
    when(service.getAllLessons()).thenReturn(lessons);
    this.mockMvc.perform(get("/lessons_api/all"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[0].name", is("Math")))
            .andExpect(jsonPath("$[0].teacherId", is(2)))
            .andExpect(jsonPath("$[0].groupId", is(1)))
            .andExpect(jsonPath("$[0].time", is("2023-02-10T10:30:00")))
            .andExpect(jsonPath("$[0].audience", is(305)))
            .andExpect(jsonPath("$[1].id", is(2)))
            .andExpect(jsonPath("$[1].name", is("Biology")))
            .andExpect(jsonPath("$[1].teacherId", is(1)))
            .andExpect(jsonPath("$[1].groupId", is(2)))
            .andExpect(jsonPath("$[1].time", is("2023-02-11T12:00:00")))
            .andExpect(jsonPath("$[1].audience", is(203)))
            .andDo(print());

    verify(service, times(1)).getAllLessons();

  }


  @Test
  void findLessonById_shouldReturnLesson_whenLessonIsExists() throws Exception {
    when(service.getLessonById(1)).thenReturn(firstLesson);
    this.mockMvc.perform(post("/lessons_api/all")
                    .param("id", "1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.name", is("Math")))
            .andExpect(jsonPath("$.teacherId", is(2)))
            .andExpect(jsonPath("$.groupId", is(1)))
            .andExpect(jsonPath("$.time", is("2023-02-10T10:30:00")))
            .andExpect(jsonPath("$.audience", is(305)))
            .andDo(print());

    verify(service, times(1)).getLessonById(1);
  }


  @Test
  void findLessonById_shouldReturnError_whenLessonDoesNotExists() throws Exception {
    when(service.getLessonById(1)).thenThrow(NoSuchElementException.class);
    this.mockMvc.perform(post("/lessons_api/all")
                    .param("id", "1"))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.error", is("This lesson does not exists.")))
            .andDo(print());

    verify(service, times(1)).getLessonById(1);
  }

  @Test
  void lessonEditForm_shouldReturnLesson_whenLessonIsExists() throws Exception {
    when(service.getLessonById(1)).thenReturn(firstLesson);
    this.mockMvc.perform(get("/lessons_api/1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.name", is("Math")))
            .andExpect(jsonPath("$.teacherId", is(2)))
            .andExpect(jsonPath("$.groupId", is(1)))
            .andExpect(jsonPath("$.time", is("2023-02-10T10:30:00")))
            .andExpect(jsonPath("$.audience", is(305)))
            .andDo(print());

    verify(service, times(1)).getLessonById(1);
  }

  @Test
  void lessonEditForm_shouldReturnError_whenLessonDoesNotExists() throws Exception {
    when(service.getLessonById(1)).thenThrow(NoSuchElementException.class);
    this.mockMvc.perform(get("/lessons_api/1"))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.error", is("This lesson does not exists.")))
            .andDo(print());

    verify(service, times(1)).getLessonById(1);
  }

  @Test
  void lessonCreate_shouldReturnSuccessString_whenGetRequiredData() throws Exception {
    when(service.createLesson(firstLesson)).thenReturn(firstLesson);
    this.mockMvc.perform(post("/lessons_api/new").contentType("application/json")
                    .content(objectMapper.writeValueAsString(firstLesson)))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("New lesson was created successfully.")))
            .andDo(print());

    verify(service, times(1)).createLesson(firstLesson);
  }

  @Test
  void lessonCreate_shouldReturnIntegrityError_whenGetNotCorrectDataForGroup() throws Exception {
    when(service.createLesson(firstLesson)).thenThrow(DataIntegrityViolationException.class);
    this.mockMvc.perform(post("/lessons_api/new").contentType("application/json")
                    .content(objectMapper.writeValueAsString(firstLesson)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error",
                    is("This group or teacher does not exists for set reference for lesson.")))
            .andDo(print());
  }

  @Test
  void lessonCreate_shouldReturnValidationError_whenGetNotCorrectData() throws Exception {
    LessonDTO lessonDTO = new LessonDTO(1, "", 0, 0,
            null, 0);

    this.mockMvc.perform(post("/lessons_api/new").contentType("application/json")
                    .content(objectMapper.writeValueAsString(lessonDTO)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.name", is("Name size should be between 2 and 20")))
            .andExpect(jsonPath("$.time", is("Time is mandatory")))
            .andDo(print());
  }

  @Test
  void lessonUpdate_shouldReturnSuccessString_whenGetRequiredData() throws Exception {
    LessonDTO lessonDTO = new LessonDTO(1, "updated", 12, 22,
            LocalDateTime.of(2023, 02, 10, 10, 30, 00), 230);
    when(service.updateLessonById(lessonDTO)).thenReturn(lessonDTO);
    this.mockMvc.perform(put("/lessons_api/1").contentType("application/json")
                    .content(objectMapper.writeValueAsString(lessonDTO)))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Lesson was updated successfully.")))
            .andDo(print());

    verify(service, times(1)).updateLessonById(lessonDTO);
  }

  @Test
  void lessonUpdate_shouldReturnBadRequest_whenGetNotValidData() throws Exception {
    LessonDTO lessonDTO = new LessonDTO(1, "", 0, 0,
            null, 0);

    this.mockMvc.perform(put("/lessons_api/1").contentType("application/json")
                    .content(objectMapper.writeValueAsString(lessonDTO)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.name", is("Name size should be between 2 and 20")))
            .andExpect(jsonPath("$.time", is("Time is mandatory")))
            .andDo(print());
  }

  @Test
  void lessonUpdate_shouldReturnIntegrityError_whenGetNotCorrectDataForGroup() throws Exception {
    when(service.updateLessonById(firstLesson)).thenThrow(DataIntegrityViolationException.class);

    this.mockMvc.perform(put("/lessons_api/1").contentType("application/json")
                    .content(objectMapper.writeValueAsString(firstLesson)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error",
                    is("This group or teacher does not exists for set reference for lesson.")))
            .andDo(print());
  }

  @Test
  void lessonDelete_shouldReturnSuccessString_whenGetExistingId() throws Exception {
    this.mockMvc.perform(delete("/lessons_api/1").contentType("application/json"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Lesson was deleted successfully.")))
            .andDo(print());
  }

  @Test
  void lessonDelete_shouldReturnBadRequest_whenIdDoesNotExists() throws Exception {
    doThrow(EmptyResultDataAccessException.class).when(service).deleteLessonById(1);
    this.mockMvc.perform(delete("/lessons_api/1").contentType("application/json"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error", is("This lesson does not exists.")))

            .andDo(print());
  }


}
