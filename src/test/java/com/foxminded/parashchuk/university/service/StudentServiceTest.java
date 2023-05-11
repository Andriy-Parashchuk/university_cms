package com.foxminded.parashchuk.university.service;

import com.foxminded.parashchuk.university.dao.StudentRepository;
import com.foxminded.parashchuk.university.models.Student;
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
class StudentServiceTest {
  @InjectMocks
  StudentService studentService;

  @Mock
  StudentRepository dao;

  @Test
  void getAllStudents_shouldCallToStudentDaoAndReturnList_whenDbIsNotEmpty(){
    List<Student> expected = Arrays.asList(
            new Student(1, "Mark", "Martin", 1),
            new Student(2, "Lois", "Bread", 2));
    when(dao.findAllByOrderById()).thenReturn(expected);
    assertEquals(expected, studentService.getAllStudents());
    verify(dao, times(1)).findAllByOrderById();
  }

  @Test
  void createStudent_shouldReturnResultOfInsert_whenGetStudent(){
    Student student = new Student(1, "Mark", "Martin", 1);
    when(dao.save(student)).thenReturn(student);
    assertEquals(student, studentService.createStudent(student));
    verify(dao, times(1)).save(any(Student.class));
  }

  @Test
  void getStudentById_shouldReturnStudent_whenGetExistingId(){
    Student student = new Student(1, "Mark", "Martin", 1);
    when(dao.findById(1)).thenReturn(Optional.of(student));
    assertEquals(student, studentService.getStudentById(1));
    verify(dao, times(1)).findById(anyInt());
  }

  @Test
  void updateStudentById_shouldReturnResultOfUpdate_whenGetStudentAndExistingId(){
    Student student = new Student(1, "Mark", "Martin", 1);
    when(dao.findById(1)).thenReturn(Optional.of(new Student(1, "", "", 1)));
    when(dao.save(student)).thenReturn(student);
    assertEquals(student, studentService.updateStudentById("1", "Mark", "Martin", 1));
    verify(dao, times(1)).save(any(Student.class));
  }

  @Test
  void deleteStudentById_shouldReturnResultOfDelete_whenGetId(){
    studentService.deleteStudentById(1);
    verify(dao, times(1)).deleteById(anyInt());
  }
}
