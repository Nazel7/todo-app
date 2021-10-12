package com.crowntive.todoservice.configs.cron;


import com.crowntive.todoservice.entities.TodoModel;
import com.crowntive.todoservice.enums.TodoStatus;
import com.crowntive.todoservice.repositories.TodoRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class TodoCronScript {

    private final TodoRepo mTodoRepo;
    private int page = 1;
    private int size = 10;
    private int counter = 0;

    @Async
    @Scheduled(cron = "*/59 */59 */23 * * *")
    public void scheduledJob()
            throws NotFoundException, InterruptedException {

        log.info("::: Preparing to deactivate Todo.....");
        Pageable todoPages = PageRequest.of(page - 1, size);
        Page<TodoModel> todoModelsPage =
                mTodoRepo.findTodoModelByTodoStatusOrderByCreatedAt(TodoStatus.ACTIVE.name(),
                                                                    todoPages);
        int startIndex = (int) todoPages.getOffset();
        int endIndex = Math.min((startIndex + todoPages.getPageSize()),
                                todoModelsPage.getContent().size());
        List<TodoModel> subList = todoModelsPage.getContent().subList(startIndex, endIndex);

        for (int i = 0; i <= todoModelsPage.getTotalPages(); i++) {

            for (TodoModel model : subList) {
                if (model.getEndDate() < System.currentTimeMillis()) {
                    model.setTodoStatus(TodoStatus.INACTIVE.name());
                    counter++;
                    log.info("::: Operation Counter: [{}] :::", counter);
                    TodoModel deactivatedTodoModel = mTodoRepo.save(model);
                    log.info("::: Todo deactivated successfully with data: [{}]",
                             deactivatedTodoModel);
                }
            }

        }

    }
}




