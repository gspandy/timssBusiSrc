/*************************************************采购申请*************************************************/
//申请明细列表
var applyItemColumns = [[
	{field:'ck',width:10,checkbox:true,hidden:true,fixed:true,title:'物资编号'},
	{field:'listId',title:'多类型列表id',hidden:true},
	{field:'itemid',width:105,fixed:true,title:'物资编号',formatter:function(value,row){
		return "<a onclick='FW.showItemInfo(\""+row.itemid+"\",\""+row.warehouseid+"\");'>"+row.itemid+"</a>";
	}},
	{field:'itemname',title:'物资名称',width:180,fixed:true},
	{field:'cusmodel',title:'型号规格',width:180,fixed:true},
	{field:'warehouseid',title:'仓库id',hidden:true},
	{field:'invcateid',title:'物资分类id',hidden:true},
	{field:'warehouse',title:'仓库名称',width:125,fixed:true},
	{field:'itemnum',width:60,fixed:true,edit:true,align:'right',
		editor:{
			type:'text',
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
		},title:'申请量'
	},
	{field:'repliednum',width:60,fixed:true,edit:true,align:'right',
		editor:{
			type:'text',
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
		},title:'批复量'
	},
	{field:'storenum',width:60,fixed:true,title:'余量',align:'right',styler: function(value){
		if (value == 0){
			return 'color:red';
		}
	},formatter:function(value){
		return parseFloat(value).toFixed(0);
	}},
	{field:'orderunitname',width:45,fixed:true,title:'单位'},
	{field:'averprice',width:90,fixed:true,edit:true,align:'right',
		editor:{
			type:'text',
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
		},title:'单价(元)'
	},
	{field:'priceTotal',width:90,fixed:true,title:'小计(元)',align:'right'},
	{field:'remark',width:1,edit:true,editor:{type:'text'},title:'备注'},
	{field:'status',title:'状态',hidden:true},
	{field:'active',width:10,fixed:true,title:'物资分类是否启用',hidden:true},
	{title:'',align:'center',field:'del',width:40,fixed:true,formatter:function(value,row){
		return "<img class='btn-delete btn-garbage' onclick='delApplyRecord(\""+row.listId+"\",\""+row.itemname+"\");' src='"+basePath+"img/purchase/btn_garbage.gif'/>";
	}}
]];

//商务网列表
var applybusiColumns = [[
	{field:'ck',width:10,checkbox:true,hidden:true,fixed:true,title:'物资编号'},
	{field:'itemid',width:90,fixed:true,title:'物资编号',formatter:function(value,row){
		return "<a onclick='FW.showItemInfo(\""+row.itemid+"\",\""+row.warehouseid+"\");'>"+row.itemid+"</a>";
	}},
	{field:'itemname',title:'物资名称',width:180},
	{field:'cusmodel',title:'型号规格',width:180},
	{field:'itemnum',width:60,fixed:true,edit:true,align:'right',
		editor:{
			type:'text',
			options:{
				align:"right",
				dataType:"digits",
				onBlur:dynaCalcTotalPrice
			}
		},title:'申请数量'
	},
	{field:'repliednum',width:60,fixed:true,edit:true,align:'right',
		editor:{
			type:'text',
			options:{
				align:"right",
				dataType:"digits",
				onBlur:dynaCalcTotalPrice
			}
		},title:'批复数量'
	},
	{field:'storenum',width:60,fixed:true,title:'余量',align:'right',formatter:function(value){
		return parseFloat(value).toFixed(2);
	}},
	{field:'orderunitname',width:45,fixed:true,title:'单位'},
	{field:'averprice',width:90,fixed:true,title:'单价(元)',align:'right'},
	{field:'priceTotal',width:90,fixed:true,title:'小计(元)',align:'right'},
	{field:'warehouseid',title:'仓库id',hidden:true},
	{field:'invcateid',title:'物资分类id',hidden:true},
	{field:'remark',width:150,edit:true,title:'备注'}
]];

