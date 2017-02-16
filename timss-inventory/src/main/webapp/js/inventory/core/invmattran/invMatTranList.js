var grid=[[
		{field:'tmpid',width:10,hidden:true,fixed:true,formatter:function(value,row){
			row.tmpid=tmpid;
			return tmpid++;
		}},
		{title:'物资id',field:'itemid',width:90,hidden:true,fixed:true},
		{title:'物资编号',field:'itemcode',width:90,fixed:true,formatter:function(value,row){
			return "<a onclick='FW.showItemInfo(\""+row.itemcode+"\",\""+row.warehouseid+"\",\""+row.invcateid+"\");'>"+row.itemcode+"</a>";
		}},
		{title:'物资名称',field:'itemname',width:90},
		{title:'物资型号',field:'cusmodel',width:70},
		{title:'采购合同号',field:'purorderno',width:140,fixed:true},
		{title:'仓库id',field:'warehouseid',hidden:true},
		{title:'仓库',field:'warehouse',width:100,fixed:true},
		{title:'货柜id',field:'binid',hidden:true},
		{title:'货柜',field:'bin',width:70,fixed:true},
		{title:'库存余量',field:'stockqty',width:70,fixed:true,hidden:true,styler: function(value){
			if (value == 0){
				return 'color:red';
			}
		}},
		{title:'采购数量',field:'itemnum',width:60,align:'right',fixed:true},
		{title:'已入库数量',field:'laststockqty',width:80,align:'right',fixed:true},
		{title:'暂存入库数量',field:'stockqtytemp',hidden:true},
		{title:'本次接收',field:'bestockqty',width:80,fixed:true,edit:true,
			editor:{
				type:'text',
				options:{
					align:"right",
					dataType:"number",
					onBlur:dynaCalcTotalPrice
				}
			}
		},
		{title:'本次接收',field:'batchstockqty',width:80,align:'right',fixed:true},
		{title:'单位',field:'unitname',width:40,fixed:true},
		{title:'单位id',field:'unit1',hidden:true},
		{title:'单元成本(元)',field:'price',hidden:true},
		{title:'成本小计(元)',field:'totalprice',hidden:true},
		{title:'外部关联id',field:'outterid',hidden:true},
		{title:'批次',field:'lotno',hidden:true},
		{title:'备注信息',field:'remark',hidden:true},
		{title:'采购申请ID',field:'puraId',hidden:true},
		{title:'物资分类ID',field:'invcateid',hidden:true}
		
]];
var returnGrid=[[
   		{field:'tmpid',width:10,hidden:true,fixed:true,formatter:function(value,row){
   			row.tmpid=tmpid;
   			return tmpid++;
   		}},
   		{title:'物资id',field:'itemid',width:90,hidden:true,fixed:true},
   		{title:'物资编号',field:'itemcode',width:90,fixed:true,formatter:function(value,row){
   			return "<a onclick='FW.showItemInfo(\""+row.itemcode+"\",\""+row.warehouseid+"\");'>"+row.itemcode+"</a>";
   		}},
   		{title:'物资名称',field:'itemname',width:90},
   		{title:'物资型号',field:'cusmodel',width:70},
   		{title:'退货单号',field:'imrsno',width:140,fixed:true},
   		{title:'仓库id',field:'warehouseid',hidden:true},
   		{title:'仓库',field:'warehouse',width:100,fixed:true},
   		{title:'货柜id',field:'binid',hidden:true},
   		{title:'货柜',field:'bin',width:70,fixed:true},
   		{title:'退货数量',field:'returnQty',width:60,align:'right',fixed:true},
   		{title:'退货原因',field:'imrsreason',width:160,fixed:true},
   		{title:'单位',field:'unitname',width:40,fixed:true},
   		{title:'单位id',field:'unit1',hidden:true},
   		{title:'单元成本(元)',field:'price',hidden:true},
   		{title:'成本小计(元)',field:'totalprice',hidden:true},
   		{title:'外部关联id',field:'outterid',hidden:true},
   		{title:'批次',field:'lotno',hidden:true}
   		
   ]];

