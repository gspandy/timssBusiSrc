<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html style="height: 99%;">
<head>
	<title>财务报销列表</title>
	<script type="text/javascript">
		_useLoadingMask = true;
		var beginYear =  '${beginYear}';
	</script>
	<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
	<style>
		.dropdown-menu{
			text-align:left;
		}
	</style>
	<script type="text/javascript">
		var siteId = ItcMvcService.getUser().siteId;
		var endYear='';
		var endMonth='';
		var urlReport = null;
		
		$(document).ready(function(){
			//月度统计
			var now=new Date();
			endYear=parseInt(now.getFullYear());
			endMonth = parseInt(now.getMonth())+1;
			setYear();
			
			var beginDate;
			if(endMonth < 10){
				beginDate = FW.long2date(endYear+"-0"+endMonth); 
			}else{
				beginDate = FW.long2date(endYear+"-"+endMonth); 
			}
			var nextMonthDate = new Date(now.setMonth(now.getMonth()+1));

			var nextMonth = parseInt(nextMonthDate.getMonth())+1;
			var endDate;
			if(nextMonth < 10){
				endDate = FW.long2date(nextMonthDate.getFullYear()+"-0"+ nextMonth); 
			}else{
				endDate = FW.long2date(nextMonthDate.getFullYear()+"-"+ nextMonth); 
			}
			
			urlReport = fileExportPath+"preview?__report=report/TIMSS2_FIN_CWBX_001_pdf.rptdesign&__format=html&beginDate="+beginDate+"&endDate="+endDate+"&siteid="+siteId;
			$("#finExpensesReportFrame").attr("src",urlReport);
			
			//查询(单独申请)
			$("#btn_advlocal").click(function(){
				var yearSelect = parseInt($('#yearSelect').iCombo('getVal'));
				var monthSelect = parseInt($('#monthSelect').iCombo('getVal'));
				var dateSelectFrom;
				if(monthSelect < 10){
					dateSelectFrom = FW.long2date(yearSelect+"-0"+ monthSelect); 
				}else{
					dateSelectFrom = FW.long2date(yearSelect+"-"+monthSelect); 
				}
				var dateSelectTo;
				
				monthSelect==12?(yearSelect+1)+"-01":
					yearSelect+"-"+(monthSelect+1)
				if(monthSelect < 9){
					dateSelectTo = FW.long2date(monthSelect==12?(yearSelect+1)+"-01":
						yearSelect+"-0"+(monthSelect+1)); 
				}else{
					dateSelectTo = FW.long2date(monthSelect==12?(yearSelect+1)+"-01":
						yearSelect+"-"+(monthSelect+1)); 
				}
				
				urlReport = fileExportPath+"preview?__report=report/TIMSS2_FIN_CWBX_001_pdf.rptdesign&__format=html&beginDate="+dateSelectFrom+"&endDate="+dateSelectTo+"&siteid="+siteId;
				$("#grid_wrap").show();
				$("#finExpensesReportFrame").attr("src",urlReport);
			});
			
			//时间段报表
			$("#btn_print").click(function(){
				monthReport();
			});
		});
		
		$(window).resize(function(){
			$("#wiGrid").datagrid('resize');
		});
		
		function setYear(){
			var year=endYear;
			var yearSelectData=[[year,year+"年",true]];
			if(beginYear!=''){
				while(year>beginYear){
					year--;
					yearSelectData.push([year,year+"年"]);
				}
			}

			$("#yearSelect").iCombo("init", {
				data : yearSelectData,
				"onChange" : function(val) {
					setMonth(val);
				}
			});
		}
		
		function setMonth(year){
			var monthSelectData=[];
			var n=year==endYear?endMonth:12;
			for(var i=n;i>=1;i--){
				monthSelectData.push([i,i+"月"]);
			}
			$("#monthSelect" ).iCombo("init", {
				data : monthSelectData
			});	
		}
		
		//月报表
		function monthReport(){
			var yearSelect = parseInt($('#yearSelect').iCombo('getVal'));
			var monthSelect = parseInt($('#monthSelect').iCombo('getVal'));
			var dateSelectFrom;
			var dateSelectTo;
			if(monthSelect < 10){
				dateSelectFrom = FW.long2date(yearSelect+"-0"+monthSelect);
			}else{
				dateSelectFrom = FW.long2date(yearSelect+"-"+monthSelect); 
			}
			if(monthSelect < 9){
				dateSelectTo = FW.long2date(monthSelect==12?(yearSelect+1)+"-01":
					yearSelect+"-0"+(monthSelect+1));
			}else{
				dateSelectTo = FW.long2date(monthSelect==12?(yearSelect+1)+"-01":
					yearSelect+"-"+(monthSelect+1));
			}
			
			urlReport = fileExportPath+"preview?__report=report/TIMSS2_FIN_CWBX_001_pdf.rptdesign&__format=pdf&beginDate="+dateSelectFrom+"&endDate="+dateSelectTo+"&siteid="+siteId;
			FW.dialog("init",{src: urlReport,
				btnOpts:[{
				"name" : "关闭",
				"float" : "right",
				"style" : "btn-default",
				"onclick" : function(){
					 _parent().$("#itcDlg").dialog("close");
					 }
				}],
				dlgOpts:{ width:1024, height:650, closed:false, title:"打印财务报销月度统计表", modal:true }
			 });
		}
	</script>
</head>
<body style="height: 100%;" class="bbox">
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <div id="toolbar1" class="btn-toolbar ">
	        <span class="ctrl-label pull-left" style='line-height: 30px;'>统计月份：</span>
		    <div class="btn-group btn-group-sm fl" style="margin-left:8px">
		    <select id="yearSelect" style="width: 90px; float: left;" >
			</select>
			<select id="monthSelect" style="width: 90px; float: left;" >
			</select>
		</div>

	    <div class="btn-group btn-group-sm fl" style="margin-left:8px">
	        <button type="button" class="btn btn-default" id="btn_advlocal">查询</button>
	    </div>

	        <div class="btn-group btn-group-sm" style="margin-left:7px;">
	            <button type="button" class="btn btn-default" id="btn_print">导出</button>
	        </div>
	    </div>
	</div>
	
	<div style="clear:both"></div>
	<div id="grid_wrap" style="width:100%;height:95%;">
	    <iframe frameborder="no" border="0" style="width:100%; height:100%;" id="finExpensesReportFrame"></iframe>
	</div>
</body>
</html>