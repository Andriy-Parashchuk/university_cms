package com.foxminded.parashchuk.university.service;

import com.foxminded.parashchuk.university.dao.TeacherRepository;
import com.foxminded.parashchuk.university.models.Teacher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {
  @InjectMocks
  TeacherService teacherService;

  @Mock
  TeacherRepository dao;

  @Test
  void getAllTeachers_shouldCallToTeacherDaoAndReturnList_whenDbIsNotEmpty(){
    List<Teacher> expected = Arrays.asList(
            new Teacher(1, "Mark", "Martin", "mark@testmail.com"),
            new Teacher(2, "Lois", "Bread", "lois@testmail.com"));
    when(dao.findAllByOrderById()).thenReturn(expected);
    assertEquals(expected, teacherService.getAllTeachers());
    verify(dao, times(1)).findAllByOrderById();
  }

  @Test
  void createTeacher_shouldReturnResultOfInsert_whenGetTeacher(){
    Teacher teacher = new Teacher(1, "Mark", "Martin", "mark@testmail.com");
    when(dao.save(teacher)).thenReturn(teacher);
    assertEquals(teacher, teacherService.createTeacher(teacher));
    verify(dao, times(1)).save(any(Teacher.class));
  }

  @Test
  void getTeacherById_shouldReturnTeacher_whenGetExistingId(){
    Teacher teacher = new Teacher(1, "Mark", "Martin", "mark@testmail.com");
    when(dao.findById(1)).thenReturn(Optional.of(teacher));
    assertEquals(teacher, teacherService.getTeacherById(1));
    verify(dao, times(1)).findById(anyInt());
  }

  @Test
  void updateTeacherById_shouldReturnResultOfUpdate_whenGetGroupAndExistingId(){
    Teacher teacher = new Teacher(1, "Mark", "Martin", "mark@testmail.com");
    when(dao.findById(1)).thenReturn(Optional.of(new Teacher(1, "", "", "")));
    when(dao.save(teacher)).thenReturn(teacher);
    assertEquals(teacher, teacherService.updateTeacherById(teacher));
    verify(dao, times(1)).save(any(Teacher.class));
  }

  @Test
  void deleteTeacherById_shouldReturnResultOfDelete_whenGetId(){
    teacherService.deleteTeacherById(1);
    verify(dao, times(1)).deleteById(anyInt());
  }
}
