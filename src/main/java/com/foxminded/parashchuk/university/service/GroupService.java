package com.foxminded.parashchuk.university.service;

import com.foxminded.parashchuk.university.dao.GroupRepository;
import com.foxminded.parashchuk.university.dto.GroupDTO;
import com.foxminded.parashchuk.university.models.Group;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**Class contains requests for GroupRepository class.*/
@Service
public class GroupService {

  private final GroupRepository dao;

  private final ModelMapper mapper;

  private static final Logger log = LoggerFactory.getLogger(GroupService.class);

  @Autowired
  public GroupService(GroupRepository dao, ModelMapper mapper) {
    this.dao = dao;
    this.mapper = mapper;
  }

  /**Get all groups from table in DB.*/
  public List<GroupDTO> getAllGroups() {
    log.info("Get all data from Groups table.");
    return dao.findAllByOrderById().stream()
            .map(group -> mapper.map(group, GroupDTO.class))
            .collect(Collectors.toList());
  }

  /**Save new Group to table by group object.*/
  public GroupDTO createGroup(GroupDTO groupDto) {
    if (groupDto == null){
      log.error("Group can not be a null");
      throw new IllegalArgumentException("Group can not be a null");
    } else {
      Group group = mapper.map(groupDto, Group.class);
      log.info("Create new Group with name {}.", group.getName());
      return mapper.map(dao.save(group), GroupDTO.class);
    }
  }

  /**Get one group from table in DB by id.*/
  public GroupDTO getGroupById(int id) {
    log.info("Get Group with id {}.", id);
    Group group = dao.findById(id).orElse(null);
    if (group == null){
      log.error("Group with id {} is not found.", id);
      throw new NoSuchElementException(String.format("Group with id %d is not found.", id));
    }
    return mapper.map(group, GroupDTO.class);
  }

/**Update group by existing id in table.*/
  public GroupDTO updateGroupById(GroupDTO groupDto) {
    log.info("Update Group with id {}.", groupDto.getId());
    GroupDTO checkedGroup = getGroupById(groupDto.getId());
    if (checkedGroup == null){
      log.error("Group with id {} is not found.", groupDto.getId());
      throw new NoSuchElementException(String.format("Group with id %d is not found.", groupDto.getId()));
    }
    return mapper.map(dao.save(mapper.map(groupDto, Group.class)), GroupDTO.class);
  }

  /**Delete group by id from table in DB.*/
  public void deleteGroupById(int id) {
    log.info("Delete Group with id {}.", id);
    dao.deleteById(id);
  }
}
