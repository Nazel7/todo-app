package com.gen.todoservice.utils;

import com.gen.todoservice.builders.TodoBuilder;
import com.gen.todoservice.dtos.responses.TodoResponse;
import com.gen.todoservice.entities.TodoModel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TodoUtil {

    public static final String DATE_MATCHER = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$";
    public static final String DATE_MATCHER_TIME = "^([0-9]{4})-([0-1][0-9])-([0-3][0-9]):([0-1][0-9]|[2][0-3]):([0-5][0-9]):([0-5][0-9])$";
    public static final String DATE_FORMAT = "yyyy-MM-dd:HH:mm:ss.SS";

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
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        dateFormat.setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));

       return dateFormat.parse(dateString);
    }

    public static boolean isMatchDate(String dateParam){
        Pattern pattern = Pattern.compile(DATE_MATCHER_TIME);
        Matcher dateMatcher= pattern.matcher(dateParam);

        return dateMatcher.matches();
    }

    public static String enforceSingleSpaceText(String text){
        log.info("::: Text single spacing enforcer in progress......");

        return text.replaceAll("\\s+", " ");
    }
}
