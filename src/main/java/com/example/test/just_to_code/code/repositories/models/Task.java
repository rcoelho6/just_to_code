package com.example.test.just_to_code.code.repositories.models;

import com.example.test.just_to_code.code.controllers.dtos.TaskDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Objects;

@Entity(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String description;
    private Long priority;

    public Task(Long id, String description, Long priority) {
        this.id = id;
        this.description = description;
        this.priority = priority;
        isValid(true);
    }

    public Task(Long id, TaskDto taskDto) {
        this.id = id;
        this.description = taskDto.getDescription();
        this.priority = taskDto.getPriority();
        isValid(true);
    }

    public Task(String description, Long priority) {
        this.description = description;
        this.priority = priority;
        isValid(false);
    }

    public Task(TaskDto taskDto) {
        this.description = taskDto.getDescription();
        this.priority = taskDto.getPriority();
        isValid(false);
    }

    public Task() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, priority);
    }

    private void isValid(boolean isUpdate) {
        // No Time to use spring validation, so I will use this method to validate the object
        if (isUpdate &&  (id == null || id.compareTo(0L) <= 0)) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST
                    , "Id cannot be null or less than or equals 0");
        }
        if (description == null || description.isBlank()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST
                    , "Description cannot be null or blank");
        }
        if (priority == null || priority < 0) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST
                    , "Priority cannot be null or negative");
        }
    }
}