/** 动态添加datagrid列
 * @param datagrid_columns 表格原有的列
 * @param addDatagridField  需要添加的列field
 * @param datagridBaseColumnsObj  列的基础库
 */
function appendDatagridFields(datagrid_columns,addDatagridField,datagridBaseColumnsObj){
	//动态添加字段到datagrid中
//	var addDatagridField = "department,doc_nbr";
	var temp = addDatagridField.split(",");
	var datagridBaseColumnsObj = datagridBaseColumns[0];
	for(var i=0; i<temp.length;i++){
		var tempFieldName = temp[i].trim();
		for(var j=0 ;j<datagridBaseColumnsObj.length; j++){
			var baseFieldObj = datagridBaseColumnsObj[j];
			if(baseFieldObj.field == tempFieldName){
				datagrid_columns[0].push(baseFieldObj);
			}
		}
	}
}
//显示表格
function createdg() {
	 datagrid_columns = [[ ]];
	//动态添加datagrid的列
	for(var i=0 ; i<finPageConfData.length;i++){
		if(finTypeEn == finPageConfData[i].reimburseType){ 
			var datagridFields = finPageConfData[i].datagridConf;
			appendDatagridFields(datagrid_columns,datagridFields,datagridBaseColumns);
			break;
		}
	}
	
	$("#finTable").datagrid({
		url: basePath + "finance/financeInfoController/viewFinanceDetail.do?fid=" + fid,
		resizeHandle : 'both',
		columns : datagrid_columns,
		fitColumns : true,
		singleSelect : true,
		collapsible : true,
		scrollbarSize : 0,
		onDblClickRow : function(rowIndex, rowData) {
			if( editDatagrid == "true" ) {
				showDtlIframe('edit', rowIndex, rowData); //可编辑
			} else {
				showDtlIframe('readOnly', rowIndex, rowData); //只读
			}
		},
		onClickCell : function(rowIndex, field, value) {
			if (field == 'delete') {
				deleteThisRow(rowIndex, field, value);
			}
		}
	});
}


//显示明细对话框(mode:编辑模式,rowIndex:选中行索引,rowData:选中行数据)
function showDtlIframe(mode, rowIndex, rowData) {
	FW.set("allowanceType",FW.getEnumMap("FIN_ALLOWANCE_TYPE"));
	var dilogheight = getDilogHeight();
	var src = basePath + "page/finance/core/financeDetail/finance_dtl.jsp?finTypeEn="+JSON.stringify(finTypeEn)
				+"&finNameEn="+JSON.stringify(finNameEn)+"&finStatus="+bussinessStatus+ "&finPageConfData=" + JSON.stringify(finPageConfData);
	if(rowData && rowData.allowanceType){
		src = src +"&defaultAllowanceType="+rowData.allowanceType;
	}else if(allowanceType){
		src = src +"&defaultAllowanceType="+allowanceType;
	}
	var btnOpts = null;
	
	//报销明细对话框
	var pri_dlgOpts = {
		width : 720,  // 720 680
		height : dilogheight,  // 240 380 500
		closed : false,
		title : "报销明细",
		modal : true
	};
	if( mode != "readOnly" ) {
		if (mode == "add") {
			src = src + "&edit="+ encodeURI(JSON.stringify("add")) + "&readOnly=0";
		} else if (mode == "edit") {
			FW.set("rowData", rowData); //设置全局变量,使其在明细页面获取
			src = src + "&edit="+ encodeURI(JSON.stringify("edit")) + "&readOnly=0";
		}
		
		btnOpts = [{
			"name" : "取消",
			"float" : "right",
			"style" : "btn-default",
			"onclick" : function() {
				return true;
			}
		},{
			"name" : "确定",
			"float" : "right",
			"style" : "btn-success",
			"onclick" : function() {
				return addDtlSubBtn(mode, rowIndex); //添加明细,点击确定按钮
			}
		} ];
	} else { //只读
		FW.set("rowData", rowData); //设置全局变量,使其在明细页面获取
		src = src + "&edit="+ encodeURI(JSON.stringify("edit")) + "&readOnly=1";
		btnOpts = [{
			"name" : "关闭",
			"float" : "right",
			"style" : "btn-default",
			"onclick" : function() {
				return true;
			}
		}];
	}
	
	FW.dialog("init", {
		"src" : src,
		"dlgOpts" : pri_dlgOpts,
		"btnOpts" : btnOpts
	});
	
}
/**
 * 获取配置的明细弹出窗的高度
 */
