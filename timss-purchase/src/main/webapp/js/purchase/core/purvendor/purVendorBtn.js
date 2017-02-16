var VendorBtn={
	init:function(){
		VendorBtn.close();
		VendorBtn.submit();
		VendorBtn.edit();
		VendorBtn.back();
	},
	//关闭按钮	
	close:function(){
		$("#btn-close").click(function(){
			closeTab();
		});
	},
	//提交按钮
	submit:function(){
		$("#btn-submit").click(function(){
			if(!$("#autoform").valid()){
				return ;
			}
			var formData =$("#autoform").ITC_Form("getdata");
			//加载用户表单数据
			$.ajax({
				type : "POST",
				url: basePath+"/purchase/purvendor/commitVendor.do",
				data: {"formData":JSON.stringify(formData)},
				dataType : "json",
				success : function(data) {
					if( data.result == "success" ){
						saveFlag = true;
						FW.success( "操作成功 ");
						closeTab();
					}else{
						FW.error( "操作失败 ");
					}
				}
			});
		});
	},
	//编辑按钮
	edit:function(){
		$("#btn-edit").click(function(){
			editForm(edit_field);
			$('#autoform').iForm("endEdit","companyNo");
			$("#btn-edit").hide();
			$("#btn-submit").show();
			$("#pageTitle").html("编辑供应商");
			$("#btn-back").show();
			FW.fixToolbar("#toolbar_wrap1");
		});
	},
	//返回按钮
	back:function(){
		$("#btn-back").click(function(){
			window.location.href = basePath + "purchase/purvendor/purVendorForm.do?type=<%=type%>&companyNo=<%=companyNo%>";
		});
	}
};

var VendorListBtn={
		init:function(){
			VendorListBtn.advlocal();
			VendorListBtn.news();
		},
		//高级查询
		advlocal:function(){
			$("#btn_advlocal").click(function(){
			    if($(this).hasClass("active")){
			    	$("#btn_advlocal").removeClass("active");
			        $("#table_vendor").iDatagrid("endSearch");
			    }
			    else{
			    	$("#btn_advlocal").addClass("active");
			       	$("#table_vendor").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(arg){
			       		isSearchMode = true;
						return {"search":JSON.stringify(arg)};
					}});
			    }
			});
		},
		//新建
		news:function(){
			$("#btn_new,.btn_new").click(function(){
				var url = basePath+ "/purchase/purvendor/purVendorForm.do?type=new";
			    var prefix = new Date().getTime() + "" + Math.floor(Math.abs(Math.random()*100));
			    FW.addTabWithTree({
			        id : "newVendorForm" + prefix,
			        url : url,
			        name : "供应商",
			        tabOpt : {
			            closeable : true,
			            afterClose : "FW.deleteTab('$arg');FW.activeTabById('purchasing');FW.getFrame(FW.getCurrentTabId()).refCurPage();"
			        }
			    });
			});
		}
};