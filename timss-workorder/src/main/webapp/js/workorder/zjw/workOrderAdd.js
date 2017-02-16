//工单提交阶段字段
var fields = [
	{title : "ID", id : "id", type : "hidden"},
	{title : "工单编号", id : "workOrderCode", type : "hidden"},
	{title : "流程ID", id : "workflowId", type : "hidden"},
	{
		title : "风电场",
		id : "woWindStation",
		type:"combobox",
		rules : {required:true},
    	dataType:"enum",
    	linebreak:true,
	    enumCat:"WO_WIND_STATION",
	    options:{
	    	allowEmpty : true
	    }
	},	
	{title : "缺陷名称", id : "description", rules : {maxlength:100,required:true}},
	{
		title : "优先级",
		id : "priorityId",
		type:"combobox",
		rules : {required:true}
	},	
	{
		title : "专业",
		id : "woSpecCode",
		//value: "MACHINE",
		type : "combobox",
		rules : {required:true},
    	dataType:"enum",
	    enumCat:"WO_SPEC",
	    linebreak:true
	},	
	{title : "报障设备", id : "equipName", type:"label", value:"请从左边设备树选择",rules : {required:true},
		formatter:function(val){
			var text = val;
			if(text=="请从左边设备树选择"){
				text = "<label style='color:red'>"+val+"</label>";
			}
			return text;
		}
	},
	{title : "设备资产ID", id : "equipId",type : "hidden"},
	{title : "设备名称编码", id : "equipNameCode",type : "hidden"},	
	{
		title : "处理方式",
		id : "woCommitHandleStyle",
		//value: "REPAIR_NOW",
		type : "combobox",
		rules : {required:true},
    	dataType:"enum",
	    enumCat:"WO_COMMIT_HANDLE_STYLE"
	},		
	{title : "风机编号", id : "equipSiteCode",dataType:"number",rules : {required:true},linebreak:true},
	{
		title : "故障分类",
		id : "faultTypeId",
		//value: "PADDLE",
		//type : "combobox",
		rules : {maxlength:10,required:true},
		messages:{maxlength:"不能超过10个汉字"}
    	//dataType:"enum",
	    //enumCat:"WO_FAULT_TYPE"
	},	
	{title : "发现时间", id : "discoverTime", type:"datetime", dataType:"datetime",rules : {required:true},value:new Date()},
	{title : "故障风速(m/s)", id : "currWindSpeed", linebreak:true,dataType:"number"},
	{
		title : "紧急程度",
		id : "urgentDegreeCode",
		type : "combobox",
		//value:"NORMAL",
    	dataType:"enum",
	    enumCat:"WO_URGENCY_DEGREE"
	},	
	{
		title : "是否走工作票",
		id : "isToWorkTicket",
		type : "combobox",
		rules : {required:true},
    	dataType:"enum",
	    enumCat:"WO_ISTOPTW",
	    options:{
	    	allowEmpty : true,
	    	onChange:function(val,obj){}
	    }
	},	
	{title : "关联工单ID", id : "parentWOId",type:"hidden"},
	{title : "关联工单", id : "parentWOCode",
		render:function(id){
			var ipt = $("#" + id);
			ipt.removeClass("form-control").attr("icon","itcui_btn_mag");
			ipt.ITCUI_Input();
			ipt.next(".itcui_input_icon").on("click",function(){
				var src = basePath + "workorder/workorder/parentWOList.do";
				var dlgOpts = {
					width : 620,
					height: 500,
					closed : false,
					title:"双击选择父工单",
					modal:true
				};
				Notice.dialog(src,dlgOpts,null);
			});
		}
	},	
	{title : "关联领料单", id : "relMatApplyIds", type:"label",linebreak:true,
		formatter:function(val){
			if(val==null || val==""){
				return;
			}
			var words = val.split(';');
			var text = "";
			for ( var i = 0; i < words.length; i++) {
				var temp = words[i];
				if(temp!=null && temp!=""){
					var no = temp.substring(0,temp.indexOf(","));
					var id = temp.substring(temp.indexOf(",")+1);
					text += "<a onclick=openMatApplyPage('"+ id + "')>" + no + "</a>,";
				}
			}
			if(text.length!=0){
				text = text.substring(0, text.length-1);
			}
			return text;
		}
	},
	{title : "关联工作票", id : "relPtwIds", type:"label",breakAll:"true",
		formatter:function(val){
			if(val==null || val==""){
				return;
			}
			var words = val.split(';');
			var text = "";
			for ( var i = 0; i < words.length; i++) {
				var temp = words[i];
				if(temp!=null && temp!=""){
					var no = temp.substring(0,temp.indexOf(","));
					var id = temp.substring(temp.indexOf(",")+1);
					text += "<a onclick=openPtwPage("+ id + ")>" + no + "</a>,";
				}
			}
			if(text.length!=0){
				text = text.substring(0, text.length-1);
			}
			return text;
		}
	},
	{title : "工作负责人", id : "endReportUserName",linebreak:true},
	{title : "班组成员", id : "woMaintainExecutorName",breakAll:"true",rules : {maxlength:30}, messages:{maxlength:"不能超过30个字"}},
	{
		title : "状态", 
		id : "currStatus", 
		type:"label",
		linebreak:true,
		formatter:function(val){
			return FW.getEnumMap("WO_STATUS")[val];
		}
	},	
	{title : "报障人", id : "createUserName",type:"label"},
	{title : "报障时间", id : "createdate", type:"label",
		formatter: function(value,row,index){
			return FW.long2time(value);
		}
	},
	{
		title : "备注",
		//value:"备注信息",
		id : "remarks",
		type : "textarea",
		linebreak:true,
		rules : {maxlength:500},
		wrapXsWidth:12,
		wrapMdWidth:12,
		height:55
	},
    {title : "班长处理方式", id : "monitorHandleStyle", type : "hidden"},
    {title : "场长(助理)处理方式", id : "chairmanHandleStyle", type : "hidden"},
	{title : "流程实例ID", id : "workflowId",type : "hidden"}
];

