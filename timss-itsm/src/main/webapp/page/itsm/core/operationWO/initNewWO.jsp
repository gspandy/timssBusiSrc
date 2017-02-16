<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.yudean.itc.util.FileUploadUtil"%>
<%@page import="com.yudean.itc.util.Constant"%>
<%@page import="com.yudean.itc.dto.sec.SecureUser"%>
<%
	SecureUser operator = (SecureUser) session
			.getAttribute(Constant.secUser);
	String sessId = request.getSession().getId();
	String valKey = FileUploadUtil.getValidateStr(operator,
			FileUploadUtil.DEL_OWNED);
	
 %>
<!DOCTYPE html>
<html>
  <head>
	<title>新建工单</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
	<script>_useLoadingMask = true;</script>
	<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
	<script>
		var  assistDeptResult = ${assistDeptResult}; 
		var woStatus = '<%=request.getParameter("woStatus")%>';
		var faultTypeParams = '<%=request.getParameter("faultTypeParams")%>';
		var woId = '<%=request.getParameter("woId")%>';
		var formRead = false;
		if(woStatus !="null" &&woStatus!="newWO" &&woStatus != "draft"){
			formRead = true;
		}
		
	</script>
	<script type="text/javascript" src="${basePath}/js/itsm/common/itsmConstant.js?ver=${iVersion}"></script>
	<script type="text/javascript" src="${basePath}js/itsm/core/initNewWorkOrder.js?ver=${iVersion}"></script>
	<script type="text/javascript" src="${basePath}js/itsm/common/workOrder.js?ver=${iVersion}"></script>
	<script type="text/javascript" src="${basePath}/js/workflow/workflow.js?ver=${iVersion}"></script>
	<script type="text/javascript">
		var faultTypeId = null;
		var createtime =""; //供审批框中显示
		var updatetime =""; //供审批框中显示
		var auditInfoShowBtn = 0 ; //控制流程信息弹出框中是否显示“审批”按钮
		var loginUserId = ItcMvcService.user.getUserId();
		var siteId = ItcMvcService.getUser().siteId;
		
		var isITCSer = false;  //客服
		var isEngineer = false;  //工程师
		var isInfoCenterUser = false;  //信息中心用户
		
		//以下五个变量的取值确定流程中审批的走向
		var itcWhbWo = false; //是否是科技公司维护部新建工单
		var infoCenterWo = false ; //是否是集团信息中心人员新建工单
		var needInfoCenterAudit = false; //是否需要信息中心审批
		var comNextAudit = "own";  //单位审批时默认选取为“本单位”
		var centerNextAudit = itsmDeptId.INFOCENTERDEPTID; //信息中心审批时默认选取为信息中心
		
		var candidateUsers;  //当前活动节点的候选人
		var processInstId ; //工单流程实例ID
		var taskId ; //活动节点ID
		var defKey="itsm_yudean_wo";  //流程ID前缀
		
		if(faultTypeParams != "null"){
			faultTypeParams = JSON.parse( faultTypeParams );
			faultTypeId = faultTypeParams.faultTypeId;
			faultTypeName = faultTypeParams.faultTypeName;
		}
		
		
		
		$(document).ready(function(){
			FW.addSideFrame({
				src:basePath+"page/itsm/core/woParamsConf/faultTypeTree.jsp?embbed=1",
					id:"itsmFaultTypeTree",
					conditions :[{tab:"^itsm_root$",tree:"^itsm_root_itsm_wo$"},
								 {tab:"^itsm_root$",tree:"^itsm_root_itsm_mtp$"},
								 {tab:"^itsm_root$",tree:"^itsm_root_itsm_proj$"},
								 {tab:"^itsm_root$",tree:"^itsm_root_itsm_woFaultTypeConf$"},
								 {tab:"^IT.+"},
								 {tab:"^newItsm.+"},
								 {tab:"^itsmInfo.+"},
								 {tab:"^newItsmMTP.+"},
								 {tab:"^openItsmMTP.+"},
								 {tab:"^newItsmJP.+"},
								 {tab:"^openItsmJP.+"},
								 {tab:"^itsmPlanInfo.+"},
								 {tab:"^newItsmFaultType.+"},
								 {tab:"^openItsmFaultType.+"}
								]
				}); 
			FW.toggleSideTree(false);  //展开服务目录树
			$.ajaxSetup({'async':false}); 
			 isCusSer(siteId);
			$("#initWorkOrderForm").iForm("init",{"fields":fields,"options":{validate:true,initAsReadonly:formRead}});
			//当选择了左边设备树然后新建时，直接将设备信息带过去
			if(faultTypeId){
				$("#initWorkOrderForm").iForm("setVal",{"faultTypeId":faultTypeId,"faultTypeName":faultTypeName});
				//展开树，显示选中的树
				setTimeout(function(){
						window.parent.document.getElementById("itsmFaultTypeTree").contentWindow.expandForHintById(faultTypeId);
					},1000);
			}
			//附件的控制
			$("#uploadfileTitle").iFold("init");
			$("#uploadfileTitle").iFold("show");  
			uploadform();
			//控制权限
			Priv.apply();
			//填充报障人的信息
			if(woId == null || woId == "null"){  //点击新建产生的页面
				fillCustomerInfo(loginUserId);
				$("#initWorkOrderForm").iForm("endEdit",["customerName","customerCode","customerCom","customerDept"]); 
				$("#initWorkOrderForm").iForm("hide",["workOrderCode"]); //,"assistDept"
				if(!isInfoCenterUser&&siteId!="ITC"){   //如果未非信息中心的用户
					$("#initWorkOrderForm").iForm("hide",["sendToITC"]); 
				}else if(isInfoCenterUser){
					$("#initWorkOrderForm").iForm("hide",["assistDept"]); 
				}
				if(siteId=="ITC"){
					$("#initWorkOrderForm").iForm("setVal",{"sendToITC":"true"}).iForm("hide",["sendToITC"]); 
				}else{
					FW.setTreeStat("fold");
				}
			}
			
			if(woId != null && woId != "null"){
				setFormVal(woId);
				//展开树，显示选中的树
				if(faultTypeId != null){
					setTimeout(function(){
						window.parent.document.getElementById("itsmFaultTypeTree").contentWindow.expandForHintById(faultTypeId);
					},1000);
				}
			}
			$.ajaxSetup({'async':true});
			
			$("#initWorkOrderForm").iForm("endEdit",["customerName"]); 
			
			if(woId == null || woId == "null"){  //点击新建产生的页面
				$("#inPageTitle").html("新建工单");
				$("#btn_wo_edit1Div").hide();
       			$("#btn_wo_deleteDiv").hide();
       			$("#initWorkOrderForm").iForm("hide",["currStatus","currHandUserName"]);
       			$("#btn_wo_audit1").hide();
       			$("#btn_auditInfo").hide();
			}
			FW.fixRoundButtons("#toolbar");
			$("#btn_sysUserList").hide();
		});
		
	

	function triggerFaultTypeTreeSelect(data){
		var selectType = data.faultTypeCode;
		var rootflag = data.type;
		if(selectType == "SD" && formRead == false && rootflag!="root"){
			$("#initWorkOrderForm").iForm("setVal",{
		     	faultTypeId : data.id,
	        	faultTypeName:data.text
		    });
		}else if(selectType.substring(0,2) == "SC"){
			FW.error("不要从“性质”目录中选择");
		}
		/* if(data.oneLevenId == '109'){
			$("#initWorkOrderForm").iForm("hide",["maintType"]);
		}else{
			$("#initWorkOrderForm").iForm("show",["maintType"]);
		} */
	} 
	
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
	function uploadform() {
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
                  			//"initFiles" : ${uploadFiles},
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
	    	<div id="btn_wo_operDiv" class="btn-group btn-group-sm">
	    		<button id="btn_wo_save1" type="button" class="btn btn-default" onclick="initCommitWO('save');">暂存</button>
				<button id="btn_wo_commit" type="button" class="btn btn-default" onclick="initCommitWO('commit');">提交</button>
				<button id="btn_wo_audit1" type="button" class="btn btn-default" onclick="audit();">审批</button>
			</div>
	        
	        <div id="btn_wo_deleteDiv" class="btn-group btn-group-sm">
	        	<button id="btn_wo_delete" type="button" class="btn btn-default" onclick="deleteInitWO();">删除</button>
	        	<button id="btn_wo_obsolete" type="button" class="btn btn-default" onclick="obsoleteWO();" style="display: none;">作废</button>
	        	<button id="btn_wo_print1" type="button" class="btn btn-default" onclick="printWO()">打印</button>
	        </div>
	        <div id="btn_flowDiagramDiv" class="btn-group btn-group-sm">
	        	<button id="btn_flowDiagram" type="button" class="btn btn-default" onclick="showDiagram();">审批信息</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	        	<button id="btn_auditInfo"type="button" class="btn btn-default" onclick="showAuditInfo();">审批信息</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	        	<button id="btn_sysUserList"type="button" class="btn btn-default" onclick="openHelpPage();">查看系统负责人</button>
	        </div>
	        
	    </div>
	</div>
	<div  class="inner-title">
		<span id="inPageTitle">工单详情</span>
	</div>
	
    <div>
    	 <form id="initWorkOrderForm" class="autoform"></form>
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
