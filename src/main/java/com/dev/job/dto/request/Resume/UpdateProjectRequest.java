package com.dev.job.dto.request.Resume;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public class UpdateProjectRequest {
    @NotBlank(message = "Project name must not be blank")
    @Size(max = 255, message = "Project name must not exceed 255 characters")
    String name;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    String description;

    @NotBlank(message = "Role must not be blank")
    @Size(max = 255, message = "Role must not exceed 255 characters")
    String role;

    @Size(max = 500, message = "Result must not exceed 500 characters")
    String result;
}
