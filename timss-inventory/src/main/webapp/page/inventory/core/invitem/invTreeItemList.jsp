<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String cateId = request.getAttribute("cateId")==null?"":String.valueOf(request.getAttribute("cateId"));
	String active = request.getParameter("active")==null?"":String.valueOf(request.getParameter("active"));
	String wareId = request.getAttribute("wareId")==null?"":String.valueOf(request.getAttribute("wareId"));
	String isspare = request.getParameter("isspare")==null?"":String.valueOf(request.getParameter("isspare"));
	String invmatapply = request.getParameter("invmatapply")==null?"":String.valueOf(request.getParameter("invmatapply"));
	String embbed = request.getAttribute("embbed")==null?"":String.valueOf(request.getAttribute("embbed"));
	String opentype = request.getAttribute("opentype")==null?"":String.valueOf(request.getAttribute("opentype"));
	String isCkShow = request.getParameter("isCkShow")==null?"":String.valueOf(request.getParameter("isCkShow"));
	String pageNum = request.getParameter("pageNum") == null ? "1":String.valueOf(request.getParameter("pageNum"));
	String categoryname = request.getParameter("categoryname")==null?"":String.valueOf(request.getParameter("categoryname"));
	String siteId = request.getAttribute("siteId") == null ? "":String.valueOf(request.getAttribute("siteId"));
	String multi_warehouse = request.getAttribute("multi_warehouse") == null ? "":String.valueOf(request.getAttribute("multi_warehouse"));
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:99%">
<head>
<link rel="shortcut icon" href="${basePath}favicon.ico" type="image/x-icon" /> 
<title>物资列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<link href="${basePath}css/inventory/inventory.css" rel="stylesheet" type="text/css" />
<script src="${basePath}js/inventory/common/inventory.js?ver=${iVersion}"></script>
<script>
	var embbed = '<%=embbed%>';
	var categoryname = '<%=categoryname%>';
	var _dialogEmmbed =embbed == '1'? true:false;
	var cateId = '<%=cateId%>';
	var active = '<%=active%>';
	var wareId = '<%=wareId%>';
	var isspare = '<%=isspare%>';
	var invmatapply = '<%=invmatapply%>';
	var opentype = '<%=opentype%>';
	var isSearchLineShow=false;
	var isSearchMode = false;
	var isCkShow='<%=isCkShow%>';
	var isShowHis = privMapping["storeQuery_tohistory"];
	var isShowsph = privMapping["storeQuery_snopshot"];		//是否显示生成库存快照按钮
	var pageNum='<%=pageNum%>';
	var multi_warehouse = '<%=multi_warehouse%>';//是否多个仓库
	
	var siteId = '<%=siteId%>';
	
	$(document).ready(function() {
		InvItemBtn.init();
		InvItemPriv.init();
		if(Priv.hasPrivilege("INVMATTRANSFER_NEW") && (multi_warehouse=="true") && "1" != embbed && "new" != opentype){
			$("#btn_mattransfer").show();
		}
		else{
			$("#btn_mattransfer").hide();
		}	
		//$("#btn_ABC").hide();
		$("#tohistory").hide();
		initGrid(cateId,wareId);
		if("new" == opentype){
			$("#item_grid").datagrid("hideColumn","ck");
			//$("#item_grid").datagrid("hideColumn","stockqty");
			$("#item_grid").datagrid("hideColumn","price");
			$("#item_grid").datagrid("hideColumn","sparecode");
			$("#item_grid").datagrid("hideColumn","manufacturer");
			$("#item_grid").datagrid({
				onDblClickRow:function(rowIndex, rowData){
					window.location.href = basePath+ "/inventory/invitem/invMainItemDetail.do?itemId="+rowData.itemid;
				}
			});
			$("#btn_matapply").hide();
			$("#tohistory").hide();
			$("#snopshot").hide();
		}
		if("1" != embbed && "new" != opentype){
			$("#item_grid").datagrid("hideColumn","sparecode");
			$("#item_grid").datagrid("hideColumn","manufacturer");
			$("#item_grid").datagrid({
				onDblClickRow:function(rowIndex, rowData){
					FW.navigate(basePath+"inventory/invitem/invTreeItemDetail.do?cateId="+rowData.cateId+"&pageType=more&pageNum="+pageNum+"&itemcode="+rowData.itemcode+"&mark=1");
				}
			});
			$("#btn_matapply").show();
			$("#btn_new").hide();
			$("#tohistory").show();
			//$("#btn_ABC").show();
			//$("#btn_ABC").bindDownload({
			//	url : fileExportPath+"preview?__format=xlsx&__report=report/TIMSS2_ABC_001_xlsx.rptdesign&siteid="+siteId
			//});
		}else{
			$("#btn_matapply").hide();
			$("#tohistory").hide();
			$("#snopshot").hide();
			$("#btn_new").hide();
		}
		
		FW.fixToolbar("#toolbar");
	});
	
	//获取列表中选择的信息
	function getSelected(){
		var obj = $("#item_grid").datagrid("getChecked");
		if(obj){
			var o = {};
			for(var i=0;i<obj.length;i++){
				o[obj[i].itemcode] = JSON.stringify(obj[i]);
			}
			return o;
		}
		else{
			return null;
		}
	}
	
	//获取所选中的条目
	function getFullDataSelected(){
		return $("#item_grid").datagrid("getChecked");		
	}
</script>
<script src="${basePath}js/inventory/common/inventory.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invitem/invTreeItemList.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invitem/invItemBtn.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invitem/invItemProc.js?ver=${iVersion}"></script>
</head>
<body style="height: 100%;" class="bbox list-page">
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
		<!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm">
	        	<button type="button" id="btn_new" class="btn btn-success priv" privilege="itemList_new">新建</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	        	<button type="button" id="btn_matapply" class="btn btn-success priv" privilege="storeQuery_itemapply">新建领料单</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	        	<button type="button" id="btn_mattransfer" class="btn btn-default priv" privilege="INVMATTRANSFER_NEW">移库</button>
	        </div>	        
	        <div class="btn-group btn-group-sm">
	        	<button type="button" id="tohistory" class="btn btn-default priv" privilege="storeQuery_tohistory" disabled="disabled">转为历史库存</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	        	<button type="button" id="btn_search" data-toggle="button" class="btn btn-default">查询</button>
	        </div>
	    </div>
	    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
	    <div id="pagination" class="toolbar-pager" bottompager="#bottomPager"></div>
	</div>
	
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div id="grid_wrap" style="width:100%">
	    <table id="item_grid" pager="#pagination" class="eu-datagrid"></table>
	    <div id="noSearchResult" style="width: 100%;display:none">
			<span>没有找到符合条件的结果</span>
		</div>
	</div>

	<div id="bottomPager" style="width:100%"></div>

	<div id="grid_error" style="display:none;width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">没有查询到相关数据</div>
			</div>
		</div>
	</div>﻿
	
</body>
</html>