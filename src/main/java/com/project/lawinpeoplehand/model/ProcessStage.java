package com.project.lawinpeoplehand.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;

@Getter
public enum ProcessStage {
	// stageOrder를 존재하는 값으로 업데이트할 때는 임시 값이 필요합니다.
	// 이를 위해 0을 비워뒀습니다.
	RECEPTION("접수"), // 4(아래부터 주석처리된 숫자는 stageOrder를 의미합니다.)
	COMMITTEE_REVIEW("위원회 심사"), // 3
	EXAMINATION_OF_LEGALITY_AND_WORDING("체계자구 심사"), // 1
	PLENARY_SITTING("본회의 심의"), // 2
	TRANSMISSION_TO_THE_GOVERNMENT("정부 이송"), // 5
	GOVERNMENT_RECONCILIATION("정부 재의"), // 6
	EMERGENCY_CABINET_COUNCIL("비상국무회의로 이관"), // 7
	SUBSTITUTED_DISPOSAL("대안반영폐기"), // 13
	AGE_EXPIRE_DISPOSAL("임기만료폐기"), // 14
	NON_RECURRING_DISPOSAL("회기불계속폐기"), // 15
	REFLECTION_AMENDMENT_DISPOSAL("수정안반영폐기"), // 16
	SEND_BACK("반려"), // 11
	DISPOSAL("폐기"), // 12
	WITHDRAW("철회"), // 10
	DENIAL("부결"), // 9
	PROMULGATION("공포"); // 8
	
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