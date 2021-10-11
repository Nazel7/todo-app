package com.crowntive.todoservice.controllers;

import com.crowntive.todoservice.dtos.requests.TodoDto;
import com.crowntive.todoservice.dtos.responses.TodoResponse;
import com.crowntive.todoservice.entities.TodoModel;
import com.crowntive.todoservice.interfaces.ITodoService;
import com.crowntive.todoservice.repositories.TodoRepo;
import com.crowntive.todoservice.services.TodoService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import javassist.NotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {TodoController.class})
@ExtendWith(SpringExtension.class)
class TodoControllerTest {
    @MockBean
    private ITodoService iTodoService;

    @Autowired
    private TodoController todoController;

    @Test
    void testDeleteTodo() throws Exception {
        when(this.iTodoService.deleteTodo((Long) any())).thenReturn("Delete Todo");
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.delete("/todos/{todoId}", 123L);
        MockMvcBuilders.standaloneSetup(this.todoController)
                       .build()
                       .perform(requestBuilder)
                       .andExpect(MockMvcResultMatchers.status().isOk())
                       .andExpect(MockMvcResultMatchers.content().contentType(
                               "text/plain;charset=ISO-8859-1"))
                       .andExpect(MockMvcResultMatchers.content().string("Delete Todo"));
    }

    @Test
    void testFetchActiveTodo() throws NotFoundException {
        TodoModel todoModel = new TodoModel();
        todoModel.setPeriod(1);
        todoModel.setEndDate(1L);
        todoModel.setTaskDescription("Task Description");
        todoModel.setTodoStatus("Todo Status");
        todoModel.setStartDate(1L);
        TodoRepo todoRepo = mock(TodoRepo.class);
        when(todoRepo.findTodoModelByIdAndTodoStatus((Long) any(), (String) any()))
                .thenReturn(todoModel);
        ResponseEntity<TodoResponse> actualFetchActiveTodoResult =
                (new TodoController(new TodoService(todoRepo)))
                        .fetchActiveTodo(123L);
        assertTrue(actualFetchActiveTodoResult.getHeaders().isEmpty());
        assertTrue(actualFetchActiveTodoResult.hasBody());
        assertEquals(HttpStatus.OK, actualFetchActiveTodoResult.getStatusCode());
        TodoResponse body = actualFetchActiveTodoResult.getBody();
        assertEquals("Task Description", body.getTaskDescription());
        assertEquals(1, body.getPeriod().intValue());
        assertEquals("Todo Status", body.getTodoStatus());
        assertNull(body.getId());
        assertNull(body.getUpdatedAt());
        assertNull(body.getTitle());
        assertNull(body.getCreatedAt());
        Date expectedEndDate = body.getStartDate();
        assertEquals(expectedEndDate, body.getEndDate());
        verify(todoRepo).findTodoModelByIdAndTodoStatus((Long) any(), (String) any());
    }


    @Test
    void testFetchTodos() throws Exception {
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/todos/ ");
        MockHttpServletRequestBuilder paramResult = getResult.param("page", String.valueOf(1));
        MockHttpServletRequestBuilder requestBuilder = paramResult.param("size", String.valueOf(1));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.todoController)
                                                           .build()
                                                           .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(500));
    }
}

