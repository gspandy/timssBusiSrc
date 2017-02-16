//保存页面信息
function saveMatTran(){
	flagCounter = 0;
	if(!$("#autoform").valid()){
		return ;
	}
	
	validateRevice();
	if(flagCounter>0){
		startEditAll();
		return ;
	}
	
	var formData =$("#autoform").ITC_Form("getdata");
	var listData =$("#mattrandetail_grid").datagrid("getRows");
	for(var i=listData.length-1; i>=0;i--){
		if(listData[i].bestockqty == 0){ //数量为0的物资，不需要入库，不需要传到后台
			listData.splice(i,1);
		}
	}
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
		url: basePath+"inventory/invmattran/saveMatTran.do", 
		data: {
			"formData":FW.stringify(formData),
			"listData":FW.stringify(listData)
			},
		dataType : "json",
		success : function(data) {
			saveFlag = true;
			imtid = data.newImtid;
			var returnData = {"imtid":data.newImtid,"sheetno" : data.sheetno};
			$("#autoform").iForm("setVal",returnData);
			$("#pageTitle").html("物资接收单详情");
			
			if( data.result == "success" ){
				var status = null;
				if(checkAllInStock("save")){//已经完成出库
					deleteMatTranDB();
					type = 'over';
				}
				//改变采购申请单状态
				updatePurApplyStatus();
				dynaCalcTotalPrice();
				//添加站点开关
				if(siteid == "ITC"){
					FW.confirm("是否有物资需要资产化？",{
						onConfirm : function(){
							$("#btn_asset").show();
							$("#btn_save").hide();
							$("#btn_print").show();
							$("#btn_preview").hide();
							$("#btn-add").hide();
							//$("#autoform").ITC_Form("readonly");
							//FW.fixToolbar("#toolbar");
							//FW.getFrame(FW.getCurrentTabId()).location.reload();
							FW.navigate(basePath+"inventory/invmattran/invMatTranForm.do?imtid="+imtid+"&openType=read");

							//$("#mattrandetail_grid").datagrid("resize");
						},
						onCancel : function(){
							closeCurTab();
						}
					});
				}else{
					//判断是否打印入库单
					FW.confirm("是否立即打印入库单？",{
						onConfirm : function(){
							//保存完毕之后绑定打印按钮
							var urlReport = null;
							var urlReportPreview = null;
							if(siteid == 'SJW'){
								urlReport = fileExportPath+"preview?&__format=pdf&__asattachment=true&__report=report/TIMSS2_SJW_SR_001_pdf.rptdesign&sheetid="+imtid+"&siteid="+siteid;
								urlReportPreview = fileExportPath+"preview?&__format=pdf&__report=report/TIMSS2_SJW_SR_001_pdf.rptdesign&sheetid="+imtid+"&siteid="+siteid;
							}else{
								urlReport = fileExportPath+"preview?__format=pdf&__asattachment=true&__report=report/TIMSS2_IMT_001_pdf.rptdesign&imtid="+imtid+"&siteid="+siteid;
								urlReportPreview = fileExportPath+"preview?__format=pdf&__report=report/TIMSS2_IMT_001_pdf.rptdesign&imtid="+imtid+"&siteid="+siteid;
							}
							FW.initPrintButton("#btn_print",urlReportPreview,"接收打印预览",null);
							$("#btn_print").bindDownload({
								url : urlReport
							});
							
							$("#btn_print").trigger("click");
							if('over' != type ){
								type = '';
							}
							$("#btn_save").hide();
							$("#btn_print").show();
							$("#btn_preview").hide();
							$("#btn-add").hide();
							$("#autoform").ITC_Form("readonly");
							
							FW.fixToolbar("#toolbar");
						},
						onCancel : function(){
							closeCurTab();
						}
					});
				}
				
				FW.success( "保存成功 ");
			}else{
				FW.error( "保存失败 ");
			}
		}
	});
}

//删除记录
function delRecord(itemid){
	Notice.confirm("删除？|确定删除所选项吗？该操作无法撤消。",function(){
		$('#mattrandetail_grid').datagrid('deleteRow',$('#mattrandetail_grid').datagrid('getRowIndex',itemid));
		var listData =$("#mattrandetail_grid").datagrid("getRows");
		if(listData.length == 0){
			$("#btn-add").text("添加到货物资");
		}else{
			$("#btn-add").text("继续添加到货物资");
		}
		dynaCalcTotalPrice();
	},null,"info");
}

//检查是否已经完全接收
function checkAllInStock(type){
	var listData =$("#mattrandetail_grid").datagrid("getRows");
	var laststockqty = 0;
	var stockqty = 0;
	var count = 0;
	var stockqtytemp = 0;
	for(var i=0;i<listData.length;i++){
		laststockqty = parseFloat(listData[i].laststockqty);
		itemnum = parseFloat(listData[i].itemnum);
		stockqtytemp = parseFloat(listData[i].stockqtytemp);
		
		if('save' == type){
			if((laststockqty+stockqtytemp) == itemnum){
				count++;
			}
		}else{
			if(laststockqty == itemnum){
				count++;
			}
		}
	}
	//完成所有接收
	if(count == listData.length){
		return true;
	}else{
		return false;
	}
}

//删除物资接收待办
function deleteMatTranDB(){
	//var processinstid = $("#f_processinstid").val();
	var po_sheetno = $("#f_pruorderno").val();
	//if(null != processinstid){
	if(null != po_sheetno){
		$.ajax({
			type : "POST",
			async: false,
			url: basePath+"inventory/invmattran/deleteMatTranDB.do",
			//data: {"dbId": $("#f_processinstid").val()},
			data: {"dbId": po_sheetno},
			dataType : "json",
			success : function() {
			}
		});
	}
}

//物资接收后更改采购申请单中的采购状态
function updatePurApplyStatus(){
	$.ajax({
		type : "POST",
		async: false,
		url: basePath+"inventory/invmattran/updatePurApplyStatus.do",
		data: {"imtid" : imtid},
		dataType : "json",
		success : function() {
		}
	});
}


//退货
function returnsMatOutStock(){
	//TODO 获取采购单的sheetno，传到退货页面，为后续退货时触发添加待办功能。
	var formData =$("#autoform").ITC_Form("getdata");
	var pruorderno = formData.pruorderno;
	
	var url = basePath+ "inventory/invmattran/invMatReturnsForm.do?imtid="+imtid+"&pruorderno="+pruorderno+"&openType=read";
	var prefix = imtid;
    FW.addTabWithTree({
        id : "editMatReturnForm" + prefix,
        url : url,
        name : "物资退货",
        tabOpt : {
            closeable : true,
            afterClose : "FW.deleteTab('$arg');FW.activeTabById('editMatTranForm"+imtid+"');"
        }
    });

}
