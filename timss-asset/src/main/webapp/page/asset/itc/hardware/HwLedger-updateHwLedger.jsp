<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<script>_useLoadingMask = true;</script>
<title>硬件台账</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src='${basePath}js/asset/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/asset/commonForm.js?ver=${iVersion}'></script>
<script src='${basePath}js/asset/commonTree.js?ver=${iVersion}'></script>
<script src='${basePath}js/asset/insertHwLedger.js?ver=${iVersion}'></script>
<script src='${basePath}js/asset/assetPriv.js?ver=${iVersion}'></script>
<script src='${basePath}js/asset/searchHwLedger.js?ver=${iVersion}'></script>

<script>
//新建跳转URL
var createUrl = "";
//新建显示文本
var textVal = "";
//本节点Id
var hwId = "";
//硬件台账类型
var hwType = "";
//是否根节点
var rootFlag = false;
//节点名字
var nodeName = "";
//parentId 
var parentId = "";

var isCreateGroupBtn;//是否有按钮组，否则用按钮

//接收树节点数据
function toShow( node ){
	FW.showMask();
	hwType = node.hwType;
	hwId = node.hwId;
	nodeName = node.hwName;
	parentId = node.parentId;
	
	if( node.isRoot == 'Y'){
		$("#rootForm").iForm("init",{"options":opts,"fields":rootFields});
		rootFlag = true;
		//加载根节点
		loadRoot( );
	}else{
		rootFlag = false;
		if("HW_L_ADDRESS,HW_L_ROOM,HW_L_CABINET".indexOf( hwType ) >= 0 ){
			$("#baseInfoForm").iForm("init",{"options":opts,"fields":baseInfoFields});
			$("#baseInfoForm").iForm("hide",["location", "relatedBusiness", "remarks"]);
			//加载非根节点
			loadData( node.hwId );
		}else if( "HW_L_SERVER" == hwType ){
			$("#baseInfoForm").iForm("show",["location", "relatedBusiness", "remarks"]);
			initService();
            
        }else if( "HW_L_VM" == hwType ){
        	$("#baseInfoForm").iForm("show",[ "ownOrg", "status", "remarks"]);
        	$("#baseInfoForm").iForm("hide",["location"]);
        	initVM();
        }
        /**
        这里加入树节点为机房设备、存储设备、网络设备时，跳转到详情页面的逻辑
        **/
        else{
        	FW.navigate(basePath+"asset/hwLedger/detailPage.do?hwLedgerId="+hwId+"&hwLedgerType="+hwType+"&mode=view");
        }
	}
	//控制按钮
	controlBtnShow( node );
}

