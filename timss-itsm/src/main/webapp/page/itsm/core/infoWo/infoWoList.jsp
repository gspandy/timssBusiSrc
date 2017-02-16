<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html style="height:99%">
<head>
<title>工单类型列表</title>
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />

<script type="text/javascript" src="${basePath}js/itsm/core/infoWo/infoWoCommon.js?ver=${iVersion}"></script>

<script>
	var isSearchMode = false;
	
	$(document).ready(function() {
		
		initLeftTree();
		//列表初始化 
		var dataGrid = $("#infoWo_table").iDatagrid("init",{
		        pageSize:pageSize,//pageSize为全局变量
		        url: basePath + "itsm/infoWo/infoWoListData.do",	     
		        singleSelect:true,
		        columns:[[ //申请单号、服务目录、名称、申请人、部门名称、申请时间、状态
						{field:"infoWoCode",title:"申请单号",width:150,fixed:true,sortable:true}, 
						{field:"serType",title:"维护类型",width:80,fixed:true,
							formatter: function(value,row,index){
									return FW.getEnumMap("ITSM_INFOWO_SERTYPE")[value]; 
								},
								"editor" : {
							        "type":"combobox",
							        "options" : {
							            "data" : FW.parseEnumData("ITSM_INFOWO_SERTYPE",_enum)	
							        }
							    }
						},
						{field:"serCataName",title:"服务目录",width:100,fixed:true},
						{field:"name",title:"名称",width:100},
						{field:"applyUserName",title:"申请人",width:60,fixed:true},
						{field:"applyDeptName",title:"申请部门",width:100,fixed:true},
						{field:"applyTime",title:"申请日期",width:110,fixed:true,
							formatter : function(value, row, index) {
								return FW.long2date(value);
							}
						},
						{field:"currHandlerName",title:"处理人",width:80,fixed:true},
						{field:"status",title:"状态",width:100,fixed:true,
							formatter: function(value,row,index){
									return FW.getEnumMap("ITSM_INFOWO_STATUS")[value]; 
								},
								"editor" : {
							        "type":"combobox",
							        "options" : {
							            "data" : FW.parseEnumData("ITSM_INFOWO_STATUS",_enum)	
							        }
							    }
						}
					]],
		        onLoadSuccess: function(data){
		        	 ftRootId = data.rootId;
		        	 if(isSearchMode){
				        //搜索时的无数据信息
				        if(data && data.total==0){
				            $("#noSearchResult").show();
				        }else{
				            $("#noSearchResult").hide();
				        }
				    }else{
				   		$("#noSearchResult").hide();
				   		if(data && data.total==0){
			                $("#mainContent").hide();
			                $("#grid1_empty").show();
			            }else{
			            	$("#mainContent").show();
			                $("#grid1_empty").hide();
			            }
				   	
				   	}
		            isSearchMode = false;
		        },
		        onDblClickRow : function(rowIndex, rowData) {
		        	openInfoWo(rowData);
				}
			});
			
		
		  //表头搜索相关的
	    $("#btn_advSearch").click(function(){
		    if($(this).hasClass("active")){
		        $("#infoWo_table").iDatagrid("endSearch");
		    }
		    else{
		        $("#infoWo_table").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(args){
				    isSearchMode = true;
				    return {"search":JSON.stringify(args)};
				}}); 
		    }
		});
	});
	
/**左边树菜单选择触发事件*/
function triggerFaultTypeTreeSelect(data){
	headSearchParams = {};
    $("#workorder_table").iDatagrid("endSearch");
	var selectType = data.faultTypeCode;
	if(selectType == "SD"){
		selectFaultTypeId = data.id;
		selectFaultTypeName = data.text;
	}
	$("#workorder_table").datagrid('options').queryParams.search = function(){
       	//	console.log("datagrid查询时" +JSON.stringify(headSearchParams));
       		return JSON.stringify(headSearchParams);
      	};
	/* if(data.type == "root"){
    	refresh();
    }else{ */
    	queryByTree();
   /*  } */
}
	

	/**打开信息工单详情*/
	function openInfoWo(rowData){
		var infoWoId = rowData.id;
	   var rand = rowData.infoWoCode;
	   var opts = {
	        id : "infoWo"+rand,
	        name : "信息工单",
	        url : basePath+ "itsm/infoWo/openInfoWoPage.do?id="+infoWoId,
	        tabOpt : {
	        	closeable : true,
	        	afterClose : "FW.deleteTab('$arg'); FW.activeTabById('itsm_root');FW.getFrame('itsm_root').refresh();"
	        }
	    }
	    _parent()._ITC.addTabWithTree(opts); 
	}
	/** 新建一个信息工单 */
	function newInfoWo(){
	   var rand = new Date().getTime() + Math.floor(Math.abs(Math.random()*100));
	   var opts = {
	        id : "newInfoWo"+rand,
	        name : "信息工单",
	        url : basePath+ "itsm/infoWo/newInfoWoPage.do",
	        tabOpt : {
	        	closeable : true,
	        	afterClose : "FW.deleteTab('$arg'); FW.activeTabById('itsm_root');FW.getFrame('itsm_root').refresh();"
	        }
	    }
	    _parent()._ITC.addTabWithTree(opts); 

	}
	
	 
function refresh(){
	window.location.href=window.location.href;
}

</script>
</head>
<body style="height: 100%;" class="bbox list-page">
	<div id="mainContent">
		<div class="bbox toolbar-with-pager" id="toolbar_wrap">
		    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
		    <div id="toolbar1" class="btn-toolbar ">
		    	<div class="btn-group btn-group-sm">
					<button id="btn_infoWo_new" type="button" class="btn btn-success" onclick="newInfoWo()">新建</button>
				</div>
				<div class="btn-group btn-group-sm">
		            <button id="btn_advSearch" type="button" data-toggle="button" class="btn btn-default">查询</button>
		        </div>
		    </div>
		    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
		    <div id="pagination_1" class="toolbar-pager" bottompager="#bottomPager">        
		    </div>
		</div>
		<!--这里要清掉分页器的右浮动效果-->
		<div style="clear:both"></div>
		<div id="grid1_wrap" style="width:100%">
		    <table id="infoWo_table" pager="#pagination_1" class="eu-datagrid">
		    </table>
		</div>
		<!-- 下页器部分-->
		<div id="bottomPager" style="width:100%;margin-top:6px"></div>
	</div>
	<!-- 表头搜索无数据时-->
	<div id="noSearchResult"  style="margin-top:20px;font-size:14px;vertical-align:middle;text-align:center" >
		没有找到符合条件的结果
	</div>
	
	<!-- 无数据 -->
	<div id="grid1_empty" style="display:none;width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">没有工单信息</div>
			    <div class="btn-group btn-group-sm margin-element">
		                 <button type="button" class="btn btn-success" onclick="newInfoWo();">新建</button>
			    </div>
			</div>
		</div>
	</div>
</body>
</html>