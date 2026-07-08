package com.example.test.just_to_code.code.applications.presenters;

import com.example.test.just_to_code.code.infrastructures.models.TaskModel;
import com.example.test.just_to_code.code.usecases.domains.Task;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Objects;

public class TaskDto {

//    @JsonProperty
    private String description;
//    @JsonProperty
    private Long priority;

    public String getDescription() {
        return description;
    }

    public Long getPriority() {
        return priority;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    public Task toDomain(Long id) {
        isValid(id);
        return new Task(id, this.description, this.priority);
    }

    public Task toDomain() {
        isValid();
        return new Task(this.description, this.priority);
    }

    private void isValid(Long id) {
        if (id == null || id.compareTo(0L) <= 0) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST
                    , "Id cannot be null or less than or equals 0");
        }
        isValid();
    }

    private void isValid() {
        if (description == null || description.isBlank()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST
                    , "Description cannot be null or blank");
        }
        if (priority == null || priority < 0) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST
                    , "Priority cannot be null or negative");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TaskDto taskDto = (TaskDto) o;
        return Objects.equals(description, taskDto.description) && Objects.equals(priority, taskDto.priority);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, priority);
    }
}
