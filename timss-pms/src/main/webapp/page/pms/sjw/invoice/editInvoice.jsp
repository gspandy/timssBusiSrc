<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yudean.itc.util.FileUploadUtil"%>
<%@page import="com.yudean.itc.dto.sec.SecureUser"%>
<%@page import="com.yudean.itc.util.Constant"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<%
 	String path = request.getContextPath();
 	
 	SecureUser operator = (SecureUser)session.getAttribute(Constant.secUser);
 	String valKey = FileUploadUtil.getValidateStr(operator, FileUploadUtil.DEL_OWNED);
 	String sessionid=session.getId();
%>
<head>
<title>发票信息</title>
<script>_useLoadingMask = true;</script><script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
var session="<%=sessionid%>";
var valKey = "<%=valKey%>";
</script>
<script src="${basePath}js/pms/common/common.js?ver=${iVersion}"></script>
<script src="${basePath}js/workflow/workflow.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/common/workflow.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/sjw/invoice/invoice.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/sjw/invoice/editInvoice.js?ver=${iVersion}"></script>

<script>
	
    var id=getUrlParam("id");
    
    
	$(document).ready(function() {
		var form=$("#form1");
		$.post(basePath+'pms/invoice/queryInvoiceById.do',{id:id},function(data){
			if(data && data.flag=='success'){
				var opt={
					form:$("#form1"),
					data:data,
					formFields:checkFormFields
				};
				pmsPager.init(opt);
			}else{
				FW.error(data.msg || "没有找到对应的发票信息");
			}
		});
		
		
	});
	function initOther(opt){
		var form=opt.form;
		var data=opt.data;
		form.iForm('endEdit');
		showReadOnlyReceiptList(data);
		pmsPager.invoiceSum=data.data.sum;
	}
	function confirmCheck(){
		var form=pmsPager.opt.form;
		if(!confirmCheck.init){
			confirmCheck.init=true;
			
			form.iForm('setVal',{"ischeck":"N"});

			$("#b-confirm").html("确认添加收款记录");

			showEditableReceiptList();
			return ;
		}
		if(!validConfirmCheckListAmount(form)){
			return ;
		}
		submitData(form);
	}
	
	//提交数据
	function submitData(form){
		var data=form.iForm('getVal');
		var gridData=getReceiptListData();
		$.post(basePath+'pms/invoice/checkReceipt.do',{"invoice":FW.stringify(data),"invoiceConfirm":FW.stringify(gridData),"isInvoiceReceiptable":pmsPager.isInvoiceReceiptable},function(result){
			showBasicMessageFromServer(result,"确认收款成功","确认收款失败");
		});
	}
	
	function validConfirmCheckListAmount(form){
		//初始化特殊变量
		pmsPager.isInvoiceReceiptable=false;
		if(!form.valid()){
			return false;
		}
		if(!dataGrid.iValidDatagrid()){
			return false;
		}
		var rows=getReceiptListData();
		var totalRecordList=countReceiptAmount(rows);
		if(totalRecordList<= pmsPager.invoiceSum){
			//如果是要确认发票已经收款完毕，则验证不通过，不在外层执行后台数据提交操作。
			if(totalRecordList==pmsPager.invoiceSum){
				FW.confirm("实际收款总金额等于发票金额，确认完结此发票已经完成收款？",{
				    onConfirm : function(){
				        pmsPager.isInvoiceReceiptable=true;
				        submitData(form);
				    },
				    onCancel : function(){
				    	pmsPager.isInvoiceReceiptable=false;
				    	FW.success("本次确认操作已经取消");
				    }
				});
				return false;
			}else{
				return true;
			}
			
		}else{
			FW.error('实际收款金额大于发票金额');
			return false;
		}
		
		function countReceiptAmount(rows){
			var result=0;
			for(var i in rows){
				result+=parseFloat(rows[i].confirmSum);
			}
			return result;
		}
		
	}
	
	function showReadOnlyReceiptList(data){
		if(data && data.data && data.data.invoiceConfirmVos && data.data.invoiceConfirmVos.length){
			$('#addReceiptWrapper').show();
			initReceiptTitle();
			initReceiptRecordList(data.data.invoiceConfirmVos);
			hideDataGridColumns(dataGrid,['garbage-colunms']);
		}
	}
	
	function showEditableReceiptList(data){
		
		$('#addReceiptWrapper').show();
		$('#b-add-receipt').show();
		initReceiptTitle();
		initReceiptRecordList(data);
		showDataGridColumns(dataGrid,['garbage-colunms']);
		
	}
	
	function initReceiptTitle(){
		if(!initReceiptTitle.first){
			initReceiptTitle.first=true;
			$('#receiptListWrapper').iFold();
		}
		
	}
	
	function initReceiptRecordList(data){
		if(!initReceiptRecordList.first){
			initReceiptRecordList.first=true;
			window.dataGrid=$('#receiptList').datagrid({
				fitColumns:true,
				singleSelect:true,
				scrollbarSize:0,
				data:data,
				onClickCell : function(rowIndex, field, value){
					if(field=='garbage-colunms'){
						var rows=dataGrid.datagrid('getRows');
						var row=rows[rowIndex];
						if(row['id']!=null){
							FW.error('不能删除已确认的收款记录');
							return ;
						}
						deleteGridRow(dataGrid,rowIndex);
					}
				},
	
				columns:[[
					{
						field : 'id',
						hidden : true
					}, {
						field : 'contractId',
						hidden : true
					}, {
						field : 'payId',
						hidden : true
					}, {
						field : 'payplanId',
						hidden : true
					}, {
						field : 'confirmTime',
						title : '到款时间',
						width : 105,
						align : 'left',
						fixed:true,
						editor:{ 
							type : 'datebox',
							dateType:"date",
							options : {
								minView:2,
								format:"yyyy-mm-dd"
							},
        					rules:{
        						required:true
        					}
						},
						formatter:function(value,row,index){
		 					//时间转date的string，还有long2date(value)方法
		 					return FW.long2date(value);
		 				}
					}, {
						field : 'confirmSum',
						title : '到款金额(元)',
						width : 105,
						align : 'right',
						fixed:true,
						editor:{ 
							type : 'text',
        					rules:{
        						required:true,
        						number:true
        					}
						}
					},
					{
						field : 'command',
						title : '备注',
						width : 90,
						align : 'left',
						editor:{ 
							type : 'text'
						}
					},garbageColunms
				]]
			});
		}
	}
	
	function addReceipt(){
		var row={};
		dataGrid.datagrid('appendRow',row);
		var rowindex=dataGrid.datagrid('getRowIndex',row);
		dataGrid.datagrid('beginEdit',rowindex);
	
		$('#b-add-receipt').html('继续添加收款记录');
	}
	
	function getReceiptListData(){
		var res=null;
		if(dataGrid){
			var rows=dataGrid.datagrid('getRows');
			for(var i=0;i<rows.length;i++){
				dataGrid.datagrid('endEdit',i);
				
			}
			res=dataGrid.datagrid('getRows');
			resetDataGridAfterGetData();
		}
		return res;
	}
	
	function resetDataGridAfterGetData(){
		var rows=dataGrid.datagrid('getRows');
		for(var i in rows){
			var row=rows[i];
			if(row["id"]==null){
				dataGrid.datagrid('beginEdit',i);
			}
		}
	}
</script>
</head>
<body>
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <div id="toolbar1" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm ">
	            <button type="button" class="btn btn-default "  onclick="closeTab();">关闭</button>
	        </div>
	        <div class="btn-group btn-group-sm ">
	            <button type="button" class="btn btn-default pms-button"  onclick="confirmCheck();" id="b-confirm">添加收款记录</button>
	        </div>



		</div>
	</div>
	<div class="inner-title">
		收款详情
	</div>

	<form id="form1" class="margin-form-title "></form>
	
	<div  class="margin-group-bottom" style="display:none;" id="addReceiptWrapper">
		<form id="receiptListWrapper" grouptitle="实际到款信息" class="margin-title-table">
			<table id="receiptList" class="eu-datagrid"></table>
		</form>
        <div class="btn-toolbar margin-foldable-button">
			<div class="btn-group btn-group-xs" >
				 <button type="button" class="btn btn-success" style="display:none;" onclick="addReceipt();" id="b-add-receipt">添加到款记录</button>
			</div>
		</div>
        
	</div>
</body>
</html>