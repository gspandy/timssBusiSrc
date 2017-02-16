var SwLedger={
		objs:{},

		init:function(){
			SwLedger.objs["isSearchMode"]=false;
			SwLedger.objs["isSearchLineShow"]=false;
			
			SwLedger.objs["form"]=[[
				{field:'swName',title:'应用系统名称',width:100,sortable:true},
				{field:'ip',title:'IP地址',width:200,formatter:function(value,row,index){
					return value;
					
					var ip="";
					for(var i=0;i<row.apps.length;i++){
						if(row.apps[i].hwlDevice)
							ip+=row.apps[i].hwlDevice.ip+"/";
					}
					return ip==""?ip:ip.substring(0, ip.length-1);
				}},
				{field:'attr01',title:'所属单位',width:150,sortable:true,fixed:true},
				{field:'attr02',title:'负责人',width:100,sortable:true,fixed:true}
			]];
			
			SwLedger.objs["datagrid"] = $("#swLedgerList").iDatagrid("init",{
				singleSelect:true,
				columns:SwLedger.objs.form,
				pageSize:pageSize,
				fitColumns:true,
				url: basePath + "asset/swLedger/getList.do",
				onLoadSuccess:function(data){
					SwLedger.changeShow(data);
				},
				onDblClickRow : function(rowIndex, rowData) {
					SwLedger.toShow(rowData);
				}
			});
		},
		
		toShowSearchLine:function(){
			if(SwLedger.objs.isSearchLineShow){
				SwLedger.objs.isSearchLineShow=false;
		        $("#swLedgerList").iDatagrid("endSearch");		        
		    }
		    else{
		    	SwLedger.objs.isSearchLineShow=true;
		       	$("#swLedgerList").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(arg){
		       		SwLedger.objs.isSearchMode = true;
					return {"search":JSON.stringify(arg)};
				}});
		    }
		},
		
		toCreate:function(){
			var url=basePath+"asset/swLedger/detailPage.do?mode=create";
			//FW.navigate(url);
			addTabWithTreeDataGrid( "addSwLedger" , "软件台账", url,"equipment", "swLedgerList");
		},

		toShow:function(obj){
			var url=basePath+"asset/swLedger/detailPage.do?mode=view&swLedgerId="+obj.swId;
			//FW.navigate(url);
			addTabWithTreeDataGrid( "editSwLedger"+obj.swId , "软件台账", url,"equipment", "swLedgerList");
		},
		
		changeShow:function(data){//切换模式，用于控制按钮和文字的改变
			//关闭查询无结果的功能
			//SwLedger.objs.isSearchMode=null;
			
			if(SwLedger.objs.isSearchMode){
		        //搜索时的无数据信息
		        if(data && data.total==0){
		            $("#noSearchResult").show();
		        }
		        else{
		            $("#noSearchResult").hide();
		        }
		    } 
		    else{
		        //初始化时的无数据信息        
		    	if(data && data.total==0){
	                $("#grid_wrap,.toolbar-with-pager").hide();
	                $("#grid_empty").show();
	            }else{
	            	$("#grid_wrap,.toolbar-with-pager").show();
	                $("#grid_empty").hide();
	            }
		        //这句要有 否则弹起按钮时信息没法隐藏
		        $("#noSearchResult").hide();
		    }
		}
};