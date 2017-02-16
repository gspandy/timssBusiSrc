<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String dataStr = request.getParameter("dataStr");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:100%">
  <head>
    <base href="<%=basePath%>">
    <title>资产化申请信息登记窗口</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
	<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
	<script type="text/javascript">
		var dataStr = '<%=dataStr%>';
		var data = new Object();
		var _dialogEmmbed = true;
		data = JSON.parse(dataStr);
		$(document).ready(function() {
			var formField=[
			{
				id : "imtdid",
				title : "imtdid",
				type : "hidden",
				value : data.imtdid
			},
			{	
				id : "itemid",
				title : "itemid",
				type : "hidden",
				value : data.itemid
			},
			{	
				id : "itemcode",
				title : "关联主项目",
				type : "label"  ,
				value : data.itemcode
			},
			{	
				id : "itemname",
				title : "主项目名称",
				type : "label",
				value : data.itemname 
			},
			{	
				id : "itemName",
				title : "资产名称",
				type : "text",
				value : data.itemname, 
				wrapXsWidth : 6,
				wrapMdWidth : 6,
				rules : {
					required : true,
					maxChLength : 330
				}
			},
			{	
				id : "financialCode",
				title : "财务资产号",
				type : "text"
			},
			{	
				id : "logo",
				title : "品牌",
				type : "text"
			},
			{	
				id : "equipmentId",
				title : "设备序列号",
				type : "text",
				rules : {
					required : true
				}
			},
			{	
				id : "cusmodel",
				title : "型号规格",
				type : "text",
				value : data.cusmodel,
				linebreak:true,
				wrapXsWidth :12,
				rules : {
					required : true,
					maxlength : 550
				}
			},
			{	
				id : "companyName",
				title : "供应商",
				type : "text",
				value : data.companyName,
				rules : {
					required : true,
					maxChLength : 330
				}
			},
			{	
				id : "companyTel",
				title : "供应商联系方式",
				value : data.companyTel,
				type : "text"
			},
			{	
				id : "purchaseDate",
				title : "采购日期",
				value : data.purchaseDate,
				type : "date"
			},
			{
				id : "memo",
				title : "备注",
				type  : "textarea",
				//value : "此处请填写领料人姓名，便于记录",
				linebreak:true,
				wrapXsWidth :12,
				wrapMdWidth :12,
				height:150,
				rules : {maxChLength : 100}
			}
			];
			//弹窗表格里的条条框框
			$("#assetForm").iForm("init", {
				"fields" : formField,
				"options":{
					validate:true,
					fixLabelWidth:true,
					labelFixWidth:"100px",
					xsWidth:6
				}
			});
		});
	</script>
  </head>
  
  <body>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<input type="hidden" id="oldSysConf"/>
	<form id="assetForm"  class="margin-form-title margin-form-foldable"></form>
  </body>
</html>
