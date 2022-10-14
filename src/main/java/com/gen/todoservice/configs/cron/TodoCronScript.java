package com.gen.todoservice.configs.cron;


import com.gen.todoservice.entities.TodoModel;
import com.gen.todoservice.enums.TodoStatus;
import com.gen.todoservice.repositories.TodoRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class TodoCronScript {

    private final TodoRepo mTodoRepo;
    private int page = 0;
    @Value("${page.size}")
    private int size;

    @Async
    @Scheduled(cron = "${spring.application.past-due-cron}")
    public void scheduledJob()
            throws NotFoundException, InterruptedException {

        log.info("::: Preparing to deactivate Todo.....");
        Pageable pageable = PageRequest.of(page, size);
        Page<TodoModel> todoModelsPage =
                mTodoRepo.findTodoModelByTodoStatusOrderByCreatedAt(TodoStatus.NOT_DONE.name(), pageable);

        for (int i = 0 ; i <= todoModelsPage.getTotalPages(); i++) {
          pageable = PageRequest.of(i, size);
            todoModelsPage = mTodoRepo.findTodoModelByTodoStatusOrderByCreatedAt(TodoStatus.NOT_DONE.name(), pageable);
            for (TodoModel model : todoModelsPage.getContent()) {
                if (model.getEndDate().getTime() < System.currentTimeMillis()) {
                    model.setTodoStatus(TodoStatus.PAST_DUE.name());
                    TodoModel deactivatedTodoModel = mTodoRepo.save(model);
                    log.info("::: Todo deactivated successfully with data: [{}]",
                             deactivatedTodoModel);
                }
            }

        }

    }

}




