<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>查询运行记事</title>
<script>_useLoadingMask = true;</script>

<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src='${basePath}js/operation/common/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/operation/common/formatterDate.js?ver=${iVersion}'></script>
<script src='${basePath}js/operation/common/stationCommon.js?ver=${iVersion}'></script>

</head>
<body style="height: 100%;" class="bbox list-page">
	
	<!-- 运行记事 -->
	<div id="noteDiv" style="margin-top: 10px;">
		<div id="tableDiv" style="margin-top: 1px;">
		    <table id="contentTb" pager="#pagination_1" class="eu-datagrid">
		    </table>
		</div>
	</div>
	<!-- 错误信息-->
	<div class="row" id="grid1_error" style="display:none">
	    无法从服务器获取数据，请检查网络是否正常
	</div>
	<!-- 无数据 -->
	<div class="row" id="grid1_empty" style="display:none">
	    没有运行记事数据
	</div>
	<!-- 错误信息-->
	<div class="row" id="grid2_error" style="display:none">
	    无法从服务器获取数据，请检查网络是否正常
	</div>
	<!-- 无数据 -->
	<div class="row" id="grid2_empty" style="display:none">
	    没有排班数据，请查看是否已安排排班
	</div>
	
<script>
	//通过岗位 日期拿到运行记事
	function setNoteHistory(){
		$("#noteDiv").show();
		var list=FW.get("OPR_NOTE_SEARCH_CONTENT");
		//此处一定要给dataGrid赋值。dataGrid为全局变量，不设置会导致不能resize
		dataGrid = $("#contentTb").iDatagrid("init",{
	        pageSize:9999,//pageSize为全局变量，自动获取的
	        singleSelect :true,
	        fitColumns:true,
	        nowrap : true,
	        remoteSort:false,
	        data: list,	   
	        "columns":[[
   				{field:'id',title:'id',width:180,fixed:true,hidden: true},
   				
   				{field:'deptId',title:'工种',width:200,hidden: true},
   				{field:'deptName',title:'工种',width:100,sortable:true},
   				{field:'jobsId',title:'岗位',width:200,hidden: true},
   				{field:'jobsName',title:'岗位',width:100,sortable:true},
   				{field:'writeTime',title:'记事时间',width:120,fixed:true,sortable:true,
   					formatter:function(val){
   						return FW.long2time(val);
   					}
   				},
   				{field:'dutyId',title:'当值',width:200,hidden: true},
   				{field:'dutyName',title:'当值',width:100,sortable:true},
   				{field:'shiftId',title:'班次',width:200,hidden: true},
   				{field:'nowscheduleDate',title:'日期',width:80,fixed:true,sortable:true,formatter:function(val){return FW.long2date(val);}},
   				{field:'shiftName',title:'班次',width:100,sortable:true},
   				{field:'type',title:'记事类型',width:80,fixed:true,sortable:true,
   					formatter:function(val){
   						return FW.getEnumMap("OPR_NOTE_TYPE")[val];
   					}	
   				},
   				{field:'crewNum',title:'机组号',width:80,fixed:true,sortable:true,
   					formatter:function(val){
   						return FW.getEnumMap("OPR_CREW_NUM")[val];
   					}	
   				},
   				{field:'content',title:'记事内容',width:400,sortable:true/* ,formatter:function(val,row,index){
					return val?val.replace(/\n|\r\n/g,"<br/>"):"";
				} */}
	    	]],
			"onLoadError": function(){
				//加载错误的提示 可以根据需要添加
				$("#noteDiv").hide();
			    $("#grid1_error").show();
			},
			onBeforeLoad : function() {
				$.each(["type"],function(name,value){
	    			$('#contentTb').datagrid("hideColumn",value);
	    		});
			},
	        onLoadSuccess: function(data){
	        	$("#grid1_error").hide();
	            //远程无数据需要隐藏整个表格和对应的工具条，然后在无数据信息中指引用户新建
	            if(data && data.total==0){
	            	$("#noteDiv").hide();
	                $("#grid1_empty").show();
	            }else{
	            	$("#noteDiv").show();
	                $("#grid1_empty").hide();
	            }
	        },
	        onDblClickRow : function( rowIndex, rowData ){
		    	var detailUrl = "${basePath}operation/note/queryNoteDetailPage.do?shiftId="
		    			+ rowData.shiftId + "&handoverId=" + rowData.handoverId + "&dutyId=" + rowData.dutyId
		    			+ "&dateStr=" + FW.long2date(rowData.nowscheduleDate) + 
		    			"&stationId=" + rowData.deptId + "&jobsId=" + rowData.jobsId;
		    	addTabWithTree( "NoteDetail"+rowData.shiftId+rowData.jobsId, "运行记事详情"+rowData.shiftId, detailUrl,FW.getCurrentTabId(), "contentTb" );
		    	_parent().$("#itcDlgsearchNote").dialog("close");
		    }
	    });
	}
	
	$(document).ready(function() {
		setNoteHistory();
	});
</script>
</body>
</html>