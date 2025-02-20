package com.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.entity.Credit;

@Repository
public interface CreditRepository extends JpaRepository<Credit, Long> {

}
