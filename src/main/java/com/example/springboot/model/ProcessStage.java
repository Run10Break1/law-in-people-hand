package com.example.springboot.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;

@Getter
public enum ProcessStage {
	RECEPTION("접수"),
	COMMITTEE_REVIEW("위원회 심사"),
	EXAMINATION_OF_LEGALITY_AND_WORDING("체계자구 심사"),
	PLENARY_SITTING("본회의 심의"),
	TRANSMISSION_TO_THE_GOVERNMENT("정부 이송"),
	GOVERNMENT_RECONCILIATION("정부 재의"),
	EMERGENCY_CABINET_COUNCIL("비상국무회의로이관"),
	SUBSTITUTED_DISPOSAL("대안반영폐기"),
	AGE_EXPIRE_DISPOSAL("임기만료폐기"),
	NON_RECURRING_DISPOSAL("회기불계속폐기"),
	DISPOSAL("폐기"),
	WITHDRAW("철회"),
	DENIAL("부결"),
	PROMULGATION("공포");
	
	private ProcessStage(String korName) {
		this.korName = korName;
	}
	
	private final String korName;
	
	// 아래는 korName으로 enum을 얻기 위한 코드
	
	private static final Map<String, ProcessStage> mapping;
	
	static {
		Map<String, ProcessStage> map = new ConcurrentHashMap<>();
		for(ProcessStage ps : ProcessStage.values()) {
			map.put(ps.korName, ps);
		}
		mapping = map;
	}
	
	public static ProcessStage get(String korName) {
		return mapping.get(korName);
	}
}