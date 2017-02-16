/*************************************************采购申请*************************************************/
//申请明细列表
//申请明细列表
var applyItemColumns = [[
	{field:'ck',width:10,checkbox:true,hidden:true,fixed:true,title:'物资编号'},
	{field:'listId',title:'多类型列表id',hidden:true},
	{field:'itemid',width:90,fixed:true,title:'物资编号',formatter:function(value,row){
		return "<a onclick='FW.showItemInfo(\""+row.itemid+"\",\""+row.warehouseid+"\");'>"+row.itemid+"</a>";
	}},
	{field:'itemname',title:'物资名称',width:180},
	{field:'cusmodel',title:'型号规格',width:180,fixed:true},
	{field:'warehouseid',title:'仓库id',hidden:true},
	{field:'invcateid',title:'物资分类id',hidden:true},
	{field:'warehouse',title:'仓库名称',width:125,fixed:true},
	{field:'itemnum',width:60,fixed:true,edit:true,align:'right',
		editor:{
			type:'text',
			options:{
				align:"right",
				dataType:"digits",
				onBlur:dynaCalcTotalPrice,
				rules : {
					 "number" : true
				},
			    messages : {
					 "number" : "请输入有效数字"
			    }
			}
		},title:'申请数量'
	},
	{field:'repliednum',width:60,fixed:true,edit:true,align:'right',
		editor:{
			type:'text',
			options:{
				align:"right",
				dataType:"digits",
				onBlur:dynaCalcTotalPrice,
				rules : {
					 "number" : true
				},
			    messages : {
					 "number" : "请输入有效数字"
			    }
			}
		},title:'批复数量'
	},
	{field:'storenum',width:60,fixed:true,title:'余量',align:'right',styler: function(value){
		if (value == 0){
			return 'color:red';
		}
	},formatter:function(value){
		return parseFloat(value).toFixed(2);
	}},
	{field:'orderunitname',width:45,fixed:true,title:'单位'},
	{field:'averprice',width:90,fixed:true,edit:true,align:'right',
		editor:{
			type:'text',
			options:{
				align:"right",
				dataType:"number",
				onBlur:dynaCalcTotalPrice,
				rules : {
					 "number" : true
				},
			    messages : {
					 "number" : "请输入有效数字"
			    }
			}
		},title:'单价(元)'
	},
	{field:'priceTotal',width:90,fixed:true,title:'小计(元)',align:'right'},
	{field:'status',width:10,fixed:true,title:'状态',hidden:true},
	{field:'active',width:10,fixed:true,title:'物资分类是否启用',hidden:true},
	{title:'',align:'center',field:'del',width:40,fixed:true,formatter:function(value,row){
		return "<img class='btn-delete btn-garbage' onclick='delApplyRecord(\""+row.listId+"\",\""+row.itemname+"\");' src='"+basePath+"img/purchase/btn_garbage.gif'/>";
	}}
]];
		
//初始化列表（通用方法）
function initList(listType){
	var sendType = "0";
	
	$("#apply_item").datagrid({
		singleSelect : ("over"==processStatus||"last"==processStatus)?false:true,
		fitColumns : true,
		idField:'listId',
		columns:applyItemColumns,
		url : basePath+"purchase/purapply/queryPurApplyItemList.do",
		scrollbarSize : 0,
		queryParams: {
			'sheetId': sheetId,
			'type':listType,
			'sendType':sendType
		},
		onLoadSuccess: function(){
			var listData =$("#apply_item").datagrid("getRows");
			
			//如果是退回首环节的单据要控制批复数量
			if(isRollBack == "Y" && processStatus == "first_save"){
				$("#apply_item").datagrid("getColumnOption",'repliednum').editor = "{'type':'label'}";
			}
			
			if(processStatus == "last"){
				if(listData.length == 0){
					$("#btn-submit").text("流程结束");
				}
				var statusFlag = false;
				for(var i=0;i<=listData.length;i++){
					if(statusFlag){
						i=0;
						statusFlag = false;
					}
					if(typeof(listData[i]) != "undefined" && listData[i] != null){
						if(listData[i].status !=1){
							$('#apply_item').datagrid('deleteRow',$('#apply_item').datagrid('getRowIndex',listData[i].listId));
							statusFlag = true;
						}
					}else{
						break;
					}
				}
			}
			var repliednum = 0;
			for(var i=0;i<listData.length;i++){
				rowCalu(i, listData[i]);
				repliednum = listData[i].repliednum;
				if("" == repliednum  && 0!=repliednum){
					$("#apply_item").datagrid("updateRow",{
						index: i,
						row: { "repliednum": listData[i].itemnum }
					});
				}
			}
			
			if('edit' == operation[0]){
				editForm(edit_single,operation[1]);
				$("#autoform").ITC_Form("readonly");
				if("over"==processStatus&&("true"==hasItemApplying&&"true"==isLastStepAssignee)){
					//有明细状态为1 且处于over状态，且是最后一步处理人打开，开放生成采购单和发送商务网功能
					$("#apply_item").datagrid("showColumn", "ck");
					/*仿照上面 processStatus为last时，隐藏非执行状态的明细*/
					var statusFlag = false;
					for(var i=0;i<=listData.length;i++){
						if(statusFlag){
							i=0;
							statusFlag = false;
						}
						if(typeof(listData[i]) != "undefined" && listData[i] != null){
							if(listData[i].status >1 ){
								$('#apply_item').datagrid('deleteRow',$('#apply_item').datagrid('getRowIndex',listData[i].listId));
								statusFlag = true;
							}
						}else{
							break;
						}
					}
				}
			}else{
				if(operation[1] == 'single'){
					$("#autoform").ITC_Form({validate:true,fixLabelWidth:true},eval(type));
					caluPrice();
				}
			}
			
			if(editStatus){
				$("#btn-edit").trigger("click");
			}
			//initFormStatus = $("#autoform").iForm("getVal");
			//initListStatus = FW.stringify(listData);
			setTimeout(function(){ 
				$("#apply_item").datagrid("resize"); 
			},200);
			//如果不是退回的单据不用控制隐藏批复数量
			if(isRollBack != "Y"){
				$("#apply_item").datagrid("hideColumn","repliednum");
			}
		},
		onRenderFinish : function(){
			$("#apply_item").iFixCheckbox();
			//需要在最后重新确定一下初始表单值
			var listData =$("#apply_item").datagrid("getRows");
			initFormStatus = $("#autoform").iForm("getVal");
			initListStatus = FW.stringify(listData);
		}
	});
}
/*************************************************采购申请*************************************************/