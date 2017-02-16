function hideDataGrid(dataGridId){
	$("#"+dataGridId).hide();
	
}
function showDataGrid(dataGridId){
	$("#"+dataGridId).hide();
	
}


/*function invOpenPageByWoCode(woCode,siteid){
	    $.post('workorder/workorder/openPageByWoCode.do', {"woCode":woCode,"siteid":siteid}, function(data){
	    	var woId = data.woId;
	    	var woStatus = data.currStatus;
	    	   //新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
	        var rand = rowData.workOrderCode;
	        var opts = {
	            id : "woPlanInfo" + rand,
	            name : "工单详情",
	            url : basePath+ "workorder/workorder/openWOPlanInfoPage.do?woId="+woId+"&woStatus="+woStatus ,
	            tabOpt : {
	            	closeable : true,
	            	afterClose : "FW.deleteTab('$arg');if(!FW.get('ptwDoNotClose')){FW.activeTabById('equmaintain');FW.getFrame('equmaintain').refresh();}FW.set('ptwDoNotClose',false);"
	            }
	        }
	        _parent()._ITC.addTabWithTree(opts); 
	    });
}
*/
