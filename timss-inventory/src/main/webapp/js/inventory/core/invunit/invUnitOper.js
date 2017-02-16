function changeUnitState(state){
		if(!state){
			state='Y';
		}
		var row=$("#unit_grid").datagrid("getSelected");
		$.ajax({
			type : "POST",
			url: basePath+"inventory/invunit/changeState.do",
			data: {"id":row.unitid,"state":state},
			dataType : "json",
			success : function(data) {
				if(data.result=='success'){
					var index=$("#unit_grid").datagrid("getRowIndex",row);
					row.active=state;
					$('#unit_grid').datagrid('updateRow',{
						index: index,
						row: row
					});
					
					var nowRow=$("#unit_grid").datagrid("getSelected");
					if(nowRow.unitid==row.unitid){
						changeStateBtnState(state);
						FW.fixToolbar("#toolbar1");
					}
					
					FW.success((state=='Y'?"启用":"停用")+"计量单位成功");
				}else{
					FW.error((state=='Y'?"启用":"停用")+"计量单位失败,请稍后重试或联系管理员");
				}
			}
		});
	}

function createUnit(){
	var url = basePath+"inventory/invunit/invUnitForm.do?mode=create";
	var prefix = new Date().getTime() + "" + Math.floor(Math.abs(Math.random()*100));
	FW.addTabWithTree({
	        id : "newUnitForm" + prefix,
	        url : url,
	        name : "计量单位",
	        tabOpt : {
	            closeable : true,
	            afterClose : "FW.deleteTab('$arg');FW.activeTabById('stock');FW.getFrame(FW.getCurrentTabId()).refCurPage();"
	        }
	});
}

function changeStateBtnState(state){
	if(Priv.hasPrivilege("unitList_enable")||Priv.hasPrivilege("unitList_disable")){
		if(state=='Y'){
			$("#btn_enable").attr("disabled",true);
			$("#btn_disable").attr("disabled",false);
		}else if(state=='N'){
			$("#btn_disable").attr("disabled",true);
			$("#btn_enable").attr("disabled",false);
		}
	}
}