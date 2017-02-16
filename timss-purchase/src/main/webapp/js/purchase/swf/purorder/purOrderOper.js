var columns_receipt = [[
							{field:'sheetNo',width:120,title:'编号'},
	    	        		{field:'supplier',width:90,title:'开票方'},
	    	        		{field:'invoiceNo',width:120,title:'发票编号'},
	    	        		{field:'noTaxSumPrice',width:120,fixed:true,title:'不含税金额(元)'},
	    	        		{field:'invoiceCreateDate',width:120,fixed:true,title:'开票日期',formatter:function(value){
	    	        			return value==null||value==""?"":new Date(value).format("yyyy-MM-dd");
	    	        		}},
	    	        		{field:'status',width:100,fixed:true,title:'状态'}
	    	        	]];
//提交按钮（通用方法）
function commitApply(obj){
	var type = obj.type;
	var listData = null;
	var policyList = null;
	if (type == "submit") {
		$("#btn-submit").button('loading');
	}
	if (type =="submit_no"){
		$("#btn-submit-no").button('loading');
	}
	if(!$("#autoform").valid()){
		$("#btn-submit").button('reset');
		$("#btn-submit-no").button('reset');
		return ;
	}
	//提交时校验自定义编号
	var spNo = $("#autoform").iForm("getVal","spNo");
	var isValid = true;
	if(""!=spNo){
		$.ajax({
			type : "POST",
			url: basePath+"purchase/purorder/isSpNoExisted.do",
			async : false,
			data: {
				spNo: function() {
					return spNo;
        		},
        		sheetId:function(){
        			if(sheetId){
        				return sheetId;
        			}
        			return "";
        		}
			},
			dataType : "json",
			success : function(data) {
				if("true" == data["isExisted"]){
					FW.error("合同编号已经存在");
					isValid = false;
	    		}
			}
		})
	}
	if(!isValid||!$("#autoform").valid()){
		$("#btn-submit").button('reset');
		$("#btn-submit-no").button('reset');
		return false;
	}
	var formData =$("#autoform").ITC_Form("getdata");
	//沙C多经的特殊判断（先屏蔽）
	if(siteId == 'SJW' && !$("#orderitemform").valid()){
		return;
	}else{
		endEditAll();
		listData = $("#order_item").datagrid("getRows");
		policyList = getContractDetailListInputs("contract_itemInfoGrid");
	}
	startEditAll();
	if((type=="submit"|| type=="submit_no")&&listData.length == 0){
		FW.error( "请选择物资后再提交 ");
		$("#btn-submit").button('reset');
		$("#btn-submit-no").button('reset');
		return;
	}
	//获取附件
	var ids = $("#uploadform").ITC_Form("getdata");
	uploadIds=JSON.stringify(ids.uploadField);
	//清掉那些可能出现中文或换行符且用不着的字段
	var listDataContent = FW.parse(FW.stringify(listData));
	for(var i =0;i<listDataContent.length;i++){
		listDataContent[i]["itemname"]="";
		listDataContent[i]["itemcus"]="";
	}
	//加载用户表单数据
	$.ajax({
		type : "POST",
		url: basePath+"purchase/purorder/commitApply.do",
		data: {
			"formData":FW.stringify(formData),
			"listData":FW.stringify(listDataContent),
			"policyList":FW.stringify(policyList),
			"type":type,
			"sheetId":sheetId,
			"taskId":taskId,
			"uploadIds":uploadIds,
			"delFlag":delFlag
			},
		dataType : "json",
		success : function(data) {
			saveFlag = true;
			taskId = data.taskId;
			sheetId = data.sheetId;
			processInstId = data.processInstanceId;
			$("#f_sheetno").val(data.sheetNo);
			if( data.result == "success" ){
				if(type=="submit"){
		        	var workFlow = new WorkFlow();
					if(processStatus == "first" || processStatus == "first_save"){
						//先在流程变量中设置一个值
					    var url = basePath + '/workflow/process_inst/setVariables.do';
					    var params={};
					    params['processInstId'] = processInstId;
					    var variables = [{'name':'nosubmit','value':'N'},{'name':'apply','value':Priv.secUser.userId}];
					    params['variables'] = JSON.stringify(variables);
					    $.post(url,params,function(data){
					        if(data.result=='ok'){
								workFlow.submitApply(taskId,null,submitCallBack,cancelCallBack,0);
					        }
					    }); 
					}else{
						if(processStatus == "last"){
							autoCommitProcess();
						}else{
							workFlow.showAudit(taskId,null,submitCallBack,pageClose,stopProcess,null,0,cancelCallBack);
						}
					}
				}else if (type=="save"){
					startEditAll();
					FW.success("暂存成功 ");
				}else{
					//先在流程变量中设置一个值
				    var url = basePath + '/workflow/process_inst/setVariables.do';
				    var params={};
				    params['processInstId'] = processInstId;
				    var variables = [{'name':'nosubmit','value':'Y'},{'name':'apply','value':Priv.secUser.userId}];
				    params['variables'] = JSON.stringify(variables);
				    $.post(url,params,function(data){
				        if(data.result=='ok'){
				        	var workFlow = new WorkFlow();
				        	workFlow.submitApply(taskId,null,submitCallBack,cancelCallBack,0);
				        }
				    }); 
				}
			}else{
				if (type == "save") {
					FW.error("暂存失败 ");
				}else{
					FW.error("提交失败");
					$("#btn-submit").button('reset');
					$("#btn-submit-no").button('reset');
				}
			}
		}
	});
}

