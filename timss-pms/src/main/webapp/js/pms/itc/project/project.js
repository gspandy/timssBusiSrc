/**
 * 项目立项基本的js引用页面
 */
var typeId = "ptype";// 项目类型在业务的字段
var typeEnum = "pms_project_type"; // 项目类型在枚举表中的字段
var prefixId = "#f_";// 前端控件前缀
var propertyId = "property"; // 项目性质在业务字段
var propertyEnum = "pms_project_property"; // 项目性质在枚举表字段
var projectAttachMap = []; // 项目立项附件信息
var projectCodeFilter = /^([A-Z0-9\-_])+$/;// 项目编号正则表达式
// 项目立项表单
var projectFormFields = [
		{
			id : "id",
			type : "hidden"
		},
		{
			title : "项目名称",
			id : "projectName",
			rules : {
				required : true,
				SynRemoteValid : {
					url : basePath + "pms/project/isProjectNameExisted.do",
					type : "post",
					data : {
						projectName : function() {
							return $("#f_projectName").val();
						},
						projectId : function() {
							if (window.id) {
						       return window.id;
					        }
					        return "";
						}
					}
				}
			},
			messages : {
				SynRemoteValid : "项目名称已存在"
			}
		},
		{
			title : "项目金额(元)",
			id : "applyBudget",
			rules : {
				required : true,
				number : true
			}
		},
		{
			title : "项目编号",
			id : "projectCode",
			rules : {
				required : true,
				regex : projectCodeFilter,
				SynRemoteValid : {
					url : basePath + "pms/project/isProjectCodeExisted.do",
					type : "post",
					data : {
						projectCode : function() {
							return $("#f_projectCode").val();
						},
						projectId : function() {
							if (window.id){
								return window.id;
							}
							return "";
						}
					}
				}
			},
			messages : {
				regex : "只能包含大写字母，数字和下划线，短横线",
				SynRemoteValid : "项目编号已存在"
			}
		},
		{
			id : "planId",
			type : "hidden"
		},
		{
			title : "所属年度计划",
			id : "planName",
			rules : {
				required : true
			}
		},
		
		
		{
			title : "年度",
			id : "pyear",
			type : "combobox",
			data : yearFields,
			value : getCurrentYear(),
			rules : {
				required : true
			}
		},
		{
			title : "项目类型",
			id : typeId,
			type : "combobox",
			dataType : "enum",
			enumCat : "pms_project_type",
			rules : {
				required : true
			}
		},
		{
			title : "项目性质",
			id : propertyId,
			type : "combobox",
			dataType : "enum",
			enumCat : "pms_project_property",
			rules : {
				required : true
			}
		},
		{
			title : "需求技术方案书",
			id : "isRs",
			type : "combobox",
			data : [ [ "true", "是" ], [ "false", "否" ] ],
			rules : {
				required : true
			}
		},

		{
			title : "计划开始日期",
			id : "startTime",
			type : "date",
			dataType : "date",
			rules : {
				required : true
			}
		}, {
			title : "计划结束日期",
			id : "endTime",
			type : "date",
			dataType : "date",
			rules : {
				required : true
			}
		}
		, 
		{
			title : "项目负责人",
			id : "projectLeader",
			rules : {
				required : true
			},
			type : "combobox",
			options : {
				url : basePath
						+ queryProjectLeaderUrl,
				remoteLoadOn : "init"
			}
		},
		{
			title : "商务负责人",
			id : "customManager",
			rules : {
				required : true
			},
			type : "combobox",
			options : {
				url : basePath
						+ queryBusinessLeaderUrl,
				remoteLoadOn : "init"
			}
		}, {
			title : "董事长审批",
			id : "needDSZ",
			type : "combobox",

			data : [ [ "Y", "是" ], [ "N", "否",true ] ]
		},{
			title : "项目描述",
			id : "command",
			type : "textarea",
			linebreak : true,
			wrapXsWidth : 12,
			wrapMdWidth : 8,
			height : 48
		}
];
var attachFormFields = [ {
	title : " ",
	id : "attach",
	linebreak : true,
	type : "fileupload",

	wrapXsWidth : 12,
	wrapMdWidth : 12,
	options : {
		"uploader" : basePath + "upload?method=uploadFile&jsessionid="
				+ session,
		"delFileUrl" : basePath + "upload?method=delFile&key=" + valKey,
		"downloadFileUrl" : basePath + "upload?method=downloadFile",
		"swf" : basePath + "js/uploadify.swf",
		"fileSizeLimit" : 10 * 1024,
		"initFiles" : projectAttachMap,
		"delFileAfterPost" : true,
		canUserDelete:function(attachment){
			if(!attachment.reserved){
				attachment.reserved=1;
			}
			//默认优先级为1，为最高级，数字越小优先级越高
			//只能删除比当前优先级优先度低的附件
			return attachment.reserved>=pmsPager.attachreserved;
		}
	}
} ];

