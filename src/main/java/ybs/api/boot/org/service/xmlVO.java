package ybs.api.boot.org.service;

import lombok.Data;

@Data
public class xmlVO {

/*
    private int SCHE_NO;
    private int RES_NO;
    private int INDEX;
    private String NAME;
    private String NODE_TYPE;
    private String POINT;
    private String LINE_STRING;
    private String COORDINATES;
    private String TURN_TYPE;
    private int POINT_INDEX;
    private String POINT_TYPE;
    private int LINE_INDEX;
    private int DISTANCE;
    private int TIME;
    private String ROAD_TYPE;
    private String FACILITY_TYPE;
    private String LINK_ID;
    private int COST_DSTN;
*/

    private int scheNo;
    private int scheSubNo;
    private String userNo;
    private int totalDist;
    private int totalTm;
    private int index;
    private String name;
    private String nodeType;
    private String point;
    private String lineString;
    private String coordinates;
    private String turnType;
    private int pointIndex;
    private String nodeId;
    private String nodeName;
    private String pointType;
    private int lineIndex;
    private int distance;
    private int time;
    private String roadType;
    private String facilityType;
    private String linkId;
    private int costdstn;
    private int sttnSeq;

}
