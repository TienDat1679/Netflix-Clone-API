package com.backend.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class UserInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	String id;
	String name;
	String password;
	@Column(unique = true)
	String email;
	Integer otp;
	int enabled;
	String image;
	LocalDate dob;

	LocalDateTime startDate;
	LocalDateTime endDate;

	@ManyToMany
	Set<Role> roles;

	@OneToOne
	ForgotPassword forgotpassword;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	List<UserLike> likedItems;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	List<UserWatchList> watchlistItems;
}