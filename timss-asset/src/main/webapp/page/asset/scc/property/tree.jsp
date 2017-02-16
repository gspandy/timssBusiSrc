<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	/* String embbed = request.getParameter("embbed");
	String multi = request.getParameter("multi");
	String forbidEdit=request.getParameter("forbidEdit");
	forbidEdit="true"; */
%>
<!DOCTYPE html>
<html style="height:100%;width:100%">
  <head>
	<title>房产树</title>
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
	<script type="text/javascript" src="js/asset/propertyTree.js?ver=${iVersion}"></script>
	<script>		
		$(document).ready(function(){
			PropertyTree.init();
		});
		
	</script>
  </head>
<body class="bbox" style="height:100%;width:100%;padding-right:0">
	<div style="padding-top:38px;width:100%;height:100%">
		<div class="input-group input-group-sm bbox" style="width:210px;position:absolute;top:0px;margin: 5px 0;margin-right:10px;">
			<input type="text" placeholder="搜索房产名称" id="search"
				style="width:100%" />     
		</div>
		<div id='propertyTreeArea' style="width:100%;height:100%;overflow:auto">
			<ul id="property_tree" style="width:100%">
				
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
