<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>编辑排班规则</title>
<script>_useLoadingMask = true;</script>

<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src='${basePath}js/operation/common/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/operation/common/commonForm.js?ver=${iVersion}'></script>
<script src='${basePath}js/operation/common/stationCommon.js?ver=${iVersion}'></script>
<script src='${basePath}js/operation/common/map.js?ver=${iVersion}'></script>
<style>
	#detail .itc_object_wrap{
		width:99% !important
	}
</style>
<script>

//行标记
var rowIndexFlag;
//值别总数
var dutyCount;
//值别
var dutyString;
//值别列表
var dutyArray;
//初始化inseartRulesDetailTable列数
var columnsArray;
//天次总数
var dayTimeCount;
//inseartRulesDetailTable 初始化天次列
var dataArray;
//班次 liwei做好换成动态
var comboArray;
//stationId
var stationId;
// uuid
var uuid;
//是否编辑模式
var isEdit=false;
//岗位Json 需要引入map.js
var stationMap = new Map();

//岗位map
function stationJson(){
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
			//设置岗位,由于getStationIdByUuid要用到stationMap，所以要ajax返回时再调用
			getStationIdByUuid( uuid );
		}
	});
}


var fields = [
				{title : "ID", id : "id",rules : {required:true},type: "hidden"},
				{title : "编号", id : "num",type: "hidden"},
	  			{title : "名称", id : "name",rules : {required:true}},
	  			{title : "值别总数", id : "dutyCount",rules : {required:true},type:"label"},
	  			{title : "值别", id : "dutyString",rules : {required:true},type:"label"},
	  			{title : "天次总数", id : "period",rules : {required:true},type:"label"},
	  			{title : "替换日期(每月)", id : "changeLimit",type:"hidden"},
	  			{title : "轮询班次规则", id : "pollSequence",type:"hidden"},
	  			{title : "排班班次", id : "classesList",rules : {required:true},type:"label"},
	  			{title : "工种", id : "stationId",rules : {required:true},type:"hidden" },
	  			{title : "工种", id : "stationName" ,type:"label"},
	  		   /*  {
	  		        title : "行政班", 
	  		        id : "isXzb",
	  		        type : "radio",
	  		        data : [
	  		            ['Y','有'],
	  		            ['N','无',true]
	  		        ],type:"hidden"
	  		    }, */
	  			{title : "行政班数", id : "xzCount",type:"hidden"}
	  			/* {title : "规则详情名称", id : "rulesDetailName",linebreak:true,rules : {required:true}}, */
	  		];
	
	var opts={
			validate:true,
			fixLabelWidth:true
		};
	
	
	//formatter 单元格
	function formatterData( val ){
		return val > 0 ? comboArray[val] : "" ;
	}
	
	//初始化表单
	function initForm( rowData ){
		var data = {
				"id" : rowData.id,
				"num" : rowData.num,
				"name" : rowData.name,
				"dutyCount" : dutyCount,
				"dutyString":dutyString,
				"period" : rowData.period,
				"changeLimit" : rowData.changeLimit,
				"pollSequence" : rowData.pollSequence,
				"available": rowData.available,
				"isXzb" : rowData.isXzb,
				"xzCount" : rowData.xzCount
				//"classesList" : "A,B,C,D,E"
			};
			$("#autoform").iForm("setVal",data);
			dayTimeCount=rowData.period;
	}
	
	
	//通过ruleId获取行列规则的数据，然后加载到form中
	function setForm( rulesId ){
		$.ajax({
			url : basePath + "operation/rules/queryRulesById.do?rulesId=" + rulesId,
			type : 'post',
			dataType : "json",
			success : function(data) {
				var result = data.result;
				if( result != null && result != "" ){
					initForm( result );
				}
			}
		});
	}
	
	//设置班次内容,包括表单里的班次和comboArray
	function setShifts(shifts){
		if(shifts == ""){
			comboArray = {};
			$("#autoform").iForm("setVal",{"classesList" : ""});
			return;
		}
		var s = "";
		comboArray = '{"0":"请选择",';
		for(var i = 0 ; i < shifts.length ; i++){
			s += shifts[i].name + ",";
			comboArray += '"' + shifts[i].id + '":"' + shifts[i].name + '",';
		}
		s=s.substr( 0, s.length - 1 );
		$("#autoform").iForm("setVal",{"classesList" : s});
		
		comboArray = comboArray.substr( 0, comboArray.length - 1 );
		comboArray += "}";
		comboArray = JSON.parse( comboArray );
		
	}
	
	//岗位变化时，调用此方法传入新的岗位id
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
				$("#autoform").iForm("endEdit");
				//根据comboArray初始化表格列数据
				columnsArray = initRulesDetailTable( dutyCount );
				//渲染排班表格，其中用到了列数据columnsArray和表格数据dataArray
				//createScheduleDatagrid();
				
				//设置datagrid数据
				setDataGrid( uuid );
			}
		});
	}
	
	//渲染值班表的表格 dCount = dutyCount
	function initRulesDetailTable( dCount ){
		var columnsArray = new Array();
		
		columnsArray.push({
			field:'dayTime',title:'天次',align:'center',width : 50,fixed:true
		});
		
		for( var i = 1; i <= dCount; i++ ){
			columnsArray.push({
				field:'field' + i,title: dutyArray[i-1],align:'center',width : 100, editor : {
			        type:"combobox",
			        options : {
			            data : comboArray
			        }
		    	},formatter: function(val,row){
		    		return formatterData( val );
		    	}
			});
		}
		
		return columnsArray;
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
			"singleSelect":true,
			"url" : url,
			"scrollbarSize":0,
			"columns" : [ columnsArray ],
			"onClickCell":function(rowIndex, field, value){
				if(!isEdit){
					return;
				}
				//开启编辑
				$("#inseartRulesDetailTable").datagrid('beginEdit', rowIndex);
				
				//关掉另外一行endEdit
				if( rowIndexFlag != rowIndex ){
					if( rowIndexFlag != -1 ){
						$("#inseartRulesDetailTable").datagrid('endEdit', rowIndexFlag);
					}
					rowIndexFlag = rowIndex;
				}
			}
		});
	}
	
	//通过uuid拿到岗位
	function getStationIdByUuid(uuid){
		$.ajax({
			url : basePath + "operation/rulesDetail/queryStationIdByUuid.do?uuid=" + uuid,
			type : 'post',
			dataType : "json",
			success : function(data) {
				stationId = data.stationId;
				var stationName = stationMap.get(stationId);
				$("#autoform").iForm("setVal",{"stationName" : stationName });
				$("#autoform").iForm("setVal",{"stationId" : stationId });
				
				//设置班次
				getShiftByStationId( stationId );

			}
		});
	}
	
	 //拿到datagrid所有数据 id: datagrid的id  rowCount:datagrid行数
	 function getAllRows( id , rowCount ){
		 for(var j = 0; j < rowCount; j++ ){
				$( "#" + id ).datagrid('endEdit', j);
			}
			var row = $("#" + id ).datagrid("getRows");
			return row;
	 }
	
	 //渲染inseartRulesDetailTable数据( sortArr 抽取序列-智能排班顺序，arrayList 第一列值，dutyCount 值别总数 )
	function initInseartRulesDetailTableDatagrid( sortArr, arrayList, dCount ) {
		var dayTimeCount = arrayList.length;
		
		var dataArray = new Array();
		for (var i = 1; i <= dayTimeCount; i++) {
			//拼接每行数据
			var obj = '{"dayTime":"' + i + '",';
			for( var j = 0; j < dCount ; j ++ ){
				//循环
				var index = ( i - 1 + parseInt( sortArr[j] ) ) % dayTimeCount;
				var fieldVal = arrayList[ index ];
				obj += '"field' + (j+1) +  '":"' + fieldVal + '",';
			}
			obj = obj.substr( 0, obj.length - 1 );
			obj += '}';
			
			dataArray.push( JSON.parse( obj ) );
		}
		return dataArray;

	}
	
	$(document).ready(function() {
		$("#autoform").iForm("init",{"options":opts,"fields":fields});
		var rowData = FW.get("operationRowData");//'${param.rowData}';
		//rowData = JSON.parse(decodeURI( rowData ) );
		var rulesId = rowData.rulesId;
		uuid = rowData.uuid;
		dutyCount = rowData.dutyCount;
		dutyString = rowData.dutyString;
		dutyArray=(dutyString==null||dutyString=="")?[]:dutyString.split(",");
		stationJson();
		changeTitle();
		//设置form
		setForm( rulesId );
		
		$("#sortRule").hide();
		$("#editButton").click(function() {
			$("#autoform").iForm("beginEdit");
			isEdit=true;
			$("#editButton").hide();
			$("#sortRule").show();
			$("#saveButton,#cancelButton").show();
			changeTitle("edit");
		});
		
		$( "#cancelButton" ).click(function(){
			$("#editButton").show();
			$("#saveButton,#cancelButton").hide();
			$("#sortRule").hide();
			$("#autoform").iForm("endEdit");
			isEdit=false;
			changeTitle();
			stationJson();
			setForm( rulesId );
		});
		
		//保存按钮注册单击事件  提交排班信息
		$("#saveButton").click(function() {
			var formData = getFormData("autoform");
			var row = getAllRows("inseartRulesDetailTable", dayTimeCount );
			var url = basePath + "operation/rulesDetail/updateRulesDetail.do";
			$.ajax({
				url : url,
				type : 'post',
				dataType : "json",
				data:{
					row :  JSON.stringify(row) ,
					formData:  formData ,
					uuid : uuid
				},
				success : function(data) {
					if( data.result == "success" ){
						$("#editButton").show();
						$("#saveButton,#cancelButton").hide();
						$("#sortRule").hide();
						$("#autoform").iForm("endEdit");
						isEdit=false;
						FW.success( "更新排班规则成功 ！");
						changeTitle();
						uuid=data.result.uuid;
						closeTab();//数据从列表获取的问题导致更改后依然是旧数据，因此关闭页面从列表刷新
					}else{
						FW.error( "更新排班规则失败 ！");
					}
					
				}
			});

		});

		//智能排班
		$("#sortRule").change(function(){
			//序列值
			var val = $(this).val();
			var sortArr= new Array();   
			sortArr = val.split(","); 
			
			//dayTimeCount datagrid总行数
			var row = getAllRows( "inseartRulesDetailTable", dayTimeCount );
			
			var arrayList = new Array();
			
			//第一列值
			for( var index in row ){
				arrayList[index] =  row[index].field1; 
			}
			
			var dataRow = initInseartRulesDetailTableDatagrid( sortArr, arrayList, dutyCount );
			
			//从新渲染datagrid
			$("#inseartRulesDetailTable").datagrid({
				"pagination" : false,
				"fitColumns" : true,
				"singleSelect":true,
				"scrollbarSize":0,
				"url" : null,
				"data" : {rows:dataRow,total:dataRow.length},
				"columns" : [ columnsArray ],
				"onClickCell":function(rowIndex, field, value){
					if(!isEdit){
						return;
					}
					//开启编辑
					$("#inseartRulesDetailTable").datagrid('beginEdit', rowIndex);
					
					//关掉另外一行endEdit
					if( rowIndexFlag != rowIndex ){
						if( rowIndexFlag != -1 ){
							$("#inseartRulesDetailTable").datagrid('endEdit', rowIndexFlag);
						}
						rowIndexFlag = rowIndex;
					}
				}
			});
			
		});
		
		//生成日历
		$("#createRulesDetail").click(function(){
			var pageUrl = "${basePath}page/operation/core/schedule/ScheduleDetail-insertScheduleDetail.jsp?rowData=" + '${param.rowData}';
			addTabWithTree("rulesDetail" + rowData.id , "生成排班日历", pageUrl,FW.getCurrentTabId());
		});
		
		//删除
		$("#deleteButton").click(function(){
			FW.confirm("确定删除本条数据吗？",function(){
				var url = basePath + "operation/rulesDetail/deleteRulesDetailByUuid.do?id=" + rulesId + "&uuid=" + uuid;
				$.ajax({
					url : url,
					type : 'post',
					dataType : "json",
					success : function(data) {
						if( data.result == "success" ){
						FW.success( "删除排班规则成功 ！");
						closeTab();
						}else{
						FW.error( "删除排班规则失败 ！");
						}
					
					}
				});
			});
		});
		
	});
	
		function changeTitle(type){
			var title=$(".inner-title");
			var name="排班规则";
			if(type=="add"){
				title.html("新建"+name);
			}else if(type=="edit"){
				title.html("编辑"+name);
			}else{
				title.html(name+"详情");
			}
		}
