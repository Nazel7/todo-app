package com.gen.todoservice.repositories;

import com.gen.todoservice.entities.TodoModel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javassist.NotFoundException;

@Repository
public interface TodoRepo extends JpaRepository<TodoModel, Long> {

    TodoModel findTodoModelByIdAndTodoStatus(Long id, String status);

    @Override
    Page<TodoModel> findAll(Pageable pageable);

    Page<TodoModel> findTodoModelByTodoStatusOrderByCreatedAt(String status, Pageable pageable)
            throws NotFoundException;

    @Query(value = "SELECT * FROM TODO_ITEM WHERE Year(TODO_ITEM.START_DATE) =:year AND Month(TODO_ITEM.START_DATE) =:month AND Day(TODO_ITEM.START_DATE) =:day OR Year(TODO_ITEM.END_DATE) =:year AND Month(TODO_ITEM.END_DATE) =:month AND Day(TODO_ITEM.END_DATE) =:day ORDER BY TODO_ITEM.START_DATE DESC", nativeQuery = true)
    Page<TodoModel> fetchByDate(Long year, Long month, Long day, Pageable pageable)
            throws NotFoundException;

    @Query(value = "SELECT t FROM TodoModel t where t.title= ?1 AND t.taskDescription= ?2")
    TodoModel getTodoByTitleAndDecription(String title, String description) throws NotFoundException;


}

