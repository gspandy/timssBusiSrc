var InvMatRecipientsPriv={
	init:function(){
		InvMatRecipientsPriv.set();
		InvMatRecipientsPriv.apply();
	},
	set:function(){//定义权限
		//发料
		Priv.map("privMapping.materiReQ_applyend","materiReQ_applyend");
		//登记使用人
		Priv.map("privMapping.materiReQ_borrow","materiReQ_borrow");
		//保存附件
		Priv.map("privMapping.materiReQ_attach","materiReQ_attach");
	},
	apply:function(){//应用权限
		//应用
		Priv.apply();
	}
};

//自动提交
function autoCommitProcess(){
    var data={};
    data['taskId'] = taskId;
    data['message'] = "审批归档。";
    var url = 'workflow/process_inst/autoComplete.do';
    $.post(url, data, function(data){
        if(data.result=='ok'){
        	FW.success( "物资领用成功 ！");
        	FW.getFrame(FW.getCurrentTabId()).location.reload();
        }
    });
}