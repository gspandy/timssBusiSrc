<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>选择组织</title>

<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<link rel="stylesheet" type="text/css"
	href="${basePath}css/public_background.css" media="all" />
<script>
	var chkOrg = {};
	
	$(document).ready(function(){
		var p = FW.getFrame(FW.getCurrentTabId());
		var orgs = p.g.orgs;
		for(var k in orgs){
			chkOrg[k] = orgs[k];
		}
		$("#orgtree").tree({
			url : basePath + "tree?method=org&onlyorg=1",
			checkbox : true,
			cascadeCheck : false,
			onCheck : function(node, checked){
				var nid = node.id.split("_")[1];
				if(checked){
					chkOrg[nid] = node.text;
				}
				else{
					delete(chkOrg[nid]);
				}
			},
			loadFilter : function(data,parent){
				for(var i=0;i<data.length;i++){
					var node = data[i];
					var nid = node.id.split("_")[1];
					if(orgs[nid]){
						data[i].checked = true;
					}
					if(node.children){
						for(var j=0;j<data[i].children.length;j++){
							var cnode = data[i].children[j];
							var cnid = cnode.id.split("_")[1];
							if(orgs[cnid]){
								data[i].children[j].checked = true;
							}
						}
					}
				}
				return data;
			} 
		});
	});
	
	function getChecked(){
		return chkOrg;
	}
	
</script>
<style>
</style>
</head>
<body style="padding:6px">
	<div style="width:100%" id="orgtree"></div>
</body>
</html>