//提交按钮
function commitStocktaking(obj){
	var type = obj.type;
	var formData = $("#autoform").ITC_Form("getdata");
	if(!$("#autoform").valid()){
		return;
	}
	
	var data = null;
	var listData = null;
	var listData2 = null;
	//列表是否存在
	var rows = $("#stocktakingdetail_grid").prev(".datagrid-view2").children(".datagrid-body").find(".datagrid-row");
	if(rows.length != 0){
		listData = $("#stocktakingdetail_grid").datagrid("getRows");
		listData2 = listData;
		if(listData.length>0){
			if(!$("#stocktakingform").valid()){
				return;
			}else{
				endEditAll();
				listData = $("#stocktakingdetail_grid").datagrid("getRows");
				listData2 = listData;
			}
			
			for(var i=0;i<rows.length;i++){
				var row = $(rows[i]);
				var qtyBefore = row.children("td[field='qtyBefore']").find("div").text();
				qtyBefore = parseFloat(qtyBefore).toFixed(4);
				
				if(isNaN(qtyBefore)){
					FW.error("物资【"+listData[i]["itemcode"]+"】无法获取物资库存量，请联系系统管理员");
					return;
				}
				
				if( process === 'fill_pan'){
					var qtyAfter = row.children("td[field='qtyAfter']").find("div").text();
					qtyAfter = parseFloat(qtyAfter).toFixed(4);
					
					var nowqty = row.children("td[field='nowqty']").find("div").text();
					nowqty = parseFloat(nowqty).toFixed(4);
					
					var stockqty = row.children("td[field='stockqty']").find("div").text();
					stockqty = parseFloat(stockqty).toFixed(4);
					
					var soQty = parseFloat(stockqty - nowqty).toFixed(2);//领料占用
					
					if(qtyAfter-soQty<0){
						numVal = listData[i]["itemcode"];
					}
				}
			}
			
			//避免影响盘点物资物资列表原来的数据
			listData2 = FW.parse( FW.stringify(listData2) );
			//清掉可能出现中文或换行符且用不着的字段 -- begin by gchw
			for(var i =0;i<listData2.length;i++){
				listData2[i]["cusmodel"]="";
				listData2[i]["itemname"]="";
			}
			//清掉可能出现中文或换行符且用不着的字段 -- end
		}else{
			if(type=="submit"){
				FW.error( "请选择盘点物资 ");
				return;
			}
		}
	}else{
		if(type=="submit"){
			FW.error( "请选择盘点物资 ");
			return;
		}
	}
	
	var ids = $("#uploadform").ITC_Form("getdata");
	uploadIds=JSON.stringify(ids.uploadField);
	
	if(null!=listData){
		data = { 
				"formData":FW.stringify(formData),
				"listData":FW.stringify(listData2),
				"istid":istid,
				"taskId":taskId,
				"uploadIds":uploadIds
			};
	}else{
		data = { 
				"formData":FW.stringify(formData),
				"istid":istid,
				"taskId":taskId,
				"uploadIds":uploadIds
			};
	}
	
	//加载用户表单数据
	$.ajax({
		type : "POST",
		async: false,
		url: basePath+"inventory/invstocktaking/saveStocktaking.do",
		data: data,
		dataType : "json",
		success : function(data) {
			taskId = data.taskId;
			processInstId = data.processInstanceId;
			if( data.result == "success" ){
				//生成快照（如果是首环节则执行）
				if("" == istid){
					$.ajax({
						url : basePath+"inventory/invmatsnapshot/saveAsSnapshot.do",
						type : "POST",
						data: {
							"remark":"库存盘点生成快照，盘点审批单为："+$("#f_sheetname").val(),
							"type":"A"
							},
						dataType : "json",
						success : function() {}
					});
				}
				
				istid = data.istid;
				saveFlag = true;
				
				if(data.switchFlag){
					var workFlow = new WorkFlow();
					if(null!=listData){
						startEditAll();
						changeCss();
					}
					
					if(type=="submit"){
						if(processStatus == "first" || processStatus == "first_save"){
							workFlow.submitApply(taskId,null,submitCallBack,cancelCallBack,null);
						}else{
							if(processStatus == "last"){
								
								if("SWF" == siteid){
							    	commitAudit(taskId,null,changeInvStock,closeCurTab,closeCurTab,proMsg,0);
							    }else{
							    	changeInvStock();
							    }
								
							}else{
								workFlow.showAudit(taskId,null,submitCallBack,closeCurTab,closeCurTab,proMsg,0);
							}
						}
					}else{
						FW.success( "操作成功 ");
						//若列表没有信息则提示打印盘点表
						if(null == listData){
							FW.confirm("导出盘点表|确定导出盘点表?",{
								onConfirm : function(){
									$("#btn_export").trigger("click");
								},
								onCancel : function(){
									FW.deleteTabById(FW.getCurrentTabId());
								}
							});
						}else{
							listName = "stocktakingdetail_grid";
						}
					}
				}else{
					if(null == listData){
						FW.success( "操作成功 ");
						FW.confirm("导出盘点表|确定导出盘点表?",{
							onConfirm : function(){
								$("#btn_export").trigger("click");
							},
							onCancel : function(){
								FW.deleteTabById(FW.getCurrentTabId());
							}
						});
					}else{
						changeInvStock();
					}
				}
			}else{
				FW.error( "操作失败 ");
			}
		}
	});
}

