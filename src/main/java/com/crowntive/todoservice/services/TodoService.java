package com.crowntive.todoservice.services;

import com.crowntive.todoservice.builders.TodoBuilder;
import com.crowntive.todoservice.dtos.requests.TodoDto;
import com.crowntive.todoservice.dtos.requests.TodoUpdateDto;
import com.crowntive.todoservice.dtos.responses.TodoResponse;
import com.crowntive.todoservice.entities.TodoModel;
import com.crowntive.todoservice.enums.TodoStatus;
import com.crowntive.todoservice.interfaces.ITodoService;
import com.crowntive.todoservice.repositories.TodoRepo;
import com.crowntive.todoservice.utils.TodoUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class TodoService implements ITodoService {

    private final TodoRepo mTodoRepo;

    @Override
    public TodoResponse createTodo(TodoDto todoDto) throws RuntimeException, ParseException {
        log.info("::: Creating todo task in progress......");

        final TodoModel model = TodoBuilder.mapToModel(todoDto);
        final TodoModel savedTask = mTodoRepo.save(model);
        log.info("::: Todo created successfully with data: [{}]", savedTask);

        return TodoBuilder.mapToDomain(savedTask);

    }

    @Override
    public TodoResponse updateTodo(Long todoId, TodoUpdateDto todoUpdateDto)
            throws NotFoundException, ParseException {

        final Optional<TodoModel> todoModelOptional = mTodoRepo.findById(todoId);
        final TodoModel todoModel;
        Date endDate = TodoUtil.getDate(todoUpdateDto.getUpdateEndDate());

        if (todoModelOptional.isEmpty()) {
            log.error("::: Todo Item not found with id: [{}] :::", todoId);
            throw new NotFoundException("Todo Task not found");
        }

        todoModel = todoModelOptional.get();
        log.info("::: Updating Todo with data: [{}]", todoModel);
        if (todoModel.getStartDate() > endDate.getTime()) {
            throw new IllegalArgumentException("StartDate cannot be more than EndDate");
        }
        if (endDate.getTime() > 0) {
            todoModel.setEndDate(endDate.getTime());
        }
        if (todoUpdateDto.getTaskDescription() != null) {
            todoModel.setTaskDescription(todoUpdateDto.getTaskDescription());
        }
        if (todoUpdateDto.getUpdateToCompleted() != null) {
            TodoStatus.getTodo(todoUpdateDto.getUpdateToCompleted());
            todoModel.setTodoStatus(TodoStatus.COMPLETED.name());
        }

        final TodoModel savedTask = mTodoRepo.save(todoModel);

        log.info("::: Todo updated successfully with data: [{}]", savedTask);
        return TodoBuilder.mapToDomain(savedTask);


    }

    @Override
    public String deleteTodo(Long todoId) throws NotFoundException {

        log.info("::: Deleting Todo task in progress......");
        mTodoRepo.deleteById(todoId);
        log.info("::: Deleted Todo task with id: [{}] :::", todoId);

        return "Deleted Todo Item with id: " + todoId;
    }

    @Override
    public TodoResponse fetchByIdActive(Long todoId) throws NotFoundException {

        log.info("::: Retrieving Active Todo Item in progress......");
        final TodoModel todoModel = mTodoRepo
                .findTodoModelByIdAndTodoStatus(todoId, TodoStatus.ACTIVE.name());

        if (todoModel == null) {
            log.error("::: Todo Item not found with id: [{}] :::", todoId);
            throw new NotFoundException("Todo Task not found");
        }

        log.info("::: Todo Item retrieved with data: [{}]", todoModel);
        return TodoBuilder.mapToDomain(todoModel);

    }

    @Override
    public Page<TodoResponse> fetchTodosAcitve(int page, int size) throws NotFoundException {

        log.info("::: Retrieving Active Todo Items in progress......");
        final List<TodoResponse> todoResponses = new ArrayList<>();
        final Pageable todoPages = PageRequest.of(page - 1, size);

        Page<TodoModel> todoModelPage =
                mTodoRepo.findTodoModelByTodoStatusOrderByCreatedAt(TodoStatus.ACTIVE.name(),
                                                                    todoPages);

        // Convert TodoModelPages to TodoResponsePages
        return TodoUtil.getTodoResponses(todoResponses, todoPages, todoModelPage);
    }

    @Override
    public Page<TodoResponse> fetchTodoHistory(int page, int size, String searchIndex)
            throws NotFoundException {

        log.info("::: Retrieving Todd History in progress.....");
        final List<TodoResponse> todoResponses = new ArrayList<>();
        final Pageable todoPages = PageRequest.of(page - 1, size);
        final Page<TodoModel> todoModelPage;
        final TodoStatus todoStatus = TodoStatus.getTodo(searchIndex);

        // Fetch TodoModel by status if searchIndex is String of TodoStatus enum value
        if (searchIndex.trim().toString().length() > 0) {
            todoModelPage =
                    mTodoRepo.findTodoModelByTodoStatusOrderByCreatedAt(todoStatus.name(),
                                                                        todoPages);
        } else {
            todoModelPage = mTodoRepo.findAll(todoPages);
        }

        // Convert TodoModelPages to TodoResponsePages
        return TodoUtil.getTodoResponses(todoResponses, todoPages, todoModelPage);

    }

    @Override
    public Page<TodoResponse> fetchTodoHistoryByDate(int page, int size, String dateSearcher)
            throws NotFoundException, ParseException {
        log.info("::: Retrieving Todo Items in progress......");
        final List<TodoResponse> todoResponses = new ArrayList<>();
        final Page<TodoModel> todoModelPage;
        final Pageable todoPages = PageRequest.of(page - 1, size);
        Date date = TodoUtil.getDate(dateSearcher);

        // Fetch TodoModel by Date alone
        todoModelPage =
                mTodoRepo.fetchByDate(date.getTime(), todoPages);

        if (todoModelPage == null) {
            log.error("::: Todo Item not found with date: [{}] :::",
                      dateSearcher);
            throw new NotFoundException("Todo Task not found");
        }

        log.info("::: Total of Todo Item retrieve by startDate is : [{}]",
                 todoModelPage.getTotalElements());
        return TodoUtil.getTodoResponses(todoResponses, todoPages, todoModelPage);

    }

    private TodoResponse fetchById(Long todoId) throws NotFoundException {

        log.info("::: Retrieving Todo Item in progress......");
        final Optional<TodoModel> todoModelOptional = mTodoRepo.findById(todoId);

        if (todoModelOptional.isEmpty()) {
            log.error("::: Todo Item not found with id: [{}] :::", todoId);
            throw new NotFoundException("Todo Task not found");
        }

        final TodoModel todoModel = todoModelOptional.get();
        log.info("::: Todo Item retrieved with data: [{}]", todoModel);

        return TodoBuilder.mapToDomain(todoModel);

    }

}

