package com.foxminded.parashchuk.university.dao;

import com.foxminded.parashchuk.university.models.Group;
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


/**Class with call to groups table in DB. */
@Repository
public class GroupDao {
  
  private static final Logger log = LoggerFactory.getLogger(GroupDao.class);
  
  @Autowired
  private JdbcTemplate jdbcTemplate;
  
  @Autowired
  public GroupDao(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }
  
  RowMapper<Group> rowMapper = BeanPropertyRowMapper.newInstance(Group.class);
  
  /**Get all groups from table in DB.*/
  public List<Group> getAllGroups() {
    String sql = "select id, name from groups order by id";
    return jdbcTemplate.query(sql, rowMapper);
  }
  
  /**Save new Group to table by group object.*/
  public int createGroup(Group group) {
    if (group == null) {
      throw new IllegalArgumentException("Group can not be a null");
    } else {
      String sql = "insert into groups (name) values (?)";
      return jdbcTemplate.update(sql, group.getName());
    }
  }
  
  /**Get one group from table in DB by id.*/
  public Optional<Group> getGroupById(int id) {
    String sql = "select id, name from groups where id = ?";
    Group group = null;
    try {
      group = jdbcTemplate.queryForObject(sql, new Object[]{id}, rowMapper);
    } catch (DataAccessException exception) {
      log.error(String.format("Group with id %d not found.", id));
    }
    return Optional.ofNullable(group);
  }
  
  /**Update group by existing id in table and group object for overwriting.
   * Return 1 if overwriting was successful*/
  public int updateGroupById(Group group, int id) {
    String sql = "update groups set name = ? where id = ?"; 
    return jdbcTemplate.update(sql, group.getName(), id);
  }
  
  /**Delete group by id from table in DB.
   * Return 1 if deleting was successful*/
  public int deleteGroupById(int id) {
    return jdbcTemplate.update("delete from groups where id = ?", id);
  }

}
