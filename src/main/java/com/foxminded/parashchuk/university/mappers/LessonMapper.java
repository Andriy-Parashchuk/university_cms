package com.foxminded.parashchuk.university.mappers;

import com.foxminded.parashchuk.university.dto.LessonDTO;
import com.foxminded.parashchuk.university.models.Lesson;
import org.springframework.stereotype.Component;

@Component
public class LessonMapper {

  public LessonDTO toDto(Lesson lesson){
    if (lesson == null){
      return null;
    }
    return new LessonDTO(
            lesson.getId(),
            lesson.getName(),
            lesson.getTeacherId(),
            lesson.getGroupId(),
            lesson.getTime(),
            lesson.getAudience());
  }

  public Lesson toLesson(LessonDTO lessonDTO){
    if (lessonDTO == null){
      return null;
    }
    return new Lesson(
            lessonDTO.getId(),
            lessonDTO.getName(),
            lessonDTO.getTeacherId(),
            lessonDTO.getGroupId(),
            lessonDTO.getTime(),
            lessonDTO.getAudience());
  }
}
