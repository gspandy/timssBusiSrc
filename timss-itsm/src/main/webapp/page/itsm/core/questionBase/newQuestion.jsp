<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yudean.itc.util.FileUploadUtil"%>
<%@page import="com.yudean.itc.util.Constant"%>
<%@page import="com.yudean.itc.dto.sec.SecureUser"%>
<%
	SecureUser operator = (SecureUser) session.getAttribute(Constant.secUser);
	String sessId = request.getSession().getId();
	String valKey = FileUploadUtil.getValidateStr(operator,FileUploadUtil.DEL_OWNED);
 %>
<!DOCTYPE html>
<html style="height: 99%;">
  <head>
	<title>新建问题</title>
	<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
	<script>_useLoadingMask = true;</script>
	<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
	<script type="text/javascript" src="${basePath}js/itsm/core/newQuestion.js?ver=${iVersion}"></script>
	<script type="text/javascript" src="${basePath}/js/workflow/workflow.js?ver=${iVersion}"></script>
	<script type="text/javascript" src="${basePath}js/itsm/common/workOrder.js?ver=${iVersion}"></script>
	<script>
		var createUserInfo = "${createUserInfo}";
		var questionId = "${questionId}";
		var categoryId = "${categoryId}";
		var categoryName = "${categoryName}";
	</script>
	<script type="text/javascript">
		var formRead = false;
		var createtime =""; //供审批框中显示
		var updatetime =""; //供审批框中显示
		var auditInfoShowBtn = 0 ; //控制流程信息弹出框中是否显示“审批”按钮
		var loginUserId = ItcMvcService.user.getUserId();
		var siteId = ItcMvcService.getUser().siteId;
		var candidateUsers;  //当前活动节点的候选人
		var processInstId ; //工单流程实例ID
		var taskId ; //活动节点ID
		var defKey="itsm_core_question";  //流程ID前缀
		$(document).ready(function(){
			//添加左边类型树
			FW.addSideFrame({
				src:basePath+"page/itsm/core/woParamsConf/faultTypeTree.jsp?embbed=1",
				id:"itsmQuestionFaultTypeTree",
				conditions :[
							 {tab:"^itsm_root$",tree:"^itsm_root_itsm_question$"},
							 {tab:"^initNewQuestion.+"},
							 {tab:"^itsmQuestion.+"},
							 {tab:"^QU.+"}
				]
			}); 
			$.ajaxSetup({'async':false}); 
			/* form表单初始化 */
			$("#questionForm").iForm("init",{"fields":fields,"options":{validate:true,initAsReadonly:formRead}});
			//附件的控制
			$("#uploadfileTitle").iFold("init");
			$("#uploadfileTitle").iFold("show");  
			initUploadform();
			//控制权限
			Priv.apply();
			//当选择了左边设备树然后新建时，直接将设备信息带过去
			if(categoryId){
				$("#questionForm").iForm("setVal",{"category":categoryId,"categoryName":categoryName});
			}
			$("#questionForm").iForm("setVal",{"createUserInfo":createUserInfo});
			if(questionId != null && questionId != "null"){
				setFormVal(questionId);
			}else if(questionId == null || questionId == "null"){  
				//点击新建产生的页面
				$("#inPageTitle").html("新建问题");
       			$("#btn_question_deleteDiv").hide();
       			$("#btn_question_audit1").hide();
       			$("#btn_printDiv").hide();
       			$("#btn_auditInfo").hide();
       			$("#questionForm").iForm("hide","opinion");
				$("#questionForm").iForm("hide","reason");
				$("#questionForm").iForm("hide","solve");
				$("#questionForm").iForm("hide","na");
				$("#questionForm").iForm("hide","ns");
				$("#questionForm").iForm("hide","status");
				$("#questionForm").iForm("setVal",{"categoryName":"请从左边目录树选择分类"});
			}
			$.ajaxSetup({'async':true});
			FW.fixRoundButtons("#toolbar");
		});
		
    /**左边树菜单选择触发事件--这个是在woParamsConf/faultTypeTree.jsp中调用的*/
	function triggerFaultTypeTreeSelect(data){
		var selectType = data.faultTypeCode;
		var rootflag = data.type;
		if(selectType == "SD" && formRead == false && rootflag!="root"){
			$("#questionForm").iForm("setVal",{
				category : data.id,
				categoryName: data.text
		    });
		}
	} 
	/**
	* 显示流程信息
	*/
	function showDiagram(){
	    var workFlow = new WorkFlow();
	    workFlow.showDiagram(defKey);
	}
	
	function showAuditInfo(){
	    var businessData={};
	    var fields = [{
			title : "创建时间", 
		    id : "createtime",
		    type : "label"
			},{
			title : "修改时间", 
		    id : "updatetime",
		    type : "label"
		}];
		
		var data={'createtime':createtime,'updatetime':updatetime};
	    businessData['fields'] = fields;
	    businessData['data'] = data;
	    
	    var workFlow = new WorkFlow();
	    
	    workFlow.showAuditInfo(processInstId,JSON.stringify(businessData),auditInfoShowBtn,audit);
	}
	
	//显示附件
	function initUploadform() {
		var uploadFiles="";
		$("#uploadform").iForm('init', {
			"fields" : [
                  	{
                  		id:"uploadfield", 
                  		title:" ",
                  		type:"fileupload",
                  		linebreak:true,
                  		wrapXsWidth:12,
                  		wrapMdWidth:12,
                  		options:{
                  		    "uploader" : basePath+"upload?method=uploadFile&jsessionid=<%=sessId%>",
                  		    "delFileUrl" : basePath+"upload?method=delFile&key=<%=valKey%>",
                  			"downloadFileUrl" : basePath + "upload?method=downloadFile",
                  			"swf" : basePath + "js/workorder/common/uploadify.swf",
                  			//"fileSizeLimit" : 20 * 1024,                  			
                  			"initFiles" : uploadFiles,
                  			"delFileAfterPost" : true
                  		}
                  	}
                  ],
			"options" : {
				"labelFixWidth" : 6,
				"labelColon" : false
			}
		});
	}
	</script>

  </head>
  
  <body style="height: 100%; " class="bbox">
   	 <div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar" class="btn-toolbar ">
	    	<div class="btn-group btn-group-sm">
	        	<button id="btn_close" type="button" class="btn btn-default" onclick="closeCurPage(true);">关闭</button>
	        </div>
	    	<div id="btn_question_operDiv" class="btn-group btn-group-sm">
	    		<button id="btn_question_save1" type="button" class="btn btn-default" onclick="commitQuestion('save');">暂存</button>
				<button id="btn_question_commit" type="button" class="btn btn-default" onclick="commitQuestion('commit');">提交</button>
				<button id="btn_question_audit1" type="button" class="btn btn-default" onclick="auditBtn();">审批</button>
			</div>
	        
	        <div id="btn_question_deleteDiv" class="btn-group btn-group-sm">
	        	<button id="btn_question_delete" type="button" class="btn btn-default" onclick="deleteQuestion();">删除</button>
	        	<button id="btn_question_obsolete" type="button" class="btn btn-default" onclick="obsoleteQuestion();" style="display: none;">作废</button>
	        </div>
	        <div id="btn_printDiv" class="btn-group btn-group-sm">
	        	<button id="btn_print" type="button" class="btn btn-default" onclick="print();">打印</button>
	        </div>
	        <div id="btn_flowDiagramDiv" class="btn-group btn-group-sm">
	        	<button id="btn_flowDiagram" type="button" class="btn btn-default" onclick="showDiagram();">审批信息</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	        	<button id="btn_auditInfo"type="button" class="btn btn-default" onclick="showAuditInfo();">审批信息</button>
	        </div>
	    </div>
	</div>
	<div  class="inner-title">
		<span id="inPageTitle">问题详情</span>
	</div>
	
    <div>
    	 <form id="questionForm" class="autoform"></form>
    </div>
     <!-- 附件层 -->
	<div class="margin-group"></div>
	<div grouptitle="附件"  id="uploadfileTitle">
		<div class="margin-title-table" id="uploadfile">
			<form id="uploadform" style=""></form>
		</div>
	</div>
  </body>
</html>
