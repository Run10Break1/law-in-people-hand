package com.project.lawinpeoplehand.model.dto;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.lawinpeoplehand.model.Bill;
import com.project.lawinpeoplehand.model.ProcessStage;

import lombok.Data;


@Data
public class BillResponse {
	
	private String billId;
	
	private Integer billNo;
	
	private Integer age;
	
	private String billName;
	
	private String proposer;
	
	private String proposerKind;
	
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate proposeDate;
	
	private String currCommitteeId;
	
	private String currCommittee;
	
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate committeeDate;
	
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate committeeProcDate;
	
	private String url;
	
	private String procResultCode;
	
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate procDate;
	
	private ProcessStage stage;
	
	private String overview;
	
	private List<String> keywords;
	
	public BillResponse(Bill bill) {
		billId = bill.getBillID();
		billNo = bill.getBillNo();
		age = bill.getAge();
		billName = bill.getBillName();
		proposer = bill.getProposer();
		proposerKind = bill.getProposerKind();
		proposeDate = bill.getProposeDate();
		currCommitteeId = bill.getCurrCommitteeId();
		currCommittee = bill.getCurrCommittee();
		committeeDate = bill.getCommitteeDate();
		committeeProcDate = bill.getCommitteeProcDate();
		url = bill.getUrl();
		procResultCode = bill.getProcResultCode();
		procDate = bill.getProcDate();
		stage = bill.getStage();
		overview = bill.getOverview();
	}
}
