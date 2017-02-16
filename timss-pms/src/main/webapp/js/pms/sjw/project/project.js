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

var companyList=[
                 ["hq","后勤服务分公司"],
	            ["jd","机电分公司"],
	            ["lw","劳务分公司"],
	            ["wy","物业分公司"],
	            ["zh","综合利用分公司"],
	            ["cw","财务分公司"],
	            ["zhf","综合分公司"]
              ]; //沙C多经立项分公司
// 项目立项表单
var projectFormFields = [
		{
			id : "id",
			type : "hidden"
		},
		{
			id : "bidCompId",
			type : "hidden"
		},
		{
			id : "planId",
			type : "hidden"
		},
		{
			id : "projectLeader",
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
			title : "项目类型",
			id : typeId,
			type : "combobox",
			dataType : "enum",
			enumCat : "pms_project_type",
			options : {
				allowEmpty : true
			},
			rules : {
				required : true
			}
			
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
							if (window.id)
								return window.id;
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
			title : "分公司",
			id : "companyId",
			rules : {
				required : true
			},
			type : "combobox",
			data:companyList,
			options: {
				allowEmpty : true,
				initOnChange:isNew,
				'onChange' : function(val){
					console.log("the change action has been triggered");
					if(isNew||!isNew&&hasInitVal){
						$.ajax({
							url : basePath + "pms/project/generateNewProjectCode.do",
					    	type:"POST",
					    	data:{companyId:val},
					    	success:function(data){
					    		$("#f_projectCode").val(data.newCode);
					    		console.log("has found something changed");
					    	},
					    	error:function(){
					    		FW.error("无法生成 项目编号");
					    	}
						});
					}else{
						hasInitVal = true;
					}
				}  
			}
		},
		{
			title : "项目负责人",
			id : "projectLeaderName",
			rules : {
				required : true
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
			render : function(id){
				$("#"+id).on("change",function(){
					//SynRemoteValid是个同步验证，而且没有留有自定义回调函数的入口了。这里用onchange+settimeout(function(){},0)加以解决
					setTimeout(function(){
						if(undefined!=$("label[for='f_projectCode']").attr("title")){
							$.ajax({
								url : basePath + "pms/project/generateNewProjectCode.do",
						    	type:"POST",
						    	data:{companyId:$("#f_companyId").iCombo("getVal")},
						    	success:function(data){
						    		FW.error("项目编号已经存在，建议使用编号:"+data.newCode);
						    	},
						    	error:function(){
						    		FW.error("无法生成 项目编号");
						    	}
							});
						};	
					},0);
				});	
			},
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
							if (window.id)
								return window.id;
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
			title : "发包意见",
			id : "contractAward",
			type : "combobox",
			dataType : "enum",
			enumCat : "PMS_CONTRACT_AWARD",
			rules : {
				required : true
			},
			options:{
				allowEmpty:true,
				'onChange' : function(val){
					refreshBidCompName("f_contractAward");
				}
			}
		},{
			title : "邀请/询价候选单位",
			id : "bidCompName",
			wrapXsWidth : 12,
			wrapMdWidth : 8,
			linebreak : true,
			rules:{
	    		required:true
	    	}
		},{
			title : "邀请/询价候选单位",
			id : "bidCompNameNull"
		},{
			title : "承包方式",
			id : "contractingMode",
			type : "combobox",
			linebreak : true,
			dataType : "enum",
			enumCat : "PMS_CONTRACTING_MODE",
			rules : {
				required : true
			},
			options:{
				allowEmpty:true
			}
		},
		
		{
			title : "工程专责",
			id : "businessLeader",
			rules : {
				required : true
			},
			type : "combobox",
			options : {
				url : basePath
						+ "pms/group/queryUserListByGroupId.do?groupId=SJW_GCHY",
				remoteLoadOn : "init"
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
		},
		{
			title : "流水号",
			id : "flowNo"
		},
		{
			title : "项目描述",
			id : "projectExplain",
			type : "textarea",
			linebreak : true,
			wrapXsWidth : 12,
			wrapMdWidth : 8,
			height : 48,
			rules:{
				maxChLength:1332
			},
			messages:{
				maxChLength:"项目描述不可超过666个汉字"
			}
		}, {
			title : "备注",
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
		"delFileAfterPost" : true
	}
} ];

/**
 * 初始化从远程数据源获取数据的控件
 */
function initRemoteField($form) {
	var $planInput = $('#f_planName');
	var $projLeaderInput = $('#f_projectLeaderName');
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
					'property' : result['property'],
					'projectLeaderName' : result["projectLeader"],
					'businessLeader' : result['customManager'],
					'startTime' : result['startTime'],
					'endTime' : result['endTime'],
					'command' : result['command']
				};
				$form.iForm("setVal", values);
			});
		}
	};
	var projLeaderInit = {
			datasource : basePath + "pms/project/queryUserListByGroupId.do?groupId=SJW_XMFZR",
			clickEvent : function(id, name) {
				$projLeaderInput.val(name);
				$.post(basePath + 'pms/project/queryUserById.do', {
					"userId" : id
				}, function(result) {
					var values = {
						'projectLeader' : result['userId'],
						'projectLeaderName' : result['userName']
					};
					$form.iForm("setVal", values);
				});
			}
		};
	$planInput.iHint('init', planInit);
	$projLeaderInput.iHint('init', projLeaderInit);
	setUserInfoToProjectLeader($("#form1"),$("#form1").iForm('getVal','projectLeader'));
}
/**
 * 初始化项目负责人
 */
