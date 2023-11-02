	/*----------- 전역변수 -----------*/
	var _map;
	var geoData;
	
	/*----------- onload 이벤트 -----------*/
	window.onload = function(){
		init();
	}
	
	/*----------- 초기화 -----------*/
	function init(){
		
		// 지도를 생성하고 VWorld 레이어를 추가합니다.
		_map = new ol.Map({
			target: 'map',
			layers: [new ol.layer.Tile({
				source: new ol.source.XYZ({
					url: 'http://xdworld.vworld.kr:8080/2d/Base/service/{z}/{x}/{y}.png'
				})
			})],
			view: new ol.View({
		        center: [129.024002, 35.376305],
		        projection : 'EPSG:4326',
		        zoom: 17
			})
		});
		
		$.getJSON('/geojson/N_RP_DATA_LINK.geojson', function(data) {
			geoData = data;
		});

		
	}
	
	function fnAddLayer(){
		$.ajax({
		    type : 'post',           // 타입 (get, post, put 등등)
		    url : '/getLink',           // 요청할 서버url
		    async : true,            // 비동기화 여부 (default : true)
		    headers : {              // Http header
		      "Content-Type" : "application/json",
		      "X-HTTP-Method-Override" : "POST"
		    },
		    dataType : 'text',       // 데이터 타입 (html, xml, json, text 등등)
		    data : JSON.stringify({  // 보낼 데이터 (Object , String, Array)
		      "no" : "2",
		    }),
		    success : function(result) { // 결과 성공 콜백함수
		    	var jsonObject = JSON.parse(result);
		    	
			    var featuresSelected = [];
			    var linkIDsToSelect = jsonObject.data.map(function(item) {
			        return item.link_id;
			    });
			    
			    geoData.features.forEach(function(featureData) {
			        var linkID = featureData.properties.LINK_ID;
			        if (linkIDsToSelect.includes(linkID)) {
			            var feature = new ol.Feature({
			                geometry: new ol.geom.MultiLineString(featureData.geometry.coordinates)
			            });
			            featuresSelected.push(feature);
			        }
			    });
			    
			    var geojson_layer = new ol.layer.Vector({
			        name: 'geojson_layer',
			        source: new ol.source.Vector({
			            features: featuresSelected
			        }),
			        style : new ol.style.Style({
			            stroke: new ol.style.Stroke({ color: '#ff0000', width: 5 })
			        })
			    });
			    _map.addLayer(geojson_layer);
		    },
		    error : function(request, status, error) { // 결과 에러 콜백함수
		        console.log(error)
		    }
		})
	}