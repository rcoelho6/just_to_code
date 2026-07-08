package com.example.test.just_to_code.code;

import com.example.test.just_to_code.code.controllers.dtos.TaskDto;
import com.example.test.just_to_code.code.repositories.TaskRepository;
import com.example.test.just_to_code.code.repositories.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

        TaskDto dto = new TaskDto();
        dto.setDescription("new");
        dto.setPriority(2L);

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

        TaskDto dto = new TaskDto();
        dto.setDescription("desc");
        dto.setPriority(1L);

        mockMvc.perform(put("/tasks/{id}", 999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void update_no_priority_returns400() throws Exception {
        taskRepo.deleteAll();
        Task existing = taskRepo.save(new Task( "old", 1L));

        TaskDto dto = new TaskDto();
        dto.setDescription("desc");

        mockMvc.perform(put("/tasks/{id}", existing.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_no_desc_returns400() throws Exception {
        taskRepo.deleteAll();
        Task existing = taskRepo.save(new Task( "old", 1L));

        TaskDto dto = new TaskDto();
        dto.setPriority(1L);

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

        TaskDto dto = new TaskDto();
        dto.setDescription("new");
        dto.setPriority(2L);

        mockMvc.perform(put("/tasks/{id}", "null")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_null_without_slash_id() throws Exception {
        taskRepo.deleteAll();
        taskRepo.save(new Task( "old", 1L));

        TaskDto dto = new TaskDto();
        dto.setDescription("new");
        dto.setPriority(2L);

        mockMvc.perform(put("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void update_null_with_slash_id() throws Exception {
        taskRepo.deleteAll();
        taskRepo.save(new Task( "old", 1L));

        TaskDto dto = new TaskDto();
        dto.setDescription("new");
        dto.setPriority(2L);

        mockMvc.perform(put("/tasks/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void create_persists_to_db() throws Exception {
        taskRepo.deleteAll();

        TaskDto dto = new TaskDto();
        dto.setDescription("created");
        dto.setPriority(5L);

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

        TaskDto dto = new TaskDto();
        dto.setDescription("created");
        dto.setPriority(5L);

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

        TaskDto dto = new TaskDto();
        dto.setPriority(5L);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_null_prior_400() throws Exception {
        taskRepo.deleteAll();

        TaskDto dto = new TaskDto();
        dto.setDescription("created");

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_empty_desc_400() throws Exception {
        taskRepo.deleteAll();

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"priority\": 5 }"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_empty_prior_400() throws Exception {
        taskRepo.deleteAll();

        TaskDto dto = new TaskDto();
        dto.setPriority(5L);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"description\": \"desc\""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_send_id() throws Exception {
        taskRepo.deleteAll();

        TaskDto dto = new TaskDto();
        dto.setDescription("created");
        dto.setPriority(5L);

       mockMvc.perform(post("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isMethodNotAllowed());
    }
}
