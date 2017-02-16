/* 表单字段定义  */
var fields = [
		{
			title : "ID",
			id : "id",
			type : "hidden"
		},
		{
			title : "作业方案ID",
			id : "jobPlanId",
			type : "hidden"
		},
		{
			title : "名称",
			id : "description",
			rules : {
				required : true,
				maxChLength : 680
			}
		},
		{
			title : "服务目录Id",
			id : "faultTypeId",
			type : "hidden"
		},
		{
			title : "服务目录",
			id : "faultTypeName",
			type : "label",
			value : "请从左边服务目录树选择",
			rules : {
				required : true
			}
		},
		{
			title : "维护周期(天)",
			id : "maintainPlanCycle",
			rules : {
				required : true,
				digits : true,
				min : 1
			}
		},
		{
			title : "周期开始时间",
			id : "currStartTime",
			type : "datetime",
			dataType : "datetime",
			rules : {
				required : true
			}
		},
		{
			title : "预警期(小时)",
			id : "alertTime",
			rules : {
				required : true,
				number : true
			}
		},
		{
			title : "负责组名",
			id : "workTeamName",
			type : "hidden"
		},
		{
			title : "负责组",
			id : "workTeam",
			type : "combobox",
			rules : {
				required : true
			},
			options : {
				url : basePath
						+ "itsm/woUtil/userGroupFilter.do?filterStr=itc_itsm_wt",
				multiselect : true,
				remoteLoadOn : "init",
				onChange : function(val) {
					var workteamName = $("#f_workTeam").iCombo("getTxt");
					$("#maintainPlanForm").iForm("setVal", {
						"workTeamName" : workteamName
					});

				}
			}
		}, {
			title : "自动生成工单",
			id : "isAutoGenerWo",
			type : "radio",
			rules : {
				required : true
			},
			data : [ [ '0', '否', true ], [ '1', '是' ] ]
		}, {
			title : "备注",
			id : "remarks",
			type : "textarea",
			linebreak : true,
			wrapXsWidth : 12,
			wrapMdWidth : 8,
			height : 110,
			rules : {
				maxChLength : 680
			}
		}

];

var toolDelteId = 1;
var toolGridField = [ [
		{
			field : 'toolDelteId',
			title : '临时ID用来指定删除row',
			hidden : true,
			formatter : function(value, row) {
				row.toolDelteId = toolDelteId;
				return toolDelteId++;
			}
		},
		{
			field : 'id',
			title : 'ID',
			hidden : true
		},
		{
			field : 'itemsId',
			title : '物资ID',
			hidden : true
		},
		{
			field : 'itemsCode',
			title : '物资编码',
			width : 80,
			fixed : true
		},
		{
			field : 'itemsName',
			title : '名称',
			width : 200
		},
		{
			field : 'itemsModel',
			title : '规格型号',
			width : 200,
			fixed : true
		},
		{
			field : 'applyCount',
			title : '使用数量',
			edit : true,
			width : 60,
			fixed : true,
			editor : {
				type : "text",
				"rules" : {
					required : true,
					"digits" : true
				},
				"options" : {
					align : "right"
				}
			}
		},
		{
			field : 'unit',
			title : '单位',
			width : 60,
			fixed : true,
			align : 'center'
		},
		//TIM-1319要求鼠标移到垃圾桶箭头变成手指  增加style="cursor:pointer"
		//addBy yangk 2016-10-27 
		{
			field : 'oper',
			title : '',
			width : 55,
			fixed : true,
			align : 'center',
			formatter : function(value, row) {
				return '<img src="'
						+ basePath
						+ 'img/workorder/btn_garbage.gif" onclick="deleteGridRow('
						+ "'toolTable'," + row.toolDelteId
						+ ')" width="16" height="16" style="cursor:pointer">';
			}
		} ] ];

