var dataGrid = null;
/* 表单字段定义 */
var fields = [ {title : "ID",id : "id",type : "hidden"}, 
	{title : "ISDRAFT",id : "isDraft",type : "hidden"},
//	{title : "HISID",id : "hisId",type : "hidden"},
	{title : "STATUS",	id : "status",	type : "hidden"}, 
	{title : "SHEETNO",	id : "sheetNo",	type : "hidden"}, 
	{title : "标准操作票编号",id : "code",rules : {required:true}}, 
	{title : "版本号",id : "version",type : "label"}, 
	{title : "生效时间", id : "beginTime", type:"datetime", dataType:"datetime"},
	{title : "失效时间", id : "endTime", type:"datetime", dataType:"datetime"},
	{title : "流程ID",	id : "procinstId",	type : "hidden"},
	{title : "taskId",id : "taskId",type : "hidden"}, 
	{	title : "操作任务",	id : "mission",	type : "text",wrapXsWidth : 12,wrapMdWidth : 12,
		rules : {maxChLength : 600,	required : true	}		
	}, 
	{title : "关联设备",id : "equipment",type : "hidden"}, 
	{title : "关联设备",id : "equipmentName",wrapXsWidth : 12,wrapMdWidth : 12,type : "label",
		rules : {required : true},
		formatter:function(val){
			if(val == "请从左边目录树选择分类"){
				return "<span style='color:red'>请从左边目录树选择分类</span>";
			}
			return val ? val : "";
		}
	}, 
	{title : "类型",id : "type",linebreak : true,type : "combobox",dataType : "enum",	
		enumCat : "PTW_SPTO_TYPE",
		rules : {required : true},
		options:{
			allowEmpty:true
		}
	}, 
	{title : "创建人",id : "createUserInfo",linebreak : true,type : "label"}, 
	{title : "审核人",id : "auditUserInfo",type : "label"}, 
	{title : "批准人",id : "permitUserInfo",type : "label"},
	{title : "状态",id : "spdesp",type : "label"},
	{
        title : "操作项备注", 
        id : "operItemRemarks",
        type : "textarea",
        linebreak:true,
        wrapXsWidth:12,
        wrapMdWidth:8,
        height:110,
        rules : {maxChLength:680}
    }
	];

fields[4].render = function(id){
	$("#"+id).iHint('init', getiHintParams());
};

function getiHintParams(){
	var iHintParams ={
			datasource : basePath + "ptw/sptoInfo/sptoCodeMultiSearch.do",
			forceParse : function(){},
			clickEvent : function(id,name,rowdata) {
				$.post(basePath + "ptw/sptoInfo/hasSameCodeSptoInAudit.do",{"sptoCode":rowdata.code,"id":id},function(data){
					if(data.result == false){  //没有在审批中的标准操作票
						$("#inPageTitle").html("标准操作票("+rowdata.code+")升级版本");
						var temp = rowdata.type;
						var oldSptoId = rowdata.id ;
						rowdata.id = null;
						rowdata.type = null;  //先不给类型赋值，赋值的时候，如果下拉框没有这种类型，则赋值为空
						rowdata.beginTime =  rowdata.beginTime.time;
						rowdata.endTime = rowdata.endTime.time;
						$("#sptoForm").iForm("setVal", rowdata);
						//给类型赋值，如果存在的话
						for(var n = 0 ; n<privTypes.length ; n++){
							if(temp == privTypes[n]){
								$("#sptoForm").iForm("setVal",{"type":temp});
							}
						}
						//操作步骤赋值
						fillOperItemBySptoId(oldSptoId);
					}else{
						FW.error("该编号的标准票已有一张在审批中");
					}
				},"json");
				
			},
			"highlight":true,
			"formatter" : function(id,name,rowdata){
				var showText = rowdata.code + " / " + rowdata.mission;
				return showText;
			}
		};
	return iHintParams;
}
function fillOperItemBySptoId(sptoId){
	$.post(basePath + "ptw/sptoInfo/getSptoItems.do", {"id" : sptoId}, 
		function(data) {
		if (data && data.result) {
			$("#safeItemTest").find(".wrap-underline").remove();
			initSafeItemListByData("safeItemTest",{"safeDatas":data.result});
			beginEditSafeItemList("safeItemTest");
		} 
	}, "json");
}
/**
 * 判断myId 是否在候选人 candidateUsers 中
 * 
 * @param candidateUsers
 * @param myId
 */
