<%@page import="com.yudean.itc.util.Constant"%>
<%@page import="com.yudean.itc.dto.sec.SecureUser"%>
<%@page import="com.yudean.itc.util.FileUploadUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String sessionid = session.getId();
	String delKey = FileUploadUtil.getValidateStr((SecureUser)session.getAttribute(Constant.secUser), FileUploadUtil.DEL_ALL);
 %>
<!DOCTYPE html>
<html style="min-height: 99%;">
<head>
<title>工作票详情</title>
<script>
	_useLoadingMask = true;
	var sessionid = '<%=sessionid%>';
	var delKey = '<%=delKey%>';
	var attachFiles = null;
</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script type="text/javascript" src="${basePath}js/ptw/OpenTabUtil.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/ptw/PtwUtil.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/ptw/commonPtwTree.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/ptw/core/ptwInterface.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/ptw/core/ptwBase.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/ptw/core/ptwButton.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/ptw/core/ptwButtonCommon.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/ptw/core/ptwSafe.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/ptw/core/ptwLic.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/ptw/core/ptwFile.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/ptw/core/ptwTask.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/ptw/core/ptwFireAudit.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}/js/workflow/workflow.js?ver=${iVersion}"></script>
<link rel="stylesheet" type="text/css" href="${basePath}css/ptw/ptwSafe.css?ver=${iVersion}"></link>
<script>
	var params = <%=request.getParameter("params")%>;
	//是否为新建工作票
	var isNewWt = params.opType == "newPtw" ? true : false;
	var isFireWt = (params.isFireWt && params.isFireWt == 1) ? true : false;
	//从标准工作票新建时，获取标准工作票的ID
	var sptwIdOnNewPtw = (params.sptwId) ? params.sptwId : null;
	//var sptwIdOnNewPtw = "j3dprmiqpxmybw7p";
	
	//工作票类型的定义信息
	var param_config = null;
	var isCopyWt = false;//是否点击了复制按钮
	
	//工作票类型的前缀
	var ptwTypeCode = (params.ptwTypeCode) ? params.ptwTypeCode : "";
	//登录用户可以开的工作票类型
	var ptwTypeCodes = (params.ptwTypeCodes) ? params.ptwTypeCodes : "";
	//动火工作票引用工作票带过来的类型
	var attachTypeCode = ptwTypeCode;
	
	//隔离措施的模板String
	var safeContentTpl = "";
	var safeAddContentTpl = "";
	
	var ptwId = null;
	var ptwTypeId = null;
	var ptwStatus = null;
	var ptwCreateUser = null;
	var loginUserId = ItcMvcService.user.getUserId();
	var siteid = ItcMvcService.getUser().siteId;
	
	var ptwNo = null;
	var ptwSafes = null;
	var ptwInfo = null;
	var ptwFireInfo = null;
	var attachPtwType = null;//动火票从属的工作票类型信息
	var ptwChangeWpic = null;//工作负责人变更信息;
	var ptwExtand = null;//工作票延期信息
	var ptwNeedRestore = false;//是否需要重新开工
	var ptwWaitRestores = null;//间断转移信息
	var ptwRemarkTask = null; //新增任务
		
	var isEditing = false;//是否处于编辑状态，用于判断签发和许可时是否需要将基本信息更新一次
	var canSelectTree = false; //能否根据左边的设备树选择设备
	var isPtwAdmin = false;//是否为管理员，需要为同步方法来判断
	
	var taskId = null;//流程节点Id
	var processInstId = null;//流程实例Id
	var ptwProcesses = null;
	var auditUsers = null;//当前审批的人列表
	var isBelongToAudit = false;//是否改当前人审批
	var fireIds = null;//附加的动火票Id
	function onTreeItemClick(node){
		if(canSelectTree){
			$("#baseInfoForm").iForm('setVal',{eqId:node.id,eqName:node.text,eqNo:node.assetCode});
		}
	}
	/**根据原来的数据增加姓名加时间的字段*/
	function joinNameAndTime(data,name,time){
		if(data[time] != null){
			data[name + "AndTime"] = data[name] + " / " + FW.long2time(data[time]);
		}
	}
	$(document).ready(function() {
		var prefix = new Date().getTime() + "" + Math.floor(Math.abs(Math.random()*100));
		initAssetTree();
		FW.toggleSideTree(false);    //展开服务目录树
		safeContentTpl = $("#safeContentTpl").html();
		safeAddContentTpl = $("#safeAddContentTpl").html();
		//判断是否为工作票模块的负责人
		var curRoles = ItcMvcService.getUser().roles;
		var adminToCompare = ItcMvcService.getUser().siteId + "_PTWADMIN";
		for(var i in curRoles){
			if(adminToCompare == curRoles[i].id){
				isPtwAdmin = true;
			}
		}
		$("#ptwToolbar .btn").hide();
		if(isNewWt){
			canSelectTree = true;
			if(isFireWt){
				$("#inner-title").html("新建动火票");
			}else{
				$("#inner-title").html("新建工作票");
			}
			param_queryWtType = isFireWt ? "fire" : "normal";
			initBtns();
			initNewBaseInfo();
			//by ahua  从标准工作票赋值
			if(sptwIdOnNewPtw){
				initDataFromSptw(sptwIdOnNewPtw);
			}
		}else if(params.opType == "handlePtw"){
			ptwId = params.id;
			$.post(basePath + "ptw/ptwInfo/queryBasePtwInfo.do",{id:ptwId},function(data){
				taskId = data.taskId;
				processInstId = data.processInstId;
				ptwProcesses = data.ptwProcesses;
				
				auditUsers = data.auditUsers;
				if(auditUsers){
					var curUser = ItcMvcService.user.getUserId();
					for(var i in auditUsers){
						if(auditUsers[i] == curUser){
							isBelongToAudit = true;
							break;
						}
					}
				}
				
				fireIds = data.fireIds;				
				ptwInfo = data.ptwInfo;
				ptwStatus = ptwInfo.wtStatus;
				ptwNo = ptwInfo.wtNo;
				ptwCreateUser = ptwInfo.createUser;
				attachFiles = data.fileMaps;
				$("#inner-title").html("工作票基本信息 " + ptwUtil.getStatusNameForFold(ptwStatus,taskId));
				
				//人名和时间的显示
				joinNameAndTime(ptwInfo,"createUserName","createDate");
				joinNameAndTime(ptwInfo,"issuer","issuedTime");
				joinNameAndTime(ptwInfo,"finWl","finWlTime");			
				joinNameAndTime(ptwInfo,"licWl","licTime");
				joinNameAndTime(ptwInfo,"approver","approveTime");
				
				ptwInfo.workContentFj = ptwInfo.workContent;
				//ptwInfo.workContentFire = ptwInfo.workContent;
				
				//设置工作票类型的值为类型名称
				ptwTypeId = ptwInfo.wtTypeId;
				ptwTypeCode = data.ptwType.typeCode;
				ptwInfo.wtTypeId = data.ptwType.typeName;
				//安全措施
				ptwSafes = data.ptwSafes;
				param_config = data.ptwTypeDefine;
							
				isFireWt = param_config.isFireWt == 1 ? true : false;
				
				//动火信息
				if(data.ptwFireInfo){
					
					if(data.attachPtwType){
						attachPtwType = data.attachPtwType;
						attachTypeCode = attachPtwType.typeCode;
						//根据工作票类型动态加载动火执行人选项
						var fireWpExecNoField = ptwUtil.findFieldById(fireBaseInfoField,"fireWpExecNo");
						fireWpExecNoField.options.url = basePath+ "ptw/ptwInfo/queryPtwUsersByGroup.do?role=PTW_workpic_"+attachTypeCode+"&prefix="+prefix;
					}
					ptwFireInfo = data.ptwFireInfo;
					//人名和时间的显示
					joinNameAndTime(ptwFireInfo,"appvXf","appvXfTime");
					joinNameAndTime(ptwFireInfo,"appvAj","appvAjTime");
					joinNameAndTime(ptwFireInfo,"appvBm","appvBmTime");			
					joinNameAndTime(ptwFireInfo,"appvCj","appvCjTime");
					
					$("#fireBaseInfoDiv").show().iFold("init");
					$("#fireBaseInfoForm").iForm("init",{"fields":fireBaseInfoField,"options":{validate:true,initAsReadonly:true}});
					$("#fireBaseInfoForm").iForm("setVal",ptwFireInfo);
					
					//申请动火时间标题修改 修改成必填
					var preWorkStartTimeField = ptwUtil.findFieldById(baseInfoField,"preWorkStartTime");
					preWorkStartTimeField.title = "申请动火时间";
					preWorkStartTimeField.rules = {required:true};
					var preWorkEndTimeField = ptwUtil.findFieldById(baseInfoField,"preWorkEndTime");
					preWorkEndTimeField.title = "申请结束时间";
					preWorkEndTimeField.rules = {required:true, greaterThan:"#f_preWorkStartTime"};
					//工作负责人标题修改
					var licWpicNoField = ptwUtil.findFieldById(baseInfoField,"licWpicNo");
					licWpicNoField.title = "动火工作负责人";
					licWpicNoField.options.url = basePath+ "ptw/ptwInfo/queryPtwUsersByGroup.do?role=PTW_workpic_"+attachTypeCode+"&prefix="+prefix;
					
				}else{
					//根据工作票类型动态加载工作负责人选项
					var licWpicNoField2 = ptwUtil.findFieldById(baseInfoField,"licWpicNo");
					licWpicNoField2.options.url = basePath+ "ptw/ptwInfo/queryPtwUsersByGroup.do?role=PTW_workpic_"+ptwTypeCode+"&prefix="+prefix;
				}
				
				
				
				$("#baseInfoForm").iForm("init",{"fields":baseInfoField,"options":{validate:true,initAsReadonly:true}});
				$("#baseInfoForm").iForm("setVal",ptwInfo);
				
				initBaseInfoByConfig();
				initSafeInfoByConfig();
				if(ptwStatus == 500 || ptwStatus == 600 || ptwStatus == 700){
					initLicInfo(ptwTypeCode);
				}
				//初始化更多办理
				initMoreInfo(data);
				initBtns();
			},"json");
		}
	});
	
