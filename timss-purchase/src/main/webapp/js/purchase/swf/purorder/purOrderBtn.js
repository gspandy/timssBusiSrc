var OrderBtn={
	init:function(){
		OrderBtn.close();
		OrderBtn.submit();
		OrderBtn.submitno();
		OrderBtn.save();
		OrderBtn.edit();
		OrderBtn.process();
		OrderBtn.add();
		OrderBtn.deletecg();
		OrderBtn.newgroup();
	},
	swfInit:function(){
		OrderBtn.init();
		OrderBtn.newInvoiceBtn();
		OrderBtn.stopOrderBtn();
	},
	//关闭按钮
	close:function(){
		$("#btn-close").click(function(){
			pageClose();
		});
	},
	//提交按钮
	submit:function(){
		$("#btn-submit").click(function(){
			var obj = {};
			obj['type'] = "submit";
			
			if(processStatus == "last"){
				Notice.confirm("是否完成|确定已完成采购并结束采购合同流程吗?",function(){
					commitApply(obj);
				},null,"info");
			}else{
				commitApply(obj);
			}
			
		});
	},
	submitno:function(){
		$("#btn-submit-no").click(function(){
			var obj = {};
			obj['type'] = "submit_no";
			commitApply(obj);
		});
	},
	//暂存按钮
	save:function(){
		$("#btn-save").click(function(){
			var obj = {};
			obj['type'] = "save";
			commitApply(obj);
		});
	},
	//流程信息按钮
	process:function(){
		$("#btn-process").click(function(){
			var workFlow = new WorkFlow();
			if(processStatus == "first"){
				workFlow.showDiagram(defKey);
			}else{
				var showFlag = 0;
				var obj = {};
				obj['type'] = "submit";
				if("editable"==isEdit&&Priv.hasPrivilege("exampurch_approve")){
					showFlag = 1;
				}
				if(processStatus == "first_save" || processStatus == "last"){
					workFlow.showAuditInfo(processInstId,null,0,commitApply,obj);
				}else{
					workFlow.showAuditInfo(processInstId,null,showFlag,commitApply,obj);
				}
			}
		});
	},
	//添加物资按钮
	add:function(){
		$("#btn-add").click(function(){
			FW.dialog("init",{
			src: basePath+"purchase/purorder/purOrderItemList.do?includeitems="+FW.stringify(deleteItems),
			btnOpts:[{
			            "name" : "取消",
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
			                var ids = p.getListIds();
			                var listData =$("#order_item").datagrid("getRows");
			                var itemidArr = [];
			                if(null!=listData){
			                	for(var i=0;i<listData.length;i++){
			                		itemidArr[i] = listData[i].applySheetId+"_"+listData[i].itemid+"_"+listData[i].warehouseid+"_"+listData[i].invcateid;
			                	}
			                }
			                var ogriIds = itemidArr.join(",");
			                if("" != ids){
			                    $.ajax({
			                        url : basePath+"/purchase/purorder/queryPurOrderItems.do",
			                        dataType: "json",
			                        type:"POST",
			                        data: { "itemids":ids, "ogriIds":ogriIds },
			                        success:function(data){
			                        	var result = data.result;
			                            if(null != result && "" != result){
			                               var resultArr = result.split(";");
			                               //追加行后立刻执行编辑操作
			                               for(var i=0;i<resultArr.length;i++){
			                            	   	//采购申请添加物资是调用FW.showInventoryDialog 带了listId
			                            	    //这里的对话框由于新建时没有listId 就用时间戳造一个临时的listId-----begin------
			                            	    var timestamp = Date.parse(new Date());
			                            	   	timestamp = timestamp / 1000;
			                            	   	resultArr[i] = resultArr[i].replace('"listId":null','"listId":"new_'+timestamp+i+'"');
			                            	    //-----end-----
			                               		$("#order_item").datagrid("appendRow",eval("(" + resultArr[i] + ")"));
			                               }
			                               startEditAll();
										   $("#btn-add").text("继续添加物资");
			                            }
			                             startEditAll();
			                             resetSelectAttr();
			                             dynaCalcTotalPrice();
			                             checkFormComboEditable();
			                             _parent().$("#itcDlg").dialog("close"); 
			                        }
			                    });
			                }
			            }
			        }],
			dlgOpts:{width:800, height:520, closed:false, title:"申请采购物资", modal:true}
			});
		});
	},
	//删除物资按钮（批量删除）
	deleted:function(){
		$("#btn-delete").click(function(){
			var rowData = $("#order_item").datagrid("getSelections");
			if( rowData == null || rowData == "" ){
				FW.error("请选择要删除的记录 ");
				return;
			}else{
				for(var i=0;i<rowData.length;i++){
					var index =$("#order_item").datagrid("getRowIndex",rowData[i]);
					$("#order_item").datagrid("deleteRow",index);
				}
			}
			var listData =$("#order_item").datagrid("getRows");
			if(listData.length == 0){
				$("#btn-delete").attr("disabled",true);
			}
		});
	},
	//编辑按钮(页面控件解锁)
	edit:function(){
		$("#btn-edit").click(function(){
			//这个事件只有initlist在调用，所以这里的方法尽可能剥离掉对表单处理的操作。
			var gridEditable = "";
			if(""!=oper){
				gridEditable = JSON.parse(oper).editable;
			}
			//editForm(edit_form); 这里之前要执行editForm 是由于下方对表单的一些字段进行了设置，但也就造成初始化表格和初始化表单的耦合
			//startEditAll(); 这个地方使用startEditAll 当明细的条目较多的时候，会导致表格 loadSuccess卡在这个操作上。
			if(processStatus == "first" || processStatus == "first_save"){
				$("#btn-save").show();
				if(processStatus == "first_save"){
					$("#btn-delete").show();
					if(classType == "Draft"){
						$("#btn-delete").html("删除");
					}
				}
				$("#order_item").datagrid("showColumn","del");
				checkFormComboEditable();
			}else{
				$("#order_item").datagrid("hideColumn","del");
			}
			$("#btn-edit").hide();
			//需要与审批信息对话框的审批按钮保持一致的权限控制
			if("editable"==isEdit&&Priv.hasPrivilege("exampurch_approve")){
				$("#btn-submit").show();
			}
			var listData =$("#order_item").datagrid("getRows");
			if(processStatus == "process" || processStatus == "last"){
				$("#pageTitle").html("采购合同详情");
			}else{
				if(""!=sheetId&&"editable"==isEdit){
					$("#pageTitle").html("编辑采购合同");
				}else{
					$("#pageTitle").html("采购合同详情");
				}
			}
			if(listData.length > 0){
				$("#btn-add").text("继续添加物资");
			}else{
				$("#btn-add").text("添加物资");
			}
			FW.fixToolbar("#toolbar1");
		});
	},
	//删除
	deletecg :function(){
		$("#btn-delete").click(function(){
			var url = basePath + "purchase/purorder/stopProcess.do";
			FW.deleteDraft(url);
		});
	},
	//新建录入发票
	newInvoiceBtn:function(){
		$("#btn-invoice").click(function(){
			var url = basePath + "purchase/purInvoice/insertInvoiceToPage.do?contractId="+sheetId+"&contractNo="+sheetNo;
		    var prefix = new Date().getTime() + "" + Math.floor(Math.abs(Math.random()*100));
		    FW.addTabWithTree({
		        id : "newInvoiceForm" + prefix,
		        url : url,
		        name : "发票",
		        tabOpt : {
		            closeable : true,
		            afterClose : "FW.deleteTab('$arg');FW.activeTabById('"+FW.getCurrentTabId()+"');FW.getFrame(FW.getCurrentTabId()).refCurPage();"
		        }
		    });
		});
	},
	//终止合同
	stopOrderBtn:function(){
		$("#btn-stopOrder").click(function(){
			FW.confirm("终止执行后，该订单剩余未接收物资将不再执行，是否确认执行该操作？",function(){
				var url = basePath+ "purchase/purorder/stopOrder.do";
				$.ajax({
	                url : url,
	                dataType: "json",
	                type:"POST",
	                data: { "sheetId":sheetId},
	                success:function(data){
	                	if("success"==data.flag){
	                		FW.success("终止合同成功");
	                		pageClose();
	                	}else{
	                		FW.error("终止合同出错");
	                	}
	                }
	            });
	        });
		});
	},
	newgroup:function(){
		$("#btn-newgroup").click(function(){
			if(!createPay()){
				$(".btn-purpay").hide();
			}else{
				$(".btn-purpay").show();
			}
		});
	}
};

