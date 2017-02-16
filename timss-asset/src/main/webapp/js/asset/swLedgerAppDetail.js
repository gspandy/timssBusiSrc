//应用名称重名验证的app
jQuery.validator.addMethod("appName", function(value, element, params) {
	if(!App||!App.objs["isInit"]){
		return true;
	}	 
	var rows=App.getParent().$("#appsList_table").datagrid("getRows");
	for(var i=0;i<rows.length;i++){
		if(rows[i].rowId!=App.objs.appBean.rowId){
			if(rows[i].appName==value&&rows[i].appType==params.appType())
				return false;
		}
	}
	return true;
},'该应用名称已在软件台账中使用');

var App={//basePath框架中已有
		objs:{},

		getParent:function(){
			//var p=_parent().window.document.getElementById("navtab_equipment").contentWindow;
			var p=FW.getFrame(FW.getCurrentTabId());
			return p;
		},
		
		init:function(){
			_enum = App.getParent()._enum;//手动给软件应用类型枚举赋值
			App.objs["isInit"]=false;//标识是否初始化完成，是的话才允许字段校验
			
			App.objs["form"]=[
  				{title : "类型",id : "appType",
					type : "combobox",
			        dataType : "enum",
			        enumCat : "AST_SW_APP_TYPE",
			        rules:{required : true},
			        options:{
			        	onChange:function(val,obj){
			        		if(App.objs.isInit){
			        			App.valid();
			        		}
			        	}
			        }
 				},
 				{title : "名称",id:"appName",rules:{
 					required : true,maxChLength:parseInt(150*2/3),
					appName:{appType:function(){
						return $("#form_baseinfo").iForm("getVal","appType");
					}}
 				},"messages" : {
 					"appName" : "已存在相同类型相同名称的应用"
 				}},
 				{title : "所属软件台账",id:"swl",type:"label",formatter:function(value,row,index){
 					return FW.specialchars(App.objs.swName);
 				}},
 				{title : "所在硬件台账",id:"hwName",formatter:function(value,row,index){
 					return FW.specialchars(App.objs.appBean.hwlDevice.hwName);
 				},render:function(id){
 					$("#" + id).attr("icon","itcui_btn_mag").removeClass("form-control").ITCUI_Input();
 			        $("#" + id).next(".itcui_input_icon").on("click",function(){
 						FW.dialog("init",{
 							src: basePath+"page/asset/itc/hardware/hwTree.jsp?isShowCard=false",
 							btnOpts:[{
 						            "name" : "取消",
 						            "float" : "right",
 						            "style" : "btn-default",
 						            "onclick" : function(){
 						                _parent().$("#itcDlghwLedgerTree").dialog("close");
 						             }
 						        },
 						        {
 						            "name" : "确定",
 						            "float" : "right",
 						            "style" : "btn-success",
 						            "onclick" : function(){
 						                var p = _parent().window.document.getElementById("itcDlghwLedgerTreeContent").contentWindow;
 						                var node=p.$('#property_tree').tree("getSelected");
 						                //判断是否可使用的节点（服务器或虚机）
 						                if(node.obj.hwId&&(node.obj.hwType=="HW_L_SERVER"||node.obj.hwType=="HW_L_VM")){
 						                	$.ajax({
	 						       				type : "POST",
	 						       				url: basePath+"asset/hwLedger/queryHwLedgerDeviceById.do",
	 						       				data: {"hwId":node.obj.hwId},
	 						       				dataType : "json",
	 						       				success : function(data) {				
		 						       				App.objs.appBean.hwlDevice.hwId=node.obj.hwId||"";
		 						                	App.objs.appBean.hwlDevice.hwName=node.obj.hwName||"";
		 						                	App.objs.appBean.hwlDevice.ip=node.obj.ip||data.hwLedgerDeviceBean.ip||"";
		 						                	
		 						                	App.setAttrsToShow();
	 						       				}
	 						       			});
 						                	
 						                	_parent().$("#itcDlghwLedgerTree").dialog("close"); 
 						                }else{
 						                	FW.error("请选择一个服务器或虚机");
 						                }
 						            }
 						        }],
 							dlgOpts:{ idSuffix : "hwLedgerTree",width:240, height:400, closed:false, title:"选择所在硬件台账", modal:true }
 						});
 					 });

 				}},
 				{title : "IP",id:"ip",type:"label",formatter:function(value){
 					return App.objs.appBean.hwlDevice.ip;
 				}},
 				{title : "端口号",id:"port",rules:{
 					maxChLength:parseInt(150*2/3)
 				}},
 				{title : "路径",id:"path",rules:{
 					maxChLength:parseInt(150*2/3)
 				}},
 				{title : "软件编号",id:"softwareSn",rules:{
 					maxChLength:parseInt(150*2/3)
 				}},
 				{title : "内部编码",id:"innerCode",rules:{
 					maxChLength:parseInt(150*2/3)
 				}},
 				{title : "中间件", id : "isMiddleware",type:"radio",data:[["Y","是"],["N","否"]]},
 				{title : "虚拟化迁移",id:"virtualMigration",rules:{
 					maxChLength:parseInt(150*2/3)
 				}},
 				{title : "NAS使用",id:"nasUsage",rules:{
 					maxChLength:parseInt(150*2/3)
 				}},
 				{title : "SAN-LUN使用",id:"sanLunUsage",rules:{
 					maxChLength:parseInt(150*2/3)
 				}},
 				{title : "本地磁盘使用",id:"localDiskUsage",type:"textarea",linebreak:true,wrapXsWidth:8,wrapMdWidth:8,height:50,rules:{
 					maxChLength:parseInt(3000*2/3)
 				}},
 				{title : "备注",id:"remarks",type:"textarea",linebreak:true,wrapXsWidth:8,wrapMdWidth:8,height:50,rules:{
 					maxChLength:parseInt(3000*2/3)
 				}}
 			];
			$("#base_info").ITCUI_Foldable();
			$("#form_baseinfo").ITC_Form({fixLabelWidth:true,validate:true,labelFixWidth:120},App.objs.form); //主卡片信息	
			
			App.loadData();
			
			App.objs["isInit"]=true;
		},
		
		changeShow:function(){//切换模式
			if(App.objs.mode=="view"){
				$("#pageTitle").html("软件台账应用信息");
				$("#form_baseinfo").ITC_Form("beginedit");//临时使用，解决空字段在点击其他节点后不隐藏的问题
				$("#form_baseinfo").ITC_Form("readonly");
			}else if(App.objs.mode=="create"){
				$("#pageTitle").html("新建软件台账应用");
				$("#form_baseinfo").ITC_Form("beginedit");
			}else if(App.objs.mode=="edit"){
				$("#pageTitle").html("编辑软件台账应用");
				$("#form_baseinfo").ITC_Form("beginedit");
			}
		},
		
		loadData:function(){//加载数据
			App.objs.swName=App.getParent().SwLedger.objs.swName;
			if(App.objs.mode=="view"||App.objs.mode=="edit"){
				App.objs.appBean=App.getParent().SwLedger.objs.appsMap[App.objs.rowId];
			}else if(App.objs.mode=="create"){
				if(!App.objs.appBean){
					//用于新建时填充表单的
					App.objs["appBean"]={
							swl:{swName:App.objs.swName},
							swName:App.objs.swName,
							attr01:"",
							relatedBusiness:"",
							attr02:"",
							attr03:"",
							toUseTime:"",
							remarks:""
					};
				}
			}

         	if(!App.objs.appBean.hwlDevice){
         		App.objs.appBean.hwlDevice={};
         	}
         	
			$("#form_baseinfo").ITC_Form("loaddata",App.objs.appBean);
			App.setAttrsToShow();
			
			App.changeShow();
		},
		
		getApp:function(){//返回一个修改过的最终的对象
			var obj=$("#form_baseinfo").iForm("getVal");
			if(App.objs.mode=="edit"){//编辑状态下，补全其他字段
				obj.appId=App.objs.appBean.appId;
				obj.isChanged=true;
				obj.rowId=App.objs.appBean.rowId;
			}
			obj.hwlDevice=App.objs.appBean.hwlDevice;
			return obj;
		},
		
		setAttrsToShow:function(){//单独设置字段用于展示，解决form的字段id不存在，不显示所在硬件台账和ip的问题
			$("#f_hwName").val(App.objs.appBean.hwlDevice.hwName);
         	$("#f_ip").html(App.objs.appBean.hwlDevice.ip);
		},
		
		valid:function(){
			$("#f_appName").removeData("previousValue");
    		return $("#form_baseinfo").valid();
		}
};