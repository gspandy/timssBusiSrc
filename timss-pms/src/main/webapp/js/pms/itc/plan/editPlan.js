

/**
 * 提交修改年度计划信息到后台
 */
function submit(_this){
	if(!$("#form1").valid()){
		
		return ;
	}
	buttonLoading(_this);
	var data=$("#form1").iForm('getVal');
	$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
	$.post(basePath+'pms/plan/updatePlan.do',{"plan":FW.stringify(data)},function(result){
		showBasicMessageFromServer(result,saveSuccessMessage,saveFailMessage,{resetId:_this});
	});
}

/**
 * 删除年度计划
 */
function del(){
	var data={id:id};
	FW.confirm("确定删除吗？删除后数据不能恢复",function(){
		$.post(basePath+'pms/plan/deletePlan.do',data,function(result){
			showBasicMessageFromServer(result,delSuccessMessage,delFailMessage);
		});
	});
	
}
/**
 * 修改年度计划并暂存
 */
function tmpSave(){
	if(!$("#form1").valid()){
		buttionReset("#b-tmp-save");
		return ;
	}
	var data=$("#form1").iForm('getVal');
	$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
	$.post(basePath+'pms/plan/tmpUpdatePlan.do',{"plan":FW.stringify(data)},function(result){

		showBasicMessageFromServer(result,tmpSaveSuccessMessage,tmpSaveFailMessage);
	});
}

/**
*  将基本状态的年度计划data，转为查看状态
*/
/*function formDataToViewStatus(preFormData){
	var result=$.extend(true,{},preFormData);
	for(var i in result){
		result[i]['type']="label";
	}
	return result;
}*/

function addProject(){
	var planId=id;
	openTab('pms/project/addProjectJsp.do?planId='+planId,'立项',"pmsAddProjectTab",openMutiTabAndBackReflesh);
}

function initOther(opt){
	var data=opt.data;
	var formFields=opt.formFields;
	
	initProjectList(data);
	
	initAttachForm(data.data.attachMap,$('#attachForm'),$('#attachFormWrapper'),opt.readOnly);
	initProfit();
	initHistInfoForm(data.data);
}

//初始化历史信息表单
function initHistInfoForm(data){
	if(hasHistInfo(data)){
		$("#histInfoWrapper").iFold();
		$("#histInfo").iForm("init",{"fields":historyFormField,options:{validate:true,initAsReadonly:true}});
		$("#histInfo").iForm("setVal",data);
	}
}

//是否有历史数据判断
function hasHistInfo(data){
	return data && data.carryOverTimes;
}
//初始化年度计划的项目列表datagrid
function initProjectList(data){
	if(data && data.data && data.data.projectVos && data.data.projectVos.length){
		$('#projectListWrapper').show();
		if(dataGrid){
			return ;
		}
		
		$('#projectList').iFold();
		dataGrid=$("#projectTable").datagrid({
			fitColumns:true,
			scrollbarSize:0,
			singleSelect:true,
		
			onDblClickRow : function(rowIndex, rowData) {
				var id=rowData["id"];
				openTab(basePath+"pms/project/editProjectJsp.do?id="+rowData['id'],'立项','pmsViewProjectTab'+id,openTabBackReflesh);
			},
			data:data.data.projectVos,
			columns:[[		
			 			/* {field:'ck',checkbox:true}, */
			 			{field:'id',hidden:true},
			 			{field:'ptype',hidden:true},
			 			{
			 				field:'pyear',title:'年份',width:50,align:'left',fixed:true	
			 			},
			 			{
			 				field:'property',title:'性质',width:60,align:'left',fixed:true,
			 				formatter: function(value,row,index){
			 					var result="成本";
			 					if(value=='income'){
			 						return "收入";
			 					}
			 					return result;
			 				}
			 			},
			 			{
			 				field:'projectName',title:'名称',width:120,align:'left'
			 			},
			 			{
			 				field:'applyBudget',title:'项目金额(元)',width:105,align:'right',fixed:true
			 			},
			 			{
			 				field:'startTime',title:'计划开始日期',width:105,align:'left',fixed:true,
			 				formatter: function(value,row,index){
			 					//时间转date的string，还有long2date(value)方法
			 					return FW.long2date(value);
			 				}
			 			},
			 			{
			 				field:'endTime',title:'计划结束日期',width:105,align:'left',fixed:true,
			 				formatter: function(value,row,index){
			 					//时间转date的string，还有long2date(value)方法
			 					return FW.long2date(value);
			 				}
			 			},
			 			{
			 				field:'statusValue',title:'状态',width:85,align:'left',fixed:true
			 			}
			 		]]

	    });
	}else{
		//隐藏防止额外的间距出现
		$('#projectListWrapper').hide();
	}
	
	
}

//重新加载datagrid信息
function pmsReload(){
$.post(basePath+'pms/plan/queryPlanById.do',{id:id},function(data){
		
		if(data && data.flag=='success'){
			var opt=data;
				
			initProjectList(data);
			if(data && data.data && data.data.projectVos && data.data.projectVos.length){
				dataGrid.datagrid("loadData",data.data.projectVos);
			}
			
			
		}
	});
}

function changePlan(){
	if(!changePlan.init){
		$("#pms-b-plan-change").html("确认结转");
		changePlan.init=true;
		
		$("#form1").iForm('endEdit');
		$("#form1").iForm('beginEdit',['type','annualIncome','annualCost','planPercent','annualPercent']);
		initProfit();
		return ;
	}
	if(!$("#form1").valid()){
		return ;
	}
	var data=$("#form1").iForm('getVal');
	$.ajax({
		url:basePath+"pms/plan/changePlan.do",
		type:"POST",
		dataType:"json",
		data:{plan:FW.stringify(data),processInstId:processInstId},
		success:function(result){
			showBasicMessageFromServer(result,"结转成功","结转失败");
		}
	});
	
	
}

function cancelNotice(){
	$.ajax({
		url:basePath+"pms/plan/cancelNotice.do?",
		type:"GET",
		dataType:"json",
		data:{processInstId:processInstId},
		success:function(result){
			FW.success("成功取消待办提醒");
			closeTab();
		},
		error:function(result){
			FW.error("取消待办提醒失败");
		}
	});
}