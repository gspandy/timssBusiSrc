//初始化列表（通用方法）
function initList(){
	//审批过程中列表字段
	var columns = [[
	        {field:'imadid',title:'外部id',hidden:'true',width:10,fixed:true},
			{field:'itemcode',title:'物资编号',width:90,fixed:true,formatter:function(value,row){
				return "<a onclick='FW.showItemInfo(\""+row.itemcode+"\",\""+row.warehouseid+"\",\""+row.invcateid+"\");'>"+row.itemcode+"</a>";
			}},
			{field:'itemid',title:'物资id',width:10,fixed:true,hidden:true},
			{field:'itemname',title:'物资名称',width:120},
			{field:'cusmodel',title:'型号规格',width:200,fixed:true},
			{field:'warehouse',title:'仓库',width:100,fixed:true},
			{field:'invcate',title:'物资类别',width:100,fixed:true},
			{field:'nowqty',title:'可用库存',width:80,align:'right',fixed:true,formatter:function(value){
				return parseFloat(value).toFixed(2);
			}},
			{field:'stockqty',title:'实际库存',width:80,align:'right',fixed:true,formatter:function(value){
				return parseFloat(value).toFixed(2);
			}},
			{field:'qtyApply',title:'申领数量',width:80,align:'right',fixed:true,edit:true,
				editor:{
					type:'text',
					options:{
						align:"right",
						dataType:"number",
						onBlur:dynaCalcTotalPrice
					}
				}
			},
			{field:'outstockqty',title:'出库量',hidden:true},
			{field:'outqty',title:'已领料数量',width:80,fixed:true,hidden:true},
			{field:'unit1',title:'单位',width:50,fixed:true},
			{field:'unitCode1',title:'单位编码',fixed:true,hidden:true},
			{field:'price',title:'单价(元)',width:80,align:'right',fixed:true,
				formatter:function(value){
		    		return parseFloat(value).toFixed(2);
				}	
			},
			{field:'totalprice',title:'小计(元)',width:90,fixed:true,hidden:true},
			{field:'status',title:'状态',width:90,fixed:true,hidden:true},
			{field:'warehouseid',title:'仓库编码',fixed:true,hidden:true},
			{field:'invcateid',title:'类型id',fixed:true,hidden:true},
			{title:'',align:'center',field:'del',width:40,fixed:true,formatter:function(value,row){
				return "<img class='btn-delete btn-garbage' onclick='delRecord(\""+row.itemid+"\",\""+row.itemname+"\");' src='"+basePath+"img/inventory/btn_garbage.gif'/>";
			}}
	]];
		
	//发料列表字段	
	var cscolumns = [[
			{field:'imadid',title:'外部id',hidden:'true',width:10,fixed:true},
			{field:'itemcode',title:'物资编号',width:90,fixed:true,formatter:function(value,row){
				return "<a onclick='FW.showItemInfo(\""+row.itemcode+"\",\""+row.warehouseid+"\",\""+row.invcateid+"\");'>"+row.itemcode+"</a>";
			}},
			{field:'itemid',title:'物资id',hidden:true},
			{field:'itemname',title:'物资名称',width:120},
			{field:'cusmodel',title:'型号规格',width:200,fixed:true},
			{field:'warehouse',title:'仓库',width:100,fixed:true},
			{field:'bin',title:'货柜',width:90,fixed:true},
			{field:'invcate',title:'物资类别',width:100,fixed:true},
			{field:'nowqty',title:'可用库存',width:80,fixed:true,hidden:true,formatter:function(value){
				return parseFloat(value).toFixed(2);
			}},
			{field:'stockqty',title:'实际库存',width:80,fixed:true,hidden:true,
				formatter:function(value){
					return parseFloat(value).toFixed(2);
				},
				styler: function(value){
					if(value <= 0){
						return 'color:red';
					}
			}},
			{field:'qtyApply',title:'申领数量',width:80,align:'right',fixed:true},
			{field:'outqty',title:'已发料',width:90,align:'right',fixed:true},
			{field:'outstockqty',title:'可出库数量',width:80,fixed:true,hidden:true},
			{field:'unit1',title:'单位',width:50,fixed:true},
			{field:'unitCode1',title:'单位编码',fixed:true,hidden:true},
			{field:'outqtytemp',title:'临时字段',hidden:true},
			{field:'waitqty',title:'待领料临时字段',hidden:true},
			{field:'warehouseid',title:'仓库id',hidden:true},
			{field:'binid',title:'货柜id',hidden:true},
			{field:'status',title:'状态',width:90,fixed:true,hidden:true},
			{field:'lotno',title:'批次',hidden:true},
			{field:'price',title:'单价(元)',width:80,align:'right',fixed:true,
				formatter:function(value){
					return parseFloat(value).toFixed(2);
				}
			},
			{field:'invcateid',title:'类型id',fixed:true,hidden:true},
			{field:'totalprice',title:'小计(元)',hidden:true}
	]];
	
	var url = null;
	var titleColumns = null;
	var idField="";
	if(processStatus == "last"){
		url = basePath+"inventory/invmatapplydetail/queryMatApplyDetailCSList.do";
		titleColumns = cscolumns;
		idField="imadid";
	}else{
		url = basePath+"inventory/invmatapplydetail/queryMatApplyDetailList.do";
		titleColumns = columns;
		idField="itemid";
	}
	$("#matapplydetail_list").iFold("init");
	$("#matapplydetail_grid").datagrid({
		singleSelect:true,
		columns:titleColumns,
		url : url,
		idField:idField,
		fitColumns:true,
		queryParams: {"imaid":imaid,"embed":0},
		onLoadSuccess: function(data){
			var listData =$("#matapplydetail_grid").datagrid("getRows");
			if(data && data.total==0){
                $("#matapplydetail_grid").hide();
            }
   			if("" != imaid){
				initForm(edit_form);
				$("#autoform").ITC_Form("readonly");
				var sheetno = $("#f_sheetno").html();
				
				if(processStatus != "first" && processStatus != "first_save"){
					var applyType = $("input[name='applyType']").val(); 
					FW.showWorkOrderInfo("workOrderNo",siteid,sheetno,applyType);
					$("#matapplydetail_grid").datagrid("hideColumn", "nowqty");
					$("#matapplydetail_grid").datagrid("hideColumn", "stockqty");
				}
			}else{
				initForm(new_form);
				$("#autoform").iForm("setVal",{totalPrice:"0.00"});
			}
   			//最后环节且可以编辑
			if(processStatus == "last" && "editable"==isEdit){
				//还没有发料
				if(hasRpts!='Y'){
					$("#btn_send").show();
					$("#btn_stopsend").show();
				}else{
					//部分发料
					if(!checkAllOutStock()){
						$("#btn_send").show(); //终止领料按钮与通知领料按钮的显示逻辑应该完全一致
						$("#btn_stopsend").show();
					}else{
						$("#btn_send").hide();
						//$("#btn_stopsend").hide();//通知领料按钮隐藏，终止领料按钮不一定隐藏
					}
				}
				$("#btn_edit").hide();
				//$("#btn_print").show();
			}
			if((processStatus != "last" && "" != imaid) && "editable"==isEdit){
				$("#btn_edit").trigger("click");
			}
			
			initListStatus = FW.stringify(listData);
			setTimeout(function(){
				$("#matapplydetail_grid").datagrid("resize");
			},200);
			FW.fixToolbar("#toolbar");
        },
        onAfterEdit:function(rowIndex, rowData){
        	var outqty = rowData.outqty;
        	var outqtytemp = rowData.outqtytemp;
        	var qtyApply = rowData.qtyApply;
        	var stockqty = rowData.stockqty;
        	var outstockqty = rowData.outstockqty;
        	var price = rowData.price;
			
			var rowTotal =null;
			var alreadyOut = null;
			
			 if(processStatus == "last"){
				var total = parseFloat(outqty*1+outstockqty*1);
				if(qtyApply < total){
					FW.error("出库量超出申请量");
					flag = false;
					return;
				}
			}else{
				if(processStatus == "first" || processStatus == "first_save"){
					if(buttonType == "submit"){
						if(qtyApply > stockqty){
							FW.error( "申请数量超出可用库存上限");
			        		flag = false;
			        		return;
						}
					}
				}
			} 
			
			if(flag){
				if(null == outqtytemp || "" == outqtytemp){
					outqtytemp = outqty;
				}
				
				if(processStatus != "last"){
					rowTotal = parseFloat(qtyApply * price).toFixed(2);
					
					alreadyOut = (outqtytemp*1+outstockqty*1);
					$("#matapplydetail_grid").datagrid("updateRow",{
						index: rowIndex,
						row: { "totalprice": Number(rowTotal),"outqty":alreadyOut,"outqtytemp": outqtytemp}
					});
				}
			}
		},
		onDblClickRow : function(rowIndex, rowData) {
			if("ITC" == siteid){
				invMatApplyInfo = $("#autoform").iForm("getVal");
				FW.dialog("init",{
					/*src: basePath+"inventory/invmatapplydetail/queryMatApplyDetailInfo.do?imadId="+ rowData.imadid 
						+ "&sheetName=领料申请001"  + "&itemCode="  + rowData.itemname + "&cusmodel=" + rowData.cusmodel,*/
					src: basePath+"inventory/invmatapplydetail/queryMatApplyDetailInfo.do?imadId="+ rowData.imadid 
							+ "&imaid=" + invMatApplyInfo.imaid
							+ "&sheetno=" + invMatApplyInfo.sheetno
							+ "&rowData=" + encodeURIComponent(JSON.stringify(rowData)),	
					btnOpts:[{
					            "name" : "关闭",
					            "float" : "right",
					            "style" : "btn-default",
					            "onclick" : function(){
					            	return true;
					            }
					        }],
					dlgOpts:{width:600, height:400, closed:false, title:"物资领用记录", modal:true, idSuffix:"ItemDetail"}
				});
			}
		}
	});
}

