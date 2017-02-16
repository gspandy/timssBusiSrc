<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html style="height:99%">
<head>
<title>钥匙箱管理列表</title>
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script type="text/javascript" src="${basePath}js/ptw/sjc/openKeyBoxTab.js?ver=${iVersion}"></script>
<script>

	$(document).ready(function() {

		dataGrid = $("#keybox_table").iDatagrid("init",{
	        pageSize:pageSize,//pageSize为全局变量
	        url: basePath + "ptw/ptwKeyBox/keyBoxListData.do",	   
	        singleSelect:true,
	        columns:[[ 
					{field:"keyBoxNo",title:"钥匙箱号",width:110,fixed:true,sortable:true}, 
					{field:"useType",title:"类型",width:100,fixed:true,sortable:true,
						formatter: function(value,row,index){
							return FW.getEnumMap("PTW_KEYBOXTYPE")[value]; 
						},
						"editor" : {
						        "type":"combobox",
						        "options" : {
						            "data" : FW.parseEnumData("PTW_KEYBOXTYPE",_enum)
						        }
						    }
					},
					{field:"purpose",title:"用途",width:200,sortable:true},
					{field:"curStatus",title:"当前状态",width:100,fixed:true,sortable:true,
						formatter: function(value,row,index){
							return FW.getEnumMap("PTW_KEYBOXSTATUS")[value]; 
						},
						"editor" : {
						        "type":"combobox",
						        "options" : {
						            "data" : FW.parseEnumData("PTW_KEYBOXSTATUS",_enum)
						        }
						    }
					}
				]],
	        onLoadSuccess: function(data){
	            //远程无数据需要隐藏整个表格和对应的工具条，然后在无数据信息中指引用户新建
	           if(data && data.total==0){
	                $("#mainContent").hide();
	                $("#grid1_empty").show();
	            }else{
	            	$("#mainContent").show();
	                $("#grid1_empty").hide();
	            } 
	        },
	        onDblClickRow : function(rowIndex, rowData) {
	        	openKeyBoxPage(rowData.id,rowData.keyBoxNo);
			}
		});
		
		
		  //表头搜索相关的
	    $("#btn_advSearch").click(function(){
		    if($(this).hasClass("active")){
		        $("#keybox_table").iDatagrid("endSearch");
		    }
		    else{
		        $("#keybox_table").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(args){
				    return {"search":JSON.stringify(args)};
				}}); 
		    }
		});
		
	});	
	
function refresh(){
	headSearchParams = {};
	$("#mainContent").show();
	$("#grid1_empty").hide();
	$("#keybox_table").datagrid("reload");
}
/**
 * 关闭当前tab 页
 */
function closeCurPage(){
	FW.deleteTabById(FW.getCurrentTabId());
}
</script>
</head>
<body style="height: 100%;" class="bbox">
	<div id="mainContent">
		<div class="bbox toolbar-with-pager" id="toolbar_wrap">
		    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
		    <div id="toolbar1" class="btn-toolbar ">
		    	<div class="btn-group btn-group-sm">
					<button id="btn_keybox_new" type="button" class="btn btn-success" onclick="newKeyBox()">新建</button>
				</div>
		        <div class="btn-group btn-group-sm">
		        	<button type="button" class="btn btn-default" onclick="refresh()">刷新</button>
		        </div>
				<div class="btn-group btn-group-sm">
		            <button id="btn_advSearch" type="button" data-toggle="button" class="btn btn-default">高级搜索</button>
		        </div>
		    </div>
		    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
		    <div id="pagination_1" class="toolbar-pager" bottompager="#bottomPager">        
		    </div>
		</div>
		<!--这里要清掉分页器的右浮动效果-->
		<div style="clear:both"></div>
		<div id="grid1_wrap" style="width:100%">
		    <table id="keybox_table" pager="#pagination_1" class="eu-datagrid">
		    </table>
		</div>
		<!-- 下页器部分-->
		<div id="bottomPager" style="width:100%;margin-top:6px"></div>
	</div>
	
	
	<!-- 无数据 -->
	<div id="grid1_empty" style="display:none;width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">未查询到“钥匙箱”信息</div>
			    <div class="btn-group btn-group-sm margin-element">
		             <button type="button" class="btn btn-success" onclick="newKeyBox();">新建</button>
			    </div>
			</div>
		</div>
	</div>
</body>
</html>