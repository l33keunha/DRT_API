package ybs.api.boot.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;

import ybs.api.boot.org.service.xmlVO;

/**
*
* @Class Name : ConnectionURLUtil.java
* @Description : 경로탐색 엔진 호출_예약 건 경유지로 적용하여 경로탐색
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
public class ConnectionURLUtil {

	// String파라미터 만들기_org
	public String makeParam(List<HashMap<String, Object>> list) {
		String BEG_X = String.valueOf(list.get(0).get("STTN_X"));
		String BEG_Y = String.valueOf(list.get(0).get("STTN_Y"));
		String END_X = String.valueOf(list.get(1).get("STTN_X"));
		String END_Y = String.valueOf(list.get(1).get("STTN_Y"));
		String WAYPOINT ="";
		
		if(list.size() > 2) {
			for(int i = 2; i < list.size(); i++) {
					WAYPOINT += list.get(i).get("STTN_X");
					WAYPOINT += ",";
					WAYPOINT += list.get(i).get("STTN_Y");
					if(i != list.size() -1) {
						WAYPOINT += "_";
					}
				}
		} else {
			WAYPOINT = "";
		}
		
		String param = "TYPE=ROUTING"
				+ "&END_X=" + END_X 
				+ "&END_Y=" + END_Y
				+ "&BEG_X=" + BEG_X
				+ "&BEG_Y=" + BEG_Y
				+ "&ANG=&OPTION=0"
				+"&WAYPOINT=" + WAYPOINT
				;
		System.out.println(param);
		return param;
	}
	
	// String파라미터 만들기_org
	public String makeParam_rev(List<Double> sttnCoordArr) {
		
		// 2023 도시형 6번 노선 기종점
		String BEG_X = "387563.9845";
		String BEG_Y = "315885.8577";
		String END_X = "388782.8396";
		String END_Y = "314516.4885";
		
		String WAYPOINT = "";
		for(int i = 0; i < sttnCoordArr.size(); i++) {
			WAYPOINT += sttnCoordArr.get(i);
			if(i%2==0) {
				WAYPOINT += ",";
			} else {
				WAYPOINT += "_";
			}
		}
		
		String param = "TYPE=ROUTING" + "&END_X=" + END_X + "&END_Y=" + END_Y + "&BEG_X=" + BEG_X + "&BEG_Y=" + BEG_Y
				+ "&ANG=&OPTION=0" + "&WAYPOINT=" + WAYPOINT;
		System.out.println(("********엔진에 보낼 파라미터***********"));
		System.out.println(param);
		
		return param;
	}
	
	public ArrayList<xmlVO> connectionURL(String param) throws Exception{
		
		URL url = new URL("http://192.168.0.119:8888/req");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(param);
        wr.flush();
        
        BufferedReader br =  new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line = null;
        
        while((line = br.readLine()) != null) {
        	sb.append(line);
        }
        System.out.println("/*---------------엔진결과");
        System.out.println(sb.toString());
        
        ParsingHashMapUtil mUtil = new ParsingHashMapUtil();
		return mUtil.xmlParsingJson(sb.toString());
	}
	
	public ArrayList<xmlVO> connectionURL_rev(String param, List<Integer> sttnList) throws Exception{
		
		URL url = new URL("http://192.168.0.119:8888/req");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setDoOutput(true);
		
		OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		wr.write(param);
		wr.flush();
		
		BufferedReader br =  new BufferedReader(new InputStreamReader(conn.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String line = null;
		
		while((line = br.readLine()) != null) {
			sb.append(line);
		}
		System.out.println("/*---------------엔진결과");
		System.out.println(sb.toString());
		
		ParsingHashMapUtil mUtil = new ParsingHashMapUtil();
		return mUtil.xmlParsingJson_rev(sb.toString(), sttnList);
	}
	
}
