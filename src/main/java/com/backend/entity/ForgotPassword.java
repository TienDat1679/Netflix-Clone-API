package com.backend.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ForgotPassword {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer fpid;
	@Column(nullable = false)
	private Integer otp;
	@Column(nullable = false)
	private Date expirationTime;
	
	@OneToOne
	@JoinColumn(name = "user_id") // Khóa ngoại trỏ tới UserInfo
	private UserInfo user;
}
