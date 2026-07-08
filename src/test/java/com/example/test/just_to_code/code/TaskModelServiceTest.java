package com.example.test.just_to_code.code;

import com.example.test.just_to_code.code.infrastructures.models.TaskModel;
import com.example.test.just_to_code.code.usecases.boundaries.TaskDatasourceBoundary;
import com.example.test.just_to_code.code.usecases.domains.Task;
import com.example.test.just_to_code.code.usecases.services.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskModelServiceTest {

    @Mock
    TaskDatasourceBoundary taskSource;

    @InjectMocks
    TaskService taskService;

    @Test
    void update_whenExistingSame_thenSaveNotCalled() {
        TaskModel existing = new TaskModel(new Task(1L, "desc", 1L));
        when(taskSource.find(existing.toDomain())).thenReturn(Optional.of(existing.toDomain()));

        TaskModel taskModel = new TaskModel(new Task(1L, "desc", 1L));
        taskService.update(taskModel.toDomain());

        verify(taskSource, never()).create(any());
    }

    @Test
    void update_whenExistingDifferent_thenSaveCalled() {
        Task task = new Task(2L, "new", 2L);
        Task existing = new Task(1L, "old", 1L);
        when(taskSource.find(task)).thenReturn(Optional.of(existing));

        taskService.update(task);

        verify(taskSource).update(task);
    }

    @Test
    void update_whenNotFound_thenThrows() {
        Task task = new Task(2L, "desc", 1L);
        when(taskSource.find(task)).thenReturn(Optional.empty());

        TaskModel taskModel = new TaskModel(task);
        assertThrows(HttpClientErrorException.class, () -> 
                taskService.update(taskModel.toDomain()));

        verify(taskSource, times(0)).update(task);
    }

    @Test
    void create_whenValid_thenCallsSaveAndReturnsSavedTask() {
        // create without id (new task)
        Task task = new Task("new task", 1L);
        TaskModel saved = new TaskModel(task);

        when(taskSource.create(task)).thenReturn(saved.toDomain());

        Task result = taskService.create(task);

        verify(taskSource).create(task);
        assertEquals(saved.toDomain(), result);
    }

}