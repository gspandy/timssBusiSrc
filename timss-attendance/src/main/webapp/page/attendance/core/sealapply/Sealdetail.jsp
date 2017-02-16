<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.yudean.itc.util.FileUploadUtil"%>
<%@page import="com.yudean.itc.dto.sec.SecureUser"%>
<%@page import="com.yudean.itc.util.Constant"%>
<%
	SecureUser operator = (SecureUser) session.getAttribute(Constant.secUser);
	String sessId = request.getSession().getId();
	String valKey = FileUploadUtil.getValidateStr(operator,FileUploadUtil.DEL_OWNED);
 %>
<!DOCTYPE html>
<html>
<head>

<title>用章申请详情</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script type="text/javascript">
_useLoadingMask=true;
</script>
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script type="text/javascript" src="${basePath}js/attendance/common/addTab.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/attendance/common/pageDetail.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/attendance/common/pageMode.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/attendance/common/commonForm.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/workflow/workflow.js?ver=${iVersion}"></script>
<script type="text/javascript">
	//流程实例ID
	var processInstId = null;
	//任务Id
	var taskId = null;
	//审批标志
	var applyFlag = null;
	//附件编辑标志
	var isEdit = null;
	//判定是否显示信息中的审批按钮(1--show/ 0--hide)
	var auditInfoShowBtn = 0;
	//站点信息
	var siteId = '${siteId}';
	//当前对象
	var sealApplyObj = null;
	//当前用户
	var userId = '${userId}';
	
