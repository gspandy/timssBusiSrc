<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html  style="height: 99%;">
<head>
<title>标准工作票列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
	var sptwFilterType = <%=request.getParameter("sptwFilterType")%> ;
</script>
<script src='${basePath}js/ptw/addTab.js?ver=${iVersion}'></script>
<script type="text/javascript" src="${basePath}js/ptw/commonPtwTree.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/ptw/core/ptwStdPrivCommon.js?ver=${iVersion}"></script>

<script>
//第一次打开页面
var modelFlag = false;
//树查询参数
var searchParams = {};
//刷新
function refreshGrid(){
	searchParams = {};
	//这里的mytable要换成datagrid对应的id
    delete(_itc_grids["contentTb"]);
    var pager = $("#contentTb").datagrid("getPager"); 
    pager.pagination("select",1);
	
	//取消树的选中		
	var p = _parent().window.document.getElementById("ptwAssetTree").contentWindow;
	if(p){
		p.unSelectTree();
	}
}
/**查询*/
function search(){
    //这里的mytable要换成datagrid对应的id
    delete(_itc_grids["contentTb"]);
    var pager = $("#contentTb").datagrid("getPager"); 
    pager.pagination("select",1);
};
//接收树的点击
function onTreeItemClick(node){
	if(node.type == 'root'){
		refreshGrid();
	}else{
		searchParams = {eqId:node.id};
		$("#contentTb").datagrid('options').queryParams.search = function(){
		       return JSON.stringify(searchParams);
		    };
		search();
	}
}

	$(document).ready(function() {
		//初始化设备树
		initAssetTree();
		//初始化权限
		setPriv();
		
		//此处一定要给dataGrid赋值。dataGrid为全局变量，不设置会导致不能resize
		dataGrid = $("#contentTb").iDatagrid("init",{
	        pageSize:pageSize,//pageSize为全局变量，自动获取的
	        singleSelect :true,
	        url: basePath + "ptw/ptwStandard/queryAllFinishPtwStandardBySiteId.do?sptwFilterType="+sptwFilterType,	//basePath为全局变量，自动获取的       
	        "columns":[[
				{field:'id',title:'id',width:20,fixed:true,hidden:true},
				{field:'wtNo',title:'编号',width:135,fixed:true, sortable: true},
				{field:'wtTypeId',title:'类型',width:80,fixed:true, sortable: true,
					formatter: function(value,row,index){
			        	return FW.getEnumMap("ptw_standard_type")[value];
			        },
			        "editor" : {
			        	"type":"combobox",
			            "options" : {
			            	"data" : FW.parseEnumData("ptw_standard_type",_enum)
			            }
			        }	
				},				
				{field:'eqName',title:'设备名称',width:150,fixed:true, sortable: true},
				{field:'workContent',title:'工作内容',width:150, sortable: true}/* ,
				{field:'flowStatus',title:'状态',width:20,formatter:function(val){
					var str = FW.getEnumMap("PTW_STD_STATUS")[val];
					if( str == null || str == "" ){
						str = val;
					}
					return str;
				},editor:{
					type : "combobox",
					options: {
						data : FW.parseEnumData("PTW_STD_STATUS",_enum)
					}
				}} */
				]],
			"onLoadError": function(){
				//加载错误的提示 可以根据需要添加
				$("#grid1_wrap,#toolbar_wrap,#bottomPager").hide();
			    $("#grid1_error").show();
			},
	        onLoadSuccess: function(data){
	            //远程无数据需要隐藏整个表格和对应的工具条，然后在无数据信息中指引用户新建
	            if(data && data.total==0){
	            	 if( modelFlag ){
		                	$("#noResult").show();
		                }else{
			            	$("#grid1_wrap,#toolbar_wrap").hide();
			                $("#grid1_empty").show();
			                $("#noResult").hide();
		                }
	            }else{
	            	$("#grid1_wrap,#toolbar_wrap").show();
	                $("#grid1_empty").hide();
	                $("#noResult").hide();
	            }
	           
	        },
	        onDblClickRow : function( rowIndex, rowData ){
	        	 var sptwId = rowData.id ;
				 var tabId = FW.getCurrentTabId();
				 var p = _parent().window.document.getElementById(tabId).contentWindow;
				 p.initDataFromSptw(sptwId);
				 _parent().$("#itcDlg").dialog("close");

		    }

	    });
		
		$("#btn_add,#addBtn").click(function(){
			var url ="${basePath}ptw/ptwStandard/insertPtwStandardMenu.do";
			addTabWithTree( "addPtwStandard", "新建标准工作票", url,"ptw", "contentTb");
		});
		
		
		//高级搜索
		$("#btn_advlocal").click(function() {
			modelFlag = true;
			if($(this).hasClass("active")){
				$("#contentTb").ITCUI_GridSearch("end");
			}
			else{
				$("#contentTb").ITCUI_GridSearch("init",{"remoteSearch":true,"onParseArgs":function(arg){
					return {"search":JSON.stringify(arg)};
				}});
			}
		});
		
	});
</script>
</head>
<body style="height: 100%;" class="bbox">
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar1" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm">
	           <button type="button" class="btn btn-default priv" data-toggle="button" id="btn_advlocal" privilege="PTW_STD_SEARCH">查询</button>
	    	</div>
	    </div>
	    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
	    <div id="pagination_1" class="toolbar-pager" bottompager="#bottomPager">        
	    </div>
	</div>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div id="grid1_wrap" style="width:100%">
	    <table id="contentTb" pager="#pagination_1" class="eu-datagrid">
	    </table>
	</div>
	<!-- 下页器部分-->
	<div id="bottomPager" style="width:100%;margin-top:6px">
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
			    <div style="font-size:14px">没有标准工作票信息</div>
			</div>
		</div>
	</div>
</body>
</html>