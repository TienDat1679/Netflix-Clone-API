package com.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.entity.Credit;

public interface CreditRepository extends JpaRepository<Credit, Long> {

}
