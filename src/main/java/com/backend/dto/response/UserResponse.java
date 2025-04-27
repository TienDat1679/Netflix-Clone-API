package com.backend.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
	String name;
	String email;
	Integer otp;
	int enabled;
	LocalDate dob;
	Set<RoleResponse> roles;
	LocalDateTime startDate;
	LocalDateTime endDate;
}
