var InvItemHistoryBtn = {
	init:function(){
		InvItemHistoryBtn.search();
		InvItemHistoryBtn.toinv();
	},
	toinv:function(){
		$("#toinv").click(function(){
			var codeArr = [];
			var listData =$("#item_grid").datagrid("getSelections");
			if( listData.length <= 0 ){
				FW.error( "请选择物资！ ");
				return;
			}
			
			for(var i=0;i<listData.length;i++){
				codeArr.push(listData[i].itemcode);
			}
			var itemCodes = codeArr.join(",");
			var url = basePath+"inventory/invitem/updateTurnToWuzi.do?itemCodes=" + itemCodes;
			
			FW.confirm("确定所选项转为物资库存吗？", function() {
				$.ajax({
					url : url,
					type : 'post',
					dataType : "json",
					success : function(data) {
						if( data.result == "success" ){
							FW.success( "转为物资库存成功 ！");
							$("#item_grid").datagrid("reload");
						}else{
							FW.error( "转为物资库存失败 ！");
						}
					}
				});
			});
		});
	},
	search:function(){
		$("#btn_search").click(function(){
		    if(isSearchLineShow){
		    	isSearchLineShow=false;
		        $("#item_grid").iDatagrid("endSearch");
		    }
		    else{
		    	isSearchLineShow=true;
		       	$("#item_grid").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(arg){
		       		isSearchMode = true;
					return {"search":JSON.stringify(arg)};
				}});
		    }
		});
	}
};