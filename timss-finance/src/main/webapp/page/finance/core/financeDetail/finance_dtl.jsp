<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<title>费用明细</title>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />

<!-- 私有部分 -->
<script type="text/javascript" src="${basePath}js/finance/common/financeUtilJS.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/finance/core/financeBaseField.js?ver=${iVersion}"></script>

<script type="text/javascript">
	//_dialogEmmbed=true; //设置combobox的下拉部分跨过对话框
	var finPageConfData = <%=request.getParameter("finPageConfData")%>;
	var finTypeEn = <%=request.getParameter("finTypeEn")%>;
	var finNameEn = <%=request.getParameter("finNameEn")%>;
</script>
<script type="text/javascript">
	var edit = <%=request.getParameter("edit")%>; //获取编辑模式
	var readOnly = <%=request.getParameter("readOnly")%>; //获取只读模式
	var finStatus = '<%=request.getParameter("finStatus")%>';
	var defaultAllowanceType = '<%=request.getParameter("defaultAllowanceType")%>';
	
	if( edit=="edit" ) {
		var rowData = FW.get("rowData");
	} 
	var allowanceType = FW.get("allowanceType");
	var dtlFormFields = [];  //表单定义
	
	$(document).ready(function() {
		
		//动态添加form的字段
		for(var i=0 ; i<finPageConfData.length;i++){
			if(finTypeEn == finPageConfData[i].reimburseType){ 
				var detailFormFields = finPageConfData[i].dgDetailConf;
				appendDetailFormFields(dtlFormFields,detailFormFields,dgFormBaseFields);
				break;
			}
		}
		showDtlForm(); //显示明细表单
		pri_loadDtlData(); //加载数据到明细页面
	});
	
	function appendDetailFormFields(dtlFormFields,addFormField,dgFormBaseFields){
		//动态添加字段到form表单中
		var temp = addFormField.split(",");
		for(var i=0; i<temp.length;i++){
			var tempFieldid = temp[i].trim();
			for(var j=0 ;j<dgFormBaseFields.length; j++){
				var baseFieldObj = dgFormBaseFields[j];
				if(baseFieldObj.id == tempFieldid){
					dtlFormFields.push(baseFieldObj);
				}
			}
		}
	}
	//显示明细表单
	function showDtlForm() {
		//$("#autoformsingle").iForm("init", {"options": {validate: true}, "fields": pri_dtlFormFields});
		$("#autoformsingle").iForm("init", {"options": {validate: true}, "fields": dtlFormFields});
		$("#autoformsingle").iForm( "setFullVal", {"oo_allowance_type":defaultAllowanceType} );
		if( readOnly == 1 ) {
			$("#autoformsingle").iForm("endEdit");
		}else if(finStatus=="applicant_modify"){
			$("#autoformsingle").iForm("endEdit",["oo_businessentertainment","oo_carcost","oo_officecost",
			"oo_welfarism","oo_stay_days","oo_allowance_days","oo_allowance_type","oo_other_days",
			"oo_meetingcost","oo_ticketcost","oo_staycost","oo_citytrafficcost","oo_fuelcost",
			"oo_longbuscost","oo_bridgecost","oo_huochecost","oo_incidentalcost","oo_traincost",
			"oo_othertrafficcost","oo_beneficiary","oo_department"]);
		}else if(finNameEn == "travelcost" && (finStatus=="wait_jydeptmgr_approve" ||finStatus== "wait_submainmgr_approve")){
			$("#autoformsingle").iForm("endEdit");
			$("#autoformsingle").iForm("beginEdit",["oo_allowance_type"]);
		}
	}
	//加载数据到明细页面
function pri_loadDtlData() {
	//如果为编辑模式,则加载数据到对话框中
	if(edit=="edit") {
		var pri_dtlData = {
			"oo_beneficiary" : rowData.beneficiary,   //报销人
			"oo_beneficiaryid" : rowData.beneficiaryid,  //报销人ID
			"oo_department" : rowData.department,  //部门
			"oo_amount" : rowData.amount, //
			"oo_doc_nbr" : rowData.doc_nbr,  //单据张数
			"oo_remark" : rowData.remark,  //备注
		//	"oo_spremark" : rowData.spremark,  //特殊说明
			"oo_businessentertainment" : rowData.businessentertainment,  //业务招待费
			"oo_strdate": rowData.strdate ,  //开始日期
			"oo_enddate":rowData.enddate ,  //结束日期
			"oo_description": rowData.description,   //报销事由
			"oo_meetingcost": rowData.meetingcost,   //会议费
			"oo_staycost": rowData.staycost ,   //住宿费
			"oo_traincost": rowData.traincost,  //培训费
			"oo_join_boss":rowData.join_boss,
			"oo_join_bossid":rowData.join_bossid,
			"oo_join_nbr": rowData.join_nbr,
			"oo_description": rowData.description,  //报销事由
			
			"oo_address":rowData.address ,//起止地址
			"oo_allowance_type": rowData.allowanceType,  //补贴类型
			"oo_allowance_days":  rowData.allowance_days ,// 补贴天数
			"oo_other_days":rowData.other_days,//其他天数
			"oo_stay_days":rowData.stay_days,//住宿天数
			"oo_staycost": rowData.staycost,//住宿费(元)
			"oo_bridgecost":rowData.bridgecost,//路桥费(元)
			"oo_citytrafficcost":rowData.citytrafficcost, //市内交通费(元)
			"oo_fuelcost":rowData.fuelcost,//油费(元)
			"oo_huochecost": rowData.huochecost,//火车费(元)
			"oo_incidentalcost": rowData.incidentalcost,//杂费(元)
			"oo_longbuscost": rowData.longbuscost  ,//长途汽车费(元)
			"oo_othertrafficcost": rowData.othertrafficcost,//其他交通费(元)
			"oo_ticketcost": rowData.ticketcost  ,//机票费(元) 
			"oo_carcost" : rowData.carcost, //汽车费
			"oo_officecost" : rowData.officecost,  //办公费用
			"oo_welfarism" : rowData.welfarism, //福利费
			"oo_pettycash" : rowData.pettycash  //备用金
			
		};
			
		$("#autoformsingle").iForm( "setFullVal", pri_dtlData );
	}
}
	
	function validateForm(){
	    if(!$("#autoformsingle").valid()){
	        return false;
	    }
	    return true;
	}
</script>
</head>

<body>
	<div>
		<div style="width:100%;margin-top: 9px;">
			<form id="autoformsingle"></form>
		</div>
	</div>
</body>
</html>
