package com.chilborne.todoapi.web.controller.v1;

import com.chilborne.todoapi.persistance.dto.TaskDto;
import com.chilborne.todoapi.persistance.dto.ToDoListDto;
import com.chilborne.todoapi.persistance.validation.OnPersist;
import com.chilborne.todoapi.service.ToDoListService;
import com.chilborne.todoapi.service.ToDoListServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Api(value = "To Do List Controller")
@RestController
@RequestMapping(path = "api/v1/list", consumes = "application/json", produces = "application/json")
public class ToDoListController {

  public static final String TO_DO_LIST_ROOT_URL = "http://localhost:8080/vi/list";

  private final ToDoListService service;

  private final Logger logger = LoggerFactory.getLogger(ToDoListController.class);

  public ToDoListController(ToDoListServiceImpl service) {
    this.service = service;
  }

  @ApiOperation(value = "Find to_do_list by Id")
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 404,
            message = "ToDoListNotFoundException -> to_do_list with id:{id} belonging to User:{username} not found")
      })
  @GetMapping(value = "/{id}")
  public ResponseEntity<ToDoListDto> getToDoListById(@PathVariable long id, Principal principal) {
    logger.info("Processing GET Request for ToDoList (id: " + id + ")");
    ToDoListDto result = service.getToDoListDtoByIdAndUsername(id, principal.getName());
    return ResponseEntity.ok(result);
  }

  @ApiOperation(value = "Find All to_do_lit belonging to logged in user")
  @GetMapping(value = "/all")
  public ResponseEntity<List<ToDoListDto>> getAllToDoLists(Principal principal) {
    logger.debug("Processing GET Request for all ToDoLists beloning to User:{}", principal.getName());
    List<ToDoListDto> result = service.getAllToDoList(principal.getName());
    return ResponseEntity.ok(result);
  }

  @ApiOperation(value = "New to_do_list", code = 401)
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 400,
            message = "InvalidDataException: { {to_do_list_property} : {constraint_message} }")
      })
  @PostMapping(value = "")
  @Validated({OnPersist.class})
  public ResponseEntity<ToDoListDto> newToDoList(@Valid @RequestBody ToDoListDto list, Principal principal) {
    logger.info("Processing POST request for new ToDoList");
    ToDoListDto result = service.newToDoList(list, principal.getName());
    return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }

  @ApiOperation(
      value = "Update to_do_list",
      notes = "Any fields provided will be updated, except list_id and date_time_created")
  @PutMapping(value = "/{id}")
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 404,
            message = "ToDoListNotFoundException -> to_do_list with id:{id} not found"),
        @ApiResponse(
            code = 400,
            message = "InvalidDataException: { {to_do_list_property} : {constraint_message} }")
      })
  public ResponseEntity<ToDoListDto> updateToDoList(
      @PathVariable long id, @Valid @RequestBody ToDoListDto toDoList, Principal principal) {
    logger.info("Processing PUT Request to update ToDoList:{} to {}", id, toDoList.toString());
    ToDoListDto result = service.updateToDoList(id, toDoList, principal.getName());
    return ResponseEntity.ok(result);
  }

  @ApiOperation(value = "Delete to_do_list")
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 404,
            message = "ToDoListNotFoundException -> to_do_list with id:{id} belonging to User:{username} not found")
      })
  @DeleteMapping(value = "/{id}")
  public ResponseEntity deleteToDoList(@PathVariable long id, Principal principal) {
    logger.info("Processing DELETE Request for List id:" + id);
    service.deleteToDoList(id, principal.getName());
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @ApiOperation(value = "Change whether to_do_list is active")
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 404,
            message = "ToDoListNotFoundException -> to_do_list with id:{id} belonging to User:{username} not found")
      })
  @PatchMapping(value = "/{id}/active/{active}")
  public ResponseEntity<ToDoListDto> setActive(
      @PathVariable long id, @PathVariable boolean active, Principal principal) {
    logger.info(String.format("Setting Active of ToDoList (id: %d) to %b", id, active));
    ToDoListDto result = service.setToDoListActive(id, principal.getName(), active);
    return ResponseEntity.ok(result);
  }

  @ApiOperation(
      value = "Add a new task to to_do_list",
      notes = "Must provide Task to be added in Request Body")
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 404,
            message = "ToDoListNotFoundException -> to_do_list with id:{id} not found"),
        @ApiResponse(
            code = 400,
            message = "InvalidDataException: { {task_property} : {constraint_message} }")
      })
  @PatchMapping(value = "/{id}/task/add")
  @Validated({OnPersist.class})
  public ResponseEntity<ToDoListDto> addTaskToList(
      @PathVariable long id, @Valid @RequestBody TaskDto task, Principal principal) {
    logger.info(
        String.format(
            "Processing PATCH Request to add new Task (name: %s) to ToDoList (id: %d)",
            task.getName(), id));
    ToDoListDto result = service.addTaskToDoList(id, principal.getName(), task);
    return ResponseEntity.ok(result);
  }

  @ApiOperation(value = "Delete task from to_do_list")
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 404,
            message = "ToDoListNotFoundException -> to_do_list with id:{id} not found"),
        @ApiResponse(
            code = 404,
            message =
                "TaskNotFoundException -> to_do_list with id:{list_id} does not contain task with id:{task_id}")
      })
  @PatchMapping(value = "/{listId}/task/remove/{taskId}")
  public ResponseEntity<ToDoListDto> removeTaskFromList(
      @PathVariable long listId, @PathVariable long taskId, Principal principal) {
    logger.info(String.format("Removing Task with id %d from ToDoList with id %d", taskId, listId));
    ToDoListDto result = service.removeTaskToDoList(listId, principal.getName(), taskId);
    return ResponseEntity.ok(result);
  }
}
