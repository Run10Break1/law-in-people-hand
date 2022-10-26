package com.example.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springboot.model.Bill;


public interface BillRepository extends JpaRepository<Bill, String> {

}
