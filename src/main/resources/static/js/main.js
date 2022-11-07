
function apiBtn(e){
    var req = "";
    var url = "";
    switch($(e).attr('id')){
        case "xmlBtn":
            exeXml();
            break;
        case "getSttn":
            req = "RUN_AREA_CD=" + $("#getSttn").find("input[name='RUN_AREA_CD']").val();
            url = "getSttn";
            exeAjax(req, url);
            break;
        case "getSche":
            req ="ROUTE_ID=" + $("#getSche").find("input[name='ROUTE_ID']").val()
                +"&STTN_IN_ID=" + $("#getSche").find("input[name='STTN_IN_ID']").val()
                +"&STTN_OUT_ID=" + $("#getSche").find("input[name='STTN_OUT_ID']").val()
                +"&SDATE=" + $("#getSche").find("input[name='SDATE']").val()
                +"&ST_TM=" + $("#getSche").find("input[name='ST_TM']").val()
                +"&USER_CNT=" + $("#getSche").find("input[name='USER_CNT']").val()
                ;
            url = "getSche";
            exeAjax(req,url);
            break;
        case "getUserMast":
            req ="ROUTE_ID=" + $("#getUserMast").find("input[name='ROUTE_ID']").val()
                +"&ROUTE_DIR=" + $("#getUserMast").find("input[name='ROUTE_DIR']").val()
                +"&SDATE=" + $("#getUserMast").find("input[name='SDATE']").val()
                +"&ST_TM=" + $("#getUserMast").find("input[name='ST_TM']").val()
            ;
            url = "getUserMast";
            exeAjax(req,url);
            break;
        case "setUserMast":
            req = "REV_DATE=" + $("#setUserMast").find("input[name='REV_DATE']").val()
                +"&REV_TM=" + $("#setUserMast").find("input[name='REV_TM']").val()
                +"&SDATE=" + $("#setUserMast").find("input[name='SDATE']").val()
                +"&ST_TM=" + $("#setUserMast").find("input[name='ST_TM']").val()
                +"&AREA_NM=" + $("#setUserMast").find("input[name='AREA_NM']").val()
                +"&AREA_CD=" + $("#setUserMast").find("input[name='AREA_CD']").val()
                +"&RUN_AREA_NM=" + $("#setUserMast").find("input[name='RUN_AREA_NM']").val()
                +"&RUN_AREA_CD=" + $("#setUserMast").find("input[name='RUN_AREA_CD']").val()
                +"&RUN_ORDER=" + $("#setUserMast").find("input[name='RUN_ORDER']").val()
                +"&ROUTE_ID=" + $("#setUserMast").find("input[name='ROUTE_ID']").val()
                +"&ROUTE_NM=" + $("#setUserMast").find("input[name='ROUTE_NM']").val()
                +"&ROUTE_NMA=" + $("#setUserMast").find("input[name='ROUTE_NMA']").val()
                +"&ROUTE_DIR=" + $("#setUserMast").find("input[name='ROUTE_DIR']").val()
                +"&PLAT_NUM=" + $("#setUserMast").find("input[name='PLAT_NUM']").val()
                +"&STTN_IN_NM=" + $("#setUserMast").find("input[name='STTN_IN_NM']").val()
                +"&STTN_OUT_NM=" + $("#setUserMast").find("input[name='STTN_OUT_NM']").val()
                +"&STTN_IN_ID=" + $("#setUserMast").find("input[name='STTN_IN_ID']").val()
                +"&STTN_OUT_ID=" + $("#setUserMast").find("input[name='STTN_OUT_ID']").val()
                +"&MAX_SEAT=" + $("#setUserMast").find("input[name='MAX_SEAT']").val()
                +"&TOTAL_DIST=" + $("#setUserMast").find("input[name='TOTAL_DIST']").val()
                +"&TOTAL_TM=" + $("#setUserMast").find("input[name='TOTAL_TM']").val()
                +"&USER_CNT=" + $("#setUserMast").find("input[name='USER_CNT']").val()
                +"&USER_NM=" + $("#setUserMast").find("input[name='USER_NM']").val()
                +"&USER_PHONE=" + $("#setUserMast").find("input[name='USER_PHONE']").val()
                +"&REV_TYPE=" + $("#setUserMast").find("input[name='REV_TYPE']").val()
            ;
            url = "setUserMast";
            exeAjax(req,url);
            break;
        case "setHist":
            req = "SDATE=" + $("#setHist").find("input[name='SDATE']").val()
                +"&ST_TM=" + $("#setHist").find("input[name='ST_TM']").val()
                +"&AREA_NM=" + $("#setHist").find("input[name='AREA_NM']").val()
                +"&AREA_CD=" + $("#setHist").find("input[name='AREA_CD']").val()
                +"&RUN_AREA_NM=" + $("#setHist").find("input[name='RUN_AREA_NM']").val()
                +"&RUN_AREA_CD=" + $("#setHist").find("input[name='RUN_AREA_CD']").val()
                +"&RUN_ORDER=" + $("#setHist").find("input[name='RUN_ORDER']").val()
                +"&ROUTE_ID=" + $("#setHist").find("input[name='ROUTE_ID']").val()
                +"&ROUTE_NM=" + $("#setHist").find("input[name='ROUTE_NM']").val()
                +"&ROUTE_NMA=" + $("#setHist").find("input[name='ROUTE_NMA']").val()
                +"&ROUTE_DIR=" + $("#setHist").find("input[name='ROUTE_DIR']").val()
                +"&PLAT_NUM=" + $("#setHist").find("input[name='PLAT_NUM']").val()
                +"&DRIVER_NM=" + $("#setHist").find("input[name='DRIVER_NM']").val()
                +"&F_NODE_NM=" + $("#setHist").find("input[name='F_NODE_NM']").val()
                +"&T_NODE_NM=" + $("#setHist").find("input[name='T_NODE_NM']").val()
                +"&F_NODE_ID=" + $("#setHist").find("input[name='F_NODE_ID']").val()
                +"&T_NODE_ID=" + $("#setHist").find("input[name='T_NODE_ID']").val()
                +"&TOTAL_DIST=" + $("#setHist").find("input[name='TOTAL_DIST']").val()
                +"&TOTAL_TM=" + $("#setHist").find("input[name='TOTAL_TM']").val()
                +"&USER_CNT=" + $("#setHist").find("input[name='USER_CNT']").val()
                +"&BOARDING_STATUS=" + $("#setHist").find("input[name='BOARDING_STATUS']").val()
            ;
            url = "setHist";
            exeAjax(req,url);
            break;
    }
}