//控制按钮的显示
function controlBtnShow( node ){
	var url = basePath + "asset/hwLedger/insertHwLedgerPage.do?";
	//显示title
	var titleVal = "";
	//权限
	HwLedgerPriv.init();
	
	isCreateGroupBtn=false;
	var isCreate = false;
	var isEdit = false;
	if( $("#createButton").is(":hidden")&&$("#createGroupButtonDiv").is(":hidden") ){
		isCreate = true;
	}
	
	if( $("#editButton").is(":hidden") ){
		isEdit = true;
	}
	
	if( node.isRoot == 'Y' ){
		textVal = "新建物理地点";
		url += "hwType=HW_L_ADDRESS&parentId=" + node.hwId;
		
		titleVal = "硬件台账汇总";
		if( !isCreate ){
			$("#createButton").show();
		}
		$("#createGroupButtonDiv").hide();
		$("#baseInfoFormDiv").hide();
		$("#editButton").hide();
		$("#rootFormDiv").show();
		$("#saveButton").hide();
		$("#serviceAllDiv").hide();
		$("#vmDiv").hide();
		$("#swDiv").hide();
	}else if( node.hwType == "HW_L_ADDRESS" ){
		textVal = "新建机房";
		url += "hwType=HW_L_ROOM&parentId=" + node.hwId;
		titleVal = "物理地点信息";
		
		if( !isCreate ){
			$("#createButton").show();
		}
		$("#baseInfoFormDiv").show();
		if( !isEdit ){
			$("#editButton").show();
		}
		$("#createGroupButtonDiv").hide();
		$("#rootFormDiv").hide();
		$("#saveButton").hide();
		$("#serviceAllDiv").hide();
		$("#vmDiv").hide();
		$("#swDiv").hide();
	}else if( node.hwType == "HW_L_ROOM" ){
		textVal = "新建机柜";
		url += "hwType=HW_L_CABINET&parentId=" + node.hwId;
		titleVal = "机房信息";
		isCreateGroupBtn=true;
		
		$("#createButton").hide();
		$("#cabinetLi").show();
		if( !isCreate ){
			$("#createGroupButtonDiv").show();
		}
		$("#baseInfoFormDiv").show();
		if( !isEdit ){
			$("#editButton").show();
		}
		$("#rootFormDiv").hide();
		$("#saveButton").hide();
		$("#serviceAllDiv").hide();
		$("#vmDiv").hide();
		$("#swDiv").hide();
	}else if( node.hwType == "HW_L_CABINET" ){
		textVal = "新建服务器";
		url += "hwType=HW_L_SERVER&parentId=" + node.hwId;
		titleVal = "机柜信息";
		isCreateGroupBtn=true;
		
		$("#createButton").hide();
		$("#cabinetLi").hide();
		if( !isCreate ){
			$("#createGroupButtonDiv").show();
		}
		$("#baseInfoFormDiv").show();
		if( !isEdit ){
			$("#editButton").show();
		}
		$("#rootFormDiv").hide();
		$("#saveButton").hide();
		$("#serviceAllDiv").hide();
		$("#vmDiv").hide();
		$("#swDiv").hide();
	}else if( node.hwType == "HW_L_SERVER" ){
		textVal = "新建虚机";
		url += "hwType=HW_L_VM&parentId=" + node.hwId;
		titleVal = "服务器信息";
		
		if( !isCreate ){
			$("#createButton").show();
		}
		$("#baseInfoFormDiv").show();
		if( !isEdit ){
			$("#editButton").show();
		}
		$("#createGroupButtonDiv").hide();
		$("#rootFormDiv").hide();
		$("#saveButton").hide();
		$("#serviceAllDiv").show();
		$("#vmDiv").hide();
		$("#deviceDiv").show();
		$("#serviceDiv").show();
		$("#swDiv").show();
	}else if( node.hwType == "HW_L_VM" ){
		titleVal = "虚机信息";
		$("#createButton").hide();
		$("#createGroupButtonDiv").hide();
		$("#baseInfoFormDiv").show();
		if( !isEdit ){
			$("#editButton").show();
		}
		$("#rootFormDiv").hide();
		$("#saveButton").hide();
		$("#serviceAllDiv").show();
		$("#vmDiv").show();
		$("#deviceDiv").hide();
		$("#serviceDiv").show();
		$("#swDiv").show();
	}
	
	createUrl = url;
	if(titleVal != "硬件台账汇总")titleVal="硬件台账详情";
	$("#titleDiv").html( titleVal );
	$("#deleteButton").hide();
	$("#backButton").hide();
	$("#createButton").text( textVal );
	
	FW.fixToolbar("#toolbar");
	FW.removeMask();
}

//加载根节点
function loadRoot(){
	var url = basePath + "asset/hwLedger/queryHwLedgerByRoot.do";
	$.ajax({
		url : url,
		type : 'post',
		dataType : "json",
		success : function(data) {
			loadRootForm( data.result );
		}
	});
}

//开始没有点击root节点
function initRoot(){
	var flag = window.parent.document.getElementById("assetHwTree").contentWindow.$;
	if( flag != null ) {
		var tree = getTree();
		if( tree.length != null && tree.length > 0 ){
			var node = tree.tree('getSelected');
			toShow( node.obj );
		}
	}else{
		var url = basePath + "asset/hwLedger/queryHwLedgerTree.do";
		$.ajax({
			url : url,
			type : 'post',
			dataType : "json",
			success : function(data) {
				toShow( data.parent );
			}
		});
	}
	
}