//物资领用
var toolDelteId = 1;
var	toolGridField = [[
		{field : 'toolDelteId',title : '临时ID用来指定删除row', hidden:true,
			formatter:function(value,row,index){
				row.toolDelteId =toolDelteId;
				return toolDelteId++;
			}
		},  
  	    {field : 'id',title : 'ID', hidden:true},
  	    {field : 'itemsId',title : '物资ID',hidden:true},
		{field : 'itemsCode',title : '物资编码',width:100,fixed:true},
		{field : 'itemsName',title : '名称', width:200},
		{field : 'itemsModel',title : '规格型号',width:100},
		{field : 'applyCount',title : '申请数量',edit:true,width:80,fixed:true,
			editor:{type:"text","options":{"rules" : {required:true,"number" : true},align:"right"}}
		},
		{field : 'warehouseid',hidden:true},
		{field : 'warehouse',title : '仓库',width:100,fixed:true,align:'left'},		
		{field : 'cateId',title : '物资类型ID',hidden:true},
		{field : 'cateName',title : '物资类型',width:100,fixed:true},
		{field : 'getCount',title : '领取数量',edit:true,width:80,fixed:true},
		{field : 'unit',title : '单位',width:80,fixed:true,align:'left'},
		{field : 'oper',title : '',width:40,fixed:true,align:'center', 
			formatter:function(value,row,index){
				return '<img src="'+basePath+'img/workorder/btn_garbage.gif" onclick="deleteGridRow('+
				     	"'toolTable',"+row.toolDelteId+')" width="16" height="16" >';
	   		}
	   	}
]];
function appendTool(){
	$("#toolTable").datagrid("resize");
	//调用库存物资树方法
	FW.showInventoryDialog({
		onParseData : function(data){
			var size = data.length;
			for(var i=0;i<size;i++){
				var row = {};
				row["itemsCode"] = data[i]["itemcode"];
				row["itemsName"] = data[i]["itemname"];
				row["itemsId"] = data[i]["itemid"];
				row["itemsModel"] = data[i]["cusmodel"];
				row["unit"] = data[i]["unitname"];
				row["bin"] = data[i]["bin"];
				row["warehouseid"] = data[i]["warehouseid"];
				row["warehouse"] = data[i]["warehouse"];				
				row["cateId"] = data[i]["cateId"];
				row["cateName"] = data[i]["cateType"];
				row["applyCount"] = 1 ;  //默认添加一个
				$("#toolTable").datagrid("appendRow",row );
			}
			beginEdit("toolTable");
		}
	});
	$("#btn_toolTable").html("继续添加物料");
} 
function deleteGridRow(dataGridId,deleteId){
	 $('#'+dataGridId).datagrid('deleteRow', $('#'+dataGridId).datagrid('getRowIndex',deleteId));
	 var rowsLength = $("#"+dataGridId).datagrid('getRows').length;
	 if(rowsLength == 0){
		 var oldBtnName =  $("#btn_"+dataGridId).html().substring(2);
		 $("#btn_"+dataGridId).html(oldBtnName);
	 }
}
function beginEdit(id){
	var rows = $("#"+id).datagrid('getRows');
	for(var i=0;i<rows.length;i++){
		$("#"+id).datagrid('beginEdit',i);
	}
}

