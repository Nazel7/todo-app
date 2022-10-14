package com.gen.todoservice.builders;

import com.gen.todoservice.dtos.requests.TodoDto;
import com.gen.todoservice.dtos.responses.TodoResponse;
import com.gen.todoservice.entities.TodoModel;
import com.gen.todoservice.enums.TodoStatus;
import com.gen.todoservice.utils.TodoUtil;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TodoBuilder {

    public static TodoModel mapToModel(TodoDto todoDto) throws ParseException {

        final Date endDate = TodoUtil.getDate(todoDto.getEndDate());
        final Date startDate= TodoUtil.getDate(todoDto.getStartDate());

        final LocalDate localEndDate =
                endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        final LocalDate localStartDate =
                startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        final long days = ChronoUnit.DAYS.between(localStartDate, localEndDate);

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

        String status = TodoStatus.CREATED.name();
//        if (startDate.getTime() >= System.currentTimeMillis()) {
//           status = TodoStatus.NOT_DONE.name();
//        }
        return TodoModel
                .builder()
                .endDate(endDate)
                .startDate(startDate)
                .todoStatus(status)
                .taskDescription(todoDto.getTaskDescription())
                .period(days)
                .title(todoDto.getTitle())
                .build();
    }

    public static TodoResponse mapToDomain(TodoModel savedTask) {

        return TodoResponse
                .builder()
                .status(true)
                .id(savedTask.getId())
                .createdAt(savedTask.getCreatedAt().getTime())
                .endDate(savedTask.getEndDate().getTime())
                .startDate(savedTask.getStartDate().getTime())
                .period(savedTask.getPeriod())
                .todoStatus(savedTask.getTodoStatus())
                .taskDescription(savedTask.getTaskDescription())
                .title(savedTask.getTitle())
                .updatedAt(savedTask.getUpdatedAt().getTime())
                .build();
    }
}
