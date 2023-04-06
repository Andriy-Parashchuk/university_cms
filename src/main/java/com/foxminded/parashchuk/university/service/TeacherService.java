package com.foxminded.parashchuk.university.service;

import com.foxminded.parashchuk.university.dao.TeacherDao;
import com.foxminded.parashchuk.university.models.Teacher;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**Class contains requests for TeacherDao class.*/
@Service
public class TeacherService {

  @Autowired
  private TeacherDao dao;
  
  public List<Teacher> getAllTeachers() {
    return dao.getAllTeachers();
  }
  
  public int createTeacher(Teacher teacher) {
    return dao.createTeacher(teacher);
  }
  
  public Teacher getTeacherById(int id) {
    Optional<Teacher> result = dao.getTeacherById(id);
    return result.orElse(null);
  }
  
  public int updateTeacherById(String id, String firstName, String lastName, int audience, String department) {
    int teacherId = Integer.parseInt(id);
    Teacher teacher = getTeacherById(teacherId);
    teacher.setFirstName(firstName);
    teacher.setLastName(lastName);
    teacher.setAudience(audience);
    teacher.setDepartment(department);
    return dao.updateTeacherById(teacher, teacherId);
  }
  
  public int deleteTeacherById(int id) {
    return dao.deleteTeacherById(id);
  }
}
