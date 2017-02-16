var currDragNode = null;
var currClickNode = null; //当前被点击的树节点
var parentNode = null; //新增加的位置节点的父节点
var nodeType = {
	"root" : {"name":"机构","level":1},
	"area" : {"name":"区域","level":2},
	"system" : {"name":"系统","level":3},
	"device" : {"name":"设备","level":4},
	"component" : {"name":"配件","level":5}
};

function getSelected(){
	var t = $('#asset_tree');
	var obj = {};
	if(!multi){
		var node = t.tree("getSelected");
		if(node){
			obj[node.id] = node.text;
			return obj;
		}
		else{
			return null;
		}
	}
	else{
		var nodes = t.tree("getChecked");
		if(nodes && nodes.length>0){					
			for(var i=0;i<nodes.length;i++){
				var n = nodes[i];
				obj[n.id] = n.text;
			}
			return obj;
		}
		else{
			return null;
		}
	}
}
function getSelectedNodes(){
	var t = $('#asset_tree');
	var obj = {};
	if(!multi){
		var node = t.tree("getSelected");
		if(node){
			return node;
		}
		else{
			return null;
		}
	}
	else{
		var nodes = t.tree("getChecked");
		if(nodes && nodes.length>0){	
			return nodes;
		}
		else{
			return null;
		}
	}
}
function jumpTo(url){
	if(!embbed){
		FW.navigate(url);
	}
	else if(embbed==1){
		_parent().$("#itcDlgAssetPage").attr("src",url);
	}
	else if(embbed=2){
		return;//页内嵌入模式没有跳转
	}
}

function updateFormVal(){
	var result = getSelected();
	
	var p = _parent();
	if(p.onAssetSelect && typeof(p.onAssetSelect)=="function"){
		p.onAssetSelect(joinKey(result),joinVal(result));
	}
	if(p.passAssetSelect && typeof(p.passAssetSelect)=="function"){
		var nodes=getSelectedNodes();
		p.passAssetSelect(nodes);
	}
}

function onSelectAssetTreeNode(node){
	if(!multi){
		updateFormVal();
	}
	if(node.isOnlySelect){//修正模式（修正树的当前选中节点非所要的节点）时，不改变页面
		node.isOnlySelect=false;
		return;
	}
	//var asset=window.parent.frames["navtab_equipment"].contentWindow.Asset;
	var asset=FW.getFrame(FW.getCurrentTabId()).Asset;
	//var asset=window.parent.document.getElementById("navtab_equipment").getAsset();
	if(asset&&node.type!='root'){//用于不闪动加载，判断当前显示的是否是卡片页
		var showBean=asset.objs["bean"];
		if(node.id==showBean.assetId){//但选择的节点是当前展示的节点时，不刷新，直接修改展示
			asset.showDetail(null,true);
		}
		else{//直接获取新bean加载
			asset.showDetail(node.id);
		}
		return;
	}
	
	if(node.type=="root"){
		jumpTo(basePath+"asset/location/locationList.do?location="+node.id);
	} else{
		jumpTo(basePath+"asset/assetInfo/assetDetail.do?mode=view&assetId=" + node.id);
	}
}