function initRemotePLField($form) {
	var $projLeaderInput = $('#f_projectLeaderName');
	var projLeaderInit = {
			datasource : basePath + "pms/project/queryUserListByGroupId.do?groupId=SJW_XMFZR",
			clickEvent : function(id, name) {
				$projLeaderInput.val(name);
				$.post(basePath + 'pms/project/queryUserById.do', {
					"userId" : id
				}, function(result) {
					var values = {
						'projectLeader' : result['userId'],
						'projectLeaderName' : result['userName']
					};
					$form.iForm("setVal", values);
				});
			}
		};
	$projLeaderInput.iHint('init', projLeaderInit);
	setUserInfoToProjectLeader($("#form1"),$("#form1").iForm('getVal','projectLeader'));
}

function setUserInfoToProjectLeader($form,id){
	if(""!=id){
		$.post(basePath + 'pms/project/queryUserById.do', {
			"userId" : id
		}, function(result) {
			var values = {
				'projectLeader' : result['userId'],
				'projectLeaderName' : result['userName']
			};
			$form.iForm("setVal", values);
		});
	}
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
			/* {field:'ck',checkbox:true}, */
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
			/* {field:'ck',checkbox:true}, */
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
						format:"yyyy-mm-dd",
						rules:{
    						
    						
    					}
					}
				},
				formatter: function(value,row,index){
 					//时间转date的string，还有long2date(value)方法
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
 					//时间转date的string，还有long2date(value)方法
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
						
						//data:FW.parseEnumData("PMS_PAYPLAN_STAGE")
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
			/* {field:'ck',checkbox:true}, */
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
				
			}
			,
			
			{
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

//招标字段初始化，使其能够远程读取招标信息
function initRemoteSupplierData(form){
	var $supplierIdInput = $('#f_bidCompId');
	//后面是用iCombo多选
	$.ajax({
		url : basePath + "pms/supplier/queryFuzzyByName.do",
    	type:"GET",
    	success:function(data){
    		var bidCompArray = [];
    		for(var ind = 0 ; ind<data.length ; ind++){
    			bidCompArray.push([data[ind]["id"],data[ind]["name"]]);
    		}
    	},
    	error:function(){
    		FW.error("获取邀请/询价候选单位失败");
    	}
	});
}

function hideBidCompAndCompany(form){
	form.iForm('hide',['businessLeader','bidCompNameNull','bidCompName','contractAward']);
}

//发布意见变化的时候执行的更新方法
function refreshBidCompName(id){
	var contractAward =  $("#"+id).iCombo("getVal");
	//根据节点设定 判断是否隐藏招标信息
	var data = pmsPager.opt.data;
	var hideBidCompName = false;
	if(data.pri&&data.pri.workflow){
		var formHide = data.pri.workflow.formHide;		
		if(formHide){
			var hideArray = formHide.split(",") ;
			if(hideArray.indexOf("bidCompName")>-1){
				hideBidCompName = true;
			}
		}
		if(!hideBidCompName&&data.pri.workflow.elements&&data.pri.workflow.elements.modifiable){
			formHide = JSON.parse(data.pri.workflow.elements.modifiable)["formHide"];
			if(formHide){
				var hideArray = formHide.split(",") ;
				if(hideArray.indexOf("bidCompName")>-1){
					hideBidCompName = true;
				}
			}
		}
	}
	if(!hideBidCompName){
		if("zb"==contractAward||"gkxj"==contractAward){
			$("#form1").iForm("hide","bidCompName");
			$("#form1").iForm("show","bidCompNameNull");
			$("#form1").iForm("endEdit","bidCompNameNull");
			$("#form1").iForm("setVal",{"bidCompNameNull":"无","bidCompId":"","bidCompName":""});
			$("#form1").iForm("setVal",{});
		}else{
			$("#form1").iForm("show","bidCompName");
			$("#form1").iForm("hide","bidCompNameNull");
			$("#form1").iForm("setVal",{"bidCompNameNull":""});
		}
	}	
}

function createFlowNo(){
	$.ajax({
		url : basePath + "pms/project/generateNewFlowNo.do",
    	type:"POST",
    	success:function(data){
    		$("#f_flowNo").val(data.newFlowNo);
    		console.log("to createFlowNo");
    	},
    	error:function(){
    		FW.error("无法生成流水号");
    	}
	});
}
