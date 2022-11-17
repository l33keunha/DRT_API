package ybs.api.boot.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ybs.api.boot.service.xmlVO;

public class ConnectionURLUtil {

	
	public String makeParam(List<HashMap<String, Object>> list) {
		String BEG_X = String.valueOf(list.get(0).get("STTN_X"));
		String BEG_Y = String.valueOf(list.get(0).get("STTN_Y"));
		String END_X = String.valueOf(list.get(1).get("STTN_X"));
		String END_Y = String.valueOf(list.get(1).get("STTN_Y"));
		String WAYPOINT ="";
		
		if(list.size() > 2) {
			for(int i = 2; i < list.size(); i++) {
					WAYPOINT += list.get(i).get("STTN_X");
					WAYPOINT += ",";
					WAYPOINT += list.get(i).get("STTN_Y");
					if(i != list.size() -1) {
						WAYPOINT += "_";
					}
				}
		} else {
			WAYPOINT = "";
		}
		
		String param = "TYPE=ROUTING"
				+ "&END_X=" + END_X 
				+ "&END_Y=" + END_Y
				+ "&BEG_X=" + BEG_X
				+ "&BEG_Y=" + BEG_Y
				+ "&ANG=&OPTION=0"
				+"&WAYPOINT=" + WAYPOINT
				;
		System.out.println(param);
		return param;
	}
	
	public ArrayList<xmlVO> connectionURL(String param) throws Exception{
		
		URL url = new URL("http://maas.onioi.com:8888/req");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(param);
        wr.flush();
        
        BufferedReader br =  new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line = null;
        
        while((line = br.readLine()) != null) {
        	sb.append(line);
        }
        System.out.println(sb.toString());
        
        ParsingHashMapUtil mUtil = new ParsingHashMapUtil();
		return mUtil.xmlParsingJson(sb.toString());
	}
}
