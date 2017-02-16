<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
	<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
	<script type="text/javascript">
		$(document).ready(function(){
			var assetId = "AST-TEMP-00000001";
			var astApplyId = '${astApplyId}';
			$.ajax({
				type:"POST",
				data:{
					astApplyId:astApplyId
					},
				dataType:"json",
				url:basePath+'inventory/invmatapplydetail/getAssetData.do',
				success:function(data){
					var assetDataStr=data.assetDataStr;
					var assetData=JSON.parse(assetDataStr);
					FW.set(assetId,{
						blankBean:{
							locationName:"未分类资产",  
							assetName:assetData.itemName, 
							imadId:assetData.imadId, 
							poId:assetData.poId,
							financialCode: assetData.financialCode,
							flowNo: assetData.flowNo,
							//assetCode:assetData.assetcode,
							imtdId:assetData.imtdId,
							itemId:assetData.itemId,
							astApplyId:astApplyId,
							itemCode:assetData.itemCode,
							itemName:assetData.iitemName
						},
						DFData:{
							"型号":assetData.cusmodel, 
							"供应商":assetData.companyName,
							"供应商联系方式":assetData.companyTel, 
							"采购日期": assetData.purchaseDate,
							"备注":assetData.memo,
							"财务资产卡片号":assetData.financialCode,
							"品牌":assetData.logo,
							"设备序列号":assetData.equipmentId
						}
					});
			    	FW.navigate( basePath+ "asset/assetInfo/createAsset.do?" + $.param({"parentName":"未分类资产","paramsMap":assetId})
			    );
				}
			})
			
			//var invMatAssetApply = JsonHelper.fromJsonStringToBean(invMatAssetApplyStr, InvMatAssetApply.class);
				
		});
	</script>
  </head>
  
  <body>
  </body>
</html>
