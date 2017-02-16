var fireWorkContentData = [["电焊","电焊"],["气焊","气焊"],["气割","气割"],["明火","明火"]];
var workConditionContentData = [["水下","水下"],["涉水","涉水"],["高空","高空"],
			["地下","地下"],["露天","露天"],["洞内","洞内"]];
//是否标准工作票,此处固定为false 
var isStdWt = false;
//工作票查询的类型
var param_queryWtType = "normal";//all/normal/fire

//工单是否存在
var isWoExist = true;

var baseInfoField = [
	{id:"id",type:"hidden"},
	{id:"workOrderId",type:"hidden"},
	{id:"eqId",type:"hidden"},
	{id:"wtNo",title : "工作票编号",type:"label",value:"test"},
	{id:"wtTypeId",title : "工作票类型",type : "label"},
	{id:"workOrderNo",title : "工单编号", rules : {remote:{
		url: basePath + "ptw/ptwInfo/verifyWorkOrderNo.do",
		type: "post",
		data: {
		    workOrderNo: function() {
		        return $("#f_workOrderNo").val();
		    }
		},
		complete:function(data){
			//成功之后给设备、负责人赋值
			if(data.responseText == "true" && !isFireWt){
				$.post(basePath + "ptw/ptwInfo/findWorkOrderInfo.do",{"workOrderNo":$("#f_workOrderNo").val()},function(data){
					isWoExist = data.woExist;
					var workOrder = data.workOrder;
					if(workOrder){
						$("#baseInfoForm").iForm('setVal',{eqId:workOrder.equipId,eqNo:workOrder.equipNameCode,
							eqName:workOrder.equipName,licWpicNo:workOrder.currHandUserName,workOrderId:workOrder.id,
							woWorkTask:workOrder.description});
						//不能再根据树来选择设备
						canSelectTree = true;
						//$("#baseInfoForm").iForm("endEdit","woWorkTask");
						var licWpicNoTxt = $("#f_licWpicNo").iCombo("getTxt");
						if(licWpicNoTxt === ""){
							$("#f_licWpicNo").iCombo("setVal","");
						}
					}else{
						//工单编号不存在还原带入信息
						canSelectTree = true;
						$("#baseInfoForm").iForm("beginEdit","woWorkTask");
						$("#baseInfoForm").iForm('setVal',{woWorkTask:""});
						FW.error("工单编号不存在或不处在工作票流程中的状态");
					}
				},"json");
			}
		}
	}},messages:{remote:"工单编号不存在或不处在工作票流程中的状态"}},
	{id:"keyBoxNo",title : "钥匙箱号",rules : {maxlength:10}},
	{id:"eqName",title : "设备名称",type:"label",value:"请从左边设备树选择",
		formatter:function(val){
			if(val == "请从左边设备树选择"){
				return "<span style='color:red'>请从左边设备树选择</span>";
			}
			return val ? val : "";
		}},
	{id:"eqNo",title : "设备编码",type:"hidden",value:"请从左边设备树选择"},

	{id:"woWorkTask",title : "工作任务",wrapXsWidth:12,wrapMdWidth:8},
	
	{id:"workContent",title : "工作内容",linebreak:true,wrapXsWidth:12,wrapMdWidth:12,rules : {required:true,maxlength:300}},
	{id:"workContentFire",title : "工作内容",type : "checkbox",data:fireWorkContentData,linebreak:true,wrapXsWidth:12,wrapMdWidth:12,rules : {required:true}},
	{id:"workContentFj",title : "工作内容",type : "textarea",linebreak:true,wrapXsWidth:12,wrapMdWidth:12,rules : {required:true,maxlength:300}},
	{id:"workPlace",title : "工作地点",linebreak:true,wrapXsWidth:12,wrapMdWidth:12,rules : {required:true,maxlength:300}},
	{id:"licWorkClass",title : "工作班",linebreak:true, rules : {maxChLength:60}},
	{id:"licWorkClassPeople",title : "工作班成员",rules : {maxlength:200}},
	{id:"licWorkClassNum",title : "工作班人数",rules : {digits:true}},
	//{id:"licWpicNo",title : "工作负责人",rules : {required:true,maxChLength:10}},
	{id:"licWpic",title : "工作负责人",rules : {maxChLength:10}},
	{id:"preWorkStartTime",title : "预计开工时间",type:"datetime",linebreak:true,dataType:"datetime"},
	{id:"preWorkEndTime",title : "预计结束时间",type:"datetime",dataType:"datetime", rules : {greaterThan:"#f_preWorkStartTime"}},
	
	
	{id:"createUserNameAndTime",title : "创建人/时间",type:"label"},
	{id:"approverAndTime",title : "审批人/时间",type:"label"},
	{id:"issuerAndTime",title : "签发人/时间",type:"label"},
	{id:"finWlAndTime",title : "结束人/时间",type:"label"},
	
	{id:"workCondition",title : "工作条件",type:"checkbox",data:workConditionContentData,linebreak:true,rules : {required:true}}
];

