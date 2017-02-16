/*************************************************采购付款*************************************************/
//付款明细列表
var payDtalColumns = [[
	{field:'payDtlId',title:'付款明细Id',hidden:true},
	{field:'payId',title:'付款Id',hidden:true},
	{field:'invcateid',title:'物资类型',hidden:true},
	{field:'itemCode',title:'物资编号',width:90,fixed:true,formatter:function(value,row){
			return "<a onclick='FW.showItemInfo(\""+row.itemCode+"\",\""+row.warehouseId+"\");'>"+row.itemCode+"</a>";
		}
	},
	{field:'itemName',title:'物资名称',width:1},
	{field:'customModel',title:'物资型号',width:1},
	{field:'unit',title:'单位',width:45,fixed:true},
	{field:'applyNum',title:'采购量',width:70,fixed:true,align:'right'},
	{field:'noSendAccount',title:'未报账',width:70,fixed:true,hidden:true},
	{field:'sendAccount',title:'本次报账',width:70,fixed:true,align:'right',edit:false},
	{field:'noTaxPrice',title:'不含税单价',width:90,fixed:true,align:'right',
		formatter:function(value,row){
			var result = "";
			if(0<row.sendAccount){
				result = (row.notaxTotal/row.sendAccount).toFixed(4);
			}else{
				result = "0";
			}
			return result;
	}
	},
	{field:'taxPrice',title:'含税单价',width:90,fixed:true,align:'right',
		formatter:function(value,row){
			var result = "";
			if(0<row.sendAccount){
				result = ((parseFloat(row.taxTotal)+parseFloat(row.notaxTotal))/row.sendAccount).toFixed(4);
			}else{
				result = "0";
			}
			return result;
	}},
	{field:'taxTotal',title:'税额',width:90,fixed:true,edit:true,align:'right',
		formatter:function(value,row){
			if(undefined!=value){
				return parseFloat(value).toFixed(2);
			}
		},
		editor:{
			type:'text',
			options:{
				align:"right",
				dataType:"number",
				rules : {
					 "number" : true
				},
			    messages : {
					 "number" : "请输入有效数字"
			    },
			    initOnChange: false,
				onChange:function(val,obj,row){
					refreshPrice(this);
				}
			}
		}
	},
	{field:'notaxTotal',title:'不含税金额',width:90,fixed:true,edit:true,align:'right',
		formatter:function(value,row){
			if(undefined!=value){
				return parseFloat(value).toFixed(2);
			}
		},
		editor:{
			type:'text',
			options:{
				align:"right",
				dataType:"number",
				rules : {
					 "number" : true
				},
			    messages : {
					 "number" : "请输入有效数字"
			    },
			    initOnChange: false,
				onBlur:function(){
					refreshPrice(this);
				}
			}
		}
	},
	{field:'status',title:'状态',hidden:true},
	{field:'imtdId',title:'库存流水Id',hidden:true},
	{field:'warehouseId',title:'仓库Id',hidden:true}
]];

function refreshPrice(obj){
	//统计表格，更新表单
	endEditAll();
	var rows = $("#purPays_dtl").datagrid("getRows");
	//只有当具备开放编辑条件的时候，才能恢复编辑
	if("editable"==isEdit){
		if( "arrivepay"==payType||"settlepay"==payType){
			startEditAll();
		}
	}
	var tax = 0 ;
	var notax = 0;
	for(var i=0;i<rows.length;i++){
		if(0<rows[i].sendAccount){
			var taxTotal = rows[i].taxTotal;
			var notaxTotal = rows[i].notaxTotal;
			taxTotal = ""!=taxTotal?taxTotal:0;
			notaxTotal = ""!=notaxTotal?notaxTotal:0;
			tax += parseFloat(taxTotal);
			notax += parseFloat(notaxTotal);
		}
	}
	$("#autoform").iForm("setVal",{"noTaxTotal":notax,"taxTotal":tax,"total":parseFloat(tax)+parseFloat(notax)});
}
/*************************************************采购付款*************************************************/