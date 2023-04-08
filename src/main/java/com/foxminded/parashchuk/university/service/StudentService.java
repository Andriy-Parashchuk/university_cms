package com.foxminded.parashchuk.university.service;

import com.foxminded.parashchuk.university.dao.StudentDao;
import com.foxminded.parashchuk.university.models.Student;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**Class contains requests for StudentDao class.*/
@Service
public class StudentService {
  @Autowired
  private StudentDao dao;
  
  public List<Student> getAllStudents() {
    return dao.getAllStudents();
  }
  
  public int createStudent(Student student) {
    return dao.createStudent(student);
  }
  
  public Student getStudentById(int id) {
    Optional<Student> result = dao.getStudentById(id);
    return result.orElse(null);
  }
  
  public int updateStudentById(String id, String firstName, String lastName, int groupId) {
    int studentId = Integer.parseInt(id);
    Student student = getStudentById(studentId);
    student.setFirstName(firstName);
    student.setLastName(lastName);
    student.setGroupId(groupId);
    return dao.updateStudentById(student, studentId);
  }
  
  public int deleteStudentById(int id) {
    return dao.deleteStudentById(id);
  }
}
