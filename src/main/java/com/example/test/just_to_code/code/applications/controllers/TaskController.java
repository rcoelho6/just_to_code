package com.example.test.just_to_code.code.applications.controllers;

import com.example.test.just_to_code.code.usecases.boundaries.TaskIncomeBoundary;
import com.example.test.just_to_code.code.applications.presenters.TaskDto;
import com.example.test.just_to_code.code.applications.presenters.TaskErrorDto;
import com.example.test.just_to_code.code.usecases.domains.Task;
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

    public final TaskIncomeBoundary taskIncomeBoundary;

    public TaskController(TaskIncomeBoundary taskIncomeBoundary) {
        this.taskIncomeBoundary = taskIncomeBoundary;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") Long id
            , @RequestBody TaskDto taskDto) {
        try {
            taskIncomeBoundary.update(taskDto.toDomain(id));
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
            Task result = taskIncomeBoundary.create(taskDto.toDomain());
            uri.pathSegment(result.id().toString());
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
