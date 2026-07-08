package com.example.test.just_to_code.code;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.logging.Logger;

//@Component
//class Solution {

    class TaskDto {

        private String description;
        private Long priority;

        TaskDto(String description, Long priority) {
            this.description = description;
            this.priority = priority;
        }

//        TaskDto() { }

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
    }

    class TaskErrorDto {

        private String message;
        private int status;

        public TaskErrorDto(String message, int status) {
            this.message = message;
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public int getStatus() {
            return status;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }

    @Entity(name = "task")
    class Task {

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        private Long id;
        private String description;
        private Long priority;

        Task(Long id, String description, Long priority) {
            this.id = id;
            this.description = description;
            this.priority = priority;
            isValid(true);
        }

        Task(Long id, TaskDto taskDto) {
            this.id = id;
            this.description = taskDto.getDescription();
            this.priority = taskDto.getPriority();
            isValid(true);
        }

        Task(String description, Long priority) {
            this.description = description;
            this.priority = priority;
            isValid(false);
        }

        Task(TaskDto taskDto) {
            this.description = taskDto.getDescription();
            this.priority = taskDto.getPriority();
            isValid(false);
        }

        Task() {

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

    interface TaskPort {

        void update(Task task);
        Task create(Task task);
    }

    @RestController
    @RequestMapping("/tasks")
    class TaskController {

        private static final Logger log = Logger.getLogger("Solution");

        public final TaskPort taskPort;

        public TaskController(TaskPort taskPort) {
            this.taskPort = taskPort;
        }

        @PutMapping("/{id}")
        public ResponseEntity<Object> update(@PathVariable("id") Long id
                , @RequestBody TaskDto taskDto) {
            try {
                Task task = new Task(id, taskDto);
                taskPort.update(task);
            } catch (RestClientResponseException ex) {
                String msg = String.format("Erro with status %d: %s", ex.getStatusCode().value(), ex.getMessage());
                log.info(msg);
                TaskErrorDto dto = new TaskErrorDto(msg, ex.getStatusCode().value());
                return ResponseEntity.status(ex.getStatusCode()).body(dto);
            } catch (Exception ex) {
                String msg = String.format("Unexpected error %s", ex.getMessage());
                log.severe(msg);
                TaskErrorDto dto = new TaskErrorDto(msg, HttpStatus.INTERNAL_SERVER_ERROR.value());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dto);
            }
            return ResponseEntity.ok().body(taskDto);
        }

        @PostMapping({"", "/"})
        public ResponseEntity<Object> create(@RequestBody TaskDto taskDto) {
            String path = TaskController.class.getAnnotation(RequestMapping.class).value()[0];
            UriBuilder uri = UriComponentsBuilder.fromPath(path);
            try {
                Task task = new Task(taskDto);
                Task result = taskPort.create(task);
                uri.pathSegment(result.getId().toString());
            } catch (RestClientResponseException ex) {
                String msg = String.format("Erro with status %d: %s", ex.getStatusCode().value(), ex.getMessage());
                log.info(msg);
                TaskErrorDto dto = new TaskErrorDto(msg, ex.getStatusCode().value());
                return ResponseEntity.status(ex.getStatusCode()).body(dto);
            } catch (Exception ex) {
                String msg = String.format("Unexpected error %s", ex.getMessage());
                log.severe(msg);
                TaskErrorDto dto = new TaskErrorDto(msg, HttpStatus.INTERNAL_SERVER_ERROR.value());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dto);
            }
            return ResponseEntity.created(uri.build()).body(taskDto);
        }
    }
    
    @Repository
    interface TaskRepository extends JpaRepository<Task, Long> {

    }
    
    @Service
    class TaskService implements TaskPort {

        @Autowired
        private final TaskRepository taskRepo;

        TaskService(TaskRepository taskRepo) {
            this.taskRepo = taskRepo;
        }

        @Override
        public void update(Task task) {
            Task existing = taskRepo.findById(task.getId()).orElseThrow(() ->
                    new HttpClientErrorException(HttpStatus.NOT_FOUND
                            , "ID not found"));
            if (existing.getDescription().equals(task.getDescription())
                    && existing.getPriority().equals(task.getPriority())) {
                return;
            }
            taskRepo.save(task);
        }

        @Override
        public Task create(Task task) {
            return taskRepo.save(task);
        }
    }
//}
