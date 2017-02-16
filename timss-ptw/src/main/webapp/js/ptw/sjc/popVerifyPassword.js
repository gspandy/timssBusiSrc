function issueIsl(){
	verifyIslPassword(id,400,"签发隔离证","签发人","以上隔离措施已正确完备，可执行。");
}
function licIsl(){
	verifyIslPassword(id,500,"许可隔离证","许可人","以上隔离措施以全部执行，与我厂的安全规程相一致。与此相关的工作可以安全进行，并且警示牌已经挂在了所有隔离点上。");
}
function finishIsl(){
	verifyIslPassword(id,600,"结束隔离证","结束人","隔离证已经被注销，上面的隔离措施可以被接触。");
}
function endIsl(){
	verifyIslPassword(id,700,"终结隔离证","终结人","以上隔离措施已经被接触，与我厂的安全规程相一致。并且警示牌已经被收回。");
}
function cancelIsl(){
	verifyIslPassword(id,800,"作废隔离证","作废人","确定要作废吗？");
}

function preLicIsl(){
	$("#moreBtnDiv .btn").hide();
	$("#editBtn").show();
	$("#addSafeBtn").show();
	$("#permitCfmBtnDiv").show();
	$("#permitBtnDiv").hide();
	FW.fixToolbar("#toolbar");
	
//	$("#baseInfoForm").iForm("show",["keyBoxNo"]);
//	$("#baseInfoForm").iForm("beginEdit","keyBoxNo");
	$("#execComboDiv").show();
	beginEditExecAndRemover("IsolationSafe",400);
	beginEditExecAndRemover("IsolationElec",400);
	beginEditExecAndRemover("IsolationJx",400);
	beginEditExecAndRemover("CompSafe",400);
}

function preEndIsl(){
	$("#moreBtnDiv .btn").hide();
	$("#endCfmBtnDiv").show();
	$("#endBtnDiv").hide();
	FW.fixToolbar("#toolbar");
	
	$("#execComboDiv").show();
	beginEditExecAndRemover("IsolationSafe",600);
	beginEditExecAndRemover("IsolationElec",600);
	beginEditExecAndRemover("IsolationJx",600);
	beginEditExecAndRemover("CompSafe",600);
}


/**
 * 打开确认密码的弹出框
 * @param {} id 隔离证id
 * @param {} status 隔离证状态
 * @param {} title 弹出框的标题
 * @param {} statType 签发人/许可人/终结人等
 * @param {} desc 提示信息
 */
function verifyIslPassword(id,status,title,statType,desc){
	if(!$("#baseInfoForm").valid()){
		return false;
	}
	var formData = $("#baseInfoForm").iForm("getVal");
	if("请从左边设备树选择" == formData.eqName || "请从左边设备树选择" == formData.eqNo ){
		FW.error( "请从左边设备树选择一项！");
		return false;
	} 
	var validSafeResult = validSafeDatagrid(islStatus,isCopy);
	if(!validSafeResult.valid){
		return false;
	}
	//关联钥匙箱变为不可编辑
	endEditRelateKeyBox();
		
	var src = basePath + "page/ptw/sjc/popVerifyPassword.jsp?status="+status+"&desc="
		+encodeURI(desc)+"&statType="+encodeURI(statType);//对话框B的页面
	var dlgOpts = {
        width : 450,
        height:300,
        title:title
    };
	var btnOpts = [{
        "name" : "取消",
        "onclick" : function(){
            return true;
        }
    },{
        "name" : "确定",
        "style" : "btn-success",
        "onclick" : function(){
        	//同步方法
        	$.ajaxSetup({'async':false}); 
        	//保存基本信息
        	saveFormAndDatagrid( false, "保存基本信息出错！" );
        	
            //itcDlgContent是对话框默认iframe的id
            var p = _parent().window.document.getElementById("itcDlgContent").contentWindow;
            //获取弹出框的数据，如果验证不通过，返回值为null
            var form = p.getFormData();
            if(form != null){
            	var password = form.password;
            	var issueSuper = form.issueSuper;
            	var issueSuperNo = form.issueSuperNo;
            	var userId = ItcMvcService.user.getUserId();
            	var finElecInfo = form.finElecInfo;
            	var keyBoxId = $("#baseInfoForm").iForm("getVal","keyBoxId");
            	$.post(basePath + "ptw/ptwIsolation/updatePtwIsolationStatusById.do",
            		{id:id,status:status,userId:userId,password:password,issueSuper:issueSuper,
            		issueSuperNo:issueSuperNo,finElecInfo: finElecInfo,keyBoxId:keyBoxId},
            		function(data){
					if(data.result == "success"){
						FW.success(title + "成功");
						_parent().$("#itcDlg").dialog("close");
						closeTab();
					}else if(data.result == 'wrongPassword'){
						FW.error("密码输入错误");
					}else{
						FW.error("出现错误");
					}
				},"json");
            }
            
            $.ajaxSetup({'async':true}); 
        }
    }];
    FW.dialog("init",{"src":src,"dlgOpts":dlgOpts,"btnOpts":btnOpts});
}

//初始化remark
function initRemarkDiv(remark){
	var fields = [{id:"remark",title : "备注",type : "textarea",wrapXsWidth:12,wrapMdWidth:8}];
	$("#remarkForm").iForm('init',{"fields":fields,"options":{
	    initAsReadonly:true
	}}).iForm("setVal",{remark:remark});	
	$("#remarkFormDiv").iFold("init");
	$("#remarkDiv").show();
}

//备注弹出框
function remarkIsl( id , oldRemark ){
	FW.remark(function(val){
	    if(!val || $.trim(val) == ""){
	    	FW.error("请输入备注内容");
	    	return true;
	    }
	    if(val.length > 250){
	    	FW.error("备注内容请控制在250个字以内");
	    	return true;
	    }
	    $.post(basePath + "ptw/ptwIsolation/updatePtwIsolationRemarkById.do",{id:id,remark:val},function(data){
			if(data.result == "success"){
				FW.success("增加备注成功");
				if($("#remarkDiv").css("display") == "none"){
					initRemarkDiv(val);
				}else{
					$("#remarkForm").iForm("setVal",{remark:val});
				}
			}else{
				FW.error("出现错误");
			}
		},"json");
	},{title:"增加备注",value: oldRemark });
}