var assetListColumns=[[
 	{field:'assetId', title:'资产ID', width:200, fixed:true, hidden:true},
 	{field:'createdate',title:'申请时间',width:150,fixed:true,
 		formatter: function(value,row,index){
 	 		return FW.long2time(value);
 	 		}
 	},
 	{field:'createuser',title:'申请人',width:200,fixed:true,hidden:true},
 	{field:'createName',title:'申请人',width:80,fixed:true},
 	{field:'status',title:'状态',width:130,fixed:true,
		formatter:function(val){
			return FW.getEnumMap("AST_APPLY_STATUS")[val];
		}
 	},
 	{field:'assetDate',title:'办理日期',width:150,fixed:true,
 		formatter: function(value,row,index){
 		return FW.long2time(value);
 		}
 	},
 	{field:'assetCode',title:'资产卡片号',width:150,fixed:true/*,
 		formatter:function(value,row){
 			if(!!value){
  			return "<a onclick='openAssetTab(\""+row.assetId+"\");'>"+row.assetCode+"</a>";
 			}
 		}*/
 	},
 	{field:'itemName',title:'资产名称',width:120}
]];
//初始化列表（通用方法）
function initList(){
	$("#mattrandetail_list").iFold("init");
	$("#mattrandetail_grid").datagrid({
		fitColumns : true,
		columns:grid,
		idField:'tmpid',
		singleSelect:true,
		url : basePath+"inventory/invmattrandetail/queryMatTranDetail.do",
		queryParams: {"imtid":imtid,"openType": openType},
		onLoadSuccess: function(data){
            if(data && data.total==0){
                $("#mattrandetail_grid").hide();
            }
            var listData =$("#mattrandetail_grid").datagrid("getRows");
			if(listData.length != 0){
				for(var i=0;i<listData.length;i++){
					var itemnum = listData[i].itemnum;
					var laststockqty = listData[i].laststockqty;
					if(itemnum>laststockqty){
						isEdit = true;
					}
				}
			}
			
			if("" != imtid){
				initAssetApplyList(); //加载资产申请记录
				initReturnList();  //加载退货记录
				if(type == 'auto'){
					if(checkAllInStock("init")){
						$("#btn_edit").hide();
						$("#mattrandetail_grid").datagrid("hideColumn","bestockqty");
					} else{
						if(isEdit){
							if(flagCounter == 0){
								$("#btn_edit").show();
								$("#btn_edit").trigger("click");
							}else{
								startEditAll();
								dynaCalcTotalPrice();
								flag = true;
							}
							$("#mattrandetail_grid").datagrid("hideColumn","batchstockqty");
						}else{
							$("#btn_edit").hide();
							$("#mattrandetail_grid").datagrid("hideColumn","bestockqty");
						}
					}
//					$("#btn_returns").hide();
				}else{
					$("#btn_edit").hide();
					$("#mattrandetail_grid").datagrid("hideColumn","bestockqty");
				}
			}else{
				$("#btn_returns").hide();
				$("#mattrandetail_grid").datagrid("hideColumn","batchstockqty");
			}
			setTimeout(function(){ 
				$("#mattrandetail_grid").datagrid("resize"); 
			},200);
			FW.fixToolbar("#toolbar");
        },
        onAfterEdit:function(rowIndex, rowData){
        	var itemnum = rowData.itemnum;
        	var laststockqty = rowData.laststockqty;
        	
        	var num = parseFloat(itemnum - laststockqty).toFixed(2);
        	var bestockqty = parseFloat(rowData.bestockqty); 
        	
        	if(bestockqty>=0){
				if(flagCounter == 0){
					var stockqtytemp = rowData.stockqtytemp;
	        		if(null == stockqtytemp || '' == stockqtytemp){
	        			stockqtytemp = laststockqty;
	        		}
					var price = parseFloat(rowData.price);
					var stockQty = parseFloat(stockqtytemp*1+bestockqty*1);
					var rowTotal = parseFloat(stockQty * price).toFixed(2);
					
					$("#mattrandetail_grid").datagrid("updateRow",{
						index: rowIndex,
						row: { "totalprice": rowTotal,"laststockqty": bestockqty,"stockqtytemp":stockqtytemp,"batchstockqty": bestockqty}
					});
    			}

        	}else{
    			FW.error( "入库数量不能小于0 ");
    			flagCounter++;
        	}
		}
	});
}

//初始化资产申请记录列表
function initAssetApplyList(){
	$("#mattranasset_list").iFold("init");
	$("#mattranassetapply_grid").datagrid({
		fitColumns : true,
		columns:assetListColumns,
		idField:'tmpid',
		singleSelect:true,
		url : basePath+"inventory/invmattrandetail/queryMatTranAssetRecord.do",
		queryParams: {"imtid":imtid},
		onLoadSuccess: function(data){
			if(data&&data.total==0){
				$("#mattranasset_list").iFold("hide");
			}
			setTimeout(function(){
				$("#mattranasset_list").datagrid("resize");
			},200);
			FW.fixToolbar("#toolbar");
		}
	});
}

