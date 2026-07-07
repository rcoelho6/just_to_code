package com.example.test.just_to_code.code;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    TaskRepository taskRepo;

    @InjectMocks
    TaskService taskService;

    @Test
    void update_whenExistingSame_thenSaveNotCalled() {
        Task existing = new Task(1L, "desc", 1L);
        when(taskRepo.findById(1L)).thenReturn(Optional.of(existing));

        Task task = new Task(1L, "desc", 1L);
        taskService.update(task);

        verify(taskRepo, never()).save(any());
    }

    @Test
    void update_whenExistingDifferent_thenSaveCalled() {
        Task existing = new Task(1L, "old", 1L);
        when(taskRepo.findById(1L)).thenReturn(Optional.of(existing));

        Task task = new Task(1L, "new", 2L);
        taskService.update(task);

        verify(taskRepo).save(task);
    }

    @Test
    void update_whenNotFound_thenThrows() {
        when(taskRepo.findById(2L)).thenReturn(Optional.empty());

        Task task = new Task(2L, "desc", 1L);
        assertThrows(HttpClientErrorException.class, () -> taskService.update(task));
    }

    @Test
    void create_whenValid_thenCallsSaveAndReturnsSavedTask() {
        // create without id (new task)
        Task toCreate = new Task(null, "new task", 1L);
        Task saved = new Task(5L, "new task", 1L);

        when(taskRepo.save(toCreate)).thenReturn(saved);

        Task result = taskService.create(toCreate);

        verify(taskRepo).save(toCreate);
        assertSame(saved, result);
    }

}