<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>显示请假/加班明细</title>

<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_dialogEmmbed=true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src='${basePath}js/attendance/common/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/attendance/common/commonForm.js?ver=${iVersion}'></script>
<script src='${basePath}js/attendance/common/dataFormat.js?ver=${iVersion}'></script>


<script>

	$(document).ready(function() {
		var userId = '${ userId }';
		var year = '${ year }';
		var month = '${ month }';
		var field = '${ field }';
		
		var url = basePath + "attendance/stat/queryStatLeaveDetail.do?userId=" + userId
			+ "&year=" + year + "&month=" + month
			+ "&field=" + field ;
		
		//此处一定要给dataGrid赋值。dataGrid为全局变量，不设置会导致不能resize
		$("#contentTb").iDatagrid("init",{
	        singleSelect :true,
	        fitColumns:true,
	        url: url,	//basePath为全局变量，自动获取的       
	        "columns":[[
				{field:'num',title:'申请单号',fixed:true,width:150 },
				{field:'startDate',title:'开始时间',fixed:true,width:120,
					 formatter:function(val){
						return FW.long2time(val);
					} 
				},
				{field:'endDate',title:'结束时间',fixed:true,width:120,
					 formatter:function(val){
						return FW.long2time(val);
					} 
				},
				{field:'leaveDays',title:'天数',width:90,formatter:function(val){
						return AtdDataFormat.formatDoubleDays(val);
					}}
				]],
			"onLoadError": function(){
				//加载错误的提示 可以根据需要添加
				$("#grid1_wrap,#toolbar_wrap,#bottomPager").hide();
			    $("#grid1_error").show();
			},
	        onLoadSuccess: function(data){
	            //远程无数据需要隐藏整个表格和对应的工具条，然后在无数据信息中指引用户新建
	            if(data && data.total==0){
	            	$("#grid1_wrap,#toolbar_wrap").hide();
	                $("#grid1_empty").show();
	                $("#noResult").hide();
	            }else{
	            	$("#grid1_wrap,#toolbar_wrap").show();
	                $("#grid1_empty").hide();
	                $("#noResult").hide();
	            }
	        }

	    });
		
	});
		
</script>

</head>
<body style="padding-left: 0px;padding-right: 0px;">
	<div id="grid1_wrap" style="width:100%">
	    <table id="contentTb" pager="#pagination_1" class="eu-datagrid">
	    </table>
	</div>
	
	<div id="noResult" style="display:none;width:100%;">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px"> 没有找到符合条件的结果</div>
			    
			</div>
		</div>
	</div>
	
	<!-- 错误信息-->
	<div class="row" id="grid1_error" style="display:none">
	    无法从服务器获取数据，请检查网络是否正常
	</div>
	<!-- 无数据 -->
	<div id="grid1_empty" style="display:none;width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">没有相关信息</div>
			</div>
		</div>
	</div>
	
</body>
</html>