</script>

</head>
<body>
	<div id="content">
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
		<div id="toolbar" class="btn-toolbar">
			<div class="btn-group-sm" style="margin-top: 2px!important;">
					<button type="button" class="btn btn-default" onclick="closeTab()">关闭</button>
					<button type="button" class="btn-default btn" id="cancelButton" style="display: none;">取消</button>
					<button type="button" class="btn btn-default" id="editButton">编辑</button>
					<button type="button" class="btn btn-success" id="saveButton" style="display: none;">保存</button>
					<button type="button" class="btn btn-default" id="deleteButton">删除</button>
					<button type="button" class="btn btn-default" id="createRulesDetail">生成排班日历</button>
					<!-- <select id="sortRule" style="width: 180px;float: right;" class="itcui_combo bbox">
						<option value="0">智能排班，请选择</option>
						<option value="0,2,4,6,8">1,3,5,7,9</option>
						<option value="0,8,6,4,2">1,9,7,5,3</option>
						<option value="0,1,2,3,4">1,2,3,4,5</option>
						<option value="0,9,8,7,6">1,10,9,8,7</option>
						<option value="0,4,8,2,6">1,5,9,3,7</option>
						<option value="0,4,2,3,1">1,5,3,4,2</option>
						<option value="0,14,7,28,21,35">1,15,8,29,22,36</option>
						<option value="0,1,2,3,4,5,6,7,8">1,2,3,4,5,6,7,8,9</option>
					</select> -->
			</div>
		</div>
	</div>
		<div class="inner-title">
			编辑排班规则
		</div>
		<form id="autoform"></form>
		
		<div class="itcui_frm_grp_title" style="width:100%;clear:both;">
			<span class="itcui_frm_grp_title_txt">从排班起始日期开始的一个周期的排班</span>	
		</div>
		<!-- 生成排班表  -->
		<div id="detail">
			<div id="tableRulesWrap">
				<table id="inseartRulesDetailTable"></table>
			</div>
		</div>
	</div>
</body>
</html>