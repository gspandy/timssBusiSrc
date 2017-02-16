<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String itemcode = request.getParameter("itemcode")==null?"":String.valueOf(request.getParameter("itemcode"));
	String pageNum = request.getParameter("pageNum") == null ? "1":String.valueOf(request.getParameter("pageNum"));
	String pageType = request.getParameter("pageType") == null ? "":String.valueOf(request.getParameter("pageType"));
	String cateId = request.getParameter("cateId") == null ? "":String.valueOf(request.getParameter("cateId"));
	String mark = request.getParameter("mark")==null?"":String.valueOf(request.getParameter("mark"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:100%">
<head>
<link rel="shortcut icon" href="${basePath}favicon.ico" type="image/x-icon" /> 
<title>物资详细表单</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>
var mark = '<%=mark%>';
if("1" == mark) {
	_useLoadingMask = true;
}
</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script type="text/javascript" src="${basePath}js/inventory/common/inventory.js?ver=${iVersion}"></script>
<link href="${basePath}css/inventory/inventory.css" rel="stylesheet" type="text/css" />
<script>
	
	//获取全局的sheetid
	var itemcode = '<%=itemcode%>';
	var isSearchLineShow=false;
	var isSearchMode = false;
	var pageNum='<%=pageNum%>';
	var pageType='<%=pageType%>';
	var cateId='<%=cateId%>';
	
	//查看采购单	
	var item_form = [
		    	{title : "物资编码", id : "itemcode"},
			    {title : "物资名称", id : "itemname"},
			    {title : "型号规格", id : "cusmodel"},
			    {title : "单位", id : "unit1"},
			    {title : "物资类型", id : "cateName"},
			    {title : "可用库存", id : "nowqty",formatter:function(value,row,index){
					return parseFloat(value).toFixed(2);
				}},
			    {title : "实际库存", id : "totalQty",formatter:function(value,row,index){
					return parseFloat(value).toFixed(2);
				}},
			    {title : "平均成本(元)", id : "price"},
			    {title : "最近入库数量", id : "lateInQty",formatter:function(value,row,index){
					return parseFloat(value).toFixed(2);
				}},
			    {title : "最近入库价格(元)", id : "lateInPrice"},
			    {title : "最近入库时间", id : "lateInDate", type:"datetime"},
			    {title : "所属设备", id : "blgEquip"},
			    {title : "生产厂家", id : "manufacturer"},
			    {title : "备件编号", id : "sparecode"},
			    {title : "安全库存量", id : "secQty"},
			    {title : "启用安全库存", id : "issafety"},
			    {title : "最低库存量", id : "qtyLowInv"},
			    {title : "经济订购量", id : "qtyEconomic"}
		];
	
	//编辑表单加载数据（通用方法）
	function editForm(){
		$("#autoform").iForm("init",{"fields":item_form,"options":{labelFixWidth:140}});
		
		//加载用户表单数据
		$.ajax({
			type : "POST",
			async: false,
			url: basePath+"/inventory/invitem/queryInvItemDetail.do",
			data: {"itemCode":itemcode,"cateId":cateId },
			dataType : "json",
			success : function(data) {
				var loaddata = {
						"itemcode" : data.itemcode, 
						"itemname" : data.itemname,
						"cusmodel" : data.cusmodel,
						"unit1" : data.unit1, 
						"price" : data.price,
						"nowqty" : data.nowqty,  
						"totalQty" : data.totalQty,  
						"prepareQty" : data.prepareQty, 
						"lateInPrice" : data.lateInPrice, 
						"lateInQty" : data.lateInQty,
						"lateInDate" : data.lateInDate,
						"blgEquip" : data.blgEquip,
						"cateName" : data.cateName,
						"manufacturer" : data.manufacturer,
						"sparecode" : data.sparecode,
						"unit2" : data.unit2,
						"secQty" : data.secQty,
						"issafety" : data.issafety==1?"是":"否",
						"qtyLowInv" : data.qtyLowInv,
						"qtyEconomic" : data.qtyEconomic
					};
				$("#autoform").iForm("setVal",loaddata);
			}
		});
	}
	
	function initWarehouseList(){
		$("#warehouse_list").iDatagrid("init",{
			singleSelect:true,
			fitColumns : true,
 			url : basePath+"inventory/invmattrandetail/queryStockInfo.do",
			queryParams: {"itemCode": itemcode,"cateId":cateId},
			onLoadSuccess: function(data){
	            if(data.rows.length==0){
	                $("#grid_error").show();
	                $("#warehouse_detail").iFold("hide");
	            }else{
	            	$("#warehouse_detail").iFold("show");
	            	$("#warehouse_detail_Grid").css({"height":"auto"});
	            }
	            setTimeout(function(){ $("#warehouse_list").datagrid("resize"); },200);
	        }
		});
	}
	
	function initOperList(){
		$("#oper_list").iDatagrid("init",{
			singleSelect:true,
			fitColumns : true,
 			url : basePath+"inventory/invmattrandetail/queryStockOperInfo.do",
			queryParams: {"itemCode": itemcode,"cateId":cateId},
			onLoadSuccess: function(data){
	            if(data.rows.length==0){
	            	if(!isSearchMode){
		                $("#grid_error").show();
		                $("#warehouse_oper").iFold("hide");
	            	}
	            	else{
	            		$("#noSearchResult").show();
	            	}
	            }else{
	            	if(!isSearchMode){
	            		$("#warehouse_oper").iFold("show");
	            		$("#warehouse_oper_Grid").css({"height":"auto"});
	            	}
	            	else{
	            		$("#noSearchResult").hide();
	            	}
	            	$("#noSearchResult").hide();
	            }
	            setTimeout(function(){ $("#oper_list").datagrid("resize"); },200);
	        },
	        onDblClickRow:function(rowIndex, rowData){
	        	FW.showDtlPage(rowData);
			}
		});
	}
	
	//初始化查询按钮
	function initSearchBtn(){
		$("#btn_search").click(function(){
			isSearchMode = true;
		    if(isSearchLineShow){
		    	isSearchLineShow=false;
		        $("#oper_list").iDatagrid("endSearch");
		    }
		    else{		    	
		    	isSearchLineShow=true;
		        $("#oper_list").iDatagrid("beginSearch",{"noSearchColumns":{0:true,2:true,3:true,4:true},"remoteSearch":true,"onParseArgs":function(arg){
					return {"search":JSON.stringify(arg)};
				}});
		    }
		});
	}
	
	//跳转详情页面   
	FW.showDtlPage = function(rowData){
		var currTabId = FW.getCurrentTabId();
		var detailUrl = "";
		var types = rowData.trantype;
		if(null != rowData.imtid){
		$.ajax({
			type : "POST",
			async: false,
			url: basePath+"/inventory/invitem/querySheetIdByImtId.do",
			data: {"imtid":rowData.imtid,"types":types },
			dataType : "json",
			success : function(data) {
				var tabName = "物资出入库信息";
				if(types == "receivingmaterial"){//物资接收
					tabName = "物资接收";
					detailUrl = basePath+ "inventory/invmattran/invMatTranForm.do?imtid="+data.sheetId+"&openType=read";
				}else if(types == "pickingmaterials"){//根据imtid（如IMI3369）查出imrid
					tabName = "物资发料";
					detailUrl = basePath+ "inventory/invmatrecipients/invMatRecipientsForm.do?imrid="+data.sheetId;
				}else if(types == "materialscounting"){//物资盘点
					tabName = "库存盘点";
					detailUrl = basePath+ "inventory/invstocktaking/invStocktakingForm.do?istid="+data.sheetId;
				}else if(types == "materialsrefunding"){//物资退库
					tabName = "物资退库";
					detailUrl = basePath+ "inventory/invmatrefund/readInvMatRefundForm.do?imrsid="+data.sheetId;
				}else if(types == "strippermaterials"){//物资退货
					tabName = "物资退货";
					detailUrl = basePath+ "inventory/invmatreturns/invMatReturnsForm.do?imrsid="+data.sheetId;
				}else if(types == "shiftmaterialsOut"  || types == "shiftmaterialsIn"){//物资移库
					tabName = "移库申请";
					detailUrl = basePath+ "inventory/invmattransfer/invMatTransferInfo.do?imtId="+data.sheetId;
				}else if(types == "acceptancematerial"){//物资验收
					tabName = "物资验收";
					detailUrl = basePath+ "inventory/invmataccept/invMatAcceptFormJsp.do?inacId="+data.sheetId;
				}else if(types == "returnasset"){//资产归还
						tabName = "资产卡片";
						detailUrl = basePath+ "asset/assetInfo/assetDetail.do?mode=view&assetId="+rowData.sheetno;
				}
			    var prefix = rowData.imtid;
				if(""!=detailUrl){
				    FW.addTabWithTree({
						id : "editMatTranForm" + prefix,
					    url : detailUrl,
					    name : tabName,
					    tabOpt : {
					    	closeable : true,
					        afterClose : "FW.deleteTab('$arg');FW.activeTabById('"+currTabId+"');"
					    }
					});	
				}
			}
		});
		}
	};
	
	//初始化界面
	$(document).ready(function() {
		editForm();
		$("#autoform").ITC_Form("readonly");
		
		$("#warehouse_detail").ITCUI_Foldable();
		initWarehouseList();
		
		$("#warehouse_oper").ITCUI_Foldable();
		initOperList();
		/******************************按钮初始化 begin***********************************/
		//关闭按钮	
		$("#btn-back").click(function(){
			if(pageType == "more"){
				window.location.href = basePath + "inventory/invitem/invTreeItemList.do?embbed=&cateId="+cateId+"&opentype=&pageNum="+pageNum;
			}else{
				window.location.href = basePath + "inventory/invitem/invItemList.do?pageNum="+pageNum;
			}
			
		});
		initSearchBtn();
	});
</script>

</head>
<body>
<div class="toolbar-with-pager bbox">
	<div class="btn-toolbar" role="toolbar">
	    <div class="btn-group btn-group-sm">
	        <button type="button" class="btn btn-default" id="btn-back">返回</button>
	    </div>
	</div>
</div>
	
	<form id="autoform" class="margin-form-title margin-form-foldable autoform"></form>
	
	<div id="warehouse_detail" grouptitle="库存明细">
		<div id="warehouse_detail_Grid" class="margin-title-table">
		<table id="warehouse_list" class="eu-datagrid">
			<thead>
				<tr>
					<th data-options="field:'warehousename',width:60">仓库</th>
					<th data-options="field:'binname',width:60">货柜</th>
					<th data-options="field:'lotno',width:56,fixed:true,hidden:true">批次</th>
					<th data-options="field:'nowqty',width:60,align:'right',
						formatter:function(value,row,index){
							if( null===value || ''===value){
								return '--';
							}else{
								return parseFloat(value).toFixed(2);
							}
					}">可用库存</th>
					<th data-options="field:'stockQty',width:60,align:'right',
						formatter:function(value,row,index){
						if( null===value || ''===value){
								return '--';
							}else{
								return parseFloat(value).toFixed(2);
							}
					}">实际库存</th>
					<th data-options="field:'status',width:70,fixed:true,hidden:true">操作类型</th>
					<th data-options="field:'createdate',width:170,hidden:true">交易时间</th>
				</tr>
			</thead>
			<tbody></tbody>
		</table>
		</div>
	</div>
	<div class="margin-group"></div>
	
	<div id="warehouse_oper" grouptitle="仓库操作明细">
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <div id="toolbar1" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm">
	        	<button type="button" id="btn_search" data-toggle="button" class="btn btn-default">查询</button>
	        </div>
	    </div>
	    <div id="pagination" class="toolbar-pager"></div>
	</div>

	<div style="clear:both"></div>
	<div id="warehouse_oper_Grid" class="margin-title-table">
		<table id="oper_list" pager="#pagination" class="eu-datagrid">
			<thead>
				<tr>
					<th data-options="field:'createdate',width:130,fixed:true,sortable:true,formatter:function(value,record){
	        			    return FW.long2time(record.createdate);
	        		    }">操作时间</th>
					<th data-options="field:'opertype',width:90,fixed:true,sortable:true,formatter:function(value,record){
							if(value=='物资领料'){
								return '物资发料';
							}
							else{
								return value;
							}
	        		    }">操作类型</th>
					<th data-options="field:'sheetname',width:1,sortable:true,formatter:function(value,record){
						if('returnasset' == record.trantype){
							return record.outterSheetno;
						}
						else{
							return value;
						}
	        		}">单据名称</th>
					<!-- <th data-options="field:'outterSheetno',width:110,fixed:true,sortable:true">关联单号</th> -->
					<th data-options="field:'warehousename',width:100,fixed:true">仓库</th>
					<th data-options="field:'binname',width:70,fixed:true">货柜</th>
					<!-- <th data-options="field:'lotno',width:40,fixed:true">批次</th>  -->
					<th data-options="field:'stockQty',width:57,fixed:true,align:'right',styler: function(value,row,index){
						if (value.indexOf('+')==0){
							return 'color:green';
						}else if(value.indexOf('-')==0){
							return 'color:red';
						}
					}">数量</th>
					<th data-options="field:'remark',width:1">备注</th>
				</tr>
			</thead>
			<tbody></tbody>
		</table>
		<div id="noSearchResult" style="width:100%;display:none">
			<span style="pull-middle">没有找到符合条件的结果</span>
		</div>
	</div>	
	</div>
	<div class="margin-group"></div>
	<div class="row" id="grid_empty" style="display:none"></div>
</body>
</html>