<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:99%">
<head>
<link rel="shortcut icon" href="${basePath}favicon.ico" type="image/x-icon" /> 
<title>物资发料列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">    
<script>
	var siteId = '${siteid}';
	var isSearchLineShow=false;
	var sdata = "";
	function initList(){
		$("#invmatrecipientsapply_grid").iDatagrid("init",{
			singleSelect:true,
			pageSize:pageSize,
			url: basePath + "inventory/invmatrecipients/queryInvMatRecipientsApplyList.do",
			queryParams : {
	        	"sdata" : function(){
	        		return sdata;
	        	}
	        },
			onLoadSuccess:function(data){
				 //远程无数据需要隐藏整个表格和对应的工具条，然后在无数据信息中指引用户新建
	            if(data && data.total==0){
	                $("#grid_wrap").show();
	                $("#grid_error").show();
	                $("#toolbar_wrap").show();
	                $("#bottomPager").hide();
	            }else{
	            	$("#toolbar_wrap").show();
	            	$("#grid_wrap").show();
	                $("#grid_error").hide();
	            }
	            setTimeout(function(){ $("#invmatrecipientsapply_grid").datagrid("resize"); },200);
			},
			onDblClickRow : function(rowIndex, rowData) {
			    var url = basePath+"inventory/invmatrecipients/invMatRecipientsForm.do?imrid=" + rowData.imrid;
			    var prefix = rowData.imrid;
				FW.addTabWithTree({
		        id : "editMatRecipientsFormA" + prefix,
		        url : url,
		        name : "物资发料",
		        tabOpt : {
		            closeable : true,
		            afterClose : "FW.deleteTab('$arg');FW.activeTabById('stock');FW.getFrame(FW.getCurrentTabId()).refCurPage();"
		        }
		    });
			}
		});
	}

	
	function searchList(){
		sdata = $("#quickSearchInput").val();
		delete(_itc_grids["invmatrecipientsapply_grid"]);
		var pager = $("#invmatrecipientsapply_grid").datagrid("getPager"); 
		pager.pagination("select",1);
	}
	
	$(document).ready(function() {
		initList();
		$("#quickSearchInput").keypress(function(e) {
			if(e.which == 13) {
				searchList();
		    }
		});
		$("#quickSearchInput").iInput("init",{
			"onClickIcon":function(){
				searchList();
			}
		});
		$("#btn_search").click(function(){
		    if(isSearchLineShow){
		    	isSearchLineShow=false;
		        $("#invmatrecipientsapply_grid").iDatagrid("endSearch");		        
		    }
		    else{
		    	isSearchLineShow=true;
		       	$("#invmatrecipientsapply_grid").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(arg){
		       		isSearchMode = true;
					return {"search":JSON.stringify(arg)};
				}});
		    }})
	});
	
</script>
</head>
  <body style="height: 100%;" class="bbox list-page">
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
		<!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar1" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm">
	        	<button type="button" id="btn_search" data-toggle="button" class="btn btn-default">查询</button>
	        </div>
	        <div class="input-group input-group-sm form-control-style" style="width: 160px; float: left; margin-left: 7px; margin-top: 1px; padding-right: 20px;">
		        <input type="text" id="quickSearchInput" icon="itcui_btn_mag" placeholder="请输入物资编号或名称" style="width: 95%; float: left; border-width: 0px; outline: none; height: 24px; display: block;">     
		    <span class="itcui_input_icon itcui_btn_mag" style="position: absolute; top: 6px; right: 6px;"></span></div>
	    </div>
	    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
	    <div id="pagination" class="toolbar-pager" bottompager="#bottomPager"></div>
	</div>
	
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div id="grid_wrap" style="width:100%">
	    <table id="invmatrecipientsapply_grid" pager="#pagination" class="eu-datagrid">
	    	<thead>
				<tr>
					<th data-options="field:'imrid',hidden:true"></th>
					<th data-options="field:'sheetno',width:135,fixed:true,sortable:true">编号</th>
					<th data-options="field:'sheetname',width:80,sortable:true">名称</th>
					<th data-options="field:'deliveryDate',width:135,fixed:true,sortable:true,formatter: function(value,row,index){return FW.long2date(value);}">发料时间</th>
					<th data-options="field:'status',width:135,fixed:true,sortable:true ,formatter:function(val){
							return FW.getEnumMap('INV_RECIPIENTSAPPLY_STATUS')[val]},editor:{
						'type':'combobox',
						'options' : {
							'data' : FW.parseEnumData('INV_RECIPIENTSAPPLY_STATUS',_enum)
						}
					}
							">状态</th>
				</tr>
			</thead>
	    </table>
	</div>
	<!-- 无数据 -->
	<div id="grid_error" style="display:none;width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">没有查询到相关数据</div>
			</div>
		</div>
	</div>
	<div id="bottomPager" style="width:100%">
	</div>

	
  </body>
</html>
