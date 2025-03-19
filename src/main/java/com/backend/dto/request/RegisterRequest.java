package com.backend.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @Size(min = 3, message = "INVALID_USERNAME")
    private String username;

    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "INVALID_EMAIL")
    private String email;

    @Size(min = 5, message = "INVALID_PASSWORD")
    private String password;
}
