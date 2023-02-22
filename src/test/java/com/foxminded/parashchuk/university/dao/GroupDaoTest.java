package com.foxminded.parashchuk.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.foxminded.parashchuk.university.config.TestConfig;
import com.foxminded.parashchuk.university.models.Group;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@ContextConfiguration(classes = {TestConfig.class})
@Sql(value = {"classpath:jdbc/groups.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class GroupDaoTest {

  @Autowired
  private GroupDao dao;
  
  @Test
  void getAllGroups_shouldReturnListWithGroups_whenTableGroupsIsNotEmpty() {
    List<Group> expected = Arrays.asList(
        new Group(1, "first"), 
        new Group(2, "second"),
        new Group(3, "third"));
    List<Group> groups = dao.getAllGroups();
    assertEquals(expected, groups);
  }
  
  @Test 
  void createGroup_shouldAddNewGroupToDb_whenGetGroup() {
    Group group = new Group(0, "New");
    List<Group> expected = Arrays.asList(
        new Group(1, "first"), 
        new Group(2, "second"),
        new Group(3, "third"),
        new Group(4, "New"));
    
    dao.createGroup(group);
    
    assertEquals(expected, dao.getAllGroups());
  }  
  
  @Test 
  void createGroup_shouldThrowIllegalArgsException_whenGetNull() {
    assertThrows(IllegalArgumentException.class, () -> dao.createGroup(null));
  }
  
  @Test
  void getGroupById_shouldReturnGroup_whenGetGroupExistsId() {
    Group group = dao.getGroupById(1).get();
    Group expected = new Group(1, "first");
    
    assertEquals(expected, group);
  }
  
  @Test
  void getGroupById_shouldReturnNull_whenGroupWithProvidedIdDoesNotExists() {

    assertFalse(dao.getGroupById(6).isPresent());
  }
  
  @Test 
  void updateGroupById_shouldUpdateGroup_whenGetExistsGroupIdAndParameters() {
    Group expected = new Group(1, "Updated group");
 
    assertEquals(1, dao.updateGroupById(new Group(0, "Updated group"), 1));
    assertEquals(expected, dao.getGroupById(1).get());
  }
  
  @Test 
  void updateGroupById_shouldReturnZero_whenProvidedIdDoesNotExists() {
    assertEquals(0, dao.updateGroupById(new Group(0, "Updated group"), 8));
  }
  
  @Test 
  void deleteGroupById_shouldDeleteGroupAndReturnOne_whenGetExistsGroupId() {
    List<Group> expected = Arrays.asList(
        new Group(1, "first"), 
        new Group(2, "second"),
        new Group(3, "third"));
    List<Group> groups = dao.getAllGroups();
    assertEquals(expected, groups);
    
    assertEquals(1, dao.deleteGroupById(3));
    
    expected = Arrays.asList(
        new Group(1, "first"), 
        new Group(2, "second"));
    
    groups = dao.getAllGroups();
    assertEquals(expected, groups);
  }
  
  @Test 
  void deleteGroupById_shouldReturnZero_whenProvidedIdDoesNotExists() {
    assertEquals(0, dao.deleteGroupById(12));
  }
}
