function viewPurApply(sheetNo){
	var url = basePath+ "purchase/purapply/purApplyForm.do?type=edit_single&sheetNo="+sheetNo+"&sheetId=null";
	var prefix = sheetNo;
    FW.addTabWithTree({
        id : "editApplyForm" + prefix,
        url : url,
        name : "采购申请",
        tabOpt : {
            closeable : true,
			afterClose : "FW.deleteTab('$arg');FW.activeTabById('"+FW.getCurrentTabId()+"');FW.getFrame(FW.getCurrentTabId()).refCurPage();"
        }
    });
}
//初始化列表（通用方法）
function initList(){
	var columns = [[
		{field:'listId',title:'多类型列表id',formatter:function(value,row){var newval = value+"_"+row.applySheetId; row.listId = newval; return newval;},hidden:true},
		{field:'itemid',width:90,fixed:true,title:'物资编号',formatter:function(value,row){
			return "<a onclick='FW.showItemInfo(\""+row.itemid+"\",\""+row.warehouseid+"\");'>"+row.itemid+"</a>";
		}},
		{field:'applySheetId',width:10,title:'sheetId',hidden:true},
		{field:'itemname',width:180,fixed:true,title:'物资名称'},
		{field:'itemcus',width:180,fixed:true,title:'型号规格'},
		{field:'warehouseid',title:'仓库id',hidden:true},
		{field:'invcateid',title:'物资分类id',hidden:true},
		{field:'warehouse',title:'仓库名称',width:70,fixed:true},
		{field:'sheetno',width:110,fixed:true,title:'采购申请编号',formatter:function(value,row){
			return "<a onclick='viewPurApply(\""+row.sheetno+"\");'>"+row.sheetno+"</a>"}},
		{field:'createUserName',width:60,fixed:true,title:'申请人'},
		{field:'itemnum',width:50,fixed:true,title:'采购量',edit:true,align:'right',
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
		{field:'orderunitname',width:40,fixed:true,title:'单位'},
		{field:'averprice',width:90,fixed:true,title:'税前单价(元)',hidden:true},
		{field:'tax',width:60,fixed:true,title:'税费(元)',hidden:true},
		{field:'taxRate',width:70,fixed:true,title:'税率(%)',align:'right',
		 editor:{ 
				type : 'combobox',
				options : {
					data : FW.parseEnumData("PUR_ORDER_TAXRATE",_enum),
					rules:{required:true,"number" : true},
					initOnChange: false,
					onChange:function(val,obj){
						if(formInited&&datagridInited){
							singleSelectControlCombox(val,obj);
						}
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
		{field:'cost',width:90,fixed:true,title:'税后单价(元)',align:'right',edit:true,editor:{type:'text',options:{align:"right",dataType:"number",onBlur:dynaCalcTotalPrice}}},
		{field:'taxTotal',title:'税费(元)',align:'right',formatter:function(value,row,index){return (row.tax*row.itemnum).toFixed(2)},hidden:true},
		{field:'priceTotal',width:70,fixed:true,title:'小计(元)',align:'right'},
		{field:'remark',width:1,edit:true,editor:{type:'text'},title:'备注'},
		{title:'',align:'center',field:'del',width:40,fixed:true,formatter:function(value,row){
			return "<img class='btn-delete btn-garbage' onclick='delRecord(\""+row.listId+"\");' src='"+basePath+"img/purchase/btn_garbage.gif'/>";
		}}
	]];
	
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
			
			var f_status = $("#f_status").val();
			/*因为receivestatus这个字段被取消了。
			if( f_status== 0){
				$("#order_item").datagrid('hideColumn','receivestatus');
			}
			*/
			//若商务网编号存在，则屏蔽流程信息按钮,注意新建页面没有businessno这个字段
			if(""!=sheetId){
				var f_businessno = $.trim($("#autoform").iForm("getVal","businessno"));
				if("" != f_businessno && null != f_businessno){
					//$("#btn-process").hide();	//商务网回来的采购合同进入待办，需要走流程，审批信息按钮显示出来（TIM-927）。
					//FW.fixToolbar("#toolbar1");
				}else{
					$("#autoform").ITC_Form("hide",["businessno"]);
				}
			}
			var listData =$("#order_item").datagrid("getRows");
			var hasCIAPriv = ""==process&&""!=processInstId&&1==privMapping.acceptList_new&&"作废"!=status;
			//20160105 modify by yuanzh 列表物资全部接收后控制物资验收按钮显示
			for(var i = 0;i<listData.length;i++){
				if(hasCIAPriv&&listData[i].receivestatus != 'ALLREV'){
					$("#btn-createInvAccept").show();
					break;
				}else{
					$("#btn-createInvAccept").hide();
				}
			}
			
			//列表物资全部接收后控制终止按钮隐藏
			var allrevNum = 0;
			for(var i = 0;i<listData.length;i++){
				if(listData[i].receivestatus == 'ALLREV'){
					allrevNum++;
				}
			}
			if(allrevNum==listData.length){
				$("#btn-stopOrder").hide();
			}
			FW.fixToolbar("#toolbar1");
		},
		onAfterEdit:function(rowIndex, rowData){
			caluRow(rowData,rowIndex);
		},
		onDblClickRow : function(rowIndex,rowData) {
			var src = basePath + "purchase/purorder/itemDetail.do?sheetId="+sheetId+"&itemid="+rowData.itemid;
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
		},
		onRenderFinish : function() {
			dynaCalcTotalPrice();
			var listData =$("#order_item").datagrid("getRows");
			initListStatus = FW.stringify(listData);
			initFormStatus = $("#autoform").iForm("getVal");
			//数据表格初始化完成
			setTimeout(function(){datagridInited = true;},0);
			if(("editable"==isEdit || ""==processInstId)&&'edit' == type){
				setTimeout(function(){ 
					startEditAll();
					$("#order_item").datagrid("resize"); 
				},1000);
			}else{
				setTimeout(function(){ 
					$("#order_item").datagrid("resize"); 
				},200);
			}
		}
	});
}
//初始化内部标签页的切换事件
function initInnerTab(){
	$("#tab_orderitem").on('shown.bs.tab', function (e) {
		$("#order_item").datagrid("resize");
		if("block"==$("#btn-add").css("display")){
			startEditAll();
		}
	});
	$("#tab_accept").on('shown.bs.tab', function (e) {
		initAcceptList();
	});
	$("#tab_pay").on('shown.bs.tab', function (e) {
		initPurPayList();
	});
	$("#tab_contract").on('shown.bs.tab', function (e) {
	});
	$("#tab_doinglist").on('shown.bs.tab', function (e) {
		initDoingList();
	});
}
//修复分页时没有气泡提示
function repairTips(){
	$("td div").each(function(){
		var $this = $(this);
		if(!$this.attr('title') && this.offsetWidth < this.scrollWidth){
			$this.attr('title', $this.text());
		} 
	});
}
//详细列表页面
function initListList(){
	//初始化列表
	$("#table_order").iDatagrid( "init",{
		singleSelect:true,
    	pageSize:pageSize,//默认每页显示的数目 只能从服务器取得
        url: basePath+"purchase/purorder/queryPurOrder.do",
        onLoadSuccess : function(data){
        	if(isSearchMode){
            	 if(data && data.total==0){
            		 $("#noSearchResult").show();
            	 }
            	 else{
            		 $("#noSearchResult").hide();
            	 }
            }
        	else{
	            if(data && data.total==0){
	                $("#grid_wrap,#toolbar_wrap").hide();
	                $("#grid_error").show();
	            }else{
	            	$("#grid_wrap,#toolbar_wrap").show();
	                $("#grid_error").hide();
	            }
	            $("#noSearchResult").hide();
        	}
        	setTimeout(function(){ 
        		$("#table_order").datagrid("resize"); 
        	},200);
        	isSearchMode = false;
        	repairTips();
        },
        onDblClickRow : function(rowIndex, rowData) {
			var url = basePath+ "purchase/purorder/purOrderForm.do?type=edit&sheetId="+rowData.sheetId+"&sheetNo="+rowData.sheetno;
	    	var prefix = rowData.sheetId;
		    FW.addTabWithTree({
		        id : "editOrderForm" + prefix,
		        url : url,
		        name : "采购合同",
		        tabOpt : {
		            closeable : true,
		            afterClose : "FW.deleteTab('$arg');FW.activeTabById('purchasing');FW.getFrame(FW.getCurrentTabId()).refCurPage();"
		        }
		    });
		}
    });
}

//初始化付款记录表格的方法
function initPurPayList(){
	var purPaycolumns = [[
		{field:'payId',title:'付款id',hidden:true},
		{field:'payNo',width:150,fixed:true,title:'付款编号'},
		{field:'payType',width:80,fixed:true,title:'款项类别',formatter:function(value){
			var data=FW.parseEnumData("PUR_PAYTYPE",_enum);
			var r="";
			for(var i in data){
				if(data[i][0]==value){
					r=data[i][1];
					break;
				}
			}
			return r;
		}},
		{field:'pay',width:70,fixed:true,title:'金额(元)',align:'right'},
		{field:'createrName',width:70,fixed:true,title:'经办人'},
		{field:'createdate',width:150,fixed:true,title:'创建日期',formatter:function(value){
			return FW.long2date(value);
		}},
		{field:'transactor',width:120,fixed:true,title:'当前办理人'},
		{field:'status',width:1,title:'状态',formatter:function(value){
			var data=FW.parseEnumData("PUR_PAYSTATUS",_enum);
			var r="";
			for(var i in data){
				if(data[i][0]==value){
					r=data[i][1];
					break;
				}
			}
			return r;
			}
		}
	]];
	$("#purpay_list").datagrid({
		fitColumns : true,
		scrollbarSize:0,
		columns:purPaycolumns,
		queryParams: {
			"sheetId": sheetId
		},
		url : basePath+"/purchase/purpay/queryPurPayList.do",
		onLoadSuccess : function(data){
			if(data && data.total==0){
				$("#no_pay").show();
        		$("#order_purPayGrid").hide();
        		$("#autoform").iForm("setVal",{totalPay:0});
            }else{
            	var len = data.rows.length;
            	var totalPayVal = 0;
        		for( var index = 0; index < len; index ++ ){
        			if("processed"==data.rows[index].status){
        				totalPayVal += data.rows[index].pay;
        			}
        		}
            	$("#autoform").iForm("setVal",{totalPay:totalPayVal});
            	$("#no_pay").hide();
            	$("#order_purPayGrid").show();
            }
        },
        onDblClickRow : function(rowIndex, rowData) {
			var url = basePath+ "purchase/purpay/purPayForm.do?sheetId="+sheetId+"&operType=edit&payId="+rowData.payId+"&payType="+rowData.payType+"&refreshTabId="+FW.getCurrentTabId();
	    	var suffix = rowData.payId;
		    FW.addTabWithTree({
		        id : "editPayForm" + suffix,
		        url : url,
		        name : "付款",
		        tabOpt : {
		            closeable : true,
		            afterClose : "FW.deleteTab('$arg');FW.activeTabById('"+FW.getCurrentTabId()+"');FW.getFrame(FW.getCurrentTabId()).refreshPayList();"
		        }
		    });
		},
        onRenderFinish:function(){
        	setTimeout(function(){ 
				$("#purpay_list").datagrid("resize"); 
			},200);
        }
	});
}

//初始化验收记录
function initAcceptList(){
	var acceptColumns = [[
		{field:'inacId',title:'验收id',hidden:true},
		{field:'inacNo',width:150,fixed:true,title:'验收单号'},
		{field:'createdate',width:150,fixed:true,title:'申请验收日期',formatter:function(value){
			return FW.long2date(value);
		}},
		{field:'status',title:'状态',width:80,fixed:true,formatter:function(value){
			var data=FW.parseEnumData("ACPT_STATUS",_enum);
			var r="";
			for(var i in data){
				if(data[i][0]==value){
					r=data[i][1];
					break;
				}
			}
			return r;
			}
		},
		{field:'acptCnlus',width:1,title:'验收结论',formatter:function(value){
			var data=FW.parseEnumData("ACPT_CNLUS",_enum);
			var r="";
			for(var i in data){
				if(data[i][0]==value){
					r=data[i][1];
					break;
				}
			}
			return r;
			}
		}
	]];
	$("#accept_list").datagrid({
		fitColumns : true,
		scrollbarSize:0,
		columns:acceptColumns,
		queryParams: {
			"search": FW.stringify({"poSheetno":sheetNo})
		},
		url : basePath+"/inventory/invmataccept/queryInvMatAcceptList.do",
		onLoadSuccess : function(data){
			if(data && data.total==0){
        		$("#no_accept").show();
				$("#order_acceptGrid").hide();
            }else{
            	$("#no_accept").hide();
            	$("#order_acceptGrid").show();
            }
        },
        onDblClickRow : function(rowIndex, rowData) {
        	var url = basePath+"inventory/invmataccept/invMatAcceptFormJsp.do?inacId="+rowData.inacId;
			var prefix = new Date().getTime() + "" + Math.floor(Math.abs(Math.random()*100));
			FW.addTabWithTree({
			        id : "editAcceptForm" + prefix,
			        url : url,
			        name : "物资验收",
			        tabOpt : {
			            closeable : true,
			            afterClose : "FW.deleteTab('$arg');FW.activeTabById('"+FW.getCurrentTabId()+"');FW.getFrame(FW.getCurrentTabId()).refreshAcceptList();"
			        }
			});
		},
        onRenderFinish:function(){
        	setTimeout(function(){ 
				$("#accept_list").datagrid("resize"); 
			},200);
        }
	});
}

function refreshPayList(){
	$("#purpay_list").datagrid("reload");
 	setTimeout(function(){ 
 		$("#purpay_list").datagrid("resize"); 
 	},800);
}

function refreshAcceptList(){
	$("#accept_list").datagrid("reload");
 	setTimeout(function(){ 
 		$("#accept_list").datagrid("resize"); 
 	},800);
}


function initDoingList(){
	$("#table_apply").datagrid({
		fitColumns : true,
		scrollbarSize:0,
        url: basePath + "purchase/purorder/orderDoingStatusList.do?sheetNo="+sheetNo,	//basePath为全局变量，自动获取的       
        queryParams: {
			"search": FW.stringify({"sheetNo":sheetNo})
		},
        columns:[[ 
				{field:"itemCode",title:"物资编号",width:90,fixed:true},
				{field:"itemname",title:"物资名称",width:180,fixed:true},   
				{field:"itemcus",title:"规格型号",width:180,fixed:true}, 
				{field:"itemnum",title:"采购量",width:70,fixed:true,align:'right'},
				{field:"inSum",title:"已入库",width:70,fixed:true,align:'right'}, 
				{field:"payedSum",title:"已报账",width:70,fixed:true,align:'right'},
				{field:'empty',title:'',width:100}
			]],
       onLoadSuccess: function(data){
	        if(data && data.total==0){
	        	$("#no_doinglist").show();
	        	$("#table_apply_Grid").hide();
	        }else{
	        	$("#no_doinglist").hide();
	        	$("#table_apply_Grid").show();
	        }
        },
        onRenderFinish:function(){
        	setTimeout(function(){ 
				$("#table_apply").datagrid("resize"); 
			},200);
        }
    });
}
//初始化合同条款
function initContractContent(){
	$.ajax({
		type : "POST",
		url: basePath+"purchase/purorder/queryPurOrderPolicyAndReceipt.do",
		data: {
			"sheetId":sheetId
		},
		dataType : "json",
		success : function(data) {
			initContractDetailsListByData("contract_itemInfoGrid",data.contractDetailList);
			if("editable"==isEdit&&gridEditable||'new' == type||"autoGenerate"==operOrder){
				beginEditContractItemList("contract_itemInfoGrid");
			}else{
				endEditContractItemList("contract_itemInfoGrid");
			}
		}
	});
}