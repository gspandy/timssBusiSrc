/**
 * 增加选项卡
 * 不带左侧导航树
 */
function addTab( pageId, pageName, pageUrl,sourceTabId ){
	    FW.addTabWithTree({
	        id : pageId ,
	        url : pageUrl,
	        name :pageName,
	        tabOpt : {
	            closeable : true,
	            afterClose : "FW.deleteTab('$arg');FW.activeTabById('" + sourceTabId + "|attendance');"
	        }
	    });
	    FW.activeTabById(pageId);
}

//带导航树的addTab，符合timss风格 by huangliwei
function addTabWithTree( pageId, pageName, pageUrl,sourceTabId,reloadTableId ){
	 FW.addTabWithTree({
	        id : pageId ,
	        url : pageUrl,
	        name :pageName,
	        tabOpt : {
	            closeable : true,
	            beforeClose : "FW.getFrame('attendance').$('#" + reloadTableId + "').datagrid('reload');",
	            afterClose : "FW.deleteTab('$arg');FW.activeTabById('" + sourceTabId + "|attendance');"
	        }
	    });
	FW.activeTabById(pageId);
}

//带导航树的addTab for fullcalendar 
function addTabFullcalendar( pageId, pageName, pageUrl,sourceTabId,reloadTableId ){
	FW.addTabWithTree({
		id : pageId ,
		url : pageUrl,
		name :pageName,
		tabOpt : {
			closeable : true,
			//beforeClose : "FW.getFrame('attendance').$('#" + reloadTableId + "').fullCalendar( 'refetchEvents' );",
			beforeClose : "FW.getFrame('attendance').initMeeting();",
			afterClose : "FW.deleteTab('$arg');FW.activeTabById('" + sourceTabId + "|attendance');"
		}
	});
	FW.activeTabById(pageId);
}
//关闭Tab by huangliwei
function closeTab(){
	FW.deleteTabById(FW.getCurrentTabId());
}
