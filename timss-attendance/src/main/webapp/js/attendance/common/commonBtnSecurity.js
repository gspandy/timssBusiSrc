//切换按钮状态为
function changeBtnState(btnObj,clickAble,tip){
	if(!tip){
		tip="处理中";
	}
	var obj=$(btnObj);
	if(obj){
		if(typeof clickAble=="undefined"){
			clickAble=typeof obj.attr("disabled")=="undefined"?false:obj.attr("disabled")=="disabled";
		}else{
			if(clickAble===obj.attr("disabled")){
				return;
			}
		}
		obj.attr("disabled",!clickAble);
		if(clickAble){
			tip=obj.data("btnName");
		}else{
			obj.data("btnName",obj.html());
		}
		obj.html(tip);
	}
}
//根据sec_function控制页面按钮
function initBtnSec( toolbar ){
	$(".atd_btn_pri").each(function( index ) {
		var hideClazz = $(this).attr("class").split(" ")[1];
		var pri=_parent().privMapping;
		if( pri[hideClazz] != 1 ){
			   $("." + hideClazz).hide();
		   }
	});
	if( toolbar != null && toolbar != "" ){
		FW.fixRoundButtons("#" + toolbar);
	}else{
		FW.fixRoundButtons("#toolbar");
	}
}