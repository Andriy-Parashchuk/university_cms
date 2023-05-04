package com.foxminded.parashchuk.university.dao;

import com.foxminded.parashchuk.university.config.TestConfig;
import com.foxminded.parashchuk.university.config.TestPersistenceConfig;
import com.foxminded.parashchuk.university.models.Group;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
@ContextConfiguration(classes = {TestConfig.class, TestPersistenceConfig.class})
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
    Group group = dao.getGroupById(1);
    Group expected = new Group(1, "first");
    
    assertEquals(expected, group);
  }
  
  @Test
  void getGroupById_shouldThrowException_whenGroupWithProvidedIdDoesNotExists() {
    assertThrows(NoSuchElementException.class, () -> dao.getGroupById(8));
  }
  
  @Test 
  void updateGroupById_shouldUpdateGroup_whenGetExistsGroupIdAndParameters() {
    Group expected = new Group(1, "Updated group");
    dao.updateGroupById(new Group(1, "Updated group"));
    assertEquals(expected, dao.getGroupById(1));
  }
  
  @Test 
  void updateGroupById_shouldThrowException_whenGroupDoesNotExists() {
    Group group = new Group(0, "Updated group");
    assertThrows(NoSuchElementException.class, () -> dao.updateGroupById(group));
  }
  
  @Test 
  void deleteGroupById_shouldDeleteGroupAndReturnOne_whenGetExistsGroupId() {
    List<Group> expected = Arrays.asList(
        new Group(1, "first"), 
        new Group(2, "second"),
        new Group(3, "third"));
    List<Group> groups = dao.getAllGroups();
    assertEquals(expected, groups);
    
    dao.deleteGroupById(3);
    
    expected = Arrays.asList(
        new Group(1, "first"), 
        new Group(2, "second"));
    
    groups = dao.getAllGroups();
    assertEquals(expected, groups);
  }
  
  @Test 
  void deleteGroupById_shouldThrowException_whenGroupDoesNotExists() {
    assertThrows(NoSuchElementException.class, () -> dao.deleteGroupById(12));
  }
}
