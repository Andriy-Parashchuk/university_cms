package com.foxminded.parashchuk.university.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.parashchuk.university.dto.LessonDTO;
import com.foxminded.parashchuk.university.exceptions.LessonsNotFoundExceptions;
import com.foxminded.parashchuk.university.service.LessonService;
import com.foxminded.parashchuk.university.service.ScheduleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ScheduleApiController.class)
class ScheduleApiControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private ScheduleService service;

  @Autowired
  @InjectMocks
  private ScheduleApiController controller;

  @Test
  void getChosenSchedule_shouldRedirectToChooseScheduleWithDangerMessage_whenGetNotCorrectTime() throws Exception {
    this.mockMvc.perform(post("/schedule_api/")
                    .param("type", "group")
                    .param("id", "3")
                    .param("period", "day")
                    .param("time", ""))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error", is("Please choose date correctly.")))
            .andDo(print());
  }

  @Test
  void getChosenSchedule_shouldRedirectToChooseScheduleWithDangerMessage_whenGetNotCorrectId() throws Exception {
    when(service.getLessonsStudentDay(1, LocalDate.parse("2023-02-10")))
            .thenThrow(NoSuchElementException.class);

    this.mockMvc.perform(post("/schedule_api/")
                    .param("type", "student")
                    .param("id", "1")
                    .param("period", "day")
                    .param("time", "2023-02-10T10:30:00"))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error", is("User with this id does not exist.")))
            .andDo(print());
  }

  @Test
  void getChosenSchedule_shouldRedirectToChooseScheduleWithDangerMessage_whenNotFoundLessonsForDate() throws Exception {
    when(service.getLessonsStudentDay(1, LocalDate.parse("2023-02-10")))
            .thenThrow(LessonsNotFoundExceptions.class);

    this.mockMvc.perform(post("/schedule_api/")
                    .param("type", "student")
                    .param("id", "1")
                    .param("period", "day")
                    .param("time", "2023-02-10T10:30:00"))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error", is("Lessons for this user for this date are not found.")))
            .andDo(print());
  }

  @Test
  void getChosenSchedule_shouldShowLessonsForStudentAtDay_whenSearchStudentDayLessons() throws Exception {
    List<LessonDTO> lessons = Arrays.asList(new LessonDTO(1, "Math", 2, 1,
            LocalDateTime.of(2023, 02, 10, 10, 30, 00), 305));
    when(service.getLessonsStudentDay(1, LocalDate.parse("2023-02-10")))
            .thenReturn(lessons);

    this.mockMvc.perform(post("/schedule_api/")
                    .param("type", "student")
                    .param("id", "1")
                    .param("period", "day")
                    .param("time", "2023-02-10T10:30:00"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[0].name", is("Math")))
            .andExpect(jsonPath("$[0].teacherId", is(2)))
            .andExpect(jsonPath("$[0].groupId", is(1)))
            .andExpect(jsonPath("$[0].time", is("2023-02-10T10:30:00")))
            .andExpect(jsonPath("$[0].audience", is(305)));
  }

  @Test
  void getChosenSchedule_shouldShowLessonsForStudentAtMonth_whenSearchStudentDayMonth() throws Exception {
    List<LessonDTO> lessons = Arrays.asList(new LessonDTO(1, "Math", 2, 1,
            LocalDateTime.of(2023, 02, 10, 10, 30, 00), 305));
    when(service.getLessonsStudentMonth(1, LocalDate.parse("2023-02-10")))
            .thenReturn(lessons);

    this.mockMvc.perform(post("/schedule_api/")
                    .param("type", "student")
                    .param("id", "1")
                    .param("period", "month")
                    .param("time", "2023-02-10T10:30:00"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[0].name", is("Math")))
            .andExpect(jsonPath("$[0].teacherId", is(2)))
            .andExpect(jsonPath("$[0].groupId", is(1)))
            .andExpect(jsonPath("$[0].time", is("2023-02-10T10:30:00")))
            .andExpect(jsonPath("$[0].audience", is(305)));
  }

  @Test
  void getChosenSchedule_shouldShowLessonsForTeacherAtDay_whenSearchTeacherDayLessons() throws Exception {
    List<LessonDTO> lessons = Arrays.asList(new LessonDTO(1, "Math", 2, 1,
            LocalDateTime.of(2023, 02, 10, 10, 30, 00), 305));
    when(service.getLessonsTeacherDay(1, LocalDate.parse("2023-02-10")))
            .thenReturn(lessons);

    this.mockMvc.perform(post("/schedule_api/")
                    .param("type", "teacher")
                    .param("id", "1")
                    .param("period", "day")
                    .param("time", "2023-02-10T10:30:00"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[0].name", is("Math")))
            .andExpect(jsonPath("$[0].teacherId", is(2)))
            .andExpect(jsonPath("$[0].groupId", is(1)))
            .andExpect(jsonPath("$[0].time", is("2023-02-10T10:30:00")))
            .andExpect(jsonPath("$[0].audience", is(305)));
  }

  @Test
  void getChosenSchedule_shouldShowLessonsForTeacherAtMonth_whenSearchTeacherDayMonth() throws Exception {
    List<LessonDTO> lessons = Arrays.asList(new LessonDTO(1, "Math", 2, 1,
            LocalDateTime.of(2023, 02, 10, 10, 30, 00), 305));
    when(service.getLessonsTeacherMonth(1, LocalDate.parse("2023-02-10")))
            .thenReturn(lessons);

    this.mockMvc.perform(post("/schedule_api/")
                    .param("type", "teacher")
                    .param("id", "1")
                    .param("period", "month")
                    .param("time", "2023-02-10T10:30:00"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[0].name", is("Math")))
            .andExpect(jsonPath("$[0].teacherId", is(2)))
            .andExpect(jsonPath("$[0].groupId", is(1)))
            .andExpect(jsonPath("$[0].time", is("2023-02-10T10:30:00")))
            .andExpect(jsonPath("$[0].audience", is(305)));
  }


}
