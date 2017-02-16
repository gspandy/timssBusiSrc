<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>生成排班规则</title>
<script>_useLoadingMask = true;</script>

<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src='${basePath}js/operation/common/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/operation/common/commonForm.js?ver=${iVersion}'></script>
<script src='${basePath}js/operation/common/stationCommon.js?ver=${iVersion}'></script>
<style>
	#detail .itc_object_wrap{
		width:99% !important
	}
</style>
<script>
	var fields = [
				{title : "编码", id : "num", type:"hidden"},
	  			{title : "名称", id : "name",rules : {required:true}},
	  			{title : "天次总数", id : "period",rules : {required:true}},
	  			{title : "工种", id : "stationId",rules : {required:true}, 
	  				  type : "combobox",
	                   options : {
	                	  onChange : function(val){
	                		  onStationChanged(val);
	                	  }
	                  } 
	  			},
	  			{title : "值别总数", id : "dutyCount",rules : {required:true},type : "label"},
	  			{title : "值别", id : "dutyString",rules : {required:true},type:"label"},
	  			{title : "排班班次", id : "classesList",rules : {required:true},type : "label"},
	  		    /*  {
	  		        title : "行政班", 
	  		        id : "isXzb",
	  		        type : "radio",
	  		        data : [
	  		            ['Y','有'],
	  		            ['N','无',true]
	  		        ]
	  		    }, */
	  			{title : "行政班数", id : "xzCount" ,type:"hidden" },
	  			{title : "替换日期(每月)", id : "changeLimit", linebreak:true ,type:"hidden" },
	  			{title : "轮询班次规则", id : "pollSequence" ,type:"hidden" }
	  		];
	
	var opts={
			validate:true,
			fixLabelWidth:true
		};
	  		
	
	//formatter 单元格
	function formatterData( val ){
		return val > 0 ? comboArray[val] : "" ;
	}
	
	
	//渲染值班表的表格
	function initRulesDetailTable( dutyCount ){
		
		var columnsArray = new Array();
		
		columnsArray.push({
			field:'dayTime',title:'天次',align:'center',width : 50,fixed:true
		});
		
		for( var i = 1; i <= dutyCount; i++ ){
				columnsArray.push({
					field:'field' + i,title: dutyArray[i-1].name,align:'center',width : 100, editor : {
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


	 //渲染inseartRulesDetailTable天次
	function initDayTime( dayTimeCount ) {
		//var dayTimeCount = rowData.period * rowData.dutyCount;
		var dataArray = new Array();
		for (var i = 1; i <= dayTimeCount; i++) {
			dataArray.push({
				"dayTime" : i,
				//"field1": "1"
			});
		}
		return dataArray;

	}
	 
	 //渲染inseartRulesDetailTable数据( sortArr 抽取序列-智能排班顺序，arrayList 第一列值，dutyCount 值别总数 )
	function initInseartRulesDetailTableDatagrid( sortArr, arrayList, dutyCount ) {
		var dayTimeCount = arrayList.length;
		
		var dataArray = new Array();
		for (var i = 1; i <= dayTimeCount; i++) {
			//拼接每行数据
			var obj = '{"dayTime":"' + i + '",';
			for( var j = 0; j < dutyCount ; j ++ ){
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
	 
	 //拿到datagrid所有数据 id: datagrid的id  rowCount:datagrid行数
	 function getAllRows( id , rowCount ){
		 for(var j = 0; j < rowCount; j++ ){
				$( "#" + id ).datagrid('endEdit', j);
			}
			var row = $("#" + id ).datagrid("getRows");
			return row;
	 }
	 
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
	//岗位
	var stationVal;
	
	//渲染
	 function createScheduleDatagrid(){
			var dataRow = dataArray.slice();
		 	$("#detail").html('<table id="inseartRulesDetailTable"></table>');
		 	rowIndexFlag = -1;
			$("#inseartRulesDetailTable").datagrid({
				"pagination" : false,
				"fitColumns" : true,
				"singleSelect":true,
				"data" : dataRow,
				"scrollbarSize":0,
				"columns" : [ columnsArray ],
				"onClickCell":function(rowIndex, field, value){
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
	
	//create datagrid
	function createDatagrid( stationId ){
		var formData = $("#autoform").iForm("getVal");
		//值别总数
		dutyCount = formData.dutyCount;
		//天次
		dayTimeCount = formData.period;
		
		//初始化天次
		dataArray = initDayTime( dayTimeCount );
		
		getShiftByStationId( stationId );
	}
	
	//岗位改变
	function onStationChanged(stationId){
		//stationVal 岗位全局变量
		stationVal = stationId;
		if(stationId== null || stationId == ""){
			setShifts("");
			//columnsArray = initRulesDetailTable( dutyCount );
			//createScheduleDatagrid();
			return;
		}
		$.ajax({
			url : basePath + "operation/duty/queryDutyByStationId.do?stationId="+stationId,
			type : 'post',
			dataType : "json",
			success : function(data) {
				//getShiftByStationId(stationId);
				var isXzb = 'N';// $("#autoform").iForm("getVal").isXzb;
				var xzCount = $("#autoform").iForm("getVal").xzCount;
				
				if( isXzb == 'N' ){
					dutyCount = data.total;
				}else if( isXzb == 'Y' ){
					if( xzCount != null || xzCount != "" ){
						//值别数 - 行政数
						dutyCount =  data.total - xzCount;
					}
				}
				dutyArray=data.rows;
				if(dutyArray.length>0){
					dutyString=dutyArray[0].name;
					for(var i=1;i<dutyArray.length;i++){
						dutyString+=","+dutyArray[i].name;
					}
				}else{
					dutyString="";
				}
				
				$("#autoform").iForm("setVal",{"dutyCount" : dutyCount,"dutyString":dutyString });
				
				//初始化datagrid
				createDatagrid( stationId );
				
			}
		});
	}
	
	//设置班次内容,包括表单里的班次和comboArray
	function setShifts(shifts){
		if(shifts==""){
		comboArray={};
		$("#autoform").iForm("setVal",{"classesList" : ""});
		return;
		}
		var s="";
		comboArray='{"0":"请选择",';
		for(var i=0;i<shifts.length;i++){
			s+=shifts[i].name+",";
			comboArray+='"'+shifts[i].id+'":"'+shifts[i].name+'",';
		}
		s=s.substr( 0, s.length - 1 );
		$("#autoform").iForm("setVal",{"classesList" : s});
		
		comboArray=comboArray.substr( 0, comboArray.length - 1 );
		comboArray+="}";
		comboArray=JSON.parse( comboArray );
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
				//渲染排班表格，其中用到了列数据columnsArray和表格数据dataArray
				createScheduleDatagrid();
			}
		});
	}
	 
	$(document).ready(function() {
		$("#autoform").iForm("init",{"options":opts,"fields":fields});
		//初始化 岗位
		stationOption( basePath, 'f_stationId',null,null,onStationChanged);
		$("#f_period").val(10);
		
		$("#f_period").change(function(){
			createDatagrid( stationVal );
		});
		
		//提交排班信息
		$("#saveButton").click(function() {
			if(!$("#autoform").valid()){
				return;
			}
			var formData = getFormData("autoform");
			
			var row = getAllRows("inseartRulesDetailTable", dayTimeCount );
			
			var url = basePath + "operation/rulesDetail/batchInsertRulesDetail.do";
			$.ajax({
				url : url,
				type : 'post',
				dataType : "json",
				data :{
					row :  JSON.stringify(row) ,
					formData :  formData 
				},
				success : function(data) {
					if( data.result == "success" ){
						FW.success( "新增排班规则成功 ！");
						closeTab();
					}else{
						FW.error( "新增排班规则失败 ！");
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
			
			//dataRow = JSON.stringify( dataRow ) ;
			
			//从新渲染datagrid
			$("#inseartRulesDetailTable").datagrid({
				"pagination" : false,
				"fitColumns" : true,
				"singleSelect":true,
				"scrollbarSize":0,
				"data" : dataRow,
				"columns" : [ columnsArray ],
				"onClickCell":function(rowIndex, field, value){
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
		
	});
</script>

</head>
<body>
	<div id="content">
		<div class="bbox toolbar-with-pager" id="toolbar_wrap">
			<div id="toolbar" class="btn-toolbar">
				<div class="btn-group btn-group-sm">
					<button type="button" class="btn btn-default" onclick="closeTab()">关闭</button>
				</div>
				<div class="btn-group btn-group-sm">
					<button type="button" class="btn btn-success" id="saveButton">保存</button>
				</div>
						<select id="sortRule" style="width: 180px;float: right;" class="itcui_combo bbox">
							<option value="0">智能排班，请选择</option>
							<option value="0,2,4,6,8">1,3,5,7,9</option>
							<option value="0,8,6,4,2">1,9,7,5,3</option>
							<option value="0,1,2,3,4">1,2,3,4,5</option>
							<option value="0,9,8,7,6">1,10,9,8,7</option>
							<option value="0,4,8,2,6">1,5,9,3,7</option>
							<option value="0,4,2,3,1">1,5,3,4,2</option>
							<option value="0,14,7,28,21,35">1,15,8,29,22,36</option>
							<option value="0,1,2,3,4,5,6,7,8">1,2,3,4,5,6,7,8,9</option>
						</select>
			</div>
		</div>
		<div class="inner-title">
			新建排班规则
		</div>
		<form id="autoform"></form>
		
		<div class="itcui_frm_grp_title" style="width:100%;clear:both;">
			<span class="itcui_frm_grp_title_txt">从排班起始日期开始的一个周期的排班</span>	
		</div>
		<!-- 生成排班表  -->
		<div id="detail">
			<table id="inseartRulesDetailTable"></table>
		</div>
	</div>
</body>
</html>