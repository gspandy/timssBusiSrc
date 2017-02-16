var InvAcceptDetailList={
	init:function(data){
		InvAcceptDetailList.initList(data);
		InvAcceptDetailList.initListData(data);
		
	},
	basicColunms:[[
				{field:'puraId',hidden:true},
				{field:'purapplyUsercode',hidden:true},
	   	        {field:'itemcode',title:'物资编号',width:80,fixed:true,formatter:function(value,row){
	   	        	return "<a onclick='FW.showItemInfo(\""+row.itemcode+"\",\""+row.warehouseid+"\",\""+row.invcateid+"\");'>"+row.itemcode+"</a>";
	   			}},
				{field:'itemname',title:'物资名称',width:150},
				{field:'cusmodel',title:'物资型号',width:150},
				{field:'warehouseid',title:'仓库id',hidden:true},
	   			{field:'warehouse',title:'仓库名称',width:100,fixed:true},
				{field:'applysheetno',title:'采购申请编号',width:120,fixed:true},
				{field:'purapplyUser',title:'采购人',width:60,fixed:true},
				{field:'itemnum',title:'采购数量',width:60,align:'right',fixed:true},
				{field:'receivenum',title:'已入库数量',fixed:true,width:75,align:'right',
					formatter:function(value){
	 					if(!value){
	 						value=0;
	 					}
	 					return value;
	 				}
				},
				{field:'unit',title:'单位',width:40,fixed:true},
				{field:'unitid',title:'单位id',hidden:true},
				{field:'binid',title:'货柜id',hidden:true},
				{field:'invcateid',title:'物资分类id',hidden:true},
				{field : 'garbage-colunms',title : '',width : 40,align : 'center',fixed:true,
				    formatter:function(value,row,index){
					     return '<img class="btn-delete btn-garbage" src="'+basePath+'img/inventory/btn_garbage.gif" title="删除当前行" width="16" height="16" >';
					}
				}
		]],
     numColumns:[[
				{field:'puraId',hidden:true},
				{field:'purapplyUsercode',hidden:true},
				{field:'itemcode',title:'物资编号',width:80,fixed:true,formatter:function(value,row){
					return "<a onclick='FW.showItemInfo(\""+row.itemcode+"\",\""+row.warehouseid+"\",\""+row.invcateid+"\");'>"+row.itemcode+"</a>";
	   			}},
	   			{field:'itemname',title:'物资名称',width:150},
	   			{field:'cusmodel',title:'物资型号',width:150},
	   			{field:'warehouseid',title:'仓库id',hidden:true},
	   			{field:'warehouse',title:'仓库名称',width:100,fixed:true},
	   			{field:'applysheetno',title:'采购申请编号',width:120,fixed:true},
	   			{field:'purapplyUser',title:'采购人',width:60,fixed:true},
	   			{field:'itemnum',title:'采购数量',width:75,align:'right',fixed:true},
				{field:'acceptnum',title:'验收数量',width:75,fixed:true,align:'right',
					'editor':{
						type : "text",
   					 	options:{
	   						dataType:"number",
	   						align:'right',
							rules:{
								required:true,
								min :0,
								max:999999,
								messages:{
	   								required:"不能为空",
	   								min :"不能少于0",
	   								max : "数量不能超过999999"	
	   							}
							}
   					 	}
    				}
				},
				{field:'unit',title:'单位',width:40,fixed:true},
				{field:'unitid',title:'单位id',hidden:true},
				{field:'invcateid',title:'物资分类id',hidden:true},
				{field:'binid',title:'货柜id',hidden:true}
				
		]],
	acceptColumns:[[
					{field:'puraId',hidden:true},
					{field:'purapplyUsercode',hidden:true},
					{field:'itemcode',title:'物资编号',width:80,fixed:true,formatter:function(value,row){
						return "<a onclick='FW.showItemInfo(\""+row.itemcode+"\",\""+row.warehouseid+"\",\""+row.invcateid+"\");'>"+row.itemcode+"</a>";
		   			}},
		   			{field:'itemname',title:'物资名称',width:150},
		   			{field:'cusmodel',title:'物资型号',width:150},
		   			{field:'warehouseid',title:'仓库id',hidden:true},
		   			{field:'warehouse',title:'仓库名称',width:100,fixed:true},
		   			{field:'itemnum',title:'采购数量',width:75,align:'right',fixed:true},
					{field:'acceptnum',title:'验收数量',width:75,align:'right',fixed:true},
					{field:'unit',title:'单位',width:40,fixed:true},
					{field:'unitid',title:'单位id',hidden:true},
					{field:'invcateid',title:'物资分类id',hidden:true},
					{field:'binid',title:'货柜id',hidden:true}
			]],
	initList:function(data){
		var colums=InvAcceptDetailList.getColums(data);
		$("#matacceptdetail_list").iFold("init");
		InvAcceptDetailList.dataGrid=$("#matapplydetail_grid").datagrid({
			singleSelect:true,
			fitColumns : true,
			columns:colums,
			onClickCell:function(rowIndex, field, value){
				if(field=='garbage-colunms'){
					InvAcceptDetailList.dataGrid.datagrid('deleteRow',rowIndex);
				}
			}
		});
	},
	initListData:function(data){
		var invMatAcceptDetails=data.invMatAcceptDetails;
	
		if(typeof invMatAcceptDetails != 'undefined'){
			InvAcceptDetailList.dataGrid.datagrid('loadData',invMatAcceptDetails);
			//如果是编辑状态
			if(InvAcceptPriv.isApprovalStatus(data) && invMatAcceptDetails && !InvAcceptPriv.getWorkflowField(data, "columnNotEdit")){
				for(var i=0;i<invMatAcceptDetails.length;i++){
					InvAcceptDetailList.dataGrid.datagrid('beginEdit',i);
				}
			}else if(data.isEdit){
				//如果是编辑页面，同时此时没有编辑权限，隐藏删除栏
				InvAcceptDetailList.dataGrid.datagrid('hideColumn','garbage-colunms');
			}
		}
		
	},
	//获取当前节点需要显示的物资详情columns
	getColums:function(data){
		var columns=InvAcceptDetailList.basicColunms;
		var modi=InvAcceptPriv.getFlowElementMod(data);
		if(modi){
			var columnName=modi.columnName;
			if(columnName){
				columns=InvAcceptDetailList[columnName];
			}
		}else{
			//如果流程为审批通过状态
			if(InvAcceptPriv.isPassApproved(data)){
				columns=InvAcceptDetailList.acceptColumns;
			}
		}
		
		return columns;
	},
	getDataThenRestore:function(data){
		var result=InvAcceptDetailList.dataGrid.datagrid('getRows');
		for(var i=0;i<result.length;i++){
			InvAcceptDetailList.dataGrid.datagrid('endEdit',i);
		}
		if(!InvAcceptPriv.getWorkflowField(data, "columnNotEdit")){
			result=InvAcceptDetailList.dataGrid.datagrid('getRows');
			for(var i=0;i<result.length;i++){
				InvAcceptDetailList.dataGrid.datagrid('beginEdit',i);
			}
		}
		
		return result;
	},
	setDefaultDetailList:function(data,list){
		InvAcceptDetailList.defaultsValue=list;
	},
	getDefaultDetailList:function(){
		return InvAcceptDetailList.defaultsValue;
	}
};

