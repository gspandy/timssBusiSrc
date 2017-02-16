function initList(){
	var columns = [[
	        		{field:'inacNo',title:'编号',width:140,fixed:true,sortable:true},
	        		{field:'deliveryDataString',title:'申请验收日期',sortable:true,width:110,fixed:true,
	        			formatter:function(value,record){
	        			    return FW.long2date(record.createdate);
	        		    }
	        		},
	        		{field:'applyUser',title:'采购人',sortable:true,width:85,fixed:true},
	        		{field:'poSheetno',title:'采购合同号',sortable:true,width:150,fixed:true,align:'left'},
	        		{field:'poName',title:'采购合同名称',sortable:true,width:120,align:'left'},
	        		{field:'status',title:'状态',width:85,fixed:true,align:'left',sortable:true,
	        			'editor':{
							 "type" : "combobox",
		 					 "options" : {
		 					    "data" : FW.parseEnumData("ACPT_STATUS",_enum)
		 				     }
		 				},
		 				formatter:function(value){
		 					var list=FW.parseEnumData("ACPT_STATUS",_enum);
		 					return getEnumValueByCode(list,value);
		 				}
     				},
	        		{field:'acptCnlus',title:'验收结论',sortable:true,width:100,fixed:true,align:'left',
	        			'editor':{
							 "type" : "combobox",
		 					 "options" : {
		 					    "data" : FW.parseEnumData("ACPT_CNLUS",_enum)
		 				     }
		 				},
		 				formatter:function(value){
		 					var list=FW.parseEnumData("ACPT_CNLUS",_enum);
		 					return getEnumValueByCode(list,value);
		 				},
		 				styler: function(value,row,index){
		 					if (row.acptCnlus=='FAILURE'){
		 						return 'color:#EA3434;';
		 					}
		 				}
	        		}
	        		
	        	]];
	dataGrid=$("#accept_grid").iDatagrid("init",{
		singleSelect:true,
		pageSize:pageSize,
		columns:columns,
		url: basePath+"inventory/invmataccept/queryInvMatAcceptList.do",
		onLoadSuccess:function(data){
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
	                $("#grid1_wrap,#toolbar_wrap").hide();
	                $("#grid1_empty").show();
	            }else{
	            	$("#grid1_wrap,#toolbar_wrap").show();
	                $("#grid1_empty").hide();
	            }
				$("#noSearchResult").hide();
			}
			
			isSearchMode = false;
		},
		onDblClickRow:function(rowIndex, rowData){
			var url = basePath+"inventory/invmataccept/invMatAcceptFormJsp.do?inacId="+rowData.inacId;
			var prefix = new Date().getTime() + "" + Math.floor(Math.abs(Math.random()*100));
			FW.addTabWithTree({
			        id : "editAcceptForm" + prefix,
			        url : url,
			        name : "物资验收",
			        tabOpt : {
			            closeable : true,
			            afterClose : "FW.deleteTab('$arg');FW.activeTabById('stock');FW.getFrame(FW.getCurrentTabId()).refCurPage();"
			        }
			});
		}
	});
	
}