$(document).ready(function() {
	PageDetail.createBean=function(btn){
		if(!PageDetail.toValid()){
			//FW.error("提交的内容有错误的地方，请修改后重试");
			return;
		}
		$.post(
			PageDetail.objs.url.create,
			PageDetail.getDataForCreate(),
			PageDetail.createCallback,
			"json"
		);
	},
	PageDetail.createCallback=function(data){
		if(data.result=="success"){
			FW.success("暂存成功");
			PageDetail.objs.form.bean = data.bean;
			PageDetail.objs["mode"]="edit";
			PageDetail.loadData();
		}else{
			FW.error("暂存失败，请稍后重试或联系管理员");
		}
	}
	PageDetail.queryAttachCallback = function(data){
		if( data.result == "success"){
			PageDetail.setAttach(data.fileMap);
		}else{
			PageDetail.setAttach("");//隐藏附件
		}
		var isEdit = PageDetail.objs.form.queryData.editFlag;
		//既要当前处理人为创建人，而且处于编辑状态（退回第一节点）
		if((isEdit == 1 && sealApplyObj.status=="commit_seal_apply")||sealApplyObj.status=="draft"){
			$("#uploadform").iForm("beginEdit");
		}else{
			$("#uploadform").iForm("endEdit");
		}
	}
	PageDetail.changeShow = function(){//切换模式
		//获取基本信息		
		sealApplyObj = PageDetail.objs.form.bean;
		processInstId = sealApplyObj.instanceId;
		createuser = sealApplyObj.createuser;	
		if(PageDetail.objs.mode=="create"){
			PageDetail.objs.form.obj.iForm("hide",["createdate","saNo", "createUserName"]);
			$( "#btnAudit" ).hide();//不可审批
			$( "#btnInvalid" ).hide();//不可作废
			$( "#btnEdit" ).hide();//不可编辑
			$( "#btnDel" ).hide();//不可删除
			$( "#btnCreate" ).show();//可暂存、提交
			$( "#btnSave" ).hide();//保存隐藏
			$( "#btnPrint" ).hide();//不可打印
		}else if(PageDetail.objs.mode=="edit"){
			if(sealApplyObj.status=="commit_seal_apply"){
				//PageMode.changeMode("edit");
				$( "#btdEdit" ).hide();//不可编辑
				$( "#btnDel" ).hide();//不可删除
				$( "#btnAudit" ).hide();//不可审批
				$( "#btnCreate" ).show()//显示暂存提交
				$( "#btnInvalid" ).show();//可作废
				$( "#btnSave" ).hide();//保存隐藏
			}else{//PageMode.changeMode("edit");
			$( "#btnAudit" ).hide();//不可审批
			$( "#btnInvalid" ).hide();//不可作废
			$( "#btnEdit" ).hide();//不可编辑
			$( "#btnDel" ).show();//可删除
			$( "#btnCreate" ).show();//可暂存、提交
			$( "#btnSave" ).hide();//保存隐藏
			}
			PageDetail.objs.form.obj.iForm("show",["createdate","saNo"]);
		}			
		else if(PageDetail.objs.mode=="view"){
			if(PageDetail.objs.form.queryData){
				taskId = PageDetail.objs.form.queryData.taskId;
				applyFlag = PageDetail.objs.form.queryData.applyFlag;
			}
			//按钮控制
			if(sealApplyObj.status=="draft"){
				PageMode.changeMode("edit");
				$( "#btdEdit" ).hide();//不可编辑
				$( "#btnDel" ).show();//可删除
				$( "#btnAudit" ).hide();//不可审批
				$( "#btnCreate" ).show()//显示暂存提交
				$( "#btnSave" ).hide();//保存隐藏
				$( "#btnInvalid" ).hide();
				$( "#btnPrint" ).hide();//不可打印
			}
			else if(sealApplyObj.status=="commit_seal_apply"){
				PageMode.changeMode("edit");
				$( "#btdEdit" ).hide();//不可编辑
				$( "#btnDel" ).hide();//不可删除
				$( "#btnAudit" ).hide();//不可审批
				$( "#btnCreate" ).show()//显示暂存提交
				$( "#btnSave" ).hide();//保存隐藏
				//第一个节点，如果当前登录用户时创建人则可以作废
				if(userId == createuser){
					$( "#btnInvalid" ).show();
				}else{
					$( "#btnInvalid" ).hide();
				}			
			}
			else{
				PageMode.changeMode("view");
				$( "#btnEdit" ).hide();//不可编辑
				$( "#btnDel" ).hide();//不可删除
				//其他节点如果是审批人，则出现审批按钮
				if(applyFlag == "approver"){
					auditInfoShowBtn = 1;
					$( "#btnAudit" ).show();//如果是当前审批人则可审批
				}
				else{
					auditInfoShowBtn = 0;
					$( "#btnAudit" ).hide();//如果非当前审批人则不可审批
				}
				$( "#btnInvalid" ).hide();//不可作废
			}
		}
	}
	
	PageDetail.init({
		namePrefix:"用章申请",
		print:{
			id:"YZSQ_001"
		},
		url : {
			query : basePath+"attendance/sealApply/getDetail.do",
			create : basePath+"attendance/sealApply/insertSealApply.do",//required
			update : basePath+"attendance/sealApply/insertSealApply.do",//required
			del : basePath+"attendance/sealApply/removeSealApply.do"//required
		},
		mode : "${param.mode}",//默认，可选view/create/edit
		withAttach:true,
		attach:{
			formId:"uploadform",
			itemType:"sealApply"
			
		},
		form : {
			id:"form_baseinfo",
			fields:[
				{title : "申请ID", id : "saId", type : "hidden"},
				{title : "文件标题", id : "title", rules : {maxChLength : 100}},
				{title : "申请人/申请部门", id : "createUserName", type : "label", formatter : function(val){
					if(val!=null && val!=""){
						return val+"/"+PageDetail.objs.form.bean["deptName"];
					}
				}},
				{title : "申请单编号", id : "saNo", type : "label"},
				{title : "发往单位", id : "sendCompany", rules : {required : true, maxChLength : 40}},
				{title : "印别", id : "category", type : "combobox", 
					data: [
						["公章","公章",true],
						["其他","其他用章"]
					]
				},
				{title : "印数", id : "count", dataType : "digits", rules : {digits:true,range:[1,99], required : true}},
				{title : "申请时间", id : "createdate", type : "date", type : "label", dataType : "date", linebreak : true, formatter : function(val){
					return FW.long2date(val);
				}},
   				{title : "盖章事由", id : "reason", linebreak : true, type : 'textarea',
   					wrapXsWidth : 18, wrapMdWidth : 8, height : 95, rules : {maxlength : 300}
   				}
			],//required
			idField : "saId",//required
			nameField : "title",//required
			beanId : "${param.saId}",
			blankBean  :{userName:Priv.secUser.userName,deptName:Priv.secUser.orgs&&Priv.secUser.orgs.length>0?Priv.secUser.orgs[0].name:""}
		}
	});
});