//回填阶段字段
var reportFields = [
    {
    	title : "处理结果",
    	id : "reportHandleStyle",
    	type : "combobox",
    	rules : {required:true},
    	value: "DONE",
    	dataType:"enum",
	    data : [
	            ["DONE","处理完成"],
	            ["SUSPEND","挂起"]
	    ],
	    options :{
    	    onChange:function(val){
	    		if(val=="SUSPEND"){
					$("#reportForm").iForm("hide","beginTime");
					$("#reportForm").iForm("hide","endTime");
					$("#reportForm").iForm("hide","newFaultRemarks");
					$("#reportForm").iForm("hide","endReport");
					//$("#reportForm").iForm("setVal",{beginTime:null,endTime:null,newFaultRemarks:"",endReport:""});
	    		}
	    		else{
					$("#reportForm").iForm("show","beginTime");
					$("#reportForm").iForm("show","endTime");
					$("#reportForm").iForm("show","newFaultRemarks");
					$("#reportForm").iForm("show","endReport");
	    		}   
    	    }	
	    }
    },  
	{title : "实际开始时间", id : "beginTime", type:"datetime", dataType:"datetime",rules : {required:true}},
	{title : "实际完成时间", id : "endTime", type:"datetime", dataType:"datetime",rules : {required:true,greaterThan:"#f_beginTime"}, messages:{greaterThan:"实际完成时间必须大于实际开始时间"}},
	{
		title : "故障现象",
		id : "newFaultRemarks",
		type : "textarea",
		linebreak:true,
		wrapXsWidth:12,
		wrapMdWidth:12,
		height:55,
		rules : {required:true,maxlength:500}
	},
	{
		title : "故障处理情况",
		id : "endReport",
		type : "textarea",
		linebreak:true,
		wrapXsWidth:12,
		wrapMdWidth:12,
		height:55,
		rules : {required:true,maxlength:500}
	}
];

//验收阶段字段
var checkFields = [
    {
    	title : "验收结果",
    	id : "checkHandleStyle",
    	type : "combobox",
    	rules : {required:true},
    	value: "Y",
    	dataType:"enum",
	    data : [
	            ["Y","通过"],
	            ["N","不通过"]
	    ],
	    options :{
    	    onChange:function(val){
	    		if(val=="Y"){
					$("#checkForm").iForm("show","loseElectricPower");
					$("#checkForm").iForm("show","approveStopTime");
	    		}
	    		else{
					$("#checkForm").iForm("hide","loseElectricPower");
					$("#checkForm").iForm("hide","approveStopTime");
	    		}   
    	    }	
	    }
    },  
	{title : "损失电量(kWh)", id : "loseElectricPower",dataType:"number"},
	{title : "累计故障小时", id : "approveStopTime", type:"label"}

];

