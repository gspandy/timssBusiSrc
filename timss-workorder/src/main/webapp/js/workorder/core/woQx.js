/* 表单字段定义  */
var fields = [
		    {title : "ID", id : "id",type : "hidden"},
		    {title : "文件编号", id : "defectCode",rules : {required:true}},
		    {title : "月度编号", id : "monthCode",rules : {required:true}},
		    {title : "设备Id", id : "equipId",type : "hidden",rules : {required:true}},
		    {title : "设备编号", id : "equipCode",type : "hidden",rules : {required:true}},
		    {title : "设备名称", id : "equipName",type:"label",value:"请从左边设备树选择",rules : {required:true}},
		    {title : "缺陷时间", id : "defectTime", type:"datetime", dataType:"datetime",
				rules : {required:true},
				options : {endDate : new Date()}
			},
		    {
		        title : "缺陷情况", 
		        id : "defectDes",
		        type : "textarea",
		        linebreak:true,
		        wrapXsWidth:12,
		        wrapMdWidth:8,
		        height:55,
		        rules : {required:true}
		    },
		    {title : "值班人员", id : "onDutyUserId",
		    	type : "combobox",
		    	data:onDutyUserGroup,
		    	options:{
			    	//url :  basePath + "workorder/woQx/getUserGroupUsers.do?userGroup="+woQxSignUserGroup.ONDUTYUSER,
			    	allowEmpty : true
//			    	remoteLoadOn : formRead?"set":"init"
		        }
		     }, 
		    {
		        title : "消缺情况", 
		        id : "defectSolveDes",
		        type : "textarea",
		        linebreak:true,
		        wrapXsWidth:12,
		        wrapMdWidth:8,
		        height:55
		    },
		    {title : "消缺时间", id : "defectSolveTime", type:"datetime", dataType:"datetime",
		    	linebreak:true ,
		    	rules : {greaterThan:"#f_defectTime"},
		    	options : {endDate : new Date()}
			},
			{title : "消缺人员", id : "defectSolveUserId",
		    	type : "combobox",
		    	data:defectSolveUserGroup,
		    	options:{
			    	allowEmpty : true
		        }
		     },
			{title : "运行人员", id : "runningUserId",
		    	type : "combobox",
		    	data:runningUserGroup,
		    	options:{
			    	allowEmpty : true
		        }
		     },
			 {
		        title : "遗留问题", 
		        id : "leftProblem",
		        type : "textarea",
		        linebreak:true,
		        wrapXsWidth:12,
		        wrapMdWidth:8,
		        height:55
		    },
		    {
		        title : "领导批示", 
		        id : "leaderInstructions",
		        type : "textarea",
		        linebreak:true,
		        wrapXsWidth:12,
		        wrapMdWidth:8,
		        height:55
		    },
			{title : "批示领导", id : "instructionsUserId",
		    	type : "combobox",
		    	data:instructionsUserGroup,
		    	options:{
			    	allowEmpty : true
		        }
		     }
		    
		];

function setFormVal(woQxId){
	$.post(basePath + "workorder/woQx/queryWoQxDataById.do",{woQxId:woQxId},
			function(woBasicData){
				var woQxFormData = woBasicData;
				var createuser = woQxFormData.createuser;
				$("#woQxRecordForm").iForm("setVal",woQxFormData).iForm("endEdit");
				
				initRelateWoDataGrid();		 
				var flag = false;
				if(loginUserId == createuser){
					flag = true;
				}
				
				if(flag){
					$("#btn_woQx_delete").show();
					$("#btn_woQx_edit1").show();
					$("#btn_woQx_commit").hide();
					$("#btn_qx_toWo").show();
				}else{
					$("#btn_woQx_commit").hide();
					$("#btn_woQx_delete").hide();
					$("#btn_woQx_edit1").hide();
				}
				
				FW.fixRoundButtons("#toolbar");   //解决按钮隐藏后的圆角问题
			},"json");
}	




/**提交工单基本信息*/
function commitWoQx(){  
	$("#btn_woQx_commit").button('loading');
	/**表单验证*/
	var tempEquiName = $("#woQxRecordForm").iForm("getVal","equipName");
	if(tempEquiName == '' || tempEquiName == "请从左边设备树选择"){
		FW.error("请从左边设备树选择");
		$("#btn_woQx_commit").button('reset');
		return;
	}
	if(!$("#woQxRecordForm").valid()){
		$("#btn_woQx_commit").button('reset');
		return ;
	}
	var woQxFormObj = $("#woQxRecordForm").iForm("getVal");
	var woQxFormData = JSON.stringify(woQxFormObj);  //取表单值
	
	 $.post(basePath + "workorder/woQx/commitWoQxdata.do",
	 		{"woQxRecordForm":woQxFormData},
			function(data){
				if(data.result == "success"){
						FW.success("保存成功");
						closeCurPage();
				}else {
					$("#btn_woQx_commit").button('reset');
					FW.error("操作失败");
				}
	  },"json");
	  
}


/** 暂存维护计划 （数据入库）*/
function editWoQx(){
	formRead = false;
	$("#woQxRecordForm").iForm("beginEdit");  //打开编辑
	$("#btn_qx_toWo").hide();
	$("#btn_woQx_edit1").hide();
	$("#btn_woQx_delete").hide();
	$("#btn_woQx_commit").show();
	FW.fixRoundButtons("#toolbar");  //解决按钮隐藏后的圆角问题
}
function deleteWoQx(){
	Notice.confirm("确定删除|确定删除该条缺陷记录么？",function(){
		var woQxFormObj = $("#woQxRecordForm").iForm("getVal");
		var woQxId = woQxFormObj.id;
		
		 $.post(basePath + "workorder/woQx/deleteWoQx.do",{"woQxId":woQxId},
					function(data){
						if(data.result == "success"){
							FW.success("删除成功");
							closeCurPage();
						}else {
							FW.error("删除失败");
						}
			  },"json");
	},null,"info");	
}

