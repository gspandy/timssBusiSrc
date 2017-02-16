<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:99%">
<head>
<title>未选择仓库</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
	function addWarehouse(){
		var prefix = new Date().getTime() + "" + Math.floor(Math.abs(Math.random()*100));
	    FW.addTabWithTree({
	        id : "addWh" + prefix,
	        url : basePath + "inventory/invwarehouse/invWarehouseForm.do?mode=create",
	        name : "仓库",
	        tabOpt : {
	            closeable : true,
	            afterClose : "FW.deleteTab('$arg');FW.activeTabById('stock');"
	        }
	    });
	}
</script>
<body style="height:100%">
	<div id="grid_error" style="width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">请先在左侧树中选择一个仓库，或者</div>
			    <div class="btn-group btn-group-sm margin-element">
		             <button type="button" class="btn btn-success btn-new" onclick="addWarehouse()">新建仓库</button>
			    </div>
			</div>
		</div>
	</div>
</body>
</html>