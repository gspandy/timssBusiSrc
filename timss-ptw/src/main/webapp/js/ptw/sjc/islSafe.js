function genBaseSafeField(execUserList){
  var baseSafeField = [[
	{field:"safeOrder",title:"编号",width:50,fixed:true,formatter: function(value,row,index){
			if( value == 0 ){
				return "已安全";
			}
			return value;
		}},  
	{field:"pointNo",title:"隔离点编号",width:110,fixed:true}, 
	{field:"pointName",title:"隔离点",width:110,fixed:true}, 
	{field:"stdMethodId",title:"隔离方法编号",width:110,fixed:true,hidden:true}, 
	{field:"methodName",title:"隔离方法名",width:300},
	{field:"elecFloorNo",title:"接地线编号",width:80,fixed:true,hidden:true},
	{field:"executerNo",title:"执行人",width:80,fixed:true,hidden:true,'editor':{
			"type" : "combobox",
			"options" : {
				"data" : execUserList             
			}
		},formatter: function(value,row,index){
			 for(var i in execUserList){
			 	if(execUserList[i][0] == value){
			 		return execUserList[i][1];
			 	}
			 }
			 return value;
		 }
	},
	{field:"removerNo",title:"解除人",width:80,fixed:true,hidden:true,'editor':{
			"type" : "combobox",
			"options" : {
				"data" : execUserList             
			}
		},formatter: function(value,row,index){
			 for(var i in execUserList){
			 	if(execUserList[i][0] == value){
			 		return execUserList[i][1];
			 	}
			 }
			 return value;
		 }
	},
	{field:"safeType",title:"类型",width:80,fixed:true,hidden:true},
	{field : 'id',title : '操作',width:55, fixed:true,hidden:true,
 		formatter:function(value,row,index){
	     	return '<img src="'+basePath+'img/ptw/btn_garbage.gif"  width="16" height="16" >';
	 	}
	}
  ]];
  return baseSafeField;
}


function findSafeColumnsById(columns,id){
	for(var i in columns[0]){
		if(columns[0][i].field == id){
			return columns[0][i];
		}
	}
}

function adjustColumnsByStatus(columns,islStatus){
	var executerNo = findSafeColumnsById(columns,"executerNo");
	var removerNo = findSafeColumnsById(columns,"removerNo");
	
	//新建或未签发时
	if(islStatus == null || islStatus == 0 || islStatus == 300){
		delete executerNo.editor;
		delete executerNo.formatter;
		delete removerNo.editor;
		delete removerNo.formatter;
	}else if(islStatus == 400){
		//已签发
		delete removerNo.editor;
		delete removerNo.formatter;
	}else if(islStatus == 500){
		//已许可
		executerNo.hidden = false;
		delete executerNo.editor;
		delete removerNo.editor;
		delete removerNo.formatter;
	}else if(islStatus == 600){
		//已结束
		executerNo.hidden = false;
		delete executerNo.editor;
		//var elecFloorNo = findSafeColumnsById(columns,"elecFloorNo");
		//delete elecFloorNo.editor;
	}else if(islStatus == 700 || islStatus == 800){
		//已终结
		executerNo.hidden = false;
		removerNo.hidden = false;
		delete executerNo.editor;
		delete removerNo.editor;
		//var elecFloorNo = findSafeColumnsById(columns,"elecFloorNo");
		//delete elecFloorNo.editor;
	}
}
//必须采取的安全措施column定义
function genIsolationSafeField(execUserList,islStatus){
	var columns = genBaseSafeField(execUserList).slice(0);
	findSafeColumnsById(columns,"elecFloorNo").hidden = true;
	adjustColumnsByStatus(columns,islStatus);
	return columns;
}

//地线的安全措施column定义
function genIsolationElecField(execUserList,islStatus){
	var columns = genBaseSafeField(execUserList).slice(0);
	var elecFloorNo = findSafeColumnsById(columns,"elecFloorNo");
	elecFloorNo.hidden = false;
	elecFloorNo.editor = {
		"type":"text",
		"options" : {
			rules : {
				required:true
			}	
		}
	};	
	adjustColumnsByStatus(columns,islStatus);
	return columns;
}

