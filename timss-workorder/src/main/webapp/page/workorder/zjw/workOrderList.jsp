<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html style="height:99%">
<title>工单列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script type="text/javascript" src="${basePath}js/workorder/common/util.js?ver=${iVersion}"></script>
<script>
	var isSearchMode = false;
	var selectEquipId = "";
	var selectEquipName="";
	var selectEquipCode = "";
	var headSearchParams = {};
	
	//表格初始化
	function initDataGrid(){
		dataGrid = $("#workorder_table").iDatagrid("init",{
			pageSize:pageSize,//pageSize为全局变量
			singleSelect:true,
			url: basePath + "workorder/workorder/workorderListdata.do",	//basePath为全局变量，自动获取的       
			columns:[[
				{field:"workOrderCode",title:"编号",width:170,fixed:true,sortable:true},
				{field:"equipSiteCode",title:"风机编号",width:70,fixed:true,sortable:true},
				{field:"woWindStation",title:"风场",width:70,fixed:true,sortable:true,
					formatter: function(value,row,index){
						return FW.getEnumMap("WO_WIND_STATION")[value];
					},
					editor : {
						"type":"combobox",
						"options" : {
							"data" : FW.parseEnumData("WO_WIND_STATION",_enum)
						}
					}
				},
				{field:"woCommitHandleStyle",title:"处理方式",width:80,fixed:true,sortable:true,
					formatter: function(value,row,index){
						return FW.getEnumMap("WO_COMMIT_HANDLE_STYLE")[value];
					},
					editor : {
						"type":"combobox",
						"options" : {
							"data" : FW.parseEnumData("WO_COMMIT_HANDLE_STYLE",_enum)
						}
					}
				},		
				{field:"description",title:"缺陷描述",width:100},
				{field:"createuser",title:"报障人",width:65,fixed:true},
				{field:"createdate",title:"报障时间",width:150,fixed:true,sortable:true,
					formatter: function(value,row,index){
						return FW.long2time(value);
					}
				},
				{field:"currHandlerUser",title:"办理人",width:65,fixed:true,sortable:true},
				{field:"currStatus",title:"状态",width:100,fixed:true,sortable:true,
					formatter: function(value,row,index){
						return FW.getEnumMap("WO_STATUS")[value];
					},
					editor : {
						"type":"combobox",
						"options" : {
							"data" : FW.parseEnumData("WO_STATUS",_enum)
						}
					}
				}
			]],
			onLoadSuccess: function(data){
				$("#noSearchResult").hide();
				$("#mainContent").hide();
				$("#grid1_empty").hide();
				if(isSearchMode){
					//搜索时的无数据信息
					if(data && data.total==0){
						$("#mainContent").show();
						$("#noSearchResult").show();
					}
					else{
						$("#mainContent").show();
						$("#noSearchResult").hide();
					}
				}
				else{
					//远程无数据需要隐藏整个表格和对应的工具条，然后在无数据信息中指引用户新建
					if(data && data.total==0){
						$("#mainContent").hide();
						$("#grid1_empty").show();
					}else{
						$("#mainContent").show();
						$("#grid1_empty").hide();
					}
				}
				$("#workorder_table").datagrid("resize");
			},
			onDblClickRow : function(rowIndex, rowData) {
				var tabId;
				if(rowData.currStatus=="DRAFT" || rowData.currStatus=="WORK_ORDER_COMMIT"){
					tabId = "workOrderAdd";
				}
				else{
					tabId = "workOrderInfo";
				}
				var woId = rowData.id;
				var rand = rowData.workOrderCode;
				var opts = {
					id : tabId + rand,
					name : "缺陷",
					url : basePath+ "workorder/workorder/openWorkOrderAddPageZJW.do?woId=" + woId,
					tabOpt : {
						closeable : true,
						afterClose : "FW.deleteTab('$arg'); FW.activeTabById('equmaintain'); FW.getFrame('equmaintain').refreshAfterClose();"
					}
				};
				_parent()._ITC.addTabWithTree(opts);
			}
		});
	}

	//页面初始化
	$(document).ready(function() {
		setWoButtonPriv();
		initWoListAssetTree();//左边设备树
		initDataGrid();  //初始化列表
		//表头搜索相关的
		$("#btn_advSearch").click(function(){
			if($(this).hasClass("active")){
				isSearchMode = false;
				$("#workorder_table").iDatagrid("endSearch");
			}
			else{
				isSearchMode = true;
				$("#workorder_table").iDatagrid("beginSearch",{
					"remoteSearch":true,
					"onParseArgs":function(args){
				    	//强烈建议转为一个string传给后台，方便转Bean或hashMap
						return {"search":JSON.stringify(args)};
					}
				});
			}
		});
	});
	
	//新建工单
	function newWO(){
		//新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
		var rand = new Date().getTime() + Math.floor(Math.abs(Math.random()*100));
		var equipParams = JSON.stringify({"equipId":selectEquipId,"equipName":selectEquipName,"equipCode":selectEquipCode});
		var urlPath = basePath+ "workorder/workorder/openWorkOrderAddPageZJW.do?woStatus=draft&equipParams="+encodeURIComponent(equipParams);
		var opts = {
			id : "workOrderAdd" + rand,
			name : "缺陷",
			url : urlPath,
			tabOpt : {
				closeable : true,
				afterClose : "FW.deleteTab('$arg'); FW.activeTabById('equmaintain');FW.getFrame('equmaintain').refreshAfterClose();"
			}
		};
		_parent()._ITC.addTabWithTree(opts);
	};
	
	//左边树菜单选择触发事件
	function passAssetSelect(data){
		if($("#btn_advSearch").hasClass("active")){ //高级查询关闭
			isSearchMode = false;
			$("#workorder_table").iDatagrid("endSearch");
			$("#btn_advSearch").removeClass("active");
		}
		headSearchParams = {};
		selectEquipId = data.id;
		selectEquipName = data.text;
		selectEquipCode = data.assetCode;
		$("#workorder_table").datagrid('options').queryParams.search = function(){
			return JSON.stringify(headSearchParams);
		};
		queryByTree();
	}
	
	//关联查询	
	function queryByTree(){
		headSearchParams.selectTreeId = selectEquipId;
		delete(_itc_grids["workorder_table"]);
		var pager = $("#workorder_table").datagrid("getPager");
		pager.pagination("select",1);
	}
	
	//刷新
	function refresh(){
		headSearchParams = {};
		$("#mainContent").show();
		$("#grid1_empty").hide();
		delete(_itc_grids["workorder_table"]);
		var pager = $("#workorder_table").datagrid("getPager");
		pager.pagination("select",1);
	}
	
	function refreshAfterClose(){
		if(FW.get("eqWOlistDoNotRefresh")){
			FW.set("eqWOlistDoNotRefresh",false);
			return;
		}
		refresh();
	}
	
	//工单权限
	function setWoButtonPriv(){
		Priv.apply();
		FW.fixRoundButtons("#toolbar");	
	}
</script>
</head>
<body style="height: 100%;" class="bbox list-page">
<div id="mainContent">
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
		<!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
		<div id="toolbar1" class="btn-toolbar ">
			<div class="btn-group btn-group-sm">
				<button type="button" class="btn btn-success priv" privilege="WO_NEW_ZJW" onclick="newWO()">新建</button>
			</div>
			<div class="btn-group btn-group-sm">
				<button type="button" id="btn_advSearch" data-toggle="button" class="btn btn-default">查询</button>
			</div>
		</div>
		<!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
		<div id="pagination_1" class="toolbar-pager" bottompager="#bottomPager">
		</div>
	</div>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div id="grid1_wrap" style="width:100%">
		<table id="workorder_table" pager="#pagination_1" class="eu-datagrid">
		</table>
	</div>
	<!-- 下页器部分-->
	<div id="bottomPager" style="width:100%;margin-top:6px">
	</div>
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
				<button type="button" class="btn btn-success priv" privilege="WO_NEW_ZJW"  onclick="newWO();">新建</button>
			</div>
		</div>
	</div>
</div>
﻿
</body>
</html>