function isMyActivitySpto(candidateUsers, myId) {
	for (var i = 0; i < candidateUsers.length; i++) {
		if (candidateUsers[i] == myId) {
			auditInfoShowBtn = 1;
			return true;
		}
	}
	return false;
}
/**
 * 提交标准操作票
 */
function commitSpto(commitStyle) {
	/** 非表单验证内容的验证 */
	var equipment = $("#sptoForm").iForm("getVal").equipment;
	var equipmentName = $("#sptoForm").iForm("getVal").equipmentName;
	if (equipment == '' || equipmentName == "请从左边目录树选择关联设备") {
		FW.error("请从左边树选择设备目录");
		return;
	}
	/** 表单验证 */
	if (!$("#sptoForm").valid()) {
		return;
	}
	// 表单
	$("#sptoForm").iForm("endEdit");
	var sptoFormObj = $("#sptoForm").iForm("getVal");
	var sptoFormData = FW.stringify(sptoFormObj);
	if(commitStyle == 'modifyCommit'){
		sptoFormObj.id = null;
	}
	
	$.post(basePath + "ptw/sptoInfo/hasSameCodeSptoInAudit.do",{"sptoCode":sptoFormObj.code,"id":sptoFormObj.id},function(data){
		if(data.result == false){  //没有在审批中的标准操作票
			// 获取附件数据
			var ids = $("#uploadform").iForm("getVal")["uploadfield"];
			// 操作项表格
			var sptoItemDataSet = []
			var getSafeItemInputsObj = getSafeItemInputs("safeItemTest");
			if(getSafeItemInputsObj.valid == false){
				FW.error(getSafeItemInputsObj.invalidMsg);
				$("#sptoForm").iForm("beginEdit");
				return false;
			}else{
				sptoItemDataSet = getSafeItemInputsObj.safeItems;
			}

			// 如果是暂存，恢复数据表格的可编辑状态
			if ("save" == commitStyle) {
				//恢复表单的可编辑
				$("#sptoForm").iForm("beginEdit");
			}

			var itemList = FW.stringify(sptoItemDataSet);
			
			$.post(basePath + "ptw/sptoInfo/commitSptodata.do", {
				"sptoForm" : sptoFormData,
				"itemList" : itemList,
				"commitStyle" : commitStyle,
				"uploadIds" : ids
			}, function(data) {
				if (data.result == "success") {
					taskId = data.taskId;
					if (commitStyle == "save") {
						$("#sptoForm").iForm("setVal", {
							"id" : data.id,
							"code" : data.code,
							"sheetNo":data.sheetNo,
							"status" : data.status
						});
					} else if ("firststepaudit" == commitStyle){
						//恢复可编辑状态
						$("#sptoForm").iForm("beginEdit");
						audit("audit");
					} else {
						$("#sptoForm").iForm("setVal", {
							"id" : data.id,
							"code" : data.code,
							"procinstId" : data.procinstId,
							"status" : data.status
						});
						audit("submit");
					}
					FW.success("保存成功");
				} else {
					FW.error("保存失败");
				}
			}, "json");
		}else{
			$("#sptoForm").iForm("beginEdit");
			FW.error("该编号的标准票已有一张在审批中");
		}
	},"json");
	
}
/**
 * 审批标准操作票记录
 */
function audit(type) {
	// 表单校验
	if (!$("#sptoForm").valid()) {
		return;
	}
	var sptoFormData = $("#sptoForm").iForm("getVal");
	processInstId = sptoFormData.procinstId;
	var id = sptoFormData.id;
	var status = sptoFormData.status;
	var major = sptoFormData.type;
	var variables = FW.stringify([ {
		"name" : "standardPtoId",
		"value" : id
	}, {
		"name" : "category",
		"value" : "spto"
	}, {
		"name" : "type",
		"value" : major
	} ]);
	$.ajaxSetup({
		'async' : false
	}); // 下面的修改关联设备必须要在审批框弹出之前执行完，所有这里要用同步
	workFlowAudit(sptoFormData, processInstId, variables, id, status,type);
	$.ajaxSetup({
		'async' : true
	});
}
/**
 * 审批弹出框
 */
