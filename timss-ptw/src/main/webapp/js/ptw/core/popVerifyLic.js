var currFrame = FW.getFrame(FW.getCurrentTabId());
var safeItems = currFrame.getLicSafeItems();
var _dialogEmmbed = true;
var datagrid = undefined;
var safeDatas = {rows:safeItems,total:safeItems.length};
$(document).ready(function() {
	$("#execCombo").iCombo("init",{
		onChange:function(val){
			if(datagrid){
				$("#safeTable").prev(".datagrid-view2").children(".datagrid-body").find(".datagrid-row")
				.find("select").each(function(){
					$(this).iCombo("setVal",val);
				});
			}
		},
		allowSearch:true
	});
	
	$("#safeExecCheck").iCheck({
        checkboxClass: 'icheckbox_flat-blue',
        radioClass: 'iradio_flat-blue'
    });
	$("#safeExecCheck").on('ifChecked', function(event){
		$("#execComboWrap").show();
		$("#checkDiv").css("width",90);
	});
	$("#safeExecCheck").on('ifUnchecked', function(event){
		$("#execComboWrap").hide();
		$("#checkDiv").css("width",124);
	});
	
	$.post(basePath+ "ptw/ptwInfo/queryPtwUsersByGroup.do",{"role" : "PTW_safe_exec","hasOther":"select"},function(data){
		$("#execCombo").iCombo("loadData",data);
		initDatagrid(data);
	},"json");
	
	var fields = [
		{id:"issueName",title : "许可人",type:"label",value:userName,wrapXsWidth:5,wrapMdWidth:5},
		{id:"password",title : "密码",dataType:"password", rules : {required:true},wrapXsWidth:5,wrapMdWidth:5}
	];
	if(isFireWt){
		fields = fireFields;
	}
	$("#bizForm").iForm("init",{"fields" : fields,"options":{validate:true}});
});

function initDatagrid(data){
	var safeTypes = ["必须采取的安全措施","检修必须采取的安全措施","运行必须采取的安全措施","应装接地线","补充安全措施","保留带电部分"];
	var columns = [[
		{field:'safeType',title:'',hidden:true},
		{field:'safeOrder',title:'',width:20,fixed:true},
		{field:'safeContent',title:'安全措施内容',width:320},
		{field:'executerNo',title:'执行人',width:120,fixed:true,'editor':{
		    "type" : "combobox",
		    "options" : {
		        "data" : data,
				 allowSearch:true             
		    }
		}}
	]];
	
	datagrid = $("#safeTable").datagrid({
		width:775,
		height:isFireWt?270:300,
		columns:columns,
		singleSelect:true,
		fitColumns:true,
        data:safeDatas,
        collapsible:true,
        view:groupview,
        groupField:'safeType',
        groupFormatter:function(value,rows){
            return safeTypes[parseInt(value)-1];
        }
	});
	
	for(var i = 0 ; i <safeDatas.total; i++){
		datagrid.datagrid('beginEdit',i);
	}
}

function commonFormData(){
	var index = 0;
	$("#safeTable").prev(".datagrid-view2").children(".datagrid-body").find(".datagrid-row")
	.find("select").each(function(){
		if($(this).iCombo("getVal") == -1){
			FW.error("请选择执行人");
			index = -1;
			return false;
		}else{
			safeItems[index].executerNo = $(this).iCombo("getVal");
			safeItems[index].executer = $(this).iCombo("getTxt");
			index++;
		}
	});
	
	if(index == -1 ||  ! $("#bizForm").valid()){
		return null;
	}
	var formData = $("#bizForm").iForm("getVal");
	return formData;
}