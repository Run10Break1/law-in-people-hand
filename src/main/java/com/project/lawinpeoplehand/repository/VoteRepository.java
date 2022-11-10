package com.project.lawinpeoplehand.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.project.lawinpeoplehand.model.Bill;
import com.project.lawinpeoplehand.model.User;
import com.project.lawinpeoplehand.model.Vote;


public interface VoteRepository extends JpaRepository<Vote, Long>, VoteRepositoryExtension {
	Page<Vote> findAllByUser(User user, Pageable pageable);
	Optional<Vote> findFirstByUserAndBill(User user, Bill bill);
}
