<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


<!DOCTYPE html>
<html style="height: 99%;">
  <head>
	<title>系统负责人查询</title>
	<script>_useLoadingMask = true;</script>
	<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
	<script type="text/javascript">
		/**
		 * 关闭当前tab 页
		 */
		function closeCurPage(){
			FW.deleteTabById(FW.getCurrentTabId());
		}
	
	</script>

  </head>
  
  <body style="height: 100%; " class="bbox list-page">
   	 <div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar" class="btn-toolbar ">
	    	<div class="btn-group btn-group-sm">
	        	<button id="btn_close" type="button" class="btn btn-default" onclick="closeCurPage();">关闭</button>
	        </div>
	    </div>
	</div>
	<div  class="inner-title">
		<span id="inPageTitle">各系统负责人</span>
	</div>
	
    <div>
    	<table border="1">
			<tr>
				<th>系统</th>
				<th>负责人</th>
				<th>电话</th>
			</tr>
			<tr>
				<td>OA、EIP系统、行政后勤系统、可靠性系统、粤电外网、企业邮箱、档案系统、传真系统、电子印章、新通讯录系统、电子地图、短信平台、无纸化系统、多媒体会议室、Lync系统、Exchange邮箱</td>
				<td>陈旭</td>
				<td>+862085136319</td>
			</tr>
			<tr>
				<td>商务网,</td>
				<td>林广银</td>
				<td>+862085136379</td>
			</tr>
			<tr>
				<td>电力营销系统、EVAM、ELN系统、OGSM绩效管理系统、水情系统、水文系统、TIMSS系统、职称评审系统</td>
				<td>赵阳</td>
				<td>+862085136365</td>
			</tr>
			<tr>
				<td>ERP系统、</td>
				<td>李程雄</td>
				<td>+862085136361</td>
			</tr>
			<tr>
				<td>人事统计系统、对标系统、先进发电企业考核系统、X3系统、新综合统计系统、政研系统、航运EAM、小指标系统、E-HR系统</td>
				<td>尹珂珂</td>
				<td>+862085136326</td>
			</tr>
			
			<tr>
				<td>基建系统、SIS系统、机房、服务器、网络、视频会议</td>
				<td>黄志勇</td>
				<td>+862085136367</td>
			</tr>
			<tr>
				<td>桌面终端类</td>
				<td>报障客服（科技公司）</td>
				<td>100</td>
			</tr>
		</table>
    </div>
  </body>
</html>
