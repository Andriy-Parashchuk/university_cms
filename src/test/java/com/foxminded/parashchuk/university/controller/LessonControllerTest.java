package com.foxminded.parashchuk.university.controller;

import com.foxminded.parashchuk.university.config.TestSecurityConfig;
import com.foxminded.parashchuk.university.dto.LessonDTO;
import com.foxminded.parashchuk.university.models.Lesson;
import com.foxminded.parashchuk.university.service.LessonService;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
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
@WebMvcTest(LessonController.class)
@AutoConfigureMockMvc
@ContextConfiguration(classes = {TestSecurityConfig.class, LessonController.class})
class LessonControllerTest {

  @MockBean
  private LessonService service;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  @InjectMocks
  private LessonController controller;

  @Test
  void showAllLessons_shouldTransferAllDataToTemplate_whenGetDataFromService() throws Exception {
    List<LessonDTO> expected = Arrays.asList(
            new LessonDTO(1, "Math", 2, 1,
                    LocalDateTime.of(2023, 02, 10, 10, 30, 00), 305),
            new LessonDTO(2, "Biology", 1, 2,
                    LocalDateTime.of(2023, 02, 11, 12, 00, 00), 203));

    when(service.getAllLessons()).thenReturn(expected);
    this.mockMvc.perform(get("/lessons/all"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(model().attribute("lessons",expected))
            .andExpect(view().name("all_lessons"));
  }

  @Test
  void findLessonById_shouldFindLessonAndRedirectItToEditPage_whenGetId() throws Exception {
    this.mockMvc.perform(post("/lessons/all")
                    .param("id", "1"))
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/lessons/1"));
  }

  @Test
  void lessonEditForm_shouldShowEditFormForLesson_whenGetIdFromPath() throws Exception {
    LessonDTO lesson = new LessonDTO(0, "Math", 2, 1,
            LocalDateTime.of(2023, 02, 10, 10, 30, 00), 305);
    when(service.getLessonById(1)).thenReturn(lesson);
    this.mockMvc.perform(get("/lessons/1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(model().attribute("lesson", lesson))
            .andExpect(view().name("edit/lesson_edit"));
  }

  @Test
  void lessonEditForm_shouldRedirectToMainLessonsPageWithDangerMessage_whenGetNotExistedId() throws Exception {
    when(service.getLessonById(3)).thenThrow(NoSuchElementException.class);
    this.mockMvc.perform(get("/lessons/3"))
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(flash().attribute("danger_message", "Lesson with id 3 does not exists."))
            .andExpect(redirectedUrl("/lessons/all"));
  }

  @Test
  void lessonCreateForm_shouldShowFormForCreateNewLesson() throws Exception {
    this.mockMvc.perform(get("/lessons/new"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("create/lesson_new"));
  }

  @Test
  void lessonEdit_shouldTransferDataToService_whenGetNeededParameters() throws Exception {
    when(service.getLessonById(1)).thenReturn(new LessonDTO(1, "Math", 2, 1,
            LocalDateTime.of(2023, 02, 10, 10, 30, 00), 305));


    this.mockMvc.perform(post("/lessons/1")
                    .param("name", "Math")
                    .param("teacherId", "3")
                    .param("groupId", "4")
                    .param("time", "2023-04-15T10:30:00")
                    .param("audience", "333"))
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(flash().attribute("success_message", "Lesson with id 1 was updated."))
            .andExpect(redirectedUrl("/lessons/all"));
  }

  @Test
  void lessonEdit_shouldShowErrors_whenGetInValidParameters() throws Exception {
    when(service.getLessonById(1)).thenReturn(new LessonDTO(1, "Math", 2, 1,
            LocalDateTime.of(2023, 02, 10, 10, 30, 00), 305));


    this.mockMvc.perform(post("/lessons/1")
                    .param("name", "")
                    .param("teacherId", "0")
                    .param("groupId", "0")
                    .param("time", "")
                    .param("audience", "0"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Name size should be between 2 and 20")))
            .andExpect(content().string(containsString("Time is mandatory")))
            .andExpect(view().name("edit/lesson_edit"));
  }

  @Test
  void lessonEdit_shouldThrowExceptionAndRedirectToMainLessonPage_whenGetNotExistedGroupOrTeacher() throws Exception {
    LessonDTO lesson = new LessonDTO(1, "Math", 2, 1,
            LocalDateTime.of(2023, 02, 10, 10, 30, 00), 305);
    when(service.getLessonById(1)).thenReturn(lesson);
    lesson.setTeacherId(3);
    lesson.setGroupId(4);
    lesson.setTime(LocalDateTime.of(2023, 04, 15, 10, 30, 00));
    lesson.setAudience(333);

    doThrow(DataIntegrityViolationException.class).when(service).updateLessonById(lesson);

    this.mockMvc.perform(post("/lessons/1")
                    .param("name", "Math")
                    .param("teacherId", "3")
                    .param("groupId", "4")
                    .param("time", "2023-04-15T10:30:00")
                    .param("audience", "333"))
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(flash().attribute("danger_message",
                    "Group with this id (4) or teacher with id (3) does not exists."))
            .andExpect(redirectedUrl("/lessons/1"));
  }


  @Test
  void lessonCreate_shouldTransferDataToService_whenGetNeededParameters() throws Exception {
    LessonDTO lesson = new LessonDTO(0, "Math", 2, 1,
            LocalDateTime.of(2023, 02, 10, 10, 30, 00), 305);

    when(service.createLesson(lesson)).thenReturn(lesson);
    this.mockMvc.perform(post("/lessons/new")
                    .param("name", "Math")
                    .param("teacherId", "2")
                    .param("groupId", "1")
                    .param("time", "2023-02-10T10:30:00")
                    .param("audience", "305"))
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(flash().attribute("success_message", "New lesson was created."))
            .andExpect(redirectedUrl("/lessons/all"));
  }

  @Test
  void lessonCreate_shouldShowErrors_whenGetInValidParameters() throws Exception {
    this.mockMvc.perform(post("/lessons/new")
                    .param("name", "")
                    .param("teacherId", "0")
                    .param("groupId", "0")
                    .param("time", "")
                    .param("audience", "0"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Name size should be between 2 and 20")))
            .andExpect(content().string(containsString("Time is mandatory")))
            .andExpect(view().name("create/lesson_new"));
  }

  @Test
  void lessonCreate_shouldThrowExceptionAndRedirectToNewLessonPage_whenGetNotExistedGroupOrTeacher() throws Exception {
    doThrow(DataIntegrityViolationException.class).when(service).createLesson(
            new LessonDTO(0, "Math", 2, 1,
            LocalDateTime.of(2023, 02, 10, 10, 30, 00), 305));
    this.mockMvc.perform(post("/lessons/new")
                    .param("name", "Math")
                    .param("teacherId", "2")
                    .param("groupId", "1")
                    .param("time", "2023-02-10T10:30:00")
                    .param("audience", "305"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(model().attribute("danger_message",
                    "Group with this id (1) or teacher with id (2) does not exists."))
            .andExpect(view().name("create/lesson_new"));
  }

  @Test
  void deleteLesson_shouldRedirectToMainLessonsPageWithSuccessMessage_whenGetExistedId() throws Exception {

    this.mockMvc.perform(get("/lessons/delete/1"))
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(flash().attribute("success_message", "Lesson with id 1 was deleted."))
            .andExpect(redirectedUrl("/lessons/all"));
  }

  @Test
  void deleteStudent_shouldRedirectToMainStudentPageWithDangerMessage_whenGetNotExistedId() throws Exception {
    doThrow(EmptyResultDataAccessException.class).when(service).deleteLessonById(1);
    this.mockMvc.perform(get("/lessons/delete/1"))
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(flash().attribute("danger_message", "Lesson with id 1 does not exists."))
            .andExpect(redirectedUrl("/lessons/all"));
  }

}
