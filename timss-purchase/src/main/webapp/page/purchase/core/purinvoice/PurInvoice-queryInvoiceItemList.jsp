<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html  style="height: 99%;">
<head>
<title>合同物资列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src='${basePath}js/purchase/core/purinvoice/addTab.js'></script>

<script>
//第一次打开页面
var modelFlag = false;
var contractId = "${contractId}";

var wuziColumns = [[
    	            {field:'itemId',title:'',checkbox:true,fixed:true},
    	            {field:'version',title:'版本号',width:140,fixed:true,hidden:true},
    	            {field:'isReceived',title:'是否已经入库',width:140,fixed:true,hidden:true},
    	            {field:'itemCode',title:'物资编号',width:100,fixed:true},
    	            {field:'purchName',title:'物资名称',width:100,fixed:true},
    	            {field:'type',title:'物资型号',width:100,fixed:true},
    	            {field:'unit',title:'单位',width:50,fixed:true},
    	            {field:'unitPrice',title:'采购单价',width:70,fixed:true},
    	            {field:'mount',title:'采购量',width:50,fixed:true},
    	            {field:'receivedMount',title:'已接收数量',width:75,fixed:true},
    	            {field:'receivedPrice',title:'已接收金额',width:75,fixed:true},
    	            {field:'noTaxInvoicePrice',title:'发票单价',width:75,fixed:true},
    	            {field:'taxUnitPrice',title:'含税单价',width:75,fixed:true},
    	            {field:'taxSum',title:'税额',width:75 }
    				]];	


	$(document).ready(function() {
		
		//此处一定要给dataGrid赋值。dataGrid为全局变量，不设置会导致不能resize
		dataGrid = $("#contentTb").iDatagrid("init",{
	        checkbox: true,
	        fitColumns:true,
	        nowrap : false,
	        url: basePath + "purchase/purInvoice/queryWuziDatagridByContractId.do?contractId=" + contractId,	//basePath为全局变量，自动获取的       
	        "columns":wuziColumns,
			"onLoadError": function(){
				//加载错误的提示 可以根据需要添加
				$("#grid1_wrap,#toolbar_wrap,#bottomPager").hide();
			    $("#grid1_error").show();
			},
	        onLoadSuccess: function(data){
	            //远程无数据需要隐藏整个表格和对应的工具条，然后在无数据信息中指引用户新建
	            if(data && data.total==0){
	                if( modelFlag ){
	                	$("#noResult").show();
	                }else{
	                	$("#grid1_wrap").hide();
		                $("#grid1_empty,#toolbar_wrap").show();
		                $("#noResult").hide();
	                }
	            }else{
	            	$("#grid1_wrap,#toolbar_wrap").show();
	                $("#grid1_empty").hide();
	                $("#noResult").hide();
	            }
	            
	        },
	        onDblClickRow : function( rowIndex, rowData ){

	        }

	    });
		
		
	});
</script>
</head>
<body style="height: 100%;" class="bbox list-page">
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div id="grid1_wrap" style="width:100%">
	    <table id="contentTb" pager="#pagination_1" class="eu-datagrid">
	    </table>
	</div>
	<!-- 下页器部分-->
	<div id="bottomPager" style="width:100%;margin-top:6px">
	</div>
	
	<div id="noResult" style="display:none;width:100%;">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px"> 没有找到符合条件的结果</div>
			    
			</div>
		</div>
	</div>
	
	<!-- 错误信息-->
	<div class="row" id="grid1_error" style="display:none">
	    无法从服务器获取数据，请检查网络是否正常
	</div>
	<!-- 无数据 -->
	<div id="grid1_empty" style="display:none;width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">没有合同相关的采购清单信息</div>
			</div>
		</div>
	</div>
</body>
</html>