//初始化内部标签页的切换事件
function initInnerTab(){
	$("#tab_applyitem").on('shown.bs.tab', function (e) {
		$("#apply_item").datagrid("resize");
		if("block"==$("#btn-add").css("display")||"editable"==isEdit){
			startEditAll();
		}
		$("#btn-nobusiness").hide();
		stopBtnCss();
		FW.fixToolbar("#toolbar1");
	});
	$("#tab_busi").on('shown.bs.tab', function (e) {
		initBusiList(operation[1]);
	});
	$("#tab_order").on('shown.bs.tab', function (e) {
		initOrderList();
		$("#btn-nobusiness").hide();
		FW.fixToolbar("#toolbar1");
	});
	$("#tab_stock").on('shown.bs.tab', function (e) {
		initStockList();
		$("#btn-nobusiness").hide();
		FW.fixToolbar("#toolbar1");
	});
	$("#tab_mat").on('shown.bs.tab', function (e) {
		initMatApplyList();
		$("#btn-nobusiness").hide();
		FW.fixToolbar("#toolbar1");
	});
	$("#tab_impl").on('shown.bs.tab', function (e) {
		initApplyImplList();
		$("#btn-nobusiness").hide();
		FW.fixToolbar("#toolbar1");
	});
}

//初始化列表（通用方法）
function initApplyList(listType){
	var sendType = "0";
	
	$("#apply_item").datagrid({
		singleSelect :"last"==processStatus?false:true,
		fitColumns : true,
		idField:"listId",
		columns:applyItemColumns,
		url : basePath+"purchase/purapply/queryPurApplyItemList.do",
		scrollbarSize : 0,
		queryParams: {
			'sheetId': sheetId,
			'type':listType,
			'sendType':sendType
		},
		onLoadSuccess: function(){
			var listData =$("#apply_item").datagrid("getRows");
			//如果是退回首环节的单据要控制批复数量
			if(isRollBack == "Y" && processStatus == "first_save"){
				$("#apply_item").datagrid("getColumnOption",'repliednum').editor = "{'type':'label'}";
			}
			
			//若最后一个环节
			if(processStatus == "last"){
				//列表没有数据返回
				if(listData.length == 0){
					//隐藏列表
					$("#foldable_area").iFold("hide");
				}
				var statusFlag = false;
				for(var i=0;i<=listData.length;i++){
					if(statusFlag){
						i=0;
						statusFlag = false;
					}
					if(typeof(listData[i]) != "undefined" && listData[i] != null){
						if(listData[i].status !=1){
							$('#apply_item').datagrid('deleteRow',$('#apply_item').datagrid('getRowIndex',listData[i].listId));
							statusFlag = true;
						}
					}else{
						break;
					}
				}
			}else if(processStatus == "over"){//如果流程结束
				var listData = $("#apply_item").datagrid("getRows");
				if (listData.length == 0) {
					$("#foldable_area").iFold("hide");
				}
				initBusiList(operation[1]);
			}
			var repliednum = 0;
			for(var i=0;i<listData.length;i++){
				rowCalu(i, listData[i]);
				repliednum = listData[i].repliednum;
				if("" == repliednum && 0!=repliednum){
					$("#apply_item").datagrid("updateRow",{
						index: i,
						row: { "repliednum": listData[i].itemnum }
					});
				}
			}
			//若是可以编辑的状态
			if('edit' == operation[0]){
				//这里有个非常特殊的情景，新建和退回首环节的采购类型需要限定范围
				if("draft"==process&&"editable" == isEdit){
					editApplyForm(editdraft_single);
				}else{
					editApplyForm(edit_single);
				}
				$("#autoform").ITC_Form("readonly");
				if(processStatus != "last"){
					$("#apply_item").datagrid("hideColumn","ck");
				}
			}else{
				$("#autoform").ITC_Form({validate:true,fixLabelWidth:true},eval(type));
				if("" == sheetId){
					$("#autoform").iForm("setVal",{tatolcost:"0.00"});
				}
				$("#autoform").iForm("setVal",{dept:orgName});
				$("#autoform").iForm("setVal",{source:sourceCome});
			}
						
			if(editStatus){
				$("#btn-edit").trigger("click");
			}
			//initFormStatus = $("#autoform").iForm("getVal");
			//initListStatus = FW.stringify(listData);
			
			setTimeout(function(){ 
				addItemFromSource();
				$("#apply_item").datagrid("resize"); 
			},200);
			//如果不是退回的单据不用控制隐藏批复数量
			if(isRollBack != "Y"){
				$("#apply_item").datagrid("hideColumn","repliednum");
			}
			if("pre_cost" == process&&"editable" == isEdit){
				$("#autoform").iForm("beginEdit", "isToBusiness");
			}
			if(("draft"==process|| "pre_cost"==process|| "prd_dept_audit"==process||
    			"office_deptmanager_audit"==process|| "lab_deptmanager_audit"==process|| "it_director_audit"==process||
    				"prd_dept_manager_audit"==process) && "editable" == isEdit){
				$("#autoform").iForm("beginEdit", "assetNature");
			}
		},
		onRenderFinish : function() {
			stopBtnCss();
			FW.fixToolbar("#toolbar1");
			$("#apply_item").iFixCheckbox();
			//需要在最后重新确定一下初始表单值
			var listData =$("#apply_item").datagrid("getRows");
			//如果页面的cataName非空时，初始化物资分类值--begin
			var enumArray = FW.parseEnumData("ITEMCLASS_TYPE",_enum);
			var itemclassidNew = "";
			for(var i = 0 ;i<enumArray.length;i++){
				if(null!=cateName&&""!=cateName&&enumArray[i][1]==cateName){
					itemclassidNew = enumArray[i][0];
				}
			}
			if(""!=itemclassidNew){
				$("#autoform").iForm("setVal",{itemclassid:itemclassidNew});
			}
			//如果页面的cataName非空时，初始化物资分类值--end
			
			initFormStatus = $("#autoform").iForm("getVal");
			initListStatus = FW.stringify(listData);
		},
		onDblClickRow : function(rowIndex,rowData) {
			var src = basePath + "purchase/purapply/itemDetail.do?sheetId="+sheetId+"&itemid="+rowData.itemid;
		    var btnOpts = [];
		    var dlgOpts = {
		        width :640,
		        height:480,
		        title:"物资信息详情"
		    };
		    var gridEditable = ""!=oper?JSON.parse(oper).editable:"";
		    if(undefined!=gridEditable&&!gridEditable){
		    	FW.dialog("init",{"src":src,"dlgOpts":dlgOpts,"btnOpts":btnOpts});
		    }
		}
	});
}

