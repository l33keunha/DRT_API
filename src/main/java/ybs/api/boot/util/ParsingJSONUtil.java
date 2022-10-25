package ybs.api.boot.util;

import org.json.simple.JSONObject;

import java.util.HashMap;

/**
 *
 * @Class Name : parsingJSONUtil.java
 * @Description : map 형식을 json으로 변환
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

public class ParsingJSONUtil {

    /**
     * map 형식을 json으로 변환
     * @param map
     * @return JSON
     */
    public JSONObject parsingJSON(HashMap<String, String> map){
        String msg = null;

        if(map.size() > 0){
            msg = "success";
        } else {
            msg = "error";
        }

        JSONObject data = new JSONObject(map);

        JSONObject json = new JSONObject();
        json.put("returnMsg", msg);
        json.put("data", data);

        return json;
    }
}
