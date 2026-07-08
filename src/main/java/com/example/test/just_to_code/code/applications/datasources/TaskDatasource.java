package com.example.test.just_to_code.code.applications.datasources;

import com.example.test.just_to_code.code.infrastructures.repositories.TaskRepository;
import com.example.test.just_to_code.code.infrastructures.models.TaskModel;
import com.example.test.just_to_code.code.usecases.boundaries.TaskDatasourceBoundary;
import com.example.test.just_to_code.code.usecases.domains.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TaskDatasource implements TaskDatasourceBoundary {

    @Autowired
    public final TaskRepository taskRepo;

    public TaskDatasource(TaskRepository taskRepo) {
        this.taskRepo = taskRepo;
    }

    @Override
    public Task update(Task task) {
        return taskRepo.save(new TaskModel(task)).toDomain();
    }

    @Override
    public Task create(Task task) {
        return taskRepo.save(new TaskModel(task)).toDomain();
    }

    @Override
    public Optional<Task> find(Task task) {
        return taskRepo.findById(task.id()).map(m -> m.toDomain());
    }
}
