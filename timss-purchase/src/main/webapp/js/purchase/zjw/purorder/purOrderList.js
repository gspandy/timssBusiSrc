//初始化列表（通用方法）
function initList(){
	var columns = [[
		{field:'listId',title:'多类型列表id',formatter:function(value,row){var newval = value+"_"+row.applySheetId; row.listId = newval; return newval;},hidden:true},
		{field:'itemid',width:70,fixed:true,title:'物资编号',formatter:function(value,row){
			return "<a onclick='FW.showItemInfo(\""+row.itemid+"\",\""+row.warehouseid+"\");'>"+row.itemid+"</a>";
		}},
		{field:'applySheetId',width:10,title:'sheetId',hidden:true},
		{field:'itemname',width:90,title:'物资名称'},
		{field:'itemcus',width:110,title:'型号规格'},
		{field:'warehouseid',title:'仓库id',hidden:true},
		{field:'invcateid',title:'物资分类id',hidden:true},
		{field:'warehouse',title:'仓库名称',width:85,fixed:true},
		{field:'createUserName',width:50,fixed:true,title:'申请人'},
		{field:'applyDept',width:90,fixed:true,title:'申请部门'},
		{field:'projectAscription',width:90,fixed:true,title:'项目名称'},
		{field:'itemnum',width:60,fixed:true,title:'采购量',edit:true,align:'right',
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
			}
		},
		{field:'orderunitname',width:45,fixed:true,title:'单位'},
		{field:'tax',title:'税费(元)',hidden:true},
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
		{field:'priceTotal',width:70,fixed:true,title:'小计(元)',align:'right'},
		{field:'remark',width:150,edit:true,editor:{type:'text'},title:'备注'},
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
			var listData =$("#order_item").datagrid("getRows");
			$("#order_item").datagrid("hideColumn","tax");
			setTimeout(function(){ 
				$("#order_item").datagrid("resize"); 
			},200);
			
			//若商务网编号存在，则屏蔽流程信息按钮,注意新建页面没有businessno这个字段
			if(""!=sheetId){
				var f_businessno = $.trim($("#autoform").iForm("getVal","businessno"));
				if("" == f_businessno || null == f_businessno){
					$("#autoform").ITC_Form("hide",["businessno"]);
				}
			}
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
			
			//若流程结束隐藏统一税率
			if(processStatus == "over"){
				$("#autoform").iForm("hide",["taxRate"]);
			}
			FW.fixToolbar("#toolbar1");
		},
		onAfterEdit:function(rowIndex, rowData){
			caluRow(rowData,rowIndex);
		},
		onRenderFinish : function() {
			dynaCalcTotalPrice();
			var listData =$("#order_item").datagrid("getRows");
			initListStatus = FW.stringify(listData);
			initFormStatus = $("#autoform").iForm("getVal");
			setTimeout(function(){datagridInited = true;},0);
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
			var url = basePath+ "purchase/purorder/purOrderForm.do?type=edit&sheetId="+rowData.sheetId;
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
			setTimeout(function(){ 
				$("#purpay_list").datagrid("resize"); 
			},200);
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

function refreshAcceptList(){
	$("#accept_list").datagrid("reload");
 	setTimeout(function(){ 
 		$("#accept_list").datagrid("resize"); 
 	},800);
}

function refreshPayList(){
	$("#purpay_list").datagrid("reload");
 	setTimeout(function(){ 
 		$("#purpay_list").datagrid("resize"); 
 	},800);
}
