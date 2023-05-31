package com.foxminded.parashchuk.university.service;

import com.foxminded.parashchuk.university.dao.LessonRepository;
import com.foxminded.parashchuk.university.dto.LessonDTO;
import com.foxminded.parashchuk.university.models.Lesson;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**Class contains requests for LessonDao class.*/

@Service
public class LessonService {
  @Autowired
  private LessonRepository dao;
  @Autowired
  private ModelMapper mapper;

  private static final Logger log = LoggerFactory.getLogger(LessonService.class);


  /**Get all lessons from table in DB.*/
  public List<LessonDTO> getAllLessons() {
    log.info("Get all data from Lessons table.");
    return dao.findAllByOrderById().stream()
            .map(lesson -> mapper.map(lesson, LessonDTO.class))
            .collect(Collectors.toList());
  }

  /**Save new Lesson to table by lesson object.*/
  public LessonDTO createLesson(LessonDTO lessonDTO) {
    if (lessonDTO == null) {
      log.error("Lesson can not be a null");
      throw new IllegalArgumentException("Lesson can not be a null");
    } else {
      log.info("Create new Lesson with name {}.", lessonDTO.getName());
      return mapper.map(dao.save(mapper.map(lessonDTO, Lesson.class)), LessonDTO.class);
    }
  }

  /**Get one lesson from table in DB by id.*/
  public LessonDTO getLessonById(int id) {
    log.info("Get Lesson with id {}.", id);
    Lesson lesson = dao.findById(id).orElse(null);
    if (lesson == null) {
      log.error("Lesson with id {} is not found.", id);
      throw new NoSuchElementException(String.format("Lesson with id %d is not found.", id));
    }
    return mapper.map(lesson, LessonDTO.class);
  }

  /**Update lesson by existing id in table.*/
  public LessonDTO updateLessonById(LessonDTO lessonDTO) {
    log.info("Update Lesson with id {}.", lessonDTO.getId());
    LessonDTO checkedLesson = getLessonById(lessonDTO.getId());
    if (checkedLesson == null){
      log.error("Lesson with id {} is not found.", lessonDTO.getId());
      throw new NoSuchElementException(String.format("Lesson with id %d is not found.", lessonDTO.getId()));
    }
    return  mapper.map(dao.save(mapper.map(lessonDTO, Lesson.class)), LessonDTO.class);
  }

  /**Delete lesson by id from table in DB.*/
  public void deleteLessonById(int id) {
    log.info("Delete Lesson with id {}.", id);
    dao.deleteById(id);
  }
}