//页面查看时表单赋值
function setFormVal(woId){
	$.post(basePath + "workorder/workorder/queryWODataById.do",{workOrderId:woId},
		function(woBasicData){
			//通用按钮控制
			setWoButtonPriv();
			$(".priv").hide();
			$("#btn_deallog").show();//打印按钮除了草稿和提交状态，其他状态一直显示
			
			//通用表单初始化
			$("#workOrderForm").iForm("init",{"fields":fields,"options":{validate:true}});
			$("#f_priorityId").iCombo("init",{
			    data : priorityArray
			});
			initDefectHint(); 
			
			//通用表单赋值
			var woFormData = eval("(" +woBasicData.workOrderForm+ ")");
			$("#workOrderForm").iForm("setVal",{"relPtwIds":woBasicData.relPtwIds,"relMatApplyIds":woBasicData.relMatApplyIds});
			$("#workOrderForm").iForm("setVal",woFormData).iForm("endEdit");
			//console.log(woFormData);
			
			//设置全局变量
			processInstId = woFormData.workflowId;
			taskId = woBasicData.taskId;
			createtime =  FW.long2time(woFormData.createDate);
			updatetime =  FW.long2time(woFormData.modifyDate);
			approveFlag = woBasicData.approveFlag;
			if(approveFlag=="approver"){
				auditInfoShowBtn = 1;
			}
			else{
				auditInfoShowBtn = 0;
			}

			//设置标题
			woStatus = woFormData.currStatus;
			if(woStatus == DRAFT)
				$("#workOrderFormTitle").html("新建缺陷");
			else
				$("#workOrderFormTitle").html("缺陷详情");
			
       		//已领物料区域初始化
 			$("#title_tool_old").show();
      		$("#toolTableOld").datagrid({
			    columns:toolGridField,
			    idField:'toolDelteId',
			    singleSelect:true,
			    fitColumns:true,
			    scrollbarSize:0,
			    nowrap:false
			}); 					
			$("#title_tool_old").iFold("init");	
			$("#toolTableOld").datagrid("hideColumn","oper"); //隐藏操作列
			
 			//加载已领物料
			var toolDataStr = woBasicData.toolData;
			var toolDataObj = eval("(" + toolDataStr + ")");
			if(toolDataObj!=null && toolDataObj.length>0){
				$("#toolTableOld").datagrid("loadData",toolDataObj);
			}
			else{
				$("#title_tool_old").iFold("hide");
			}
			
			//根据状态进行判断
			if(woStatus == DRAFT){	//0 草稿状态
				//按钮控制
				$("#btn_wo_save").show();//暂存
				$("#btn_wo_commit").show();//提交
				if(loginUserId == woFormData.createuser){
					$("#btn_wo_delete").show();//删除
				}
				$("#btn_deallog").hide();//打印
				FW.fixRoundButtons("#toolbar");
				//表单控制
				$("#workOrderForm").iForm("beginEdit");
				$("#workOrderForm").iForm("endEdit","woWindStation");
				$("#workOrderForm").iForm("hide","relMatApplyIds");
				$("#workOrderForm").iForm("hide","relPtwIds");
				$("#workOrderForm").iForm("hide","endReportUserName");
				$("#workOrderForm").iForm("hide","woMaintainExecutorName");
				$("#workOrderForm").iForm("hide","isToWorkTicket");
				//生成和选择设备树
				equipId = woFormData.equipId;
				initWoInfoAssetTree();
			}
			else if(woStatus == WORK_ORDER_COMMIT){	//1 工单提交状态
				//表单控制
				$("#workOrderForm").iForm("endEdit");
				$("#workOrderForm").iForm("hide","relMatApplyIds");
				$("#workOrderForm").iForm("hide","relPtwIds");
				$("#workOrderForm").iForm("hide","endReportUserName");
				$("#workOrderForm").iForm("hide","woMaintainExecutorName");
				
				$("#btn_deallog").hide();//打印
				if(loginUserId == woFormData.createuser){
					//按钮控制
					$("#btn_wo_edit").show();//可编辑
					$("#btn_wo_commit").show();//可提交
					$("#btn_wo_obsolete").show();//可作废
					FW.fixRoundButtons("#toolbar");
					//生成和选择设备树
					equipId = woFormData.equipId;
					initWoInfoAssetTree();
				}
			}
			else if(woStatus == MONITOR_AUDIT){ //2班长审核
				$("#workOrderForm").iForm("endEdit");
				$("#workOrderForm").iForm("hide","endReportUserName");
				if(approveFlag == "approver"){
					$("#workOrderForm").iForm("beginEdit", "woMaintainExecutorName");
					$(".monitorAudit").show();
				}
			}
			else if(woStatus== CHAIRMAN_AUDIT){ //3场长(助理)审核
				$("#workOrderForm").iForm("endEdit");
				$("#workOrderForm").iForm("hide","endReportUserName");
				if(approveFlag == "approver"){
					$(".chairmanAudit").show();
				}
			}
			else if(woStatus == MONITOR_DISTRIBUTE){ //4班长分发
				$("#workOrderForm").iForm("endEdit");
				$("#workOrderForm").iForm("hide","endReportUserName");
				if(approveFlag == "approver"){
					$("#workOrderForm").iForm("beginEdit", "woMaintainExecutorName");
					$("#btn_wo_audit").show();
				}
			}
			else if(woStatus == WORK_ORDER_PLAN){ //5工作策划
				$("#workOrderForm").iForm("endEdit");
				if(approveFlag == "approver"){
					$("#btn_wo_audit").show();
					$("#workOrderForm").iForm("beginEdit","isToWorkTicket");
					//待领物料
		 			$("#title_tool").show();
		      		$("#toolTable").datagrid({
					    columns:toolGridField,
					    idField:'toolDelteId',
					    singleSelect:true,
					    fitColumns:true,
					    scrollbarSize:0,
					    nowrap:false
					}); 	
		 			$("#title_tool").iFold("init");
				}
			}
			else if(woStatus== WORK_TICKET_PROCEDURE){//6工作票流程
				$("#workOrderForm").iForm("endEdit");
				if(approveFlag == "approver"){
					if(woBasicData.hasValidPtw){	//TIM-775  1.如果关联工作票，且工作票处于未作废未终结状态，则不显示“审批”和“新建工作票”按钮，工作票终结会自动触发工单走到下一步。
						$("#btn_wo_audit").hide();
						$("#btn_ptw_new").hide();
					}
					else{	//2.否则显示“作废”和“新建工作票”按钮，
						$("#btn_wo_obsolete").show();//可作废
						$("#btn_ptw_new").show();
					}
				}
			}
			else if(woStatus == WORK_ORDER_REPORT){ //7填写故障缺陷处理单
				$("#workOrderForm").iForm("endEdit");
				if(approveFlag == "approver"){
					//回填div
					$("#reportFormDiv").show();
					$("#reportForm").iForm("init",{"fields":reportFields,"options":{validate:true}});
					$("#reportForm").iForm("setVal",woFormData);
					$("#reportForm").iForm("beginEdit");
					$("#btn_wo_audit").show();//可审批
					FW.fixRoundButtons("#toolbar");
				}
			}
			else if(woStatus == WORK_ORDER_CHECK){ //8验收
				$("#workOrderForm").iForm("endEdit");
				//回填div
				$("#reportFormDiv").show();
				$("#reportForm").iForm("init",{"fields":reportFields,"options":{validate:true}});
				$("#reportForm").iForm("setVal",woFormData);
				$("#reportForm").iForm("endEdit");

				if(approveFlag == "approver"){
					//验收div
					$("#checkFormDiv").show();
					$("#checkForm").iForm("init",{"fields":checkFields,"options":{validate:true}});
					if(woFormData.endTime - woFormData.discoverTime<0){
						woFormData.approveStopTime = 0;
					}
					else{
						woFormData.approveStopTime = ((woFormData.endTime - woFormData.discoverTime)/(3600*1000)).toFixed(2); 
					}
					$("#checkForm").iForm("setVal",woFormData);
					$("#btn_wo_audit").show();//可审批
					FW.fixRoundButtons("#toolbar");
				}
				else{
					$("#checkForm").iForm("endEdit");
				}
			}
			else if(woStatus==SHIELD_OPR_DIRECTOR_AUDIT || woStatus==SHIELD_OPR_MINISTER_AUDIT ||
					woStatus==SHIELD_SAFE_CLERK_AUDIT || woStatus==SHIELD_SAFE_MINISTER_AUDIT ||
					woStatus==SHIELD_DUTY_RESET ){ //9运检主管审批  10运检部长审批  11生安专责审批   12生安部部长审批 13值长复位
				$("#workOrderForm").iForm("endEdit");
				if(approveFlag == "approver"){
					$("#btn_wo_audit").show();
					FW.fixRoundButtons("#toolbar");
				}
			}
			else if(woStatus==SUSPEND){ //14挂起   和场长(助理)审核界面一致
				$("#workOrderForm").iForm("endEdit");
				if(approveFlag == "approver"){
					$(".restart").show();
				}
			}
			else if(woStatus == OBSELETE){ //15已作废
				$("#workOrderForm").iForm("endEdit");
			}
			else if(woStatus==DONE){ //16已完成
				$("#workOrderForm").iForm("endEdit");
				//回填div
				$("#reportFormDiv").show();
				$("#reportForm").iForm("init",{"fields":reportFields,"options":{validate:true}});
				$("#reportForm").iForm("setVal",woFormData);
				$("#reportForm").iForm("endEdit");
				//验收div
				$("#checkFormDiv").show();
				$("#checkForm").iForm("init",{"fields":checkFields,"options":{validate:true}});
				$("#checkForm").iForm("setVal",woFormData);
				$("#checkForm").iForm("endEdit");
			}
			if(woFormData.woCommitHandleStyle!=null && woFormData.woCommitHandleStyle == "REMOTE_RESET"){
				$("#reportFormDiv").hide();
				$("#checkFormDiv").hide();
			}
			FW.fixRoundButtons("#toolbar");
			$(window).resize();
		},"json");
}

