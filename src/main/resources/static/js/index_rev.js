
	/*----------- 전역변수 -----------*/
	var idCnt = 0;
	var passengerCnt = 1;
	var txtfocus;
	
	/*----------- onload 이벤트 -----------*/
	window.onload = function(){
		init();
	}
	
	/*----------- 초기화 -----------*/
	function init(){
		setTime();
		document.getElementById('beg0').onfocus = function() { txtfocus = this; };
		document.getElementById('end0').onfocus = function() { txtfocus = this; };
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
	
	/*----------- 추가 버튼 이벤트 -----------*/
	function fnAddBtn(){
    	idCnt++;
    	passengerCnt++;

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

        $('.test-area table').append(newTable);
        setTime();
        
        document.getElementById('beg'+idCnt).onfocus = function() { txtfocus = this; };
        document.getElementById('end'+idCnt).onfocus = function() { txtfocus = this; };
	};
	
	/*----------- 포커스 맞춰진 곳에 setValue -----------*/
	document.addEventListener('DOMContentLoaded', function() {
	    var rows = document.querySelectorAll('.sttn-area table tr');

	    rows.forEach(function(row) {
	        row.addEventListener('mouseover', function() {
	            row.style.backgroundColor = '#ffc';
	        });

	        row.addEventListener('mouseout', function() {
	            row.style.backgroundColor = '';
	        });
	    });

	    // sttn-area 클릭 시 이벤트
	    var listRows = document.querySelectorAll('.sttn-area table tr');
	    listRows.forEach(function(row, index) {
	        row.addEventListener('click', function() {
	            var cells = row.getElementsByTagName('td');
	            var sttnNM = cells[1].innerText; 
	            var sttnSEQ = Number(cells[0].innerText); 

	            if (txtfocus) {
	                txtfocus.value = sttnNM; 
	                txtfocus.setAttribute('data-sttn-seq', sttnSEQ); 
	            }
	        });
	    });
	});
	
	/*----------- 제출 -----------*/
	function fnClickBtn(){
		var begDataSttnSeq = [];
		var endDataSttnSeq = [];
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
		    var hhVal = hhElement.value;
		    var endVal = mmElement.value;
		    
		    begDataSttnSeq.push(begDataSeq);
		    endDataSttnSeq.push(endDataSeq);
		    hhList.push(hhVal);
		    mmList.push(endVal);
		}
		var data = {
				"userCnt": idCnt,
				"begDataSttnSeq": begDataSttnSeq,
				"endDataSttnSeq": endDataSttnSeq,
				"hhList": hhList,
				"mmList": mmList
		};
		exeAjax(data, "submitSttn");
	};
	
	function exeAjax(req, url) {
	    $.ajax({
	        url : url,
	        type : "post",
	        contentType: 'application/json',
	        data: JSON.stringify(req), 
	        success:function(data){
	            console.log("성공");
	        }
	    })
	}
	
	

