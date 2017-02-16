

//1 服务器基本信息
var baseInfoFields = [
                  {title : "hwId", id : "hwId" , type: "hidden"},
				{title : "名称", id : "hwName" ,rules : {required:true,remote:{
					url: basePath + "asset/hwLedger/queryCheckHwLedgerName.do",
					type: "post",
					data: {
						"paramsMap" : function(){
							var params = {
									"hwName": $("#f_hwName").val(),
									"hwId": $("#f_hwId").val()
							};
							 return FW.stringify(params);
						}
						
					}
				}},
                	 messages:{remote:"名称已存在，请输入其他名称"}
				},
	  			{title : "类型", id : "hwType",
					type : "combobox",
					dataType : "enum",
					enumCat : "AST_HW_TYPE"	
	  			},
	  			{title : "父节点", id : "parentId", type: "hidden"},
	  			{title : "父节点", id : "parentName"},
	  			{title : "位置", id : "location"},
	  			{title : "外联业务", id : "relatedBusiness",type:"textarea", linebreak:true,
	  				 wrapXsWidth:12,
	 		        wrapMdWidth:8,
	 		        height : 50
			     },
	  			{title : "备注", id : "remarks",type:"textarea", linebreak:true,
	  				wrapXsWidth:12,
	  				wrapMdWidth:8,
	  				height : 50
	  			}
	  			
	  		];

//2 服务器硬件信息
var serviceFields = [
				{title : "设备型号", id : "serverModel", type: "hidden" },
				{title : "设备型号", id : "serverModelName"/*,
					render:function(id){
						var ipt = $("#" + id );
						 ipt.attr("icon","itcui_btn_mag");
						 ipt.iInput("init");
						//设备型号
						//initShowBox( 'serverModel', 'serverModel' );
						 ipt.next(".itcui_input_icon").on("click",function(){
					         var src = basePath + "asset/hwLedger/showHwModelBox.do?inputId=" + "serverModel" + "&modelType=" + "serverModel";
					         var dlgOpts = {
					             width : 800,
					             height:500,
					             closed : false,
					             title:"双击选择硬件型号",
					             modal:true
					         };
					         Notice.dialog(src,dlgOpts,null);
					     });
		             }*/
				},
	  			{title : "SN编码", id : "snCode" },
	  			{title : "服务器品牌", id : "serverBrand", type: "hidden"},
	  			{title : "服务器品牌", id : "serverBrandName"},
	  			{title : "物理处理器型号", id : "cpuModel", type: "hidden"},
	  			{title : "物理处理器型号", id : "cpuModelName"},
	  			{title : "处理器数量", id : "cpuNum",rules : {digits: true} },
	  			{title : "物理内存型号", id : "memModel", type: "hidden"},
	  			{title : "物理内存型号", id : "memModelName"},
	  			{title : "内存数量", id : "memNum",rules : {digits: true} },
	  			{title : "物理硬盘型号", id : "harddiskModel", type: "hidden"},
	  			{title : "物理硬盘型号", id : "harddiskModelName"},
	  			{title : "物理硬盘数量", id : "harddiskNum",rules : {digits: true} },
	  			{title : "HBA型号", id : "hbaModel", type: "hidden"},
	  			{title : "HBA型号", id : "hbaModelName"},
	  			{title : "HBA数量", id : "hbaNum",rules : {digits: true} },
	  			{title : "服务器网卡型号", id : "netcardModel", type: "hidden"},
	  			{title : "服务器网卡型号", id : "netcardModelName"},
	  			{title : "网卡数量", id : "netcardNum",rules : {digits: true} },
	  			{title : "服务器阵列卡", id : "raidModel", type: "hidden"},
	  			{title : "服务器阵列卡", id : "raidModelName"},
	  			{title : "电源功率(KW)", id : "power",rules : {number: true} }
	  		];

