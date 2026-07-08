package com.example.test.just_to_code.code.services;

import com.example.test.just_to_code.code.repositories.models.Task;

public interface TaskPort {

    void update(Task task);
    Task create(Task task);
}
