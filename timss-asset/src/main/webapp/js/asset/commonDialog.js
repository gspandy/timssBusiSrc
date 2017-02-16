

/**
 * desc : 弹出框 
 *  @param width 宽度
 *  @param  height 高度
 *  @param  title 显示标题
 *  @param  url 打开的源
 *  @param callBack 回调函数
 *  
 */
function showDialogIframe(width, height, title, url , callBack ){
	
	//对话框基本参数
	var pri_dlgOpts = {
		width : width == null ? 550 :width,
		height :  height == null ? 150 :height,
		closed : false,
		title : title,
		modal : true
	};
	
	var btnOpts = [{
		"name" : "取消",
		"float" : "right",
		"style" : "btn-default",
		"onclick" : function() {
			return true;
		}
	},{
		"name" : "确定",
		"float" : "right",
		"style" : "btn-success",
		"onclick" : function() {
			if( callBack != null && callBack != "" ){
				callBack();
			}
			return true;
		}
	} ];

	FW.dialog("init", {
		"src" : url,
		"dlgOpts" : pri_dlgOpts,
		"btnOpts" : btnOpts
	});
}

/**
 * desc : 弹出框 没有确定按钮
 *  @param width 宽度
 *  @param  height 高度
 *  @param  title 显示标题
 *  @param  url 打开的源
 *  @param callBack 取消回调函数
 *  
 */
function showDialogIframeOnlyCancel(width, height, title, url , callBack ){
	
	//对话框基本参数
	var pri_dlgOpts = {
			width : width == null ? 550 :width,
					height :  height == null ? 150 :height,
							closed : false,
							title : title,
							modal : true
	};
	
	var btnOpts = [{
		"name" : "关闭",
		"float" : "right",
		"style" : "btn-default",
		"onclick" : function() {
			if( callBack != null && callBack != "" ){
				callBack();
			}
			return true;
		}
	}];
	
	FW.dialog("init", {
		"src" : url,
		"dlgOpts" : pri_dlgOpts,
		"btnOpts" : btnOpts
	});
}