/**
 * 增加选项卡
 * 不带左侧导航树
 */
/**
 * pageId -- 新建页面的ID
 * pageName --新建页面的名字
 * pageUrl -- 打开新建页面的URL
 * sourceTabId -- 来源于那个页面的ID
 * modelTableId -- 模块ID
 */
function addTab( pageId, pageName, pageUrl,sourceTabId, modelTableId ){
	    FW.addTabWithTree({
	        id : pageId ,
	        url : pageUrl,
	        name :pageName,
	        tabOpt : {
	            closeable : true,
	            afterClose : "FW.deleteTab('$arg');FW.activeTabById('" + sourceTabId + "|" + modelTableId +"');"
	        }
	    });
	    FW.activeTabById(pageId);
}

//带导航树的addTab
/**
 * pageId -- 新建页面的ID
 * pageName --新建页面的名字
 * pageUrl -- 打开新建页面的URL
 * sourceTabId -- 来源于那个页面的ID
 * modelTableId -- 模块ID
 */
function addTabWithTree( pageId, pageName, pageUrl,sourceTabId, modelTableId ){
	 FW.addTabWithTree({
	        id : pageId ,
	        url : pageUrl,
	        name :pageName,
	        tabOpt : {
	            closeable : true,
	            afterClose : "FW.deleteTab('$arg');FW.activeTabById('" + sourceTabId + "|" + modelTableId +"');"
	        }
	    });
	FW.activeTabById(pageId);
}

//带导航树的addTab，符合timss风格,并刷新datagrid
/**
 * pageId -- 新建页面的ID
 * pageName --新建页面的名字
 * pageUrl -- 打开新建页面的URL
 * sourceTabId -- 来源于那个页面的ID
 * reloadTableId -- 来源页面的datagrid id
 * modelTableId -- 模块ID
 */
function addTabWithTreeDataGrid( pageId, pageName, pageUrl,sourceTabId,reloadTableId, modelTableId  ){
	 FW.addTabWithTree({
	        id : pageId ,
	        url : pageUrl,
	        name :pageName,
	        tabOpt : {
	            closeable : true,
	            beforeClose : "FW.getFrame('" + sourceTabId + "').$('#" + reloadTableId + "').datagrid('reload');",
	            afterClose : "FW.deleteTab('$arg');FW.activeTabById('" + sourceTabId  + "|" + modelTableId +"');"
	        }
	    });
	FW.activeTabById(pageId);
}
//关闭Tab
function closeTab(){
	FW.deleteTabById(FW.getCurrentTabId());
}