function workFlowAudit(sptoFormData, processInstId, variables, id, status,type) {
	var url = basePath + "workflow/process_inst/setVariables.do";
	var status = $("#sptoForm").iForm("getVal","status");
	var params = {};
	if (undefined == taskId) {
		taskId = sptoFormData.taskId;
	}
	params['processInstId'] = processInstId;
	params['businessId'] = id;
	params['variables'] = variables;
	if("submit"==type){
		$.post(url, params, function(data) {
			if (data.result == 'ok') {
				var workFlow = new WorkFlow();
				workFlow.submitApply(taskId,FW.stringify(sptoFormData),closeCurPage,null,0,closeCurPage);
			}
		});
	}else if("firststep"==status && "audit"!=type){
		//如果是来自第一环节的审批
		commitSpto("firststepaudit");
	}else{
		$.post(url, params, function(data) {
			if (data.result == 'ok') {
				var workFlow = new WorkFlow();
				workFlow.showAudit(taskId, FW.stringify(sptoFormData), agree,rollback, stop, null, 0);
			}
		});
	}	
}
/**
 * 审批的三个回调函数，分别是“同意”，“回退”，“终止”
 */
// 同意回调
function agree() {
	closeCurPage();
};
// 回退回调
function rollback() {
	closeCurPage();
};
// 终止回调
function stop(rowdata) {
	var data = {};
	data['processInstId'] = rowdata.processInstId;
	data['reason'] = rowdata.reason;
	data['businessId'] = id;
	var url = 'ptw/sptoInfo/stopSpto.do';
	$.post(url, data, function(data) {
		if (data.result == 'success') {
			FW.success("审批成功");
			_parent().$("#itcDlg").dialog("close");
			closeCurPage();
			homepageService.refresh();
		} else {
			FW.error("审批失败");
		}
	});
}
/**
 * 删除
 */
function deleteSpto() {
	Notice.confirm("确定删除|确定删除该条标准操作票信息么？", function() {
		var sptoFormObj = $("#sptoForm").iForm("getVal");
		var id = sptoFormObj.id;
		$.post(basePath + "ptw/sptoInfo/deleteSptoDraft.do", {"id" : id}, function(data) {
			if (data.result == "success") {
				FW.success("删除成功");
				closeCurPage();
			} else {
				FW.error("删除失败");
			}
		}, "json");
	}, null, "info");
}
/**
 * 作废
 */
function obsoleteSpto() {
	Notice.confirm("确定作废|确定作废该条标准操作票信息么？", function() {
		var sptoFormObj = $("#sptoForm").iForm("getVal");
		var id = sptoFormObj.id;
		$.post(basePath + "ptw/sptoInfo/obsoleteSpto.do", {"id" : id }, function(data) {
			if (data.result == "success") {
				FW.success("作废成功");
				closeCurPage();
			} else {
				FW.error("作废失败");
			}
		}, "json");
	}, null, "info");
}


/*
 * 设置有效时间
 * */
function setValidTime(){
	var sptoFormObj = $("#sptoForm").iForm("getVal");
	var beginTime = sptoFormObj.beginTime;
	var endTime = sptoFormObj.endTime;
	FW.set("oldBegin",beginTime);
	FW.set("oldEnd",endTime);
    var src = basePath +  "ptw/sptoInfo/setValidTimePage.do"; 
    var btnOpts = [{"name" : "关闭",
		            "onclick" : function(){
		                return true;
		               }
		        	},
		           {
		            "name" : "确定",
		            "style" : "btn-success",
		            "onclick" : function(){
		                //itcDlgContent是对话框默认iframe的id
		                var p = _parent().window.document.getElementById("itcDlgContent").contentWindow;
		                /** 表单验证 */
		            	if (!p .$("#setValidTimeForm").valid()) {
		            		return;
		            	}
		                var setDateForm = p.$("#setValidTimeForm").iForm("getVal");
		                var sptoFormObj = $("#sptoForm").iForm("getVal");
		        		var sptoCode = sptoFormObj.code;
		                
        				$.post(basePath + "ptw/sptoInfo/setValidTimeData.do", 
		                		{"id" : sptoId,"beginTime":setDateForm.beginTime,
        					"endTime": setDateForm.endTime}, function(data) {
		        			if (data.result == "success") {
		        				FW.success("设置成功");
		        				$("#sptoForm").iForm("setVal",{
		        					"beginTime" : setDateForm.beginTime,
		        					"endTime" : setDateForm.endTime
		        				});
		        				 _parent().$("#itcDlg").dialog("close");
		        			} else {
		        				FW.error("设置失败");
		        			}
		        		}, "json");
		            }
		           }];
    var dlgOpts = {width : 300,height:200, title:"设置标准操作票有效时间"};
    FW.dialog("init",{"src":src,"dlgOpts":dlgOpts,"btnOpts":btnOpts}); 
}

