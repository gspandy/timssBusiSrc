<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:99%">
<head>
<link rel="shortcut icon" href="${basePath}favicon.ico" type="image/x-icon" />
<title>物资出入库查询列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<link href="${basePath}css/inventory/inventory.css" rel="stylesheet" type="text/css" />
<script>
	var isSearchLineShow=false;
	var isSearchMode = false;
	var inColumns = [[
			{field:'imtid',title:'交易表id',width:10,fixed:true,hidden:true},
			{field:'sheetno',title:'编号',width:150,fixed:true,sortable:true,
				formatter:function(value,row){
					if("returnasset" == row.tranType){
						return row.outsheetno;
					}
					else{
						return value;
					}
				}
			},
			{field:'tranTypeName',title:'入库类型',width:100,fixed:true,sortable:true},
			{field:'createdate',title:'入库日期',width:100,fixed:true,sortable:true},
			{field:'operuser',title:'入库人',width:70,fixed:true},
			{field:'warehouseName',title:'仓库',width:120,fixed:true},
			{field:'totalPrice',title:'总成本(元)',width:90,fixed:true,align:'right',sortable:true},
			{field:'remark',title:'备注',width:120},
			{field:'tranType',title:'类型',hidden:true}
		]];
		
	var outColumns = [[
			{field:'imtid',title:'交易表id',width:10,fixed:true,hidden:true},
			{field:'sheetno',title:'编号',width:150,fixed:true,sortable:true},
			//{field:'outsheetno',title:'领料申请单编号',width:120,fixed:true,sortable:true},
			//{field:'outsheetname',title:'名称',width:150,sortable:true},
			{field:'tranTypeName',title:'出库类型',width:100,fixed:true,sortable:true,
				formatter:function(value){
					if(value=="物资领料"){
						return "物资发料";
					}
					else{
						return value;
					}
				}
			},
			{field:'createdate',title:'出库日期',width:100,fixed:true,sortable:true},
			{field:'operuser',title:'出库人',width:70,fixed:true},
			{field:'warehouseName',title:'仓库',width:120,fixed:true},
			{field:'totalPrice',title:'总成本(元)',width:120,fixed:true,align:'right',sortable:true},
			{field:'remark',title:'备注',width:120},
			{field:'tranType',title:'类型',hidden:true}
		]];
	var inUrl = basePath+"inventory/invmattran/queryInMatTran.do";
	var outUrl = basePath+"inventory/invmattran/queryOutMatTran.do";
	
	$(document).ready(function() {
		initList(inUrl,inColumns);
		$("#in_label").addClass("active");
		initBtn();
	});
	
	function initList(url,columns){
		var detailUrl = "";
		$("#invmattran_grid").iDatagrid("init",{
			singleSelect:true,
			columns:columns,
			url: url,
			pageSize:pageSize,
			onLoadSuccess:function(data){
				if(isSearchMode){
	            	 if(data && data.total==0){
	            		 $("#noSearchResult").show();
	            	 }
	            	 else{
	            		 $("#noSearchResult").hide();
	            	 }
	            }
				else{
					if(data && data.total==0){
						$("#grid_wrap").hide();
		                $("#grid_error").show();
					}else{
		                $("#grid_wrap").show();
		                $("#grid_error").hide();
					}
					$("#noSearchResult").hide();
				}
				isSearchMode = false;
				$("#invmattran_grid").datagrid("resize");
			},
			onDblClickRow : function(rowIndex, rowData) {
				if(null != rowData.imtid){
					var types = rowData.tranType;
					var tabName = "物资出入库信息";
					if(types == "receivingmaterial"){//物资接收
						tabName = "物资接收";
						detailUrl = basePath+ "inventory/invmattran/invMatTranForm.do?imtid="+rowData.imtid+"&openType=read";
					}else if(types == "pickingmaterials"){//物资领料
						tabName = "物资发料";
						detailUrl = basePath+ "inventory/invmatrecipients/invMatRecipientsForm.do?imrid="+rowData.imtid;
					}else if(types == "materialscounting"){//物资盘点
						tabName = "库存盘点";
						detailUrl = basePath+ "inventory/invstocktaking/invStocktakingForm.do?istid="+rowData.imtid;
					}else if(types == "materialsrefunding"){//物资退库
						tabName = "物资退库";
						detailUrl = basePath+ "inventory/invmatrefund/readInvMatRefundForm.do?imrsid="+rowData.imtid;
					}else if(types == "strippermaterials"){//物资退货
						tabName = "物资退货";
						detailUrl = basePath+ "inventory/invmatreturns/invMatReturnsForm.do?imrsid="+rowData.imtid;
					}else if(types == "shiftmaterialsOut"  || types == "shiftmaterialsIn"){//物资移库
						tabName = "移库申请";
						detailUrl = basePath+ "inventory/invmattransfer/invMatTransferInfo.do?imtId="+rowData.imtid;
					}else if(types == "acceptancematerial"){//物资验收
						tabName = "物资验收";
						detailUrl = basePath+ "inventory/invmataccept/invMatAcceptFormJsp.do?inacId="+rowData.imtid;
					}else if(types == "returnasset"){//资产归还
						tabName = "资产卡片";
						detailUrl = basePath+ "asset/assetInfo/assetDetail.do?mode=view&assetId="+rowData.sheetno;
					}
					
			    	var prefix = rowData.imtid;
				    FW.addTabWithTree({
				        id : "editMatTranForm" + prefix,
				        url : detailUrl,
				        name : tabName,
				        tabOpt : {
				            closeable : true,
				            afterClose : "FW.deleteTab('$arg');FW.activeTabById('stock');FW.getFrame(FW.getCurrentTabId()).refCurPage();"
				        }
				    });
				}
			}
		});
	}
	
	function initBtn(){
		//查询
		$("#btn_search").click(function(){
		    if(isSearchLineShow){
			    isSearchLineShow=false;
		        $("#invmattran_grid").iDatagrid("endSearch");
		    }
		    else{
		    	isSearchLineShow=true;		    	
		       	$("#invmattran_grid").iDatagrid("beginSearch",{"noSearchColumns":{7:true},"remoteSearch":true,"onParseArgs":function(arg){
		       		isSearchMode = true;
					return {"search":JSON.stringify(arg)};
				}});
		    }
		});
	}
</script>
</head>
<body style="height: 100%;" class="bbox list-page">
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <div class="btn-group btn-group-sm fl" data-toggle="buttons" style="margin-right: 7px;">
		    <label class="btn btn-default" onclick="javascript:initList(inUrl,inColumns,'in')" id="in_label">
		        <input type="radio" name="searchBtn" >物资入库查询
		    </label>
		    <label class="btn btn-default" onclick="javascript:initList(outUrl,outColumns,'out')" id="out_label">
		        <input type="radio" name="searchBtn">物资出库查询
		    </label>
		</div>
		<div class="btn-group btn-group-sm fl">
		    <button type="button" id="btn_search" data-toggle="button" class="btn btn-default">查询</button>
		</div>
	    
	    <div id="pagination" class="toolbar-pager" bottompager="#bottomPager"></div>
	</div>
	
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div id="grid_wrap" style="width:100%">
	    <table id="invmattran_grid" pager="#pagination" class="eu-datagrid"></table>
	    <div id="noSearchResult" style="width: 100%;display:none">
			<span>没有找到符合条件的结果</span>
		</div>
	</div>
	
	<!-- 无数据 -->
	<div id="grid_error" style="display:none;width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">没有查询到相关数据</div>
			</div>
		</div>
	</div>
	<div id="bottomPager" style="width:100%"></div>
</body>
</html>