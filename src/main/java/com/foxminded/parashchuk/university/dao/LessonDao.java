package com.foxminded.parashchuk.university.dao;

import com.foxminded.parashchuk.university.models.Lesson;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**Class with call to lessons table in DB. */
@Repository
public class LessonDao {
  private static final Logger log = LoggerFactory.getLogger(LessonDao.class);
  @Autowired
  private JdbcTemplate jdbcTemplate;
  
  public LessonDao(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }
  
  RowMapper<Lesson> rowMapper = BeanPropertyRowMapper.newInstance(Lesson.class);
  
  /**Get all lessons from table in DB.*/
  public List<Lesson> getAllLessons() {
    String sql = "select id, name, teacher_id, group_id, \"time\", audience "
        + "from lessons order by \"time\"";
    return jdbcTemplate.query(sql, rowMapper);
  }
  
  /**Save new Lesson to table by lesson object.*/
  public int createLesson(Lesson lesson) {
    if (lesson == null) {
      throw new IllegalArgumentException("Teacher can not be a null");
    } else {
      String sql = "insert into lessons (name, teacher_id, group_id, \"time\", audience) "
          + "values (?, ?, ?, ?, ?)";
      return jdbcTemplate.update(
          sql, 
          lesson.getName(),
          lesson.getTeacherId(),
          lesson.getGroupId(),
          Timestamp.valueOf(lesson.getTime()),
          lesson.getAudience());
    }
  }
  
  /**Get one lesson from table in DB by id.*/
  public Optional<Lesson> getLessonById(int id) {
    String sql = "select id, name, teacher_id, group_id, \"time\", audience "
        + "from lessons where id = ?";
    Lesson lesson = null;
    try {
      lesson = jdbcTemplate.queryForObject(sql, new Object[]{id}, rowMapper);
    } catch (DataAccessException exception) {
      log.error(String.format("Lesson with id %d not found.", id));
    }
    return Optional.ofNullable(lesson);
  }
  
  /**Update lesson by existing id in table and lesson object for overwriting.
   * Return 1 if overwriting was successful*/
  public int updateLessonById(Lesson lesson, int id) {
    String sql = "update lessons set name = ?, teacher_id = ?, group_id = ?, \"time\" = ?, "
        + "audience = ? where id = ?"; 
    return jdbcTemplate.update(
        sql, 
        lesson.getName(), 
        lesson.getTeacherId(),
        lesson.getGroupId(),
        lesson.getTime(),
        lesson.getAudience(),
        id);
  }
  
  /**Delete lesson by id from table in DB.
   * Return 1 if deleting was successful*/
  public int deleteLessonById(int id) {
    return jdbcTemplate.update("delete from lessons where id = ?", id);
  }
  
  
}