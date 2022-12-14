package ybs.api.boot.util;

import org.json.JSONArray;
import org.json.XML;
import org.json.JSONObject;
import ybs.api.boot.service.xmlVO;

import java.util.ArrayList;
import java.util.HashMap;

public class ParsingHashMapUtil {

    /**
     * xml 형식을 json으로 변환
     * @param file
     * @return JSON
     */
    public ArrayList<xmlVO> xmlParsingJson(String xmlDoc) {
        
        // 파일 읽어오기
        org.json.JSONObject json = XML.toJSONObject(xmlDoc);
        // first root 열기
        JSONObject content = (JSONObject) json.get("kml");
        // 실제 데이터들이 담겨있는 Document 열기
        JSONObject document = (JSONObject) content.get("Document");
        // 구간별 array 생성
        JSONArray array = (JSONArray) document.get("Placemark");

        ArrayList<xmlVO> list = new ArrayList<xmlVO>();
        xmlVO vo = null;

        for(int i = 0; i < array.length(); i++){
        	
            JSONObject obj = (JSONObject) array.get(i);
            vo = new xmlVO();

            vo.setTotalDist((Integer) document.get("totalDistance"));
            vo.setTotalTm((Integer) document.get("totalTime"));

            vo.setIndex((int) obj.get("index"));
            vo.setName(String.valueOf(obj.get("name")));
            vo.setNodeType(String.valueOf(obj.get("nodeType")));

            if(("출발지").equals(obj.get("name")) || ("도착지").equals(obj.get("name"))){
            	JSONObject coord = (JSONObject) obj.get("Point");
                vo.setCoordinates(String.valueOf(coord.get("coordinates")));
                
                vo.setTurnType(String.valueOf(obj.get("turnType")));
                
            } else if(("POINT").equals(obj.get("nodeType"))){
                JSONObject coord = (JSONObject) obj.get("Point");
                vo.setCoordinates(String.valueOf(coord.get("coordinates")));

                vo.setPoint("Y");
                vo.setPointIndex((int) obj.get("pointIndex"));
                vo.setPointType(String.valueOf(obj.get("pointType")));
                
            } else if(("LINE").equals(obj.get("nodeType"))){
                JSONObject coord = (JSONObject) obj.get("LineString");
                vo.setCoordinates(String.valueOf(coord.get("coordinates")));

                vo.setLineString("Y");
                vo.setLineIndex((int) obj.get("lineIndex"));
                vo.setDistance((int) obj.get("distance"));
                vo.setTime((int) obj.get("time"));
                vo.setRoadType(String.valueOf(obj.get("roadType")));
                vo.setFacilityType(String.valueOf(obj.get("facilityType")));
                vo.setLinkId(String.valueOf(obj.get("linkid")));
                vo.setCostdstn((int)obj.get("costdstn"));
                
            }

            list.add(vo);
        }

        return  list;

    }
}

