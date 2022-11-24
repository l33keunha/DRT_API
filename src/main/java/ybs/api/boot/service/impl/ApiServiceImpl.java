package ybs.api.boot.service.impl;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ybs.api.boot.service.ApiService;
import ybs.api.boot.service.xmlVO;
import ybs.api.boot.util.ConnectionURLUtil;
import ybs.api.boot.util.TimeUtil;

/**
 *
 * @Class Name : ApiServiceImpl.java
 * @Description : DRT API ApiServiceImpl
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

@Service
public class ApiServiceImpl implements ApiService {

    @Autowired
    private ApiMapper mapper;

    /**
     * 1. 운행지역 기준 정류장 정보 조회
     * @param map
     * @return list<map>
     */
    @Override
    public List<HashMap<String,Object>> getSttn(HashMap<String, Object> map) { return mapper.getSttn(map); }

    /**
     * 2. 탑승 조건 기준 배차 정보 조회
     * @param map
     * @return map
     * @throws Exception 
     */
    @Override
    public List<HashMap<String, Object>> getSche(HashMap<String, Object> map) throws Exception { 
    	/* 재료준비 */
    	
    	List<HashMap<String, Object>> fList = new ArrayList<HashMap<String, Object>>();				// return
    	HashMap<String, Object> fMap = new HashMap<String, Object>();									// return
    	
    	List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();				// List to 경로탐색 : set xy좌표
    	ArrayList<xmlVO> vList = new ArrayList<xmlVO>();															// return from 경로탐색 : get 소요시간
    	HashMap<String, Object>mapST_STTN = new HashMap<String, Object>();							// return from getBoardingTmST 탑승여부, 탑승시간
    	HashMap<String, Object>mapWAY_STTN = new HashMap<String, Object>();						// return from getBoardingTmWAY : get 탑승여부
    	
    	HashMap<String, Object> goMap = new HashMap<String, Object>();									// ** 예약순번과 정류장 거르기에 필요한 마라미터만 담을 것
    	
    	
    	// Util 호출
    	ConnectionURLUtil cUtil = new ConnectionURLUtil();
    	TimeUtil tUtil = new TimeUtil();
    	
    	
    	// Q1. 노선의 첫번째 예약자인가? - findUser(ST_TM)
    	goMap.put("ROUTE_ID",  map.get("ROUTE_ID"));
    	goMap.put("SDATE",  map.get("SDATE"));
    	goMap.put("ST_TM", map.get("ST_TM")); 																			// 사용자가 원하는 시간을 먼저 고려한다.
    	
    	int cnt = mapper.findUser(goMap);																					// cnt == 0 : 첫번째 예약자 , cnt != 0 : 첫번째 예약자X 
    	if( cnt == 0 ) {		// Q1 - A1. 첫번 째 예약자
    		// (기점 - 정류장 소요시간 비교)
    		goMap.put("STTN_IN_ID", map.get("STTN_IN_ID")); goMap.put("STTN_OUT_ID", map.get("STTN_OUT_ID")); 
    		list = mapper.getSt2In(goMap);																					// (기점 - 정류장 XY좌표)
    		vList = cUtil.connectionURL(cUtil.makeParam(list));														// RP엔진 경로
    		
    		goMap.put("ROUTE_TM", map.get("ST_TM"));																// 버스 기점 출발시간
    		goMap.put("TOTAL_TM", vList.get(0).getTotalTm());														// (기점 - 정류장 소요시간)
    		
    		mapST_STTN = tUtil.getBoardingTmST(goMap);
    		
    		goMap.put("BOARDING_TM", mapST_STTN.get("BOARDING_TM"));
    		/* 승차정류장 - (경유지) - 하차정류장 소요거리 및 소요시간 */
    		goMap.put("TOTAL_TIME", cUtil.connectionURL(cUtil.makeParam(mapper.getUserXY(goMap))).get(0).getTotalTm());
    		goMap.put("TOTAL_DISTANCE", cUtil.connectionURL(cUtil.makeParam(mapper.getUserXY(goMap))).get(0).getTotalDist());
    		fMap.put("1", mapper.getSche(goMap));
    		
    		// Q2. 시간 내 탑승이 가능한가? 
    		String YN = String.valueOf(mapST_STTN.get("YN"));														// mapST_STTN.get("YN") == "Y" : 시간 내 가능, mapST_STTN.get("YN") == "N" : 불가능
    		
    		if( YN == "Y" ) {		// Q2 - A1. 시간 내 탑승 가능함.
    			System.out.println("TM1 -End [F1]");
    			
    			fList.add(fMap);

    		} else if( YN == "N" ) {		// Q2 - A2. 시간 내 탑승 불가능함.											// ** [다음 배차 비교] 여기서 ST_TM -> ST_TM_NEXT로 변경
    			// Q3. 다음 노선의 첫번째 예약자인가? - findUser(ST_TM_NEXT)
    			int tmp = Integer.parseInt(String.valueOf(map.get("ST_TM")).substring(0,2)) +1;
    			String ST_TM_NEXT = String.valueOf(tmp) + "0000"; 													// 다음 배차시간
    			
    			goMap.put("ST_TM",  ST_TM_NEXT);																		// 다음 배차시간을 기준으로 스케줄을 가져온다.
    			cnt = mapper.findUser(goMap);																				// cnt == 0 : 첫번째 예약자 , cnt != 0 : 첫번째 예약자X
    			if( cnt == 0 ) {		// Q3 - A1. 첫번 째 예약자  -> (다음 배차 기점 - 정류장 소요시간 비교)
    				System.out.println("TM1 + TM2 -End [F2]");
    				
    				// (다음 배차 기점 - 정류장 소요시간 비교)
    	    		goMap.put("ROUTE_TM", ST_TM_NEXT);																// 버스 기점 출발시간			※ 여기서는 다음 배차시간이 된다.
    	    		goMap.put("TOTAL_TM", vList.get(0).getTotalTm());												// (기점 - 정류장 소요시간)   ※ 위와 동일
    	    		
    	    		mapST_STTN = tUtil.getBoardingTmST(goMap);
    	    		
    	    		goMap.put("BOARDING_TM", mapST_STTN.get("BOARDING_TM"));
    	    		/* 승차정류장 - (경유지) - 하차정류장 소요거리 및 소요시간 */
    	    		goMap.put("TOTAL_TIME", cUtil.connectionURL(cUtil.makeParam(mapper.getUserXY(goMap))).get(0).getTotalTm());
    	    		goMap.put("TOTAL_DISTANCE", cUtil.connectionURL(cUtil.makeParam(mapper.getUserXY(goMap))).get(0).getTotalDist());
    	    		fMap.put("2", mapper.getSche(goMap));
    				
    	    		fList.add(fMap);
    	    		
    			} else if (cnt != 0 ) {		// Q3 - A2. 첫번 째 예약자가 아님 
    				// Q4. 겹치는 정류장이 있는가?
    				String BOARDING_TM = mapper.findSttn(goMap);													// mapper.findSttn(goMap) !=  null : 겹치는 정류장O , mapper.findSttn(goMap) == null : 겹치는 정류장X
    				
    				if( BOARDING_TM != null ) {		// Q4 - A1. 겹치는 정류장이 있음 -> 그대로 갖다 씀
    					System.out.println("TM1 + TM2 -End [F3]");
    					
    					goMap.put("BOARDING_TM", BOARDING_TM);													// 다음 배차시간에 예약되어있던 정류장의 탑승시간을 그대로 쓴다. (무조건 탑승)
    		    		/* 승차정류장 - (경유지) - 하차정류장 소요거리 및 소요시간 */
    		    		goMap.put("TOTAL_TIME", cUtil.connectionURL(cUtil.makeParam(mapper.getUserXY_2(goMap))).get(0).getTotalTm());
    		    		goMap.put("TOTAL_DISTANCE", cUtil.connectionURL(cUtil.makeParam(mapper.getUserXY_2(goMap))).get(0).getTotalDist());
        	    		fMap.put("2", mapper.getSche(goMap));
    					
        	    		fList.add(fMap);
        	    		
    				} else if( BOARDING_TM == null ) {		// Q4 - A.2 겹치는 정류장이 없음 -> (다음 배차의 마지막 정류장 - 정류장 소요시간 비교)
        				System.out.println("TM1 + TM2 -End [F4]");
        				
        				// (다음 배차의 마지막 정류장 - 정류장 소요시간 비교)
        				
        				// {SDATE=20221007, STTN_IN_ID=7023, ROUTE_TM=131000, ROUTE_ID=48421053, TOTAL_TM=7, BOARDING_TM=130007, ST_TM=140000}
        				list = mapper.getWay2In(goMap);																	// (마지막정류장 - 정류장 XY좌표)
        				vList = cUtil.connectionURL(cUtil.makeParam(list));											// RP엔진 경로
        				
        				goMap.put("ROUTE_TM", list.get(0).get("BOARDING_TM"));									// 마지막 정류장 탑승시간(=출발시간)
        				goMap.put("TOTAL_TM", vList.get(0).getTotalTm());											// (기마지막정류장점 - 정류장 소요시간)
        				
        				goMap.put("BOARDING_TM", tUtil.getBoardingTm4Tm(goMap));
        				/* 승차정류장 - (경유지) - 하차정류장 소요거리 및 소요시간 */
    		    		goMap.put("TOTAL_TIME", cUtil.connectionURL(cUtil.makeParam(mapper.getUserXY(goMap))).get(0).getTotalTm());
    		    		goMap.put("TOTAL_DISTANCE", cUtil.connectionURL(cUtil.makeParam(mapper.getUserXY(goMap))).get(0).getTotalDist());
        	    		fMap.put("2", mapper.getSche(goMap));
        	    		
        	    		fList.add(fMap);
    				}
    			}
    		}
    	} else if( cnt != 0 ) {		// Q1 - A2. 첫번 째 예약자가 아님
    		// Q2. 겹치는 정류장이 있는가?
    		goMap.put("STTN_IN_ID", map.get("STTN_IN_ID")); goMap.put("STTN_OUT_ID", map.get("STTN_OUT_ID"));
    		String BOARDING_TM = mapper.findSttn(goMap);											// mapper.findSttn(goMap) !=  null : 겹치는 정류장O , mapper.findSttn(goMap) == null : 겹치는 정류장X
    		if( BOARDING_TM != null ) {		// Q2 - A1. 겹치는 정류장이 있음 
    			// (겹치는 정류장 탑승시간 - 나의 시간 소요시간 비교)
    			goMap.put("BOARDING_TM", BOARDING_TM);
    			// Q3. 시간내 탑승이 가능한가?
    			String YN = tUtil.booleanBoarding(goMap);												// mapWAY_STTN.get("YN") == "Y" : 시간 내 가능, mapWAY_STTN.get("YN") == "N" : 불가능
    			
    			/* 승차정류장 - (경유지) - 하차정류장 소요거리 및 소요시간 */
	    		goMap.put("TOTAL_TIME", cUtil.connectionURL(cUtil.makeParam(mapper.getUserXY_2(goMap))).get(0).getTotalTm());
	    		goMap.put("TOTAL_DISTANCE", cUtil.connectionURL(cUtil.makeParam(mapper.getUserXY_2(goMap))).get(0).getTotalDist());
				fMap.put("1", mapper.getSche(goMap));
				
				if( YN == "Y" ) {		// Q3 - A1. 시간 내 탑승 가능함.
    					System.out.println("TM1 -End [F5]");
    	    		
    					fList.add(fMap);
    	    		
    			} else if( YN == "N" ) {		// Q3 - A2. 시간 내 탑승 불가능함.										// ** [다음 배차 비교] 여기서 ST_TM -> ST_TM_NEXT로 변경
    				
        			list = mapper.getWay2In(goMap);
        			
        			System.out.println("보너스 : " + goMap.toString());
        			
        			if( mapper.findSttn_BONUS(goMap) > 0 ) {
        				/* 보너스 */
        				vList = cUtil.connectionURL(cUtil.makeParam(list));					
        				
        				goMap.put("ROUTE_TM", list.get(0).get("BOARDING_TM"));								// 마지막 정류장 탑승시간(=출발시간)
        				goMap.put("TOTAL_TM", vList.get(0).getTotalTm());										// (기마지막정류장점 - 정류장 소요시간)
        				
        				goMap.put("BOARDING_TM", tUtil.getBoardingTm4Tm(goMap));
        				/* 승차정류장 - (경유지) - 하차정류장 소요거리 및 소요시간 */
    		    		goMap.put("TOTAL_TIME", cUtil.connectionURL(cUtil.makeParam(mapper.getUserXY(goMap))).get(0).getTotalTm());
    		    		goMap.put("TOTAL_DISTANCE", cUtil.connectionURL(cUtil.makeParam(mapper.getUserXY(goMap))).get(0).getTotalDist());
        				fMap.put("2", mapper.getSche(goMap));
        			}
    				
    				// Q4. 다음 노선의 첫번째 예약자인가? - findUser(ST_TM_NEXT)
        			int tmp = Integer.parseInt(String.valueOf(map.get("ST_TM")).substring(0,2)) +1;
        			String ST_TM_NEXT = String.valueOf(tmp) + "0000"; 												// 다음 배차시간
        			
        			goMap.put("ST_TM",  ST_TM_NEXT);																	// 다음 배차시간을 기준으로 스케줄을 가져온다.
    				cnt = mapper.findUser(goMap);																			// cnt == 0 : 첫번째 예약자 , cnt != 0 : 첫번째 예약자X
    				if( cnt == 0 ) { 	// Q4 - A1. 다음 노선의 첫번 째 예약자 ->  (다음 배차 기점 - 정류장 소요시간 비교)
    					System.out.println("TM1 + TM2 -End [F6]");
    					
    					list = mapper.getSt2In(goMap);																		// (기점 - 정류장 XY좌표)
    					vList = cUtil.connectionURL(cUtil.makeParam(list));											// RP엔진 경로
    					
    		    		goMap.put("ROUTE_TM", ST_TM_NEXT);															// 버스 기점 출발시간
    		    		goMap.put("TOTAL_TM", vList.get(0).getTotalTm());											// (기점 - 정류장 소요시간)
    		    		
    		    		mapST_STTN = tUtil.getBoardingTmST(goMap);
    		    		
    		    		goMap.put("BOARDING_TM", mapST_STTN.get("BOARDING_TM"));
    		    		/* 승차정류장 - (경유지) - 하차정류장 소요거리 및 소요시간 */
    		    		goMap.put("TOTAL_TIME", cUtil.connectionURL(cUtil.makeParam(mapper.getUserXY(goMap))).get(0).getTotalTm());
    		    		goMap.put("TOTAL_DISTANCE", cUtil.connectionURL(cUtil.makeParam(mapper.getUserXY(goMap))).get(0).getTotalDist());
    		    		fMap.put("3", mapper.getSche(goMap));
    		    		
    		    		fList.add(fMap);
    					 
    				} else if( cnt != 0) { 	// Q4 - A2. 다음 노선의 첫번 째 예약자 아님
    					// Q5. 다음 노선에 겹치는 정류장이 있는가?
    					BOARDING_TM = mapper.findSttn(goMap);														// mapper.findSttn(goMap) !=  null : 겹치는 정류장O , mapper.findSttn(goMap) == null : 겹치는 정류장X
    																																	// 다음 배차시간의 겹치는 정류장 확인
    					
    					if( BOARDING_TM != null ) {		// Q5 - A1. 겹치는 정류장이 있음 -> 그대로 갖다 씀
    						System.out.println("TM1 + TM2 -End [F7]");
    						
        					goMap.put("BOARDING_TM", BOARDING_TM);												// 다음 배차시간에 예약되어있던 정류장의 탑승시간을 그대로 쓴다. (무조건 탑승)
        					/* 승차정류장 - (경유지) - 하차정류장 소요거리 및 소요시간 */
        		    		goMap.put("TOTAL_TIME", cUtil.connectionURL(cUtil.makeParam(mapper.getUserXY_2(goMap))).get(0).getTotalTm());
        		    		goMap.put("TOTAL_DISTANCE", cUtil.connectionURL(cUtil.makeParam(mapper.getUserXY_2(goMap))).get(0).getTotalDist());
            	    		fMap.put("3", mapper.getSche(goMap));
        					
            	    		fList.add(fMap);
    						
    					} else if( BOARDING_TM == null ) {		// Q5 - A2. 겹치는 정류장이 없음 -> (다음 배차의 마지막 정류장 - 정류장 소요시간 비교)
    						System.out.println("TM1 + TM2 -End [F8]");
    						
    						// (다음 배차의 마지막 정류장 - 정류장 소요시간 비교)
    						
            				list = mapper.getWay2In(goMap);																// (마지막정류장 - 정류장 XY좌표)
            				vList = cUtil.connectionURL(cUtil.makeParam(list));										// RP엔진 경로
            				
            				goMap.put("ROUTE_TM", list.get(0).get("BOARDING_TM"));								// 마지막 정류장 탑승시간(=출발시간)
            				goMap.put("TOTAL_TM", vList.get(0).getTotalTm());										// (기마지막정류장점 - 정류장 소요시간)
            				
            				goMap.put("BOARDING_TM", tUtil.getBoardingTm4Tm(goMap));
            				/* 승차정류장 - (경유지) - 하차정류장 소요거리 및 소요시간 */
        		    		goMap.put("TOTAL_TIME", cUtil.connectionURL(cUtil.makeParam(mapper.getUserXY(goMap))).get(0).getTotalTm());
        		    		goMap.put("TOTAL_DISTANCE", cUtil.connectionURL(cUtil.makeParam(mapper.getUserXY(goMap))).get(0).getTotalDist());
            	    		fMap.put("3", mapper.getSche(goMap));
            	    		
            	    		fList.add(fMap);
    					}
    				}
    			}
    		} else if( BOARDING_TM == null ) {	// Q2 - A2 겹치는 정류장이 없음
    			list = mapper.getWay2In(goMap);																	// (마지막정류장 - 정류장 XY좌표)
				vList = cUtil.connectionURL(cUtil.makeParam(list));											// RP엔진 경로
				
				goMap.put("ROUTE_TM", list.get(0).get("BOARDING_TM"));									// 마지막 정류장 탑승시간(=출발시간)
				goMap.put("TOTAL_TM", vList.get(0).getTotalTm());											// (마지막정류장점 - 정류장 소요시간)
				goMap.put("BOARDING_TM", tUtil.getBoardingTm4Tm(goMap));
				/* 승차정류장 - (경유지) - 하차정류장 소요거리 및 소요시간 */
	    		goMap.put("TOTAL_TIME", cUtil.connectionURL(cUtil.makeParam(mapper.getUserXY(goMap))).get(0).getTotalTm());
	    		goMap.put("TOTAL_DISTANCE", cUtil.connectionURL(cUtil.makeParam(mapper.getUserXY(goMap))).get(0).getTotalDist());
	    		fMap.put("1", mapper.getSche(goMap));
	    		
	    		// Q3. 시간 내 탑승이 가능한가?
	    		String YN = tUtil.booleanBoarding(goMap);														// mapST_STTN.get("YN") == "Y" : 시간 내 가능, mapST_STTN.get("YN") == "N" : 불가능
	    		if( YN == "Y" ) { 	// Q3 - A1. 시간 내 탑승 가능함.
	    			System.out.println("TM1 -End [F9]");
	    			
	    			fList.add(fMap);

	    		} else if(YN == "N" ) {		// Q3 - A2. 시간 내 탑승 불가능함.								// ** [다음 배차 비교] 여기서 ST_TM -> ST_TM_NEXT로 변경
	    			// Q4. 다음 노선의 첫번째 예약자인가? - findUser(ST_TM_NEXT)
	    			int tmp = Integer.parseInt(String.valueOf(map.get("ST_TM")).substring(0,2)) +1;
        			String ST_TM_NEXT = String.valueOf(tmp) + "0000"; 												// 다음 배차시간
        			
        			goMap.put("ST_TM",  ST_TM_NEXT);																	// 다음 배차시간을 기준으로 스케줄을 가져온다.
    				cnt = mapper.findUser(goMap);																			// cnt == 0 : 첫번째 예약자 , cnt != 0 : 첫번째 예약자X
    				
    				if( cnt == 0 ) { 	// Q4 - A1. 다음 노선의 첫번 째 예약자 ->  (다음 배차 기점 - 정류장 소요시간 비교)
    					System.out.println("TM1 + TM2 -End [F10]");
    					
    					list = mapper.getSt2In(goMap);																		// (기점 - 정류장 XY좌표)
    					vList = cUtil.connectionURL(cUtil.makeParam(list));											// RP엔진 경로
    					
    		    		goMap.put("ROUTE_TM", ST_TM_NEXT);															// 버스 기점 출발시간
    		    		goMap.put("TOTAL_TM", vList.get(0).getTotalTm());											// (기점 - 정류장 소요시간)
    		    		
    		    		mapST_STTN = tUtil.getBoardingTmST(goMap);
    		    		
    		    		goMap.put("BOARDING_TM", mapST_STTN.get("BOARDING_TM"));
    		    		/* 승차정류장 - (경유지) - 하차정류장 소요거리 및 소요시간 */
    		    		goMap.put("TOTAL_TIME", cUtil.connectionURL(cUtil.makeParam(mapper.getUserXY(goMap))).get(0).getTotalTm());
    		    		goMap.put("TOTAL_DISTANCE", cUtil.connectionURL(cUtil.makeParam(mapper.getUserXY(goMap))).get(0).getTotalDist());
    		    		fMap.put("2", mapper.getSche(goMap));
    		    		
    		    		fList.add(fMap);
    				} else if( cnt != 0) { 	// Q4 - A2. 다음 노선의 첫번 째 예약자 아님
    					// Q5. 다음 노선에 겹치는 정류장이 있는가?
    					BOARDING_TM = mapper.findSttn(goMap);														// mapper.findSttn(goMap) !=  null : 겹치는 정류장O , mapper.findSttn(goMap) == null : 겹치는 정류장X
    																																	// 다음 배차시간의 겹치는 정류장 확인
    					
    					if( BOARDING_TM != null ) {		// Q5 - A1. 겹치는 정류장이 있음 -> 그대로 갖다 씀
    						System.out.println("TM1 + TM2 -End [F11]");
    						
        					goMap.put("BOARDING_TM", BOARDING_TM);												// 다음 배차시간에 예약되어있던 정류장의 탑승시간을 그대로 쓴다. (무조건 탑승)
        					/* 승차정류장 - (경유지) - 하차정류장 소요거리 및 소요시간 */
        		    		goMap.put("TOTAL_TIME", cUtil.connectionURL(cUtil.makeParam(mapper.getUserXY_2(goMap))).get(0).getTotalTm());
        		    		goMap.put("TOTAL_DISTANCE", cUtil.connectionURL(cUtil.makeParam(mapper.getUserXY_2(goMap))).get(0).getTotalDist());
            	    		fMap.put("2", mapper.getSche(goMap));
        					
            	    		fList.add(fMap);
    						
    					} else if( BOARDING_TM == null ) {		// Q5 - A2. 겹치는 정류장이 없음 -> (다음 배차의 마지막 정류장 - 정류장 소요시간 비교)
    						System.out.println("TM1 + TM2 -End [F12]");
    						
    						// (다음 배차의 마지막 정류장 - 정류장 소요시간 비교)
    						
            				list = mapper.getWay2In(goMap);																// (마지막정류장 - 정류장 XY좌표)
            				vList = cUtil.connectionURL(cUtil.makeParam(list));										// RP엔진 경로
            				goMap.put("ROUTE_TM", list.get(0).get("BOARDING_TM"));									// 마지막 정류장 탑승시간(=출발시간)
            				goMap.put("TOTAL_TM", vList.get(0).getTotalTm());											// (기마지막정류장점 - 정류장 소요시간)
            				
            				goMap.put("BOARDING_TM", tUtil.getBoardingTm4Tm(goMap));
            				/* 승차정류장 - (경유지) - 하차정류장 소요거리 및 소요시간 */
        		    		goMap.put("TOTAL_TIME", cUtil.connectionURL(cUtil.makeParam(mapper.getUserXY(goMap))).get(0).getTotalTm());
        		    		goMap.put("TOTAL_DISTANCE", cUtil.connectionURL(cUtil.makeParam(mapper.getUserXY(goMap))).get(0).getTotalDist());
            	    		fMap.put("2", mapper.getSche(goMap));
            	    		
            	    		fList.add(fMap);
    					}
    				}
	    		}
    		} 
    	}
    	
    	
    	for(Object m : fList) {
    		System.out.println(m.toString());
    	}
        
    	return fList;
    }

    /**
     * 3. 운전자가 운행할 차량의 예약자 조회
     * @param map
     * @return List<Map>
     */
    @Override
    public List<HashMap<String, Object>> getUserMast(HashMap<String, Object> map) { return mapper.getUserMast(map); }

    /**
     * 4. 예약 정보 저장
     * @param map
     * @return int
     * @throws Exception 
     */
    @Override
    public int setUser(HashMap<String, Object> map) throws Exception {
    	
    	/* 재료 준비 */
    	// 정류장ID에 매칭되는 XY좌표를 가져온다.
    	List<HashMap<String, Object>> UserXY = mapper.getUserXY(map);
    	
    	// RP엔진에서 소요거리와 소요시간을 가져온다.
    	ConnectionURLUtil util = new ConnectionURLUtil();
    	ArrayList<xmlVO> vList = util.connectionURL(util.makeParam(UserXY));
    	
    	map.put("TOTAL_DIST", vList.get(0).getTotalDist());
    	map.put("TOTAL_TM", vList.get(0).getTotalTm());
    	
    	// INSERT USER
        return mapper.setUser(map);
    }

    /**
     * 5. 노선정보 조회
     * @return List<Map>
     */
    @Override
    public List<HashMap<String, Object>> getRoute() { return mapper.getRoute(); }

    /**
     * 6. 이름 / 연락처로 예약 조회
     * @param map
     * @return List<Map>
     */
    @Override
    public List<HashMap<String, Object>> getUser(HashMap<String, Object> map) { return mapper.getUser(map); }

    /**
     * 7. 운행 종료 후 운행 정보 저장
     * @param map
     * @return int
     */
    @Override
    public int updateHist(HashMap<String, Object> map) { return mapper.updateHist(map); }

    /**
     * 8. 경로 탐색을 위한 출/도착지, 경유지의 xy 정보 제공
     * @param map
     * @return List<Map>
     */
    @Override
    public List<HashMap<String, Object>> getWayPoint(HashMap<String, Object> map) { return mapper.getWayPoint(map); }

    /**
     * 9. 경로 탐색 결과 값 저장 (xml -> db)
     * @param map
     * @return List<Map>
     * @throws Exception 
     */
    @Override
    public List<HashMap<String, Object>> setPath(HashMap<String, Object> map) throws Exception {
    	/*
				※ 필드 
					rList : 출도착지 xy List
					wList : 경유지 (정류장) xy List
					rwList = 출도착지 + 경유지 xy List ( 출발지 - 경유지 - 도착지 순서보장 )
					vList = RP엔진 리턴 값 ArrayList<xmlVO> 형태 List
    	 * */
    	
    	String scheNo = null;
    	int scheSubNo = 1;
        int result = 0;
        
        // 1. 출, 도착지 XY 가져온다.
        List<HashMap<String, Object>> rList = mapper.getRouteXY(map);
        // 1. 경유지 XY 가져온다.
        map.put("ST_TM", String.valueOf(map.get("ST_TM")).substring(0, 2));
        List<HashMap<String, Object>> wList = mapper.getWayPoint(map);
        
        List<HashMap<String, Object>> rwList = new ArrayList<HashMap<String, Object>>();
        rwList.add(rList.get(0));
        rwList.add(rList.get(1));
        for(HashMap<String, Object> obj : wList) {
        	rwList.add(obj);
        }
        
        for(HashMap<String, Object> oo : rwList) {
        	System.out.println(oo.toString());
        }
        
        // 2. rwList 로 요청하여 RP엔진에서 경로를 받아온다.
        ConnectionURLUtil cUtil = new ConnectionURLUtil();
        ArrayList<xmlVO> vList = cUtil.connectionURL(cUtil.makeParam(rwList));
        map.put("vList", vList);
        
        // 3. SCHE_NO 존재유무 확인 : 있으면 SUB_NO UPDATE, 없다면 NEW 운행 정보 
    	if(mapper.getScheNo(map) == null) {
    		int check = mapper.setHist(map);
    		if(check > 0) {
    			map.put("SCHE_NO", mapper.getScheNo(map));
    			map.put("SCHE_SUB_NO", "1");

    		} else {
            }
    	} else {
            scheNo = mapper.getScheNo(map);
    		scheSubNo = mapper.getScheSubNo(scheNo);
            scheSubNo++;

            map.put("SCHE_NO", scheNo);
            map.put("SCHE_SUB_NO", scheSubNo);
    	}
    	
    	// 4. INSERT PATH
    	int result2 = mapper.setPath(map);
    	if(result2 > 0) {
    		// 5. SELECT PATH
    		return mapper.getPath(map);
    	}
    	
    	return null; 
    }


    /**
     * 10. 배차 차량에 경로 제공
     * @param map
     * @return List<Map>
     */
    @Override
    public List<HashMap<String, Object>> getPath(HashMap<String, Object> map) { return mapper.getPath(map); }

    /**
     * 11. 운행 요약 정보 조회
     * @param map
     * @return List<Map>
     */
    @Override
    public List<HashMap<String, Object>> getHist(HashMap<String, Object> map) { return mapper.getHist(map); }


}