/**
 * 检查时间设置是否合法
 * */
function checkValidTime(sptoId,sptoCode,timeJson){
	$.post(basePath + "ptw/sptoInfo/checkValidTimeData.do", 
    		{"id":sptoId,"code" : sptoCode,"beginTime":timeJson.beginTime,"endTime": timeJson.endTime}, function(data) {
		if (data.result == "success") {
			return true;
		} else {
			return false;
		}
	}, "json");
}
/**
 * 修改
 */
function modifySpto() {
	var closeTabId = FW.getCurrentTabId();
	var sptoFormObj = $("#sptoForm").iForm("getVal");
	$.post(basePath + "ptw/sptoInfo/hasSameCodeSptoInAudit.do",{"sptoCode":sptoFormObj.code,"id":sptoFormObj.id},function(data){
		if(data.result == false){  //没有在审批中的标准操作票
			newSptoPageWithSptoId(sptoFormObj.id,"标准操作票",closeTabId,"Y");
		}else{
			FW.error("该编号的标准票已有一张在审批中");
		}
	},"json");
	
    
}
/**
 * 关闭当前tab 页
 */
function closeCurPage(flag) {
	if (flag) {
		FW.set("SptolistDoNotRefresh", true);
	}
	FW.deleteTabById(FW.getCurrentTabId());
}

/**
 *  开放编辑
 */
function allBeginEdit(){
	$("#sptoForm").iForm("beginEdit");
	$("#uploadfileTitle").iFold("show");
	$("#uploadform").iForm("beginEdit");
}
/**
 * 隐藏所有按钮
 */
function hideAllButtons() {
	$("#btn_spto_save1").hide();
	$("#btn_spto_commit").hide();
	$("#btn_spto_audit1").hide();
	$("#btn_spto_delete").hide();
	$("#btn_spto_obsolete").hide();
	$("#btn_flowDiagram").hide();
	$("#btn_auditInfo").hide();
	$("#btn_print").hide();
	$("#btn_modifySpto").hide();
	$("#btn_spto_audit2").hide();
	Priv.map("false","pto_new");
	$("#b-add-SptoItemFromSpto").hide();
}
/**
 * 查看标准操作票基本信息，给form表单赋值
 */
