package com.foxminded.parashchuk.university.service;

import com.foxminded.parashchuk.university.dao.LessonRepository;
import com.foxminded.parashchuk.university.dto.LessonDTO;
import com.foxminded.parashchuk.university.models.Lesson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class LessonServiceTest {
  @InjectMocks
  private LessonService lessonService;

  @Mock
  LessonRepository dao;
  @Spy
  ModelMapper mapper;

  LocalDateTime time1 = LocalDateTime.of(2023, 2, 12, 15, 40);
  LocalDateTime time2 = LocalDateTime.of(2023, 3, 11, 10, 40);

  Lesson lesson = new Lesson(1, "Bio", 1, 2, time1, 22);
  LessonDTO lessonDTO = new LessonDTO(1, "Bio", 1, 2, time1, 22);

  @Test
  void getAllLessons_shouldCallToGroupDaoAndReturnList_whenDbIsNotEmpty() {
    List<Lesson> expected = Arrays.asList(
            new Lesson(1, "Bio", 1, 2, time1, 22),
            new Lesson(2, "Geo", 2, 2, time2, 22));
    List<LessonDTO> expectedDTO = Arrays.asList(
            new LessonDTO(1, "Bio", 1, 2, time1, 22),
            new LessonDTO(2, "Geo", 2, 2, time2, 22));
    when(dao.findAllByOrderById()).thenReturn(expected);
    assertEquals(expectedDTO, lessonService.getAllLessons());
    verify(dao, times(1)).findAllByOrderById();
  }

  @Test
  void createLesson_shouldReturnResultOfInsert_whenGetGroup(){
    when(dao.save(lesson)).thenReturn(lesson);
    assertEquals(lessonDTO, lessonService.createLesson(lessonDTO));
    verify(dao, times(1)).save(any(Lesson.class));
  }

  @Test
  void getGroupById_shouldReturnGroup_whenGetExistingId() {
    when(dao.findById(1)).thenReturn(Optional.of(lesson));
    assertEquals(lessonDTO, lessonService.getLessonById(1));
    verify(dao, times(1)).findById(anyInt());
  }

  @Test
  void updateGroupById_shouldReturnResultOfUpdate_whenGetGroupAndExistingId() {
    when(dao.findById(1)).thenReturn(Optional.of(new Lesson(1, "", 1, 1, time2, 0)));
    when(dao.save(lesson)).thenReturn(lesson);
    assertEquals(lessonDTO, lessonService.updateLessonById(lessonDTO));
    verify(dao, times(1)).save(any(Lesson.class));
  }

  @Test
  void deleteGroupById_shouldReturnResultOfDelete_whenGetId() {
    lessonService.deleteLessonById(1);
    verify(dao, times(1)).deleteById(anyInt());
  }
}
