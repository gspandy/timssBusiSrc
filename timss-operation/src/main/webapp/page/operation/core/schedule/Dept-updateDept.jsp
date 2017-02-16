<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>编辑岗位信息</title>
<script>_useLoadingMask = true;</script>

<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src='${basePath}js/operation/common/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/operation/common/commonForm.js?ver=${iVersion}'></script>
<script src='${basePath}js/operation/common/stationCommon.js?ver=${iVersion}'></script>
<script src='${basePath}js/operation/common/pageMode.js?ver=${iVersion}'></script>

<script>

var fields = [
				{title : "工种编码", id : "deptId", type:"hidden",linebreak:true},
	  			{title : "名称", id : "name",rules : {required:true},linebreak:true},
	  			{title : "是否可用", id : "isActive",rules : {required:true},linebreak:true,
	  				type : "radio",
	  		        data : [
	  		            ['Y','是'],
	  		            ['N','否']
	  		        ],	
	  			}
			];
	var opts={
		validate:true,
		fixLabelWidth:true
	};
	
	//加载表单数据,在请求岗位数据的ajax返回时调用
	function loadFormData( rowData ){
		var data = {
				"deptId" : rowData.deptId,
				"name" : rowData.name,
				"isActive" : rowData.isActive
			};
			$("#autoform").iForm("setVal",data);
			$("#autoform").iForm("endEdit");
	}
	
	//初始化岗位信息
	function initForm( deptId ){
		var url = basePath + "operation/dept/queryDeptByDeptId.do?deptId=" +  deptId;
		$.ajax({
			url : url,
			type : 'post',
			dataType : "json",
			success : function(data) {
				loadFormData( data.result );
			}
		});
	}
	
	$(document).ready(function() {
		PageMode.objs.pageName="工种";
		PageMode.changeMode();
		
		$("#autoform").iForm("init",{"options":opts,"fields":fields});
		//var rowData = FW.get("operationRowData");//'<%=request.getParameter("rowData") %>';
		//rowData = JSON.parse(decodeURI( rowData ) );
		var deptId = '${param.id}';
		initForm( deptId );
		
		
		$( "#editButton" ).click(function(){
			PageMode.changeMode("edit");
		});
		
		$( "#updateButton" ).click(function(){
			if(!$("#autoform").valid()){
				return;
			}
			var formData = getFormData( "autoform" );
			var url = basePath + "operation/dept/updateDept.do";
			$.ajax({
				url : url,
				type : 'post',
				dataType : "json",
				data:{"formData":formData},
				success : function(data) {
					if (data.result == "success") {
						FW.success("修改工种成功 ！");
						PageMode.changeMode();
					} else {
						FW.error("修改工种失败 ！");
					}
				}
			});
		});
		
		$( "#deleteButton" ).click(function(){
			FW.confirm("确定删除本条数据吗？",function(){
			var formData = getFormData( "autoform" );
			var url = basePath + "operation/dept/deleteDept.do";
				$.ajax({
					url : url,
					type : 'post',
					dataType : "json",
					data:{"formData":formData},
					success : function(data) {
						if( data.result == "success" ){
							FW.success( "删除工种成功 ！");
							closeTab();
						}else{
							FW.error( "删除工种失败 ！");
						}
					}
				});
			});
		});
		
		$( "#cancelButton" ).click(function(){
			initForm( deptId );
			PageMode.changeMode();
		});
	});
</script>
</head>
<body>
<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <div id="toolbar" class="btn-toolbar " >
	        <div class="btn-group btn-group-sm" id="closeButtonDiv">
	            <button type="button" class="btn btn-default" onclick="closeTab()">关闭</button>
	        </div>
	        <div class="btn-group btn-group-sm" id="cancelButtonDiv">
				<button type="button" class="btn-default btn" id="cancelButton">取消</button>
			</div>
	        <div class="btn-group btn-group-sm" id="editButtonDiv">
				<button type="button" class="btn btn-default" id="editButton">编辑</button>
	        </div>
	        <div class="btn-group btn-group-sm" style="display: none;" id="updateButtonDiv">
				<button type="button" class="btn btn-success" id="updateButton" >保存</button>
	        </div>
	        <div class="btn-group btn-group-sm" id="deleteButtonDiv">
				<button type="button" class="btn btn-default" id="deleteButton">删除</button>
	        </div>
	    </div>
	</div>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div class="inner-title">
		编辑工种
	</div>
	<form id="autoform"></form>
</body>
</html>