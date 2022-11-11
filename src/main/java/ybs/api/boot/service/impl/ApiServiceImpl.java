package ybs.api.boot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ybs.api.boot.service.ApiService;
import ybs.api.boot.service.xmlVO;

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
    public List<HashMap<String,Object>> getSttn(HashMap<String, Object> map) {
        return mapper.getSttn(map);
    }

    /**
     * 2. 탑승 조건 기준 배차 정보 조회
     * @param map
     * @return map
     */
    @Override
    public HashMap<String, Object> getSche(HashMap<String, Object> map) {
        return mapper.getSche(map);
    }

    /**
     * 3. 운전자가 운행할 차량의 예약자 조회
     * @param map
     * @return List<Map>
     */
    @Override
    public List<HashMap<String, Object>> getUserMast(HashMap<String, Object> map) {
        return mapper.getUserMast(map);
    }

    /**
     * 4. 예약 정보 저장
     * @param map
     * @return int
     */
    @Override
    public int setUserMast(HashMap<String, Object> map) {
        return mapper.setUserMast(map);
    }

    /**
     * 5. 노선정보 조회
     * @return List<Map>
     */
    @Override
    public List<HashMap<String, Object>> getRoute() {
        return mapper.getRoute();
    }

    /**
     * 6. 이름 / 연락처로 예약 조회
     * @param map
     * @return List<Map>
     */
    @Override
    public List<HashMap<String, Object>> getUser(HashMap<String, Object> map) {
        return mapper.getUser(map);
    }


    /**
     * 7. 운행 종료 후 운행 정보 저장
     * @param map
     * @return int
     * @throws Exception
     */
    @Override
    public int updateHist(HashMap<String, Object> map) {
        return mapper.updateHist(map);
    }

    /**
     * 8. 경로 탐색을 위한 출/도착지, 경유지의 xy 정보 제공
     * @param map
     * @return JSON
     */
    @Override
    public List<HashMap<String, Object>> getForPath(HashMap<String, String> map) { return mapper.getForPath(map); }

    /**
     * 9. 경로 탐색 결과 값 저장 (xml -> db)
     * @param ArrayList<xmlVO>
     * @return JSON
     */
    @Override
    public int setPath(String scheNo, ArrayList<xmlVO> list) { return mapper.setPath(scheNo, list); }
	@Override
	public String getScheNo(HashMap<String, String> map) {	return mapper.getScheNo(map); }
	@Override
	public int setHist(HashMap<String, String> map) { return mapper.setHist(map); }
	@Override
	public int getScheSubNo(String scheNo) { return mapper.getScheSubNo(scheNo); }

    /**
     * 10. 배차 차량에 경로 제공
     * @param map
     * @return JSON
     */
    @Override
    public List<HashMap<String, Object>> getForDriver(HashMap<String, String> map) { return mapper.getForDriver(map); }




    

    


}
