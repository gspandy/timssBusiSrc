<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="org.apache.commons.lang3.StringEscapeUtils"%>
<%
	String assetId = request.getParameter("assetId");
	String imtdId = request.getParameter("imtdId") == null ? "":String.valueOf(request.getParameter("imtdId"));
	String itemcode = request.getParameter("itemcode") == null ? "":String.valueOf(request.getParameter("itemcode"));
	String itemname = request.getParameter("itemname") == null ? "":String.valueOf(request.getParameter("itemname"));
	itemname = StringEscapeUtils.escapeEcmaScript(itemname);  
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:100%">
<head>
	<link rel="shortcut icon" href="${basePath}favicon.ico" type="image/x-icon" />
	<title>归还领用信息</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
	<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
	<script>
		var assetId = "<%=assetId%>";
		var imtdId = "<%=imtdId%>";
		var itemcode = "<%=itemcode%>";
		var itemname = "<%=itemname%>";
		var date = new Date();
		$(document).ready(function() {
			var borrowFormField=[
				{
					id : "borrowRecordId",
					title : "ID",
					type : "hidden"
				},
				{
					id : "borrowDate",
					title : "领用日期",
					type  : "date",
					dataType: "date",
					wrapXsWidth : 6,
					wrapMdWidth : 6,
					linebreak:true,
					value:date.getTime(),
					rules : {
						required : true,
						maxChLength : 66
					}
				},
				{
					id : "borrowUserId",
					title : "领用人ID",
					type : "hidden"
				},
				{
					id : "borrowUserName",
					title : "资产使用人",
					wrapXsWidth : 6,
					wrapMdWidth : 6,
					linebreak:true,
					rules : {
						required : true,
						maxChLength : 330
					}
				},
				{
					id : "returnDate",
					title : "归还日期",
					type  : "date",
					dataType: "date",
					wrapXsWidth : 6,
					wrapMdWidth : 6,
					linebreak:true,
					value:date.getTime(),
					rules : {
						greaterEqualThan:"#f_borrowDate",
						required : true,
						maxChLength : 66
					}
				},
				{
					id : "returnUserId",
					title : "归还人ID",
					type : "hidden"
				},
				{
					id : "returnUserName",
					title : "归还人",
					wrapXsWidth : 6,
					wrapMdWidth : 6,
					linebreak:true,
					rules : {
						required : true,
						maxChLength : 330
					}
				},
				{
					id : "memo",
					title : "备注",
					type  : "textarea",
					linebreak:true,
					wrapXsWidth :12,
					wrapMdWidth :12,
					height:120,
					rules : {
						maxChLength : 500
					}
				},
				{
					id : "warn",
					title : "提醒",
					wrapXsWidth :12,
					type  : "label"
				}
			];
			
			//弹窗表格里的条条框框
			$("#borrowForm").iForm("init", {"fields" : borrowFormField,"options":{
				validate:true,
				fixLabelWidth:true,
				labelFixWidth:"80px",
				xsWidth:6
			}});
			borrowFormField[2].render = getUserHint("f_borrowUserId","f_borrowUserName");
			borrowFormField[4].render = getUserHint("f_returnUserId","f_returnUserName");
			//归还模式下，发起异步请求
			$.ajax({
				type : "POST",
				async: false,
				url: basePath+"astBorrowRecord/getInfo.do",
				//将assetId插入
				data: {"assetId":assetId},
				dataType : "json",
				success : function(result) {
					var borrowData = {
						"borrowRecordId":result.borrowRecordId,
						"borrowDate" : result.borrowDate, 
						"borrowUserName" : result.borrowUserDeptName,//FW.long2date(data.dhdate),(逗号记得去掉)
						"memo":result.memo
					};
					//若borrowDate为空，则显示借用相关的条框，隐藏归还的条框
					if(result.borrowDate==null || result.borrowDate==""){
						$("#borrowForm").iForm("hide","returnDate");
						$("#borrowForm").iForm("hide","returnUserName");
						$("#borrowForm").iForm("beginEdit",["borrowDate","borrowUserName"]);
					}
					else{
						$("#borrowForm").iForm("show","returnDate");
						$("#borrowForm").iForm("show","returnUserName");
						$("#borrowForm").iForm("setVal",borrowData);
						$("#borrowForm").iForm("endEdit",["borrowDate","borrowUserName"]);
						if(!!imtdId){
							$("#borrowForm").iForm("show","warn");
							var warnInfo = "归还后，【" + itemcode + "】" + itemname + "的库存数量将会加1，请仓管确认。";
    						$("#borrowForm").iForm("setVal",{"warn":warnInfo});
						}
						else{
							$("#borrowForm").iForm("hide","warn");
						}						
					}
				}
			});
		});


		//直接输入人员姓名查询
		function getUserHint(f_id,f_name){
			//放大镜图标属于基础资源，可以直接引用
			var opts = {
				"datasource":basePath + "user?method=hint",
				"getDataOnKeyPress":true,
				"clickEvent":function(id,name){
					$("#" + f_id).val(id);
					$("#" + f_name).val(name);
				},
				"showOn":"input",
				"highlight":true,
				"formatter" : function(id,name){
					return name + " / " + id.split("_")[0];
				}
			};
			$("#" + f_name).iHint("init",opts);
		}
	</script>
</head>
<body>
<!--这里要清掉分页器的右浮动效果-->
<div style="clear:both"></div>
<input type="hidden" id="oldSysConf"/>
<form id="borrowForm"  class="margin-form-title margin-form-foldable"></form>

</body>
</html>