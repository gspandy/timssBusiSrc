
/**
 * 
 * @param id ( value: 1, 2, 3, 4, 5)
 * @param isNewWt (是否新建)
 */
function addSafeContents(id, isNew){
	if(isNew){
		if($("#safeItem"+id).children().length == 0){
			$("#safeItem"+id).append(safeAddContentTpl).append(safeAddContentTpl).append(safeAddContentTpl);
		}
	}else{
		$("#safeItem"+id).append(safeAddContentTpl);
	}
}

/**
 * 为新增的安全措施的输入栏绑定事件
 */
function bindNewSafeEvent(obj){
	var key = window.event.keyCode;
	if(key == 13){
		if($(obj).parent().nextAll().length == 0){
			$(obj).parent().parent().append(safeAddContentTpl);
		};
		$(obj).parent().next().find('input').focus();
	}
}

/**
 * 获取安全措施input的内容
 * @return {}
 */
function getSafeInputs(){
	var safeItems = [];
	var safeItemsDiv = $(".safeItems");
	var invalidMsg = "";
	var valid = true;
	for(var i = 0 ; i < safeItemsDiv.length ; i++){
		if($(safeItemsDiv[i]).css('display') == 'block'){
			var inputs = $(safeItemsDiv[i]).find("input");
			var safeType = $(safeItemsDiv[i]).attr("safetype");
			var safeOrder = 1;
			for(var j = 0 ;j < inputs.length; j++){
				var tempValue = $.trim($(inputs[j]).val());
				if(tempValue.length > 100){
					valid = false;
					invalidMsg = "部分安全措施超过了100个字";
					return {valid:valid,invalidMsg:invalidMsg,safeItems:safeItems};
				}
				if(tempValue){
					var safeItem = {safeType:safeType};
					safeItem.safeContent = tempValue;
					safeItem.safeOrder = safeOrder;
					safeItems.push(safeItem);
					safeOrder++;
				}
			}
		}
	}
	if(safeItems.length == 0){
		valid = false;
		invalidMsg = "至少应该有一条安全措施";
	}
	if(!valid){
		FW.error(invalidMsg);
	}
	return {valid:valid,invalidMsg:invalidMsg,safeItems:safeItems};
}

/**
 * 设置安全措施值
 * @param items
 */
function setSafeContent( items ){
	//判定是否已经重置过safeItem
	var clearContent = {};
	
	for( obj in items ){
		var safeType = items[obj].safeType;
		if( safeType != null && safeType.length > 0 ){
			safeType = safeType.charAt(safeType.length - 1);
		}
		var safeItemId = "safeItem" + safeType;
		if( !clearContent[safeItemId] ){
			$("#" + safeItemId).iFold("show");
			$("#" + safeItemId).html("");
			clearContent[safeItemId] = true;
		}
		var append = $(safeAddContentTpl).appendTo($("#" + safeItemId));
		$(append).find(".safe-input-content").html(FW.specialchars(items[obj].safeContent));
		$(append).find(".safe-input").val(items[obj].safeContent);
	}
}

/**
 * 初始化标准工作票信息
 * @param bean autoform对应的fields
 * @param items 安全措施
 */
function setPtwStandard( bean, items ){
	$("#autoform").iForm("setVal", bean );
	//setSafeContent( items );
	var safeItemList = {};
	var afterConvertItems = convertToSafeItemStyle(items);
	safeItemList.safeDatas = afterConvertItems;
	
	$("#safeItem1").find(".wrap-underline").remove();	//先删掉html
	initSafeItemListByData("safeItem1",safeItemList);
	if(flowStatus == null || flowStatus.indexOf('_flow_std_status_0') >= 0){//如果是草稿状态
		beginEditSafeItemList("safeItem1");
	}else{
		endEditSafeItemList("safeItem1");
	}
}
function convertToSafeItemStyle(items){
	for ( var int = 0; int < items.length; int++) {
		var array_element = items[int];
		array_element.content = array_element.safeContent;
		array_element.showOrder = array_element.safeOrder;
	}
	return items;
}
/** 
 * 暂存
 * @param id 按钮id
 * * @returns {Boolean}
 */
