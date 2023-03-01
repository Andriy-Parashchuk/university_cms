package com.foxminded.parashchuk.university.service;

import com.foxminded.parashchuk.university.dao.StudentDao;
import com.foxminded.parashchuk.university.dao.TeacherDao;
import com.foxminded.parashchuk.university.models.Student;
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
class TeacherRequestTest {
  @InjectMocks
  TeacherRequest teacherRequest;

  @Mock
  TeacherDao dao;

  @Test
  void getAllTeachers_shouldCallToTeacherDaoAndReturnList_whenDbIsNotEmpty(){
    List<Teacher> expected = Arrays.asList(
            new Teacher(1, "Mark", "Martin"),
            new Teacher(2, "Lois", "Bread"));
    when(dao.getAllTeachers()).thenReturn(expected);
    assertEquals(expected, teacherRequest.getAllTeachers());
    verify(dao, times(1)).getAllTeachers();
  }

  @Test
  void createTeacher_shouldReturnResultOfInsert_whenGetTeacher(){
    Teacher teacher = new Teacher(1, "Mark", "Martin");
    when(dao.createTeacher(teacher)).thenReturn(1);
    assertEquals(1, teacherRequest.createTeacher(teacher));
    verify(dao, times(1)).createTeacher(any(Teacher.class));
  }

  @Test
  void getTeacherById_shouldReturnTeacher_whenGetExistingId(){
    Teacher teacher = new Teacher(1, "Mark", "Martin");
    when(dao.getTeacherById(1)).thenReturn(Optional.of(teacher));
    assertEquals(teacher, teacherRequest.getTeacherById(1));
    verify(dao, times(1)).getTeacherById(anyInt());
  }

  @Test
  void updateTeacherById_shouldReturnResultOfUpdate_whenGetGroupAndExistingId(){
    Teacher teacher = new Teacher(1, "Mark", "Martin");
    when(dao.updateTeacherById(teacher, 1)).thenReturn(1);
    assertEquals(1, teacherRequest.updateTeacherById(teacher, 1));
    verify(dao, times(1)).updateTeacherById(any(Teacher.class), anyInt());
  }

  @Test
  void deleteTeacherById_shouldReturnResultOfDelete_whenGetId(){
    when(dao.deleteTeacherById(1)).thenReturn(1);
    assertEquals(1, teacherRequest.deleteTeacherById(1));
    verify(dao, times(1)).deleteTeacherById(anyInt());
  }
}