//提交工单基本信息
function commitWO(commitStyle){
	//表单验证
	var tempEquiName = $("#workOrderForm").iForm("getVal","equipName");
	if(tempEquiName == '' || tempEquiName == "请从左边设备树选择"){
		FW.error("请从左边设备树选择");
		return;
	}
	if(!$("#workOrderForm").valid()){
		FW.error("基本信息不完整");
		return ;
	}
	if(!$("#reportForm").valid()){
		FW.error("回填信息不完整");
		return ;
	}
	if(!$("#checkForm").valid()){
		FW.error("验收信息不完整");
		return ;
	}	
	var operation = "";
	if(commitStyle=="commit"){
		$("#btn_wo_commit").button('loading');
		operation = "提交";
	}
	else{
		operation = "暂存";
	}
	var woFormObj = $("#workOrderForm").iForm("getVal");
	var woFormData = JSON.stringify(woFormObj);  //取表单值

	$.post(basePath + "workorder/workorder/commitWorkOrderdata.do",
		{"workOrderForm":woFormData,"commitStyle":commitStyle},
		function(data){
			if(data.result == "success"){
				FW.success(operation + "成功");
				woId = data.woId;
				if(commitStyle=="save"){ //暂存
					$("#workOrderForm").iForm("setVal",{"id":data.woId,"workOrderCode":data.workOrderCode});
					if(!woFormObj.currStatus){
						$("#btn_wo_deleteDiv").show();
						$("#btn_wo_delete").show();
					}
					FW.fixRoundButtons("#toolbar");  //解决按钮隐藏后的圆角问题
				}
				else{//提交
					var workFlow = new WorkFlow();
					var taskId = data.taskId;
					var multiple = 1;
					if(woFormObj.woCommitHandleStyle=="REMOTE_RESET"){
						submitSuccess();
					}
					else{
						workFlow.submitApply(taskId,JSON.stringify(woFormData), submitSuccess,cancel,multiple); //班长审核可多选
					}
				}
			}
			else {
				FW.error(operation + "失败");
				$("#btn_wo_commit").button('reset');
			}
		},"json");
}

