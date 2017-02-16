var columns = [[
			{field:'parentid',title:'资产编码（父级）',width:120,fixed:true},
			{field:'assetid',title:'资产编码',width:80,fixed:true},
			{field:'assetname',title:'设备名称',width:250,fixed:true},
			{field:'spec',title:'专业',width:50,fixed:true},
			{field:'description',title:'资产说明/备注',width:120,fixed:true},
			{field:'modeldesc',title:'详细参数',width:70},
			{field:'manufacturer',title:'制造商',width:90,fixed:true},
			{field:'producedate',title:'生产日期',width:70,fixed:true},
			{field:'isroot',title:'根节点',width:50,fixed:true},
			{field:'cumodel',title:'物资型号',width:70,fixed:true},
			{field:'remark',title:'出错备注',width:120,formatter:function(value,row){
				if(null == value){
					value = "";
				}
				return "<a onclick='FW.showErrorInfo(\""+value+"\");'>"+value+"</a>";
			}},
			{field:'status',title:'状态',hidden:true}
	]];


function initList(){
	var url = null;
	if(searchFlag){
		url = basePath+"asset/astorganizedata/queryAstOrganizeDataList.do";
		
	}
	
	$("#astODUpload_grid").iDatagrid("init",{
		singleSelect:true,
		pageSize:pageSize,//默认每页显示的数目 只能从服务器取得
		url:url,
		columns:columns,
		fitColumns:true,
		rowStyler: function(index,row){
			if (row.status == 1){
				return 'color:#ED0024;'; 
			}
		},
		onLoadSuccess: function(data){
			if(searchFlag){
				$.ajax({
					type : "POST",
					async: false,
					url: basePath+"asset/astorganizedata/queryAstOrganizeDataByStatus.do",
					dataType : "json",
					success : function(data) {
						var counter = data.listSize;
						if(counter > 0){
							FW.confirm("导入异常|导入数据中存在"+counter+"条异常数据，请修改后重新导入",{
							});
						}else{
							FW.confirm("导入确定|所有校验通过，是否确认导入数据|导入后将覆盖所有数据",{
								onConfirm : function(){
									doInitData();
								}
							});
						}
						searchFlag = false;
					}
				});
			}
		}
	});
}

function doInitData(){
	$.ajax({
		type : "POST",
		async: false,
		url: basePath+"asset/astorganizedata/callAstOrganizeDataInit.do",
		dataType : "json",
		success : function(data) {
			if(data.result == 'success'){
				FW.success("初始化数据完成");
			}else{
				FW.error("数据分发失败");
			}
		}
	});
}

FW.showErrorInfo = function(remark){
	FW.confirm("错误信息|"+remark,{});
};