//删除记录
function delRecord(tmpid){
	Notice.confirm("删除？|确定删除所选项吗？该操作无法撤消。",function(){
		var rowData =$("#order_item").datagrid("getRows");
		if(0<rowData.length){
			for( var i = 0 ; i<rowData.length; i++ ){
				if(tmpid==rowData[i].listId){
					deleteItems.push(rowData[i].itemid+'_'+rowData[i].invcateid+'_'+rowData[i].sheetno);
					break;
				}
			}
		}
		$('#order_item').datagrid('deleteRow',$('#order_item').datagrid('getRowIndex',tmpid));		
		var listData =$("#order_item").datagrid("getRows");
		if(listData.length == 0){
			$("#btn-delete").attr("disabled",true);
			$("#btn-add").text("添加物资");
		}else{
			$("#btn-add").text("继续添加物资");
		}
		delFlag = true;
		resetSelectAttr();
		dynaCalcTotalPrice();
		checkFormComboEditable();
	},null,"info");
}

//供应商查询
function companySearch(id){
 	//放大镜图标属于基础资源，可以直接引用
    $("#" + id).attr("icon","itcui_btn_mag").removeClass("form-control").ITCUI_Input();
    $("#" + id).next(".itcui_input_icon").on("click",function(){
	FW.dialog("init",{
		src: basePath+"purchase/purorder/purOrderCompanyList.do",
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
	                var info = p.getCompanyIds();
	                if(null != info && "" != info){
	                	var infoArr = info.split(",");
	                	if(infoArr.length>0){
		                	for(var i=0;i<infoArr.length;i++){
		                		var companyArr = infoArr[i].split("_");
		                		if(i == 0){
		                			$("#autoform").iForm("setVal",{"companyNo":companyArr[0]});
                            		$("#autoform").iForm("setVal",{"companyName":companyArr[1]});
		                		}
		                	}
	                	}
                    }
                    _parent().$("#itcDlg").dialog("close");
	            }
	        }],
		dlgOpts:{ width:800, height:520, closed:false, title:"公司查询", modal:true }
		});
	 });
 }
