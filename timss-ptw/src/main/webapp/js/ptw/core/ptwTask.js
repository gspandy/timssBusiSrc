/**关闭页面*/
function closePtw(DoNotRefresh){
	if(isNewWt){
		//第一个参数中|表示双行文字
		FW.confirm("确认|新建工作票未保存，是否关闭？",function(){
		    FW.deleteTabById(FW.getCurrentTabId());
		});
	}else{
		if(DoNotRefresh){
			FW.set("PtwDoNotRefresh",true);
		}
		//工作票插入后不做立即关闭操作
		if(FW.get("PtwAfterInsert")){
			FW.set("PtwAfterInsert",false);
			FW.set("PtwDoNotRefresh",false);
		}
		FW.deleteTabById(FW.getCurrentTabId());
	}
}

/**删除工作票*/
function deletePtw(){
	//第一个参数中|表示双行文字
	FW.confirm("确认|确定要删除工作票吗？删除后无法恢复。",function(){
	    $.post(basePath + "ptw/ptwInfo/deletePtw.do",{id:ptwId},function(data){
			if(data.result == "ok"){
				FW.success("删除工作票成功");
				closePtw();
			}else{
				FW.error("出现错误");
			}
		},"json");
	});
}
function printWt(){
	var template = param_config.noToPrint;
	if(!template){
		FW.error("未初始化工作票打印模板的配置");
		return;
	}
	var printUrl = "http://timss.gdyd.com/";
	var src = fileExportPath + "preview?__report=report/"+template+".rptdesign&__format=pdf&wt_id="+ptwId+
				"&author="+loginUserId+"&url="+printUrl;

	var url = encodeURI(encodeURI(src));

	var title ="工作票详情"
	FW.dialog("init",{
		src: url,
		btnOpts:[{
	            "name" : "关闭",
	            "float" : "right",
	            "style" : "btn-default",
	            "onclick" : function(){
	                _parent().$("#itcDlg").dialog("close");
	             }
	        }],
		dlgOpts:{ width:800, height:650, closed:false, title:title, modal:true }
	});
}

function initRemarkDiv(remark){
	var fields = [{id:"remark",title : "备注",type : "textarea",wrapXsWidth:12,wrapMdWidth:8}];
	$("#remarkForm").iForm('init',{"fields":fields,"options":{
	    initAsReadonly:true
	}}).iForm("setVal",{remark:remark});	
	$("#remarkDiv").show().iFold("init");
}

function remarkPtw(){
	var initRemark = $("#remarkForm").iForm("getVal").remark?$("#remarkForm").iForm("getVal").remark:"";
	FW.remark(function(val){
	    if(!val || $.trim(val) == ""){
	    	FW.error("请输入备注内容");
	    	return true;
	    }
	    if(val.length > 250){
	    	FW.error("备注内容请控制在250个字以内");
	    	return true;
	    }
	    $.post(basePath + "ptw/ptwInfo/remarkPtw.do",{id:ptwId,remark:val},function(data){
			if(data.result == "ok"){
				FW.success("增加备注成功");
				if($("#remarkDiv").css("display") == "none"){
					initRemarkDiv(val);
				}else{
					$("#remarkForm").iForm("setVal",{remark:val});
				}
			}else{
				FW.error("出现错误");
			}
		},"json");
	},{title:"增加备注",value:initRemark});
}

function initPtwChangeWpicDiv(){
	var changeWpicField = [
		{id:"chaOldWpic",title : "原工作负责人",type:"label"},
		{id:"chaNewWpic",title : "变更后工作负责人",type:"label"},
		{id:"chaWl",title : "许可人",type:"label"},
		{id:"chaSignTime",title : "变更时间",dataType:"datetime",type:"label"}
	];
	$("#changeWpicDiv").show().iFold("init");
	$("#changeWpicForm").iForm("init",{"fields":changeWpicField,"options":{initAsReadonly:true}});
	$("#changeWpicForm").iForm("setVal",ptwChangeWpic);
}

