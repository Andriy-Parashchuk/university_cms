package com.foxminded.parashchuk.university.controller;

import com.foxminded.parashchuk.university.models.Teacher;
import com.foxminded.parashchuk.university.service.TeacherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(TeacherController.class)
@AutoConfigureMockMvc
class TeacherControllerTest {

  @MockBean
  private TeacherService service;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  @InjectMocks
  private TeacherController controller;


  @Test
  void showAllTeachers_shouldTransferAllDataToTemplate_whenGetDataFromService() throws Exception {
    List<Teacher> expected = Arrays.asList(
            new Teacher(1, "Chris", "Martin"),
            new Teacher(2, "Mari", "Oswald"));
    when(service.getAllTeachers()).thenReturn(expected);
    this.mockMvc.perform(get("/teachers/all"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(model().attribute("teachers",expected))
            .andExpect(view().name("all_teachers"));
  }

  @Test
  void findTeacherById_shouldFindTeacherAndRedirectItToEditPage_whenGetId() throws Exception {
    this.mockMvc.perform(post("/teachers/all")
                    .param("id", "1"))
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/teachers/1"));
  }

  @Test
  void teacherEditForm_shouldShowEditFormForTeacher_whenGetIdFromPath() throws Exception {
    Teacher teacher =  new Teacher(2, "Mari", "Oswald");
    when(service.getTeacherById(2)).thenReturn(teacher);
    this.mockMvc.perform(get("/teachers/2"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(model().attribute("teacher", teacher))
            .andExpect(view().name("edit/teacher_edit"));
  }

  @Test
  void teacherEditForm_shouldRedirectToMainTeachersPageWithDangerMessage_whenGetNotExistedId() throws Exception {
    when(service.getTeacherById(3)).thenThrow(NoSuchElementException.class);
    this.mockMvc.perform(get("/teachers/3"))
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(flash().attribute("danger_message", "Teacher with id 3 does not exists."))
            .andExpect(redirectedUrl("/teachers/all"));
  }

  @Test
  void teacherCreateForm_shouldShowFormForCreateNewTeacher() throws Exception {
    this.mockMvc.perform(get("/teachers/new"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("create/teacher_new"));
  }

  @Test
  void teacherEdit_shouldTransferDataToService_whenGetNeededParameters() throws Exception {
    when(service.getTeacherById(1)).thenReturn(new Teacher(1, "Chris", "Martin"));
    when(service.updateTeacherById("1", "Updated", "Teacher", 0, ""))
            .thenReturn(1);

    this.mockMvc.perform(post("/teachers/1")
                    .param("firstName", "Updated")
                    .param("lastName", "Teacher"))
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(flash().attribute("success_message", "Teacher with id 1 was updated."))
            .andExpect(redirectedUrl("/teachers/all"));
  }

  @Test
  void teacherCreate_shouldTransferDataToService_whenGetNeededParameters() throws Exception {
    when(service.createTeacher(new Teacher(1, "Chris", "Martin"))).thenReturn(1);

    this.mockMvc.perform(post("/teachers/new")
                    .param("firstName", "Chris")
                    .param("lastName", "Martin"))
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(flash().attribute("success_message", "New teacher was created"))
            .andExpect(redirectedUrl("/teachers/all"));
  }

  @Test
  void deleteTeacher_shouldRedirectToMainTeachersPageWithSuccessMessage_whenGetExistedId() throws Exception {
    when(service.deleteTeacherById(1)).thenReturn(1);
    this.mockMvc.perform(get("/teachers/delete/1"))
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(flash().attribute("success_message", "Teacher with id 1 was deleted."))
            .andExpect(redirectedUrl("/teachers/all"));
  }

  @Test
  void deleteTeacher_shouldRedirectToMainTeacherPageWithDangerMessage_whenGetNotExistedId() throws Exception {
    when(service.deleteTeacherById(1)).thenThrow(NoSuchElementException.class);
    this.mockMvc.perform(get("/teachers/delete/1"))
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(flash().attribute("danger_message", "Teacher with id 1 does not exists."))
            .andExpect(redirectedUrl("/teachers/all"));
  }
}
