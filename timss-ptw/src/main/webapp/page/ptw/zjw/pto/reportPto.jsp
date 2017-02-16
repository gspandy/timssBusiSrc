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
	<title>操作票详情-审批中</title>
	<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
	<script>_useLoadingMask = true;</script>
	<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
	<script>
		var loginUserId = ItcMvcService.user.getUserId();
		var siteid = ItcMvcService.getUser().siteId;
		var ptoInfoVoData = ${ptoInfoVo};
		var roleUserMap = ${roleUserMap};
		var ptoAttachment = ${attachmentMap};
		var processInstId = "${processInstId}";  //流程实例ID
		var taskId = ${taskId};
		var newSptoPrivFlag = ${newSptoPrivTypes};
		var newBtnShowFlag = ${newBtnShowFlag};
		var sessId = '<%=sessId%>';
		var valKey = '<%=valKey%>';
		var fields = "";
	</script>
	<script type="text/javascript" src="${basePath}js/pto/core/ptoInterface.js?ver=${iVersion}"></script>
	<script type="text/javascript" src="${basePath}js/pto/core/reportPto.js?ver=${iVersion}"></script>
	<script type="text/javascript" src="${basePath}js/pto/core/ptoUtil.js?ver=${iVersion}"></script>
	<script type="text/javascript" src="${basePath}/js/workflow/workflow.js?ver=${iVersion}"></script>
	<script type="text/javascript" src="${basePath}/js/common/safeItemTool.js?ver=${iVersion}"></script>
	<link rel="stylesheet" type="text/css" href="${basePath}css/ptw/ptoSafe.css?ver=${iVersion}"></link>
	<script type="text/javascript">
		
		var formRead = false;
		var workflowKey = "ptw_"+siteid.toLowerCase()+"_pto" ;
		var createtime =""; //供审批框中显示
		var updatetime =""; //供审批框中显示
		var auditInfoShowBtn = 0 ; //控制流程信息弹出框中是否显示“审批”按钮
		
		var hasWindStation = true;
		
		$(document).ready(function(){
			//添加左边类型树
			FW.toggleSideTree(false);
			FW.addSideFrame({
				src:basePath+"page/ptw/core/tree.jsp?jumpMode=func",
				id:"assetTree",
				conditions :[
					{tab:"^ptw$",tree:"^ptw_sptolist$"},
					{tab:"^ptw$",tree:"^ptw_pto_ptolist$"},
					{tab:"^initNewSpto.+"},
					{tab:"^PTO.+"},
					{tab:"^newPto.+"},
					{tab:"^ptwSpto.+"},
					{tab:"^ptwPto.+"}
				]
			}); 
			
			/* form表单初始化 */
			$("#ptoForm").iForm("init",{"fields":fields,"options":{validate:true,initAsReadonly:formRead}});
			//操作项列表初始化
			
			//附件的控制
			$("#uploadfileTitle").iFold("init");
			$("#ptoItemListWrapper").iFold("init");
			$("#uploadfileTitle").iFold("show");
			$("#ptoItemListWrapper").iFold("show"); 
			initUploadform();
			
			//点击产生的页面
			$("#inPageTitle").html("操作票详情");
			$("#safeItemTest").iFold("init");
			$("#safeItemTest").iFold("show"); 
			
			showPage(ptoInfoVoData);
			
			FW.fixRoundButtons("#toolbar");
		});
		
    
	</script>

  </head>
  
  <body style="height: 100%; " class="bbox">
   	 <div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar" class="btn-toolbar ">
	    	<div class="btn-group btn-group-sm">
	        	<button id="btn_close" type="button" class="btn btn-default" onclick="closeCurPage(true);">关闭</button>
	        </div>
	    	<div id="btn_pto_operDiv" class="btn-group btn-group-sm">
				<button id="btn_pto_audit1" type="button" class="btn btn-default" onclick="audit();">审批</button>
			</div>
			 <div class="btn-group btn-group-sm">
	        	<button id="btn_newSpto" style="display:none" type="button" class="btn btn-default" onclick="newSpto();">生成标准操作票</button>
	        	<button id="pto_btn_copy" type="button" class="btn btn-default" tyle="display:none"  onclick="copyPto();" >复制</button>
	        </div>
	        
	        <div id="btn_printDiv" class="btn-group btn-group-sm">
	        	<button id="btn_print" type="button" class="btn btn-default" onclick="print();">打印</button>
	        </div>
	        
	        <div class="btn-group btn-group-sm">
	        	<button id="btn_auditInfo"  type="button" class="btn btn-default" onclick="showAuditInfo();">审批信息</button>
	        </div>
	    </div>
	</div>
	<div class="inner-title">
		<span id="inPageTitle">操作票详情</span>
	</div>
	
    <div>
    	 <form id="ptoForm" class="autoform"></form>
    </div>
    
	<div id="safeItemTest" grouptitle="操作步骤与内容">
		<div id="operItemTitle" style="position:relative;padding-left:130px;background:#CCCCCC" >
			<div style="width:150px;position:absolute;top:0;left:0">
				<div style="width:30px; font-size: 12px;float:left" ><span > 序号</span></div>
				<div style="width:40px; font-size: 12px;float:left"><span>标记</span></div>
				<div style="width:80px; font-size: 12px;float:left"><span>记录时间</span></div>	
			</div>
			<div style="padding-left: 10px;">
				<div style="width:70%; font-size: 12px;float:left"><span>操作项内容</span></div>
				<div style="width:30%; font-size: 12px;display:inline-block;padding-left: 10px;"><span>备注</span></div>	
			</div>
		</div>
		<div></div>
						
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