function initMTPPage(mtpFullData) {
	$("#title_tool").iFold("init");

	/* form表单初始化 */
	$("#maintainPlanForm").iForm("init", {
		"fields" : fields,
		"options" : {
			validate : true,
			initAsReadonly : formRead
		}
	});

	if (faultTypeId) {
		$("#maintainPlanForm").iForm("setVal", {
			"faultTypeId" : faultTypeId,
			"faultTypeName" : faultTypeName
		});
	}

	$("#toolTable").datagrid({
		columns : toolGridField,
		idField : 'toolDelteId',
		singleSelect : true,
		fitColumns : true,
		scrollbarSize : 0
	});

	if (mtpFullData != 0) { // jpFullData==0时表示新建作业方案

		var mtpFormData = JSON.parse( mtpFullData.maintainPlanForm );
		mtpType = mtpFormData.maintainPlanFrom;// "cycle_maintainPlan"：周期性维护计划
		var attachmentData = mtpFullData.attachmentMap;

		if (mtpFormData.maintainPlanCycle == 0) {
			mtpFormData.maintainPlanCycle = null;
		}
		if (mtpFormData.alertTime == 0) {
			mtpFormData.alertTime = null;
		}

		$("#maintainPlanForm").iForm("setVal", mtpFormData);
		$("#maintainPlanForm").iForm("setVal", {
			"principalName" : mtpFormData.principalName,
			"selectPrincipal" : mtpFormData.principalName
		});
		$("#maintainPlanForm").iForm("hide", [ 'selectPrincipal' ]);
		if (attachmentData.length > 0) {
			$("#uploadfileTitle").iFold("show");
			$("#uploadform").iForm("setVal", {
				uploadfield : attachmentData
			});
		} else {
			$("#uploadfileTitle").iFold("hide");
		}
		// 按钮控制
		var principalId = mtpFormData.principal;
		if (mtpFormData.maintainPlanFrom == "cycle_maintainPlan") {
			$("#btn_mtp_toWoDiv").hide();
		} else { // 如果是遗留问题和不立即处理问题，则没有编辑功能，只能生成工单时修改
			$("#btn_mtp_editDiv").hide();
		}
		if (principalId!= null && principalId.length != 0 && principalId.indexOf(loginUser)) { 
			// 如果登录用户是负责人之一，则可以手动生成工单
			$("#btn_mtp_toWoDiv").show();
		} else {
			$("#btn_mtp_toWoDiv").hide();
		}

		$("#btn_mtp_saveDiv").hide();
		if (mtpType != "cycle_maintainPlan") {
			$("#btn_mtp_unavailableDiv").hide();
		}

		// 工具
		var toolDataStr = mtpFullData.toolData;
		if (toolDataStr.length != 0) {
			$("#toolTable").datagrid("loadData", toolDataStr);
			$("#toolTable").datagrid("hideColumn", "oper"); // 隐藏某一列
			$("#toolBtnDiv").hide();
		} else {
			$("#title_tool").iFold("hide");
		}

	} else { // 新建维护计划
		$("#maintainPlanForm").iForm("hide", [ 'principalName' ]);
		$("#btn_mtp_editDiv").hide();
		$("#btn_mtp_unavailableDiv").hide();
		$("#btn_mtp_toWoDiv").hide();
	}

	FW.fixRoundButtons("#toolbar");
}

function beginEdit(id) {
	var rows = $("#" + id).datagrid('getRows');
	for ( var i = 0; i < rows.length; i++) {
		$("#" + id).datagrid('beginEdit', i);
	}
}
function endEdit(id) {
	var rows = $("#" + id).datagrid('getRows');
	for ( var i = 0; i < rows.length; i++) {
		$("#" + id).datagrid('endEdit', i);
	}
}

/* 提交维护计划（数据入库，关闭页面，刷新计划列表） */
function commitMTP() {
	var tempfaultTypeName = $("#maintainPlanForm").iForm("getVal",
			"faultTypeName");
	if (tempfaultTypeName == '' || tempfaultTypeName == "请从左边服务目录树选择") {
		FW.error("请从左边选择服务目录");
		return;
	}
	/** 表单验证 */
	if (!$("#maintainPlanForm").valid()) {
		return;
	}
	/** datagrid验证 */
	if (!$("#toolTable").iValidDatagrid()) {
		return;
	}

	endEdit("toolTable");
	var mtpFormData = $("#maintainPlanForm").iForm("getVal"); // 取表单值

	var currStartTime = mtpFormData.currStartTime; // 第一次周期开始时间（毫秒）
	var woCyc = mtpFormData.maintainPlanCycle * 24 * 60 * 60 * 1000; // 周期时长（毫秒）
	var alertLength = mtpFormData.alertTime * 60 * 60 * 1000; // 预警时长（毫秒）
	mtpFormData.newToDoTime = currStartTime + woCyc - alertLength; // 下次生成待办时间

	var toolData = $("#toolTable").datagrid("getData");
	$("#toolTable").datagrid("clearChecked");

	// 获取附件数据
	var ids = $("#uploadform").iForm("getVal")["uploadfield"];

	var maintainPlanId = mtpFormData.id;
	// 如果是新建作业方案的保存，则暂时设置jobPlanId = 0;
	if (maintainPlanId == "") { 
		maintainPlanId = 0;
	}

	$.post(basePath + "itsm/maintainPlan/commitMaintainPlandata.do", {
		"maintainPlanForm" : JSON.stringify(mtpFormData),
		"toolData" : JSON.stringify(toolData),
		"maintainPlanId" : maintainPlanId,
		"uploadIds" : ids
	}, function(data) {
		if (data.result == "success") {
			$("#toolBtnDiv").hide();
			$("#btn_mtp_saveDiv").hide();
			$("#toolTable").datagrid("hideColumn", "oper"); // 隐藏某一列
			FW.success("保存成功");
			closeCurPage();
		} else {
			FW.error("保存失败");
		}
	}, "json");

	$("#maintainPlanForm").iForm("endEdit"); // 关闭编辑，
}

