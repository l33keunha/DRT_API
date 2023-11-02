package ybs.api.boot.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;

import org.springframework.stereotype.Component;

/**
*
* @Class Name : parsingJSONUtil.java
* @Description : 경로탐색 엔진 호출_예약자의 출도착지간의 소요시간 및 거리 리턴
* @Modification Information
*
*  수정일			수정자		수정내용
*  -------------	---------	-------------------------------
*  2022. 10. 25.				최초생성
*
* @author 이근하
* @since 2022. 10. 25.
* @version 1.0
* @see
*
*  Copyright (C) by YBS All right reserved.
*/

@Component
public class TimeUtil {


	public HashMap<String, Object> getBoardingTmST(HashMap<String, Object> map) {
		String ROUTE_TM = String.valueOf(map.get("ROUTE_TM")).substring(0,2) + ":" + "00" + ":" + "00";
		String ST_TM = null;
		
		if(map.containsKey("ST_TM_NEXT")) {
			ST_TM = String.valueOf(map.get("ST_TM_NEXT")).substring(0,2) 
					+ ":" + String.valueOf(map.get("ST_TM_NEXT")).substring(2,4)
					+ ":" + String.valueOf(map.get("ST_TM_NEXT")).substring(4,6);
		} else {
			ST_TM = String.valueOf(map.get("ST_TM")).substring(0,2) 
					+ ":" + String.valueOf(map.get("ST_TM")).substring(2,4)
					+ ":" + String.valueOf(map.get("ST_TM")).substring(4,6);
		}
		
		// 노선 출발시간
		LocalTime ROUTELocalTime = LocalTime.parse(ROUTE_TM);
		// 예약시간
		LocalTime STLocalTime = LocalTime.parse(ST_TM);
		// 탑승예정시간
		LocalTime BOARDINGLocalTime = ROUTELocalTime.plusSeconds((Integer)(map.get("TOTAL_TM")));
		
		System.out.println("버스 출발 시간 : " + ROUTELocalTime);
		System.out.println("내가 타려는 시간 : " + STLocalTime);
		System.out.println("버스 도착 예정 시간 : " + BOARDINGLocalTime);
		
		HashMap<String, Object> mapST_STTN = new HashMap<String, Object>();
		
		if(STLocalTime.isAfter(BOARDINGLocalTime)){
			mapST_STTN.put("YN","N");
		} else {
			mapST_STTN.put("YN","Y");
		}
		mapST_STTN.put("BOARDING_TM", String.valueOf(BOARDINGLocalTime).replaceAll(":", ""));
		return mapST_STTN;
	}
	
	public HashMap<String, Object> getBoardingTmWAY(HashMap<String, Object> map) {
		String BOARDING_TM = String.valueOf(map.get("BOARDING_TM")).substring(0,2) + ":" + "00" + ":" + "00";
		String ST_TM = String.valueOf(map.get("ST_TM")).substring(0,2) 
				+ ":" + String.valueOf(map.get("ST_TM")).substring(2,4)
				+ ":" + String.valueOf(map.get("ST_TM")).substring(4,6);
		
		// 전 정류장 탑승시간
		LocalTime BOARDINGLocalTime = LocalTime.parse(BOARDING_TM);
		LocalTime STLocalTime = LocalTime.parse(ST_TM);
		
		
		System.out.println("전 정류장 탑승 시간 : " + BOARDINGLocalTime);
		System.out.println("내가 타려는 시간 : " + STLocalTime);
		
		HashMap<String, Object> mapWAY_STTN = new HashMap<String, Object>();
		
		if(STLocalTime.isAfter(BOARDINGLocalTime)){
			mapWAY_STTN.put("YN","N");
		} else {
			mapWAY_STTN.put("YN","Y");
		}
		return mapWAY_STTN;
	}

	public String booleanBoarding(HashMap<String, Object> map) {
		String result = null;
		
		String ST_TM = String.valueOf(map.get("ST_TM")).substring(0,2) 
				+ ":" + String.valueOf(map.get("ST_TM")).substring(2,4)
				+ ":" + String.valueOf(map.get("ST_TM")).substring(4,6);
		String BOARDING_TM = String.valueOf(map.get("BOARDING_TM")).substring(0,2) 
				+ ":" + String.valueOf(map.get("BOARDING_TM")).substring(2,4)
				+ ":" + String.valueOf(map.get("BOARDING_TM")).substring(4,6);
		
		// 예약시간
		LocalTime STLocalTime = LocalTime.parse(ST_TM);
		// 탑승예정시간
		LocalTime BOARDINGLocalTime = LocalTime.parse(BOARDING_TM);
		
		if(STLocalTime.isAfter(BOARDINGLocalTime)){
			result = "N";
		} else {
			result = "Y";
		}
		
		return result;
	}

	public String getBoardingTm4Tm(HashMap<String, Object> map) {
		String ST_TM = String.valueOf(map.get("ST_TM")).substring(0,2) 
				+ ":" + String.valueOf(map.get("ST_TM")).substring(2,4)
				+ ":" + String.valueOf(map.get("ST_TM")).substring(4,6);
		
		String ROUTE_TM = String.valueOf(map.get("ROUTE_TM")).substring(0,2) 
				+ ":" + String.valueOf(map.get("ROUTE_TM")).substring(2,4)
				+ ":" + String.valueOf(map.get("ROUTE_TM")).substring(4,6);
		
		// 노선 출발시간
		LocalTime ROUTELocalTime = LocalTime.parse(ROUTE_TM);
		// 예약시간
		LocalTime STLocalTime = LocalTime.parse(ST_TM);
		// 탑승예정시간
		LocalTime BOARDINGLocalTime = ROUTELocalTime.plusSeconds((Integer)(map.get("TOTAL_TM")));
		
		return String.valueOf(BOARDINGLocalTime).replaceAll(":", "");
	}
	
	public String getScheNo() {
		
		 Date date = new Date();
	        
        // Date 객체를 Timestamp로 변환합니다.
        Timestamp timestamp = new Timestamp(date.getTime());
        
        // Timestamp를 문자열로 변환합니다.
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String scheNo = sdf.format(timestamp);
        
        System.out.println("경로탐색 운행번호 : " +  scheNo);
		return scheNo;
	}
	
}
