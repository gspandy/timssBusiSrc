var InvItemBtn = {
	init:function(){
		InvItemBtn.search();
		InvItemBtn.matapply();
		InvItemBtn.mattransfer();
		InvItemBtn.news();
		InvItemBtn.tohistory();
		InvItemBtn.snopshot();
		InvItemBtn.initRealTimeData();
	},
	matapply:function(){
		$("#btn_matapply").click(function(){
			var codeArr = [];
			var listData =$("#item_grid").datagrid("getSelections");
			for(var i=0;i<listData.length;i++){
				if(listData[i].nowqty<=0){
					FW.error( "请检查所选物资，至少一项当前可用库存量为0 ");
					return;
				}
				codeArr.push(listData[i].itemcode + "_" + listData[i].cateId);
			}
			
			var codes = codeArr.join(",");
			var url = basePath+ "inventory/invmatapply/invMatApplyForm.do?imaid=&codes="+codes;
	    	var prefix = new Date().getTime() + "" + Math.floor(Math.abs(Math.random()*100));
		    FW.addTabWithTree({
		        id : "newMatApplyForm" + prefix,
		        url : url,
		        name : "物资领料",
		        tabOpt : {
		            closeable : true,
		            afterClose : "FW.deleteTab('$arg');FW.activeTabById('stock');FW.getFrame(FW.getCurrentTabId()).refCurPage();"
		        }
		    });
		});	
	},
	mattransfer:function(){
		$("#btn_mattransfer").click(function(){
			var itemIdArr = [];
			var itemCodeArr = [];
			var cateTypeIdArr = [];
			var listData =$("#item_grid").datagrid("getSelections");
			if(listData.length==0){
				FW.error( "请先选择物资");
				return;
			}
			var wareHouseFromId = null;
			var wareHouseFromName = null;
			for(var i=0;i<listData.length;i++){
				if(wareHouseFromId == null){
					wareHouseFromId = listData[i].warehouseid;
					wareHouseFromName = listData[i].warehouse;
				}				
				else {
					if(wareHouseFromId != listData[i].warehouseid){
						FW.error( "请选择同一仓库物资");
						return;
					}
				}
				if(listData[i].stockqty<=0){
					FW.error( "请检查所选物资，至少一项当前库存数量为0 ");
					return;
				}
				itemIdArr.push(listData[i].itemid);
				itemCodeArr.push(listData[i].itemcode);
				cateTypeIdArr.push(listData[i].cateId2);//cateId可能是父级缓存物资类型
			}
			
			var itemIds = itemIdArr.join(",");
			var itemCodes = itemCodeArr.join(",");
			var cateTypeIds = cateTypeIdArr.join(",");
			var url = basePath+ "inventory/invmattransfer/invMatTransferForm.do?itemIds="+itemIds
					+ "&itemCodes=" + itemCodes 
					+ "&cateTypeIds=" + cateTypeIds 
					+ "&wareHouseFromId=" + wareHouseFromId 
					+ "&wareHouseFromName=" + wareHouseFromName;
	    	var prefix = new Date().getTime() + "" + Math.floor(Math.abs(Math.random()*100));
		    FW.addTabWithTree({
		        id : "invMatTransferForm" + prefix,
		        url : url,
		        name : "移库申请",
		        tabOpt : {
		            closeable : true,
		            afterClose : "FW.deleteTab('$arg');FW.activeTabById('stock');"
		        }
		    });
		});	
	},
	news:function(){
		$("#btn_new").show();
		$("#btn_new,.btn-new").click(function(){
			FW.showInvMatDetailPage("");
	    });
	},
	search:function(){
		$("#btn_search").click(function(){
		    if(isSearchLineShow){
		    	isSearchLineShow=false;
		        $("#item_grid").iDatagrid("endSearch");
		    }else{
		    	isSearchLineShow=true;
		       	$("#item_grid").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(arg){
		       		isSearchMode = true;
					return {"search":JSON.stringify(arg)};
				}});
		    }
		});
	},
	tohistory:function(){
		$("#tohistory").click(function(){
			var codeArr = [];
			var listData =$("#item_grid").datagrid("getSelections");
			if( listData.length <= 0 ){
				FW.error( "请选择物资！ ");
				return;
			}
			
			for(var i=0;i<listData.length;i++){
				/*if(listData[i].stockqty!=0){
					FW.error( "请检查所选物资，至少一项当前库存数量不为0 ");
					return;
				}*/
				codeArr.push(listData[i].itemcode);
			}
			var itemCodes = codeArr.join(",");
			var url = basePath+"inventory/invitem/updateTurnToHistory.do?itemCodes=" + itemCodes;
			
			FW.confirm("确定所选项转为历史库存吗？", function() {
				$.ajax({
					url : url,
					type : 'post',
					dataType : "json",
					success : function(data) {
						if( data.result == "success" ){
							FW.success( "转为历史库存成功 ！");
							$("#item_grid").datagrid("reload");
						}else{
							if(data.isZero){
								if( data.isBusyCode != null && data.isBusyCode != ""){
									FW.error( "正在使用物资编号为： " + data.isBusyCode + " ,转为历史库存失败!" );
								}else{
									FW.error( "转为历史库存失败 ！");
								}
							}else{
								FW.error( data.msg );
							}
						}
					}
				});
			});
		});
	},
	snopshot:function(){
		$("#snopshot").click(function(){
			FW.confirm("生成库存快照|确定生成库存历史快照?", function() {
				FW.remark(function(val){
					 $.ajax({
		     			url : basePath+"inventory/invmatsnapshot/saveAsSnapshot.do",
		     			type : 'POST',
		     			data: {
		     					"remark":val,
		     					"type":"M"
		     				  },
		     			dataType : "json",
		     			success : function(data) {
		     				if( data.result == "success" ){
		     					FW.success( "快照生成成功 ！");
		     				}else{
		     					FW.error( "快照生成失败 ！");
		     				}
		     			}
		     		});
				});
			});
		});
	},
	initRealTimeData:function(){
		$("#btn_initRealTimeData").click(function(){
			FW.confirm("您确定要初始化物资实时数据吗？",function(){
				$("#btn_initRealTimeData").button('loading');
				
				var itemCodes = [];
				var listData =$("#item_grid").datagrid("getSelections");
				for(var i=0;i<listData.length;i++){
					itemCodes.push(listData[i].itemcode);
				}
				
				$.ajax({
	     			type : 'GET',
	     			url : basePath+"inventory/invitem/initRealTimeData.do",
	     			data: {
     					"itemCodes":itemCodes.toString()
     				},
	     			success : function(data) {
	     				if( data.result == "success" ){
	     					FW.success( "初始化实时数据成功" );
	     				}else{
	     					FW.error( "初始化实时数据失败" );
	     				}
	     				$("#btn_initRealTimeData").button('reset');
	     			}
	     		});
	        });
			
		});	
	}
};