package com.example.test.just_to_code.code.usecases.services;

import com.example.test.just_to_code.code.usecases.boundaries.TaskDatasourceBoundary;
import com.example.test.just_to_code.code.usecases.boundaries.TaskIncomeBoundary;
import com.example.test.just_to_code.code.usecases.domains.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class TaskService implements TaskIncomeBoundary {

    @Autowired
    public final TaskDatasourceBoundary taskSource;

    public TaskService(TaskDatasourceBoundary taskSource) {
        this.taskSource = taskSource;
    }

    @Override
    public void update(Task task) {
        Task existing = taskSource.find(task).orElseThrow(() ->
                new HttpClientErrorException(HttpStatus.NOT_FOUND
                , "ID not found")
        );
        if (existing.description().equals(task.description())
                && existing.priority().equals(task.priority())) {
            return;
        }
        taskSource.update(task);
    }

    @Override
    public Task create(Task task) {
        return taskSource.create(task);
    }
}