//3 服务信息
var serviceInfoFields = [
                {title : "资产编号", id : "assetCode"},
                {title : "所属单位", id : "ownOrg"},
				{title : "使用状态", id : "status",rules : {required:true},
					type : "combobox",
				dataType : "enum",
				enumCat : "AST_HW_SERVICE_STATUS"	
				},
				{title : "投入使用时间", id : "toUseTime",type:"datetime",dataType:"datetime" },
	  			{title : "过保时间", id : "elapsedTime",type:"datetime",dataType:"datetime"},
	  			{title : "运维负责人", id : "principal", type: "hidden"},
	  			{title : "运维负责人", id : "principalName",
	  				render: function(id) {
	  					$("#" + id).attr("placeholder","请输入姓名或账号").initHintPersonList();
	  					$("#" + id).initHintPersonList({
	  						clickEvent: function(id, name) {
	  							var uid = id.split("_")[0];
	  							$("#f_principal").val(uid);
	  							$("#f_principalName").val(name);
	  						}
	  					});

	  				}
	  			},
	  			{title : "供应商", id : "supplier"},
	  			{title : "维修记录", id : "repairRecard",type:"textarea", linebreak:true,
	  				wrapXsWidth:12,
	  				wrapMdWidth:8,
	  				height : 50
	  			}
	  			
	  		];

//4 系统信息
var sysFields = [
				{title : "计算机名", id : "computerName" },
				{title : "逻辑处理器", id : "logicCpu"},
	  			{title : "逻辑内存", id : "logicMem"},
	  			{title : "逻辑硬盘", id : "logicHarddisk"},
	  			{title : "服务器操作系统", id : "os"},
	  			{title : "服务器补丁", id : "osPath"}
	  		];
	  		
//5 网络信息
var netFields = [
				{title : "服务器IP地址", id : "ip" ,rules : {required:true,
					regex:/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/ } 
				},
	  			{title : "是否集群", id : "isClusterIp", 
					type : "radio",
					data : [['Y','是'],['N','否',true]]
				},
	  			{title : "MAC地址", id : "mac"},
	  			{title : "服务器VLAN", id : "vlan"},
	  			{title : "服务器网络区域", id : "netArea",
	  				type : "combobox",
					dataType : "enum",
					enumCat : "AST_HW_NETAREA",
					options:{
						allowEmpty:true
					}
	  			},
	  			{title : "服务器公网通讯", id : "isPublicNet",
	  				type : "radio",
					data : [['Y','是'],['N','否',true]]
	  			}
	  		];
	  		
//6 备份信息
var backupFields = [
				{title : "远灾备份", id : "isRemoteBackup" ,
					type : "radio",
					data : [['Y','是'],['N','否',true]]	
				},
	  			{title : "虚拟带备份", id : "isVtlBackup" ,
					type : "radio",
					data : [['Y','是'],['N','否',true]]},
	  			{title : "CDP备份", id : "isCdpBackup" ,
					type : "radio",
					data : [['Y','是'],['N','否',true]]},
	  			{title : "物理带库备份", id : "isPtlBackup" ,
					type : "radio",
					data : [['Y','是'],['N','否',true]]},
	  			{title : "服务器手工备份", id : "isManualBackup" ,
					type : "radio",
					data : [['Y','是'],['N','否',true]]}
	  		];
	  		
//7 存储信息
var driverFields = [
				{title : "存储硬件类型", id : "storageModel", type: "hidden" },
				{title : "存储硬件类型", id : "storageModelName" },
	  			{title : "服务器存储类型", id : "storageType", type: "hidden"},
	  			{title : "服务器存储类型", id : "storageTypeName"},
	  			{title : "SAN_LUN", id : "sanLun"},
	  			{title : "RAID类型", id : "raidType", type: "hidden"},
	  			{title : "RAID类型", id : "raidTypeName"},
	  			{title : "LUN大小", id : "lunNum"},
	  			{title : "数据变化度", id : "dataChangeDegree"}
	  		];

//2_1 虚机
var vmFields = [{title : "V-CPU", id : "vmAttr01"},
	  			{title : "V-RAM", id : "vmAttr02"},
	  			{title : "V-Storage", id : "vmAttr03"},
                {title : "虚拟化部署位置", id : "virtualDeployLocation",
					type : "combobox",
					dataType : "enum",
					enumCat : "AST_HW_VMDEPLOC"
				}];

