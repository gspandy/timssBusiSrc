var hasInitedSafeItemFold = false;
function initSafeInfoByConfig(isPosition){
	if(!hasInitedSafeItemFold){
		for(var i = 1 ; i <= 6 ; i++){
			$("#safeItem"+i).iFold("init");
		}
		hasInitedSafeItemFold = true;
	}
	for(var i = 1 ; i <= 6 ; i++){
		$("#safeItem"+i).iFold("hide");
	}
	
	if(!isPosition){
		//如果为动火票 采取的检修安全措施和运行安全措施进行换位
		if(param_config.isFireWt==1){
			var $safeItem2Obj = $("#safeItem2");
			var $safe2Obj = $("#safeItem2").prev();
			$("#safeItem2").prev().remove();
			$("#safeItem2").remove();
			
			$("#safeItem3").after($safeItem2Obj).after($safe2Obj);
			
		}
	}
	if(isNewWt){
		initSafeItemsByConfig();
	}else{
		initSafeItemsByData();
	}
}

function beginEditSafeItems(){
	initSafeItemsByConfig();
//	$(".safeItems .wrap-underline").css("paddingRight",0);
//	$(".safeItems .safe-input-content,.exec-span,.remover-span,.safe-unremove-remarks").hide();
//	$(".safeItems .safe-input,.safe-img").show();

}

function endEditSafeItems(){
	for(var i = 1 ; i <= 6 ; i++){
		$("#safeItem"+i).iFold("hide");
		if(i != 6){
			$("#safeItem"+ i).find(".wrap-underline").remove();
		}
	}
	
	initSafeItemsByData();
}

function initSafeItemsByConfig(){
	if(param_config.hasElec == 1){
		$("#safeItem4").iFold("show");//安全措施中的应装接电线
		$("#safeItem6").iFold("show");//保留带电部分
		addSafeContents(4);
	}
	if(param_config.hasSafeDefault==1){
		$("#safeItem1").iFold("show");//必须采取的安全措施
		addSafeContents(1);
	}
	if(param_config.hasSafeRepair==1){
		$("#safeItem2").iFold("show");///必须采取的检修安全措施
		addSafeContents(2);
	}
	if(param_config.hasSafeOprate==1){
		$("#safeItem3").iFold("show");//必须采取的运行安全措施
		addSafeContents(3);
	}
}

function initSafeItemsByData(){
	if(ptwSafes){
		var safeDatas = ptwSafes.safeDatas;
		for(var i = 0 ; i < ptwSafes.ptwTypes.length;i++){
			var safeType = ptwSafes.ptwTypes[i];
			$("#safeItem" + safeType).iFold("show");
			for(var j = 0 ; j < safeDatas.length;j++){
				if(safeDatas[j].safeType == safeType){
					if(safeType == 6){
						$("#safeItem6Content").find(".safe-input-content").show().html(FW.specialchars(safeDatas[j].safeContent));
						$("#safeItem6Content").find(".safe-input").val(safeDatas[j].safeContent).hide();
					}else{
						var append = $(safeContentTpl).appendTo($("#safeItem" + safeType));
						$(append).find(".safe-input-content").html(FW.specialchars(safeDatas[j].safeContent));
						$(append).find(".safe-input").val(safeDatas[j].safeContent);
						
						//执行人、解除人信息的更新
						var rightPadding = 0;
						if(safeDatas[j].executer){
							$(append).find(".exec-span").html(FW.specialchars(safeDatas[j].executer));
							rightPadding++;
						}else{
							$(append).find(".exec-span").hide();
						}
						if(safeDatas[j].remarks){
							$(append).find(".safe-unremove-remarks").html(FW.specialchars(safeDatas[j].remarks));
							var contentHeight = $(append).find(".safe-input-content").css("height");
							var remarkHeight = $(append).find(".safe-unremove-remarks").css("height");
							if(contentHeight && remarkHeight){
								contentHeight = Number(contentHeight.substring(0,contentHeight.length-2));
								var remarkHeight2 = Number(remarkHeight.substring(0,remarkHeight.length-2));
								if(contentHeight!=='NaN' && remarkHeight!=='NaN' && contentHeight<remarkHeight2){
									$(append).find(".safe-input-content").css("height",remarkHeight);
								}
							}
							rightPadding++;
						}else{
							$(append).find(".safe-unremove-remarks").hide();
						}
						if(safeDatas[j].remover){
							$(append).find(".remover-span").html(FW.specialchars(safeDatas[j].remover));
							rightPadding++;
						}else{
							$(append).find(".remover-span").hide();
						}
						if(rightPadding != 3){
							if(safeDatas[j].remover){
								$(append).css("paddingRight",(rightPadding+1) * 100 + "px");
							}else{
								$(append).css("paddingRight",(rightPadding) * 100 + "px");
							}
						}else{
							$(append).css("paddingRight",410 + "px");
						}
						//只有执行人时，需要往右移动
						if(rightPadding == 2){
							if(safeDatas[j].remarks){
								$(append).find(".safe-unremove-remarks").css("right",0);
								$(append).find(".exec-span").css("right",200);
							}else{
								$(append).find(".exec-span").css("right",200);
							}
						}
						if(rightPadding == 1){
							$(append).find(".exec-span").css("right",0);
						}
					}
				}
			}
		}
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

function addSafeContents(id){
	if(isNewWt){
		initEmptySafeItemDatagrid("safeItem"+id);
//		if($("#safeItem"+id).children().length == 0){
//			$("#safeItem"+id).append(safeAddContentTpl).append(safeAddContentTpl).append(safeAddContentTpl);
//		}
	}else{
		var safeItemList = {};
		safeItemList.safeDatas = getSafeItemInputs("safeItem"+id).safeItems;
		$("#safeItem"+ id).find(".wrap-underline").remove();	//先删掉html
		initSafeItemListByData("safeItem"+id,safeItemList)
		beginEditSafeItemList("safeItem"+id);
//		$("#safeItem"+id).append(safeAddContentTpl);
	}
}

/**增加补充安全措施*/
function addOtherSafeContents(){
	$("#safeItem5").iFold("show");
	if($("#safeItem5").children().length == 0){
		initEmptySafeItemDatagrid("safeItem5");
		$("#safeItem5").children().first().find("input").focus();
//		$("#safeItem5").append(safeAddContentTpl).append(safeAddContentTpl).append(safeAddContentTpl);
//		$("#safeItem5").children().first().find("input").focus();
	}else{
		if($("#safeItem5").children().last().find("input").val() != ""){
			$("#safeItem5").append(safeAddContentTpl);
		}
		$("#safeItem5").children().last().find("input").focus();
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
					FW.error(invalidMsg);
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
 * 为删除的安全措施的输入栏绑定事件
 */
function removeSafeItem(obj){
	if($(obj.parentNode.parentNode).children().length>1){
		$(obj.parentNode).remove();
	}else{
		$(obj).prev().val("");
	}
}
//修改补充安全措施
function beginEditOtherSafe(){
	if(ptwSafes){
		var safeDatas = ptwSafes.safeDatas;
		for(var i = 0 ; i < ptwSafes.ptwTypes.length;i++){
			var safeType = ptwSafes.ptwTypes[i];
			if(safeType == 5){
				addOtherSafeContents();
				$("#safeItem5 .wrap-underline").css("paddingRight",0);
				$("#safeItem5 .safe-input-content,#safeItem5 .exec-span,#safeItem5 .remover-span,#safeItem5 .safe-unremove-remarks").hide();
				$("#safeItem5 .safe-input,#safeItem5 .safe-img").show();
				break;
			}
		}
	}
}