function initTree(){
	var opts = {
		url : basePath + "asset/location/getAssetTree.do",
		dnd : true,
		onSelect : function(node){
			onSelectAssetTreeNode(node);
		},
		onBeforeDrag : function(node){
			//不准根节点
			if(node.type=="root"){
				return false;
			}
			if(forbidEdit||!Priv.hasPrivilege("VIRTUAL-MOVE")){//不可编辑时，不许拖动
				return false;
			}
			if(node.forbidMove=="Y"){//节点被设置为不能移动时，不许拖动
				return false;
			}
			return true;
		},
		onStartDrag : function(node){
			//拖拽时关闭右键菜单
			$("#wt_menu_tree").removeClass("open");
			currDragNode = node;
		},
		onDragOver : function(target,source){
			var t = $('#asset_tree').tree("getNode",target);
		},
		onBeforeDrop : function(target, source,point){
			var t=$('#asset_tree').tree("getNode",target);
			var s=source;
			var tp=$('#asset_tree').tree("getParent",target);
			var sp=$('#asset_tree').tree("getParent",source.target);
			
			if(point=="append"){//移动到目标节点内时
				if(t==sp){//移动到原父节点内
					return true;
				}else{
					changeNodeParent(s,t,point);
				}
			}else{//移动到目标节点同级时
				if(tp==sp){//目标节点是兄弟节点时
					return true;
				}else{
					changeNodeParent(s,tp,point,t);
				}
			}
			return false;
		},
		onDrop : function(target, source,point){
			//拖动完成后选中拖动的点
			$('#asset_tree').tree("select",$('#asset_tree').tree("find",source.id).target);
		},
		onContextMenu : function(e, node){			
			e.preventDefault();
			if(!embbed){
				_parent().$("#wtTreeMenu").css({
					left:e.pageX + 200,
					top:e.pageY + 89
				});
				_parent().$("#wtTreeMenuToggle").dropdown("toggle");
				currClickNode = node; //保存当前点击的树节点的信息
			}
		},
		
		onBeforeCheck : function(node, checked){
			//不准选择根节点
			return node.type!="root";
		},
		
		onBeforeSelect : function(node){
			return true;//node.type!="root";
		},
		
		onCheck : function(node, checked){
			if(multi){
				updateFormVal();
			}
		},
		
		//用于修改数据，例如图标
		loadFilter:function(data,parent){
			for(var i=0;i<data.length;i++){
				changeIconByAssetType(data[i]);	
				if(data[i].children){
					for(var j=0;j<data[i].children.length;j++){
						changeIconByAssetType(data[i].children[j]);
					}
				}
			}
			return data;
		},
		
		onLoadSuccess:function(node, data){
			var rollbackFuncName="AssetTreeLoadRollBackFunc";
			var rollbackFuncAttrsName="AssetTreeLoadRollBackFuncAttrs";
			if(!isAssetTreeInitFinished){//第一次初始化完成
				isAssetTreeInitFinished=true;
				rollbackFuncName="AssetTreeInitRollBackFunc";
				rollbackFuncAttrsName="AssetTreeInitRollBackFuncAttrs";
			}else{//重载数据完成
				
			}
			//执行回调函数
			var rollbackFunc=FW.get(rollbackFuncName);
			if(rollbackFunc&&typeof(eval(rollbackFunc)) == "function"){
				var rollbackFuncAttrs=FW.get(rollbackFuncAttrsName);
				rollbackFunc(node,data,rollbackFuncAttrs);
				//执行完后清空
				FW.set(rollbackFuncName,null);
				FW.set(rollbackFuncAttrsName,null);
			}
		}
	};
	if(multi){
		opts["cascadeCheck"] = false;
		opts["checkbox"] = true;
	}
	$('#asset_tree').tree(opts);		
}
function changeIconByAssetType(data){
		/*if(data.type=='root'){
			//data.iconCls='icon-folder';
			data.iconCls='tree-folder';
		}else{
			//data.iconCls='icon-file';
			data.iconCls='tree-file';
		}	*/
}	

//修改节点的父节点,传入sourceNode,parentNode,point,targetNode
function changeNodeParent(s,p,point,t){
	FW.confirm("确认移动|您确定将<span style='red'>"+FW.specialchars(s.text)+"</span>移动到<span style='red'>"+FW.specialchars(p.text)+"</span>中吗？",function(){
		$.post(basePath + "asset/assetInfo/updateAssetLocation.do",
			{"id":s.id,"parentId":p.id},
			function(data){
				if(data.result=="ok"){
					FW.success("移动成功");
		   			var node = $('#asset_tree').tree("pop",s.target);
		   			if(point=="append"){//需判断目标是否已展开
		   				if(p.state=="open"){
		   					$('#asset_tree').tree("append",{
								parent : p.target,
								data : node,
								state:"closed",
								type :"system"
							});
		   				}else{
		   					$('#asset_tree').tree('expand',p.target);//可能有bug，如果展开速度比下面的展示卡片慢，可能下面选择不到该节点
		   				}
		   			}else if(point=="top"){
		   				$('#asset_tree').tree("insert",{
		   					before : t.target,
							data : node,
							state:"closed",
							type :"system"
						});
		   			}else if(point=="bottom"){
		   				$('#asset_tree').tree("insert",{
		   					after : t.target,
							data : node,
							state:"closed",
							type :"system"
						});
		   			}
		   			//更新展示的bean，展示卡片，确保了展示的bean的父节点的正确
		   			FW.navigate(basePath+"asset/assetInfo/assetDetail.do?mode=view&assetId="+s.id);
		   		}else if(data.result=="forbidMoveRoot"){
		   			FW.error("移动失败,根节点不能移动");
		   		}else if(data.result=="forbidMove"){
		   			FW.error("移动失败,该节点不能移动");
		   		}else{
		   			FW.error("移动失败");
		   		}   		
			}
		);
	});
}

function joinKey(obj){
	var arr = [];
	for(var k in obj){
		arr.push(k);
	}
	return arr.join(",");
};

function joinVal(obj){
	var arr = [];
	for(var k in obj){
		arr.push(obj[k]);
	}
	return arr.join(",");
};