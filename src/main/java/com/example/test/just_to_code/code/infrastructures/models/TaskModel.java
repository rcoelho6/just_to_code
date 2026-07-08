package com.example.test.just_to_code.code.infrastructures.models;

import com.example.test.just_to_code.code.applications.presenters.TaskDto;
import com.example.test.just_to_code.code.usecases.domains.Task;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Objects;

@Entity(name = "task")
public class TaskModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String description;
    private Long priority;

    public TaskModel(Task task) {
        this.id = task.id();
        this.description = task.description();
        this.priority = task.priority();
    }

    public TaskModel(String description, Long priority) {
        this.description = description;
        this.priority = priority;
    }

    public TaskModel() { }

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

    public Task toDomain() {
        return new Task(this.id, this.description, this.priority);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TaskModel taskModel = (TaskModel) o;
        return Objects.equals(this.id, taskModel.id)
                && Objects.equals(this.description, taskModel.description)
                && Objects.equals(this.priority, taskModel.priority);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, priority);
    }
}