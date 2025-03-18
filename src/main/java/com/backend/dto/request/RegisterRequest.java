package com.backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @Size(min = 3, message = "INVALID_USERNAME")
    private String username;

    @Email(message = "INVALID_EMAIL")
    private String email;

    @Size(min = 5, message = "INVALID_PASSWORD")
    private String password;
}
