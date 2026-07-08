package com.example.test.just_to_code.code.controllers;

import com.example.test.just_to_code.code.repositories.models.Task;
import com.example.test.just_to_code.code.controllers.dtos.TaskDto;
import com.example.test.just_to_code.code.controllers.dtos.TaskErrorDto;
import com.example.test.just_to_code.code.services.TaskPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.logging.Logger;

@RestController
@RequestMapping("/tasks")
public class TaskController {

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
