package com.foxminded.parashchuk.university.controller;

import com.foxminded.parashchuk.university.models.Group;
import com.foxminded.parashchuk.university.service.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
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
    List<Group> groups = service.getAllGroups();
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
      Group group = service.getGroupById(Integer.parseInt(groupId));
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
  public String groupCreateForm(){
    log.info("Show form for creating new group.");
    return "create/group_new";
  }

  /**Get new info from fields on the page for edit existing Group.*/
  @PostMapping("/{groupId}")
  public String groupEdit(@RequestParam String name, @PathVariable String groupId,
                          RedirectAttributes redirectAttributes){
    service.updateGroupById(groupId, name);
    log.info("Group with id {} was updated.", groupId);
    redirectAttributes.addFlashAttribute("success_message",
            "Group with id " + groupId +" was updated.");
    return "redirect:/groups/all";
  }

  /**Get info from fields on the page for creating new Group.*/
  @PostMapping("/new")
  public String groupCreate(@RequestParam String name, RedirectAttributes redirectAttributes){
    Group group = new Group(0, name);
    service.createGroup(group);
    log.info("New group was created");
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
    } catch (NoSuchElementException e){
      log.error("Group with id {} does not exists.", groupId);
      redirectAttributes.addFlashAttribute("danger_message",
              "Group with id " + groupId + " does not exists.");
      return "redirect:/groups/all";
    }
  }
}
