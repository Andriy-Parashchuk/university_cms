package com.foxminded.parashchuk.university.dao;

import com.foxminded.parashchuk.university.models.Group;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**Class with call to groups table in DB. */
public interface GroupRepository extends CrudRepository<Group, Integer> {

  List<Group> findAllByOrderById();

}
