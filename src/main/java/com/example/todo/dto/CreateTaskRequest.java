package com.example.todo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class CreateTaskRequest{
    @NotBlank(message = "Title is required")
    private String title;

    private String description;
}
