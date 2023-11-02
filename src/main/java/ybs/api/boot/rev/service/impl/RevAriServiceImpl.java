package ybs.api.boot.rev.service.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ybs.api.boot.rev.service.RevAriService;
import ybs.api.boot.util.CanBoardLogicUtil;
import ybs.api.boot.util.ConnectionURLUtil;
import ybs.api.boot.util.ParsingHashMapUtil;
import ybs.api.boot.util.TimeUtil;

@Service
public class RevAriServiceImpl implements RevAriService {
	public static final String ROUTE_ID = "2";
	public static final String BEG_X = "387563.9845";
	public static final String BEG_Y = "315885.8577";
	public static final String END_X = "388782.8396";
	public static final String END_Y = "314516.4885";

	@Autowired
	private RevApiMapper mapper;
	@Autowired(required=true)
	private ConnectionURLUtil conUtil;
	@Autowired(required=true)
	private ParsingHashMapUtil parsListUtil;
	@Autowired(required=true)
	private TimeUtil timeUtil;
	@Autowired(required=true)
	private CanBoardLogicUtil cblUtil;
	
	
	// 정류장 정보 보내기 테스트
	@Override
	public List<HashMap<String, Object>> selectSttn() {
		return mapper.selectSttn(ROUTE_ID);
	}
	
	// 링크 레이어 테스트
	@Override
	public List<HashMap<String,Object>> selectLink() {
		return mapper.selectLink();
	}

	/**
    * 선택한 노선의 정류장들을 가져온다.
    */	
	@Override
	public List<HashMap<String, Object>> submitRouteId(Map<String, Object> map) {
		return mapper.submitRouteId(map);
	}


	
	
