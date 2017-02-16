	//打开硬件台账多条件查询页面
	function showDtlIframe( id, name ){
		var src = basePath + "asset/hwLedger/searchHwLedgerPage.do?hwId=" + id + "&" + $.param({"hwName":name});
		//对话框
		var pri_dlgOpts = {
			width : 500,
			height : 400,
			closed : false,
			title : "硬件台账多条件查询",
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
			"name" : "查询",
			"float" : "right",
			"style" : "btn-success",
			"onclick" : function() {				
				var conWin = _parent().window.document.getElementById("itcDlgContent").contentWindow;
				if (!conWin.valid()) {
					return false;
				}
				var formdata = conWin.formData();
				toHwLedger( formdata );
				return true;
			}
		} ];

		FW.dialog("init", {
			"src" : src,
			"dlgOpts" : pri_dlgOpts,
			"btnOpts" : btnOpts
		});
	}
	
	/**
	 * 跳转到多条件查询结果页
	 * @param formData
	 */
	function toHwLedger( formData ){
		var url = basePath + "asset/hwLedger/queryHwLedgerMultiPage.do?formData=" + FW.stringify( formData );
		addTabWithTree( "queryHwLedger" + formData.hwId , "硬件台账多条件查询", url,"equipment", formData.hwId);
	}
	