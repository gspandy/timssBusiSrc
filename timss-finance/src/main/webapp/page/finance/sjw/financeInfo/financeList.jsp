<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html style="height: 99%;">
<head>
	<title>财务报销列表</title>
	<script type="text/javascript">
		_useLoadingMask = true;
	</script>
	<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
	<script type="text/javascript" src="${basePath}/js/finance/common/eventTab.js?ver=${iVersion}"></script>
	<style>
		.dropdown-menu{
			text-align:left;
		}
	</style>
	<script type="text/javascript">
		var selectMode="d"; 
		var searchType=1;
		var randomNbr="";
		var clkSchBtnFlag=0; //已点击"查询"按钮标志.0-未点击过查询 ,1-已点击过查询

		function createdg() {
			
			$("#wiGrid").iDatagrid( "init", {
				url : "${basePath}"+"finance/financeInfoController/financeListData.do",
			   
			    singleSelect:true,
				  pagination:true,
			
				  resizeHandle:'both',
				  fitColumns:true,
				  collapsible:true,
				  scrollbarSize:0,
				  //pageSize: 10,
				  pageSize: pageSize,
				  //pageList :[10,15,20,30,100],
				  onRenderFinish : function(){
					  $("#wiGrid").ITCUI_Pagination("create","#pagination_1",styleOpt);  
				  },
			     onDblClickRow : function(rowIndex, rowData){
			    	 //同一张报销单不能同时打开两个tab编辑
				     var newId="edit"+rowData.fid;
				  
				     var pageName=rowData.finance_flow;
				     var pageUrl="${basePath}"+"finance/financeInfoController/viewFinanceInfo.do"+"?businessId="+rowData.fid;
				     //var pageUrl="${basePath}"+"finance/financeInfoController/viewFinanceInfo.do"+"?fid="+rowData.fid;
				     var oldId = FW.getCurrentTabId();
				     //var oldId = "itcui_nav_tab_homepage";
				     addEventTab(newId, pageName, pageUrl,oldId);
				  }, 
				   "onLoadError": function(){
		          //加载错误的提示 可以根据需要添加
		          $("#grid1_wrap,#toolbar_wrap").hide();
		          $("#grid1_error").show();
		      }, "onLoadSuccess" : function(data){
		          //远程无数据需要隐藏整个表格和对应的工具条，然后在无数据信息中指引用户新建
		          if(data && data.total==0){
		              //$("#grid1_wrap,#toolbar_wrap").hide();
		              //alert(clkSchBtnFlag);
		              if( clkSchBtnFlag == 1 ) {
		            	  $("#grid1_queryempty").show();
		              } else if( clkSchBtnFlag == 0 ) {
		            	  $("#grid1_empty").show();
		            	  $("#grid1_show").hide();
		              }
		          } else {
		        	  $("#grid1_show").show();
		          	  $("#grid1_empty").hide();
		          	  $("#grid1_queryempty").hide();
		          }
		      },
			    columns:[[
			        {field:'fid',title:'编号',width:160,fixed:true},
			        {field:'fname',title:'名称',width:40},
			        {field:'creatorname',title:'申请人',width:80,fixed:true},
			      //  {field:'department',title:'申请部门',width:65,fixed:true},
			        {
						field:'createdate',
						title:'申请日期',
						width:90,
						fixed:true,
						formatter : function(value, row, index) {
							return FW.long2date(value);
						}
					},
			        {field:'finance_flow',title:'报销流程',width:160,fixed:true},
			        {field:'finance_type',title:'报销类型',width:80,fixed:true},
			        {field:'status',title:'状态',width:120,fixed:true}
			    ]]
			});
		}
		
		function changeTime(val,row){
			var newDate=new Date(val);
			var cd=newDate.getFullYear()+"-"+(newDate.getMonth()+1)+"-"+newDate.getDate();
				return '<span style="">'+cd+'</span>';
		}
		
		function formatStyle(val,row){
		}
		
		$(document).ready(function(){
			createdg();
		});
	
		$(window).resize(function(){
			$("#wiGrid").datagrid('resize');
		});	
	
		function search() {
			clkSchBtnFlag = 1; //已点击过"查询"按钮
			if (searchType == 1) {
				$("#wiGrid").ITCUI_GridSearch("init", {
					"remoteSearch" : true,
					"onParseArgs" : function(arg) {
						return {
							"search" : JSON.stringify(arg)
						};
					}
				});
				searchType = 0;
			} else {
				$("#wiGrid").ITCUI_GridSearch("end");
				searchType = 1;
			}
		}

		/* 新建报销页面 .报销名称,报销类型 ,如"travelcost,only" */
		function creFinOprPage(finNameEn, finTypeEn) {
			var oldId = FW.getCurrentTabId(); //获取当前所在选项卡编号
			var nbr = Math.floor(Math.random() * ( 10 + 1)); //随机数
			var newId = finNameEn + finTypeEn + nbr; //新建选项卡编号
			var finNameCn=""; //报销名称(中文)
			var finTypeCn=""; //报销类型(中文)
			
			if( finNameEn == "pettycash" ) {
				finNameCn = "备用金";
			} else if( finNameEn == "traincost" ) {
				finNameCn = "培训费";
			} else if( finNameEn == "hometripcost" ) {
				finNameCn = "探亲路费";
			} else if( finNameEn == "medicalinsurance" ) {
				finNameCn = "补充医疗保险";
			} else if( finNameEn == "familymedicinecost" ) {
				finNameCn = "家属医药费";
			} else if( finNameEn == "carcost" ) {
				finNameCn = "汽车费";
			} else if( finNameEn == "officecost" ) {
				finNameCn = "办公费";
			} 

			if( finTypeEn == "only" ) {
				finTypeCn = "自己";
			} else if( finTypeEn == "other" ) {
				finTypeCn = "他人";
			} else if( finTypeEn == "more" ) {
				finTypeCn = "多人";
			}
			
			var pageName = finNameCn + "报销";
			var pageUrl = "${basePath}"+"finance/financeInfoController/createPage.do?"
				+"page=financeNew&finNameEn="+finNameEn
				+"&finTypeEn="+finTypeEn;

			addEventTab( newId, pageName, pageUrl, oldId );
		}
		
		//刷新主页面
		function refreshFinancePage() {
			
			$("#wiGrid").datagrid("reload");
			
		}
	</script>
