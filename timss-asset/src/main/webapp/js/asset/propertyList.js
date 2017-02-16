var Property={//basePath框架中已有
		objs:{},

		init:function(){
			Property.objs["isSearchMode"]=false;
			
			Property.objs["form"]=[[
				{field:'houseName',title:'房产名称',width:200,sortable:true,fixed:true},
				{field:'houseType',title:'类型',width:200,sortable:true,fixed:true,formatter: function(value,row,index){
					return FW.getEnumMap("AST_HOUSE_TYPE")[value];
				}},
				{field:'ownerName',title:'业主姓名',width:100,sortable:true,fixed:true},
				{field:'area',title:'面积',width:100,sortable:true,fixed:true,formatter: function(value,row,index){
					return value==null||value==""?"":(value+" "+row.unit);
				}},
				{field:'rightOwner',title:'权属',width:90,fixed:true,sortable:true},
				{field:'empty',title:'',width:100}
			]];
			
			FW.setTreeStat("fold");
			
			FW.addSideFrame({
				id : "propertyTree",
				src : basePath+"page/asset/scc/property/tree.jsp",
				conditions : [
					{tab:"^equipment$",tree:"^equipment_propertyManagement$"}/*,
					{tab:"^equipment.+"}*/
				]
			});
			
			Property.objs["datagrid"] = $("#propertyList").iDatagrid("init",{
				singleSelect:true,
				columns:Property.objs.form,
				pageSize:pageSize,
				fitColumns:true,
				url: basePath + "asset/property/getList.do",
				queryParams :{"propertyId":function(){
					return Property.objs.propertyId;
				}},
				onLoadSuccess:function(data){
					Property.changeShow(data);
				},
				onDblClickRow : function(rowIndex, rowData) {
					var node=Property.getTree().tree('find', rowData.houseId);
					if(node){
						Property.getTree().tree('select',node.target);
					}else{
						var tree=Property.getTree();
						node=tree.tree('getSelected');
						Property.objs["waitingExpandNode"]=node;
						Property.objs["waitingSelectRow"]=rowData;
						tree.tree('expand',node.target);
						tree.tree('options')
							.onExpand=function(node){
								if(Property.objs.waitingExpandNode==node){
									delete Property.objs.waitingExpandNode;
									var tree=Property.getTree();
									var newNode = tree.tree('find', Property.objs.waitingSelectRow.houseId);
									delete Property.objs.waitingSelectRow;
									tree.tree('select', newNode.target);
								}
							};
					}
					
				}
			});
		},
		
		getTree:function(){
			try{
				Property.objs["tree"]=window.parent.document.getElementById("propertyTree").contentWindow.$('#property_tree');
			}catch(err){
				return null;
			}
			return Property.objs.tree;
		},
		
		toShow:function(obj){
			if(obj.houseType=="house"){
				Property.objs["propertyId"]=obj.houseId;
				Property.objs["propertyBean"]=obj;
				$("#propertyList").datagrid("reload");
			}else if(obj.houseType=="room"){
				FW.navigate(basePath+"asset/property/detailPage.do?mode=view&houseType="+obj.houseType+"&propertyId="+obj.houseId);
			}
		},
		
		toCreate:function(type){
			FW.navigate(basePath+"asset/property/detailPage.do?mode=create&houseType="+type+"&propertyId="+Property.objs.propertyId);
		},
		
		toEdit:function(){
			FW.navigate(basePath+"asset/property/detailPage.do?mode=edit&houseType=house&propertyId="+Property.objs.propertyId);
		},
		
		toBack:function(){
			var tree=Property.getTree();
			var node=tree.tree('find', Property.objs.propertyId);
			tree.tree('select',tree.tree('getParent',node.target).target);
		},

		changeShow:function(data){//切换模式，用于控制按钮和文字的改变
			if(Property.objs.propertyBean){//排除 第一次初始化无该对象 以及 之后在详情页面通过树选择重新返回的空对象
				if(Property.objs.propertyBean.isRoot=="Y"){//如果是根节点
					Property.objs["isSearchMode"]=false;
					//隐藏返回和编辑按钮
					$("#btnBack,#btnEdit").hide();
				}else{
					Property.objs["isSearchMode"]=true;
					$("#btnBack,#btnEdit").show();
				}
			}else{
				var tree=Property.getTree();
				if(tree){
					var node=tree.tree('getSelected');
					if(node){
						if(node.isRoot=="Y"){//如果是根节点
							Property.objs["isSearchMode"]=false;
							//隐藏返回和编辑按钮
							$("#btnBack,#btnEdit").hide();
						}else{
							Property.objs["isSearchMode"]=true;
							$("#btnBack,#btnEdit").show();
						}
					}
				}
			}
			
			//关闭查询无结果的功能
			//Property.objs.isSearchMode=null;
			
			if(Property.objs.isSearchMode){
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