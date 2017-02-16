var StandardTree={
		objs:{},
		init:function(){
			StandardTree.objs["isInitFinish"]=false;
			StandardTree.initTree();
			StandardTree.initHint();
		},
		
		//树的函数
		initTree:function(){
			var opts = {
				url : basePath + "ptw/ptwStandardTree/queryPtwStdTree.do",
				dnd : false,
				onSelect : function(node){
					if(!StandardTree.objs.isShowCard){//不显示卡片详情
						return;
					}
					if(StandardTree.objs.isInitFinish){//树已经初始化完成
						var tabId = FW.getCurrentTabId();
						if( tabId == 'ptw'){
							tabId = "navtab_ptw";
						}
						window.parent.document.getElementById( tabId ).contentWindow.toShow(node);
					}else{//初始化后选择根节点时不再加载根节点列表
						StandardTree.objs.isInitFinish=true;
					}
				},
				onLoadSuccess:function(node, data){
					if(!StandardTree.objs.isInitFinish){//第一次初始化要选择根节点
						for(var i=0;i<data.length;i++){
							if(data[i].parentId==0){
								node=$('#property_tree').tree("find",data[i].id);
								StandardTree.objs["rootNode"]=node;
								$('#property_tree').tree("select",StandardTree.objs.rootNode.target);
								break;
							}
						}
					}
				},
				loadFilter:function(data,parent){//用于修改数据，使适合树
					var result=new Array();
					if(data.parent){//有根节点信息，则当前节点为根节点，底下加入子节点
						var obj={};
						obj.id=data.parent.id;
						obj.text=data.parent.name;
						obj.parentId=data.parent.parentId;
						obj.assetCode = data.parent.code;
						obj.state="open";
						obj.children=StandardTree.changeToTreeNodeList(data.children);
						obj.iconCls=data.parent.iconCls;
						obj.obj=data.parent;
						result.push(obj);
					}else if(data.children){
						result=StandardTree.changeToTreeNodeList(data.children);
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
				result.push(StandardTree.changeToTreeNode(list[i]));
			}
			return result;
		},
		changeToTreeNode:function(bean){
			var obj={};
			obj.id=bean.id;
			obj.text=bean.name;
			obj.parentId=bean.parentId;
			obj.assetCode = bean.code;
			//obj.iconCls=bean.iconCls == null ?"tree-file" : bean.iconCls;
			if( bean.iconCls == "tree-file" ){
				obj.children=[];
				obj.state="open";
			}else{
				obj.state="closed";
			}
			obj.obj=bean;
			return obj;
		},
		changeIcon:function(data){
			var iconCls="";
			if(data.parentId!='aa'){
				//data.iconCls='icon-folder';
				iconCls='tree-file';
			}else{
				//data.iconCls='icon-file';
				iconCls='tree-folder';
			}	
			return iconCls;
		},	
		
		//搜索的函数
		initHint:function(){
			$("#search").iInput("init");
			$("#search").parent().children("ul").css({"left":"0","width":"180px"});			
			$("#search").ITCUI_HintList({
				"datasource":basePath + "ptw/ptwStandardTree/searchPtwStdHint.do",
				"getDataOnKeyPress":true,
				"clickEvent":function(id,name){
					$("#search").val(name);
					$.ajax({
						url : basePath + "ptw/ptwStandardTree/searchHintPtwStdParentIds.do?id=" + id,
						type:'POST',
						dataType:"json",
						success : function(data){
							StandardTree.objs.expandList = data;
							StandardTree.objs.currExpand = data.length-1;
							$('#property_tree').tree("options").onExpand=function(node){
								if(StandardTree.objs.expandList){
									StandardTree.expandForHint();
								}
							};
							StandardTree.expandForHint();
						}
					});
				},
				"showOn":"input",
				"highlight":true
			});
		},
		expandForHint:function(){//搜索点击后展开的操作		
			if(StandardTree.objs.currExpand>0){
				var node=$('#property_tree').tree("find",StandardTree.objs.expandList[StandardTree.objs.currExpand]);
				if(node.state=='open'){
					StandardTree.objs.currExpand=StandardTree.objs.currExpand-1;
					StandardTree.expandForHint();
				}else{
					$('#property_tree').tree("expand",node.target);
					StandardTree.objs.currExpand=StandardTree.objs.currExpand-1;
				}
			}else{
				StandardTree.onExpandHintNode(StandardTree.objs.expandList[StandardTree.objs.currExpand]);
				StandardTree.objs.expandList=null;
			}				
		},
		onExpandHintNode:function(propertyId){//快速搜索后点击展开树时，当展开到最后一个节点，即选择的节点的操作
			var node=$('#property_tree').tree("find",propertyId);
			$('#property_tree').tree("select",node.target);
			$('#property_tree').tree("scrollTo",node.target);			
		}		
};

function unSelectStdTree(){
	var node = $("#property_tree").tree("getSelected");
	if(node){
		$("#property_tree").tree("unselect",node.target);
	}
}
function getStdSelectedNode(){
	return $("#property_tree").tree("getSelected");
}
