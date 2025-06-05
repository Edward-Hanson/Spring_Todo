package com.example.todo.manager;

import java.util.List;
import com.example.todo.model.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TodoManager extends PagingAndSortingRepository<Task,Integer>, CrudRepository<Task,Integer> {
    List<Task> findByCompletedFalse();
    List<Task> findByCompletedTrue();
    List<Task> findAll();
}