//已经出库的记录列表
function initOutList(imaid){
	//查看出库记录
	var outcolumns = [[
			{field:'opertype',title:'操作类型',width:90,fixed:true,
				formatter:function(value,row){
					return "物资发料";
				}
			},
			{field:'sheetno',title:'单据编号',width:125,fixed:true,formatter:function(value,row){
				return "<a onclick='FW.showRecipientPage(\""+row.imrid+"\");'>"+row.sheetno+"</a>";
			}},
			{field:'createdate',title:'时间',width:130,fixed:true,
				formatter:function(value,row){
					return FW.long2time(value);
				}
			},
			{field:'status',title:'状态',width:80,
				formatter:function(value,row){
					if("Y"==value){
						return "已发料";
					}
					else{
						return "未发料";
					}
				},
				styler: function(value){
					if("Y"!=value){
						return 'color:red';
					}
				}
			}
	]];
	
	$("#matapplyout_list").iFold("init");
	$("#matapplyout_grid").datagrid({
		singleSelect:true,
		fitColumns : true,
		columns:outcolumns,
		onDblClickRow : function(rowIndex, rowData) {
			FW.showRecipientPage(rowData.imrid);
		},
		onLoadSuccess: function(data){
        	 if(data && data.total==0){
        		 $("#matapplyout_list").iFold("hide");
        		 $("#btn_refund").hide();
        	 }else{
        		// by ahua 显示退库列表
        		 if(processStatus != "last"){
        			 if (processStatus == "over") {// 流程结束
        					initRefundList(imaid);
        				}
        		 }else{
        			 initRefundList(imaid);
        		 }
        		 //initPrint();
        		 var rows = data.rows;
        		 //判断是否都未发料
        		 var hasRefund = false;
        		 for (var i = 0; i < rows.length; i++) {
        				var status = rows[i].status;
        				if(status != 'N'){
        					hasRefund = true;
        					break;
        				}
    			 }
        		 if(!hasRefund){
        			 $("#btn_refund").hide();
        		 }
        	 }
        	 setTimeout(function(){
             	$("#matapplyout_grid").datagrid("resize");
             },200);
        }
	});
	loadOutListData("init");
}

