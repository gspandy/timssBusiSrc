<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html  style="height: 99%;">
<head>
<title>部门列表</title>
<script>_useLoadingMask = true;</script>

<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src='${basePath}js/attendance/common/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/attendance/workstatus.js?ver=${iVersion}'></script>
<style type="text/css">
.itcui_combo{
	height: 28px;
}
</style>

<script>
	$(document).ready(function() {
		checkExclude="mornEnd,noonStart,noonEnd";
		getDatagridColumns=function(){//页面自定义列
			var checkWidth=88;
			
			return [[
		    			{field:'id',title:'id',width:20,fixed:true,hidden:true},
		    			{field:'userId',title:'工号',width:70,fixed:true, sortable: true},
		    			{field:'userName',title:'姓名',width:70,fixed:true, sortable: true},
		    			{field:'deptName',title:'部门',width:120, fixed:true,sortable: true},
		    			{field:'workDate',title:'日期',width:100,fixed:true, sortable: true},
		    			
		    			{field:'mornStartCheck',title:'上班',width:checkWidth,fixed:true, sortable: true,'editor':checkEditor,
		    				formatter:checkFormatter
		    			},
		    			{field:'mornStartTime',title:'上班打卡时间',width:170,fixed:true, sortable: true,
		    				formatter:checkTimeFormatter},
		    			{field:'mornStartCheckType',title:'上班',hidden:true},
		    				
		    			{field:'dutyName',title:'值别',width:70,fixed:true, sortable: true,hidden:!isOpr},
		    			{field:'shiftName',title:'班次',width:170,fixed:true, sortable: true,hidden:!isOpr,
		    				formatter:shiftNameFormatter},	
		    			{field:'blank',title:'',width:130}
		    		]];	
		}
		
		init();
		
		//高级搜索
		$("#btn_advlocal").click(function() {
			if($(this).hasClass("active")){
				$("#contentTb").ITCUI_GridSearch("end");
			}
			else{
				$("#contentTb").ITCUI_GridSearch("init",{"remoteSearch":true,noSearchColumns:{10:true},"onParseArgs":function(arg){
					var list=["_mornStartCheck","_mornEndCheck","_noonStartCheck","_noonEndCheck"]; 
					for(var i=0;i<list.length;i++){
						var elt=list[i];
						if(arg[elt]!="null"){
							if(FW.getEnumMap("ATD_LEI_CATEGORY")[arg[elt]]){
								arg[elt+"Type"]="leave";
							}else if(FW.getEnumMap("ATD_AB_CATEGORY")[arg[elt]]){
								arg[elt+"Type"]="abnormity";
							}else{
								arg[elt+"Type"]=arg[elt];
								arg[elt]="null";
							}
						}
					}
					return {"search":JSON.stringify(arg)};
				}});
			}
		});
	});
	
</script>
</head>
<body style="height: 100%;min-width:900px" class="bbox list-page">
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar" class="btn-toolbar ">
	    	<div id="personTypeDiv" class="btn-group btn-group-sm" style="height: 28px;" >
				<select id="personTypeSelect" style="width: 100px; float: left;height: 28px;" >
				</select>
			</div>
			<div id="checkInvalidDiv" class="btn-group btn-group-sm" style="height: 28px;" >
				<select id="checkInvalidSelect" style="width: 100px; float: left;height: 28px;" >
				</select>
			</div>
	        <div class="btn-group btn-group-sm">
	           <button type="button" class="btn btn-default" data-toggle="button" id="btn_advlocal">查询</button>
	    	</div>
			<div id="reportDiv" class="btn-group btn-group-sm">
				<button type="button" class="btn btn-default dropdown-toggle" id="btnReport" data-toggle="dropdown" >
					生成报表
					<span class="caret"></span>
				</button>
				<ul class="dropdown-menu">
		            <li id="btn_print"><a href="javascript:void(0)" onclick="mothReport('KQMX')">出勤明细</a></li>
		            <li id="btn_print2"><a href="javascript:void(0)" onclick="mothReport('QJMX')">请假明细</a></li>
		            <li id="btn_print3"><a href="javascript:void(0)" onclick="mothReport('JBMX')">加班明细</a></li>
		            <li id="btn_print1"><a href="javascript:void(0)" onclick="mothReport('KQTJ')">考勤表</a></li>
		        </ul>
			</div>
	    </div>
	    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
	    <div id="pagination_1" class="toolbar-pager" bottompager="#bottomPager">        
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