function getDilogHeight(){
	var dilogheight = 500 ;
	//根据finTypeEn去查找高度配置
	for(var i=0 ; i<finPageConfData.length;i++){
		if(finTypeEn == finPageConfData[i].reimburseType){ 
			dilogheight = finPageConfData[i].dilogHeight;
			break;
		}
	}
	return dilogheight;
}
//添加明细,点击确定按钮
function addDtlSubBtn(mode, rowIndex) {
	var conWin = _parent().window.document.getElementById("itcDlgContent").contentWindow;
	
	if (conWin.validateForm() == false) {
		return false;
	}
	
	//取值(一条明细对应的form表单的值)
	var detailFormVal = conWin.$("#autoformsingle").iForm("getVal");
	
	var appendDataJson = buildAppendData(mode, rowIndex,detailFormVal); //组件添加到datagrid中的数据格式
	if(appendDataJson == false){
		return false;
	}
	var ooamount = appendDataJson.ooamount;
	
	if( mode == "add" ) {
		$('#finTable').datagrid('appendRow',appendDataJson );
	} else if( mode == "edit" ) {
		$('#finTable').datagrid('updateRow', {
			index : rowIndex,
			row : appendDataJson
		});
	}
	//添加明细点击确定调用公共方法
	clkAddDtlSubBtnComm();
	
	return true;
}

function getAllowanceTypeEnum(enum_ecat,enum_code){
	var result = 0 ;
	var enumObjArray = _enum[enum_ecat];
	for(var i=0; i<enumObjArray.length; i++){
		if(enum_code == enumObjArray[i].code){
			result = enumObjArray[i].attribute1
		}
	}
	return parseFloat(result);
}

