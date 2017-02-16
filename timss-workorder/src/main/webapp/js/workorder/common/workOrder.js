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

FW.showWorkOrderListDialog = function(opts){
	if(!opts){
		return;
	}
	opts.multiSelect = opts.multiSelect || false; 
	opts.allowEdit = opts.allowEdit || false;
	opts.basePath = opts.basePath || window.basePath || "";
	opts.width = opts.width || "75%";
	opts.height = opts.height || "75%";
	opts.spare = opts.spare || "";
	
	var treeSrc = "";// opts.treeSrc;
	var siteId = opts.siteId;
	//TODO 写死了，硬编码
	if(siteId == 'ITC'){
		treeSrc = basePath + "workorder/woParamsConf/openFaultTypeTreePage.do?embbed=0&opentype=";
	}else{
		var assetTreeRollBackFunc=function(attrs){
			window.parent.document.getElementById("itcDlgWorkOrderTree").contentWindow.onSelectAssetTreeNode=function(node){
				var url = basePath + "workorder/workorder/parentWOList.do?embbed=0&opentype=";
				url +="&selectTreeId="+node.id; 
				window.parent.$("#itcDlgWorkOrderPage").attr("src",url);
			};
		};
		var assetTreeRollbackFuncAttrs={
			result:'success'
		};
		FW.set("AssetTreeInitRollBackFunc",assetTreeRollBackFunc);
		FW.set("AssetTreeInitRollBackFuncAttrs",assetTreeRollbackFuncAttrs);
		treeSrc = basePath + "page/asset/core/assetinfo/assetTree.jsp?embbed=0&opentype=";
	}

	var listSrc = basePath + "workorder/workorder/parentWOList.do?embbed=3&opentype=";
	
	
	
	if(_parent().$("#itcDlgInventoryTree").length==1){
		_parent().$("#itcDlgInventory").dialog("open");
		_parent().$("#itcDlgInventoryTree").attr("src",treeSrc);
		_parent().$("#itcDlgInventoryPage").attr("src",listSrc);
		return;
	}else{		
		var dlgHtml = '<div id="itcDlgWorkOrder">' +
			'<div style="width:100%;height:100%;padding-left:240px;position:relative;overflow:hidden" class="bbox">' + 
			    '<iframe class="tree-iframe" frameborder="no" border="0" style="width:240px;height:100%;position:absolute;left:0px;top:0px;" id="itcDlgWorkOrderTree">' +
			    '</iframe>'+
			    '<iframe frameborder="no" border="0" style="width:100%;height:100%;" id="itcDlgWorkOrderPage">' +
			    '</iframe>' +
		    '</div>' + 
		'</div>' +
		'<div id="itcDlgWorkOrderBtn" style="display:none;" class="bbox itcdlg-btns">' +
	    	'<div id="itcDlgWorkOrderBtnWrap" style="width:100%;height:100%">' + 
	    	'</div>' +
		'</div>';
		_parent().$("body").append(dlgHtml);
	}
	var dlgOpts = {
		idSuffix : "WorkOrder",
		width : opts.width,
		height : opts.height,
		title : opts.title || "选择工单"
	};
	var btnOpts = [{
            "name" : "关闭",
            "onclick" : function(){
                _parent().$("#itcDlgWorkOrder").dialog("destroy");
				_parent().$("#itcDlgWorkOrder").remove();
				_parent().$("#itcDlgWorkOrderBtn").remove();
            }
        },{
        	"name" : "确定",
            "style" : "btn-success",
            "onclick" : function(){
            	var pa = _parent();
                var p = pa.window.document.getElementById("itcDlgWorkOrderPage").contentWindow;
                var result = p.getSelected();
                if(!result){
                	FW.error("未选择工单");
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
                _parent().$("#itcDlgWorkOrder").dialog("destroy");
				_parent().$("#itcDlgWorkOrder").remove();
				_parent().$("#itcDlgWorkOrderBtn").remove();
            }
        }
    ];
	FW.dialog("init",{btnOpts:btnOpts,dlgOpts:dlgOpts});
	_parent().$("#itcDlgWorkOrderTree").attr("src",treeSrc);
	_parent().$("#itcDlgWorkOrderPage").attr("src",listSrc);
};



//查看工单的详细信息
FW.showWorkOrderInfo = function(WorkOrderId){
	FW.dialog("init",{
		src: basePath+"inventory/invitem/queryWorkOrderBaseInfo.do?woId="+WorkOrderId,
		btnOpts:[{
		            "name" : "关闭",
		            "float" : "right",
		            "style" : "btn-default",
		            "onclick" : function(){
		               return true;
		             }
		        }],
		dlgOpts:{width:924, height:257, closed:false, title:"工单详细信息", modal:true,idSuffix:"Items"}
		});
};









