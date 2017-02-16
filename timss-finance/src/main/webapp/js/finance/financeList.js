/**
*动态添加新建按钮下的报销类型
*/
function appendBtn(){
	var flowTypeEnums =  FW.getEnumMap("FIN_FLOW_TYPE")
	 for(var key in flowTypeEnums){  
		var appendHtml = '<li><a onclick="creFinOprPage('+"'"+key+"'"+')">'+flowTypeEnums[key]+"</a></li>";
         $("#newButton1").append(appendHtml);
		 $("#newButton2").append(appendHtml);
	}  
}
/**
*  列表的初始化
*/
function createdg() {
	$("#wiGrid").iDatagrid( "init", {
		  url : basePath +"finance/financeInfoController/financeListData.do",
	   	  singleSelect:true,
		  pagination:true,
		  resizeHandle:'both',
		  fitColumns:true,
		  collapsible:true,
		  scrollbarSize:0,
		  pageSize: pageSize,
		  onRenderFinish : function(){
			  $("#wiGrid").ITCUI_Pagination("create","#pagination_1",styleOpt);  
		  },
	     onDblClickRow : function(rowIndex, rowData){
	    	 //同一张报销单不能同时打开两个tab编辑
		     var newId="edit"+rowData.fid;
		  
		     var pageName=rowData.finance_flow;
		     var pageUrl= basePath+"finance/financeInfoController/viewFinanceInfo.do"+"?businessId="+rowData.fid;
		     var isAdministractiveExpenses = rowData.isAdministractiveExpenses;
		     if("1"==isAdministractiveExpenses){
		    	 //此时，为行政报销记录
		    	 pageUrl= basePath+"finance/expenses/editAdministractiveExpensesJsp.do"+"?id="+rowData.fid;
		     }
		     var oldId = FW.getCurrentTabId();
		     addEventTab(newId, pageName, pageUrl,oldId);
		  }, 
		   "onLoadError": function(){
          //加载错误的提示 可以根据需要添加
          $("#grid1_wrap,#toolbar_wrap").hide();
          $("#grid1_error").show();
          $("#grid1_queryempty").hide();
      }, "onLoadSuccess" : function(data){
          //远程无数据需要隐藏整个表格和对应的工具条，然后在无数据信息中指引用户新建
          if(data && data.total==0){
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
	        {field:'fid',title:'编号',width:140,fixed:true},
	        {field:'fname',title:'名称',width:40},
	        {field:'creatorname',title:'申请人',width:80,fixed:true},
	        {field:'createdate',title:'申请日期',width:110,fixed:true,
				formatter : function(value, row, index) {
					return FW.long2date(value);
				}
			},
	        {field:'finance_flow',title:'报销流程',width:140,fixed:true},
	        {field:'finance_type',title:'报销类型',width:80,fixed:true},
	        {field:'statusName',title:'状态',width:120,fixed:true}
	    ]]
	});
}
/* 新建报销页面 .报销名称,报销类型 ,如"travelcost,only" */
function creFinOprPage(finNameEn) {
	var finTypeEn="only";
	var oldId = FW.getCurrentTabId(); //获取当前所在选项卡编号
	var nbr = Math.floor(Math.random() * ( 10 + 1)); //随机数
	var newId = finNameEn + finTypeEn + nbr; //新建选项卡编号
	var finNameCn=""; //报销名称(中文)
	var page = "financeNew";
	var flowTypeEnums =  FW.getEnumMap("FIN_FLOW_TYPE")
	finNameCn = flowTypeEnums[finNameEn];
	var pageName = finNameCn;
	var pageUrl =  basePath+"finance/financeInfoController/createPage.do?"
		+"page="+page+"&finNameEn="+finNameEn+"&finTypeEn="+finTypeEn;
	if( "administrativeExpenses" == finNameEn ){
		//行政报销时，使用新的跳转
		pageUrl= basePath+"finance/expenses/addAdministractiveExpensesJsp.do";
	}
	addEventTab( newId, pageName, pageUrl, oldId );
}

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

