//检查输入的是否超出可领用的
function checkQtyIsOver(){
	var stockqty = $(this).parents(".datagrid-row").children("td[field='stockqty']").find("div").text();
	stockqty = parseInt(stockqty,10);
	
	var waitqty = $(this).parents(".datagrid-row").children("td[field='waitqty']").find("div").text();
	waitqty = parseInt(waitqty,10);
	
	var outstockqty = $(this).parents(".datagrid-row").children("td[field='outstockqty']").find("input").val();
	outstockqty = parseInt(outstockqty,10);
	
	if(outstockqty > waitqty){
		FW.error("本次领用量不能超过待领用量");
		return false;
	}else if(outstockqty > stockqty){
		FW.error("本次领用量不能超过当前库存量");
		return false;
	}else if(outstockqty<0){
		FW.error("领用量不能小于0");
		return false;
	}else{
		return true;
	}
}

//全局保存之前做验证
function checkQtyOverBeforeSave(){
	var check = true;
	var rows = $("#matapplydetail_grid").prev(".datagrid-view2").children(".datagrid-body").find(".datagrid-row");
    var allZero = true;//TIM-1156 如果明细当中没有物资或者所有明细的数量都填0，不允许提交
    
	for(var i=0;i<rows.length;i++){
		var row = $(rows[i]);
		var stockqty = row.children("td[field='stockqty']").find("div").text();
		stockqty = parseFloat(stockqty);
		
		var waitqty = row.children("td[field='waitqty']").find("div").text();
		waitqty = parseFloat(waitqty);
		
		var outstockqty = row.children("td[field='outstockqty']").find("input").val();
		outstockqty = parseFloat(outstockqty);
    	
		if(outstockqty > waitqty){
			FW.error("本次领用量不能超过待领用量");
			check = false;
		}else if(outstockqty > stockqty){
			FW.error("本次领用量不能超过当前库存量");
			check = false;
		}else if(outstockqty<0){
			FW.error("领用量不能小于0");
			check = false;
		}else if(outstockqty>0){
			allZero = false;
		}
	}
	if(allZero){
		FW.error("物资领用量不能全部为0");
		check = false;
	}
	return check;
}