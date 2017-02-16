<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% 
	String imsid = request.getParameter("imsid") == null ? "":String.valueOf(request.getParameter("imsid"));
	String siteid = request.getAttribute("siteid")==null?"":String.valueOf(request.getAttribute("siteid")); 
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:99%">
<head>
<link rel="shortcut icon" href="${basePath}favicon.ico" type="image/x-icon" />
<title>库存快照列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<link href="${basePath}css/inventory/inventory.css" rel="stylesheet" type="text/css" />
<script src="${basePath}js/inventory/common/inventory.js?ver=${iVersion}"></script>
<script>
	var isSearchLineShow=false;
	var isSearchMode = false;
	var imsid = '<%=imsid%>';
	var siteid = '<%=siteid%>';
	
	$(document).ready(function(){
		initList();
		
		//快速查询输入框
		$("#quickSearchInput").keypress(function(e) {
		    if(e.which == 13) {
		        initList();
		    }
		});
		$("#quickSearchInput").iInput("init",{"onClickIcon":function(val){initList();}});
		
		//打印按钮
		 $("#btn_print").bindDownload({
			url : fileExportPath+"preview?__format=xlsx&__report=report/TIMSS2_IMS_001_xlsx.rptdesign&imsid="+imsid+"&siteid="+siteid
		}); 
		
		//预览
		$("#btn_preview").click(function(){
			FW.dialog("init",{
				src: fileExportPath+"preview?__report=report/TIMSS2_IMS_001_HTML.rptdesign&imsid="+imsid+"&siteid="+siteid,
				btnOpts:[{
			            "name" : "关闭",
			            "float" : "right",
			            "style" : "btn-default",
			            "onclick" : function(){
			                _parent().$("#itcDlg").dialog("close");
			             }
			        }],
				dlgOpts:{ width:800, height:650, closed:false, title:"库存快照", modal:true }
			});
		});
	});

	//页面刷新
	function refCurPage(){
	 	$("#invmatsnapshotdetail_grid").datagrid("reload");
	}
	
	//列表
	function initList(){
		var qsi = encodeURIComponent($("#quickSearchInput").val());
		$("#invmatsnapshotdetail_grid").iDatagrid("init",{
			singleSelect:true,
			pageSize:pageSize,
			queryParams: {
				"quickSearch":qsi,
				"imsid": imsid 
				},
			url: basePath+"inventory/invmatsnapshot/quickMatSnapshotSearch.do",
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
		                $("#grid_wrap,#toolbar_wrap").hide();
		                $("#grid_error").show();
		            }else{
		            	$("#grid_wrap,#toolbar_wrap").show();
		                $("#grid_error").hide();
		            }
		            $("#noSearchResult").hide();
				}
				changeStyle();
				setTimeout(function(){ $("#invmatsnapshotdetail_grid").datagrid("resize"); },200);
				isSearchMode = false;
			}
		});
	}
	
	function changeStyle(){
		var rows = $("#invmatsnapshotdetail_grid").prev(".datagrid-view2").children(".datagrid-body").find(".datagrid-row");
		for(var i=0;i<rows.length;i++){
			var row = $(rows[i]);
			
			var stockQty = row.children("td[field='stockQty']").children("div").html();
			stockQty = parseFloat(stockQty);
			var stockQtyNow = row.children("td[field='stockQtyNow']").children("div").html();
			stockQtyNow = parseFloat(stockQtyNow);
			if(stockQty<stockQtyNow){
				row.children("td[field='stockQtyNow']").children("div").css("color","green");
			}else if(stockQty>stockQtyNow){
				row.children("td[field='stockQtyNow']").children("div").css("color","red");
			}
			
			var price = row.children("td[field='price']").children("div").html();
			price = parseFloat(price);
			var priceNow = row.children("td[field='priceNow']").children("div").html();
			priceNow = parseFloat(priceNow);
			if(price<priceNow){
				row.children("td[field='priceNow']").children("div").css("color","green");
			}else if(price>priceNow){
				row.children("td[field='priceNow']").children("div").css("color","red");
			}
		}
	}
</script>
</head>
<body style="height: 100%;" class="bbox list-page">
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
		<!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar1" class="btn-toolbar">
	        <div class="btn-group btn-group-sm">
	        	<button type="button" id="btn_print" class="btn btn-default">导出</button>
	        	<button type="button" id="btn_preview" class="btn btn-default">预览</button>
	        </div>
	        <div class="input-group input-group-sm" style="width:250px;float:left;margin-left:7px;margin-top:1px">
		        <input type="text" id="quickSearchInput" icon="itcui_btn_mag" placeholder="请输入物资编号、名称或型号" style="width:250px"/>     
		    </div>
	    </div>
	    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
	    <div id="pagination" class="toolbar-pager" bottompager="#bottomPager"></div>
	</div>
	
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div id="grid_wrap" style="width:100%">
	    <table id="invmatsnapshotdetail_grid" pager="#pagination" class="eu-datagrid">
	    	<thead>
				<tr>
					<th data-options="field:'itemcode',width:80,fixed:true,sortable:true">物资编号</th>
					<th data-options="field:'itemname',width:120,sortable:true">物资名称</th>
					<th data-options="field:'cusmodel',width:160,fixed:true,sortable:true">规格型号</th>
					<th data-options="field:'invcatename',width:100,fixed:true,sortable:true">物资类型</th>
					<th data-options="field:'warehousename',width:120,fixed:true">仓库</th>
					<th data-options="field:'binname',width:80,fixed:true">货柜</th>
					<th data-options="field:'unitname',width:60,fixed:true">单位</th>
					<th data-options="field:'stockQty',width:80,fixed:true,sortable:true,
						formatter:function(value,row,index){
							return parseFloat(value).toFixed(2);
					}">存量</th>
					<th data-options="field:'noTaxPrice',width:90,fixed:true,sortable:true,
						formatter:function(value,row,index){
							return parseFloat(value).toFixed(2);
					}">税前单价(元)</th>
					<th data-options="field:'price',width:90,fixed:true,sortable:true,
						formatter:function(value,row,index){
							return parseFloat(value).toFixed(2);
					}">税后单价(元)</th>					
				</tr>
			</thead>
	    </table>
	    <div id="noSearchResult" style="width: 100%;display:none">
			<span>没有查询到相关数据</span>
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
	<!--大表需要加下分页器-->
	<div id="bottomPager" style="width:100%"></div>
</body>
</html>