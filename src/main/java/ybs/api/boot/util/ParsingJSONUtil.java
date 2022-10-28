package ybs.api.boot.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @Class Name : parsingJSONUtil.java
 * @Description : 결과 값을 json으로 변환
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

        JSONObject json = new JSONObject();
        String code = null;
        String msg = null;

    /**
     * map 형식을 json으로 변환
     * @param map
     * @return JSON
     */
    public JSONObject mapParsingJSON(HashMap<String, Object> map){
        if(map.size() > 0){
            JSONObject data = new JSONObject(map);
            json.put("data", data);
            code = "200";
            msg = "success";
        } else {
            code = "400";
            msg = "error. no file.";
        }

        json.put("returnCode", code);
        json.put("returnMsg", msg);

        return json;
    }

    /**
     * list 형식을 json으로 변환
     * @param list
     * @return JSON
     */
    public JSONObject listParsingJSON(List<HashMap<String, Object>> list){
        if(list.size() > 0){
        	JSONArray data = new JSONArray();
        	for(HashMap<String, Object> map : list) {
        		data.add(map);
        	}
            json.put("data", data);
            code = "200";
            msg = "success";
        } else {
            code = "400";
            msg = "error. no file.";
        }

        json.put("returnCode", code);
        json.put("returnMsg", msg);

        return json;
    }

    public JSONObject resultParsingJSON(int result) {
        if(result > 0){
            code = "201";
            msg = "success INSERT";
        } else{
            code = "401";
            msg = "error. can't INSERT.";
        }

        json.put("returnCode", code);
        json.put("returnMsg", msg);

        return json;
    }
}
