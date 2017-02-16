/**
 * @param sptwId
 * 含初始化数据的新建工作票
 * @param
 * sptoId = "sdsrewffdg";
 * 
 */
function  newPtwPageWithData(sptwId){
	var params;
	 //新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
	if(typeof(ptwTypeCodes)!="undefined"){
		params = {opType:"newPtw",sptwId:sptwId,ptwTypeCodes:ptwTypeCodes};
	}else{
		params = {opType:"newPtw",sptwId:sptwId};
	}
	
	var tabId = new Date().getTime();
	tabId = "ptwInfoDetail" + tabId;
	openNewTab(tabId,"新建工作票","ptw/ptwInfo/preQueryPtwInfo.do",params);
	
}

/**
 * 含初始化数据的新建标准工作票
 * @param ptwId 工作票id
 * @param modify C 创建标准工作票
 */

function  newSptwPageWithData(ptwId){
	//新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
    var rand = new Date().getTime() + Math.floor(Math.abs(Math.random()*100));
    var tabId = "addPtwStandard" + rand;
    var sourceTabId = "contentTb";
    var urlPath = basePath+ "ptw/ptwStandard/copyPtwToStdPtwPage.do?ptwId="+ptwId ;  
    var opts = {
        id : tabId,
        name : "新建标准工作票",
        url : urlPath,
        tabOpt : {
        	closeable : true,
        	afterClose : "FW.deleteTab('$arg'); FW.activeTabById('"+sourceTabId+"|ptw');FW.getFrame('ptw').refreshAfterClose();"
        }
    };
    FW.addTabWithTree(opts); 

}

/**
 * 含初始化数据的新建标准操作票
 * @param sptoInfoId 标准操作票id
 * @param title 页内标题
 * @param sourceTabId 触发这个功能所在页的tabid
 * @param modify Y 创建标准操作票
 */

function  newSptoPageWithSptoId(sptoInfoId,title,sourceTabId,modify){
	//新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
    var rand = new Date().getTime() + Math.floor(Math.abs(Math.random()*100));
    var tabId = "initNewSpto" + rand;
    var urlPath = basePath+ "ptw/sptoInfo/newSpto.do?sptoInfoId="+sptoInfoId+"&modify="+modify+"&title="+title;  
    var opts = {
        id : tabId,
        name : title,
        url : urlPath,
        tabOpt : {
        	closeable : true,
        	afterClose : "FW.deleteTab('$arg'); FW.activeTabById('"+sourceTabId+"|ptw');FW.getFrame('ptw').refreshAfterClose();"
        }
    };
    FW.addTabWithTree(opts); 

}



//标准工作票已审批通过的列表 FW.dialog
function showStdPtwDtlIframe(sptwFilterType){
	var pri_dlgOpts = {
			width : 750,
			height : 500,
			closed : false,
			title : "双击选择标准工作票",
			modal : true
		};
	if(arguments.length>0){
		var src = basePath + "ptw/ptwStandard/queryAllFinishPtwStandardListPage.do?sptwFilterType="+sptwFilterType;
	}else{
		var src = basePath + "ptw/ptwStandard/queryAllFinishPtwStandardListPage.do";
	}
	
	var btnOpts = [{
		"name" : "取消",
		"float" : "right",
		"style" : "btn-default",
		"onclick" : function() {
			return true;
		}
	}];

	FW.dialog("init", {
		"src" : src,
		"dlgOpts" : pri_dlgOpts,
		"btnOpts" : btnOpts
	});
}
