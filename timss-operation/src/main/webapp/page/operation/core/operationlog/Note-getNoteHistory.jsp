<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>查询运行记事</title>
<script>_useLoadingMask = true;</script>

<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src='${basePath}js/operation/common/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/operation/common/formatterDate.js?ver=${iVersion}'></script>
<script src='${basePath}js/operation/common/stationCommon.js?ver=${iVersion}'></script>

</head>
<body style="height: 100%;" class="bbox list-page">
	<div class="bbox toolbar-with-pager" id="toolbar_wrap" style="height: 40px;">
	    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar1" class="btn-toolbar">
	    	<div style="width: 15%;float:left;">
	    		<div class="input-group input-group-sm" style="width:100%;float:left;margin-left:0px;margin-top:6px;">
					<input type="text" id="search" icon="itcui_btn_mag" placeholder="请输入记事内容" style="width:150px"/> 
				</div>
	    	</div>
        	<div style="width: 42%;float:left;">
	          <form id="toolbarForm" style=""></form> 
	        </div>
	        <div class="btn-group-sm" style="width:43%;float: left;margin-top:5px;">
			  	<button type="button" class="btn btn-default"  id="searchBtn" style="display:none">查询</button>
			  	<%-- <button type="button" class="btn btn-default " privilege="OPR_NOTE_PRINT" id="printButton">打印</button>--%>
			  	<div class="btn-group btn-group-sm" style="float: none;">
				  	<button type="button" class="btn btn-default" id="upBtn">上一天</button>
				    <button type="button" class="btn btn-default"  id="downBtn">下一天</button>
			  	</div>
	        </div>
	    </div>
	</div>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both;"></div>
	
	<div id="baseInfoDiv">
			<!-- <form id="baseForm" class="autoform" ></form> -->
			<div  class="itcui_frm_grp_title" style="width:100%;">
			<span class="itcui_frm_grp_title_txt">基本信息</span>
			</div>
			<div style="margin-top: 1px;">
				<table id="contentTb2" class="eu-datagrid" class="eu-datagrid">
			    </table>
			</div>
	</div>
		
	<!-- 运行记事 -->
	<div id="noteDiv" style="margin-top: 5px;">
		<div  class="itcui_frm_grp_title" style="width:100%;">
			<span class="itcui_frm_grp_title_txt">运行记事</span>
		</div>
		<div id="tableDiv" style="margin-top: 1px;">
		    <table id="contentTb" pager="#pagination_1" class="eu-datagrid">
		    </table>
		</div>
	</div>
	<!-- 错误信息-->
	<div class="row" id="grid1_error" style="display:none">
	    无法从服务器获取数据，请检查网络是否正常
	</div>
	<!-- 无数据 -->
	<div class="row" id="grid1_empty" style="display:none">
	    没有运行记事数据
	</div>
	<!-- 错误信息-->
	<div class="row" id="grid2_error" style="display:none">
	    无法从服务器获取数据，请检查网络是否正常
	</div>
	<!-- 无数据 -->
	<div class="row" id="grid2_empty" style="display:none">
	    没有排班数据，请查看是否已安排排班
	</div>
	
<script>
var flag = true;

//全局变量，使得可以通过jobsId获取jobs名字
var jobsList = new Array();
//填充工种
function jobsOption( stationId ){
	$("#f_jobsId").html("");
	var url = basePath + "operation/jobs/queryJobsByStationId.do?stationId=" + stationId ;
	$.ajax({
		url : url,
		type : 'post',
		dataType : "json",
		success : function(data) {
			if( data.total > 0 ){
				jobsList = data.rows;
				var jobsArr = [];
				//jobsArr.push( ["0", "请选择工种",true]);
				for( var index in jobsList ){
					jobsArr.push( [jobsList[index].id,jobsList[index].name]);
				}
				$("#f_jobsId").iCombo("init", {
					data : jobsArr,onChange :searchShift
				});
				
				//flag = true表示第一次加载页面
				if( flag ){
					var date = new Date().Format("yyyy-MM-dd");
					//date = "2014-07-18";
					$("#f_date").val(date);
					getBaseInfo(stationId,jobsList[0].id,0,date);
					setNoteHistory( stationId, jobsList[0].id, 0, date );
					flag = false;
				}
			}
		}
	});
}

