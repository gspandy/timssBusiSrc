<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>事件管理KPI统计</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script  type="text/javascript" src="${basePath}js/itsm/common/dateUtil.js?ver=${iVersion}"></script> 
<script>
	var printNum = "";
	var beginTime = getPreMonthFirst();  //上月的第一天
	var endTime = getPreMonthEnd();  //上月的最后一天
	/* 表单字段定义  */
var fields = [
				{title : "开始时间", id : "beginTime", type:"date", dataType:"datetime",
					options:{
						allowEmpty:false,
						endDate : new Date()
					}
				},
				{title : "结束时间", id : "endTime", type:"date", dataType:"datetime",
					options:{
						allowEmpty:false,
						endDate : new Date()
					}
				}
			];
	$(document).ready(function(){
		$("#selectTimeForm").iForm("init",{"fields":fields,options:{labelFixWidth:65,mdWidth:6}});
		$("#selectTimeForm").iForm("setVal",{"beginTime":beginTime,"endTime":endTime})
		
 		dataGrid = $("#woRequestStatistic_table").iDatagrid("init",{
	        pageSize:50,//pageSize为全局变量
	        url: basePath + "itsm/woStatis/woItManageKPIData.do",     
	        queryParams:{
	        	statisticType:"eventStatistic",  //requstStatistic、eventStatistic、
	        	begin:beginTime.getTime(),
	        	end:endTime.getTime()+24*60*60*1000
	        },
	        singleSelect:true,
	        columns:[[ 
					{field:"serLevel",title:"服务级别",width:100,align:'center',fixed:true}, 
					{field:"eventType",title:"事件分类",width:110,fixed:true}, 
					{field:"sum",title:"事件总数",width:130,align:'center'}, 
					{field:"solveRatio",title:"成功解决率",width:130,align:'center',
						formatter:function(value,row,index){
							return value+"%";
						}
					}, 
					{field:"overTimeSolveRatio",title:"超时解决率",width:130,align:'center',
						formatter:function(value,row,index){
							return value+"%";
						}
					}, 
					{field:"overTimeRespondRatio",title:"超时响应率",width:130,align:'center',
						formatter:function(value,row,index){
							return value+"%";
						}
					},   
					{field:"csOnTimeSolveRatio",title:"服务台及时解决率",width:130,align:'center',
						formatter:function(value,row,index){
							return value+"%";
						}
					}, 
					{field:"teamOnTimeSolveRatio",title:"二线团队及时解决率",width:130,align:'center',
						formatter:function(value,row,index){
							return value+"%";
						}
					},   
					{field:"avgSolveTime",title:"平均解决时间(h)",width:130,align:'center'}
				]],
	        onLoadSuccess: function(data){
	        	printNum = data.printNum;
	        	var serLevelSum = data.serLevelSum;
	        	var oneLevFTSum = data.oneLevFTSum;
	        	mergeDatagridCells(serLevelSum,oneLevFTSum); //合并单元格
	        },
	        rowStyler: function(index,row){
	        	if(row.eventType == "小计"){
	        		return 'background-color:#f5f5f5;color:#000000;';
	        	}
	        	if(row.serLevel=="合计"){
	        		return 'background-color:#CCCCCC;color:#000000;font-weight:bold;';
	        	}
	        }
	    }); 
	    
	    
	    //高级搜索
		/* $("#btn_advlocal").click(function() {
			if($(this).hasClass("active")){
				 $("#advSearchBar").hide();
			}
			else{
				$("#advSearchBar").show();
			}
		});  */
		
	});
	function mergeDatagridCells(serLevelSum, oneLevFTSum){
		for(var i=0 ;i<serLevelSum; i++){
			var beginIndex = i*oneLevFTSum;
			$("#woRequestStatistic_table").datagrid('mergeCells', {
				index: beginIndex,
				field: "serLevel",
				rowspan: oneLevFTSum,
				type: "body"
			});
		}
	}
	function queryStatisticList(){
		var tempTime = $("#selectTimeForm").iForm("getVal");
		
		var time1 = tempTime.endTime;
		var time2 = tempTime.beginTime;
		var timeStepLength = tempTime.endTime - tempTime.beginTime;
		if(timeStepLength>366*24*60*60*1000){
			FW.error("查询跨度不能大于1年");
			return ;
		}
		if(tempTime.endTime < tempTime.beginTime){
			FW.error("请修改统计的时间区间");
			return ;
		}else{
			FW.showMask();
			dataGrid.datagrid("load",{
				statisticType:"eventStatistic",  
		        begin:tempTime.beginTime,
		        end:tempTime.endTime+24*60*60*1000
			});
			setTimeout(function(){FW.removeMask();},6000);
		}
	}
	
	
	
	
	
	function printStatisticList(){
		var tempTime = $("#selectTimeForm").iForm("getVal");
		var beginStr = FW.long2date(tempTime.beginTime);
		var endStr = FW.long2date(tempTime.endTime)
		 
		var src = fileExportPath + "preview?__report=report/TIMSS2_ITSM_ITEVENT_KPI.rptdesign&__format=pdf"+
							"&printNum="+printNum+"&begin="+beginStr+"&end="+endStr;
		var url = encodeURI(encodeURI(src));
		var title ="事件管理KPI统计"
		FW.dialog("init",{
			src: url,
			btnOpts:[{
		            "name" : "关闭",
		            "float" : "right",
		            "style" : "btn-default",
		            "onclick" : function(){
		                _parent().$("#itcDlg").dialog("close");
		             }
		        }],
			dlgOpts:{ width:800, height:650, closed:false, title:title, modal:true }
		});
	}
</script>
</head>
<body>
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar1" class="btn-toolbar ">
	    	<div style="width:400px;float:left;">
		    	 <form id="selectTimeForm" class="autoform"></form>
			</div>
			<div class="atd_btn_pri atd_stat_search btn-group btn-group-sm">
	           <button type="button" class="btn btn-default" data-toggle="button" id="btn_advlocal" onclick="queryStatisticList()">查询</button>
	    	</div> 
			
	    	<div class="btn-group btn-group-sm" style="float:left;" >
				<button id="bt_print" class="btn btn-default" onclick="printStatisticList()">打印</button>
			</div>
	    </div>
	</div>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	
	<!-- <div id="advSearchBar" style="height: 35px;margin-top: 6px;display: none;" class="bbox">
			<div style="width:400px;float:left;">
		    	 <form id="selectTimeForm" class="autoform"></form>
			</div>
		    <div class="btn-group btn-group-sm fl" style="margin-left:8px">
		        <button type="button" class="btn btn-default" onclick="queryStatisticList()">确定</button>
		    </div>
	</div>  -->
	
	<div id="grid1_wrap" style="width:100%">
	    <table id="woRequestStatistic_table" pager="#pagination_1" class="eu-datagrid"></table>
	</div>
</body>
</html>