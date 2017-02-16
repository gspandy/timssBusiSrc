/**
 * @param ptoData
 * 含初始化数据的新建操作票
 * @param
 * sptoId = "sdsrewffdg";
 * 
 */
function  newPtoPageWithData(sptoId){
	 //新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
    var rand = new Date().getTime() + Math.floor(Math.abs(Math.random()*100));
    var tabId = "newPto" + rand;
    var urlPath = basePath+ "ptw/ptoInfo/newPto.do?sptoInfoId="+sptoId;  
    var opts = {
        id : tabId,
        name : "新建操作票",
        url : urlPath,
        tabOpt : {
        	closeable : true,
        	afterClose : "FW.deleteTab('$arg'); FW.activeTabById('ptw');FW.getFrame('ptw').refreshAfterClose();"
        }
    };
    _parent()._ITC.addTabWithTree(opts); 
}

/**
 * 含初始化数据的新建标准操作票
 * @param ptoInfoId 操作票id
 * @param title 页内标题
 * @param sourceTabId 触发这个功能所在页的tabid
 * @param modify C 创建标准操作票
 */

function  newSptoPageWithData(ptoInfoId,title,sourceTabId,modify){
	//新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
    var rand = new Date().getTime() + Math.floor(Math.abs(Math.random()*100));
    var tabId = "initNewSpto" + rand;
    var urlPath = basePath+ "ptw/sptoInfo/newSpto.do?"+ $.param({title:title,ptoInfoId:ptoInfoId,modify:modify});  
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