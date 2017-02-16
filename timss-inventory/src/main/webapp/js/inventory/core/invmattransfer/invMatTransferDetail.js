//物资表单
var toolDelteId = 1;

var	toolGridField = [[
		{field : 'toolDelteId',title : '临时ID用来指定删除row', hidden:true,
			formatter:function(value,row,index){
				row.toolDelteId =toolDelteId;
				return toolDelteId++;
			}
		},
		{field : 'imtdId', hidden:true},
		{field : 'imtId', hidden:true},
		{field : 'itemId',hidden:true},
		{field : 'itemCode',title : '物资编码',width:100,fixed:true,formatter:function(value,row){
			return "<a onclick='FW.showItemInfo(\""+row.itemCode+"\",\""+row.wareHouseId+"\",\""+row.cateTypeId+"\");'>"+row.itemCode+"</a>";
		}},
		{field : 'itemName',title : '名称', width:100},
		{field : 'price',hidden:true},
		{field : 'cusModel',title : '规格型号',width:60},
		{field : 'nowqty',title : '可用库存',width:60,fixed:true,align:'left'},
		{field : 'stockQty',title : '实际库存',width:60,fixed:true,align:'left'},
		{field : 'transferQty',title : '数量',edit:true,width:60,fixed:true,
			editor:{
				type:"text",
				rules : {required:true,"digits": true},
				options:{
					align:"right"
				}
			}
		},
		{field : 'transferQtyText',title : '数量',width:60,fixed:true,align:'left', hidden:true,
			formatter:function(value,row,index){
				return row.transferQty;
			}	
		},
		{field : 'wareHouseId',hidden:true},
		{field : 'wareHouseName',title : '移出仓库',width:100,fixed:true,align:'left',hidden:true},
		{field : 'toWareHouseId',hidden:true},
		{field : 'toWareHouseName',title : '移入仓库',width:80,fixed:true,align:'left',hidden:true},
		{field : 'binId',hidden:true},
		{field : 'binName',title : '移出货柜',width:120,fixed:true,align:'left'},
		{field : 'toBinId',title : '移入货柜Id',hidden:true},
		{field : 'toBinName',title : '移入货柜',width:120,fixed:true,align:'left', hidden:true},
		{field : 'cateTypeId',hidden:true},
		{field : 'cateTypeName',title : '物资类型',width:120,fixed:true,align:'left'},
		{field : 'toCateTypeId',title : '物资类型Id',hidden:true},
		{field : 'toCateTypeName',title : '物资类型',width:120,fixed:true,align:'left', hidden:true},
		{field : 'unitId',hidden:true},
		{field : 'unitName',title : '单位',width:60,fixed:true,align:'left'},
		{field : 'oper',title : '',width:30,fixed:true,align:'center',
			formatter:function(value,row,index){
				return '<img src="'+basePath+'img/workorder/btn_garbage.gif" onclick="deleteGridRow('+
					"'toolTable',"+row.toolDelteId+')" width="16" height="16" >';
			}
		}
	]];

//添加物资
function appendTool(){
	$("#toolTable").datagrid("resize");
	//调用库存物资树方法
	FW.showInventoryDialog({	
		warehouseid: wareHouseFromId,
		onParseData : function(data){
			var size = data.length;
			for(var i=0;i<size;i++){
				var row = {};
				if(!itemExist(data[i]["itemid"])){
					row["itemId"] = data[i]["itemid"];
					row["itemCode"] = data[i]["itemcode"];
					row["itemName"] = data[i]["itemname"];
					row["cusModel"] = data[i]["cusmodel"];
					row["nowqty"] = data[i]["nowqty"];
					row["stockQty"] = data[i]["stockqty"];
					row["transferQty"] = 1 ;  //默认添加一个
					row["unitId"] = data[i]["unit1"];
					row["unitName"] = data[i]["unitname"];
					row["wareHouseId"] = data[i]["warehouseid"];
					row["wareHouseName"] = data[i]["warehouse"];
					row["binId"] = data[i]["binid"];
					row["binName"] = data[i]["bin"];
					row["cateTypeId"] = data[i]["cateId"];
					row["cateTypeName"] = data[i]["cateType"];
					row["price"] = data[i]["price"];
					$("#toolTable").datagrid("appendRow",row );
				}

			}
			beginEdit("toolTable");
		}
	});
	$("#btn_toolTable").html("继续添加物资");
} 

