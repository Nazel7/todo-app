package com.gen.todoservice.interfaces;

import com.gen.todoservice.dtos.requests.TodoDto;
import com.gen.todoservice.dtos.requests.TodoUpdateDto;
import com.gen.todoservice.dtos.responses.TodoResponse;

import org.springframework.data.domain.Page;

import java.text.ParseException;

import javassist.NotFoundException;

public interface ITodoService {

    TodoResponse createTodo(TodoDto todoDto) throws RuntimeException, ParseException, NotFoundException;

    TodoResponse findTodoById(Long todoId) throws NotFoundException;

    TodoResponse updateTodo(Long todoId, TodoUpdateDto todoUpdateDto)
            throws NotFoundException, ParseException;

    String deleteTodo(Long todoId) throws NotFoundException;

    TodoResponse fetchByIdAndNotDone(Long todoId) throws NotFoundException;

    Page<TodoResponse> fetchTodosNotDone(int page, int size) throws NotFoundException;

    Page<TodoResponse> fetchTodoHistory(int page, int size, String searchIndex)
            throws NotFoundException, ParseException;

}