//加载根数据
function loadRootForm( data ){
	var dataArr = {};
	for( var index in data ){
		var dataMap = data[index];
		if( dataMap.HWTYPE == "HW_L_ADDRESS" ){
			dataArr.address = dataMap.HWTYPECOUNT;
		}else if( dataMap.HWTYPE == "HW_L_ROOM" ){
			dataArr.room = dataMap.HWTYPECOUNT;
		}else if( dataMap.HWTYPE == "HW_L_CABINET" ){
			dataArr.cabinet = dataMap.HWTYPECOUNT;
		}else if( dataMap.HWTYPE == "HW_L_SERVER" ){
			dataArr.server = dataMap.HWTYPECOUNT;
		}else if( dataMap.HWTYPE == "HW_L_VM" ){
			dataArr.vm = dataMap.HWTYPECOUNT;
		}else if( dataMap.HWTYPE == "HW_L_ROOM_EQPT" ){
			dataArr.roomEqpt = dataMap.HWTYPECOUNT;
		}else if( dataMap.HWTYPE == "HW_L_NETWORK" ){
			dataArr.networkEqpt = dataMap.HWTYPECOUNT;
		}else if( dataMap.HWTYPE == "HW_L_STORAGE" ){
			dataArr.storageEqpt = dataMap.HWTYPECOUNT;
		}
	}
	$("#rootForm").iForm("setVal",dataArr);
	$("#rootForm").iForm("endEdit");
	
}

//通过Id加载form
function loadData( id ){
	var url = basePath + "asset/hwLedger/queryHwLedgerById.do?id=" + id;
	$.ajax({
		url : url,
		type : 'post',
		dataType : "json",
		success : function(data) {
			if( data.result == "success" ){
				loadForm( data.baseInfoVo );
			}else{
				FW.error( "加载数据失败 ！");
			}
		}
	});
}

//初始化form
function loadForm( rowData ){
	var data = {
			"hwId" :rowData.hwId,
			"hwName" : rowData.hwName,
			"hwType" : rowData.hwType,
			"parentId" : rowData.parentId,
			"parentName":rowData.parentName
		};
	
	$("#baseInfoForm").iForm("setVal",data);
	$("#baseInfoForm").iForm("endEdit");
}

//初始化服务器form
function initService(){
	initHwModelHint();
	loadServiceData( hwId );
	initSwDatagrid( hwId );
}

//初始化vmform
function initVM(){
	initHwModelHint();
	loadVMData( hwId );
	initSwDatagrid( hwId );
}

//初始化所有form
function initAllForm( ){
	$("#deviceFormDiv").iFold("init");
	$("#serviceFormDiv").iFold("init");
	$("#sysFormDiv").iFold("init");
	$("#netFormDiv").iFold("init");
	$("#backupFormDiv").iFold("init");
	$("#driverFormDiv").iFold("init");
	$("#vmFormDiv").iFold("init");
	$("#swDatagridDiv").iFold("init");

	$("#baseInfoForm").iForm("init",{"options":opts,"fields":baseInfoFields});
	$("#deviceForm").iForm("init",{"options":opts,"fields":serviceFields});
	$("#serviceForm").iForm("init",{"options":opts,"fields":serviceInfoFields});
	$("#sysForm").iForm("init",{"options":opts,"fields":sysFields});
	$("#netForm").iForm("init",{"options":opts,"fields":netFields});
	$("#backupForm").iForm("init",{"options":opts,"fields":backupFields});
	$("#driverForm").iForm("init",{"options":opts,"fields":driverFields});
	$("#vmForm").iForm("init",{"options":opts,"fields":vmFields});
}
	  		
