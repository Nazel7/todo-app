package com.crowntive.todoservice.utils;

import com.crowntive.todoservice.builders.TodoBuilder;
import com.crowntive.todoservice.dtos.responses.TodoResponse;
import com.crowntive.todoservice.entities.TodoModel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TodoUtil {

    public static Page<TodoResponse> getTodoResponses(List<TodoResponse> todoResponses,
                                                       Pageable todoPages, Page<TodoModel> todoModelPage) {
        // Convert TodoModelPages to TodoResponsePages
        final List<TodoModel> todoModels = todoModelPage.getContent();
        for (TodoModel model : todoModels) {
            TodoResponse todoResponse = TodoBuilder.mapToDomain(model);
            todoResponses.add(todoResponse);

        }

        // Convert List of TodoResponse retrieved by Pages
        if (todoPages.getOffset() >= todoModels.size()) {
            return Page.empty();
        }

        int startIndex = (int) todoPages.getOffset();
        int endIndex = Math.min((startIndex + todoPages.getPageSize()), todoResponses.size());
        List<TodoResponse> subList = todoResponses.subList(startIndex, endIndex);

        log.info("::: Total of [{}] Todo Items history returned with sub-data: [{}]",
                 todoModels.size(), subList);
        return new PageImpl<>(subList, todoPages, todoResponses.size());
    }

    public static Date getDate(String dateString) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
    }
}
