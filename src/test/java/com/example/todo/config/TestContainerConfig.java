package com.example.todo.config;

import org.testcontainers.containers.PostgreSQLContainer;

public class TestContainerConfig {

    private static PostgreSQLContainer postgres;

    static PostgreSQLContainer<?> getInstance(){
        if(postgres==null){
            postgres = new PostgreSQLContainer<>("postgres:15-alpine")
                    .withDatabaseName("todo_test")
                    .withUsername("testUser")
                    .withPassword("testPassword")
                    .withReuse(true);
            postgres.start();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (postgres != null && postgres.isRunning()) {
                    postgres.stop();
                }
            }));
        }
        return postgres;
    }
}