</script>

</head>
<body style="height: 100%;" class="bbox">
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <div id="ptwToolbar" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm">
			    <button type="button" class="btn btn-default" id="ptw_btn_close" onclick="closePtw(true);">关闭</button>
			</div>
	        
	        <div class="btn-group btn-group-sm">
	        	 <button type="button" class="btn btn-success" id="ptw_btn_process" onclick="processPtw();">审批</button>
	        </div>
	        
	        <div class="btn-group btn-group-sm">
	        	 <button type="button" class="btn btn-success" id="ptw_btn_depart" onclick="departPtw();">审批</button>
	        </div>
	        
	        <div class="btn-group btn-group-sm">
	        	 <button type="button" class="btn btn-success" id="ptw_btn_auditing" onclick="auditingPtw();">审核</button>
	        </div>
	        
	        <div class="btn-group btn-group-sm">
	        	 <button type="button" class="btn btn-success" id="ptw_btn_newSubmit" onclick="savePtwInfo();" data-loading-text="保存中...">保存</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	        	 <button type="button" class="btn btn-success priv only-hide" privilege="ptw-issue" id="ptw_btn_iuuse" onclick="issuePtw();">签发</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	        	 <button type="button" class="btn btn-success priv only-hide" privilege="ptw-lic" id="ptw_btn_apprv" onclick="preLicPtw();">许可</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	        	 <button type="button" class="btn btn-success" id="ptw_btn_apprvSubmit" onclick="licPtw();">提交</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	        	 <button type="button" class="btn btn-success priv only-hide" privilege="ptw-lic" id="ptw_btn_finish" onclick="finPtw();">结束</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	        	 <button type="button" class="btn btn-success priv only-hide" privilege="ptw-lic" id="ptw_btn_end" onclick="endPtw();">终结</button>
	        </div>
	        
	        <div class="btn-group btn-group-sm" id="ptw_btn_moreDiv">
			    <button type="button" class="btn btn-default dropdown-toggle  priv only-hide" privilege="ptw-lic" id="ptw_btn_more"  data-toggle="dropdown">
			   	 	更多办理
			   	 	<span class="caret"></span>
			    </button>
			</div>
			
	        <div class="btn-group btn-group-sm" style="display:none;" id="div_btn_group">
	        	 <button type="button" class="btn btn-default" style="display: none;" onclick="tempSavePtwInfo(true);" id="ptw_btn_tempSave" data-loading-text="暂存中...">暂存</button>
	        	 <button type="button" class="btn btn-default" style="display: none;" onclick="editPtwBaseInfoAndSafe();" id="ptw_btn_modify">编辑</button>
	        	 <button type="button" class="btn btn-default" style="display: none;" onclick="remarkPtw();" id="ptw_btn_remark">备注</button>
	        	 <button type="button" class="btn btn-default priv only-hide"  privilege="ptw-cancel" style="display: none;" onclick="cancelPtw();" id="ptw_btn_abort">作废</button>
	        	 <button type="button" class="btn btn-default" style="display: none;" onclick="deletePtw();" id="ptw_btn_delete">删除</button>
	        	 <button type="button" class="btn btn-default" style="display: none;" onclick="addOtherSafeContents();" id="ptw_btn_addSafe">补充安全措施</button>
	        	 <button type="button" class="btn btn-default" style="display: none;" onclick="newFirePtwInfo();" id="ptw_btn_attachFire">附加动火票</button>
	        	 <button type="button" class="btn btn-default" style="display: none;" onclick="showUploadFileDiv();" id="ptw_btn_uploadFile">附加文件</button>
	        	 <button type="button" class="btn btn-default" style="display: none;" onclick="copyPtw();" id="ptw_btn_copy">复制</button>
	        </div>
	        
			<div class="btn-group btn-group-sm">
	        	 <!-- <button id="ptw_btn_importSptw" type="button" class="btn btn-default priv" privilege="ptw-ptwfromsptw" style="display:none"  onclick="importSptw();">标准工作票导入</button> -->
	        	 <button id="ptw_btn_ptwNewSptw" type="button" class="btn btn-default priv" privilege="ptw-ptwtosptw" style="display:none"  onclick="ptwNewSptw();">生成标准工作票</button>
	        </div>
	        
	        <!-- <div class="btn-group btn-group-sm">
	        	 <button id="ptw_btn_importHisPtw" type="button" class="btn btn-default" style="display:none"  onclick="importHisPtw();">由历史票导入</button>
	        </div> -->
	        <div class="btn-group btn-group-sm">
	        	 <button id="importButtonGroup" type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
	        	 	导入
	        	 	<span class="caret"></span> <!-- 添加"新建"文字右边的"下箭头" -->
	        	 </button>
	        	 <ul id="importButton" class="dropdown-menu"></ul>
	        </div>
	        
			<div class="btn-group btn-group-sm">
	        	 <button type="button" class="btn btn-default" id="ptw_btn_queryFireWtSingle">查看动火票</button>
	        </div>
			<div class="btn-group btn-group-sm" id="ptw_btn_queryFireWtDiv">
			    <button type="button" class="btn btn-default dropdown-toggle" id="ptw_btn_queryFireWt"  data-toggle="dropdown">
			   	 	查看动火票
			   	 	<span class="caret"></span>
			    </button>
			</div>
			<div  class="btn-group btn-group-sm" >
				 <button type="button" class="btn btn-default" style="display: none;" onclick="printWt();" id="ptw_btn_print">打印</button>
			</div>
			 <div class="btn-group btn-group-sm" id="ptw_btn_auditInfoDiv">
			    <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" id="ptw_btn_auditInfo">
			   	 	审批信息
			   	 	<span class="caret"></span>
			    </button>
			</div>
	    </div>
	</div>
	<div id="inner-title" class="inner-title">
		工作票基本信息
	</div>
	<form id="baseInfoForm"></form> 
	<div id="fireBaseInfoDiv" style="display: none;" grouptitle="动火部门、人员">
		<form id="fireBaseInfoForm"></form>
	</div>
	<div id="licInfoDiv" style="display: none;" grouptitle="许可信息">
		<form id="licInfoForm"></form>
	</div>
	
	<script type="text/html" id="safeContentTpl">
		<div class="wrap-underline">
		    <span class="safe-number-span"></span>
		    <span class="safe-input-content" ></span>
			<input class="safe-input" type="text" onkeyup="bindNewSafeEvent(this);" style="display: none;"/>
		    <span class="exec-span"></span>
			<span class="safe-unremove-remarks" ></span>
		    <span class="remover-span"></span>
			<img width="17" height="18" style="display: none;" class="safe-img" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA0AAAAOCAIAAAB7HQGFAAAABmJLR0QA/wD/AP+gvaeTAAAACXBIWXMAAA7EAAAOxAGVKw4bAAABe0lEQVQokY3Ru0scURgF8O+7j5lxdXVxR2QiGrQIZKsgIoJdHmAh+b8S8n+ktROSQkJSpU1ChIAKxsH37M7O3Dv3NV+KbEy7p/415xwkIpgiAgCICBHviuo8L8uxWn+crmY9IkD879D7UGs7rvTh0fHp2WWnE98Wav/FYOvZBuc414kn7v3B10dZv9eVVWWiSDIGw5FKkkhpf/K7fLW7sb7WBwBx9OU78sjZJnhDrW90pXXjnREyJj63korVbF5IiVe35cfPJ2tZZ6GbOB8Q8cOnn8babz9+7T3ffP1y0E9TzjmjlgBIRiJJZDHSxoZKmTgSQNCdnUmShHMOACwE19RDILQ2SMnui7quFBF6b9W4YIz97cGcs9c3l1yIWpuLfKi0CdQqbUyjRuUdPjggsk1NxBrjuGSIMBNLpa21CgHg3wkiTRcHT5+8eftOcLLGEFELMrS41Jvf3tkNIUz2IyLnHADL8/xhfc45YyzLlkMIQoiJm+ZfNg0CgD/9HMi6IOQOSwAAAABJRU5ErkJggg==" 
			onclick="removeSafeItem(this)"/>		    
		</div>
	</script>
	<script type="text/html" id="safeAddContentTpl">
		<div class="wrap-underline wrap-underline-single">
		    <span class="safe-number-span"></span>
		    <span class="safe-input-content" style="display: none;"></span>
			<input class="safe-input" type="text" onkeyup="bindNewSafeEvent(this);"/>
			<img width="17" height="18" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA0AAAAOCAIAAAB7HQGFAAAABmJLR0QA/wD/AP+gvaeTAAAACXBIWXMAAA7EAAAOxAGVKw4bAAABe0lEQVQokY3Ru0scURgF8O+7j5lxdXVxR2QiGrQIZKsgIoJdHmAh+b8S8n+ktROSQkJSpU1ChIAKxsH37M7O3Dv3NV+KbEy7p/415xwkIpgiAgCICBHviuo8L8uxWn+crmY9IkD879D7UGs7rvTh0fHp2WWnE98Wav/FYOvZBuc414kn7v3B10dZv9eVVWWiSDIGw5FKkkhpf/K7fLW7sb7WBwBx9OU78sjZJnhDrW90pXXjnREyJj63korVbF5IiVe35cfPJ2tZZ6GbOB8Q8cOnn8babz9+7T3ffP1y0E9TzjmjlgBIRiJJZDHSxoZKmTgSQNCdnUmShHMOACwE19RDILQ2SMnui7quFBF6b9W4YIz97cGcs9c3l1yIWpuLfKi0CdQqbUyjRuUdPjggsk1NxBrjuGSIMBNLpa21CgHg3wkiTRcHT5+8eftOcLLGEFELMrS41Jvf3tkNIUz2IyLnHADL8/xhfc45YyzLlkMIQoiJm+ZfNg0CgD/9HMi6IOQOSwAAAABJRU5ErkJggg==" 
			onclick="removeSafeItem(this)"/>
		</div>
	</script>
	<div id="safeItem1" class="safeItems" safeType="1" grouptitle="必须采取的安全措施"></div>
	<div id="safeItem2" class="safeItems" safeType="2" grouptitle="检修必须采取的安全措施"></div>
	<div id="safeItem3" class="safeItems" safeType="3" grouptitle="运行必须采取的安全措施"></div>
	<div id="safeItem4" class="safeItems" safeType="4" grouptitle="应接地线"></div>
	<div id="safeItem5" class="safeItems" safeType="5" grouptitle="补充安全措施"></div>
	<div id="safeItem6" class="safeItems" safeType="6" grouptitle="保留带电部分">
		<div id="safeItem6Content" class="wrap-underline wrap-underline-single">
		    <span class="safe-input-content" style="display: none;"></span>
		    <input class="safe-input" type="text"/>
		</div>
	</div>
		
	<div id="changeWpicDiv" style="display: none;" grouptitle="工作负责人变更">
		<form id="changeWpicForm"></form>
	</div>
	<div id="extandDiv" style="display: none;" grouptitle="工作票延期">
		<form id="extandForm"></form>
	</div>
	<div id="waitRestoreDiv" style="display: none;" grouptitle="工作票间断和转移">
	</div>
	<div id="remarkTaskDiv" style="display: none;" grouptitle="新增任务">
		<form id="remarkTaskForm"></form>
	</div>
	<div id="remarkDiv" style="display: none;" grouptitle="备注">
		<form id="remarkForm"></form>
	</div>
	
	<div id="uploadFileDiv" style="display: none;" grouptitle="附加文件">
		<form id="uploadFileForm"></form>
	</div>
</body>
</html>