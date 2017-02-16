function applyTypeChange(val){
    if(val == 'wo_picking' || val == 'itsm_picking'){
    	$("#autoform").iForm("show",["workOrderNo"]);
    	if(processStatus == "first" || processStatus == "first_save"){
    		new_form[3].render = workOrderNoSearch(val);
		}
    }else{
    	$("#autoform").iForm("hide",["workOrderNo"]);
    }
}

//编辑列表中所有的行
function startEditAll(){
	var rows = $("#matapplydetail_grid").datagrid("getRows");
	for(var i=0;i<rows.length;i++){
		var qtyApply = rows[i].qtyApply;
		var outqty = rows[i].outqty;
		if(qtyApply>outqty){
			$("#matapplydetail_grid").datagrid("beginEdit",i);
		}
	}
}

//关闭编辑列表
function endEditAll(){
	var rows = $("#matapplydetail_grid").datagrid("getRows");
	for(var i=0;i<rows.length;i++){
		$("#matapplydetail_grid").datagrid("endEdit",i);
	}
}

//计算总额
function caluPrice(){
	endEditAll();
	var listData =$("#matapplydetail_grid").datagrid("getRows");
	var totalPrices = 0;
	var totalNoTaxPrices = 0;
	
	for(var i=0;i<listData.length;i++){
		totalPrices = parseFloat(totalPrices*1+listData[i].totalprice*1);
		totalNoTaxPrices = parseFloat( totalNoTaxPrices*1 + listData[i].totalNoTaxPrice*1 );
	}
	var total = {totalPrice:totalPrices, totalNoTaxPrice:totalNoTaxPrices};
	$("#autoform").iForm("setVal",total);
}

//检查是否全部都已经出库
function checkAllOutStock(){
	var listData =$("#matapplydetail_grid").datagrid("getRows");
	var outstockqty = 0;
	var count = 0;
	for(var i=0;i<listData.length;i++){
		outstockqty = parseFloat(listData[i].outstockqty);
		if(outstockqty == 0){
			count++;
		}
	}
	if(count == listData.length){
		return true;
	}else{
		return false;
	}
}

//验证此次出库是否已经完全出库
function checkSubmitIsAllOut(){
	var listData =$("#matapplydetail_grid").datagrid("getRows");
	var outstockqty = 0;
	var waitqty = 0;
	var count = 0;
	for(var i=0;i<listData.length;i++){
		waitqty = parseFloat(listData[i].waitqty);
		outstockqty = parseFloat(listData[i].outstockqty);
		if(outstockqty == waitqty){
			count++;
		}
	}
	if(count == listData.length){
		return true;
	}else{
		return false;
	}
}

//编辑模式下计算总价
function dynaCalcTotalPrice(){
	var rows = $("#matapplydetail_grid").prev(".datagrid-view2").children(".datagrid-body").find(".datagrid-row");
	var cnt = 0;
	var stk = 0;
	var totalPrices = 0;
	var totalNoTaxPrices = 0;
	for(var i=0;i<rows.length;i++){
		var row = $(rows[i]);
		if(processStatus == "last"){
			cnt = row.children("td[field='outqty']").find("input").val();
		}else{
			cnt = row.children("td[field='qtyApply']").find("input").val();
			stk = row.children("td[field='nowqty']").find("div").text();
		}
		cnt = parseFloat(cnt);
		stk = parseFloat(stk);
		var price = row.children("td[field='price']").children("div").html();
		price = parseFloat(price);
		if(isNaN(cnt) || isNaN(price)){
			totalPrices = "非法的输入";
			return;
		}
		if( (typeof(siteid) != "undefined") && ("SJW" == siteid) ){
			var noTaxPrice;
			noTaxPrice = row.children("td[field='noTaxPrice']").children("div").html();
			noTaxPrice = parseFloat(noTaxPrice);
			if(isNaN(cnt) || isNaN(noTaxPrice)){
				totalNoTaxPrices = "非法的输入";
				break;
			}
			var rowTotalPrice = cnt*price;
			var rowTotalNoTaxPrice = cnt*noTaxPrice;
			row.children("td[field='totalprice']").children("div").html(rowTotalPrice.toFixed(2));
			row.children("td[field='totalNoTaxPrice']").children("div").html(rowTotalNoTaxPrice.toFixed(2));
			totalPrices += rowTotalPrice;
			totalNoTaxPrices += rowTotalNoTaxPrice;
		}
		else{
			var rowTotalPrice = cnt*price;
			totalPrices += rowTotalPrice;
		}
		if(cnt > stk){
			FW.error( "申请数量超出可用库存上限");
			break;
		}
	}
	totalPrices = parseFloat(totalPrices).toFixed(2);
	totalNoTaxPrices = parseFloat(totalNoTaxPrices).toFixed(2);
	var total = {totalPrice:totalPrices, totalNoTaxPrice: totalNoTaxPrices};
	$("#autoform").iForm("setVal",total);
}

