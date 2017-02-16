<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yudean.itc.util.FileUploadUtil"%>
<%@page import="com.yudean.itc.util.Constant"%>
<%@page import="com.yudean.itc.dto.sec.SecureUser"%>
<%
	String sessId = request.getSession().getId();
	SecureUser operator = (SecureUser)session.getAttribute(Constant.secUser);
	String valKey = FileUploadUtil.getValidateStr(operator, FileUploadUtil.DEL_OWNED);
	String site=operator.getCurrentSite().toLowerCase();
%>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:99%">
<head>
	<title>资产详细信息</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<script type="text/javascript">
		_useLoadingMask=true;
	</script>
	<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
	<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
	<script type="text/javascript" src="${basePath}js/asset/asset.js?ver=${iVersion}"></script>
	<script type="text/javascript" src="${basePath}js/asset/borrowRecord.js?ver=${iVersion}"></script>
	<script type="text/javascript" src="${basePath}js/inventory/common/inventory.js?ver=${iVersion}"></script>
	<script type="text/javascript" src="${basePath}js/asset/assetPriv.js?ver=${iVersion}"></script>
	<script type="text/javascript" src="${basePath}js/asset/assetLayout.js?ver=${iVersion}"></script>
	<script type="text/javascript" src="${basePath}js/asset/initAssetTree.js?ver=${iVersion}"></script>
	
	<script type="text/javascript">
		var uploadIds = "";
		var pageCode = "<%=site%>_assetDetail";
		var formId = "form_baseinfo";
		var astApplyId;
		var flowNo;
		var imtdId;
		var itemId;
		//判断是否来自待办
		var isFrom=false;
		
		$(document).ready(function() {
			respond.update();
			/* Asset.objs.intForm.push({title : '字表ID',id: 'imtdId',type:'hidden'});
			Asset.objs.intForm.push({title : '货物ID',id: 'itemId',type:'hidden'}); */ 
			$.extend(Asset.objs,{
				bean:{assetId:"${assetId}"},
				mode:"${mode}",//模式有浏览、新建、编辑三种：view/create/edit
				sessId:"<%=sessId%>",
				valKey:"<%=valKey%>",
				pageTitle:"资产卡片"
			});
			
			$.each(Asset.objs.intForm,function(i,val){
				if(val.id=="assetCode"){
					Asset.objs.intForm[i].rules.required=true;
				}
			})
			 
			$.extend(Asset.site,{
				init:function(){//站点初始化时，在其他初始化操作完成之后，页面状态变更之前调用，进行站点自定义初始化
					AssetLayout.init();
					 if(Asset.objs.mode=='create'&&Asset.objs.initParams&&Asset.objs.initParams.blankBean){
					 isFrom=true;
					 astApplyId=Asset.objs.initParams.blankBean.astApplyId;
					 flowNo=Asset.objs.initParams.blankBean.flowNo;
					 imtdId=Asset.objs.initParams.blankBean.imtdId;
					 itemId=Asset.objs.initParams.blankBean.itemId;
					 if(astApplyId!=null&&astApplyId!=""){
						$("#btnCancle").show();
					}
					} 
					//设置领用和归还按钮disabled属性
					initDataGrid();  //初始化表格	
				},
				changeViewMode:function(){//切换为只读的操作
					AssetLayout.changeState("read");
					changeBorrowShow(true);
					$("#btnCancle").hide();
				},
				changeCreateMode:function(){//切换为新建的操作
					if(Asset.objs["src"] && Asset.objs["src"]=='copy'){
						var tmp=$.extend({},AssetLayout.objs["layout"]);
						AssetLayout.loadData([]);
						$.each(tmp,function(i,val){
							$('#assetLayout_table').datagrid('appendRow',{type:val.type,value:val.value});
						});
 						
					}else{
						AssetLayout.loadData([]);
					}
					AssetLayout.changeState("edit");
					$("#form_baseinfo").iForm('show',["allowBorrow"]);
					changeBorrowShow(false);
				},
				changeEditMode:function(){//切换为编辑的操作
					AssetLayout.changeState("edit");
					$("#form_baseinfo").iForm('endEdit',["itemName"]);
					$("#form_baseinfo").iForm('beginEdit',["allowBorrow"]);
				},
				loadData:function(){//刷新数据的操作
					AssetLayout.loadData();
					$("#borrowRecordListTable").datagrid("reload");
				},
				createAsset:function(){//执行新建的操作
					AssetLayout.submitChange();
					if(isFrom){
						if(astApplyId!=null&&astApplyId!=""){
							//新建的回调函数中，删除待办
							$.ajax({
									type : "POST",
									url : basePath +"asset/assetInfo/invalidAssetApply.do",
									data : {"astApplyId":astApplyId, "flowNo":flowNo, "sign":"N"},
									dataType : "json"
							});
						}
						//isFrom=false;
					}
				},
				updateAsset:function(){//执行更新的操作
					AssetLayout.submitChange();
				},
				delAsset:function(){//执行删除的操作
					
				},
				valid:function(){//执行校验
					return AssetLayout.validDatagrid();
				}
			});
			Asset.init("${param.paramsMap}");
		});

		function goBack() {
			if(Asset.objs["bean"].assetId==Asset.objs["assetTreeRootNode"].id){//根节点则退回列表
				window.parent.document.getElementById("assetTree").contentWindow.jumpTo(
					basePath+"asset/location/locationList.do?location="+Asset.objs["bean"].assetId
				);
			}else if(isFrom){
				FW.deleteTabById(FW.getCurrentTabId());
			}else{
				Asset.showDetail(null,true);
			}
			$("#btn-create").button('reset');
		}
		function invalidAsset(){
			$.ajax({
				type : "POST",
				url : basePath +"asset/assetInfo/invalidAssetApply.do",
				data : {"astApplyId":astApplyId, "flowNo":flowNo, "sign":"Y"},
				dataType : "json",
				success: function(data){
					if(data.result=="success"){
						FW.success("取消成功");
						FW.deleteTabById(FW.getCurrentTabId());
					}else{
						FW.error("取消失败");
					}
					
				}
			});
		}
	</script>
	<style type="text/css">
		.btn-garbage{
			cursor:pointer;
		}
	</style>
