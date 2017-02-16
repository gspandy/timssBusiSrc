/* 表单字段定义 ZJW具有风电场字段*/
var fields = [ {title : "ID",id : "id",type : "hidden"}, 
               {title : "SPTOID",id : "sptoId",type : "hidden"},
               {title : "编号",	id : "code",	type : "hidden"}, 
               {id:"windStation",title : "风电场",type:"combobox",
            	   dataType:"enum",
            	   enumCat:"WO_WIND_STATION",
            	   options : {allowEmpty : true},
            	   rules : {
						required : true
					}
            },
               {title : "操作任务",	id : "task",
					rules : {
						maxChLength : 600,
						required : true
					},
					wrapXsWidth : 12,
					wrapMdWidth : 12,
					type : "text"
               }, 
               {title : "状态",	id : "currStatus",	type : "combobox",dataType : "enum",
					enumCat : "PTW_PTO_STATUS",
					rules : {
						required : true
					}	
              }, 
				{title : "关联设备",	id : "assetId",	linebreak : true, type : "hidden"}, 
				{title : "关联设备",
					id : "assetName",
					type : "label",
					rules : {
						required : true
					}
				}, 
				{title : "预计操作时间", id : "preBeginOperTime", type:"datetime", dataType:"datetime"},
				{title : "预计结束时间", id : "preEndOperTime", type:"datetime", dataType:"datetime",
	          	  rules : {greaterThan:"#f_preBeginOperTime"} 
	            },
				{title : "类型",id : "type",	type : "combobox",dataType : "enum",
					enumCat : "PTW_SPTO_TYPE",
					rules : {
						required : true
					}					
				}, 
				{title : "监护人",id : "guardian",type : "combobox",data:roleUserMap[0].PTOGUARDIAN,
					rules : {
						required : true
					}},
				{title : "发令人",	id : "commander",	type : "combobox",data:roleUserMap[0].PTOCOMMANDER,
					rules : {
						required : true
					}},
	            {title : "实际操作时间", id : "beginOperTime",	linebreak : true, type:"datetime", dataType:"datetime",
	            	rules : {
						required : true
					}
				},
				{title : "实际结束时间", id : "endOperTime", type:"datetime", dataType:"datetime",
	          	  rules : {required : true,greaterThan:"#f_beginOperTime"}
				},
	          	{title : "操作人",id : "operator",type : "combobox",data:roleUserMap[0].PTOOPERATOR,
	          		options : {allowEmpty : true},
	          		rules : {
						required : true
					}
				},
		            
	          	{title : "值班负责人",id : "ondutyPrincipal",type : "combobox",data:roleUserMap[0].PTOONDUTYP,
	          		options : {allowEmpty : true},
	          		rules : {
						required : true
					}
				},
		            	
				{title : "值长",	id : "ondutyMonitor",	type : "combobox",data:roleUserMap[0].PTOONDUTYM,
	          		options : {allowEmpty : true},
	          		rules : {
						required : true
					}
				},
				{
			        title : "操作项备注", 
			        id : "operItemRemarks",
			        type : "textarea",
			        linebreak:true,
			        wrapXsWidth:12,
			        wrapMdWidth:8,
			        height:110,
			        rules : {maxChLength:680}
			    },
				{
			        title : "是否合格",id : "isProper",type : "combobox",
			        options : {allowEmpty : true},
			        data : [
			            ["Y","是"],
			            ["N","否"]
			        ]
			    },
			    {
			        title : "存在的问题", 
			        id : "problem",
			        type : "textarea",
			        linebreak:true,
			        wrapXsWidth:12,
			        wrapMdWidth:8,
			        height:110,
			        rules : {required:true,maxChLength:680}
			    }
			];

/**
 * 关闭当前tab 页
 */
function closeCurPage(flag) {
	if (flag) {
		FW.set("PtolistDoNotRefresh", true);
	}
	FW.deleteTabById(FW.getCurrentTabId());
}

/**
 * 打印报表
 */ 
