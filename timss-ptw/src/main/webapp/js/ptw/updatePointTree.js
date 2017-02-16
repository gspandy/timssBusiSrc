//拿到树
function getTree(){
	var tree = window.parent.document.getElementById("pointTree").contentWindow.$('#asset_tree');
	return tree;
}

//在树上加上节点
function addTreeNode( bean ){
	var tree = window.parent.document.getElementById("pointTree").contentWindow.$('#asset_tree');
	var node = tree.tree('getSelected');
	if (node){
		if(node.state=="open"){
			tree.tree('append', {
				parent: node.target,
				data: window.parent.document.getElementById("pointTree").contentWindow.changeToTreeNode(bean)
				//data:function(){return {children:[Property.objs.propertyBean]};}
			});
			var newNode = tree.tree('find', bean.assetId);
			tree.tree('select', newNode.target);
		}else{
			var objs = node;
			tree.tree('expand',node.target);
			tree.tree('options')
				.onExpand=function(node){
					if(objs == node){
						delete objs;
						var newNode = tree.tree('find', bean.assetId);
						tree.tree('select', newNode.target);
					}
				};
		}
	}
}

//更新树节点
function updateTreeNode( bean ){
	var node = getTree().tree('getSelected');
	if (node){
		getTree().tree('update', {
			target: node.target,
			text: bean.assetName
		});
	}
}

//删除树节点
function deleteTreeNode( pId ){
	var tree = getTree();
	tree.tree('remove', tree.tree('getSelected').target);
	var parent = tree.tree('find',pId);
	tree.tree('select',parent.target);
}