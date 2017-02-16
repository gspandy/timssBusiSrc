<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html style="min-height: 99%;">
<head>
<title>标准隔离证</title>
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script type="text/javascript" src="${basePath}js/ptw/sjc/isolationArea.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/ptw/OpenTabUtil.js?ver=${iVersion}"></script>
<script>

	var id = '<%=request.getParameter("id")%>'; 
	var _dialogEmmbed = true;
	var userName = ItcMvcService.user.getUserName();
	var userId = ItcMvcService.user.getUserId();
	
	$(document).ready(function() {

		if(id == null || id == 'null'){  //新建隔离方法,参数定为0传过去
				initIsolationAreaPage(0); 
				$("#btn_isolationArea_save").show(); 
				$("#btn_isolationArea_edit").hide(); 
				$("#btn_isolationArea_deleteDiv").hide(); 
				FW.fixRoundButtons("#toolbar");
		}else{  //查看隔离方法
			initIsolationAreaPage(id);
		}
			
	});
	
	function closeCurPage(){
		FW.deleteTabById(FW.getCurrentTabId());
	}
	
	/**
	 * @returns 获取表单值
	 */
	function getFormData(){
		
		if(! $("#isolationAreaForm").valid()){
			return null;
		}
		var formData = $("#isolationAreaForm").iForm("getVal");
		return formData;
	}

	/**
	 * 提交隔离方法
	 */
	function commitIsolationArea(){
		var formData = getFormData();
		var rowDatas = $("#isolationPointTable").datagrid('getRows');
		var len = rowDatas.length;
		if( len <= 0 ){
			FW.error( "请添加隔离点！");
			return ;
		}
		for( var i = 0; i < 1; i++ ){
			$("#isolationPointTable").datagrid( 'endEdit', i );
		}
		rowDatas = $("#isolationPointTable").datagrid('getRows');
		var url = basePath + "ptw/ptwIsolationArea/commitPtwIsolationArea.do";
		
		$.ajax({
			url : url,
			type : 'post',
			dataType : "json",
			data :{
				isolationAreaFormDate : JSON.stringify( formData ),
				rowDatas:  FW.stringify( rowDatas )
			},
			success : function(data) {
				if(data.result == "success"){
					FW.success("保存成功");
					closeCurPage();
				}else {
					FW.error("保存失败");
				}
			}
		}); 
		
	}
	
	function newIslInfoFromArea(){
		//新建卡片
		var params = {opType:"newIsolation",areaId : id};
		
		var p = _parent().window.document.getElementById("ptwAssetTree").contentWindow;
		var node = undefined;
		if(p){
			node = p.getSelectedNode();
			if(node){
				params.eqId = node.id;
				params.eqName = node.text;
				params.eqNo = node.assetCode;
			}
		}
		openNewTab("islInfoDetail"+new Date().getTime(),"新建隔离证","ptw/ptwIsolation/preQueryIslInfo.do",params);
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
	    	<div id="btn_isolationArea_saveDiv" class="btn-group btn-group-sm">
				<button id="btn_isolationArea_save" type="button" class="btn btn-success" onclick="commitIsolationArea()">保存</button>
				<button id="btn_isolationArea_edit" type="button" class="btn btn-default" onclick="editIsolationArea()">编辑</button>
			</div>
	        <div id="btn_isolationArea_deleteDiv" class="btn-group btn-group-sm">
	        	<button id="btn_isolationArea_delete" type="button" class="btn btn-default" onclick="deleteIsolationArea()">删除</button>
	        </div>
	        <div id="btn_isolationArea_newIslInfoDiv" class="btn-group btn-group-sm">
	        	<button id="btn_isolationArea_newIslInfo" type="button" class="btn btn-default" onclick="newIslInfoFromArea()">新建隔离证</button>
	        </div>
	    </div>
	</div>
    <div>
    	<form id="isolationAreaForm"  class="autoform"></form>
    </div>
	<div id="isolationPointContentDiv">
		<div id="title_isolationPoint" grouptitle="隔离方法">
			<div class="margin-title-table">
				<form><table id="isolationPointTable" class="eu-datagrid"></table></form>
				<div id="isolationPointBtnDiv" class="row btn-group-xs">
					 <button id="btn_isolationPointTable" onclick="appendIsolationPoint();" type="button" class="btn btn-success">添加</button>
				</div>
			</div>
		</div>
    </div>
</body>
</html>
