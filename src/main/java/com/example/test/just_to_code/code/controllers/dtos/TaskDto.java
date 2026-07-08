package com.example.test.just_to_code.code.controllers.dtos;

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
