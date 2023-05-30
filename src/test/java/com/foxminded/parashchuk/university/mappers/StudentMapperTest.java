package com.foxminded.parashchuk.university.mappers;

import com.foxminded.parashchuk.university.dto.StudentDTO;
import com.foxminded.parashchuk.university.models.Student;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class StudentMapperTest {
  StudentMapper mapper = new StudentMapper();

  Student student = new Student(1, null, "lastname", 1, "test@test.test");

  StudentDTO studentDTO = new StudentDTO(1, null, "lastname", 1, "test@test.test");;

  @Test
  void toDto_shouldReturnDtoObjectOfStudent_whenGetNotNullStudent() {
    assertEquals(studentDTO, mapper.toDto(student));
  }

  @Test
  void toDto_shouldReturnNull_whenGetNull() {
    assertNull(mapper.toDto(null));
  }

  @Test
  void toStudent_shouldReturnObjectOfStudent_whenGetNotNullStudentDTO() {
    assertEquals(studentDTO, mapper.toDto(student));
  }

  @Test
  void toStudent_shouldReturnNull_whenGetNull() {
    assertNull(mapper.toDto(null));
  }

}