function qxToWoBtn(){
	var woQxFormObj = $("#woQxRecordForm").iForm("getVal");
	var woQxId = woQxFormObj.id;
	   //新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
	var rand = woQxFormObj.defectCode;
	var opts = {
	     id : "newWO" + rand,
	     name : "新建工单",
	     url : basePath+ "workorder/workorder/openNewWOPage.do?woQxId="+woQxId,
	     tabOpt : {
	     	closeable : true,
	     	afterClose : "FW.deleteTab('$arg'); FW.activeTabById('equmaintain');FW.getFrame('equmaintain').refresh();"
	     }
	 }
	 _parent()._ITC.addTabWithTree(opts); 
}
/**
 * 关闭当前tab 页
 */
function closeCurPage(flag){
	if(flag){
		FW.set("eqwoQxlistDoNotRefresh",true);
	}
	FW.deleteTabById(FW.getCurrentTabId());
}



/**
 * 详情打印
 */
function printWoQx(){
	var printUrl = "http://timss.gdyd.com/";
	var src = fileExportPath + "preview?__report=report/TIMSS2_WO_QXCLJL.rptdesign&__format=pdf"+
						"&woBdzqxId="+woQxId+"&author="+loginUserId+"&url="+printUrl;
	var url = encodeURI(encodeURI(src));

	var title ="缺陷记录详情"
	FW.dialog("init",{
		src: url,
		btnOpts:[{
	            "name" : "关闭",
	            "float" : "right",
	            "style" : "btn-default",
	            "onclick" : function(){
	                _parent().$("#itcDlg").dialog("close");
	             }
	        }],
		dlgOpts:{ width:800, height:650, closed:false, title:title, modal:true }
	});
	
}

function initRelateWoDataGrid(){
	$("#title_relateWo").iFold("init");
	$("#title_relateWo").iFold("show");
	var woQxFormObj = $("#woQxRecordForm").iForm("getVal");
	var woQxId = woQxFormObj.id;
	
    dataGrid = $("#relateWoTable").iDatagrid("init",{
        pageSize:20,//pageSize为全局变量
        singleSelect:true,
        url: basePath + "workorder/woQx/qxRelateWoListdata.do?woQxId="+woQxId,	//basePath为全局变量，自动获取的       
        columns:[[ 
				{field:"workOrderCode",title:"编号",width:110,fixed:true,sortable:true},
				{field:"woSpecCode",title:"专业",width:85,fixed:true,sortable:true,
					formatter: function(value,row,index){
							return FW.getEnumMap("WO_SPEC")[value]; 
						},
						"editor" : {
					        "type":"combobox",
					        "options" : {
					        	"data" : FW.parseEnumData("WO_SPEC",_enum)						        	
					        }
					    }
				},   
				{field:"description",title:"名称",width:280}, 
				{field:"createuser",title:"报障人",width:70,fixed:true},
				{field:"createdate",title:"报障时间",width:130,fixed:true,sortable:true,
					formatter: function(value,row,index){
						//时间转date的string，还有long2date(value)方法
						return FW.long2time(value);
					}
				},   
				{field:"currStatus",title:"状态",width:130,fixed:true,sortable:true,
					formatter: function(value,row,index){
							return FW.getEnumMap("WO_STATUS")[value]; 
						},
						"editor" : {
					        "type":"combobox",
					        "options" : {
					            "data" : FW.parseEnumData("WO_STATUS",_enum)	
					        }
					    }
				}
			]],
		onLoadSuccess: function(data){
	        //搜索时的无数据信息
	        if(data && data.total==0){
	        	$("#title_relateWo").iFold("hide");
	        }
        },
        onDblClickRow : function(rowIndex, rowData) {
        	switch(rowData.currStatus){
        		case "draft":;
        		case "newWo":;
        		case "monitorAudit":;
        		case "assistantAudit":;
        		case "plantLeaderAudit":;
        		case "assistantToTeamleader":toWoBaseInfoPage(rowData);break;
        		case "woPlan":;
        		case "inPTWing":;
        		case "endWOReport":;
        		case "woAcceptance":;
        		case "woFiling":;
        		case "woObsolete":toWoPlanPage(rowData);break;
        	}
		}
    });
}
function  toWoPlanPage(rowData){
	var woId = rowData.id;
	var woStatus = rowData.currStatus;
	   //新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
   var rand = rowData.workOrderCode;
   var opts = {
        id : "woPlanInfo" + rand,
        name : "工单详情",
        url : basePath+ "workorder/workorder/openWOPlanInfoPage.do?woId="+woId+"&woStatus="+woStatus ,
        tabOpt : {
        	closeable : true,
        	afterClose : "FW.deleteTab('$arg');if(!FW.get('ptwDoNotClose')){FW.activeTabById('equmaintain');FW.getFrame('equmaintain').refreshAfterClose();}FW.set('ptwDoNotClose',false);"
        }
    }
    _parent()._ITC.addTabWithTree(opts); 
}
function toWoBaseInfoPage(rowData){
	var woId = rowData.id;
	var woStatus = rowData.currStatus;
   var rand = rowData.workOrderCode;
   var opts = {
        id : "woInfo" + rand,
        name : "工单详情",
        url : basePath+ "workorder/workorder/openNewWOPage.do?woId="+woId+"&woStatus="+woStatus ,
        tabOpt : {
        	closeable : true,
        	afterClose : "FW.deleteTab('$arg'); FW.activeTabById('equmaintain');FW.getFrame('equmaintain').refreshAfterClose();"
        }
    }
    _parent()._ITC.addTabWithTree(opts); 
}
