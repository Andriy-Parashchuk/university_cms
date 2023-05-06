package com.foxminded.parashchuk.university.service;

import com.foxminded.parashchuk.university.dao.StudentDao;
import com.foxminded.parashchuk.university.models.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**Class contains requests for StudentDao class.*/
@Service
public class StudentService {
  @Autowired
  private StudentDao dao;
  
  public List<Student> getAllStudents() {
    return dao.getAllStudents();
  }
  
  public Student createStudent(Student student) {
    return dao.createStudent(student);
  }
  
  public Student getStudentById(int id) {
    return dao.getStudentById(id);
  }
  
  public Student updateStudentById(String id, String firstName, String lastName, int groupId) {
    int studentId = Integer.parseInt(id);
    Student student = getStudentById(studentId);
    student.setFirstName(firstName);
    student.setLastName(lastName);
    student.setGroupId(groupId);
    return dao.updateStudentById(student);
  }
  
  public void deleteStudentById(int id) {
    dao.deleteStudentById(id);
  }
}
