<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>新建参数项值</title>
<script>_useLoadingMask = true;</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src='${basePath}js/asset/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/asset/commonForm.js?ver=${iVersion}'></script>
<script src='${basePath}js/asset/cmdbCommon.js?ver=${iVersion}'></script>
<script src='${basePath}js/asset/cmdbPrivCommon.js?ver=${iVersion}'></script>

<script>
//formId 可改变
var formId = "autoform";
//当前登录人userId/userName
var userId = '${ userId }';
var userName = '${ userName }';
var cId = "";

/**** -----------------------------------------------动态表单 start ------------------------------ */
var pageCode = "itc_cmdb_formcode";
var formOptions = opts;
var formSearchParam = { "id" : cId, "sheetId" : cId, "formSwitch" : "on"};
var formUrlParam = basePath + "asset/cmdbCi/queryCmdbPubCiById.do";
var isFireChange=true;//是否激活onchange函数（改变ci类型时）
/**** -----------------------------------------------动态表单 end ------------------------------ */

//初始化插入的表单
function initInsertForm(){
	FW.dynamicForm(insertFields, formOptions, formSearchParam, formUrlParam);
	
	//单位 iHint
	var iHintUrl = basePath + "asset/cmdbCi/searchCmdbParamsHint.do?paramType=CMDB_PARAM_5";
	searchHint( "unitId", "unitName", iHintUrl );
	//a端 iHint
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
	//当前登录人
	$("#" + formId).iForm("setVal",{"responUserId":userId, "responUserName":userName});
	//隐藏A/B端、机房信息、机柜信息
	$( "#" + formId ).iForm("hide",["aportId", "aportName", "bportId", "bportName","cabinetId", "cabinetName","engineRoomId", "engineRoomName"]);
}


	$(document).ready(function() {
		//$("#" + formId).iForm("init",{"options":opts,"fields":insertFields});
		initInsertForm();
		//权限
		setCiPriv();
		
		//保存
		$( "#saveButton" ).click(function(){
			if(!$( "#" + formId ).valid()){
				return;
			}
			var formData = getFormData( formId );
			var url = basePath + "asset/cmdbCi/insertCmdbPubCi.do";
			$.ajax({
				url : url,
				type : 'post',
				dataType : "json",
				data :{
					formData :  formData
				},
				success : function(data) {
					if( data.result == "success" ){
						FW.success( "保存成功 ！");
						//resetForm();
						closeTab();
					}else{
						FW.error( "保存失败 ！");
					}
				}
			});
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
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default priv" privilege="CMDB_CI_CLOSE" onclick="closeTab()">关闭</button>
	        </div>
	         <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-success priv" privilege="CMDB_CI_SAVE" id="saveButton">保存</button>
            </div>
            <div class="btn-group btn-group-sm" id="dfromButtonDiv">
	            <button type="button" class="btn btn-default priv" privilege="CMDB_CI_DFROM" id="dfromButton">配置表单</button>
            </div>
	    </div>
	</div>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div class="inner-title">
		新建配置项
	</div>
	<form id="autoform" class="changFormId"></form>
</body>
</html>