//填充班次
function shiftOption( stationId ){
	$("#f_shiftId").html("");
	var url = basePath + "operation/shift/queryShiftByStationId.do?stationId="+stationId ;
	$.ajax({
		url : url,
		type : 'post',
		dataType : "json",
		success : function(data) {
			if( data.total > 0 ){
				var shiftList = data.rows;
				var shiftArr = [];
				shiftArr.push( ["0", "全天",true]);
				for( var index in shiftList ){
					if(shiftList[index].type == "normal"){
					shiftArr.push( [shiftList[index].id,shiftList[index].name]);
					}
				}
				$("#f_shiftId").val("0");
				/* $("#f_shiftId").iCombo("init", {
					data : shiftArr
				}); */
			}
		}
	});
}


//基础信息
var baseFields = [
	  			
	  		];
	  		
var opts={
		validate:true,
		labelFixWidth: 1,
		labelColon : false
	};
	
	//通过岗位 日期拿到运行记事
	function setNoteHistory( stationId, jobsId, shiftId, date ){
		$("#noteDiv").show();
		var url = basePath + "operation/note/queryNoteHistory.do?stationId=" + stationId  + "&jobsId=" 
				+ jobsId + "&shiftId=" + shiftId + "&date=" + date;
		
		//此处一定要给dataGrid赋值。dataGrid为全局变量，不设置会导致不能resize
		dataGrid = $("#contentTb").iDatagrid("init",{
	        pageSize:pageSize,//pageSize为全局变量，自动获取的
	        singleSelect :true,
	        fitColumns:true,
	        nowrap : false,
	        url: url,	//basePath为全局变量，自动获取的       
	        "columns":[[
	    				{field:'id',title:'id',width:180,fixed:true,hidden: true},
	    				{field:'writeTime',title:'记事时间',width:160,fixed:true,sortable:true,
	    					formatter:function(val){
	    						return FW.long2time(val);
	    					}
	    				},
	    				{field:'type',title:'记事类型',width:100,fixed:true,sortable:true,
	    					formatter:function(val){
	    						return FW.getEnumMap("OPR_NOTE_TYPE")[val];
	    					}	
	    				},
	    				{field:'crewNum',title:'机组号',width:100,fixed:true,sortable:true,
	    					formatter:function(val){
	    						return FW.getEnumMap("OPR_CREW_NUM")[val];
	    					}	
	    				},
	    				{field:'content',title:'记事内容',width:200,formatter:function(val,row,index){
							return val?val.replace(/\n|\r\n/g,"<br/>"):"";
						}},
	    				{field:'dutyName',title:'值别',width:70,fixed:true}
	    				]],
			"onLoadError": function(){
				//加载错误的提示 可以根据需要添加
				$("#noteDiv").hide();
			    $("#grid1_error").show();
			},
	        onLoadSuccess: function(data){
	        	$("#grid1_error").hide();
	            //远程无数据需要隐藏整个表格和对应的工具条，然后在无数据信息中指引用户新建
	            if(data && data.total==0){
	            	$("#noteDiv").hide();
	                $("#grid1_empty").show();
	            }else{
	            	$("#noteDiv").show();
	                $("#grid1_empty").hide();
	            }
	        }
	    });
	}
	
	//全局变量，根据岗位和工种请求回来的personJobs
	var personJobs = new Array();
	//全局变量，根据搜索条件请求回来的交接班记录
	var handovers = new Array();
	//正常班次的日历数据
	var normalCalendars = new Array();
	
	//通过dutyId获取同一值别下的人员信息，返回值为Array
	function getMembers(dutyId){
		var members= new Array();
		for ( var index in personJobs) {
			if(personJobs[index].dutyId == dutyId){
				members.push(personJobs[index]);
			}
		}
		return members;
	}
	
	//逻辑同getMembers，返回值是这些人员名字的拼接字符串，以逗号隔开：张三，李四
	function getMemberString(dutyId){
		var members = "";
		for ( var index in personJobs) {
			if(personJobs[index].dutyId == dutyId){
				members += personJobs[index].userName;
				members += ",";
			}
		}
		if(members == ""){
			members = "该值没有分配人员";
		}else{
			members = members.substring(0, members.length - 1);
		}
		return members;
	}
	
	//判断参数persons里面是否包含userId对应的员工
	function isInPersonJobs(userId,persons){
		for ( var index in persons) {
			if(userId == persons[index].userId){
				return true;
			}
		}
		return false;
	}
	
	//在全局变量handovers里查找对应nowScheduleId的交接班记录，参数是nowScheduleId
	function getHandoverByScheduleId(nowScheduleId){
		for(var index in handovers){
			if(handovers[index].nowScheduleId == nowScheduleId){
				return handovers[index];
			}
		}
		return null;
	}
	//在全局变量personJobs里面返回对应userId的人员名字
	function getUserNameByUserId(userId){
		for ( var index in personJobs) {
			if(personJobs[index].userId == userId){
				return personJobs[index].userName;
			}
		}
		return "无名氏";
	}
	
	//通过jobId拿到job名字，其中jobsList是全局变量
	function getJobsNameByJobsId(jobsId){
		for (var index in jobsList){
			if(jobsList[index].id == jobsId){
				return jobsList[index].name;
			}
		}
	}
	
	//将传入的calendar数组筛选出normal类型班次的日历
	function getNormalCalendars(calendars){
		var tempArr = new Array();
		for(var index in calendars){
			if(calendars[index].shiftType == "normal"){
				tempArr.push(calendars[index]);
			}
		}
		return tempArr;
	}
	
	//通过shiftId在全局变量normalCalendars里查找对应shiftId的日历，normalCalendars只含正常班次
	function getCalendarsByShiftId(shiftId){
		var tempArr = new Array();
		for(var index in normalCalendars){
			if(normalCalendars[index].shiftId == shiftId){
				tempArr.push(normalCalendars[index]);
				return tempArr;
			}
		}
		return tempArr;
	}
	function getBaseInfo(stationId,jobsId,shiftId,dateStr){
		$("#baseInfoDiv").show();
		var url = basePath + "operation/note/queryNoteBaseInfoBySearch.do?stationId=" + stationId + 
		"&jobsId="+ jobsId + "&shiftId=" + shiftId + "&date=" + dateStr;
		dataGrid2 = $("#contentTb2").iDatagrid("init",{
	        pageSize:pageSize,//pageSize为全局变量，自动获取的
	        url: url,
	        nowrap:false,
	        fitColumns:true,
	        singleSelect:true,
	        "columns":[[
					{field:'shiftId',title:'班次ID',width:60,fixed:true,hidden:true},
					{field:'shiftName',title:'班次',width:60,fixed:true},
					{field:'dutyId',title:'值别Id',width:60,fixed:true,hidden:true},
					{field:'dutyName',title:'值别',width:60,fixed:true},
					{field:'handoverId',title:'交接班Id',width:60,fixed:true,hidden:true},
					{field:'persons',title:'值班人员',width:200,formatter:function(value,row,index){
						return value?value:"没有值班人员";
					}},
					{field:'isOver',title:'交接状态',width:70,fixed:true},
					{field:'lastUserName',title:'交班人',width:70,fixed:true},
					{field:'nextUserName',title:'接班人',width:70,fixed:true},
					{field:'nextContent',title:'交班情况',width:350,fixed:true,hidden:true},
					{field:'nextRemark',title:'交班交待',width:350,hidden:true,hidden:true}
				]],
			"onLoadError": function(){
				//加载错误的提示 可以根据需要添加
				$("#baseInfoDiv").hide();
			    $("#grid2_error").show();
			},
	        onLoadSuccess: function(data){
	        	$("#grid2_error").hide();
	            //远程无数据需要隐藏整个表格和对应的工具条，然后在无数据信息中指引用户新建
	            if(data && data.total==0){
	            	$("#baseInfoDiv").hide();
	                $("#grid2_empty").show();
	            }else{
	            	$("#baseInfoDiv").show();
	                $("#grid2_empty").hide();
	            }
	        },
	        onDblClickRow : function( rowIndex, rowData ){
	        	var dataForm = $("#toolbarForm").iForm("getVal");
				var dateStr = dataForm.date;
				var stationId = dataForm.stationId;
				var jobsId = dataForm.jobsId;
				
		    	var detailUrl = "${basePath}/operation/note/queryNoteDetailPage.do?shiftId="
		    			+ rowData.shiftId + "&handoverId=" + rowData.handoverId + "&dutyId=" + rowData.dutyId
		    			+ "&dateStr=" + dateStr + "&stationId=" + stationId + "&jobsId=" + jobsId;
		    	addTabWithTree( "NoteDetail"+rowData.shiftId+jobsId, "运行记事", detailUrl,"noteHistory", "contentTb" );
		    }
	    });
	}
	
	function getBaseInfo2(stationId,jobsId,shiftId,dateStr){
		var url = basePath + "operation/note/queryNoteBaseInfoBySearch.do?stationId=" + stationId + 
				"&jobsId="+ jobsId + "&shiftId=" + shiftId + "&date=" + dateStr;
		$.ajax({
			url : url,
			type : 'post',
			dataType : "json",
			success : function(data) {
				//console.log(data);
				if(data.success == 1){
					var jobsName = getJobsNameByJobsId(jobsId);
					personJobs = data.personJobs;
					handovers = data.handovers;
					//将calendars里面的正常班次日历筛选出来，存入一个全局变量当中
					normalCalendars = getNormalCalendars(data.calendars);
					var shiftIdSelected = $("#toolbarForm").iForm("getVal","shiftId");
					if( shiftIdSelected != 0){
						normalCalendars = getCalendarsByShiftId( shiftIdSelected );
					}
					//表单项的fields
					baseFields = [];
					//变得数据对象，拼接字符串，然后转JSON
					var baseFormData = "{";
					//对请求回来的日历进行遍历，生成表单项和表单数据对象
					for(var index in normalCalendars){
							var tempShiftId = normalCalendars[index].shiftId;
							var tempDutyId =  normalCalendars[index].dutyId;
							var tempShiftName = normalCalendars[index].shiftName;
							var tempDutyName = normalCalendars[index].dutyName;
							var tempNowScheduleId = normalCalendars[index].id;
							var suffix = jobsId + "_" + tempDutyId + "_" + tempShiftId;
							//baseFields.push({title : "岗位",id : "jobsId_"+suffix,type : "label",linebreak:true});
							baseFields.push({title : "值班",id : "zb_"+suffix,type : "label",linebreak : true});
							baseFields.push({title : "人员",id : "members_"+suffix,type : "label"});
							baseFields.push({title : "交接班状态",id : "isHandover_"+suffix,type : "label"});
							baseFields.push({title : "交班人",id : "lastUserName_"+suffix,type : "label",linebreak : true});
							baseFields.push({title : "接班人",id : "nextUserName_"+suffix,type : "label"});
							baseFields.push({title : "交班情况",id : "content_"+suffix,type : "label",breakAll : true,
								linebreak:true,wrapXsWidth : 6,wrapMdWidth : 6});
							baseFields.push({title : "交班交待",id : "remark_"+suffix,type : "label",breakAll : true,
								wrapXsWidth : 6,wrapMdWidth : 6});
							
							baseFormData += '"jobsId_' + suffix + '" : "' + jobsName + '",';
							baseFormData += '"members_' + suffix + '" : "' + getMemberString(tempDutyId) + '",';
							baseFormData += '"zb_' + suffix + '" : "' + tempDutyName + " " + tempShiftName + '",';
							baseFormData += '"isHandover_' + suffix + '" : "未交接"' + ',';
							//baseFormData += '"content_' + suffix + '" : "' + '没有交接班记录' + '",';
							//baseFormData += '"remark_' + suffix + '" : "' + '没有交接班记录' + '",';
							var handover = getHandoverByScheduleId(tempNowScheduleId);
							if(handover != null && handover.isOver == "Y"){
								var lastUserName = getUserNameByUserId(handover.currentPerson);
								var nextUserName = getUserNameByUserId(handover.nextPerson);
								baseFormData += '"isHandover_' + suffix + '" : "已交接"' + ',';
								baseFormData += '"lastUserName_' + suffix + '" : "' + lastUserName + '",';
								baseFormData += '"nextUserName_' + suffix + '" : "' + nextUserName + '",';
								baseFormData += '"content_' + suffix + '" : "' + handover.nextContent + '",';
								baseFormData += '"remark_' + suffix + '" : "' + handover.nextRemark + '",';
							}
					}
					baseFormData = baseFormData.substr( 0, baseFormData.length - 1 );
					baseFormData += "}";
					baseFormData = JSON.parse( baseFormData );
					$("#baseInfoDiv").html('<form id="baseForm" class="autoform" ></form>');
					$("#baseForm").iForm("init",{"options":opts,"fields":baseFields});
					$("#baseForm").iForm("setVal",baseFormData);
					$("#baseForm").iForm("endEdit");
				}else{
					FW.error("查询不到该天的排班数据");
				}
			}
		});
	}

	function searchNote(val){
		//查询指定记事内容
		if(val===""){
			FW.error("请输入要查询的记事内容");
			return;
		}
		
		$.ajax({
			url : basePath + "operation/note/queryNoteListByContent.do",
			type : 'post',
			dataType : "json",
			data:{content:val},
			success : function(data) {
				if(data.total>0){//弹窗显示
					FW.set("OPR_NOTE_SEARCH_CONTENT",data);
					var title="包含“"+FW.specialchars(val)+"”的运行记事";
					FW.dialog("init",{
						src: basePath+"operation/note/queryNoteListByContentPage.do",
						btnOpts:[{
					            "name" : "关闭",
					            "float" : "right",
					            "style" : "btn-default",
					            "onclick" : function(){
					                _parent().$("#itcDlgsearchNote").dialog("close");
					             }
					        }
					    ],
						dlgOpts:{ idSuffix : "searchNote",width:800, height:500, closed:false, title:title, modal:true }
					});
				}else{
					FW.error("没有查询到相关记事内容");
				}
			}
		});
	}
	
	function searchShift(){//查询指定日期和岗位的班次和运行记事
		var dataForm = $("#toolbarForm").iForm("getVal");
		var dateStr = dataForm.date;
		var stationId = dataForm.stationId;
		var jobsId = dataForm.jobsId;
		var shiftId = dataForm.shiftId;
		if( dateStr == null || dateStr == "" ){
			FW.error( "请选择日期！");
			return ;
		}
		
		//查询基本信息、交接班情况
		getBaseInfo(stationId,jobsId,shiftId,dateStr);
		//查询运行记事历史
		setNoteHistory( stationId, jobsId, shiftId, dateStr );
	}
	
	var toolBarFields = [
		{title : " ", id : "date",type : "date",
			wrapXsWidth :4,
			wrapMdWidth :4
		},
		{title : " ", id : "stationId",
               type : "combobox",
               wrapXsWidth :4,
       		  wrapMdWidth :4,
       		labelFixWidth : "7px",
		},
		{title : " ", id : "jobsId",
               type : "combobox",
               wrapXsWidth :4,
       		  wrapMdWidth :4
		},
		{title : " ", id : "shiftId",
               type : "hidden"
		}	  			
	];
			
	$(document).ready(function() {
		if(window.parent.$("#itcui_nav_tab_noteHistory").length>0){
			FW.activeTabById("noteHistory");
			//parent.document.getElementById("navtab_opmm").contentWindow.history.back();
		}else{
			var pageUrl = basePath + "operation/menuController/queryRecordListMenu.do" ;
			addTabWithTree("noteHistory", "日志查询", pageUrl);
		}
		
		$("#toolbarForm").iForm("init",{"options":opts,"fields":toolBarFields});
		$("#search").iInput("init",{onSearch:searchNote});
		//岗位
		stationOption(basePath, 'f_stationId',function(stationList){
			//jobsOption( stationList[0][0] );
			//shiftOption( stationList[0][0] );
		},null,function(val){
			jobsOption( val );
			shiftOption( val );
		});
		
		//点击查询按钮
		$("#searchBtn").click(function(){
			searchShift();
		});
		
		$("#f_date").change(searchShift);
		
		//点击上一天
		$("#upBtn").click(function(){
			var dataForm = $("#toolbarForm").iForm("getVal");
			var dateStr = dataForm.date;
			var stationId = dataForm.stationId;
			var jobsId = dataForm.jobsId;
			var shiftId = dataForm.shiftId;
			
			if( dateStr == null || dateStr == "" ){
				FW.error( "请选择日期！");
				return ;
			}
			//转换成日期
			var dateArr = dateStr.split("-");
			var date = new Date( dateArr[0], (dateArr[1] - 1), dateArr[2] ).dateAdd('d',-1);
			
			$("#f_date").val(date.Format("yyyy-MM-dd"));
			//查询基本信息、交接班情况
			getBaseInfo(stationId,jobsId,shiftId,date.Format("yyyy-MM-dd"));
			//查询运行记事历史
			setNoteHistory( stationId, jobsId, shiftId, date.Format("yyyy-MM-dd") );
			
		});
		
		
		//点击下一天
		$("#downBtn").click(function(){
			var dataForm = $("#toolbarForm").iForm("getVal");
			var dateStr = dataForm.date;
			var stationId = dataForm.stationId;
			var jobsId = dataForm.jobsId;
			var shiftId = dataForm.shiftId;
			
			if( dateStr == null || dateStr == "" ){
				FW.error( "请选择日期！");
				return ;
			}
			//转换成日期
			var dateArr = dateStr.split("-");
			var date = new Date( dateArr[0], (dateArr[1] - 1), dateArr[2] ).dateAdd('d',1);
			
			$("#f_date").val(date.Format("yyyy-MM-dd"));
			//查询基本信息、交接班情况
			getBaseInfo(stationId,jobsId,shiftId,date.Format("yyyy-MM-dd"));
			//查询运行记事历史
			setNoteHistory( stationId, jobsId, shiftId, date.Format("yyyy-MM-dd") );
			
		});
		
		$("#printButton").click(function(){
			var dataForm = $("#toolbarForm").iForm("getVal");
			var dateStr = dataForm.date;
			var jobsId = dataForm.jobsId;
			var stationId = dataForm.stationId;
			var siteId = Priv.secUser.siteId;
			var userId = Priv.secUser.userId;
			var url= fileExportPath+"preview?__format=pdf&__report=report/TIMSS2_SWF_OPRHISLOG_001.rptdesign&jobsId="+jobsId
			+"&stationId="+stationId
			+"&date="+dateStr
			+"&siteId="+siteId
			+"&author="+userId
			+"&__isnull=url";
			FW.dialog("init",{
				src: url,
				btnOpts:[
						{
						    "name" : "关闭",
						    "float" : "right",
						    "style" : "btn-default",
						    "onclick" : function(){
						        _parent().$("#itcDlg").dialog("close");
						    }
						}
			        ],
				dlgOpts:{ width:800, height:650, closed:false, title:"日志查询打印预览", modal:true }
			});
		});
	});
	
</script>
</body>
</html>