//补充的安全措施column定义
function genIsolationCompField(execUserList,islStatus){
	var columns = genBaseSafeField(execUserList).slice(0);
	var elecFloorNo = findSafeColumnsById(columns,"elecFloorNo");
	elecFloorNo.hidden = false;
	elecFloorNo.editor = {
		"type" : "text"
	};
	adjustColumnsByStatus(columns,islStatus);
	return columns;
}

//初始化datagrid
function initSingleSafeDatagrid(safeType,islId,wtId,tableId,columns){
	var url = basePath + "ptw/ptwIsolation/querySafeDatagridByWtOrIslId.do";
	$("#title"+tableId).iFold("init");
	$("#"+tableId+"BtnDiv").hide();
	$("#"+tableId+"Datagrid").datagrid({
	    columns:columns,
	    fitColumns:true,
	    scrollbarSize:0,
	    url : url,
	    queryParams:{
	    	safeType:safeType,
	    	islId:islId,
	    	wtId:wtId
	    },
	    singleSelect:true,
	    onClickCell : function(rowIndex, field, value) {
			if (field == 'id') {
				deleteThisRow(tableId + "Datagrid", rowIndex, field, value, 'btn'+tableId+'Table');
			}
		},
        onLoadSuccess: function(data){
        	var len = data.total;
        	var textVal =  len > 0 ? "继续添加" : "添加";
        	$("#btn"+tableId+"Table").text( textVal );
        	if(!editFlag && len == 0){
        		$("#"+tableId+"Div").hide();
        	}
        	
        	if( editFlag ){
        		$("#"+tableId+"Datagrid").datagrid("showColumn","id");
        		$("#"+tableId+"BtnDiv").show();
        		beginEditor(tableId);
        	}else{
        		$("#"+tableId+"Datagrid").datagrid("hideColumn","id");
        		$("#"+tableId+"BtnDiv").hide();
        	}
        	
        	//增加从标准隔离证复制的功能
        	if(tableId == "IsolationSafe"){
        		if(params.areaId){
        			loadSafeDataFromIslArea();
        		}
        	}
        }
	}); 
}

function initSafeDataGrid(execUserList,islStatus,islId,wtId){
	initSingleSafeDatagrid(2,islId,wtId,"IsolationJx",genIsolationSafeField(execUserList,islStatus));
	initSingleSafeDatagrid(1,islId,wtId,"IsolationSafe",genIsolationSafeField(execUserList,islStatus));
	initSingleSafeDatagrid(4,islId,wtId,"IsolationElec",genIsolationElecField(execUserList,islStatus));
	initSingleSafeDatagrid(5,islId,wtId,"CompSafe",genIsolationCompField(execUserList,islStatus));
	if(islStatus == null || islStatus == 0){
		beginEditSafeItems();
	}
}


//删除一行datagrid数据
function deleteThisRow(datagridId, rowIndex, field, value, tableId ) {
	if (field == 'id') {
		FW.confirm("删除？<br/>确定删除所选项吗？该操作无法撤销。", function() {
			$("#" + datagridId ).datagrid("deleteRow",rowIndex);
			var rows = $("#" + datagridId).prev(".datagrid-view2").children(".datagrid-body").children("table").find(".datagrid-row");
			var rowData = $("#" + datagridId).datagrid("getRows");
			var len = rowData.length;
		 	var safeIndex = 0;
			for( var i = 0; i < len; i++ ){
				var safeOrder = rowData[i].safeOrder;
				if( safeOrder != 0 ){
					rowData[i].safeOrder = ++safeIndex;
					$(rows[i]).children("td[field='safeOrder']").children("div").html(safeIndex);
				}
			}
			var textVal =  len > 0 ? "继续添加" : "添加";
        	$("#" + tableId ).text( textVal );
		});
	}
}

//单击的datagrid
var clickDatagridId = "";