//提交成功
function submitSuccess(){
	FW.success("提交成功");
	closeCurPage();
};

//取消
function cancel(){ 
	closeCurPage();
};

//班长审核
function monitorAudit(monitorHandleStyleParam){
	$("#workOrderForm").iForm("setVal",{monitorHandleStyle:monitorHandleStyleParam});
	audit();
}

//场长审核
function chairmanAudit(chairmanHandleStyleParam){
	$("#workOrderForm").iForm("setVal",{chairmanHandleStyle:chairmanHandleStyleParam});
	audit();
}

//结束编辑
function endEdit(id){
	var rows = $("#"+id).datagrid('getRows');
	for(var i=0;i<rows.length;i++){
		$("#"+id).datagrid('endEdit',i);
	}
}
/** 给取消审核时，补充一个恢复物资申请数可编辑的控制 ---begin*/
function beginEdit(id){
	var rows = $("#"+id).datagrid('getRows');
	for(var i=0;i<rows.length;i++){
		$("#"+id).datagrid('beginEdit',i);
	}
}
function resetTable(){
	//如果有物资表，就恢复可编辑，除了提交工单这个环节
	if($("#toolTable").prev().length>0){
		beginEdit("toolTable");
	}
}
/** 给取消审核时，补充一个恢复物资申请数可编辑的控制 ---end*/
//开始审批
function audit(){  
	if(!$("#workOrderForm").valid()){
		FW.error("基本信息校验异常");
		return ;
	}
	if(woStatus== WORK_ORDER_PLAN && !$("#toolTableForm").valid()){
		FW.error("物料信息校验异常");
		return ;
	}
	if(woStatus== WORK_ORDER_REPORT && !$("#reportForm").valid()){
		FW.error("故障缺陷处理情况信息校验异常");
		return ;
	}
	if(woStatus == WORK_ORDER_CHECK && !$("#checkForm").valid()){
		FW.error("验收信息校验异常");
		return ;
	}
	//获取表单数据
	var woFormObj = $("#workOrderForm").iForm("getVal");
	var woFormData = JSON.stringify(woFormObj);
	var reportFormObj = $("#reportForm").iForm("getVal");
	var reportFormData = JSON.stringify(reportFormObj);
	if(woStatus == WORK_ORDER_REPORT && reportFormObj.reportHandleStyle=="DONE"){
		if(reportFormObj.beginTime<woFormObj.discoverTime){
			FW.error("实际开始时间不能小于发现时间");
			return ;
		}
	}

	var checkFormObj = $("#checkForm").iForm("getVal");
	var checkFormData = JSON.stringify(checkFormObj);
	var toolData;
	
	if(woStatus==WORK_ORDER_PLAN){
		isToPTW = woFormObj.isToWorkTicket;
		endEdit("toolTable");
		toolData = $("#toolTable").datagrid("getData");//物资领用
	}
	var restartHandleStyle = "";
	
	//挂起节点传入的参数
	if(woFormObj.woCommitHandleStyle=="MAINTAIN")
		restartHandleStyle = "MAINTAIN";
	else
		restartHandleStyle = "REPAIR_NOW";
	
	var woId = woFormObj.id;
	var workflowId = woFormObj.workflowId;
	
	var url = basePath + "workflow/process_inst/setVariables.do";
	var params = {};
	params['processInstId'] = workflowId;
	params['businessId'] = woId;
	var variables = [{'name':'monitorHandleStyle','value':woFormObj.monitorHandleStyle},
	                 {'name':'chairmanHandleStyle','value':woFormObj.chairmanHandleStyle},
	                 {'name':'reportHandleStyle','value':reportFormObj.reportHandleStyle},
	                 {'name':'checkHandleStyle','value':checkFormObj.checkHandleStyle},
	                 {'name':'restartHandleStyle','value':restartHandleStyle},
	                 {'name':'isToWT','value':isToPTW}];
	params['variables'] = JSON.stringify(variables);

	$.post(url,params,function(data){
		if(data.result == 'ok'){
			var businessData = {
					"workOrderId":woId,
					"workOrderForm":woFormData,
					"reportForm":reportFormData,
					"checkForm":checkFormData,
					"toolData":toolData
			};
			var workFlow = new WorkFlow();
			var multiple = 0;
			if(woStatus==MONITOR_AUDIT && woFormObj.monitorHandleStyle=="SUSPEND"){
				multiple = 1;
			}
			else if(woStatus==CHAIRMAN_AUDIT && woFormObj.chairmanHandleStyle=="SUSPEND"){
				multiple = 1;
			}	
			else if(woStatus==WORK_ORDER_REPORT){ //验收或挂起都支持多选
				multiple = 1;
			}	
			else if(woStatus==SHIELD_DUTY_RESET){
				multiple = 1;
			}	
			workFlow.showAudit(taskId,JSON.stringify(businessData),agree,rollback,stop,null,multiple,resetTable);
		}
	});
}

