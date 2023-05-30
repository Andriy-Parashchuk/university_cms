package com.foxminded.parashchuk.university.mappers;

import com.foxminded.parashchuk.university.dto.GroupDTO;
import com.foxminded.parashchuk.university.models.Group;
import org.springframework.stereotype.Component;

@Component
public class GroupMapper {
  public GroupDTO toDto(Group group){
    if (group == null){
      return null;
    }
    return new GroupDTO(group.getId(), group.getName());
  }

  public Group toGroup(GroupDTO groupDto){
    if (groupDto == null){
      return null;
    }
    return new Group(groupDto.getId(), groupDto.getName());
  }

}
