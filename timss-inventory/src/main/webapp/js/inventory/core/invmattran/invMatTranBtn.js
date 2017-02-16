var InvMatTranBtn={
	init:function(){
		InvMatTranBtn.close();
		InvMatTranBtn.save();
		InvMatTranBtn.returnsMat();
		InvMatTranBtn.add();
		InvMatTranBtn.deleted();
		InvMatTranBtn.edit();
		InvMatTranBtn.asset();
	},
	initList:function(){
		InvMatTranBtn.search();
		InvMatTranBtn.news();
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
				saveMatTran();
		});
	},
	//退货按钮 
	returnsMat:function(){
		$("#btn_returns").click(function(){
				returnsMatOutStock();
		});
	},
	//添加物资按钮
	add:function(){
		$("#btn-add").click(function(){
			var listData =$("#mattrandetail_grid").datagrid("getRows");
			var arrayData = [];
			for(var z=0;z<listData.length;z++){
				arrayData.push(listData[z].itemcode);
			}
			
			var pruordernoVal = $("#f_pruorderno").val();
			var lotnoVal = $("#f_lotno").val();
			if(null != pruordernoVal&& "" != pruordernoVal){
				FW.dialog("init",{
					src: basePath+"inventory/invitem/invArrivalItemList.do?pruordernoVal="+pruordernoVal+"&lotnoVal="+lotnoVal,
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
				                var arr = p.getArrivalItem();
				                var count = arr.length;
				                for(var i=0;i<count;i++){
			                		for(var j=0;j<arrayData.length;j++){
				                		if(arr.length>0 && null != arr[i] && arrayData[j] == arr[i].itemcode){
				                			arr.splice(i,1);
				                			j=-1;
				                		}
				                	}
				                }
				                
				                for(var i=0;i<arr.length;i++){
				                	$("#mattrandetail_grid").datagrid("appendRow",arr[i]);
				                }
				                
				                var listData =$("#mattrandetail_grid").datagrid("getRows");
								if(listData.length > 0){
									$("#btn-add").text("继续添加到货物资");
								}else{
									$("#btn-add").text("添加到货物资");
								}
				                
				                startEditAll();
				                dynaCalcTotalPrice();
	                            _parent().$("#itcDlg").dialog("close"); 
				            }
				        }],
					dlgOpts:{ width:800, height:520, closed:false, title:"到货物资列表", modal:true }
				});
			}else{
				FW.error("请先选择采购合同号 ");
			}
		});
	},
	//删除物资按钮（批量删除）
	deleted:function(){
		$("#btn-delete").click(function(){
			var rowData = $("#mattrandetail_grid").datagrid("getSelections");
			if( rowData == null || rowData == "" ){
				FW.error("请选择要删除的记录 ");
				return;
			}else{
				for(var i=0;i<rowData.length;i++){
					var index =$("#mattrandetail_grid").datagrid("getRowIndex",rowData[i]);
					$("#mattrandetail_grid").datagrid("deleteRow",index);
				}
			}
			var listData =$("#mattrandetail_grid").datagrid("getRows");
			if(listData.length == 0){
				$("#btn-delete").attr("disabled",true);
			}	
		});
	},
	//编辑按钮(页面控件解锁)
	edit:function(){
		$("#btn_edit").click(function(){
			initPageProcess.edit();
		});
	},
	//列表查询
	search:function(){
		$("#btn_search").click(function(){
		    if(isSearchLineShow){
			    isSearchLineShow=false;
		        $("#invmattran_grid").iDatagrid("endSearch");
		    }
		    else{
		    	isSearchLineShow=true;
		       	$("#invmattran_grid").iDatagrid("beginSearch",{"noSearchColumns":{7:true},"remoteSearch":true,"onParseArgs":function(arg){
		       		isSearchMode = true;
					return {"search":JSON.stringify(arg)};
				}});
		    }
		});
	},
	//新建
	news:function(){
		$("#btn_new,.btn_new").click(function(){
		   	var url = basePath+ "inventory/invmattran/invMatTranForm.do";
		    var prefix = new Date().getTime() + "" + Math.floor(Math.abs(Math.random()*100));
		    FW.addTabWithTree({
		        id : "newMatTranForm" + prefix,
		        url : url,
		        name : "物资接收",
		        tabOpt : {
		            closeable : true,
		            afterClose : "FW.deleteTab('$arg');FW.activeTabById('stock');FW.getFrame(FW.getCurrentTabId()).refCurPage();"
		        }
		    });
		});
	},
	//资产化
	asset:function(){
		$("#btn_asset").click(function(){
			var selectedRow = $("#mattrandetail_grid").datagrid("getSelected");
			var acceptDate = $("#autoform").iForm("getVal").createdate;
			if(selectedRow==null || selectedRow==""){
				FW.error( "请选择要资产化的物资" );
				return;
			}
			else{
			    $.ajax({
					type : "POST",
					url: basePath + "inventory/invmattrandetail/queryMatTranDetailAssetInfo.do",
					data: {
						"imtdId" : selectedRow.imtdid,
						"batchstockqty" : selectedRow.batchstockqty,
						"outterid" : selectedRow.outterid 
					},
					dataType : "json",
					success : function(data) {
						if(data.result=="success"){
							//弹框出现，赋值弹框内的隐藏框
							data['imtdid'] = selectedRow.imtdid;
							data['itemid'] = selectedRow.itemid;
							data['itemcode'] = selectedRow.itemcode;
							data['itemname'] = selectedRow.itemname;
							data['cusmodel'] = selectedRow.cusmodel;
							//接收日期赋值
							data['purchaseDate'] = acceptDate;
							openAssetWin(selectedRow,data);
						}
						else{
							FW.error( data.msg );
						}
					},
					error: function (XMLHttpRequest, textStatus, errorThrown) {
						FW.error(XMLHttpRequest.responseJSON.msg);
						//FW.error("请选择要资产化的物资3");
					}
				});
			}
		});
	}
};
	
