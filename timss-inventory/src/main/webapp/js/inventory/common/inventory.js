FW.joinKey = function(obj){
	var arr = [];
	for(var k in obj){
		 if (obj.hasOwnProperty(k)) {
			 arr.push(k);
		 }
	}
	return arr.join(",");
};

FW.joinVal = function(obj){
	var arr = [];
	for(var k in obj){
		if (obj.hasOwnProperty(k)) {
			arr.push(obj[k]);
		}
	}
	return arr.join(",");
};

FW.showInventoryDialog = function(opts){
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
	opts.categoryname = opts.categoryname || "";
	//绑定分类是否启用
	opts.active = opts.active || "";
	//20160108 add by yuanzh 添加根据仓库id查找
	opts.warehouseid = opts.warehouseid || "";
	var treeSrc = basePath + "inventory/invitem/invItemTree.do?embbed=1&active="+opts.active+"&opentype=";
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
	if(opts.categoryname){
		treeSrc += "&categoryname="+opts.categoryname;
		listSrc += "&categoryname="+opts.categoryname;
	}
	//20160108 add by yuanzh 添加根据仓库id查找
	if(opts.warehouseid){
		treeSrc += "&warehouseid="+opts.warehouseid;
		listSrc += "&warehouseid="+opts.warehouseid;
	}
	
	if(opts.active){
		listSrc += "&active="+opts.active;
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

FW.showOnlyInventoryTree = function(opts){
	opts = opts || {};
	opts.basePath = opts.basePath || window.basePath || "";
	opts.multiSelect = opts.multiSelect || false;
	opts.bboxMode = opts.bboxMode  || true;
		
	var _body = $("body");
	if(!opts.bboxMode){
		_body.wrapInner("<div class='cbox' id='inventoryRightContainer' style='width:100%;height:100%'></div>");
	}
	var pl = parseInt(_body.css("padding-left"));
	_body.css({
		"padding-left":(pl + 230) + "px",
		width : "100%",
		height : "100%"
	}).addClass("bbox");
	
	var treeSrc = opts.basePath + "inventory/invitem/invItemTree.do?embbed=2&opentype=";
	if(opts.multiSelect){
		treeSrc += "&multi=1";
	}
	
	var tfHtml = '<iframe frameborder="no" border="0" class="tree-iframe" style="width:240px;height:100%;position:absolute;left:0px;top:0px;" id="itcEmbbedInventoryTree" src="' + treeSrc +' "></div>';
    _body.append(tfHtml);
};

//查看物资的详细信息
FW.showItemInfo = function(itemcode,warehouseid, invcateid){
	if( typeof(invcateid)==='undefined' || invcateid===null || invcateid==="null"){
		invcateid="";
	}
	FW.dialog("init",{
		src: basePath+"inventory/invitem/queryItemInfo.do?itemcode="+itemcode+"&warehouseid="+warehouseid+"&invcateid="+invcateid,
		btnOpts:[{
		            "name" : "关闭",
		            "float" : "right",
		            "style" : "btn-default",
		            "onclick" : function(){
		               return true;
		             }
		        }],
		dlgOpts:{width:924, height:260, closed:false, title:"物资详细信息", modal:true,idSuffix:"Items"}
		});
};

//物资树刷新
FW.itemTreeReflesh = function(){
	var itemTree = window.parent.$("#itemTree");
	if(itemTree.length>0){
		itemTree.get(0).contentWindow.$("#item_tree").tree('reload');
	}
};

//打开领料详细页面   
FW.showRecipientInfo = function(sheetno){
	$.ajax({
		type : "POST",
		async: false,
		url: basePath+"inventory/invmatrecipients/queryRecipientsIdBySheetNo.do",
		data: {"sheetNo":sheetno},
		dataType : "json",
		success : function(data) {
			var imrid = data.imrid;
			FW.dialog("init",{
				src: basePath+"inventory/invmatrecipients/invMatRecipientsForm.do?imrid="+imrid+"&embed=1",
				btnOpts:[{
				            "name" : "关闭",
				            "float" : "right",
				            "style" : "btn-default",
				            "onclick" : function(){
				                _parent().$("#itcDlg").dialog("close");
				             }
				        }],
				dlgOpts:{width:800, height:520, closed:false, title:"发料明细", modal:true}
			});
			
		} 
	});
};

//打开发料单页面   
FW.showRecipientPage = function(imrId){
	var currTabId = FW.getCurrentTabId();
	var urlPath = basePath+ "inventory/invmatrecipients/invMatRecipientsForm.do?imrid="+imrId;
	var opts = {
        id : "recipient" + imrId,
        name : "物资发料",
        url : urlPath,
        tabOpt : {
        	closeable : true,
        	//关闭发料单后，返回对应的领料单，并刷新页面数据（invMatApply.js中）
        	afterClose : "FW.deleteTab('$arg');FW.activeTabById('" + currTabId + "');FW.getFrame(FW.getCurrentTabId()).refreshPage()" 
        }
    };
    _parent()._ITC.addTabWithTree(opts); 
};

//打开退库详细弹出页面  sheetno:退库单no,imaid:物资申请单id
FW.showRefundInfo = function(sheetno,remark,imaid){
	$.ajax({
		type : "POST",
		async: false,
		url: basePath+"inventory/invmatrefund/queryRefundIdBySheetNo.do",
		data: {"sheetNo":sheetno},
		dataType : "json",
		success : function(data) {
			var imrsid = data.imrid;
			FW.dialog("init",{
				src: basePath+"inventory/invmatrefund/invMatRefundForm.do?imrsid="+imrsid+"&refundReason="+remark+"&sheetno="+sheetno+"&imaid="+imaid+"&embed=1",
				btnOpts:[{
				            "name" : "关闭",
				            "float" : "right",
				            "style" : "btn-default",
				            "onclick" : function(){
				                _parent().$("#itcDlg").dialog("close");
				             }
				        }],
				dlgOpts:{width:800, height:520, closed:false, title:"退库明细", modal:true}
			});
		} 
	});
};

FW.showErrorInfo = function(remark){
	FW.confirm("错误信息|"+remark,{});
};

//权限控制（判断是否具有列表操作权限）
FW.privilegeOperation = function(functionId,gridId){
	var isAllowEdit = privMapping[functionId];
	if(isAllowEdit == "1" && functionId.indexOf("listAllowEdit")>-1){
		listAllowEdit(oper,gridId);
	}else if(isAllowEdit == "1" && functionId.indexOf("btnAllowEdit")>-1){
		btnAllowEdit(oper);
	}else if(isAllowEdit == "1" && functionId.indexOf("attachAllowEdit")>-1){
		attachAllowEdit(oper);
	}
};

//列表是否可用操作
function listAllowEdit(oper,gridId){
	if("" != oper){
		var column = JSON.parse(oper);
		var columns = column.disableColumn;
		if(typeof(columns) != "undefined" && columns != ""){
			var colArr = columns.split("||");
			for(var i = 0;i < colArr.length; i++){
				$("#"+gridId).datagrid("getColumnOption",colArr[i]).editor = "{'type':'label'}";
			}
		}
		
		var hcolumns = column.hiddenColumn;
		if(typeof(hcolumns) != "undefined" && hcolumns != ""){
			var colArrHidden = hcolumns.split("||");
			for(var i = 0;i < colArrHidden.length; i++){
				$("#"+gridId).datagrid("hideColumn",colArrHidden[i]);
			}
		}
	}
}

//按钮是否可视
function btnAllowEdit(oper){
	if("" != oper){
		var btn = JSON.parse(oper);
		if("" != btn){
			var ebtn = btn.btnHide;
			if(typeof(ebtn) != "undefined" && ebtn != ""){
				var btnArr = ebtn.split("||");
				for(var i = 0;i < btnArr.length; i++){
					$("#"+btnArr[i]).hide();
				}
			}
		}
	}
}

//附件上传是否编辑
function attachAllowEdit(oper){
	if("" != oper){
		var form = JSON.parse(oper);
		if("" != form){
			var sForm = form.stopAttach;
			if(typeof(sForm) != "undefined" && sForm != ""){
				$("#"+sForm).iForm('endEdit');
			}
		}
	}
}

//增加关闭页面事件
function addFormCloseEvent(){
	var evtMap = _parent()._ITC.navTab.getEventMap();
    var tabId = FW.getCurrentTabId();
    evtMap[tabId].beforeClose = function(){
    	
    	if(null != formName){
    		currFormStatus = $("#"+formName).iForm("getVal");
    	}
    	
    	if(null != listName){
    		endEditAll();
    		currListStatus = FW.stringify($("#"+listName).datagrid("getRows"));
    		startEditAll();
    	}
    	
    	if(saveFlag){
    		return true;
    	}else{
    		if((JSON.stringify(initFormStatus)!=JSON.stringify(currFormStatus)) || 
        			(initListStatus!=currListStatus)){
        		FW.confirm("关闭？|确定关闭当前页面？关闭后未保存信息将丢失。",function(){
    	            delete(evtMap[tabId].beforeClose);
    	            FW.deleteTabById(tabId);
    	        });
        	}else{
        		 return true;
        	}
    	}
    };
}

//直接输入人员姓名查询
function getUserHint(f_id,f_name){
	//放大镜图标属于基础资源，可以直接引用
	var opts = {
		"datasource":basePath + "user?method=hint",
		"getDataOnKeyPress":true,
		"clickEvent":function(id,name){
			$("#" + f_id).val(id);
			$("#" + f_name).val(name);
		},
		"showOn":"input",
		"highlight":true,
		"formatter" : function(id,name){
			return name + " / " + id.split("_")[0];
		}
	};
	$("#" + f_name).iHint("init",opts);
}

FW.showWorkOrderInfo = function(workOrderNo,siteId,sheetno,applyType){
	var funcName = 'invOpenPageByWoCode';
	if(applyType == 'itsm_picking'){
		funcName = 'invOpenPageByItsmWoCode';
	}
	
	if(workOrderNo == 'workOrderNo'){
		$("table[fieldid='workOrderNo']").each(function(){
		      var span = $(this).find("span");
		      span.wrap("<a onclick='"+funcName+"(\""+span.text()+"\",\""+siteId+"\",\""+sheetno+"\")' style='display:block;width:100%'></a>");
		});
	}else{
		$("table[fieldid='outterNo']").each(function(){
		      var span = $(this).find("span");
		      span.wrap("<a onclick='"+funcName+"(\""+span.text()+"\",\""+siteId+"\",\""+sheetno+"\")' style='display:block;width:100%'></a>");
		});
	}
};

function invOpenPageByWoCode(woCode,siteid,sheetno){
    $.post('workorder/workorder/openPageByWoCode.do', {"woCode":woCode,"siteid":siteid}, function(data){
    	var woId = data.woId;
    	var woStatus = data.currStatus;
    	   //新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
        var rand = woCode;
        var opts = {
            id : "woPlanInfo" + rand,
            name : "工单详情",
            url : basePath+ "workorder/workorder/openWOPlanInfoPage.do?woId="+woId+"&woStatus="+woStatus ,
            tabOpt : {
            	closeable : true,
            	afterClose : "FW.deleteTab('$arg');FW.activeTabById('"+sheetno+"');"
            }
        };
        _parent()._ITC.addTabWithTree(opts); 
    });
};

function invOpenPageByItsmWoCode(woCode,siteid,sheetno){
    $.post('itsm/workorder/openPageByWoCode.do', {"woCode":woCode,"siteid":siteid}, function(data){
    	var woId = data.woId;
    	var woStatus = data.currStatus;
    	   //新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
        var rand = woCode;
        var opts = {
            id : "itsmPlanInfo" + rand,
            name : "工单详情",
            url : basePath+ "itsm/workorder/openWOPlanInfoPage.do?woId="+woId+"&woStatus="+woStatus ,
            tabOpt : {
            	closeable : true,
            	afterClose : "FW.deleteTab('$arg');FW.activeTabById('"+sheetno+"');"
            }
        };
        _parent()._ITC.addTabWithTree(opts); 
    });
};

FW.deleteDraft = function(url,sheetId){
	var tip = null;
	if(classType == "Processed"){
		tip = "作废审批";
	}else{
		tip = "删除草稿";
	}
	
	Notice.confirm(tip+"|是否确定要"+tip+"？",function(){
		$.ajax({
			url : url,
			dataType : "json",
			type : "POST",
			async: false,
			data : {
				"taskId" : taskId,
				"message" : processName + "--" + tip,
				"sheetId" : sheetId,
				"classType":classType
			},
			success : function(data) {
				if (data.result == "success") {
					FW.success(tip+"成功 ");
					saveFlag = true;
					closeCurTab();
				} else {
					FW.error(tip+"失败 ");
				}
			}
		});
	},null,"info");
};

/**
 * 初始化打印按钮
 * @param buttonId 打印按钮id，用于获取打印按钮
 * @param url 要打印的报表url，请注意url的__format参数要设置为pdf，同时不要带有__asattachment参数，如果设置该参数将不能预览.
 * @param title 打开的预览对话框的标题
 * @param extParams 预留的参数，暂时不使用
 */
FW.initPrintButton = function(buttonId,url,title,extParams){
	$(buttonId).click(function(){
		FW.dialog("init",{
			src: url,
			btnOpts:[
					{
					    "name" : "关闭",
					    "float" : "right",
					    "style" : "btn-default",
					    "onclick" : function(){
					        _parent().$("#itcDlg").dialog("close");
					    }
					}
		        ],
			dlgOpts:{ width:800, height:650, closed:false, title:title, modal:true }
		});
	});
};

FW.showStockInfo = function(imtid){
	var url = basePath+ "inventory/invmattran/invMatTranForm.do?imtid="+imtid+"&openType=read";
	var prefix = imtid;
    FW.addTabWithTree({
        id : "showMatTranForm" + prefix,
        url : url,
        name : "物资接收",
        tabOpt : {
            closeable : true,
            afterClose : "FW.deleteTab('$arg');FW.activeTabById(FW.getCurrentTabId());FW.getFrame(FW.getCurrentTabId()).refCurPage();"
        }
    });
};

function getEnumValueByCode(enumList,enumCode){
	var r="";
	for(var i in enumList){
		if(enumList[i][0]==enumCode){
			r=enumList[i][1];
			break;
		}
	}
	return r;
}

//打开主项目详情页面   
FW.showInvMatDetailPage = function(itemId,haswh){
	var currTabId = FW.getCurrentTabId();
	var urlPath;
	if(haswh){
		urlPath = basePath+ "inventory/invitem/invMainItemDetail.do?itemId="+itemId+"&haswh="+haswh;
	}
	else{
		urlPath = basePath+ "inventory/invitem/invMainItemDetail.do?itemId="+itemId;
	}
	
	var opts = {
        id : "invMainItemDetail" + itemId,
        name : "主项目物资",
        url : urlPath,
        tabOpt : {
        	closeable : true,
        	afterClose : "FW.deleteTab('$arg');FW.activeTabById('" + currTabId + "');" 
        }
    };
    _parent()._ITC.addTabWithTree(opts); 
};