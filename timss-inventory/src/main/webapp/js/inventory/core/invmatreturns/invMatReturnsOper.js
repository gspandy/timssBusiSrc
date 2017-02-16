//物资退货的提交（提取form表单信息，和datagrid数据信息，主要是退货原因和退货物资、数量）
function commitMatReturns(){
	saveFlag = false;
	if(!$("#autoform").valid()){
		return ;
	}
	var formData =$("#autoform").ITC_Form("getdata");
	endEditAll();
	var listData =$("#matreturnsdetail_grid").datagrid("getRows");
	for(var i=0;i<listData.length;i++){
		var temp = listData[i];
		var returnQty = temp.returnQty;  //退货数量
		var returnableqty = temp.returnableqty;  //可退货数量
		if(returnQty > returnableqty || returnQty<0){
			FW.error("请输入合法的退货数量 ");
			startEditAll();
			return;
		}
		
	}
	startEditAll();
	//清掉可能出现中文或换行符且用不着的字段 -- begin by gchw
	for(var i =0;i<listData.length;i++){
		listData[i]["cusmodel"]="";
		listData[i]["itemname"]="";
	}
	//清掉可能出现中文或换行符且用不着的字段 -- end
	if(!saveFlag){
		//加载用户表单数据
		$.ajax({
			type : "POST",
			async: false,
			url: basePath+"inventory/invmatreturns/saveMatReturns.do",
			data: {
				"formData":FW.stringify(formData),
				"listData":FW.stringify(listData),
				"imrsid":imrsid,
				"pruorderno":pruorderno
				},
			dataType : "json",
			success : function(data) {
				saveFlag = true;
				imrsid = data.imrsid;
				if (data.result == "success") {
					FW.success("保存成功 ");
					closeCurTab();
				}else{
					FW.error("保存失败 ");
				}
			},
			error : function(data) {
				FW.error(data.responseJSON.msg);
			}	
		});
	}else{
		startEditAll();
	}
}
