<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String imaCreateUserId = request.getParameter("imaCreateUserId") == null ? "":String.valueOf(request.getParameter("imaCreateUserId"));
	String imaCreateUserName = request.getParameter("imaCreateUserName") == null ? "":String.valueOf(request.getParameter("imaCreateUserName"));
	String imrdItemId = request.getParameter("imrdItemId") == null ? "":String.valueOf(request.getParameter("imrdItemId"));
	String deliveryTimeStampStr = request.getParameter("deliveryDate") == null ? "":String.valueOf(request.getParameter("deliveryDate"));
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:100%">
<head>
<title>固定资产登记使用页面</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
	<script type="text/javascript">
		var imaCreateUserId = '<%=imaCreateUserId%>';//领料申请人ID
		var imaCreateUserName = '<%=imaCreateUserName%>';//领料申请人名
		var imrdItemId = '<%=imrdItemId%>';//物资ID
 		var deliveryTimeStampStr = '<%=deliveryTimeStampStr%>';//发料日期时间戳
 		var deliveryTimeStamp;
 		if(/^\d+$/.test(deliveryTimeStampStr)){
 			deliveryTimeStamp = parseInt(deliveryTimeStampStr);
 		}
 		else{
 			deliveryTimeStamp = new Date();
 		}
		var deliveryDate = new Date(deliveryTimeStamp);//根据时间戳生成的时间对象
 		var _dialogEmmbed = true;
		$(document).ready(function() {
			var date = new Date();
			var borrowFormField=[
				{
					id : "borrowRecordId",
					title : "领用记录ID",
					linebreak:true,
					type : "hidden"
				},
				{
					id : "assetId",
					title : "资产ID",
					linebreak:true,
					type : "hidden"
				},
				{
					id : "assetCode",
					title : "资产编码",
					linebreak:true,
					rules : {
						maxChLength:20,
						required : true
					}
				},
				{
					id : "borrowUserId",
					title : "使用人ID",
					type : "hidden",
					value: imaCreateUserId,
					linebreak:true
				},
				{
					id : "borrowUserName",
					title : "资产使用人",
					linebreak:true,
					value: imaCreateUserName,
					rules : {
						required : true,
						maxChLength : 30
					}
				},				
				{
					id : "assetName",
					title : "资产名称",
					type:"label",
					linebreak:true,
					value:"请先输入资产编码"
				},
				{
					id : "borrowDate",
					title : "领用日期",
					type  : "date",
					dataType: "date",
					value:deliveryDate,//FW.long2time(value)
					linebreak:true,
					rules : {
						required : true
					}
				}
			];
			//弹窗表格里的条条框框
			$("#borrowForm").iForm("init",{
				"fields" : borrowFormField,
				"options":{
					validate:true,
					fixLabelWidth:true,
					labelFixWidth:"100px",
					xsWidth:12
				}
			});
			borrowFormField[1].render = getAssetHint("f_assetCode","f_assetId","f_assetName");
			borrowFormField[2].render = getUserHint("f_borrowUserName","f_borrowUserId");
		});
		
		//根据资产编码查询
		function getAssetHint(f_code,f_id,f_name){
			var opts = {
				"datasource":basePath + "asset/assetInfo/queryAssetForHintByCode.do",
				"getDataOnKeyPress":true,
				"clickEvent":function(id,name,assetObj){
					if(!assetObj.itemid){
						FW.error("该资产卡片没有关联主项目，请先进行修改");
						return;
					}
					if(assetObj.itemid != imrdItemId){
						FW.error("该资产卡片关联的物资和发料物资不一致，请重新输入");
						return;
					}
					var borrowData = {
						"assetCode" : assetObj.code,
						"assetId" : assetObj.id,
						"assetName": assetObj.name
					};
					$("#borrowForm").iForm("setVal",borrowData);
				},
				"showOn":"input",
				"highlight":true,
				"formatter" : function(id,name,assetObj){
					return assetObj.code + " / " + assetObj.name;
				}
			};
			$("#" + f_code).iHint("init",opts);
		}
		
		//根据工号查询
		function getUserHint(f_name,f_id){
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
	<form id="borrowForm" class="margin-form-title margin-form-foldable autoform"></form>
  </body>
</html>
