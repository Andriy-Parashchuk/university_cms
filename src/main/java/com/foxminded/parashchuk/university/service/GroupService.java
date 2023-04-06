package com.foxminded.parashchuk.university.service;

import com.foxminded.parashchuk.university.dao.GroupDao;
import com.foxminded.parashchuk.university.models.Group;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**Class contains requests for GroupDao class.*/
@Service
public class GroupService {

  private GroupDao dao;

  @Autowired
  public GroupService(GroupDao dao) {
    this.dao = dao;
  }

  public List<Group> getAllGroups() {
    return dao.getAllGroups();
  }
  
  public int createGroup(Group group) {
    return dao.createGroup(group);
  }
  
  public Group getGroupById(int id) {
    Optional<Group> result = dao.getGroupById(id);
    return result.orElse(null);
  }
  
  public int updateGroupById(String id, String name) {
    int groupId = Integer.parseInt(id);
    Group group = getGroupById(groupId);
    group.setName(name);
    return dao.updateGroupById(group, groupId);
  }
  
  public int deleteGroupById(int id) {
    return dao.deleteGroupById(id);
  }
}