//发送到商务网列表
function initBusiList(listType) {
	var sendType = "1";
	$("#apply_busi").datagrid({
		fitColumns : true,
		idField : 'listId',
		columns : applyItemColumns,
		url : basePath + "purchase/purapply/queryPurApplyItemList.do",
		scrollbarSize : 0,
		queryParams : {
			'sheetId' : sheetId,
			'type' : listType,
			'sendType' : sendType
		},
		onLoadSuccess : function(data) {
			$("#apply_busi").datagrid("hideColumn", "del");
			if (data && data.total == 0) {
				$("#no_busi").show();
				$("#apply_busi_Grid").hide();
				$("#btn-nobusiness").hide();
				FW.fixToolbar("#toolbar1");
			} else {
				$("#no_busi").hide();
				$("#apply_busi_Grid").show();
				if (Priv.hasRole("SWF_CG")||Priv.hasRole("SWF_CGY")) {
					var active = $("#tab_busi").attr("class");
					if("active"==active){
						$("#btn-nobusiness").show();	
					}
					$("#apply_busi").datagrid("showColumn","ck");
					FW.fixToolbar("#toolbar1");
				}
			}
		},
		onRenderFinish:function(){
			caluPrice();
			setTimeout(function(){ 
				$("#apply_busi").datagrid("resize");
			},200);
			$("#apply_busi").iFixCheckbox();
			stopBtnCss();
			FW.fixToolbar("#toolbar1");
			//需要在最后重新确定一下初始表单值
			var listData =$("#apply_item").datagrid("getRows");
			initFormStatus = $("#autoform").iForm("getVal");
			initListStatus = FW.stringify(listData);
		}
	});
}

