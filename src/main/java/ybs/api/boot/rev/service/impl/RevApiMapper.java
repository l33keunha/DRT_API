package ybs.api.boot.rev.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RevApiMapper {

	// 정류장 정보 보내기 테스트
	List<HashMap<String, Object>> selectSttn(String runAreaCd);

	// 링크 레이어 테스트
	List<HashMap<String,Object>> selectLink();
	
	/**
    * 선택한 노선의 정류장들을 가져온다.
    */
	List<HashMap<String, Object>> submitRouteId(Map<String, Object> map);
	
	/**
	 * 경로탐색좌표배열
	 */
	List<Double> getSttnCoordArr(@Param("RUN_AREA_CD")String runAreaCd, @Param("sttnList") List<Integer> sttnList); 

	/**
	 * 경로탐색좌표리스트
	 */
	List<HashMap<String, Double>> getSttnCoordList(@Param("RUN_AREA_CD")String runAreaCd, @Param("sttnList") List<Integer> sttnList);

	void setPath(Map<String, Object> rp_result_map);

	List<HashMap<String, Object>> getSttnTime(@Param("scheNo")String scheNo, @Param("i")int i);

	List<HashMap<String, Object>> getResultLink(@Param("scheNo")String scheNo, @Param("i")int cnt);

	int setUserList(@Param("scheNo")String scheNo, @Param("userList")List<HashMap<String, Object>> userList);

	List<HashMap<String, Object>> getUserListForUpdate(@Param("scheNo")String scheNo, @Param("i")int i);

	int updateUserList(@Param("scheNo")String scheNo, @Param("map")HashMap<String, Object> hashMap, @Param("i")int i, @Param("userNo")int userNo);

	List<HashMap<String, Object>> getUserList(@Param("scheNo")String scheNo);
	
	List<HashMap<String, Object>> getUserListExcel(@Param("scheNo")String scheNo);




}
