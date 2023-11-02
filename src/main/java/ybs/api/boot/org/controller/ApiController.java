package ybs.api.boot.org.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ybs.api.boot.org.service.ApiService;
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
     * index page 이동/main.do
     */
    @RequestMapping("/index_org.do")
    public String goMain(HttpServletRequest req, HttpServletResponse res){ return "index_org"; }

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
        return util.listParsingJSON(service.getSche(map));

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
    @RequestMapping("/setUser")
    public JSONObject setUser(@RequestParam HashMap<String, Object> map) throws Exception{

    	ParsingJSONUtil util = new ParsingJSONUtil();
    	return util.resultParsingJSON(service.setUser(map), 1);

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
    @RequestMapping("/getWayPoint")
    public JSONObject getWayPoint(@RequestParam HashMap<String, Object> map) throws Exception {

        ParsingJSONUtil util = new ParsingJSONUtil();
        return util.listParsingJSON(service.getWayPoint(map));
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
    public JSONObject setPath(@RequestParam HashMap<String, Object> map) throws Exception {
    	
    	ParsingJSONUtil util = new ParsingJSONUtil();
    	return util.listFromdbParsingJSON(service.setPath(map));
    	
    }

    /**
     * 10. 배차 차량에 경로 제공
     * @param map
     * @return JSON
     * @throws Exception
     * 추가 사항 : 2022.11.10
     */
    @ResponseBody
    @RequestMapping("/getPath")
    public JSONObject getPath(@RequestParam HashMap<String, Object> map) throws Exception {

        ParsingJSONUtil util = new ParsingJSONUtil();
        return util.listFromdbParsingJSON(service.getPath(map));

    }

    /**
     * 11. 운행 요약 정보 조회
     * @param map
     * @return JSON
     * @throws Exception
     * 추가 사항 : 2022.11.14
     */
    @ResponseBody
    @RequestMapping("/getHist")
    public JSONObject getHist(@RequestParam HashMap<String, Object> map) throws Exception {

        ParsingJSONUtil util = new ParsingJSONUtil();
        return util.listParsingJSON(service.getHist(map));

    }

}



