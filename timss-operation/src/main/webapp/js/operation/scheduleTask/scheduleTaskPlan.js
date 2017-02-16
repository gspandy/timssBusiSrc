var contentFormFields=[
		 {id:"id",type:"hidden"},
		 {title : "编号",id:"code"},
		 {title : "类型", id:"type", type : "combobox",
				dataType : "enum",
				enumCat : "OPR_SCHEDULE_TYPE"
		 },
		{title : "关联设备Id", id : "assetId",type : "hidden"},
	    {title : "关联设备",id : "assetName",type:"label",value:"请从左边设备树选择",rules : {required:true},
	    	formatter:function(val){
				var text = val;
				if(text=="请从左边设备树选择"){
					text = "<label style='color:red'>"+val+"</label>";
				}
				return text;
			}
	    },
	    {
			title : "状态",
			id : "activityFlag",
			type : "combobox",
			data : [ [ "Y", "启用" ], [ "N", "停用" ] ]
		},
		 {
		        title : "工作内容", 
		        id : "content",
		        type : "textarea",
		        linebreak:true,
		        wrapXsWidth:12,
		        wrapMdWidth:8,
		        height:48,
		        rules : {
					required : true,
					maxChLength:300
				}
		    }	    
];
var cycleFormFields=[
              		 {title : "重复", id:"cycleType", type : "combobox",
              				dataType : "enum",
              				enumCat : "OPR_SCHEDULE_CYCLE",
              				options:{
            					initOnChange: false,
            					onChange:function(val){
            						switch(val){
            						case "O" :
            							$("#cycleForm").iForm("setVal",{"cycleLen":null,"specifiedDate":null});
            							$("#cycleForm").iForm("hide",["cycleLen","specifiedDate","specifiedWeek"]);
            							break;
            						case "D" :
            						
            						case "M" :
            						case "Y" :
            							getCycleLenWeekNumber(val);
            							if( id == "" || id == null){
            								$("#cycleForm").iForm("setVal",{"cycleLen":null,"specifiedDate":null});
            							}
            							$("#cycleForm").iForm("show",["cycleLen"]);
            							$("#cycleForm").iForm("hide",["specifiedDate","specifiedWeek"]);
            							break;
            						case "W" :
            							getCycleLenWeekNumber(val);
            							if( id == "" || id == null){
            								$("#cycleForm").iForm("setVal",{"cycleLen":null,"specifiedDate":null});
            							}
            							$("#cycleForm").iForm("show",["cycleLen"]);
            							$("#cycleForm").iForm("hide",["specifiedDate"]);
            							break;
            						case "FD_M" :
            							$("#cycleForm").iForm("setVal",{"cycleLen":1,"specifiedDate":null})
            							$("#cycleForm").iForm("hide",["cycleLen","specifiedDate"]);
            							break;
            						case "LD_M" :
            							$("#cycleForm").iForm("setVal",{"cycleLen":1,"specifiedDate":null})
            							$("#cycleForm").iForm("hide",["cycleLen","specifiedDate"]);
            							break;
            						case "SD_M" :
            							$("#cycleForm").iForm("setVal",{"cycleLen":1})
            							$("#cycleForm").iForm("hide",["cycleLen"]);
            							$("#cycleForm").iForm("show",["specifiedDate"]);
            							break;
            						}
                  				}
            				}
              		 },
              		{title : "重复频率", id : "cycleLen",type : "combobox",
              			 rules : {required:true}
              		 },
              		{title : "指定日期", id : "specifiedDate",
              			type : "combobox",
              			rules : {
              				required : true
              			}
              		 },
              		{title : "指定星期", id : "specifiedWeek",
               			type : "combobox",
               			rules : {
               				required : true
               			}
               		 },
              		{title : "开始日期", id : "beginTime", type:"date",dataType:"date",
    					rules : {required:true},
    					options : {startDate : new Date()}
    				},
    				{title : "失效日期", id : "invalideTime", type:"date", dataType:"date",
    					rules : {greaterThan:"#f_beginTime"},
    					options : {startDate : new Date()},
    					helpMsg:"如果不填，表示永久有效"
    				},
              	    {
              			title : "转其他班次",
              			id : "nextShift",
              			type : "combobox",
              			data : [ [ "Y", "允许" ], [ "N", "不允许" ] ],
              			rules : {
              				required : true
              			}
              		}    
              ];
var workteamFormFields=[
              		{title : "工种", id:"deptId", type : "combobox",
              			rules : {required : true}
              		},              		
              	    {title : "班次",id : "shiftIds",type : "combobox",
              			rules : {required : true}
              		}    
              ];

function getCycleLenWeekNumber(type){
	var unit = "";
	switch(type){	
	case "D" :unit = "天" ;break;	
	case "M" :unit = "月" ;break;
	case "W" :unit = "周" ;break;
	case "Y" :unit = "年" ;break;
	}
	var dateArr = [];
	for ( var i = 1; i <= 30; i++) {
		dateArr.push( [i,i+" "+unit]);
	}
	$("#f_cycleLen").iCombo("init", {
		data : dateArr,
		allowEmpty:true
	});
	
	$("#cycleForm").iForm("setVal",{"cycleLen":cycleLen});
}


