var doingStatusPriv=function(){
	if("over"==processStatus){
		return true;
	}else{
		return false;
	}
}
var PurApplyPriv={
		init:function(){
			PurApplyPriv.set();
			PurApplyPriv.apply();
		},
		set:function(){//定义权限
			//编辑
			Priv.map("privMapping.applypurch_edit","applypurch_edit");
			//暂存
			Priv.map("privMapping.applypurch_save","applypurch_save");
			//提交
			Priv.map("privMapping.applypurch_commitApply","applypurch_commitApply");
			//删除
			Priv.map("privMapping.applypurch_delete","applypurch_delete");
			//执行情况
			Priv.map("doingStatusPriv()","applypurch_doingStatusPriv");
			//终止采购申请
			Priv.map("privMapping.applypurch_stop","applypurch_stop");
		},
		apply:function(){//应用权限
			//应用
			Priv.apply();
		}
};

//设置流程状态
function setProcessStatus(){
	if("" == process || "draft" == process){
		processStatus = "first";//首环节
		if( "draft" == process && "" != processInstId){
			processStatus = "first_save";//首环节暂存
		}else if("" != processInstId){
			processStatus = "over";//流程结束
		}
	}else if(process.indexOf("procurement", 0)>-1){
		processStatus = "last";//流程最后环节
	}else{
		processStatus = "process";//流程中
		if("deputy_oper_manager"==process){
			processStatus = "dom";//经营部副总经理审批
		}
	}
}

//开始编辑所有行
function startEditAll(){
	var rows = $("#apply_item").datagrid("getRows");
	for(var i=0;i<rows.length;i++){
		$("#apply_item").datagrid("beginEdit",i);
	}
}

//结束编辑所有行
function endEditAll(){
	var rows = $("#apply_item").datagrid("getRows");
	for(var i=0;i<rows.length;i++){
		$("#apply_item").datagrid("endEdit",i);
	}
}

//行计算
function rowCalu(rowIndex,rowData){
	var repliednum = 0.0;
	//若是首环节则计算itemnum字段，不是首环节则计算reliednum字段
	if("" == sheetId){
		repliednum = parseFloat(rowData.itemnum);
	}else{
		repliednum = parseFloat(rowData.repliednum);
	}
	//获取平均价格
	var averprice = rowData.averprice;
	var pt = parseFloat(repliednum * averprice).toFixed(2);
	//将每行总价赋值
	$("#apply_item").datagrid("updateRow",{
		index: rowIndex,
		row: { "priceTotal": pt,"repliednum": repliednum}
	});
}

//计算所有物资总值
function caluPrice(){
	endEditAll();
	var listData =$("#apply_item").datagrid("getRows");
	var totalPrice = 0.0;
	for(var i=0;i<listData.length;i++){
		var repliednum = 0.0;
		if(processStatus == "first" || processStatus == "first_save"){
			repliednum = parseFloat(listData[i].itemnum);
		}else{
			repliednum = parseFloat(listData[i].repliednum);
		}
		
		var averprice = listData[i].averprice;
		totalPrice += repliednum * averprice;
	}
	var total = {tatolcost:totalPrice.toFixed(2)};
	$("#autoform").iForm("setVal",total);
}

//动态计算总价
function dynaCalcTotalPrice(){
	var rows = $("#apply_item").prev(".datagrid-view2").children(".datagrid-body").find(".datagrid-row");
	var totalPrice = 0;
	for(var i=0;i<rows.length;i++){
		var repliednum = 0;
		var row = $(rows[i]);
		if(processStatus == "first" || processStatus == "first_save"){
			repliednum = row.children("td[field='itemnum']").find("input").val();
			if(repliednum == '0'){
				FW.error( "申请数量不能为0");
				row.children("td[field='itemnum']").find("input").val('1');
				repliednum = '1';
			}
		}else{
			repliednum = row.children("td[field='repliednum']").find("input").val();
			if(typeof(repliednum) == "undefined"){
				repliednum = row.children("td[field='repliednum']").find("div").text();
			}
		}
		repliednum = Number(repliednum);
		var averprice = row.children("td[field='averprice']").find("input").val();
		if(typeof(averprice) == "undefined"){
			averprice = row.children("td[field='averprice']").children("div").html();
		}
		averprice = parseFloat(averprice);
		if(isNaN(repliednum) || isNaN(averprice)){
			break;
		}
		var lineSum = repliednum * averprice;
		row.children("td[field='priceTotal']").children("div").html(lineSum.toFixed(2));
		totalPrice += lineSum;
	}
	var total = {tatolcost:totalPrice.toFixed(2)};
	$("#autoform").iForm("setVal",total);
}

//安全库存生成采购申请单
function addItemFromSource() {
	if (sourceCome == 'safetyStock') {//如果是来自安全库存的采购申请，则获得安全库存用户选择的物资进行插入
		var list = FW.get("safetyStockPurapply");
		for ( var i = 0; i < list.length; i++) {
			var row = {};
			row["ck"] = '';
			row["listId"] = Priv.secUser.siteId+"_"+list[i]["itemid"]+"_"+list[i]["warehouseid"]+"_"+list[i]["cateId2"];
			row["itemid"] = list[i]["itemcode"];
			row["itemname"] = list[i]["itemname"];
			row["cusmodel"] = list[i]["cusmodel"];
			row["warehouseid"] = list[i]["warehouseid"];
			row["warehouse"] = list[i]["warehouse"];
			row["invcateid"] = list[i]["cateId2"];
			row["itemnum"] = list[i]["qtyEconomic"] == 0 ? 1 : list[i]["qtyEconomic"];
			row["repliednum"] = '1';
			row["orderunitname"] = list[i]["unitname"];
			row["storenum"] = list[i]["qtyStock"];
			row["averprice"] = parseFloat(list[i]["price"]).toFixed(2);
			row["priceTotal"] = parseFloat(list[i]["price"]).toFixed(2);
			$("#apply_item").datagrid("appendRow", row);
		}
		caluPrice();
		startEditAll();
		$("#btn-add").text("继续添加物资");
		dynaCalcTotalPrice();
	}
}

//自动生成采购单
function autoGenerateOrder(){
	var itemIds = null;
	var listData = [];
	var selectData = $("#apply_item").datagrid("getSelections");
	if(null != selectData){
		for(var i=0;i<selectData.length;i++){
			listData.push(selectData[i].itemid);
		}
		itemIds = listData.join("','");
	}
	
	var url = basePath+"purchase/purapply/autoGenerateOrder.do?appSheetId="+sheetId+"&itemIds="+itemIds;
	var prefix = new Date().getTime() + "" + Math.floor(Math.abs(Math.random()*100));
	var newTag = "newOrderForm" + prefix;
	FW.addTabWithTree({
        id : newTag,
        url : url,
        name : "采购合同",
        tabOpt : {
            closeable : true,
            afterClose : "FW.deleteTab('$arg');FW.activeTabById('purchasing');"
        }
    });
}

//20160106 add by yuanzh 说是要添加附件
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
	                    	 "swf" : basePath + "js/purchase/common/uploadify.swf",
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