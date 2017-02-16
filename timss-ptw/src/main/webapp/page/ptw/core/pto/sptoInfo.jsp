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
	<title>新建标准操作票</title>
	<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
	<script>_useLoadingMask = true;</script>
	<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
	<script type="text/javascript" src="${basePath}js/pto/core/sptoInfo.js?ver=${iVersion}"></script>
	<script type="text/javascript" src="${basePath}js/pto/core/ptoInterface.js?ver=${iVersion}"></script>
	<script type="text/javascript" src="${basePath}/js/workflow/workflow.js?ver=${iVersion}"></script>
	<script type="text/javascript" src="${basePath}js/common/safeItemTool.js?ver=${iVersion}"></script>
	<link rel="stylesheet" type="text/css" href="${basePath}css/ptw/ptoSafe.css?ver=${iVersion}"></link>
	
	
	<script>
		var createUserInfo = "${createUserInfo}";
		var sptoId = "${sptoId}";
		var categoryId = "${categoryId}";
		var categoryName = "${categoryName}";
		var defKey= "${defKey}";
		var sptoData= ${sptoData};
		var sptoItems= ${sptoItems};
		var sptoAttachment = ${sptoAttachment};
		var title= "${title}";
		var modify = "${modify}";
		var sessId = '<%=sessId%>';
		var valKey = '<%=valKey%>';
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
		var privTypes = ${privTypes};
		var privTypes_pto = ${privTypes_pto};
		$(document).ready(function(){
			var safeAddContentTpl = $("#safeAddContentTpl").html();
			//添加左边类型树
			FW.toggleSideTree(false);
			
			FW.addSideFrame({
				src:basePath+"page/ptw/core/tree.jsp?jumpMode=func",
				id:"assetTree",
				conditions :[
					{tab:"^ptw$",tree:"^ptw_sptolist$"},
					{tab:"^initNewSpto.+"},
					{tab:"^ptwSpto.+"}
				]
			}); 
			
			$.ajaxSetup({'async':false}); 
			/* form表单初始化 */
			$("#sptoForm").iForm("init",{"fields":fields,"options":{validate:true,initAsReadonly:formRead}});
			//附件的控制
			$("#uploadfileTitle").iFold("init");
			$("#sptoItemListWrapper").iFold("init");
			$("#uploadfileTitle").iFold("show");
			$("#sptoItemListWrapper").iFold("show"); 
			initUploadform();
			$("#sptoForm").iForm("setVal",{"createUserInfo":createUserInfo});
			//初始化新的操作项
			$("#safeItemTest").iFold("init");
			if(sptoId != null && sptoId != "null"){
				setFormVal(sptoId);
			}else if(sptoId == null || sptoId == "null"){  
				//重定义type --begin
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
				//重定义type --end
				$("#sptoForm").iForm("hide",["version","spdesp","createUserInfo","auditUserInfo","permitUserInfo","beginTime","endTime"]);
				if("{}"!=FW.stringify(sptoData)){
					if("Y"==modify){
						//修改标准操作票打开标准操作票页面
						//修改标准操作票的页面
						$("#inPageTitle").html("修改操作票记录");
						sptoData.beginTime = null;
						sptoData.endTime = null;
						$("#sptoForm").iForm("setVal",sptoData);
						initSafeItemListByData("safeItemTest",{"safeDatas":sptoItems});
						beginEditSafeItemListInSpto("safeItemTest");
						if (sptoAttachment.length > 0) {
							$("#uploadform").iForm("setVal", {uploadfield : sptoAttachment});
						}
						$("#btn_spto_save1").hide();
						$("#btn_spto_commit").hide();
						$("#btn_spto_audit2").show();
					}else if("C"==modify){
						//暂存后打开标准操作票页面
						//生成标准操作票的页面
						$("#inPageTitle").html(title);
						$("#sptoForm").iForm("setVal",{"mission":sptoData.mission,"equipment":sptoData.equipment,
						"equipmentName":sptoData.equipmentName,"type":sptoData.type,"operItemRemarks":sptoData.operItemRemarks});
						initSafeItemListByData("safeItemTest",{"safeDatas":sptoItems});
						beginEditSafeItemListInSpto("safeItemTest");
						//$("#b-add-SptoItemFromSpto").show();// 显示从参考操作票导入
						if (sptoAttachment.length > 0) {
							$("#uploadform").iForm("setVal", {uploadfield : sptoAttachment});
						}
					}
				}else{
					beginEditSafeItemList("safeItemTest");
					//点击新建产生的页面
					$("#inPageTitle").html("新建标准操作票");
					//当选择了左边设备树然后新建时，直接将设备信息带过去
					if(categoryId){
						$("#sptoForm").iForm("setVal",{"equipment":categoryId,"equipmentName":categoryName});
					}else{
						$("#sptoForm").iForm("setVal",{"equipmentName":"请从左边目录树选择分类"});
					}
				}
			}
			//控制权限
			Priv.apply();
			if(sptoId != null && sptoId != "null"){
				var sptoFormObj = $("#sptoForm").iForm("getVal");
				var endTime = sptoFormObj.endTime;
				if(new Date().getTime() > endTime){
					$("#btn_setValidTime").hide();
				}
			}else if(sptoId == null || sptoId == "null"){  
				$("#btn_modifySpto").hide();
				$("#btn_setValidTime").hide();
			}
			$.ajaxSetup({'async':true});
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
	    	<div id="btn_spto_operDiv" class="btn-group btn-group-sm">
	    		<button id="btn_spto_save1" type="button" class="btn btn-default" onclick="commitSpto('save');">暂存</button>
				<button id="btn_spto_commit" type="button" class="btn btn-default" onclick="commitSpto('commit');">提交</button>
				<button id="btn_spto_audit1" style="display:none" type="button" class="btn btn-default" onclick="audit();">审批</button>
			</div>
	        <div id="btn_spto_deleteDiv" class="btn-group btn-group-sm">
	        	<button id="btn_spto_delete" style="display:none" type="button" class="btn btn-default" onclick="deleteSpto();">删除</button>
	        	<button id="btn_spto_obsolete" style="display:none" type="button" class="btn btn-default" onclick="obsoleteSpto();" style="display: none;">作废</button>
	        </div>
	        <div id="btn_printDiv" class="btn-group btn-group-sm">
	        	<button id="btn_print" style="display:none" type="button" class="btn btn-default" onclick="print();">打印</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	        	<button id="btn_modifySpto" style="display:none" type="button" class="btn btn-default" onclick="modifySpto();">修改</button>
	        	<button id="btn_spto_audit2" style="display:none" type="button" class="btn btn-default"  onclick="commitSpto('modifyCommit');">保存</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	        	<button id="btn_setValidTime" style="display:none" type="button" class="btn btn-default priv"  privilege="ptw-spto-setTime" onclick="setValidTime();">设置有效时间</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	        	<button id="btn_newPto" type="button" class="btn btn-default priv" privilege = "pto_new" onclick="newPto();">生成操作票</button>
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
		<span id="inPageTitle">标准操作票详情</span>
	</div>
	
    <div>
    	<form id="sptoForm" class="autoform"></form>
    </div>
	<script type="text/html" id="safeAddContentTpl">
		<div class="wrap-underline wrap-underline-single">
		    <span class="safe-number-span"></span>
		    <span class="safe-input-content" style="display: none;"></span>
			<input class="safe-input" type="text" onkeyup="bindSptoItemEvent(this);"/>
		</div>
	</script>
	<div id="sptoItemListWrp" class="safeItems" grouptitle="操作项"></div>
	<div id="safeItemTest" grouptitle="操作项"></div>
	<%-- 
	<div class="btn-toolbar margin-foldable-button" id="dataGridBtnGrp">
		<div class="btn-group btn-group-xs">
			<button type="button" class="btn btn-default" onclick="addSptoItemFromSpto();" id="b-add-SptoItemFromSpto">从参考操作票导入</button>
		</div>
	</div>
	--%>
     <!-- 附件层 -->
	<div class="margin-group"></div>
	<div grouptitle="附件"  id="uploadfileTitle">
		<div class="margin-title-table" id="uploadfile">
			<form id="uploadform" style=""></form>
		</div>
	</div>
	<!-- 同编号的其他版本 -->
	<div style="clear:both"></div>
	<div id="title_sameCodeList" grouptitle="其他版本">
		<div id="grid1_wrap" style="width:100%">
		    <table id="sptoInfo_table"  class="eu-datagrid">
		    </table>
		</div>
	</div>
	
	
  </body>
</html>
