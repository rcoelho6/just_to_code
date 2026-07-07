package com.example.test.just_to_code.code;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    TaskPort taskPort;

    @InjectMocks
    TaskController controller;

    @Test
    void update_success() {
        TaskDto dto = new TaskDto();
        dto.setDescription("desc");
        dto.setPriority(1L);

        ResponseEntity<Object> res = controller.update(1L, dto);

        assertEquals(HttpStatus.OK.value(), res.getStatusCodeValue());
        assertEquals(dto, res.getBody());
    }

    @Test
    void update_restClientException() {
        doThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad request")).when(taskPort).update(any());

        TaskDto dto = new TaskDto();
        dto.setDescription("desc");
        dto.setPriority(1L);

        ResponseEntity<Object> res = controller.update(1L, dto);

        assertEquals(HttpStatus.BAD_REQUEST.value(), res.getStatusCodeValue());
        assertTrue(res.getBody() instanceof TaskErrorDto);
        TaskErrorDto err = (TaskErrorDto) res.getBody();
        assertEquals(HttpStatus.BAD_REQUEST.value(), err.getStatus());
        assertTrue(err.getMessage().contains("Erro with status"));
    }

    @Test
    void update_genericException() {
        doThrow(new RuntimeException("boom")).when(taskPort).update(any());

        TaskDto dto = new TaskDto();
        dto.setDescription("desc");
        dto.setPriority(1L);

        ResponseEntity<Object> res = controller.update(1L, dto);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), res.getStatusCodeValue());
        assertTrue(res.getBody() instanceof TaskErrorDto);
        TaskErrorDto err = (TaskErrorDto) res.getBody();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), err.getStatus());
        assertTrue(err.getMessage().contains("Unexpected error"));
    }


    @Test
    void create_success() {
        TaskDto dto = new TaskDto();
        dto.setDescription("desc");
        dto.setPriority(1L);

        Task result = new Task(dto);
        result.setDescription("desc");
        result.setPriority(1L);
        result.setId(11L);

        doReturn(result).when(taskPort).create(any());

        ResponseEntity<Object> res = controller.create(dto);

        assertEquals(HttpStatus.CREATED.value(), res.getStatusCodeValue());
        assertEquals(dto, res.getBody());
    }

    @Test
    void create_restClientException() {
        doThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad request")).when(taskPort).create(any());

        TaskDto dto = new TaskDto();
        dto.setDescription("desc");
        dto.setPriority(1L);

        ResponseEntity<Object> res = controller.create(dto);

        assertEquals(HttpStatus.BAD_REQUEST.value(), res.getStatusCodeValue());
        assertTrue(res.getBody() instanceof TaskErrorDto);
        TaskErrorDto err = (TaskErrorDto) res.getBody();
        assertEquals(HttpStatus.BAD_REQUEST.value(), err.getStatus());
        assertTrue(err.getMessage().contains("Erro with status"));
    }

    @Test
    void create_genericException() {
        doThrow(new RuntimeException("boom")).when(taskPort).create(any());

        TaskDto dto = new TaskDto();
        dto.setDescription("desc");
        dto.setPriority(1L);

        ResponseEntity<Object> res = controller.create(dto);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), res.getStatusCodeValue());
        assertTrue(res.getBody() instanceof TaskErrorDto);
        TaskErrorDto err = (TaskErrorDto) res.getBody();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), err.getStatus());
        assertTrue(err.getMessage().contains("Unexpected error"));
    }

}

