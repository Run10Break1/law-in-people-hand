package com.project.lawinpeoplehand.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.lawinpeoplehand.model.See;


public interface SeeRepository extends JpaRepository<See, Long>, SeeRepositoryExtension {

}
