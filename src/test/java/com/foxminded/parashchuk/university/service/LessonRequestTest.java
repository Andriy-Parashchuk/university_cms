package com.foxminded.parashchuk.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import com.foxminded.parashchuk.university.dao.LessonDao;
import com.foxminded.parashchuk.university.models.Lesson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class LessonRequestTest {
  @InjectMocks
  private LessonRequest lessonRequest;

  @Mock
  LessonDao dao;

  LocalDateTime time1 = LocalDateTime.of(2023, 2, 12, 15, 40);
  LocalDateTime time2 = LocalDateTime.of(2023, 3, 11, 10, 40);

  @Test
  void getAllGroups_shouldCallToGroupDaoAndReturnList_whenDbIsNotEmpty() {
    List<Lesson> expected = Arrays.asList(
            new Lesson(1, "Bio", 1, 2, time1, 22),
            new Lesson(2, "Geo", 2, 2, time2, 22));
    when(dao.getAllLessons()).thenReturn(expected);
    assertEquals(expected, lessonRequest.getAllLessons());
    verify(dao, times(1)).getAllLessons();
  }

  @Test
  void createGroup_shouldReturnResultOfInsert_whenGetGroup(){
    Lesson lesson = new Lesson(1, "Bio", 1, 2, time1, 22);
    when(dao.createLesson(lesson)).thenReturn(1);
    assertEquals(1, lessonRequest.createLesson(lesson));
    verify(dao, times(1)).createLesson(any(Lesson.class));
  }

  @Test
  void getGroupById_shouldReturnGroup_whenGetExistingId() {
    Lesson lesson = new Lesson(1, "Bio", 1, 2, time1, 22);
    when(dao.getLessonById(1)).thenReturn(Optional.of(lesson));
    assertEquals(lesson, lessonRequest.getLessonById(1));
    verify(dao, times(1)).getLessonById(anyInt());
  }

  @Test
  void updateGroupById_shouldReturnResultOfUpdate_whenGetGroupAndExistingId() {
    Lesson lesson = new Lesson(1, "Bio", 1, 2, time1, 22);
    when(dao.updateLessonById(lesson, 1)).thenReturn(1);
    assertEquals(1, lessonRequest.updateLessonById(lesson, 1));
    verify(dao, times(1)).updateLessonById(any(Lesson.class), anyInt());
  }

  @Test
  void deleteGroupById_shouldReturnResultOfDelete_whenGetId() {
    when(dao.deleteLessonById(1)).thenReturn(1);
    assertEquals(1, lessonRequest.deleteLessonById(1));
    verify(dao, times(1)).deleteLessonById(anyInt());
  }
}