function setFormVal(id) {
	$.post(basePath + "ptw/sptoInfo/queryItSptoDataById.do",
			{id : id},
			function(sptoBasicData) {
				var attachmentData = sptoBasicData.attachmentMap;
				var newThisSptoPriv = sptoBasicData.newThisSptoPriv;
				var taskId = sptoBasicData.taskId;
				var candidateUsers = sptoBasicData.candidateUsers;
				var list = sptoBasicData.list;
				var sptoFormData = eval("(" + sptoBasicData.sptoForm + ")");
				var createDate = sptoFormData.createDate;
				var auditDate = sptoFormData.auditDate;
				var permitDate = sptoFormData.permitDate;
				var isDraft = sptoFormData.isDraft;
				//拥有编辑权限的用户组
				var groups = sptoBasicData.groups;
				//格式化日期输出
				if (undefined != createDate) {
					createDate = new Date(createDate).format("yyyy-MM-dd");
				}
				if (undefined != auditDate) {
					auditDate = new Date(auditDate).format("yyyy-MM-dd");
				}
				if (undefined != permitDate) {
					permitDate = new Date(permitDate).format("yyyy-MM-dd");
				}
				processInstId = sptoFormData.procinstId;
				var status = sptoFormData.status;
				$("#sptoForm").iForm("hide",[ "beginTime","endTime" ]);
				var isRunningSpto = 'N';  //失效版本
				var currTime = new Date().getTime();				
				if(currTime >sptoFormData.beginTime && currTime<sptoFormData.endTime){ //正生效版本
					isRunningSpto = 'Y';
				}else if(currTime < sptoFormData.beginTime ){
					isRunningSpto = 'P';  //计划版本
				}
				if("Y"==isDraft){
					status = "draft";
				}
				if (sptoFormData.equipment == null) {
					sptoFormData.equipmentName = "<span style='color:red'>请从左边目录树选择关联设备</span>";
				}
				//rollback to firststep redefine type domain -- begin
				if(("draft"==status||"firststep"==status)&&sptoFormData.createuser == loginUserId){
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
				//rollback to firststep redefine type domain -- end
				$("#sptoForm").iForm("setVal", sptoFormData);
				$("#sptoForm").iForm("setVal", {
					"equipmentName" : sptoFormData.equipmentName
				});
				$("#sptoForm").iForm("setVal",{"createUserInfo" : sptoFormData.createUserName+ "/" + createDate});
				$("#sptoForm").iForm("setVal",{"auditUserInfo" : sptoFormData.auditUserName+ "/" + auditDate});
				$("#sptoForm").iForm("setVal",{"permitUserInfo" : sptoFormData.permitUserName+ "/" + permitDate});
				$("#sptoForm").iForm("setVal", {"taskId" : taskId});
				
				if (attachmentData.length > 0) {
					$("#uploadfileTitle").iFold("show");
					$("#uploadform").iForm("setVal", {uploadfield : attachmentData});
				}
				//初始化新数据表
				initSafeItemListByData("safeItemTest",{"safeDatas":list});
				// 是否为候选人
				if (null == candidateUsers) {
					candidateUsers = [];
				}
				var flag = isMyActivitySpto(candidateUsers, loginUserId);
				hideAllButtons();

				$("#sptoForm").iForm("endEdit");
				$("#uploadform").iForm("endEdit"); // 控制附件的权限（只能下载）
				$(".safe-input-content").show();
				$(".safe-input").hide();
				if (attachmentData.length == 0) {
					$("#uploadfileTitle").iFold("hide");
				}
				$("#sptoForm").iForm("hide",[ "spdesp","createUserInfo", "auditUserInfo","permitUserInfo" ]);
				switch (status) {
				case "firststep":
					if (sptoFormData.createuser == loginUserId) { // 如果有流程ID，即为退回的单（可以作废）
						$("#btn_spto_audit1").show();// 显示审批按钮
						$("#btn_spto_obsolete").show();// 显示作废按钮
						$("#btn_auditInfo").show(); // 显示查看审批流程信息按钮
						//$("#b-add-SptoItemFromSpto").show();// 显示从参考操作票导入
						allBeginEdit()//打开页面所有编辑
						$("#sptoForm").iForm("hide",[ "version" ]);
						beginEditSafeItemListInSpto("safeItemTest");
					} else {
						$("#btn_auditInfo").show(); // 显示查看审批流程信息按钮
					}
					break;
				case "secondstep":
					$("#btn_print").show();
					$("#sptoForm").iForm("show", [ "createUserInfo" ]);
					if (flag) { // 是候选人
						$("#btn_spto_audit1").show();// 显示审批按钮
						$("#btn_auditInfo").show(); // 显示查看审批流程信息按钮
					} else {
						$("#btn_auditInfo").show(); // 显示查看审批流程信息按钮
					}
					break;
				case "thirdstep":
					$("#btn_print").show();
					$("#sptoForm").iForm("show",[ "auditUserInfo", "createUserInfo" ]);
						if (flag) { // 是候选人
							$("#btn_spto_audit1").show();// 显示审批按钮
							$("#btn_auditInfo").show(); // 显示查看审批流程信息按钮
						} else {
							$("#btn_auditInfo").show(); // 显示查看审批流程信息按钮
						}
					break;
				case "draft":
					$("#inPageTitle").html("标准操作票记录（草稿）");
					if (sptoFormData.createuser == loginUserId) {
						flag = true;
						$("#btn_spto_commit").show();// 显示提交按钮
						$("#btn_spto_save1").show();// 显示暂存按钮
						$("#btn_flowDiagramDiv").show();// 显示无当前流程节点的流程信息图
						$("#btn_spto_delete").show();// 显示删除按钮
						//$("#b-add-SptoItemFromSpto").show();// 显示从参考操作票导入
						allBeginEdit()//打开页面所有编辑
						$("#sptoForm").iForm("hide",[ "version" ]);
						beginEditSafeItemListInSpto("safeItemTest");
					} else {
						$("#btn_flowDiagramDiv").show();// 显示无当前流程节点的流程信息图
					}
					
					break;
				case "passed":
					$("#sptoForm").iForm("show",[ "createUserInfo", "auditUserInfo","permitUserInfo","beginTime","endTime" ]);
					$("#btn_print").show();
					$("#btn_auditInfo").show();
					//判断，如果新建此类标准操作票的权限，则显示修改按钮
					if(newThisSptoPriv == true){
						$("#btn_modifySpto").show();
					}
					
					//此处要加一个权限控制
					if("Y"==isRunningSpto){  //运行版本
						$("#btn_setValidTime").show();
						if(0==privTypes_pto.length){
							Priv.map("false","pto_new");
						}else{
							for(var i = 0 ;i<=privTypes_pto.length;i++){
								if(privTypes_pto[i]==sptoFormData.type){
									Priv.map("true","pto_new");	
								}
							}
						}
					}else if(isRunningSpto=='N'){  //失效版本
						$("#sptoForm").iForm("show",[ "spdesp" ]);
					}else{  //计划版本
						$("#btn_setValidTime").show();
						Priv.map("false","pto_new");
					}
				default:
					$("#btn_auditInfo").show();
					break;
				}
				if (!flag) {
					$("#btn_spto_operDiv").hide();// 关闭 暂存 提交区域隐藏
					$("#btn_spto_delete").hide();// 不可删除
				}
				FW.fixRoundButtons("#toolbar"); // 解决按钮隐藏后的圆角标准操作票
				//显示相同的编号的标准操作票
				initDataGrid(sptoFormData.id,sptoFormData.code);
			}, 
	"json");
	
}

/**
 * 打印报表
 */ 
function print() {
	var printUrl = "http://timss.gdyd.com/";
	var url = fileExportPath
			+ "preview?__report=report/TIMSS2_PTW_SPTO_pdf.rptdesign&__format=pdf"
			+ "&id=" + sptoId + "&author=" + loginUserId + "&url=" + printUrl;
	var title = "标准操作票信息";
	FW.dialog("init", {
		src : url,
		btnOpts : [ {
			"name" : "关闭",
			"float" : "right",
			"style" : "btn-default",
			"onclick" : function() {
				_parent().$("#itcDlg").dialog("close");
			}
		} ],
		dlgOpts : {
			width : 800,
			height : 650,
			closed : false,
			title : title,
			modal : true
		}
	});

}

/**
 * 从参考操作票导入操作项
 */
function addSptoItemFromSpto(taskId, processInstId, parameters, index, id,
		status) {
	var src = basePath + "ptw/sptoInfo/preQuerySptoInfoVoListDlg.do";
	var btnOpts = [ {
		"name" : "确定",
		"style" : "btn-success",
		"onclick" : function() {
			var p = _parent().window.document.getElementById("itcDlgContent").contentWindow;
			var selectRow = p.$("#sptord_table").datagrid("getSelected");
			var id = "";
			if (null != selectRow) {
				id = selectRow["id"];
			}
			_parent().$("#itcDlg").dialog("close");
			var parameters = {
				id : id
			};
			if(""!=id){
			$.post(
				basePath + 'ptw/sptoInfo/getSptoItems.do',
				parameters,
				function(data) {
					if (data && data.result) {
						if($("#safeItemTest").children().length == 0){  
							initSafeItemListByData("safeItemTest",{"safeDatas":data.result});
						}else{
							for (var ind = 0; ind < data.result.length; ind++) {
								var row = eval(data.result[ind]);
								$("#safeItemTest").append(addSafeItemHtml);
								var len = $("#safeItemTest").children().length;
								$($("#safeItemTest").children()[len-1]).find(".safe-input").val(row.content);
								$($("#safeItemTest").children()[len-1]).find(".safe-input-content").html(FW.specialchars(row.content));
							}
						}
						//resetSnNew(("#sptoItemListWrp"));
					} else {
						FW.error(result.msg || "出错了，请重试");
					}
				});
			}
		}
	} ];
	var dlgOpts = {
		width : 640,
		height : 480,
		title : "从参考操作票导入操作项"
	};
	FW.dialog("init", {
		"src" : src,
		"dlgOpts" : dlgOpts,
		"btnOpts" : btnOpts
	});
}


/**
 *左边树菜单选择触发事件(只有点击的树节点为设备节点，且表单状态为可编辑的status才触发此功能)
 */
function onTreeItemClick(data){
	var selectType = data.faultTypeCode;
	var rootflag = data.type;
	var status = $("#sptoForm").iForm("getVal","status");
	if("passed"!=status&&"secondstep"!=status&&"thirdstep"!=status&&rootflag!='root'){
		$("#sptoForm").iForm("setVal",{
			equipment : data.id,
			equipmentName: data.text
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
/**
* 显示流程信息(审批时)
*/
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

/**
 * 初始化附件
 */
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
           		    "uploader" : basePath+"upload?method=uploadFile&jsessionid=" + sessId,
           		    "delFileUrl" : basePath+"upload?method=delFile&key=" + valKey,
           			"downloadFileUrl" : basePath + "upload?method=downloadFile",
           			"swf" : basePath + "js/workorder/common/uploadify.swf",
           			//"fileSizeLimit" : 10 * 1024,                  			
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
function beginEditSafeItemListInSpto(outerDivId){
	$("#" + outerDivId).iFold("show");
	$("#" + outerDivId).find("img").show();    //垃圾箱显示
	$(".wrap-underline").css("paddingRight",0);
	$(".safe-input-content").hide();   //隐藏不可编辑的
	$(".safe-input").show();  //显示可编辑的
}

//新建操作票
function newPto(){
	// 标准操作票id
	var id = $("#sptoForm").iForm("getVal").id;
	newPtoPageWithData(id);
}

// 其他版本的标准操作票
var sptoInfoDataGridColumn = [[ 
           {field:"version",title:"版本",width:80,fixed:true,
        	   formatter : function(value, row, index) {
        		   if(value==null || value==""){
        			   return 0;
        		   }else{
        			   return value;
        		   }
				}
           },
           {field:"mission",title:"操作任务",width:90},
           {field:"createUserName",title:"编写人",width:80,fixed:true},
           {field:"beginTime",title:"生效时间",width:110,fixed:true,
        	   formatter : function(value, row, index) {
					return FW.long2time(value);
				}
           },
           {field:"endTime",title:"失效时间",width:110,fixed:true,
        	   formatter : function(value, row, index) {
					return FW.long2time(value);
				}
           },
           {field:"status",title:"状态",width:100,fixed:true,
           	formatter: function(value,row,index){
               	return FW.getEnumMap("PTW_SPTO_STATUS")[value]; 
               },
               "editor" : {
               	"type":"combobox",
                   "options" : {
                   	"data" : FW.parseEnumData("PTW_SPTO_STATUS",_enum)	
                   }
               }
            }
       ]];
   /**
* 初始化数据表格
*/
function initDataGrid(sptoid,code){
   	var dataGrid = $("#sptoInfo_table").iDatagrid("init",{
   			pageSize:pageSize,//pageSize为全局变量
   			singleSelect:true,
		    url: basePath + "ptw/sptoInfo/sameCodeSptoListData.do?id="+sptoid+"&code="+code,	//basePath为全局变量，自动获取的       
		    columns:sptoInfoDataGridColumn,
		    onLoadSuccess: function(data){
		    	$("#title_sameCodeList").iFold("init");
				if(data && data.total==0){
					$("#title_sameCodeList").iFold("hide");
			    }else{
			    	$("#title_sameCodeList").iFold("show");
			 	}
		    },
		    onDblClickRow : function(rowIndex, rowData) {
		    	toSptoBaseInfoPage(rowData);
		   	}
	   	});
}

/**
 * 打开基本信息标签页
 */
function toSptoBaseInfoPage(rowData){
	var id = rowData.id;
	//新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
    var rand = new Date().getTime() + Math.floor(Math.abs(Math.random()*100));
    var opts = {
        id : "ptwSpto" + rand,
        name : "标准操作票",
        url : basePath+ "ptw/sptoInfo/todolistTOSptoPage.do?id="+id,
        tabOpt : {
        	closeable : true,
        	afterClose : "FW.deleteTab('$arg'); FW.activeTabById('ptw');FW.getFrame('ptw').refreshAfterClose();"
        }
    };
    _parent()._ITC.addTabWithTree(opts); 
}