<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html style="min-height: 99%;">
<head>
<title>隔离方法</title>
<script>
	_useLoadingMask = true;
</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script type="text/javascript" src="${basePath}js/ptw/sjc/islMethDef.js?ver=${iVersion}"></script>
<script>

	var id = '<%=request.getParameter("id")%>'; 
	var _dialogEmmbed = true;
	var userName = ItcMvcService.user.getUserName();
	var userId = ItcMvcService.user.getUserId();
	
	$(document).ready(function() {

		if(id == null || id == 'null'){  //新建隔离方法,参数定为0传过去
				initIslMethDefPage(0); 
				$("#btn_islMethDef_save").show(); 
				$("#btn_islMethDef_edit").hide(); 
				$("#btn_islMethDef_deleteDiv").hide(); 
				FW.fixRoundButtons("#toolbar");
		}else{  //查看隔离方法
			initIslMethDefPage(id);
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
	    	<div id="btn_islMethDef_saveDiv" class="btn-group btn-group-sm">
				<button id="btn_islMethDef_save" type="button" class="btn btn-success" onclick="commitIslMethDef()">保存</button>
				<button id="btn_islMethDef_edit" type="button" class="btn btn-default" onclick="editIslMethDef()">编辑</button>
			</div>
	        <div id="btn_islMethDef_deleteDiv" class="btn-group btn-group-sm">
	        	<button id="btn_islMethDef_delete" type="button" class="btn btn-default" onclick="deleteIslMethDef()">删除</button>
	        </div>
	        
	    </div>
	</div>
	<div  class="inner-title">
		<span id="inPageTitle">隔离方法</span>
	</div>
    <div>
    	<form id="islMethDefForm"  class="autoform"></form>
    </div>
	
</body>
</html>
