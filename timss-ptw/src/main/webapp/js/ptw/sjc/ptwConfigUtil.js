FW.joinKey = function(obj){
	var arr = [];
	for(var k in obj){
		arr.push(k);
	}
	return arr.join(",");
};

FW.joinVal = function(obj){
	var arr = [];
	for(var k in obj){
		arr.push(obj[k]);
	}
	return arr.join(",");
};

FW.showIsolationDialog = function(opts){
	opts = opts || {};
	opts.basePath = opts.basePath || window.basePath || "";
	opts.width = opts.width || "50%";
	opts.height = opts.height || "75%";
	//页面地址
	var src = basePath + "ptw/ptwIsolationPoint/selectIslMethodPage.do";
	var dlgOpts = {
		idSuffix : "Isolation",
		width : opts.width,
		height : opts.height,
		title : opts.title || "选择隔离方法"
	};
	var btnOpts = [{
            "name" : "关闭",
            "onclick" : function(){
                return true;
            }
        },{
        	"name" : "确定",
            "style" : "btn-success",
            "onclick" : function(){
            	var pa = _parent();
                var p = pa.window.document.getElementById("itcDlgIsolationContent").contentWindow;
                var result = p.getSelected();
                if(!result){
                	FW.error("未选择任何隔离方法");
                	return;
                }
                if(opts.idContainer){
                	$(opts.idContainer).val(FW.joinKey(result));
                }
                if(opts.nameContainer){
                	$(opts.nameContainer).val(FW.joinVal(result));
                }
				if(opts.onParseData && typeof(opts.onParseData)=="function"){
					opts.onParseData(p.getFullDataSelected());
				}
                _parent().$("#itcDlgIsolation").dialog("close");
            }
        }
    ];
	FW.dialog("init",{btnOpts:btnOpts,dlgOpts:dlgOpts,src:src});
};
//TODO
/**
 * 弹出隔离方法和隔离点的选择框
 * @param {} opts
 */
FW.showIslPointAndMethod = function(opts){
	if(!opts){
		return;
	}
	opts.multiSelect = opts.multiSelect || false;
	opts.allowEdit = opts.allowEdit || false;
	opts.basePath = opts.basePath || window.basePath || "";
	opts.width = opts.width || "75%";
	opts.height = opts.height || "75%";
	opts.spare = opts.spare || "";
	opts.invmatapply = opts.invmatapply || "";
	var treeSrc = basePath + "inventory/invitem/invItemTree.do?embbed=1&opentype=";
	var listSrc = basePath + "inventory/invitem/invTreeItemList.do?embbed=1&opentype=";
	if(opts.multiSelect){
		treeSrc += "&multi=1";
	}
	if(opts.spare){
		treeSrc += "&isspare=1";
		listSrc += "&isspare=1";
	}
	if(opts.invmatapply){
		treeSrc += "&invmatapply="+opts.invmatapply;
		listSrc += "&invmatapply="+opts.invmatapply;
	}
	
	if(_parent().$("#itcDlgInventoryTree").length==1){
		_parent().$("#itcDlgInventory").dialog("open");
		_parent().$("#itcDlgInventoryTree").attr("src",treeSrc);
		_parent().$("#itcDlgInventoryPage").attr("src",listSrc);
		return;
	}
	else{		
		var dlgHtml = '<div id="itcDlgInventory">' +
			'<div style="width:100%;height:100%;padding-left:240px;position:relative;overflow:hidden" class="bbox">' + 
			    '<iframe class="tree-iframe" frameborder="no" border="0" style="width:240px;height:100%;position:absolute;left:0px;top:0px;" id="itcDlgInventoryTree">' +
			    '</iframe>'+
			    '<iframe frameborder="no" border="0" style="width:100%;height:100%;" id="itcDlgInventoryPage">' +
			    '</iframe>' +
		    '</div>' + 
		'</div>' +
		'<div id="itcDlgInventoryBtn" style="display:none;" class="bbox itcdlg-btns">' +
	    	'<div id="itcDlgInventoryBtnWrap" style="width:100%;height:100%">' + 
	    	'</div>' +
		'</div>';
		_parent().$("body").append(dlgHtml);
	}
	var dlgOpts = {
		idSuffix : "Inventory",
		width : opts.width,
		height : opts.height,
		title : opts.title || "选择物资"
	};
	var btnOpts = [{
            "name" : "关闭",
            "onclick" : function(){
                _parent().$("#itcDlgInventory").dialog("destroy");
				_parent().$("#itcDlgInventory").remove();
				_parent().$("#itcDlgInventoryBtn").remove();
            }
        },{
        	"name" : "确定",
            //"name" : "添加",
            "style" : "btn-success",
            "onclick" : function(){
            	var pa = _parent();
                var p = pa.window.document.getElementById("itcDlgInventoryPage").contentWindow;
                var result = p.getSelected();
                if(!result){
                	FW.error("未选择物资");
                	return;
                }
                if(opts.idContainer){
                	$(opts.idContainer).val(FW.joinKey(result));
                }
                if(opts.nameContainer){
                	$(opts.nameContainer).val(FW.joinVal(result));
                }
				if(opts.onParseData && typeof(opts.onParseData)=="function"){
					opts.onParseData(p.getFullDataSelected());
				}
				//pa.$(pa.$("#itcDlgInventoryBtnWrap").children(".btn-group")[1]).children("button").html("继续添加");
                _parent().$("#itcDlgInventory").dialog("destroy");
				_parent().$("#itcDlgInventory").remove();
				_parent().$("#itcDlgInventoryBtn").remove();
            }
        }
    ];
	FW.dialog("init",{btnOpts:btnOpts,dlgOpts:dlgOpts});
	_parent().$("#itcDlgInventoryTree").attr("src",treeSrc);
	_parent().$("#itcDlgInventoryPage").attr("src",listSrc);
};