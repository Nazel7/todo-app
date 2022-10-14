package com.gen.todoservice.dtos.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.*;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class TodoResponse {

    private Boolean status;
    private Long id;
    private String title;
    private String taskDescription;
    private Long startDate;
    private Long endDate;
    private Long period;
    private String todoStatus;
    private Long createdAt;
    private Long updatedAt;
}
