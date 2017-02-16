var form = [];
$(document).ready(function(){
	respond.update();
	form= [
		{title : "工号",id : "uid",type:"label"},
		{title : "姓名",id : "name",rules : {	required : true,maxlength : 10}},
		{title : "职务",id : "job",rules : {maxlength : 128}},
		/*{title : "职位",id : "title",	rules : {maxlength : 30}},*/
		{title : "电子邮箱",id : "email",rules : {email : true,maxlength : 50}},
		{title : "手机号码",id : "mobile",	rules : {maxlength : 30}},
		{title : "办公电话",id : "officetel",rules : {maxlength : 30}},
		{title : "微波电话", id : "microtel",	rules : {maxlength : 20}},
		{title : "入职日期", id : "arrdate",dataType:"date",type:"date"},
		{title : "离职日期", id : "resdate",dataType:"date",type:"date"},
		{title : "办公地址", id : "officeaddr"},
		{
			title : "账户类型", 
			id : "type",
			type:"label",
			nouse : mode=="create",
			formatter : function(val){
				return val=="YES"?"域账号":"系统账号";
			}
		},
		{
			title : "状态", 
			id : "status",
			type : "label",
			nouse : mode=="create",
			formatter : function(val){
				return val=="YES"?"启用":"<span style='color:#f00'>禁用</span>";
			}
		}
	];
	
	$("#form_baseinfo").ITC_Form({fixLabelWidth:true,validate:true},form);
	if(mode!="create"){
		$("#form_baseinfo").ITC_Form("loaddata",g);
		$("#form_baseinfo").ITC_Form("readonly");
		$("#btnResetPass").show();
	}
	else{
		showEditBtn();
		$("#f_orgs").html("无");
	}
	if(!g.status || g.status=='NO'){
		$("#btnDisable").html("启用");
	}
	if(g.type=="YES"){
		//$("#btnResetPass").parent().hide();
	}
});

function save(){
	if(!$("#form_baseinfo").valid()){
		return;
	}
	f = $("#form_baseinfo").ITC_Form("getdata");
	if(mode!="create"){
		f["uid"] = g.uid; 
	}
	$.ajax({
		url : basePath + "attuser?method=" + mode,
		data : f,
		type : "post",
		dataType : "json",
		success : function(data){
			if(data.status==1){
				_parent().Notice.successTopNotice(data.msg);
				goBack();
			}
			else{
				_parent().Notice.errorTopNotice(data.msg);
			}
		}
	});
}

function goBack(){
	window.location.href = basePath + jspPath + "attuser_list.jsp";
}


function beignEdit(){
	$("#form_baseinfo").ITC_Form("beginedit");
	$("#page_title").html("编辑人员");
	showEditBtn();
}

function showEditBtn(){
	$("#btnEdit").hide();
	$("#btnSave").show();	
	$("#btnDisable").parent().hide();
}