//列表页面按钮控制
var OrderListBtn={
		init:function(){
			OrderListBtn.newBtn();
			OrderListBtn.advlocalBtn();
		},
		//新建
		newBtn:function(){
			$("#btn_new,.btn_new").click(function(){
				var url = basePath+ "purchase/purorder/purOrderForm.do?type=new&sheetId=";
			    var prefix = new Date().getTime() + "" + Math.floor(Math.abs(Math.random()*100));
			    FW.addTabWithTree({
			        id : "newOrderForm" + prefix,
			        url : url,
			        name : "采购合同",
			        tabOpt : {
			            closeable : true,
			            afterClose : "FW.deleteTab('$arg');FW.activeTabById('purchasing');FW.getFrame(FW.getCurrentTabId()).refCurPage();"
			        }
			    });
			});
		},
		//查询
		advlocalBtn:function(){
			$("#btn_advlocal").click(function(){
			    if($(this).hasClass("active")){
			    	$("#btn_advlocal").removeClass("active");
			        $("#table_order").iDatagrid("endSearch");
			    }
			    else{
			    	$("#btn_advlocal").addClass("active");
			       	$("#table_order").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(arg){
			       		isSearchMode = true;
						return {"search":JSON.stringify(arg)};
					}});
			    }
			});
		},
		ipsBtn:function(){
			$("#btn_invPickSupplies").click(function(){
		    	var sheetIds = [];
		    	var rowData = $("#table_order").datagrid("getSelections");
		    	for(var i=0;i<rowData.length;i++){
		    		if(rowData[i].statusName == '审批通过' || rowData[i].statusName == '部分入库'){
		    			sheetIds.push(rowData[i].sheetId);
		    		}
				}
				if(sheetIds.length>0){
					var generateId = sheetIds.join(",");
		    		FW.autoGenerateMatTran(generateId.toString());
				}else{
					FW.error("请选择审批通过后的记录生成物资接收单");
				}
		    });
		}
};