/**
 * 初始化从远程数据源获取数据的控件
 */
function initRemoteField($form) {
	var $planInput = $('#f_planName');
	var planInit = {
		datasource : basePath + "pms/plan/queryPlanByKeyWord.do",
		clickEvent : function(id, name) {
			$planInput.val(name);
			$.post(basePath + 'pms/plan/queryPlanById.do', {
				id : id
			}, function(result) {
				result = result.data;
				var values = {
					'planId' : result["id"],
					'planName' : result['planName'],
					'pyear' : result['year'],
					'ptype' : result['type'],
					'property' : result['property'],
					'projectLeader' : result["projectLeader"],
					'customManager' : result['customManager'],
					'startTime' : result['startTime'],
					'endTime' : result['endTime'],
					'command' : result['command']
				};
				$form.iForm("setVal", values);
			});
		}
	};
	$planInput.iHint('init', planInit);
}
// 初始化附件表单
function initAttachForm(data, $form, $wrapper, readOnly) {
	var result = {
		data : data,
		$form : $form,
		$wrapper : $wrapper,
		attachMap : projectAttachMap,
		attachFormFields : attachFormFields,
		readOnly : readOnly
	};
	initAttachFormTemplate(result);
}
// 项目编码事件添加
function addProjectCodeEvent() {
}
function initMilestoneForSetUp(data){
	$('#milestoneWrapper').show();
	initMilestoneTable(data);
	initMilestoneTable.dataGrid.datagrid('hideColumn',["actualTime","expectedTime"]);
}
function addMilestone(){
	var dataGrid=initMilestoneTable.dataGrid;
	var row={};
	dataGrid.datagrid('appendRow',row);
	var rowindex=dataGrid.datagrid('getRowIndex',row);
	dataGrid.datagrid('beginEdit',rowindex);
	$('#b-add-milestone').html('继续添加里程碑');
}

function initMilestoneForRead(data){
	if(data){
		$('#milestoneWrapper').show();
		initMilestoneTable(data);
		initMilestoneTable.dataGrid.datagrid('hideColumn',["garbage-colunms"]);
		$('#b-add-milestone').hide();
		$('#b-save-milestone-model').hide();
		$('#b-load-milestone-model').hide();
	}
}


function initMilestoneChangeTable(data){
	if(!initMilestoneChangeTable.dataGrid){
		$('#milestoneChangeList').iFold();
		initMilestoneChangeTable.dataGrid=$("#milestoneChangeTable").datagrid({
			fitColumns:true,
			scrollbarSize:0,
			singleSelect:true,
			onDblClickRow : function(rowIndex, rowData) {
			},
			data:data,
			onClickCell:function(rowIndex, field, value){
				if(field=="garbage-colunms"){
					initMilestoneChangeTable.dataGrid.datagrid('deleteRow',rowIndex);
				}
			},
			columns : [ [
			{
				field : 'bidId',
				hidden : true
			}, {
				field : 'user',
				title : '人员',
				width : 80,
				fixed :true,
				align : 'left',
				editor:{ 
					type : 'text'
				}
			}, {
				field : 'date',
				title : '日期',
				width : 105,
				fixed :true,
				align : 'left',
				editor:{
					type:"text"
				}
			}, {
				field : 'content',
				title : '操作内容',
				width : 100,
				
				align : 'left',
				editor:{
					type:"text"
				}
			} ] ]
		});
	}
}

function initMilestoneTable(data){
	if(!initMilestoneTable.dataGrid){
		$('#milestoneList').iFold();
		initMilestoneTable.dataGrid=$("#milestoneTable").datagrid({
			fitColumns:true,
			scrollbarSize:0,
			singleSelect:true,
			onDblClickRow : function(rowIndex, rowData) {
			},
			data:data,
			onClickCell:function(rowIndex, field, value){
				if(field=="garbage-colunms"){
					initMilestoneTable.dataGrid.datagrid('deleteRow',rowIndex);
				}
			},
			columns : [ [
			{
				field : 'bidId',
				hidden : true
			}, {
				field : 'type',
				hidden : true
			}, {
				field : 'milestoneName',
				title : '名称',
				width : 300,
				fixed :true,
				align : 'left',
				editor:{
					type:"text",
					options:{
						rules:{
							required:true
						}
					}
				}
			}, {
				field : 'originTime',
				title : '原始计划日期',
				width : 105,
				align : 'left',
				fixed : true,
				editor:{ 
					type : 'datebox',
					options : {
						dataType:"date",
						minView:2,
						format:"yyyy-mm-dd",
						rules:{
    						required:true
    					}
					}
				},
				formatter: function(value,row,index){
 					//时间转date的string，还有long2date(value)方法
 					return FW.long2date(value);
 				}
			}, {
				field : 'expectedTime',
				title : '计划完成日期',
				width : 105,
				align : 'left',
				fixed : true,
				editor:{ 
					type : 'datebox',
					options : {
						dataType:"date",
						minView:2,
						format:"yyyy-mm-dd"
					}
				},
				formatter: function(value,row,index){
 					return FW.long2date(value);
 				}
			}, {
				field : 'actualTime',
				title : '实际完成日期',
				width : 105,
				align : 'left',
				fixed : true,
				editor:{ 
					type : 'datebox',
					options : {
						dataType:"date",
						minView:2,
						format:"yyyy-mm-dd"
					}
				},
				formatter: function(value,row,index){
 					return FW.long2date(value);
 				}
			}, {
				field : 'command',
				title : '备注',
				width : 85,
				align : 'left',
				editor:{
					type:"text"
				}
			},garbageColunms] ]
		});
	}
}

