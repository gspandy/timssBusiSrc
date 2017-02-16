var noteFormHideField=[];//不显示的表单字段
var opts={//动态表单配置
	validate:true,
	fixLabelWidth:true,
	labelFixWidth: 200
};
//基础信息
var baseFields = [
				{title : "日期", id : "dateTime", type : "label"},
	  			{title : "当值", id : "dutyName", type : "label"},
	  			{title : "当前值别Id", id : "currentDutyId", type : "hidden"},
	  			{title : "当前班次Id", id : "currenShiftId", type : "hidden"},
	  			{title : "当前日历Id", id : "nowScheduleId", type : "hidden"},
	  			{title : "动态表单KEY", id : "keyword", type : "hidden"},
	  			{title : "值班人员", id : "dutyPerson",type : "label",breakAll : true,linebreak:true,wrapXsWidth:12,wrapMdWidth :12},
	  			{title : "交班人", id : "lastUserName",type : "label",breakAll : true},
	  			{title : "接班人", id : "nextUserName",type : "label",breakAll : true},
	  			{title:"班前会",id:"preShiftMeeting",linebreak:true,type:'textarea',
   					wrapXsWidth:12,wrapMdWidth:8,height:48,rules:{maxChLength:parseInt(4000*2/3)}
   				},
   				{title:"班后会",id:"postShiftMeeting",linebreak:true,type:'textarea',
   					wrapXsWidth:12,wrapMdWidth:8,height:48,rules:{maxChLength:parseInt(4000*2/3)}
   				},
	  			{
			        title : "上一班情况", 
			        id : "lastContent_base",
			        type : "hidden",
			        breakAll : true,
			        linebreak:true,
			        wrapXsWidth:12,
			        wrapMdWidth:8,
			        height : 50
			    },
			    {
			        title : "上一班交待", 
			        id : "lastRemark_base",
			        type : "hidden",
			        breakAll : true,
			        wrapXsWidth:12,
			        wrapMdWidth:8,
			        height : 50
			    }
	  		];

var pageCode = "opr_note_formcode_";
var formId = "baseform";

String.prototype.replaceAll = function(s1,s2) { 
    return this.replace(new RegExp(s1,"gm"),s2); 
};

//动态表单初始化
function dynamicForm( jobsId, stationId, handoverId ){
	pageCode = "opr_note_formcode_";
	var keyword = jobsId + "_" + stationId + "_" + handoverId;
	keyword = keyword.replaceAll("-", "_").replaceAll("_", "");
	/**** -----------------------------------------------动态表单 start ------------------------------ */
	var tempStation = '';
	if( stationId != null && stationId != "" && stationId.length > 8 ){
		tempStation = stationId.replaceAll("-", "_").replaceAll("_", "").substring(8);
	}else{
		tempStation = stationId;
	}
	pageCode += jobsId + "_" + tempStation;
	pageCode = pageCode.replaceAll("_", "");
	var formOptions = opts;
	var formSearchParam = { "keyword" : keyword, "sheetId" : keyword, "formSwitch" : "on"};
	var formUrlParam = basePath + "operation/note/queryOnDutyBaseInfo.do";
	/**** -----------------------------------------------动态表单 end ------------------------------ */
	
	//初始化基础信息
	//$("#baseform").iForm("init",{"options":opts,"fields":baseFields});
	FW.dynamicForm(baseFields, formOptions, formSearchParam, formUrlParam);
}

//设置基础表单
function setBaseformData(){
	var url = basePath + "operation/note/queryNoteBaseInfoBySearch.do?stationId=" + stationId + 
	"&jobsId="+ jobsId + "&shiftId=" + shiftId + "&date=" + dateStr;
	$.ajax({
		url : url,
		type : 'post',
		dataType : "json",
		success : function(data) {
			if( data.total == 1 ){
				var vo = data.rows[0];
				var keyword = jobsId + "_" + stationId + "_" + vo.handoverId;
				keyword = keyword.replaceAll("-", "_").replaceAll("_", "");
				var formData = {
						"currentDutyId" : vo.dutyId,
						"currentShiftId" : vo.shiftId,
						"nowScheduleId" : vo.nowScheduleId,
						"dutyName" : vo.deptName+" "+vo.dutyName + " " + vo.shiftName,
						"dutyPerson" : vo.persons?vo.persons:"没有值班人员",
						"lastUserName" : vo.lastUserName,
						"nextUserName" : vo.nextUserName,
						"lastContent_base" : vo.nextContent,
						"lastRemark_base" : vo.nextRemark,
						"dateTime" : dateStr,
						"keyword" :keyword,
						"preShiftMeeting":vo.preShiftMeeting,
						"postShiftMeeting":vo.postShiftMeeting
				};
				$("#baseform").iForm("setVal", formData );
				$("#baseform").iForm("hide",noteFormHideField);
				$("#baseform").iForm("endEdit");
			}else{
				FW.error( "无基础数据！" );
			}
		}
	});
}

