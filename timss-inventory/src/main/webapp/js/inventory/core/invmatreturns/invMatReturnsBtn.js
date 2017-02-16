var InvMatReturnBtn={
	init:function(){
		InvMatReturnBtn.close();
		InvMatReturnBtn.save();
	},
	//关闭按钮	
	close:function(){
		$("#btn_close").click(function(){
			closeCurTab();
		});
	},
	//提交退货按钮
	save:function(){
		$("#btn_save").click(function(){
			commitMatReturns();
		});
	}
	
};
