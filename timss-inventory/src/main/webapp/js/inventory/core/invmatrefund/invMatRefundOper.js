//提交退库信息
function commitMatRefund(){
	$("#btn_submit").button('loading');
	if(!$("#autoform").valid()){  //检查是否填了退库原因
		$("#btn_submit").button('reset');
		return ;
	}
	var formData =$("#autoform").ITC_Form("getdata");
	endEditAll(null);
	var listData =$("#matrefunddetail_grid").datagrid("getRows");
	var allRefundSum = 0;
	for(var i=0;i<listData.length;i++){  //检查退库数量是否合法
		var temp = listData[i];
		var refundqty = temp.refundqty;  //退库数量
		allRefundSum = allRefundSum+refundqty ;
		var refundableqty = temp.refundableqty;  //可退库数量
		if(refundqty > refundableqty || refundqty<0){
			FW.error("请输入合法的退库数量 ");
			startEditAll();
			$("#btn_submit").button('reset');
			return;
		}
	}
	if(allRefundSum == 0){
		FW.error("至少应包含一种退货物资");
		startEditAll();
		$("#btn_submit").button('reset');
		return;
	}
	startEditAll();
	//清掉可能出现中文或换行符且用不着的字段 -- begin by gchw
	for(var i =0;i<listData.length;i++){
		listData[i]["cusmodel"]="";
		listData[i]["itemname"]="";
	}
	//清掉可能出现中文或换行符且用不着的字段 -- end
	//加载用户表单数据
	$.ajax({
		type : "POST",
		async: false,
		url: basePath+"inventory/invmatrefund/commitMatRefund.do",
		data: {
			"formData":FW.stringify(formData),
			"listData":FW.stringify(listData),
			"imrsid":imrsid
			},
		dataType : "json",
		success : function(data) {
			saveFlag = true;
			taskId = data.taskId;
			imaid = data.imaid;
			if( data.result == "success" ){
				FW.success("提交成功 ");
				$("#btn_submit").hide();
				FW.deleteTabById(FW.getCurrentTabId());
			}else{
				FW.error("提交失败 ");
				$("#btn_submit").button('reset');
			}
			FW.deleteTabById(FW.getCurrentTabId());
		}
	});
}

//检查退库数量是否合法
function checkRefundNum(){
	var refundNum = $(this).val(); //退库数量
	 //可退库数量
	var refundableNum = $(this).parents(".datagrid-cell").parent().siblings("td[field=refundableqty]").children(0).html();
	if(parseFloat(refundNum)<0 || parseFloat(refundNum)>parseFloat(refundableNum) ){
		FW.error("请输入正确的退库数量！")
	}
	
}