<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html  style="height: 99%;">
<head>
<title>发票列表</title>
<script>_useLoadingMask = true;</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src='${basePath}js/purchase/core/purinvoice/privInvoice.js'></script>
<script src='${basePath}js/purchase/core/purinvoice/addTab.js'></script>

<script>
//第一次打开页面
var modelFlag = false;
//查询参数
var searchParams = {};

	$(document).ready(function() {
		$("#invoiceNo").iInput("init");
		
		//此处一定要给dataGrid赋值。dataGrid为全局变量，不设置会导致不能resize
		dataGrid = $("#contentTb").iDatagrid("init",{
	        pageSize:pageSize,//pageSize为全局变量，自动获取的
	        singleSelect :true,
	        queryParams : {
			    search : function (){return JSON.stringify(searchParams);},
			},
	        fitColumns:true,
	        url: basePath + "purchase/purInvoice/queryInvoiceBySiteId.do",	//basePath为全局变量，自动获取的       
	        "columns":[[
				{field:'id',title:'id',width:20,fixed:true,hidden:true},
				{field:'sheetNo',title:'编号',width:120,fixed:true, sortable: true},
				{field:'supplierId',title:'供应商ID',width:90,fixed:true,hidden:true},
				{field:'supplier',title:'供应商',width:350,fixed:true, sortable: true},
				{field:'invoiceNo',title:'发票号',width:150,fixed:true, sortable: true},
				{field:'noTaxSumPrice',title:'不含税金额',width:120,fixed:true, sortable: true},
				{field:'contractId',title:'合同ID',width:90,fixed:true, sortable: true,hidden:true},
				{field:'contractNo',title:'采购合同',width:120,fixed:true, sortable: true},
				{field:'invoiceCreateDate',title:'开票日期',fixed:true,width:100, sortable: true,
					 formatter:function(value,row,index){
						 var d = new Date( value );
						 d.setDate( d.getDate() + 150 );
						 var showText = "";
						 if( row.status == 'PUR_INVOICE_STATUS_1' && d.getTime() < new Date().getTime() ){
							 showText = '<span style="color: red;">' + FW.long2date(value) + '</span>';
						 }else{
							 showText = FW.long2date(value);
						 }
						return showText;
					} 
				},
				{field:'status',title:'状态',width:90,formatter:function(val){
					return FW.getEnumMap("PUR_INVOICE_STATUS")[val];
				}}
				]],
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
			            	$("#grid1_wrap,#toolbar_wrap").hide();
			                $("#grid1_empty").show();
			                $("#noResult,#bottomPager").hide();
		                }
	            }else{
	            	$("#grid1_wrap,#toolbar_wrap").show();
	                $("#grid1_empty").hide();
	                $("#noResult").hide();
	            }
	            //页面权限
	            setInvoicePriv();
	        },
	        onDblClickRow : function( rowIndex, rowData ){
		    	var url = "${basePath}/purchase/purInvoice/updateInvoiceToPage.do?id=" + rowData.id;
		    	addTabWithTreeDataGrid( "updateInvoice"+rowData.id, "发票信息详细"+rowData.id, url,"purchasing", "contentTb", "purchasing" );
		    }

	    });
		//新建
		$("#btn_add,#addBtn").click(function(){
			var url ="${basePath}purchase/purInvoice/insertInvoiceToPage.do";
			addTabWithTreeDataGrid( "addInvoice", "新建发票", url,"purchasing", "contentTb","purchasing");
		});
		
		//查询
		$("#invoiceNo").keypress(function(e) {
		    if(e.which == 13) {
		    	searchParams = {};
				searchParams.invoiceNo = $("#invoiceNo").val();
				
				 var pager = $("#contentTb").datagrid("getPager"); 
				 pager.pagination("select",1);
		    }
		});
		$("#invoiceNo").iInput("init",{
			"onClickIcon":function(){
				searchParams = {};
				searchParams.invoiceNo = $("#invoiceNo").val();
				
				 var pager = $("#contentTb").datagrid("getPager"); 
				 pager.pagination("select",1);
				}
		});
		
	});
</script>
</head>
<body style="height: 100%;" class="bbox list-page">
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm">
	           <button type="button" class="btn btn-success priv" privilege="PUR_INVOICE_ADD" id="btn_add">新建</button>
	        </div>
	    	<div class="input-group input-group-sm" style="width:250px;float:left;margin-top:1px">
		        <input type="text" id="invoiceNo" name="invoiceNo" icon="itcui_btn_mag" placeholder="请输入发票编号或者合同编号" style="width:250px"/>     
		    </div>
	    </div>
	    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
	    <div id="pagination_1" class="toolbar-pager" bottompager="#bottomPager">        
	    </div>
	</div>
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
			    <div style="font-size:14px">没有发票信息</div>
			    <div class="btn-group btn-group-sm margin-element">
		                 <button type="button" class="btn btn-success priv" privilege="PUR_INVOICE_ADD"  id="addBtn">新建</button>
			    </div>
			</div>
		</div>
	</div>
</body>
</html>