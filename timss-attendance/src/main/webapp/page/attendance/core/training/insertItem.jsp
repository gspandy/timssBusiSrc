<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.yudean.itc.util.FileUploadUtil"%>
<%@page import="com.yudean.itc.util.Constant"%>
<%@page import="com.yudean.itc.dto.sec.SecureUser"%>
<!DOCTYPE html>
<html>
<head>

<title>添加人员</title>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script type="text/javascript">
_dialogEmmbed=true;
</script>
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script type="text/javascript" src="${basePath}js/workflow/workflow.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/attendance/common/addTab.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/attendance/common/pageDetail.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/attendance/common/pageMode.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/attendance/common/commonForm.js?ver=${iVersion}"></script>
<script type="text/javascript">
var isToUnify = "${isToUnify}";

var fields = (isToUnify=="1")?
	[
		{title : "学历", id : "education",rules : {maxChLength:parseInt(150*2/3)}},
		{title : "现从事工作或岗位 ", id : "job",rules : {maxChLength:parseInt(150*2/3)}}
 	]:
	[
		{title : "ID", id : "userId",type:"hidden"},
		{title : "姓名", id : "userName",linebreak:true,rules : {required:true},
			render : function(id){
				$('#' + id).attr('placeholder','请输入姓名或账号').initHintPersonList();
				$('#' + id).initHintPersonList({
					clickEvent: function(id, name) {
						var info = id.split('_');
						$("#itemForm").iForm("setVal",{userId:info[0],userName:name});
						var url = basePath + "attendance/training/getUserJob.do?userId="+info[0];
						$.ajax({
							url : url,
							type : 'post',
							dataType : "json",
							success : function(data) {
								if(data.result =="success"){
									$("#itemForm").iForm("setVal",{job : data.job});
								}else{
									FW.erroe("信息获取失败");
								}
							}
						});
					}
				});	
			}
		},
		{title : "学历", id : "education",linebreak:true,rules : {maxChLength:parseInt(150*2/3)}},
		{title : "现从事工作或岗位 ", id : "job", type : 'hidden', linebreak:true,rules : {maxChLength:parseInt(150*2/3)}}
 	];
 	
	var opts={
		validate:true,
		fixLabelWidth:true,
		xsWidth:11,
		mdWidth:11
	};
		
	//校验
	function valid(){
		return $("#itemForm").valid();
	}
	
	$(document).ready(function() {
		$("#itemForm").iForm("init",{"options":opts,"fields":fields});
	});
</script>
</head>
<body>
	<div class="margin-title-table">
		<form id="itemForm">
			<table id="itemDatagrid" class="eu-datagrid"></table>
		</form>
	</div>
</body>
</html>

