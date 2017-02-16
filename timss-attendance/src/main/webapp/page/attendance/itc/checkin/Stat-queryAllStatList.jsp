<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html  style="height: 99%;">
<head>
<title>统计列表</title>
<script>_useLoadingMask = true;</script>

<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src='${basePath}js/attendance/common/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/attendance/common/commonBtnSecurity.js?ver=${iVersion}'></script>
<script src='${basePath}js/attendance/common/commonDialog.js?ver=${iVersion}'></script>
<script src='${basePath}js/attendance/common/dataFormat.js?ver=${iVersion}'></script>
<style type="text/css">
.pointable-cell{
	cursor:pointer;
	text-decoration:underline
}
</style>
<script>
var roleFlag = '${ roleFlag }';
var userId = '${ userId }';
var deptId = '${ deptId }';
var siteId = '${ siteId }';

//核减年假
function setSubAnnualDays( subAnnualDays, remark ){

	var url = basePath + "attendance/stat/updateStatSubAnnual.do";
	$.ajax({
		url : url,
		type : 'post',
		dataType : "json",
		data :{
			subAnnualDays : subAnnualDays,
			remark : remark
		},
		success : function(data) {
			if( data.result == "success"){
				 FW.success("核减年假处理成功");
				 $("#contentTb").datagrid("reload");
			}else{
				FW.error( data.reason );
			}
		}
	});
}

//核减年假细对话框
var pri_dlgOpts = {
	width : 550,
	height : 150,
	closed : false,
	title : "核减年假",
	modal : true
};

//核减年假
function showDtlIframe(){
	var src = basePath + "page/attendance/core/checkin/StatItem-updateStatItem.jsp";
	
	var btnOpts = [{
		"name" : "取消",
		"float" : "right",
		"style" : "btn-default",
		"onclick" : function() {
			return true;
		}
	},{
		"name" : "确定",
		"float" : "right",
		"style" : "btn-success",
		"onclick" : function() {				
			var conWin = _parent().window.document.getElementById("itcDlgContent").contentWindow;
			if (!conWin.valid()) {
				return false;
			}
			var formdata = conWin.getFormData("statItemFrom");
			var subAnnualDays = JSON.parse(formdata).subAnnualDays;
			var remark = JSON.parse(formdata).remark1;
			setSubAnnualDays( subAnnualDays , remark);
			return true;
		}
	} ];

	FW.dialog("init", {
		"src" : src,
		"dlgOpts" : pri_dlgOpts,
		"btnOpts" : btnOpts
	});
}

//初始化form
function saveRemainForm( formData ){
	var url =  basePath + "attendance/stat/updateStatRemain.do";
	$.ajax({
		url : url,
		type : 'post',
		dataType : "json",
		data:{formData:formData},
		success : function(data) {
			if( data.result == "success"){
				FW.success("保存成功 ！");
				 $("#contentTb").datagrid("reload");
			}else{
				if( data.reason != null || data.reason != "" ){
					FW.error( data.reason );
				}else{
					FW.error( "加载结转信息出错！" );
				}
			}
		}
	});
}

//修改结转对话框
var remain_dlgOpts = {
	width : 550,
	height : 280,
	closed : false,
	title : "编辑结转信息",
	modal : true
};



//结转年假
function showRemainIframe( url ){
	var remiainBtnOpts = [{
		"name" : "取消",
		"float" : "right",
		"style" : "btn-default",
		"onclick" : function() {
			return true;
		}
	},{
		"name" : "确定",
		"float" : "right",
		"style" : "btn-success",
		"onclick" : function() {				
			var conWin = _parent().window.document.getElementById("itcDlgContent").contentWindow;
			if (!conWin.valid()) {
				return false;
			}
			
			var formdata = conWin.getFormData("statItemFrom");
			saveRemainForm( formdata );
			
			return true;
		}
	} ];

	FW.dialog("init", {
		"src" : url,
		"dlgOpts" : remain_dlgOpts,
		"btnOpts" : remiainBtnOpts
	});
}


//高级查询中DIV --- 查询按钮
function advSearch(){
	$(".itcui_btn_mag").first().click();
}