//自动生成表单
function autoGenerateForm(){
	var columns = [[
	                {field:'listId',title:'多类型列表id',hidden:true},
	        		{field:'itemid',width:90,fixed:true,title:'物资编号',formatter:function(value,row){
	        			return "<a onclick='FW.showItemInfo(\""+row.itemid+"\",\""+row.warehouseid+"\");'>"+row.itemid+"</a>";
	        		}},
	        		{field:'applySheetId',width:10,title:'sheetId',hidden:true},
	        		{field:'itemname',width:90,title:'物资名称'},
	        		{field:'itemcus',width:90,title:'型号规格'},
	        		{field:'warehouseid',title:'仓库id',hidden:true},
	        		{field:'warehouse',title:'仓库名称',width:125,fixed:true},
	        		{field:'sheetno',width:120,title:'采购申请编号',formatter:function(value,row){
	        			return "<a onclick='viewPurApply(\""+row.sheetno+"\");'>"+row.sheetno+"</a>"}},
	        		{field:'createUserName',width:90,title:'采购人'},
	        		{field:'itemnum',width:60,fixed:true,title:'采购量',edit:true,
	        			editor:{
	        				type:'label',
	        				options:{
	        					align:"right",
	        					dataType:"number",
	        					onBlur:dynaCalcTotalPrice
	        				}
	        			}
	        		},
	        		{field:'orderunitname',width:45,fixed:true,title:'单位'},
	        		{field:'averprice',width:90,fixed:true,title:'不含税价(元)'},
	        		{field:'tax',width:60,fixed:true,title:'税费(元)',hidden:true},
	        		{field:'taxRate',width:70,fixed:true,title:'税率(%)',
	        		 editor:{
	        				type : 'combobox',
	        				options : {
	        					data : FW.parseEnumData("PUR_ORDER_TAXRATE",_enum),
	        					rules:{required:true},
	        					initOnChange: false,
	        					onChange:function(val,obj){
	        						singleSelectControlCombox(val,obj);
	        					}
	        				}
	        		 },
	        		 formatter:function(value){
	        			var data=FW.parseEnumData("PUR_ORDER_TAXRATE",_enum);
	        			var r="";
	        			for(var i in data){
	        				if(data[i][0]==value){
	        					r=data[i][1];
	        					break;
	        				}
	        			}
	        			return r;
	        		}},
	        		{field:'cost',width:70,fixed:true,title:'单价(元)',edit:true,
	        			editor:{
	        				type:'text',
	        				options:{
	        					align:"right",
	        					dataType:"number",
	        					onBlur:dynaCalcTotalPrice
	        				}
	        			}
	        		},
	        		{field:'priceTotal',width:70,fixed:true,title:'小计(元)'},
	        		{field:'remark',width:100,fixed:true,edit:true,editor:{type:'text'},title:'备注'},
	        		{title:'',align:'center',field:'del',width:40,fixed:true,formatter:function(value,row){
	        			return "<img class='btn-delete btn-garbage' onclick='delRecord(\""+row.listId+"\");' src='"+basePath+"img/purchase/btn_garbage.gif'/>";
	        		}}
	        	]];
	var columns_receipt = [[
	    	        		{field:'supplier',width:90,title:'开票方'},
	    	        		{field:'invoiceNo',width:120,title:'发票编号'},
	    	        		{field:'noTaxSumPrice',width:70,fixed:true,title:'不含税金额(元)'},
	    	        		{field:'invoiceCreateDate',width:70,fixed:true,title:'开票日期'},
	    	        		{field:'status',width:70,fixed:true,title:'状态'}
	    	        	]];
	defKey = "purchase_"+siteId+"_purorder";
	$("#btn-edit").hide();
	$("#pageTitle").html("新建采购合同");
    $("#btn-add").text("继续添加物资");
    $("#order_item").datagrid({
    	singleSelect:true,
		fitColumns : true,
		idField:'listId',
		columns:columns,
		onAfterEdit:function(rowIndex, rowData){
			caluRow(rowData,rowIndex);
		}
    });
    FW.privilegeOperation("exampurch_listAllowEdit","order_item");
	var dataList = JSON.parse(poListData);
	var size = dataList.length;
	for(var i=0;i<size;i++){
		var row = {};
		row["listId"] = dataList[i]["listId"]+"_"+dataList[i]["applySheetId"];
		row["itemid"] = dataList[i]["itemid"];
		row["itemname"] = dataList[i]["itemname"];
		row["itemcus"] = dataList[i]["itemcus"];
		row["itemnum"] = dataList[i]["itemnum"];
		row["orderunitname"] = dataList[i]["orderunitname"];
		row["averprice"] = dataList[i]["averprice"];
		row["tax"] = dataList[i]["tax"];
		row["cost"] = dataList[i]["cost"];
		row["priceTotal"] = dataList[i]["priceTotal"];
		row["remark"] = dataList[i]["remark"];
		row["warehouseid"] = dataList[i]["warehouseid"];
		row["warehouse"] = dataList[i]["warehouse"];
		row["applySheetId"] = dataList[i]["applySheetId"];
		row["sheetno"] = dataList[i]["sheetno"];
		row["createUserName"] = dataList[i]["createUserName"];
		$("#order_item").datagrid("appendRow",row );
	}
	$("#autoform").ITC_Form({validate:true,fixLabelWidth:true},new_form);
	//供应商输入框渲染
	new_form[2].render = companySearch("f_companyName");
	var loaddata = JSON.parse(poData);
	$("#autoform").iForm("setVal",loaddata);
	startEditAll();
	$("#order_item").datagrid("showColumn","del");
	$("#btn-doingStatus").hide();
	$("#foldable_area_accept").iFold("hide");
	repositionTab();
	//补充自定义合同条款
	//1.合同基本信息
	//2.合同条款详情以及发票信息
	/*
	$.ajax({
		type : "POST",
		url: basePath+"purchase/purorder/queryPurOrderPolicyAndReceipt.do",
		data: {
			"sheetId":sheetId
		},
		dataType : "json",
		success : function(data) {
			//初始化合同条款
			initContractDetailsListByData("contract_itemInfoGrid",data.contractDetailList);
			//开放编辑
			beginEditContractItemList("contract_itemInfoGrid");
			//初始化发票列表
			$("#order_receipt").datagrid({
		    	singleSelect:true,
				fitColumns : true,
				columns:columns_receipt,
				data:data.contractReceiptList,
				onLoadSuccess:function(data){
					if(data && data.total==0){
						$("#order_receiptGrid").hide();
	            		$("#noSearchResult").show();
	            		$("#foldable_area_receipt").iFold("hide");
	            	}
	            	else{
	            		$("#order_receiptGrid").show();
	            		$("#noSearchResult").hide();
	            	}
				}
		    });
		}
	});
	*/
	initContractContent();
}
var addcontractDetailHtml = '<div class="wrap-underline wrap-underline-single">' +
'<span class="safe-number-span"></span>'+
'<span class="safe-input-content" style="display: none;"></span>'+
'<input class="safe-input" type="text" onkeyup="bindNewContractItemEvent(this);"/>'+
'<img width="17" height="18" src="'+basePath+'img/purchase/btn_garbage.gif"'+
'onclick="removeContractItem(this)" style="margin-left:10px"/>'+
'</div>';
/**
 * 为新增的条款的输入栏绑定事件
 */
