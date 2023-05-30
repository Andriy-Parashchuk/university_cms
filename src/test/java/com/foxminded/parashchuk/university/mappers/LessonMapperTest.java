package com.foxminded.parashchuk.university.mappers;

import com.foxminded.parashchuk.university.dto.LessonDTO;
import com.foxminded.parashchuk.university.models.Lesson;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class LessonMapperTest {
  Lesson lesson = new Lesson(1, "Bio", 1, 2, null, 22);

  LessonDTO lessonDTO = new LessonDTO(1, "Bio", 1, 2, null, 22);;

  LessonMapper mapper = new LessonMapper();

  @Test
  void toDto_shouldReturnLessonDto_whenGetNotNullLesson(){
    assertEquals(lessonDTO, mapper.toDto(lesson));
  }

  @Test
  void toDto_shouldReturnNull_whenGetNull(){
    assertNull(mapper.toDto(null));
  }

  @Test
  void toLesson_shouldReturnLesson_whenGetNotNullLessonDto(){
    assertEquals(lessonDTO, mapper.toDto(lesson));
  }

  @Test
  void toLesson_shouldReturnNull_whenGetNull(){
    assertNull(mapper.toLesson(null));
  }

}