function savePtwStandard( id ){
	var formData = $("#autoform").iForm("getVal");
	
	if( formData.eqName == "请从左边设备树选择" ){
		FW.error("请在设备树中先选择一个设备");
		return false;
	}
	
	if(! $("#autoform").valid()){
		return false;
	}
	
	//拿到安全措施内容
//	var safeResult = getSafeInputs();
	var safeResult = getSafeItemInputs("safeItem1");
	
	if(!safeResult.valid){
		return false;
	}
	
	$("#" + id).button("loading");
	var url = basePath + "ptw/ptwStandard/insertOrUpdatePtwStandard.do";
	$.ajax({
		url : url,
		type : 'post',
		dataType : "json",
		data : {
			formData :  JSON.stringify( formData ),
			safeItems : JSON.stringify( safeResult.safeItems )
		},
		success : function(data) {
			if( data.result == "success" ){
				FW.success( "保存成功 ！");
				setPtwStandard( data.bean, data.items );
				beginEditSafeItemList("safeItem1");
				//closeTab();
			}else{
				FW.error( "保存失败 ！");
			}
			$("#" + id).button("reset");
		}
	});
}


/** 
 * 提交
 * @param id 按钮id
 * * @returns {Boolean}
 */
function commitPtwStandard( btnId ){
	var formData = $("#autoform").iForm("getVal");
	var type = formData.wtTypeId;
	var equipment = formData.eqNo;
	var equipmentName = formData.eqName;
	if (equipment == '' || equipmentName == "请从左边设备树选择") {
		FW.error("请从左边设备树选择");
		return;
	}
	if(! $("#autoform").valid()){
		return false;
	}
	$.post(basePath + "ptw/ptwStandard/hasSameCodeSptwInAudit.do",{"sptwCode":formData.wtNo,"id":formData.id},function(data){
		if(data.result == false){  //没有在审批中的标准票
			//拿到安全措施内容
//			var safeResult = getSafeInputs();
			var safeResult = getSafeItemInputs("safeItem1");			
			if(!safeResult.valid){
				return false;
			}
			var sptwId = null;
			if(flowStatus!= null && (flowStatus.indexOf("_flow_std_status_0") >0||flowStatus.indexOf("_flow_std_status_1") >0 )){
				sptwId = id ;
			}
			$("#" + btnId).button("loading");
			if( instantId != null && instantId != "" ){
				var dataArr = {};
				dataArr.editFlag = "edit";
				dataArr.formData = JSON.stringify( formData );
				dataArr.safeItems = JSON.stringify( safeResult.safeItems );
				$("#" + btnId).button("reset");
				var workFlow = new WorkFlow();
				workFlow.showAudit(taskId,JSON.stringify( dataArr ),closeTab,null,stop);
			}else{
				var url = basePath + "ptw/ptwStandard/insertPtwStandard.do";
				$.ajax({
					url : url, 
					type : 'post',
					dataType : "json",
					data : {
						formData :  JSON.stringify( formData ),
						safeItems : JSON.stringify( safeResult.safeItems )
					},
					success : function(data) {
						if( data.result == "success" ){
							FW.success( "提交成功 ！");
							taskId = data.taskId;
							instantId = data.processInstId;
							if(taskId != null){
								audit();
							}
						}else{
							FW.error( "提交失败 ！");
						}
						$("#" + btnId).button("reset");
					}
				});
			}
		}else{
			$("#autoform").iForm("beginEdit");
			FW.error("该编号的标准票已有一张在审批中");
		}
	},"json");
	
}


function audit(){  //审批
	var formData = $("#autoform").iForm("getVal");
	var type = formData.wtTypeId;
    //拿到安全措施内容
  	var safeResult = getSafeInputs();
  	
      var dataArr = {};
      	dataArr.editFlag = "edit";
		dataArr.formData = JSON.stringify( formData );
		dataArr.safeItems = JSON.stringify( safeResult.safeItems );
	
	//审批弹出框
	var url = basePath + "workflow/process_inst/setVariables.do";
	var params = {};
	params['processInstId'] = instantId;
	var variables = [{'name':'type','value':type},
	                 {'name':'category','value':'sptw'}];  
	params['variables'] = FW.stringify(variables);
	 
	var auditNodeDes = "";
		
	$.post(url,params,function(data){
		if(data.result == 'ok'){
			var sptwSendFormData = dataArr;
			var workFlow = new WorkFlow();
			var updateDesc = auditNodeDes;
			var multiSelect = 0;  //multiselect为1时多选，为0时单选，不传递这个参数则默认为1.
			
			workFlow.showAudit(taskId,FW.stringify(sptwSendFormData),agree,rollback,null,updateDesc,multiSelect);
		}
	});
}

/**
 * 审批的三个回调函数，分别是“同意”，“回退”，“终止”
 */
function agree(){
	closeCurPage();
};
function rollback(){
	closeCurPage();
    
};

/**
 * 关闭当前tab 页
 */
function closeCurPage(flag){
	FW.deleteTabById(FW.getCurrentTabId());
}


