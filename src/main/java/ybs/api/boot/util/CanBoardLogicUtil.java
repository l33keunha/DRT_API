package ybs.api.boot.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ybs.api.boot.org.service.xmlVO;
import ybs.api.boot.rev.service.impl.RevApiMapper;

@Component
public class CanBoardLogicUtil {
	public static final String ROUTE_ID = "2";
	@Autowired
	private RevApiMapper mapper;
	@Autowired(required=true)
	private ConnectionURLUtil conUtil;
	
	public List<Integer> getWayPoinList (List<Integer> begDataSttnSeq, List<Integer> endDataSttnSeq){
		List<Integer> sttnList = new ArrayList<>();
		
		for (int i = 0; i < begDataSttnSeq.size(); i++) {
			sttnList.add(begDataSttnSeq.get(i));
			sttnList.add(endDataSttnSeq.get(i));
		}
		sttnList = (List) sttnList.stream().distinct().collect(Collectors.toList());
		Collections.sort(sttnList);		
		
		System.out.println(("********경로탐색배열***********"));
		System.out.println(sttnList);
		
		return sttnList;
	}
	
	public void exeRPEngineSetResult(List<Integer> sttnList, String scheNo, int i) throws Exception {
	
		List<Double> sttnCoordArr = mapper.getSttnCoordArr(ROUTE_ID, sttnList);
		System.out.println(("********경로탐색좌표배열***********"));
		System.out.println(sttnCoordArr.toString());
		
		// ⌈¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
		// |
		// |	엔진 파라미터 만들기
				String param = conUtil.makeParam_rev(sttnCoordArr);
		// |			
		// |	엔진 연결 + xml -> List<VO>으로 변환
				ArrayList<xmlVO> rp_result = conUtil.connectionURL_rev(param, sttnList);
		// |			
		// |	db 경로탐색 결과 저장				
				Map<String, Object> rp_result_map = new HashMap<String, Object>();
				rp_result_map.put("SCHE_NO", scheNo);
				rp_result_map.put("SCHE_SUB_NO", i);
				rp_result_map.put("rp_result", rp_result);
				mapper.setPath(rp_result_map);
		// |				
		// ⌊_____________________________________________________________________________________________
	}
}
