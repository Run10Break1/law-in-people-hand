package com.project.lawinpeoplehand.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

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
	@Column(nullable = false)
	private Integer billNo;
	
	@JsonProperty("AGE")
	@Column(nullable = false)
	private Integer age;
	
	@JsonProperty("BILL_NAME")
	@Column(nullable = false)
	private String billName;
	
	@JsonProperty("PROPOSER")
	private String proposer;
	
	@JsonProperty("PROPOSER_KIND")
	@Column(nullable = false)
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
	@Column(nullable = false)
	private String url;
	
	@JsonProperty("PROC_RESULT_CD")
	private String procResultCode;
	
	@JsonProperty("PROC_DT")
	private LocalDate procDate;
	
	@Enumerated(EnumType.STRING) // enum을 데이터베이스에 string으로 저장하기
	@Column(nullable = false)
	private ProcessStage stage;
	
	//@Column(nullable = false)
	private Integer stageOrder;
	
	private LocalDate stageUpdateAt;
	
	@Column(columnDefinition = "TEXT")
	private String overview;
	
	@OneToMany(mappedBy = "bill", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Vote> votedList = new ArrayList<>();
	
	@OneToMany(mappedBy = "bill", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<BillKeyword> billKeywordList = new ArrayList<>();
}
