<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html  style="height: 99%;">
<head>
<title>标准工作列表</title>
<script>_useLoadingMask = true;</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
	var newBtnShowFlag =  ${newBtnShowFlag};
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
		//setPriv();
		
		//此处一定要给dataGrid赋值。dataGrid为全局变量，不设置会导致不能resize
		dataGrid = $("#contentTb").iDatagrid("init",{
	        pageSize:pageSize,//pageSize为全局变量，自动获取的
	        singleSelect :true,
	        rowStyler : rowStyler,
	        url: basePath + "ptw/ptwStandard/queryPtwStandardBySiteId.do",	//basePath为全局变量，自动获取的       
	        "columns":[[
				{field:'id',title:'id',width:20,fixed:true,hidden:true},
				{field:'wtNo',title:'编号',width:100,fixed:true, sortable: true},
				{field:'wtTypeId',title:'类型',width:100,fixed:true, sortable: true,
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
				{field:'workContent',title:'工作内容',width:300, sortable: true},
				{field:'createUserName',title:'编写人',width:70,fixed:true, sortable: true},
				{field:'approveUserName',title:'批准人',width:70,fixed:true, sortable: true},
				{field:'flowStatus',title:'状态',width:100,formatter:function(val){
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
		    	var url = "${basePath}ptw/ptwStandard/updatePtwStandardMenu.do?id=" + rowData.id;
		    	
		    	addTabWithTree( "updatePtwStandard"+rowData.id, "标准工作票详细"+rowData.id, url,"ptw", "contentTb" );
		    }

	    });
		
		if(newBtnShowFlag){  //控制新建按钮的显示与隐藏
			$("#btn_add").show();
			$("#addBtn").show();
		}else{
			$("#btn_add").hide();
			$("#addBtn").hide();
		}
	
		$("#btn_add,#addBtn").click(function(){
			var url ="${basePath}ptw/ptwStandard/insertPtwStandardMenu.do";
			addTabWithTree( "addPtwStandard", "新建标准工作票", url,"ptw", "contentTb");
		});
		
		
		//表头搜索相关的
		$("#btn_advlocal").click(function() {
			modelFlag = true;
			if($(this).hasClass("active")){
				//$("#contentTb").ITCUI_GridSearch("end");
				$("#contentTb").iDatagrid("endSearch");
			}
			else{
				/* $("#contentTb").ITCUI_GridSearch("init",{"remoteSearch":true,"onParseArgs":function(arg){
					return {"search":JSON.stringify(arg)};
				}}); */
				 $("#contentTb").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(args){
		        	isSearchMode = true;
				    //强烈建议转为一个string传给后台，方便转Bean或hashMap
				    return {"search":JSON.stringify(args)};
				}});
			}
		});
		
	});
function rowStyler(rowIndex, rowData) {
		if (rowData.flowStatus.indexOf("_flow_std_status_5") >=0) {
			//已作废
			return 'color:#c0c0c0;';
		}
	
}

</script>
</head>
<body style="height: 100%;" class="bbox list-page">
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar1" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm">
	           <button type="button" class="btn btn-success" id="btn_add">新建</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	           <button type="button" class="btn btn-default" data-toggle="button" id="btn_advlocal" >查询</button>
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
			    <div class="atd_btn_pri atd_ot_create btn-group btn-group-sm margin-element">
		                 <button type="button" class="btn btn-success" id="addBtn">新建</button>
			    </div>
			</div>
		</div>
	</div>
</body>
</html>