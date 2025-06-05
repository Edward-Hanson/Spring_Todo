package com.example.todo.service;

import com.example.todo.dto.CreateTaskRequest;
import com.example.todo.dto.UpdateTaskRequest;
import com.example.todo.exceptions.TodoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import com.example.todo.manager.TodoManager;
import com.example.todo.model.Task;


@RequiredArgsConstructor
@Service
public class TodoService {
    private final TodoManager todoManager;

    private static final String TASK_NOT_FOUND = "Task not found";

    public List<Task> getAll(Integer offset, Integer limit) {
        List<Task> tasks = todoManager.findAll();

        return getTasks(offset, limit, tasks);
    }


    public List<Task> getUncompletedTasks(Integer offset, Integer limit) {
       List<Task> tasks = todoManager.findByCompletedFalse();

       return getTasks(offset, limit, tasks);
    }

    public List<Task> getCompletedTasks(Integer offset, Integer limit) {
        List<Task> tasks = todoManager.findByCompletedTrue();

        return getTasks(offset, limit, tasks);
    }


    public Task detailTask(int id){
        return todoManager.findById(id).orElseThrow(() -> new TodoException(TASK_NOT_FOUND));
    }

    public Task createTask(CreateTaskRequest request){
           Task task = Task.builder()
                   .title(request.getTitle())
                   .description(request.getDescription())
                   .completed(false)
                   .createdAt(LocalDateTime.now())
                   .build();
           return todoManager.save(task);

    }

    public Task taskUpdate (UpdateTaskRequest request, Integer id){
        Task task = todoManager.findById(id).orElseThrow(() -> new TodoException(TASK_NOT_FOUND));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());

        return todoManager.save(task);
    }

    public void taskDelete(int id){
       if (todoManager.existsById(id)) {
           todoManager.deleteById(id);
       }
    }

    public String toggleStatus(Integer id) {
        Task task = todoManager.findById(id).orElseThrow(()-> new TodoException(TASK_NOT_FOUND));

        task.setCompleted(!task.getCompleted());

        todoManager.save(task);

        return "Task successfully updated";
    }

    private List<Task> getTasks(Integer offset, Integer limit, List<Task> tasks) {
        int safeOffset = offset != null ? offset : 0;
        int safeLimit = limit != null ? limit : tasks.size();

        if (safeOffset < 0 || safeLimit < 0) {
            throw new TodoException("Offset and limit must be non-negative.");
        }

        List<Task> sortedTasks = tasks.stream().sorted(Comparator.comparing(Task::getCreatedAt).reversed()).toList();

        if (safeOffset >= sortedTasks.size()) {
            return List.of();
        }

        int endIndex = Math.min(safeOffset + safeLimit, sortedTasks.size());
        return sortedTasks.subList(safeOffset, endIndex);
    }
}
