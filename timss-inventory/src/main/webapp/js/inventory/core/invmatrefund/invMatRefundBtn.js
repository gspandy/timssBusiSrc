var InvMatRefundBtn={
	init:function(){
		InvMatRefundBtn.close();
		InvMatRefundBtn.submit();
	},
	//关闭按钮	
	close:function(){
		$("#btn_close").click(function(){
			FW.deleteTabById(FW.getCurrentTabId());
		});
	},
	//提交按钮
	submit:function(){
		$("#btn_submit").click(function(){
			commitMatRefund();
		});
	}
};
