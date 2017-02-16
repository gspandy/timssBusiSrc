function openNewTab(tabId,title,url,params){
    url = basePath + url;
    if(params){
    	url += "?params="+encodeURIComponent(JSON.stringify(params));
    }
    var opts = {
        id : tabId,
        name : title,
        url : url,
        tabOpt : {
        	closeable : true,        	
		    afterClose : "FW.deleteTab('$arg'); FW.activeTabById('ptw');FW.getFrame('ptw').searchAfterClosePtw();"
        }
    };
    _parent()._ITC.addTabWithTree(opts); 	
}

function openNewFireTab(tabId,title,url,params){
    url = basePath + url;
    var currTabId = FW.getCurrentTabId();
    if(params){
    	url += "?params="+encodeURIComponent(JSON.stringify(params));
    }
    var opts = {
        id : tabId,
        name : title,
        url : url,
        tabOpt : {
        	closeable : true,
        	afterClose :"FW.deleteTab('$arg');FW.activeTabById('" + currTabId + "|ptw');"
        }
    };
    _parent()._ITC.addTabWithTree(opts); 	
}