/**工作负责人变更*/
function changeWpic(){
	var chaOldWpic = ptwInfo.licWpic;
	var chaOldWpicNo = ptwInfo.licWpicNo;
	var src;
//	if(ptwTypeCode){
//		src = basePath + "page/ptw/core/moreChangeWpic.jsp?ptwTypeCode="+ptwTypeCode+"&chaOldWpic="+encodeURI(chaOldWpic);
//	}else{
//		src = basePath + "page/ptw/sbs/moreChangeWpic.jsp?chaOldWpic="+encodeURI(chaOldWpic);
//	}
	src = basePath + "ptw/ptwMore/moreChangeWpicPage.do?chaOldWpic="+encodeURI(chaOldWpic);
	if(ptwTypeCode){
		src = src+"&ptwTypeCode="+ptwTypeCode;
	}
	
	var dlgOpts = {
        width : 450,
        height:300,
        title:"变更工作负责人"
    };
	var btnOpts = [{
        "name" : "取消",
        "onclick" : function(){
            return true;
        }
    },{
        "name" : "确定",
        "style" : "btn-success",
        "onclick" : function(){
            //itcDlgContent是对话框默认iframe的id
            var p = _parent().window.document.getElementById("itcDlgContent").contentWindow;
            //获取弹出框的数据，如果验证不通过，返回值为null
            var form = p.getFormData();
            if(form != null){
            	form.chaOldWpic = chaOldWpic;
            	form.chaOldWpicNo = chaOldWpicNo;
            	form.wtId = ptwId;
            	$.post(basePath + "ptw/ptwMore/insertPtwChangeWpic.do",{changeWpicParam:FW.stringify(form)},function(data){
					if(data.result == "ok"){
						FW.success("变更工作负责人成功");
						_parent().$("#itcDlg").dialog("close");
						ptwChangeWpic = form;
						initPtwChangeWpicDiv();
						$("#btn_ChangeWpic").hide();
					}else{
						FW.error("出现错误");
					}
				},"json");
            }
        }
    }];
    FW.dialog("init",{"src":src,"dlgOpts":dlgOpts,"btnOpts":btnOpts});
}

function initPtwExtandDiv(){
	var extandField = [
		{id:"extApprTime",title : "批准延期到",type:"label",dataType:"datetime"},
		{id:"extWpic",title : "工作负责人",type:"label"},
		{id:"extWl",title : "许可人",type:"label"},
		{id:"extSignTime",title : "批准时间",type:"label",dataType:"datetime"}
	];
	$("#extandDiv").show().iFold("init");
	$("#extandForm").iForm("init",{"fields":extandField,"options":{initAsReadonly:true}});
	$("#extandForm").iForm("setVal",ptwExtand);
	
}

/**工作票延期*/
function extandPtw(){
	var latestWpicInfo = getLatestWpic();
	var latestWpic = latestWpicInfo.wpic;
	var latestWpicNo = latestWpicInfo.wpicNo;
	var src = basePath + "page/ptw/core/moreExtand.jsp?latestWpic="+encodeURI(latestWpic);//对话框B的页面
	var dlgOpts = {
        width : 450,
        height:300,
        title:"工作票延期"
    };
	var btnOpts = [{
        "name" : "取消",
        "onclick" : function(){
            return true;
        }
    },{
        "name" : "确定",
        "style" : "btn-success",
        "onclick" : function(){
            //itcDlgContent是对话框默认iframe的id
            var p = _parent().window.document.getElementById("itcDlgContent").contentWindow;
            //获取弹出框的数据，如果验证不通过，返回值为null
            var form = p.getFormData();
            if(form != null){
            	form.extWpic = latestWpic;
            	form.extWpicNo = latestWpicNo;
            	form.wtId = ptwId;
            	$.post(basePath + "ptw/ptwMore/insertPtwExtand.do",{extandParam:FW.stringify(form)},function(data){
					if(data.result == "ok"){
						FW.success("工作票延期成功");
						_parent().$("#itcDlg").dialog("close");
						ptwExtand = form;
						initPtwExtandDiv();
						$("#btn_ExtandPtw").hide();
					}else{
						FW.error("出现错误");
					}
				},"json");
            }
        }
    }];
    FW.dialog("init",{"src":src,"dlgOpts":dlgOpts,"btnOpts":btnOpts});
}

function initWaitRestoreDiv(){
	if(ptwWaitRestores == null){
		return;
	}
	$("#waitRestoreDiv").show();
	for(var i in ptwWaitRestores){
		addWaitForm(ptwWaitRestores[i]);
		if(ptwWaitRestores[i].resTime){
			addRestoreForm(ptwWaitRestores[i]);
		}
	}
	$("#waitRestoreDiv").iFold("init");
}

