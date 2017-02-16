var InvStocktakingPriv={
	init:function(){
		InvStocktakingPriv.set();
		InvStocktakingPriv.apply();
	},
	set:function(){//定义权限
		//保存
		Priv.map("privMapping.storecheck_save","storecheck_save");
		//编辑
		Priv.map("privMapping.storecheck_edit","storecheck_edit");
		//打印
		Priv.map("privMapping.storecheck_export","storecheck_export");
		//提交
		Priv.map("privMapping.storecheck_commit","storecheck_commit");
		//新建
		Priv.map("privMapping.storecheck_new","storecheck_new");
		//删除
		Priv.map("privMapping.storecheck_delete","storecheck_delete");
	},
	apply:function(){//应用权限
		//应用
		Priv.apply();
	}
};

//设置流程状态
function setProcessStatus(){
	if("" == process || "draft" == process){
		processStatus = "first";//首环节
		if( "draft" == process && "" != processInstId){
			processStatus = "first_save";//首环节暂存
		}else if("" != processInstId || status == 'approval_complete'){
			processStatus = "over";//流程结束
		}
	}else if(process == "fill_pan"){
		processStatus = "last";//流程最后环节
	}else{
		processStatus = "process";//流程中
	}
}

//页面初始化的时候做
var initPageProcess = {
	init : function() {
		if(!switchFlag){
			$("#btn_processinfo").hide();
		}
		
		if(processStatus == "over"){
			initPageProcess.read();
			initList();
			uploadform();
			$("#btn_edit").hide();
			saveFlag = true;
		}else{
			queryWarehouse();		
			if(null == istid || "" == istid){
				initPageProcess.news();
				$("#btn_submit").hide();
				$("#btn_edit").hide();
				$("#btn_add").hide();
				$("#btn_export").hide();
				$("#btn_save").text("保存到草稿");
			}else{
				initPageProcess.read();
				initList();
				uploadform();
				var ids = $("#uploadform").ITC_Form("getdata");
				uploadIds=ids.uploadField;
				if("" != uploadIds || "" == istid || null == istid || processStatus == "first" || processStatus == "first_save"){
					$("#uploadfileTitle").iFold("init");
				}else{
					$("#uploadfileTitle").iFold("hide");
				}
				//只有在首环节的时候才可以上传附件
				if(processStatus != "first" && processStatus != "first_save"){
					$("#uploadform").iForm('endEdit');
				}
			}
		}
	},
	read : function() {
		$("#pageTitle").html("库存盘点详情");
		initForm(edit_form);
		$("#autoform").ITC_Form("readonly");
		
		$("#btn_save").hide();
		$("#btn_delete").hide();
		$("#btn_submit").hide();
		$("#btn_add").hide();
		if("editable"!=isEdit){
			$("#btn_edit").hide();
			$('#matapplydetail_grid').datagrid('hideColumn', "del");
		}
	},
	edit : function() {
		$("#btn_edit").hide();
		$("#btn_submit").show();
		if(processStatus != "last"){
			if(processStatus == "first" || processStatus == "first_save"){
				$("#pageTitle").html("库存盘点详情");
				if('' != status){
					$("#btn_save").show();
				}
				$("#btn_add").show();
				$("#btn_delete").show();
				if(classType == "Draft"){
					$("#btn_delete").html("删除");
				}
			}else{
				$("#pageTitle").html("库存盘点详情");
			}
			startEditAll();
			var listData =$("#stocktakingdetail_grid").datagrid("getRows");
			if(listData.length > 0){
				$("#btn_add").text("继续添加物资");
			}else{
				$("#btn_add").text("添加物资");
			}
		}else{
			$("#pageTitle").html("库存盘点详情");
		}
	},
	news : function() {
		$("#pageTitle").html("新建库存盘点");
		initForm(new_form);
		$("#btn_edit").hide();
		$("#btn_delete").hide();
	}
};


//默认提交归档
function autoCommitProcess(){
    var data={};
    data['taskId'] = taskId;
    data['message'] = "审批归档。";
    var url = 'workflow/process_inst/autoComplete.do';
    $.post(url, data, function(data){
        if(data.result=='ok'){
			closeCurTab();
        }
    });
}

//提交后回调执行
function submitCallBack(){
	closeCurTab();
}

function cancelCallBack(){
	//由于取消按钮没有任何操作，这里给一个空的方法
}