var rootFields = [
				{title : "物理地点", id : "address",type: "label" },
				{title : "机房", id : "room",type: "label" },
				{title : "机柜", id : "cabinet",type: "label" },
				{title : "服务器", id : "server",type: "label" },
				{title : "虚机", id : "vm",type: "label" },
				{title : "机房设备", id : "roomEqpt",type: "label" },
				{title : "网络设备", id : "networkEqpt",type: "label" },
				{title : "存储设备", id : "storageEqpt",type: "label" }
	  		];
	  		
	  		
	var opts={
		validate:true,
		fixLabelWidth:true,
		labelFixWidth:150
	};
	
	
	
	//重新加载form数据
	function reloadFormData( data ){
		var hwLedgerBean = data.hwLedgerBean;
		hwType = hwLedgerBean.hwType;
		controlBtnShow( data.baseInfoVo );
		
		if("HW_L_ADDRESS,HW_L_ROOM,HW_L_CABINET".indexOf( hwType ) >= 0 ){
			loadBaseInfoVoForm( data.baseInfoVo );
			
		}else if( "HW_L_SERVER" == hwType ){
			loadServiceFormData( data );
            
        }else if( "HW_L_VM" == hwType ){
        	loadVMFormData( data );
        }
		
		updateTreeNode( data.baseInfoVo );
	}
	
	//部署的软件台账
	function initSwDatagrid( hwId ){
		var url = basePath + "asset/hwLedger/querySwLedgerById.do?hwId=" + hwId;
		$("#swDatagrid").iDatagrid("init",{
	        singleSelect :true,
	        url: url,    
	        "columns":[[
				{field:'appId',title:'appId',width:20,fixed:true,hidden:true},
				{field:'swName',title:'软件台账',width:200,fixed:true,formatter:function(val, row, index){
					return row.swl.swName;
				}},
				{field:'appType',title:'类型',width:90,fixed:true,formatter:function(val){
					return FW.getEnumMap("AST_SW_APP_TYPE")[val];
				}
				},
				{field:'appName',title:'应用名称',width:150,fixed:true},
				{field:'path',title:'路径',width:200},
				{field:'port',title:'端口号',width:90,fixed:true}
				]],
			"onLoadError": function(){
				//加载错误的提示 可以根据需要添加
				$("#grid1_wrap,#toolbar_wrap,#bottomPager").hide();
			    $("#grid1_error").show();
			},
	        onLoadSuccess: function(data){
	            //远程无数据需要隐藏整个表格和对应的工具条，然后在无数据信息中指引用户新建
	            if(data && data.total==0){
                	$("#swDiv").hide();
                	$("#swDatagridDiv").iFold("hide");
	               /*  $("#grid1_empty").show();
	                $("#noResult").hide(); */
	            }else{
	            	$("#swDiv").show();
	            	$("#swDatagridDiv").iFold("show");
	            	$("#swDatagrid").datagrid("resize");
	               /*  $("#grid1_empty").hide();
	                $("#noResult").hide(); */
	            }
	        }
	    });
	}
	
	//按钮组新建
	function createGroupClick( hwType ,title ){
		var url = basePath + "asset/hwLedger/insertHwLedgerPage.do?hwType=" + hwType + "&parentId=" + hwId;
		addTabWithTree( "addHwLedger" + hwId , "硬件台账", url,"equipment", hwId);
	}
	
	function createHwLedger(hwType){
		FW.navigate(basePath+"asset/hwLedger/detailPage.do?&hwLedgerType="+hwType+"&mode=create");
	}
	
	$(document).ready(function() {
		initAssetHwTree();
		//初始化所有form
		initAllForm();
		
		//刚刚开始没有点击根，初始化页面
		initRoot();
		
		//点击保存
		$( "#saveButton" ).click(function(){
			var url = "";
			var params = {};
			if( "HW_L_ADDRESS,HW_L_ROOM,HW_L_CABINET".indexOf( hwType ) >= 0 && rootFlag == false ){
				if(!$("#baseInfoForm").valid()){
					return;
				}
				
				var formData = getFormData( "baseInfoForm" );
				url = basePath + "asset/hwLedger/insertOrUpdateHwLedger.do";
				params.hwType = hwType;
				params.formData = formData;
			}if( hwType == "HW_L_SERVER" && rootFlag == false ){
				params = getServiceAllForm( );
				url = basePath + "asset/hwLedger/insertOrUpdateServiceHw.do";
				params.hwType = hwType;
			}if( hwType == "HW_L_VM" && rootFlag == false ){
				params = getVMAllForm( );
				url = basePath + "asset/hwLedger/insertOrUpdateVMHw.do";
				params.hwType = hwType;
			}
			
			$.ajax({
				url : url,
				type : 'post',
				dataType : "json",
				data : params,
				success : function(data) {
					if( data.result == "success" ){
						FW.success( "保存成功 ！");
						$("#editButton,#searchButton").show();
						if(isCreateGroupBtn){
							$("#createGroupButtonDiv").show();
						}else{
							$("#createButton").show();
						}
						//重新加载form
						reloadFormData( data );
					}else{
						if( data.reason != null ){
							FW.error( data.reason );
						}else {
							FW.error( "保存失败 ！");
						}
					}
				}
			});
		});
		
		//点击新建
		$("#createButton").click(function(){
			addTabWithTree( "addHwLedger" + hwId , "硬件台账", createUrl,"equipment", hwId);
		});
		
		//编辑
		$("#editButton").click( function(){
			$("#titleDiv").html( "编辑硬件台账" );
			$("#backButton").show();
			$("#saveButton").show();
			$("#deleteButton").show();
			$("#editButton").hide();
			$("#createGroupButtonDiv,#createButton,#searchButton").hide();
			
			if( hwType == "HW_L_ADDRESS" && rootFlag == false ){
				$("#baseInfoForm").iForm("beginEdit",["hwName"]);
			}else if( hwType == "HW_L_ROOM" && rootFlag == false ){
				$("#baseInfoForm").iForm("beginEdit",["hwName"]);
			}if( hwType == "HW_L_CABINET" && rootFlag == false ){
				$("#baseInfoForm").iForm("beginEdit",["hwName"]);
			}if( hwType == "HW_L_SERVER" && rootFlag == false ){
				$("#baseInfoForm").iForm("beginEdit",["location", "hwName", "relatedBusiness", "remarks"]);
				$("#deviceForm").iForm("beginEdit");
				$("#serviceForm").iForm("beginEdit");
				$("#sysForm").iForm("beginEdit");
				$("#netForm").iForm("beginEdit");
				$("#backupForm").iForm("beginEdit");
				$("#driverForm").iForm("beginEdit");
			}if( hwType == "HW_L_VM" && rootFlag == false ){
				$("#baseInfoForm").iForm("beginEdit",["hwName", "relatedBusiness", "remarks"]);
				$("#baseInfoForm").iForm("hide",["location"]);
				$("#serviceForm").iForm("beginEdit");
				$("#vmForm").iForm("beginEdit");
				$("#sysForm").iForm("beginEdit");
				$("#netForm").iForm("beginEdit");
				$("#backupForm").iForm("beginEdit");
				$("#driverForm").iForm("beginEdit");
			}
			FW.fixToolbar("#toolbar");
		});
		
		//返回
		$("#backButton").click( function(){
			$("#titleDiv").html( "硬件台账详情" );
			$("#saveButton").hide();
			$("#deleteButton").hide();
			$("#backButton").hide();
			$("#editButton,#searchButton").show();
			if(isCreateGroupBtn){
				$("#createGroupButtonDiv").show();
			}else{
				$("#createButton").show();
			}
			
			if( hwType == "HW_L_ADDRESS" && rootFlag == false ){
				$("#baseInfoForm").iForm("endEdit");
			}else if( hwType == "HW_L_ROOM" && rootFlag == false ){
				$("#baseInfoForm").iForm("endEdit");
			}if( hwType == "HW_L_CABINET" && rootFlag == false ){
				$("#baseInfoForm").iForm("endEdit");
			}if( hwType == "HW_L_SERVER" && rootFlag == false ){
				$("#baseInfoForm").iForm("endEdit");
				$("#deviceForm").iForm("endEdit");
				$("#serviceForm").iForm("endEdit");
				$("#sysForm").iForm("endEdit");
				$("#netForm").iForm("endEdit");
				$("#backupForm").iForm("endEdit");
				$("#driverForm").iForm("endEdit");
			}if( hwType == "HW_L_VM" && rootFlag == false ){
				$("#baseInfoForm").iForm("endEdit");
				$("#baseInfoForm").iForm("endEdit");
				$("#serviceForm").iForm("endEdit");
				$("#vmForm").iForm("endEdit");
				$("#sysForm").iForm("endEdit");
				$("#netForm").iForm("endEdit");
				$("#backupForm").iForm("endEdit");
				$("#driverForm").iForm("endEdit");
				$("#createButton").hide();
			}
			FW.fixToolbar("#toolbar");
		});
		
		
		//点击删除
		$("#deleteButton").click(function(){
			if(rootFlag){
				//根节点不可删除
				FW.error("根节点不可删除");
				return;
			}
			FW.confirm("确定删除" + nodeName + "节点及其子节点吗？该操作无法恢复。", function() {
				var url = basePath + "asset/hwLedger/deleteHwLedger.do?hwType=" + hwType + "&hwId=" + hwId;
				$.ajax({
					url : url,
					type : 'post',
					dataType : "json",
					success : function(data) {
						if( data.result == "success" ){
							FW.success("删除成功！");
				        	deleteTreeNode( parentId );
						}else{
							FW.error( "保存失败 ！");
						}
					}
				});
			});
		});
		
		//查询
		$("#searchButtonDiv").click(function(){
			showDtlIframe( hwId, nodeName );
		});
		
	});
		
		function exportHwLedgers(i){
			var exportPath=fileExportPath;
			//exportPath="http://10.133.96.77:8091/itc_report/";//测试用
			//exportPath="http://10.0.17.153:8080/itc_report/";
			
			var list=[//在这里添加要下载的链接
				//exportPath+"preview?__report=Report/ITSM-03-CM-01-04.rptdesign&__format=xls",
				//exportPath+"preview?__report=Report/ITSM-03-CM-01-05.rptdesign&__format=xls",
				//exportPath+"preview?__report=Report/ITSM-03-CM-01-06.rptdesign&__format=xls",
				//exportPath+"preview?__report=Report/ITSM-03-CM-01-07.rptdesign&__format=xls",
				exportPath+"preview?__report=report/ITSM-03-CM-01-08.rptdesign&__format=xlsx"
			];
			
			
			if(i<list.length){
				window.open(list[i]);
				if(i!=list.length-1)
					window.setTimeout('exportHwLedgers('+(i+1)+')',50);
			}
		}
