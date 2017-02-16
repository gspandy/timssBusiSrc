function selectParticipantToFrom(formId){
	 
	setParticipantForDialog(formId);
	var src =  basePath + "page/attendance/core/select_role_user.jsp";
    var btnOpts = [{
            "name" : "取消",
            "float" : "right",
            "style" : "btn-default",
            "onclick" : function(){
                return true;
            }
        },{
            "name" : "确定",
            "float" : "right",
            "style" : "btn-success",
            "onclick" : function(){
            	var p = _parent().window.document.getElementById("itcDlgContent").contentWindow;
            	g.users = p.getChecked();
            	var count = 0;
            	for(var i in g.users)
        		{
            		if (g.users[i]!=null) {
						count+=1;
					}
        		}
            	if (count > 3) {
            		FW.error("最多只能选择三人!");
            		return;
				}
            	saveParticipantToFrom(formId);
            	_parent().$("#itcDlg").dialog("close");	
            }
        }
    ];
    var dlgOpts = {
        width : 450,
        height: 420,
        closed : false,
        title:"选择人员",
        modal:true
    };
    Notice.dialog(src,dlgOpts,btnOpts);
}
//为选人弹出框初始化准备数据
function setParticipantForDialog(formId){
	var data=$('#'+formId).iForm('getVal');
	var recomIds = "";
	var recomName = "";
	if(typeof(data.recomIds)!="undefined" && data.recomIds!=null && data.recomIds!="") {
		recomIds = data.recomIds.split(",");
		recomName = data.recomName.split(",");
	}
	var schedulePerson={};
	if(recomIds.length>0){
			for(var i=0;i<recomIds.length;i++){
				schedulePerson[recomIds[i]]=recomName[i];
			}
	}
	g.users = schedulePerson;
}
//保存修改的值班人员
function saveParticipantToFrom(formId){
	var strId = "";
	var strName="";
	for(var k in g.users){
		strId += k + ","
		strName += g.users[k]+",";
	}
	if(strId==""){
		strId = "";
		strName="";
	}else{
		strId = strId.substring(0, strId.length - 1);
		strName = strName.substring(0, strName.length - 1);
	}
	$("#"+formId).iForm("setVal",{"recomName":strName,"recomIds":strId});
	
	/*$.ajax({
		url : basePath + "operation/scheduleDetail/updateSchedulePerson.do",
		type : 'post',
		data :{
			nowScheduleId : OprNote.objs.nowScheduleId,
			userIdStr : FW.stringify(lst)
		},
		dataType : "json",
		success : function(data) {
			if( data.result == "success" ){
				FW.success( "值班人员修改成功 ！");
				OprNote.objs.schedulePerson=g.users;
				$("#baseform").iForm("setVal",{"dutyPerson":str});
			}else{
				FW.error( "值班人员修改失败 ！");
			}
		}
	});*/
}