var fireBaseInfoField = [
	{id:"fireUnit",title : "动火单位",rules : {maxlength:20}},
	{id:"fireWc",title : "动火班组",rules : {maxChLength:60}},
	{id:"fireWpExec",title : "动火执行人"},
	{id:"guardXmCo",title : "消防监护人",rules : {maxChLength:60}},
	{id:"attachWtNo",title : "对应工作票编号",type : "label"},
	{id:"appvXfAndTime",title : "消防审核人/时间",type : "label",linebreak:true},
	{id:"appvAjAndTime",title : "安监审核人/时间",type : "label"},
	{id:"appvBmAndTime",title : "部门审核人/时间",type : "label"},
	{id:"appvCjAndTime",title : "厂级审核人/时间",type : "label"}
];

/**新建工作票时的基本信息展示*/
function initNewBaseInfo(){
	var prefix = new Date().getTime() + "" + Math.floor(Math.abs(Math.random()*100));
	if(isFireWt){
		//申请动火时间标题修改 修改成必填
		var preWorkStartTimeField = ptwUtil.findFieldById(baseInfoField,"preWorkStartTime");
		preWorkStartTimeField.title = "申请动火时间";
		preWorkStartTimeField.rules = {required:true};
		var preWorkEndTimeField = ptwUtil.findFieldById(baseInfoField,"preWorkEndTime");
		preWorkEndTimeField.title = "申请结束时间";
		preWorkEndTimeField.rules = {required:true, greaterThan:"#f_preWorkStartTime"};
		//工作负责人标题修改
		var licWpicNoField = ptwUtil.findFieldById(baseInfoField,"licWpic");
		licWpicNoField.title = "动火工作负责人";
	}

	
	//工作票类型修改
	var wtTypeIdField = ptwUtil.findFieldById(baseInfoField,"wtTypeId");
	wtTypeIdField.type = "combobox";
	wtTypeIdField.options = {
		url : basePath+ "ptw/ptwType/queryPtwTypes.do?queryType="+param_queryWtType+"&ptwTypeCodes="+ptwTypeCodes+"&prefix="+prefix,
		remoteLoadOn : "init",
		onChange:function(value){
			if(isNewWt){
				$.ajax({
					type : "POST",
					async: false,
					url: basePath+ "ptw/ptwType/queryPtwTypeInfo.do?ptwTypeId="+value+"&prefix="+prefix,
					data: {},
					dataType : "json",
					success : function(data) {
						param_config = data.ptwTypeDefine;
						ptwTypeCode = data.ptwType.typeCode;
						ptwTypeId = data.ptwType.id;
						isFireWt = param_config.isFireWt == 1 ? true : false;
				
						initBaseInfoByConfig();
						initSafeInfoByConfig();
					}
				});
			}
		}
	};
	
	$("#baseInfoForm").iForm("init",{"fields":baseInfoField,"options":{validate:true}});
	
	//将url中的的参数设置到form中
	$("#baseInfoForm").iForm("setVal",params);
	$("#baseInfoForm").iForm("hide","keyBoxNo");
	
	if(isFireWt){
		$("#fireBaseInfoDiv").show().iFold("init");
		$("#fireBaseInfoForm").iForm("init",{"fields":fireBaseInfoField,"options":{validate:true}});
		$.post(basePath + "ptw/ptwInfo/queryBasePtwInfo.do",{id:params.attachWtId,isNewFireWt:true},function(data){
			var pInfo = data.ptwInfo;
			$("#baseInfoForm").iForm('setVal',{workOrderId:pInfo.workOrderId,workOrderNo:pInfo.workOrderNo,
				eqId:pInfo.eqId,eqNo:pInfo.eqNo,eqName:pInfo.eqName,licWpicNo:pInfo.licWpicNo,workContent:pInfo.workContent,
				workPlace:pInfo.workPlace,preWorkStartTime:pInfo.preWorkStartTime,preWorkEndTime:pInfo.preWorkEndTime,woWorkTask:pInfo.woWorkTask});
			$("#baseInfoForm").iForm("endEdit",["workOrderNo","licWpicNo"]);
			
			canSelectTree = false;
			$("#fireBaseInfoForm").iForm('setVal',{"attachWtNo":pInfo.wtNo});
			$("#fireBaseInfoForm").iForm("endEdit","attachWtNo");
		},"json");
	}
	
	//从工单传递过来的新建工作票
	if(params.workOrderId && params.workOrderId != 0){
		//关闭工单详情
		if(params.currTabId){
			FW.set('ptwDoNotClose',true);
			FW.deleteTabById(params.currTabId);
		}
		$.post(basePath + "ptw/ptwInfo/findWorkOrderInfoById.do",{"workOrderId":params.workOrderId},function(data){
			var workOrder = data.bean;
			if(workOrder){
				$("#baseInfoForm").iForm('setVal',{eqId:workOrder.equipId,eqNo:workOrder.equipNameCode,
					eqName:workOrder.equipName,licWpicNo:workOrder.currHandlerUser,
					workOrderId:workOrder.id,workOrderNo:workOrder.workOrderCode,
					woWorkTask:workOrder.description});
				//不能再根据树来选择设备
				canSelectTree = false;
				$("#baseInfoForm").iForm("endEdit",["workOrderNo","woWorkTask"]);
			}
		},"json");
	}
}

