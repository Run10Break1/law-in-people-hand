package com.example.springboot.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Getter;


@Entity
@Data
public class Bill {
	
	@JsonProperty("BILL_ID")
	@Id
	private String billID;
	
	@JsonProperty("BILL_NO")
	private Integer billNo;
	
	@JsonProperty("AGE")
	private Integer age;
	
	@JsonProperty("BILL_NAME")
	private String billName;
	
	@JsonProperty("PROPOSER")
	private String proposer;
	
	@JsonProperty("PROPOSER_KIND")
	private String proposerKind;
	
	@JsonProperty("PROPOSE_DT")
	private LocalDate proposeDate;
	
	@JsonProperty("CURR_COMMITTEE_ID")
	private String currCommitteeId;
	
	@JsonProperty("CURR_COMMITTEE")
	private String currCommittee;
	
	@JsonProperty("COMMITTEE_DT")
	private LocalDate committeeDate;
	
	@JsonProperty("COMMITTEE_PROC_DT")
	private LocalDate committeeProcDate;
	
	@JsonProperty("LINK_URL")
	private String url;
	
	@JsonProperty("PROC_RESULT_CD")
	private String procResultCode;
	
	@JsonProperty("PROC_DT")
	private LocalDate procDate;
	
	@Enumerated(EnumType.STRING) // enum을 데이터베이스에 string으로 저장하기
	private ProcessStage stage;
}
