//获取列表中选择的信息
function getSelected() {
	var obj = $("#item_grid").datagrid("getChecked");
	if (obj) {
		var o = {};
		for ( var i = 0; i < obj.length; i++) {
			o[obj[i].itemcode] = JSON.stringify(obj[i]);
		}
		return o;
	} else {
		return null;
	}
}

//获取选中的条目
function getFullDataSelected() {
	return $("#item_grid").datagrid("getChecked");
}

//刷新页面
function refCurPage() {
	$("#item_grid").datagrid("uncheckAll");
	$("#item_grid").datagrid("reload");
}

//改变列表字体颜色
function changeCss(){
	var rows = $("#item_grid").prev(".datagrid-view2").children(".datagrid-body").find(".datagrid-row");
	for(var i=0;i<rows.length;i++){
		var row = $(rows[i]);
		
		var qtyStock = row.children("td[field='qtyStock']").find("div").text();
		qtyStock = parseFloat(qtyStock).toFixed(4);
		
		var qtyLowInv = row.children("td[field='qtyLowInv']").find("div").text();
		qtyLowInv = parseFloat(qtyLowInv).toFixed(4);
		
		if(isNaN(qtyStock) || isNaN(qtyLowInv)){
			break;
		}
		
		var breakeven = parseFloat(qtyStock - qtyLowInv).toFixed(2);
		
		if(breakeven < 0){
			row.children("td[field='qtyStock']").find("div").css("color","red");
		}else if(breakeven >= 0){
			row.children("td[field='qtyStock']").find("div").css("color","green");
		}
	}
}

//选择仓库
function onSelectWareHouse(wareHouseId, wareHouseName, refresh){
	//设置全局变量已选仓库
	currentWareHouseName = wareHouseName;
	//仓库选择菜单变化
	$("#currentWareHouseNameLabel").html(currentWareHouseName);	
	$('#selectWareHouseMenu li a').css('font-weight','normal');
	$("#"+wareHouseId + "MenuItem").css('font-weight','bold');
	//重新查询
	if(refresh){
		refreshData();
	}
}

//选择查询范围
function onSelectRange(rangeVal, rangeName, refresh){
	//设置全局变量已选范围
	currentRangeVal = rangeVal;
	//仓库选择菜单变化
	//$("#currentRangeNameLabel").html(rangeName);	
	$('#selectRangeMenu li a').css('font-weight','normal');
	$("#" + rangeVal + "MenuItem").css('font-weight','bold');
	//重新查询
	if(refresh){
		refreshData();
	}
}

//重新查询
function refreshData(){
	//如果处于编辑状态，则提示先保存，不允许翻页。
	if($('#btn_saveSafeQty').css('display')!="none"){
		FW.error( "请先保存当前页面安全库存值" );
		return false;
	}
	
	//获取物资id或编码
	currentItemInfo = $("#item_search")[0].value;
	$("#item_grid").iDatagrid("init",{
		queryParams: {
			search: FW.stringify({warehouse:currentWareHouseName}),
			type:currentRangeVal,
			itemInfo:currentItemInfo
		}
	});
}

//编辑安全库存
function editSafeQty(){
	var rows = $("#item_grid").datagrid('getRows');
	for(var i=0;i<rows.length;i++){
		$("#item_grid").datagrid('beginEdit',i); 
	}
	$("#btn_editSafeQty").hide();
	$("#btn_saveSafeQty").show();
	$("#item_grid").datagrid("resize");
	FW.fixToolbar("#toolbar1");
}

//保存安全库存
function saveSafeQty(){
	//结束编辑
	var rows =$("#item_grid").datagrid("getRows");
	for(var i=0;i<rows.length;i++){
		var ed = $('#item_grid').datagrid('getEditor', {index:i,field:'qtyLowInv'});
		var qtyLowInv = $(ed.target).find("input").val();
		if(qtyLowInv==null || qtyLowInv==""){
			FW.error(rows[i]["itemname"] + "的安全库存量不能为空");
			return;
		}
		if(qtyLowInv < 0){
			FW.error(rows[i]["itemname"] + "的安全库存量不能小于0");
			return;
		}
	}
	
	for(var i=0;i<rows.length;i++){
		$("#item_grid").datagrid('endEdit',i); 
		rows[i]['invcateid'] = rows[i]['cateId2'];
	}
	$("#btn_editSafeQty").show();
	$("#btn_saveSafeQty").hide();
	$("#item_grid").datagrid("resize");
	FW.fixToolbar("#toolbar1");
	
	//提交表单数据
	$.ajax({
		type : "POST",
		url: basePath+"inventory/invwarehouseitem/batchUpdateSafeQty.do",
		data: {
			"listData":FW.stringify(rows)
		},
		dataType : "json",
		success : function(data) {
			if( data.result == "success" ){
				FW.success( "保存成功 " );
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			FW.error( "保存失败 " );
		}
	});
}


