<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
  	String filter = request.getParameter("filter");
  	String filterType = request.getParameter("filterType");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="/page/sysconf/auth_include.jsp" flush="false" />
<link rel="stylesheet" type="text/css"
	href="${basePath}${resBase}css/public_background.css" media="all" />



<title>用户管理</title>
<style>

.layout-panel-west{
	border-right-color: rgb(198, 198, 198);
	border-right-style: solid;
	border-right-width: 1px;
}
</style>
<script>
	var tbUser;
	var firstInit = true;
	var filterType = "<%=filterType%>";
	var filter = "<%=filter%>";	
	
	function reloadDatagrid(){	
		var columns = [
			{field:'name',width:90,title:'姓名',fixed:true},
	    	{field:'uid',width:70,title:'工号',fixed:true},	    	
	    	{field:'org',width:110,title:'部门',fixed:true},
	    	{field:'ruzhi',width:90,title:'入职日期',formatter:function(val){
	    		if(val){
	    			return FW.long2date(val);
	    		}
	    		else{
	    			return "";
	    		}
	    	}}/*,
	    	{field:'lizhi',width:90,title:'离职日期',formatter:function(val){
	    		if(val){
	    			return FW.long2date(val);
	    		}
	    		else{
	    			return "";
	    		}
	    	},hidden:true}*/
	    ];
	    tbUser = $("#table_user").datagrid({
			fitColumns:true,
	        url:"${basePath}attuser?method=getusers",
	        queryParams:{
	        	"filter":filter,
	        	"filterType":filterType,
	        	"onlyActive":true
	        },
	        columns:[columns],
	        singleSelect:true,
	        pagination :true,
	        scrollbarSize:0,
	        pageSize :rowLen,
	        pageList :[10,15,20,30,100],
	        onRenderFinish : function(){
	            if(firstInit){
	               	$("#table_user").ITCUI_Pagination("create", "#pager",styleOpt);
	               	firstInit=false;
				}
			},
			onDblClickRow : function(rowIndex, rowData) {
				window.location.href = "${basePath}attuser?method=getuser&mode=edit&uid=" + rowData.uid;
			}
		});
	}
	
	function search(){
		filterType = 'person';
		filter = encodeURIComponent($("#search").val());
		firstInit = true;
		reloadDatagrid();
	}
	
	function refresh(){
		filterType = "";
		filter = "";
		$("#search").val("");
		firstInit = true;
		reloadDatagrid();
	}
		
	$(document).ready(function(){
		firstInit = true;
		reloadDatagrid();
		$(window).resize(function(){
			if(tbUser){
				tbUser.datagrid("resize");
			}
		});
		$("#search").ITCUI_Input();
		$("#search").keypress(function(e) {
		    if(e.which == 13) {
		        search();
		    }
		});
		$("#search").next(".itcui_btn_mag").click(function(){
			search();
		});		
	});
	

	function createUser(){
		window.location.href = "${basePath}user?method=getuser&mode=create";
	}
</script>
</head>
<body>
	<div class="toolbar-with-pager bbox">
		<div class="btn-toolbar" role="toolbar">
			<div class="btn-group btn-group-sm">
				<button type="button" class="btn btn-default" onclick="refresh()">刷新</button>
			</div>
			<div class="input-group input-group-sm" style="width:150px;float:left;margin-left:7px;margin-top:1px">
		        <input type="text" id="search" icon="itcui_btn_mag" placeholder="请输入姓名或账号" style="width:150px"/>     
		    </div>
			<div id="pager" style="float:right;width:200px" bottompager="#bottomPager"></div>
		</div> 			
	</div>
	<div style="clear:both"></div>
	<table id="table_user" class="eu-datagrid">
		
	</table>
	<div id="bottomPager" style="width:100%"></div>
</body>
</html>