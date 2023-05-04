package com.foxminded.parashchuk.university.controller;

import com.foxminded.parashchuk.university.models.Group;
import com.foxminded.parashchuk.university.service.GroupService;
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

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(GroupController.class)
@AutoConfigureMockMvc
class GroupControllerTest {
  @MockBean
  private GroupService service;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  @InjectMocks
  private GroupController controller;

  @Test
  void showAllGroups_shouldTransferAllDataToTemplate_whenGetDataFromService() throws Exception {
    List<Group> expected = Arrays.asList(
            new Group(1, "first"),
            new Group(2, "second"),
            new Group(3, "third"));
    when(service.getAllGroups()).thenReturn(expected);
    this.mockMvc.perform(get("/groups/all"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(model().attribute("groups",expected))
            .andExpect(view().name("all_groups"));
  }

  @Test
  void findGroupById_shouldFindGroupAndRedirectItToEditPage_whenGetId() throws Exception {
    this.mockMvc.perform(post("/groups/all")
                    .param("id", "1"))
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/groups/1"));
  }

  @Test
  void groupEditForm_shouldShowEditFormForGroup_whenGetIdFromPath() throws Exception {
    Group group = new Group(1, "first");
    when(service.getGroupById(1)).thenReturn(group);
    this.mockMvc.perform(get("/groups/1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(model().attribute("group", group))
            .andExpect(view().name("edit/group_edit"));
  }

  @Test
  void groupEditForm_shouldRedirectToMainGroupsPageWithDangerMessage_whenGetNotExistedId() throws Exception {
    when(service.getGroupById(3)).thenThrow(NoSuchElementException.class);
    this.mockMvc.perform(get("/groups/3"))
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(flash().attribute("danger_message", "Group with id 3 does not exists."))
            .andExpect(redirectedUrl("/groups/all"));
  }

  @Test
  void groupCreateForm_shouldShowFormForCreateNewGroup() throws Exception {
    this.mockMvc.perform(get("/groups/new"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("create/group_new"));
  }


  @Test
  void groupEdit_shouldTransferDataToService_whenGetNeededParameters() throws Exception {

    this.mockMvc.perform(post("/groups/1")
                    .param("name", "updated"))
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(flash().attribute("success_message", "Group with id 1 was updated."))
            .andExpect(redirectedUrl("/groups/all"));
  }

  @Test
  void groupCreate_shouldTransferDataToService_whenGetNeededParameters() throws Exception {
    this.mockMvc.perform(post("/groups/new")
                    .param("name", "first"))
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(flash().attribute("success_message", "New group was created"))
            .andExpect(redirectedUrl("/groups/all"));
  }

  @Test
  void deleteGroup_shouldRedirectToMainGroupPageWithSuccessMessage_whenGetExistedId() throws Exception {
    this.mockMvc.perform(get("/groups/delete/1"))
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(flash().attribute("success_message", "Group with id 1 was deleted."))
            .andExpect(redirectedUrl("/groups/all"));
  }

  @Test
  void deleteGroup_shouldRedirectToMainGroupPageWithDangerMessage_whenGetNotExistedId() throws Exception {
    doThrow(NoSuchElementException.class).when(service).deleteGroupById(1);
    this.mockMvc.perform(get("/groups/delete/1"))
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(flash().attribute("danger_message", "Group with id 1 does not exists."))
            .andExpect(redirectedUrl("/groups/all"));
  }
}
