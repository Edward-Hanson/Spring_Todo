package com.example.todo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;

import com.example.todo.service.TodoService;
import com.example.todo.model.Tasks;

@RestController
@RequestMapping("todo")
public class TodoController {
    @Autowired
    TodoService todoService;


    @GetMapping("/all-tasks")
    public ResponseEntity<Page<Tasks>> getAll(@RequestParam Integer pageNo, @RequestParam Integer pageSize){

        return todoService.getAll(pageNo,pageSize);
    }


    @GetMapping("/")
    public ResponseEntity<List<Tasks>> unCompleted(){

        return todoService.getUncompletedTasks();
    }

    @GetMapping("/complete")
    public ResponseEntity<List<Tasks>> Completed(){

        return todoService.getCompletedTasks();
    }


    @GetMapping("/details/{id}")
    public ResponseEntity<Optional<Tasks>> detailView(@PathVariable("id") Integer id){
        return todoService.detailTask(id);
    }

    @PostMapping("/new")
    public ResponseEntity<Tasks> createTask(@RequestBody Tasks task){
        return todoService.createTask(task);
    }

    @PutMapping("/task/update/{id}")
    public ResponseEntity<Tasks> updateTask(@RequestBody Tasks task, @PathVariable Integer id){
        return todoService.taskUpdate(task, id);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Tasks> deleteTask(@PathVariable("id") Integer id){
        return todoService.taskDelete(id);
    }
}
