var fireWorkContentData = [["电焊","电焊"],["气焊","气焊"],["气割","气割"],["明火","明火"]];
var workConditionContentData = [["水下","水下"],["涉水","涉水"],["高空","高空"],
			["地下","地下"],["露天","露天"],["洞内","洞内"]];
//是否标准工作票按钮，是否为选中状态
var isStdWt = false;
//工作票查询的类型
var param_queryWtType = "normal";//all/normal/fire

var baseInfoField = [
	{id:"id",type:"hidden"},
	{id:"workOrderId",type:"hidden"},
	{id:"eqId",type:"hidden"},
	{id:"wtNo",title : "工作票编号",type:"label",value:"test"},
	{id:"wtTypeId",title : "工作票类型",type : "label"},
	{id:"workOrderNo",title : "工单编号", rules : {required:true,remote:{
		url: basePath + "ptw/ptwInfo/verifyWorkOrderNo.do",
		type: "post",
		data: {
		    workOrderNo: function() {
		        return $("#f_workOrderNo").val();
		    }
		},
		complete:function(data){
			//成功之后给设备、负责人赋值
			if(data.responseText == "true"){
				$.post(basePath + "ptw/ptwInfo/findWorkOrderInfo.do",{"workOrderNo":$("#f_workOrderNo").val()},function(data){
					var workOrder = data.workOrder;
					if(workOrder){
						var currHandlerUser = workOrder.currHandlerUser;
						var licWpicNo = currHandlerUser ? currHandlerUser : $("#f_licWpicNo").iCombo("getVal");
						$("#baseInfoForm").iForm('setVal',{eqId:workOrder.equipId,eqNo:workOrder.equipNameCode,
							eqName:workOrder.equipName,licWpicNo:licWpicNo,workOrderId:workOrder.id,
							woWorkTask:workOrder.description});
						//不能再根据树来选择设备
						canSelectTree = false;
						$("#baseInfoForm").iForm("endEdit","licWpicNo","woWorkTask");
						
					}
				},"json");
			}
		}
	}},messages:{remote:"工单编号不存在或不处在工作票流程中的状态"}},
	{id:"keyBoxNo",title : "钥匙箱号", rules : {required:true}},
	{id:"eqName",title : "设备名称",type:"label",value:"请从左边设备树选择",
		formatter:function(val){
			if(val == "请从左边设备树选择"){
				return "<span style='color:red'>请从左边设备树选择</span>";
			}
			return val ? val : "";
		}},
	{id:"eqNo",title : "设备编码",type:"hidden",value:"请从左边设备树选择"},
	{id:"createUserNameAndTime",title : "创建人/时间",type:"label"},
	{id:"issuerAndTime",title : "签发人/时间",type:"label"},
	{id:"finWlAndTime",title : "结束人/时间",type:"label"},
	{id:"preWorkStartTime",title : "预计开工时间",type:"datetime",dataType:"datetime", rules : {required:true}},
	{id:"preWorkEndTime",title : "预计结束时间",type:"datetime",dataType:"datetime", rules : {required:true,greaterThan:"#f_preWorkStartTime"}},
	{id:"woWorkTask",title : "工作任务",rules : {required:true}},
	{id:"licWpicNo",title : "工作负责人",
		type:"combobox",
		rules : {required:true},
		options:{
			allowSearch:true,
			remoteLoadOn:"init",
			url:basePath+ "ptw/ptwInfo/queryPtwUsersByGroup.do?role=PTW_work_wpic"
			}
	},
	{id:"licWorkClass",title : "工作班",rules : {required:true,maxlength:100}},
	{id:"licWorkClassPeople",title : "工作班成员",rules : {required:true,maxlength:200}},
	{id:"licWorkClassNum",title : "工作班人数",rules : {required:true,digits:true}},
	{id:"workPlace",title : "工作地点",linebreak:true,wrapXsWidth:12,wrapMdWidth:8,rules : {required:true,maxlength:300}},
	{id:"workCondition",title : "工作条件",type:"checkbox",data:workConditionContentData,linebreak:true,rules : {required:true}},
	{id:"workContent",title : "工作内容",linebreak:true,wrapXsWidth:12,wrapMdWidth:8,rules : {required:true,maxlength:300}},
	{id:"workContentFire",title : "工作内容",type : "checkbox",data:fireWorkContentData,linebreak:true,wrapXsWidth:12,wrapMdWidth:8,rules : {required:true}},
	{id:"workContentFj",title : "工作内容",type : "textarea",linebreak:true,wrapXsWidth:12,wrapMdWidth:8,rules : {required:true,maxlength:300}},
	{id:"isStdWt",title : "是否为标准票",linebreak:true,type:"radio",data:[[1,"是"],[0,"否",true]],render:function(id){
		$("#" + id + "_0").parents(".element-wrap").find('input').on("ifClicked",function(){
			var val = $(this).val();
			if(val == 0){
				//普通票
				param_queryWtType = "normal";
				isStdWt = false;
				if(isFireWt){
					$("#fireBaseInfoDiv").iFold("show");
				}
				$("#uploadFileDiv").iFold("show");
				
			}else{
				//标准票
				param_queryWtType = "all";
				isStdWt = true;
				if(isFireWt){
					$("#fireBaseInfoDiv").iFold("hide");
				}
				$("#uploadFileDiv").iFold("hide");
			}
			if(isNewWt){
				var defaultTypeId = $("#baseInfoForm").iForm('getVal','wtTypeId');
				$.post(basePath+ "ptw/ptwType/queryPtwTypes.do?queryType="+param_queryWtType,
					{defaultTypeId : defaultTypeId},function(data){
					$("#f_wtTypeId").iCombo("loadData",data);				
				});
			}
			initBaseInfoByConfig();
		});
	}}
];
//根据班组输入提示
baseInfoField[16].render = function(id){
	$("#"+id).iHint('init', getiHintParams("WO_WORKTEAM"));
	
}
function getiHintParams(searchEnum){
	var iHintParams ={
			datasource : basePath + "ptw/ptwUtil/queryEnum.do?ecatCode="+searchEnum+"&siteid="+siteid,
			forceParse : function(){},
			clickEvent : function(id,name,rowdata) {
				if(rowdata != null){
					$("#f_licWorkClass").val(rowdata.enumName);
				}
			},
			"highlight":true,
			"formatter" : function(id,name,rowdata){
				var showText = rowdata.enumName;
				return showText;
			}

		};
	return iHintParams;
}