/**
 * 关闭当前tab 页
 */
function closeCurPage(flag) {
	if (flag) {
		FW.set("MTPlistDoNotRefresh", true);
	}
	FW.deleteTabById(FW.getCurrentTabId());
}
/**
 * 删除dataGrid中的某一行数据
 * 
 * @param dataGridId
 * @param index
 */
function deleteGridRow(dataGridId, deleteId) {
	$('#' + dataGridId).datagrid('deleteRow',
			$('#' + dataGridId).datagrid('getRowIndex', deleteId));
}
/* 编辑维护计划 （按钮） */
function editMTP() {
	formRead = false; // 编辑模式
	$("#maintainPlanForm").iForm("beginEdit"); // 打开编辑
	$("#maintainPlanForm").iForm("hide", [ 'principalName' ]);
	$("#maintainPlanForm").iForm("show", [ 'selectPrincipal' ]);

	$("#title_tool").iFold("show");

	// 判断按钮该显示的文字
	var toolData = $("#toolTable").datagrid("getData");
	if (toolData.total != 0) {
		$("#btn_toolTable").html("继续添加工具");
	}
	beginEdit("toolTable");
	$("#toolTable").datagrid("showColumn", "oper"); // 隐藏某一列
	$("#toolBtnDiv").show();

	$("#btn_mtp_saveDiv").show();
	$("#btn_mtp_editDiv").hide();
	$("#btn_mtp_toWoDiv").hide();
	$("#btn_mtp_unavailableDiv").hide();
}

function appendTool() {
	// 调用库存物资树方法
	FW.showInventoryDialog({
		onParseData : function(data) {
			var size = data.length;
			for ( var i = 0; i < size; i++) {
				var row = {};
				row["itemsCode"] = data[i]["itemcode"];
				row["itemsName"] = data[i]["itemname"];
				row["itemsId"] = data[i]["itemid"];
				row["itemsModel"] = data[i]["cusmodel"];
				row["unit"] = data[i]["unit1"];
				row["bin"] = data[i]["bin"];
				row["warehouse"] = data[i]["warehouse"];
				row["applyCount"] = 1;
				$("#toolTable").datagrid("appendRow", row);
			}
			beginEdit("toolTable");
			$("#btn_toolTable").html("继续添加工具");
		}
	});
}
/**
 * 禁用标准作业方案
 */
function unavailableMTP() {
	Notice.confirm("确定禁用|确定禁用该维护计划么？", function() {
		var mtpFormData = $("#maintainPlanForm").iForm("getVal"); // 取表单值
		var maintainPlanId = mtpFormData.id;

		$.post(basePath + "itsm/maintainPlan/unavailableMTP.do", {
			"maintainPlanId" : maintainPlanId
		}, function(data) {
			if (data.result == "success") {
				$("#btn_mtp_unavailableDiv").hide();
				$("#btn_mtp_editDiv").hide();
				$("#btn_mtp_toWoDiv").hide();
				FW.success("禁用成功");
				closeCurPage();
			} else {
				FW.error("禁用失败");
			}
		}, "json");
	}, null, "info");
}

function mtpToWoBtn() {
	if (mtpType == "cycle_maintainPlan") { // 周期性维护计划直接生成工单（流程直接走到工作策划那一步）
		Notice.confirm("确定生成工单|确定生成工单么？该操作无法撤销。", function() {
			$.post(basePath + "itsm/workorder/cycMtpToWo.do", {
				"mtpId" : mtpId,
				"todoId" : todoId
			}, function(data) {
				if (data.result == "success") {
					FW.success("新建成功");
					closeCurPage();
				} else if (data.result == "notyours") {
					FW.error("您不是负责组成员");
					closeCurPage();
				} else {
					FW.error("新建失败");
				}
			}, "json");
		}, null, "info");
	} else {
		// 新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
		var rand = new Date().getTime()
				+ Math.floor(Math.abs(Math.random() * 100));
		var opts = {
			id : "newWO" + rand,
			name : "新建工单",
			url : basePath + "itsm/workorder/openNewWOPage.do?mtpId=" + mtpId
					+ "&mtpType=" + mtpType,
			tabOpt : {
				closeable : true,
				afterClose : "FW.deleteTab('$arg'); FW.activeTabById('itsm_root');FW.getFrame('itsm_root').refreshAfterClose();"
			}
		};
		_parent()._ITC.addTabWithTree(opts);
	}

}
