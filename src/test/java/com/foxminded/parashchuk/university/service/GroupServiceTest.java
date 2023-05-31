package com.foxminded.parashchuk.university.service;

import com.foxminded.parashchuk.university.dao.GroupRepository;
import com.foxminded.parashchuk.university.dto.GroupDTO;
import com.foxminded.parashchuk.university.models.Group;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class GroupServiceTest {
  @InjectMocks
  private GroupService groupService;

  @Mock
  GroupRepository dao;
  @Spy
  ModelMapper mapper;

  @Test
  void getAllGroups_shouldCallToGroupDaoAndReturnList_whenDbIsNotEmpty() {
    List<GroupDTO> expectedDTO = Arrays.asList(
            new GroupDTO(1, "first"),
            new GroupDTO(2, "second"));
    List<Group> expected = Arrays.asList(
            new Group(1, "first"),
            new Group(2, "second"));
    when(dao.findAllByOrderById()).thenReturn(expected);
    assertEquals(expectedDTO, groupService.getAllGroups());
    verify(dao, times(1)).findAllByOrderById();
  }

  @Test
  void createGroup_shouldReturnResultOfInsert_whenGetGroup() {
    GroupDTO groupDTO = new GroupDTO(1, "first");
    Group group = new Group(1, "first");
    when(dao.save(group)).thenReturn(group);
    assertEquals(groupDTO, groupService.createGroup(groupDTO));
    verify(dao, times(1)).save(any(Group.class));
  }

  @Test
  void getGroupById_shouldReturnGroup_whenGetExistingId() {
    Group group = new Group(1, "first");
    GroupDTO groupDTO = new GroupDTO(1, "first");
    when(dao.findById(1)).thenReturn(Optional.of(group));
    assertEquals(groupDTO, groupService.getGroupById(1));
    verify(dao, times(1)).findById(anyInt());
  }

  @Test
  void updateGroupById_shouldReturnResultOfUpdate_whenGetGroupAndExistingId() {
    Group group = new Group(1, "first");
    GroupDTO groupDTO = new GroupDTO(1, "first");
    when(dao.findById(1)).thenReturn(Optional.of(new Group(1, "")));
    when(dao.save(group)).thenReturn(group);
    assertEquals(groupDTO, groupService.updateGroupById(groupDTO));
    verify(dao, times(1)).save(any(Group.class));
  }

  @Test
  void deleteGroupById_shouldReturnResultOfDelete_whenGetId() {
    groupService.deleteGroupById(1);
    verify(dao, times(1)).deleteById(anyInt());
  }
}
