function addEventTab(newId,pageName,pageUrl,oldId){
	var reflesh="var frame = FW.getFrame('"+oldId+"');if(frame.refreshFinancePage){frame.refreshFinancePage();}";
	FW.addTabWithTree({
        id : newId,
        url : pageUrl,
        name : pageName,
        tabOpt : {
            closeable : true,
            afterClose : "FW.deleteTab('"+newId+"');FW.activeTabById('"+oldId+"|attendance');"+reflesh
            /*
            function(newId){
                //至少要执行deleteTab这行，否则无法再次打开该选项卡
                FW.deleteTab(newId);
                //nav3是API选项卡的标识符
                FW.activeTabById(oldId+'|attendance');
                refreshFinancePage();
            }*/
        }
    });
}

//提交成功后关闭页面并提示提交成功信息
function closeTab(){
	submitDone();
	FW.deleteTabById(FW.getCurrentTabId());
}

//关闭页面
function closeTabUnMsg() {
	FW.deleteTabById(FW.getCurrentTabId());
}

function submitDone() {
	//_parent().Notice.successTopNotice("操作成功");
	FW.success("操作成功");
}

function showInWorkflowIframe(url,w,h){
	var src = basePath+url;
	var btnOpts = [{
			"name" : "取消",
			"float" : "right",
			"style" : "btn-default",
			"onclick" : function(){
				return true;
			}
		},{
			"name" : "确定",
			"float" : "right",
			"style" : "btn-success",
			"onclick" : function(){
				return true;
			}
		}
	];


	var dlgOpts = {
		width : w,
		height:h,
		closed : false,
		title:"明细",
		modal:true
	};
	Notice.dialog(src,dlgOpts,btnOpts);
}

function addTabWithTree(newId, pageName, pageUrl,oldId){
	var opts = {
		    id : newId,
		    name : pageName,
		    url : pageUrl,
		    tabOpt: {
		    	closeable : true
			},
		    afterClose : function(){
		    	_parent()._ITC.deleteTab(newId);
		    	_parent()._ITC.navTab.activeById(oldId);
		    }
		    
		};
	//opts.tabOpt.closable=true;
	  _parent()._ITC.addTabWithTree(opts);
}