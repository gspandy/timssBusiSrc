/* 表单字段定义 */
fields = [ {title : "ID",id : "id",type : "hidden"}, 
               {title : "SPTOID",id : "sptoId",type : "hidden"},
               {title : "编号",	id : "code",	type : "hidden"}, 
               {id:"windStation",title : "风电场",type:"combobox",
            	   dataType:"enum",
            	   enumCat:"WO_WIND_STATION",
            	   options : {allowEmpty : true},
            	   rules : {
						required : true
					}
               },
               {title : "操作任务",	id : "task",
					rules : {
						maxChLength : 600,
						required : true
					},
					wrapXsWidth : 12,
					wrapMdWidth : 12,
					type : "text"
               }, 
               {title : "状态",	id : "currStatus",	type : "combobox",dataType : "enum", enumCat : "PTW_PTO_STATUS"}, 
				{title : "关联设备",	id : "assetId",	linebreak : true, type : "hidden"}, 
				{title : "关联设备",
					id : "assetName",
					type : "label",
					rules : {
						required : true
					},
					formatter:function(val){
						if(val == "请从左边目录树选择分类"){
							return "<span style='color:red'>请从左边目录树选择分类</span>";
						}
						return val ? val : "";
					}
				}, 
				{title : "预计操作时间", id : "preBeginOperTime", type:"datetime", dataType:"datetime"},
				{title : "预计结束时间", id : "preEndOperTime", type:"datetime", dataType:"datetime",
	          	  rules : {greaterThan:"#f_preBeginOperTime"} 
	            },
				{title : "类型",id : "type",	type : "combobox",dataType : "enum",
					enumCat : "PTW_SPTO_TYPE",
					rules : {
						required : true
					}					
				}, 
				{title : "监护人",id : "guardian",type : "combobox",data:roleUserMap[0].PTOGUARDIAN,
					rules : {
						required : true
					}	
				},
				{title : "发令人",	id : "commander",	type : "combobox",data:roleUserMap[0].PTOCOMMANDER,
					rules : {
						required : true
					}	
				},
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
/* 表单字段定义 ---为无流程的情况，监护人和发令人非必填*/
var fields_noflow = [ {title : "ID",id : "id",type : "hidden"}, 
           {title : "SPTOID",id : "sptoId",type : "hidden"},
           {title : "编号",	id : "code",	type : "hidden"}, 
           {id:"windStation",title : "风电场",type:"combobox",
        	   dataType:"enum",
        	   enumCat:"WO_WIND_STATION",
        	   options : {allowEmpty : true},
        	   rules : {
					required : true
				}
           },
           {title : "操作任务",	id : "task",
				rules : {
					maxChLength : 600,
					required : true
				},
				wrapXsWidth : 12,
				wrapMdWidth : 12,
				type : "text"
           }, 
           {title : "状态",	id : "currStatus",	type : "combobox",dataType : "enum", enumCat : "PTW_PTO_STATUS"}, 
			{title : "关联设备",	id : "assetId",	linebreak : true, type : "hidden"}, 
			{title : "关联设备",
				id : "assetName",
				type : "label",
				rules : {
					required : true
				},
				formatter:function(val){
					if(val == "请从左边目录树选择分类"){
						return "<span style='color:red'>请从左边目录树选择分类</span>";
					}
					return val ? val : "";
				}
			}, 
			{title : "预计操作时间", id : "preBeginOperTime", type:"datetime", dataType:"datetime"},
			{title : "预计结束时间", id : "preEndOperTime", type:"datetime", dataType:"datetime",
          	  rules : {greaterThan:"#f_preBeginOperTime"} 
            },
			{title : "类型",id : "type",	type : "combobox",dataType : "enum",
				enumCat : "PTW_SPTO_TYPE",
				rules : {
					required : true
				}					
			}, 
			{title : "监护人",id : "guardian",type : "combobox",data:roleUserMap[0].PTOGUARDIAN},
			{title : "发令人",	id : "commander",	type : "combobox",data:roleUserMap[0].PTOCOMMANDER},
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

/**
 * 判断myId 是否在候选人 candidateUsers 中
 * 
 * @param candidateUsers
 * @param myId
 */
function isMyActivityPto(candidateUsers, myId) {
	for (var i = 0; i < candidateUsers.length; i++) {
		if (candidateUsers[i] == myId) {
			auditInfoShowBtn = 1;
			return true;
		}
	}
	return false;
}

function showPage(ptoInfoVoData){
	if(typeof(hasWindStation)=="undefined"){
		$("#ptoForm").iForm("hide",["windStation"]);
	}
	if(ptoInfoVoData){
		var flag = false;
		var currStatus = ptoInfoVoData.currStatus;
		var currHandlerUser = ptoInfoVoData.currHandlerUser;
		workflowId = ptoInfoVoData.workflowId;
		if(currHandlerUser != "" && currHandlerUser != null){
			var currHandlerUserArray = currHandlerUser.split(",");
			flag = isMyActivityPto(currHandlerUserArray,loginUserId);
		}
		if(currStatus == "draft" || currStatus == null){  //草稿
			flag = true;
			if(currStatus == "draft"){
				$("#btn_pto_delete").show();
				$("#ptoForm").iForm("endEdit",["currStatus"]);
				if(workflowId){//提交取消后草稿显示工作流审批信息
					$("#btn_flowDiagram").hide();
					$("#btn_auditInfo").show();
				}
			}
			$("#btn_pto_done").hide();
			$("#btn_pto_undo").hide();
			
		}else if(currHandlerUser == loginUserId && currStatus == "new"){  //回退回来的
			$("#btn_pto_save1").hide();
			$("#btn_pto_commit").hide();
			$("#btn_pto_audit1").show();
			$("#btn_pto_obsolete").show();
			$("#btn_flowDiagram").hide();
			$("#btn_auditInfo").show();
		}else{ //  不是草稿，是新建，退回来的，但是不是创建人
			$("#btn_pto_save1").hide();
			$("#btn_pto_commit").hide();
			$("#btn_importSpto").hide();
			$("#btn_flowDiagram").hide();
			$("#btn_auditInfo").show();
		}
		//表单赋值
		$("#ptoForm").iForm("setVal",ptoInfoVoData);
		var guardianVal = filterGuardian();
		$("#f_guardian").iCombo("setVal",guardianVal);
		//操作项赋值
		var operItemList = {};
		operItemList.safeDatas = ptoInfoVoData.ptoOperItemList;
		initSafeItemListByData("safeItemTest",operItemList);
		
		if (ptoAttachment.length > 0) {
			$("#uploadfileTitle").iFold("show");
			$("#uploadform").iForm("setVal", {uploadfield : ptoAttachment});
		}
		
		if(flag){  //在自己手上
			beginEditSafeItemList("safeItemTest");
		}else{  //不在自己手上
			$("#ptoForm").iForm("endEdit");
			$("#uploadform").iForm("endEdit");
			canSelectTree=false;
		}
	}else{
		initEmptySafeItemDatagrid("safeItemTest");
		$("#ptoForm").iForm("hide",["currStatus"]);
		$("#btn_pto_done").hide();
		$("#btn_pto_undo").hide();
		$("#btn_print").hide();
	}
	if(!isNeedFlow){  //不需要走流程时
		$("#btn_pto_save1").hide();
		$("#btn_flowDiagram").hide();
		$("#btn_auditInfo").hide();
		if(ptoInfoVoData.id != null){
			$("#btn_print").show();
		}
	}
}
/**
 * 提交标准操作票
 */
function commitPto(commitStyle) {
	/** 非表单验证内容的验证 */
	var assetId = $("#ptoForm").iForm("getVal").assetId;
	var assetName = $("#ptoForm").iForm("getVal").assetName;
	if (assetId == '' || assetName == "请从左边目录树选择关联设备") {
		FW.error("请从左边树选择设备目录");
		return;
	}
	var typeTxt = $("#f_type").iCombo("getTxt");
	if(typeTxt === ""){
		$("#f_type").iCombo("setVal","");
	}
	/** 表单验证 */
	if (!$("#ptoForm").valid()) {
		return;
	}
	// 表单
	var ptoFormObj = $("#ptoForm").iForm("getVal");
	var ptoFormData = FW.stringify(ptoFormObj);
	// 获取附件数据
	var ids = $("#uploadform").iForm("getVal")["uploadfield"];
	// 操作项表格
	var ptoItemData = null;

	var ptoItemDataObj = getSafeItemInputs("safeItemTest");
	if(ptoItemDataObj.valid == false){
		FW.error(ptoItemDataObj.invalidMsg);
		return false;
	}else{
		ptoItemData = ptoItemDataObj.safeItems;
	}
//	var ptoItemObj = safeItemFilter(ptoItemData);
//	if(!ptoItemObj.hasItemData){
//		return;
//	}
//	ptoItemData = ptoItemObj.safeDatas;
	var itemList = FW.stringify(ptoItemData);
	$.post(basePath + "ptw/ptoInfo/commitPtodata.do", {"ptoForm" : ptoFormData,
		"itemList" : itemList,"commitStyle" : commitStyle,"uploadIds" : ids}, function(data) {
		if (data.result == "success") {
			taskId = data.taskId;
			if (commitStyle == "save") {
				$("#ptoForm").iForm("setVal", {
					"id" : data.id,
					"code" : data.code,
					"currStatus" : data.currStatus
				});
			} else {
				$("#ptoForm").iForm("setVal", {
					"id" : data.id,
					"code" : data.code,
					"workflowId" : data.workflowId,
					"currStatus" : data.currStatus
				});
				if(data.isNeedFlow){  //需要启动流程时才弹出审批框
					audit("submit");
				}else{ //隐藏操作按钮
					$("#ptoForm").iForm("endEdit");
					endEditSafeItemList("safeItemTest");
					$("#uploadform").iForm("endEdit");
					$("#btn_pto_save1").hide();
					$("#btn_pto_commit").hide();
					$("#btn_importSpto").hide();
					$("#btn_flowDiagram").hide();
					$("#btn_auditInfo").hide();
					$("#btn_printDiv").show();
					$("#btn_print").show();
				}
			}
			FW.fixRoundButtons("#toolbar");
			FW.success("保存成功");
		} else {
			FW.error("保存失败");
		}
	}, "json");
}
/**
 * 审批标准操作票记录
 */
function audit(type) {
	// 表单校验
	if (!$("#ptoForm").valid()) {
		return;
	}
	var ptoFormData = $("#ptoForm").iForm("getVal");
	var id = ptoFormData.id;
	var variables = FW.stringify([]);
	var ptoItemData = getSafeItemInputs("safeItemTest").safeItems;
	// 获取附件数据
	var ids = $("#uploadform").iForm("getVal")["uploadfield"];
	
	var ptoData = {"ptoFormData":ptoFormData,"ptoItemData":ptoItemData,"uploadIds":ids};
	
	// 下面的修改关联设备必须要在审批框弹出之前执行完，所有这里要用同步
	workFlowAudit(ptoData, workflowId, variables, id, status,type);
	
}
/**
 * 删除
 */
function deletePto() {
	Notice.confirm("确定删除|确定删除该条标准操作票信息么？", function() {
		var ptoFormObj = $("#ptoForm").iForm("getVal");
		var id = ptoFormObj.id;
		$.post(basePath + "ptw/ptoInfo/deletePtoDraft.do", {"id" : id}, function(data) {
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
function obsoletePto() {
	Notice.confirm("确定作废|确定作废该条操作票信息么？", function() {
		var ptoFormObj = $("#ptoForm").iForm("getVal");
		var id = ptoFormObj.id;
		$.post(basePath + "ptw/ptoInfo/obsoletePtoInfo.do", {"id" : id }, function(data) {
			if (data.result == "success") {
				FW.success("作废成功");
				closeCurPage();
			} else {
				FW.error("作废失败");
			}
		}, "json");
	}, null, "info");
}

/**
 * 已执行
 */
function ptoOperDone(){
	Notice.confirm("确定已执行|确认该操作票已执行吗？", function() {
		var ptoFormObj = $("#ptoForm").iForm("getVal");
		var id = ptoFormObj.id;
		$.post(basePath + "ptw/ptoInfo/hasDonePtoInfo.do", {"id" : id }, function(data) {
			if (data.result == "success") {
				FW.success("确定成功");
				closeCurPage();
			} else {
				FW.error("确定失败");
			}
		}, "json");
	}, null, "info");
}
/**
 * 已作废
 */
function ptoOperUndo(){
	obsoletePto();
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
    businessData.fields = fields;
    businessData.data = data;
    
    var workFlow = new WorkFlow();
    
    workFlow.showAuditInfo(workflowId,JSON.stringify(businessData),auditInfoShowBtn,audit);
}

function importSpto(){
	 var src = basePath + "ptw/sptoInfo/sptoListDlgOth.do"; 
     var dlgOpts = {
         width : 750,
         height:500,
         closed : false,
         title:"双击选择标准操作票",
         modal:true
     };
     Notice.dialog(src,dlgOpts,null);
	
}

/**
 * @param sptoId 查询标准操作票的数据，并初始化新建页面
 */
function initDataFromSpto(sptoId){
	$.post(basePath + "ptw/ptoInfo/queryInitdataFromSpto.do", {"sptoId" : sptoId},
				function(result) {
					var ptoInfoJsonData = JSON.parse(result.data) ;
					//表单赋值
					$("#ptoForm").iForm("setVal",ptoInfoJsonData);
					//操作项赋值
					var operItemList = {};
					operItemList.safeDatas = ptoInfoJsonData.ptoOperItemList;
					$("#safeItemTest").empty();
					initSafeItemListByData("safeItemTest",operItemList);
					beginEditSafeItemList("safeItemTest");
				}, "json");
}

//过滤赋值监护人时 有些字段不在监护人角色列表里
function filterGuardian(){
	var ptoGuardian = roleUserMap[0].PTOGUARDIAN;
	var guardianObj = $("#f_guardian").iCombo("getData");
	var guardianName = $("#f_guardian").iCombo("getTxt");
	var guardianVal = "";
	for(var i=0;i<guardianObj.length;i++){
		if(guardianName.indexOf(guardianObj[i].title)>-1){
			guardianVal += guardianObj[i].value + ",";
		}
	}
	if(guardianVal.length>0){
		guardianVal = guardianVal.substring(0,guardianVal.length-1);
	}
	return guardianVal;
}