//关闭当前tab
function closeCurTab(){
	homepageService.refresh();
	FW.deleteTabById(FW.getCurrentTabId());
	FW.activeTabById("stock");
}

//重置Tab
function repositionTab(){
	var tab = _parent().$(".itcui-navigation-ul").find("span.navitem-name");
	var activeTabId = FW.getCurrentTabId();
	for(var i=0;i<tab.length;i++){
		if("发放物资" == tab[i].innerText || "物资发料" == tab[i].innerText){
			var id = tab[i].parentNode.id;
			var idArr = id.split("itcui_nav_tab_");
			FW.deleteTabById(idArr[1]);
			FW.activeTabById(activeTabId);
		}
	}
}

//初始化打印按钮
function initPrint(){
	var outterListSize =$("#matapplyout_grid").datagrid("getRows").length;
	if(outterListSize>0){
		$("#btn_print").show();
		var outterListData =$("#matapplyout_grid").datagrid("getSelected");
		if(null!=outterListData && ""!=outterListData){
			printData(outterListData);
		}else{
			outterListData = $('#matapplyout_grid').datagrid('getSelected',$('#matapplyout_grid').datagrid('selectRow',0));
			printData(outterListData);
		}
		FW.fixToolbar("#toolbar");
	}
}

//实现打印
function printData(outterListData){
	var sheetNo = outterListData["sheetno"];
	$.ajax({
		type : "POST",
		url: basePath + "inventory/invmatrecipients/queryRecipientsIdBySheetNo.do",
		async : false,
		data: {"sheetNo":sheetNo},
		dataType : "json",
		success : function(data) {
			var url = null;
			if(data.imrid=='' || null == data.imrid){
				url = fileExportPath+"preview?__format=pdf&__report=report/TIMSS2_IMA_001_pdf.rptdesign&siteid="+siteid+"&imaid="+imaid;
	        }else{
	        	var imrid = data.imrid;
	        	if('ZJW' == siteid){
	        		url = fileExportPath+"preview?__format=pdf&__report=report/TIMSS2_ZJW_LINGLIAO_001_pdf.rptdesign&siteid="+siteid+"&imaid="+imaid;
	        	}else if('ITC' == siteid){
	        		url = fileExportPath+"preview?__format=pdf&__report=report/TIMSS2_ITC_LINGLIAO_001_pdf.rptdesign&siteid="+siteid+"&imaid="+imaid;
	        	}else if('SWF' == siteid){
	        		url = fileExportPath+"preview?__format=pdf&__report=report/TIMSS2_SWF_LINGLIAO_001_pdf.rptdesign&siteid="+siteid+"&imaid="+imaid;
	        	}else{
	        		url = fileExportPath+"preview?__format=pdf&__report=report/TIMSS2_LINGLIAO_001_pdf.rptdesign&siteid="+siteid+"&imaid="+imaid;
	        	}
	        }
			FW.initPrintButton("#btn_print",url,"领料打印预览",null);
		}
	});
}

