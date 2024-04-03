package com.example.todo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;

import java.net.http.HttpResponse;
import java.util.*;
import java.time.LocalDateTime;

import com.example.todo.manager.TodoManager;
import com.example.todo.model.Tasks;

@Service
public class TodoService {
    @Autowired
    TodoManager todoManager;


    public ResponseEntity<Page<Tasks>> getAll(int pageNo, int pageSize) {
        try {
            PageRequest pr = PageRequest.of(pageNo,pageSize);
            Page allPages = todoManager.findAll(pr);

            if (pageNo > allPages.getTotalPages()){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(allPages, HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




    public ResponseEntity<List<Tasks>> getUncompletedTasks() {
        try {
            return new ResponseEntity<>(todoManager.findByCompletedFalse(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(new ArrayList<>(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<List<Tasks>> getCompletedTasks() {
        try {
            return new ResponseEntity<>(todoManager.findByCompletedTrue(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }


    public ResponseEntity<Optional<Tasks>> detailTask(int id){
        try {
            Optional<Tasks> optionalTask = todoManager.findById(id);

            if (optionalTask.isPresent()) {
                return new ResponseEntity<>(optionalTask, HttpStatus.FOUND);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Tasks> createTask( Tasks task){
            try{
                return new ResponseEntity<>(todoManager.save(task),HttpStatus.CREATED);
            }
            catch( Exception e){
                e.printStackTrace();
            }

            return  new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);

    }

    public ResponseEntity<Tasks> taskUpdate (Tasks task, int id){
        Optional <Tasks> optionalTask = todoManager.findById(id);
        try {
            if (!optionalTask.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Tasks targetTask = optionalTask.get();

            if (task.getTitle() != null) {
                targetTask.setTitle(task.getTitle());
            }

            if (task.getDescription() != null) {
                targetTask.setDescription(task.getDescription());
            }

            targetTask.setCreated_at(LocalDateTime.now());

            todoManager.save(targetTask);
            return new ResponseEntity<>(targetTask, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        }


    public ResponseEntity<Tasks> taskDelete(int id){

        try {
            Optional <Tasks> optionalDelete = todoManager.findById(id);
             if (!optionalDelete.isPresent()){
                 return new ResponseEntity<>(HttpStatus.NOT_FOUND);
             }
             Tasks taskDelete = optionalDelete.get();

             todoManager.delete(taskDelete);
             return new ResponseEntity<>(taskDelete,HttpStatus.GONE);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }
    }
}