	@Override
	public HashMap<String, Object> submitSttn(Map<String, Object> map) throws IOException, Exception {
		// ⌈¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
		// |========================= 재료 준비 =========================
		// |	예약자 수 배열크기 값
				int cnt = (int) map.get("userCnt");
				int bnParam = Integer.parseInt(String.valueOf(map.get("bnParam")));
		// |			
		// |	예약 정보 
				List<String> begDataSttnSeqStr = (List<String>) map.get("begDataSttnSeq");
				List<String> endDataSttnSeqStr = (List<String>) map.get("endDataSttnSeq");
				List<String> begDataSttnNmStr = (List<String>) map.get("begDataSttnNm");
				List<String> endDataSttnNmStr = (List<String>) map.get("endDataSttnNm");
				List<String> hhListStr = (List<String>) map.get("hhList");
				List<String> mmListStr = (List<String>) map.get("mmList");
		// |	
		// |	예약 정보 -> 숫자형으로 변환
				List<Integer> begDataSttnSeq = new ArrayList<>();
				List<Integer> endDataSttnSeq = new ArrayList<>();
				List<Integer> hhList = new ArrayList<>();
				List<Integer> mmList = new ArrayList<>();
				for (int i = 0; i <= cnt; i++) {
					begDataSttnSeq.add(Integer.parseInt(begDataSttnSeqStr.get(i)));
					endDataSttnSeq.add(Integer.parseInt(endDataSttnSeqStr.get(i)));
					hhList.add(Integer.parseInt(hhListStr.get(i)));
					mmList.add(Integer.parseInt(mmListStr.get(i)));
				}
		// |				
		// ⌊_____________________________________________________________________________________________
		
		// *********탑승객리스트***********
		List<HashMap<String, Object>> userList = new ArrayList<>();
		HashMap<String, Object> userMap;
		for (int i = 0; i <= cnt; i++) {
			userMap = new HashMap<>();
			userMap.put("userNo", i);
			userMap.put("begDataSttnSeq", begDataSttnSeq.get(i));
			userMap.put("endDataSttnSeq", endDataSttnSeq.get(i));
			userMap.put("begDataSttnNm", begDataSttnNmStr.get(i));
			userMap.put("endDataSttnNm", endDataSttnNmStr.get(i));
			userMap.put("hhList", hhList.get(i));
			userMap.put("mmList", mmList.get(i));
			userMap.put("flag", 0);
			userList.add(i, userMap);
		}
		
		System.out.println(("*********탑승객리스트***********"));
		System.out.println(userList.toString());
		
// ------------------------------------------------------------------------------------------------------------------ //
		// 경로탐색 운행번호
		String scheNo = timeUtil.getScheNo();
		
		// 경로탐색 결과리스트
		List<HashMap<String,Object>> orderMaster_link = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String,Object>> order10_link = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String,Object>> order11_link = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String,Object>> orderMaster_node = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String,Object>> order10_node = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String,Object>> order11_node = new ArrayList<HashMap<String, Object>>();
		
		// |========================= 탑승 로직 =========================
		// #-- flag 분리
		// ⌈¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
				for(int i = 0; i < userList.size(); i++) {
					begDataSttnSeq.clear();
					endDataSttnSeq.clear();
					for(int j = 0; j <= i; j++) {
						if(Integer.parseInt(String.valueOf(userList.get(j).get("flag"))) == 0) {
							begDataSttnSeq.add(Integer.parseInt(String.valueOf(userList.get(j).get("begDataSttnSeq"))));
							endDataSttnSeq.add(Integer.parseInt(String.valueOf(userList.get(j).get("endDataSttnSeq"))));
						}
					}
					
					// 경로탐색배열
					List<Integer> sttnList = cblUtil.getWayPoinList(begDataSttnSeq, endDataSttnSeq);
					// 엔진 실행 후 결과 db저장
					cblUtil.exeRPEngineSetResult(sttnList, scheNo, i);
					
					// flag 분리
					List<HashMap<String, Object>> list = mapper.getSttnTime(scheNo, i);
					list.remove(list.size()-1);
					for(int k = 0; k < list.size(); k++) {
						int sttnSeq = Integer.parseInt(String.valueOf(list.get(k).get("STTN_SEQ")));
						int timeSumMmm = Integer.parseInt(String.valueOf(list.get(k).get("TIME_SUM_MMM"))); /* 도착시간 */
						
						if(Integer.parseInt(String.valueOf(userList.get(i).get("begDataSttnSeq"))) == sttnSeq) {
							if(i == 0) {
								if(Integer.parseInt(String.valueOf(userList.get(i).get("mmList"))) <= timeSumMmm) {
									/*
									 * userList.get(i).put("timeSumMmm", timeSumMmm); userList.get(i).put("orderTm",
									 * 10);
									 */
								} else { 
									userList.get(i).put("flag", 1); 
								} 
							} else {
								List<HashMap<String, Object>> list_be = mapper.getSttnTime(scheNo, i-1); /* 이전 결과 경로 */
								int timeSumMmm_be = Integer.parseInt(String.valueOf(list_be.get(list_be.size()-2).get("TIME_SUM_MMM"))); /* 이전 결과 경로에서 도착시간 */
								if( timeSumMmm-timeSumMmm_be < bnParam) {
									if(Integer.parseInt(String.valueOf(userList.get(i).get("mmList"))) <= timeSumMmm) {
										/*
										 * userList.get(i).put("timeSumMmm", timeSumMmm); userList.get(i).put("orderTm",
										 * 10);
										 */
									} else { 
										userList.get(i).put("flag", 1); 
									} 
								} else {
									userList.get(i).put("flag", 1); 
								}
							}
						}
					}
				}
		
		// |				
		// ⌊_____________________________________________________________________________________________
				
		// # 10시 , 11시 분리된 배열 최종 엔진
		// ⌈¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
				List<Integer> begDataSttnSeq_10 = new ArrayList<Integer>();
				List<Integer> endDataSttnSeq_10 = new ArrayList<Integer>();
				List<Integer> begDataSttnSeq_11 = new ArrayList<Integer>();
				List<Integer> endDataSttnSeq_11 = new ArrayList<Integer>();
				
				for(int i = 0; i < userList.size(); i++) {
					if(Integer.parseInt(String.valueOf(userList.get(i).get("flag"))) == 0){ /* 10시배차 */
						begDataSttnSeq_10.add(Integer.parseInt(String.valueOf(userList.get(i).get("begDataSttnSeq"))));
						endDataSttnSeq_10.add(Integer.parseInt(String.valueOf(userList.get(i).get("endDataSttnSeq"))));
					} else if(Integer.parseInt(String.valueOf(userList.get(i).get("flag"))) == 1){ /* 11시배차 */
						begDataSttnSeq_11.add(Integer.parseInt(String.valueOf(userList.get(i).get("begDataSttnSeq"))));
						endDataSttnSeq_11.add(Integer.parseInt(String.valueOf(userList.get(i).get("endDataSttnSeq"))));
					}
				}
				
				// 경로탐색배열
				List<Integer> sttnList_10 = cblUtil.getWayPoinList(begDataSttnSeq_10, endDataSttnSeq_10);
				List<Integer> sttnList_11 = cblUtil.getWayPoinList(begDataSttnSeq_11, endDataSttnSeq_11);
				
				List<Integer> forArr = new ArrayList<>();
				// 엔진 실행 후 결과 db저장
				if(sttnList_10.size() != 0) {
					cblUtil.exeRPEngineSetResult(sttnList_10, scheNo, 10);
					forArr.add(10);
				}
				if(sttnList_11.size() != 0) {
					cblUtil.exeRPEngineSetResult(sttnList_11, scheNo, 11);
					forArr.add(11);
				}
				
				// # 최종리스트 다듬고 저장
				// ⌈¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
						for(int i = 0; i < forArr.size(); i++){
							List<HashMap<String, Object>> list = mapper.getSttnTime(scheNo, forArr.get(i));
							for(int j = 0; j < list.size(); j++) {
								int sttnSeq = Integer.parseInt(String.valueOf(list.get(j).get("STTN_SEQ")));
								int timeSumMmm = Integer.parseInt(String.valueOf(list.get(j).get("TIME_SUM_MMM")));
								int distSumM = Integer.parseInt(String.valueOf(list.get(j).get("DIST_SUM_M")));
								for(int k = 0; k < userList.size(); k++) {
									userList.get(k).put("totalTm", Integer.parseInt(String.valueOf(list.get(j).get("TOTAL_TM"))));
									userList.get(k).put("totalDist", Integer.parseInt(String.valueOf(list.get(j).get("TOTAL_DIST"))));
									// 1. 10시, 11시 배차 구분
									if(Integer.parseInt(String.valueOf(userList.get(k).get("flag"))) == 0){
										userList.get(k).put("orderTm", 10);
									} else {
										userList.get(k).put("orderTm", 11);
									}
									// 2. 탑승 정류장 도착시간 매핑
									if(Integer.parseInt(String.valueOf(userList.get(k).get("begDataSttnSeq"))) == sttnSeq) {
										userList.get(k).put("timeSumMmm", timeSumMmm); 
										userList.get(k).put("distSumM", distSumM); 
									}
								}
							}
						}
						
						// 예약정보 저장
						int result = mapper.setUserList(scheNo, userList);
						// 소요시간 및 소요거리 업데이트
						if(result > 0) {
							for(int i = 0; i < forArr.size(); i++) {
								List<HashMap<String, Object>> list = mapper.getUserListForUpdate(scheNo, forArr.get(i));
								for(int j = 0; j < list.size(); j++) {
									mapper.updateUserList(scheNo, list.get(j), forArr.get(i), Integer.parseInt(String.valueOf(list.get(j).get("USER_NO"))));
								}
							}
							userList.clear();
							userList = mapper.getUserList(scheNo);
						}
				// |				
				// ⌊_____________________________________________________________________________________________
				
		// |				
		// ⌊_____________________________________________________________________________________________
				

				System.out.println("최종리스트 : " + userList.toString());
			
		
				orderMaster_link = mapper.getResultLink("master", 0);
				order10_link = mapper.getResultLink(scheNo, 10);
				order11_link = mapper.getResultLink(scheNo, 11);
				orderMaster_node = mapper.getSttnTime("master", 0);
				order10_node = mapper.getSttnTime(scheNo, 10);
				order11_node = mapper.getSttnTime(scheNo, 11);
		
				HashMap<String, Object> returnOrderList = new HashMap<String, Object>();
				returnOrderList.put("userList", userList);
				returnOrderList.put("orderMaster_link", orderMaster_link);
				returnOrderList.put("order10_link", order10_link);
				returnOrderList.put("order11_link", order11_link);
				returnOrderList.put("orderMaster_node", orderMaster_node);
				returnOrderList.put("order10_node", order10_node);
				returnOrderList.put("order11_node", order11_node);
				
				return returnOrderList;
	}

	@Override
	public List<HashMap<String, Object>> excelDownload(String scheNo) {
		return mapper.getUserListExcel(scheNo);
	}




}
