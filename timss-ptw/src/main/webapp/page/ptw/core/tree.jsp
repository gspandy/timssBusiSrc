<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String jumpMode = request.getParameter("jumpMode");
 %>
<!DOCTYPE html>
<html style="height: 100%;">
<head>
<title>设备树</title>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
	var jumpMode = "<%=jumpMode%>" || "func";
	$(document).ready(function(){
		
		$("#search").iInput("init");
		initHint();
		initTree();
	});
	
	function unSelectTree(){
		var node = $("#asset_tree").tree("getSelected");
		if(node){
			$("#asset_tree").tree("unselect",node.target);
		}
	}
	function getSelectedNode(){
		return $("#asset_tree").tree("getSelected");
	}
	
	function initTree(){
		$('#asset_tree').tree({
			url : basePath + "asset/location/getAssetTree.do",
			onSelect : function(node){
				var frame = null;
				if(jumpMode=="url"){
				
				}
				else if(jumpMode=="dialog"){
					var frame = _parent().window.document.getElementById("itcDlgIsolationPage").contentWindow;
					if(frame.onTreeItemClick && typeof(frame.onTreeItemClick)=="function"){
						frame.onTreeItemClick(node);
					}
				}
				else if(jumpMode=="func"){
					var currTab = FW.getCurrentTabId();
					var frame = FW.getFrame(currTab);
					if(frame.onTreeItemClick && typeof(frame.onTreeItemClick)=="function"){
						frame.onTreeItemClick(node);
					}
				}
			}		
		});		
	}
	
	
	var expandList;
	var currExpand;
	function initHint(){
		$("#search").ITCUI_HintList({
			"datasource":basePath + "asset/assetInfo/assetHint.do",
			"getDataOnKeyPress":true,
			"clickEvent":function(id,name){
				$("#search").val(name);
				$.ajax({
					url : basePath + "asset/assetInfo/assetParents.do?id=" + id,
					type:'POST',
					dataType:"json",
					success : function(data){
						expandList = data;
						currExpand = data.length-1;
						$('#asset_tree').tree("options").onExpand=function(node){
							if(expandList){
								expandForHint();
							}
						};
						expandForHint();
					}
				});
			},
			"showOn":"input",
			"highlight":true
		});
		resizeAssetTreeHeight();
		$(parent.window).resize(function() {
			resizeAssetTreeHeight();
		});
	}
	function expandForHint(){			
		if(currExpand>0){
			var node=$('#asset_tree').tree("find",expandList[currExpand]);
			if(node.state=='open'){
				currExpand=currExpand-1;
				expandForHint();
			}else{
				$('#asset_tree').tree("expand",node.target);
				currExpand=currExpand-1;
			}
		}else{
			onExpandHintNode(expandList[currExpand]);
			expandList=null;
		}				
	}
	//快速搜索后点击展开树时，当展开到最后一个节点，即选择的节点的操作
	function onExpandHintNode(assetId){
		var node=$('#asset_tree').tree("find",assetId);
		$('#asset_tree').tree("select",node.target);
		$('#asset_tree').tree("scrollTo",node.target);			
	}
	
	function resizeAssetTreeHeight(){
		//var height=$(parent.document.getElementById('mainframe_content')).height()-26-10-5;
		//$("#assetTreeArea").css({"height":height});
	}
	
	
</script>
</head>
<body class="bbox" style="height:100%;width:100%">
	<div style="padding-top:33px;width:100%;height:100%">
		<div class="input-group input-group-sm bbox" style="width:210px;position:absolute;top:5px">
			<input type="text" placeholder="按名称或位置搜索" id="search" icon="itcui_btn_mag"/>     
		</div>
		<div style="width:100%;height:100%;overflow:auto">
			<ul id="asset_tree" style="width:100%">
				
			</ul>
		</div>
	</div>
</body>

</html>