function bindNewContractItemEvent(obj){
	var key = window.event.keyCode;
	if(key == 13){   //回车键
		$(obj).parent().after(addcontractDetailHtml);  //在当前行后添加一行
		var index = 0;
		$(".safe-number-span").each(function(){
			index++;
			$(this).html(index);
		});
		$(obj).parent().next().find('input').focus();  //光标移动到下一行的输入框中
	}
}
/** 设置可编辑
 * @param outerDivId
 */
function beginEditContractItemList(outerDivId){
	$("#" + outerDivId).iFold("show");
	$("#" + outerDivId).find("img").show();    //垃圾箱显示
	$("#"+outerDivId).append(addcontractDetailHtml);
	$(".wrap-underline").css("paddingRight",0);
	$(".safe-input-content").hide();   //隐藏不可编辑的
	$(".safe-input").show();  //显示可编辑的
}

/** 设置不可编辑
 * @param outerDivId
 */
function endEditContractItemList(outerDivId){
	var tempdata = {};
	tempdata = getContractDetailListInputs(outerDivId);    //提取数据
	$("#"+ outerDivId).find(".wrap-underline").remove();	//先删掉html
	initContractDetailsListByData(outerDivId,tempdata);    //连带数据一起插入进去
}
/**
 * 为删除的条款的输入栏绑定事件
 */
