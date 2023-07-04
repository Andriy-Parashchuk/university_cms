package com.foxminded.parashchuk.university.api;

import com.foxminded.parashchuk.university.dto.GroupDTO;
import com.foxminded.parashchuk.university.service.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;


/**Class for connecting UI with Group model via REST api.*/

@RestController
@RequestMapping("/api/groups")
public class GroupApiController {
  private static final Logger log = LoggerFactory.getLogger(GroupApiController.class);

  @Autowired
  private GroupService service;

  /**Return all Groups from database.*/
  @GetMapping
  public List<GroupDTO> getAll(){
    log.info("All data from groups was transfer to REST api");
    return service.getAllGroups();
  }

  /**Return Group with id in path from database and show it to edit page.*/
  @GetMapping("/{groupId}")
  public GroupDTO getById(@PathVariable String groupId){
    log.info("Show edit form for group with id {} for REST api", groupId);
    return service.getGroupById(Integer.parseInt(groupId));
  }

  /**Get info from fields on the page for creating new Group.*/
  @PostMapping
  public ResponseEntity<GroupDTO> create(@Valid @RequestBody GroupDTO groupDTO){
    GroupDTO savedGroup = service.createGroup(groupDTO);
    log.info("New group was created with name {}", savedGroup.getName());
    return new ResponseEntity<>(savedGroup, HttpStatus.CREATED);
  }

  /**Get new info from fields on the page for update existing Group.*/
  @PutMapping("/{groupId}")
  public ResponseEntity<GroupDTO> update(@PathVariable String groupId, @Valid @RequestBody GroupDTO groupDTO){
    groupDTO.setId(Integer.parseInt(groupId));
    GroupDTO updatedGroup = service.updateGroupById(groupDTO);
    log.info("Group with id {} was updated via REST.", groupId);
    return ResponseEntity.ok(updatedGroup);
  }

  /**Delete Group by existing id.*/
  @DeleteMapping("/{groupId}")
  public ResponseEntity<String> delete(@PathVariable String groupId){
    service.deleteGroupById(Integer.parseInt(groupId));
    log.info("Group with id {} was deleted via REST.", groupId);
    return ResponseEntity.noContent().build();
  }

  /**Handler for Validation Exception.*/
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Map<String, String> handleValidationExceptions(
          MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach(error -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    log.error("Something gone wrong {}", errors);
    return errors;
  }

  /**Handler for Deleting and Not Exists Exceptions.*/
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({NoSuchElementException.class, EmptyResultDataAccessException.class})
  public Map<String, String> handleNoElementException() {
    Map<String, String> errors = new HashMap<>();
    errors.put("error", "This group does not exists.");
    log.error("Something gone wrong {}", errors);
    return errors;
  }


}