//已经退库的记录列表
function initRefundList(imaid){
	//查看出库记录
	var refundcolumns = [[
			{field:'opertype',title:'操作类型',width:90,fixed:true},
			{field:'sheetno',title:'单据编号',width:125,fixed:true,formatter:function(value,row){
				return "<a onclick='FW.showRefundInfo(\""+row.sheetno+"\",\""+row.remark+"\",\""+imaid+"\");'>"+row.sheetno+"</a>";
			}},
			{field:'createdate',title:'时间',width:130,fixed:true,
				formatter:function(value,row){
					return FW.long2time(value);
				}
			},
			{field:'warehousename',title:'仓库',width:100},
			{field:'remark',title:'退库原因',hidden:true}
	]];
	var outterList = $("#matapplyout_grid").datagrid("getRows");
	
	$("#matrefund_list").iFold("init");
	$("#matrefund_grid").datagrid({
		singleSelect:true,
		fitColumns : true,
		columns:refundcolumns 
	});
	$.ajax({
	       url : basePath+"inventory/invmattrandetail/queryRefundDetail.do",
	       async: false,
	       dataType: "json",
	       type:"POST",
	       data: {"imaid": imaid},
	       success:function(data){
	       		var result = data.listdata;
	           	if(null != result && "" != result){
	                var resultArr = result.split(";");
	                //追加行后立刻执行编辑操作
	                for(var i=0;i<resultArr.length;i++){
	                	var rowJ = JSON.parse(resultArr[i]);
	                	var row = {};
	                	row["opertype"]=rowJ.opertype;
	                	row["sheetno"]=rowJ.sheetno;
	                	row["createdate"]=rowJ.createdate;
	                	row["warehousename"]=rowJ.warehousename;
	                	row["remark"]=rowJ.remark;
						$("#matrefund_grid").datagrid("appendRow",row );
	                }
	           }else{
	        	   $("#matrefund_list").iFold("hide");
	           }
	           	if(data.refundable == "Y"){
	           		$("#btn_refund").hide(); //隐藏退库按钮
	           	}else{
	           		if(Priv.hasPrivilege("materiReQ_refund")){
		           		$("#btn_refund").show();
	           		}
	           	}
	       }
	   });
}


