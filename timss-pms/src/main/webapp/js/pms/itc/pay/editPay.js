function showReadOnlyInvoice(data){
	if(data && data.data && data.data.invoices && data.data.invoices.length){
		$('#addInvoiceWrapper').show();
		$("#invoiceListWrapper").iFold();
		initInvoice(data.data.invoices);
		$('#b-add-invoice').hide();
		dataGrid.datagrid('hideColumn',"garbage-colunms");
	}
}

function showEditableInvoice(data){
	$('#addInvoiceWrapper').show();
	$("#invoiceListWrapper").iFold();
	
	initInvoice(data&& data.data && data.data.invoices);
	var rows=dataGrid.datagrid('getRows');
	for(var i in rows){
		dataGrid.datagrid('beginEdit',i);
	}
}

/**
 * 以何种方式显示发票信息
 * @param opt 包含是否显示发票的信息
 * @param data 付款的所有信息
 */
function showInvoice(opt,data){
	if(!opt.readOnly){
		if(ctype=='cost'){
		    showEditableInvoice(data);
		}else{
			showReadOnlyInvoice(data);
		}
		
	}else if(opt.invoice){
		showEditableInvoice(data);
	}else{
		showReadOnlyInvoice(data);
	}
}
/**
 * 申请退票
 */
function applyUndo(){
	FW.dialog("init",{
		src: basePath+"page/pms/core/pay/applyUndo.jsp",
		btnOpts:[{
	            "name" : "关闭",
	            "float" : "right",
	            "style" : "btn-default",
	            "onclick" : function(){
	                _parent().$("#itcDlg").dialog("close");
	             }
	        },
	        {
	            "name" : "确定",
	            "float" : "right",
	            "style" : "btn-success",
	            "onclick" : function(){
	            	var p = _parent().window.document.getElementById("itcDlgContent").contentWindow;
	            	var data =  $("#form1").iForm("getVal");
	            	data["bepay"] =data["bepay"].split(" ")[0];
        			data["actualpay"] =data["actualpay"].split(" ")[0];
	            	var id = $("#form1").iForm("getVal","id");
	            	var undoFlowId = $("#form1").iForm("getVal","undoFlowId");
	                var undoRemark = p.$("#form1").iForm("getVal","undoRemark"); 
	                //提交后台处理
	                $.ajax({
	                	url:basePath+"pms/pay/applyUndo.do",
	                	type:"post",
	                	data:{undoFlowId:undoFlowId,undoRemark:undoRemark,id:id},
	                	success:function(result){
	                		if(result && result.flag=="success"){
	                			FW.success("保存成功");
	                			//关闭对话框和标签页
	                			_parent().$("#itcDlg").dialog("close");
	                			if( ""==undoFlowId || null==undoFlowId ){
	                				undoFlowId = result.data.undoFlowId;
	                				$("#form1").iForm("setVal",{undoFlowId:undoFlowId,undoRemark:undoRemark});
	                				data["undoRemark"]=undoRemark;
	                			}
	                			forbidTipAfterCloseTab();
	                			var workFlow = new WorkFlow();
	                			workFlow.submitApply(result.data.taskId,FW.stringify(data),closeTab,null,0,closeTab);
	                		}
	                	}
	                });
	             }
	        }],
		dlgOpts:{ width:320, height:180, closed:false, title:"填写退票说明", modal:true }
	});
}

function openViewProject(){
	var projectId=$("#form1").iForm('getVal')["projectId"];
	openTab('pms/project/editProjectJsp.do?id='+projectId,'立项','pmsViewProjectTab'+projectId);
}