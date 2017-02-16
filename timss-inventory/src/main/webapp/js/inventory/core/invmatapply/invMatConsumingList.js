//发料列表字段	
var csColumns = [[
	{field:'itemcode',title:'物资编号',width:90,fixed:true,formatter:function(value,row){
		return "<a onclick='FW.showItemInfo(\""+row.itemcode+"\",\""+row.warehouseid+"\");'>"+row.itemcode+"</a>";
		}},
	{field:'itemid',title:'物资id',hidden:true},
	{field:'itemname',title:'物资名称',width:425},
	{field:'stockqty',title:'现有库存',width:80,align:'right',fixed:true,formatter:function(value){
			return parseFloat(value).toFixed(2);
		}},
	{field:'waitqty',title:'待领用量',width:80,align:'right',fixed:true},
	{field:'outstockqty',title:'本次领用量',width:90,align:'right',fixed:true,edit:true,editor:{type:'text',options:{align:"right",dataType:"number",onBlur:checkQtyIsOver}}},
	{field:'outqtytemp',title:'临时字段',hidden:true},
	{field:'warehouseid',title:'仓库id',hidden:true},
	{field:'warehouse',title:'仓库名称',width:110,fixed:true},
	{field:'invcateid',title:'物资分类id',hidden:true}
]];

//初始化列表（通用方法）
function initConsumingList(){
	$("#matapplydetail_list").iFold("init");
	$("#matapplydetail_grid").datagrid({
		singleSelect:true,
		columns:csColumns,
		fitColumns:true,
			url : basePath+"inventory/invmatapplydetail/queryMatApplyDetailCSList.do",
			idField:"itemid",
		queryParams: {"imaid":imaid,"embed":0},
		onLoadSuccess: function(){
			startEditAll();
			var dataRow = $("#matapplydetail_grid").datagrid("getRows");
			for(var i = 0;i<dataRow.length;i++){
				if(dataRow[i].outstockqty == 0){
					$("#matapplydetail_grid").datagrid("endEdit",i);
				}else{
					counterBefore++;
				}
			}
			setTimeout(function(){ 
				$("#matapplydetail_grid").datagrid("resize"); 
			},200);
        },
        onAfterEdit:function(rowIndex, rowData){
        	counterAfter = 0;
        	var outstockqty = rowData.outstockqty;
			if(outstockqty == 0){
				counterAfter++;
			}
		}
	});
}