<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html style="height: 99%;">
<head>
<title>会议室预定情况</title>
<script>
	_useLoadingMask = true;
</script>

<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<!-- fullcalendar  -->
<link href='${basePath}css/meetingShow.css?ver=${iVersion}' rel='stylesheet' />
<script src='${basePath}js/attendance/common/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/attendance/meeting/meetingShow.js?ver=${iVersion}'></script>

<script>
$(document).ready(function() {
	initMeeting();
		
	$("#btn_add").click(function() {
		var url = "${basePath}attendance/meeting/insertMeetingMenu.do";
		addTabFullcalendar("addMeeting",
				"会议室预定", url,
				"attendance",
				"meetingCalendar");
	});

});
</script>
</head>
<body style="height: 100%;" class="bbox">
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
		<!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
		<div id="toolbar" class="btn-toolbar ">
			<div class="atd_btn_pri atd_ab_create btn-group btn-group-sm">
				<button type="button" class="btn btn-success" id="btn_add">新建</button>
			</div>
		</div>
	</div>
	<div style="" id="contentDiv" >
	
	</div>

</body>
</html>