//报表显示列 siteId--站点  fYM--年月标志
function forRepNoShowColumn( siteId , fYM ){
	var hide = "";
	if( siteId == 'ZJW' && fYM == "m"){
		hide = "&__isnull=hide";
	}else if( fYM == "m" ){
		hide = "&hide=_9_10_11_12_13_14_15_16_17_";
	}else if( siteId == 'ZJW' && fYM == "y" ){
		hide = "&__isnull=hide";
	}else if( fYM == "y" ){
		hide = "&hide=_10_11_12_13_14_15_16_17_18_";
	}
	return hide;
}

//月报表
function monthReport(format){
	var month = getMonthVal();
	var deptName = $("input[field='deptName']").val();
	if( month==0 ){
		yearReport(format);
	}else{
		
		var year = getYearVal();
		
		var onStatus = getStatusVal();
		format = "excel"==format?"xls":"pdf";
		//非年报表
		var template = "TIMSS_ATD_MONTH_001";
		var url = fileExportPath + "preview?__report=report/"+template+".rptdesign&__format="+format+"&roleFlag="+roleFlag
		+ "&userId=" + userId + "&deptId=" + deptId + "&year=" + year + "&month=" + month + 
		"&siteId=" + siteId + "&onStatus=" + onStatus  + forRepNoShowColumn( siteId, "m") + "&deptName="+deptName
		+"&__asattachment="+("xls"==format?"true":"false");
		if("xls"==format){
			window.open(url);
		}else{
			FW.dialog("init",{src: url,
				btnOpts:[{
				"name" : "关闭",
				"float" : "right",
				"style" : "btn-default",
				"onclick" : function(){
				 _parent().$("#itcDlg").dialog("close");
				 }
				}
			    ],
				dlgOpts:{ width:1024, height:650, closed:false, title:"打印月度报表", modal:true }
			 });
		}
	}
}

//年报表
function yearReport(format){
	var year=getYearVal();
	var deptName = $("input[field='deptName']").val();
	var onStatus = getStatusVal();
	if(undefined == deptName){
		deptName = '';
	}
	var template = "TIMSS_ATD_YEAR_001";
	format = "excel"==format?"xls":"pdf";
	var url = fileExportPath + "preview?__report=report/"+template+".rptdesign&__format="+format+"&roleFlag="+roleFlag
			+ "&userId=" + userId + "&deptId=" + deptId + "&year=" + year + "&siteId=" + siteId + 
			"&onStatus=" + onStatus + forRepNoShowColumn( siteId, "y")+ "&deptName="+deptName
			+"&__asattachment="+("xls"==format?"true":"false");
	if("xls"==format){
		window.open(url);
	}else{
		FW.dialog("init",{src: url,
			btnOpts:[{
			"name" : "关闭",
			"float" : "right",
			"style" : "btn-default",
			"onclick" : function(){
			 _parent().$("#itcDlg").dialog("close");
			 }
			}
		    ],
			dlgOpts:{ width:1024, height:650, closed:false, title:"打印年度报表", modal:true }
		 });
	}
}

//通过filed转换成相应的名字
function fieldToTitleName( field ){
	var title = "";
	switch (field)
	{
	case "transferCompensate":
		  title="转补休";
		  break;
	case "enventLeave":
		  title="事假";
		  break;
	case "sickLeave":
		title="病假";
	  	break;
	case 'marryLeave':
		title="婚假";
	  	break;
	case 'birthLeave':
		title="产假";
	  	break;
	case 'otherLeave':
		title="其他假";
	  	break;
	case 'overTime':
		title="加班天数";
	 	break;
	case 'compensateLeave':
		title="已补休";
	 	break;
	case 'annualLevel':
		title="年休假";
	 	break;
	}
	//扩展
	if( title == "" ){
		var enumArr = FW.getEnumMap("ATD_LEI_CATEGORY");
		for( key in enumArr ){
			var codeArr = key.split( "_" );
			if( codeArr[ codeArr.length - 1 ] >= 8 ){
		        var category = "category_" + codeArr[ codeArr.length - 1 ];
		        if( category == field ){ 
		        	title = enumArr[key];
		        	break;
		        };
			}
		}
	}
	return title;
}