function initBaseInfoByConfig(){
	var hideFields = [];
	var showFields = [];
	//工作内容
	if(param_config.hasRemarkWork == 1){//工作票类型为风机票时，工作内容为textarea
		hideFields.push('workContentFire');
		hideFields.push('workContent');
		showFields.push('workContentFj');
	}else{
		hideFields.push('workContentFj');
		hideFields.push('workContentFire');
		showFields.push('workContent');
	}
	
	//水工工作条件
	if(param_config.hasWorkCondition){
		showFields.push('workCondition');
	}else{
		hideFields.push('workCondition');
	}
	
	if(param_config.hasPreTime == 0){
		hideFields.push('preWorkStartTime');
		hideFields.push('preWorkEndTime');
	}else{
		showFields.push('preWorkStartTime');
		showFields.push('preWorkEndTime');
	}
	
	//如果是动火票
	if(param_config.isFireWt == 1){
		hideFields = hideFields.concat(['licWorkClass','licWorkClassPeople','licWorkClassNum']);
	}else{
		showFields = showFields.concat(['licWorkClass','licWorkClassPeople','licWorkClassNum']);
	}
	
	//新建票时
	if(isNewWt || isCopyWt){
		hideFields = hideFields.concat(['wtNo','createUserNameAndTime','issuerAndTime','finWlAndTime','approverAndTime']);
		$("#fireBaseInfoForm").iForm("hide",["appvXfAndTime","appvAjAndTime","appvBmAndTime","appvCjAndTime"]);
	}else{
		showFields = showFields.concat(['wtNo','createUserNameAndTime','issuerAndTime','finWlAndTime','approverAndTime']);
	}
	
	$("#baseInfoForm").iForm("hide",hideFields);
	$("#baseInfoForm").iForm("show",showFields);
		
	if(param_config.isFireChkBox==1){
		$("#fireBaseInfoForm").iForm("show",["fireWpExec","guardXmCo"]);
	}else{
		$("#fireBaseInfoForm").iForm("hide",["fireWpExec","guardXmCo"]);
	}

	
}