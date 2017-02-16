//列表字段
var columns = [[
	{field:'listId',title:'多类型列表id',formatter:function(value,row){var newval = value+"_"+row.applySheetId; row.listId = newval; return newval;},hidden:true},
	{field:'itemid',width:90,fixed:true,title:'物资编号',formatter:function(value,row){
		return "<a onclick='FW.showItemInfo(\""+row.itemid+"\",\""+row.warehouseid+"\");'>"+row.itemid+"</a>";
	}},
	{field:'applySheetId',width:10,title:'sheetId',hidden:true},
	{field:'itemname',width:90,title:'物资名称'},
	{field:'itemcus',width:90,title:'型号规格'},
	{field:'itemnum',width:60,fixed:true,title:'采购量',edit:true,align:'right',
		editor:{
			type:'label',
			options:{
				align:"right",
				dataType:"number",
				onBlur:dynaCalcTotalPrice,
				rules : {
					 "number" : true
				},
			    messages : {
					 "number" : "请输入有效数字"
			    }
			}
		}
	},
	{field:'orderunitname',width:45,fixed:true,title:'单位'},
	{field:'averprice',width:90,fixed:true,title:'税前单价(元)',align:'right'},
	{field:'tax',width:60,fixed:true,title:'税费(元)',align:'right'},
	{field:'taxRate',width:70,fixed:true,title:'税率(%)',align:'right',
		 editor:{ 
				type : 'combobox',
				options : {
					data : FW.parseEnumData("PUR_ORDER_TAXRATE",_enum),
					rules:{required:true,"number" : true},
					initOnChange: false,
					onChange:function(val,obj){
						singleSelectControlCombox(val,obj);
					},
				    messages : {
						 "number" : "请输入有效数字"
				    }
				}
		 },
		 formatter:function(value){
			var data=FW.parseEnumData("PUR_ORDER_TAXRATE",_enum);
			var r="";
			for(var i in data){
				if(data[i][0]==value){
					r=data[i][1];
					break;
				}
			}
			return r;
		}},
	{field:'tempCost',title:'税后单价(暂存)',hidden:true},
	{field:'cost',width:90,fixed:true,title:'税后单价(元)',align:'right',edit:true,editor:{type:'text',options:{
		align:"right",
		dataType:"number",
		onBlur:dynaCalcTotalPrice,
		rules : {
			max : '@tempCost',
			messages : {max :"输入价格不能大于原来的价格"}
		}
	}}},
	{field:'taxTotal',title:'税费(元)',formatter:function(value,row,index){return row.tax*row.itemnum},hidden:true},
	{field:'priceTotal',width:70,fixed:true,align:'right',title:'小计(元)'},
	{field:'remark',width:150,edit:true,editor:{type:'text'},title:'备注'},
	{field:'warehouseid',title:'仓库id',hidden:true},
	{field:'invcateid',title:'物资分类id',hidden:true},
	{title:'',align:'center',field:'del',width:40,fixed:true,formatter:function(value,row){
		return "<img class='btn-delete btn-garbage' onclick='delRecord(\""+row.listId+"\");' src='"+basePath+"img/purchase/btn_garbage.gif'/>";
	}}
]];


		
//初始化列表（通用方法）
function initList(){
	$("#order_item").datagrid({
	singleSelect:true,
	fitColumns : true,
	idField:'listId',
	scrollbarSize:0,
	columns:columns,
	url : basePath+"/purchase/purorder/queryPurOrderItemList.do",
	queryParams: {
		"sheetId": sheetId,
		"queryMode": queryMode
	},
	onLoadSuccess: function(){
		if(("editable"==isEdit || ""==processInstId)&&'edit' == type){
			$("#btn-edit").trigger("click");
		}
		$("#order_item").datagrid("hideColumn","tax");
		setTimeout(function(){ 
			$("#order_item").datagrid("resize"); 
		},200);
	},
	onAfterEdit:function(rowIndex, rowData){
			caluRow(rowData,rowIndex);
	},
	onRenderFinish : function() {
		dynaCalcTotalPrice();
		var listData =$("#order_item").datagrid("getRows");
		initListStatus = FW.stringify(listData);
		initFormStatus = $("#autoform").iForm("getVal");
	}
	});
}