//必须采取的安全措施 append-->datagrid
function appendIsolationSafe(tableId ){
	var datagridId = tableId + "Datagrid";
	clickDatagridId = tableId;
	FW.showIsolationMethod({
		onParseData : function(data){
			var rowData = $("#" + datagridId ).datagrid( 'getRows' );
			var size = rowData.length;
			
			var flag = false;
			var msg = "";
			for(var i=0;i<size;i++){
				for( var j = 0; j < data.length; j ++ ){
					var dataId = data[j].pointNo;
					if( dataId == rowData[i].pointNo ){
						flag = true;
						msg += " " + dataId;
					}
				}
			}
			if( !flag ){
				for( var j = 0; j < data.length; j ++ ){
					data[j].safeOrder = changeSafeOrder( size, data[j] );
					
					//应接地线，safeType = 4
					if( datagridId == "IsolationElecDatagrid" ){
						data[j].safeType = 4; 
					}else if(datagridId == "CompSafeDatagrid"){
						data[j].safeType = 5;
					}else if(datagridId == "IsolationJxDatagrid"){
						data[j].safeType = 2;
					}else{
						data[j].safeType = 1;
					}
					//检查补充安全措施是否在安全/地线中
					if( checkCompSafe( data[j].pointNo ) ){
						return;
					}
					$("#" + datagridId ).datagrid("appendRow",data[j] );
					size++;
				}
			}else{
				FW.error( msg + "隔离点已经存在！" );
			}
			$("#btn" + tableId+"Table" ).text("继续添加");
			beginEditor( tableId );
		}
	});
}

//修改序号
function changeSafeOrder( size, data ){
	return ++size;
}

//开启编辑模式
function beginEditor( tableId ){
	var rowSize = $("#" + tableId + "Datagrid" ).datagrid('getRows').length;
	for( var i = 0 ; i < rowSize; i++ ){
    	$("#" + tableId + "Datagrid").datagrid('beginEdit', i);
	}
}

//开启执行人解除人的编辑模式
function beginEditExecAndRemover(tabId,islStatus){
	var columnNo = "executerNo";
	if(islStatus == 600){
		columnNo = "removerNo";
	}
	if(islStatus == 400 || islStatus == 600){
		if($("#"+tabId+"Div:visible")){
			$("#"+tabId+"Datagrid").datagrid('showColumn',columnNo);
			var length = $("#"+tabId+"Datagrid").datagrid('getRows').length;
			for(var i = 0 ; i < length; i++){
				$("#"+tabId+"Datagrid").datagrid('beginEdit',i);
			}
		}
	}
}
/**私有方法，开启单个安全措施方法Div的编辑*/
function editSafeDiv(tabId){
	$("#"+tabId+"Div").show();
	$("#"+tabId+"Datagrid").datagrid("showColumn","id");
	beginEditor(tabId);
	$("#"+tabId+"BtnDiv").show();
}

/**开启除补充安全措施方法Div外的编辑*/
function beginEditSafeItems(){
	editSafeDiv("IsolationSafe");
	if(jxFlag){
   		editSafeDiv("IsolationJx");
	}
	if(elecFlag){
   		editSafeDiv("IsolationElec");
	}
}

//关闭编辑模式
function endEditor( tabId ){
	var rowSize = $("#" + tabId+"Datagrid" ).datagrid('getRows').length;
	if(rowSize == 0){
		$("#"+tabId+"Div").hide();
	}
	for( var i = 0 ; i < rowSize; i++ ){
    	$("#" + tabId+"Datagrid" ).datagrid('endEdit', i);
	}
	$("#"+tabId+"Datagrid").datagrid("hideColumn","id");
	$("#"+tabId+"BtnDiv").hide();
}

//检查补充安全措施是否在安全/地线中
function checkCompSafe( pointNo ){
	var isCheck = false;
	var safeData = $("#IsolationSafeDatagrid" ).datagrid( 'getRows' );
	var elecData = $("#IsolationElecDatagrid" ).datagrid( 'getRows' );
	var jxData = $("#IsolationJxDatagrid" ).datagrid( 'getRows' );
	
	var safeLen = safeData.length;
	var elecLen = elecData.length;
	var jxLen = jxData.length;
	for( var i = 0 ; i < safeLen; i ++ ){
		if( pointNo == safeData[i].pointNo ){
			isCheck = true;
			FW.error( "已在必须采取的安全措施，请重新选择！");
			break;
		}
	}
	if( !isCheck ){
		for( var j = 0; j < elecLen; j ++ ){
			if( pointNo == elecData[j].pointNo ){
				isCheck = true;
				FW.error( "已在应接地线，请重新选择！");
				break;
			}
		}
	}
	if( !isCheck ){
		for( var j = 0; j < jxLen; j ++ ){
			if( pointNo == jxData[j].pointNo ){
				isCheck = true;
				FW.error( "已在检修必须采取的安全措施中，请重新选择！");
				break;
			}
		}
	}
	return isCheck;
}
	
