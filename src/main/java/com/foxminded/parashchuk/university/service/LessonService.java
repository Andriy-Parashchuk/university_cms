package com.foxminded.parashchuk.university.service;

import com.foxminded.parashchuk.university.dao.LessonDao;
import com.foxminded.parashchuk.university.models.Lesson;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**Class contains requests for LessonDao class.*/

@Service
public class LessonService {
  @Autowired
  private LessonDao dao;

  public List<Lesson> getAllLessons() {
    return dao.getAllLessons();
  }
  
  public Lesson createLesson(Lesson lesson) {
    return dao.createLesson(lesson);
  }
  
  public Lesson getLessonById(int id) {
    return dao.getLessonById(id);
  }
  
  public Lesson updateLessonById(String id, String name, int teacherId, int groupId,  String time, int audience) {
    int lessonId = Integer.parseInt(id);
    Lesson lesson = getLessonById(lessonId);
    lesson.setName(name);
    lesson.setTeacherId(teacherId);
    lesson.setGroupId(groupId);
    lesson.setTime(LocalDateTime.parse(time));
    lesson.setAudience(audience);
    return dao.updateLessonById(lesson);
  }
  
  public void deleteLessonById(int id) {
    dao.deleteLessonById(id);
  }

}
