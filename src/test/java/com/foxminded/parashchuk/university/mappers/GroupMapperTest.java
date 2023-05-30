package com.foxminded.parashchuk.university.mappers;

import com.foxminded.parashchuk.university.dto.GroupDTO;
import com.foxminded.parashchuk.university.models.Group;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class GroupMapperTest {

  Group group = new Group(1, "first");
  GroupDTO groupDTO = new GroupDTO(1, "first");

  private GroupMapper mapper = new GroupMapper();

  @Test
  void toDto_shouldReturnDtoObjectOfGroup_whenGetNotNullGroup(){
    assertEquals(groupDTO, mapper.toDto(group));
  }

  @Test
  void toDto_shouldReturnNull_whenGetNull(){
    assertNull(mapper.toDto(null));
  }

  @Test
  void toGroup_shouldReturnObjectOfGroup_whenGetNotNullGroupDto(){
    assertEquals(group, mapper.toGroup(groupDTO));
  }

  @Test
  void toGroup_shouldReturnNull_whenGetNull(){
    assertNull(mapper.toGroup(null));
  }

}
