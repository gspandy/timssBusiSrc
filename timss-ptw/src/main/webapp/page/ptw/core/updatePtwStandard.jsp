<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html style="min-height: 99%;">
<head>
<title>标准工作票详情</title>
<script>_useLoadingMask = true;</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script type="text/javascript">
	var privTypes = ${privTypes};
	var newPtwPrivTypes =${newPtwPrivTypes};
</script>
<script type="text/javascript" src="${basePath}/js/workflow/workflow.js?ver=${iVersion}"></script>
<script src='${basePath}js/ptw/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/ptw/commonForm.js?ver=${iVersion}'></script>
<script src='${basePath}js/ptw/sjc/updateStdTree.js?ver=${iVersion}'></script>
<script src='${basePath}js/ptw/core/ptwStdCommon.js?ver=${iVersion}'></script>
<script src='${basePath}js/ptw/core/updatePtwStandard.js?ver=${iVersion}'></script>
<script type="text/javascript" src="${basePath}js/common/safeItemTool.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/ptw/commonPtwTree.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/ptw/core/ptwStdPrivCommon.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/ptw/core/ptwInterface.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/ptw/OpenTabUtil.js?ver=${iVersion}"></script>
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
//标准工作票ID
var id = '${id}';
//当前登录人
var currentUser = '';
//流程实例ID
var instantId = null;
//流程状态
var flowStatus = null;
//标准工作票类型
var stdType = null;
//是否在编辑状态
var editFlag = false;
//任务Id
var taskId = null;
//审批标志
var applyFlag = null;
//是否在编辑状态
var editFlag = false;
//判定是否显示信息中的审批按钮(1--show/ 0--hide)
var auditInfoShowBtn = 0;

var ptwTypeCodes = "";



//接收树的点击
function onTreeItemClick( node ){
	if( editFlag ){
		eqNo = node.id;
		eqName = node.text;
		setForm();
	}
}

	$(document).ready(function() {
		initAssetTree();
		$("#autoform").iForm("init",{"options":opts,"fields":updateFields});
		
		safeAddContentTpl = $("#safeAddContentTpl").html();
		//初始化必须采取的安全措施
		//addSafeContents(1, true);
		$("#safeItem1").iFold("init");
		
		//初始化新建按钮
		$.post(basePath+ "ptw/ptwInfo/auditPriv.do",{"ptwTypeCode" : null,"ptwStatus":"xj"},function(data){
			initForm( id );
			var types = data.types;
			if(types && types.length > 0){
				ptwTypeCodes = types;
			}else{
				$("#newPtwButton" ).hide();
			}
		},"json");
		
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
			var workFlow = new WorkFlow();
			if( !instantId ){
				var defKey = "ptw_" + siteId.toLowerCase() + "_std";
				workFlow.showDiagram( defKey );
			}else{
				//workFlow.showAuditInfo(instantId,id);
				workFlow.showAuditInfo(instantId,JSON.stringify(id),auditInfoShowBtn,audit);
			}
		});
		
		//审批
		$("#approveBtn").click(function(){
			audit1();
		});
		
		//删除
		$("#deleteButton").click(function(){
			FW.confirm("删除？<br/>确定删除所选项吗？该操作无法撤销。", function() {
				deletePtwStandard( id, "delete");
			});
		});
		//作废
		$("#cancelButton").click(function(){
			FW.confirm("作废？<br/>确定作废所选项吗？该操作无法撤销。", function() {
				deletePtwStandard( id, "cancel");
			});
		});
		//过期
		/* $("#expireButton").click(function(){
			FW.confirm("设置过期？<br/>确定设置过期所选项吗？该操作无法撤销。", function() {
				deletePtwStandard( id, "expire");
			});
		}); */
		//修改
		$("#modifyButton").click(function(){
			var formData = $("#autoform").iForm("getVal");
			//var safeItems = getSafeInputs().safeItems;
			
			//当前id设置给parentWtId
			//formData.parentWtId = formData.id;
			//formData.id = '';
			$.post(basePath + "ptw/ptwStandard/hasSameCodeSptwInAudit.do",{"sptwCode":formData.wtNo,"id":formData.id},function(data){
					if(data.result == false){
						copyToInintStdPtw( formData.id );
					}else{
						FW.error("该编号的标准票已有一张在审批中");
					}
				},"json");
			
		});
		
		//生成工作票
		$("#newPtwButton").click(function(){
			newPtwPageWithData( id );
		});
		
	});
		
</script>

</head>
<body>
<div class="bbox toolbar-with-pager" id="toolbar_wrap">
   <div id="toolbar" class="btn-toolbar ">
   		<div class="btn-group btn-group-sm" id="closeDiv">
            <button id="closeBut" type="button"  class="btn btn-default" onclick="closeTab()">关闭</button>
        </div>
    	<div class="btn-group btn-group-sm" id="editDiv">
			<button id="saveButton" style="display:none"  type="button" class="btn btn-default">暂存</button>
			<button id="commitButton" style="display:none"  type="button" class="btn btn-default"  >提交</button>
			<button id="approveBtn" style="display:none" type="button" class="btn btn-default" >审批</button>
		</div>
	  	<div class="btn-group btn-group-sm" id="deleteButtonDiv">
			<button id="deleteButton" style="display:none" type="button" class="btn btn-default"  >删除</button>
        </div>
        <div class="btn-group btn-group-sm"  id="cancelButtonDiv">
			<button id="cancelButton" style="display:none" type="button" class="btn btn-default">作废</button>
        </div>
       <!--  <div class="btn-group btn-group-sm" id="expireButtonDiv">
			<button id="expireButton" type="button" class="btn btn-default priv"  privilege="PTW_STD_EXPIRE">过期</button>
        </div> -->
        <div class="btn-group btn-group-sm" id="modifyButtonDiv">
			<button id="modifyButton" style="display:none"  type="button" class="btn btn-default">修改</button>
        </div>
        <div class="btn-group btn-group-sm">
	        <button id="btn_setValidTime" style="display:none" type="button" class="btn btn-default priv" privilege="PTW_STD_SETTIME"  onclick="setValidTime();">设置有效时间</button>
	    </div>
        <!-- <div class="btn-group btn-group-sm" id="printButtonDiv">
			<button id="printButton" type="button" class="btn btn-default priv" privilege="PTW_STD_PRINT" >打印</button>
        </div> -->
         <div class="btn-group btn-group-sm" id="newPtwButtonDiv">
			<button id="newPtwButton" type="button" class="btn btn-default">生成工作票</button>
        </div>
        <div id="flowDiagramDiv" class="btn-group btn-group-sm">
        	<button id="flowDiagramBtn" style="display:none" type="button" class="btn btn-default">审批信息</button>
        </div>
    </div>
</div>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div class="inner-title">
		标准工作票详情
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
	<!-- 同编号的其他版本 -->
	<div style="clear:both"></div>
	<div id="title_sameCodeList" grouptitle="其他版本">
		<div id="grid1_wrap" style="width:100%">
		    <table id="sptwInfo_table"  class="eu-datagrid">
		    </table>
		</div>
	</div>
</body>
</html>