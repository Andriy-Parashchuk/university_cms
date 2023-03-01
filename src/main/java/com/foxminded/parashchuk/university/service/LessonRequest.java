package com.foxminded.parashchuk.university.service;

import com.foxminded.parashchuk.university.dao.LessonDao;
import com.foxminded.parashchuk.university.models.Lesson;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**Class contains requests for LessonDao class.*/

@Service
public class LessonRequest {
  @Autowired
  private LessonDao dao;

  public List<Lesson> getAllLessons() {
    return dao.getAllLessons();
  }
  
  public int createLesson(Lesson lesson) {
    return dao.createLesson(lesson);
  }
  
  public Lesson getLessonById(int id) {
    Optional<Lesson> result = dao.getLessonById(id);
    return result.orElse(null);
  }
  
  public int updateLessonById(Lesson lesson, int id) {
    return dao.updateLessonById(lesson, id);
  }
  
  public int deleteLessonById(int id) {
    return dao.deleteLessonById(id);
  }

}
