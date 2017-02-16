//初始化列表（通用方法）
function initList(){
	//审批过程中列表字段
	var columns = [[
	        {field:'imadid',title:'申请明细id',hidden:'true'},
			{field:'itemcode',title:'物资编号',width:90,fixed:true,formatter:function(value,row){
				return "<a onclick='FW.showItemInfo(\""+row.itemcode+"\",\""+row.warehouseid+"\",\""+row.invcateid+"\");'>"+row.itemcode+"</a>";
			}},
			{field:'itemid',title:'物资id',width:10,fixed:true,hidden:true},
			{field:'invcateid',title:'物资类型id',hidden:true},
			{field:'itemname',title:'物资名称',width:90},
			{field:'cusmodel',title:'型号规格',width:90},
			{field:'warehousename',title:'仓库名称',width:100,fixed:true},
			{field:'outqty',title:'已领料数量',width:80,align:'right',fixed:true},
			{field:'refundableqty',title:'可退库量',width:80,align:'right',fixed:true},
			{field:'refundqty',title:'退库数量',width:90,align:'right',fixed:true,edit:true,
				editor:{
					type:'text',
					options:{
						align:"right",
						dataType:"number",
						onBlur:checkRefundNum
					}
				}
			},
			{field:'unitid',title:'单位id',hidden:true},
			{field:'warehouseid',title:'仓库id',hidden:true},
			{field:'binid',title:'货柜id',hidden:true},
			{field:'noTaxPrice',title:'税前单价',hidden:true},
			{field:'tax',title:'税额',hidden:true},
			{field:'unitname',title:'单位',width:50,fixed:true}
	]];
	
	var url = basePath+"inventory/invmatrefund/queryMatRefundDetailList2.do";
	var titleColumns = columns;
	var idField="itemid";
	
	$("#matrefunddetail_list").iFold("init");
	$("#matrefunddetail_grid").datagrid({
		singleSelect:true,
		columns:titleColumns,
		idField:idField,
		fitColumns:true
	});
	//加载用户表单数据
	$.ajax({
		type : "POST",
		async:false,
		url : url,
		data: {"imaid":imaid,"imrsid":imrsid,"sheetno":sheetno,"embed":embed},
		dataType : "json",
		success : function(data) {
				imaid = data.imaid;  //申请单ID
				refundReason = data.refundReason;  //退库原因
				for(var i=0; i<data.list.length; i++){
					$('#matrefunddetail_grid').datagrid('appendRow',data.list[i]);
				}
				var listData =$("#matrefunddetail_grid").datagrid("getRows");

				if(embed != "1"){
					if(imrsid == "" || imrsid == null){
						for(var i=0;i<listData.length;i++){  //设置可编辑
							listData[i].refundqty = 0;
							$('#matrefunddetail_grid').datagrid('updateRow',{index: i,row:listData[i]});
							$("#matrefunddetail_grid").datagrid("beginEdit",i);
						}
					}else{
						$("#matrefunddetail_grid").datagrid("hideColumn","outqty");
						$("#matrefunddetail_grid").datagrid("hideColumn","refundableqty");
					}
				}else{
					$("#matrefunddetail_grid").datagrid("hideColumn","refundableqty"); //隐藏可退货数量
					$("#toolbar_wrap").hide();
				}
				//form表单的初始化
				initForm();
				setTimeout(function(){
					$("#matrefunddetail_grid").datagrid("resize");
				},200);
				FW.fixToolbar("#toolbar");
		}
	});
	
}


