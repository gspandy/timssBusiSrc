<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html  style="height: 99%;">
<head>
<title>部门列表</title>
<script>_useLoadingMask = true;</script>

<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src='${basePath}js/attendance/common/addTab.js?ver=${iVersion}'></script>

<script>
//第一次打开页面
var modelFlag = false;

	$(document).ready(function() {
		$("#searchDateFrom").iDate("init",{datepickerOpts:{format:"yyyy-mm-dd hh:ii",minView:0}});
		$("#searchDateEnd").iDate("init",{datepickerOpts:{format:"yyyy-mm-dd hh:ii",minView:0}});
		$('#onStatus').iCheck({
		    checkboxClass: 'icheckbox_flat-blue',
		    radioClass: 'iradio_flat-blue',
		});
		
		//此处一定要给dataGrid赋值。dataGrid为全局变量，不设置会导致不能resize
		dataGrid = $("#contentTb").iDatagrid("init",{
	        pageSize:pageSize,//pageSize为全局变量，自动获取的
	        singleSelect :true,
	        url: basePath + "attendance/cardData/queryCardDataList.do",	//basePath为全局变量，自动获取的       
	        "columns":[[
					{field:'id',title:'id',width:20,fixed:true,hidden:true},
					{field:'checkDate',title:'打卡时间',width:180, fixed:true,sortable: true},
					{field:'userId',title:'工号',width:70,fixed:true, sortable: true},
					{field:'userName',title:'姓名',width:70,fixed:true, sortable: true},
					{field:'deptName',title:'部门',width:120, fixed:true,sortable: true},
					{field:'workStatus',title:'备注',width:300,fixed:true,sortable:true,'editor':{
							 "type" : "combobox",
							 "options" : {
								 data:FW.parseEnumData("ATD_MACHINE_WORK_STATUS",_enum)
							 }
						 },formatter:function(value,row,index){
						 	var str="";
						 	 if(row.dutyName&&row.shiftName){
						 	 	str+="("+row.dutyName+" "+row.shiftName;
						 	 	if(row.startTime&&row.longTime){
						 	 		var sth=row.startTime.substr(0,2);
									var stm=row.startTime.substr(2,4);
									var eth=(parseInt(sth)+row.longTime)%24;
									str+=sth+":"+stm+"-"+(eth<10?"0":"")+eth+":"+stm;
						 	 	}
						 	 	str+=")";
						 	 }
							 return FW.getEnumMap("ATD_MACHINE_WORK_STATUS")[value]+str;
						 }
					},
					{field:'blank',title:' ',width:120, sortable: false},
				]],
			"onLoadError": function(){
				//加载错误的提示 可以根据需要添加
				$("#grid1_wrap,#toolbar_wrap,#bottomPager").hide();
			    $("#grid1_error").show();
			},
	        onLoadSuccess: function(data){
	            //远程无数据需要隐藏整个表格和对应的工具条，然后在无数据信息中指引用户新建
	            if(data && data.total==0){
	                if( modelFlag ){
	                	$("#noResult").show();
	                }else{
	                	$("#grid1_wrap,#toolbar_wrap").hide();
		                $("#grid1_empty").show();
		                $("#noResult").hide();
	                }
	            }else{
	            	$("#grid1_wrap,#toolbar_wrap").show();
	                $("#grid1_empty").hide();
	                $("#noResult").hide();
	            }
	        }

	    });
		
		
		//高级搜索
		$("#btn_advlocal").click(function() {
			modelFlag = true;
			if($(this).hasClass("active")){
				$("#contentTb").ITCUI_GridSearch("end");
				$("#advSearchBar").hide();
			}
			else{
				$("#advSearchBar").show();
				$("#contentTb").iDatagrid("beginSearch",{noSearchColumns:{6:true},"remoteSearch":true,"onParseArgs":function(arg){
					var searchDateFrom = $("#sDateFrom").val();
					var searchDateEnd = $("#sDateEnd").val();
					arg.searchDateFrom = searchDateFrom;
					arg.searchDateEnd = searchDateEnd;
					var onStatus = $("#onStatus").parent().hasClass("checked")?1:0;
					arg.onStatus = onStatus;
					
					return {"search":JSON.stringify(arg)};
				}});
			}
		});
		
	});
	
	//高级查询中DIV --- 查询按钮
	function advSearch(){
		searchFlag = true;
		
		$(".itcui_btn_mag").first().click();
	}
</script>
</head>
<body style="height: 100%;" class="bbox list-page">
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar" class="btn-toolbar ">
	        <div class="atd_btn_pri atd_ab_search btn-group btn-group-sm">
	           <button type="button" class="btn btn-default" data-toggle="button" id="btn_advlocal">查询</button>
	    	</div>
	    </div>
	    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
	    <div id="pagination_1" class="toolbar-pager" bottompager="#bottomPager">        
	    </div>
	</div>
	<div style="clear:both"></div>
	 <div id="advSearchBar" style="height: 35px;margin-top: 6px;display: none;" class="bbox">
			<span class="ctrl-label pull-left">打卡时间：</span>
		    <div class="pull-left" id="searchDateFrom" style="width: 130px;" style="margin-left:8px" input-id="sDateFrom"></div>
		    <span class="ctrl-label pull-left"  style="margin-left:8px">到：</span>
		    <div class="pull-left" id="searchDateEnd" style="width: 130px;" input-id="sDateEnd"></div>
		    <div class="btn-group btn-group-sm fl" style="margin-left:8px">
		          <input type="checkbox" id="onStatus"></input>
				<label for="set_default" class="pure-label">只显示打卡异常</label>
		    </div>
		    <div class="btn-group btn-group-sm fl" style="margin-left:8px">
		        <button type="button" class="btn btn-default" onclick="advSearch()">查询</button>
		    </div>
	</div>
	
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div id="grid1_wrap" style="width:100%">
	    <table id="contentTb" pager="#pagination_1" class="eu-datagrid">
	    </table>
	</div>
	<!-- 下页器部分-->
	<div id="bottomPager" style="width:100%;margin-top:6px">
	</div>
	
	<div id="noResult" style="display:none;width:100%;">
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
	<div id="grid1_empty" style="display:none;width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">没有打卡记录</div>
			</div>
		</div>
	</div>
</body>
</html>