</script>

</head>
<body>
<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <div id="toolbar" class="btn-toolbar ">
	    
	    	<div class="btn-group btn-group-sm margin-element " privilege="AST_HWL_NEW" >
	        	 <button type="button" class="btn btn-success dropdown-toggle priv"  id="createGroupButtonDiv"  data-toggle="dropdown">
	        	 	新建
	        	 	<span class="caret"></span> <!-- 添加"新建"文字右边的"下箭头" -->
	        	 </button>
	        	 <ul class="dropdown-menu">
		            <li id="cabinetLi" ><a onclick="createGroupClick( 'HW_L_CABINET' ,'新建机柜' )">机柜</a></li>
		            <li id="serverLi" ><a onclick="createGroupClick( 'HW_L_SERVER' ,'新建服务器' )">服务器</a></li>
		            <li ><a onclick="createHwLedger('HW_L_NETWORK')">网络设备</a></li>
		            <li ><a onclick="createHwLedger('HW_L_STORAGE')">存储设备</a></li>
		            <li><a onclick="createHwLedger('HW_L_ROOM_EQPT')">机房设备</a></li>
		        </ul>
	        </div>
	    
	        <div class="btn-group btn-group-sm" id="createButtonDiv">
	            <button type="button" class="btn btn-success priv" privilege="AST_HWL_NEW" id="createButton">新建</button>
	        </div>
	        <div class="btn-group btn-group-sm" id="backButtonDiv">
	            <button type="button" class="btn btn-default" id="backButton">返回</button>
	        </div>
	        <div class="btn-group btn-group-sm" id="saveButtonDiv" >
	            <button type="button" class="btn btn-default" id="saveButton">保存</button>
            </div>
	        <div class="btn-group btn-group-sm" id="deleteButtonDiv" >
	            <button type="button" class="btn btn-default" id="deleteButton">删除</button>
            </div>
	        <div class="btn-group btn-group-sm" id="editButtonDiv">
	            <button type="button" class="btn btn-default priv" privilege="AST_HWL_EDIT" id="editButton">编辑</button>
            </div>
	        <div class="btn-group btn-group-sm" id="searchButtonDiv">
	            <button type="button" class="btn btn-default priv" privilege="" id="searchButton">查询</button>
            </div>
            <div class="btn-group btn-group-sm" id="exportButtonDiv">
	            <button type="button" class="btn btn-default priv" privilege="AST_HWL_EXPORT" id="exportButton" onclick='exportHwLedgers(0);'>导出硬件台账</button>
            </div>
	    </div>
	</div>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div class="inner-title" id="titleDiv">
		新建硬件台账
	</div>
