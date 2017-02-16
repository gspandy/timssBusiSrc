var columns = [[
			{field:'warehouseName',title:'仓库名称',width:100,fixed:true},
			{field:'containerName',title:'货柜名称',width:130,fixed:true},
			{field:'categroyO',title:'物资分类（一级）',width:120,fixed:true},
			{field:'categroyS',title:'物资分类（二级）',width:130,fixed:true},
			{field:'itemName',title:'物资名称',width:120,fixed:true},
			{field:'cusmodel',title:'型号规格',width:70},
			{field:'qty',title:'数量',width:70,fixed:true},
			{field:'unitName',title:'单位',width:50,fixed:true},
			{field:'price',title:'单价(元)',width:90,fixed:true},
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
		url = basePath+"inventory/invorganizedata/queryInvOrganizeDataList.do";
	}
	
	$("#invODUpload_grid").iDatagrid("init",{
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
					url: basePath+"inventory/invorganizedata/queryOrganizeDataByStatus.do",
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