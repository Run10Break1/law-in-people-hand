package com.example.springboot.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;


@Entity
@Data
public class Bill {
	
	@Id
	private String billID;
	
	private Integer billNo;
	
	private Integer age;
	
	private String billName;
	
	private String proposer;
	
	private String proposerKind;
	
	private LocalDate proposeDate;
	
	private Integer currCommitteeId;
	
	private String currCommittee;
	
	private LocalDate committeeDate;
	
	private LocalDate committeeProcDate;
	
	private String url;
	
	private String procResultCode;
	
	private LocalDate procDate;
}
