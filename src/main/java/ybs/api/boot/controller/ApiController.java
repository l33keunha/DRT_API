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

import org.w3c.dom.Document;
import ybs.api.boot.service.ApiService;
import ybs.api.boot.service.xmlVO;
import ybs.api.boot.util.ParsingJSONUtil;
import ybs.api.boot.util.ParsingHashMapUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
     * 이용자가 승/하차할 정류장 선택
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
     * 이용자가 탑승 가능한 배차 정보 조회
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
     * 예약자 등록
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
     * 운전자가 운행할 차량의 예약자 조회
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
     * 운전자가 운행 종료 후 저장
     * @param map
     * @return JSON
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/setHist")
    public JSONObject setHist(@RequestParam HashMap<String, Object> map) throws Exception{

        ParsingJSONUtil util = new ParsingJSONUtil();
        return util.resultParsingJSON(service.setHist(map));

    }

    /**
     * 운행정보 저장(사전예약 기준 경로탐색)
     * @param
     * @return JSON
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/setPath")
    public JSONObject setLog(HttpServletRequest rsq
                             ,@RequestParam HashMap<String, Document> map) throws Exception {

        Path path = Paths.get("D:\\STSworkspace\\DRT_API\\src\\main\\resources\\static\\xml\\sample.xml");
        String file = Files.readString(path);

        /* 
            다른 util인 이유 : json.simple 과 json 라이브러리 호환 문제
            json.simple => JSONObject, JSONArray 때 필요
            json => xml 파싱 때 필요
        */

        ParsingHashMapUtil util = new ParsingHashMapUtil();
        ArrayList<xmlVO> list = util.xmlParsingJson(file);
        System.out.println(list.toString());
        int result = service.setPath(list);
        System.out.println(list.toString());
        System.out.println(result);

        return null;

    }

    @ResponseBody
    @RequestMapping("/practiceXml")
    public void practiceXml(HttpServletRequest req) throws Exception{
        // xml factory 취득
        XMLInputFactory factory = XMLInputFactory.newInstance();

        // 파일 stream 취득 (자동 close)
        FileReader reader = new FileReader("D:/DRT_API-workspace/DRT_API/src/main/resources/static/xml/sample.xml");
        // xmlreader 생성
        XMLStreamReader xmlreader = factory.createXMLStreamReader(reader);

        // iterator 패턴으로 태그를 반복적으로 취득
        while(xmlreader.hasNext()){
            
            // text에서 태그 행 번호와 열 번호
            // System.out.println(xmlreader.getLocation().getLineNumber() + " / " + xmlreader.getLocation().getColumnNumber());

            // System.out.println("콘솔 출력 시작 : [ ");
            // 태그의 타입 별로 출력 방법을 구분한다.
            switch (xmlreader.getEventType()){

                // xml 시작 태그
/*                case XMLStreamReader.START_DOCUMENT: {
                    // 태그 열기 출력
                    System.out.print("<?xml");
                    // 버젼 출력
                    System.out.print(" version='" + xmlreader.getVersion() + "'");
                    // 인코딩 타입 출력
                    System.out.print(" encoding='" + xmlreader.getCharacterEncodingScheme() + "'");
                    // standalone 타입 출력
                    if (xmlreader.isStandalone()) {
                            // 콘솔 출력
                            System.out.print(" standalone='yes'");
                        } else {
                            // 콘솔 출력
                            System.out.print(" standalone='no'");
                        }
                        // 태그 닫기 출력
                        System.out.print("?>");
                }
                break;
*/

                
                // 일반 시작 태그
                case XMLStreamReader.START_ELEMENT: {
                    // 일반 시작 태그 출력
                    System.out.print("일반 시작 태그 <");
                    // 태그 이름을 가지고 있을 경우
                    if(xmlreader.hasName()){
                        // 접두어 취득
                        String uri = xmlreader.getPrefix();
                        // 태그 이름 취득
                        String localName = xmlreader.getLocalName();
                        
                        // uri가 있으면
                        if(uri != null && !"".equals(uri)){
                            System.out.println("[" + uri + "]");
                        }
                        // 접두어가 있으면
                        if(prefix != null && !"".equals(prefix)){
                            System.out.println(prefix + ":");
                        }
                        // 태그 이름이 있으면
                        if(localName != null){
                            System.out.println(localName);
                        }
                    }
                    
                    // 네임 스페이스 갯수만큼 반복
                    for(int i = 0; i < xmlreader.getNamespaceCount(); i++){
                        // 네임 스페이스 접두어 취득
                        String prefix = xmlreader.getNamespacePrefix(i);
                        // url 취득
                        String uri = xmlreader.getNamespaceURI(i);
                        System.out.println(" ");

                        if(prefix == null){
                            // uri만 출력
                            System.out.println("xmlns=' " + uri + "'");
                        } else{
                            System.out.println("xmlns: " + prefix + "='" + uri + "'");
                        }
                    }

                    for(int i = 0; i < xmlreader.getAttributeCount(); i++){
                        // 접두어 취득
                        String prefix = xmlreader.getAttributePrefix(i);
                        // 네임 스페이스 취득
                        String namespace = xmlreader.getAttributeNamespace(i);
                        // 속성 이름 취득
                        String localName = xmlreader.getAttributeLocalName(i);
                        // 값 취득
                        String value = xmlreader.getAttributeValue(i);

                        // 네임 스페이스가 있으면
                        if(namespace != null && !"".equals(namespace)){
                            System.out.println("[ " + namespace + " ]");
                        }
                        // 접두어가 있으면
                        if(prefix != null && !"".equals(prefix)){
                            System.out.println(prefix + " : ");
                        }
                        // 속성 이름이 있으면
                        if(localName != null){
                            System.out.println(localName);
                        }
                        // 값 출력
                        System.out.println("=' " + value + " '");
                    }
                    // 일반 시작 태그 출력
                    System.out.print("> 일반 시작 태그");
                }
                break;

                // 일반 닫기 태그
/*
                case XMLStreamReader.END_ELEMENT: {
                    // 일반 닫기 태그 출력
                    System.out.print("</");
                    // 태그 이름을 가지고 있을 경우
                    if (xmlreader.hasName()) {
                        // 접두어 취득
                        String prefix = xmlreader.getPrefix();
                        // uri 취득
                        String uri = xmlreader.getNamespaceURI();
                        // 태그 이름 취득
                        String localName = xmlreader.getLocalName();
                        // url가 있으면
                        if (uri != null && !"".equals(uri)) {
                            // 콘솔 출력
                            System.out.print("[" + uri + "]");
                        }
                        // 접두어가 있으면
                        if (prefix != null && !"".equals(prefix)) {
                            // 콘솔 출력
                            System.out.print(prefix + ":");
                        }
                        // 태그 이름이 있으면
                        if (localName != null) {
                            // 콘솔 출력
                            System.out.print(localName);
                        }
                    }
                    // 일반 닫기 태그 출력
                    System.out.print(">");
                }
                break;
*/

                // 태그 공백일 경우, 문자일 경우

                case XMLStreamReader.SPACE:
                case XMLStreamReader.CHARACTERS: {
                    // 문자의 시작 index
                    int start = xmlreader.getTextStart();
                    // 문자의 갯수
                    int length = xmlreader.getTextLength();
                    // 콘솔 출력
                    System.out.print(new String(xmlreader.getTextCharacters(), start, length));
                }
                break;


                // 명령 태그
/*
                case XMLStreamReader.PROCESSING_INSTRUCTION: {
                    // 명령 태그 시작 출력
                    System.out.print("<?");
                    // 문자가 존재하면
                    if (xmlreader.hasText()) {
                        // 콘솔 출력
                        System.out.print(xmlreader.getText());
                    }
                    // 명령 태그 닫기 출력
                    System.out.print("?>");
                }
                break;
*/

                // CDATA 태그 (문자 데이터)
/*
                case XMLStreamReader.CDATA: {
                    // CDATA 열기 출력
                    System.out.print("<![CDATA[");
                    // 문자의 시작 index
                    int start = xmlreader.getTextStart();
                    // 문자의 갯수
                    int length = xmlreader.getTextLength();
                    // 콘솔 출력
                    System.out.print(new String(xmlreader.getTextCharacters(), start, length));
                    // CDATA 닫기 출력
                    System.out.print("]]>");
                }
                break;
*/

                // 주석 출력
/*
                case XMLStreamReader.COMMENT: {
                    // 주석 열기 출력
                    System.out.print("<!--");
                    // 문자가 존재하면
                    if (xmlreader.hasText()) {
                        // 콘솔 출력
                        System.out.print(xmlreader.getText());
                    }
                    // 주석 닫기 출력
                    System.out.print("-->");
                }
                break;
*/

                // 엔티티 참조
/*
                case XMLStreamReader.ENTITY_REFERENCE: {
                    // 콘솔 출력
                    System.out.print(xmlreader.getLocalName() + "=");
                    // 문자가 존재하면
                    if (xmlreader.hasText()) {
                        // 콘솔 출력
                        System.out.print("[" + xmlreader.getText() + "]");
                    }
                }
                break;
*/
            }

            // 콘솔 출력 닫기
            System.out.println("]");
            // iterator 패턴의 다음 데이터
            xmlreader.next();
        }
        // xmlreader 닫기
        xmlreader.close();
    }
    

}



