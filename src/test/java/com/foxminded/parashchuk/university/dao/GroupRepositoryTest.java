package com.foxminded.parashchuk.university.dao;

import com.foxminded.parashchuk.university.config.TestPersistenceConfig;
import com.foxminded.parashchuk.university.models.Group;
import com.foxminded.parashchuk.university.models.Lesson;
import com.foxminded.parashchuk.university.models.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ContextConfiguration(classes = {TestPersistenceConfig.class})
@Sql(value = {"classpath:jdbc/groups.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DataJpaTest
@EnableAutoConfiguration
class GroupRepositoryTest {

  @Autowired
  private GroupRepository dao;

  @Test
  void getAllGroups_shouldReturnListWithGroups_whenTableGroupsIsNotEmpty() {
    List<Group> expected = Arrays.asList(
        new Group(1, "first", new ArrayList<Student>(), new ArrayList<Lesson>()),
        new Group(2, "second", new ArrayList<Student>(), new ArrayList<Lesson>()),
        new Group(3, "third", new ArrayList<Student>(), new ArrayList<Lesson>()));
    List<Group> groups = dao.findAllByOrderById();
    assertEquals(expected, groups);
  }
  
  @Test 
  void createGroup_shouldAddNewGroupToDb_whenGetGroup() {
    Group group = new Group(0, "New", new ArrayList<Student>(), new ArrayList<Lesson>());
    List<Group> expected = Arrays.asList(
        new Group(1, "first", new ArrayList<Student>(), new ArrayList<Lesson>()),
        new Group(2, "second", new ArrayList<Student>(), new ArrayList<Lesson>()),
        new Group(3, "third", new ArrayList<Student>(), new ArrayList<Lesson>()),
        new Group(4, "New", new ArrayList<Student>(), new ArrayList<Lesson>()));
    
    assertEquals(group, dao.save(group));
    
    assertEquals(expected, dao.findAllByOrderById());
  }  
  
  @Test 
  void createGroup_shouldThrowIllegalArgsException_whenGetNull() {
    assertThrows(InvalidDataAccessApiUsageException.class, () -> dao.save(null));
  }
  
  @Test
  void getGroupById_shouldReturnGroup_whenGetGroupExistsId() {
    Group group = dao.findById(1).orElse(null);
    Group expected = new Group(1, "first", new ArrayList<Student>(), new ArrayList<Lesson>());
    
    assertEquals(expected, group);
  }
  
  @Test
  void getGroupById_shouldThrowException_whenGroupWithProvidedIdDoesNotExists() {
    assertEquals(Optional.empty(), dao.findById(8));
  }
  
  @Test 
  void updateGroupById_shouldUpdateGroup_whenGetExistsGroupIdAndParameters() {
    Group expected = new Group(1, "Updated group", new ArrayList<Student>(), new ArrayList<Lesson>());
    assertEquals(expected, dao.save(
            new Group(1, "Updated group", new ArrayList<Student>(), new ArrayList<Lesson>())));
    assertEquals(expected, dao.findById(1).orElse(null));
  }
  
  @Test 
  void deleteGroupById_shouldDeleteGroupAndReturnOne_whenGetExistsGroupId() {
    List<Group> expected = Arrays.asList(
        new Group(1, "first", new ArrayList<Student>(), new ArrayList<Lesson>()),
        new Group(2, "second", new ArrayList<Student>(), new ArrayList<Lesson>()),
        new Group(3, "third", new ArrayList<Student>(), new ArrayList<Lesson>()));
    List<Group> groups = dao.findAllByOrderById();
    assertEquals(expected, groups);
    
    dao.deleteById(3);
    
    expected = Arrays.asList(
        new Group(1, "first", new ArrayList<Student>(), new ArrayList<Lesson>()),
        new Group(2, "second", new ArrayList<Student>(), new ArrayList<Lesson>()));
    
    groups = dao.findAllByOrderById();
    assertEquals(expected, groups);
  }
  
  @Test 
  void deleteGroupById_shouldThrowException_whenGroupDoesNotExists() {
    assertThrows(EmptyResultDataAccessException.class, () -> dao.deleteById(12));
  }
}
