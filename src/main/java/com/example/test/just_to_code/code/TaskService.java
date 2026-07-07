package com.example.test.just_to_code.code;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class TaskService implements TaskPort {

    @Autowired
    public final TaskRepository taskRepo;

    public TaskService(TaskRepository taskRepo) {
        this.taskRepo = taskRepo;
    }

    @Override
    public void update(Task task) {
        Task existing = taskRepo.findById(task.getId()).orElseThrow(() ->
                new HttpClientErrorException(HttpStatus.NOT_FOUND
                , "ID not found"));
        if (existing.getDescription().equals(task.getDescription())
                && existing.getPriority().equals(task.getPriority())) {
            return;
        }
        taskRepo.save(task);
    }

    @Override
    public Task create(Task task) {
        return taskRepo.save(task);
    }
}
