var PayBtn={
	init:function(){
		PayBtn.close();
		PayBtn.deletecg();
		PayBtn.revoke();
		PayBtn.save();
		PayBtn.submit();
		PayBtn.audit();
		PayBtn.process();
		PayBtn.newQapay();
		PayBtn.erp();
	},
	//关闭按钮
	close:function(){
		$("#btn-close").click(function(){
			pageClose();
		});
	},
	//暂存按钮
	save:function(){
		$("#btn-save").click(function(){
			var obj = {};
			obj['saveType'] = "save";
			commitApply(obj);
		});
	},
	//删除
	deletecg :function(){
		$("#btn-delete").click(function(){
			var url = basePath + "purchase/purpay/removePay.do";
			removePay("del",url);
		});
	},
	//作废
	revoke :function(){
		$("#btn-revoke").click(function(){
			var url = basePath + "purchase/purpay/removePay.do";
			removePay("revoke",url);
		});
	},
	//提交按钮
	submit:function(){
		$("#btn-submit").click(function(){
			var obj = {};
			obj['saveType'] = "submit";
			commitApply(obj);
		});
	},
	//审批按钮
	audit:function(){
		$("#btn-audit").click(function(){
			var obj = {};
			obj['saveType'] = "audit";
			commitApply(obj);
		});
	},
	//流程信息按钮
	process:function(){
		$("#btn-process").click(function(){
			var workFlow = new WorkFlow();
			if("new" == formStatus){
				workFlow.showDiagram(defKey);
			}else{
				var obj = {};
				obj['saveType'] = "audit";
				if("processed"!= formStatus && "true" ==isCandidate ){
					//弹出的对话框带审批按钮
					workFlow.showAuditInfo(procInstId,null,1,commitApply,obj);
				}else{
					//弹出的对话框不带审批按钮
					workFlow.showAuditInfo(procInstId,null,0,commitApply,obj);
				}
			}
		});
	},
	newQapay:function(){
		$("#btn-newqapay").click(function(){
			var url = basePath + "purchase/purpay/purPayForm.do?sheetId="+sheetId+"&operType=new&payType=qualitypay&relatepayId="+payId+"&payId=";
		    var suffix = new Date().getTime() + Math.floor(Math.abs(Math.random()*100));
		    FW.addTabWithTree({
		        id : "newPayForm" + suffix,
		        url : url,
		        name : "质保金付款",
		        tabOpt : {
		            closeable : true,
		            afterClose : "FW.deleteTab('$arg');FW.activeTabById('"+FW.getCurrentTabId()+"');FW.deleteTabById(FW.getCurrentTabId());"
		        }
		    });
		});
	},
	erp:function(){
		if(erpStatus == '1'){
			$("#btn-erp").text('重导ERP');
		}
		var flag = false;
		$("#btn-erp").click(function(){
			//固定资产的，不允许导入到ERP
			var rows = $('#purPays_dtl').datagrid('getRows');
			$.each(rows, function(i, item){      
			    if(item.invcateid == 'ICI8900'){
			    	flag = true;
			    	return false;
			    }
			}); 
			if(flag){
				FW.error("\"固定资产类\"物资不能导入ERP.");
				return;
			}
			var src=basePath + "page/purchase/swf/purpay/sendERP.jsp?payId=" + payId;
	    	var submiturl = basePath+"purchase/purpay/sendToERP.do";
	    	erpDiag(src,"导入ERP",submiturl);
		});
	}
};
//删除的方法
function removePay(deleteType,url){
	var tip = null;
	if(deleteType == "del"){
		tip = "删除草稿";
	}else if(deleteType == "revoke"){
		tip = "作废审批";
	}
	Notice.confirm(tip+"|是否确定要"+tip+"？",function(){
		$.ajax({
			url : url,
			dataType : "json",
			type : "POST",
			async: false,
			data : {
				"procInstId" : procInstId,
				"taskId" : taskId,
				"message" :"报账单--" + tip,
				"payId" : payId,
				"deleteType" : deleteType
			},
			success : function(data) {
				if (data.result == "success") {
					FW.success(tip+"成功 ");
					pageClose();
				} else {
					FW.error(tip+"失败 ");
				}
			}
		});
	},null,"info");
}

function erpDiag(src,title,submiturl){
	
	var btnOpts = [{
		"name" : "取消",
		"float" : "right",
		"style" : "btn-default",
		"onclick" : function() {
			return true;
		}
	},{
		"name" : "确定",
		"float" : "right",
		"style" : "btn-success",
		"onclick" : function() {
			var conWin = _parent().window.document.getElementById("itcDlgContent").contentWindow;
			var form=conWin.$("#form1").iForm('getVal');
			//var siteId=conWin.$("#form1").iForm('getVal','siteId');
			if(!validForm(form)){
				return ;
			}
			var buttonReturnFlag=false;
			$.ajax({
				type:"post",
				url:submiturl,
				data:{
					payId:form.payId,
					invNum:form.invNum,
					invDesc:form.invDesc
				},
				dataType:"json",
				async:false,
				complete:function(res){
					var response=res.responseJSON;
					var result= response && response.result;
					if(result=='success'){
						FW.success("保存成功");
						//$("#siteConf_grid").datagrid("reload");
						buttonReturnFlag = true;
						//修改按钮名称
						//$('#btn-erp').attr('disabled','disabled');
						$('#btn-erp').text('重发ERP');
					}else{
						//执行失败,则通知用户错误原因，同时不关闭对话框
						if(result == '-1'){
							FW.error("发票编号重复，请重新输入.");
						}else{
							FW.error("系统执行错误");
						}
					}
				}
			});
			return buttonReturnFlag;
		}
	}];
	//新建系统配置对话框
	var dlgOpts = {
		width : 450,
		height : 220,
		closed : false,
		title : title,
		modal : true
	};
	FW.dialog("init", {
		"src" : src,
		"dlgOpts" : dlgOpts,
		"btnOpts" : btnOpts
	});
}

function validForm(form){
	if(form.invNum =='' ||form.invDesc ==''){
		FW.error("请填写必要的发票信息.");
		return false;
	}else if(form.invNum.length > 40){
		FW.error("发票编号长度不能大于40.");
		return false;
	} else if(form.invDesc.length > 200){
		FW.error("发票描述长度不能大于200.");
		return false;
	}else{
		return true;
	}
	
}