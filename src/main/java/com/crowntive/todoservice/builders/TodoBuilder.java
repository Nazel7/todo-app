package com.crowntive.todoservice.builders;

import com.crowntive.todoservice.dtos.requests.TodoDto;
import com.crowntive.todoservice.dtos.responses.TodoResponse;
import com.crowntive.todoservice.entities.TodoModel;
import com.crowntive.todoservice.enums.TodoStatus;
import com.crowntive.todoservice.utils.TodoUtil;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.ReadableInstant;

import java.text.ParseException;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TodoBuilder {

    public static TodoModel mapToModel(TodoDto todoDto) throws ParseException {
        final ReadableInstant startDateInstant = new DateTime(todoDto.getStartDate());
        final ReadableInstant endDateInstant = new DateTime(todoDto.getEndDate());
        final int days= Days.daysBetween(startDateInstant, endDateInstant).getDays();
        final Date endDate = TodoUtil.getDate(todoDto.getEndDate());
        final Date startDate= TodoUtil.getDate(todoDto.getStartDate());

        if (startDate.getTime() >= endDate.getTime()) {
            log.error("::: Date error, StartDate [{}] cannot be more than EndDate [{}]",
                      todoDto.getStartDate(), todoDto.getEndDate());

            throw new IllegalArgumentException("StartDate cannot be more than EndDate");
        }
        if (startDate.getTime() < System.currentTimeMillis()){
            log.error("::: Date error, StartDate [{}] cannot be more less CurrentDate [{}]",
                      todoDto.getStartDate(), new Date(System.currentTimeMillis()));

            throw new IllegalArgumentException("StartDate cannot be less than CurrentDate");
        }

        return TodoModel
                .builder()
                .endDate(endDate.getTime())
                .startDate(startDate.getTime())
                .todoStatus(TodoStatus.ACTIVE.name())
                .taskDescription(todoDto.getTaskDescription())
                .period(days)
                .title(todoDto.getTitle())
                .build();
    }

    public static TodoResponse mapToDomain(TodoModel savedTask) {

        return TodoResponse
                .builder()
                .id(savedTask.getId())
                .createdAt(savedTask.getCreatedAt())
                .endDate(new Date(savedTask.getEndDate()))
                .startDate(new Date(savedTask.getStartDate()))
                .period(savedTask.getPeriod())
                .todoStatus(savedTask.getTodoStatus())
                .taskDescription(savedTask.getTaskDescription())
                .title(savedTask.getTitle())
                .updatedAt(savedTask.getUpdatedAt())
                .build();
    }
}
