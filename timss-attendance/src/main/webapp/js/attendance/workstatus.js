var isOpr=false;//是否查看运行人员
var checkInvalid="all";//只查看打卡统计异常，all所有异常，no全部数据
var checkEditor;//考勤结果列的editor
var checkExclude="no";//不检索的列，默认检索全部，可取值mornStart/mornEnd/

//报表
function mothReport(atdType){
	var src = basePath + "attendance/cardData/report.do";
			
	var btnOpts = [{
		"name" : "取消",
		"float" : "right",
		"style" : "btn-default",
		"onclick" : function() {
			return true;
		}
	},{
		"name" : "确定",
		"float" : "right",
		"style" : "btn-success",
		"onclick" : function() {	
			var privStr="";
			privStr+="&siteId="+Priv.secUser.siteId;
			if(Priv.hasPrivilege("atd_stat_site")){
				
			}else if(Priv.hasPrivilege("atd_stat_dept")&&Priv.secUser.orgs.length>0){
				privStr+="&deptId="+Priv.secUser.orgs[0].code;
			}else{
				privStr+="&userId="+Priv.secUser.userId;
			}
						
			var conWin = _parent().window.document.getElementById("itcDlgContent").contentWindow;
			if (!conWin.valid()) {
				return false;
			}
			var formdata = conWin.$("#reportFrom").iForm("getVal");
			_parent().$("#itcDlg").dialog("close");
			
			var siteId = Priv.secUser.siteId;
			if('ZJW'==siteId){//湛江风电的报表采用生物质的模板
				siteId = 'SWF';
			}
			var yearVal =  formdata['year'];
			var monthVal =  formdata['month'];
			if( monthVal < 10 ){
				monthVal = "0" + monthVal;
			}
			var date = yearVal + "-" + monthVal;
			var deptName=formdata['deptName'];
			var dutyName=formdata['dutyName'];
			var url = "";
			var title = "";
			if(atdType=="KQMX"){
				title = "打印考勤明细";
				url = fileExportPath + "preview?__report=report/TIMSS2_"+siteId+"_"+(isOpr?"YX":"XZ")+atdType+"_001.rptdesign&__format=pdf"  
				+privStr+"&year=" + yearVal+"&month=" + monthVal+"&"+$.param({deptName:deptName})+"&"+$.param({dutyName:dutyName});
			}else if(atdType=="QJMX"){
				title = "打印请假明细";
				url = fileExportPath + "preview?__report=report/TIMSS2_"+siteId+"_LEAVELIST_001.rptdesign&__format=pdf" 
				+privStr+"&year=" + yearVal+"&month=" + monthVal+"&"+$.param({deptName:deptName})+"&"+$.param({dutyName:dutyName});
			}else if(atdType=="JBMX"){
				title = "打印加班明细";
				url = fileExportPath + "preview?__report=report/TIMSS2_"+siteId+"_OTLIST_001.rptdesign&__format=pdf" 
				+privStr+"&year=" + yearVal+"&month=" + monthVal+"&"+$.param({deptName:deptName})+"&"+$.param({dutyName:dutyName});
			}else{
				title = "打印考勤表";
				url = fileExportPath + "preview?__report=report/TIMSS2_"+siteId+"_KQTJ_001.rptdesign&__format=pdf" 
				+privStr+"&year=" + yearVal+"&month=" + monthVal+"&"+$.param({deptName:deptName})+"&"+$.param({dutyName:dutyName});
			}
			FW.dialog("init",{
				"src": url,
				"btnOpts":[{
					"name" : "关闭",
					"float" : "right",
					"style" : "btn-default",
					"onclick" : function(){
						 _parent().$("#itcDlgPrint").dialog("close");
				 	}
				}],
				"dlgOpts":{ width:1020, height:"75%", closed:false, title:title, modal:true, idSuffix:"Print"  }
			 });
		}
	} ];

	FW.dialog("init", {
		"src" : src,
		"dlgOpts" : {
			width : 350,
			height : 250,
			closed : false,
			title : "生成报表",
			modal : true
		},
		"btnOpts" : btnOpts
	});
}

function checkFormatter(val,row,index){//考勤结果的列的格式化
	var type=row[this.field+"Type"];
	var map=FW.getEnumMap("ATD_WORK_STATUS_TYPE");
	var str = map[type];
	if(type=='leave'){
		map=FW.getEnumMap("ATD_LEI_CATEGORY");
		str=map[val];
	}else if(type=='abnormity'){
		map=FW.getEnumMap("ATD_AB_CATEGORY");
		str=map[val];
	}
	
	/* if( row.status == '0' ){
		str = "休息";
	}else if( row.status != '0' && !val ){
		str = "未打卡";
	} */
	return str;
}

function checkTimeFormatter(val,row,index){//考勤时间的列的格式化
	return val;
	//return val?val.substr(11,val.length):"";
}	

function shiftNameFormatter(val,row,index){//班次名称格式化
	var str="";
	if(row.startTime&&row.longTime){
 		var sth=row.startTime.substr(0,2);
		var stm=row.startTime.substr(2,4);
		var eth=(parseInt(sth)+row.longTime)%24;
		str+="("+sth+":"+stm+"-"+(eth<10?"0":"")+eth+":"+stm+")";
 	}
 	return (val?val:"")+str;
}

