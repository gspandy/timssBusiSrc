var Machine={
		objs:{},

		init:function(){
			Machine.objs["isSearchMode"]=false;
			Machine.objs["isSearchLineShow"]=false;
			
			Machine.objs["form"]=[[
				{field:'amIp',title:'IP',width:110,sortable:true,fixed:true},
				{field:'amPort',title:'端口',width:70,sortable:true,fixed:true},
				{field:'lastSync',title:'同步时间',width:160,fixed:true,sortable:true,formatter:function(val){
					return FW.long2time(val);
				}},
				{field:'lastImport',title:'导入数据时间戳',width:160,sortable:true,formatter:function(val){
					return FW.long2time(val);
				}}
			]];
			
			Machine.objs["datagrid"] = $("#machineList").iDatagrid("init",{
				singleSelect:true,
				columns:Machine.objs.form,
				pageSize:pageSize,
				fitColumns:true,
				url: basePath + "attendance/machine/getList.do",
				onLoadSuccess:function(data){
					Machine.changeShow(data);
				},
				onDblClickRow : function(rowIndex, rowData) {
					Machine.toShow(rowData);
				}
			});
		},
		
		toShowSearchLine:function(){
			if(Machine.objs.isSearchLineShow){
				Machine.objs.isSearchLineShow=false;
		        $("#machineList").iDatagrid("endSearch");		        
		    }
		    else{
		    	Machine.objs.isSearchLineShow=true;
		       	$("#machineList").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(arg){
		       		Machine.objs.isSearchMode = true;
					return {"search":JSON.stringify(arg)};
				}});
		    }
		},
		
		toCreate:function(){
			FW.navigate(basePath+"attendance/machine/detailPage.do?mode=create");
		},

		toShow:function(obj){
			FW.navigate(basePath+"attendance/machine/detailPage.do?mode=view&machineId="+obj.amId);
		},
		
		changeShow:function(data){//切换模式，用于控制按钮和文字的改变
			//关闭查询无结果的功能
			//Machine.objs.isSearchMode=null;
			
			if(Machine.objs.isSearchMode){
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