<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html style="height: 99%;">
<head>
<title>新建标准工作票</title>
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script type="text/javascript">
	var privTypes = ${privTypes};
</script>
<script type="text/javascript" src="${basePath}/js/workflow/workflow.js?ver=${iVersion}"></script>
<script src='${basePath}js/ptw/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/ptw/commonForm.js?ver=${iVersion}'></script>
<script src='${basePath}js/ptw/sjc/updateStdTree.js?ver=${iVersion}'></script>
<script src='${basePath}js/ptw/core/ptwStdCommon.js?ver=${iVersion}'></script>
<script type="text/javascript" src="${basePath}js/common/safeItemTool.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/ptw/commonPtwTree.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/ptw/core/ptwStdPrivCommon.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/ptw/core/insertPtwStandard.js?ver=${iVersion}"></script>
<link rel="stylesheet" type="text/css" href="${basePath}css/ptw/ptwSafe.css?ver=${iVersion}"></link>

<script>
//父节点ID
var eqNo = '';
//父节名字
var eqName = "请从左边设备树选择";
//隔离措施模板
var safeAddContentTpl = "";
//站点
var siteId = '${siteId}';
//流程实例ID
var instantId = null;
//是否有此类型标准工作票的新建权限
//var hasNewPriv = false;
//基本信息bean
var bean = ${ bean };
//安全措施
var items = ${items};
//状态
var flowStatus = null;
var opts={
	validate:true,
	fixLabelWidth:true,
	labelFixWidth:150
};
	
	
	$(document).ready(function() {
		initAssetTree();
		$("#autoform").iForm("init",{"options":opts,"fields":fields});
		$("#autoform").iForm("hide",[ "version"]);
		//重定义type --begin
		var privTypeEnum = FW.parseEnumData("ptw_standard_type",_enum);
		var privTypeList = []; 
		for(var i=0;i<privTypeEnum.length;i++){
			var type = privTypeEnum[i][0];
			for(var j=0 ;j<privTypes.length;j++){
				if(privTypes[j]==type){
					privTypeList.push(privTypeEnum[i]);
				}
			}
		}
		$("#f_wtTypeId").iCombo("init",{data:privTypeList});
		//重定义type --end	
				
		setForm();
		//树节点
		treeSelectNode();
		
		safeAddContentTpl = $("#safeAddContentTpl").html();
		//初始化必须采取的安全措施
		beginEditSafeItemList("safeItem1");
		//addSafeContents(1, true);
		$("#safeItem1").iFold("init").iFold("show");
		
		if( bean != null && bean != "" && bean.toString().length>0){
			bean.beginTime = null;
			bean.endTime = null;
			setPtwStandard( bean, items );
		}
		
		//初始化权限
		setPriv();
		
		//暂存
		$( "#saveButton" ).click(function(){
			savePtwStandard("saveButton");
		});
		
		//提交
		$( "#commitButton" ).click(function(){
			commitPtwStandard("commitButton");
		});
		
		//显示流程图
		$("#flowDiagramBtn").click(function(){
			//showFlowDialog( "atd_itc_leave" );
			var workFlow = new WorkFlow();
			var defKey = "ptw_" + siteId.toLowerCase() + "_std";
			workFlow.showDiagram( defKey );
		});
		
		
	});
	
</script>

</head>
<body>
<div class="bbox toolbar-with-pager" id="toolbar_wrap">
   <div id="toolbar" class="btn-toolbar ">
   		<div id="btn_wo_operCloseDiv" class="btn-group btn-group-sm">
        	<button id="btn_close" type="button" class=" btn btn-default" onclick="closeTab();">关闭</button>
		</div>
    	<div id="btn_wo_operDiv" class="btn-group btn-group-sm">
			<button id="saveButton" type="button" class="btn btn-default">暂存</button>
			<button id="commitButton" type="button" class="btn btn-default" >提交</button>
		</div>
	  	<div class="btn-group btn-group-sm" style="display: none;" id="deleteButtonDiv">
			<button id="deleteButton" type="button" class="btn btn-default" >删除</button>
        </div>
        <div id="btn_flowDiagramDiv" class="btn-group btn-group-sm">
        	<button id="flowDiagramBtn" type="button" class="btn btn-default">审批信息</button>
        </div>
    </div>
</div>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div class="inner-title">
		新建标准工作票
	</div>
	<form id="autoform"></form>
	
	<script type="text/html" id="safeAddContentTpl">
		<div class="wrap-underline wrap-underline-single">
		    <span class="safe-number-span"></span>
		    <span class="safe-input-content" style="display: none;"></span>
			<input class="safe-input" type="text" onkeyup="bindNewSafeEvent(this);"/>
		</div>
	</script>
	<div id="safeItem1" class="safeItems" safeType="1" grouptitle="必须采取的安全措施"></div>
</body>
</html>