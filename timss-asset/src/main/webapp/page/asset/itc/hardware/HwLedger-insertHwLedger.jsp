<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>硬件台账</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src='${basePath}js/asset/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/asset/commonForm.js?ver=${iVersion}'></script>
<script src='${basePath}js/asset/commonTree.js?ver=${iVersion}'></script>
<script src='${basePath}js/asset/insertHwLedger.js?ver=${iVersion}'></script>

<script>
//硬件台账类型
var hwType = '${ hwType }';
//父节点ID
var parentId = "${ parentId }";
//父节点名称
var parentName = "${ parentName }";

var opts={
		validate:true,
		fixLabelWidth:true,
		labelFixWidth:150
	};

//初始化form
function setHwLedgerForm( ){
	var data = {
			"hwType" : hwType,
			"parentId" : parentId,
			"parentName" : parentName
		};
	
	$("#baseInfoForm").iForm("setVal",data);
	$("#baseInfoForm").iForm("endEdit");
	$("#baseInfoForm").iForm("beginEdit",["hwName"]);
	$("#baseInfoForm").iForm("hide",["location", "relatedBusiness", "remarks"]);
}


	
	//初始化服务器form
	function initService(){
		$("#deviceFormDiv").iFold("init");
		$("#serviceFormDiv").iFold("init");
		$("#sysFormDiv").iFold("init");
		$("#netFormDiv").iFold("init");
		$("#backupFormDiv").iFold("init");
		$("#driverFormDiv").iFold("init");

		$("#baseInfoForm").iForm("init",{"options":opts,"fields":baseInfoFields});
		$("#deviceForm").iForm("init",{"options":opts,"fields":serviceFields});
		$("#serviceForm").iForm("init",{"options":opts,"fields":serviceInfoFields});
		$("#sysForm").iForm("init",{"options":opts,"fields":sysFields});
		$("#netForm").iForm("init",{"options":opts,"fields":netFields});
		$("#backupForm").iForm("init",{"options":opts,"fields":backupFields});
		$("#driverForm").iForm("init",{"options":opts,"fields":driverFields});
		
		var data = {
				"hwType" : hwType,
				"parentName" : parentName,
				"parentId" : parentId
			};
		
		$("#baseInfoForm").iForm("setVal",data);
		$("#baseInfoForm").iForm("endEdit",["hwType", "parentId","parentName"]);
		$("#serviceAllDiv").show();
		$("#vmDiv").hide();
		initHwModelHint();
		
	}
	
	//初始化虚机form
	function initVM(){
		$("#sysFormDiv").iFold("init");
		$("#serviceFormDiv").iFold("init");
		$("#netFormDiv").iFold("init");
		$("#backupFormDiv").iFold("init");
		$("#driverFormDiv").iFold("init");
		$("#vmFormDiv").iFold("init");

		$("#baseInfoForm").iForm("init",{"options":opts,"fields":baseInfoFields});
		$("#serviceForm").iForm("init",{"options":opts,"fields":serviceInfoFields});
		$("#sysForm").iForm("init",{"options":opts,"fields":sysFields});
		$("#netForm").iForm("init",{"options":opts,"fields":netFields});
		$("#backupForm").iForm("init",{"options":opts,"fields":backupFields});
		$("#driverForm").iForm("init",{"options":opts,"fields":driverFields});
		$("#vmForm").iForm("init",{"options":opts,"fields":vmFields});
		
		var data = {
				"hwType" : hwType,
				"parentName" : parentName,
				"parentId" : parentId
			};
		
		$("#baseInfoForm").iForm("setVal",data);
		$("#baseInfoForm").iForm("endEdit",["hwType", "parentId","parentName"]);
		$("#baseInfoForm").iForm("hide",["location"]);
		$("#serviceAllDiv").show();
		$("#deviceDiv").hide();
		$("#serviceDiv").show();
		initHwModelHint();
		
	}
	
	
	$(document).ready(function() {
		initAssetHwTree();
		
		if( "HW_L_ADDRESS,HW_L_ROOM,HW_L_CABINET".indexOf(hwType) >= 0 ){
			$("#baseInfoForm").iForm("init",{"options":opts,"fields":baseInfoFields});
			$("#serviceAllDiv").hide();
			$("#baseFormDiv").show();
			setHwLedgerForm();
		}else if( "HW_L_SERVER" == hwType ){
			initService();
		}else if(  "HW_L_VM" == hwType ){
			initVM();
		}
		
		//点击保存
		$( "#saveButton" ).click(function(){
			var url = "";
			var params = {};
			//基本类型
			if( "HW_L_ADDRESS,HW_L_ROOM,HW_L_CABINET".indexOf( hwType ) >= 0){
				if(!$("#baseInfoForm").valid()){
					return;
				}
				
				var formData = getFormData( "baseInfoForm" );
				url = basePath + "asset/hwLedger/insertOrUpdateHwLedger.do";
				params.hwType = hwType;
				params.formData = formData;
			}if( hwType == "HW_L_SERVER"){
				params = getServiceAllForm( );
				url = basePath + "asset/hwLedger/insertOrUpdateServiceHw.do";
				params.hwType = hwType;
			}if( hwType == "HW_L_VM"  ){
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
						FW.success( "新增成功 ！");
						addTreeNode( data.baseInfoVo );
						closeTab();
					}else{
						if( data.reason != null ){
							FW.error( data.reason );
						}else {
							FW.error( "新增失败 ！");
						}
					}
				},complete: function (jqXHR, textStatus){
					
				}
			});
		});
		
	});
		
</script>

</head>
<body>
<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <div id="toolbar" class="btn-toolbar ">
	        <!-- <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default" id="createButton">新建</button>
	        </div> -->
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default" onclick="closeTab()">返回</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-success" id="saveButton">保存</button>
            </div>
	        <!-- <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-success" id="editButton">编辑</button>
            </div> -->
	    </div>
	</div>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div class="inner-title">
		新建硬件台账
	</div>
	<!-- <div id="baseFormDiv">
		<form id="autoform"></form>
	</div> -->
	
	
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
    
    </div>
</body>
</html>