//拿到虚机所有的form 数据
function getVMAllForm(){
	//校验所有form
	if(!$("#baseInfoForm").valid() || !$("#serviceForm").valid()
		|| !$("#sysForm").valid() || !$("#netForm").valid() || !$("#backupForm").valid() 
		|| !$("#driverForm").valid() || !$("#vmForm").valid()
	){
		return;
	}
	
	//拿到各个form数据
	var baseInfoFormData = getFormData( "baseInfoForm" );
	var serviceFormData = getFormData( "serviceForm" );
	var sysFormData = getFormData( "sysForm" );
	var netFormData = getFormData( "netForm" );
	var backupFormData = getFormData( "backupForm" );
	var driverFormData = getFormData( "driverForm" );
	var vmFormData = getFormData( "vmForm" );
	
	var data = {};
	data.baseInfoFormData = baseInfoFormData;
	data.serviceFormData = serviceFormData;
	data.sysFormData = sysFormData;
	data.netFormData = netFormData;
	data.backupFormData = backupFormData;
	data.driverFormData = driverFormData;
	data.vmFormData = vmFormData;
	return data;
}

//拿到服务器所有的form 数据
function getServiceAllForm(){
	//校验所有form
	if(!$("#baseInfoForm").valid() || !$("#deviceForm").valid() || !$("#serviceForm").valid()
			|| !$("#sysForm").valid() || !$("#netForm").valid() || !$("#backupForm").valid() 
			|| !$("#driverForm").valid()		
	){
		return;
	}
	
	//拿到各个form数据
	var baseInfoFormData = getFormData( "baseInfoForm" );
	var deviceFormData = getFormData( "deviceForm" );
	var serviceFormData = getFormData( "serviceForm" );
	var sysFormData = getFormData( "sysForm" );
	var netFormData = getFormData( "netForm" );
	var backupFormData = getFormData( "backupForm" );
	var driverFormData = getFormData( "driverForm" );
	
	var data = {};
	data.baseInfoFormData = baseInfoFormData;
	data.deviceFormData = deviceFormData;
	data.serviceFormData = serviceFormData;
	data.sysFormData = sysFormData;
	data.netFormData = netFormData;
	data.backupFormData = backupFormData;
	data.driverFormData = driverFormData;
	return data;
}

//硬件类型 input hint事件
function searchHwModelHint( inputId, modelType ){
	var $firstPartyInput=$('#f_' + inputId + "Name");
	
	var firstPartyInit = {
		datasource : basePath + "asset/hwLedger/searchHwModelHint.do?modelType=" + modelType,
		clickEvent : function(id, name) {
			$("#f_" + inputId ).val( id );
			$firstPartyInput.val( name );
		},maxItemCount : 100
	};
	
	$firstPartyInput.change(function(){
		var val = $.trim( $firstPartyInput.val() );
		if( val == "" ){
			$("#f_" + inputId ).val( "" );
		}
	});
	$firstPartyInput.iHint('init', firstPartyInit);

}


//初始化放大镜--硬件型号
function initShowBox( inputId, modelType ){
	 var ipt = $("#" + inputId + "Name");
	 ipt.next(".itcui_input_icon").on("click",function(){
         var src = basePath + "asset/hwLedger/showHwModelBox.do?id=" + inputId + "&modelType=" + modelType;
         var dlgOpts = {
             width : 800,
             height:500,
             closed : false,
             title:"双击选择硬件型号",
             modal:true
         };
         Notice.dialog(src,dlgOpts,null);
     });
}

//初始化input 
function initHwModelHint(){
	//设备型号 
	searchHwModelHint( 'serverModel', 'serverModel' );
	//服务器品牌
	searchHwModelHint( 'serverBrand', 'serverBrand' );
	//物理处理器型号
	searchHwModelHint( 'cpuModel', 'cpuModel' );
	//物理内存型号
	searchHwModelHint( 'memModel', 'memoryModel' );
	//物理硬盘型号
	searchHwModelHint( 'harddiskModel', 'harddiskModel' );
	//HBA型号
	searchHwModelHint( 'hbaModel', 'hbaModel' );
	//服务器阵列卡
	searchHwModelHint( 'raidModel', 'arraycardModel' );
	//服务器网卡型号
	searchHwModelHint( 'netcardModel', 'networkcardModel' );
	//存储硬件类型
	searchHwModelHint( 'storageModel', 'storageModel' );
	//服务器存储类型
	searchHwModelHint( 'storageType', 'storageType' );
	//RAID类型
	searchHwModelHint( 'raidType', 'raidType' );
}