//初始化打印按钮
function initPrinBtn(){
	var currentUserId = Priv.secUser.userId;
	if('ZJW' == siteid){
		url = fileExportPath+"preview?__format=pdf&__report=report/TIMSS2_ZJW_LINGLIAO_001_pdf.rptdesign&siteid="+siteid+"&imaid="+imaid + "&author=" + currentUserId;
	}else if('ITC' == siteid){
		url = fileExportPath+"preview?__format=pdf&__report=report/TIMSS2_ITC_LINGLIAO_001_pdf.rptdesign&siteid="+siteid+"&imaid="+imaid + "&author=" + currentUserId;
	}else if('SWF' == siteid){
		url = fileExportPath+"preview?__format=pdf&__report=report/TIMSS2_SWF_LINGLIAO_001_pdf.rptdesign&siteid="+siteid+"&imaid="+imaid + "&author=" + currentUserId;
	}else{
		url = fileExportPath+"preview?__format=pdf&__report=report/TIMSS2_LINGLIAO_001_pdf.rptdesign&siteid="+siteid+"&imaid="+imaid + "&author=" + currentUserId;
	}
	FW.initPrintButton("#btn_print",url,"领料打印预览",null);
}

//选择工单编码
function workOrderNoSearch(obj){
	//放大镜图标属于基础资源，可以直接引用
    $("#f_workOrderNo").attr("icon","itcui_btn_mag").removeClass("form-control").ITCUI_Input();
    $("#f_workOrderNo").next(".itcui_input_icon").on("click",function(){
    	if(obj == 'itsm_picking'){
    		FW.showItsmWOListDialog({siteId:siteid,
    			onParseData : function(data){
    				$("#autoform").iForm("setVal",{"workOrderNo":data[0].workOrderCode});
    			}
    		});
    	}else{
    		FW.showWorkOrderListDialog({siteId:siteid,
    			onParseData : function(data){
    				$("#autoform").iForm("setVal",{"workOrderNo":data[0].workOrderCode});
    			}
    		});
    	}
	});
}

//刷新领料单页面数据（FW.showRecipientPage关闭后触发）
function refreshPage(){
	loadOutListData("reload");
}

//刷新发料单列表
function loadOutListData(type){
	$.ajax({
    	url : basePath+"inventory/invmatrecipients/queryInvMatRecipientsApplyList.do",
       	async: false,
       	dataType: "json",
       	type:"POST",
       	data: {"imaid": imaid},
       	success:function(data){
       		if(data && data.rows.length>0){
       			var outListData = $("#matapplyout_grid").datagrid("getRows");
       			var oldCount = 0;
       			for(var i=0; i<outListData.length; i++){
       				if( "Y" == outListData[i].status ){
       					oldCount++;
       				}
       			}
       			var newOutListData = data.rows;
       			var newCount = 0;
      			for(var i=0; i<newOutListData.length; i++){
       				if( "Y" == newOutListData[i].status ){
       					newCount++;
       				}
       				else{
       					//具有按钮权限的用户，只要还有一张发料单未发料，则显示“终止领料”按钮。
       					if(Priv.hasPrivilege("materiReQ_stopsend")){
           					$("#btn_stopsend").show();
       					}
       				}
       			}
      			//新建页面只加载数据
      			if("init" == type){
           			$("#matapplyout_grid").datagrid("loadData", data.rows);
      			}
      			else{
      				//关闭发料单页面会触发当前函数，如果新的发料列表和旧的发料列表不一致，则刷新领料单页面。
           			if( newCount != oldCount ){
           				FW.getFrame(FW.getCurrentTabId()).location.reload();
           			}
      			}
       		}
       		else{
   				$("#matapplyout_list").iFold("hide");
   				$("#btn_refund").hide();
       		}
       	}
   	});		
}