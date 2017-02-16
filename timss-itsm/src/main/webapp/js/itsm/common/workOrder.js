var loginUserSiteId = null;
var loginUserDeptId = null;

FW.showItsmWOListDialog = function(opts){
	if(!opts){
		return;
	}
	opts.multiSelect = opts.multiSelect || false; 
	opts.allowEdit = opts.allowEdit || false;
	opts.basePath = opts.basePath || window.basePath || "";
	opts.width = opts.width || "75%";
	opts.height = opts.height || "75%";
	opts.spare = opts.spare || "";
	
	var treeSrc = basePath + "itsm/itsmFaultType/openFaultTypeTreePage.do?embbed=0&opentype=";
	var listSrc = basePath + "itsm/workorder/parentWOList.do?embbed=3&opentype=";
	
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



/**  判断myId 是否在候选人 candidateUsers 中
 * @param candidateUsers
 * @param myId
 */
function isMyActivityWO(candidateUsers,myId){
	for(var i=0; i<candidateUsers.length; i++){
		if(candidateUsers[i] == myId ){
			auditInfoShowBtn = 1 ;
			return true;
		}
	}
	return false;
}


//判断是否是客服和工程师
function isCusSer(siteId){ 
	$.post(basePath + "itsm/workorder/loginUserIsCusSer.do",{},
		function(data){
			if(data.isCuser){
				isITCSer = true;
			}
			if(data.isEngineer){
				isEngineer = true;
			}
			if(data.isMtpCharge){
				isMtpCharge = true;
			}
			if(data.isItsmAdmin){
				isItsmAdmin = true;
			}
			if(data.isInfoCenterUser&&siteId==itsmSiteCode.INFOCENTERSITEID){
				isInfoCenterUser = true;
			}
			
		},"json");
}

function onlyShowBaseInfo(formId){
	$.post(basePath + "itsm/workorder/loginUserIsCusSer.do",{},
			function(data){
				if(!data.isEngineer){
					$("#"+formId).iForm("hide",["influenceScope","urgentDegreeCode","priorityId"]);
					$("#title_plan").iFold("hide");
					$("#title_report").iFold("hide");
					$("#title_feedback").iFold("hide");
					$("#woPlanDiv").hide();	
					FW.toggleSideTree(true);
				}
			},"json");
}