function addWaitForm(formValues){
	var waitField = [
		{id:"witTime",title : "工作票收回时间",type:"label",dataType:"datetime"},
		{id:"witWpic",title : "工作负责人",type:"label"},
		{id:"witWl",title : "许可人",type:"label"}
	];	
	$("#waitRestoreDiv").append(
		$("<form id='tmpWaitForm'></form>"));
	$("#tmpWaitForm").iForm("init",{"fields":waitField,"options":{initAsReadonly:true}}).iForm("setVal",formValues);
}
function addRestoreForm(formValues){
	var restoreField = [
		{id:"resTime",title : "重新开工时间",type:"label",dataType:"datetime"},
		{id:"resWpic",title : "工作负责人",type:"label"},
		{id:"resWl",title : "许可人",type:"label"}
	];	
	$("#waitRestoreDiv").append(
		$("<form id='tmpRestoreForm'></form>"));
	$("#tmpRestoreForm").iForm("init",{"fields":restoreField,"options":{initAsReadonly:true}}).iForm("setVal",formValues);
}

/**工作票收回*/
function waitPtw(){
	var latestWpicInfo = getLatestWpic();
	var latestWpic = latestWpicInfo.wpic;
	var latestWpicNo = latestWpicInfo.wpicNo;
	var src = basePath + "page/ptw/core/moreWait.jsp?latestWpic="+encodeURI(latestWpic);//对话框B的页面
	var dlgOpts = {
        width : 450,
        height:300,
        title:"工作票收回"
    };
	var btnOpts = [{
        "name" : "取消",
        "onclick" : function(){
            return true;
        }
    },{
        "name" : "确定",
        "style" : "btn-success",
        "onclick" : function(){
            //itcDlgContent是对话框默认iframe的id
            var p = _parent().window.document.getElementById("itcDlgContent").contentWindow;
            //获取弹出框的数据，如果验证不通过，返回值为null
            var form = p.getFormData();
            if(form != null){
            	form.witWpic = latestWpic;
            	form.witWpicNo = latestWpicNo;
            	form.wtId = ptwId;
            	$.post(basePath + "ptw/ptwMore/insertPtwWait.do",{waitParam:FW.stringify(form)},function(data){
					if(data.result == "ok"){
						FW.success("工作票收回成功");
						_parent().$("#itcDlg").dialog("close");
						form.id = data.ptwWaitRestoreId;
						ptwNeedRestore = true;
						if(ptwWaitRestores == null){
							ptwWaitRestores = [form];
							initWaitRestoreDiv();
						}else{
							ptwWaitRestores.push(form);
							addWaitForm(form);
						}
						$("#btn_WaitPtw").hide();
						$("#btn_RestorePtw").show();
						
					}else{
						FW.error("出现错误");
					}
				},"json");
            }
        }
    }];
    FW.dialog("init",{"src":src,"dlgOpts":dlgOpts,"btnOpts":btnOpts});
}

/**工作票重新开工*/
function restorePtw(){
	if(ptwWaitRestores == null || !ptwNeedRestore){
		FW.error("工作票不处于押票状态，不能进行重新开工操作");
		return;
	}
	var latestWpicInfo = getLatestWpic();
	var latestWpic = latestWpicInfo.wpic;
	var latestWpicNo = latestWpicInfo.wpicNo;
	var src = basePath + "page/ptw/core/moreRestore.jsp?latestWpic="+encodeURI(latestWpic);//对话框B的页面
	var dlgOpts = {
        width : 450,
        height:300,
        title:"重新开工"
    };
	var btnOpts = [{
        "name" : "取消",
        "onclick" : function(){
            return true;
        }
    },{
        "name" : "确定",
        "style" : "btn-success",
        "onclick" : function(){
            //itcDlgContent是对话框默认iframe的id
            var p = _parent().window.document.getElementById("itcDlgContent").contentWindow;
            //获取弹出框的数据，如果验证不通过，返回值为null
            var form = p.getFormData();
            if(form != null){
            	form.resWpic = latestWpic;
            	form.resWpicNo = latestWpicNo;
            	form.wtId = ptwId;
            	form.id = ptwWaitRestores[ptwWaitRestores.length - 1].id;
            	$.post(basePath + "ptw/ptwMore/upatePtwRestore.do",{restoreParam:FW.stringify(form)},function(data){
					if(data.result == "ok"){
						FW.success("重新开工成功");
						_parent().$("#itcDlg").dialog("close");
						
						ptwNeedRestore = false;
						ptwWaitRestores.resWpic = form.resWpic;
						ptwWaitRestores.resWpicNo = form.resWpicNo;
						ptwWaitRestores.resWl = form.resWl;
						ptwWaitRestores.resWlNo = form.resWlNo;
						ptwWaitRestores.resTime = form.resTime;
						addRestoreForm(form);
						$("#btn_WaitPtw").show();
						$("#btn_RestorePtw").hide();
						
					}else{
						FW.error("出现错误");
					}
				},"json");
            }
        }
    }];
    FW.dialog("init",{"src":src,"dlgOpts":dlgOpts,"btnOpts":btnOpts});
}

