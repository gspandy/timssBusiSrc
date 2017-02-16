<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% String isCkShow = request.getParameter("isCkShow")==null?"":String.valueOf(request.getParameter("isCkShow"));%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:99%">
<head>
<link rel="shortcut icon" href="${basePath}favicon.ico" type="image/x-icon" /> 
<title>物资列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
	var isSearchLineShow=false;
	var isCkShow='<%=isCkShow%>';
	var isSearchMode = false;
	isCkShow='true';
	var wareHouseArray = ${wareHouseArray};//仓库数组
	var currentWareHouseName;//当前选中仓库
	var currentRangeVal;//当前查询范围
	var currentItemInfo;//当前物资信息
	
	$(document).ready(function() {
		//查询工具栏初始化
		InvSafetyStockBtn.init();
		FW.fixToolbar("#toolbar1");
		
		//仓库选择项初始化
		for(var i=0; i<wareHouseArray.length; i++){
			var wareHouseName = "";
			if(!!wareHouseArray[i][1]){
				wareHouseName = wareHouseArray[i][1].replace(/\'/g, "\\\'");
			}
			var funcStr = "onSelectWareHouse('" + wareHouseArray[i][0] + "','" + wareHouseName + "',true)";
			var str = "<li><a id=" + wareHouseArray[i][0] + "MenuItem onclick=" + funcStr + ">" + wareHouseArray[i][1] + "</a></li>";
			$("#selectWareHouseMenu").append(str);
		}

		//默认选中第一个仓库，不触发查询
		if(wareHouseArray.length>0){
			onSelectWareHouse(wareHouseArray[0][0],wareHouseArray[0][1],false);
		}
		
		//默认只显示库存不足物资，不触发查询
		onSelectRange("notEnough","只显示库存不足物资",false);
		
		//表格初始化并进行第一次查询
		initItemGrid();
	});
	
</script>
<script src="${basePath}js/inventory/common/inventory.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invsafetystock/invItemSafetyStock.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invsafetystock/invItemSafetyStockList.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invsafetystock/invItemSafetyStockBtn.js?ver=${iVersion}"></script>
</head>
<body style="height: 100%;" class="bbox list-page">
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
		<!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div class="btn-toolbar" id="toolbar1">
		    <div class="btn-group btn-group-sm" style="float:left;margin-left:7px;">
			    <button type="button" class="btn btn-default priv dropdown-toggle" id="btn-selectWareHouse" data-toggle="dropdown">
			        <label id="currentWareHouseNameLabel">请选择仓库</label>
			        <span class="caret"></span>
			    </button>
		        <ul class="dropdown-menu" id="selectWareHouseMenu">
		        </ul>
		    </div>	  		 
		    <div class="btn-group btn-group-sm" style="float:left;margin-left:7px;">
			    <button type="button" class="btn btn-default priv dropdown-toggle" id="btn-selectRange" data-toggle="dropdown">
			        <label id="currentRangeNameLabel">查看</label>
			        <span class="caret"></span>
			    </button>
		        <ul class="dropdown-menu" id="selectRangeMenu">
		        	<li><a id="notEnoughMenuItem" onclick="onSelectRange('notEnough','库存不足',true)">库存不足</a></li>
		        	<li><a id="allMenuItem" onclick="onSelectRange('all','全部',true)">全部</a></li>
		        </ul>
		    </div>	  		       
		    <div class="btn-group btn-group-sm" style="float:left;margin-left:7px;">
	        	<button type="button" id="btn_purapply" class="btn btn-default" disabled="disabled">生成采购申请</button>
	        </div>
		    <div class="btn-group btn-group-sm" style="float:left;margin-left:7px;">
	        	<button type="button" id="btn_editSafeQty" onclick="editSafeQty()" class="btn btn-default">修改安全库存</button>
	        	<button type="button" id="btn_saveSafeQty" onclick="saveSafeQty()" class="btn btn-default" style="display:none">保存安全库存</button>
	        </div>	        
	        <div class="input-group input-group-sm" style="float:left;margin-right: 7px;">
		        <input type="text" id="item_search" icon="itcui_btn_mag" placeholder="请输入物资编号或名称" style="width:160px"/>     
		    </div>	 	        
	    </div>
	    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
	    <div id="pagination" class="toolbar-pager" bottompager="#bottomPager"></div>
	</div>
	
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div id="grid_wrap" style="width:100%">
	    <table id="item_grid" pager="#pagination" class="eu-datagrid"></table>
	    <div id="noSearchResult" style="width: 100%;display:none;font-size:14px;text-align:center">
			<span>没有找到符合条件的结果</span>
		</div>
	</div>

	<!-- 无数据 -->
	<div id="grid_error" style="display:none;width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">当前仓库没有物资低于安全库存量</div>
			    <div class="btn-group btn-group-sm margin-element">
		        	<button type="button" class="btn btn-success btn_goset" id="btn_goset">去设置</button>
			    </div>
			</div>
		</div>
	</div>

	<div id="bottomPager" style="width:100%">
	</div>
</body>
</html>