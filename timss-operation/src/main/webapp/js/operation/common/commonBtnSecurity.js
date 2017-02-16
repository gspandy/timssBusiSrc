
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