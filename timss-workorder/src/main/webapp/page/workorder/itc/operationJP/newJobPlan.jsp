<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
	<title>新建标准作业方案</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
	<script>_useLoadingMask = true;</script>
	<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
	<script>
		var jpId = '<%=request.getParameter("jobPlanId")%>'; 
		var loginUserId = ItcMvcService.user.getUserId();
		var siteId = ItcMvcService.getUser().siteId;
		var formRead = false;
		if(jpId != "null"){
			formRead = true;
		}
	</script>
	<script type="text/javascript" src="${basePath}js/workorder/itc/newJobPlan.js?ver=${iVersion}"></script>
	<script type="text/javascript" src="${basePath}js/inventory/common/inventory.js?ver=${iVersion}"></script>
	<script type="text/javascript">
		var faultTypeParams = '<%=request.getParameter("faultTypeParams")%>';
		var faultTypeId = null;
		var faultTypeName = null;
		if(faultTypeParams != "null"){
			faultTypeParams = eval("("+faultTypeParams+")");
			faultTypeId = faultTypeParams.faultTypeId;
			faultTypeName = faultTypeParams.faultTypeName;
		}
		
		$(document).ready(function() {
			if(jpId == null || jpId == 'null'){  //新建作业方案,参数定为0传过去
				initJPPage(0); 
			}else{  //查看工作方案
				$.post(basePath + "workorder/jobPlan/queryJPDataById.do",{jobPlanId:jpId},
					function(jpFullData){
						initJPPage(jpFullData);
					},"json");
			}
			//控制权限
			Priv.apply();
		});
		function triggerFaultTypeTreeSelect(data){
			var faultTypeCode = data.faultTypeCode;
			if(formRead == false && faultTypeCode.substring(0,2) =="SD"){
				$("#jobPlanForm").iForm("setVal",{
			     	faultTypeId : data.id,
		        	faultTypeName:data.text
		        	
			    });
			}
			
		} 
	</script>
  </head>
  
  <body>
   	 <div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar" class="btn-toolbar ">
	    	<div class="btn-group btn-group-sm">
	        	<button id="btn_close" type="button" class="btn btn-default" onclick="closeCurPage(true);">关闭</button>
	        </div>
	    	<div id="btn_jp_saveDiv" class="btn-group btn-group-sm">
				<button id="btn_jp_save" type="button" class="btn btn-success priv" privilege="wo-jobPlan-oper"  onclick="commitJP()">保存</button>
			</div>
	        <div id="btn_jp_editDiv" class="btn-group btn-group-sm">
	        	<button id="btn_jp_edit" type="button" class="btn btn-default priv" privilege="wo-jobPlan-oper"  onclick="editJP()">编辑</button>
	        </div>
	        <div id="btn_jp_unavailableDiv" class="btn-group btn-group-sm">
	        	<button id="btn_jp_unavailable" type="button" class="btn btn-default priv" privilege="wo-jobPlan-oper"  onclick="unavailableJP()">禁用</button>
	        </div>
	        
	    </div>
	</div>
    <div>
    	 <form id="jobPlanForm" class="autoform"></form>
    </div>
   <div id="jobPlanContentDiv">
		<div id="title_tool" grouptitle="工具">
			<div class="margin-title-table">
				<form id="toolTableForm"><table id="toolTable" class="eu-datagrid"></table></form>
				<div id="toolBtnDiv" class="row btn-group-xs" >
					 <button id="btn_toolTable" onclick="appendTool();" type="button" class="btn btn-success">添加工具</button>
				</div>
			</div>
		</div>
    </div>
  </body>
</html>
