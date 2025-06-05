package com.example.todo.controller;

import java.util.List;

import com.example.todo.dto.CreateTaskRequest;
import com.example.todo.dto.UpdateTaskRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.todo.service.TodoService;
import com.example.todo.model.Task;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("todo")
public class TodoController {

    private final TodoService todoService;

    @GetMapping("/all-tasks")
    public ResponseEntity<List<Task>> getAll(@RequestParam(required = false) Integer offset, @RequestParam(required = false) Integer limit){
        return new ResponseEntity<>(todoService.getAll(offset,limit), HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<List<Task>> uncompleted(@RequestParam(required = false) Integer offset, @RequestParam(required = false) Integer limit){
        return new ResponseEntity<>(todoService.getUncompletedTasks(offset,limit), HttpStatus.OK);
    }

    @GetMapping("/complete")
    public ResponseEntity<List<Task>> completed(@RequestParam(required = false) Integer offset, @RequestParam(required = false) Integer limit){

        return new ResponseEntity<>(todoService.getCompletedTasks(offset,limit), HttpStatus.OK);
    }


    @GetMapping("/details/{id}")
    public ResponseEntity<Task> getTask(@PathVariable("id") Integer id){
        return new ResponseEntity<>(todoService.detailTask(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody CreateTaskRequest request){
        return new ResponseEntity<>(todoService.createTask(request), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Task> updateTask(@Valid @RequestBody UpdateTaskRequest request, @PathVariable Integer id){
        return new ResponseEntity<> (todoService.taskUpdate(request, id), HttpStatus.OK);
    }

    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<String> toggleStatus(@PathVariable("id") Integer id){
        return new ResponseEntity<> (todoService.toggleStatus(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable("id") Integer id){
        todoService.taskDelete(id);
        return  ResponseEntity.noContent().build();
    }
}