//加载服务器内容
function loadServiceData( hwId ){
	//通过Id加载form
	var url = basePath + "asset/hwLedger/queryHwLedgerServiceById.do?hwId=" + hwId;
	$.ajax({
		url : url,
		type : 'post',
		dataType : "json",
		success : function(data) {
			if( data.result == "success" ){
				loadServiceFormData( data );
			}else{
				FW.error( "加载数据失败 ！");
			}
		}
	});
}

//服务器填充
function loadServiceFormData( data ){
	//1 baseInfoForm
	loadBaseInfoVoForm( data.baseInfoVo );
	//2 deviceForm
	loadServiceVoForm( data.serviceVo );
	//3 serviceForm
	loadServiceInfoVoForm( data.serviceInfoVo );
	//4 sysForm
	loadSysVoForm( data.sysVo );
	//5 netForm
	loadNetVoForm( data.netVo );
	//6 backupForm
	loadBackupVoForm( data.backupVo );
	//7 driverForm
	loadDriverVoForm( data.driverVo );
}

//加载虚机内容
function loadVMData( hwId ){
	//通过Id加载form
	var url = basePath + "asset/hwLedger/queryHwLedgerVMById.do?hwId=" + hwId;
	$.ajax({
		url : url,
		type : 'post',
		dataType : "json",
		success : function(data) {
			if( data.result == "success" ){
				loadVMFormData( data );
			}else{
				FW.error( "加载数据失败 ！");
			}
		}
	});
}


//填充虚机form
function loadVMFormData( data ){
	//1 baseInfoForm
	loadBaseInfoVoForm( data.baseInfoVo );
	//3 serviceForm
	loadServiceInfoVoForm( data.serviceInfoVo );
	//4 sysForm
	loadSysVoForm( data.sysVo );
	//5 netForm
	loadNetVoForm( data.netVo );
	//6 backupForm
	loadBackupVoForm( data.backupVo );
	//7 driverForm
	loadDriverVoForm( data.driverVo );
	//2_1 vmForm
	loadVmBeanForm( data.vmBean );
}


//1初始化服务器基本信息
function loadBaseInfoVoForm( formData ){
	var data = {
			"hwId" : formData.hwId,
			"hwName" : formData.hwName,
			"hwType" : formData.hwType,
			"parentId" : formData.parentId,
			"parentName" : formData.parentName,
			"location" : formData.location,
			"relatedBusiness" : formData.relatedBusiness,
			"remarks" : formData.remarks
	};
	$("#baseInfoForm").iForm("setVal",data);
	$("#baseInfoForm").iForm("endEdit");
}

//2_1 初始化虚机基本信息
function loadVmBeanForm( formData ){
	var vmDepLoc = formData.virtualDeployLocation;
	var data = {
			"virtualDeployLocation" : vmDepLoc,
			"vmAttr01" : formData.vmAttr01,
			"vmAttr02" : formData.vmAttr02,
			"vmAttr03" : formData.vmAttr03
	};
	$("#vmForm").iForm("setVal",data);
	$("#vmForm").iForm("endEdit");
}

//2 初始化服务器硬件信息
function loadServiceVoForm( formData ){
	var data = {
			"serverModel" : formData.serverModel,
			"serverModelName" : formData.serverModelName,
			"snCode" : formData.snCode,
			"serverBrand" : formData.serverBrand,
			"serverBrandName" : formData.serverBrandName,
			"cpuModel" : formData.cpuModel,
			"cpuModelName" : formData.cpuModelName,
			"cpuNum" : formData.cpuNum,
			"memModel" : formData.memModel,
			"memModelName" : formData.memModelName,
			"memNum" : formData.memNum,
			"harddiskModel" : formData.harddiskModel,
			"harddiskModelName" : formData.harddiskModelName,
			"harddiskNum" : formData.harddiskNum,
			"hbaModel" : formData.hbaModel,
			"hbaModelName" : formData.hbaModelName,
			"hbaNum" : formData.hbaNum,
			"netcardModel" : formData.netcardModel,
			"netcardModelName" : formData.netcardModelName,
			"netcardNum" : formData.netcardNum,
			"raidModel" : formData.raidModel,
			"raidModelName" : formData.raidModelName,
			"power" : formData.power,
			"assetCode" : formData.assetCode
	};
	$("#deviceForm").iForm("setVal",data);
	$("#deviceForm").iForm("endEdit");
}

