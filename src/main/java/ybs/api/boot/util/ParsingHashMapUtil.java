package ybs.api.boot.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.stereotype.Component;

import ybs.api.boot.org.service.xmlVO;

@Component
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
                
                vo.setPoint("Y");
                vo.setPointIndex((int) obj.get("pointIndex"));
                vo.setTurnType(String.valueOf(obj.get("turnType")));
                vo.setNodeId(String.valueOf(obj.get("nodeid")));
                vo.setNodeName(String.valueOf(obj.get("nodename")));
                
            } else if(("POINT").equals(obj.get("nodeType"))){
                JSONObject coord = (JSONObject) obj.get("Point");
                vo.setCoordinates(String.valueOf(coord.get("coordinates")));

                vo.setPoint("Y");
                vo.setPointIndex((int) obj.get("pointIndex"));
                vo.setNodeId(String.valueOf(obj.get("nodeid")));
                vo.setNodeName(String.valueOf(obj.get("nodename")));
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
        
		System.out.println("/*---------------JSON형태_2022ver");
		System.out.println(list.toString()); // JSON 데이터 출력

        return  list;

    }
    
    public ArrayList<xmlVO> xmlParsingJson_rev(String xmlDoc, List<Integer> sttnList) {
    	
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
    	int j = 0;
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
    			
    			vo.setPoint("Y");
    			vo.setPointIndex((int) obj.get("pointIndex"));
    			vo.setTurnType(String.valueOf(obj.get("turnType")));
    			vo.setNodeId(String.valueOf(obj.get("nodeid")));
    			vo.setNodeName(String.valueOf(obj.get("nodename")));
    			
    			if(("도착지").equals(obj.get("name"))){
    				if(j < sttnList.size()) {
    					vo.setSttnSeq(sttnList.get(j));
    					j++;
    				} else {
    					vo.setSttnSeq(999);
    				}
    			}
    			
    		} else if(("POINT").equals(obj.get("nodeType"))){
    			JSONObject coord = (JSONObject) obj.get("Point");
    			vo.setCoordinates(String.valueOf(coord.get("coordinates")));
    			
    			vo.setPoint("Y");
    			vo.setPointIndex((int) obj.get("pointIndex"));
    			vo.setNodeId(String.valueOf(obj.get("nodeid")));
    			vo.setNodeName(String.valueOf(obj.get("nodename")));
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
    	
    	System.out.println("/*---------------JSON형태_2022ver");
    	System.out.println(list.toString()); // JSON 데이터 출력
    	
    	return  list;
    	
    }
    
}