function init(){
	$("#personTypeSelect").iCombo("init", {
		data : [
			["clerk","行政人员",true],
			["opr","运行人员"]
		],
		initOnChange:false,
		onChange:function(val){
			if("opr"==val){
				isOpr=true;
			}else{
				isOpr=false;
			}
			if($("#btn_advlocal").hasClass("active")){
				$("#btn_advlocal").click();
			}
			initDatagrid();
		}		
	});
	
	$("#checkInvalidSelect").iCombo("init", {
		data : [
			["all","所有异常",true],
			["late","迟到"],
			["early","早退"],
			["invalid","未打卡"],
			["abnormity","考勤异常"],
			["leave","请假"],
			["no","全部"]
		],
		initOnChange:false,
		onChange:function(val){
			checkInvalid=val;
			if($("#btn_advlocal").hasClass("active")){
				$("#btn_advlocal").click();
			}
			initDatagrid();
		}		
	});
	
	var checkList=new Array();
	var enumList=FW.parseEnumData("ATD_WORK_STATUS_TYPE",_enum);
	for(var i=0;i<enumList.length;i++){
		var elt=enumList[i];
		if($.inArray(elt[0],["overtime","leave","abnormity"])<0){
			checkList.push(elt);
		}
	}
	checkList=checkList.concat(FW.parseEnumData("ATD_LEI_CATEGORY",_enum),FW.parseEnumData("ATD_AB_CATEGORY",_enum));
	checkEditor={
		"type" : "combobox",
		"options" : {
			data:checkList
		}
	};
	
	initDatagrid();
	//湛江风电站点没有加班申请
	if("ZJW"==Priv.secUser.siteId){
		$("#btn_print3").hide();
	}
	
}

function getDatagridColumns(){//页面自定义列
	var preTitle="";
	if(!isOpr){
		preTitle="上午";
	}
	
	var checkWidth=88;
	
	return [[
    			{field:'id',title:'id',width:20,fixed:true,hidden:true},
    			{field:'userId',title:'工号',width:70,fixed:true, sortable: true},
    			{field:'userName',title:'姓名',width:70,fixed:true, sortable: true},
    			{field:'deptName',title:'部门',width:90, fixed:true,sortable: true},
    			{field:'workDate',title:'日期',width:100,fixed:true, sortable: true},
    			
    			{field:'mornStartCheck',title:preTitle+'上班',width:checkWidth,fixed:true, sortable: true,'editor':checkEditor,
    				formatter:checkFormatter
    			},
    			{field:'mornStartTime',title:preTitle+'上班打卡时间',width:170,fixed:true, sortable: true,
    				formatter:checkTimeFormatter},
    			{field:'mornStartCheckType',title:preTitle+'上班',hidden:true},
    				
    			{field:'mornEndCheck',title:preTitle+'下班',width:checkWidth,fixed:true, sortable: true,'editor':checkEditor,
    				formatter:checkFormatter
    			},
    			{field:'mornEndTime',title:preTitle+'下班打卡时间',width:170,fixed:true, sortable: true,
    				formatter:checkTimeFormatter},
    			{field:'mornEndCheckType',title:preTitle+'下班',hidden:true},
    				
    			{field:'noonStartCheck',title:'下午上班',width:checkWidth,fixed:true, sortable: true,hidden:isOpr,'editor':checkEditor,
    				formatter:checkFormatter	
    			},
    			{field:'noonStartTime',title:'下午上班打卡时间',width:170,fixed:true, sortable: true,hidden:isOpr,
    				formatter:checkTimeFormatter},
    			{field:'noonStartCheckType',title:'下午上班',hidden:true},
    				
    			{field:'noonEndCheck',title:'下午下班',width:checkWidth,fixed:true, sortable: true,hidden:isOpr,'editor':checkEditor,
    				formatter:checkFormatter	
    			},
    			{field:'noonEndTime',title:'下午下班打卡时间',width:170,fixed:true, sortable: true,hidden:isOpr,
    				formatter:checkTimeFormatter},
    			{field:'noonEndCheckType',title:'下午下班',hidden:true},	
    			{field:'dutyName',title:'值别',width:70,fixed:true, sortable: true,hidden:!isOpr},
    			{field:'shiftName',title:'班次',width:170,fixed:true, sortable: true,hidden:!isOpr,
    				formatter:shiftNameFormatter
    			},	
    			{field:'blank',title:'',width:130}
    		]];	
}

function initDatagrid(){
	//此处一定要给dataGrid赋值。dataGrid为全局变量，不设置会导致不能resize
	dataGrid = $("#contentTb").iDatagrid("init",{
        pageSize:pageSize,//pageSize为全局变量，自动获取的
        singleSelect :true,
        url: basePath + "attendance/cardData/queryWorkStatusList.do",	//basePath为全局变量，自动获取的       
        queryParams:{"isOpr":function(){return isOpr},"checkInvalid":function(){return checkInvalid},"checkExclude":function(){return checkExclude}},
        "columns":getDatagridColumns(),
		"onLoadError": function(){
			//加载错误的提示 可以根据需要添加
			$("#grid1_wrap,#toolbar_wrap,#bottomPager").hide();
		    $("#grid1_error").show();
		},
        onLoadSuccess: function(data){
            //远程无数据需要隐藏整个表格和对应的工具条，然后在无数据信息中指引用户新建
            if(data && data.total==0){
            	$("#noResult").show();
                /* if( modelFlag ){
                	$("#noResult").show();
                }else{
                	$("#grid1_wrap,#toolbar_wrap").hide();
	                $("#grid1_empty").show();
	                $("#noResult").hide();
                } */
            }else{
            	//$("#grid1_wrap,#toolbar_wrap").show();
                //$("#grid1_empty").hide();
                $("#noResult").hide();
            }
        },
		onDblClickCell: function(index,field,value){
			var row=$(this).datagrid('getSelected');
			if(row[field+"Src"]&&console){
				console.log(row.userId+" "+row.workDate+" "+$(this).datagrid('getColumnOption',field)["title"]+" "+
							row[field+"Type"]+" "+row[field+"Src"]);
			}
		}
    });
}