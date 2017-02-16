var addSafeItemHtml = '<div class="wrap-underline wrap-underline-single">' +
							'<span class="safe-number-span"></span>'+
							'<span class="safe-input-content" style="display: none;"></span>'+
							'<input class="safe-input" type="text" onkeyup="bindNewSafeItemEvent(this);"/>'+
							'<img width="17" height="18" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA0AAAAOCAIAAAB7HQGFAAAABmJLR0QA/wD/AP+gvaeTAAAACXBIWXMAAA7EAAAOxAGVKw4bAAABe0lEQVQokY3Ru0scURgF8O+7j5lxdXVxR2QiGrQIZKsgIoJdHmAh+b8S8n+ktROSQkJSpU1ChIAKxsH37M7O3Dv3NV+KbEy7p/415xwkIpgiAgCICBHviuo8L8uxWn+crmY9IkD879D7UGs7rvTh0fHp2WWnE98Wav/FYOvZBuc414kn7v3B10dZv9eVVWWiSDIGw5FKkkhpf/K7fLW7sb7WBwBx9OU78sjZJnhDrW90pXXjnREyJj63korVbF5IiVe35cfPJ2tZZ6GbOB8Q8cOnn8babz9+7T3ffP1y0E9TzjmjlgBIRiJJZDHSxoZKmTgSQNCdnUmShHMOACwE19RDILQ2SMnui7quFBF6b9W4YIz97cGcs9c3l1yIWpuLfKi0CdQqbUyjRuUdPjggsk1NxBrjuGSIMBNLpa21CgHg3wkiTRcHT5+8eftOcLLGEFELMrS41Jvf3tkNIUz2IyLnHADL8/xhfc45YyzLlkMIQoiJm+ZfNg0CgD/9HMi6IOQOSwAAAABJRU5ErkJggg=="'+ 
							'onclick="removeSafeItem(this)"/>'+
						'</div>';

/**
 * 初始化空白的安全措施输入栏，默认为三行空白行
 * */
function initEmptySafeItemDatagrid(outerDivId){
	if($("#"+outerDivId).children().length == 0){  //初始化默认添加三行
			$("#"+outerDivId).append(addSafeItemHtml).append(addSafeItemHtml).append(addSafeItemHtml);
	}else{
		$("#"+outerDivId).append(addSafeItemHtml);
	}
}

/**
 * 为新增的安全措施的输入栏绑定事件
 */
function bindNewSafeItemEvent(obj){
	var key = window.event.keyCode;
	if(key == 13){   //回车键
		$(obj).parent().after(addSafeItemHtml);  //在当前行后添加一行
		$(obj).parent().next().find('input').focus();  //光标移动到下一行的输入框中
	}
}


/** 设置可编辑
 * @param outerDivId
 */
function beginEditSafeItemList(outerDivId){
	$("#" + outerDivId).iFold("show");
	$("#" + outerDivId).find("img").show();    //垃圾箱显示
	$("#"+outerDivId).append(addSafeItemHtml);
	$(".wrap-underline").css("paddingRight",0);
	$(".safe-input-content").hide();   //隐藏不可编辑的
	$(".safe-input").show();  //显示可编辑的
}

/** 设置不可编辑
 * @param outerDivId
 */
function endEditSafeItemList(outerDivId){	
	var tempdata = {};
	tempdata.safeDatas = getSafeItemInputs(outerDivId);    //提取数据
	$("#"+ outerDivId).find(".wrap-underline").remove();	//先删掉html
	initSafeItemListByData(outerDivId,tempdata);    //连带数据一起插入进去
}

/** 初始化时的赋值，
 * 
 * [{"content":"安全措施001","showOrder":1},
 *  {"content":"安全措施002","showOrder":2}]
 * 
 * @param outerDivId
 */
function initSafeItemListByData(outerDivId,safeItemList){
	if(safeItemList){
		var safeDatas = safeItemList.safeDatas;
		$("#" + outerDivId).iFold("show");
		for(var j = 0 ; j < safeDatas.length;j++){
			var append = $(addSafeItemHtml).appendTo($("#" + outerDivId));
			$(append).find(".safe-input-content").html(safeDatas[j].content).show();
			$(append).find(".safe-input").val(safeDatas[j].content).hide();
			$(append).children("img").hide();   //垃圾箱隐藏
		}
	}
}

/**
 * 为删除的安全措施的输入栏绑定事件
 */
function removeSafeItem(obj){
	if($(obj.parentNode.parentNode).children().length>1){
		$(obj.parentNode).remove();
	}else{
		$(obj).prev().val("");
	}
};
							
	
/**
 * 获取安全措施input的内容
 * [{"content":"安全措施001","showOrder":1},
 *  {"content":"安全措施002","showOrder":2}]
 * @return {}
 */
function getSafeItemInputs(outerDivId){
	var safeItems = [];
	var inputs = $("#"+outerDivId).find("input");
	var showOrder = 1;
	for(var j = 0 ;j < inputs.length; j++){
		var tempValue = $.trim($(inputs[j]).val());
		if(tempValue){
			var safeItem = {};
			safeItem.content = tempValue;
			safeItem.showOrder = showOrder;
			safeItems.push(safeItem);
			showOrder++;
		}
	}
	return safeItems;
}



