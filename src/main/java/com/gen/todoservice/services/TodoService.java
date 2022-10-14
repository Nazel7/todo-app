package com.gen.todoservice.services;

import com.gen.todoservice.builders.TodoBuilder;
import com.gen.todoservice.dtos.requests.TodoDto;
import com.gen.todoservice.dtos.requests.TodoUpdateDto;
import com.gen.todoservice.dtos.responses.TodoResponse;
import com.gen.todoservice.entities.TodoModel;
import com.gen.todoservice.enums.TodoStatus;
import com.gen.todoservice.interfaces.ITodoService;
import com.gen.todoservice.repositories.TodoRepo;
import com.gen.todoservice.utils.TodoUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.ZoneId;
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
    public TodoResponse createTodo(TodoDto todoDto)
            throws RuntimeException, ParseException, NotFoundException {
        log.info("::: In createTodo.....");
        log.info("::: Creating todo task in progress......");
        String title = TodoUtil.enforceSingleSpaceText(todoDto.getTitle());
        String taskDecription = TodoUtil.enforceSingleSpaceText(todoDto.getTaskDescription());

        final TodoModel modelVerify =
                mTodoRepo.getTodoByTitleAndDecription(title, taskDecription);
        if (modelVerify != null) {
            throw new IllegalArgumentException(
                    "Todo title or description already exist in your list, "
                            + "Please try again");
        }
        todoDto.setTaskDescription(taskDecription);
        todoDto.setTitle(title);
        final TodoModel model = TodoBuilder.mapToModel(todoDto);
        final TodoModel savedTask = mTodoRepo.save(model);
        log.info("::: Todo created successfully with data: [{}]", savedTask);

        return TodoBuilder.mapToDomain(savedTask);

    }

    @Override
    public TodoResponse findTodoById(Long todoId) throws NotFoundException {
        log.info("::: In findTodoById.....");
        final Optional<TodoModel> todoModelOptional = mTodoRepo.findById(todoId);

        if (todoModelOptional.isEmpty()) {
            log.error("::: Todo Item not found with id: [{}] :::", todoId);
            throw new NotFoundException("Todo Task not found");
        }

        log.info("::: Todo Item retrieved with data: [{}]", todoModelOptional.get());
        return TodoBuilder.mapToDomain(todoModelOptional.get());

    }

    @Override
    public TodoResponse updateTodo(Long todoId, TodoUpdateDto todoUpdateDto)
            throws NotFoundException, ParseException {
        log.info("::: In updateTodo.....");

        final Optional<TodoModel> todoModelOptional = mTodoRepo.findById(todoId);
        final TodoModel todoModel;
        Date endDate = TodoUtil.getDate(todoUpdateDto.getEndDate());

        if (todoModelOptional.isEmpty()) {
            log.error("::: Todo Item not found with id: [{}] :::", todoId);
            throw new NotFoundException("Todo Task not found");
        }

        TodoModel savedTask = null;
        todoModel = todoModelOptional.get();
        log.info("::: Updating Todo with data: [{}]", todoModel);
        if (todoModel.getStartDate().getTime() > endDate.getTime()) {
            log.error("::: StartDate cannot be more than EndDate");
        }

        // update endDate
        if (endDate.getTime() > 0 &&
                !(todoModel.getTodoStatus().equalsIgnoreCase(TodoStatus.PAST_DUE.name())) &&
                !(todoModel.getStartDate().getTime() > endDate.getTime())) {
            todoModel.setEndDate(endDate);
            savedTask = mTodoRepo.save(todoModel);
        }

        // Update TodoDesc
        if (todoUpdateDto.getTaskDescription() != null) {
            final String desc = TodoUtil.enforceSingleSpaceText(todoUpdateDto.getTaskDescription());
            todoModel.setTaskDescription(desc);
            savedTask = mTodoRepo.save(todoModel);
        }

        // Update TodoStatus DONE
        if (todoUpdateDto.getTodoStatus() != null &&
                todoUpdateDto.getTodoStatus().equalsIgnoreCase(TodoStatus.DONE.name())) {
            if (todoModel.getEndDate().getTime() <= System.currentTimeMillis()) {
                TodoStatus.getInstance(todoUpdateDto.getTodoStatus());
                todoModel.setTodoStatus(TodoStatus.DONE.name());
                savedTask = mTodoRepo.save(todoModel);
            }else {
                log.error("::: Cannot update to completed, EndDate cannot be in the future");
            }
        }

        // Update TodoStatus to NOT_DONE
        if (todoUpdateDto.getTodoStatus() != null &&
                todoUpdateDto.getTodoStatus().equalsIgnoreCase(TodoStatus.NOT_DONE.name())) {

            if (todoModel.getStartDate().getTime() >= System.currentTimeMillis()  ||
                    todoModel.getStartDate().getTime() <= System.currentTimeMillis() &&
                            todoModel.getEndDate().getTime() >= System.currentTimeMillis()) {
                todoModel.setTodoStatus(TodoStatus.NOT_DONE.name());
                savedTask = mTodoRepo.save(todoModel);
            }else {
                log.error("::: Cannot update to not-done, StartDate cannot be more than EndDate or endDate is due");
            }
        }

        log.info("::: Todo updated successfully with data: [{}]", savedTask);
        assert savedTask != null;
        return TodoBuilder.mapToDomain(savedTask);

    }

    @Override
    public String deleteTodo(Long todoId) throws NotFoundException {
        log.info("::: In deleteTodo.....");
        log.info("::: Deleting Todo task in progress......");
        mTodoRepo.deleteById(todoId);
        log.info("::: Deleted Todo task with id: [{}] :::", todoId);

        return "Deleted Todo Item with id: " + todoId;
    }

    @Override
    public TodoResponse fetchByIdAndNotDone(Long todoId) throws NotFoundException {

        log.info("::: In fetchByIdNotDone.....");
        log.info("::: Retrieving Active Todo Item in progress......");
        final TodoModel todoModel =
                mTodoRepo.findTodoModelByIdAndTodoStatus(todoId, TodoStatus.NOT_DONE.name());

        if (todoModel == null) {
            log.error("::: Todo Item not found with id: [{}] :::", todoId);
            throw new NotFoundException("Todo Task not found");
        }

        log.info("::: Todo Item retrieved with data: [{}]", todoModel);
        return TodoBuilder.mapToDomain(todoModel);

    }

    @Override
    public Page<TodoResponse> fetchTodosNotDone(int page, int size) throws NotFoundException {

        log.info("::: In fetchTodosNotDone.....");

        log.info("::: Retrieving Active Todo Items in progress......");
        final List<TodoResponse> todoResponses = new ArrayList<>();
        final Pageable todoPages = PageRequest.of(page - 1, size);

        Page<TodoModel> todoModelPage =
                mTodoRepo.findTodoModelByTodoStatusOrderByCreatedAt(TodoStatus.NOT_DONE.name(), todoPages);

        if (todoModelPage == null) {
            todoModelPage = mTodoRepo.findAll(todoPages);
        }
        // Convert TodoModelPages to TodoResponsePages
        return TodoUtil.getTodoResponses(todoResponses, todoPages, todoModelPage);
    }

    @Override
    public Page<TodoResponse> fetchTodoHistory(int page, int size, String searchIndex)
            throws NotFoundException, ParseException {

        log.info("::: In fetchTodoHistory.....");

        log.info("::: Retrieving Todd History in progress.....");
        final List<TodoResponse> todoResponses = new ArrayList<>();
        final Pageable todoPages = PageRequest.of(page - 1, size);
        final Page<TodoModel> todoModelPage;
        final boolean isDateMatch = TodoUtil.isMatchDate(searchIndex);
        final boolean isStatusAvailable = TodoStatus.isTodoStatus(searchIndex);

        // Fetch TodoModel by status if searchIndex is String of TodoStatus enum value
       if (isDateMatch){


             return fetchTodoHistoryByDate(page, size, searchIndex);

        }
       else if (isStatusAvailable) {

           todoModelPage =
                   mTodoRepo.findTodoModelByTodoStatusOrderByCreatedAt(searchIndex, todoPages);
       }
        else {
            todoModelPage = mTodoRepo.findAll(todoPages);
        }

        // Convert TodoModelPages to TodoResponsePages
        return TodoUtil.getTodoResponses(todoResponses, todoPages, todoModelPage);

    }

    private Page<TodoResponse> fetchTodoHistoryByDate(int page, int size, String searchIndex)
            throws NotFoundException, ParseException {
        log.info("::: In fetchTodoHistoryByDate.....");

        final long year = TodoUtil.getDate(searchIndex).toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();
        final long month = TodoUtil.getDate(searchIndex).toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonth().getValue();
        final long day = TodoUtil.getDate(searchIndex).toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfMonth();

        log.info("::: Retrieving Todo Items in progress......");
        final List<TodoResponse> todoResponses = new ArrayList<>();
        final Page<TodoModel> todoModelPage;
        final Pageable todoPages = PageRequest.of(page - 1, size);

        // Fetch TodoModel by Date alone
        todoModelPage =
                mTodoRepo.fetchByDate(year, month, day, todoPages);

        if (todoModelPage == null) {
            log.error("::: Todo Item not found");
            throw new NotFoundException("Todo Task not found");
        }

        log.info("::: Total of Todo Item retrieve by startDate is : [{}]",
                 todoModelPage.getTotalElements());
        return TodoUtil.getTodoResponses(todoResponses, todoPages, todoModelPage);

    }


}

