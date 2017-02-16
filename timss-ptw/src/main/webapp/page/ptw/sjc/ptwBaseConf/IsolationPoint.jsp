<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html style="min-height: 99%;">
<head>
<title>隔离方法</title>
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src='${basePath}js/ptw/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/ptw/commonForm.js?ver=${iVersion}'></script>
<script type="text/javascript" src="${basePath}js/ptw/sjc/isolationPoint.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/ptw/sjc/ptwConfigUtil.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/ptw/updatePointTree.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/ptw/commonPtwTree.js?ver=${iVersion}"></script>
<script>

	var pointNo = '<%=request.getParameter("id")%>'; 
	var _dialogEmmbed = true;
	var userName = ItcMvcService.user.getUserName();
	var userId = ItcMvcService.user.getUserId();
	
	$(document).ready(function() {
		initStdPointTree();
		initIsolationPointPage(pointNo);
	});
	
	/**左边树菜单选择触发事件*/
	function onTreeItemClick(data){
		if(data.type == "root"){
			window.location.href = basePath+ "ptw/ptwIsolationPoint/openIsolationPointListPage.do";
	    }else{
	    	openIsolationPointPage(data.id);
	    }
	}
	
	function openIsolationPointPage(assetId){
		var isolationPointId = assetId;
		window.location.href = basePath+ "ptw/ptwIsolationPoint/openPtwIsolationPointPage.do?id="+isolationPointId;
	}
	
</script>
</head>
<body style="height: 100%;" class="bbox">
 <div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="isolationPointbar" class="btn-toolbar ">
	    	<div id="btn_isolationPoint_create" class="btn-group btn-group-sm">
				<button id="createButton" type="button" class="btn btn-success priv only-hide" privilege="PTW_POINT_NEW" onclick="createIslPoint();" >新建</button>
			</div>
	    	<div id="btn_isolationPoint_back" class="btn-group btn-group-sm">
				<button id="backButton" type="button" class="btn btn-default" onclick="endEdit();" >返回</button>
			</div>
	    	<div id="btn_isolationPoint_saveDiv" class="btn-group btn-group-sm">
				<button id="btn_isolationPoint_save" type="button" class="btn btn-success" onclick="commitIsolationPoint( false )">保存</button>
				<button id="btn_isolationPoint_edit" type="button" class="btn btn-default priv only-hide" privilege="PTW_POINT_EDIT" onclick="editIsolationPoint()">编辑</button>
			</div>
	    	<div id="btn_isolationPoint_delete" class="btn-group btn-group-sm">
				<button id="btn_isolationPoint_delete" type="button" class="btn btn-default" onclick="deleteIsolationPoint()">删除</button>
			</div>
	    </div>
	</div>
	<div  class="inner-title">
		<span id="inPageTitle">隔离点</span>
	</div>
    <div>
    	<form id="isolationPointForm"  class="autoform"></form>
    </div>
	<div id="isolationMethodContentDiv">
		<div id="title_isolationMethod" grouptitle="相关隔离方法">
			<div class="margin-title-table">
				<form><table id="isolationMethodTable" class="eu-datagrid"></table></form>
				<div id="isolationMethodBtnDiv" class="row btn-group-xs">
					 <button id="btn_isolationMethodTable" onclick="appendIsolationMethod();" type="button" class="btn btn-success">添加隔离方法</button>
				</div>
			</div>
		</div>
    </div>
</body>
</html>
