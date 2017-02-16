function changeStateBtnState(state){
	if(Priv.hasPrivilege("storeList_enable")||Priv.hasPrivilege("storeList_disable")){
		if(state=='Y'){
			$("#btn_enable").attr("disabled",true);
			$("#btn_disable").attr("disabled",false);
		}else if(state=='N'){
			$("#btn_enable").attr("disabled",false);
			$("#btn_disable").attr("disabled",true);
		}
	}
}

function confirmBeforeChangeWare(state){
	FW.confirm("确定停用？|请确认此仓库的物资已盘点或移库。停用后，将不能领用此仓库物资。",function(){
		changeWarehouseState(state);
	})
}

function changeWarehouseState(state){
	if(!state){
		state='Y';
	}

	var row=$("#warehouse_grid").datagrid("getSelected");
	$.ajax({
		type : "POST",
		url: basePath+"inventory/invwarehouse/changeState.do",
		data: {"id":row.warehouseid,"state":state},
		dataType : "json",
		success : function(data) {
			if(data.result=='success'){
				var index=$("#warehouse_grid").datagrid("getRowIndex",row);
				row.active=state;
				$('#warehouse_grid').datagrid('updateRow',{
					index: index,
					row: row
				});
				
				var nowRow=$("#warehouse_grid").datagrid("getSelected");
				if(nowRow.warehouseid==row.warehouseid){
					changeStateBtnState(state);
					FW.fixToolbar("#toolbar1");
				}
				
				FW.success((state=='Y'?"启用":"停用")+"仓库成功");
			}else{
				FW.error((state=='Y'?"启用":"停用")+"仓库失败,请稍后重试或联系管理员");
			}
			FW.itemTreeReflesh();
		}
	});
}

function createWarehouse(){
	var url = basePath+"inventory/invwarehouse/invWarehouseForm.do?mode=create";
	var prefix = new Date().getTime() + "" + Math.floor(Math.abs(Math.random()*100));
	FW.addTabWithTree({
	        id : "newWarehouseForm" + prefix,
	        url : url,
	        name : "仓库",
	        tabOpt : {
	            closeable : true,
	            afterClose : "FW.deleteTab('$arg');FW.activeTabById('stock');FW.getFrame(FW.getCurrentTabId()).refCurPage();"
	        }
	});
}