package com.foxminded.parashchuk.university.dao;

import com.foxminded.parashchuk.university.models.Group;
import java.util.List;
import java.util.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.transaction.Transactional;


/**Class with call to groups table in DB. */
@Repository
@Transactional
public class GroupDao {
  
  private static final Logger log = LoggerFactory.getLogger(GroupDao.class);

  @PersistenceContext
  private EntityManager entityManager;

  
  /**Get all groups from table in DB.*/
  public List<Group> getAllGroups() {
    log.info("Get all data from Groups table.");
    return entityManager.createQuery("SELECT g FROM Group g order by g.id", Group.class).getResultList();
  }
  
  /**Save new Group to table by group object.*/
  public Group createGroup(Group group) {
    if (group == null) {
      log.error("Group can not be a null");
      throw new IllegalArgumentException("Group can not be a null");
    } else {
      log.info("Create new Group with name {}.", group.getName());
      entityManager.persist(group);
      return group;
    }
  }

  /**Get one group from table in DB by id.*/
  public Group getGroupById(int id) {
    log.info("Get Group with id {}.", id);
    Group group = entityManager.find(Group.class, id);
    if (group == null){
      log.error("Group with id {} is not found.", id);
      throw new NoSuchElementException(String.format("Group with id %d is not found.", id));
    }
    return group;
  }

  /**
   * Update group by existing id in table and group object for overwriting.
   */
  public Group updateGroupById(Group group){
    log.info("Update Group with id {}.", group.getId());
    Group checkedGroup = entityManager.find(Group.class, group.getId());
    if (checkedGroup == null){
      log.error("Group with id {} is not found.", group.getId());
      throw new NoSuchElementException(String.format("Group with id %d is not found.", group.getId()));
    }
    return entityManager.merge(group);

  }

  /**Delete group by id from table in DB.
   * Return 1 if deleting was successful*/
  public void deleteGroupById(int id) {
    log.info("Delete Group with id {}.", id);
    Group group = entityManager.find(Group.class, id);
    if (group == null){
      log.error("Group with id {} is not found.", id);
      throw new NoSuchElementException(String.format("Group with id %d is not found.", id));
    }
    entityManager.remove(group);
  }

}