//通过岗位 日期拿到运行记事
function setNoteHistory( ){
	var url = basePath + "operation/note/queryNoteHistory.do?stationId=" + stationId  + "&jobsId=" 
			+ jobsId + "&shiftId=" + shiftId + "&date=" + dateStr;
	
	//此处一定要给dataGrid赋值。dataGrid为全局变量，不设置会导致不能resize
	$("#noteTb").iDatagrid("init",{
        pageSize:pageSize,//pageSize为全局变量，自动获取的
        singleSelect :true,
        fitColumns:true,
        nowrap : false,
        url: url,	//basePath为全局变量，自动获取的       
        "columns":[[
    				{field:'id',title:'id',width:180,fixed:true,hidden: true},
    				{field:'writeTime',title:'记事时间',width:130,fixed:true,sortable:true,
    					formatter:function(val){
    						return FW.long2time(val);
    					}
    				},
    				{field:'type',title:'记事类型',width:140,fixed:true,sortable:true,
    					formatter:function(val){
    						return FW.getEnumMap("OPR_NOTE_TYPE")[val];
    					}	
    				},
    				{field:'crewNum',title:'机组号',width:70,fixed:true,sortable:true,
    					formatter:function(val){
    						return FW.getEnumMap("OPR_CREW_NUM")[val];
    					}	
    				},
    				{field:'content',title:'记事内容',width:200,formatter:function(val,row,index){
						return val?val.replace(/\n|\r\n/g,"<br/>"):"";
					}},
					{field:'blankColumn',title:'',width:7,fixed:true}
    				]],
		"onLoadError": function(){
			//加载错误的提示 可以根据需要添加
			$("#noteDiv").hide();
		    $("#grid1_error").show();
		},
        onLoadSuccess: function(data){
            //远程无数据需要隐藏整个表格和对应的工具条，然后在无数据信息中指引用户新建
            if(data && data.total==0){
                $("#grid1_empty").show();
            }else{
                $("#grid1_empty").hide();
            }
        },
        onBeforeLoad : function() {
        	if(noteDatagridHideColumns){
	    		$.each(noteDatagridHideColumns,function(name,value){
	    			$('#noteTb').datagrid("hideColumn",value);
	    		});
        	}
	    }
    });
}


//填充分组
function teamOption( jobsId, teams, dutyId, handoverId  ){
	if( teams.length > 0 ){
		var teamObj=eval(teams);
		var teamsArr = [];
		for( var index in teamObj ){
			teamsArr.push( [teamObj[index],teamObj[index]]);
		}
		$("#teamSelect").iCombo("init", {
			data : teamsArr,
			"onChange" : function(val) {
				loadModeContentDataByDutyJobsHandover(  dutyId, jobsId, handoverId, val);
				setTimeout('$("#defined_msk").css({"display":"none"});',500);
				
			},onBeforeChange: function(val){
		  		var width = $("#modeContentDiv").width();
		  		var height = $("#modeContentDiv").height();
		  		$("#defined_msk").css({"width":width, "height":height, "display":"table"});
		  	}
		});
		//当只有一种分组时
		if( teamsArr.length == 1 ){
			$("#teamSelectDiv").hide();
		}
	}else{
		$("#modeContentDiv").iFold("hide");
	}
}

//初始化运行方式
function loadModeContentDataByDutyJobsHandover( dutyId, jobsId, handoverId, team ){
	var url = basePath + "operation/modeContent/queryModeContentByDutyJobsHandover.do";
	$.ajax({
		url : url,
		type : 'post',
		data :{
			dutyId : dutyId,
			jobsId : jobsId,
			handoverId : handoverId,
			team : team
		},
		dataType : "json",
		success : function(data) {
			if( data.result == "success" ){
				$("#modeFormDiv").show();
				$("#mode_empty").hide();
				var vos = data.vos;
				setFields( vos, "modeContentForm" );
			}else{
				$("#modeFormDiv").hide();
				$("#mode_empty").show();
			}
		}
	});
}

//设置运行方式字段for 显示字段
function setFields( beans, modeFormId ){
	//构造fields
	var modeContentFields = '';
	for( var i in beans ){
		modeContentFields += ',{' +
		'	title : "'+ beans[i].assetName +'", ' +
		'	id : "'+ beans[i].assetId +'",' +
		'	type : "label",' +
		'	labelXsWidth : 8,' +
		'	labelMdWidth : 8,' +
		'	inputXsWidth : 4,' +
		'	inputMdWidth : 4' +
		'}' ;
	}
	modeContentFields = "[" + modeContentFields.substring(1) +"]";
	var modeOpt ={
			validate:true,
			fixLabelWidth:false,
			labelFixWidth : "250px"
		};

	$("#" + modeFormId ).iForm("init",{"options":modeOpt,"fields":eval( modeContentFields )});
	var data = {};
	for( var index in beans ){
		data[beans[index].assetId] = beans[index].content;
	}
	$("#" + modeFormId ).iForm("setVal", data);
}

