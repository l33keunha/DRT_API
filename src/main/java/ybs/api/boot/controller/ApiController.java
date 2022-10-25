package ybs.api.boot.controller;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ybs.api.boot.service.ApiService;
import ybs.api.boot.util.ParsingJSONUtil;

import java.util.HashMap;

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
     * 이용자가 탑승 가능한 배차 정보 조회
     * @param map
     * @return JSON
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/getSche")
    public JSONObject getSche(@RequestBody HashMap<String, String> map) throws Exception{
    	
    	ParsingJSONUtil util = new ParsingJSONUtil();
        return util.parsingJSON(service.getSche(map));

    }
}
