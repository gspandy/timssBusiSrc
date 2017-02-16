var contentFormFields=[
		 {id:"id",type:"hidden"},
		 {title : "工种", id:"deptName"},  
		 {title : "类型", id:"type", type : "combobox",
				dataType : "enum",
				enumCat : "OPR_SCHEDULE_TYPE"
		 },
		{title : "关联设备Id", id : "assetId",type : "hidden"},
	    {title : "关联设备",id : "assetName"},
	    {
	        title : "工作内容", 
	        id : "content",
	        type : "textarea",
	        linebreak:true,
	        wrapXsWidth:12,
	        wrapMdWidth:8,
	        height:48
	    },
	    {title : "创建时间", id : "createdate", type:"datetime",linebreak:true},
	    {title : "工种", id:"deptId", type : "hidden"}, 
		{title : "负责班次", id : "shiftName"}
];
var doInfoFormFields=[
                     {title : "执行状态", id : "doStatus",type : "combobox",
                    	 dataType : "enum",
         				  enumCat : "OPR_SCHEDULE_DOSTATUS"
         			 },
              		 {title : "执行结果", id:"doResult", type : "combobox",
         				data : [
         				         ['normal','正常'],
								 ['abnormal','异常']								 
								]
              		 },
              		{title : "执行时间", id : "doTime", type:"datetime"},
    				{title : "执行人", id : "doUserNames"},
    				{title : "记录人", id : "recorderName", linebreak:true},
    				{title : "记录时间", id : "recordeTime", type:"datetime"},
              	    {
              			title : "情况及备注",
              			id : "remarks",
              			 type : "textarea",
         		        linebreak:true,
         		        wrapXsWidth:12,
         		        wrapMdWidth:8,
         		        height:48
              		}    
              ];

var	changeInfoGridField = [[
   	                 	{field : 'id',title : 'ID', hidden:true},
   	                 	{field : 'createdate',title : '操作时间',width:90,fixed:true,
	   	                 	formatter: function(value,row,index){
								return FW.long2date(value);
							}
   	                 	}, 
   	              		{field : 'beforeShift',title : '流转前班次',width:160,fixed:true}, 
   	              		{field : 'afterShift',title : '流转后班次',width:160,fixed:true},
   	              		{field : 'remarks',title : '原因及备注',width:200}   	              		
   	              	]];

function initPageData(id){
	$.post(basePath + "operation/scheduleTask/queryTaskById.do",
		{"id":id},
		function(data){
			if(data.result == "success"){
				var scheduleTask = JSON.parse(data.scheduleTask);
				var currShiftDate = scheduleTask.shiftDate;
				var dateFlag = data.dateFlag;
				var doStatus = scheduleTask.doStatus;
				var oprBtnPriv = data.oprBtnPriv;
				var canNextShiftFlag = data.canNextShiftFlag;
				if(oprBtnPriv == "Y"){  //登录人和待执行工作工种是同一工种
					sameStationIdFlag = true;
				}
				$("#contentForm").iForm("setVal",scheduleTask);
				$("#contentForm").iForm("endEdit");
				$("#btn_do").hide();
				$("#btn_undo").hide();
				$("#btn_change").hide();
				if(doStatus != "undo"){
					$("#title_doInfo").iFold("init");
					$("#title_doInfo").iFold("show");
					$("#doInfoForm").iForm('init',{"fields":doInfoFormFields});
					$("#doInfoForm").iForm("setVal",scheduleTask);
					$("#doInfoForm").iForm("endEdit");
				}else{
					$("#title_doInfo").iFold("hide");
					// 通过日期判断(如果当前日期大于等于负责班次的日期，就显示)
					var oprBtnAndDatePriv = (oprBtnPriv == "Y") && (dateFlag == "true");
					if(oprBtnAndDatePriv){   //如果是同一个工种的,并且当前日期大于负责班次的日期，则有操作权限
						$("#btn_do").show();
						$("#btn_undo").show();
						if(canNextShiftFlag == "Y"){
							$("#btn_change").show();
						}
					}
				}
				
				$("#title_changeInfo").iFold("init");
				var changeInfo =  $("#changeInfoGrid").datagrid({
					    columns:changeInfoGridField,
					    singleSelect:true,
					    fitColumns:true,
					    nowrap : false,  //设置可以换行
					    scrollbarSize:0
					}); 
				var taskChangeShiftListObj = JSON.parse( data.taskChangeShiftList );
				if(taskChangeShiftListObj != null && taskChangeShiftListObj.length!=0){
					$("#changeInfoGrid").datagrid("loadData",taskChangeShiftListObj );
				}else{
					$("#title_changeInfo").iFold("hide");
				}	
					
			}
			//控制权限
			Priv.apply();
			if(listdoStatus == "changeShift" ){
				$("#btn_do").hide();
				$("#btn_undo").hide();
				$("#btn_change").hide();
			}
			FW.fixToolbar("#toolbar1");
	},"json");
}


