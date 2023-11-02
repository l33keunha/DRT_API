package ybs.api.boot.rev.controller;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ybs.api.boot.rev.service.RevAriService;
import ybs.api.boot.util.ParsingJSONUtil;

@Controller
public class RevAriController {
	
    @Autowired
    private RevAriService service;
    
    /**
     * 테이블만 있는 페이지 이동 (정류장 정보 보내기 테스트)
     */
    @RequestMapping(value = "/index_rev.do", method = RequestMethod.GET)
    public String goIndex_test(Model model, HttpServletRequest req, HttpServletResponse res){
    	List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
    	model.addAttribute("list", service.selectSttn());
    	return "index_rev";
    }
    
    /**
     * 지도만 있는 페이지 (링크 레이어 테스트)
     */
    @RequestMapping("/map.do")
    public String goMap(Model model, HttpServletRequest req, HttpServletResponse res) {
    	return "map";
    }
    
    // 링크 레이어 테스트
    @ResponseBody
    @RequestMapping("/getLink")
    public JSONObject getLink(@RequestBody Map<String, Object> map) {
        ParsingJSONUtil util = new ParsingJSONUtil();
        return util.listParsingJSON(service.selectLink());
    }
    
    
    
    
    /**
     * index page 이동
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String goIndex(Model model, HttpServletRequest req, HttpServletResponse res){
    	List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
    	model.addAttribute("list", service.selectSttn());
    	return "index";
    }
    
    /**
     * 선택한 노선의 정류장들을 가져온다.
     */
    @ResponseBody
    @RequestMapping("/submitRouteId")
    public JSONObject submitRouteId(@RequestBody Map<String, Object> map) throws Exception {
        ParsingJSONUtil util = new ParsingJSONUtil();
        return util.listParsingJSON(service.submitRouteId(map));
    }
    
    /**
     * 경로탐색
     */
    @ResponseBody
    @RequestMapping("/submitSttn")
    public JSONObject submitSttn(@RequestBody Map<String, Object> map) throws Exception {
    	ParsingJSONUtil util = new ParsingJSONUtil();
    	HashMap<String, Object> list = service.submitSttn(map);
        return util.mapParsingJSON(list);
    }
    
    /** 
     * 엑셀 다운로드
     */
    @RequestMapping("/excelDownload")
    public void excelDownload(HttpServletResponse response, @RequestParam Map<String, Object> map) throws Exception {
    	String scheNo = (String) map.get("scheNo");
    	List<HashMap<String, Object>> userList = service.excelDownload(scheNo);
    	
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("sheet1");
        Row row = null;
        Cell cell = null;
        int rowNum = 0;
        
        // Header
        String[] headers = { "순서", "배차시간", "출발정류장명", "도착정류장명", "예약시간", "기존탑승시각", "경로탐색탑승시각", "대기시간",
        							"기존소요시간", "경로탐색소요시간", "기존이동거리", "경로탐색이동거리", "기존노선총소요시간", "경로탐색총소요시간", "기존노선총이동거리", "경로탐색총이동거리"};
        row = sheet.createRow(rowNum++);
        for(int i = 0; i < headers.length; i++) {
        	cell = row.createCell(i);
        	cell.setCellValue(headers[i]);
        }
        
        // Body
        String[] bodies = { "USER_NO", "ORDER_TM", "BEG_DATA_STTN_NM", "END_DATA_STTN_NM", "REV_TM", "TIME_SUM_MMM_ORG", "TIME_SUM_MMM", "WAIT_TIME",
        						  "LONG_TM_ORG", "LONG_TM", "LONG_DIST_ORG", "LONG_DIST", "TOTAL_TM_ORG", "TOTAL_TM", "TOTAL_DIST_ORG", "TOTAL_DIST" };
        for (int i = 0; i < userList.size(); i++) {
            row = sheet.createRow(rowNum++);
            HashMap<String, Object> dataMap = userList.get(i);
            for(int j = 0; j < bodies.length; j++) {
            	cell = row.createCell(j);
            	cell.setCellValue(dataMap.get(bodies[j]).toString());
            }
        }
        
        // 컨텐츠 타입과 파일명 지정
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("경로탐색_결과리스트.xlsx", "UTF-8"));

        // Excel File Output
        wb.write(response.getOutputStream());
        wb.close();
    }

}
