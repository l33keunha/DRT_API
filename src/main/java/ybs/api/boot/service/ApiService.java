package ybs.api.boot.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Document;

public interface ApiService {

    /**
     * 이용자가 승/하차할 정류장 선택
     * @param map
     * @return List<Map>
     * @throws Exception
     */
    List<HashMap<String, Object>> getSttn(HashMap<String, Object> map);

    /**
     * 이용자가 탑승 가능한 배차 정보 조회
     * @param map
     * @return map
     */
    HashMap<String, Object> getSche(HashMap<String, Object> map);

    /**
     * 예약자 등록
     * @param map
     * @return int
     */
    int setUserMast(HashMap<String, Object> map);

    /**
     * 운전자가 운행할 차량의 예약자 조회
     * @param map
     * @return List<Map>
     */
    List<HashMap<String, Object>> getUserMast(HashMap<String, Object> map);

    /**
     * 운전자가 운행 종료 후 저장
     * @param map
     * @return int
     * @throws Exception
     */
    int setHist(HashMap<String, Object> map);

    int setPath(ArrayList<xmlVO> list);

    /**
     * 노선정보 조회
     * @return List<Map>
     */
	List<HashMap<String, Object>> getRoute();
	
	/**
     * 예약검색시 이름과 전화번호로 예약조회
     * @param map
     * @return List<Map>
     */
    List<HashMap<String, Object>> getUser(HashMap<String, Object> map);
	
}
