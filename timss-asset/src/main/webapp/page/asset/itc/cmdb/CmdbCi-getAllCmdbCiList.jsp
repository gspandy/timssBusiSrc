<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html  style="height: 99%;">
<head>
<title>配置项列表</title>
<script>_useLoadingMask = true;</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src='${basePath}js/asset/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/asset/cmdbPrivCommon.js?ver=${iVersion}'></script>

<script>
//第一次打开页面
var modelFlag = false;
//查询参数
var searchParams = {};
//站点
var siteId = '${ siteId }';

function initSearch(){
	var ciEnum = FW.getEnumMap("CMDB_CI_TYPE");
	var html = "<option value=''>" + "全部" + "</option>";
	for( key in ciEnum ){
		html += "<option value='" + key + "'>" + ciEnum[key] + "</option>";
	}
	$("#ciType").append( html );
	
	$("#ciType").iCombo("init");
	$("#name").iInput("init");
	$("#responUserName").iInput("init");
	$("#unitName").iInput("init");
}

//报表
function downReport( ){
	var template = "TIMSS2_CMDB_001";
	var ciType = $("#ciType").iCombo("getVal");
	var name = $("#name").val();
	var responUserName = $("#responUserName").val();
	var unitName = $("#unitName").val();

	var url = fileExportPath + "preview?__report=report/"+template+".rptdesign&__format=xlsx&siteId=" + siteId +"&ciType="+ciType
			+ "&name=" + name + "&responUserName=" + responUserName + "&unitName=" + unitName;
	window.open(url);
}

	$(document).ready(function() {
		initSearch();
		//权限
		setCiPriv();
		
		//此处一定要给dataGrid赋值。dataGrid为全局变量，不设置会导致不能resize
		dataGrid = $("#contentTb").iDatagrid("init",{
	        pageSize:pageSize,//pageSize为全局变量，自动获取的
	        singleSelect :true,
	        queryParams : {
			    search : function (){return JSON.stringify(searchParams);},
			},
	        url: basePath + "asset/cmdbCi/queryCmdbPubCiBySiteId.do",	//basePath为全局变量，自动获取的       
	        "columns":[[
				{field:'id',title:'id',width:20,fixed:true,hidden:true},
				{field:'name',title:'CI名称',width:150,fixed:true, sortable: true},
				{field:'ciType',title:'CI类型',width:100,fixed:true, sortable: true,formatter:function(val){
					return FW.getEnumMap("CMDB_CI_TYPE")[val];
				},editor:{
					type : "combobox",
					options: {
						data : FW.parseEnumData("CMDB_CI_TYPE",_enum)
					}
				}},
				{field:'subType',title:'CI子类型',width:100,fixed:true, sortable: true },
				{field:'status',title:'CI状态',width:100,fixed:true, sortable: true},
				{field:'responUserName',title:'责任人',width:70,fixed:true, sortable: true},
				{field:'eqId',title:'资产编号',width:100,fixed:true, sortable: true},
				{field:'unitName',title:'所属单位名称',width:150,fixed:true, sortable: true},
				{field:'supplier',title:'供应商',width:90, sortable: true}
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
		    	var url = "${basePath}/asset/cmdbCi/queryCmdbPubCiDetailMenu.do?id=" + rowData.id + "&ciType=" + rowData.ciType;
		    	addTabWithTreeDataGrid( "updateCmdbCi"+rowData.id, "配置项", url,"equipment", "contentTb" );
		    }

	    });
		
		$("#btn_add,#addBtn").click(function(){
			var url ="${basePath}asset/cmdbCi/insertCmdbPubCiMenu.do";
			addTabWithTreeDataGrid( "addCmdbCi", "配置项", url,"equipment", "contentTb");
		});
		
		
		//查询
		$("#searchBtn").click(function() {
			searchParams = {};
			searchParams.ciType = $("#ciType").iCombo("getVal");
			searchParams.name = $("#name").val();
			searchParams.responUserName = $("#responUserName").val();
			searchParams.unitName = $("#unitName").val();
			
			 var pager = $("#contentTb").datagrid("getPager"); 
			 pager.pagination("select",1);
		});
		
		//导出报表
		$("#downloadBtn").click(function(){
			downReport();
		});
		
	});
</script>
</head>
<body style="height: 100%;" class="bbox list-page">
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm">
	           <button type="button" class="btn btn-success priv" id="btn_add" privilege="CMDB_CI_ADD">新建</button>
	        </div>
	        <div class="input-group input-group-sm">
			    <select type="checkbox" id="ciType" name="ciType"></select> 
			</div>
	        <div class="input-group input-group-sm" >
			    <input type="text" id="name" name="name" placeholder="CI名称"/>     
			</div>
	        <div class="input-group input-group-sm">
			    <input type="text" id="responUserName" name="responUserName"  placeholder="CI责任人"/>     
			</div>
	        <div class="input-group input-group-sm">
			    <input type="text" id="unitName" name="unitName" placeholder="所属单位"/>     
			</div>
	        <div class="btn-group btn-group-sm" style="margin-left: 8px;">
	           <button type="button" class="btn btn-default priv" privilege="CMDB_CI_SEARCH" data-toggle="button" id="searchBtn">查询</button>
	    	</div>
	        <div class="btn-group btn-group-sm">
	           <button type="button" class="btn btn-default priv" privilege="CMDB_CI_DOWNLOAD" data-toggle="button" id="downloadBtn">导出查询</button>
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
			    <div style="font-size:14px">没有配置项信息</div>
			    <div class="btn-group btn-group-sm margin-element">
		                 <button type="button" class="btn btn-success priv" privilege="CMDB_CI_ADD" id="addBtn">新建</button>
			    </div>
			</div>
		</div>
	</div>
</body>
</html>