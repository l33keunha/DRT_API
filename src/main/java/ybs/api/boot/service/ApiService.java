package ybs.api.boot.service;

import java.util.HashMap;

public interface ApiService {

    /**
     * 이용자가 탑승 가능한 배차 정보 조회
     * @param map
     * @return map
     */
    HashMap<String, String> getSche(HashMap<String, String> map);
}
