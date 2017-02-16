function addEventTab(newId,pageName,pageUrl,oldId){
//	var reflesh="console.log('111');var frame = FW.getFrame('"+oldId+"');if(frame.refresh){frame.refresh();}";
	FW.addTabWithTree({
        id : newId,
        url : pageUrl,
        name : pageName,
        tabOpt : {
            closeable : true,
            afterClose : "FW.deleteTab('"+newId+"');FW.activeTabById('"+oldId+"|opmm');var frame = FW.getFrame('"+oldId+"');if(frame.refresh){frame.refresh();}"
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
	FW.success("操作成功");
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
//		    	_parent()._ITC.deleteTab(newId);
//		    	_parent()._ITC.navTab.activeById(oldId);
		    	FW.deleteTabById(newId);
		    	FW.activeTabById(oldId)
		    }
		    
		};
	//opts.tabOpt.closable=true;
	  _parent()._ITC.addTabWithTree(opts);
}