var fireBaseInfoField = [
	{id:"fireUnit",title : "动火单位",rules : {required:true,maxlength:20}},
	{id:"fireWc",title : "动火班组",rules : {required:true,maxChLength:60}},
	{id:"fireWpExecNo",title : "动火执行人",type:"combobox",rules : {required:true},options:{allowSearch:true,remoteLoadOn:"init",url:basePath+ "ptw/ptwInfo/queryPtwUsersByGroup.do?role=PTW_work_wpic"}},
	{id:"guardXmCoNo",title : "消防监护人",type:"combobox",rules : {required:true},options:{allowSearch:true,remoteLoadOn:"init",url:basePath+ "ptw/ptwInfo/queryPtwUsersByGroup.do?role=PTW_fire_mgr"}},
	{id:"attachWtNo",title : "对应工作票编号",type : "text",
		rules:{required:true,remote:{
				url: basePath + "ptw/ptwInfo/verifyPtwNo.do",
				type: "post",
				data: {
				    attachWtNo: function() {
				        return $("#f_attachWtNo").val();
				    }
				},
				complete:function(data){
					//成功之后给设备、负责人赋值
					if(data.responseText == "true"){
						$.post(basePath + "ptw/ptwInfo/findPtwInfoByNo.do",{"attachWtNo":$("#f_attachWtNo").val()},function(pInfo){
							if(pInfo){
								params.attachWtId = pInfo.id;
																
								$("#baseInfoForm").iForm('setVal',{workOrderId:pInfo.workOrderId,workOrderNo:pInfo.workOrderNo,
									eqId:pInfo.eqId,eqNo:pInfo.eqNo,eqName:pInfo.eqName,licWpicNo:pInfo.licWpicNo,
									workPlace:pInfo.workPlace,preWorkStartTime:pInfo.preWorkStartTime,preWorkEndTime:pInfo.preWorkEndTime});
								$("#baseInfoForm").iForm("endEdit",["licWpicNo","workOrderNo"]);
								//不能再根据树来选择设备
								canSelectTree = false;
							}
						},"json");
					}
				}
			}},
			messages:{remote:"工作票编号不存在或工作票不处于可附加动火票的状态"}}
];

