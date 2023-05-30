package com.foxminded.parashchuk.university.mappers;

import com.foxminded.parashchuk.university.dto.StudentDTO;
import com.foxminded.parashchuk.university.models.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {

  public StudentDTO toDto(Student student){
    if (student == null){
      return null;
    }
    return new StudentDTO(
            student.getId(),
            student.getFirstName(),
            student.getLastName(),
            student.getGroupId(),
            student.getEmail());
  }

  public Student toStudent(StudentDTO studentDTO){
    if (studentDTO == null){
      return null;
    }
    return new Student(
            studentDTO.getId(),
            studentDTO.getFirstName(),
            studentDTO.getLastName(),
            studentDTO.getGroupId(),
            studentDTO.getEmail());
  }
}
