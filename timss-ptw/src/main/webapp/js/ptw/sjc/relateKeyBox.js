//初始化datagrid
function initRelateKeyBoxDatagrid(islId,wtId){
	var tableId = "RelateKeyBox";
	var url = basePath + "ptw/ptwKeyBox/queryRelateKeyBox.do";
	$("#title"+tableId).iFold("init");
	$("#"+tableId+"BtnDiv").hide();
	$("#"+tableId+"Datagrid").datagrid({
	    columns:[[
	      		{field:"keyBoxNo",title:"钥匙箱号",width:110,fixed:true}, 
	    		{field:"purpose",title:"用途",width:200},
	    		{field:"curStatus",title:"当前状态",width:110,fixed:true,
	    			formatter: function(value,row,index){
	    				return FW.getEnumMap("PTW_KEYBOXSTATUS")[value]; 
	    			}
	    		},
	    		{field : 'id',title : '操作',width:55, fixed:true,hidden:true,
	    	 		formatter:function(value,row,index){
	    		     	return '<img src="'+basePath+'img/ptw/btn_garbage.gif"  width="16" height="16" >';
	    		 	}
	    		}
	    	]],
	    fitColumns:true,
	    scrollbarSize:0,
	    url : url,
	    queryParams:{
	    	islId:islId,
	    	wtId:wtId
	    },
	    singleSelect:true,
	    onClickCell : function(rowIndex, field, value) {
			if (field == 'id') {
				deleteThisKeyBoxRow(tableId + "Datagrid", rowIndex, field, value, 'btn'+tableId+'Table');
			}
		},
		onDblClickRow:function(rowIndex,rowData){
			openKeyBoxPage(rowData.id, rowData.keyBoxNo);
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
        		beginEditRelateKeyBox(tableId);
        	}else{
        		$("#"+tableId+"Datagrid").datagrid("hideColumn","id");
        		$("#"+tableId+"BtnDiv").hide();
        	}
        }
	}); 
}

function checkRelateKeyBox(keyBoxInfo){
	//TODO 需判断钥匙箱的那些状态允许作为关联箱
	if(keyBoxInfo.curStatus != 'safe'){
		FW.error("钥匙箱"+keyBoxInfo.keyBoxNo+"不处于已安全状态，无法被关联");
		return false;
	}
	
	var rows = $("#RelateKeyBoxDatagrid").datagrid("getRows");
	for(var i in rows){
		if(rows[i].id == keyBoxInfo.id){
			FW.error("钥匙箱"+ keyBoxInfo.keyBoxNo+"已经被关联了");
			return false;
		}
	}
	return true;
}

function apppendToRelateBoxDatagrid(keyBoxInfo){
	var result = checkRelateKeyBox(keyBoxInfo);
	if(result){
		$("#RelateKeyBoxDatagrid").datagrid("appendRow",keyBoxInfo);
	}
	return result;
}

function getRelateKeyBoxForSave(){
	endEditRelateKeyBox();
	var rows = $("#RelateKeyBoxDatagrid").datagrid("getRows");
	var ids = [];
	for(var i in rows){
		ids.push(rows[i].id);
	}
	return ids.join(",");
}

function popKeyBoxWindow(){
	var src = basePath + "ptw/ptwIsolationArea/SelectKeyBox.do?from=relateKeyBox";
    var dlgOpts = {
        width : 800,
        height:500,
        closed : false,
        title:"双击选择关联的钥匙箱",
        modal:true
    };
    Notice.dialog(src,dlgOpts,null);
}

function beginEditRelateKeyBox(){
	var tabId = "RelateKeyBox";
	$("#"+tabId+"Div").show();
	$("#"+tabId+"Datagrid").datagrid("showColumn","id");
	$("#"+tabId+"BtnDiv").show();
}

//关闭编辑模式
function endEditRelateKeyBox(){
	var tabId = "RelateKeyBox";
	var rowSize = $("#" + tabId+"Datagrid" ).datagrid('getRows').length;
	if(rowSize == 0){
		$("#"+tabId+"Div").hide();
	}
	$("#"+tabId+"Datagrid").datagrid("hideColumn","id");
	$("#"+tabId+"BtnDiv").hide();
}

//删除一行datagrid数据
function deleteThisKeyBoxRow(datagridId, rowIndex, field, value, tableId ) {
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



