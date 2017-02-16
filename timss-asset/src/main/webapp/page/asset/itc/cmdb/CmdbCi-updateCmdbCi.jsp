<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>配置项详情</title>
<script>_useLoadingMask = true;</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src='${basePath}js/asset/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/asset/commonForm.js?ver=${iVersion}'></script>
<script src='${basePath}js/asset/cmdbPrivCommon.js?ver=${iVersion}'></script>
<script src='${basePath}js/asset/cmdbCommon.js?ver=${iVersion}'></script>
<script src='${basePath}js/asset/commonDialog.js?ver=${iVersion}'></script>

<script>
//formId 可改变
var formId = "autoform_" + "${ciType}";
//CI Id
var cId = '${ id }';

var subTypeList = '${subTypeList}';
var statusList = '${statusList}';

/**** -----------------------------------------------动态表单 start ------------------------------ */
var pageCode = "itc_cmdb_formcode";
var formOptions = opts;
var formSearchParam = { "id" : cId, "sheetId" : cId, "formSwitch" : "on"};
var formUrlParam = basePath + "asset/cmdbCi/queryCmdbPubCiById.do";
/**** -----------------------------------------------动态表单 end ------------------------------ */

//初始化插入的表单
function initInsertForm(){
	$.ajaxSetup({async:false});
	FW.dynamicForm(updateFields, formOptions, formSearchParam, formUrlParam);
	
	var iHintUrl = url = basePath + "asset/cmdbCi/searchCmdbParamsHint.do?paramType=CMDB_PARAM_5";
	searchHint( "unitId", "unitName", iHintUrl );
	//A端 iHint
	var aPortHintUrl = basePath + "asset/cmdbCi/queryHintCmdbByCiTypeAndName.do?ciType=CMDB_TYPE_8";
	searchHint( "aportId", "aportName", aPortHintUrl );
	//B端 iHint
	var bPortHintUrl = basePath + "asset/cmdbCi/queryHintCmdbByCiTypeAndName.do?ciType=CMDB_TYPE_8";
	searchHint( "bportId", "bportName", bPortHintUrl );
	//机房 iHint
	var bPortHintUrl = basePath + "asset/cmdbCi/queryHintCmdbByCiTypeAndName.do?ciType=CMDB_TYPE_11";
	searchHint( "engineRoomId", "engineRoomName", bPortHintUrl );
	//所在机柜iHint
	var cabinetHintUrl = basePath + "asset/cmdbCi/queryHintCmdbCabinetByName.do";
	searchHint( "cabinetId", "cabinetName", cabinetHintUrl );
	//供应商 iHint
	var supplierUrl = basePath + "asset/cmdbCi/queryHintSupplierByName.do";
	searchSupplierHint( "supplierId", "supplier", supplierUrl );
	
	//CI子类型
	var  subArr = [["","请选择"]];
	//CI状态
	var  statusArr = [["","请选择"]];
	
	subTypeList = JSON.parse(subTypeList);
	statusList = JSON.parse(statusList);
	
	for( var index in subTypeList ){
		subArr.push( [ subTypeList[index].id, subTypeList[index].paramVal ] );
	}
	//初始化CI子类型
	$("#f_subType" ).iCombo("init", {
		data : subArr
	});
	for( var index in statusList ){
		statusArr.push( [ statusList[index].id, statusList[index].paramVal ] );
	}
	//初始化CI状态枚举
	$("#f_status" ).iCombo("init", {
		data : statusArr
	});
	
	//初始化数据
	loadFormData( cId );
	$.ajaxSetup({async:true});
}
	
	$(document).ready(function() {
		$(".changFormId").attr("id", formId );
		//初始化form表单
		initInsertForm();
		
		//保存
		$( "#saveButton" ).click(function(){
			changeDtlIframe();
		});
		
		//编辑
		$("#editButton").click(function(){
			$(".inner-title").html("编辑配置项");
			$( "#" + formId ).iForm("beginEdit");
			$( "#" + formId ).iForm("endEdit",["ciType"]);
			$("#backButtonDiv, #deleteButtonDiv, #saveButtonDiv").show();
			$("#closeButtonDiv, #editButtonDiv, #logButtonDiv, #relationButtonDiv").hide();
			FW.fixRoundButtons("#toolbar");
		});
		
		//删除
		$("#deleteButton").click(function(){
			var url = basePath + "asset/cmdbCi/deleteCmdbPubCiById.do?id=" + cId;
			FW.confirm("确定删除本条数据吗？该操作无法恢复。", function() {
				$.ajax({
					url : url,
					type : 'post',
					dataType : "json",
					success : function(data) {
						if( data.result == "success" ){
							FW.success( "删除成功 ！");
							//resetForm();
							closeTab();
						}else{
							FW.error( "删除失败 ！");
						}
					}
				});
			});
			
		});
		
		//返回
		$("#backButton").click(function(){
			loadFormData( cId );
		});
		
		//日志
		$("#logButton").click(function(){
			var url ="${basePath}asset/cmdbCi/queryCmdbCiLogMenu.do?ciId=" + cId;
			showDialogIframeOnlyCancel(800, 550, "CI日志列表", url , null );
		});
		
		//关联关系
		$("#relationButton").click(function(){
			var url ="${basePath}asset/cmdbCi/queryCmdbRelationMenu.do?ciId=" + cId;
			addTabWithTree( "downUpDownCmdbRelation" + cId, "关联关系", url,"updateCmdbCi" + cId, "contentTb");
		});
		
		//配置表单
		$("#dfromButton").click(function(){
			FW.openDDBox();
		});
	});
		
</script>

</head>
<body>
<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <div id="toolbar" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm" id="closeButtonDiv">
	            <button type="button" class="btn btn-default priv" privilege="CMDB_CI_CLOSEUPDATE" id="closeButton" onclick="closeTab()">关闭</button>
	        </div>
	        <div class="btn-group btn-group-sm" id="backButtonDiv">
	            <button type="button" class="btn btn-default priv" privilege="CMDB_CI_BACKUPDATE" id="backButton">取消</button>
	        </div>
	        <div class="btn-group btn-group-sm" id="editButtonDiv">
	            <button type="button" class="btn btn-default priv" privilege="CMDB_CI_EDITUPDATE" id="editButton">编辑</button>
	        </div>
	         <div class="btn-group btn-group-sm" id="saveButtonDiv">
	            <button type="button" class="btn btn-success priv" privilege="CMDB_CI_SAVEUPDATE" id="saveButton">保存</button>
            </div>
	         <div class="btn-group btn-group-sm" id="deleteButtonDiv">
	            <button type="button" class="btn btn-default priv" privilege="CMDB_CI_DELETEUPDATE" id="deleteButton">删除</button>
            </div>
	         <div class="btn-group btn-group-sm" id="logButtonDiv">
	            <button type="button" class="btn btn-default priv" privilege="CMDB_CI_LOGUPDATE" id="logButton">日志</button>
            </div>
	         <div class="btn-group btn-group-sm" id="relationButtonDiv">
	            <button type="button" class="btn btn-default priv" privilege="CMDB_CI_RELATIONUPDATE" id="relationButton">关联关系</button>
            </div>
	         <div class="btn-group btn-group-sm" id="dfromButtonDiv">
	            <button type="button" class="btn btn-default priv" privilege="CMDB_CI_DFROM" id="dfromButton">配置表单</button>
            </div>
	    </div>
	</div>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div class="inner-title">
		配置项详情
	</div>
	<form id="autoform" class="changFormId"></form>
</body>
</html>