//显示明细
function showFieldIdDetail(rowIndex, field, value){
	if( value == 0 ){
		//FW.warn( "没有明细信息！");
		return;
	}
	
	var row = $("#contentTb").datagrid("getRows")[rowIndex];
	var userId =  row.userId;
	
	var year = getYearVal();
	var month = getMonthVal();
	
	var url = basePath + "attendance/stat/queryStatLeaveDetailToPage.do?userId=" + userId
			+ "&year=" + year + "&month=" + month
			+ "&field=" + field ;
	var title = fieldToTitleName( field );
	
	showDialogIframeOnlyCancel(550, 300, title + "明细", url , null );	
}

//冻结列
function conFrozenColumns(){
	var columnsArray = new Array();
	columnsArray.push({field:'id',title:'id',width:20,fixed:true,hidden:true});
	columnsArray.push({field:'userName',title:'姓名',width:60,fixed:true, sortable: true});
	columnsArray.push({field:'deptName',title:'部门',width:100,fixed:true, sortable: true});
	columnsArray.push({field:'userStatus',title:'状态',width:80, sortable: true,editor:{
		"type" : "combobox",
		 "options" : {
			 data:[["在职","在职",true],["离职","离职"]]
		 }
	}});
	
	columnsArray.push({field:'annualRemain',title:'结转年假',width:60,fixed:true, sortable: true,formatter:function(value,row,index){
		return constructLeaveColStyle(this,value,row,index);
	}});
	columnsArray.push({field:'annual',title:'可享年假',width:60,fixed:true, sortable: true,formatter:function(value,row,index){
		return constructLeaveColStyle(this,value,row,index);
	}});
	columnsArray.push({field:'subAnualLeave',title:'核减年假',width:60,fixed:true, sortable: true,formatter:function(value,row,index){
		return constructLeaveColStyle(this,value,row,index);
	}});
	columnsArray.push({field:'surplusAnnual',title:'剩余年假',width:60,fixed:true, sortable: true,formatter:function(value,row,index){
		return constructLeaveColStyle(this,value,row,index);
	}});
	
	columnsArray.push({field:'compensateRemain',title:'结转补休',width:60,fixed:true, sortable: true,formatter:function(value,row,index){
		return constructLeaveColStyle(this,value,row,index);
	}});
	columnsArray.push({field:'overTime',title:'加班天数',width:60,fixed:true, sortable: true,formatter:function(value,row,index){
		return constructLeaveColStyle(this,value,row,index);
	}});
	columnsArray.push({field:'transferCompensate',title:'转补休',width:60,fixed:true, sortable: true,formatter:function(value,row,index){
		return constructLeaveColStyle(this,value,row,index);
	}});
	columnsArray.push({field:'surplusCompensate',title:'未补休',width:60,fixed:true, sortable: true,formatter:function(value,row,index){
		return constructLeaveColStyle(this,value,row,index);
	}});
	
	columnsArray.push({field:'countDays',title:'请假合计',width:60,fixed:true, sortable: true,formatter:function(value,row,index){
		return constructLeaveColStyle(this,value,row,index);
	}});
	
	return columnsArray;
}

var fields = ",annualLevel,enventLeave,sickLeave,marryLeave,birthLeave,otherLeave,compensateLeave,overTime,transferCompensate,";
function constructLeaveColStyle(col,original,row,index,rtnVal){//请假详情可点击
	if(rtnVal!==0&&!rtnVal){
		rtnVal=original;
	}
	rtnVal=AtdDataFormat.formatDoubleDays(rtnVal);
	//哪些单元格有明细
	var field=col.field;
   	var fieldStr = "," + field + "," ;
   	var onclick="";
	if ( fields.indexOf( fieldStr ) >= 0 || field.indexOf( "category_" ) >=0 ) {
		onclick+="class='pointable-cell' onclick='showFieldIdDetail("+index+", \""+field+"\", "+rtnVal+");'";
	}
	return rtnVal>0?("<span "+onclick+">"+rtnVal+"</span>"):rtnVal;
}

