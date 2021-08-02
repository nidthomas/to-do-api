package com.chilborne.todoapi.service;

import com.chilborne.todoapi.exception.TaskNotFoundException;
import com.chilborne.todoapi.exception.ToDoListNotFoundException;
import com.chilborne.todoapi.persistance.dto.TaskDto;
import com.chilborne.todoapi.persistance.dto.ToDoListDto;
import com.chilborne.todoapi.persistance.model.ToDoList;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface ToDoListService {

    ToDoListDto getToDoListDtoByIdAndUsername(long id, String username) throws ToDoListNotFoundException;

    ToDoListDto saveToDoList(ToDoList list);

    ToDoListDto newToDoList(ToDoListDto listDto, String username);

    List<ToDoListDto> getAllToDoList(String username);

    void deleteToDoList(long id, String username);

    ToDoListDto updateToDoList(long id, ToDoListDto toDoList, String username);

    ToDoListDto setToDoListActive(long id, String username, boolean active) throws ToDoListNotFoundException;

    ToDoListDto addTaskToDoList(long id, String username, TaskDto task) throws ToDoListNotFoundException;

    ToDoListDto removeTaskToDoList(long listId, String username, long taskId) throws TaskNotFoundException;


}
