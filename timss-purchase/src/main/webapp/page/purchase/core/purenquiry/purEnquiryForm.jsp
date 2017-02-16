<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	//获取List页面传送过来的参数，由于没有使用el表达式，所以只能通过java代码获取
	String type = request.getParameter("type")==null?"":String.valueOf(request.getParameter("type"));
	String rowid = request.getParameter("rowid")==null?"":String.valueOf(request.getParameter("rowid"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:100%">
<head>
<title>询价单列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_useLoadingMask = true</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
	
	//新建时field
	var new_field = [
			    {title : "询价单名称", id : "enquiryname", rules : {required:true}},
			    {title : "采购申请单", id : "purchaseorderno", rules : {required:true}},
			    {title : "报价截止日期", id : "expirydate",type : "datetime",rules : {required:true}},
			    {title : "联系人", id : "contactname",rules : {required:true}},
			    {title : "交货地点", id : "deliverplace"},
			    {title : "联系电话", id : "contactphone",rules : {required:true}},
			    {title : "描述", id : "discription"},
			];
			
	//修改时field		
	var edit_field = [
		    {title : "询价单名称", id : "enquiryname",type:"label"},
		    {title : "采购申请单", id : "purchaseorderno",type:"label"},
		    {title : "报价截止日期", id : "expirydate",type:"label"},
		    {title : "联系人", id : "contactname",type:"label"},
		    {title : "交货地点", id : "deliverplace",type:"label"},
		    {title : "联系电话", id : "contactphone",type:"label"},
		    {title : "创建人", id : "createuser",type:"label"},
		    {title : "创建日期", id : "createdate",type:"label"},
		    {title : "修改人", id : "modifyuser",type:"label"},
		    {title : "修改日期", id : "modifydate",type:"label"},
		    {title : "描述", id : "discription",type:"label"},
		];
	
	//提交按钮
	function commitBtn(type){
		if(!$("#autoform").valid()){
			return ;
		}
		var formData = getFormData("autoform");
		var url = basePath+"/purchase/purenquiry/commitEnquiry.do";
		$.ajax({
			url : url,
			type : "POST",
			data: {"formData":encodeURI(formData),"type":type},
			dataType : "JSON",
			success : function(data) {
				if( data.result == "success" ){
					FW.success( "操作成功 ");
				}else{
					FW.error( "操作失败 ");
				}
			}
		});
	}
	
	$(document).ready(function() {
		var enquiryItemGrid = null;
		//判断用户是新建操作还是查看操作
		if('edit' == '<%=type%>'){
			$("#autoform").ITC_Form({validate:true,fixLabelWidth:true},edit_field);
			
			//加载用户表单数据
			$.ajax({
				type : "POST",
				url: basePath+"/purchase/purenquiry/queryPurEnquiryDetail.do",
				data: {"rowId":'<%=rowid%>'},
				dataType : "json",
				success : function(data) {
					var loaddata = {
						"enquiryname" : data.enquiryname,
						"purchaseorderno" : data.purchaseorderno,
						"expirydate" : data.expirydate,
						"contactname" : data.contactname,
						"deliverplace" : data.deliverplace,
						"contactphone" : data.contactphone,
						"createuser" : data.createuser,
						"createdate": data.createdate,
						"modifyuser": data.modifyuser,
						"modifydate": data.modifydate,
						"discription": data.discription
					};
					$("#autoform").ITC_Form("loaddata",loaddata);
				}
			});
			
			$("#btn-save").hide();
			$("#btn-submit").hide();
			
		}else{
			$("#autoform").ITC_Form({validate:true,fixLabelWidth:true},new_field);
			
			$("#btn-save").click(function(){
				commitBtn('save');
			});
			
			$("#btn-submit").click(function(){
				commitBtn('submit');
			});
		}
		
		//关闭按钮	
		$("#btn-close").click(function(){
			Notice.confirm("关闭？|确定关闭当前页面？关闭后未保存信息将丢失。",function(){
				FW.deleteTabById(FW.getCurrentTabId());
			},null,"info");	
		});
		
		
		//初始化列表
		$("#enquiry_item").datagrid({
			fitColumns : true,
			scrollbarSize : 0,
 			url : basePath+"/purchase/purenquiry/queryPurEnquiryItems.do",
			queryParams: {'id': '<%=rowid%>'},
		});
		
		//折叠区标题
		$("#foldable_area").ITCUI_Foldable();
	});
</script>
</head>
<body>
	<div class="btn-toolbar" role="toolbar">
	    <div class="btn-group btn-group-sm">
	        <button type="button" class="btn btn-default" id="btn-close">关闭</button>
	    </div>
	    <div class="btn-group btn-group-sm">
	        <button type="button" class="btn btn-default" id="btn-save">暂存</button>
	        <button type="button" class="btn btn-default" id="btn-submit">提交</button>
	    </div>
	</div>
	
	<form id="autoform" class="autoform"></form>
	
	<div id="foldable_area" grouptitle="物资条目">
		<table id="enquiry_item" class="eu-datagrid">
			<thead>
				<tr>
					<th data-options="field:'itemid',width:100,fixed:true">物资编号</th>
					<th data-options="field:'itemname',width:300">物资名称</th>
					<th data-options="field:'classname',width:200,fixed:true">物资类型</th>
					<th data-options="field:'itemnum',width:200,fixed:true">采购数量</th>
					<th data-options="field:'orderunitname',width:200,fixed:true">单位</th>
				</tr>
			</thead>
			<tbody></tbody>
		</table>
	</div>
</body>
</html>