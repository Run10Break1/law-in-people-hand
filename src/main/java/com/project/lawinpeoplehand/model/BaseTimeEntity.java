package com.project.lawinpeoplehand.model;

import java.time.LocalDate;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;


@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public class BaseTimeEntity {
	
	@CreatedDate
	private LocalDate createdAt;
	
	@LastModifiedDate
	private LocalDate updatedAt;
}
