var InvMatApplyBtn={
	init:function(){
		InvMatApplyBtn.close();
		InvMatApplyBtn.save();
		InvMatApplyBtn.submit();
		InvMatApplyBtn.processinfo();
		InvMatApplyBtn.add();
		InvMatApplyBtn.edit();
		InvMatApplyBtn.send();
		InvMatApplyBtn.stopsend();
		InvMatApplyBtn.deletecg();
		InvMatApplyBtn.rebackTostock();
		InvMatApplyBtn.asset();
	},
	initList:function(){
		InvMatApplyBtn.news();
		InvMatApplyBtn.search();
		InvMatApplyBtn.initInput();
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
			buttonType = "save";
			commitMatApply("save");
		});
	},
	//提交按钮
	submit:function(){
		$("#btn_submit").click(function(){
			buttonType = "submit";
			commitMatApply("submit");
		});
	},
	//预览按钮
	processinfo:function(){
		if(processInstId.indexOf("wo_")>-1 || processInstId.indexOf("itsm_")>-1){
			$("#btn_processinfo").hide();
		}else{
			//流程信息
			$("#btn_processinfo").click(function(){
				var workFlow = new WorkFlow();
		    	if(processStatus == "first" || processInstId == "0"){ //验收后自动生成的领料单流程实例ID设置为0
					workFlow.showDiagram(defKey);
				}else{
					var showFlag = 0;
					var obj = {};
					obj['type'] = "submit";
					
					if("editable"==isEdit){
						showFlag = 1;
					}
					
					if(processStatus == "first_save" || processStatus == "last"){
						workFlow.showAuditInfo(processInstId,null,0,saveMatApply,obj);
					}else{
						workFlow.showAuditInfo(processInstId,null,showFlag,saveMatApply,obj);
					}
				}
			});
		}
	},
	//添加物资按钮
	add:function(){
		$("#btn_add").click(function(){
			FW.showInventoryDialog({invmatapply:1,categoryname:$("#f_applyType").parent().find(".itcui_combo_text").text(),
				onParseData : function(data){
					addCallBack("normal");
					var codeArr = [];
					var wareHouseIdArr = [];
					var invCateIdArr = [];
					var listData =$("#matapplydetail_grid").datagrid("getRows");
					//去除已存在的记录 --begin
					for(var z=0;z<listData.length;z++){
						codeArr.push(listData[z].itemcode);
						wareHouseIdArr.push(listData[z].warehouseid);
						invCateIdArr.push(listData[z].invcateid);
					}

					for(var i=0;i<data.length;i++){
						for(var j=0;j<codeArr.length;j++){
							if(data.length>0 && null != data[i]
								&& ((codeArr[j] == data[i]["itemcode"]) && (invCateIdArr[j] == data[i]["cateId"]))){
								data.splice(i,1);
								j=-1;
							}
						}
					}
					//去除已存在的记录 --end
					addInvMatApplyItem(data,"normal");
					var newListData =$("#matapplydetail_grid").datagrid("getRows");
					if(0<newListData.length){
						$("#btn_add").text("继续添加物资");
					}else{
						$("#btn_add").text("添加物资");
					}
					caluPrice();
					startEditAll();
				}
			});
		});
		//根据采购申请添加按钮
		$("#btn_addFromPA").click(function(){
			FW.dialog("init",{
				src: basePath+"inventory/invmatapply/invMatApplyPurApplyList.do",
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
				                var rows = p.$("#table_apply").datagrid("getRows");
				                //因为单选开启模式下的datagrid,取消复选框不可以触发selected和checked属性的消除，这里重新遍历一次复选框，判断是否选择了元素
				                var hasSelected = false;
				                for(var i = 0;i<rows.length;i++){
				                	var element = p.$("[datagrid-row-index="+i+"] .icheckbox_flat-blue").attr("aria-checked");
				                	if("true"==element){
				                		hasSelected = true;
				                	}
				                }
				                if(!hasSelected){
				                	FW.error("请选择一条采购申请");
				                	return false;
				                }
				                var selectRow = p.$("#table_apply").datagrid("getSelected");
				                var sheetId = selectRow["sheetId"];
				                if(null==sheetId){
				                	console.log("ERROR--can't get selected purApplyId");
				                	return false;
				                }
				                addCallBack("fpa");
				                //异步请求，找到相应的采购申请的物资明细
				                $.ajax({
				            		type : "POST",
				            		async: false,
				            		url: basePath+"inventory/invmatapply/invMatApplyPurApplyItemList.do",
				            		data: {"sheetId":sheetId},
				            		dataType : "json",
				            		success : function(data) {
				            			addInvMatApplyItem(data.data,"fpa");
				    					//设置关联值
				            			$("#autoform").iForm("setVal",{"relatePurApplyIdsList":sheetId});
				            			caluPrice();
				    					startEditAll();
				    					_parent().$("#itcDlg").dialog("close");
				            		} 
				            	});
				            }
				        }],
				dlgOpts:{width:800, height:520, closed:false, title:"选择采购申请添加物资", modal:true}
			});
		});
	},
	//编辑按钮(页面控件解锁)
	edit:function(){
		$("#btn_edit").click(function(){
			initPageProcess.edit();
		});
	},
	//列表查询
	send:function(){
		$("#btn_send").click(function(){
			if(!checkAllOutStock()){
				FW.dialog("init",{
					src: basePath+"inventory/invmatapply/invMatConsuming.do?imaid="+imaid,
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
					                var listData =$("#matapplydetail_grid").datagrid("getRows");
					                var rowData = p.consumData();
					                
					                if(null != rowData && "" != rowData){
					                	for(var i=0;i<listData.length;i++){
						                	 for(var j=0;j<rowData.length;j++){
						                		 if(listData[i].itemid == rowData[j].itemid && 
						                				 listData[i].invcateid == rowData[j].invcateid){
						                			 $("#matapplydetail_grid").datagrid("updateRow",{
						     							index: i,
						     							row: { "outstockqty": Number(rowData[j].outstockqty)}
						     						 });
						                		 }
						                	 }
						                }
					                	
					                	 $("#btn_send").text("继续通知领料");
						                _parent().$("#itcDlg").dialog("close");
						                autoGenerateConsuming();
					                }
					            }
					        }],
					dlgOpts:{width:800, height:520, closed:false, title:"领料数量填写", modal:true}
				});
			}else{
				FW.error( "申请物资库存量不足不能领料");
			}
		});
	},
	//终止领料
	stopsend:function(){
		$("#btn_stopsend").click(function(){
			FW.confirm("确认终止领料|确定不再领取此领料单中剩余未发料的物资吗？", function() {
			    $.ajax({
					type : "POST",
					url: basePath + "inventory/invmatapply/stopSend.do",
					data: {
						"imaId": imaid,
						"taskId": taskId
					},
					dataType : "json",
					success : function(data) {
						if(data.result=="success"){
							FW.success( "终止成功" );
							FW.getFrame(FW.getCurrentTabId()).location.reload();
						}
						else{
							FW.error( "终止失败" );
						}
					},
					error: function (XMLHttpRequest, textStatus, errorThrown) {
						FW.error(XMLHttpRequest.responseJSON.msg);
					}
				});
			});
		});
	},
	news:function(){
		$("#btn_new,.btn_new").click(function(){
		   	var url = basePath+ "inventory/invmatapply/invMatApplyForm.do?imaid=";
		    var prefix = new Date().getTime() + "" + Math.floor(Math.abs(Math.random()*100));
		    FW.addTabWithTree({
		        id : "newMatApplyForm" + prefix,
		        url : url,
		        name : "物资领料",
		        tabOpt : {
		            closeable : true,
		            afterClose : "FW.deleteTab('$arg');FW.activeTabById('stock');FW.getFrame(FW.getCurrentTabId()).refCurPage();"
		        }
		    });
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
	
	initInput:function(data){
		$("#quickSearchInput").keypress(function(e) {
		    if(e.which == 13) {
		    	InvMatApplyBtn.initListWithPo(data);
		    }
		});
		$("#quickSearchInput").iInput("init",{
			"onClickIcon":function(){
				InvMatApplyBtn.initListWithPo(data);
			}
		});
	},
	//根据物资编码和物资名称模糊搜索
	initListWithPo:function(data){
		var item=$.trim($("#quickSearchInput")[0].value);
		isSearchMode = true;
		$("#invmatapply_grid").iDatagrid("init",{
			queryParams: {
				search: FW.stringify({itemCode:item, itemName:item})
			}
		});
		
	},
	
	//删除草稿
	deletecg :function(){
		$("#btn_delete").click(function(){
			var url = basePath + "inventory/invmatapply/stopProcess.do";
			FW.deleteDraft(url,imaid);
		});
	},
	//退库
	rebackTostock:function(){
		$("#btn_refund").click(function(){
			openRefundPage();
		});
	},
	//资产化
	asset:function(){
		$("#btn_asset").click(function(){
			var selectedRow = $("#matapplydetail_grid").datagrid("getSelected");
			if(selectedRow==null){
				FW.error( "请先从物资明细列表选中一条物资" );
				return;
			}
			else{
			    $.ajax({
					type : "POST",
					url: basePath + "inventory/invmatapplydetail/queryMatApplyDetailAssetInfo.do",
					data: {
						"imadId":selectedRow.imadid
					},
					dataType : "json",
					success : function(data) {
						if(data.result=="success"){
							//弹框出现，赋值弹框内的隐藏框
							openAssetWin(selectedRow,data);
						}
						else{
							FW.error( data.msg );
						}
					},
					error: function (XMLHttpRequest, textStatus, errorThrown) {
						FW.error(XMLHttpRequest.responseJSON.msg);
					}
				});
			}
		});
	}
};

function addCallBack(type){
	var relatePurApplyIdsList = $("#autoform").iForm("getVal","relatePurApplyIdsList");
	//如果上次是普通添加且这次也是普通添加,才不删除已有的物资表
	if(undefined!=relatePurApplyIdsList){
		if(!(""==relatePurApplyIdsList&&"fpa"!=type)){
		var rows = $("#matapplydetail_grid").datagrid("getRows");
		for(var i = rows.length-1;i>=0;i--){
			$("#matapplydetail_grid").datagrid("deleteRow",i);
		}
		console.log("INFO--has deleted "+rows.length +" records。");
	}
	//清空领料关联的采购申请
	$("#autoform").iForm("setVal",{"relatePurApplyIdsList":""})
	console.log("INFO--has deleted relatePurApplyIdsList。");
	}
}

//添加物资领料表单的物资明细
function addInvMatApplyItem(data,type){
	console.log("INFO--detecte "+data.length+" rows rec")
	//价格校验和设置
	for(var i=0;i<data.length;i++){
		//价格设置
		var tempPrice;
		var tempNoTaxPrice;//暂时只沙C多经用，其余站点依旧含税或不含税单价存储在price字段
		if("SWF"==siteid){	//生物质领料价取不含税单价，其余站点取含税单价
			tempPrice = data[i]["noTaxPrice"];
		}
		else if("SJW"==siteid){	//沙C多经站点价格严格区分
			tempPrice = data[i]["price"];
			tempNoTaxPrice = data[i]["noTaxPrice"];
		}
		else{
			tempPrice = data[i]["price"];

		}
		if(typeof(tempPrice) != "undefined" && tempPrice != null){
			data[i]["price"] = parseFloat(tempPrice).toFixed(2);
		}
		else{
			FW.error( "无法获取物资"+data[i]["itemcode"]+"价格，请联系系统管理员");
			return;
		}
		if( "SJW" == siteid ){
			if(typeof(tempNoTaxPrice) != "undefined" && tempNoTaxPrice != null){
				data[i]["noTaxPrice"] = parseFloat(tempNoTaxPrice).toFixed(2);
			}
			else{
				FW.error( "无法获取物资"+data[i]["itemcode"]+"不含税价格，请联系系统管理员");
				return;
			}
		}
	}
	for(var i=0;i<data.length;i++){
		var row = {};
		row["tmpid"]=i+1;
		row["price"] = data[i]["price"];
		row["noTaxPrice"] = data[i]["noTaxPrice"];
		row["itemid"] = data[i]["itemid"];
		row["itemcode"] = data[i]["itemcode"];
		row["itemname"] = data[i]["itemname"];
		row["cusmodel"] = data[i]["cusmodel"];
		if("fpa"!=type){
			row["qtyApply"] = '1';
		}else if("fpa"==type){
			row["qtyApply"] = data[i]["invNum"];
		}
		row["stockqty"] = data[i]["stockqty"];
		row["nowqty"] = data[i]["nowqty"];
		row["unit1"] = data[i]["unitname"];
		row["unitCode1"] = data[i]["unit1"];

		row["totalprice"] = row["price"];
		row["totalNoTaxPrice"] = row["noTaxPrice"];
		row["outqty"] = '0';
		row["outqtytemp"] = '';
		row["outstockqty"] = '0';
		row["warehouseid"] = data[i]["warehouseid"];
		row["warehouse"] = data[i]["warehouse"];
		row["invcateid"] = data[i]["cateId"];
		row["invcate"] = data[i]["cateType"];
		row["status"] = '';
		
		$("#matapplydetail_grid").datagrid("appendRow",row );
	}
	$("#matapplydetail_grid").datagrid("resize");
}

//点击弹框出现
function openAssetWin(selectedRow,data) {
	var src = basePath+"page/inventory/core/invmatapply/invMatApplyWin.jsp";
	var submiturl;
	submiturl = basePath + 'inventory/invmatapplydetail/passMemo.do';
	showAssetWindow(src,'资产化申请',submiturl,selectedRow,data);
}

//弹窗的“确定”，“取消” 按钮功能设定
function showAssetWindow(src,title,submiturl,selectedRow,data){
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
		"onclick" : function(){
			//捕获表单里的信息,并转为String型
			//子窗口
			var conWin = _parent().window.document.getElementById("itcDlgContent").contentWindow;
			var memoData=conWin.$("#assetForm").iForm('getVal');
			if(!conWin.$("#assetForm").valid()){
				//FW.error("提交的内容有错误的地方，请修改后重试");
				return false;
			}
			//var memoDataStr = JSON.stringify(memoData);  //取表单值
			//构造对象并且赋值
			var invMatAssetApply = new Object();
			invMatAssetApply.itemName = selectedRow.itemname;
			invMatAssetApply.imadId = selectedRow.imadid;
			invMatAssetApply.poId = data.purOrderId;
			invMatAssetApply.cusmodel = selectedRow.cusmodel;
			invMatAssetApply.companyName = data.companyName;
			invMatAssetApply.companyTel = data.companyTel;
			invMatAssetApply.purchaseDate = data.purchaseDate;
			invMatAssetApply.memo = memoData.memo;
			//bean内没有的字段
			invMatAssetApply.locationName = "未分类资产";
			//转为string
			var BeanStr = JSON.stringify(invMatAssetApply);
			
			$.ajax({
				type : "POST",
				cache: false,
				data: {
					//assetApplyBean:assetApplyBean
					BeanData:BeanStr
				},
				url: submiturl,
				success: function(data){
					if(data.result=="success"){
						FW.success("提交成功");
					}
				},
				error:function(XMLHttpRequest, textStatus, errorThrown){
					FW.error(XMLHttpRequest.responseJSON.msg);
				}
			});
			
			_parent().$("#itcDlg").dialog("close");
		}
	}];
	//新建系统配置对话框
	var dlgOpts = {
		width : 600,
		height : 600,
		height : title=='资产化申请'?460:460,
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

