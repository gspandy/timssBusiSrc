//查询仓库
/* function queryWarehouse() {
	$.ajax({
		type : "POST",
		async: false,
		url: basePath+"inventory/invwarehouse/queryWarehouseInList.do",
		dataType : "json",
		success : function(data) {
			warehouseArr = data;
		}
	});
} */

//编辑列表中所有的行
function startEditAll(){
	var rows = $("#stocktakingdetail_grid").datagrid("getRows");
	for(var i=0;i<rows.length;i++){
		$("#stocktakingdetail_grid").datagrid("beginEdit",i);
	}
}

//关闭编辑列表
function endEditAll(){
	var rows = $("#stocktakingdetail_grid").datagrid("getRows");
	for(var i=0;i<rows.length;i++){
		$("#stocktakingdetail_grid").datagrid("endEdit",i);
	}
}

//仓库查询
function queryWarehouse() {
	var formData =$("#autoform").ITC_Form("getdata");
	var formWarehouseId = formData.warehouseid;
    $.ajax({
		type : "POST",
		async: false,
		url: basePath+"inventory/invwarehouse/queryAllWarehouse.do",
		dataType : "json",
		success : function(data) {
			var array = data.hashMapList;
			for(var i=0;i<array.length;i++){
				if(typeof(formWarehouseId) != "undefined" && formWarehouseId != ""){
					if(formWarehouseId == array[i].key){
						comboxArr.push([array[i].key,array[i].value,true]);
					}else{
						comboxArr.push([array[i].key,array[i].value]);
					}
				}else{
					comboxArr.push([array[i].key,array[i].value]);
				}
			}
		}
	});
}

//显示附件
function uploadform() {
	var file = null;
	
	if("" != uploadFiles){
		file = JSON.parse(uploadFiles);
	}
	
	$("#uploadform").iForm('init', {
		"fields" : [
                    	{
                    	 id:"uploadField", 
                    	 title:" ",
                    	 type:"fileupload",
                    	 linebreak:true,
                    	 wrapXsWidth:12,
                    	 wrapMdWidth:12,
                    	 options:{
                    	     "uploader" : basePath+"upload?method=uploadFile&jsessionid="+sessId,
                    	     "delFileUrl" : basePath+"upload?method=delFile&key="+valKey,
	                    	 "downloadFileUrl" : basePath + "upload?method=downloadFile",
	                    	 "swf" : basePath + "js/inventory/common/uploadify.swf",
	                    	 "initFiles" : file,
	                    	 "delFileAfterPost" : true
	                    	 }
                    	}
                    ],
		"options" : {
		"labelFixWidth" : 6,
		"labelColon" : false
		}
	});
	
}

//改变列表中盈亏样式
function changeCss(){
	var rows = $("#stocktakingdetail_grid").prev(".datagrid-view2").children(".datagrid-body").find(".datagrid-row");
	var rowDatas = $('#stocktakingdetail_grid').datagrid("getRows"); // 这段代码是// 对某个单元格赋值
	for(var i=0;i<rows.length;i++){
		var row = $(rows[i]);
		
		var qtyAfter = row.children("td[field='qtyAfter']").find("input").val();
		qtyAfter = parseFloat(qtyAfter).toFixed(4);
		
		var qtyBefore = row.children("td[field='qtyBefore']").find("div").text();
		qtyBefore = parseFloat(qtyBefore).toFixed(4);
		
		var nowqty = row.children("td[field='nowqty']").find("div").text();
		nowqty = parseFloat(nowqty).toFixed(4);
		
		if(isNaN(qtyBefore)){
			FW.error("物资【"+rowDatas[i]["itemcode"]+"】无法获取物资库存量，请联系系统管理员");
			break;
		}
		
		if(isNaN(qtyBefore) || isNaN(qtyAfter)){
			break;
		}
		
		var soQty = parseFloat(qtyBefore - nowqty).toFixed(2);//领料占用
		
		if(qtyAfter-soQty<0 && qtyAfter>=0){
			numVal = rowDatas[i]["itemcode"];
			FW.error("已有领料单领用了物资"+numVal+"，请保留充足数量或者取消领料单后再操作");
			break;
		}else{
			numVal = "";
		}
		
		var breakeven = parseFloat(qtyAfter - qtyBefore).toFixed(2);
		
		if(breakeven > 0){
			row.children("td[field='breakeven']").find("div").css("color","green");
			row.children("td[field='breakeven']").find("div").html("+"+breakeven);
			rowDatas[i]['breakeven'] = "+"+breakeven;
		}else if(breakeven < 0){
			row.children("td[field='breakeven']").find("div").css("color","red");
			row.children("td[field='breakeven']").find("div").html(breakeven);
			rowDatas[i]['breakeven'] = breakeven;
		}else{
			row.children("td[field='breakeven']").find("div").html("--");
			rowDatas[i]['breakeven'] = "--";
		}
	}
}

//关闭当前tab
function closeCurTab(){
	homepageService.refresh();
	FW.deleteTabById(FW.getCurrentTabId());
	FW.activeTabById("stock");
}