function print() {
	var ptoFormObj = $("#ptoForm").iForm("getVal");
	var ptoId = ptoFormObj.id;
	var printUrl = "http://timss.gdyd.com/";
	var url = fileExportPath +
			"preview?__report=report/TIMSS2_"+siteid+"_PTO.rptdesign&__format=pdf" + 
			"&id=" + ptoId + "&author=" + loginUserId + "&url=" + printUrl;
	var title = "操作票信息";
	FW.dialog("init", {
		src : url,
		btnOpts : [ {
			"name" : "关闭",
			"float" : "right",
			"style" : "btn-default",
			"onclick" : function() {
				_parent().$("#itcDlg").dialog("close");
			}
		} ],
		dlgOpts : {
			width : 800,
			height : 650,
			closed : false,
			title : title,
			modal : true
		}
	});

}

/**
* 显示流程信息
*/
function showDiagram(){
    var workFlow = new WorkFlow();
    workFlow.showDiagram(workflowKey);
}

/**
* 显示流程信息(审批时)
*/
function showAuditInfo(){
    var businessData={};
    var fields = [{
		title : "创建时间", 
	    id : "createtime",
	    type : "label"
		},{
		title : "修改时间", 
	    id : "updatetime",
	    type : "label"
	}];
	
	var data={'createtime':createtime,'updatetime':updatetime};
    businessData.fields = fields;
    businessData.data = data;
    
    var workFlow = new WorkFlow();
    
    workFlow.showAuditInfo(processInstId,JSON.stringify(businessData),auditInfoShowBtn,audit);
}

/**
 * 审批标准操作票记录
 */
function audit(type) {
	// 表单校验
	if (!$("#ptoForm").valid()) {
		return;
	}
	var ptoFormData = $("#ptoForm").iForm("getVal");
	var id = ptoFormData.id;
	var status = ptoFormData.status;
	var variables = FW.stringify([]);
	var ptoItemData = getOperSafeItemInputs("safeItemTest");
	var ptoItemObj = safeItemFilter(ptoItemData);
	if(!ptoItemObj.hasItemData){
		return;
	}
	ptoItemData = ptoItemObj.safeDatas;
	// 获取附件数据
	var ids = $("#uploadform").iForm("getVal")["uploadfield"];
	var ptoData = {"ptoFormData":ptoFormData,"ptoItemData":ptoItemData,"uploadIds":ids};
	
	// 下面的修改关联设备必须要在审批框弹出之前执行完，所有这里要用同步
	workFlowAudit(ptoData, processInstId, variables, id, status,type);
	
}
/**
 * 审批弹出框
 */
function workFlowAudit(ptoData, processInstId, variables, id, status,type) {
	var url = basePath + "workflow/process_inst/setVariables.do";
	var currStatus = $("#ptoForm").iForm("getVal","currStatus");
	var params = {};
	if (undefined == taskId) {
		taskId = ptoData.ptoFormData.taskId;
	}
	params['processInstId'] = processInstId;
	params['businessId'] = id;
	params['variables'] = variables;
	if("submit"==type){
		$.post(url, params, function(data) {
			if (data.result == 'ok') {
				var workFlow = new WorkFlow();
				workFlow.submitApply(taskId,FW.stringify(ptoData),closeCurPage,null,0,closeCurPage);
			}
		});
	}else {
		$.post(url, params, function(data) {
			if (data.result == 'ok') {
				var workFlow = new WorkFlow();
				workFlow.showAudit(taskId, FW.stringify(ptoData), agree,rollback, stop, null, 0);
			}
		});
	}	
}
/**
 * 审批的三个回调函数，分别是“同意”，“回退”，“终止”
 */
// 同意回调
function agree() {
	closeCurPage();
};
// 回退回调
function rollback() {
	closeCurPage();
};
// 终止回调
function stop(rowdata) {
	var data = {};
	data['processInstId'] = rowdata.processInstId;
	data['reason'] = rowdata.reason;
	data['businessId'] = id;
	var url = 'ptw/sptoInfo/stopSpto.do';
	$.post(url, data, function(data) {
		if (data.result == 'success') {
			FW.success("审批成功");
			_parent().$("#itcDlg").dialog("close");
			closeCurPage();
			homepageService.refresh();
		} else {
			FW.error("审批失败");
		}
	});
}

/**
 * 初始化附件
 */
