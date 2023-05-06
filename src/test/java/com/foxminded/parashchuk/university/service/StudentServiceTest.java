package com.foxminded.parashchuk.university.service;

import com.foxminded.parashchuk.university.dao.StudentDao;
import com.foxminded.parashchuk.university.models.Student;
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
class StudentServiceTest {
  @InjectMocks
  StudentService studentService;

  @Mock
  StudentDao dao;

  @Test
  void getAllStudents_shouldCallToStudentDaoAndReturnList_whenDbIsNotEmpty(){
    List<Student> expected = Arrays.asList(
            new Student(1, "Mark", "Martin", 1),
            new Student(2, "Lois", "Bread", 2));
    when(dao.getAllStudents()).thenReturn(expected);
    assertEquals(expected, studentService.getAllStudents());
    verify(dao, times(1)).getAllStudents();
  }

  @Test
  void createStudent_shouldReturnResultOfInsert_whenGetStudent(){
    Student student = new Student(1, "Mark", "Martin", 1);
    when(dao.createStudent(student)).thenReturn(student);
    assertEquals(student, studentService.createStudent(student));
    verify(dao, times(1)).createStudent(any(Student.class));
  }

  @Test
  void getStudentById_shouldReturnStudent_whenGetExistingId(){
    Student student = new Student(1, "Mark", "Martin", 1);
    when(dao.getStudentById(1)).thenReturn(student);
    assertEquals(student, studentService.getStudentById(1));
    verify(dao, times(1)).getStudentById(anyInt());
  }

  @Test
  void updateStudentById_shouldReturnResultOfUpdate_whenGetStudentAndExistingId(){
    Student student = new Student(1, "Mark", "Martin", 1);
    when(dao.getStudentById(1)).thenReturn(new Student(1, "", "", 1));
    when(dao.updateStudentById(student)).thenReturn(student);
    assertEquals(student, studentService.updateStudentById("1", "Mark", "Martin", 1));
    verify(dao, times(1)).updateStudentById(any(Student.class));
  }

  @Test
  void deleteStudentById_shouldReturnResultOfDelete_whenGetId(){
    studentService.deleteStudentById(1);
    verify(dao, times(1)).deleteStudentById(anyInt());
  }
}
