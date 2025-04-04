package com.backend.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import jakarta.persistence.*;
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
	@ElementCollection(fetch = FetchType.EAGER)
	Set<String> roles; // Đổi từ String thành Set<String>
	Integer otp;
	int enabled;

	LocalDateTime startDate; // Ngày bắt đầu VIP
	LocalDateTime endDate;

	
	@OneToOne
	ForgotPassword forgotpassword;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    List<UserLike> likedItems;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    List<UserWatchList> watchlistItems;
}