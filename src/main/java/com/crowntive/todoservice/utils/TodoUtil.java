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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TodoUtil {

    public static final String DATE_MATCHER = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$";
    public static final String DATE_FORMAT = "yyyy-MM-dd";

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
        return new SimpleDateFormat(DATE_FORMAT).parse(dateString);
    }

    public static boolean isMatchDate(String dateParam){
        Pattern pattern = Pattern.compile(DATE_MATCHER);
        Matcher dateMatcher= pattern.matcher(dateParam);

        return dateMatcher.matches();
    }

    public static String enforceSingleSpaceText(String text){
        log.info("::: Text single spacing enforcer in progress......");

        Pattern pattern = Pattern.compile("\\s+");
        Matcher matcher= pattern.matcher(text);
        log.info(" Text single spacing isEnforced = [{}] ",matcher.matches());

        return matcher.replaceAll(" ");
    }
}