//点击弹框出现
function openAssetWin(selectedRow,data) {
	var dataStr = JSON.stringify(data);
	var companyName = data.companyName;
	var src = basePath+"page/inventory/core/invmatapply/invMatApplyWin.jsp?dataStr="+encodeURIComponent(dataStr);
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
			var objectData=conWin.$("#assetForm").iForm('getVal');
			if(!conWin.$("#assetForm").valid()){
				//FW.error("提交的内容有错误的地方，请修改后重试");
				return false;
			}
			//var memoDataStr = JSON.stringify(memoData);  //取表单值
			//构造对象并且赋值
			var invMatAssetApply = new Object();
			invMatAssetApply.imtdId = objectData.imtdid,
			invMatAssetApply.itemId = objectData.itemid,
			invMatAssetApply.itemName = objectData.itemName;
			invMatAssetApply.financialCode = objectData.financialCode;
			invMatAssetApply.logo = objectData.logo;
			invMatAssetApply.equipmentId = objectData.equipmentId;
			invMatAssetApply.cusmodel = objectData.cusmodel;
			invMatAssetApply.companyName = objectData.companyName;
			invMatAssetApply.companyTel = objectData.companyTel;
			invMatAssetApply.purchaseDate = objectData.purchaseDate;
			invMatAssetApply.memo = objectData.memo;
			//待校验
			invMatAssetApply.poId = selectedRow.outterid;
			//bean内没有的字段
			invMatAssetApply.locationName = "未分类资产";
			//转为string
			var BeanStr = JSON.stringify(invMatAssetApply);
			
			$.ajax({
				type : "POST",
				cache: false,
				data: {
					BeanData:BeanStr
				},
				url: submiturl,
				success: function(data){
					if(data.result=="success"){
						FW.success("提交成功");
						FW.getFrame(FW.getCurrentTabId()).location.reload();
					}
				},
				error:function(XMLHttpRequest, textStatus, errorThrown){
					FW.error(XMLHttpRequest.responseJSON.msg);
				}
			});
			
			_parent().$("#itcDlg").dialog("close");
			//$("#mattranasset_list").datagrid("reload")
			//FW.navigate(basePath+"inventory/invmattrandetail/queryMatTranAssetRecord.do?imtid=IMI3673");
		}
	}];
	//新建系统配置对话框
	var dlgOpts = {
		width : 600,
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

