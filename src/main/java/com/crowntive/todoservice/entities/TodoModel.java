package com.crowntive.todoservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Duration;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Tolerate;

@Entity
@Table(name = "todo_item", indexes = {
        @Index(name = "title_todo_index", columnList = "title"),
        @Index(name = "id_todo_index", columnList = "id"),
        @Index(name = "status_todo_index", columnList = "todo_status")
})
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class TodoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(unique = true)
    @Setter(AccessLevel.NONE)
    private String title;

    private String taskDescription;
    private Long startDate;
    private Long endDate;
    private Integer period;

    @Column(name = "todo_status")
    private String todoStatus;

    @CreationTimestamp
    @Setter(AccessLevel.NONE)
    private Date createdAt;

    @UpdateTimestamp
    @Setter(AccessLevel.NONE)
    private Date updatedAt;

    @Tolerate
    public TodoModel(){

    }

}
