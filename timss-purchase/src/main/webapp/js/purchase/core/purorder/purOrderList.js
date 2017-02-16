//初始化列表（通用方法）
function initList(){
	var columns = [[
		{field:'listId',title:'多类型列表id',formatter:function(value,row){var newval = value+"_"+row.applySheetId; row.listId = newval; return newval;},hidden:true},
		{field:'itemid',width:90,fixed:true,title:'物资编号',formatter:function(value,row){
			return "<a onclick='FW.showItemInfo(\""+row.itemid+"\",\""+row.warehouseid+"\");'>"+row.itemid+"</a>";
		}},
		{field:'applySheetId',width:10,title:'sheetId',hidden:true},
		{field:'itemname',width:90,title:'物资名称'},
		{field:'itemcus',width:90,title:'型号规格'},
		{field:'warehouseid',title:'仓库id',hidden:true},
		{field:'invcateid',title:'物资分类id',hidden:true},
		{field:'warehouse',title:'仓库名称',width:85,fixed:true},
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
			
			//若流程结束隐藏统一税率
			if(processStatus == "over"){
				$("#autoform").iForm("hide",["taxRate"]);
			}
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