package com.project.lawinpeoplehand.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.lawinpeoplehand.model.Congressman;


public interface CongressmanRepository extends JpaRepository<Congressman, Long> {
	List<Congressman> findAllByNameAndAge(String name, Integer age);
}
