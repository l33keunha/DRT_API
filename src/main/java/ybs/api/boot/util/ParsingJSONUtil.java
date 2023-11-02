package ybs.api.boot.util;

import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

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

@Component
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

    /**
     * result 형식을 json으로 변환
     * @param int
     * @return JSON
     */
    public JSONObject resultParsingJSON(int result, int errorkey) {
        if(result > 0){
            code = "201";
            msg = "success INSERT";
        } else{
            switch (errorkey){
                case 1:
                    code = "401";
                    msg = "insert error. check Parameter.";
                    break;
                case 2:
                    code = "402";
                    msg = "update error. check Parameter.";
                    break;
                case 3:
                    code = "403";
                    msg = "select error. check Parameter.";
                    break;
            }
        }

        json.put("returnCode", code);
        json.put("returnMsg", msg);

        return json;
    }


    public JSONObject listFromdbParsingJSON(List<HashMap<String, Object>> listFromdb) {
        if(listFromdb.size() > 0){

            JSONObject document = new JSONObject();
            JSONObject placeMark = new JSONObject();
            JSONArray arr = new JSONArray();

            for(int i = 0; i < listFromdb.size(); i++){
                if(("출발지").equals(listFromdb.get(i).get("NAME")) || ("도착지").equals(listFromdb.get(i).get("NAME"))){
                    placeMark = new JSONObject();
                    placeMark.put("index", listFromdb.get(i).get("INDEX"));
                    placeMark.put("pointIndex", listFromdb.get(i).get("POINT_INDEX"));
                    placeMark.put("nodeid", listFromdb.get(i).get("NODE_ID"));
                    placeMark.put("nodename", listFromdb.get(i).get("NODE_NAME"));
                    placeMark.put("name", listFromdb.get(i).get("NAME"));
                    placeMark.put("nodeType", listFromdb.get(i).get("NODE_TYPE"));
                    placeMark.put("turnType", listFromdb.get(i).get("TURN_TYPE"));
                    placeMark.put("pointType", listFromdb.get(i).get("POINT_TYPE"));
                    JSONObject point = new JSONObject();
                    point.put("coordinates",listFromdb.get(i).get("COORDINATES"));
                    placeMark.put("Point", point);

                    arr.add(placeMark);
                } else if (("POINT").equals(listFromdb.get(i).get("NODE_TYPE"))){
                    placeMark = new JSONObject();
                    placeMark.put("index", listFromdb.get(i).get("INDEX"));
                    placeMark.put("pointIndex", listFromdb.get(i).get("POINT_INDEX"));
                    placeMark.put("nodeid", listFromdb.get(i).get("NODE_ID"));
                    placeMark.put("nodename", listFromdb.get(i).get("NODE_NAME"));
                    placeMark.put("name", listFromdb.get(i).get("NAME"));
                    placeMark.put("nodeType", listFromdb.get(i).get("NODE_TYPE"));
                    placeMark.put("pointType", listFromdb.get(i).get("POINT_TYPE"));
                    JSONObject point = new JSONObject();
                    point.put("coordinates",listFromdb.get(i).get("COORDINATES"));
                    placeMark.put("Point", point);

                    arr.add(placeMark);
                } else if (("LINE").equals(listFromdb.get(i).get("NODE_TYPE"))){
                    placeMark = new JSONObject();
                    placeMark.put("index", listFromdb.get(i).get("INDEX"));
                    placeMark.put("lineIndex", listFromdb.get(i).get("LINE_INDEX"));
                    placeMark.put("name", listFromdb.get(i).get("NAME"));
                    placeMark.put("nodeType", listFromdb.get(i).get("NODE_TYPE"));
                    placeMark.put("distance", listFromdb.get(i).get("DISTANCE"));
                    placeMark.put("time", listFromdb.get(i).get("TIME"));
                    placeMark.put("roadType", listFromdb.get(i).get("ROAD_TYPE"));
                    placeMark.put("facilityType", listFromdb.get(i).get("FACILITY_TYPE"));
                    JSONObject lineString = new JSONObject();
                    lineString.put("coordinates",listFromdb.get(i).get("COORDINATES"));
                    placeMark.put("LineString", lineString);
                    placeMark.put("linkid", listFromdb.get(i).get("LINK_ID"));
                    placeMark.put("costdstn", listFromdb.get(i).get("COSTDSTN"));

                    arr.add(placeMark);
                }
            }
                document.put("PlaceMark", arr);

                document.put("scheNo", listFromdb.get(0).get("SCHE_NO"));
                document.put("scheSubNo", listFromdb.get(0).get("SCHE_SUB_NO"));
                document.put("totalDistance", listFromdb.get(0).get("TOTAL_DIST"));
                document.put("totalTime", listFromdb.get(0).get("TOTAL_TM"));

                json.put("document", document);

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

}
