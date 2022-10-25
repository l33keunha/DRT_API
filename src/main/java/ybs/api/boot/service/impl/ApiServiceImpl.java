package ybs.api.boot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ybs.api.boot.service.ApiService;

import java.util.HashMap;

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
     * 이용자가 탑승 가능한 배차 정보 조회
     * @param map
     * @return map
     */
    @Override
    public HashMap<String, String> getSche(HashMap<String, String> map) {

        return mapper.getSche(map);

    }
}