function initRemarkTaskDiv(){
	var fileds = [
		{id:"remarkWorkContent",title : "工作内容",type : "textarea",wrapXsWidth:12,wrapMdWidth:8},
		{id:"remarkWpic",title : "工作负责人",type:"label",linebreak:true},
		{id:"remarkWl",title : "许可人",type:"label"},
		{id:"remarkSignTime",title : "许可时间",type:"label",dataType:"datetime"}
	];
	$("#remarkTaskDiv").show().iFold("init");
	$("#remarkTaskForm").iForm("init",{"fields":fileds,"options":{initAsReadonly:true}});
	$("#remarkTaskForm").iForm("setVal",ptwRemarkTask);
}

/**增加任务*/
function remarkTask(){
	var latestWpicInfo = getLatestWpic();
	var latestWpic = latestWpicInfo.wpic;
	var latestWpicNo = latestWpicInfo.wpicNo;
	var src = basePath + "page/ptw/core/moreRemarkTask.jsp?latestWpic="+encodeURI(latestWpic);//对话框B的页面
	var dlgOpts = {
        width : 450,
        height:300,
        title:"增加任务"
    };
	var btnOpts = [{
        "name" : "取消",
        "onclick" : function(){
            return true;
        }
    },{
        "name" : "确定",
        "style" : "btn-success",
        "onclick" : function(){
            //itcDlgContent是对话框默认iframe的id
            var p = _parent().window.document.getElementById("itcDlgContent").contentWindow;
            //获取弹出框的数据，如果验证不通过，返回值为null
            var form = p.getFormData();
            if(form != null){
            	form.remarkWpic = latestWpic;
            	form.remarkWpicNo = latestWpicNo;
            	form.wtId = ptwId;
            	$.post(basePath + "ptw/ptwMore/insertRemarkTask.do",{remarkTaskParam:FW.stringify(form)},function(data){
					if(data.result == "ok"){
						FW.success("增加任务成功");
						_parent().$("#itcDlg").dialog("close");
						initRemarkTaskDiv();
						$("#btn_RemarkTask").hide();
						
					}else{
						FW.error("出现错误");
					}
				},"json");
            }
        }
    }];
    FW.dialog("init",{"src":src,"dlgOpts":dlgOpts,"btnOpts":btnOpts});
}


function initMoreInfo(ptwData){
	//初始化附加文件
	showUploadFileDiv(true);
	
	//工作负责人变更信息
	if(ptwData.ptwChangeWpic){
		ptwChangeWpic = ptwData.ptwChangeWpic;
		initPtwChangeWpicDiv();
	}
	//工作票延期
	if(ptwData.ptwExtand){
		ptwExtand = ptwData.ptwExtand;
		initPtwExtandDiv();
	}
	//间断和转移
	if(ptwData.ptwWaitRestores){
		ptwWaitRestores = ptwData.ptwWaitRestores;
		ptwNeedRestore = ptwData.ptwNeedRestore;
		initWaitRestoreDiv();
	}
	//新增任务
	if(ptwData.ptwRemarkTask){
		ptwRemarkTask = ptwData.ptwRemarkTask;
		initRemarkTaskDiv();
	}
	var ptwInfo = ptwData.ptwInfo;
	if(ptwInfo.remark){
		initRemarkDiv(ptwInfo.remark);
	}
}