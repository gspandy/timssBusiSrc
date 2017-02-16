<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html style="height:99%">
<head>
<title>投诉记录表</title>
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
	var isSearchMode = false;
	$(document).ready(function() {
	$("#complain_table").iDatagrid("init",{
		 pageSize:pageSize,//pageSize为全局变量
		 url: basePath + "itsm/complainRecords/complainRecordsListData.do",	     
		 singleSelect:true,
		 loadFilter: function(data){
		 	var rows = data.rows;
			for(var i=0; i<rows.length; i++){
				rows[i].content = FW.unspecialchars(rows[i].content.replace(/<.*?>/g, ""));
		    }
		    return data;
		 },
		 columns:[[
				  {field:"code",title:"编号",width:130,fixed:true,sortable:true,align:'left'},
				  {field:"complainantCom",title:"公司",width:120,fixed:true,sortable:true,align:'left'},
				  {field:"complainant",title:"投诉人",width:80,fixed:true,sortable:true,align:'left'},
				  {field:"createdate",title:"投诉时间",width:150,fixed:true,sortable:true,align:'left',
						formatter: function(value,row,index){
							 return FW.long2time(value);
						}},
				  {field:"complainTypeName",title:"投诉类别",width:120,sortable:true},        
				  {field:"content",title:"内容",width:300,sortable:true},
		          {field:"complainHandlerUser",title:"处理人",width:100,fixed:true,align:'left'},
				  {field:"currStatus",title:"状态",width:100,align:'left',
					    formatter: function(value,row,index){
							 return FW.getEnumMap("ITSM_COMPLAIN_STATUS")[value]; 
						},
						"editor" : {
						"type":"combobox",
						 "options" : {
							 "data" : FW.parseEnumData("ITSM_COMPLAIN_STATUS",_enum)	
						 	}
						}}
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
		        	openComplainRdInfo(rowData);
				} 
			});
		  //表头搜索相关的
	    	$("#btn_advSearch").click(function(){
		    	if($(this).hasClass("active")){
		        	$("#complain_table").iDatagrid("endSearch");
		    	}
		    	else{
		        	$("#complain_table").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(args){
				    	isSearchMode = true;
				    	return {"search":JSON.stringify(args)};
					}}); 
		   	 	}
			});
		});
		
	/*打开投诉记录详情*/
	function openComplainRdInfo(rowData){
		var complainRdId = rowData.id;
		//新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
	   var rand =  rowData.code;
	   var opts = {
	        id : "openComplainRd"+rand,
	        name : "投诉记录",
	        url : basePath+ "itsm/complainRecords/openComplainPage.do?complainRdId="+complainRdId,
	        tabOpt : {
	        	closeable : true,
	        	afterClose : "FW.deleteTab('$arg'); FW.activeTabById('itsm_root');FW.getFrame('itsm_root').refresh();"
	        }
	    };
	    _parent()._ITC.addTabWithTree(opts); 
	}
	
	/** 新建一个投诉 */
	function newComplainPage(){
	 //新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
	   var rand = new Date().getTime() + Math.floor(Math.abs(Math.random()*100));
	   var opts = {
	        id : "newWoType"+rand,
	        name : "投诉记录",
	        url : basePath+ "itsm/complainRecords/openComplainPage.do",
	        tabOpt : {
	        	closeable : true,
	        	afterClose : "FW.deleteTab('$arg'); FW.activeTabById('itsm_root');FW.getFrame('itsm_root').refresh();"
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
<body style="height: 100%;" class="bbox list-page">
	<div id="mainContent">
		<div class="bbox toolbar-with-pager" id="toolbar_wrap">
			<!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
			<div id="toolbar1" class="btn-toolbar ">
				<div class="btn-group btn-group-sm">
					<button id="btn_jp_new" type="button" class="btn btn-success"
						onclick="newComplainPage()">新建</button>
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
		<div id="grid1_wrap" style="width:100%">
			<table id="complain_table" pager="#pagination_1" class="eu-datagrid">
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
				<div style="font-size:14px">未查询到相关数据</div>
				<div class="btn-group btn-group-sm margin-element">
					<button type="button" class="btn btn-success"
						onclick="newComplainPage()">新建</button>
				</div>
			</div>
		</div>
	</div>
</body>
</html>