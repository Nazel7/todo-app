package com.crowntive.todoservice.dtos.responses;

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

    private Long id;
    private String title;
    private String taskDescription;
    private Date startDate;
    private Date endDate;
    private Integer period;
    private String todoStatus;
    private Date createdAt;
    private Date updatedAt;
}
