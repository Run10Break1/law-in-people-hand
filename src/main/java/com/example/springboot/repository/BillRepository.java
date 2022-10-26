package com.example.springboot.repository;

import java.util.List;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springboot.model.Bill;


public interface BillRepository extends JpaRepository<Bill, String> {

}
