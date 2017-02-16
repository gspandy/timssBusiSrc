var InvStocktakingBtn = {
	init:function(){
		InvStocktakingBtn.save();
		InvStocktakingBtn.submit();
		InvStocktakingBtn.edit();
		InvStocktakingBtn.close();
		InvStocktakingBtn.add();
		InvStocktakingBtn.processinfo();
		InvStocktakingBtn.print();
		InvStocktakingBtn.deletecg();
		
		if(processStatus == "first" || processStatus == "first_save"){
			$("#btn_submit").html("提交");
		}
	},
	initList:function(){
		InvStocktakingBtn.search();
		InvStocktakingBtn.news();
		InvStocktakingBtn.print();
	},
	//保存按钮
	save:function(){
		$("#btn_save").click(function(){
			if(numVal===""){
				var obj = {};
				obj['type'] = "save";
				commitStocktaking(obj);
			}else{
				FW.error("已有领料单领用了物资"+numVal+"，请保留充足数量或者取消领料单后再操作");
			}
		});
	},
	//提交按钮
	submit:function(){
		$("#btn_submit").click(function(){
			if(numVal===""){
				var obj = {};
				obj['type'] = "submit";
				commitStocktaking(obj);
			}else{
				FW.error("已有领料单领用了物资"+numVal+"，请保留充足数量或者取消领料单后再操作");
			}
		});
	},
	//编辑按钮
	edit:function(){
		$("#btn_edit").click(function(){
			initPageProcess.edit();
		});
	},
	//关闭按钮
	close:function(){
		$("#btn_close").click(function(){
			FW.deleteTabById(FW.getCurrentTabId());
		});
	},
	//添加按钮
	add:function(){
		$("#btn_add").click(function(){
			var warehouseid = $("#autoform").iForm("getVal", "warehouseid");
			
			FW.showInventoryDialog({
				warehouseid: warehouseid,
				onParseData : function(data){
					var isInitData = true;
					$("#btn_add").text("继续添加物资");
					var codeArr = [];
					var listData =$("#stocktakingdetail_grid").datagrid("getRows");
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
						row["itemid"] = data[i]["itemid"];
						row["itemcode"] = data[i]["itemcode"];
						row["itemname"] = data[i]["itemname"];
						row["cusmodel"] = data[i]["cusmodel"];
						row["invcateid"] = data[i]["invcateid"];
						row["warehouseid"] = data[i]["warehouseid"];
						row["nowqty"] = data[i]["nowqty"]===""||data[i]["nowqty"]===null?"--":data[i]["nowqty"];
						row["qtyBefore"] = data[i]["stockqty"]===""||data[i]["stockqty"]===null?"--":data[i]["stockqty"];
						if(data[i]["stockqty"]===""||data[i]["stockqty"]===null){
							isInitData = false;
						}
						row["qtyAfter"] = data[i]["stockqty"];
						row["breakeven"] = '--';
						row["unitname"] = data[i]["unitname"];
						row["unitid"] = data[i]["unit1"];
						row["price"] = data[i]["price"]===""||data[i]["price"]===null?0:parseFloat(data[i]["price"]).toFixed(2);
						row["binid"] = data[i]["binid"];
						row["bin"] = data[i]["bin"];
						$("#stocktakingdetail_grid").datagrid("appendRow",row );
					}
					if(!isInitData){
						FW.error("无法获取物资库存量，请联系系统管理员");
					}
					startEditAll();
				}
			});
		});
	},
	//流程信息按钮
	processinfo:function(){
		$("#btn_processinfo").click(function(){
			var workFlow = new WorkFlow();
	    	if(processStatus == "first"){
				workFlow.showDiagram(defKey);
			}else{
				var showFlag = 0;
				var obj = {};
				obj['type'] = "submit";
				
				if("editable"==isEdit){
					showFlag = 1;
				}
				
				if(processStatus == "first_save" || processStatus == "last"){
					workFlow.showAuditInfo(processInstId,null,0,commitStocktaking,obj);
				}else{
					workFlow.showAuditInfo(processInstId,null,showFlag,commitStocktaking,obj);
				}
			}
		});
	},
	//打印按钮
	print:function(){
		var browserType = ( navigator.userAgent.indexOf("Trident/7.0")>-1||navigator.userAgent.indexOf("IE")>-1||navigator.userAgent.indexOf("Edge")>-1)?"IE":"OTHER";
		var dataStr = FW.long2date(new Date()).replace("-","").replace("-","");
		$("#btn_export,.btn_export").bindDownload({
			url : fileExportPath+"preview?__format=xlsx&__report=report/TIMSS2_IST_001_xlsx.rptdesign&siteid="+siteid+"&cnFileName=库存盘点表_"+dataStr+"&browserType="+browserType
		});
	},
	//查询按钮
	search:function(){
		$("#btn_search").click(function(){
		    if(isSearchLineShow){
			    isSearchLineShow=false;
		        $("#invstocktaking_grid").iDatagrid("endSearch");
		    }
		    else{
		    	isSearchLineShow=true;
		       	$("#invstocktaking_grid").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(arg){
		       		isSearchMode = true;
					return {"search":JSON.stringify(arg)};
				}});
		    }
		});
	},
	//新建
	news:function(){
		$("#btn_new,.btn_new").click(function(){
		   	var url = basePath+ "inventory/invstocktaking/invStocktakingForm.do?istid=";
		    var prefix = new Date().getTime() + "" + Math.floor(Math.abs(Math.random()*100));
		    FW.addTabWithTree({
		        id : "newStocktakingForm" + prefix,
		        url : url,
		        name : "库存盘点",
		        tabOpt : {
		            closeable : true,
		            afterClose : "FW.deleteTab('$arg');FW.activeTabById('stock');FW.getFrame(FW.getCurrentTabId()).refCurPage();"
		        }
		    });
		});
	},
	//删除草稿
	deletecg :function(){
		$("#btn_delete").click(function(){
			var url = basePath + "inventory/invstocktaking/stopProcess.do";
			FW.deleteDraft(url,istid);
		});
	}
};