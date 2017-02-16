var IMRLoadingForm={
	init:function(){
		if(embed == '1'){	//FW.showRecipientInfo里用到
			IMRLoadingForm.window();
		}else{
			IMRLoadingForm.page();
		}
		initForm();
		$("#autoform").ITC_Form("readonly");
	},
	page:function(){
		form = [
				{title : "id信息", id : "imrid", type:"hidden"},
				{title : "申请单名称", id : "sheetname", type:"label"},
				{title : "申请单号", id : "sheetno",type:"label", type:"hidden"},
		    	{title : "领用部门", id : "applyDept", type : "label"},
				{title : "申请单号", id : "applySheetNo",type:"label",
					formatter:function(val){
						var text = "<a onclick=openInvMatApply('"+ imaid + "')>" + val + "</a>";
		 				return text;
		 			}	
				},
		    	{title : "领料单类型", id : "applyType", type : "label"},
		    	{title : "发料状态", id : "status", type : "label",
		    		formatter:function(value,row){
						if("Y"==value){
							return "已发料";
						}
						else{
							return "未发料";
						}
					}
		    	},
		    	{title : "发料时间", id : "deliveryDate", type : "label",
		    		formatter:function(value,row){
						return FW.long2time(value);
					}	
		    	},
		    	{title : "工单编号", id : "outterNo", type : "label"},
		    	{title : "备注", id : "remark", type : "textarea", linebreak:true, wrapXsWidth:12, wrapMdWidth:8},
		    	{title : "用途说明", id : "applyUse", type : "textarea", linebreak:true, wrapXsWidth:12, wrapMdWidth:8},
		    	{title : "imaid信息", id : "imaid", type:"hidden"}
		    ];
	},
	window:function(){
		form = [
				{title : "id信息", id : "imrid",type:"hidden"},
				{title : "申请单名称", id : "sheetname", type:"label",labelFixWidth:110},
				{title : "申请单号", id : "sheetno",type:"label",labelFixWidth:110},
		    	{title : "领料单类型", id : "applyType",type : "label",labelFixWidth:110},
		    	{title : "工单编号", id : "outterNo",type : "label",labelFixWidth:110},
		    	{title : "备注", id : "remark",type : "label",labelFixWidth:110},
		    	{title : "imaid信息", id : "imaid",type:"hidden"}
			];
	}
};
	
//编辑表单加载数据（通用方法）
function initForm(){
	$("#autoform").iForm("init",{"fields":form,"options":{validate:true,labelFixWidth:160,xsWidth:4}});
	//加载用户表单数据
	$.ajax({
		type : "POST",
		async:false,
		url: basePath+"inventory/invmatrecipients/queryInvMatRecipientsForm.do",
		data: {"imrid":imrid},
		dataType : "json",
		success : function(data) {
			if("" != imrid){
				var	loaddata = {
						"imrid":imrid,
						"sheetname" : data.sheetname,
						"sheetno" : data.sheetno, 
						"applySheetNo" : data.applySheetNo, 
						"applyDept" : data.applyDept, 
						"applyType" : data.applyType,
						"status" : data.status,
						"deliveryDate" : data.deliveryDate,
						"outterNo" : data.outterNo,
						"remark" : data.remark, 
						"applyUse" : data.applyUse, 
						"imaid" : data.imaid
					};
				$("#autoform").iForm("setVal",loaddata);
				deliveryDate = data.deliveryDate;
				if("Y" != data.status){
					$("#btn_borrow").hide();
				}
			}
		}
	});
}

function openInvMatApply(imadid){
	var currTabId = FW.getCurrentTabId();
	var url = basePath+ "inventory/invmatapply/invMatApplyForm.do?imaid="+imaid;
	var prefix = imaid;
    FW.addTabWithTree({
        id : "editMatApplyForm" + prefix,
        url : url,
        name : "物资领料",
        tabOpt : {
            closeable : true,
            afterClose : "FW.deleteTab('$arg');FW.activeTabById('" + currTabId + "');" //回到对应的发料单界面
        }
    });
}

//点击领用或归还按钮，弹窗出现	
function openBorrowWindow() {
	var selectedRow = $("#matrecdetail_grid").datagrid("getSelected");
	if(!selectedRow){
		FW.error("请先选择一行发料明细");
		return;
	}
	
	var btnOpts = [{
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
		"onclick" : function(){
			//捕获表单里的信息,并转为String型
			//子窗口
			var conWin = _parent().window.document.getElementById("itcDlgContent").contentWindow;
			var borrowData = conWin.$("#borrowForm").iForm('getVal');
			if(!borrowData.assetId){
				FW.error("资产ID为空，请重新输入资产编码");
				return false;
			}
			borrowData.imrdid = selectedRow.imrdid;
			if(!conWin.$("#borrowForm").valid()){
				//FW.error("提交的内容有错误的地方，请修改后重试");
				return false;
			}
			//放入assetId
			var borrowDataStr = JSON.stringify(borrowData);  //取表单值
			
			$.ajax({
				type : "POST",
				data: {
					borrowData:borrowDataStr,
					outQty: selectedRow.outstockqty
				},
				url: basePath + '/astBorrowRecord/insertBorrowRecord.do',
				success: function(data){
					if(data.result=="success"){
						FW.success("提交成功");
						_parent().$("#itcDlg").dialog("close");
					}
					else{
						FW.error(data.msg);
					}
				},
				error:function(XMLHttpRequest, textStatus, errorThrown){
					FW.error(XMLHttpRequest.responseJSON.msg);
				}
			});
		}
	}];
	//新建系统配置对话框
	var dlgOpts = {
		width : 400,
		height : 240,
		closed : false,
		title : "登记资产领用",
		modal : true
	};
	//初始化弹出框
	FW.dialog("init", {
		"src" : basePath+"page/inventory/core/invmatrecipients/invMatRecipientsBorrow.jsp?imaCreateUserId=" + imaCreateUserId 
				+ "&imaCreateUserName=" + encodeURIComponent(imaCreateUserName)
				+ "&imrdItemId=" + selectedRow.itemid
				+ "&deliveryDate=" + deliveryDate,
		"dlgOpts" : dlgOpts,
		"btnOpts" : btnOpts
	});
}