function removeContractItem(obj){
	obj.parentNode.remove();
}
function initContractDetailsListByData(outerDivId,contractDatas){
	if(contractDatas){
		$("#" + outerDivId).iFold("show");
		var firstInd = 0;
		var secInd = 0;
		for(var j = 0 ; j < contractDatas.length;j++){
			var append = $(addcontractDetailHtml).appendTo($("#" + outerDivId));
			if(contractDatas[j].isFirstLevel){
				firstInd++;
				secInd = 0;
			}	
			$(append).find(".safe-number-span").html(j+1).show();
			$(append).find(".safe-input-content").html(contractDatas[j].policyContent).show();
			$(append).find(".safe-input").val(contractDatas[j].policyContent).hide();
			$(append).children("img").hide();   //垃圾箱隐藏
			secInd++;
		}
	}
}
function getContractDetailListInputs(outerDivId){
	var contractDetails = [];
	var inputs = $("#"+outerDivId).find("input");
	var showOrder = 1;
	for(var j = 0 ;j < inputs.length; j++){
		var tempValue = $.trim($(inputs[j]).val());
		if(tempValue){
			var contractDetail = {};
			contractDetail.policyContent = tempValue;
			contractDetail.sort = $(inputs[j]).prev().prev().html();
			contractDetails.push(contractDetail);
			showOrder++;
		}
	}
	return contractDetails;
}
//手工生成表单
function newOrderForm(){
	//列表初始化
	initList();
	var gridEditable = ""!=oper?JSON.parse(oper).editable:"";
	FW.privilegeOperation("exampurch_listAllowEdit","order_item");
	FW.privilegeOperation("exampurch_btnAllowEdit",null);
	/******************************表单初始化 begin***********************************/
	//判断用户是新建操作还是新建操作
	if('edit' == type){
		editForm(edit_form);
		$("#btn-submit").hide();
		$("#btn-save").hide();
		$("#btn-delete").hide();
		if(processStatus == "first_save"&&"editable"==isEdit){
			$("#pageTitle").html("编辑采购合同");
		}else{
			$("#pageTitle").html("采购合同详情");
		}
		$("#order_item").datagrid("hideColumn","del");
		//验收记录初始化
		initAcceptList();
	}else{
		//供应商输入框渲染
		new_form[2].render = companySearch;
		$("#autoform").ITC_Form({validate:true,fixLabelWidth:true},new_form);
		$("#autoform").iForm("setVal",{totalPrice:"0.00"});
		checkFormComboEditable();
		$("#pageTitle").html("新建采购合同");
		$("#order_item").datagrid("showColumn","del");
		$("#btn-edit").hide();
		$("#btn-doingStatus").hide();
		$("#foldable_area_accept").iFold("hide");
	}
	//补充自定义合同条款
	//1.合同基本信息
	//2.合同条款详情
	/*
	$.ajax({
		type : "POST",
		url: basePath+"purchase/purorder/queryPurOrderPolicyAndReceipt.do",
		data: {
			"sheetId":sheetId
		},
		dataType : "json",
		success : function(data) {
			initContractDetailsListByData("contract_itemInfoGrid",data.contractDetailList);
			if("editable"==isEdit&&gridEditable||'new' == type){
				beginEditContractItemList("contract_itemInfoGrid");
			}else{
				endEditContractItemList("contract_itemInfoGrid");
			}
			//初始化发票列表
			$("#order_receipt").datagrid({
		    	singleSelect:true,
				fitColumns : true,
				columns:columns_receipt,
				data:data.contractReceiptList,
				onLoadSuccess:function(data){
					if(data && data.total==0){
						$("#order_receiptGrid").hide();
	            		$("#noSearchResult").show();
	            		$("#foldable_area_receipt").iFold('hide');
	            	}
	            	else{
	            		$("#order_receiptGrid").show();
	            		$("#noSearchResult").hide();
	            	}
				}
		    });
		}
	});
	*/
	initContractContent()
	//补充付款记录(触发自动计算)
	initPurPayList();
}