	/* -----------------------------------------------------------
	 
	 	초기화
	 	 
	-----------------------------------------------------------*/
	/*----------- 전역변수 -----------*/
	var idCnt = 0;
	var passengerCnt = 1;
	var txtfocus;
	
	var _map;
	var _overlay;
	var nodeData;
	var linkData;
	
	var userList;
	
	/*----------- onload 이벤트 -----------*/
	window.onload = function(){
		init();
	}
	
	/*----------- 초기화 -----------*/
	function init(){
		setTime();
		document.getElementById('beg0').onfocus = function() { txtfocus = this; };
		document.getElementById('end0').onfocus = function() { txtfocus = this; };
		addMap();
		
	    // 이벤트 리스너를 추가합니다.
	    _map.on('pointermove', function (evt) {
	    	var pixel = evt.pixel;
	    	var feature = _map.forEachFeatureAtPixel(pixel, function (feature) {
	    		return feature;
	    	});
	    	
	    	if (feature) {
	    		
	    		// 여기서 feature의 속성을 이용하여 어떤 동작을 수행합니다.
	    		var properties = feature.getProperties();
	    		if(!properties.order){
	    			// 마우스가 feature 위에 있을 때
	    			_map.getTargetElement().style.cursor = 'pointer';
	    			// 지도에 팝업 추가
	    			_map.addOverlay(_overlay);
	    			// 오버레이 클릭한 좌표로 조정
	    			_overlay.setPosition(evt.coordinate);
	    			
	    			// 오버레이 객체 호출하여 컨텐츠 추가
	    			var element = _overlay.getElement();
	    			
	    			// _map 객체의 최상단 레이어 가져오기
	    			var topLayer = _map.getLayers().getArray()[_map.getLayers().getArray().length - 1].get('name');
	    			
	    			var content;
	    			/*	    		if(topLayer == 'sttnLayer'){*/
	    			content = '<p>정류장순번: ' + properties.sttnSeq + '</p>' 
	    			+ '<p>정류장이름: ' + properties.sttnNm + '</p>'; 
	    			/*	    		} else {
	    			if(properties.order == undefined){
	    				content = '<p>경유지</p>'; 
	    			} else {
	    				content = '<p>' + properties.order + ' 배차 노선</p>';
	    			}
	    		}*/
	    			
	    			element.innerHTML = content;
	    			
	    			_overlay.setOffset([0, -22]);
	    		}

	    	} else {
	    		// 마우스가 feature 위에 없을 때
	    		_map.getTargetElement().style.cursor = '';
	    		_map.removeOverlay(_overlay);
	    	}
	    });
	    
	    // 마우스 클릭 이벤트 리스너
	    _map.on('click', function (evt) {
	    	var pixel = evt.pixel;
	    	var feature = _map.forEachFeatureAtPixel(pixel, function (feature) {
	    		return feature;
	    	});
	    	
	    	if (feature) {
	    		// 여기서 feature를 클릭했을 때의 동작을 정의합니다.
	    		// 예를 들어, 속성을 팝업으로 표시할 수 있습니다.
	    		var properties = feature.getProperties();
	    		
	    		var topLayer = _map.getLayers().getArray()[_map.getLayers().getArray().length - 1].get('name');
	    		if(topLayer == 'sttnLayer'){
		            if (txtfocus) {
		                txtfocus.value = properties.sttnNm + "_" + properties.sttnSeq; 
		                txtfocus.setAttribute('data-sttn-seq', properties.sttnSeq); 
		                txtfocus.setAttribute('data-sttn-nm', properties.sttnNm); 
		            }
	    		} else {
	    			//nodeLayerVisible(properties.order);
	    		}
	    	}
	    });
	}
	
	/*----------- selcetbox 시/분 세팅 -----------*/
	function setTime(){
		for (var i = 10; i <= 10; i++) {
			$('.hh').append($('<option>', {value: i, text: i}));
		}
		for (var i = 0; i <= 59; i++) {
			$('.mm').append($('<option>', {value: i, text: i}));
		}
	}
	
	/*----------- 지도 초기화 & geoJSON 읽기 -----------*/
	function addMap(){
		_overlay = new ol.Overlay({
			  element: document.getElementById('popup'),
			  autoPan: true,
			  autoPanAnimation: {
			    duration: 250
			  }
		});
		
		_map = new ol.Map({
			target: 'map',
			layers: [new ol.layer.Tile({
				source: new ol.source.XYZ({
					url: 'http://xdworld.vworld.kr:8080/2d/Base/service/{z}/{x}/{y}.png'
				})
			})],
			view: new ol.View({
		        center: [129.06515049450681, 35.422114861687561],
		        projection : 'EPSG:4326',
		        zoom: 17
			}),
			overlays: [_overlay]
		});
		
		$.getJSON('/geojson/N_RP_DATA_NODE.geojson', function(data) {
			nodeData = data;
		});
		$.getJSON('/geojson/N_RP_DATA_LINK.geojson', function(data) {
			linkData = data;
		});
		
	}
	
	/* ajax 실행 */
	function exeAjax(req, url, fnCallback) {
		$.ajax({
			url : url,
			type : "post",
			contentType: 'application/json',
			data: JSON.stringify(req), 
			success:function(data){
				fnCallback(data);
			}
		})
	}
	
	/* -----------------------------------------------------------
	 
	 	버튼 이벤트
	 	 
	-----------------------------------------------------------*/
	/*----------- 노선검색 버튼 이벤트 -----------*/
	function fnGetSttnBtn(){
		var routeId = document.getElementById('routeId').value;
		var data = {
				"routeId": routeId,
		};
		var fnCallback = successFnGetSttnBtn;
		exeAjax(data, "submitRouteId", fnCallback);
	}
	
	/*----------- 추가 버튼 이벤트 -----------*/
	function fnAddBtn(){
		idCnt++;
		passengerCnt++;
		if(idCnt < 5){
			var newTable = '<tbody>';
			newTable += '<tr>';
			newTable += '<td rowspan="3" id="user'+ idCnt +'" >' + passengerCnt + '번 탑승객</td>';
			newTable += '<td>출발 정류장</td>';
			newTable += '<td colspan="2"><input type="text"  id=beg' + idCnt + '></td>';
			newTable += '</tr>';
			newTable += '<tr>';
			newTable += '<td>도착 정류장</td>';
			newTable += '<td colspan="2"><input type="text"  id=end' + idCnt + '></td>';
			newTable += '</tr>';
			newTable += '<tr>';
			newTable += '<td>예약 시간</td>';
			newTable += '<td><select class="hh" id="hh' + idCnt + '"></select> 시</td>';
			newTable += '<td><select class="mm" id="mm' + idCnt + '"></select> 분</td>';
			newTable += '</tr>';
			newTable += '</tbody>';
			
			$('.param-area table').append(newTable);
			setTime();
			
			document.getElementById('beg'+idCnt).onfocus = function() { txtfocus = this; };
			document.getElementById('end'+idCnt).onfocus = function() { txtfocus = this; };
		} else {
			alert("최대 5번까지 가능합니다.");
			$("#addBtn").prop("disabled", true);
		}
	};
	
	/*----------- 제출 -----------*/
	function fnClickBtn(){
		var bnParam = prompt("탑승객 배차노선 선정기준 값을 숫자로 입력하세요. (기본값: 5분)", "5");
		
		var begDataSttnSeq = [];
		var endDataSttnSeq = [];
		var begDataSttnNm = [];
		var endDataSttnNm = [];
		var hhList = [];
		var mmList = [];
		
		for (var i = 0; i <= idCnt; i++) {
			var begId = 'beg' + i;
			var endId = 'end' + i;
			var hhId = 'hh' + i;
			var mmId = 'mm' + i;
			
			var begElement = document.getElementById(begId);
			var endElement = document.getElementById(endId);
			var hhElement = document.getElementById(hhId);
			var mmElement = document.getElementById(mmId);
			
			var begDataSeq = begElement.getAttribute('data-sttn-seq');
			var endDataSeq = endElement.getAttribute('data-sttn-seq');
			var begDataNm = begElement.getAttribute('data-sttn-nm');
			var endDataNm = endElement.getAttribute('data-sttn-nm');
			var hhVal = hhElement.value;
			var endVal = mmElement.value;
			
			begDataSttnSeq.push(begDataSeq);
			endDataSttnSeq.push(endDataSeq);
			begDataSttnNm.push(begDataNm);
			endDataSttnNm.push(endDataNm);
			hhList.push(hhVal);
			mmList.push(endVal);
		}
		var data = {
				"userCnt": idCnt,
				"begDataSttnSeq": begDataSttnSeq,
				"endDataSttnSeq": endDataSttnSeq,
				"begDataSttnNm": begDataSttnNm,
				"endDataSttnNm": endDataSttnNm,
				"hhList": hhList,
				"mmList": mmList,
				"bnParam": bnParam
		};
		var fnCallback = successFnSubmitSttn;
		exeAjax(data, "submitSttn", fnCallback);
	};
	
	/*----------- 제출 -----------*/
	function fnShowResult(){
		const btn = document.getElementById('resultBtn');
		const modal = document.getElementById('modalWrap');
		const closeBtn = document.getElementById('closeBtn');

		btn.onclick = function() {
		  modal.style.display = 'block';
		}
		closeBtn.onclick = function() {
		  modal.style.display = 'none';
		}

		window.onclick = function(event) {
		  if (event.target == modal) {
		    modal.style.display = "none";
		  }
		}
		
		var result = "";
		
		for(i in userList){
			result += '<tr>';
		    result += '<td class="tg-0lax">'+ (Number(userList[i].USER_NO) + 1) +' 번</td>'; // 순번
		    result += '<td class="tg-0lax">'+ userList[i].ORDER_TM +' 시</td>'; // 배차시간
		    result += '<td class="tg-0lax">'+ userList[i].BEG_DATA_STTN_NM +'</td>'; // 출발정류장명
		    result += '<td class="tg-0lax">'+ userList[i].END_DATA_STTN_NM +'</td>'; // 도착정류장명
		    result += '<td class="tg-0lax">' + userList[i].REV_HH + '시 ' + userList[i].REV_MM +'분</td>'; // 예약시간
		    result += '<td class="tg-0lax">10시 '+ userList[i].TIME_SUM_MMM_ORG +'분</td>'; // 기존탑승시각
		    result += '<td class="tg-0lax">' + userList[i].ORDER_TM + '시 ' + userList[i].TIME_SUM_MMM +'분</td>'; // RP탑승시각
		    result += '<td class="tg-0lax">' + userList[i].WAIT_TM +'분</td>'; // 대기시간
		    result += '<td class="tg-0lax">' + userList[i].LONG_TM_ORG +'분</td>'; // 기존소요시간
		    result += '<td class="tg-0lax">' + userList[i].LONG_TM +'분</td>'; // RP소요시간
		    result += '<td class="tg-0lax">' + userList[i].LONG_DIST_ORG +'m</td>'; // 기존이동거리
		    result += '<td class="tg-0lax">' + userList[i].LONG_DIST +'m</td>'; // RP이동거리
		    result += '<td class="tg-0lax">' + userList[i].TOTAL_TM_ORG +'분</td>'; // 기존노선 총소요시간
		    result += '<td class="tg-0lax">' + userList[i].TOTAL_TM +'분</td>'; // RP 총소요시간
		    result += '<td class="tg-0lax">' + userList[i].TOTAL_DIST_ORG +'m</td>'; // 기존노선 총이동거리
		    result += '<td class="tg-0lax">' + userList[i].TOTAL_DIST +'m</td>'; // RP 총이동거리
		    result += '</tr>';
		    
		    $(".modal-tbody").html(result);
		}
	}
	
	/* -----------------------------------------------------------
	 
 		콜백함수
 	 
	-----------------------------------------------------------*/
	function successFnGetSttnBtn(data){
		var featuresSelected = [];
		for(var i = 0; i < data.data.length; i++){
		    var item = data.data[i];
		    var feature = new ol.Feature({
		        geometry: new ol.geom.Point([item.STTN_X, item.STTN_Y])
		    });
		    feature.set("nodeId", item.NODE_ID);
		    feature.set("sttnSeq", item.STTN_SEQ);
		    feature.set("sttnNm", item.STTN_NM);
		    featuresSelected.push(feature);
		}

		var sttnLayer = new ol.layer.Vector({
		    name: 'sttnLayer',
		    source: new ol.source.Vector({
		        features: featuresSelected
		    }),
		    style: new ol.style.Style({
		        image: new ol.style.Icon({
		            src: 'img/DRT_icon2.png',
		            scale: 0.5,
		        })
		    })
		});
		sttnLayer.setZIndex(1);
		_map.addLayer(sttnLayer);
	}
	
	function successFnSubmitSttn(result){
		console.log(result);
		
    	var orderMaster_link = result.data.orderMaster_link;
    	var order10_link = result.data.order10_link;
    	var order11_link = result.data.order11_link;
    	var orderMaster_node = result.data.orderMaster_node;
    	var order10_node = result.data.order10_node;
    	var order11_node = result.data.order11_node;
    	userList = result.data.userList;
    	$("#scheNo").val(userList[0].SCHE_NO);
    	
    	$("#getSttnBtn").css('display','none');
    	$(".param-area").css('display','none');
    	$(".button-area").css('display','none');
    	$(".result-area").css('display','block');
    	
    	var sumStr ='';
    	if(order10_node.length > 0){
    		sumStr += '<div>';
    		sumStr += '<div>10시 배차 &nbsp&nbsp&nbsp</div>';
    		sumStr += '<div>총 정류장 수 : ' + order10_node.length + ' 개&nbsp&nbsp&nbsp</div>';
    		sumStr += '<div>총 운행시간 : ' + order10_node[0].TOTAL_TM + ' 분&nbsp&nbsp&nbsp</div>';
    		sumStr += '</div>';
    	}
    	if(order11_node.length > 0){
    		sumStr += '<div>';
    		sumStr += '<div>11시 배차 &nbsp&nbsp&nbsp</div>';
    		sumStr += '<div>총 정류장 수 : ' + order11_node.length + ' 개&nbsp&nbsp&nbsp</div>';
    		sumStr += '<div>총 운행시간 : ' + order11_node[0].TOTAL_TM + ' 분&nbsp&nbsp&nbsp</div>';
    		sumStr += '</div>';
    	}
    	$('.result-area-sum').html(sumStr);
    	
    	var resultStr = '';
    	for(var i in userList){
    		resultStr += '<div class="user-area-box">';
    		resultStr += '<div>';
    		resultStr += '<div>' + (Number(userList[i].USER_NO) + 1) + ' 번째 이용자&nbsp&nbsp&nbsp</div>';
    		resultStr += '<div>예약시간 : ' + userList[i].REV_HH + ' 시 ' + userList[i].REV_MM + ' 분&nbsp&nbsp&nbsp</div>';
    		resultStr += '<div>배차시각 : ' + userList[i].ORDER_TM + ' 시&nbsp&nbsp&nbsp</div>';
    		resultStr += '</div>';
    		resultStr += '<div>' + userList[i].BEG_DATA_STTN_NM +' - '+ userList[i].END_DATA_STTN_NM +'</div>';
    		resultStr += '<div>';
    		resultStr += '<div>탑승시각 : ' + userList[i].ORDER_TM + ' 시 ' + userList[i].TIME_SUM_MMM + ' 분&nbsp&nbsp&nbsp</div>';
    		resultStr += '<div>기점 - 승차정류장 도착거리 : ' + userList[i].DIST_SUM_M + ' m</div>';
    		resultStr += '</div>';
    		resultStr += '<div>';
    		resultStr += '<div>소요시간 : ' + userList[i].LONG_TM + ' 분&nbsp&nbsp&nbsp</div>';
    		resultStr += '<div>이동거리 : ' + userList[i].LONG_DIST + ' m</div>';
    		resultStr += '</div>';
    		resultStr += '</div>';
    		
    		if(userList[i].ORDER_TM == 10){
    			$('.user-area-box').css("background-color", "wheat")
    		}
    	}
    	$(".user-area").html(resultStr);
    	
    	
    	
    	
    	// -- 범례 설정
    	$("#mapLegend").css("display", "block");
    	
    	
    	
    	
    	// -- 기존 정류장 레이어 삭제
/*    	var layerToRemove = _map.getLayers().getArray().find(function(layer) {
    	  return layer.get('name') === 'sttnLayer';
    	// 레이어가 존재하면 삭제
    	if (layerToRemove) {
    	  _map.removeLayer(layerToRemove);
    	}
    	});*/

    	addLayerLink(orderMaster_link, '기존', 'orderMaster_link', '#8080809e', 15);
    	addLayerNode(orderMaster_node, 'orderMaster_node');
    	$("#orderMaster_link").prop('checked',true);
    	if(order10_link != ""){
    		addLayerLink(order10_link, '10시', 'order10_link', '#1fc900bf', 8);
    		addLayerNode(order10_node, 'order10_node');
    		$("#order10_link").prop('checked',true);
    	} else {
    		$("#order10_link").attr('disabled',true);
    		$("#order10_node").attr('disabled',true);
    	}
    	if(order11_link != ""){
    		addLayerLink(order11_link, '11시', 'order11_link', '#0056b3f2', 5);
    		addLayerNode(order11_node, 'order11_node');
    		$("#order11_link").prop('checked',true);
    	} else {
    		$("#order11_link").attr('disabled',true);
    		$("#order11_node").attr('disabled',true);
    	}
	}
	
	function addLayerLink(jsonObject, orderName, layerName, colorCode, lineWidth){
		var featuresSelected = [];
		var linkIDsToSelect = jsonObject.map(function(item) {
			return item.LINK_ID;
		});
		linkData.features.forEach(function(featureData) {
			var linkID = featureData.properties.LINK_ID;
			if (linkIDsToSelect.includes(linkID)) {
				var feature = new ol.Feature({
					geometry: new ol.geom.MultiLineString(featureData.geometry.coordinates)
				});
				feature.set("order", orderName);
				featuresSelected.push(feature);
			}
		});
		var geojson_layer = new ol.layer.Vector({
			name: layerName,
			source: new ol.source.Vector({
				features: featuresSelected
			}),
			style : new ol.style.Style({
				stroke: new ol.style.Stroke({ color: colorCode, width: lineWidth })
			})
		});
		_map.addLayer(geojson_layer);
		
	}
	
	function addLayerNode(jsonObject, layerName){
		var featuresSelected = [];
		var nodeIDsToSelect = jsonObject.map(function(item) {
			return item.NODE_ID;
		});
		nodeData.features.forEach(function(featureData) {
			var nodeID = featureData.properties.NODE_ID;
			if (nodeIDsToSelect.includes(nodeID)) {
				var feature = new ol.Feature({
					geometry: new ol.geom.Point(featureData.geometry.coordinates)
				});
			    feature.set("order", layerName);
				featuresSelected.push(feature);
			}
		});
		var geojson_layer = new ol.layer.Vector({
			name: layerName,
			source: new ol.source.Vector({
				features: featuresSelected
			}),
			style : new ol.style.Style({
		        image: new ol.style.Icon({
		            src: 'img/DRT_icon.png',
		            scale: 0.5,
		        })
			}),
			visible: false,
		});
		_map.addLayer(geojson_layer);
		geojson_layer.setZIndex(2);
	}
	
	function nodeLayerVisible(layerName){
		// 맵에서 삭제할 레이어 이름들
		var layerNamesToDelete = ['orderMaster_node', 'order10_node', 'order11_node'];

		// 각 레이어 이름을 순회하면서 해당 이름을 가진 레이어 삭제
		layerNamesToDelete.forEach(function(layerName) {
		    var selectLayer = _map.getLayers().getArray().find(function(layer) {
		        return layer.get('name') === layerName;
		    });

		    if (selectLayer) {
		    	selectLayer.setVisible(false);
		    }
		});

    	var selectLayer;
		if(layerName == '기존'){
			selectLayer = _map.getLayers().getArray().find(function(layer) {
				return layer.get('name') == 'orderMaster_node'
			});
		} else if (layerName == '10시'){
			selectLayer = _map.getLayers().getArray().find(function(layer) {
				return layer.get('name') == 'order10_node'
			});
		} else if (layerName == '11시'){
			selectLayer = _map.getLayers().getArray().find(function(layer) {
				return layer.get('name') == 'order11_node'
			});
		}
    	selectLayer.setVisible(true);
	}
	
	$(document).on("click", ".legendCheck", function(){
		 var id_check = $(this).attr("id");
	     var isChecked = $("#" + id_check).is(":checked");
	     
	     var selectLayer;
	     selectLayer = _map.getLayers().getArray().find(function(layer) {
	    	 return layer.get('name') == id_check
	     });
	     if(isChecked == true){
    	 	selectLayer.setVisible(true);
	     } else{
	    	selectLayer.setVisible(false);
	    	 
	     }
	});
