<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
	<title>不执行</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
	<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
	<!-- js引入的示例,basePath使用EL表达式获取 -->
	<script type="text/javascript">
	var _dialogEmmbed = true;
	/* 表单字段定义  */
	var fields = [
		        {
			        title : "原因及备注", 
			        id : "remarks",
            		type : "textarea",
       		        linebreak:true,
       		        rules : {required:true,maxChLength:600},
       		        wrapXsWidth:12,
       		        wrapMdWidth:8,
       		        height:200
			    }
			];
		
	$(document).ready(function() {
		/* form表单初始化 */
		$("#notdoTaskForm").iForm("init",{"fields":fields,"options":
			{
				validate:true,
				xsWidth:12,
				mdWidth:8,
				labelFixWidth:100
			}});
	});
		
	//校验
	function valid(){
		return $("#notdoTaskForm").valid();
	}
	
	</script>
  </head>
  <body>
    <div style="margin-top:8px">
    	 <form id="notdoTaskForm" class="autoform"></form>
    </div>
  </body>
</html>
