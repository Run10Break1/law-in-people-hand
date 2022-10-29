package com.project.lawinpeoplehand.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Entity
@Data
@EqualsAndHashCode(of = "id", callSuper = false)
public class Vote extends BaseTimeEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn
	private User user;
	
	@ManyToOne
	@JoinColumn
	private Bill bill;
	
	@Enumerated(EnumType.STRING)
	private VoteType voteType;
	
	private Boolean isLiked;
	
	private LocalDateTime voteAt;
	
	private LocalDateTime viewAt;
}