//构造显示列
function constructColArr(){
	var columnsArray = new Array();
	columnsArray.push({field:'annualLevel',title:'年休假',width:50,fixed:true, sortable: true,formatter:function(value,row,index){
		return constructLeaveColStyle(this,value,row,index);
	}});
	columnsArray.push({field:'compensateLeave',title:'补休假',width:50,fixed:true, sortable: true,formatter:function(value,row,index){
		return constructLeaveColStyle(this,value,row,index);
	}});
	columnsArray.push({field:'enventLeave',title:'事假',width:50,fixed:true, sortable: true,formatter:function(value,row,index){
		return constructLeaveColStyle(this,value,row,index);
	}});
	columnsArray.push({field:'sickLeave',title:'病假',width:50,fixed:true, sortable: true,formatter:function(value,row,index){
		return constructLeaveColStyle(this,value,row,index);
	}});
	columnsArray.push({field:'marryLeave',title:'婚假',width:50,fixed:true, sortable: true,formatter:function(value,row,index){
		return constructLeaveColStyle(this,value,row,index);
	}});
	columnsArray.push({field:'birthLeave',title:'产假',width:50,fixed:true, sortable: true,formatter:function(value,row,index){
		return constructLeaveColStyle(this,value,row,index);
	}});
	columnsArray.push({field:'otherLeave',title:'其他',width:50,fixed:true, sortable: true,formatter:function(value,row,index){
		return constructLeaveColStyle(this,value,row,index);
	}});
	
	var enumArr = FW.getEnumMap("ATD_LEI_CATEGORY");
	for( key in enumArr ){
		var codeArr = key.split( "_" );
		if( codeArr[ codeArr.length - 1 ] >= 8 ){
	        var category = "category_" + codeArr[ codeArr.length - 1 ];
	        columnsArray.push({field: category,title: enumArr[key],width:50,fixed:true, sortable: true,formatter:function(value,row,index){
				return constructLeaveColStyle(this,value,row,index);
			}});
		}
	}
	
	//columnsArray.push({field:'blank',title:' ',width:50});
	
	return columnsArray;
}

	$(document).ready(function() {
		//月度统计
		var now=new Date();
		endYear=parseInt(now.getFullYear());
		endMonth = parseInt(now.getMonth())+1;
		getYear();
		setYear();
		
		//初始化sec_function 按钮权限
		initBtnSec( );
		
		//此处一定要给dataGrid赋值。dataGrid为全局变量，不设置会导致不能resize
		dataGrid = $("#contentTb").iDatagrid("init",{
	        pageSize:pageSize,//pageSize为全局变量，自动获取的
	        singleSelect :true,
	        rownumbers : false,
	        fitColumns : false,
	        frozenColumns : [conFrozenColumns() ],
	        url: basePath + "attendance/stat/queryStatList.do",	//basePath为全局变量，自动获取的       
	        queryParams:{"year":function(){return getYearVal()},"month":function(){return getMonthVal()}},
	        "columns":[ constructColArr() ],				
			"onLoadError": function(){
				//加载错误的提示 可以根据需要添加
				$("#grid1_wrap,#toolbar_wrap").hide();
			    $("#grid1_error").show();
			},
	        onLoadSuccess: function(data){
	            //远程无数据需要隐藏整个表格和对应的工具条，然后在无数据信息中指引用户新建
	            if(data && data.total==0){
	            	$("#grid1_wrap").hide();
	                $("#grid1_empty").show();
	            }else{
	            	$("#grid1_wrap").show();
	                $("#grid1_empty").hide();
	            }
	        },
	        onDblClickRow : function( rowIndex, rowData ){
		    	 //非子查询 有权限
		    	 if(privMapping.atd_stat_annual){
			    	 var url = "${basePath}attendance/stat/updateStatRemainToPage.do?id=" + rowData.id;
		    	 	 showRemainIframe( url );
		    	 }
		    },onClickCell : function(rowIndex, field, value) {
		    	
			}
	    });
		
		//高级搜索
		$("#btn_advlocal").click(function() {
			setYear();
			if($(this).hasClass("active")){
				$("#contentTb").ITCUI_GridSearch("end");
				 $("#advSearchBar").hide();
			}
			else{
				$("#advSearchBar").show();
				$("#contentTb").ITCUI_GridSearch("init",{"remoteSearch":true,"onParseArgs":function(arg){
					return {"search":JSON.stringify(arg)};
				}});
			}
		});
		
		//年报表
		$("#countBtn").click(function(){
			yearReport();
		});
		
		//时间段报表
		$("#monthBtn").click(function(){
			monthReport();
		});
		
		$("#countBtn2").click(function(){
			yearReport("excel");
		});
		
		//时间段报表
		$("#monthBtn2").click(function(){
			monthReport("excel");
		});
	});
	
	//为空判断函数
	function isNull( arg1 ){
		return !arg1 && arg1!==0 && typeof arg1!=="boolean"?true:false;
	}
	
	function getStatusVal(){
		var status=$("div.itc-gs-wrap[fieldid=gs_contentTb_userStatus]").find("input");
		if(!status.length){
			status=null;
		}else{
			status=status.val();
		}
		return status;
	}
	function getYearVal(){
		return $("#yearSelect").parent().find("input").val();
	}
	function getMonthVal(){
		return $("#monthSelect").parent().find("input").val();
	}
	var startYear='${startYear}';
	var startMonth='${startMonth}';
	var endYear='';
	var endMonth='';
	var yearSelectData=[];
	var monthSelectData =[];
	function getYear(){
		var year=endYear;
		yearSelectData=[[year,year+"年",true]];
		while(year>startYear){
			year--;
			yearSelectData.push([year,year+"年"]);
		}
	}
	function setYear(){
		$("#yearSelect").iCombo("init", {
			data : yearSelectData,
			"onChange" : function(val) {
				setMonth(val);
			}
		});
	}
	function setMonth(year){
		monthSelectData=[["0","全年"]];
		var m=year==startYear?endMonth:1;
		var n=year==endYear?endMonth:12;
		for(var i=n;i>=m;i--){
			monthSelectData.push([i,i+"月"]);
		}
		$("#monthSelect" ).iCombo("init", {
			data : monthSelectData
		});	
	}
