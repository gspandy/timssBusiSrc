var HwModel={//basePath框架中已有
		objs:{},

		init:function(){
			HwModel.objs["isInit"]=false;
			
			HwModel.objs["form"]=[
 				{title : "类型",id : "modelType",
					type : "combobox",
			        dataType : "enum",
			        enumCat : "AST_HW_MODEL_TYPE",
			        value : "",
			        rules:{required : true},
			        options:{
			        	onChange:function(val,obj){
			        		if(HwModel.objs.isInit){
			        			$("#f_modelName").removeData("previousValue");
				        		$("#form_baseinfo").valid();
			        		}
			        	}
			        }
 				},
 				{title : "名称",id:"modelName",rules:{
 					required : true,maxChLength:parseInt(150*2/3),
					remote:{
						"url": basePath + "asset/hwModel/isNameExistByType.do",
						type:"post",
						data:{
							modelType : function(){return $("#f_modelType").iCombo("getVal","modelType");},
							modelName : function(){return $("#f_modelName").val();},
							modelId : function(){return HwModel.objs.hwModelBean?HwModel.objs.hwModelBean.modelId:"";}
						},
						complete:function(data){
							
						}
					}
 				}}
 			];
			$("#base_info").ITCUI_Foldable();
			$("#form_baseinfo").ITC_Form({fixLabelWidth:true,validate:true,labelFixWidth:120},HwModel.objs.form); //主卡片信息	
			HwModel.loadData();
			
			if(HwModel.objs.mode!="create")//查看和编辑模式下显示使用它的硬件台账
				HwModel.initHwLedgerList();
			
			HwModel.objs["isInit"]=true;
		},
		
		toEdit:function(){
			HwModel.objs["mode"]="edit";
			HwModel.changeShow();
		},

		toDelete:function(){
			Notice.confirm("确认删除|是否确定要删除硬件类型“"+HwModel.objs.hwModelBean.modelName+"”？该操作无法撤销。",function(){HwModel.delHwModel();});
		},
		toClose:function(){
			closeTab();
		},
		toBack:function(){//返回的操作
			if(HwModel.objs.mode=="view"){
				HwModel.toList();
			}else if(HwModel.objs.mode=="create"){
				HwModel.toList();
			}else if(HwModel.objs.mode=="edit"){
				HwModel.objs["mode"]="view";
				HwModel.loadData();
			}
		},
		
		toList:function(){
			FW.navigate(basePath+"asset/hwModel/listPage.do");
		},
		
		changeShow:function(){//切换模式
			if(HwModel.objs.mode=="view"){
				$("#pageTitle").html("硬件类型详情");
				$("#form_baseinfo").ITC_Form("beginedit");//临时使用，解决空字段在点击其他节点后不隐藏的问题
				$("#form_baseinfo").ITC_Form("readonly");
				
				$("#btnEdit,#btnBack").show();
				$("#btnCreate,#btnSave,#btnDel,#btnCancel").hide();
			}else if(HwModel.objs.mode=="create"){
				$("#pageTitle").html("新建硬件类型");
				$("#form_baseinfo").ITC_Form("beginedit");
				
				$("#btnCreate,#btnBack").show();
				$("#btnEdit,#btnDel,#btnSave,#btnCancel").hide();
			}else if(HwModel.objs.mode=="edit"){
				$("#pageTitle").html("编辑硬件类型");
				$("#form_baseinfo").ITC_Form("beginedit","modelName");

				$("#btnSave,#btnBack,#btnDel,#btnCancel").show();
				$("#btnCreate,#btnEdit").hide();
			}
		},
		
		loadData:function(){//加载数据
			if(HwModel.objs.mode=="view"||HwModel.objs.mode=="edit"){
				$("#form_baseinfo").ITC_Form("loaddata",HwModel.objs.hwModelBean);
			}else if(HwModel.objs.mode=="create"){
				if(!HwModel.objs.blankBean){
					//用于新建时填充表单的
					HwModel.objs["blankBean"]={
							modelType:"",	
							modelName:""
					};
				}
				/**加载空数据**/
				$("#form_baseinfo").ITC_Form("loaddata",HwModel.objs.blankBean);
			}
			HwModel.changeShow();
		},

		createHwModel:function(){
			if(!$("#form_baseinfo").valid()){
				//FW.error("提交的内容有错误的地方，请修改后重试");
				return;
			}
			var obj = $("#form_baseinfo").iForm("getVal");

			$.ajax({
				type : "POST",
				url: basePath+"asset/hwModel/insertHwModel.do",
				data: {"hwModelBean":FW.stringify(obj)},
				dataType : "json",
				success : function(data) {				
					if(data.status==1){
						FW.success("硬件类型创建成功");
						HwModel.objs["hwModelBean"]=data.hwModelBean;						
						//设为浏览模式并重载数据
						HwModel.objs["mode"]="view";
						HwModel.loadData();
					}else{
						FW.error("硬件类型创建失败");
					}
				}
			});
		},
		
		updateHwModel:function(){
			if(!$("#form_baseinfo").valid()){
				//FW.error("提交的内容有错误的地方，请修改后重试");
				return;
			}
			
			var params = $("#form_baseinfo").iForm("getVal");
			//补充缺少的参数
			params.modelId=HwModel.objs.hwModelBean.modelId;

			$.post(basePath + "asset/hwModel/updateHwModel.do",{
					"hwModelBean":FW.stringify(params)
				},function(data){
				if(data.status == "1"){
					FW.success("更新成功");
					HwModel.objs["hwModelBean"]=data.hwModelBean;
					HwModel.toBack();
				}else{
					FW.error("更新失败");
				}
			},"json");
		},
		
		delHwModel:function(){
			$.post(basePath + "asset/hwModel/deleteHwModel.do",{"hwModelId":HwModel.objs.hwModelBean.modelId},function(data){
				if(data.result == "ok"){
					FW.success("删除成功");
					HwModel.toList();
				}else{
					FW.error("删除失败"+","+"请稍后重试或联系管理员");
				}
			},"json");
		},

		initHwLedgerList:function(){
			$("#hwLedgerList_info").iFold("init");
			
			HwModel.objs["hwLedgerList"]=[[
			    {field:'hwName',title:'设备名称',width:200,fixed:true},
			    {field:'ip',title:'IP地址',width:120,fixed:true},
			    {field:'status',title:'状态',width:60,fixed:true,formatter:function(value,row,index){
			    	return FW.getEnumMap("AST_HW_SERVICE_STATUS")[value];
			    }},
			    {field:'empty',title:' ',width:200}
			]];   
			              			
			$("#hwLedgerList_table").datagrid({
			    columns:HwModel.objs.hwLedgerList,
			    fitColumns : true,
			    singleSelect:true,
			    data:HwModel.objs.hwModelBean.deviceList,
			    //data:[{hwName:"硬件台账01",ip:"10.0.0.0",status:"online"}],//测试数据
			    onLoadSuccess:function(data){
					if(data.total==0){
						$("#hwLedgerList_info").iFold("hide");
					}else{
						$("#hwLedgerList_info").iFold("show");
						$("#hwLedgerList_table").datagrid("resize");
					}
			    }
			});
		}
};