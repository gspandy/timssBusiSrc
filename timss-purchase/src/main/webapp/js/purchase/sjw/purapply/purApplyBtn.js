var ApplyBtn={
	init:function(){
		ApplyBtn.close();
		ApplyBtn.submit();
		ApplyBtn.save();
		ApplyBtn.edit();
		ApplyBtn.process();
		ApplyBtn.add();
		ApplyBtn.print();
		ApplyBtn.deletecg();
		ApplyBtn.nullifyStop();
		ApplyBtn.stop();
		ApplyBtn.stopprocess();
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
			obj['operation'] = operation;
			obj['activeStatus'] = activeStatus;
			//需要在此处执行，否则非数字就变为NaN了
			if (!$("#tableform").valid()) {
				FW.error("物资列表填写有误");
				return;
			}
			var listData = $("#apply_item").datagrid("getRows");
			if(processStatus == "last"||processStatus == "over"){
				listData = $("#apply_item").datagrid("getSelections");
			}
			
			if(listData.length == 0){
				FW.error( "请选择物资 ");
				return;
			}
			
			if(processStatus == "last"||processStatus == "over"){
				Notice.confirm("采购|确定执行采购？",function(){
					commitApplyForm(obj);
				},null,"info");
			}else{
				var isActive = true;
				if(processStatus === "first" || processStatus === "first_save"){
					for(var i=0;i<listData.length;i++){
						var active = listData[i].active;
						if( active === 'N' ){
							isActive = false;
							FW.error(listData[i].itemid+"物资的分类绑定已变化，请删除明细后重新添加");
							break;
						}
					}
				}
				if(isActive){
					commitApplyForm(obj);
				}
			}
		});
	},
	//暂存按钮
	save:function(){
		$("#btn-save").click(function(){
			var obj = {};
			obj['type'] = "save";
			obj['operation'] = operation;
			//需要在此处执行，否则非数字就变为NaN了
			if (!$("#tableform").valid()) {
				FW.error("物资列表填写有误");
				return;
			}
			commitApplyForm(obj);
		});
	},
	//结束审批流程
	over:function(){
		$("#btn-over").click(function(){
			commitApplyForm("over",operation);
		});
	},
	//流程信息按钮
	process:function(){
		$('[id=btn-process]').click(function(){
			var workFlow = new WorkFlow();
			
			if(processStatus == "first"){
				workFlow.showDiagram(defKey);
			}else{
				var showFlag = 0;
				var obj = {};
				obj['type'] = "submit";
				obj['operation'] = operation;
				
				if("editable"==isEdit){
					showFlag = 1;
				}
				
				if(processStatus == "first_save" || processStatus == "last"){
					workFlow.showAuditInfo(processInstId,null,0,commitApplyForm,obj);
				}else{
					workFlow.showAuditInfo(processInstId,null,showFlag,commitApplyForm,obj);
				}
			}
		});
	},
	//添加物资按钮
	add:function(){
		$("#btn-add").click(function(){
			currOpFilter = {};
			itemFilter = null;
			FW.showInventoryDialog({
				active: "Y",
				onParseData : function(data){
					var needAddOne = {};
					if(!itemFilter){
						itemFilter = {};
						var listData =$("#apply_item").datagrid("getRows");
						for(var i=0;i<listData.length;i++){
							itemFilter[listData[i].listId] = true;
						}
					}
					for(var i=0;i<data.length;i++){
						var code = data[i].listId;
						//单次打开对话框操作涉及的条目直接跳过
						if(currOpFilter[code]){
							continue;
						}
						currOpFilter[code] = true;
						if(!itemFilter[code]){
							//表格中没有的物品直接添加
							var row = {};
							row["ck"]='';
							row["listId"] = data[i]["listId"];
							row["itemid"] = data[i]["itemcode"];
							row["itemname"] = data[i]["itemname"];
							row["cusmodel"] = data[i]["cusmodel"];
							row["warehouseid"] = data[i]["warehouseid"];
							row["invcateid"] = data[i]["invcateid"];
							row["warehouse"] = data[i]["warehouse"];
							row["itemnum"] = '1';
							row["repliednum"] = '0';
							row["orderunitname"] = data[i]["unitname"];
							row["storenum"] = data[i]["stockqty"]===""||data[i]["stockqty"]===null?0:data[i]["stockqty"];
							row["averprice"] = data[i]["lateInPrice"]===""||data[i]["lateInPrice"]===null?0:parseFloat(data[i]["lateInPrice"]).toFixed(2);
							row["priceTotal"] = data[i]["lateInPrice"]===""||data[i]["lateInPrice"]===null?0:parseFloat(data[i]["lateInPrice"]).toFixed(2);
							$("#apply_item").datagrid("appendRow",row );
						}
						else{
							needAddOne[code]=true;
						}							
					}	
					//对表中已经存在的条目数量加1
					var rows = $("#apply_item").prev(".datagrid-view2").children(".datagrid-body").find(".datagrid-row");
					for(var j=0;j<rows.length;j++){
						var row = $(rows[j]);
						var rowCode = row.children("td[field='listId']").find("div").text();
						if(needAddOne[rowCode]){
							var ipt = row.children("td[field='itemnum']").find("input");
							ipt.val(Number(ipt.val())+1);
						}
					}					
					caluPrice();
					startEditAll();
					$("#btn-add").text("继续添加物资");
				}
			});
		});
	},
	//删除物资按钮（批量删除）
	deleted:function(){
		$("#btn-delete").click(function(){
			var rowData = $("#apply_item").datagrid("getSelections");
			if( rowData == null || rowData == "" ){
				FW.error("请选择要删除的记录 ");
				return;
			}else{
				for(var i=0;i<rowData.length;i++){
					var index =$("#apply_item").datagrid("getRowIndex",rowData[i]);
					$("#apply_item").datagrid("deleteRow",index);
				}
			}
			
			var listData =$("#apply_item").datagrid("getRows");
			if(listData.length == 0){
				$("#btn-delete").attr("disabled",true);
			}
		});
	},
	//编辑按钮(页面控件解锁)
	edit:function(){
		$("#btn-edit").click(function(){
			editForm(edit_single,operation[1]);
			startEditAll();
			if(processStatus == "first" || processStatus == "first_save"){
				$("#btn-save").show();
				if(processStatus == "first_save"){
					$("#btn-delete").show();
					if(classType == "Draft"){
						$("#btn-delete").html("删除");
					}
				}
			}else {
				$("#autoform").ITC_Form("readonly");
			}
			$("#btn-edit").hide();
			$("#btn-submit").show();
			
			if(processStatus == "process"){
				$("#pageTitle").html("采购申请详情");
			}else{
				if(""!=sheetId&&"editable"==isEdit&&"last"!=processStatus){
					$("#pageTitle").html("编辑采购申请");
				}else{
					$("#pageTitle").html("采购申请详情");
				}
			}
			
			/*if(processStatus != "last"){
				$("#apply_item").datagrid("showColumn","del");
			}else{
				$("#apply_item").datagrid("hideColumn","del");
			}*/
			var listData =$("#apply_item").datagrid("getRows");
			if(listData.length > 0){
				$("#btn-add").text("继续添加物资");
			}else{
				$("#btn-add").text("添加物资");
			}
			FW.fixToolbar("#toolbar1");
		});
	},
	print:function(){
		var url = fileExportPath+"preview?__format=pdf&__report=report/TIMSS2_SJW_PA_001_pdf.rptdesign&siteid="+siteId+"&sheetid="+sheetId;
		FW.initPrintButton("#btn_print",url,"采购申请打印预览",null);
		$("#btn_exportApplyItem").click(function(){
			var sheetno = $("#autoform").iForm("getVal","sheetno");
			var url = fileExportPath+"preview?__asattachment=true&__format=doc&__report=report/TIMSS2_SJW_PAITEM_001.rptdesign&sheetid="+sheetId+"&cnFileName=("+encodeURIComponent("采购申请物资清单")+")"+sheetno+")"+"&browserType="+browserType;
			window.location.href = url;
		});
	},
	//删除草稿
	deletecg :function(){
		$("#btn-delete").click(function(){
			var url = basePath + "purchase/purapply/stopProcess.do";
			FW.deleteDraft(url);
		});
	},//作废终止按钮
	nullifyStop:function(){
		$("#btn-nullifyStop").click(function(){
			nullifyStop();
		});
	},// 终止按钮
	stop:function(){
		if(""!=stopStatus){
			$("#btn-stop").html("审批终止");
		}
		$("#btn-stop").click(function(){
			if(""==stopStatus){
				FW.confirm("确定？|确认要终止采购此申请上物资吗？",function(){
					applyStop();
    	        });
			}else{
				applyStop();
			}
		});
	},//流程信息按钮
	stopprocess:function(){
		$("#btn-stopprocess").click(function(){
			var workFlow = new WorkFlow();
			if(""==stopProcInstId){
				workFlow.showDiagram(stopDefKey);
			}else{
				var showFlag = 0;
				var obj = {};
				if("true"==canStop){
					showFlag = 1;
				}
				workFlow.showAuditInfo(stopProcInstId,null,showFlag,applyStop,obj);
			}
		});
	}
};