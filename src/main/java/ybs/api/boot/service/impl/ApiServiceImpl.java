package ybs.api.boot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ybs.api.boot.service.ApiService;

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
     * 이용자가 승/하차할 정류장 선택
     * @param map
     * @return list<map>
     */
    @Override
    public List<HashMap<String,Object>> getSttn(HashMap<String, Object> map) {
        return mapper.getSttn(map);
    }

    /**
     * 이용자가 탑승 가능한 배차 정보 조회
     * @param map
     * @return map
     */
    @Override
    public HashMap<String, Object> getSche(HashMap<String, Object> map) {
        return mapper.getSche(map);
    }

    /**
     * 예약자 등록
     * @param map
     * @return int
     */
    @Override
    public int setUserMast(HashMap<String, Object> map) {
         return mapper.setUserMast(map);
    }

    /**
     * 운전자가 운행할 차량의 예약자 조회
     * @param map
     * @return List<Map>
     */
    @Override
    public List<HashMap<String, Object>> getUserMast(HashMap<String, Object> map) {
        return mapper.getUserMast(map);
    }

    /**
     * 운전자가 운행 종료 후 저장
     * @param map
     * @return int
     * @throws Exception
     */
    @Override
    public int setHist(HashMap<String, Object> map) {
        return mapper.setHist(map);
    }

}
