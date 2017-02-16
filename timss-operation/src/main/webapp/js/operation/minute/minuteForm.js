var fields = [
		{title : "会议编号", id : "id", type : "hidden"},
		{title : "日期", id : "issueDate",rules : {required:true}, dataType : "date", type : "date"},
		{title : "主持人", id : "presider", rules : {required:true}, linebreak:true,
			render : function(id){
				//在实际使用时还要指定clickEvent才有意义
					$("#" + id).initHintPersonList({
						clickEvent : function(id,name){
							$("#autoform").iForm("setVal",{presider:name});
						}
					});
			}
		},
		{title : "参会人员",id:"attendee",type : "textarea",linebreak:true,wrapXsWidth:12,
			wrapMdWidth:12,rules : {required:true,maxChLength:660}},
		{title : "会议内容",id:"minute",rules : {required:true},type : "textarea",linebreak:true,wrapXsWidth:12,
			wrapMdWidth:12,height:400}
];

var MinutePriv={
	init:function(){
		MinutePriv.set();
		MinutePriv.apply();
	},
	set:function(){//定义权限
		//新建
		Priv.map("privMapping.minute_edit","minute_edit");
		Priv.map("privMapping.minute_del","minute_del");
	},
	apply:function(){//应用权限
		//应用
		Priv.apply();
	}
};

//编辑表单加载数据（通用方法）
function init(){
	$("#autoform").iForm("init",{"fields":fields,"options":{validate:true}});
	$("#f_presider").attr("placeholder","请输入工号或姓名");
	changeMode(objs.type);
	if(objs.type!='create'){
		loadData();
	}
	
}

function changeMode(mode){
	if(!mode){
		mode='view';
	}
	if(mode=='view'){
		$("#autoform").iForm("endEdit");
		$("#btnSave,#btnCreate,#btnDel").hide();
		$("#btnEdit").show();
		$("#pageTitle").html("生产碰头会详情");
	}else if(mode=='create'){
		$("#autoform").iForm("beginEdit");
		$("#btnSave,#btnEdit,#btnDel").hide();
		$("#btnCreate").show();
		$("#pageTitle").html("新建生产碰头会");
	}else if(mode=='edit'){
		$("#autoform").iForm("beginEdit");
		$("#btnEdit,#btnCreate").hide();
		$("#btnSave,#btnDel").show();
		$("#pageTitle").html("编辑生产碰头会");
	}
	objs.type=mode;
}


function loadData(){
	$.ajax({
		type : "POST",
		url: basePath+"operation/minute/queryMinuteById.do",
		data: {"id" : objs.id},
		dataType : "json",
		success : function(data) {
			objs.minute=data;
			$("#autoform").iForm("setVal",objs.minute);
			var minuteContext = $("#readonly_f_minute").html();
			$("#readonly_f_minute").html(minuteContext.replaceAll("BRBR","<br/>"));
		}
	});
}

function update(){
	if(!$("#autoform").valid()){
		return;
	}
	var obj = $("#autoform").iForm("getVal");
	
	$.ajax({
		type : "POST",
		url: basePath+"operation/minute/saveOrupdateMinute.do",
		data: {"minute":FW.stringify(obj)},
		dataType : "json",
		success : function(data) {				
			if(data.result=="success"){
				FW.success("保存成功");
				changeMode("view");
			}else{
				FW.error("保存失败");
			}
		}
	});
}

function create(){
	if(!$("#autoform").valid()){
		return;
	}
	var obj = $("#autoform").iForm("getVal");
	
	$.ajax({
		type : "POST",
		url: basePath+"operation/minute/saveOrupdateMinute.do",
		data: {"minute":FW.stringify(obj)},
		dataType : "json",
		success : function(data) {				
			if(data.result=="success"){
				FW.success("保存成功");
				closeTab();
			}else{
				FW.error("保存失败");
			}
		}
	});
}

function del(){
	Notice.confirm("确认删除|是否确定要删除生产碰头会记录吗？删除后无法恢复。",function(){
		$.post(basePath+"operation/minute/deleteMinute.do",{"id":objs.id},function(data){
			if(data.result == "success"){
			  FW.success("删除成功");
			  closeTab();
			}else{
				FW.error("删除失败");
			}
		},"json");
	});	
}
	
	