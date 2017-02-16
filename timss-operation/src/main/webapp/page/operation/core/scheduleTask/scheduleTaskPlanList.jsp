<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" style="height:99%" xml:lang="en">

<head>
<title>定期工作计划列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script type="text/javascript" src="${basePath}/js/operation/common/eventTab.js?ver=${iVersion}"></script>
<script>
    var isSearchMode=false;
    var dataGrid = null;
	$(document).ready(function() {
		//控制权限
		Priv.apply();
		//此处一定要给dataGrid赋值。dataGrid为全局变量，不设置会导致不能resize
		dataGrid = $("#schedulePlanGrid").iDatagrid("init",{
		        pageSize:pageSize,//pageSize为全局变量，自动获取的
		        url: basePath + "operation/scheduleTaskPlan/scheduleTaskPlanListData.do",	//basePath为全局变量，自动获取的       
		        singleSelect:true,
		        nowrap : false,  //设置可以换行
		        columns : [ [
		         		    {field : 'id',hidden : true},
		         		    {field : 'code',title : '编号',width : 140,align : 'left',
		         		   	 	fixed : true,sortable:true}, 
		         		   	{field : 'type',title : '类型',width : 80,align : 'left',
		         				fixed : true,sortable:true,
		         				formatter: function(value,row,index){
									return FW.getEnumMap("OPR_SCHEDULE_TYPE")[value]; 
								},
								"editor" : {
							        "type":"combobox",
							        "options" : {
							            "data" : FW.parseEnumData("OPR_SCHEDULE_TYPE",_enum)	
							        }
							    }
		         			},
		         			{field : 'content',title : '工作内容',width : 50,align : 'left'}, 
		         			{field : 'cycleLen',title : '周期间隔',width : 60,	align : 'right',fixed : true}, 
		         			{field : 'cycleType',title : '周期类型',width : 110,align : 'left',fixed : true,
		         				formatter: function(value,row,index){
									return FW.getEnumMap("OPR_SCHEDULE_CYCLE")[value]; 
								},
								"editor" : {
							        "type":"combobox",
							        "options" : {
							            "data" : FW.parseEnumData("OPR_SCHEDULE_CYCLE",_enum)	
							        }
							    }
							    
		         			}, 
		         			{field : 'beginTime',title : '开始日期',width : 100,align : 'left',
		         				fixed : true,sortable:true,
		         				formatter:function(value,row,index){
		         					var value=FW.long2date(value);
		         					return value;
		         				}
		         			},
		         			{field : 'invalideTime',title : '失效日期',width : 100,align : 'left',
		         				fixed : true,sortable:true,
		         				formatter:function(value,row,index){
		         					var value=FW.long2date(value);
		         					return value;
		         				}
		         			},
		         			{field : 'activityFlag',title : '状态',width : 60,align : 'left',
		         				fixed : true,sortable:true,
		         				formatter:function(value,row,index){
		         					if(value == "Y"){
		         						return "启用";
		         					}else{
		         						return "停用";
		         					}
		         				}
		         			} 
		         		] ],
		          onLoadSuccess: function(data){
		            //远程无数据需要隐藏整个表格和对应的工具条，然后在无数据信息中指引用户新建
		        	if(isSearchMode){
				        if(data && data.total==0){
				            $("#noSearchResult").show();
				        }else{
				            $("#noSearchResult").hide();
				        }
				    }else{
				    	$("#noSearchResult").hide();
			            if(data && data.total==0){
		            		$("#main_content").hide();
		                	$("#grid_empty").show();
			            }else{
			            	$("#main_content").show();
			                $("#grid_empty").hide();
			            }
				    }
				    isSearchMode = false;
		        },
		        onDblClickRow:function(rowIndex, rowData){
		        	var newId="scheduleTaskPlan"+rowData['id'];
		    		var pageName="定期工作计划";
		    		var pageUrl=basePath + "operation/scheduleTaskPlan/openTaskPlanPage.do?id="+rowData['id'];
		    		var oldId=FW.getCurrentTabId();
		    		addEventTab( newId, pageName, pageUrl, oldId );
		        },
	
		    });
		//表头搜索相关的
	    $("#btn_advSearch").click(function(){
		    if($(this).hasClass("active")){
		        $("#schedulePlanGrid").iDatagrid("endSearch");
		    }
		    else{
		    	
		        $("#schedulePlanGrid").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(args){
		        	isSearchMode=true;
				    //强烈建议转为一个string传给后台，方便转Bean或hashMap
				    return {"search":FW.stringify(args)};
				}});
		    }
		});
	});
	
	function refresh(){
		window.location.href=window.location.href;
	}
	
	function _afterCloseCallBack(){
	}
	
	

	function addSchedulePlanTab(){
		//新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
	   var rand = new Date().getTime() + Math.floor(Math.abs(Math.random()*100));
		var newId="taskPlan"+rand;
		var pageName="定期工作计划";
		var pageUrl=basePath + "operation/scheduleTaskPlan/openTaskPlanPage.do";
		var oldId=FW.getCurrentTabId();
		addEventTab( newId, pageName, pageUrl, oldId );
	}
</script>
</head>
<body style="height: 100%; min-width:850px" class="bbox list-page">
    <div id="main_content">
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar1" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm " >                            
	            <button type="button" class="btn btn-success priv" privilege="OPR_SCHEDTASKPLAN" id="opr_schedTaskPlan_list" onclick="addSchedulePlanTab();">新建</button>
	        </div> 
	        <div class="btn-group btn-group-sm">
	            <button type="button" id="btn_advSearch" data-toggle="button" class="btn btn-default">查询</button>
	        </div>

	    </div>
	    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
	    <div id="pagination_1" class="toolbar-pager" bottompager="#bottomPager">        
	    </div>
	</div>
	
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div id="grid1_wrap" style="width:100%">
	    <table id="schedulePlanGrid" pager="#pagination_1" class="eu-datagrid">
	        
	    </table>
	</div>
	<!-- 下页器部分-->
	<div id="bottomPager" style="width:100%;margin-top:6px">
	</div>
	</div>
	<div id="noResult" style="display:none;width:100%;height:62%;">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px"> 没有找到符合条件的结果</div>
			    
			</div>
		</div>
	</div>
	<!-- 错误信息-->
	<div class="row" id="grid1_error" style="display:none">
	    	无法从服务器获取数据，请检查网络是否正常
	</div>
	<!-- 无数据 -->
	<div id="grid_empty" style="display:none;width:100%;height:62%;">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">没有定期工作计划信息</div>
			    <div class="btn-group btn-group-sm margin-element">
		            <button type="button" class="btn btn-success priv" privilege="OPR_SCHEDTASKPLAN" onclick="addSchedulePlanTab();">新建</button>
			    </div>
			    
			</div>
		</div>
	</div>
</body>
</html>