package com.project.lawinpeoplehand.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.project.lawinpeoplehand.model.Bill;
import com.project.lawinpeoplehand.model.ProcessStage;


public interface BillRepository extends JpaRepository<Bill, String>, BillRepositoryExtension {
	
	Page<Bill> findAll(Pageable pageable);
	
	// 이 줄 아래부터 MigrationService를 위한 쿼리 메소드
	Page<Bill> findAllByStageNull(Pageable pageable);
	Page<Bill> findAllByStage(ProcessStage stage, Pageable pageable);
	
	Page<Bill> findAllByOverviewNull(Pageable pageable);
	Page<Bill> findAllByOverviewNotNull(Pageable pageable);
}
