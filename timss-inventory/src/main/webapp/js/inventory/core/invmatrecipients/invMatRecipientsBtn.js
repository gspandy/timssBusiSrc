var InvMatRecipientsBtn = {
	init:function(){
		InvMatRecipientsBtn.close();
		InvMatRecipientsBtn.print();
		InvMatRecipientsBtn.submit();
		
		if(embed == "1"){
			$("#btn_close").hide();
			$("#btn_submit").hide();
		}else if(isEdit != "editable"){
			$("#btn_submit").hide();
		}
	},
	//关闭按钮	
	close:function(){
		$("#btn_close").click(function(){
			FW.deleteTabById(FW.getCurrentTabId());
		});
	},
	print:function(){
		var url = null;
		if(siteid == 'SJW'){
			url = fileExportPath+"preview?__format=pdf&__report=report/TIMSS2_SJW_SA_001_pdf.rptdesign&siteid="+siteid+"&sheetid="+imrid;
       	}else if(siteid == 'SWF'){
       		url = fileExportPath+"preview?__format=pdf&__report=report/TIMSS2_SWF_SO_001_pdf.rptdesign&siteid="+siteid+"&sheetid="+imrid;
       	}else if(siteid == 'ZJW'){
       		url = fileExportPath+"preview?__format=pdf&__report=report/TIMSS2_ZJW_IMR_001_pdf.rptdesign&siteid="+siteid+"&imrid="+imrid+"&imaid="+imaid;
       	}else{
       		url = fileExportPath+"preview?__format=pdf&__report=report/TIMSS2_IMR_001_pdf.rptdesign&siteid="+siteid+"&imrid="+imrid+"&imaid="+imaid;
       	}
		FW.initPrintButton("#btn_print",url,"发料打印预览",null);
	},
	submit:function(){
		$("#btn_submit").click(function(){
			var fileIds = $("#uploadform").ITC_Form("getdata");
			var uploadIds=JSON.stringify(fileIds.uploadfield);
			if( "ZJW" == siteid  && !$("#autoform").valid() ){
				FW.error( "发料时间不能为空" );
				return;
			}
			
			var listData =$("#matrecdetail_grid").datagrid("getRows");
			//清掉可能出现中文或换行符且用不着的字段 -- begin by gchw
			for(var i =0;i<listData.length;i++){
				listData[i]["cusmodel"]="";
				listData[i]["itemname"]="";
			}
			//清掉可能出现中文或换行符且用不着的字段 -- end
			$.ajax({
				type : "POST",
				async: false,
				url: basePath+"inventory/invmatrecipients/validateCanOut.do",
				data: { 
					"listData":FW.stringify(listData)
				},
				dataType : "json",
				success : function(data) {
					if(data.result == "success"){
						Notice.confirm("提交发料|是否确定要发放物资？",function(){
							var formData =$("#autoform").ITC_Form("getdata");
							//加载用户表单数据
							$.ajax({
								type : "POST",
								async: false,
								url: basePath+"inventory/invmatrecipients/saveMatRecipients.do",
								data: {
									"formData":FW.stringify(formData),
									"listData":FW.stringify(listData),
									"taskId":taskId,
									"imrid":imrid,
									"uploadIds":uploadIds
									},
								dataType : "json",
								success : function(data) {
									if(data.result == 'success'){
										autoCommitProcess();
									}else if(data.result == 'same'){
										FW.success( "该物资已被其他仓管员发放！");
										homepageService.refresh();
										FW.deleteTabById(FW.getCurrentTabId());
										FW.activeTabById("stock");
									}else{
										FW.error( "物资发放失败！");
									}
								},
								error : function(data) {
									FW.error(data.responseJSON.msg);
								}									
							});
						},null,"info");	
					}else{
						FW.error("物资编号为："+data.remark+"的数量不足发料要求");
					}
				}
			});
		});
	}
};