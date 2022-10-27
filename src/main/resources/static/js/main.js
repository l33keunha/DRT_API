
function callApi(e){
    var req = "";
    var url = "";
    switch($(e).attr('class')){
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
