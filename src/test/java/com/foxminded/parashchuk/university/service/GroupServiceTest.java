package com.foxminded.parashchuk.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.foxminded.parashchuk.university.dao.GroupDao;
import com.foxminded.parashchuk.university.models.Group;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class GroupServiceTest {
  @InjectMocks
  private GroupService groupService;

  @Mock
  GroupDao dao;

  @Test
  void getAllGroups_shouldCallToGroupDaoAndReturnList_whenDbIsNotEmpty() {
    List<Group> expected = Arrays.asList(
            new Group(1, "first"),
            new Group(2, "second"));
    when(dao.getAllGroups()).thenReturn(expected);
    assertEquals(expected, groupService.getAllGroups());
    verify(dao, times(1)).getAllGroups();
  }

  @Test
  void createGroup_shouldReturnResultOfInsert_whenGetGroup() {
    Group group = new Group(1, "first");
    when(dao.createGroup(group)).thenReturn(1);
    assertEquals(1, groupService.createGroup(group));
    verify(dao, times(1)).createGroup(any(Group.class));
  }

  @Test
  void getGroupById_shouldReturnGroup_whenGetExistingId() {
    Group group = new Group(1, "first");
    when(dao.getGroupById(1)).thenReturn(Optional.of(group));
    assertEquals(group, groupService.getGroupById(1));
    verify(dao, times(1)).getGroupById(anyInt());
  }

  @Test
  void updateGroupById_shouldReturnResultOfUpdate_whenGetGroupAndExistingId() {
    Group group = new Group(1, "first");
    when(dao.getGroupById(1)).thenReturn(Optional.of(new Group(1, "")));
    when(dao.updateGroupById(group, 1)).thenReturn(1);
    assertEquals(1, groupService.updateGroupById("1", "first"));
    verify(dao, times(1)).updateGroupById(any(Group.class), anyInt());
  }

  @Test
  void deleteGroupById_shouldReturnResultOfDelete_whenGetId() {
    when(dao.deleteGroupById(1)).thenReturn(1);
    assertEquals(1, groupService.deleteGroupById(1));
    verify(dao, times(1)).deleteGroupById(anyInt());
  }
}