/**新建工作票时的基本信息展示*/
function initNewBaseInfo(){	
	//工作票类型修改
	var wtTypeIdField = ptwUtil.findFieldById(baseInfoField,"wtTypeId");
	wtTypeIdField.type = "combobox";
	wtTypeIdField.rules = {required:true};
	wtTypeIdField.options = {
		url : basePath+ "ptw/ptwType/queryPtwTypes.do?queryType="+param_queryWtType,
		remoteLoadOn : "init",
		onChange:function(value){
			if(isNewWt){
				$.post(basePath+ "ptw/ptwType/queryPtwTypeInfo.do?ptwTypeId="+value,{},function(data){
					param_config = data.ptwTypeDefine;
					ptwTypeCode = data.ptwType.typeCode;
					ptwTypeId = data.ptwType.id;
					isFireWt = param_config.isFireWt == 1 ? true : false;
					initBaseInfoByConfig();
					initSafeInfoByConfig(true);
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
				eqId:pInfo.eqId,eqNo:pInfo.eqNo,eqName:pInfo.eqName,licWpicNo:pInfo.licWpicNo,
				workPlace:pInfo.workPlace,preWorkStartTime:pInfo.preWorkStartTime,preWorkEndTime:pInfo.preWorkEndTime,woWorkTask:pInfo.woWorkTask});
			$("#baseInfoForm").iForm("endEdit",["licWpicNo","workOrderNo"]);
			
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
				$("#baseInfoForm").iForm("endEdit",["licWpicNo","workOrderNo","woWorkTask"]);
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
	}else if(param_config.isFireChkBox==1){//动火工作内容是否为checkbox形式
		hideFields.push('workContentFj');
		hideFields.push('workContent');
		showFields.push('workContentFire');
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
	
	if(param_config.hasPreTime == 0  || isStdWt){
		hideFields.push('preWorkStartTime');
		hideFields.push('preWorkEndTime');
	}else{
		showFields.push('preWorkStartTime');
		showFields.push('preWorkEndTime');
	}
	
		
	//新建标准票时，需要显示和隐藏一部分信息
	if(isStdWt){
		hideFields = hideFields.concat(['workOrderNo','licWpicNo']);
	}else{
		showFields = showFields.concat(['workOrderNo','licWpicNo']);
	}
	
	//如果是动火票
	if(param_config.isFireWt == 1 || isStdWt){
		hideFields = hideFields.concat(['licWorkClass','licWorkClassPeople','licWorkClassNum']);
	}else{
		showFields = showFields.concat(['licWorkClass','licWorkClassPeople','licWorkClassNum']);
	}
	
	//新建票时
	if(isNewWt || isCopyWt){
		hideFields = hideFields.concat(['wtNo','createUserNameAndTime','issuerAndTime','finWlAndTime']);
	}else{
		showFields = showFields.concat(['wtNo','createUserNameAndTime','issuerAndTime','finWlAndTime']);
	}
	//动火票时，隐藏标准票的按钮
	if(isPtwAdmin &&((isNewWt && !isFireWt) || isCopyWt)){
		showFields.push('isStdWt');
	}else{
		hideFields.push('isStdWt');
	}
	$("#baseInfoForm").iForm("hide",hideFields);
	$("#baseInfoForm").iForm("show",showFields);
		
	if(param_config.isFireChkBox==1){
		$("#fireBaseInfoForm").iForm("show",["fireWpExecNo","guardXmCoNo"]);
	}else{
		$("#fireBaseInfoForm").iForm("hide",["fireWpExecNo","guardXmCoNo"]);
	}
	
	
}