package com.example.todo.manager;

import java.util.List;
import com.example.todo.model.Tasks;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TodoManager extends PagingAndSortingRepository<Tasks,Integer>, CrudRepository<Tasks,Integer> {
    List<Tasks> findByCompletedFalse();
    List<Tasks> findByCompletedTrue();
}
