package com.gen.todoservice.enums;

import javassist.NotFoundException;

public enum TodoStatus {
    CREATED,
    NOT_DONE,
    PAST_DUE,
    DONE;


    public static TodoStatus getInstance(String statusValue) throws NotFoundException {

        for (TodoStatus todoStatus: TodoStatus.values()){
            if (todoStatus.toString().equalsIgnoreCase(statusValue)){

                return todoStatus;
            }
        }

        throw new NotFoundException("TodoStatus not found of value: "+ statusValue);

    }

    public static boolean isTodoStatus(String statusValue) throws NotFoundException {

        for (TodoStatus todoStatus: TodoStatus.values()){
            if (todoStatus.toString().equalsIgnoreCase(statusValue)){

                return true;
            }
        }

       return false;

    }

}
