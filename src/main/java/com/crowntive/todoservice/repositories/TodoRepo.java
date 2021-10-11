package com.crowntive.todoservice.repositories;

import com.crowntive.todoservice.entities.TodoModel;

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

    @Query(value = "SELECT m FROM TodoModel m WHERE m.startDate = ?1 OR m.endDate = ?1 ORDER BY m.startDate DESC", nativeQuery = true)
    Page<TodoModel> fetchByDate(Long dateSearchIndex, Pageable pageable)
            throws NotFoundException;


}

