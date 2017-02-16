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
	<title>新建操作票</title>
	<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
	<script>_useLoadingMask = true;</script>
	<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
	<script>
		var loginUserId = ItcMvcService.user.getUserId();
		var siteid = ItcMvcService.getUser().siteId;
		var ptoInfoVoData = ${ptoInfoVo};  //操作票信息
		var roleUserMap = ${roleUserMap};
		var ptoAttachment = ${attachmentMap};
		var taskId = ${taskId};
		var privTypes = ${privTypes};
		var isNeedFlow = ${isNeedFlow};
		var sessId = '<%=sessId%>';
		var valKey = '<%=valKey%>';
	</script>
	<script type="text/javascript" src="${basePath}js/pto/core/ptoUtil.js?ver=${iVersion}"></script>
	<script type="text/javascript" src="${basePath}/js/workflow/workflow.js?ver=${iVersion}"></script>
	<script type="text/javascript" src="${basePath}/js/common/safeItemTool.js?ver=${iVersion}"></script>
	<link rel="stylesheet" type="text/css" href="${basePath}css/ptw/ptoSafe.css?ver=${iVersion}"></link>
	<script type="text/javascript" src="${basePath}js/pto/core/ptoInfo.js?ver=${iVersion}"></script>
	<script type="text/javascript">
		var formRead = false;
		var createtime =""; //供审批框中显示
		var updatetime =""; //供审批框中显示
		var auditInfoShowBtn = 0 ; //控制流程信息弹出框中是否显示“审批”按钮
		var workflowKey = "ptw_"+siteid.toLowerCase()+"_pto";
		var workflowId = "";  //流程实例ID
		var canSelectTree = true;
		
		$(document).ready(function(){
			initSideTree();
			Priv.apply();
			/* form表单初始化 */
			if(isNeedFlow){
				$("#ptoForm").iForm("init",{"fields":fields,"options":{validate:true,initAsReadonly:formRead}});
			}else{
				$("#ptoForm").iForm("init",{"fields":fields_noflow,"options":{validate:true,initAsReadonly:formRead}});
			}
			
			$("#f_guardian").iCombo("init",{
			    data : roleUserMap[0].PTOGUARDIAN,
			    multiselect : true,
			    allowEmpty : true,
			    allowSearch : true
			});
			$("#f_commander").iCombo("init",{
			    data : roleUserMap[0].PTOCOMMANDER,
			    multiselect : true,
			    allowEmpty : true,
			    allowSearch : true
			});
			
			if(ptoInfoVoData != ""){
				//从标准票生成操作票时，隐藏状态信息
				$("#ptoForm").iForm("hide",["currStatus"]);
			}
			//重定义type，将登陆人没权限新建的类型除掉
			if(isNeedFlow){ //不走流程时，不需要过滤，因为它的下一步审批也是这个页面，可能审批人没有新建此类单权限
				changePtoTypeSource();
			}
			
			//操作项列表初始化
			
			//附件的控制
			$("#uploadfileTitle").iFold("init");
			$("#ptoItemListWrapper").iFold("init");
			$("#uploadfileTitle").iFold("show");
			$("#ptoItemListWrapper").iFold("show"); 
			initUploadform();
			
			//点击新建产生的页面
			$("#inPageTitle").html("新建操作票");
			if(ptoInfoVoData !="" && ptoInfoVoData.id){
				$("#inPageTitle").html("操作票详情");
			}
			$("#ptoForm").iForm("setVal",{"assetName":"请从左边目录树选择分类"});
			
			 $("#safeItemTest").iFold("init");
			$("#safeItemTest").iFold("show"); 
			
			showPage(ptoInfoVoData);
			
			FW.fixRoundButtons("#toolbar");
		});
		
		//过滤登陆用户可建的操作票类型
		function changePtoTypeSource(){
			var privTypeEnum = FW.parseEnumData("PTW_SPTO_TYPE",_enum);
			var privTypeList = []; 
			for(var i=0;i<privTypeEnum.length;i++){
				var type = privTypeEnum[i][0];
				for(var j=0 ;j<privTypes.length;j++){
					if(privTypes[j]==type){
						privTypeList.push(privTypeEnum[i]);
					}
				}
			}
			$("#f_type").iCombo("init",{data:privTypeList});
		}
    
	</script>

  </head>
  
  <body style="height: 100%; " class="bbox">
   	 <div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar" class="btn-toolbar ">
	    	<div class="btn-group btn-group-sm">
	        	<button id="btn_close" type="button" class="btn btn-default" onclick="closeCurPage();">关闭</button>
	        </div>
	    	<div id="btn_pto_operDiv" class="btn-group btn-group-sm">
	    		<button id="btn_pto_save1" type="button" class="btn btn-default" onclick="commitPto('save');">暂存</button>
				<button id="btn_pto_commit" type="button" class="btn btn-default" onclick="commitPto('commit');">提交</button>
				<button id="btn_pto_audit1" style="display:none" type="button" class="btn btn-default" onclick="audit();">审批</button>
				<button id="btn_pto_done"  style="display:none" type="button" class="btn btn-default priv" privilege="ptoOper_isDone" onclick="ptoOperDone();">已执行</button>
				<button id="btn_pto_undo"  style="display:none" type="button" class="btn btn-default priv" privilege="ptoOper_isDone" onclick="ptoOperUndo();">作废</button>
			</div>
	        <div id="btn_pto_deleteDiv" class="btn-group btn-group-sm">
	        	<button id="btn_pto_delete" style="display:none" type="button" class="btn btn-default" onclick="deletePto();">删除</button>
	        	<button id="btn_pto_obsolete" style="display:none" type="button" class="btn btn-default" onclick="obsoletePto();" style="display: none;">作废</button>
	        </div>
	         <div id="btn_importSptoDiv" class="btn-group btn-group-sm">
	        	<button id="btn_importSpto" type="button" class="btn btn-default" onclick="importSpto();">标准操作票导入</button>
	        </div>
	        <div id="btn_printDiv" class="btn-group btn-group-sm">
	        	<button id="btn_print" style="display:none" type="button" class="btn btn-default" onclick="print();">打印</button>
	        </div>
	        <div id="btn_flowDiagramDiv" class="btn-group btn-group-sm">
	        	<button id="btn_flowDiagram" type="button" class="btn btn-default" onclick="showDiagram();">审批信息</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	        	<button id="btn_auditInfo" style="display:none" type="button" class="btn btn-default" onclick="showAuditInfo();">审批信息</button>
	        </div>
	    </div>
	</div>
	<div  class="inner-title">
		<span id="inPageTitle">操作票详情</span>
	</div>
	
    <div>
    	 <form id="ptoForm" class="autoform"></form>
    </div>
    
	<div id="safeItemTest" grouptitle="操作步骤与内容"> </div>
	
     <!-- 附件层 -->
	<div class="margin-group"></div>
	<div grouptitle="附件"  id="uploadfileTitle">
		<div class="margin-title-table" id="uploadfile">
			<form id="uploadform" style=""></form>
		</div>
		
	</div>
  </body>
</html>
