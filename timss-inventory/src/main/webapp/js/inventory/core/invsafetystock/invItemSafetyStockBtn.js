var InvSafetyStockBtn={
	init:function(){
		InvSafetyStockBtn.initItemSearch();
		InvSafetyStockBtn.purapply();
		InvSafetyStockBtn.goSet();
		$("#btn_purapply").attr("disabled",true);
	},
	initItemSearch:function(){
		$("#item_search").keypress(function(e) {
		    if(e.which == 13) {
		    	refreshData();
		    }
		});
		$("#item_search").iInput("init",{
			"onClickIcon":function(){
		    	refreshData();
			}
		});
	},
	purapply:function(){
		$("#btn_purapply").click(function(){
			var data =$("#item_grid").datagrid("getChecked");
			var cateName = "";
			for(var i=0;i<data.length;i++){
				if(""==cateName&&data[i].cateName!=cateName){
					cateName = encodeURI(encodeURI(data[i].cateName));
				}else if(""!=cateName&&encodeURI(encodeURI(data[i].cateName))!=cateName){
					FW.error("请选择相同物资分类的主项目");
					return false;
				}
			}
			FW.set("safetyStockPurapply",data);
			var url = basePath+ "purchase/purapply/purApplyForm.do?type=new_single&source=safetyStock&cateName="+cateName+"&sheetId=";
		    var prefix = new Date().getTime() + "" + Math.floor(Math.abs(Math.random()*100));
		    FW.addTabWithTree({
		        id : "newApplyForm" + prefix,
		        url : url,
		        name : "采购申请",
		        tabOpt : {
		            closeable : true,
		            afterClose : "FW.deleteTab('$arg');FW.activeTabById('stock');FW.getFrame(FW.getCurrentTabId()).refCurPage('table_apply');"
		        }
		    });
		});	
	},
	goSet:function(){
		$("#btn_goset").click(function(){
			var url = basePath+ "inventory/invitem/invItemList.do?opentype=new";
		    var prefix = new Date().getTime() + "" + Math.floor(Math.abs(Math.random()*100));
		    FW.addTabWithTree({
		        id : "newItemList" + prefix,
		        url : url,
		        name : "主数据列表",
		        tabOpt : {
		            closeable : true,
		            afterClose : "FW.deleteTab('$arg');FW.activeTabById('stock');setTimeout(function(){FW.getFrame(FW.getCurrentTabId()).refCurPage('item_grid');},200);"
		        }
		    });
		});	
	}
};