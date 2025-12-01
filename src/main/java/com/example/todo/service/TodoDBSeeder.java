package com.example.todo.service;

import com.example.todo.manager.TodoManager;
import com.example.todo.model.Task;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Profile("prod")
public class TodoDBSeeder implements CommandLineRunner {

    private final TodoManager todoManager;

    final Faker faker = new Faker();
    final Random random = new Random();

    @Override
    public void run(String... args) throws Exception {
        if (todoManager.count() == 0) {
            seedDB();
        }
    }

    private void seedDB(){
        List<Task> tasks = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            Date fakeDate = faker.date().past(30, TimeUnit.DAYS);

            Task task = Task.builder()
                    .title(faker.book().title())
                    .description(faker.lorem().sentence())
                    .completed(random.nextBoolean())
                    .createdAt(fakeDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                    .build();
            tasks.add(task);
        }
       todoManager.saveAll(tasks);
    }
}
