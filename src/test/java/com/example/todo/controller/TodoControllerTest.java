package com.example.todo.controller;

import com.example.todo.config.BaseIntegrationTest;
import com.example.todo.dto.CreateTaskRequest;
import com.example.todo.dto.UpdateTaskRequest;
import com.example.todo.manager.TodoManager;
import com.example.todo.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Sql(scripts = "/sql/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class TodoControllerTest extends BaseIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TodoManager repository;

    @BeforeEach
    void setUp(){
        repository.deleteAll();
    }

    @Test
    @DisplayName("Should create todo successfully")
    void createTask()  {
        CreateTaskRequest newTask = new CreateTaskRequest();
        newTask.setTitle("Test_Task");
        newTask.setDescription("Test_Description");

        ResponseEntity<Task> response = restTemplate.postForEntity("/todo", newTask, Task.class);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo(newTask.getTitle());
        assertThat(response.getBody().getDescription()).isEqualTo(newTask.getDescription());

        long count = repository.count();
        assertThat(count).isEqualTo(1);

        Task savedTask = repository.findById(response.getBody().getId()).orElse(null);
        assert savedTask != null;
        assertThat(savedTask.getTitle()).isEqualTo("Test_Task");
        assertThat(savedTask.getDescription()).isEqualTo("Test_Description");
    }

    @Test
    @DisplayName("Should update the name of the Task")
    void updateTask()  {
        UpdateTaskRequest updateTask = new UpdateTaskRequest();
        updateTask.setTitle("Test_Task_1");
        updateTask.setDescription("Test_Description_1");

        Task existingTask = new Task();
        existingTask.setTitle("Test_Task");
        existingTask.setDescription("Test_Description");
        existingTask.setCompleted(false);
        repository.save(existingTask);

        long id = 1L;

        ResponseEntity<Task> response = restTemplate.exchange("/todo/{id}",HttpMethod.PATCH,new HttpEntity<>(updateTask),Task.class,id);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo(updateTask.getTitle());
        assertThat(response.getBody().getDescription()).isEqualTo(updateTask.getDescription());

        long count = repository.count();
        assertThat(count).isEqualTo(1);

        Task updatedTask = repository.findById(response.getBody().getId()).orElse(null);
        assert updatedTask != null;
        assertThat(updatedTask.getTitle()).isEqualTo("Test_Task_1");
        assertThat(updatedTask.getDescription()).isEqualTo("Test_Description_1");
    }

    @Test
    void getAllTasks(){
        createTestTask();

        ResponseEntity<List<Task>> tasks = restTemplate.exchange(
                "/todo/all-tasks",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        assertThat(tasks.getBody()).isNotNull();
        assertThat(tasks.getBody()).hasSize(1);
        assertThat(tasks.getBody().get(0).getTitle()).isEqualTo("Test_Task");
        assertThat(tasks.getBody().get(0).getDescription()).isEqualTo("Test_Description");
    }

    @Test
    void deleteTask(){
        createTestTask();

        ResponseEntity<Void> response = restTemplate.exchange("/todo/{id}",HttpMethod.DELETE,null,Void.class,1L);
        assertThat(response.getStatusCode().value()).isEqualTo(204);
    }

    private void createTestTask(){
        Task task = new Task();
        task.setTitle("Test_Task");
        task.setDescription("Test_Description");
        task.setCompleted(false);
        repository.save(task);
    }
}