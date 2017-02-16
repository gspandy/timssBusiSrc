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
	<title>标准树</title>
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
	<script type="text/javascript" src="${basePath}js/ptw/sjc/stdTree.js?ver=${iVersion}"></script>
	<script>		
		$(document).ready(function(){
			StandardTree.objs["isShowCard"]=<%=isShowCard%>;
			StandardTree.init();
		});
		
	</script>
  </head>
<body class="bbox" style="height:100%;width:100%;padding-right:0">
	<div style="padding-top:38px;width:100%;height:100%">
		<div class="input-group input-group-sm bbox" style="width:210px;position:absolute;top:0px;margin: 5px 0;margin-right:10px;">
			<input type="text" placeholder="搜索名称或编码" id="search"
				style="width:100%" icon="itcui_btn_mag" />     
		</div>
		<div id='propertyTreeArea' style="width:100%;height:100%;overflow:auto">
			<ul id="property_tree" style="width:100%">
				
			</ul>
		</div>
	</div>
</body>
</html>
