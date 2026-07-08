package com.example.test.just_to_code.code.usecases.domains;

public record Task(
        Long id,
        String description,
        Long priority
) {

    public Task(String description, Long priority) {
        this(null, description, priority);
    }

    public Task(Long id) {
        this(id, null, null);
    }
}