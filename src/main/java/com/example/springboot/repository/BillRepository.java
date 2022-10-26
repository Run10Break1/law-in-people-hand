package com.example.springboot.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springboot.model.Bill;


public interface BillRepository extends JpaRepository<Bill, String> {
	Page<Bill> findAllByStageNull(Pageable pageable);
}