function initUploadform() {
	var uploadFiles="";
	$("#uploadform").iForm('init', {
		"fields" : [
           	{
           		id:"uploadfield", 
           		title:" ",
           		type:"fileupload",
           		linebreak:true,
           		wrapXsWidth:12,
           		wrapMdWidth:12,
           		options:{
           			"uploader" : basePath+"upload?method=uploadFile&jsessionid=" + sessId,
           		    "delFileUrl" : basePath+"upload?method=delFile&key=" + valKey,
           			"downloadFileUrl" : basePath + "upload?method=downloadFile",
           			"swf" : basePath + "js/workorder/common/uploadify.swf",             			
           			"initFiles" : uploadFiles,
           			"delFileAfterPost" : true
           		}
           	}
           ],
		"options" : {
			"labelFixWidth" : 6,
			"labelColon" : false
		}
	});
}

function newSpto(){
	var ptoFormData = $("#ptoForm").iForm("getVal");
	var ptoInfoId = ptoFormData.id;
	var title = "新建标准操作票";
	var sourceTabId = FW.getCurrentTabId();
	var modify = "C";
	newSptoPageWithData(ptoInfoId,title,sourceTabId,modify);
}


function ptoCheck(){
	var ptoFormObj = $("#ptoForm").iForm("getVal");
	var ptoId = ptoFormObj.id;
    var src = basePath + "page/ptw/core/pto/ptoCheck.jsp";
    var btnOpts = [{"name" : "关闭",
		            "onclick" : function(){
		                return true;
		               }
		        	},
		           {
		            "name" : "确定",
		            "style" : "btn-success",
		            "onclick" : function(){
		                //itcDlgContent是对话框默认iframe的id
		                var p = _parent().window.document.getElementById("itcDlgContent").contentWindow;
		                var ptoCheckForm = p.$("#ptoCheckForm").iForm("getVal");
		                
		                $.post(basePath + "ptw/ptoInfo/checkPto.do",{"ptoId" : ptoId,
		                	"ptoCheckForm" :FW.stringify(ptoCheckForm)}, function(data) {
		        			if (data.result == "success") {
		        				FW.success("操作成功");
		        				closeCurPage();
		        			} else {
		        				FW.error("操作失败");
		        			}
		        		}, "json");
		                
		                return true;
		            }
		           }];
    var dlgOpts = {width : 400,height:300, title:"检查操作票"};
    FW.dialog("init",{"src":src,"dlgOpts":dlgOpts,"btnOpts":btnOpts}); 
}

//初始化左边的树
function initSideTree(){
	//添加左边类型树
	FW.toggleSideTree(false);
	FW.addSideFrame({
		src:basePath+"page/ptw/core/tree.jsp?jumpMode=func",
		id:"assetTree",
		conditions :[
			{tab:"^ptw$",tree:"^ptw_sptolist$"},
			{tab:"^ptw$",tree:"^ptw_pto_ptolist$"},
			{tab:"^initNewSpto.+"},
			{tab:"^PTO.+"},
			{tab:"^newPto.+"},
			{tab:"^ptwSpto.+"},
			{tab:"^ptwPto.+"}
		]
	}); 
}
//复制操作票
function copyPto(){
	//新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
    var rand = new Date().getTime() + Math.floor(Math.abs(Math.random()*100));
    var tabId = "newPto" + rand;
    var urlPath = basePath+ "ptw/ptoInfo/newPto.do?ptoId=" + ptoInfoVoData.id;
    var sourceTabId = FW.getCurrentTabId();
    var opts = {
        id : tabId,
        name : "新建操作票",
        url : urlPath,
        tabOpt : {
        	closeable : true,
        	afterClose : "FW.deleteTab('$arg'); FW.activeTabById('"+sourceTabId+"|ptw');FW.getFrame('ptw').refreshAfterClose();"
        }
    };
    _parent()._ITC.addTabWithTree(opts);
}

/**
 *左边树菜单选择触发事件(只有点击的树节点为设备节点，且表单状态为可编辑的status才触发此功能)
 */
function onTreeItemClick(data){
	if(!canSelectTree){
		return;
	}
	var selectType = data.faultTypeCode;
	
	$("#ptoForm").iForm("setVal",{
		assetId : data.id,
		assetName: data.text
    });
} 