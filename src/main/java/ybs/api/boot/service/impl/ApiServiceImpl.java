package ybs.api.boot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ybs.api.boot.service.ApiService;
import ybs.api.boot.service.xmlVO;
import ybs.api.boot.util.ConnectionURLUtil;
import ybs.api.boot.util.ParsingHashMapUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
     */
    @Override
    public HashMap<String, Object> getSche(HashMap<String, Object> map) { return mapper.getSche(map); }

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
    	
    	// 정류장ID에 매칭되는 XY좌표를 가져온다.
    	List<HashMap<String, Object>> list = mapper.getUserXY(map);
    	
    	// RP엔진에서 소요거리와 소요시간을 가져온다.
    	ConnectionURLUtil util = new ConnectionURLUtil();
    	ArrayList<xmlVO> vList = util.connectionURL(util.makeParam(list));
    	
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
     * @throws Exception
     */
    @Override
    public int updateHist(HashMap<String, Object> map) { return mapper.updateHist(map); }

    /**
     * 8. 경로 탐색을 위한 출/도착지, 경유지의 xy 정보 제공
     * @param map
     * @return JSON
     */
    @Override
    public List<HashMap<String, Object>> getWayPoint(HashMap<String, Object> map) { return mapper.getWayPoint(map); }

    /**
     * 9. 경로 탐색 결과 값 저장 (xml -> db)
     * @param ArrayList<xmlVO>
     * @return JSON
     * @throws Exception 
     */
    @Override
    public List<HashMap<String, Object>> setPath(HashMap<String, Object> map) throws Exception {
    	String scheNo = null;
    	int scheSubNo = 1;
        int result = 0;
        
        // 1. 출, 도착지 XY 가져온다.
        List<HashMap<String, Object>> rList = mapper.getRouteXY(map);
        // 1. 경유지 XY 가져온다.
        map.put("ST_TM", String.valueOf(map.get("ST_TM")).substring(0, 1));
        List<HashMap<String, Object>> wList = mapper.getWayPoint(map);
        
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        list.add(rList.get(0));
        list.add(rList.get(1));
        for(HashMap<String, Object> obj : wList) {
        	list.add(obj);
        }
        
        ConnectionURLUtil cUtil = new ConnectionURLUtil();
        ArrayList<xmlVO> vList = cUtil.connectionURL(cUtil.makeParam(list));
        
        for(xmlVO vo : vList) {
        	System.out.println(vo.toString());
        }
        
    	
        // 2. SCHE_NO 존재유무 확인 : 있으면 SUB_NO UPDATE, 없다면 NEW 운행 정보 
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
    	
    	// 5. SELECT PATH
    	
    	return null; 
    }


    /**
     * 10. 배차 차량에 경로 제공
     * @param map
     * @return JSON
     */
    @Override
    public List<HashMap<String, Object>> getPath(HashMap<String, Object> map) { return mapper.getPath(map); }

    /**
     * 11. 운행 요약 정보 조회
     * @param map
     * @return JSON
     * @throws Exception
     * 추가 사항 : 2022.11.14
     */
    @Override
    public List<HashMap<String, Object>> getHist(HashMap<String, Object> map) { return mapper.getHist(map); }


}
