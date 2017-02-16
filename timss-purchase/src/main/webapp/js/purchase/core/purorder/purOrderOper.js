//提交按钮（通用方法）
function commitApply(obj){
	var type = obj.type;
	var listData = null;
	
	if (type == "submit") {
		$("#btn-submit").button('loading');
	}
	
	if(!$("#autoform").valid()){
		$("#btn-submit").button('reset');
		return ;
	}
	var formData =$("#autoform").ITC_Form("getdata");
	
	//沙C多经的特殊判断（先屏蔽）
	if(siteId == 'SJW' && !$("#orderitemform").valid()){
		return;
	}else{
		endEditAll();
		listData = $("#order_item").datagrid("getRows");
	}
	
	if(processStatus == "first" || processStatus == "first_save"){
		startEditAll();
	}
	
	if(type=="submit"&&listData.length == 0){
		FW.error( "请选择物资后再提交 ");
		$("#btn-submit").button('reset');
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
						workFlow.submitApply(taskId,null,submitCallBack,cancelCallBack,0);
					}else{
						if(processStatus == "last"){
							autoCommitProcess();
						}else{
							workFlow.showAudit(taskId,null,submitCallBack,pageClose,stopProcess,null,0,cancelCallBack);
						}
					}
				}else{
					startEditAll();
					FW.success("暂存成功 ");
				}
			}else{
				if (type == "submit") {
					FW.error("提交失败");
					$("#btn-submit").button('reset');
				}else{
					FW.error("暂存失败 ");
				}
			}
		}
	});
}

//删除记录
function delRecord(tmpid){
	Notice.confirm("删除？|确定删除所选项吗？该操作无法撤消。",function(){
		var rowData = $('#order_item').datagrid('getRows');
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
	        		{field:'invcateid',title:'物资分类id',hidden:true},
	        		{field:'warehouse',title:'仓库名称',width:125,fixed:true},
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
		row["warehouseid"] = dataList[i]["warehouseid"];
		row["invcateid"] = dataList[i]["invcateid"];
		row["warehouse"] = dataList[i]["warehouse"];
		row["priceTotal"] = dataList[i]["priceTotal"];
		row["remark"] = dataList[i]["remark"];
		row["applySheetId"] = dataList[i]["applySheetId"];
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
	repositionTab();
}

//手工生成表单
function newOrderForm(){
	//列表初始化
	initList();
	FW.privilegeOperation("exampurch_listAllowEdit","order_item");
	FW.privilegeOperation("exampurch_btnAllowEdit",null);
	/******************************表单初始化 begin***********************************/
	//判断用户是新建操作还是新建操作
	if('edit' == type){
		editForm(edit_form);
		$("#autoform").ITC_Form("readonly");
		if("editable"==isEdit){
			checkFormComboEditable();
		}
		FW.showVendorInfo("companyNo","companyName");
		
		$("#btn-submit").hide();
		$("#btn-save").hide();
		$("#btn-delete").hide();
		if(processStatus == "first_save"&&"editable"==isEdit){
			$("#pageTitle").html("编辑采购合同");
		}else{
			$("#pageTitle").html("采购合同详情");
		}
		$("#order_item").datagrid("hideColumn","del");
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
	}
}