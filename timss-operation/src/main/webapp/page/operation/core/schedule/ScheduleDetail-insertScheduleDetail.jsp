<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>

<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<title>排班日历生成</title>
<script>_useLoadingMask = true;</script>
<script src='${basePath}js/operation/common/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/operation/common/commonForm.js?ver=${iVersion}'></script>
<script src='${basePath}js/operation/common/map.js?ver=${iVersion}'></script>


<script>
//根据comboArray初始化表格列数据
var columnsArray;
//值别数
var dutyCount;
var dutyString;
var dutyArray;

var uuid;

//岗位Json 需要引入map.js
var stationMap = new Map();

//岗位map
function stationJson(stationId){
	var url = basePath + "operation/duty/queryStationInfoBySitId.do";
	$.ajax({
		url : url,
		type : 'post',
		dataType : "json",
		success : function(data) {
			var stationList = data.result;
			for( var index in stationList ){
				stationMap.put( stationList[index].roleId, stationList[index].name );
			}
			
			$("#autoform").iForm("setVal",{"stationName" : stationMap.get( stationId ) });
		}
	});
}

function getValidDate(){
	var curDate = new Date();
	var newDate=new Date(curDate.setDate(curDate.getDate()+2));
	return newDate.format("yyyy-MM-dd");
}

	var fields = [
				{title : "uuid", id : "uuid", rules : {required:true},type:"hidden"},
	  			{title : "名称", id : "name",rules : {required:true}, linebreak:true,type:"label"},
	  			{title : "工种", id : "stationId",rules : {required:true}, linebreak:true,type:"hidden" },
	  			{title : "工种", id : "stationName",rules : {required:true}, linebreak:true,type:"label"},
	  			{title : "开始时间", id : "startTime",
	  				rules : {
	  					required:true,
	  					greaterEqualThan:getValidDate(),//时间校验，只能取后天及之后的日期避免交接班出错
	  				}, linebreak:true,
	  				type : "date",
	  				options : {
	  					startDate : '+2d'//new Date().format("yyyy-MM-dd")
	  				}
	  			},
	  			{title : "结束时间", id : "endTime",
	  				rules : {
	  					required:true,
	  					greaterEqualThan:"#f_startTime"
	  				}, linebreak:true,
	  				type : "date",
	  				options : {
	  					startDate : '+2d'//new Date().format("yyyy-MM-dd")
	  				}
	  			},
	  			{title : "起始行", id : "startFlag",rules : {required:true,digits:true}, linebreak:true}
	  		];
	//重置插入表单
	function resetForm(){
		var data = {
				"uuid" : "",
				"name" : "",
				"stationId" : "",
				"startTime" : "",
				"endTime" : "",
			};
			$("#autoform").ITC_Form("loaddata",data);
	}
	
	//初始化表单
	function initForm( rowData ){
		var data = {
			"uuid" : rowData.uuid,
			"name" : rowData.name,
			"stationId" : rowData.stationId
		};
		$("#autoform").iForm("setVal",data);
	}
	

	//formatter 单元格
	function formatterData( val ){
		return val > 0 ? comboArray[val] : "" ;
	}
	
	//设置datagrid数据
	function setDataGrid( uuid ){
		$("#detail").html("");
		var url = basePath + "operation/rulesDetail/queryRulesDetailByUuid.do?uuid=" + uuid;
		$("#detail").html('<table id="inseartRulesDetailTable"></table>');
	 	rowIndexFlag = -1;
		$("#inseartRulesDetailTable").datagrid({
			"pagination" : false,
			"fitColumns" : true,
			"singleSelect" : true,
			"url" : url,
			"scrollbarSize" : 0,
			"columns" : [ columnsArray ],
			"onSelect": function(rowIndex, rowData){
				$("#autoform").iForm("setVal",{"startFlag" : ( rowIndex + 1 ) });
			}
		});
	}
	
	//设置班次内容,包括表单里的班次和comboArray
	function setShifts(shifts){
		var s="";
		comboArray='{"0":"请选择",';
		for(var i=0;i<shifts.length;i++){
			s+=shifts[i].name+",";
			comboArray+='"'+shifts[i].id+'":"'+shifts[i].name+'",';
		}
		s=s.substr( 0, s.length - 1 );
		
		comboArray=comboArray.substr( 0, comboArray.length - 1 );
		comboArray+="}";
		comboArray=JSON.parse( comboArray );
		
	}
	
	//渲染值班表的表格 dCount = dutyCount
	function initRulesDetailTable( dCount ){
		var columnsArray = new Array();
		
		columnsArray.push({
			field:'dayTime',title:'天次',align:'center',width : 22
		});
		
		for( var i = 1; i <= dCount; i++ ){
				columnsArray.push({
					field:'field' + i,title: dutyArray[i-1],align:'center',width : 100,formatter: function(val,row){
			    		return formatterData( val );
			    	}
				});
		}
		
		return columnsArray;
	}
	
	//通过uuid拿到岗位 dCount = dutyCount
	function getStationIdByUuid(uuid){
		$.ajax({
			url : basePath + "operation/rulesDetail/queryStationIdByUuid.do?uuid=" + uuid,
			type : 'post',
			dataType : "json",
			success : function(data) {
				stationId = data.stationId;
				
				//设置班次
				getShiftByStationId( stationId );
			}
		});
	}
	
	//岗位变化事件处理
	function getShiftByStationId(stationId){
		$.ajax({
			url : basePath + "operation/shift/queryShiftByStationId.do?stationId="+stationId,
			type : 'post',
			dataType : "json",
			success : function(data) {
				if( data.total > 0 ){
					//设置comboArray
					setShifts(data.rows);
				}else{
					setShifts("");
				}
				//根据comboArray初始化表格列数据
				columnsArray = initRulesDetailTable( dutyCount );
				
				//设置datagrid数据
				setDataGrid( uuid );
			}
		});
	}
	
	$(document).ready(function() {
		$("#autoform").iForm("init",{"options":{validate:true,fixLabelWidth:true},fields:fields});
		var rowData = FW.get("operationRowData");//'${param.rowData}';
		//rowData = JSON.parse(decodeURI( rowData ) );
		
		dutyString = rowData.dutyString;
		dutyArray=(dutyString==null||dutyString=="")?[]:dutyString.split(",");
		
		initForm( rowData );
		
		stationJson( rowData.stationId );
		
		uuid = rowData.uuid;
		dutyCount = rowData.dutyCount;
		
		//设置岗位
		getStationIdByUuid( uuid );
		
		var inUrl = "${basePath}operation/scheduleDetail/insertScheduleDetail.do?";
		
		$( "#saveButton" ).click(function(){
			if(!$("#autoform").valid()){
				return;
			}
			var formData = getFormData( "autoform" );
			var url = inUrl + "formDataDrop=1";
			$.ajax({
				url : url,
				type : 'post',
				dataType : "json",
				data:{"formData":formData},
				success : function(data) {
					//是否存在数据
					if(data.isDateValid==false){//日期是否合法
						FW.error( "选择的日期非法！");
					}else if( data.flag == true ){
						FW.confirm( "有部分日历已经生成，确定要删除覆盖？", function(){
							inUrl += "&deal=1&";
							$( "#saveButton" ).click();
						}) ;
					}else{
						if( data.result == "success" ){
							FW.success( "新增日历成功 ！");
						}else{
							FW.error( "新增日历失败 ！");
						}
					}
				}
			});
		});
		
		
	});
		
</script>

</head>
<body>
	<div id="content">
		<div class="bbox toolbar-with-pager" id="toolbar_wrap">
			<div id="toolbar" class="btn-toolbar ">
				<div class="btn-group btn-group-sm">
					<button type="button" class="btn btn-default" onclick="closeTab()">关闭</button>
				</div>
				<div class="btn-group btn-group-sm">
					<button type="button" class="btn btn-success" id="saveButton">保存</button>
				</div>
			</div>
		</div>
	<div class="inner-title">
		新建日历
	</div>
	<form id="autoform" class="autoform">
	</form>
	 <div id="tableRulesWrap">
			<table id="inseartRulesDetailTable"  class="eu-datagrid"></table>
	</div>
	</div>
</body>
</html>