<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>运行记事</title>
<script>_useLoadingMask = true;</script>

<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src='${basePath}js/operation/common/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/operation/common/commonForm.js?ver=${iVersion}'></script>
<script src='${basePath}js/operation/common/formatterDate.js?ver=${iVersion}'></script>
<script>
//本页面所有form共用的options
var opts={
	validate:true,
	fixLabelWidth:true
};
</script>
<style type="text/css">
#defined_msk{
	position: absolute;
    left: 0;
    z-index: 2333;
    background-color: #FFF;
    display: table;
    vertical-align: middle;
}
#jiaojieForm  .itcui_combo_text{  width: auto !important;}
</style>
<script src='${basePath}js/operation/operationlog/oprNoteMode.js?ver=${iVersion}'></script>
<script src='${basePath}js/operation/operationlog/oprMode.js?ver=${iVersion}'></script>

<script>
var siteId = '${siteId}';

var pageCode = "opr_note_formcode_";
var formId = "baseform";

$(function(){
	//加载岗位和工种
 	OprNote.objs.deptList=${deptList};
	OprNote.objs.userJobsStr="${userJobsStr}";
	OprNote.init();
});

</script>
<style>
.itcui_frm_grp_title{
	position:relative;
}
</style>
</head>
<body>
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar" class="btn-toolbar ">
	        <div class="btn-group-sm" style="margin-top: 2px!important;">
		        <div id="jobsDiv" class="pull-left">
		        	<span class="pull-left ctrl-label" style="line-height:28px;margin-right:5px">切换岗位：</span>
		        	<div style="display: inline-block;margin-right:5px;">
						<select id="deptSelect" style="width: 150px; float: left;" >
						</select>
					</div>
					<div style="display: inline-block;">
						<select id="jobsSelect" style="width: 150px; float: left;" >
						</select>
					</div>
				</div>
				<div id="teamSelectDiv" class="pull-left">
		        	<span class="pull-left ctrl-label" style="line-height:28px;margin-right:5px;margin-left:10px">分组：</span>
					<select id="teamSelect" style="width: 150px; float: left;" >
					</select>
				</div>
	        </div>
	    </div>
	</div>
	<div id="contentDiv">
		<div class="inner-title" id="noteInnerTitle">
			运行方式
		</div>
		<!-- 基础信息  -->
		<div id="baseInfoDiv">
			<form id="baseform" class="autoform" ></form>
		</div>
		
		 <div style="clear:both;margin-top:10px"></div>
		 
		<!-- 运行方式  -->
		<div id="modeContentDiv" grouptitle="运行方式">
		 	<div id="modeFormDiv">
				<form id="modeContentForm"></form>
			</div>
			<div id="mode_error_area" style="display:none;width:100%;height:62%">
				<div style="height:100%;display:table;width:100%">
					<div style="display:table-cell;vertical-align:middle;text-align:center">
					    <div style="font-size:14px" id="error_msg">
					   		<div id="mode_empty">没有运行方式数据</div>
					    </div>
					</div>
				</div>
			</div>
		</div>
		
		<div style="clear:both;margin-top:10px"></div>
	</div>
	
	<!-- 有错误 -->
	<div id="error_area" style="display:none;width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px" id="error_msg">
			   		<div id="dept_empty">没有可以查看的工种和岗位</div>
			   		<div id="jobs_empty">该工种下没有可以查看的岗位</div>
			   		<div id="grid1_error">无法从服务器获取数据，请检查网络是否正常</div>
			   		<div id="init_error">请检查当前查看的岗位是否具有排班信息</div>
			    </div>
			</div>
		</div>
	</div>
</body>
</html>