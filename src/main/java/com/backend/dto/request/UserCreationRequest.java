package com.backend.dto.request;

import java.time.LocalDate;

import com.backend.validator.DobConstraint;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {

    @Size(min = 3, message = "INVALID_USERNAME")
    String username;

    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "INVALID_EMAIL")
    String email;

    @Size(min = 5, message = "INVALID_PASSWORD")
    String password;

    @DobConstraint(min = 18, message = "INVALID_DOB")
    LocalDate dob;
}