<!-- 	<div id="baseFormDiv">
		<form id="autoform"></form>
	</div> -->
	
	<div id="rootFormDiv">
		<form id="rootForm"></form>
	</div>
	
	
	<div id="baseInfoFormDiv">
		<form id="baseInfoForm"></form>
	</div>

	<div id="vmDiv">
		<div id="vmFormDiv" grouptitle="虚拟化信息">
			<div class="margin-title-table">
				<form id="vmForm"></form>
			</div>
		</div>
    </div>
	    
	<div id="serviceAllDiv">
		<div id="deviceDiv">
			<div id="deviceFormDiv" grouptitle="服务器硬件信息">
				<div class="margin-title-table">
					<form id="deviceForm"></form>
				</div>
			</div>
	    </div>
    
		<div id="serviceDiv">
			<div id="serviceFormDiv" grouptitle="服务信息">
				<div class="margin-title-table">
					<form id="serviceForm"></form>
				</div>
			</div>
	    </div>
    
		<div id="sysDiv">
			<div id="sysFormDiv" grouptitle="系统信息">
				<div class="margin-title-table">
					<form id="sysForm"></form>
				</div>
			</div>
	    </div>
    
		<div id="netDiv">
			<div id="netFormDiv" grouptitle="网络信息">
				<div class="margin-title-table">
					<form id="netForm"></form>
				</div>
			</div>
	    </div>
    
		<div id="backupDiv">
			<div id="backupFormDiv" grouptitle="备份信息">
				<div class="margin-title-table">
					<form id="backupForm"></form>
				</div>
			</div>
	    </div>
    
		<div id="driverDiv">
			<div id="driverFormDiv" grouptitle="存储信息">
				<div class="margin-title-table">
					<form id="driverForm"></form>
				</div>
			</div>
	    </div>
    
    <div id="swDiv">
	    <div grouptitle="部署的软件台账" id="swDatagridDiv">
			<form class="margin-title-table" id="swFormDatagrid">
				<table id="swDatagrid" style="" class="eu-datagrid"></table>
			</form>
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
				    <div style="font-size:14px">没有部署的软件台账数据</div>
				</div>
			</div>
		</div>
    </div>
	
    </div>
</body>
</html>