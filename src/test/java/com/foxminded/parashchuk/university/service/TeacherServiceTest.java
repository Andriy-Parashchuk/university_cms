package com.foxminded.parashchuk.university.service;

import com.foxminded.parashchuk.university.dao.TeacherDao;
import com.foxminded.parashchuk.university.models.Teacher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {
  @InjectMocks
  TeacherService teacherService;

  @Mock
  TeacherDao dao;

  @Test
  void getAllTeachers_shouldCallToTeacherDaoAndReturnList_whenDbIsNotEmpty(){
    List<Teacher> expected = Arrays.asList(
            new Teacher(1, "Mark", "Martin"),
            new Teacher(2, "Lois", "Bread"));
    when(dao.getAllTeachers()).thenReturn(expected);
    assertEquals(expected, teacherService.getAllTeachers());
    verify(dao, times(1)).getAllTeachers();
  }

  @Test
  void createTeacher_shouldReturnResultOfInsert_whenGetTeacher(){
    Teacher teacher = new Teacher(1, "Mark", "Martin");
    when(dao.createTeacher(teacher)).thenReturn(teacher);
    assertEquals(teacher, teacherService.createTeacher(teacher));
    verify(dao, times(1)).createTeacher(any(Teacher.class));
  }

  @Test
  void getTeacherById_shouldReturnTeacher_whenGetExistingId(){
    Teacher teacher = new Teacher(1, "Mark", "Martin");
    when(dao.getTeacherById(1)).thenReturn(teacher);
    assertEquals(teacher, teacherService.getTeacherById(1));
    verify(dao, times(1)).getTeacherById(anyInt());
  }

  @Test
  void updateTeacherById_shouldReturnResultOfUpdate_whenGetGroupAndExistingId(){
    Teacher teacher = new Teacher(1, "Mark", "Martin");
    when(dao.getTeacherById(1)).thenReturn(new Teacher(1, "", ""));
    when(dao.updateTeacherById(teacher)).thenReturn(teacher);
    assertEquals(teacher, teacherService.updateTeacherById("1", "Mark", "Martin", 0, null));
    verify(dao, times(1)).updateTeacherById(any(Teacher.class));
  }

  @Test
  void deleteTeacherById_shouldReturnResultOfDelete_whenGetId(){
    teacherService.deleteTeacherById(1);
    verify(dao, times(1)).deleteTeacherById(anyInt());
  }
}
