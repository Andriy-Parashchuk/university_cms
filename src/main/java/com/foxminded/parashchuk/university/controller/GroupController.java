package com.foxminded.parashchuk.university.controller;

import com.foxminded.parashchuk.university.dto.GroupDTO;
import com.foxminded.parashchuk.university.models.Group;
import com.foxminded.parashchuk.university.service.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**Class for connecting UI with Group model.*/
@Controller
@RequestMapping("/groups")
public class GroupController {
  @Autowired
  private GroupService service;
  private static final Logger log = LoggerFactory.getLogger(GroupController.class);

  /**Return all Groups from database and show them to UI.*/
  @GetMapping("/all")
  public String showAllGroups(Model model){
    List<GroupDTO> groups = service.getAllGroups();
    model.addAttribute("groups", groups);
    log.info("All data from groups was transfer to web-page");
    return "all_groups";
  }

  /**Get id from field on the page and show edit page for this Group.*/
  @PostMapping("/all")
  public String findGroupById(@RequestParam(defaultValue="0") int id, Model model){
    model.addAttribute("id", id);
    log.info("Request for finding group by id {}", id);
    return "redirect:/groups/" + id;
  }

  /**Return Group with id in path from database and show it to edit page.*/
  @GetMapping("/{groupId}")
  public String groupEditForm(Model model, @PathVariable String groupId, RedirectAttributes redirectAttributes){
    try{
      GroupDTO group = service.getGroupById(Integer.parseInt(groupId));
      model.addAttribute("group", group);
      log.info("Show edit form for group with id {}", groupId);
      return "edit/group_edit";
    } catch (NoSuchElementException exception) {
      log.error("Group with id {} does not exists.", groupId);
      redirectAttributes.addFlashAttribute("danger_message",
              "Group with id " + groupId + " does not exists.");
      return "redirect:/groups/all";
    }
  }

  /**Show page for creating new Group.*/
  @GetMapping("/new")
  public String groupCreateForm(Model model){
    model.addAttribute("group", new GroupDTO());
    log.info("Show form for creating new group.");
    return "create/group_new";
  }

  /**Get new info from fields on the page for edit existing Group.*/
  @PostMapping("/{groupId}")
  public String groupEdit(@Valid GroupDTO group, BindingResult bindingResult, Model model,
                          @PathVariable String groupId,
                          RedirectAttributes redirectAttributes){
    group.setId(Integer.parseInt(groupId));
    if (bindingResult.hasErrors()){
      Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
      model.addAttribute("group", group);
      model.mergeAttributes(errorsMap);
      log.error("Some fields get errors: {}", bindingResult.getModel());
      return "edit/group_edit";
    }
    service.updateGroupById(group);
    log.info("Group with id {} was updated.", groupId);
    redirectAttributes.addFlashAttribute("success_message",
            "Group with id " + groupId +" was updated.");
    return "redirect:/groups/all";
  }

  /**Get info from fields on the page for creating new Group.*/
  @PostMapping("/new")
  public String groupCreate(@Valid GroupDTO group, BindingResult bindingResult,
                            Model model, RedirectAttributes redirectAttributes){
    if (bindingResult.hasErrors()){
      log.error("Some fields get errors: {}", bindingResult.getModel());
      Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
      model.addAttribute("group", group);
      model.mergeAttributes(errorsMap);
      return "create/group_new";
    }
    GroupDTO savedGroup = service.createGroup(group);
    log.info("New group was created with name {}", savedGroup.getName());
    redirectAttributes.addFlashAttribute("success_message",
            "New group was created");
    return "redirect:/groups/all";
  }


  /**Delete Group by existing id.*/
  @GetMapping("/delete/{groupId}")
  public String deleteGroup(@PathVariable String groupId, RedirectAttributes redirectAttributes){
    try {
      service.deleteGroupById(Integer.parseInt(groupId));
      redirectAttributes.addFlashAttribute("success_message",
              "Group with id "+ groupId +" was deleted.");
      log.info("Group with id {} was deleted.", groupId);
      return "redirect:/groups/all";
    } catch (EmptyResultDataAccessException e){
      log.error("Group with id {} does not exists.", groupId);
      redirectAttributes.addFlashAttribute("danger_message",
              "Group with id " + groupId + " does not exists.");
      return "redirect:/groups/all";
    }
  }
}
