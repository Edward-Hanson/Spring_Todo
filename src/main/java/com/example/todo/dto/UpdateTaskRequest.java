package com.example.todo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTaskRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String description;
}
