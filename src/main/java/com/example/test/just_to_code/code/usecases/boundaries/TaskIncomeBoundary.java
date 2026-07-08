package com.example.test.just_to_code.code.usecases.boundaries;

import com.example.test.just_to_code.code.infrastructures.models.TaskModel;
import com.example.test.just_to_code.code.usecases.domains.Task;

public interface TaskIncomeBoundary {

    void update(Task task);
    Task create(Task task);
}
