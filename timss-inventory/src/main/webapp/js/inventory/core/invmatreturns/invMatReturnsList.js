var grid=[[
		{field:'tmpid',width:10,hidden:true,fixed:true,formatter:function(value,row){
			row.tmpid=tmpid;
			return tmpid++;
		}},
		{title:'接收物资id',field:'imtdid',hidden:true},
		{title:'物资id',field:'itemid',hidden:true},
		{title:'物资类型id',field:'invcateid',hidden:true},
		{title:'物资编号',field:'itemcode',width:90,fixed:true,formatter:function(value,row){
			return "<a onclick='FW.showItemInfo(\""+row.itemcode+"\",\""+row.warehouseid+"\");'>"+row.itemcode+"</a>";
		}},
		{title:'物资名称',field:'itemname',width:90},
		{title:'物资型号',field:'cusmodel',width:70},
		{title:'仓库名称',field:'warehouse',width:100,fixed:true},
		{title:'采购申请单号',field:'puraNo',width:140,fixed:true},
		{title:'采购合同号',field:'purorderno',width:140,fixed:true},
		{title:'采购数量',field:'itemnum',width:60,align:'right',fixed:true},
		{title:'已入库数量',field:'laststockqty',width:80,align:'right',fixed:true},
		{title:'暂存入库数量',field:'stockqtytemp',hidden:true},
		{title:'可退货数量',field:'returnableqty',width:80,align:'right',fixed:true},
		{title:'退货数量',field:'returnQty',width:85,align:'right',fixed:true,edit:true,
			editor:{
				type:'text',
				options:{
					align:"right",
					dataType:"number",
					onBlur:dynaCalcReturnsSum
				}
			}
		},
		{field:'unitname',title:'单位',width:42,fixed:true},
		{field:'warehouseid',title:'仓库id',hidden:true},
		{field:'puraId',title:'采购申请ID',hidden:true}
]];

//初始化列表（通用方法）
function initList(){
	$("#matreturnsdetail_list").iFold("init");
	$("#matreturnsdetail_grid").datagrid({
		fitColumns : true,
		columns:grid,
		idField:'tmpid',
		singleSelect:true 
	});
	//加载用户表单数据
	$.ajax({
		type : "POST",
		async:false,
		url : basePath+"inventory/invmattrandetail/queryMatReturnAbleDetail.do",
		data: {"imtid":imtid,"imrsid":imrsid,"openType": openType},
		dataType : "json",
		success : function(data) {
				imtid = data.imtid;  //接收单ID
				returnReason = data.returnReason;  //退货原因
				for(var i=0; i<data.list.length; i++){
					if(isNaN(data.list[i].returnQty) || null == data.list[i].returnQty){
						data.list[i].returnQty = 0;
					}
					
					$('#matreturnsdetail_grid').datagrid('appendRow',data.list[i]);
					/*if(data.list[i].returnableqty > 0){
						$('#matreturnsdetail_grid').datagrid('appendRow',data.list[i]);
					}*/
					
				}
				var listData =$("#matreturnsdetail_grid").datagrid("getRows");
				if(listData.length == 0){
					 $("#matreturnsdetail_grid").hide();
				}
				if( "" !=imrsid && null != imrsid){
					$("#matreturnsdetail_grid").datagrid("hideColumn","purorderno");
					$("#matreturnsdetail_grid").datagrid("hideColumn","itemnum");
					$("#matreturnsdetail_grid").datagrid("hideColumn","laststockqty");
					$("#matreturnsdetail_grid").datagrid("hideColumn","returnableqty");
				}else{
					//设置可编辑
					startEditAll();
				}
				//初始化表单
				initPageProcess.init(); 
				setTimeout(function(){ 
					$("#matreturnsdetail_grid").datagrid("resize"); 
				},200);
				FW.fixToolbar("#toolbar");
		}
	});
}