</head>

<body>
	<div class="toolbar-with-pager bbox">
		<div class="btn-toolbar" role="toolbar">
			<div class="btn-group btn-group-sm" id="btnBack">
				<button class="btn-default btn" onclick="goBack()">关闭</button>
			</div>
			<div class="btn-group btn-group-sm" id="btnNew" style="margin-left:0">
				<button class="btn-default btn priv" privilege="VIRTUAL-NEW" onclick="Asset.changeMode('create','child')">新建子资产</button>
			</div>
			<div class="btn-group btn-group-sm" id="btnCopy">
				<button class="btn-default btn priv" privilege="VIRTUAL-NEW" onclick="Asset.changeMode('create','copy')">复制</button>
			</div>
			<div class="btn-group btn-group-sm" id="btnEdit">
				<button class="btn-default btn priv" privilege="VIRTUAL-EDIT" onclick="Asset.changeMode('edit')">编辑</button>
			</div>
			<div class="btn-group btn-group-sm" id="btnSave" style="display:none">
				<button class="btn-success btn" onclick="Asset.updateAsset()">保存</button>
			</div>
			<div class="btn-group btn-group-sm" id="btnCreate" style="display:none">
				<button type="button" class="btn-success btn" id="btn-create" onclick="Asset.createAsset()">保存</button>
			</div>
			<!-- 1.按钮控制 2.id 3.函数 -->
			<div class="btn-group btn-group-sm" id="btnCancle" style="display:none">
				<button class="btn-default btn" onclick="invalidAsset()" >取消申请</button>
			</div>
			<div class="btn-group btn-group-sm" id="btnDel">
				<button class="btn-default btn priv" privilege="VIRTUAL-DELETE" onclick="Asset.delAsset()">删除</button>
			</div>
			<div class="btn-group btn-group-sm" id="borrowDiv">