//初始化退货列表（通用方法）
function initReturnList(){
	$("#matreturndetail_list").iFold("init");
	$("#matreturndetail_grid").datagrid({
		fitColumns : true,
		columns:returnGrid,
		idField:'tmpid',
		singleSelect:true,
		url : basePath+"inventory/invmattrandetail/queryMatReturnDetail.do",
		queryParams: {"imtid":imtid},
		onLoadSuccess: function(data){
            if(data && data.total==0){
            	$("#matreturndetail_list").iFold("hide");
            }
            var flag = checkReturnable();  //检查是否可以退货
            if(flag){
            	$("#btn_returns").show();
            }else{
            	$("#btn_returns").hide();
            }
			setTimeout(function(){ 
				$("#matreturndetail_grid").datagrid("resize"); 
			},200);
			FW.fixToolbar("#toolbar");
        }
	});
}
//检查是否可以退货
function checkReturnable(){
	//如果没有退货权限，直接返回不能退货，否则继续根据业务逻辑判断是否可以退货
	if(!Priv.hasPrivilege("storeinQ_returns")){
		return false;
	}	
	var listTranData =$("#mattrandetail_grid").datagrid("getRows");
	var listReturnData =$("#matreturndetail_grid").datagrid("getRows");
	var laststockqtySum = 0 ;  //总的已入库数量
	for(var i=0; i<listTranData.length; i++){
		laststockqtySum += listTranData[i].laststockqty; //已入库数量
	}
	//没有退货记录，且接收单明细已入库数量不全为0(防止通知入库页面上面显示退货按钮)
	if(listTranData.length > listReturnData.length && laststockqtySum >0){ 
		return true;
	}
	for(var i=0; i<listTranData.length; i++){
		var tranDataTemp = listTranData[i];
		var tranItemCodeTemp = tranDataTemp.itemcode;  //物资编码
		var tranItemQtyTemp = tranDataTemp.batchstockqty;  //本次接收数量
		var returnItemQtyTemp = 0 ;  //退货数量
		for(var j=0; j<listReturnData.length; j++){
			var returnDataTemp = listReturnData[j];
			var returnItemCodeTemp = returnDataTemp.itemcode;  //退货物资编码
			if(returnItemCodeTemp == tranItemCodeTemp){
				returnItemQtyTemp += returnDataTemp.returnQty;  //退货数量
			}
			//同一类物资的所有退货统计完毕，还小于本次接收的数量，则可以退货
			if(j ==listReturnData.length-1 && returnItemQtyTemp < tranItemQtyTemp){
				return true;
			}
		}
	}
	return false;
}
//检查是否可以退货
//function checkReturnable(){
//	var listTranData =$("#mattrandetail_grid").datagrid("getRows");
//	for(var i=0; i<listTranData.length; i++){
//		var tranDataTemp = listTranData[i];
//		var num1 = tranDataTemp.laststockqty;  //已入库数量
//		var num2 = tranDataTemp.batchstockqty;  //入库数量
////		if(num1 != num2){
//		if(num1 > 0){
//			return true ;
//		}
//	}
//	return false;
//}
//快速查询按钮
function initTranList(){
	var qsi = encodeURIComponent($("#quickSearchInput").val());
	$("#invmattran_grid").iDatagrid("init",{
		singleSelect:true,
		pageSize:pageSize,
		queryParams: { "schfield":qsi },
		url: basePath+"inventory/invmattran/quickSearch.do",
		onLoadSuccess:function(data){
			if(isSearchMode){
            	 if(data && data.total==0){
            		 $("#noSearchResult").show();
            	 }
            	 else{
            		 $("#noSearchResult").hide();
            	 }
            }
			else{
	            if(data && data.total==0){
	                $("#grid_wrap,#toolbar_wrap").hide();
	                $("#grid_error").show();
	            }else{
	            	$("#grid_wrap,#toolbar_wrap").show();
	                $("#grid_error").hide();
	            }
	            $("#noSearchResult").hide();
			}
			setTimeout(function(){
				$("#invmattran_grid").datagrid("resize");
			},200);
			isSearchMode = false;
		},
		onDblClickRow : function(rowIndex, rowData) {
			var url = basePath+ "inventory/invmattran/invMatTranForm.do?imtid="+rowData.imtid+"&openType=read";
	    	var prefix = rowData.imtid;
		    FW.addTabWithTree({
		        id : "editMatTranForm" + prefix,
		        url : url,
		        name : "物资接收",
		        tabOpt : {
		            closeable : true,
		            afterClose : "FW.deleteTab('$arg');FW.activeTabById('stock');FW.getFrame(FW.getCurrentTabId()).refCurPage();"
		        }
		    });
		}
	});
}