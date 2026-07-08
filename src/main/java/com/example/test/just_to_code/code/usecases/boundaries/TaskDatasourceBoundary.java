package com.example.test.just_to_code.code.usecases.boundaries;

import com.example.test.just_to_code.code.usecases.domains.Task;

import java.util.Optional;

public interface TaskDatasourceBoundary {

    Task update(Task task);
    Task create(Task task);
    Optional<Task> find(Task task);
}
