package com.foxminded.parashchuk.university.mappers;

import com.foxminded.parashchuk.university.dto.TeacherDTO;
import com.foxminded.parashchuk.university.models.Teacher;
import org.springframework.stereotype.Component;

@Component
public class TeacherMapper {
  public TeacherDTO toDto(Teacher teacher){
    if (teacher == null){
      return null;
    }
    TeacherDTO teacherDTO = new TeacherDTO(
            teacher.getId(),
            teacher.getFirstName(),
            teacher.getLastName(),
            teacher.getEmail());
    teacherDTO.setAudience(teacher.getAudience());
    teacherDTO.setDepartment(teacher.getDepartment());

    return teacherDTO;
  }

  public Teacher toTeacher(TeacherDTO teacherDTO){
    if (teacherDTO == null){
      return null;
    }
    Teacher teacher = new Teacher(
            teacherDTO.getId(),
            teacherDTO.getFirstName(),
            teacherDTO.getLastName(),
            teacherDTO.getEmail());
    teacher.setAudience(teacherDTO.getAudience());
    teacher.setDepartment(teacherDTO.getDepartment());
    return teacher;
  }

}
