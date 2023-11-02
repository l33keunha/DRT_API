package ybs.api.boot.rev.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public interface RevAriService {

	// 정류장 정보 보내기 테스트
	List<HashMap<String, Object>> selectSttn();

	// 링크 레이어 테스트
	List<HashMap<String, Object>> selectLink();

	/**
    * 선택한 노선의 정류장들을 가져온다.
    */
	List<HashMap<String, Object>> submitRouteId(Map<String, Object> map);

    /**
     * 경로탐색
     */
	HashMap<String, Object> submitSttn(Map<String, Object> map) throws IOException, Exception;

	List<HashMap<String, Object>> excelDownload(String scheNo);

}
