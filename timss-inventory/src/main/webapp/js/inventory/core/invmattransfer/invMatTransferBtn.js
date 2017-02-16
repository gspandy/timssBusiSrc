var InvMatTransferBtn={
	init:function(){
		InvMatTransferBtn.close();
		InvMatTransferBtn.save();
		InvMatTransferBtn.submit();
		InvMatTransferBtn.add();
		InvMatTransferBtn.edit();
		InvMatTransferBtn.audit();
		InvMatTransferBtn.deletecg();
	},
	initList:function(){
		InvMatTransferBtn.news();
		InvMatTransferBtn.search();
	},
	//关闭按钮	
	close:function(){
		$("#btn_close").click(function(){
			FW.deleteTabById(FW.getCurrentTabId());
		});
	},
	//暂存按钮
	save:function(){
		$("#btn_save").click(function(){
			commitMatApply("save");
		});
	},
	//提交按钮
	submit:function(){
		$("#btn_submit").click(function(){
			commitMatApply("submit");
		});
	},
	//添加物资按钮
	add:function(){
		$("#btn_add").click(function(){
			FW.showInventoryDialog({invmatapply:1,categoryname:$("#f_applyType").parent().find(".itcui_combo_text").text(),
				onParseData : function(data){
					$("#btn_add").text("继续添加物资");
					var codeArr = [];
					var listData =$("#matapplydetail_grid").datagrid("getRows");
					for(var z=0;z<listData.length;z++){
						codeArr.push(listData[z].itemcode);
					}
					
					for(var i=0;i<data.length;i++){
						for(var j=0;j<codeArr.length;j++){
	                		if(data.length>0 && null != data[i] && codeArr[j] == data[i]["itemcode"]){
	                			data.splice(i,1);
	                			j=-1;
	                		}
	                	}
					}
				
					for(var i=0;i<data.length;i++){
						var row = {};
						row["tmpid"]=i+1;
						row["itemid"] = data[i]["itemid"];
						row["itemcode"] = data[i]["itemcode"];
						row["itemname"] = data[i]["itemname"];
						row["cusmodel"] = data[i]["cusmodel"];
						row["qtyApply"] = '1';
						row["stockqty"] = data[i]["nowqty"];
						row["unit1"] = data[i]["unit1"];
						row["price"] = parseFloat(data[i]["price"]).toFixed(2);
						row["totalprice"] = parseFloat(data[i]["price"]).toFixed(2);
						row["outqty"] = '0';
						row["outqtytemp"] = '';
						row["outstockqty"] = '0';
						row["warehouse"] = data[i]["warehouse"];
						row["bin"] = data[i]["bin"];
						row["cateType"] = data[i]["cateType"];
						row["warehouseid"] = data[i]["warehouseid"];
						row["invcateid"] = data[i]["cateId"];
						row["status"] = '';
						$("#matapplydetail_grid").datagrid("appendRow",row );
					}
					caluPrice();
					startEditAll();
				}
			});
		});
	},
	//编辑按钮(页面控件解锁)
	edit:function(){
		$("#btn_edit").click(function(){
			initPageProcess.edit();
		});
	},
	//审批
	audit:function(){
		$("#btn_audit").click(function(){
			initPageProcess.edit();
		});
	},
	search:function(){
		$("#btn_search").click(function(){
		    if(isSearchLineShow){
			    isSearchLineShow=false;
		        $("#invmatapply_grid").iDatagrid("endSearch");
		    }
		    else{
		    	isSearchLineShow=true;
		       	$("#invmatapply_grid").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(arg){
		       		isSearchMode = true;
					return {"search":JSON.stringify(arg)};
				}});
		    }
		});
	},
	//删除草稿
	deletecg :function(){
		$("#btn_delete").click(function(){
			var url = basePath + "inventory/invmatapply/stopProcess.do";
			FW.deleteDraft(url,imaid);
		});
	}
};



//工单权限
function initButtonPriv(){
	//保存
	Priv.map("privMapping.INVMATTRANSFER_SAVE","INVMATTRANSFER_SAVE");
	//编辑
	Priv.map("privMapping.INVMATTRANSFER_COMMIT","INVMATTRANSFER_COMMIT");
	//打印
	Priv.map("privMapping.INVMATTRANSFER_DELETE","INVMATTRANSFER_DELETE");
	//提交
	Priv.map("privMapping.INVMATTRANSFER_EDIT","INVMATTRANSFER_EDIT");
	//新建
	Priv.map("privMapping.INVMATTRANSFER_INVALID","INVMATTRANSFER_INVALID");
	//删除
	Priv.map("privMapping.INVMATTRANSFER_AUDIT","INVMATTRANSFER_AUDIT");
	Priv.apply();
	FW.fixRoundButtons("#toolbar");	
}