<!-- 				<button class="btn-default btn priv" privilege="VIRTUAL-BORROW" onclick="openBorrowWin('borrow')" id="borrowRecord">领用</button>
 -->				<button class="btn-default btn priv" privilege="VIRTUAL-RETURN" onclick="openBorrowWin('return')" id="returnRecord">归还</button>
			</div>
			<div class="btn-group btn-group-sm" id="btnConf">
				<button class="btn-default btn priv" privilege="VIRTUAL-CONFIG" onclick="FW.openDDBox()">配置表单</button>
			</div>
		</div>
	</div>
	<!-- <div style="clear:both"></div> -->

	<div class="inner-title" id="pageTitle">
		此处写页标题
	</div>
	
	<form id="form_baseinfo" class="margin-form-title margin-form-foldable">
	</form>
	
	<div id="assetSpare_info" grouptitle="备件信息">
		<div id="assetSpareGrid" class="margin-title-table">
			<table id="assetSpare_table" class="eu-datagrid">
			</table>
		</div>
		<div id="spareBtn" class="btn-toolbar margin-foldable-button" role="toolbar" style='display:none;'>
			<div class="btn-group btn-group-xs">
				<button type="button" class="btn btn-success" id="btnAddSpare" onclick="Asset.addSpare()">添加备件</button>
			</div>
		</div>
	</div>
	<div id="purchase_list" grouptitle="采购信息">
		<div id="purchaseGrid" class="margin-title-table">
			<table id="purchase_grid" class="eu-datagrid"></table>
		</div>
	</div>	
	<div class="margin-group"></div>
	
	<div id="specs_info" grouptitle="技术规范" style='overflow: hidden;'>
		<form id="form_specs" class="margin-form-title margin-form-foldable">
		</form>
	</div>
	<div class="margin-group"></div>
	
	<div id="assetLayout_info" grouptitle="设备配置清单">		
		<div id="assetLayoutGrid" class="margin-title-table">
			<form id="assetLayout_form">
				<table id="assetLayout_table" class="eu-datagrid">
				</table>
			</form>
		</div>
		<div id="layoutBtn" class="btn-toolbar margin-foldable-button" role="toolbar" style='display:none;'>
			<div class="btn-group btn-group-xs">
				<button type="button" class="btn btn-success" id="btnAddLayout" onclick="AssetLayout.addLayout()">添加配置</button>
			</div>
			<div class="btn-group btn-group-xs">
				<button type="button" class="btn btn-default" id="btnImportTemplate" onclick="AssetLayout.importTemplate()">导入模板</button>
			</div>
		</div>
	</div>
	<div class="margin-group"></div>
	
	<div id="workOrder_info" grouptitle="工单" style="overflow: hidden;">		
		<div id="workOrderGrid" class="margin-title-table">
			<table id="workOrder_table" class="eu-datagrid">
			</table>
		</div>
	</div>
	<div class="margin-group"></div>
	
	<div id="maintainPlan_info" grouptitle="维护计划" style="">
		<div id="maintainPlanGrid" class="margin-title-table">
			<table id="maintainPlan_table" class="eu-datagrid">
			</table>
		</div>
	</div>
	<div class="margin-group"></div>
	
	<div id="woQx_info" grouptitle="缺陷记录" style="">
		<div id="woQxGrid" class="margin-title-table">
			<table id="woQx_table" class="eu-datagrid">
			</table>
		</div>
	</div>
	<div class="margin-group"></div>
	
	<div id="borrowRecordListDiv" grouptitle="领用记录" style="overflow: hidden;">
		<div class="margin-title-table">
			<table id="borrowRecordListTable" class="eu-datagrid">
			</table>
		</div>
	</div>
	<div class="margin-group"></div>
</body>
</html>
