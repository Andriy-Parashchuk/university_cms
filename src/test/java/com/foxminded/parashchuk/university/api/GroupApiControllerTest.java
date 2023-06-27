package com.foxminded.parashchuk.university.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.parashchuk.university.dto.GroupDTO;
import com.foxminded.parashchuk.university.service.GroupService;
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
@WebMvcTest(GroupApiController.class)
class GroupApiControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private GroupService service;

  @Autowired
  @InjectMocks
  private GroupApiController controller;


  private final GroupDTO firstGroup = new GroupDTO(1, "first");
  private final GroupDTO secondGroup = new GroupDTO(2, "second");

  private final List<GroupDTO> groups = Arrays.asList(firstGroup, secondGroup);

  @Test
  void getAllGroups_shouldReturnAllData_whenDbIsNotEmpty() throws Exception {
    when(service.getAllGroups()).thenReturn(groups);
    this.mockMvc.perform(get("/groups_api/all"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[0].name", is("first")))
            .andExpect(jsonPath("$[1].id", is(2)))
            .andExpect(jsonPath("$[1].name", is("second")))
            .andDo(print());

    verify(service, times(1)).getAllGroups();
  }

  @Test
  void findGroupById_shouldReturnGroup_whenGroupIsExists() throws Exception {
    when(service.getGroupById(1)).thenReturn(firstGroup);
    this.mockMvc.perform(post("/groups_api/all")
                    .param("id", "1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.name", is("first")))
            .andDo(print());

    verify(service, times(1)).getGroupById(1);
  }

  @Test
  void findGroupById_shouldReturnError_whenGroupDoesNotExists() throws Exception {
    when(service.getGroupById(1)).thenThrow(NoSuchElementException.class);
    this.mockMvc.perform(post("/groups_api/all")
                    .param("id", "1"))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.error", is("This group does not exists.")))
            .andDo(print());

    verify(service, times(1)).getGroupById(1);
  }

  @Test
  void groupEditForm_shouldReturnGroup_whenGroupIsExists() throws Exception {
    when(service.getGroupById(1)).thenReturn(firstGroup);
    this.mockMvc.perform(get("/groups_api/1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.name", is("first")))
            .andDo(print());

    verify(service, times(1)).getGroupById(1);
  }

  @Test
  void groupEditForm_shouldReturnError_whenGroupDoesNotExists() throws Exception {
    when(service.getGroupById(1)).thenThrow(NoSuchElementException.class);
    this.mockMvc.perform(get("/groups_api/1"))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.error", is("This group does not exists.")))
            .andDo(print());

    verify(service, times(1)).getGroupById(1);
  }

  @Test
  void groupCreate_shouldReturnSuccessString_whenGetRequiredData() throws Exception {
    GroupDTO groupDTO = new GroupDTO(0, "new");
    when(service.createGroup(groupDTO)).thenReturn(new GroupDTO(3, "new"));
    this.mockMvc.perform(post("/groups_api/new").contentType("application/json")
                    .content(objectMapper.writeValueAsString(groupDTO)))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("New group was created successfully.")))
            .andDo(print());

    verify(service, times(1)).createGroup(groupDTO);
  }

  @Test
  void groupCreate_shouldReturnValidationError_whenGetNotCorrectData() throws Exception {
    GroupDTO groupDTO = new GroupDTO(0, "");
    this.mockMvc.perform(post("/groups_api/new").contentType("application/json")
                    .content(objectMapper.writeValueAsString(groupDTO)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.name", is("Name size should be between 2 and 20.")))
            .andDo(print());
  }

  @Test
  void groupUpdate_shouldReturnSuccessString_whenGetRequiredData() throws Exception {
    GroupDTO groupDTO = new GroupDTO(1, "updated");
    when(service.updateGroupById(groupDTO)).thenReturn(new GroupDTO(1, "updated"));
    this.mockMvc.perform(put("/groups_api/1").contentType("application/json")
                    .content(objectMapper.writeValueAsString(groupDTO)))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("New group was update successfully.")))
            .andDo(print());

    verify(service, times(1)).updateGroupById(groupDTO);
  }

  @Test
  void groupUpdate_shouldReturnBadRequest_whenGetNotValidData() throws Exception {
    GroupDTO groupDTO = new GroupDTO(1, "");
    this.mockMvc.perform(put("/groups_api/1").contentType("application/json")
                    .content(objectMapper.writeValueAsString(groupDTO)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.name", is("Name size should be between 2 and 20.")))
            .andDo(print());
  }

  @Test
  void groupDelete_shouldReturnSuccessString_whenGetExistingId() throws Exception {
    this.mockMvc.perform(delete("/groups_api/1").contentType("application/json"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("New group was deleted successfully.")))
            .andDo(print());
  }

  @Test
  void groupDelete_shouldReturnBadRequest_whenIdDoesNotExists() throws Exception {
    doThrow(EmptyResultDataAccessException.class).when(service).deleteGroupById(1);
    this.mockMvc.perform(delete("/groups_api/1").contentType("application/json"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error", is("This group does not exists.")))

            .andDo(print());
  }
}
