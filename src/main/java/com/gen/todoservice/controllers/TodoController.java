package com.gen.todoservice.controllers;

import com.gen.todoservice.dtos.requests.TodoDto;
import com.gen.todoservice.dtos.requests.TodoUpdateDto;
import com.gen.todoservice.dtos.responses.TodoResponse;
import com.gen.todoservice.interfaces.ITodoService;

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

        final TodoResponse todoResponse = mTodoService.createTodo(todoDto);

        return new ResponseEntity<>(todoResponse, HttpStatus.CREATED);
    }

    @GetMapping(" ")
    public ResponseEntity<Page<TodoResponse>> fetchTodos(
            @RequestParam(value = "page", defaultValue = "1")  int page,
            @RequestParam(value = "size", defaultValue = "10") int size)
            throws NotFoundException {

        final Page<TodoResponse> todoResponse = mTodoService.fetchTodosNotDone(page, size);

        return new ResponseEntity<>(todoResponse, HttpStatus.OK);
    }

    @PutMapping("/{todoId}")
    public ResponseEntity<TodoResponse> updateTodo(@PathVariable("todoId") Long todoId,
                                                   @RequestBody final TodoUpdateDto todoUpdateDto)
            throws NotFoundException, ParseException {

        final TodoResponse todoResponse = mTodoService.updateTodo(todoId, todoUpdateDto);

        return new ResponseEntity<>(todoResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{todoId}")
    public ResponseEntity<String> deleteTodo(@PathVariable("todoId") Long todoId)
            throws NotFoundException {

        final String todoResponse = mTodoService.deleteTodo(todoId);

        return new ResponseEntity<>(todoResponse, HttpStatus.OK);
    }

    @GetMapping("/{todoId}")
    public ResponseEntity<TodoResponse> fetchNotDoneTodoById(@PathVariable("todoId") Long todoId)
            throws NotFoundException {

        final TodoResponse todoResponse = mTodoService.fetchByIdAndNotDone(todoId);

        return new ResponseEntity<>(todoResponse, HttpStatus.OK);
    }

    @GetMapping("/todo/{todoId}")
    public ResponseEntity<TodoResponse> fetchTodoById(@PathVariable("todoId") Long todoId)
            throws NotFoundException {

        final TodoResponse todoResponse = mTodoService.findTodoById(todoId);

        return new ResponseEntity<>(todoResponse, HttpStatus.OK);
    }

    @GetMapping("/history")
    public ResponseEntity<Page<TodoResponse>> fetchTodoHistory(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam("searchIndex") String searchIndex)
            throws NotFoundException, ParseException {

        final Page<TodoResponse> todoResponseObject = mTodoService.fetchTodoHistory(page, size, searchIndex);

        return new ResponseEntity<>(todoResponseObject, HttpStatus.OK);
    }

//    @GetMapping("/history-date")
//    public ResponseEntity<Page<TodoResponse>> fetchTodoHistoryByDate(
//            @RequestParam(value = "page", defaultValue = "1") int page,
//            @RequestParam(value = "size", defaultValue = "10") int size,
//            @RequestParam("searchIndex") String searchIndex)
//            throws NotFoundException, ParseException {
//
//        final Page<TodoResponse> todoResponseObject = mTodoService.fetchTodoHistoryByDate(page, size, searchIndex);
//
//        return new ResponseEntity<>(todoResponseObject, HttpStatus.OK);
//    }

}