//3初始化服务信息
function loadServiceInfoVoForm( formData ){
	var data = {
			"assetCode" : formData.assetCode,
			"ownOrg" : formData.ownOrg,
			"status" : formData.status,
			"principal" : formData.principal,
			"principalName" : formData.principalName,
			"toUseTime" : formData.toUseTime,
			"elapsedTime" : formData.elapsedTime,
			"maintain" : formData.maintain,
			"supplier" : formData.supplier,
			"repairRecard" : formData.repairRecard
	};
	$("#serviceForm").iForm("setVal",data);
	$("#serviceForm").iForm("endEdit");
}

//4初始化系统信息
function loadSysVoForm( formData ){
	var data = {
			"computerName" : formData.computerName,
			"logicMem" : formData.logicMem,
			"logicHarddisk" : formData.logicHarddisk,
			"os" : formData.os,
			"osPath" : formData.osPath,
			"logicCpu" : formData.logicCpu
	};
	$("#sysForm").iForm("setVal",data);
	$("#sysForm").iForm("endEdit");
}

//5初始化系统信息
function loadNetVoForm( formData ){
	var data = {
			"ip" : formData.ip,
			"isClusterIp" : formData.isClusterIp,
			"mac" : formData.mac,
			"vlan" : formData.vlan,
			"netArea" : formData.netArea,
			"isPublicNet" : formData.isPublicNet
	};
	$("#netForm").iForm("setVal",data);
	$("#netForm").iForm("endEdit");
}

//6初始化系统信息
function loadBackupVoForm( formData ){
	var data = {
			"isRemoteBackup" : formData.isRemoteBackup,
			"isVtlBackup" : formData.isVtlBackup,
			"isCdpBackup" : formData.isCdpBackup,
			"isPtlBackup" : formData.isPtlBackup,
			"isManualBackup" : formData.isManualBackup
	};
	$("#backupForm").iForm("setVal",data);
	$("#backupForm").iForm("endEdit");
}
//7 初始化存储信息
function loadDriverVoForm( formData ){
	var data = {
			"storageModel" : formData.storageModel,
			"storageModelName" : formData.storageModelName,
			"storageType" : formData.storageType,
			"storageTypeName" : formData.storageTypeName,
			"sanLun" : formData.sanLun,
			"raidType" : formData.raidType,
			"raidTypeName" : formData.raidTypeName,
			"lunNum" : formData.lunNum,
			"dataChangeDegree" : formData.dataChangeDegree
	};
	$("#driverForm").iForm("setVal",data);
	$("#driverForm").iForm("endEdit");
}


//拿到树
function getTree(){
	var tree = window.parent.document.getElementById("assetHwTree").contentWindow.$('#property_tree');
	return tree;
}

//在树上加上节点
function addTreeNode( hwLedgerBean ){
	var tree = window.parent.document.getElementById("assetHwTree").contentWindow.$('#property_tree');
	var node = tree.tree('getSelected');
	if (node){
		if(node.state=="open"){
			tree.tree('append', {
				parent: node.target,
				data: window.parent.document.getElementById("assetHwTree").contentWindow.HardwareTree.changeToTreeNode(hwLedgerBean)
				//data:function(){return {children:[Property.objs.propertyBean]};}
			});
			var newNode = tree.tree('find', hwLedgerBean.hwId);
			tree.tree('select', newNode.target);
		}else{
			var objs = node;
			tree.tree('expand',node.target);
			tree.tree('options')
				.onExpand=function(node){
					if(objs == node){
						delete objs;
						var newNode = tree.tree('find', hwLedgerBean.hwId);
						tree.tree('select', newNode.target);
					}
				};
		}
	}
}

//更新树节点
function updateTreeNode( bean ){
	var node = getTree().tree('getSelected');
	if (node){
		getTree().tree('update', {
			target: node.target,
			text: bean.hwName
		});
	}
}

//删除树节点
function deleteTreeNode( pId ){
	var tree = getTree();
	tree.tree('remove', tree.tree('getSelected').target);
	var parent = tree.tree('find',pId);
	tree.tree('select',parent.target);
}

	  		