//通过列表选中物资生成领料
function listToMatApply(){
	var columns = [[
	    			{field:'itemcode',title:'物资编号',width:90,fixed:true,formatter:function(value,row){
	    				return "<a onclick='FW.showItemInfo(\""+row.itemcode+"\",\""+row.warehouseid+"\",\""+row.invcateid+"\");'>"+row.itemcode+"</a>";
	    			}},
	    			{field:'itemid',title:'物资id',width:10,fixed:true,hidden:true},
	    			{field:'itemname',title:'物资名称',width:120},
	    			{field:'cusmodel',title:'型号规格',width:200,fixed:true},
	    			{field:'warehouse',title:'仓库',width:100,fixed:true},
	    			{field:'invcate',title:'物资类别',width:100,fixed:true},
	    			{field:'nowqty',title:'可用库存',width:80,align:'right',fixed:true,formatter:function(value){
	    				return parseFloat(value).toFixed(2);
	    			}},
	    			{field:'stockqty',title:'实际库存',width:80,align:'right',fixed:true,formatter:function(value){
	    				return parseFloat(value).toFixed(2);
	    			}},
	    			{field:'qtyApply',title:'申领数量',width:80,align:'right',fixed:true,edit:true,
	    				editor:{
	    					type:'text',
	    					options:{
	    						align:"right",
	    						dataType:"number",
	    						onBlur:dynaCalcTotalPrice
	    					}
	    				}
	    			},
	    			{field:'outstockqty',title:'出库量',hidden:true},
	    			{field:'outqty',title:'已领料数量',width:80,fixed:true,hidden:true},
	    			{field:'unit1',title:'单位',width:50,fixed:true},
	    			{field:'unitCode1',title:'单位编码',fixed:true,hidden:true},
	    			{field:'price',title:'单价(元)',width:80,align:'right',fixed:true,
	    				formatter:function(value){
		    				return parseFloat(value).toFixed(2);
		    			}	
	    			},
	    			{field:'totalprice',title:'小计(元)',width:90,fixed:true,hidden:true},
	    			{field:'status',title:'状态',width:90,fixed:true,hidden:true},
	    			{field:'warehouseid',title:'仓库编码',fixed:true,hidden:true},
	    			{field:'invcateid',title:'类型id',fixed:true,hidden:true},
	    			{title:'',align:'center',field:'del',width:40,fixed:true,formatter:function(value,row){
	    				return "<img class='btn-delete btn-garbage' onclick='delRecord(\""+row.itemid+"\",\""+row.itemname+"\");' src='"+basePath+"img/inventory/btn_garbage.gif'/>";
	    			}}
	    	]];
	
	 var totalP = 0.0;
	 var totalPrices = 0.0;
	 $("#autoform").iForm("init",{"fields":new_form,"options":{validate:true}});
	 $("#matapplydetail_list").iFold("init");
	 $('#matapplydetail_grid').datagrid({       
         singleSelect:true,
         columns:columns,
         idField:"itemid",
		 fitColumns:true
     });
	
	$.ajax({
       url : basePath+"inventory/invmatapplydetail/queryMatApplyDetailItems.do",
       async: false,
       dataType: "json",
       type:"POST",
       data: {"codes":codes },
       success:function(data){
       		var result = data.result;
           	if(null != result && "" != result){
                var resultArr = result.split(";");
                //追加行后立刻执行编辑操作
                for(var i=0;i<resultArr.length;i++){
                	var rowJ = JSON.parse(resultArr[i]);
                	var row = {};
                	
                	totalP = parseFloat(rowJ.price).toFixed(2);
                	
					row["tmpid"]=i+1;
					row["itemid"] = rowJ.itemid;
					row["itemcode"] = rowJ.itemcode;
					row["itemname"] = rowJ.itemname;
					row["cusmodel"] = rowJ.cusmodel;
					row["invcate"] = rowJ.invcate;
					row["qtyApply"] = '1';
					row["stockqty"] = rowJ.stockqty;
					row["nowqty"] = rowJ.nowqty;
					row["unit1"] = rowJ.unit1;
					row["price"] = totalP;//后台代码已根据站点进行设置，含税或不含税单价
					row["totalprice"] = totalP;
					row["outqty"] = '0';
					row["outqtytemp"] = '';
					row["outstockqty"] = '0';
					row["warehouseid"] = rowJ.warehouseid;
					row["invcateid"] = rowJ.invcateid;
					row["warehouse"] = rowJ.warehouse;
					$("#matapplydetail_grid").datagrid("appendRow",row );
					
					totalPrices = parseFloat(totalPrices*1 + totalP*1).toFixed(2);
                }
                var forOrg = mvcService.getUser().orgs;
				if(forOrg.length>0){
					var deptName = mvcService.getUser().orgs[0].name;
				}else{
					var deptName = "该账户不属于任何部门";
				}
            	var total = {totalPrice:totalPrices,dept:deptName};
            	$("#autoform").iForm("setVal",total);
                
				startEditAll();
		 		$("#btn_add").text("继续添加物资");
           }
       },
       error: function(data) {
    	   FW.error(data.responseJSON.msg);
       }
   });
}

