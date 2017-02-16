<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html style="height: 99%;">
<head>
<title>用车申请列表</title>
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src='${basePath}js/attendance/common/addTab.js?ver=${iVersion}'></script>
<script>
	var isSearchMode = false;
	$(document).ready(function() {
	$("#carApply_table").iDatagrid("init",{
		        pageSize:pageSize,//pageSize为全局变量
		        url: basePath + "attendance/carApply/carApplyListData.do",	     
		        singleSelect:true,
		        columns:[[
						{field:"createDate",title:"申请日期",width:100,sortable:true,fixed:true,
							formatter: function(value,row,index){
							    return FW.long2date(value);
						        }},
						/* {field:"caNum",title:"申请单号",width:135,fixed:true,sortable:true}, */
						{field:"createUserName",title:"申请人",width:70,fixed:true,sortable:true},
						{field:"deptName",title:"申请部门",width:90,fixed:true,sortable:true},
						{field:"destination",title:"目的地",width:110,fixed:true},
						{field:"reason",title:"用车理由",width:100},        
						{field:"startTime",title:"开始时间",width:150,sortable:true,fixed:true,
							formatter: function(value,row,index){
							    return FW.long2time(value);
						        }},
						{field:"endTime",title:"结束时间",width:150,sortable:true,fixed:true,
							formatter: function(value,row,index){
							    return FW.long2time(value);
						        }},
						{field:"phone",title:"联系方式",width:115,fixed:true},
						{field:"driver",title:"驾驶员",width:70,fixed:true},
						{field:"carType",title:"车牌号",width:80,fixed:true},
						{field:"status",title:"状态",width:75,
							formatter: function(value,row,index){
								return FW.getEnumMap("ATD_CARAPPLY_STATUS")[value]; 
							},
							"editor" : {
						        "type":"combobox",
						        "options" : {
						            "data" : FW.parseEnumData("ATD_CARAPPLY_STATUS",_enum)	
						        }
						    }
						}
					]],
		        onLoadSuccess: function(data){
		        	if(isSearchMode){
				        //搜索时的无数据信息
				        if(data && data.total==0){
				            $("#noSearchResult").show();
				        }
				        else{
				            $("#noSearchResult").hide();
				        }
				    }else{
				    //远程无数据需要隐藏整个表格和对应的工具条，然后在无数据信息中指引用户新建
			            $("#noSearchResult").hide();
			            if(data && data.total==0){
			                $("#mainContent").hide();
			                $("#grid1_empty").show();
			            }else{
			            	$("#mainContent").show();
			                $("#grid1_empty").hide();
			            }
			        }
		            
		        },
		         onDblClickRow : function(rowIndex, rowData) {
		        	openCarApplyInfo(rowData);
				} 
			});
		  //表头搜索相关的
	    	$("#btn_advSearch").click(function(){
		    	if($(this).hasClass("active")){
		        	$("#carApply_table").iDatagrid("endSearch");
		    	}
		    	else{
		        	$("#carApply_table").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(args){
				    	isSearchMode = true;
				    	return {"search":JSON.stringify(args)};
					}}); 
		   	 	}
			});
		});
		
	/**打开详情**/
	function openCarApplyInfo(rowData){
		var caId = rowData.caId;
		//新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
	   var rand =  rowData.priorityCode;
	   var opts = {
	        id : "openCarApply"+rand,
	        name : "用车申请",
	        url : basePath+ "attendance/carApply/openCarApplyPage.do?caId="+caId,
	        tabOpt : {
	        	closeable : true,
	        	afterClose : "FW.deleteTab('$arg');FW.activeTabById('"+FW.getCurrentTabId()+"');FW.getFrame('attendance').refresh();"
	        }
	    };
	    _parent()._ITC.addTabWithTree(opts); 
	}
	
	/** 新建 */
	function newCarApplyPage(){
	 //新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
	   var rand = new Date().getTime() + Math.floor(Math.abs(Math.random()*100));
	   var opts = {
	        id : "new"+rand,
	        name : "用车申请",
	        url : basePath+ "attendance/carApply/openCarApplyPage.do",
	        tabOpt : {
	        	closeable : true,
	        	afterClose : "FW.deleteTab('$arg');FW.activeTabById('"+FW.getCurrentTabId()+"');FW.getFrame('"+FW.getCurrentTabId()+"').refresh();"
	        }
	    };
	    _parent()._ITC.addTabWithTree(opts); 
	}
	
 /**刷新页面**/	
function refresh(){
	window.location.href=window.location.href;
}


/**
 * 关闭当前tab 页
 */
function closeCurPage(){
	FW.deleteTabById(FW.getCurrentTabId());
}
</script>
</head>
<body style="height: 100%; min-width:850px" class="bbox list-page">
	<div id="mainContent">
		<div class="bbox toolbar-with-pager" id="toolbar_wrap">
			<!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
			<div id="toolbar1" class="btn-toolbar ">
				<div class="btn-group btn-group-sm">
					<button id="btn_jp_new" type="button" class="btn btn-success"
						onclick="newCarApplyPage()">新建</button>
				</div>
				
				<div class="btn-group btn-group-sm">
					<button id="btn_advSearch" type="button" data-toggle="button"
						class="btn btn-default">查询</button>
				</div>
			</div>
			<!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
			<div id="pagination_1" class="toolbar-pager"
				bottompager="#bottomPager"></div>
		</div>
		<!--这里要清掉分页器的右浮动效果-->
		<div style="clear:both"></div>
		<div id="grid1_wrap" style="width:100%">
			<table id="carApply_table" pager="#pagination_1" class="eu-datagrid">
			</table>
		</div>
		<!-- 下页器部分-->
		<div id="bottomPager" style="width:100%;margin-top:6px"></div>
	</div>
	<!-- 表头搜索无数据时-->
	<div id="noSearchResult"
		style="margin-top:20px;font-size:14px;vertical-align:middle;text-align:center">
		没有找到符合条件的结果</div>

 	<!-- 无数据 -->
	<div id="grid1_empty" style="display:none;width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div
				style="display:table-cell;vertical-align:middle;text-align:center">
				<div style="font-size:14px">未查询到用车申请信息</div>
				<div class="btn-group btn-group-sm margin-element">
					<button type="button" class="btn btn-success"
						onclick="newCarApplyPage()">新建</button>
				</div>
			</div>
		</div>
	</div>  
</body>
</html>