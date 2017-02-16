function selectTreeRootNode(){
	var assetTree=window.parent.document.getElementById("assetTree").contentWindow.$('#asset_tree');
	var selectNode=assetTree.tree("getSelected");
	if(!selectNode||selectNode.type!='root'){//当未选中根节点或选中的不是根节点时，选中根节点
		var rootNode=assetTree.tree("getRoot");
		rootNode.isOnlySelect=true;
		assetTree.tree('select',rootNode.target);
	}
}
function initAssetTree(initFn){
	FW.addSideFrame({
		id : "assetTree",
		src : basePath+"page/asset/core/assetinfo/assetTree.jsp",
		conditions : [
			{tab:"^equipment$",tree:"^equipment_assetcard$"},
			{tab:"^equipment.+"}
		]
	});
	//修正树的选择节点为根节点
	if(isTreeExist()){
		selectTreeRootNode();
	}else{//如果树还没生成，给生成后的回调赋值
		FW.set("AssetTreeInitRollBackFunc",function(){
			selectTreeRootNode();
			if(initFn){
				initFn();
			}
		});
	}
};
function isTreeExist(){
	var treeArea=window.parent.document.getElementById("assetTree");
	return treeArea&&treeArea.contentWindow&&treeArea.contentWindow.isAssetTreeInitFinished;
};