function commitSealApply(btn){
	if(!$("#form_baseinfo").valid()){
		return;
	}
	$(btn).button("loading");
	var formData = getFormData("form_baseinfo");
	
	//退回提交
	if( processInstId != null && processInstId != "" ){
		var workFlow = new WorkFlow();
		workFlow.showAudit(taskId,formData,closeTab,null,null,"",0,function(){$(btn).button("reset");});
		return;
	}
	PageDetail.getDataForCreate()
	var url = basePath + "attendance/sealApply/commitSealApply.do";
	var fileIds = $("#uploadform").iForm("getVal")["attachment"];
	$.ajax({
		url : url,
		type : 'post',
		dataType : "json",
		data:{
			formData : formData,
			"fileIds" : fileIds
		},
		success : function(data){
			if(data.result == "success"){
				//FW.success("提交成功");
				var taskId = data.taskId;
				if(taskId !=null){
					var workFlow = new WorkFlow();
					workFlow.submitApply(taskId, null, closeTab, null, 0);
				}
			}else{
				if(data.reason !=null){
					FW.error(data.reason);
				}else{
					FW.error("提交失败");
				}
			}
			//PageDetail.objs.form.bean=data.bean;
			//PageDetail.objs["mode"]="view";
			//PageDetail.loadData();
			//$(btn).button("reset");
			
		},
		loadData:function(){//加载数据
			PageDetail.objs.form.obj.ITC_Form("loaddata",PageDetail.objs.form.bean);
			PageDetail.afterLoadData();
			PageDetail.changeShow();
		}
	});
};
//显示流程信息
function showAuditMessage(){
	var defKey = "atd_" + siteId.toLowerCase() + "_sealapply" ;
	var workFlow = new WorkFlow();
	if( processInstId == null || processInstId == "" ){
		workFlow.showDiagram(defKey);
	}else{
		workFlow.showAuditInfo(processInstId,sealApplyObj,auditInfoShowBtn,auditSealApply);
	}	
}

//审批
function auditSealApply(){
	var workFlow = new WorkFlow();
	workFlow.showAudit(taskId,null,closeTab,closeTab,null,"",0);
}
//作废
function invalidSealApply(){
	var formData = $( "#form_baseinfo" ).iForm("getVal");
	FW.confirm("确定作废本条数据吗|该操作无法恢复", function() {
		var url = basePath + "attendance/sealApply/invalidSealApply.do";
		$.ajax({
			url : url,
			type : 'post',
			dataType : "json",
			data:{
				saId : formData.saId
			},			
			success : function(data) {
				if( data.result == "success" ){
					FW.success( "作废成功");
					closeTab();
				}else{
					FW.error( "作废失败");
				}
			}
		});
	});	
}

</script>
<style type="text/css">
.btn-garbage{
cursor:pointer;
}
html{
	height:95%
}
body{
	height:100%
}
</style>
</head>

<body>
	<div class="toolbar-with-pager bbox">
		<div class="btn-toolbar" role="toolbar">
			<div class="btn-group btn-group-sm" id="closeButtonDiv">
	            <button type="button" class="btn btn-default" onclick="closeTab()">关闭</button>
	        </div>
			<div class="btn-group btn-group-sm" id="btnEdit">
				<button class="btn-default btn " privilege="" onclick="PageDetail.toEdit()">编辑</button>
			</div>
			<div class="btn-group btn-group-sm" id="btnAudit">
				<button class="btn-default btn " privilege="" onclick="auditSealApply()">审批</button>
			</div>
			<div class="btn-group btn-group-sm" id="btnSave" style="display:none">
				<button class="btn-success btn " privilege="" onclick="PageDetail.updateBean()">保存</button>
			</div>
			<div class="btn-group btn-group-sm" id="btnCreate" style="display:none">
				<button class="btn-default btn " privilege="" onclick="PageDetail.createBean(this)">暂存</button>
				<button class="btn-default btn " privilege="" onclick="commitSealApply(this)">提交</button>
			</div>
			<div class="btn-group btn-group-sm" id="btnDel">
				<button class="btn-default btn " privilege="" onclick="PageDetail.toDelete()">删除</button>
			</div>	
     		<div class="btn-group btn-group-sm" id="btnInvalid">
				<button class="btn-default btn " privilege="" onclick="invalidSealApply()">作废</button>
			</div>
			<div class="btn-group btn-group-sm" id="btnPrint">
				<button class="btn-default btn" onclick="PageDetail.toPrint()">打印</button>
			</div>
			<div class="btn-group btn-group-sm" style="">
				<button class="btn-default btn" onclick="showAuditMessage()">审批信息</button>
			</div>
		</div>
	</div>
	<div class="inner-title" id="pageTitle">
		新建用章申请
	</div>
	<form id="form_baseinfo" class="margin-form-title margin-form-foldable">
	</form>
	<div class="margin-group"></div>
	
	<!-- 附件层 -->
    <div class="margin-group"></div>
    <div grouptitle="附件"  id="uploadfileTitle">
		<div class="margin-title-table" id="uploadfile">
			<form id="uploadform" style=""></form>
		</div>
	</div>
</body>
</html>