//判断物资是否存在
function itemExist(itemIdParam){
	var rows = $("#toolTable").datagrid("getRows");
	for(var i=0;i<rows.length;i++){
		var itemId = rows[i].itemId;
		if(itemId == itemIdParam ){
			FW.success("重复物资已过滤");
			return true;
		}
	}
	return false;
}

//删除表格中的物资
function deleteGridRow(dataGridId,deleteId){
	 $('#'+dataGridId).datagrid('deleteRow', $('#'+dataGridId).datagrid('getRowIndex',deleteId));
	 var rowsLength = $("#"+dataGridId).datagrid('getRows').length;
	 if(rowsLength == 0){
		 var oldBtnName =  $("#btn_"+dataGridId).html().substring(2);
		 $("#btn_"+dataGridId).html(oldBtnName);
	 }
}

//开始编辑
function beginEdit(id){
	var rows = $("#"+id).datagrid('getRows');
	for(var i=0;i<rows.length;i++){
		var row = rows[i];
		var index = $('#toolTable').datagrid('getRowIndex', row);
		if((row.toBinName==null) || (row.toBinName=="")){
			$("#"+id).datagrid('beginEdit',index); 
		}
		else{
			$("#"+id).datagrid('endEdit',index); 
		}
	}
	$("#"+id).datagrid("resize");
}

//结束编辑
function endEdit(id){
	var rows = $("#"+id).datagrid('getRows');
	for(var i=0;i<rows.length;i++){
		$("#"+id).datagrid('endEdit',i);
	}
	$("#"+id).datagrid("resize");
}

//通过列表选中物资生成领料
function listToMatApply(){
	$.ajax({
       url : basePath+"inventory/invmattransfer/queryMatTransferDetailItems.do",
       async: false,
       dataType: "json",
       type:"POST",
       data: {"wareHouseId":wareHouseFromId, "itemIds":itemIds, "itemCodes":itemCodes, "cateTypeIds":cateTypeIds},
       success:function(data){
       		var result = data.result;
           	if(null != result && "" != result){
                var resultArr = result.split(";");
                //追加行后立刻执行编辑操作
                for(var i=0;i<resultArr.length;i++){
                	var rowJ = JSON.parse(resultArr[i]);
            	   	if(rowJ!=null){
                	   	var row = {};
    					row["tmpid"]=i+1;
    					row["itemId"] = rowJ.itemid;
    					row["itemCode"] = rowJ.itemcode;
    					row["itemName"] = rowJ.itemname;
    					row["cusModel"] = rowJ.cusmodel;
    					row["nowqty"] = rowJ.nowqty;
    					row["stockQty"] = rowJ.stockqty;
    					row["transferQty"] = 1;
    					row["unitId"] = rowJ.unit1;
    					row["unitName"] = rowJ.unitname;
    					row["wareHouseId"] = rowJ.warehouseid;
    					row["wareHouseName"] = rowJ.warehouse;
    					row["binId"] = rowJ.binid;
    					row["binName"] = rowJ.bin;
    					row["cateTypeId"] = rowJ.cateId;
    					row["cateTypeName"] = rowJ.cateName;//此处和弹出框中字段名  row["cateTypeName"] = data[i]["cateType"];
    					row["price"] = rowJ.price;
    					$("#toolTable").datagrid("appendRow",row );
            	   	}
                }
    			beginEdit("toolTable");
		 		$("#btn_toolTable").text("继续添加物资");
           }
       }
   });
}

//编辑列表中所有的行
function startEditAll(){
	var rows = $("#toolTable").datagrid("getRows");
	for(var i=0;i<rows.length;i++){
		$("#toolTable").datagrid("beginEdit",i);
	}
}

//验证物资列表
function validateTable(wareHouseFromId){
	var rows = $("#toolTable").datagrid("getRows");
	if(rows.length==0){
		FW.error("请先选择要转移的物资");
		return false;
	}
	for(var i=0;i<rows.length;i++){
		var itemName = rows[i].itemName;
		var nowqty = rows[i].nowqty;
		var stockQty = rows[i].stockQty;
		var transferQty = rows[i].transferQty;
		var wareHouseId = rows[i].wareHouseId;
		if(transferQty == 0){
			FW.error("【" + itemName +"】的转移数量不能为0，请修改");
			return false;
		}
		if(transferQty > stockQty || transferQty > nowqty ){
			FW.error("【" + itemName +"】的移出数量不能超过可用库存和实际库存");
			return false;
		}
		if(wareHouseId != wareHouseFromId){
			FW.error("【" + itemName +"】不是移出仓库的物资，请先删除");
			return false;
		}
	}
	return true;
}