function getDateNumberOfMonth(){
	var dateArr = [];
	for ( var i = 1; i <= 31; i++) {
		dateArr.push( [i,i]);
	}
	$("#f_specifiedDate").iCombo("init", {
		data : dateArr,
		multiselect:true,
		allowEmpty:true
	});
	$("#cycleForm").iForm("hide",["specifiedDate"]);
}
function getWeekNumberOfWeek(){
	var weekArr = [];
		weekArr.push( [1,"星期一"]);
		weekArr.push( [2,"星期二"]);
		weekArr.push( [3,"星期三"]);
		weekArr.push( [4,"星期四"]);
		weekArr.push( [5,"星期五"]);
		weekArr.push( [6,"星期六"]);
		weekArr.push( [0,"星期日"]);
	$("#f_specifiedWeek").iCombo("init", {
		data : weekArr,
		multiselect:true,
		allowEmpty:true
	});
	$("#cycleForm").iForm("hide",["specifiedWeek"]);
}

function initPageData(id){
	$.post(basePath + "operation/scheduleTaskPlan/queryTaskPlanById.do",
		{"id":id},
		function(data){
			if(data.result == "success"){
				var taskplan = JSON.parse(data.taskPlan);
				shiftIds = taskplan.shiftIds;
				cycleLen = taskplan.cycleLen;
				$("#inPageTitle").html("定期工作计划详情");
				$("#contentForm").iForm("setVal",taskplan);
//				getCycleLenWeekNumber(taskplan.cycleType);
				$("#cycleForm").iForm("setVal",taskplan);
				if(taskplan.assetId == "" || taskplan.assetId == null ){
					$("#contentForm").iForm("hide","assetName");
				}
				$.ajaxSetup({'async':false}); 
				//先加载班次数据
				changeShift(taskplan.deptId);
				$("#workteamForm").iForm("setVal",taskplan);
				$("#workteamForm").iForm("endEdit");
				$.ajaxSetup({'async':true});
				
				$("#contentForm").iForm("endEdit");
				$("#cycleForm").iForm("endEdit");
				
				$("#btn_save").hide();
			}
			FW.fixToolbar("#toolbar1");
	},"json");
}

//删除按钮
function del(){
	FW.confirm("确定删除吗？删除后数据不能恢复",function(){
		$.post(basePath+'operation/scheduleTaskPlan/delTaskPlanById.do',{"id":id},function(data){
			if(data.result == "success"){
				FW.success("删除成功");
				FW.deleteTabById(FW.getCurrentTabId());
			}
		});
	});
	
}

function edit(){
	$("#btn_save").show();
	$("#btn_edit").hide();
	$("#contentForm").iForm("beginEdit");
	$("#contentForm").iForm("endEdit","code");
	$("#cycleForm").iForm("beginEdit",["invalideTime","nextShift"]);
	$("#contentForm").iForm("show","assetName");
	$("#workteamForm").iForm("beginEdit");
	
	FW.fixToolbar("#toolbar1");
}

function save(_this){
	if(!valid("contentForm") || !valid("cycleForm") || !valid("workteamForm")){
		return ;
	}
	var contentData=$("#contentForm").iForm('getVal');
	var cycleData=$("#cycleForm").iForm('getVal');
	var workteamData=$("#workteamForm").iForm('getVal');
	var scheduleTaskPlanData = {};
	$.fn.extend(true,scheduleTaskPlanData,contentData,cycleData,workteamData);
	var jumpPath = basePath+'operation/scheduleTaskPlan/insertTaskPlan.do';
	if(scheduleTaskPlanData.id && scheduleTaskPlanData.id!=null ){
		jumpPath = basePath+'operation/scheduleTaskPlan/updateTaskPlan.do';
	}
	$.post(jumpPath,{"scheduleTaskPlan":FW.stringify(scheduleTaskPlanData)},function(data){
		if(data.result == "success"){
			$("#contentForm").iForm('setVal',{id:data.id,code:data.code});
			FW.success("保存成功");
			closeTabUnMsg();
		}
	});
}

function valid(formId){
	if(!$("#"+formId).valid()){
		return false;
	}
	return true;
}

/**左边树菜单选择触发事件*/
function passAssetSelect(data){
	selectEquipId = data.id;
	selectEquipName = data.text;
	selectEquipCode = data.assetCode;
	$("#contentForm").iForm("setVal",{assetId:selectEquipId,assetName:selectEquipName+"/"+selectEquipId})
  
}

function changeShift(val){
	$("#workteamForm").iForm("setVal",{"shiftIds":null});
	var url = basePath + "operation/shift/queryShiftByStationId.do?stationId="+val ;
	$.ajax({
		url : url,
		type : 'post',
		dataType : "json",
		success : function(data) {
			if( data.total > 0 ){
				var shiftList = data.rows;
				var shiftArr = [];
				var tempshiftIds = "";
				for( var index in shiftList ){
					if(shiftList[index].type == "normal"){
						shiftArr.push( [shiftList[index].id,shiftList[index].name]);
						if(shiftIds.indexOf(shiftList[index].id) >= 0 ){
							tempshiftIds = tempshiftIds + shiftList[index].id + ",";  
						}
					}
				}
				
				$("#f_shiftIds").iCombo("init", {
					data : shiftArr,
					multiselect:true,
					allowEmpty:true
				});
				$("#workteamForm").iForm("setVal",{"shiftIds":tempshiftIds});
			}
		}
	});
}