var addPtoOperItemHtml = '<div class="wrap-underline2 wrap-underline-single2">' +
							'<div style="position:relative;padding-left:130px" >' +
								'<div style="width:130px;position:absolute;top:0;left:0">' +
									'<span class="safe-number-span"></span>'+
									'<span class="check-content" style="margin-left:45px;display: none;"></span>'+
									'<input class="check-input" type="checkbox" value="false" style="margin-left:45px" >'+
									'<span class="time-content pull-right" style="width: 60px;display: none;"></span>'+
									'<div class="testinput pull-right" style="width: 60px;" input-id="operDate" ></div>'+
								'</div>' +
								'<div style="margin-left:10px;overflow:hidden">' +
									'<div style="float:left;width:70%">' +
										'<span class="safe-input-content" style="display: none;"></span>'+
										'<input class="safe-input" type="text" />'+	
									'</div>' +
									'<div style="width:30%;display:inline-block;padding-left: 10px;">' +
										'<span class="safeRemark-input-content" style="display: none;"></span>'+
										'<input class="safeRemark-input"  type="text"/>'+
									'</div>' +
								'</div>' +
							'</div>' +
						'</div>';


function initOperSafeItemListWithData(outerDivId){
	if($("#"+outerDivId).children().length == 2){  //初始化默认添加三行
			$("#"+outerDivId).append(addPtoOperItemHtml).append(addPtoOperItemHtml).append(addPtoOperItemHtml);
	}else{
		$("#"+outerDivId).append(addPtoOperItemHtml);
	}
	$(".testinput").iDate("init",{datepickerOpts:{format:"hh:ii"}});
}


/** 不可编辑时的赋值，
 * 
 * [{"content":"安全措施001","showOrder":1,“operTime”：“08:52”,"hasOper":"Y","remarks":""},
 *  {"content":"安全措施002","showOrder":2,“operTime”：“09:52”,"hasOper":"N","remarks":""}]
 * 
 * @param outerDivId
 */
function showOperSafeItemListByData(outerDivId,safeItemList){
	if(safeItemList){
		var safeDatas = safeItemList.safeDatas;
		$("#" + outerDivId).iFold("show");
		for(var j = 0 ; j < safeDatas.length;j++){
			var append = $(addPtoOperItemHtml).appendTo($("#" + outerDivId));
			//操作项内容
			$(append).find(".safe-input-content").html(safeDatas[j].content).show();
			$(append).find(".safe-input").val(safeDatas[j].content).hide();
			//是否有执行
			$(append).find(".check-content").html(safeDatas[j].hasOper).show();
			$(append).find(".check-input").hide();
			//执行时间
			$(append).find(".time-content").html(safeDatas[j].operTime).show();
			//备注
			$(append).find(".safeRemark-input-content").html(safeDatas[j].remarks).show();
			$(append).find(".safeRemark-input").val(safeDatas[j].remarks).hide();
		}
	}
}

/** 汇报初始化时的赋值，可编辑
 * 
 * @param outerDivId
 */
function editOperSafeItemListByData(outerDivId,safeItemList){
	if(safeItemList){
		var safeDatas = safeItemList.safeDatas;
		$("#" + outerDivId).iFold("show");
		for(var j = 0 ; j < safeDatas.length;j++){
			var append = $(addPtoOperItemHtml).appendTo($("#" + outerDivId));
			//操作项内容
			$(append).find(".safe-input").val(safeDatas[j].content);		
		}
		$(".testinput").iDate("init",{datepickerOpts:{format:"hh:ii",maxView:1,startView:1}});
	}
}
/**
 * 获取安全措施input的内容
 * [{"content":"安全措施001","safeOrder":1,“operTime”：“08:52”,"hasOper":"Y","remarks":"sdfsd"},
 *  {"content":"安全措施002","safeOrder":2,“operTime”：“09:52”,"hasOper":"N","remarks":"yhjtgh"}]
 * @return {}
 */
function getOperSafeItemInputs(outerDivId){
	var safeItems = [];
	var inputs = $("#"+outerDivId).find("input");
	var showOrder = 1;
	for(var j = 0 ;j < inputs.length; j=j+4){
		var checkedValue = inputs[j].checked==true?'Y':'N';
		var timeValue = $.trim($(inputs[j+1]).val());
		var itemValue = $.trim($(inputs[j+2]).val());
		var remarksValue = $.trim($(inputs[j+3]).val());
		var safeItem = {};
		safeItem.content = itemValue;
		safeItem.operTime = timeValue;
		safeItem.hasOper = checkedValue;
		safeItem.remarks = remarksValue;
		safeItem.showOrder = showOrder;
		safeItems.push(safeItem);
		showOrder++;
	}
	return safeItems;
}

/**
 * @param outerDivId  在只读模式下读取值
 * @returns {Array}
 */
function getOperSafeItemsInShow(outerDivId){
	var safeItems = [];
	var inputs = $("#"+outerDivId).find("input");
	var showOrder = 1;
	for(var j = 0 ;j < inputs.length; j=j+3){
		var checkedValue = inputs[j].checked==true?'Y':'N';
		var itemValue = $.trim($(inputs[j+1]).val());
		var remarksValue = $.trim($(inputs[j+2]).val());
		var safeItem = {};
		safeItem.content = itemValue;
		safeItem.hasOper = checkedValue;
		safeItem.remarks = remarksValue;
		safeItem.showOrder = showOrder;
		safeItems.push(safeItem);
		showOrder++;
	}
	return safeItems;
}