function showDoDialog(){
	if(!sameStationIdFlag){
		FW.error("当前登录人不是该工种的人员");
		return false;
	}
    var src = basePath + "page/operation/core/scheduleTask/doTask.jsp";
    var btnOpts = [{"name" : "取消",
		            "onclick" : function(){
		                return true;
		               }
		        	},
		           {
		            "name" : "确定",
		            "style" : "btn-success",
		            "onclick" : function(){
		                //itcDlgContent是对话框默认iframe的id
		                var p = _parent().window.document.getElementById("itcDlgContent").contentWindow;
		                if (!p.valid()) {
							return false;
						}
		                var doTaskDataForm = p.$("#doTaskForm").iForm("getVal");
		                $.post(basePath + "operation/scheduleTask/doTaskById.do",
								{"id":id,"doTaskInfo":FW.stringify(doTaskDataForm)},
									function(data){
										if(data.result == "success"){
											FW.success("操作成功");
											_parent().$("#itcDlg").dialog("close");
											FW.deleteTabById(FW.getCurrentTabId());
										}
							},"json");
						
		            }
		           }];
    var dlgOpts = {width : 400,height:300, title:"填写执行情况",};
    FW.dialog("init",{"src":src,"dlgOpts":dlgOpts,"btnOpts":btnOpts}); 
}

function showUnDoDialog(){
	if(!sameStationIdFlag){
		FW.error("当前登录人不是该工种的人员");
		return false;
	}
    var src = basePath + "page/operation/core/scheduleTask/notdoTask.jsp";
    var btnOpts = [{"name" : "取消",
		            "onclick" : function(){
		                return true;
		               }
		        	},
		           {
		            "name" : "确定",
		            "style" : "btn-success",
		            "onclick" : function(){
		                //itcDlgContent是对话框默认iframe的id
		                var p = _parent().window.document.getElementById("itcDlgContent").contentWindow;
		                if (!p.valid()) {
							return false;
						}
		                var notdoTaskForm = p.$("#notdoTaskForm").iForm("getVal");
		                var notdoTaskRemarks = notdoTaskForm.remarks;
		                
						$.post(basePath + "operation/scheduleTask/undoTaskById.do",
							{"id":id,"remarks":notdoTaskRemarks},
								function(data){
									if(data.result == "success"){
										FW.success("操作成功");
										_parent().$("#itcDlg").dialog("close");
										FW.deleteTabById(FW.getCurrentTabId());
									}
						},"json");
						
						
		            }
		           }];
    var dlgOpts = {width : 400,height:300, title:"填写不执行原因",};
    FW.dialog("init",{"src":src,"dlgOpts":dlgOpts,"btnOpts":btnOpts}); 
}

function showChangeDialog(){
	if(!sameStationIdFlag){
		FW.error("当前登录人不是该工种的人员");
		return false;
	}
	var deptId = $("#contentForm").iForm("getVal").deptId;  //工种id
    var src = basePath + "page/operation/core/scheduleTask/changeShift.jsp?stationId="+deptId;
    var btnOpts = [{"name" : "取消",
		            "onclick" : function(){
		                return true;
		               }
		        	},
		           {
		            "name" : "确定",
		            "style" : "btn-success",
		            "onclick" : function(){
		                //itcDlgContent是对话框默认iframe的id
		                var p = _parent().window.document.getElementById("itcDlgContent").contentWindow;
		                if (!p.valid()) {
							return false;
						}
		                var changeShiftForm = p.$("#changeShiftForm").iForm("getVal");
		               
						$.post(basePath + "operation/scheduleTask/changeShiftById.do",
								{"id":id,"shiftInfo":FW.stringify(changeShiftForm)},
								function(data){
									if(data.result == "success"){
										var shiftName = data.shiftName;
										$("#contentForm").iForm("setVal",{"shiftName":shiftName});
										FW.success("操作成功");
										_parent().$("#itcDlg").dialog("close");
										refresh();
									}
							},"json");
		            }
		           }];
    var dlgOpts = {width : 400,height:300, title:"转其他班次",};
    FW.dialog("init",{"src":src,"dlgOpts":dlgOpts,"btnOpts":btnOpts}); 
}


