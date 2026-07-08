package com.example.test.just_to_code.code;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    TaskPort taskPort;

    @InjectMocks
    TaskController controller;

    @Test
    void update_success() {
        TaskDto dto = new TaskDto("desc", 1L);

        ResponseEntity<Object> res = controller.update(1L, dto);

        assertEquals(HttpStatus.OK.value(), res.getStatusCodeValue());
        assertEquals(dto, res.getBody());
    }

    @Test
    void update_restClientException() {
        doThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad request")).when(taskPort).update(any());

        TaskDto dto = new TaskDto("desc", 1L);

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

        TaskDto dto = new TaskDto("desc", 1L);

        ResponseEntity<Object> res = controller.update(1L, dto);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), res.getStatusCodeValue());
        assertTrue(res.getBody() instanceof TaskErrorDto);
        TaskErrorDto err = (TaskErrorDto) res.getBody();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), err.getStatus());
        assertTrue(err.getMessage().contains("Unexpected error"));
    }


    @Test
    void create_success() {
        TaskDto dto = new TaskDto("desc", 1L);

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

        TaskDto dto = new TaskDto("desc", 1L);

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

        TaskDto dto = new TaskDto("desc", 1L);

        ResponseEntity<Object> res = controller.create(dto);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), res.getStatusCodeValue());
        assertTrue(res.getBody() instanceof TaskErrorDto);
        TaskErrorDto err = (TaskErrorDto) res.getBody();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), err.getStatus());
        assertTrue(err.getMessage().contains("Unexpected error"));
    }

}


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
        Task toCreate = new Task("new task", 1L);
        Task saved = new Task(5L, "new task", 1L);

        when(taskRepo.save(toCreate)).thenReturn(saved);

        Task result = taskService.create(toCreate);

        verify(taskRepo).save(toCreate);
        assertSame(saved, result);
    }

}

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = Replace.ANY)
class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskRepository taskRepo;

    @Test
    void update_persists_to_db() throws Exception {
        taskRepo.deleteAll();
        Task existing = taskRepo.save(new Task( "old", 1L));

        TaskDto dto = new TaskDto("new", 2L);

        mockMvc.perform(put("/tasks/{id}", existing.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        Optional<Task> updated = taskRepo.findById(existing.getId());
        assertTrue(updated.isPresent());
        assertEquals("new", updated.get().getDescription());
        assertEquals(2L, updated.get().getPriority());
    }

    @Test
    void update_notFound_returns404() throws Exception {
        taskRepo.deleteAll();

        TaskDto dto = new TaskDto("desc", 1L);

        mockMvc.perform(put("/tasks/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void update_no_priority_returns400() throws Exception {
        taskRepo.deleteAll();
        Task existing = taskRepo.save(new Task( "old", 1L));

        TaskDto dto = new TaskDto("desc", null);

        mockMvc.perform(put("/tasks/{id}", existing.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_no_desc_returns400() throws Exception {
        taskRepo.deleteAll();
        Task existing = taskRepo.save(new Task( "old", 1L));

        TaskDto dto = new TaskDto(null, 1L);

        mockMvc.perform(put("/tasks/{id}", existing.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_empty_json_body() throws Exception {
        taskRepo.deleteAll();
        Task existing = taskRepo.save(new Task( "old", 1L));

        mockMvc.perform(put("/tasks/{id}", existing.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_null_body() throws Exception {
        taskRepo.deleteAll();
        Task existing = taskRepo.save(new Task( "old", 1L));

        mockMvc.perform(put("/tasks/{id}", existing.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_no_desc_field_json() throws Exception {
        taskRepo.deleteAll();
        Task existing = taskRepo.save(new Task( "old", 1L));

        mockMvc.perform(put("/tasks/{id}", existing.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"priority\": 1 }"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_null_priority_field_json() throws Exception {
        taskRepo.deleteAll();
        Task existing = taskRepo.save(new Task( "old", 1L));

        mockMvc.perform(put("/tasks/{id}", existing.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"description\": \"new desc\", \"priority\": null }"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_null_desc_field_json() throws Exception {
        taskRepo.deleteAll();
        Task existing = taskRepo.save(new Task( "old", 1L));

        mockMvc.perform(put("/tasks/{id}", existing.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{  \"description\": null, \"priority\": 1 }"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_no_priority_field_json() throws Exception {
        taskRepo.deleteAll();
        Task existing = taskRepo.save(new Task( "old", 1L));

        mockMvc.perform(put("/tasks/{id}", existing.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"description\": \"new desc\" }"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_null_string_id() throws Exception {
        taskRepo.deleteAll();
        taskRepo.save(new Task( "old", 1L));

        TaskDto dto = new TaskDto("new", 2L);

        mockMvc.perform(put("/tasks/{id}", "null")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_null_without_slash_id() throws Exception {
        taskRepo.deleteAll();
        taskRepo.save(new Task( "old", 1L));

        TaskDto dto = new TaskDto("new", 2L);

        mockMvc.perform(put("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void update_null_with_slash_id() throws Exception {
        taskRepo.deleteAll();
        taskRepo.save(new Task( "old", 1L));

        TaskDto dto = new TaskDto("new", 2L);

        mockMvc.perform(put("/tasks/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void create_persists_to_db() throws Exception {
        taskRepo.deleteAll();

        TaskDto dto = new TaskDto("created", 5L);

        var mvcResult = mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn();

        String location = mvcResult.getResponse().getHeader("Location");
        assertNotNull(location);
        String idStr = location.substring(location.lastIndexOf('/') + 1);
        assertEquals("/tasks/" + idStr, location);
        Long id = Long.valueOf(idStr);

        var saved = taskRepo.findById(id);
        assertTrue(saved.isPresent());
        assertEquals("created", saved.get().getDescription());
        assertEquals(5L, saved.get().getPriority());
    }

    @Test
    void create_persists_two_equals_to_db() throws Exception {
        taskRepo.deleteAll();

        TaskDto dto = new TaskDto("created", 5L);

        taskRepo.save(new Task(dto));

        var mvcResult = mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn();

        String location = mvcResult.getResponse().getHeader("Location");
        assertNotNull(location);
        String idStr = location.substring(location.lastIndexOf('/') + 1);
        assertEquals("/tasks/" + idStr, location);
        Long id = Long.valueOf(idStr);

        var saved = taskRepo.findById(id);
        assertTrue(saved.isPresent());
        assertEquals("created", saved.get().getDescription());
        assertEquals(5L, saved.get().getPriority());
    }

    @Test
    void create_null_desc_400() throws Exception {
        taskRepo.deleteAll();

        TaskDto dto = new TaskDto(null, 5L);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_null_prior_400() throws Exception {
        taskRepo.deleteAll();

        TaskDto dto = new TaskDto("created", null);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_missing_desc_400() throws Exception {
        taskRepo.deleteAll();

        var result = mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"priority\": 5 }"))
                .andExpect(status().isBadRequest())
                .andReturn();

        var error = result.getResponse().getContentAsString();
        assertTrue(error.contains("Description cannot be null or blank"));

    }

    @Test
    void create_missing_prior_400() throws Exception {
        taskRepo.deleteAll();

        var result = mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"description\": \"desc\" }"))
                .andExpect(status().isBadRequest())
                .andReturn();
        var error = result.getResponse().getContentAsString();
        assertTrue(error.contains("Erro with status 400: 400 Priority cannot be null or negative"));
    }

    @Test
    void update_extra_field_ok() throws Exception {
        taskRepo.deleteAll();

        Task created = taskRepo.save(new Task("created", 15L));

        mockMvc.perform(put("/tasks/{id}", created.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"description\": \"updated\", \"priority\": 10, \"extra\": 0 }"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void create_extra_field_ok() throws Exception {
        taskRepo.deleteAll();

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"description\": \"created\", \"priority\": 5, \"extra\": 0 }"))
                .andExpect(status().isCreated());
    }

    @Test
    void create_send_id() throws Exception {
        taskRepo.deleteAll();

        TaskDto dto = new TaskDto("created", 5L);

        mockMvc.perform(post("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isMethodNotAllowed());
    }
}