//同意
function agree(){
	if(woStatus==WORK_ORDER_PLAN){
		//setFormVal(woId);
		//FW.deleteTabById(FW.getCurrentTabId());
		if(isToPTW == "toPTW"){  //如果走工作票，则打开新建工作票页面
			newPtw(false);
		}
		$("#btn_wo_audit").remove();
		$("#workOrderForm").iForm("endEdit");
		FW.fixRoundButtons("#toolbar");
	}
	else{
		closeCurPage();
	}
};

//跳转到关联工单
function openWoPageByCode(woCode){
	//新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
	var currTabId = FW.getCurrentTabId();
	var rand = new Date().getTime() + Math.floor(Math.abs(Math.random()*100));
	var urlPath = basePath+ "workorder/workorder/openWorkOrderAddPageZJW.do?woCode=" + woCode;
	var opts = {
        id : "wo" + rand,
        name : "工单详情",
        url : urlPath,
        tabOpt : {
        	closeable : true,
        	afterClose : "FW.deleteTab('$arg');FW.activeTabById('" + currTabId + "');" 
        }
    };
    _parent()._ITC.addTabWithTree(opts); 
}

//跳转到新建工作票页面（打开工作票页面）
function newPtw(closeCurrent){
	//新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
	var currTabId = FW.getCurrentTabId();
	var params = {opType:"newPtw",workOrderId:woId};
	params = JSON.stringify(params);
	var urlPath = basePath+ "ptw/ptwInfo/preQueryPtwInfo.do?params="+params;
	var opts = {
        id : "ptw" + currTabId,
        name : "新建工作票",
        url : urlPath,
        tabOpt : {
        	closeable : true,
        	afterClose : "FW.deleteTab('$arg');FW.deleteTabById('" + currTabId + "'); FW.activeTabById('equmaintain'); FW.getFrame('equmaintain').refreshAfterClose(); " 
        }
    };
    //FW.activeTabById("ptw" + currTabId);
    _parent()._ITC.addTabWithTree(opts); 
    if(closeCurrent==true){
    	FW.deleteTabById(currTabId);
    }
}

//跳转到相关工作票页面
function openPtwPage(ptwId){
	 //新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
	var params = {opType:"handlePtw",id:ptwId};
	params = JSON.stringify(params);
	var currTabId = FW.getCurrentTabId();
	var rand = new Date().getTime() + Math.floor(Math.abs(Math.random()*100));
	var urlPath = basePath+ "ptw/ptwInfo/preQueryPtwInfo.do?params="+params;
	var opts = {
        id : "ptw" + rand,
        name : "关联工作票",
        url : urlPath,
        tabOpt : {
        	closeable : true,
        	afterClose : "FW.deleteTab('$arg');FW.activeTabById('" + currTabId + "');" 
        }
    };
    _parent()._ITC.addTabWithTree(opts); 
}

//跳转到相关领料单页面
function openMatApplyPage(matApplyId){
	 //新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
	var currTabId = FW.getCurrentTabId();
	var rand = new Date().getTime() + Math.floor(Math.abs(Math.random()*100));
	var urlPath = basePath+ "inventory/invmatapply/invMatApplyForm.do?imaid="+matApplyId;
	var opts = {
        id : "matApply" + rand,
        name : "关联领料单",
        url : urlPath,
        tabOpt : {
        	closeable : true,
        	afterClose : "FW.deleteTab('$arg');FW.activeTabById('" + currTabId + "');" 
        }
    };
    _parent()._ITC.addTabWithTree(opts); 
}

//回退
function rollback(){
	closeCurPage();
};

//终止
function stop(rowdata){
	var data={};
	data['processInstId'] = rowdata.processInstId;
	data['reason'] = rowdata.reason;
	data['businessId'] = woId;
	var url = 'workorder/workorder/stopWorkOrder.do';
	$.post(url, data, function(data){
		if(data.result=='success'){
			FW.success("提交成功");
			_parent().$("#itcDlg").dialog("close");
			closeCurPage();
			homepageService.refresh();
		}
		else{
			FW.error("提交失败");
		}
	});
}