function initApplyTable(){
	//初始化列表(单独申请)
	$("#table_apply").iDatagrid( "init",{
		singleSelect:true,
    	pageSize:pageSize,//默认每页显示的数目 只能从服务器取得
        url: basePath+"purchase/purapply/queryPurApply.do",
        onLoadSuccess : function(data){
            //远程无数据需要隐藏整个表格和对应的工具条，然后在无数据信息中指引用户新建
        	if(isSearchMode1){
            	 if(data && data.total==0){
            		 $("#noSearchResult1").show();
            	 }
            	 else{
            		 $("#noSearchResult1").hide();
            	 }
            }
        	else{
	            if(data && data.total==0){
	                $("#grid1_wrap,#toolbar_wrap").hide();
	                $("#grid_error").show();
	            }else{
	                $("#grid1_wrap,#toolbar_wrap").show();
	                $("#grid_error").hide();
	            }
	            $("#noSearchResult1").hide();
        	}
        	setTimeout(function(){ 
        		$("#table_apply").datagrid("resize"); 
        	},200);
            isSearchMode1 = false;
        },
        onDblClickRow : function(rowIndex, rowData) {
			var url = basePath+ "purchase/purapply/purApplyForm.do?type=edit_single&sheetId="+rowData.sheetId;
	    	var prefix = rowData.sheetId;
		    FW.addTabWithTree({
		        id : "editApplyForm" + prefix,
		        url : url,
		        name : "采购申请",
		        tabOpt : {
		            closeable : true,
					afterClose : "FW.deleteTab('$arg');FW.activeTabById('purchasing');FW.getFrame(FW.getCurrentTabId()).refCurPage();"
		        }
		    });
		}
    });
}

