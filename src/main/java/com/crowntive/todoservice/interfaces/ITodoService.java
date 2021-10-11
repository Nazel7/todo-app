package com.crowntive.todoservice.interfaces;

import com.crowntive.todoservice.dtos.requests.TodoDto;
import com.crowntive.todoservice.dtos.requests.TodoUpdateDto;
import com.crowntive.todoservice.dtos.responses.TodoResponse;

import org.springframework.data.domain.Page;

import java.text.ParseException;

import javassist.NotFoundException;

public interface ITodoService {

    TodoResponse createTodo(TodoDto todoDto)
            throws RuntimeException, ParseException, NotFoundException;

    TodoResponse updateTodo(Long todoId, TodoUpdateDto todoUpdateDto)
            throws NotFoundException, ParseException;

    String deleteTodo(Long todoId) throws NotFoundException;

    TodoResponse fetchByIdActive(Long todoId) throws NotFoundException;

    Page<TodoResponse> fetchTodosAcitve(int page, int size) throws NotFoundException;

    Page<TodoResponse> fetchTodoHistory(int page, int size, String searchIndex)
            throws NotFoundException, ParseException;

    Page<TodoResponse> fetchTodoHistoryByDate(int page, int size, String dateSearchIndex)
            throws NotFoundException, ParseException;
}
