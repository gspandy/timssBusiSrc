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

	var _dialogEmmbed = true;
	var assetId = '${assetId}';
	var assetName = '${assetName}';
	
	var initData = {
			parentId : assetId,
			locationName : assetName
	};
	
	
	$(document).ready(function() {
		initStdPointTree();
		
		$("#isolationPointForm").iForm("init",{"fields" : createFields,"options":{validate:true}});
		$("#isolationPointForm").iForm("setVal", initData );
		
		$("#title_isolationMethod").iFold("init");
		var isolationMethodDatagrid =  $("#isolationMethodTable").datagrid({
		    columns:isolationMethodGridField,
		    idField:'isolationMethodDelteId',
		    singleSelect:true,
		    fitColumns:true,
		    scrollbarSize:0
		}); 
	});
	
	
</script>
</head>
<body style="height: 100%;" class="bbox">
 <div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="isolationPointbar" class="btn-toolbar ">
	    	<div id="btn_isolationPoint_close" class="btn-group btn-group-sm">
				<button id="closeButton" type="button" class="btn btn-default" onclick="closeTab()" >关闭</button>
			</div>
	    	<div id="btn_isolationPoint_saveDiv" class="btn-group btn-group-sm">
				<button id="btn_isolationPoint_save" type="button" class="btn btn-success" onclick="commitIsolationPoint( true )">保存</button>
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