</script>
</head>
<body style="height: 100%;" class="bbox list-page">
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar" class="btn-toolbar ">
	        <div class="atd_btn_pri atd_stat_search btn-group btn-group-sm">
	           <button type="button" class="btn btn-default" data-toggle="button" id="btn_advlocal">查询</button>
	    	</div>
	        <div class="atd_btn_pri atd_stat_count btn-group btn-group-sm">
		        <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
		                            年度报表
		        <span class="caret"></span>
		        </button>
		        <ul class="dropdown-menu">
		            <li><a id="countBtn" href="javascript:void(0);">预览pdf</a></li>
		            <li><a id="countBtn2" href="javascript:void(0);">导出excel</a></li>
		        </ul>
	        </div>
	        <div class="atd_btn_pri atd_stat_annual btn-group btn-group-sm">
	           <button type="button" class="btn btn-default" id="subBtn" onclick="showDtlIframe();">统一核减年假</button>
	        </div>
	    </div>
	    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
	    <div id="pagination_1" class="toolbar-pager" bottompager="#bottomPager">        
	    </div>
	</div>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	 <div id="advSearchBar" style="height: 35px;margin-top: 6px;display: none;" class="bbox">
	 	
		<span class="ctrl-label pull-left">月度统计：</span>
		<div class="btn-group btn-group-sm fl" style="margin-left:8px">
		    <select id="yearSelect" style="width: 90px; float: left;" >
			</select>
			<select id="monthSelect" style="width: 90px; float: left;" >
			</select>
		</div>

	    <div class="btn-group btn-group-sm fl" style="margin-left:8px">
	        <button type="button" class="btn btn-default" onclick="advSearch()">查询</button>
	    </div>
	    <div class="atd_btn_pri atd_stat_count btn-group btn-group-sm fl" style="margin-left:8px">
		        <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
		                            月度报表
		        <span class="caret"></span>
		        </button>
		        <ul class="dropdown-menu">
		            <li><a id="monthBtn" href="javascript:void(0);">预览pdf</a></li>
		            <li><a id="monthBtn2" href="javascript:void(0);">导出excel</a></li>
		        </ul>
	    </div>
	</div>
	<div id="grid1_wrap" style="width:100%">
	    <table id="contentTb" pager="#pagination_1" class="eu-datagrid">
	    </table>
	</div>
	
	
	<!-- 下页器部分-->
	<div id="bottomPager" style="width:100%;margin-top:6px">
	</div>
	<!-- 错误信息-->
	<div class="row" id="grid1_error" style="display:none">
	    无法从服务器获取数据，请检查网络是否正常
	</div>
	<!-- 无数据 -->
	<div id="grid1_empty" style="display:none;width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">没有统计信息</div>
			</div>
		</div>
	</div>
</body>
</html>