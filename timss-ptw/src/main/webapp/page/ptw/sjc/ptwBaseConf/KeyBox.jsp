<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html style="min-height: 99%;">
<head>
<title>钥匙箱</title>
<script>
	_useLoadingMask = true;
</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
	var id = '<%=request.getParameter("id")%>'; 
	var userName = ItcMvcService.user.getUserName();
	var userId = ItcMvcService.user.getUserId();
	var formRead = false;
	if(id != "null"){
		formRead = true;
	}
</script>
<script type="text/javascript" src="${basePath}js/ptw/sjc/keyBox.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/ptw/sjc/openKeyBoxTab.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/ptw/OpenTabUtil.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/ptw/PtwUtil.js?ver=${iVersion}"></script>
<script>

	
	$(document).ready(function() {

		if(id == null || id == 'null'){  //新建隔离方法,参数定为0传过去
				initKeyBoxPage(0); 
				$("#btn_keyBox_save").show(); 
				$("#btn_keyBox_edit").hide(); 
				$("#btn_keyBox_deleteDiv").hide(); 
				FW.fixRoundButtons("#toolbar");
		}else{  //查看隔离方法
			initKeyBoxPage(id);
		}
			
	});
	
	function closeCurPage(){
		FW.deleteTabById(FW.getCurrentTabId());
	}
</script>
</head>
<body style="height: 100%;" class="bbox">
 <div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar" class="btn-toolbar ">
	    	<div class="btn-group btn-group-sm">
	        	<button id="btn_close" type="button" class="btn btn-default" onclick="closeCurPage();">关闭</button>
	        </div>
	    	<div id="btn_keyBox_saveDiv" class="btn-group btn-group-sm">
				<button id="btn_keyBox_save" type="button" class="btn btn-success" onclick="commitKeyBox()">保存</button>
				<button id="btn_keyBox_edit" type="button" class="btn btn-default" onclick="editKeyBox()">编辑</button>
			</div>
	        <div id="btn_keyBox_deleteDiv" class="btn-group btn-group-sm">
	        	<button id="btn_keyBox_delete" type="button" class="btn btn-default" onclick="deleteKeyBox()">删除</button>
	        </div>
	        
	    </div>
	</div>
	<div  class="inner-title">
		<span id="inPageTitle">钥匙箱</span>
	</div>
    <div>
    	<form id="keyBoxForm"  class="autoform"></form>
    </div>
    
    <div id="PtwDiv">
		<div id="titlePtw" grouptitle="对应的工作票">
			<div class="margin-title-table">
				<table id="PtwDatagrid" class="eu-datagrid"></table>
			</div>
		</div>
    </div>
    
    <div id="IslDiv">
		<div id="titleIsl" grouptitle="对应的隔离证">
			<div class="margin-title-table">
				<table id="IslDatagrid" class="eu-datagrid"></table>
			</div>
		</div>
    </div>
	
</body>
</html>
