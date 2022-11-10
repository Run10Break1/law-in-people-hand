package com.project.lawinpeoplehand.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.lawinpeoplehand.model.Congressman;


public interface CongressmanRepository extends JpaRepository<Congressman, Long> {

}