//修改库存数量
function changeInvStock(){
	$.ajax({
		type : "POST",
		async: false,
		url: basePath+"inventory/invstocktaking/changeInvStock.do",
		data: {"istid":istid},
		dataType : "json",
		success : function(data) {
			FW.success( "操作成功 ");
			if(data.result == "success" && data.switchFlag ){
				if("SWF" != siteid){
					autoCommitProcess();
				}else{
					closeCurTab();
				}
			}
			$("#btn_save").hide();
			$("#btn_submit").hide();
			closeCurTab();
		},
		error: function(data){
			FW.error(data.responseJSON.msg);
			startEditAll();
		}
	});
}

//删除记录
function delRecord(itemid,itemname){
	Notice.confirm("删除？|确定删除所选项吗？该操作无法撤消。",function(){
		var msg = "删除\""+itemname+"\"项。";
		proMsg += msg;
		$('#stocktakingdetail_grid').datagrid('deleteRow',$('#stocktakingdetail_grid').datagrid('getRowIndex',itemid));
		var listData =$("#stocktakingdetail_grid").datagrid("getRows");
		if(listData.length == 0){
			$("#btn_add").text("添加物资");
		}else{
			$("#btn_add").text("继续添加物资");
		}	
	},null,"info");	
};

//针对swf盘点审批最后一步验证盘点数量
function commitAudit(taskId,businessData,agree,rollback,stop,updateDesc,multiSelect,onCloseFunc){
	if(multiSelect == undefined){
		multiSelect=1;
	}
	var src = basePath+"workflow/page/auditJsp.do?taskId="+taskId+"&updateDesc="+updateDesc+"&multiSelect="+multiSelect;
	var param = {};
	param['businessData'] = businessData;
    var btnOpts =  [{
        "name" : "取消",
        "float" : "right",
        "style" : "btn-default",
        "onclick" : function(){
            _parent().$("#itcDlgShowAudit").dialog("close");
        }
    },{
        "name" : "确定",
        "float" : "right",
        "style" : "btn-success",
        "onclick" : function(){
        	if(numVal===""){
    			var p = _parent().window.document.getElementById("itcDlgShowAuditContent").contentWindow;
                p.submit(businessData,agree,rollback,stop);
        	}else{
        		FW.error("已有领料单领用了物资"+numVal+"，请保留充足数量或者取消领料单后再操作");
        	}
        }
    }
   ];
    
   $.post(src,param,function(data){
	   var dialogsrc = basePath + data.path+"?taskId="+taskId+"&updateDesc="+updateDesc+"&multiSelect="+multiSelect;
	   var dlgOpts = {		
			   width : 480,
	            height:data.height,
	            idSuffix : "ShowAudit",
	            closed : false,
	            title:data.dialogtitle,
	            modal:true,
	            onClose:onCloseFunc
	   };
	    Notice.dialog(dialogsrc,dlgOpts,btnOpts);
   });
}