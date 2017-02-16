/**打开钥匙箱详情*/
function openKeyBoxPage(keyBoxId,keyBoxCode){
   var opts = {
        id : "openKeyBox"+keyBoxCode,
        name : "钥匙箱详情",
        url : basePath+ "ptw/ptwKeyBox/openPtwKeyBoxPage.do?id="+keyBoxId,
        tabOpt : {
        	closeable : true,
        	afterClose : "FW.deleteTab('$arg'); FW.activeTabById('ptw');FW.getFrame('ptw').refresh();"
        }
    };
    _parent()._ITC.addTabWithTree(opts); 
}

/** 新建钥匙箱*/
function newKeyBox(){
 //新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
   var rand = new Date().getTime() + Math.floor(Math.abs(Math.random()*100));
   var opts = {
        id : "newKeyBox"+rand,
        name : "新建钥匙箱",
        url : basePath+ "ptw/ptwKeyBox/openPtwKeyBoxPage.do",
        tabOpt : {
        	closeable : true,
        	afterClose : "FW.deleteTab('$arg'); FW.activeTabById('ptw');FW.getFrame('ptw').refresh();"
        }
    };
    _parent()._ITC.addTabWithTree(opts); 
}