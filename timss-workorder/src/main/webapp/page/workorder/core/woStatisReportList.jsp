<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>维护计划列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />

<script>
	/* 表单字段定义  */
	var fields = [
				    {title : "全场风机数(台)", id : "all_workteam_sum"},
				    {title : "全场停机(小时)", id : "all_workteam_hour"},
				];
	var myDate = new Date();
	var year = myDate.getFullYear();   //当前年份
	var month = myDate.getMonth()+1;  //当前月份()
	var workTeamCode = "all_workteam";
	var firstYear ;
	var workTeamBaseDate ; //维护组基本数据信息（负责风机台数）
	var rateAndStopTime;  //维护组的风机故障率和停机时间
	var dataGrid;
	$(document).ready(function(){
		initPage();
		
		dataGrid = $("#woStatistic_table").iDatagrid("init",{
	        pageSize:pageSize,//pageSize为全局变量
	        url: basePath + "/workorder/woStatis/WOStatisListdata.do",     
	        queryParams:{
	        	workteam:workTeamCode,
	        	year:year,
	        	month:month
	        },
	        singleSelect:true,
	        columns:[[ 
					{field:"equipNameCode",title:"风机编号",width:110,fixed:true}, 
					{field:"description",title:"故障",width:180}, 
					{field:"discoverTime",title:"故障发生时间",width:130,fixed:true,align:'center',
						formatter: function(value,row,index){
							//时间转date的string，还有long2date(value)方法
							return FW.long2time(value);
						}
					}, 
					{field:"faultConfrimUser",title:"运行登记人",width:80,fixed:true}, 
					{field:"approveStopTime",title:"批准停机<br/>(小时)",width:70,fixed:true,align:'center'}, 
					{field:"endTime",title:"风机恢复时间",width:130,fixed:true,align:'center',
						formatter: function(value,row,index){
							return FW.long2time(value);
						}
					},   
					{field:"faultStopTime",title:"故障停机<br/>(小时)",width:70,fixed:true,align:'center'}, 
					{field:"acceptanceUser",title:"运行登记人",width:80,fixed:true},   
					{field:"endReportUser",title:"维护确认人",width:80,fixed:true},   
					{field:"remarks",title:"备注",width:60}  
				]],
	        onLoadSuccess: function(data){
	        	rateAndStopTime = data.rateAndStopTime;
	        	
	        	var wtVal = $("#workTeam_combo").iCombo("getVal");
	        	var data ={};
	        	var wholeStopTime = 0;
	        	for(var s in rateAndStopTime){
	        		var rate = rateAndStopTime[s][0];
	        		if(rate!= 0){
	        			rate = rate.toFixed(2);  //保留两位小数
	        		}
	        		data[s] = rate; //给form中某个班组赋值故障率
	        		
	        		if(s==wtVal){
	        			wholeStopTime = rateAndStopTime[s][1]; 
	        			if(wholeStopTime!= 0){
		        			wholeStopTime = wholeStopTime.toFixed(2);  //保留两位小数
		        		}
	        		}
	        		
	        	} 
	        	
	        	var selectWTStopTimeId = wtVal+"_hour";
	        	data[selectWTStopTimeId] = wholeStopTime; //给form中停机时间赋值
	        	
	        	$("#statisForm").iForm("setVal",data); //form表单赋值
	        	
	            //远程无数据需要隐藏整个表格和对应的工具条，然后在无数据信息中指引用户新建
	            if(data && data.total==0){
	                $("#grid1_wrap").hide();
	                $("#grid1_empty").show();
	            }else{
	            	$("#grid1_wrap").show();
	                $("#grid1_empty").hide();
	            }
	        }
	    });
	});
	function initPage(){
		$.post(basePath + "workorder/woStatis/initPageBaseData.do",{},
			function(data){
				if(data.result == "success"){
					firstYear = data.firstYear;
					workTeamBaseDate = data.workTeamBaseDate;
					workteamWinSumMap= workTeamBaseDate.workTeamWindCount;
				 	//console.log(workTeamBaseDate);
					var yearComboData =[];
					var monthComboData =[];
					var workTeamComboData = [];
					
					var combo_year = year;  //初始化时，默认值
					var combo_month = month;
					if(month==1){  //如果上个月是去年的12月份
						combo_year = year-1;
						combo_month = 12;
					}else{ //如果上个月还在本年度之内
						combo_year = year;
						combo_month = month-1;
					}
					
					for(var i = 0 ; i <12 ;i++){
						if(combo_month-1==i){
							monthComboData.push([i+1,i+1,true]);
						}else{
							monthComboData.push([i+1,i+1]);
						}
					}
					
					for(var i = year; i >=firstYear ;i--){	
						if(combo_year==i){
							yearComboData.push([i,i,true]);
						}else{
							yearComboData.push([i,i]);
						}			
					}  
					
				 	
					$("#year_combo").iCombo("init",{
						data:yearComboData,
						initOnChange:false,
						onChange:function(val){
							year = val;
							reloadData();
    						//console.log("你选择了年：" + val);
    					}
					});
					$("#month_combo").iCombo("init",{
						data:monthComboData,
						initOnChange:false,
						onChange:function(val){
							month =  Number(val) +1;
							reloadData();
    						//console.log("你选择了月：" + val);
    					}
					});
					
					$("#workTeam_combo").iCombo("init",{
						data:workTeamBaseDate.workTeamName,
						initOnChange:false,
						onChange:function(val){
							var workteamName = ""
							if(val!="all_workteam"){
								workteamName = workteamNameMap[val].substring(2,5);
							}else{
								workteamName = workteamNameMap[val];
							}
							if(val != "all_workteam"){
								fields[0].title = workteamNameMap[val].substring(2,5)+"风机数(台)";
								fields[1].title = workteamNameMap[val].substring(2,5)+"停机(小时)";
							}else{
								fields[0].title = workteamNameMap[val]+"风机数(台)";
								fields[1].title = workteamNameMap[val]+"停机(小时)";
							}
					 		fields[0].id = val+"_sum";
					 		fields[1].id = val+"_hour";
					 		$("#statisForm").iForm("init",{"fields":fields});
					 		
					 		var selectWTid = val+"_sum";
					 		var selectWTwindnum = workteamWinSumMap[val];
							var o = {};
							o[selectWTid] = selectWTwindnum;
					 		$("#statisForm").iForm("setVal",o);
					 		workTeamCode = val;
					 		reloadData();
    						//console.log("你选择了班组：" + val);
    					}
					});
					
					var workteamCodeList = workTeamBaseDate.workteamCodeList;
					var workteamNameMap = workTeamBaseDate.workTeamName;
					var i=2;
					workteamCodeList.forEach(function(e){
						fields.push({});
						if(i==2){
							fields[i].title = workteamNameMap[e]+"故障率(%)";
						}else{
							fields[i].title = workteamNameMap[e].substring(2,5)+"故障率(%)";
						}
						fields[i].id = e;
						i++;
					});
					$("#statisForm").iForm("init",{"fields":fields});
					var data ={};
					var str ="all_workteam_sum";
					data[str] = workteamWinSumMap["all_workteam"];
		 			$("#statisForm").iForm("setVal",data);
		 			$("#statisForm").iForm("endEdit");
				}else {
					FW.error("打开失败");
				}
	  	},"json");
	}
	function  reloadData(){
		dataGrid.datagrid("load",{
			workteam: workTeamCode,
			year: year,
			month: month
		});
		$("#statisForm").iForm("endEdit");
	}
	function printWOList(){
	
		var selectYear = $("#year_combo").iCombo("getVal");
		var selectMonth = $("#month_combo").iCombo("getVal");
		var selectWorkTeam = $("#workTeam_combo").iCombo("getVal");
		$.post(basePath + "workorder/woStatis/printWOStatistic.do",
			{"year":selectYear,"month":selectMonth,"workteam":selectWorkTeam},
			function(data){
				if(data.result=="success"){
					printNum = data.printNum;
					//console.log("打印的次数为："+printNum);
					var workteamCnName = "全场"
					if(selectWorkTeam!="all_workteam"){
						workteamCnName = FW.getEnumMap("WO_WORKTEAM")[selectWorkTeam];
					}
					//console.log("统计班组为："+workteamCnName);
					var workteamRate = 0;
					var workteamStopTime = 0;
					var someWTWindSum = 0;
					for(var s in rateAndStopTime){
		        		if(s==selectWorkTeam){
		        			workteamRate = rateAndStopTime[s][0];
		        			workteamStopTime = rateAndStopTime[s][1]; 
		        		}
		        	} 
		        	if(workteamRate!= 0){
		       			workteamRate = workteamRate.toFixed(2);  //保留两位小数
		       		}
		       		if(workteamStopTime!= 0){
		       			workteamStopTime = workteamStopTime.toFixed(2);  //保留两位小数
		       		}
	        		workteamWinSumMap= workTeamBaseDate.workTeamWindCount;
	        		someWTWindSum = workteamWinSumMap[selectWorkTeam];
					var src = fileExportPath + "preview?__report=report/TIMSS2_WO_FJTJMX.rptdesign&__format=pdf"+
							"&printNum="+printNum+"&workteamCnName="+workteamCnName+"&someWTWindSum="+someWTWindSum+
							"&workteamRate="+workteamRate+"&workteamStopTime="+workteamStopTime+"&year="+year+"&month="+month;
					var url = encodeURI(encodeURI(src));
					var title ="设备故障统计报表"
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
					/* $("#but_wo_printWOList").bindDownload({
						url : src
					}); */
				}else{
					FW.error("打印失败");
				}
			},"json");
		
	}
	function printAllRate(){
		var selectYear = $("#year_combo").iCombo("getVal");
		var selectMonth = $("#month_combo").iCombo("getVal");
		var selectWorkTeam = $("#workTeam_combo").iCombo("getVal");
		var temp_attr ="";  //用于传递表中的数据进去
		var allRate =0; //全场故障率
		var workteamCnName = "全场"
		for(var s in rateAndStopTime){
			if(s!="all_workteam"){
				workteamCnName = FW.getEnumMap("WO_WORKTEAM")[s];
				var rate = rateAndStopTime[s][0];
				//console.log(rate);
	       		if(rate!= 0){
	       			rate = rate.toFixed(2);  //保留两位小数
	       		}
	       		temp_attr += workteamCnName+"="+rate+"@"
			}else{
				allRate = rateAndStopTime[s][0];
			}
       	} 
       	if(allRate!= 0){
   			allRate = allRate.toFixed(2);  //保留两位小数
   		}
       	temp_attr = temp_attr + "全场="+allRate;
       	
		/* temp_attr
		* 数据样式    :  维护甲组=1.92@维护乙组=0@维护丙组=1.82@全场=1.24
		 */

		var src = fileExportPath + "preview?__report=report/TIMSS2_WO_FJZB.rptdesign&__format=pdf"+
					"&year="+selectYear+"&month="+selectMonth+"&content="+temp_attr;
		var url = encodeURI(encodeURI(src));
		var title ="故障率统计报表"
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
		/* $("#but_wo_printAllRate").bindDownload({
			url : src
		}); */ 
	}

	
