package ybs.api.boot.service.impl;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

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
     * 이용자가 탑승 가능한 배차 정보 조회
     * @param map
     * @return map
     */
    HashMap<String, String> getSche(HashMap<String, String> map);

}
