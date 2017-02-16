//发料列表字段	
var columns = [[
	{field:'imrdid',title:'imrdid',fixed:true,hidden:true},
	{field:'itemcode',title:'物资编号',width:80,fixed:true,
		formatter:function(value,row){
			return "<a onclick='FW.showItemInfo(\""+row.itemcode+"\",\""+row.warehouseid+"\",\""+row.invcateid+"\");'>"+row.itemcode+"</a>";
		}
	},
	{field:'itemname',title:'物资名称',width:90},
	{field:'cusmodel',title:'型号规格',width:70},
	{field:'invcatename',title:'物资类别',width:100,fixed:true},
	{field:'qtyApply',title:'申请数量',width:70,align:'right',fixed:true},
	{field:'outstockqty',title:'发料数量',width:60,align:'right',fixed:true},
	{field:'actualQty',title:'当前实际库存',width:90,align:'right',fixed:true,
		formatter:function(value,row){
			if(null === value || "" === value){
				return "--";
			}
			else{
				return value;
			}
		}	
	},
	{field:'unit1',title:'单位',width:42,fixed:true},
	{field:'price',title:'单价(元)',width:80,align:'right',fixed:true,
		formatter:function(value,row){
			if("Y" == row.isled){
				return parseFloat(row.price).toFixed(4);
			}
			else{
				return "未发料";
			}
		}
	},
	{field:'total',title:'金额(元)',width:80,align:'right',fixed:true,
		formatter:function(value,row){
			if("Y" == row.isled){
				return parseFloat(row.price * row.outstockqty).toFixed(2);
			}
			else{
				return "未发料";
			}
		}
	},
	{field:'unitCode1',title:'单位编码',fixed:true,hidden:true},
	{field:'imadid',title:'imadid',fixed:true,hidden:true},
	{field:'invcateid',title:'物资分类id',fixed:true,hidden:true},
	{field:'warehouse',title:'仓库',width:90,fixed:true},
	{field:'bin',title:'货柜',width:90,fixed:true,hidden:true},
	{field:'itemid',title:'itemid',fixed:true,hidden:true},
	{field:'warehouseid',title:'仓库id',hidden:true},
	{field:'binid',title:'binid',fixed:true,hidden:true},
	{field:'remark',title:'remark',fixed:true,hidden:true}
]];

//初始化列表（通用方法）
function initList(){
	$("#matrecdetail_list").iFold("init");
	$("#matrecdetail_grid").datagrid({
		singleSelect:true,
		columns:columns,
		fitColumns:true,
		url : basePath+"inventory/invmatrecipients/queryInvMatRecipientsList.do",
		idField:"imrdid",
		queryParams: {"imrid":imrid}
	});
	if(siteid=="ITC"){
		$("#matrecdetail_grid").datagrid("hideColumn","price");
		$("#matrecdetail_grid").datagrid("hideColumn","total");
	}
}