function initExecAndDatagrid(islStatus,islId,wtId){
	if(islStatus == 600){
		$("#execCheckBoxLabel").html("同一解除人");
	}else if(islStatus == 400){
		$("#execCheckBoxLabel").html("同一执行人");
	}
	$("#execCombo").iCombo("init",{
		onChange:function(val){
			
			$("#IsolationSafeDatagrid").prev(".datagrid-view2").children(".datagrid-body").find(".datagrid-row")
			.find("select").each(function(){
				$(this).iCombo("setVal",val);
			});
			$("#IsolationElecDatagrid").prev(".datagrid-view2").children(".datagrid-body").find(".datagrid-row")
			.find("select").each(function(){
				$(this).iCombo("setVal",val);
			});
			$("#IsolationJxDatagrid").prev(".datagrid-view2").children(".datagrid-body").find(".datagrid-row")
			.find("select").each(function(){
				$(this).iCombo("setVal",val);
			});
			$("#CompSafeDatagrid").prev(".datagrid-view2").children(".datagrid-body").find(".datagrid-row")
			.find("select").each(function(){
				$(this).iCombo("setVal",val);
			});
		},
		allowSearch:true
	});
	$("#safeExecCheck").iCheck({
        checkboxClass: 'icheckbox_flat-blue',
        radioClass: 'iradio_flat-blue'
    });
	$("#safeExecCheck").on('ifChecked', function(event){
		$("#execComboWrap").show();
		$("#checkDiv").css("width",90);
	});
	$("#safeExecCheck").on('ifUnchecked', function(event){
		$("#execComboWrap").hide();
		$("#checkDiv").css("width",90);
	});
	$("#execComboDiv").hide();
	$.post(basePath+ "ptw/ptwInfo/queryPtwUsersByGroup.do",{"role" : "PTW_safe_exec","hasOther":"select"},function(data){
		$("#execCombo").iCombo("loadData",data);
		initSafeDataGrid(data,islStatus,islId,wtId);
	},"json");
}

