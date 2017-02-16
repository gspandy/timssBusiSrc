<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html style="height:99%">
<head>
<title>工单类型列表</title>
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
	var isSearchMode = false;
	
	$(document).ready(function() {
		//列表初始化 
		dataGrid = $("#normKnowledge_table").iDatagrid("init",{
	        pageSize:pageSize,//pageSize为全局变量
	        url: basePath + "itsm/knowledge/knowledgeListData.do",	     
	        singleSelect:true,
	        columns:[[ 
					{field:"knowledgeCode",title:"编码",width:130,fixed:true,sortable:true}, 
					{field:"source",title:"来源",width:100,fixed:true,
						formatter: function(value,row,index){
								return FW.getEnumMap("ITSM_KL_SOURCE")[value]; 
							},
							"editor" : {
						        "type":"combobox",
						        "options" : {
						            "data" : FW.parseEnumData("ITSM_KL_SOURCE",_enum)	
						        }
						    }
					},
					{field:"typeName",title:"类别",width:100,fixed:true},
					{field:"name",title:"标题",width:150},
					{field:"keywords",title:"关键字",width:150},
					{field:"createuserName",title:"创建人",width:70,fixed:true},
					{field:"createdate",title:"创建时间",width:145,fixed:true,sortable:true,
						formatter: function(value,row,index){
							return FW.long2time(value);
						}
					},
					{field:"auditUserName",title:"审核人",width:70,fixed:true},
					{field:"currHandlerUserName",title:"处理人",width:70,fixed:true},
					{field:"currStatus",title:"状态",width:100,fixed:true,sortable:true,
						formatter: function(value,row,index){
								return FW.getEnumMap("ITSM_KL_STATUS")[value]; 
							},
							"editor" : {
						        "type":"combobox",
						        "options" : {
						            "data" : FW.parseEnumData("ITSM_KL_STATUS",_enum)	
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
	        	openKnowledgeInfo(rowData);
			}
		});
		
		
		  //表头搜索相关的
	    $("#btn_advSearch").click(function(){
		    if($(this).hasClass("active")){
		        $("#normKnowledge_table").iDatagrid("endSearch");
		    }
		    else{
		        $("#normKnowledge_table").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(args){
				    isSearchMode = true;
				    return {"search":JSON.stringify(args)};
				}}); 
		    }
		});
	});
	
	
	
	
	

	/**打开故障类型详情*/
	function openKnowledgeInfo(rowData){
		var knowledgeId = rowData.id;
	   var rand = rowData.knowledgeCode;
	   var opts = {
	        id : "openItsmKnowledge"+rand,
	        name : "知识单详情",
	        url : basePath+ "itsm/knowledge/openKnowledgePage.do?klId="+knowledgeId,
	        tabOpt : {
	        	closeable : true,
	        	afterClose : "FW.deleteTab('$arg'); FW.activeTabById('itsm_root');FW.getFrame('itsm_root').refresh();"
	        }
	    }
	    _parent()._ITC.addTabWithTree(opts); 
	}
	/** 新建一个知识类型 */
	function newKnowledge(){
	   var rand = new Date().getTime() + Math.floor(Math.abs(Math.random()*100));
	   var opts = {
	        id : "newItsmKnowledge"+rand,
	        name : "新建知识单",
	        url : basePath+ "itsm/knowledge/openKnowledgePage.do",
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
					<button id="btn_jp_new" type="button" class="btn btn-success" onclick="newKnowledge()">新建</button>
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
		    <table id="normKnowledge_table" pager="#pagination_1" class="eu-datagrid">
		    </table>
		</div>
		<!-- 下页器部分-->
		<div id="bottomPager" style="width:100%;margin-top:6px"></div>
	</div>
	<!-- 表头搜索无数据时-->
	<div id="noSearchResult"  style="margin-top:20px;font-size:14px;vertical-align:middle;text-align:center" >
		没有找到符合条件的结果x
	</div>
	
	<!-- 无数据 -->
	<div id="grid1_empty" style="display:none;width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">没有审批知识单信息</div>
			    <div class="btn-group btn-group-sm margin-element">
		                 <button type="button" class="btn btn-success" onclick="newKnowledge();">新建</button>
			    </div>
			</div>
		</div>
	</div>
</body>
</html>