function buildAppendData(mode, rowIndex,formData){

	var oodescription = formData.oo_description; //报销事由
	var oojoin_nbr = formData.oo_join_nbr; //参与人数
	var oojoin_boss = formData.oo_join_boss; //参与领导
	var oojoin_bossid = formData.oo_join_bossid; //参与领导编号
	var oodoc_nbr = formData.oo_doc_nbr; //单据张数
	var ooremark = formData.oo_remark; //备注
//	var oospremark = formData.oo_spremark; //特殊说明
	var oobusinessentertainment = formData.oo_businessentertainment; //业务招待费
	var oobeneficiary = formData.oo_beneficiary; //报销人
	var oobeneficiaryid = formData.oo_beneficiaryid; // 报销人ID
	var oodepartment = formData.oo_department; //部门
	
	var  oodescription= formData.oo_description; //报销事由
	var  oostrdate= formData.oo_strdate; //开始日期
	var  ooenddate= formData.oo_enddate; //结束日期
	var  ooaddress= formData.oo_address; //起止地址
	var  ooallowance_days= formData.oo_allowance_days;// 补贴天数
	var  ooallowance_type = formData.oo_allowance_type;  //补贴类型
	var  ooother_days= formData.oo_other_days; //其他天数
	var  oostay_days= formData.oo_stay_days; //住宿天数
	var  oostaycost= formData.oo_staycost; //住宿费(元)
	var  oobridgecost= formData.oo_bridgecost; //路桥费(元)
	var  oocitytrafficcost= formData.oo_citytrafficcost; //市内交通费(元)
	var  oofuelcost= formData.oo_fuelcost; //油费(元)
	var  oohuochecost= formData.oo_huochecost; //火车费(元)
	var  ooincidentalcost= formData.oo_incidentalcost; //杂费(元)
	var  oolongbuscost= formData.oo_longbuscost; //长途汽车费(元)
	var  ooothertrafficcost= formData.oo_othertrafficcost; //其他交通费(元)
	var  ooticketcost= formData.oo_ticketcost;//机票费(元)
	var oocarcost = formData.oo_carcost; //汽车费
	var ooofficecost = formData.oo_officecost;//办公费用
	var oowelfarism = formData.oo_welfarism; //福利费
	var oomeetingcost = formData.oo_meetingcost; //会议费 
	var ootraincost = formData.oo_traincost; //培训费
	var oopettycash = formData.oo_pettycash; //备用金
	
	ooticketcost = costToDouble(ooticketcost);
	oostaycost = costToDouble(oostaycost);
	oocitytrafficcost = costToDouble(oocitytrafficcost);
	oofuelcost = costToDouble(oofuelcost);
	oolongbuscost = costToDouble(oolongbuscost);
	oobridgecost = costToDouble(oobridgecost);
	oohuochecost = costToDouble(oohuochecost);
	ooincidentalcost = costToDouble(ooincidentalcost);
	ooothertrafficcost = costToDouble(ooothertrafficcost);
	ooallowance_days = costToDouble(ooallowance_days);
	oomeetingcost=costToDouble(oomeetingcost);
	ootraincost = costToDouble(ootraincost);
	oopettycash = costToDouble(oopettycash);
	var ootrafficcost; //交通费
	var ooallowancecost; //补贴费
	var ooallowancecostPerDay ; //补贴标准(元/天)

	//汇总五类金额
	//住宿费=住宿费
	//杂费=杂费
	ootrafficcost = oocitytrafficcost + oolongbuscost + oohuochecost + ooothertrafficcost; //交通费=市内交通费+长途汽车费+火车费+机票+其他交通费
	ootrafficcost = parseFloat(ootrafficcost.toFixed(2));
	//TODO 根据补贴类型，修改补贴标准计算出的补贴费
	ooallowancecost = ooallowance_days * allowancePerDay; //补贴费=出差补贴天数*每天出差补贴
//	ooallowancecostPerDay = allowancePerDay;
//	if(ooallowance_type == "businessTravel"){
		var realAllowancePerDay = getAllowanceTypeEnum("FIN_ALLOWANCE_TYPE",ooallowance_type);
		ooallowancecost = ooallowance_days * realAllowancePerDay; //商务补贴费=出差补贴天数*每天出差补贴
		ooallowancecostPerDay = realAllowancePerDay;
		if(ooallowance_days == 0){  //如果补贴天数为0，则清空补贴类型（即使选了补贴类型）
			ooallowancecostPerDay = 0;
			ooallowance_type = "";
		}
		
//	}
	ooallowancecost = parseFloat(ooallowancecost.toFixed(2));
	
	var ooamount = 0 ;
	
	if(finNameEn ==finFlowKeyConstant.BUSINESS ){
		//每条明细的总金额
		ooamount = parseFloat(oobusinessentertainment);
		ooamount = parseFloat(ooamount.toFixed(2));
	}else if(finNameEn ==finFlowKeyConstant.TRAVEL ){
		oocarcost = oofuelcost + oobridgecost; //车辆费=油费+路桥费
		oocarcost = parseFloat(oocarcost.toFixed(2));
		// 可能要除掉，对于完全无效的报销明细进行过滤
		if(ooallowance_days!=null&&ooallowance_days!=""&&
				(ooallowance_type==null||ooallowance_type=="null"||ooallowance_type=="")){
			FW.error("请选择补贴标准");
			return false;
		}
		if(0==ooallowance_days&&0==ooticketcost&&0==oostaycost&&
				0==oocitytrafficcost&&0==oofuelcost&&0==oolongbuscost&&0==oobridgecost&&
				0==oohuochecost&&0==ooincidentalcost&&0==ooothertrafficcost
		){
			FW.error("请至少填写补贴天数、各种费中一项");
			return false;
		}
		//每条明细的总金额
		ooamount = ooticketcost + oostaycost + oocitytrafficcost + oofuelcost + oolongbuscost
			+ oobridgecost + oohuochecost + ooincidentalcost + ooothertrafficcost + ooallowancecost;
		ooamount = parseFloat(ooamount.toFixed(2));
	}else if(finNameEn == finFlowKeyConstant.CAR){
		oocarcost = costToDouble(oocarcost);
		ooamount = oocarcost;
	}else if(finNameEn == finFlowKeyConstant.OFFICE){
		ooofficecost = costToDouble(ooofficecost);
		ooamount = ooofficecost;
	}else if(finNameEn == finFlowKeyConstant.WELFARISM){//福利费报销
		oowelfarism = costToDouble(oowelfarism);
		ooamount = oowelfarism;
	}else if(finNameEn == finFlowKeyConstant.MEETING){//会议费报销
		if(0==oomeetingcost&&0==ooallowance_days&&0==oostaycost&&
				0==ooincidentalcost&&0==ooothertrafficcost){
			Notice.errorTopNotice("请至少填写补贴天数、各种费中一项");
			return false;
		}
		// 根据补贴类型，修改补贴标准计算出的补贴费
		if(ooallowance_type && ooallowance_type == "otherTravel"){
			ooallowancecost = ooallowance_days * costToDouble(allowancePerDay); //补贴费=出差补贴天数*每天出差补贴
			ooallowancecostPerDay = allowancePerDay;
		}else if(ooallowance_type && ooallowance_type == "businessTravel"){
			ooallowancecost = ooallowance_days * costToDouble(businessAllowancePerDay);  //补贴费=出差补贴天数*每天出差补贴
			ooallowancecostPerDay = businessAllowancePerDay;
		}
		ooamount = oostaycost+ooincidentalcost+ooothertrafficcost+oomeetingcost+ooallowancecost;

	}else if(finNameEn == finFlowKeyConstant.TRAIN){//培训费报销
		if(0==ooallowance_days&&0==ooticketcost&&0==oostaycost&&
				0==oocitytrafficcost&&0==oofuelcost&&
				0==oolongbuscost&&0==oobridgecost&&
				0==oohuochecost&&0==ooincidentalcost&&
				0==ootraincost&&0==ooothertrafficcost
		){
			Notice.errorTopNotice("请至少填写补贴天数、各种费中一项");
			return false;
		}
		oocarcost = oofuelcost + oobridgecost; //车辆费=油费+路桥费
		oocarcost = parseFloat(oocarcost.toFixed(2));
		
		ooallowancecost = ooallowance_days * trainAllowancePerDay; //补贴费=出差补贴天数*每天出差补贴
		
		ooallowancecost = parseFloat(ooallowancecost.toFixed(2));
		//每条明细的总金额
		ooamount = ooticketcost + oostaycost + oocitytrafficcost + oofuelcost + oolongbuscost
			+ oobridgecost + oohuochecost + ooincidentalcost + ootraincost + ooothertrafficcost + ooallowancecost;
		ooamount = parseFloat(ooamount.toFixed(2));
	}else if(finNameEn == finFlowKeyConstant.PETTY){//备用金报销
		ooamount = parseFloat(oopettycash);
		ooamount = parseFloat(ooamount.toFixed(2));
	}else if(finNameEn == finFlowKeyConstant.HOMETRIP){//探亲路费报销
		 
	}else if(finNameEn == finFlowKeyConstant.MEDICAL){//补充医疗保险报销
		 
	}else if(finNameEn == finFlowKeyConstant.FAMILYMEDICINE){//家属医药费报销
		 
	}else if(finNameEn == finFlowKeyConstant.ADMINISTRATIVE){//行政报销
		 
	}
	
	//检验总金额是否超出精度
	if( !chkTtlAmtLen2(mode, rowIndex, ooamount) ) {
		return false;
	}
	var result = {};  
	result.amount = ooamount;
	result.description = oodescription;
	result.join_nbr = oojoin_nbr;
	result.join_boss = oojoin_boss;
	result.join_bossid = oojoin_bossid;
	result.doc_nbr = oodoc_nbr;
	result.remark = ooremark;
	//result.spremark = oospremark;
	result.businessentertainment = oobusinessentertainment;
	result.beneficiary = oobeneficiary;
	result.beneficiaryid = oobeneficiaryid;
	result.department = oodepartment;
	result.officecost = ooofficecost;

	result.strdate = oostrdate;
	result.enddate = ooenddate;
	result.address = ooaddress;
	result.allowance_days = ooallowance_days;
	result.other_days = ooother_days;
	result.stay_days = oostay_days;
	result.ticketcost = ooticketcost;
	result.staycost = oostaycost;
	result.citytrafficcost= oocitytrafficcost;
	result.fuelcost= oofuelcost;
	result.longbuscost= oolongbuscost;
	result.bridgecost= oobridgecost;
	result.huochecost= oohuochecost;
	result.incidentalcost= ooincidentalcost;
	result.othertrafficcost= ooothertrafficcost;
	result.trafficcost= ootrafficcost;
	result.carcost= oocarcost;
	result.allowanceType = ooallowance_type;
	result.allowancecostPerDay = ooallowancecostPerDay;
	result.allowancecost= ooallowancecost;	
	result.welfarism = oowelfarism;
	result.meetingcost = oomeetingcost;
	result.traincost = ootraincost;
	result.pettycash = oopettycash;
	return result;
}












/**
 * @param cost 将cost转换成两位小数的数字
 * @returns
 */
function costToDouble(cost){
	if( cost != undefined && trim(cost) != '' ) {
		cost = parseFloat(cost);
		cost = parseFloat(cost.toFixed(2));
	} else {
		cost = 0.0;
	}
	return cost;
}


//检验总金额是否超出精度
function chkTtlAmtLen2(mode, rowIndex, newAmt) {
	var oottlamount=0;
	var rows = $('#finTable').datagrid('getRows'); //获取记录行数
	for ( var i = 0; i < rows.length; i++) {
		//忽略
		if( mode == "edit" ) {
			var selIndex = $('#finTable').datagrid('getRowIndex', ($('#finTable').datagrid('getRows'))[i]);
			if(rowIndex == selIndex) {
				continue;
			}
		}
		oottlamount += parseFloat(rows[i]['amount']);
	}
	oottlamount += parseFloat(newAmt);
	oottlamount = parseFloat(oottlamount.toFixed(2));

	if( oottlamount > 9999999.99 ) {
		Notice.errorTopNotice("总金额(元)为" + oottlamount + ", 不能超过9999999.99");
		return false;
	}
	return true;
}