//编辑工单
function editWO(){
	$("#workOrderForm").iForm("beginEdit");
	$("#workOrderForm").iForm("endEdit","woWindStation");
	if(woStatus == WORK_ORDER_COMMIT){
		$("#workOrderForm").iForm("hide","isToWorkTicket");
	}
	$("#btn_wo_edit").hide();
	$("#btn_wo_save").show();
	FW.fixRoundButtons("#toolbar");
}

//删除工单
function deleteWO(){
	FW.confirm("确定删除|确定删除该条工单信息么？",function(){
		var woFormObj = $("#workOrderForm").iForm("getVal");
		var woFormData = JSON.stringify(woFormObj);  //取表单值
		var woId = woFormObj.id;
		$.post(basePath + "workorder/workorder/deleteWorkOrderDraft.do",{"woId":woId},
			function(data){
				if(data.result == "success"){
					FW.success("删除成功");
					closeCurPage();
				}else {
					FW.error("删除失败");
				}
			},"json");
	},null,"info");
}

//作废工单（非草稿，仅工单发起人可以作废）
function obsoleteWO(){
	FW.confirm("确定作废|确定作废该条工单信息么？",function(){
		var woFormObj = $("#workOrderForm").iForm("getVal");
		var woFormData = JSON.stringify(woFormObj);  //取表单值
		var woId = woFormObj.id;

		$.post(basePath + "workorder/workorder/obsoleteWorkOrder.do",{"woId":woId},
			function(data){
				if(data.result == "success"){
					FW.success("作废成功");
					closeCurPage();
				}else {
					FW.error("作废失败");
				}
			},"json");
	},null,"info");
}

//选中设备树事件
function passAssetSelect(data){
	$("#workOrderForm").iForm("setVal",{
		equipId : data.id,
		equipNameCode:data.assetCode,
		equipName : data.text
	});
}

//显示审批流程(流程未启动)
function showDiagram(){
	var workFlow = new WorkFlow();
	workFlow.showDiagram(defKey);
}

//显示审批信息(流程已启动)
function showAuditInfo(){
	if( processInstId == null || processInstId == "" ){
		var defKey="workorder_"+siteId.toLowerCase()+"_wo";
		var workFlow = new WorkFlow();
		workFlow.showDiagram(defKey);
	}else{
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
		if(woStatus==MONITOR_AUDIT || woStatus==CHAIRMAN_AUDIT || woStatus==WORK_TICKET_PROCEDURE){
			auditInfoShowBtn = 0;
		}		
		workFlow.showAuditInfo(processInstId,JSON.stringify(businessData),auditInfoShowBtn,audit);
	}
}

//关闭当前tab 页
function closeCurPage(flag){
	if(flag){
		FW.set("eqWOlistDoNotRefresh",true);
	}
	FW.deleteTabById(FW.getCurrentTabId());
}

//工单权限
function setWoButtonPriv(){
	Priv.map("privMapping.OPR_PATROl_ADD","WO_ADD");
	Priv.map("privMapping.WO_SAVE","WO_SAVE");
	Priv.map("privMapping.WO_COMMIT","WO_COMMIT");
	Priv.map("privMapping.WO_EDIT","WO_EDIT");
	
	Priv.map("privMapping.WO_DELETE","WO_DELETE");
	Priv.map("privMapping.WO_INVALID","WO_INVALID");
	
	Priv.map("privMapping.WO_AUDIT","WO_AUDIT");
	Priv.apply();
	FW.fixRoundButtons("#toolbar");	
}

//树加载完成
function assetTreeRollBackFunc(){
	if(equipId != null && equipId.length>0){
		setTimeout(function(){
			window.parent.document.getElementById("woInfoAssestTree").contentWindow.expandForHintById(equipId);
		},500);
	}
}	

//缺陷下拉提示
function initDefectHint(){
	$("#f_description").iHint("init", {
		datasource:basePath + "workorder/workorder/faultTypeHint.do",
		showOn:"input",
		forceParse:"function",
		clickEvent:function(id,name){
			$("#f_description").val(name);
		}
	});
}

//打印按钮初始化
function intPrintButton(){
	//fileExportPath = "http://localhost/itc_report/";
	var urlExport = fileExportPath + "preview?__report=report/TIMSS2_WO_DEAL_LOG_001.rptdesign&__format=pdf"+ "&woId=" + woId + "&siteId=" + siteId;							
	$("#btn_deallog").click(function(){
		FW.dialog("init",{
			src: urlExport,
			btnOpts:[
					{
					    "name" : "关闭",
					    "float" : "right",
					    "style" : "btn-default",
					    "onclick" : function(){
					        _parent().$("#itcDlg").dialog("close");
					    }
					}
		        ],
			dlgOpts:{ width:800, height:600, closed:false, title:"故障缺陷处理记录", modal:true }
		});
	});
}