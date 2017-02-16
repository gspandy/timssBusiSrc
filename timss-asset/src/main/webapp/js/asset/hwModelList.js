var HwModel={
		objs:{},

		init:function(){
			HwModel.objs["isSearchMode"]=false;
			HwModel.objs["isSearchLineShow"]=false;
			
			HwModel.objs["form"]=[[
				{field:'modelType',title:'类型',width:200,sortable:true,fixed:true,'editor':{
						 "type" : "combobox",
						 "options" : {
							 data:FW.parseEnumData("AST_HW_MODEL_TYPE",_enum)
						 }
					 },formatter:function(value,row,index){
						 return FW.getEnumMap("AST_HW_MODEL_TYPE")[value];
					 }
				},
				{field:'modelName',title:'名称',width:200}
			]];
			
			HwModel.objs["datagrid"] = $("#hwModelList").iDatagrid("init",{
				singleSelect:true,
				columns:HwModel.objs.form,
				pageSize:pageSize,
				fitColumns:true,
				url: basePath + "asset/hwModel/getList.do",
				onLoadSuccess:function(data){
					HwModel.changeShow(data);
				},
				onDblClickRow : function(rowIndex, rowData) {
					HwModel.toShow(rowData);
				}
			});
		},
		
		toShowSearchLine:function(){
			if(HwModel.objs.isSearchLineShow){
				HwModel.objs.isSearchLineShow=false;
		        $("#hwModelList").iDatagrid("endSearch");		        
		    }
		    else{
		    	HwModel.objs.isSearchLineShow=true;
		       	$("#hwModelList").iDatagrid("beginSearch",{noSearchColumns:{2:true},"remoteSearch":true,"onParseArgs":function(arg){
		       		HwModel.objs.isSearchMode = true;
					return {"search":JSON.stringify(arg)};
				}});
		    }
		},
		
		toCreate:function(){
			var url=basePath+"asset/hwModel/detailPage.do?mode=create";
			//FW.navigate(url);
			addTabWithTreeDataGrid( "addHwModel" , "硬件类型", url,"equipment", "hwModelList");
		},

		toShow:function(obj){
			var url=basePath+"asset/hwModel/detailPage.do?mode=view&hwModelId="+obj.modelId;
			//FW.navigate(url);
			addTabWithTreeDataGrid( "editHwModel"+obj.modelId , "硬件类型", url,"equipment", "hwModelList");
		},
		
		changeShow:function(data){//切换模式，用于控制按钮和文字的改变
			//关闭查询无结果的功能
			//HwModel.objs.isSearchMode=null;
			
			if(HwModel.objs.isSearchMode){
		        //搜索时的无数据信息
		        if(data && data.total==0){
		            $("#noSearchResult").show();
		        }
		        else{
		            $("#noSearchResult").hide();
		        }
		    } 
		    else{
		        //初始化时的无数据信息        
		    	if(data && data.total==0){
	                $("#grid_wrap,.toolbar-with-pager").hide();
	                $("#grid_empty").show();
	            }else{
	            	$("#grid_wrap,.toolbar-with-pager").show();
	                $("#grid_empty").hide();
	            }
		        //这句要有 否则弹起按钮时信息没法隐藏
		        $("#noSearchResult").hide();
		    }
		}
};