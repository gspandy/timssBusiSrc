<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:99%">
<head>
<title>两票统计</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
	var siteId = '${siteId}';	
	var urlReport = null;
	var parmasYear = null;
	var parmasMonth = null; 
	//fileExportPath = "http://10.0.17.153:8080/itc_report/";
	
	//初始化执行js
	$(document).ready(function() {
		initPackage();
		$("#btn_query").click();
		if(null == urlReport){
			$("#grid_error").show();
			$("#grid_wrap").hide();
		}else{
			$("#grid_error").hide();
			$("#grid_wrap").show();
		}
	});
	
	//初始化按钮
	function initPackage(){
		var myDate = new Date();
		var currentYear = myDate.getFullYear();    //获取完整的年份
		var currentMonth = myDate.getMonth()+1; //获取完整月份
		var data = "";
		for ( var i = 2014; i <= currentYear; i++) {
			data += "['"+ i +"' , '" + i + "年'],";
		}
		if(data!=""){
			data = "[" + data.substring(0, data.length-1) + "]";
		}
		//年份
		$("#parmasYear").iCombo("init",{
			data:eval(data),
			onChange:function(){
				changeExportUrl();
			}
		});	
		$("#parmasYear").iCombo("setVal",currentYear);
		//月份
		$("#parmasMonth").iCombo("init",{
			data:[
			    ["1","1月"],
			    ["2","2月"],
			    ["3","3月"],
			    ["4","4月"],
			    ["5","5月"],
			    ["6","6月"],
			    ["7","7月"],
			    ["8","8月"],
			    ["9","9月"],
			    ["10","10月"],
			    ["11","11月"],
			    ["12","12月"]
			],
			onChange:function(){
				changeExportUrl();
			}
		});			
		$("#parmasMonth").iCombo("setVal",currentMonth);
			
		
					
		//查询
		$("#btn_query").click(function(){
			parmasYear = $("#parmasYear").iCombo("getVal");
			parmasMonth = $("#parmasMonth").iCombo("getVal");
			
			urlReport = fileExportPath + "preview?__report=report/TIMSS2_SWF_PTWOSTAT_html.rptdesign"
					+ "&parmasYear="+parmasYear+"&parmasMonth="+parmasMonth+"&siteid="+siteId;							
			$("#grid_error").hide();
			$("#grid_wrap").show();
			$("#ptwptoStatisticFrame").attr("src",urlReport);
		});
		changeExportUrl();
	} 
	
	//更改下载地址
	function changeExportUrl(){
		parmasYear = $("#parmasYear").iCombo("getVal");
		parmasMonth = $("#parmasMonth").iCombo("getVal");
		urlExport = fileExportPath + "preview?__report=report/TIMSS2_SWF_PTWOSTAT.rptdesign&__format=pdf"
				+ "&parmasYear="+parmasYear+"&parmasMonth="+parmasMonth+"&siteid="+siteId;							
		var	title = "两票统计报表";
		FW.initPrintButton("#btn_export",urlExport,title,null);
	}
</script>
</head>
<body style="height: 100%;" class="bbox">
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <div id="toolbar1" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm" style="margin-left:7px;">
	        
			    <span class="ctrl-label pull-left"  style="margin-left:8px">年份：</span>
		    	<select id="parmasYear" style="width:100px;float:left">
				</select>	
				
			    <span class="ctrl-label pull-left"  style="margin-left:8px">月份：</span>
		    	<select id="parmasMonth" style="width:100px;float:left">
				</select>	
							        
			</div>
										        
	        <div class="btn-group btn-group-sm" style="margin-left:7px;">
	            <button type="button" class="btn btn-default" id="btn_query">查询</button>
	        </div>
	        <div class="btn-group btn-group-sm" style="margin-left:7px;">
	            <button type="button" class="btn btn-default" id="btn_export">打印</button>
	        </div>
	    </div>
	</div>
	
	<div style="clear:both"></div>
	<div id="grid_wrap" style="width:100%;height:95%;">
	    <iframe frameborder="no" border="0" style="width:100%; height:100%;" id="ptwptoStatisticFrame"></iframe>
	</div>
	
	<div id="grid_error" style="display:none;width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">没有查询到相关数据</div>
			</div>
		</div>
	</div>
</body>
</html>