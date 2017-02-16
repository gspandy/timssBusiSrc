//提交保存用方法
function consumData(){
	flag = checkQtyOverBeforeSave();
	if(flag){
		endEditAll();
		if(counterBefore == counterAfter){
			FW.error("本次领用量不能全部为0");
			$("#matapplydetail_grid").datagrid("reload");
			startEditAll();
			return ;
		}else {
			return $("#matapplydetail_grid").datagrid("getRows");
		}
	}else{
		return ;
	}
}