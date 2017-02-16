var HwLedger={//basePath框架中已有
		objs:{},

		getTree:function(){
			try{
				HwLedger.objs["tree"]=window.parent.document.getElementById("assetHwTree").contentWindow.$('#property_tree');
			}catch(err){
				return null;
			}
			return HwLedger.objs.tree;
		},
		
		init:function(){
			HwLedger.objs["isInit"]=false;			
			HwLedger.initForms();									
			HwLedger.loadData();
			
			HwLedger.objs["isInit"]=true;
		},
		
		toEdit:function(){
			HwLedger.objs["mode"]="edit";
			HwLedger.changeShow();
		},

		toDelete:function(){
			Notice.confirm("确认删除|是否确定要删除硬件台账“"+HwLedger.objs.hwLedgerBean.hwName+"”及其所有下级节点？该操作无法撤销。",function(){HwLedger.delHwLedger();});
		},
		
		toBack:function(){//返回的操作
			if(HwLedger.objs.mode=="edit"){
				HwLedger.objs["mode"]="view";
				HwLedger.loadData();
			}else if(HwLedger.objs.mode=="create"){
				var tree=HwLedger.getTree();
				var parentNode = tree.tree('find', tree.tree('getSelected').obj.hwId);
				tree.tree('select', parentNode.target);
			}
		},
		
		beginEditForms:function(){
			$("#baseinfo_form").iForm("beginEdit");
			$("#baseinfo_form").iForm("endEdit",["hwType","parentName"]);
			
			$("#serviceInfo_form").iForm("beginEdit");
			
			if(HwLedger.objs.hwLedgerType=='HW_L_ROOM_EQPT'){
				$("#re_hardwareInfo_form").iForm("beginEdit");
				$("#re_configureInfo_form").iForm("beginEdit");
			}else if(HwLedger.objs.hwLedgerType=='HW_L_NETWORK'){
				$("#nw_deviceInfo_form").iForm("beginEdit");
			}else if(HwLedger.objs.hwLedgerType=='HW_L_STORAGE'){
				$("#s_hardwareInfo_form").iForm("beginEdit");
				$("#s_configureInfo_form").iForm("beginEdit");
			}
		},
		
		endEditForms:function(){
			$("#baseinfo_form").iForm("endEdit");
			$("#serviceInfo_form").iForm("endEdit");
			if(HwLedger.objs.hwLedgerType=='HW_L_ROOM_EQPT'){
				$("#re_hardwareInfo_form").iForm("endEdit");
				$("#re_configureInfo_form").iForm("endEdit");
			}else if(HwLedger.objs.hwLedgerType=='HW_L_NETWORK'){
				$("#nw_deviceInfo_form").iForm("endEdit");
			}else if(HwLedger.objs.hwLedgerType=='HW_L_STORAGE'){
				$("#s_hardwareInfo_form").iForm("endEdit");
				$("#s_configureInfo_form").iForm("endEdit");
			}
		},
		
		//对form检查高度，如果为0则隐藏整个fold，可控制全部显示和隐藏
		checkFolds:function(attr){//{hideAll:true};
			$(".formFold").each(function(){
			    if((attr&&attr.hideAll)||$(this).height()==0){
			    	$("#"+$(this).attr("id")).iFold("hide");
			    }else{
			    	$("#"+$(this).attr("id")).iFold("show");
			    }
			});
		},
		
		changeShow:function(){//切换模式
			if(HwLedger.objs.mode=="view"){
				$("#pageTitle").html("硬件台账详情");
				
				HwLedger.endEditForms();
				
				$("#btnEdit,#searchButtonDiv").show();
				$("#btnCreate,#btnSave,#btnDel,#btnBack").hide();
				
				HwLedger.checkFolds();//检查是否要隐藏整个fold
				
			}else if(HwLedger.objs.mode=="create"){
				HwLedger.checkFolds();
				
				$("#pageTitle").html("新建硬件台账");
				
				HwLedger.beginEditForms();
				
				$("#btnCreate,#btnBack").show();
				$("#btnEdit,#btnDel,#btnSave,#searchButtonDiv").hide();
				
			}else if(HwLedger.objs.mode=="edit"){
				HwLedger.checkFolds();
				
				$("#pageTitle").html("编辑硬件台账");
				
				HwLedger.beginEditForms();

				$("#btnSave,#btnBack,#btnDel").show();
				$("#btnCreate,#btnEdit,#searchButtonDiv").hide();
				
			}
			FW.fixRoundButtons(".btn-toolbar");
		},
		
		loadData:function(){//加载数据
			//HwLedger.objs.mode="create";//测试新建的页面
			if(HwLedger.objs.mode=="view"||HwLedger.objs.mode=="edit"){
				
			}else if(HwLedger.objs.mode=="create"){
				if(!HwLedger.objs.blankBean){
					//用于新建时填充表单的
					HwLedger.objs["blankBean"]={
					};
				}
				/**加载空数据**/
				HwLedger.objs.hwLedgerBean=HwLedger.objs.blankBean;
			}
				
			//加载到form中
			$("#baseinfo_form").ITC_Form("loaddata",HwLedger.objs.hwLedgerBean);
			$("#serviceInfo_form").ITC_Form("loaddata",HwLedger.objs.hwLedgerBean);			
			$(".specialForms").hide();//隐藏所有iFold
			if(HwLedger.objs.hwLedgerType=='HW_L_ROOM_EQPT'){
				$("#roomEqptForms").show();
				$("#re_hardwareInfo_form").ITC_Form("loaddata",HwLedger.objs.hwLedgerBean);
				$("#re_configureInfo_form").ITC_Form("loaddata",HwLedger.objs.hwLedgerBean);
			}else if(HwLedger.objs.hwLedgerType=='HW_L_NETWORK'){
				$("#networkForms").show();
				$("#nw_deviceInfo_form").ITC_Form("loaddata",HwLedger.objs.hwLedgerBean);
			}else if(HwLedger.objs.hwLedgerType=='HW_L_STORAGE'){
				$("#storageForms").show();
				$("#s_hardwareInfo_form").ITC_Form("loaddata",HwLedger.objs.hwLedgerBean);
				$("#s_configureInfo_form").ITC_Form("loaddata",HwLedger.objs.hwLedgerBean);
			}
			
			HwLedger.changeShow();
		},
		
		getDetail:function(id,type){
			//FW.navigate(basePath+"asset/hwLedger/detailPage.do?hwLedgerId="+id+"&hwLedgerType="+type+"&mode=view");
			//return;
			
			$.ajax({
				type : "get",
				url: basePath+"asset/hwLedger/getDetail.do",
				data: {"hwLedgerId":id,
					"hwLedgerType":type},
				dataType : "json",
				success : function(data) {				
					HwLedger.objs["mode"]="view";
					HwLedger.objs["hwLedgerId"]=id;
					HwLedger.objs["hwLedgerType"]=type;
					HwLedger.objs["hwLedgerBean"]=data.hwLedgerBean;
					HwLedger.initForms();
					HwLedger.loadData();
				}
			});
		},
		
		formsValid:function(){//根据类型对form进行验证
			var result=$("#baseinfo_form").valid()&&$("#serviceInfo_form").valid();
			if(HwLedger.objs.hwLedgerType=='HW_L_ROOM_EQPT'){
				result=result&&$("#re_hardwareInfo_form").valid()&&$("#re_configureInfo_form").valid();
			}else if(HwLedger.objs.hwLedgerType=='HW_L_NETWORK'){
				result=result&&$("#nw_deviceInfo_form").valid();
			}else if(HwLedger.objs.hwLedgerType=='HW_L_STORAGE'){
				result=result&&$("#s_hardwareInfo_form").valid()&&$("#s_configureInfo_form").valid();
			}
			return result;
		},
		
		getFormsValues:function(){//根据类型取得form的值
			var obj={};
			var base=$("#baseinfo_form").iForm("getVal");
			$.extend(obj,base);
			var service=$("#serviceInfo_form").iForm("getVal");
			$.extend(obj,service);
			if(HwLedger.objs.hwLedgerType=='HW_L_ROOM_EQPT'){
				var reHardware=$("#re_hardwareInfo_form").iForm("getVal");
				$.extend(obj,reHardware);
				var reConfigure=$("#re_configureInfo_form").iForm("getVal");
				$.extend(obj,reConfigure);
			}else if(HwLedger.objs.hwLedgerType=='HW_L_NETWORK'){
				var nwDevice=$("#nw_deviceInfo_form").iForm("getVal");
				$.extend(obj,nwDevice);
			}else if(HwLedger.objs.hwLedgerType=='HW_L_STORAGE'){
				var sHardware=$("#s_hardwareInfo_form").iForm("getVal");
				$.extend(obj,sHardware);
				var sConfigure=$("#s_configureInfo_form").iForm("getVal");
				$.extend(obj,sConfigure);
			}
			return obj;
		},
		
		createHwLedger:function(){
			if(!HwLedger.formsValid()){
				//FW.error("提交的内容有错误的地方，请修改后重试");
				return;
			}
			var params = HwLedger.getFormsValues();
			//补充缺少的参数
			params.parentId=HwLedger.getTree().tree('getSelected').obj.hwId;
			params.hwType=HwLedger.objs.hwLedgerType;
			
			$.ajax({
				type : "POST",
				url: basePath+"asset/hwLedger/insertHwLedger.do",
				data: {
					"hwLedgerBean":FW.stringify(params),
					"hwLedgerType":HwLedger.objs.hwLedgerType
				},
				dataType : "json",
				success : function(data) {				
					if(data.status==1){
						FW.success("硬件台账创建成功");
						HwLedger.objs["hwLedgerBean"]=data.hwLedgerBean;	

						var tree=HwLedger.getTree();
						var node = tree.tree('getSelected');
						if (node){
							if(node.state=="open"){
								tree.tree('append', {
									parent: node.target,
									data: window.parent.document.getElementById("assetHwTree").contentWindow.HardwareTree.changeToTreeNode(HwLedger.objs.hwLedgerBean)
								});
								var newNode = tree.tree('find', HwLedger.objs.hwLedgerBean.hwId);
								tree.tree('select', newNode.target);
							}else{
								HwLedger.objs["waitingExpandNode"]=node;
								tree.tree('expand',node.target);
								tree.tree('options').onExpand=function(node){
									if(HwLedger.objs.waitingExpandNode==node){
										delete HwLedger.objs.waitingExpandNode;
										var tree=HwLedger.getTree();
										var newNode = tree.tree('find', HwLedger.objs.hwLedgerBean.hwId);
										tree.tree('select', newNode.target);
									}
								};
							}
						}
					}else if(data.status == "-1"){
						FW.error("创建失败，硬件台账的类型错误");
					}else{
						FW.error("硬件台账创建失败");
					}
				}
			});
		},
		
		updateHwLedger:function(){
			if(!HwLedger.formsValid()){
				//FW.error("提交的内容有错误的地方，请修改后重试");
				return;
			}
			
			var params = HwLedger.getFormsValues();
			//补充缺少的参数
			params.hwId=HwLedger.objs.hwLedgerId;
			
			$.post(basePath + "asset/hwLedger/updateHwLedger.do",{
					"hwLedgerBean":FW.stringify(params),
					"hwLedgerType":HwLedger.objs.hwLedgerType
				},function(data){
				if(data.status == "1"){
					FW.success("硬件台账更新成功");
					HwLedger.objs["hwLedgerBean"]=data.hwLedgerBean;
					HwLedger.toBack();

					var node = HwLedger.getTree().tree('getSelected');
					if (node){
						HwLedger.getTree().tree('update', {
							target: node.target,
							text: HwLedger.objs.hwLedgerBean.hwName
						});
					}
				}else if(data.status == "-1"){
					FW.error("更新失败，硬件台账的类型错误");
				}else{
					FW.error("硬件台账更新失败");
				}
			},"json");
		},
		
		delHwLedger:function(){
			$.post(basePath + "asset/hwLedger/deleteHwLedger.do",{"hwType":HwLedger.objs.hwLedgerBean.hwType,"hwId":HwLedger.objs.hwLedgerBean.hwId},function(data){
				if(data.result == "success"){
					FW.success("删除成功");
					
					var tree=HwLedger.getTree();
					var node=tree.tree('getSelected');
					var parent=tree.tree('getParent', node.target);
					tree.tree('remove', node.target);
					tree.tree('select',parent.target);
				}else{
					FW.error("删除失败"+","+"请稍后重试或联系管理员");
				}
			},"json");
		},
		
		initForms:function(){//初始化所有的form
			if(!HwLedger.objs.isInit){//页面未初始化
				//基础form
				HwLedger.objs["baseForm"]=[
					{title : "名称",id:"hwName",rules:{
						required : true,maxChLength:parseInt(150*2/3)
					}},
					{title : "类型",id : "hwType" ,
						type : "combobox",
				        dataType : "enum",
				        enumCat : "AST_HW_TYPE",
				        value : HwLedger.objs.hwLedgerType
					},
					{title : "父节点",id : "parentName",type:"label",value:"",formatter:function(value){
						return value?value:(HwLedger.objs.mode=="create"?HwLedger.getTree().tree('getSelected').obj.hwName:HwLedger.getTree().tree('getParent',HwLedger.getTree().tree('getSelected').target).obj.hwName);
					}},				
					{title : "位置",id:"location",rules:{
						maxChLength:parseInt(150*2/3)
					}},
					{title : "关联设备",id:"relatedEqpt",type:"textarea",linebreak:true,wrapXsWidth:8,wrapMdWidth:8,height:50,rules:{
						maxChLength:parseInt(3000*2/3)
					}},
					{title : "外联业务",id:"relatedBusiness",type:"textarea",linebreak:true,wrapXsWidth:8,wrapMdWidth:8,height:50,rules:{
						maxChLength:parseInt(3000*2/3)
					}},
					{title : "备注",id:"remarks",type:"textarea",linebreak:true,wrapXsWidth:8,wrapMdWidth:8,height:50,rules:{
						maxChLength:parseInt(3000*2/3)
					}}
				];
				$("#baseinfo_form").ITC_Form({fixLabelWidth:true,validate:true,labelFixWidth:120},HwLedger.objs.baseForm); //主卡片信息	
				
				//服务信息
				HwLedger.objs["serviceForm"]=[
					{title : "资产编号", id : "assetCode",rules:{
	 					maxChLength:parseInt(150*2/3),
	 					remote:{
							"url": basePath + "asset/hwLedger/isAssetCodeExist.do",
							type:"get",
							data:{
								assetCode : function(){return $("#f_assetCode").val();},
								hwId : function(){return HwLedger.objs.hwLedgerBean?HwLedger.objs.hwLedgerBean.hwId:"";}
							},
							complete:function(data){
								
							}
						}
	 				}},
					{title : "所属单位", id : "eqptAttr02",rules:{
	 					maxChLength:parseInt(150*2/3)
	 				}},
					{title : "使用状态", id : "status",rules : {required:true},
						type : "combobox",dataType : "enum",enumCat : "AST_HW_SERVICE_STATUS",value:"HW_SERVICE_ONLINE"
					},
					{title : "投入使用时间", id : "toUseTime",type:"datetime",dataType:"datetime" },
					{title : "过保时间", id : "elapsedTime",type:"datetime",dataType:"datetime"},
					{title : "运维负责人", id : "principal", type: "hidden"},
					{title : "运维负责人", id : "eqptAttr01",
						render: function(id) {
							$("#" + id).attr("placeholder","请输入姓名或账号").initHintPersonList();
							$("#" + id).initHintPersonList({
								clickEvent: function(id, name) {
									var uid = id.split("_")[0];
									$("#serviceInfo_form").iForm("setVal",{principal:uid,eqptAttr01:name});
								}
							});
						}
					},
					{title : "供应商", id : "eqptAttr03",rules:{
	 					maxChLength:parseInt(150*2/3)
	 				}},
					{title : "维修记录", id : "repairRecard",type:"textarea", linebreak:true,wrapXsWidth:12,wrapMdWidth:8,height:50,
						rules:{maxChLength:parseInt(3000*2/3)}
					}
				];
				$("#serviceInfo_div").iFold("init");
	   			$("#serviceInfo_form").ITC_Form({fixLabelWidth:true,validate:true,labelFixWidth:120},HwLedger.objs.serviceForm); 
			}
			
			//删除页面各类型节点的form元素，用于重建和重新初始化
			$(".specialForms").empty();
   			
			//初始化各类型节点的form
			if(HwLedger.objs.hwLedgerType=='HW_L_ROOM_EQPT'){
				//重建机房设备的form元素
				$("#roomEqptForms").html(
					"<div id='re_hardwareInfo_div' grouptitle='硬件信息' class='formFold'>"+
						"<form id='re_hardwareInfo_form' class='margin-form-title margin-form-foldable'>"+
						"</form>"+
					"</div>"+
					"<div class='margin-group'></div>"+
					"<div id='re_configureInfo_div' grouptitle='配置信息' class='formFold'>"+
						"<form id='re_configureInfo_form' class='margin-form-title margin-form-foldable'>"+
						"</form>"+
					"</div>	"+
					"<div class='margin-group'></div>"
				);
				
				//机房设备 硬件信息
				HwLedger.objs["re_hardwareForm"]=[
     				{title : "设备型号",id:"eqptModel", type: "hidden"},{title : "设备型号",id:"eqptModelName"},
     				{title : "品牌",id:"eqptBrand", type: "hidden"},{title : "品牌",id:"eqptBrandName"}
     			];
				$("#re_hardwareInfo_div").iFold("init");
				$("#re_hardwareInfo_form").ITC_Form({fixLabelWidth:true,validate:true,labelFixWidth:120},HwLedger.objs.re_hardwareForm);
				searchHwModelHint( 'eqptModel', 'roomEqptModel' );
  				searchHwModelHint( 'eqptBrand', 'roomEqptBrand' );
  				
				//机房设备 配置信息
				HwLedger.objs["re_configureForm"]=[
       				{title : "供电电路编号",id:"powerSupplyNo",rules:{
       					maxChLength:parseInt(150*2/3)
       				}},
       				{title : "额定电源功率",id:"power",rules:{
       					maxChLength:parseInt(150*2/3)
       				}}
       			];
  				$("#re_configureInfo_div").iFold("init");
  				$("#re_configureInfo_form").ITC_Form({fixLabelWidth:true,validate:true,labelFixWidth:120},HwLedger.objs.re_configureForm);
  				
			}else if(HwLedger.objs.hwLedgerType=='HW_L_NETWORK'){
				//重建网络设备的form元素
				$("#networkForms").html(
					"<div id='nw_deviceInfo_div' grouptitle='设备信息' class='formFold'>"+
						"<form id='nw_deviceInfo_form' class='margin-form-title margin-form-foldable'>"+
						"</form>"+
					"</div>	"+
					"<div class='margin-group'></div>"
				);
				
				//网络设备 设备信息
				HwLedger.objs["nw_deviceForm"]=[
       				{title : "设备型号",id:"networkModel", type: "hidden"},{title : "设备型号",id:"networkModelName"},
       				{title : "SN",id:"snCode",rules:{
       					maxChLength:parseInt(150*2/3)
       				}},
       				{title : "品牌",id:"networkBrand", type: "hidden"},{title : "品牌",id:"networkBrandName"},
       				{title : "类型",id:"networkType", type: "hidden"},{title : "类型",id:"networkTypeName"},
       				{title : "网络位置", id : "networkLocation",
    					type : "combobox",dataType : "enum",enumCat : "AST_HW_L_NETWORK_LOCATION",value:"",
    					options:{allowEmpty:true}
    				},
       				{title : "管理IP",id:"ip",rules:{
       					maxChLength:parseInt(150*2/3),required:true
       				}},
       				{title : "管理方式",id:"networkManageType", type: "hidden"},{title : "管理方式",id:"networkManageTypeName"},
       				{title : "OS版本",id:"osVersion",rules:{
       					maxChLength:parseInt(150*2/3)
       				}},
       				{title : "软件许可",id:"softwareLicense",rules:{
       					maxChLength:parseInt(150*2/3)
       				}},
       				{title : "RJ-45接口数量",id:"rjNum",rules:{
       					maxChLength:parseInt(150*2/3),digits:true
       				}},
       				{title : "FC接口数量",id:"fcNum",rules:{
       					maxChLength:parseInt(150*2/3),digits:true
       				}},
       				{title : "额定电源功率",id:"power",rules:{
       					maxChLength:parseInt(150*2/3)
       				}}
       			];
  				$("#nw_deviceInfo_div").iFold("init");
  				$("#nw_deviceInfo_form").ITC_Form({fixLabelWidth:true,validate:true,labelFixWidth:120},HwLedger.objs.nw_deviceForm);
  				searchHwModelHint( 'networkModel', 'netEqptModel' );
  				searchHwModelHint( 'networkBrand', 'netEqptBrand' );
  				searchHwModelHint( 'networkType', 'netEqptType' );
  				searchHwModelHint( 'networkManageType', 'netEqptManageType' );
  				
			}else if(HwLedger.objs.hwLedgerType=='HW_L_STORAGE'){
				//重建网络设备的form元素
				$("#storageForms").html(
					"<div id='s_hardwareInfo_div' grouptitle='硬件信息' class='formFold'>"+
						"<form id='s_hardwareInfo_form' class='margin-form-title margin-form-foldable'>"+
						"</form>"+
					"</div>	"+
					"<div class='margin-group'></div>"+
					"<div id='s_configureInfo_div' grouptitle='配置信息' class='formFold'>"+
						"<form id='s_configureInfo_form' class='margin-form-title margin-form-foldable'>"+
						"</form>"+
					"</div>	"+
					"<div class='margin-group'></div>"
				);
				
				//存储设备 硬件信息
				HwLedger.objs["s_hardwareForm"]=[
       				{title : "设备型号",id:"storageModel", type: "hidden"},{title : "设备型号",id:"storageModelName"},
       				{title : "SN",id:"snCode",rules:{
       					maxChLength:parseInt(150*2/3)
       				}},
       				{title : "品牌",id:"storageBrand", type: "hidden"},{title : "品牌",id:"storageBrandName"},
       				{title : "控制器",id:"controller",type:"textarea",linebreak:true,wrapXsWidth:8,wrapMdWidth:8,height:50,rules:{
    					maxChLength:parseInt(3000*2/3)
    				}},
    				{title : "缓存",id:"cache",type:"textarea",linebreak:true,wrapXsWidth:8,wrapMdWidth:8,height:50,rules:{
    					maxChLength:parseInt(3000*2/3)
    				}},
    				{title : "硬盘",id:"harddisk",type:"textarea",linebreak:true,wrapXsWidth:8,wrapMdWidth:8,height:50,rules:{
    					maxChLength:parseInt(3000*2/3)
    				}},
    				{title : "前端主机接口情况",id:"frontInterface",type:"textarea",linebreak:true,wrapXsWidth:8,wrapMdWidth:8,height:50,rules:{
    					maxChLength:parseInt(3000*2/3)
    				}},
    				{title : "后端接口情况",id:"backInterface",type:"textarea",linebreak:true,wrapXsWidth:8,wrapMdWidth:8,height:50,rules:{
    					maxChLength:parseInt(3000*2/3)
    				}},
    				{title : "额定电源功率",id:"power",linebreak:true,rules:{
       					maxChLength:parseInt(150*2/3)
       				}}
       			];
  				$("#s_hardwareInfo_div").iFold("init");
  				$("#s_hardwareInfo_form").ITC_Form({fixLabelWidth:true,validate:true,labelFixWidth:120},HwLedger.objs.s_hardwareForm);
  				searchHwModelHint( 'storageModel', 'storageEqptModel' );
  				searchHwModelHint( 'storageBrand', 'storageEqptBrand' );
  				
  				//存储设备 配置信息
				HwLedger.objs["s_configureForm"]=[
				    {title : "统一虚拟化情况",id:"virtualSituation",rules:{
						maxChLength:parseInt(150*2/3)
					}},
					{title : "管理IP",id:"ip",rules:{
						required:true,maxChLength:parseInt(150*2/3),
       					regex:/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/ 
       				}},
       				{title : "管理MAC",id:"mac",rules:{
       					maxChLength:parseInt(150*2/3)
       				}},
       				{title : "管理VLAN",id:"vlan",rules:{
       					maxChLength:parseInt(150*2/3)
       				}},
       				{title : "网络区域", id : "netArea",
    					type : "combobox",dataType : "enum",enumCat : "AST_HW_NETAREA",value:"",
    					options:{allowEmpty:true}
    				},
    				{title : "虚拟化部署位置", id : "virtualDeployLocation",
    					type : "combobox",dataType : "enum",enumCat : "AST_HW_VMDEPLOC",value:"",
    					options:{allowEmpty:true}
    				},
    				{title : "存储类型",id:"storageType", type: "hidden"},{title : "存储类型",id:"storageTypeName"},
    				{title : "RAID类型",id:"raidType", type: "hidden"},{title : "RAID类型",id:"raidTypeName"},
    				{title : "有效容量",id:"effectiveCapacity",rules:{
       					maxChLength:parseInt(150*2/3)
       				}},
       				{title : "热备盘数量",id:"spareDrivesNum",rules:{
       					maxChLength:parseInt(150*2/3),digits:true
       				}},
       				{title : "LUN名称",id:"lunName",rules:{
       					maxChLength:parseInt(150*2/3)
       				}},
       				{title : "LUN大小",id:"lunNum",rules:{
       					maxChLength:parseInt(150*2/3)
       				}},
       				{title : "LUN使用主机",id:"lunHost",rules:{
       					maxChLength:parseInt(150*2/3)
       				}}
       			];
  				$("#s_configureInfo_div").iFold("init");
  				$("#s_configureInfo_form").ITC_Form({fixLabelWidth:true,validate:true,labelFixWidth:120},HwLedger.objs.s_configureForm);
  				searchHwModelHint( 'storageType', 'storageType' );
  				searchHwModelHint( 'raidType', 'raidType' );
			}
		}
};

//硬件类型 input hint事件
function searchHwModelHint( inputId, modelType ){
	var $firstPartyInput=$('#f_' + inputId + "Name");
	
	var firstPartyInit = {
		datasource : basePath + "asset/hwLedger/searchHwModelHint.do?modelType=" + modelType,
		clickEvent : function(id, name) {
			$("#f_" + inputId ).val( id );
			$firstPartyInput.val( name );
		}
	};
	
	$firstPartyInput.change(function(){
		var val = $.trim( $firstPartyInput.val() );
		if( val == "" ){
			$("#f_" + inputId ).val( "" );
		}
	});
	$firstPartyInput.iHint('init', firstPartyInit);

	if(!$("#f_" + inputId ).val()&&HwLedger.objs.hwLedgerBean&&HwLedger.objs.hwLedgerBean[inputId]){
		$("#f_" + inputId ).val( HwLedger.objs.hwLedgerBean[inputId] );
	}
	if($("#f_" + inputId ).val()&&!$firstPartyInput.val()){
		$firstPartyInput.val( HwLedger.objs.hwLedgerBean[inputId+"Name"] );
	}
}