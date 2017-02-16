<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String embbed = request.getParameter("embbed");
	
	//是否多选
	String multi = request.getParameter("multi");
	
	//是否禁止编辑
	String forbidEdit=request.getParameter("forbidEdit");
	//forbidEdit="true";
	
	//是否显示卡片详情
	String isShowCard=request.getParameter("isShowCard");
	if(isShowCard==null||"".equals(isShowCard))
		isShowCard="true";
		
%>
<!DOCTYPE html>
<html style="height:100%;width:100%">
  <head>
	<title>硬件台账树</title>
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
	<script type="text/javascript" src="js/asset/hwTree.js?ver=${iVersion}"></script>
	<link rel="stylesheet" type="text/css"
	href="${ basePath }css/asset/hwTree.css?ver=${iVersion}" media="all" />
	<script>		
		$(document).ready(function(){
			HardwareTree.objs["isShowCard"]=<%=isShowCard%>;
			HardwareTree.init();
			
			var p = _parent();
			//如果外层已经有一个菜单了 删掉就好
			if(p.$("#hwTreeDiv").length>0){
				$("#hwTreeDiv").remove();
			}
			//将准备好的菜单扔到外面去
			$("#hwTreeDiv").appendTo(p.$("body"));
		});
		
	</script>
  </head>
<body class="bbox" style="height:100%;width:100%;padding-right:0">
	<div style="padding-top:38px;width:100%;height:100%">
		<div class="input-group input-group-sm bbox" style="width:210px;position:absolute;top:0px;margin: 5px 0;margin-right:10px;">
			<input type="text" placeholder="搜索名称或IP" id="search"
				style="width:100%" />     
		</div>
		<div id='propertyTreeArea' style="width:100%;height:100%;overflow:auto">
			<ul id="property_tree" style="width:100%">
				
			</ul>
		</div>
		<div id="hwTreeDiv" class="hide-on-click" style="position:absolute;display: none;z-index: 200;
		height: 120px;width: 200px;border-width: 1px;border-bottom-color: #000000;border-style:solid;background-color: #FFFFFF;">
			<div id="hwTreeContentDiv">
			</div>
		</div>
	</div>
</body>
</html>