</script>
</head>
<body>
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar1" class="btn-toolbar ">
		    <div style="width:320px;float:left;padding-top:3px;">
		    	<select id="year_combo" style="width:80px;float:left">
				</select>
				<span style="margin-left:5px;margin-right:10px;float:left">年</span>
				
				<select id="month_combo" style="width:80px;float:left"  maxlength=18>
				</select>
				<span style="float:left;margin-left:5px;margin-right:10px;font-size:14px;">月</span>
	
				<select id="workTeam_combo" style="width:80px;float:left"  maxlength=18>
				</select>
			</div>
			
	    	<div class="btn-group btn-group-sm">
				<button type="but_wo_printWOList" class="btn btn-default" onclick="printWOList()">打印</button>
			</div>
	        <div class="btn-group btn-group-sm">
	        	<button type="but_wo_printAllRate" class="btn btn-default" onclick="printAllRate()" >打印总表</button>
	        </div>
	    </div>
	    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
	    <div id="pagination_1" class="toolbar-pager">        
	    </div>
	</div>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	 <div>
    	 <form id="statisForm" class="autoform"></form>
    </div>
	<div id="grid1_wrap" style="width:100%">
	    <table id="woStatistic_table" pager="#pagination_1" class="eu-datagrid">
	    </table>
	</div>
	
	<!-- 无数据 -->
	<div class="row" id="grid1_empty" style="display:none">
	    	没有相关工单的数据 
	</div>
</body>
</html>