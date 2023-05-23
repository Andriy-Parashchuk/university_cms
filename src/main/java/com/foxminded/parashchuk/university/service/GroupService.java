package com.foxminded.parashchuk.university.service;

import com.foxminded.parashchuk.university.dao.GroupRepository;
import com.foxminded.parashchuk.university.models.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

/**Class contains requests for GroupRepository class.*/
@Service
public class GroupService {

  private final GroupRepository dao;

  private static final Logger log = LoggerFactory.getLogger(GroupService.class);

  @Autowired
  public GroupService(GroupRepository dao) {
    this.dao = dao;
  }

  /**Get all groups from table in DB.*/
  public List<Group> getAllGroups() {
    log.info("Get all data from Groups table.");
    return dao.findAllByOrderById();
  }

  /**Save new Group to table by group object.*/
  public Group createGroup(Group group) {
    if (group == null){
      log.error("Group can not be a null");
      throw new IllegalArgumentException("Group can not be a null");
    } else {
      log.info("Create new Group with name {}.", group.getName());
      return dao.save(group);
    }
  }

  /**Get one group from table in DB by id.*/
  public Group getGroupById(int id) {
    log.info("Get Group with id {}.", id);
    Group group = dao.findById(id).orElse(null);
    if (group == null){
      log.error("Group with id {} is not found.", id);
      throw new NoSuchElementException(String.format("Group with id %d is not found.", id));
    }
    return group;
  }

/**Update group by existing id in table.*/
  public Group updateGroupById(Group group) {
    log.info("Update Group with id {}.", group.getId());
    Group checkedGroup = getGroupById(group.getId());
    if (checkedGroup == null){
      log.error("Group with id {} is not found.", group.getId());
      throw new NoSuchElementException(String.format("Group with id %d is not found.", group.getId()));
    }
    return dao.save(group);
  }

  /**Delete group by id from table in DB.*/
  public void deleteGroupById(int id) {
    log.info("Delete Group with id {}.", id);
    dao.deleteById(id);
  }
}
