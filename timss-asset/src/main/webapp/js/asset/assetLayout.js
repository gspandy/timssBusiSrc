var AssetLayout={
	objs:{
		datagridCloumns:[[
			{field:'type',title:'配置名称',width:200,fixed:true,editor:{
		        type:"text",
		        options:{
		        	rules:{required:true,maxChLength : parseInt(120*2/3)}
		        }
			}},
			{field:'value',title:'配件',width:400,fixed:true,editor:{
		        type:"text",
		        options:{
		        	rules:{required:true,maxChLength : parseInt(240*2/3)}
		        }
			}},
			{field:'layoutId',title:' ',width:40,fixed:true,formatter: function(value,row,index){
					return "<img src='"+basePath+"img/asset/btn_garbage.gif' class='btn-garbage'/>";
				}
			},
			{field:'blankField',title:' ',width:40}   
		]]
	},
	
	getAssetId:function(){
		return Asset.objs["bean"].assetId;
	},
	
	init:function(){
		$("#assetLayout_info").iFold("init");
		
		$("#assetLayout_table").datagrid({
			columns:AssetLayout.objs["datagridCloumns"],
			fitColumns : true,
			//idField:"layoutId",
			pageSize:9999,
			singleSelect:true,
			url:basePath+"asset/layout/queryAllByAssetId.do",
			queryParams :{'assetId': AssetLayout.getAssetId},
			onLoadSuccess:function(data){
				AssetLayout.objs["layout"]=data.rows?data.rows:[];
				AssetLayout.changeState();
			},
			onRenderFinish : function(){
				
			},
			onDblClickRow : function(rowIndex, rowData) {
				
			},
			onClickRow:function (rowIndex, rowData){
				
			},
	        onClickCell : function(rowIndex, field, value) {
				if (field == 'layoutId') {
					AssetLayout.delLayout(rowIndex);
				}
			}
		});
	},	
	
	loadData:function(data){
		if(data){
			$("#assetLayout_table").datagrid("loadData",data);
		}else{
			$("#assetLayout_table").datagrid("reload");
		}
	},
	
	changeState:function(state){//read/edit
		if(state){
			AssetLayout.objs["state"]=state;
		}
		if(!AssetLayout.objs["state"]){
			AssetLayout.objs["state"]="read";
		}
		
		if(!AssetLayout.objs["layout"]){//未初始化完成要退出
			return;
		}
		
		if(AssetLayout.objs["state"]=="read"&&AssetLayout.objs["layout"].length==0){//无数据时，控制datagrid是否显示
			$("#assetLayout_info").css({"height":0});
			$("#assetLayout_info").iFold("hide");
		}else{
			$("#assetLayout_info").iFold("show");
        	$("#assetLayout_info").css({"height":"auto"});	
        	$("#assetLayout_table").datagrid("resize");
        	
        	if(AssetLayout.objs["state"]=="read"){//控制添加删除按钮
        		$("#layoutBtn").hide();
    			$("#assetLayout_table").datagrid("hideColumn","layoutId");
        		AssetLayout.changeEditDatagrid(false);
    		}else if(AssetLayout.objs["state"]=="edit"){
    			AssetLayout.changeAddLayoutBtn($("#assetLayout_table").datagrid('getRows').length);
    			$("#layoutBtn").show();
    			$("#assetLayout_table").datagrid("showColumn","layoutId");
    			AssetLayout.changeEditDatagrid(true);
    		}
		}
	},
	
	changeEditDatagrid:function(isBegin,isOnlyLast,isOnlyUpdate){
		var type=(isBegin?"begin":"end")+"Edit";
		var rowSize = $("#assetLayout_table").datagrid('getRows').length;
		if(isOnlyLast){
			$("#assetLayout_table").datagrid(type, rowSize-1);
		}else{
			for( var i = 0 ; i < rowSize; i++ ){
		    	$("#assetLayout_table").datagrid(type, i);
			}
		}
	},
	
	changeAddLayoutBtn:function(num){
		if(num==0){
			$("#btnAddLayout").html("添加配置");
		}else{
			$("#btnAddLayout").html("继续添加配置");
		}
	},
	
	addLayout:function(){
		$('#assetLayout_table').datagrid('appendRow',{});
		AssetLayout.changeEditDatagrid(true,true);
	},
	
	delLayout:function(rowIndex,name){
		//Notice.confirm("确认删除|是否确定要删除设备配置“"+name+"”？",function(){
		$('#assetLayout_table').datagrid('deleteRow',rowIndex);
		var rows=$("#assetLayout_table").datagrid("getRows");
		AssetLayout.changeAddLayoutBtn(rows.length);
		$("#assetLayout_table").datagrid("resize");
		//});
	},
	
	importTemplate:function(){
		var fieldId=Asset.getDyFormFieldByName("资产类型");
		var type=$("input[name="+fieldId+"]").val();
		if(type==""){
			FW.error("请先选择资产类型");
			return;
		}
		var typeName=$("input[name="+fieldId+"]").next().html();
		Notice.confirm("确认导入“"+typeName+"”设备配置的模板|原有的设备配置信息将会被清空？",function(){
			var rows=$("#assetLayout_table").datagrid("getRows");
			for( var i = rows.length ; i >0; i-- ){
		    	$("#assetLayout_table").datagrid('deleteRow', $("#assetLayout_table").datagrid('getRowIndex',rows[i-1]));//清空原数据
			}
			$.post(basePath + "asset/layout/queryTemplateByAssetType.do",
				{assetType:type},
				function(data){
					if(data.total>0){
						for(var i=0;i<data.total;i++){
							$('#assetLayout_table').datagrid('appendRow',{type:data.rows[i].field_name});
							AssetLayout.changeState();
						}
					}
				},
				"json"
			);
		});
	},
	
	getDataForSubmit:function(){
		AssetLayout.changeEditDatagrid(false);
		var addRows=$("#assetLayout_table").datagrid('getChanges','inserted');
		var delRows=$("#assetLayout_table").datagrid('getChanges','deleted');
		var updateRows=$("#assetLayout_table").datagrid('getChanges','updated');
		AssetLayout.changeEditDatagrid(true);
		
		return {
			assetId: AssetLayout.getAssetId(),
			addRows:JSON.stringify(addRows),
			delRows:JSON.stringify(delRows),
			updateRows:JSON.stringify(updateRows)
		};
	},
	
	submitChange:function(){
		$.post(basePath + "asset/layout/submitChange.do",
			AssetLayout.getDataForSubmit(),
			function(data){
				if(data.result == "ok"){
					//FW.success("设备配置修改成功");
					AssetLayout.loadData();
				}else{
					FW.error("设备配置修改失败");
				}
			},
			"json"
		);
	},
	
	validDatagrid:function(){
		return $("#assetLayout_form").valid();
	}
};