function exeAjax(req, url) {
    $.ajax({
        url : url,
        type : "post",
        data : req,
        success:function(data){
            $("#res").val(JSON.stringify(data,null,4));
        }
    })
}

function examBtn(e){
    switch($(e).attr('id')) {
        case "getSttn":
            $("#getSttn").find("input[name='RUN_AREA_CD']").val("1");
            break;
        case "getSche":
            $("#getSche").find("input[name='ROUTE_ID']").val("48421050");
            $("#getSche").find("input[name='STTN_IN_ID']").val("4895000002");
            $("#getSche").find("input[name='STTN_OUT_ID']").val("4895000008");
            $("#getSche").find("input[name='SDATE']").val("20221007");
            $("#getSche").find("input[name='ST_TM']").val("13");
            $("#getSche").find("input[name='USER_CNT']").val("2");
            break;
        case "getUserMast":
            $("#getUserMast").find("input[name='ROUTE_ID']").val("48421050");
            $("#getUserMast").find("input[name='ROUTE_DIR']").val("1");
            $("#getUserMast").find("input[name='SDATE']").val("20221007");
            $("#getUserMast").find("input[name='ST_TM']").val("140000");
            break;
        case "setUserMast":
            $("#setUserMast").find("input[name='REV_DATE']").val("20221007");
            $("#setUserMast").find("input[name='REV_TM']").val("130000");
            $("#setUserMast").find("input[name='SDATE']").val("20221007");
            $("#setUserMast").find("input[name='ST_TM']").val("140000");
            $("#setUserMast").find("input[name='AREA_NM']").val("양산시");
            $("#setUserMast").find("input[name='AREA_CD']").val("4833000000");
            $("#setUserMast").find("input[name='RUN_AREA_NM']").val("어곡산단");
            $("#setUserMast").find("input[name='RUN_AREA_CD']").val("1");
            $("#setUserMast").find("input[name='RUN_ORDER']").val("6");
            $("#setUserMast").find("input[name='ROUTE_ID']").val("48421050");
            $("#setUserMast").find("input[name='ROUTE_NM']").val("도시형5(어곡산단)");
            $("#setUserMast").find("input[name='ROUTE_NMA']").val("시형5(어곡산업관리공단방향)");
            $("#setUserMast").find("input[name='ROUTE_DIR']").val("1");
            $("#setUserMast").find("input[name='PLAT_NUM']").val("경남11가 1111");
            $("#setUserMast").find("input[name='STTN_IN_NM']").val("어곡사거리");
            $("#setUserMast").find("input[name='STTN_OUT_NM']").val("아워홈");
            $("#setUserMast").find("input[name='STTN_IN_ID']").val("3426");
            $("#setUserMast").find("input[name='STTN_OUT_ID']").val("7022");
            $("#setUserMast").find("input[name='MAX_SEAT']").val("16");
            $("#setUserMast").find("input[name='TOTAL_DIST']").val();
            $("#setUserMast").find("input[name='TOTAL_TM']").val();
            $("#setUserMast").find("input[name='USER_CNT']").val("2");
            $("#setUserMast").find("input[name='USER_NM']").val("김다라");
            $("#setUserMast").find("input[name='USER_PHONE']").val("01011112222");
            $("#setUserMast").find("input[name='REV_TYPE']").val("early");
            break;
        case "setHist":
            $("#setHist").find("input[name='SDATE']").val("20221007");
            $("#setHist").find("input[name='ST_TM']").val("130000");
            $("#setHist").find("input[name='AREA_NM']").val("양산시");
            $("#setHist").find("input[name='AREA_CD']").val("4833000000");
            $("#setHist").find("input[name='RUN_AREA_NM']").val("어곡산단");
            $("#setHist").find("input[name='RUN_AREA_CD']").val("1");
            $("#setHist").find("input[name='RUN_ORDER']").val("6");
            $("#setHist").find("input[name='ROUTE_ID']").val("48421050");
            $("#setHist").find("input[name='ROUTE_NM']").val("도시형5(어곡산단)");
            $("#setHist").find("input[name='ROUTE_NMA']").val("도시형5(어곡산업관리공단방향)");
            $("#setHist").find("input[name='ROUTE_DIR']").val("1");
            $("#setHist").find("input[name='PLAT_NUM']").val("경남11가 1111");
            $("#setHist").find("input[name='DRIVER_NM']").val("홍길동1");
            $("#setHist").find("input[name='F_NODE_NM']").val("어곡사거리");
            $("#setHist").find("input[name='T_NODE_NM']").val("어곡사거리");
            $("#setHist").find("input[name='F_NODE_ID']").val("4895000002");
            $("#setHist").find("input[name='T_NODE_ID']").val("4895000001");
            $("#setHist").find("input[name='TOTAL_DIST']").val();
            $("#setHist").find("input[name='TOTAL_TM']").val();
            $("#setHist").find("input[name='USER_CNT']").val("6");
            $("#setHist").find("input[name='BOARDING_STATUS']").val("N");
            break;
    }
}

function exeXml(req, url) {
    $.ajax({
        url : "setPath",
        type : "post",
        success:function(data){
            $("#res").val(JSON.stringify(data,null,4));
        }
    })
}
