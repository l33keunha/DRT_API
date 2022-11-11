package ybs.api.boot.controller;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import ybs.api.boot.service.ApiService;
import ybs.api.boot.service.xmlVO;
import ybs.api.boot.util.ParsingJSONUtil;
import ybs.api.boot.util.ParsingHashMapUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import static org.apache.naming.SelectorContext.prefix;


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
        return util.resultParsingJSON(service.setUserMast(map));

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
        return util.resultParsingJSON(service.updateHist(map));

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
		        다른 util인 이유 : json.simple 과 json 라이브러리 호환 문제
		        json.simple => JSONObject, JSONArray 때 필요
		        json => xml 파싱 때 필요
    	 */
    	ParsingHashMapUtil mUtil = new ParsingHashMapUtil();
    	ArrayList<xmlVO> list = mUtil.xmlParsingJson(map.get("xmlDoc"));
    	
    	// 1. SCHE_NO 존재유무 확인 : 있으면 SUB_NO UPDATE, 없다면 NEW 운행 정보 
    	String scheNo = null;
    	int scheSubNo = 0;
    	
    	if(service.getScheNo(map) == null) {
    		System.out.println("새로운 운행정보기에 NEW 운행정보");
    		int result = service.setHist(map);
    		if(result > 0) {
    			scheNo = service.getScheNo(map);
    			service.setPath(scheNo, list);
    		}
    	} else {
    		System.out.println("원래 있던 운행정보기에 덮어쓰기");
    		scheSubNo = service.getScheSubNo(scheNo);
    	}
    	
    	System.out.println(scheNo);
    	System.out.println(scheSubNo);

//        ParsingJSONUtil util = new ParsingJSONUtil();
//        util.resultParsingJSON(service.setPath(list));
        return null;

    }

    /**
     * 10. 배차 차량에 경로 제공
     * @param map
     * @return JSON
     * @throws Exception
     * 추가 사항 : 2022.11.10
     */
    @ResponseBody
    @RequestMapping("/getForDriver")
    public JSONObject getForDriver(@RequestParam HashMap<String, String> map) throws Exception {

        ParsingJSONUtil util = new ParsingJSONUtil();
        System.out.println(util.listParsingJSON(service.getForDriver(map)));
        return null;

    }

}



