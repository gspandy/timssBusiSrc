//表单字段定义 
var fields = [
	{title : "ID", id : "id",type : "hidden"},
	{title : "优先级名称",id : "name", rules : {required:true,maxlength:6},linebreak:true},
	{title : "解决时间(小时)", id : "solveLength", rules : {required:true,number:true}, linebreak:true, helpMsg:"无穷大请写-1"},
	{title : "排序",id : "sortNum", rules : {required:true,number:true}},
	{
		title : "说明",
		id : "remarks",
		type : "textarea",
		linebreak:true,
		wrapXsWidth:12,
		wrapMdWidth:8,
		rules : {maxlength:80},
		height:55
	}];

//页面初始化
$(document).ready(function() {
	$("#woPriorityForm").iForm("init",{"fields":fields,"options":{validate:true}});
	if(woPriorityId == null || woPriorityId == 'null'){
		woPriorityId = 0 ;
		currentSortNum = -1;
		$("#btn_edit").hide();
		$("#btn_delete").hide();
		FW.fixRoundButtons("#toolbar");
	}
	else{
		$.post(basePath + "workorder/woParamsConf/queryWoPriorityDataById.do",{"woPriorityId":woPriorityId},
			function(woPriorityData){
				$("#btn_save").hide();
				FW.fixRoundButtons("#toolbar");
				var woPriorityFormData = JSON.parse( woPriorityData.baseData );
				currentSortNum = woPriorityFormData.sortNum;
				$("#woPriorityForm").iForm("setVal",woPriorityFormData);
				$("#woPriorityForm").iForm("endEdit");
		},"json");
	}
});

//保存
function savePriority(){
	if(!$("#woPriorityForm").valid()){
		return ;
	}
	
	var woPriorityFormData = $("#woPriorityForm").iForm("getVal");
	var newSortNum = woPriorityFormData.sortNum;
	$.post(
			basePath + "workorder/woParamsConf/woPriorityListData.do",
			{"search":"{\"sortNum\":\""+woPriorityFormData.sortNum+"\"}"},
			function(data){
				//插入
				if(woPriorityId==0 && data.total>0){
					FW.error("该排序序号已存在，请重新选择");
					return;
				}
				//更新
				if(woPriorityId!=0 && newSortNum!=currentSortNum && (data.total>0)){
					FW.error("该排序序号已存在，请重新选择");
					return;
				}				
				woPriorityId = woPriorityFormData.id;
				if(woPriorityId==""){
					woPriorityId = 0;
				};
				$.post(basePath + "workorder/woParamsConf/commitWoPriority.do",
					{"woPriorityForm":FW.stringify(woPriorityFormData),"woPriorityId":woPriorityId},
					function(data){
						if(data.result == "success"){
							FW.success("保存成功");
							closeCurPage();
						}else {
							FW.error("保存失败");
						}
				},"json");
			},
		"json"
	);
}

//开始编辑
function editPriority(){
	$("#woPriorityForm").iForm("beginEdit");
	$("#btn_save").show();
	$("#btn_edit").hide();
	FW.fixRoundButtons("#toolbar");
}

//删除服务级别
function deletePriority(){
	Notice.confirm("删除确认|确定删除此优先级吗？",function(){
		$.post(basePath + "workorder/woParamsConf/deletePriority.do",
			{"priId":woPriorityId},
			function(data){
				if(data.result == "success"){
					FW.success("删除成功");
					closeCurPage();
				}else {
					FW.error("删除失败");
				}
			},"json");
	},null,"info");
}

//关闭
function closeCurPage(){
	FW.deleteTabById(FW.getCurrentTabId());
}