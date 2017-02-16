//带导航树的addTab，符合timss风格 by huangliwei
function addTabWithTree( pageId, pageName, pageUrl,sourceTabId,reloadTableId ){
	var tabOpt={
        closeable : true,
        afterClose : "FW.deleteTab('$arg');FW.activeTabById('" + sourceTabId + "|opmm');"
    };
	if(reloadTableId){
		tabOpt["beforeClose"]="FW.getFrame('opmm').$('#" + reloadTableId + "').datagrid('reload');";
	}
	FW.addTabWithTree({
        id : pageId ,
        url : pageUrl,
        name :pageName,
        tabOpt : tabOpt
    });
	FW.activeTabById(pageId);
}

//关闭Tab by huangliwei
function closeTab(){
	FW.deleteTabById(FW.getCurrentTabId());
}
