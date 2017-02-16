<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:100%">
<head>
<title>询价单列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_useLoadingMask = true</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<!-- 新建tab表单 -->
<script>
	$(document).ready(function() {
		var enquiryGrid = null;
		var firstInit = true;
		//高级查询
		$("#btn_advlocal").click(function(){
		    if($(this).hasClass("active")){
		        $("#table_enquiry").ITCUI_GridSearch("end");
		    }
		    else{
		       	$("#table_enquiry").ITCUI_GridSearch("init",{"remoteSearch":true,"onParseArgs":function(arg){
					return {"search":JSON.stringify(arg)};
				}});
		    }
		});
		
		//删除
		$("#btn_delete").click(function(){
			var rowData = $("#table_enquiry").datagrid("getSelections");
			if( rowData == null || rowData == "" ){
				FW.error("请选择要删除的记录 ");
				return;
			}else{
				var arr = [];
				for(var i=0;i<rowData.length;i++){
					arr[i]=rowData[i].enquiryid;
				}
				var ids = arr.join(",");
				$.ajax({
					type: "POST",
					url: basePath+"/purchase/purenquiry/deletePurEnquiry.do",
					data: {"rowId":ids},
					dataType: "json",
					success: function(data) {
						if(data.result == "success"){
						  FW.success("删除询价单成功");
						}else{
							FW.error("删除失败");
						}
					}
				});
			}
		});
		
		//初始化列表
		enquiryGrid = $("#table_enquiry").datagrid({
			fitColumns : true,
 			url : basePath+"/purchase/purenquiry/queryPurEnquiry.do",
			pagination : true,
			scrollbarSize : 0,
			pageSize : pageSize,
			pageList : [ 10, 15, 20, 30, 100 ],
			onRenderFinish : function() {
			 	if(firstInit){
	               	$("#table_enquiry").ITCUI_Pagination("create", "#pager", styleOpt);
	               	firstInit=false;
				}
				
			},
			onDblClickRow : function(rowIndex, rowData) {
				var url = basePath+"/purchase/purenquiry/purEnquiryForm.do?type=edit&rowid="+rowData.enquiryid;
		    	var prefix = rowData.enquiryid;
			    FW.addTabWithTree({
			        id : "editEnquiryForm" + prefix,
			        url : url,
			        name : "打开询价单",
			        tabOpt : {
			            closeable : true,
			            afterClose : "FW.deleteTab('$arg');FW.activeTabById('purchasing');"
			        }
			    });
			}
		});

		//分页处理
		$.post(basePath+"/purchase/purenquiry/queryPurEnquiry.do", {
			rows : pageSize,
			page : '1'
		}, function(data, textStatus) {}, "json");
		
	});
</script>
</head>
<body style="height: 100%;" class="bbox list-page">
	<div class="toolbar" style="width: 100%; height: 36px;margin-bottom:2px">
		<div class="btn-toolbar" role="toolbar">
			<div class="btn-group btn-group-sm">
				<button type="button" class="btn btn-default" data-toggle="button" id="btn_advlocal">查询</button>
				<button type="button" class="btn btn-default" id="btn_delete">删除</button>
			</div>
			<div id="pager" bottompager="#bottomPager"></div>
		</div>
	</div>
	<div style="clear:both"></div>
	<table id="table_enquiry" class="eu-datagrid">
		<thead>
			<tr>
				<th data-options="field:'enquiryid',width:100,checkbox:'true',fixed:true">询价单ID</th>
				<th data-options="field:'enquiryname',width:300">询价单名称</th>
				<th data-options="field:'contactname',width:200">联系人</th>
				<th data-options="field:'contactphone',width:200,fixed:true">联系电话</th>
				<th data-options="field:'expirydate',width:200,fixed:true">报价截止日期</th>
				<th data-options="field:'status',width:200,fixed:true">状态</th>
			</tr>
		</thead>
		<tbody></tbody>
	</table>
	<div id="bottomPager"></div>
</body>
</html>