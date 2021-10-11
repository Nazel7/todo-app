package com.crowntive.todoservice.controllers;

import com.crowntive.todoservice.dtos.requests.TodoDto;
import com.crowntive.todoservice.dtos.requests.TodoUpdateDto;
import com.crowntive.todoservice.dtos.responses.TodoResponse;
import com.crowntive.todoservice.interfaces.ITodoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("todos")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TodoController {

    private final ITodoService mTodoService;

    @PostMapping(" ")
    public ResponseEntity<TodoResponse> createTodos(@Valid @RequestBody final TodoDto todoDto)
            throws NotFoundException, ParseException {

        final TodoResponse todoResponse= mTodoService.createTodo(todoDto);

        return new ResponseEntity<>(todoResponse, HttpStatus.OK);
    }

    @GetMapping(" ")
    public ResponseEntity<Page<TodoResponse>> fetchTodos(
            @RequestParam(value = "page", defaultValue = "1")  int page,
            @RequestParam(value = "size", defaultValue = "10") int size)
            throws NotFoundException {

        final Page<TodoResponse> todoResponse= mTodoService.fetchTodosAcitve(page, size);

        return new ResponseEntity<>(todoResponse, HttpStatus.OK);
    }

    @PutMapping("/{todoId}")
    public ResponseEntity<TodoResponse> updateTodo(@NotNull @PathVariable("todoId") Long todoId,
                                                    @Valid @RequestBody final TodoUpdateDto todoUpdateDto)
            throws NotFoundException, ParseException {

        final TodoResponse todoResponse= mTodoService.updateTodo(todoId, todoUpdateDto);

        return new ResponseEntity<>(todoResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{todoId}")
    public ResponseEntity<String> deleteTodo(@PathVariable("todoId") Long todoId)
            throws NotFoundException {

        final String todoResponse= mTodoService.deleteTodo(todoId);

        return new ResponseEntity<>(todoResponse, HttpStatus.OK);
    }

    @GetMapping("/{todoId}")
    public ResponseEntity<TodoResponse> fetchActiveTodo(@PathVariable("todoId") Long todoId)
            throws NotFoundException {

        final TodoResponse todoResponse= mTodoService.fetchByIdActive(todoId);

        return new ResponseEntity<>(todoResponse, HttpStatus.OK);
    }

    @GetMapping("/history")
    public ResponseEntity<Page<TodoResponse>> fetchTodoHistory(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam("searchIndex") String searchIndex)
            throws NotFoundException, ParseException {

        final Page<TodoResponse> todoResponseObject= mTodoService.fetchTodoHistory(page, size, searchIndex);

        return new ResponseEntity<>(todoResponseObject, HttpStatus.OK);
    }

}
