package com.foxminded.parashchuk.university.mappers;

import com.foxminded.parashchuk.university.dto.TeacherDTO;
import com.foxminded.parashchuk.university.models.Teacher;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TeacherMapperTest {
  Teacher teacher = new Teacher(1, "firstname", null, "test@test.test");

  TeacherDTO teacherDTO =  new TeacherDTO(1, "firstname", null, "test@test.test");

  TeacherMapper mapper = new TeacherMapper();

  @Test
  void toDto_shouldReturnDtoObjectOfTeacher_whenGetNotNullTeacher() {
    assertEquals(teacherDTO, mapper.toDto(teacher));
  }

  @Test
  void toDto_shouldReturnNull_whenGetNull() {
    assertNull(mapper.toDto(null));
  }

  @Test
  void toTeacher_shouldReturnObjectOfTeacher_whenGetNotNullTeacherDto() {
    assertEquals(teacher, mapper.toTeacher(teacherDTO));
  }

  @Test
  void toTeacher_shouldReturnNull_whenGetNull() {
    assertNull(mapper.toTeacher(null));
  }
}
