var PropertyTree={
		objs:{},
		init:function(){
			PropertyTree.objs["isInitFinish"]=false;
			PropertyTree.initTree();
			PropertyTree.initHint();
		},
		
		//树的函数
		initTree:function(){
			var opts = {
				url : basePath + "asset/property/getTree.do",
				dnd : false,
				onSelect : function(node){
					if(PropertyTree.objs.isInitFinish){//树已经初始化完成
						window.parent.document.getElementById("navtab_equipment").contentWindow.Property.toShow(node.obj);
					}else{//初始化后选择根节点时不再加载根节点列表
						PropertyTree.objs.isInitFinish=true;
					}
				},
				onLoadSuccess:function(node, data){
					if(!PropertyTree.objs.isInitFinish){//第一次初始化要选择根节点
						for(var i=0;i<data.length;i++){
							if(data[i].isRoot=="Y"){
								node=$('#property_tree').tree("find",data[i].id);
								PropertyTree.objs["rootNode"]=node;
								$('#property_tree').tree("select",PropertyTree.objs.rootNode.target);
								break;
							}
						}
					}
				},
				loadFilter:function(data,parent){//用于修改数据，使适合树
					var result=new Array();
					if(data.parent){//有根节点信息，则当前节点为根节点，底下加入子节点
						var obj={};
						obj.id=data.parent.houseId;
						obj.text=data.parent.houseName;
						obj.state="open";
						obj.type=data.parent.houseType;
						obj.isRoot=data.parent.isRoot;
						obj.children=PropertyTree.changeToTreeNodeList(data.children);
						obj.iconCls=PropertyTree.changeIcon(data.parent);
						obj.obj=data.parent;
						result.push(obj);
					}else if(data.children){
						result=PropertyTree.changeToTreeNodeList(data.children);
					}else{
						result=data;
					}
					return result;
				}
			};
			$('#property_tree').tree(opts);
		},
		changeToTreeNodeList:function(list){//将数据列表转为树节点列表
			var result=new Array();
			for(var i=0;i<list.length;i++){
				result.push(PropertyTree.changeToTreeNode(list[i]));
			}
			return result;
		},
		changeToTreeNode:function(bean){
			var obj={};
			obj.id=bean.houseId;
			obj.text=bean.houseName;
			obj.state="closed";
			obj.type=bean.houseType;
			obj.isRoot=bean.isRoot;
			obj.iconCls=PropertyTree.changeIcon(bean);
			obj.obj=bean;
			return obj;
		},
		changeIcon:function(data){
			var iconCls="";
			if(data.houseType=='house'){
				//data.iconCls='icon-folder';
				iconCls='tree-folder';
			}else{
				//data.iconCls='icon-file';
				iconCls='tree-file';
			}	
			return iconCls;
		},	
		
		//搜索的函数
		initHint:function(){
			$("#search").iInput("init");
			$("#search").parent().children("ul").css({"left":"0","width":"180px"});			
			$("#search").ITCUI_HintList({
				"datasource":basePath + "asset/property/hint.do",
				"getDataOnKeyPress":true,
				"clickEvent":function(id,name){
					$("#search").val(name);
					$.ajax({
						url : basePath + "asset/property/hintParents.do?id=" + id,
						type:'POST',
						dataType:"json",
						success : function(data){
							PropertyTree.objs.expandList = data;
							PropertyTree.objs.currExpand = data.length-1;
							$('#property_tree').tree("options").onExpand=function(node){
								if(PropertyTree.objs.expandList){
									PropertyTree.expandForHint();
								}
							};
							PropertyTree.expandForHint();
						}
					});
				},
				"showOn":"input",
				"highlight":true
			});
		},
		expandForHint:function(){//搜索点击后展开的操作		
			if(PropertyTree.objs.currExpand>0){
				var node=$('#property_tree').tree("find",PropertyTree.objs.expandList[PropertyTree.objs.currExpand]);
				if(node.state=='open'){
					PropertyTree.objs.currExpand=PropertyTree.objs.currExpand-1;
					PropertyTree.expandForHint();
				}else{
					$('#property_tree').tree("expand",node.target);
					PropertyTree.objs.currExpand=PropertyTree.objs.currExpand-1;
				}
			}else{
				PropertyTree.onExpandHintNode(PropertyTree.objs.expandList[PropertyTree.objs.currExpand]);
				PropertyTree.objs.expandList=null;
			}				
		},
		onExpandHintNode:function(propertyId){//快速搜索后点击展开树时，当展开到最后一个节点，即选择的节点的操作
			var node=$('#property_tree').tree("find",propertyId);
			$('#property_tree').tree("select",node.target);
			$('#property_tree').tree("scrollTo",node.target);			
		}
};