//已执行采购物资列表
function initOrderList() {
	
	//已执行采购物资列表
	var applyOrderColumns = [[
		{field:'itemid',width:105,fixed:true,title:'物资编号',formatter:function(value,row){
			return "<a onclick='FW.showItemInfo(\""+row.itemid+"\",\""+row.warehouseid+"\");'>"+row.itemid+"</a>";
		}},
		{field:'itemname',title:'物资名称',width:180,fixed:true},
		{field:'itemcus',title:'型号规格',width:180,fixed:true},
		{field:'orderSheetNo',title:'采购合同',width:125,fixed:true,formatter:function(value,row){
			return "<a onclick='FW.showOrderInfo(\""+row.orderSheetNo+"\");'>"+row.orderSheetNo+"</a>";
		}},
		{field:'businessno',title:'由商务网询价',width:90,fixed:true,formatter:function(value,row){
			if("" == value||isNull(value)){
				return "否";
			}else{
				return "是";
			}
		}},
		{field:'itemnum',width:60,fixed:true,edit:true,
			editor:{
				type:'label',
				options:{
					align:"right",
					dataType:"digits"
				}
			},title:'采购数量'
		},
		{field:'cost',width:85,fixed:true,title:'税后单价(元)'},
		{field:'priceTotal',width:65,fixed:true,title:'小计(元)'},
		{field:'warehouseid',title:'仓库id',hidden:true},
		{field:'remark',width:150,edit:true,title:'备注'}
	]];
	
	
	$("#apply_order").datagrid({
		singleSelect:true,
		fitColumns : true,
		idField : 'itemid',
		columns : applyOrderColumns,
		url : basePath + "purchase/purorder/queryPurApplyOrderItemList.do",
		scrollbarSize : 0,
		queryParams : {
			'sheetId' : sheetId
		},
		onLoadSuccess : function(data) {
			if (data && data.total == 0) {
				$("#no_order").show();
				$("#apply_order_Grid").hide();
			}else{
				$("#no_order").hide();
				$("#apply_order_Grid").show();
			}
			setTimeout(function(){ 
				$("#apply_order").datagrid("resize");
			},200);
		}
	});
	
}
//已入库物资列表
function initStockList() {
	
	var applyStockColumns = [[
  		{field:'itemid',title:'物资id',hidden:true,fixed:true},
		{field:'itemcode',title:'物资编号',width:105,fixed:true,formatter:function(value,row){
			return "<a onclick='FW.showItemInfo(\""+row.itemcode+"\",\""+row.warehouseid+"\");'>"+row.itemcode+"</a>";
		}},
  		{field:'itemname',title:'物资名称',width:180,fixed:true},
  		{field:'cusmodel',title:'型号规格',width:180,fixed:true},
  		{field:'imtid',title:'接收id',hidden:true,fixed:true},
  		{field:'sheetno',title:'入库单号',width:125,fixed:true,formatter:function(value,row){
  			return "<a onclick='FW.showStockInfo(\""+row.imtid+"\");'>"+row.sheetno+"</a>";
  		}},
  		{field:'createdate',title:'入库日期',sortable:true,width:105,fixed:true,
			formatter:function(value,record){
			    return FW.long2date(record.createdate);
		    }
		},
  		{field:'warehousename',title:'仓库',width:90,fixed:true},
  		{field:'warehouseid',title:'仓库id',hidden:true},
  		{field:'invcateid',title:'物资分类id',hidden:true},
  		{field:'binname',title:'货柜',width:90}
  	]];
	
	$("#apply_stock").datagrid({
		singleSelect:true,
		fitColumns : true,
		idField : 'itemid',
		columns : applyStockColumns,
		url : basePath + "purchase/purorder/queryPurApplyStockItemList.do",
		scrollbarSize : 0,
		queryParams : {
			'sheetId' : sheetId
		},
		onLoadSuccess : function(data) {
			if (data && data.total == 0) {
				$("#no_stock").show();
				$("#apply_stock_Grid").hide();
			}else{
				$("#no_stock").hide();
				$("#apply_stock_Grid").show();
			}
		},
		onRenderFinish : function(){
			setTimeout(function(){ 
				$("#apply_stock").datagrid("resize");
			},200);
		}
	});
	
}
//相关领料单列表
function initMatApplyList() {
	var matApplyColumns = [[
	    {field:'imaid',title:'领料id',hidden:true,fixed:true},
  		{field:'sheetno',title:'领料单号',width:105,fixed:true,formatter:function(value,row){
  			return "<a onclick='viewMatApply(\""+row.imaid+"\");'>"+row.sheetno+"</a>";
		}},
  		{field:'sheetname',title:'领料名称',width:180,fixed:true},
  		{field:'createuser',title:'申请人',width:90,fixed:true},
  		{field:'createdate',title:'创建日期',sortable:true,width:105,fixed:true,
			formatter:function(value,record){
			    return FW.long2date(record.createdate);
		    }
		},
		{field:'status',title:'状态',width:90}
  	]];
	$("#apply_mat").datagrid({
		singleSelect:true,
		fitColumns : true,
		idField : 'matApplyCode',
		columns : matApplyColumns,
		url : basePath + "purchase/purapply/queryRelateMatApplyList.do",
		scrollbarSize : 0,
		queryParams : {
			'sheetId' : sheetId
		},
		onLoadSuccess : function(data) {
			if (data && data.total == 0) {
				$("#no_mat").show();
				$("#apply_mat_Grid").hide();
			}else{
				$("#no_mat").hide();
				$("#apply_mat_Grid").show();
			}
		},
		onRenderFinish : function(){
			setTimeout(function(){ 
				$("#apply_mat").datagrid("resize");
			},200);
		}
	});
}
//初始化执行情况列表
function initApplyImplList (){
	dataGrid = $("#apply_impl").iDatagrid("init",{
        pageSize:pageSize,//pageSize为全局变量
        url: basePath + "purchase/purorder/queryPurApplyImplemetationStatusList.do?sheetId="+sheetId,	//basePath为全局变量，自动获取的       
        columns:[[ 
				{field:"itemcode",title:"物资编号",width:105,fixed:true},
				{field:"itemname",title:"物资名称",width:180,fixed:true},   
				{field:"cusmodel",title:"型号规格",width:180,fixed:true},
				{field:"isToBusiness",title:"经商务网",width:60,fixed:true},
				{field:"repliednum",title:"批准数量",width:60,fixed:true,align:'right'},
				{field:"itemnum",title:"采购数量",width:60,fixed:true,align:'right'},
				{field:"sheetno",title:"采购合同",width:105,fixed:true},
				{field:"applyPrice",title:"预算单价(元)",width:110,fixed:true,align:'right'},
				{field:"orderPrice",title:"合同税后价(元)",width:110,fixed:true,align:'right'},
				{field:"invNum",title:"已入库",width:60,align:'right',fixed:true},
				{field:'empty',title:'',width:100}
			]],
       onLoadSuccess: function(data){
	        if(data && data.total==0){
	        	$("#no_impl").show();
				$("#apply_impl_Grid").hide();
	        }else{
	        	$("#no_impl").hide();
	        	$("#apply_impl_Grid").show();
	        }
	        setTimeout(function(){ 
				$("#apply_impl").datagrid("resize");
			},200);
        }
    });
}
/*************************************************采购申请*************************************************/