function getMilestoneData(){
	return getDatagridData(initMilestoneTable.dataGrid);
}

function addOutsourcing(){
	var dataGrid=initOutsourcingTable.dataGrid;
	var row={};
	dataGrid.datagrid('appendRow',row);
	var rowindex=dataGrid.datagrid('getRowIndex',row);
	dataGrid.datagrid('beginEdit',rowindex);
	$('#b-add-outsourcing').html('继续添加外购需求');
}

function addWorkload(){
	var dataGrid=initWorkloadTable.dataGrid;
	var row={};
	dataGrid.datagrid('appendRow',row);
	var rowindex=dataGrid.datagrid('getRowIndex',row);
	dataGrid.datagrid('beginEdit',rowindex);
	$('#b-add-workload').html('继续添加工作量记录');
}

function initWorkloadTable(data){
	if(!initWorkloadTable.dataGrid){
		$('#workloadList').iFold();
		initWorkloadTable.dataGrid=$("#workloadTable").datagrid({
			fitColumns:true,
			scrollbarSize:0,
			singleSelect:true,
			onDblClickRow : function(rowIndex, rowData) {
			},
			data:data,
			onClickCell:function(rowIndex, field, value){
				if(field=="garbage-colunms"){
					initWorkloadTable.dataGrid.datagrid('deleteRow',rowIndex);
				}
			},
			columns : [ [
			/* {field:'ck',checkbox:true}, */
			{
				field : 'workloadId',
				hidden : true
			}, {
				field : 'type',
				hidden : true
			}, {
				field : 'userLevel',
				title : '人员',
				width : 80,
				fixed :true,
				align : 'left',
				editor:{ 
					type : 'combobox',
					options : {
						data : FW.parseEnumData("PMS_WORKLOAD_TYPE",_enum),
						rules:{
							required:true
						}
					}
				},
				formatter:function(value){
					var data=FW.parseEnumData("PMS_WORKLOAD_TYPE",_enum);
					var r="";
					for(var i in data){
						if(data[i][0]==value){
							r=data[i][1];
							break;
						}
					}
					return r;
				}
			}, {
				field : 'workloadValue',
				title : '工作量(人/天)',
				width : 100,
				fixed :true,
				align : 'right',
				editor:{
					type:"text",
					options:{
						rules:{
							required:true,
							number:true
						}
					}
				}
				
			}, {
				field : 'command',
				title : '备注',
				width : 100,
				
				align : 'left',
				editor:{
					type:"text"
				}
			}, garbageColunms ] ]
		});
	}
}

function initOutsourcingTable(data){
	if(!initOutsourcingTable.dataGrid){
		$('#outsourcingList').iFold();
		initOutsourcingTable.dataGrid=$("#outsourcingTable").datagrid({
			fitColumns:true,
			scrollbarSize:0,
			singleSelect:true,
			onDblClickRow : function(rowIndex, rowData) {
			},
			data:data,
			onClickCell:function(rowIndex, field, value){
				if(field=="garbage-colunms"){
					initOutsourcingTable.dataGrid.datagrid('deleteRow',rowIndex);
				}
			},
			columns : [ [
			{
				field : 'bidId',
				hidden : true
			}, {
				field : 'outsourcingName',
				title : '名称',
				width : 300,
				fixed :true,
				align : 'left',
				editor:{
					type:"text",
					options:{
						rules:{
							required:true
						}
					}
				}
				
			}, {
				field : 'outsourcingType',
				title : '型号',
				width : 150,
				fixed :true,
				align : 'left',
				editor:{
					type:"text"
				}
			}, {
				field : 'num',
				title : '数量',
				width : 100,
				fixed :true,
				align : 'right',
				editor:{
					type:"text",
					options:{
						rules:{
							number:true
						}
					}
				}
			}, {
				field : 'price',
				title : '估价(元)',
				width : 105,
				fixed :true,
				align : 'right',
				editor:{
					type:"text",
					options:{
						rules:{
							number:true
						}
					}
				}
			}, {
				field : 'command',
				title : '备注',
				width : 100,
				align : 'left',
				editor:{
					type:"text"
				}
			}, garbageColunms] ]
		});
	}
}

function hideDMZFormField(form){
	form.iForm("hide",["needDSZ"]);
}