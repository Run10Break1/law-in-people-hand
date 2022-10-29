package com.project.lawinpeoplehand.model.dto;

import java.time.LocalDate;

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
	
	private LocalDate proposeDate;
	
	private String currCommitteeId;
	
	private String currCommittee;
	
	private LocalDate committeeProcDate;
	
	private String url;
	
	private String procResultCodee;
	
	private LocalDate procDate;
	
	private ProcessStage stage;
	
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
		committeeProcDate = bill.getCommitteeProcDate();
		url = bill.getUrl();
		procResultCodee = bill.getProcResultCode();
		procDate = bill.getProcDate();
		stage = bill.getStage();
	}
}
