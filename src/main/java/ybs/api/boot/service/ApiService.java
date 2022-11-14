package ybs.api.boot.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface ApiService {

    /**
     * 1. 운행지역 기준 정류장 정보 조회
     * @param map
     * @return List<Map>
     */
    List<HashMap<String, Object>> getSttn(HashMap<String, Object> map);

    /**
     * 2. 탑승 조건 기준 배차 정보 조회
     * @param map
     * @return map
     */
    HashMap<String, Object> getSche(HashMap<String, Object> map);

    /**
     * 3. 운전자가 운행할 차량의 예약자 조회
     * @param map
     * @return List<Map>
     */
    List<HashMap<String, Object>> getUserMast(HashMap<String, Object> map);

    /**
     * 4. 예약 정보 저장
     * @param map
     * @return int
     */
    int setUserMast(HashMap<String, Object> map);

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
     * @return JSON
     */
    List<HashMap<String, Object>> getWayPoint(HashMap<String, String> map);

    /**
     * 9. 경로 탐색 결과 값 저장 (xml -> db)
     * @param scheNo 
     * @param ArrayList<xmlVO>
     * @return JSON
     */
    int setPath(HashMap<String, Object> map2db);
    String getScheNo(HashMap<String, String> map);
    int setHist(HashMap<String, String> map);
    int getScheSubNo(String scheNo);

    /**
     * 10. 배차 차량에 경로 제공
     * @param map
     * @return JSON
     */
    List<HashMap<String, Object>> getPath(HashMap<String, Object> map);


	
}