</head>
<body style="height: 100%;" class="bbox list-page">
	<div id="grid1_show">
		<!-- 工具栏层 -->
		<div class="toolbar-with-pager bbox" >
		    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
		    <div id="toolbar1" class="btn-toolbar" role="toolbar">
		        <div class="btn-group btn-group-sm">
		        	 <button type="button" class="btn btn-success dropdown-toggle" data-toggle="dropdown">
		        	 	新建
		        	 	<span class="caret"></span> <!-- 添加"新建"文字右边的"下箭头" -->
		        	 </button>
		        	 <ul class="dropdown-menu">
		        	 	<li><a onclick="creFinOprPage('businessentertainment','only')">业务招待费报销</a></li>
			            <li><a onclick="creFinOprPage('travelcost','only')">差旅费报销</a></li>
			            <li><a onclick="creFinOprPage('carcost','only')">汽车费报销</a></li>
			            <li><a onclick="creFinOprPage('officecost','only')">办公费报销</a></li>
			            <li><a onclick="creFinOprPage('welfarism','only')">福利费报销</a></li>
			            <li><a onclick="creFinOprPage('meetingcost','only')">会议费报销</a></li>
			            <li><a onclick="creFinOprPage('traincost','only')">培训费报销</a></li>
			            <li><a onclick="creFinOprPage('pettycash','only')">备用金报销</a></li>
				       
			        </ul>
		        </div>
		        <div class="btn-group btn-group-sm">
		            <button type="button" class="btn btn-default" onclick="search();">查询</button>
		            <button type="button" class="btn btn-default" id="flowinfo" onclick="refreshFinancePage();">刷新</button>
		        </div>
		    </div>
		    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
		    <div id="pagination_1" class="toolbar-pager" bottompager="#bottomPager">        
		    </div>
		</div>
		
		<!--这里要清掉分页器的右浮动效果-->
		<div style="clear:both"></div>
		
		<!-- 表格层 -->
		<div id="grid1_wrap" style="width:100%">
		    <table id="wiGrid" pager="#pagination_1" class="eu-datagrid">
		    </table>
		</div>
		
		<!-- 下分页器部分-->
		<div id="bottomPager" style="width:100%">
		</div>
	
		<!-- 错误信息-->
		<div class="row" id="grid1_error" style="display:none">
			无法从服务器获取数据，请检查网络是否正常
		</div>
		
		<!-- 点击查询按钮,结果为无数据 -->
		<div id="grid1_queryempty" style="width: 100%; height: 62%; margin: 10px;">
			<div style="height:100%;display:table;width:100%">
				<div style="display:table-cell;vertical-align:middle;text-align:center">
				    <div style="font-size:14px">没有找到符合条件的结果</div>
				    
				</div>
			</div>
		</div>
	</div>
	
	<!-- 无数据 -->
	<!-- 
	<div class="row" id="grid1_empty" style="display:none">
		没有xxxxxxx数据，单击这里创建新的....
	</div>
	-->
	
	<!-- 点击财务报销主查询界面,结果为无数据 -->
	<!-- 
	<div id="grid1_empty" style="display:none;width:100%; margin-top: 20%">
	 -->
	 <div id="grid1_empty" style="display:none;width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">没有报销信息</div>
			    <div class="btn-group btn-group-sm margin-element">
		        	 <button type="button" class="btn btn-success dropdown-toggle" data-toggle="dropdown">
		        	 	新建
		        	 	<span class="caret"></span> <!-- 添加"新建"文字右边的"下箭头" -->
		        	 </button>
		        	 <ul class="dropdown-menu">
			            <li><a onclick="creFinOprPage('businessentertainment','only')">业务招待费报销</a></li>
			            <li><a onclick="creFinOprPage('travelcost','only')">差旅费报销</a></li>
			            <li><a onclick="creFinOprPage('carcost','only')">汽车费报销</a></li>
			            <li><a onclick="creFinOprPage('officecost','only')">办公费报销</a></li>
			            <li><a onclick="creFinOprPage('welfarism','only')">福利费报销</a></li>
			            <li><a onclick="creFinOprPage('meetingcost','only')">会议费报销</a></li>
			            <li><a onclick="creFinOprPage('traincost','only')">培训费报销</a></li>
			            <li><a onclick="creFinOprPage('pettycash','only')">备用金报销</a></li>
				        <li><a onclick="creFinOprPage('hometripcost','only')">探亲路费报销</a></li>
				        <li><a onclick="creFinOprPage('medicalinsurance','only')">补充医疗保险报销</a></li>
				        <li><a onclick="creFinOprPage('familymedicinecost','only')">家属医药费报销</a></li>
			        </ul>
		        </div>
			</div>
		</div>
	</div>
</body>
</html>