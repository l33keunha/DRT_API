package ybs.api.boot.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ybs.api.boot.service.ApiService;
import ybs.api.boot.service.xmlVO;
import ybs.api.boot.util.ParsingHashMapUtil;
import ybs.api.boot.util.ParsingJSONUtil;


/**
 *
 * @Class Name : ApiController.java
 * @Description : DRT API Controller
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

@Controller
public class ApiController {

    @Autowired
    private ApiService service;

    /**
     * index page 이동
     */
    @RequestMapping("/main.do")
    public String goMain(HttpServletRequest req, HttpServletResponse res){ return "index"; }

    /**
     * 1. 운행지역 기준 정류장 정보 조회
     * @param map
     * @return JSON
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/getSttn")
    public JSONObject getSttn(@RequestParam HashMap<String, Object> map) throws Exception {

        ParsingJSONUtil util = new ParsingJSONUtil();
        return util.listParsingJSON(service.getSttn(map));

    }

    /**
     * 2. 탑승 조건 기준 배차 정보 조회
     * @param map
     * @return JSON
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/getSche")
    public JSONObject getSche(@RequestParam HashMap<String, Object> map) throws Exception {

        ParsingJSONUtil util = new ParsingJSONUtil();
        return util.mapParsingJSON(service.getSche(map));

    }

    /**
     * 3. 운전자가 운행할 차량의 예약자 조회
     * @param map
     * @return JSON
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/getUserMast")
    public JSONObject getUserMast(@RequestParam HashMap<String, Object> map) throws Exception{

        ParsingJSONUtil util = new ParsingJSONUtil();
        return util.listParsingJSON(service.getUserMast(map));

    }

    /**
     * 4. 예약 정보 저장
     * @param map
     * @return JSON
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/setUserMast")
    public JSONObject setUserMast(@RequestParam HashMap<String, Object> map) throws Exception{

        ParsingJSONUtil util = new ParsingJSONUtil();
        return util.resultParsingJSON(service.setUserMast(map), 1);

    }

    /**
     * 5. 노선정보 조회
     * @return JSON
     * @throws Exception
     * 요청 사항 : 2022.11.09
     */
    @ResponseBody
    @RequestMapping("/getRoute")
    public JSONObject getRoute() {
    	
    	ParsingJSONUtil util = new ParsingJSONUtil();
        return util.listParsingJSON(service.getRoute());
        	
    }
    
    /**
     * 6. 이름 / 연락처로 예약 조회
     * @param map
     * @return JSON
     * @throws Exception
     * 요청 사항 : 2022.11.09
     */
    @ResponseBody
    @RequestMapping("/getUser")
    public JSONObject getUser(@RequestParam HashMap<String, Object> map) throws Exception{

        ParsingJSONUtil util = new ParsingJSONUtil();
        return util.listParsingJSON(service.getUser(map));

    }

    /**
     * 7. 운행 종료 후 운행 정보 저장
     * @param map
     * @return JSON
     * @throws Exception
     * 요청 사항 : 2022.11.09
     */
    @ResponseBody
    @RequestMapping("/updateHist")
    public JSONObject updateHist(@RequestParam HashMap<String, Object> map) throws Exception{

        ParsingJSONUtil util = new ParsingJSONUtil();
        return util.resultParsingJSON(service.updateHist(map), 2);

    }

    /**
     * 8. 경로 탐색을 위한 경유지만 x,y 정보 제공
     * @param map
     * @return JSON
     * @throws Exception
     * 추가 사항 : 2022.11.10
     */
    @ResponseBody
    @RequestMapping("/getForPath")
    public JSONObject getForPath(@RequestParam HashMap<String, String> map) throws Exception {

        ParsingJSONUtil util = new ParsingJSONUtil();
        return util.listParsingJSON(service.getForPath(map));
    }

    /**
     * 9. 경로 탐색 결과 값 저장 (xml -> db) 하고 바로 10번 메소드 실행
     * @param map(String(xml))
     * @return JSON
     * @throws Exception
     * 추가 사항 : 2022.11.10
     */
    @ResponseBody
    @RequestMapping("/setPath")
    public JSONObject setPath(@RequestParam HashMap<String, String> map) throws Exception {
    	
    	/* 
    	 		※ xml -> list 파싱
		        다른 util인 이유 : json.simple 과 json 라이브러리 호환 문제
		        json.simple => JSONObject, JSONArray 때 필요
		        json => xml 파싱 때 필요
    	 */
    	ParsingHashMapUtil mUtil = new ParsingHashMapUtil();
    	ArrayList<xmlVO> list = mUtil.xmlParsingJson(map.get("xmlDoc"));

        /*
         		 ※ DB에 보낼 map 생성
         		 map          : request Parameter from web
         		 map2db     : xml 데이터 list로 파싱한 parameter to db
         */
        HashMap<String, Object> map2db = new HashMap<>();
    	
        /*
         	 	※ if문 1. 필드
         */
    	String scheNo = null;
    	int scheSubNo = 1;
        int result = 0;
        ParsingJSONUtil util = new ParsingJSONUtil();

        // SCHE_NO 존재유무 확인 : 있으면 SUB_NO UPDATE, 없다면 NEW 운행 정보 
    	if(service.getScheNo(map) == null) {
    		int check = service.setHist(map);				
    		if(check > 0) {
                map2db.put("SCHE_NO", service.getScheNo(map));
                map2db.put("SCHE_SUB_NO", "1");
                map2db.put("list", list);

                result = service.setPath(map2db);
    		} else {
    			return util.resultParsingJSON(result, 1);
            }
    	} else {
            scheNo = service.getScheNo(map);
    		scheSubNo = service.getScheSubNo(scheNo);
            scheSubNo++;

            map2db.put("SCHE_NO", scheNo);
            map2db.put("SCHE_SUB_NO", scheSubNo);
            map2db.put("list", list);

            result = service.setPath(map2db);
    	}
        /*
				 ※ DB에 보낼 map 생성
				 list         		 : xml -> list 파싱
				 listFromdb     : db로부터 받아온 경로 List<Map<String, Object>>
         */
    	List<HashMap<String, Object>> listFromdb = new ArrayList<HashMap<String, Object>>();
    	
    	// 경로 저장 후 저장한 경로 다시 web으로 반환
        if(result > 0){
        	listFromdb = service.getForDriver(map2db);
            return util.listFromdbParsingJSON(listFromdb);
        }
        	return util.resultParsingJSON(result, 3);
    }

    /**
     * 10. 배차 차량에 경로 제공
     * @param map
     * @return listFromdb
     * @throws Exception
     * 추가 사항 : 2022.11.10
     */
    @ResponseBody
    @RequestMapping("/getForDriver")
    public JSONObject getForDriver(@RequestParam HashMap<String, Object> map) throws Exception {

        ParsingJSONUtil util = new ParsingJSONUtil();
        return util.listFromdbParsingJSON(service.getForDriver(map));

    }

    

}



