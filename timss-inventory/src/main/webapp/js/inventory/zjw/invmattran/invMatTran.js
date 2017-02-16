//异步返回物资类型中文(停用)
/*function queryItemType(data){
	TimssService.getEnum("INV_OPERATION_TYPE", function(Object){
		var datalist = Object.INV_OPERATION_TYPE;
		for(var i=0;i<datalist.length;i++){
			if(datalist[i].code == data){
				$("#f_tranType").val(datalist[i].label);
				break;
			}
		}
	});
}*/

//编辑列表中所有的行
function startEditAll(){
	var rows = $("#mattrandetail_grid").datagrid("getRows");
	for(var i=0;i<rows.length;i++){
		$("#mattrandetail_grid").datagrid("beginEdit",i);
	}
}

//关闭编辑列表
function endEditAll(idx){
	if(null != idx){
		$("#mattrandetail_grid").datagrid("endEdit",idx);
	}else{
		var rows = $("#mattrandetail_grid").datagrid("getRows");
		for(var i=0;i<rows.length;i++){
			$("#mattrandetail_grid").datagrid("endEdit",i);
		}
	}
}

//计算总额
function validateRevice(){
	var listData =$("#mattrandetail_grid").datagrid("getRows");
	if(listData.length>0){
		for(var i=0;i<listData.length;i++){
			endEditAll(i);
			if(flagCounter>0 && ''!=imtid){
				$("#mattrandetail_grid").datagrid("reload");
				$("#mattrandetail_grid").datagrid("resize"); 
				break;
			}
		}
	}else{
		FW.error( "入库前请选择物资 ");
		flagCounter++;
	}
}

//动态计算总价
function dynaCalcTotalPrice(){
	var rows = $("#mattrandetail_grid").prev(".datagrid-view2").children(".datagrid-body").find(".datagrid-row");
	var totalPrices = 0;
	var price = 0;
	var bestockqty = 0;
	var rowTotal = 0;
	for(var i=0;i<rows.length;i++){
		var row = $(rows[i]);
		price = row.children("td[field='price']").find("div").text();
		price = parseFloat(price);
		
		bestockqty = row.children("td[field='bestockqty']").find("input").val();
		if(bestockqty == undefined){
			bestockqty = row.children("td[field='bestockqty']").find("div").html();
		}else if("" == bestockqty){
			bestockqty = 0;
		}
		
		bestockqty = parseFloat(bestockqty);
		if(isNaN(price) || isNaN(bestockqty)){
			totalPrices = "非法的输入";
			break;
		}
		
		rowTotal = price * bestockqty;
		totalPrices += rowTotal;
		
		row.children("td[field='totalprice']").children("div").html(_toFixed(rowTotal,2));
	}
	var total = {totalPrice:totalPrices.toFixed(2)};
	$("#autoform").iForm("setVal",total);
}

//采购单号查询
function purOrderNoSearch(id){
	//放大镜图标属于基础资源，可以直接引用
	$("#" + id).attr("icon","itcui_btn_mag").removeClass("form-control").ITCUI_Input();
	$("#" + id).next(".itcui_input_icon").on("click",function(){
		FW.dialog("init",{
			src: basePath+"inventory/invmattran/purOrderNoList.do",
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
		                var info = p.getPurOrderNos();
		                var datas = info.split(","); 
		                var noArr = [];
		                var nameArr = [];
		                var typeArr = [];
		                if(datas.length>0){
		                	for(var i=0;i<datas.length;i++){
		                		var data = datas[i].split("||");
		                		noArr.push(data[0]);
		                		nameArr.push(data[1]);
		                		typeArr.push(data[2]);
		                	}
		                	var noArrString = noArr.join(",");
		                	var typeArrString = typeArr.join(",");
		                	if(null != info && "" != info){
			                	$("#autoform").iForm("setVal",{
			                							"pruorderno":noArrString,
			                							"remark" : "选中采购合同为："+nameArr.join("+"),
			                							"applyTypeName":typeArrString
			                							});
	                        }
		                	//加载添加物资的列表
		                	$.post(basePath+"inventory/invitem/queryArrivalItem.do",{"pruordernoVal":noArrString},
		                			function(data){
				                		var listData =$("#mattrandetail_grid").datagrid("getRows");
				            			for(var z=listData.length-1;z>=0;z--){
				            				$("#mattrandetail_grid").datagrid("deleteRow",z);
				            			}
		                				//数据填充
			                		  	for(var i=0;i<data.rows.length;i++){
			                		  		data.rows[i].bestockqty = 0;
			                		  		data.rows[i].lotno = 0 ;  //默认接收的批次为0
						                	$("#mattrandetail_grid").datagrid("appendRow",data.rows[i]);
						                }
			                		    startEditAll();
						                dynaCalcTotalPrice();
		                				_parent().$("#itcDlg").dialog("close"); 
		                			},"json");
		                }
		            }
		        }],
			dlgOpts:{ width:800, height:520, closed:false, title:"采购合同查询", modal:true }
				});
		 });
}

//关闭当前选项卡
function closeCurTab(){
	homepageService.refresh();
	FW.deleteTabById(FW.getCurrentTabId());
	FW.activeTabById("stock");
}

//按键event
function keyPressEvent(){
	$("#quickSearchInput").keypress(function(e) {
	    if(e.which == 13) {
	        initTranList();
	    }
	});
	$("#quickSearchInput").iInput("init",{
		"onClickIcon":function(){
			initTranList();
		}
	});
}
