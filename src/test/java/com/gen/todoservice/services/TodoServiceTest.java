package com.gen.todoservice.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gen.todoservice.dtos.requests.TodoDto;
import com.gen.todoservice.entities.TodoModel;
import com.gen.todoservice.repositories.TodoRepo;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import javassist.NotFoundException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {TodoService.class})
@ExtendWith(SpringExtension.class)
class TodoServiceTest {
    @MockBean
    private TodoRepo todoRepo;

    @Autowired
    private TodoService todoService;

    @DisplayName("create_todo_returns_success_for_valid_parameters")
    @Test
    void testCreateTodo() throws RuntimeException, ParseException, NotFoundException {
        TodoModel todoModel = new TodoModel();
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        todoModel.setEndDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        todoModel.setPeriod(1L);
        todoModel.setTaskDescription("Task Description");
        todoModel.setTodoStatus("Todo Status");
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        todoModel.setStartDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        when(this.todoRepo.getTodoByTitleAndDecription((String) any(), (String) any()))
                .thenReturn(todoModel);
        TodoDto todoDto = mock(TodoDto.class);
        when(todoDto.getTaskDescription()).thenReturn("Task Description");
        when(todoDto.getTitle()).thenReturn("Dr");
        assertThrows(IllegalArgumentException.class, () -> this.todoService.createTodo(todoDto));
        verify(this.todoRepo).getTodoByTitleAndDecription((String) any(), (String) any());
        verify(todoDto).getTaskDescription();
        verify(todoDto).getTitle();
    }

    @DisplayName("fins_todo_by_id_is_successful_for_valid_id")
    @Test
    void testFindTodoById() throws NotFoundException {
        when(this.todoRepo.findById((Long) any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> this.todoService.findTodoById(123L));
        verify(this.todoRepo).findById((Long) any());
    }

    @DisplayName("fetch_todos_active_is_successful_for_valid_params")
    @Test
    void testFetchTodosNotDone() throws NotFoundException {
        when(this.todoRepo.findTodoModelByTodoStatusOrderByCreatedAt((String) any(),
                                                                     (org.springframework.data.domain.Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        assertTrue(this.todoService.fetchTodosNotDone(1, 3).toList().isEmpty());
        verify(this.todoRepo).findTodoModelByTodoStatusOrderByCreatedAt((String) any(),
                                                                        (org.springframework.data.domain.Pageable) any());
    }


    @DisplayName("fetch_todo_history_verify_successfully_with_valid_data")
    @Test
    void testFetchTodoHistory() throws ParseException, NotFoundException {
        when(this.todoRepo.findAll((org.springframework.data.domain.Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        assertTrue(this.todoService.fetchTodoHistory(1, 3, "Search Index").toList().isEmpty());
        verify(this.todoRepo).findAll((org.springframework.data.domain.Pageable) any());
    }
}

