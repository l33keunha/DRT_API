package ybs.api.boot.org.service.impl;

import org.apache.ibatis.annotations.Mapper;
import ybs.api.boot.org.service.xmlVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @Class Name : ApiMapper.java
 * @Description : DRT API ApiMapper
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

@Mapper
public interface ApiMapper {

    /**
     * 1. 운행지역 기준 정류장 정보 조회
     * @param map
     * @return List<Map>
     */
    List<HashMap<String, Object>> getSttn(HashMap<String, Object> map);

    /**
     * 2. 탑승 조건 기준 배차 정보 조회 : 기점 정류장 - 예약자 승차 정류장 소요시간
     * @param List<Map>
     * @return map
     */
    List<HashMap<String, Object>> getSt2In(HashMap<String, Object> map);
    /**
     * 2. 탑승 조건 기준 배차 정보 조회
     * @param List<Map>
     * @return map
     */
    HashMap<String, Object> getSche(HashMap<String, Object> map);
    int findUser(HashMap<String, Object> map);
    HashMap<String, Object> getSt_XY(HashMap<String, Object> map);
    HashMap<String, Object> getFN_XY(HashMap<String, Object> map);
    String findSttn(HashMap<String, Object> map);
    List<HashMap<String, Object>> getWay2In(HashMap<String, Object> map);
    List<HashMap<String, Object>> getUserXY_2(HashMap<String, Object> goMap);
    int findSttn_BONUS(HashMap<String, Object> goMap);
    
    /**
     * 3. 운전자가 운행할 차량의 예약자 조회
     * @param map
     * @return List<Map>
     */
    List<HashMap<String, Object>> getUserMast(HashMap<String, Object> map);

    /**
     * 4. 예약 정보 저장 : 예약자 승하차 정류장 xy 조회
     * @param map
     * @return int
     */
    List<HashMap<String, Object>> getUserXY(HashMap<String, Object> map);
    /**
     * 4. 예약 정보 저장 
     * @param map
     * @return int
     */
    int setUser(HashMap<String, Object> map);

    /**
     * 5. 노선정보 조회
     * @return List<Map>
     */
    List<HashMap<String, Object>> getRoute();

    /**
     * 6. 이름 / 연락처로 예약 조회
     * @param map
     * @return List<Map>
     */
    List<HashMap<String, Object>> getUser(HashMap<String, Object> map);

    /**
     * 7. 운행 종료 후 운행 정보 저장
     * @param map
     * @return int
     */
    int updateHist(HashMap<String, Object> map);

    /**
     * 8. 경로 탐색을 위한 출/도착지, 경유지의 xy 정보 제공
     * @param map
     * @return List<Map>
     */
    List<HashMap<String, Object>> getWayPoint(HashMap<String, Object> map);
    List<HashMap<String, Object>> getWayPoint_old(HashMap<String, Object> map);

    /**
     * 9. 운행 시작 및 경로 탐색 : 출,도착지 xy 조회
     * @param map
     * @return List<Map>
     */
    List<HashMap<String, Object>> getRouteXY(HashMap<String, Object> map);
    /**
     * 9. 운행 시작 및 경로 탐색 : 운행정보 요약정보 저장 
     * @param map
     * @return int
     */
    int setHist(HashMap<String, Object> map);
    /**
     * 9. 운행 시작 및 경로 탐색 : 경로 저장
     * @param map
     * @return int
     */
    int setPath(HashMap<String, Object> map);
    /**
     * 9. 운행 시작 및 경로 탐색 : 운행 번호 조회
     * @param map
     * @return String
     */
    String getScheNo(HashMap<String, Object> map);
    /**
     * 9. 운행 시작 및 경로 탐색 : 운행 번호 (실시간예약시) 조회
     * @param map
     * @return int
     */
    int getScheSubNo(String scheNo);

    /**
     * 10. 배차 차량에 경로 제공
     * @param map
     * @return List<Map>
     */
    List<HashMap<String, Object>> getPath(HashMap<String, Object> map);

    /**
     * 11. 운행 요약 정보 조회
     * @param map
     * @return List<Map>
     */
    List<HashMap<String, Object>> getHist(HashMap<String, Object> map);


	

	

	

	

	





}