/**控制检修必须采取的安全措施和应接地线*/
function showDatagridByConfig(jxFlag,elecFlag){
	//是否开启 检修datagrid
	if(jxFlag){
		$("#IsolationJxDiv").show();
		if($("#IsolationJxDatagrid").data().datagrid){
			$("#IsolationJxDatagrid").datagrid("resize");
		}
		
	}else{
		$("#IsolationJxDiv").hide();
	}
	//是否开启 应接地线datagrid
	if( elecFlag ){
		$("#IsolationElecDiv").show();
		if($("#IsolationElecDatagrid").data().datagrid){
			$("#IsolationElecDatagrid").datagrid("resize");
		}
	}else{
		$("#IsolationElecDiv").hide();
	}
}
function getSafeInputs(islStatus,isCopy){
	var result = validSafeDatagrid(islStatus,isCopy);
	if(result.valid){
		result.safeItems = {"safe":FW.stringify( result.IsolationSafeDatagrid ),
							"jxSafe":FW.stringify( result.IsolationJxDatagrid),
							"elecSafe":FW.stringify( result.IsolationElecDatagrid),
							"compSafe":FW.stringify( result.CompSafeDatagrid)
		};
	}
	return result;
}
//校验datagrid & 取值
function validSafeDatagrid(islStatus,isCopy){
	var result = {"valid":true};
	if(!$("#IsolationElecForm").valid() || !$("#CompSafeForm").valid() ){
		result.valid = false;
		return result;
	}
	
	result.IsolationSafeDatagrid = $("#IsolationSafeDatagrid").datagrid('getRows');
	result.IsolationElecDatagrid = $("#IsolationElecDatagrid").datagrid('getRows');
	result.IsolationJxDatagrid = $("#IsolationJxDatagrid").datagrid('getRows');
	result.CompSafeDatagrid = $("#CompSafeDatagrid").datagrid('getRows');
	/*if( result.IsolationSafeDatagrid.length + result.IsolationElecDatagrid.length + result.IsolationJxDatagrid.length <= 0 ){
		FW.error( "至少应该有一条安全措施！");
		result.valid = false;
		return result;
	}*/
	
	//复制把补充隔离措施数据置空
	if( isCopy ){
		result.CompSafeDatagrid = [];
		//置空执行人 解除人
		for(var i = 0 ; i < result.IsolationSafeDatagrid.length; i++){
			result.IsolationSafeDatagrid[i].removerNo = '';			
			result.IsolationSafeDatagrid[i].executerNo = '';			
		}
		for(var i = 0 ; i < result.IsolationElecDatagrid.length; i++){
			result.IsolationElecDatagrid[i].removerNo = '';			
			result.IsolationElecDatagrid[i].executerNo = '';			
		}
		for(var i = 0 ; i < result.IsolationJxDatagrid.length; i++){
			result.IsolationJxDatagrid[i].removerNo = '';			
			result.IsolationJxDatagrid[i].executerNo = '';			
		}
	}else{
		updateExecAndRemoveInfo(islStatus, "IsolationSafe", result);
		if(!result.valid){
			return result;
		}
		updateExecAndRemoveInfo(islStatus, "IsolationElec", result);
		if(!result.valid){
			return result;
		}
		updateExecAndRemoveInfo(islStatus, "IsolationJx", result);
		if(!result.valid){
			return result;
		}
		updateExecAndRemoveInfo(islStatus, "CompSafe", result);
	}
	
	//获取接地线编号数据
	endEditor( "IsolationSafe" );
	endEditor( "IsolationElec" );
	endEditor( "IsolationJx" );
	endEditor( "CompSafe" );
	
	var elecData = $("#IsolationElecDatagrid").datagrid('getRows');
	for( var index in result.IsolationElecDatagrid ){
		result.IsolationElecDatagrid[index].elecFloorNo = elecData[index].elecFloorNo;
	}
	if(!isCopy){
		var comSafeData = $("#CompSafeDatagrid").datagrid('getRows');
		for( var i in result.CompSafeDatagrid ){
			result.CompSafeDatagrid[i].elecFloorNo = comSafeData[i].elecFloorNo;
		}
	}
	return result;		
}

/**更新执行人、解除人信息并校验*/
function updateExecAndRemoveInfo(islStatus,tabId,result){
	var msg = "请选择执行人";
	var columnNo = "executerNo";
	var columnName = "executer";
	if(islStatus == 600){
		msg = "请选择解除人";
		columnNo = "removerNo";
		columnName = "remover";
	}
	if(islStatus == 400 || islStatus == 600){
		if($("#"+tabId+"Div:visible")){
			var index = 0;
			$("#"+tabId+"Datagrid").prev(".datagrid-view2").children(".datagrid-body").find(".datagrid-row")
			.find("select").each(function(){
				if($(this).iCombo("getVal") == -1){
					FW.error(msg);
					index = -1;
					result.valid = false;
					return result;
				}else{
					result[tabId+"Datagrid"][index][columnNo] = $(this).iCombo("getVal");
					result[tabId+"Datagrid"][index][columnName] = $(this).iCombo("getTxt");
					index++;
				}
			});
		}
		
	}
	return result;
}

function controlSafeDivForCopy(){
	
	$("#IsolationSafeDatagrid").datagrid("hideColumn","executerNo");
	$("#IsolationSafeDatagrid").datagrid("hideColumn","removerNo");
	$("#IsolationElecDatagrid").datagrid("hideColumn","executerNo");
	$("#IsolationElecDatagrid").datagrid("hideColumn","removerNo");
	$("#IsolationJxDatagrid").datagrid("hideColumn","executerNo");
	$("#IsolationJxDatagrid").datagrid("hideColumn","removerNo");
	beginEditSafeItems();
	$("#CompSafeDiv").hide();
}