function initMatApplyList(){
	$("#invmatapply_grid").iDatagrid("init",{
		singleSelect:true,
		pageSize:pageSize,
		url: basePath+"inventory/invmatapply/queryMatApplyList.do",
		onLoadSuccess:function(data){
			if(isSearchMode){
            	 if(data && data.total==0){
            		 $("#noSearchResult").show();
            	 }
            	 else{
            		 $("#noSearchResult").hide();
            	 }
            }
			else{
	            if(data && data.total==0){
	                $("#grid_wrap,#toolbar_wrap").hide();
	                $("#grid_error").show();
	            }else{
	            	$("#toolbar_wrap,#grid_wrap").show();
	                $("#grid_error").hide();
	            }
	            $("#noSearchResult").hide();
			}
			isSearchMode = false;
			setTimeout(function(){ 
				$("#invmatapply_grid").datagrid("resize"); 
			},200);
		},
		onDblClickRow : function(rowIndex, rowData) {
			var url = basePath+ "inventory/invmatapply/invMatApplyForm.do?imaid="+rowData.imaid;
	    	var prefix = rowData.imaid;
		    FW.addTabWithTree({
		        id : "editMatApplyForm" + prefix,
		        url : url,
		        name : "物资领料",
		        tabOpt : {
		            closeable : true,
		            afterClose : "FW.deleteTab('$arg');FW.activeTabById('stock');FW.getFrame(FW.getCurrentTabId()).refCurPage();"
		        }
		    });
		}
	});
}
