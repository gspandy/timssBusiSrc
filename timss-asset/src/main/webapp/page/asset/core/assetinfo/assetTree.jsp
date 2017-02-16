<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String embbed = request.getParameter("embbed");
	String multi = request.getParameter("multi");
	String forbidEdit=request.getParameter("forbidEdit");
	//forbidEdit="true";
%>
<!DOCTYPE html>
<html style="height:100%;width:100%">
  <head>
	<title>资产树</title>
	<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
	<style>
		<%-- style --%>
		.icon-file{
			background:url({basePath}+'/img/asset/file-img.png') no-repeat;
		}
		.icon-folder{
			background:url({basePath}+'/img/asset/folder-img.png') no-repeat;
		}
		#ITC_HintList_Wrap{
			left:0!important;
		}
		.tree-node-selected .tree-title{
			background: #CCE6FF;
		}
		.tree-node-hover .tree-title{
			background: #e6e6e6;
		}
	</style>
	<script type="text/javascript" src="${basePath}js/asset/assetTree.js?ver=${iVersion}"></script>
	<script type="text/javascript" src="${basePath}js/asset/assetPriv.js?ver=${iVersion}"></script>
	<script>
		var embbed = <%=embbed%>;//是否是嵌入对话框中显示
		var multi = <%=multi%>;//是否允许多选		
		var forbidEdit=<%=forbidEdit%>;//是否禁止编辑（移动树节点）
		var isAssetTreeInitFinished=false;//用于判断树是否初始化完成
		
		$(document).ready(function(){
			$("#search").iInput("init");
			$("#search").parent().children("ul").css({"left":"0","width":"180px"});
			
			initHint();
			initTree();
				
			AssetPriv.init();
		});
		
		var expandList;
		var currExpand;
		function initHint(){
			$("#search").ITCUI_HintList({
				"datasource":basePath + "asset/assetInfo/assetHint.do",
				"getDataOnKeyPress":true,
				"clickEvent":function(id,name,assetObj){
					$("#search").val(name);
					expandForHintById(id);
				},
				"showOn":"input",
				"highlight":true,
				"formatter" : function(id,name,assetObj){
					return assetObj.code + " / " + assetObj.name;
				}
			});
		}
		function expandForHintById(id){
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
	</script>
  </head>
<body class="bbox" style="height:100%;width:100%;padding-right:0">
	<div style="padding-top:38px;width:100%;height:100%">
		<div class="input-group input-group-sm bbox" style="width:210px;position:absolute;top:0px;margin: 5px 0;margin-right:10px;">
			<input type="text" placeholder="搜索资产名称或资产编码" id="search"
				style="width:100%" />     
		</div>
		<div id='assetTreeArea' style="width:100%;height:100%;overflow:auto">
			<ul id="asset_tree" style="width:100%">
				
			</ul>
		</div>
		<div class="dropdown" id="wtTreeMenu" style="position:absolute;display:none;">
			<a data-toggle="dropdown" id="wtTreeMenuToggle"></a>
			<ul class="dropdown-menu" role="menu">
				<li><a class="menuitem" id="createNode"
					onclick="createChildNode()">新建子级资产</a>
				</li>
				<li><a class="menuitem" id="deleteNode" onclick="deleteNode()">删除</a>
				</li>
				<li><a class="menuitem" id="renameNode" onclick="renameNode()">重命名</a>
				</li>
			</ul>
		</div>
	</div>
</body>
</html>
