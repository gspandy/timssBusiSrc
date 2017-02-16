<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>

<title>考勤机详情</title>


<script type="text/javascript">
_useLoadingMask=true;
</script>
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script type="text/javascript" src="${basePath}js/attendance/machineDetail.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/attendance/atdPriv.js?ver=${iVersion}"></script>
<script type="text/javascript">
	$(document).ready(function() {		
		//respond.update();
		
		Machine.objs["mode"]="${params.mode}";//模式有浏览、新建、编辑三种：view/create/edit
		Machine.objs["machineId"]="${params.machineId}";
		Machine.objs["machineBean"]=${params.machineBean};
		MachinePriv.init();
		Machine.init();		
	});
	
</script>
<style type="text/css">
.btn-garbage{
cursor:pointer;
}
html{
	height:95%
}
body{
	height:100%
}
</style>
</head>

<body>
	<div class="toolbar-with-pager bbox">
		<div class="btn-toolbar" role="toolbar">
			<div class="btn-group btn-group-sm" id="btnBack">
				<button class="btn-default btn" onclick="Machine.toBack()">返回</button>
			</div>
			<div class="btn-group btn-group-sm" id="btnEdit">
				<button class="btn-default btn priv" privilege="VIRTUAL-EDIT" onclick="Machine.toEdit()">编辑</button>
			</div>
			<div class="btn-group btn-group-sm" id="btnSave" style="display:none">
				<button class="btn-success btn" onclick="Machine.updateMachine()">保存</button>
			</div>
			<div class="btn-group btn-group-sm" id="btnCreate" style="display:none">
				<button class="btn-success btn" onclick="Machine.createMachine()">保存</button>
			</div>
			<div class="btn-group btn-group-sm" id="btnDel">
				<button class="btn-default btn priv" privilege="VIRTUAL-DELETE" onclick="Machine.toDelete()">删除</button>
			</div>	
		</div>
	</div>
	<!-- <div style="clear:both"></div> -->
	
	<div class="inner-title" id="pageTitle">
		此处写页标题
	</div>
	<form id="form_baseinfo" class="margin-form-title margin-form-foldable">

	</form>
	
	<div class="margin-group"></div>
</body>
</html>
