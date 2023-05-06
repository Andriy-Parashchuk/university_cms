package com.foxminded.parashchuk.university.controller;

import com.foxminded.parashchuk.university.exceptions.LessonsNotFoundExceptions;
import com.foxminded.parashchuk.university.models.Lesson;
import com.foxminded.parashchuk.university.service.ScheduleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ScheduleController.class)
@AutoConfigureMockMvc
class ScheduleControllerTest {

  @MockBean
  private ScheduleService service;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  @InjectMocks
  private ScheduleController controller;

  @Test
  void showScheduleForm_shouldShowFormForChooseScheduleByTypeTimePeriod() throws Exception {
    this.mockMvc.perform(get("/schedule/"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("schedule_form"));
  }

  @Test
  void getChosenSchedule_shouldRedirectToChooseScheduleWithDangerMessage_whenGetNotCorrectTime() throws Exception {

    this.mockMvc.perform(post("/schedule/")
                    .param("type", "group")
                    .param("id", "3")
                    .param("period", "day")
                    .param("time", ""))
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(flash().attribute("danger_message", "Please choose date correctly."))
            .andExpect(redirectedUrl("/schedule/"));
  }

  @Test
  void getChosenSchedule_shouldRedirectToChooseScheduleWithDangerMessage_whenGetNotCorrectId() throws Exception {
    when(service.getLessonsStudentDay(1, LocalDate.parse("2023-02-10")))
            .thenThrow(NoSuchElementException.class);

    this.mockMvc.perform(post("/schedule/")
                    .param("type", "student")
                    .param("id", "1")
                    .param("period", "day")
                    .param("time", "2023-02-10T10:30:00"))
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(flash().attribute("danger_message",
                    "Student or teacher with id 1 does not exists."))
            .andExpect(redirectedUrl("/schedule/"));
  }

  @Test
  void getChosenSchedule_shouldRedirectToChooseScheduleWithDangerMessage_whenNotFoundLessonsForDate() throws Exception {
    when(service.getLessonsStudentDay(1, LocalDate.parse("2023-02-10")))
            .thenThrow(LessonsNotFoundExceptions.class);

    this.mockMvc.perform(post("/schedule/")
                    .param("type", "student")
                    .param("id", "1")
                    .param("period", "day")
                    .param("time", "2023-02-10T10:30:00"))
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(flash().attribute("danger_message",
                    "Lessons for this user on 2023-02-10 not found."))
            .andExpect(redirectedUrl("/schedule/"));
  }

  @Test
  void getChosenSchedule_shouldShowLessonsForStudentAtDay_whenSearchStudentDayLessons() throws Exception {
    List<Lesson> lessons = Arrays.asList(new Lesson(1, "Math", 2, 1,
            LocalDateTime.of(2023, 02, 10, 10, 30, 00), 305));
    when(service.getLessonsStudentDay(1, LocalDate.parse("2023-02-10")))
            .thenReturn(lessons);

    this.mockMvc.perform(post("/schedule/")
                    .param("type", "student")
                    .param("id", "1")
                    .param("period", "day")
                    .param("time", "2023-02-10T10:30:00"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(model().attribute("info","student for day"))
            .andExpect(model().attribute("lessons", lessons))
            .andExpect(view().name("all_lessons"));
  }

  @Test
  void getChosenSchedule_shouldShowLessonsForStudentAtMonth_whenSearchStudentDayMonth() throws Exception {
    List<Lesson> lessons = Arrays.asList(new Lesson(1, "Math", 2, 1,
            LocalDateTime.of(2023, 02, 10, 10, 30, 00), 305));
    when(service.getLessonsStudentMonth(1, LocalDate.parse("2023-02-10")))
            .thenReturn(lessons);

    this.mockMvc.perform(post("/schedule/")
                    .param("type", "student")
                    .param("id", "1")
                    .param("period", "month")
                    .param("time", "2023-02-10T10:30:00"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(model().attribute("info","student for month"))
            .andExpect(model().attribute("lessons", lessons))
            .andExpect(view().name("all_lessons"));
  }

  @Test
  void getChosenSchedule_shouldShowLessonsForTeacherAtDay_whenSearchTeacherDayLessons() throws Exception {
    List<Lesson> lessons = Arrays.asList(new Lesson(1, "Math", 2, 1,
            LocalDateTime.of(2023, 02, 10, 10, 30, 00), 305));
    when(service.getLessonsTeacherDay(1, LocalDate.parse("2023-02-10")))
            .thenReturn(lessons);

    this.mockMvc.perform(post("/schedule/")
                    .param("type", "teacher")
                    .param("id", "1")
                    .param("period", "day")
                    .param("time", "2023-02-10T10:30:00"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(model().attribute("info","teacher for day"))
            .andExpect(model().attribute("lessons", lessons))
            .andExpect(view().name("all_lessons"));
  }

  @Test
  void getChosenSchedule_shouldShowLessonsForTeacherAtMonth_whenSearchTeacherDayMonth() throws Exception {
    List<Lesson> lessons = Arrays.asList(new Lesson(1, "Math", 2, 1,
            LocalDateTime.of(2023, 02, 10, 10, 30, 00), 305));
    when(service.getLessonsTeacherMonth(1, LocalDate.parse("2023-02-10")))
            .thenReturn(lessons);

    this.mockMvc.perform(post("/schedule/")
                    .param("type", "teacher")
                    .param("id", "1")
                    .param("period", "month")
                    .param("time", "2023-02-10T10:30:00"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(model().attribute("info","teacher for month"))
            .andExpect(model().attribute("lessons", lessons))
            .andExpect(view().name("all_lessons"));
  }
}
