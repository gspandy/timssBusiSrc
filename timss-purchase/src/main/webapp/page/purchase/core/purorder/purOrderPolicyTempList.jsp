<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:99%">
<head>
<title>标准条款查询列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
	var isSearchMode = false;
	function openPolicyTempForm(type,policyId){
		var url = basePath+ "purchase/purorder/purOrderPolicyTempForm.do?type="+type+"&policyId="+policyId;
	    var prefix = new Date().getTime() + "" + Math.floor(Math.abs(Math.random()*100));
	    FW.addTabWithTree({
	        id : "newPolicyTempForm" + prefix,
	        url : url,
	        name : "合同标准条款",
	        tabOpt : {
	            closeable : true,
	            afterClose : "FW.deleteTab('$arg');FW.activeTabById('purchasing');FW.getFrame(FW.getCurrentTabId()).refCurPage();"
	        }
	    });
	}
	//刷新当前页面
	function refCurPage(){
	 	isSearchMode = false;
	 	$("#btn_advlocal").removeClass("active");
		$("#table_orderPolicyTempList").iDatagrid("endSearch");
	 	$("#table_orderPolicyTempList").datagrid("reload");
	 	/*
	 	setTimeout(function(){ 
	 		$("#table_orderPolicyTempList").datagrid("resize"); 
	 	},200);*/
	} 
	//初始化item信息
	$(document).ready(function() {
		//新建
		Priv.map("privMapping.policyTemp_new","policyTemp_new");
		//删除
		Priv.map("privMapping.policyTemp_del","policyTemp_del");
		//详情
		Priv.map("privMapping.policyTemp_dtl","policyTemp_dtl");
		Priv.apply();
		FW.fixToolbar("#toolbar1");
		//新建按钮事件绑定
		$("#btn_new,.btn_new").click(function(){
			openPolicyTempForm("new",null);
		});
		
       $("#btn_advlocal").click(function(){
		    if($(this).hasClass("active")){
		    	isSearchMode = false;
		    	$("#btn_advlocal").removeClass("active");
		        $("#table_orderPolicyTempList").iDatagrid("endSearch");
		    }
		    else{
		    	isSearchMode = true;
		    	$("#btn_advlocal").addClass("active");
		       	$("#table_orderPolicyTempList").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(arg){
					return {"search":JSON.stringify(arg)};
				}});
		    }
		});
		
		//初始化列表
		$("#table_orderPolicyTempList").iDatagrid( "init",{
        	pageSize:pageSize,//默认每页显示的数目 只能从服务器取得
	        url: basePath+"purchase/purorder/purPolicyTempListData.do",
	        singleSelect:true,
	        onLoadSuccess : function(data){
	        	var total = data.rows.length; 
	            //远程无数据需要隐藏整个表格和对应的工具条，然后在无数据信息中指引用户新建
	            if(total == 0){
	                if(!isSearchMode){
	                	$("#grid1_wrap,#toolbar_wrap").hide();
	                	$("#grid_error").show();
	                	$("#noResult").hide();
	                }else{
	                	$("#noResult").show();
	                	$("#grid_error").hide();
	                }
	            }else{
	            	$("#grid1_wrap,#toolbar_wrap").show();
	                $("#grid_error").hide();
	                $("#noResult").hide();
	            }
	            setTimeout(function(){ 
	            	$("#table_orderPolicyTempList").datagrid("resize"); 
	            },200);
	        },
	        onDblClickRow:function(rowIndex,rowData){
	        	if(Priv.vPriv["policyTemp_dtl"]){
	        		openPolicyTempForm("modify",rowData.policyId);
	        	}
	        }
	    });
	});
</script>
</head>
<body style="height: 100%;" class="bbox">
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
		<div id="toolbar1" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-success priv" id="btn_new" privilege="policyTemp_new">新建</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default" id="btn_advlocal">查询</button>
	        </div>
	    </div>
	    <div id="pagination_1" class="toolbar-pager" bottompager="#bottomPager_1"></div>
	</div>
	
	<div style="clear:both"></div>
	<div id="grid1_wrap" style="width:100%">
	    <table id="table_orderPolicyTempList" pager="#pagination_1" class="eu-datagrid">
			<thead>
				<tr>
					<th data-options="field:'policyId',hidden : true"></th>
					<th data-options="field:'sort',width:50,fixed:true">排序</th>
					<th data-options="field:'policyContent',width:1">条款内容</th>
				</tr>
			</thead>
		</table>
	</div>
	<div id="noResult" style="display:none;width:100%;height:62%;margin:10px;">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">没有找到符合条件的结果</div>
			</div>
		</div>
	</div>
	<!-- 无数据 -->
	<div id="grid_error" style="display:none;width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">没有查询到相关数据</div>
			    <div class="btn-group btn-group-sm margin-element">
		                 <button type="button" class="btn btn-success btn_new">新建</button>
			    </div>
			</div>
		</div>
	</div>
	<div id="bottomPager_1" style="width:100%"></div>
</body>
</html>