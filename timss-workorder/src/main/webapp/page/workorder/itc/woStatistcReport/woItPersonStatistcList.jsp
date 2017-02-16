<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>个人工作量统计</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script  type="text/javascript" src="${basePath}js/workorder/common/dateUtil.js?ver=${iVersion}"></script> 
<script>
	var printNum = "";
	var beginTime = getPreMonthFirst();  //上月的第一天
	var endTime = getPreMonthEnd();  //上月的最后一天
	/* 表单字段定义  */
	var fields = [
				{title : "开始时间", id : "beginTime", type:"date", dataType:"datetime",
					options:{
						allowEmpty:false,
						endDate : new Date(),
					}
				},
				{title : "结束时间", id : "endTime", type:"date", dataType:"datetime",
					options:{
						allowEmpty:false,
						endDate : new Date(),
					}
				}
			];
	$(document).ready(function(){
		$("#selectTimeForm").iForm("init",{"fields":fields,options:{labelFixWidth:80,mdWidth:6}});
		$("#selectTimeForm").iForm("setVal",{"beginTime":beginTime,"endTime":endTime})
		
 		dataGrid = $("#woItPersonStatistic_table").iDatagrid("init",{
	        pageSize:50,//pageSize为全局变量
	        url: basePath + "/workorder/woStatis/woItMaintainKPIData.do",     
	        queryParams:{
	        	statisticType:"personStatistic",  //personStatistic、teamStatistic、
	        	begin:beginTime.getTime(),
	        	end:endTime.getTime()+24*60*60*1000
	        },
	        singleSelect:true,
	        columns:[[ 
					{field:"serLevel",title:"姓名",width:60,fixed:true,
						styler: function(value,row,index){
							return 'background-color:#FFFFFF;';
						}
					}, 
					{field:"eventType",title:"事件分类",width:110,fixed:true}, 
					{field:"sum",title:"事件(请求)总数",width:130,align:'center'}, 
					{field:"solveRatio",title:"成功解决率",width:130,align:'center',
						formatter:function(value,row,index){
							return value+"%";
						}
					}, 
					{field:"overTimeSolveRatio",title:"及时解决率",width:130,align:'center',
						formatter:function(value,row,index){
							return value+"%";
						}
					}, 
					{field:"overTimeRespondRatio",title:"及时响应率",width:130,align:'center',
						formatter:function(value,row,index){
							return value+"%";
						}
					},   
					{field:"avgSolveTime",title:"平均解决时间(h)",width:130,align:'center'}
				]],
	        onLoadSuccess: function(data){
	        	printNum = data.printNum;
	        	var serLevelSum = data.serLevelSum;
	        	var mergeNumList = data.mergeNumList;
	        	mergeDatagridCells(serLevelSum,mergeNumList); //合并单元格
	        },
	        rowStyler: function(index,row){
	        	if(row.eventType == "小计"){
	        		return 'background-color:#CCCCCC;color:#000000;';
	        	}
	        	if(row.serLevel=="合计"){
	        		return 'background-color:#999999;color:#000000;font-weight:bold;';
	        	}
	        }
	    }); 
	    
	});
	function mergeDatagridCells(serLevelSum, mergNumList){
		var beginIndex = 0;
		for(var i=0 ;i<serLevelSum; i++){
			$("#woItPersonStatistic_table").datagrid('mergeCells', {
				index: beginIndex,
				field: "serLevel",
				rowspan: mergNumList[i],
				type: "body"
			});
			beginIndex = beginIndex +mergNumList[i];
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
		}else{
			dataGrid.datagrid("load",{
				statisticType:"personStatistic",  
		        begin:tempTime.beginTime,
		        end:tempTime.endTime+24*60*60*1000
			});
		}
	}
	
	function printStatisticList(){
		var tempTime = $("#selectTimeForm").iForm("getVal");
		var beginStr = FW.long2date(tempTime.beginTime);
		var endStr = FW.long2date(tempTime.endTime)
		 
		var src = fileExportPath + "preview?__report=report/TIMSS2_WO_ITPERSON_KPI.rptdesign&__format=pdf"+
							"&printNum="+printNum+"&begin="+beginStr+"&end="+endStr;
		var url = encodeURI(encodeURI(src));
		var title ="个人运维KPI统计"
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
		    <div style="width:400px;float:left;padding-top:3px;">
		    	 <form id="selectTimeForm" class="autoform"></form>
			</div>
			
			<div class="btn-group btn-group-sm" style="float:left;padding-left:30px;" >
				<button id="bt_query" class="btn btn-default" onclick="queryStatisticList()">查询</button>
			</div>
			
	    	<div class="btn-group btn-group-sm" style="float:left;" >
				<button id="bt_print" class="btn btn-default" onclick="printStatisticList()">打印</button>
			</div>
	    </div>
	    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
	    <!-- <div id="pagination_1" class="toolbar-pager">        
	    </div> -->
	</div>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	 <div>
    	 <form id="statisForm" class="autoform"></form>
    </div>
	<div id="grid1_wrap" style="width:100%">
	    <table id="woItPersonStatistic_table" pager="#pagination_1" class="eu-datagrid"></table>
	</div>
</body>
</html>