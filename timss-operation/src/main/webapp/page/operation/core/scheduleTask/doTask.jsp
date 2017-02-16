<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
	<title>转其他班次</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
	<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
	<!-- js引入的示例,basePath使用EL表达式获取 -->
	<script type="text/javascript">
	var _dialogEmmbed = true;
	var loginUserName = ItcMvcService.user.getUserName();
	/* 表单字段定义  */
	var fields = [
				{title : "执行结果", id : "doResult",type : "radio",
						 data : [
								 ['normal','正常',true],
								 ['abnormal','异常']
								]
				 },
			    {title : "执行时间", id : "doTime",type:"datetime",dataType:"datetime",
					rules : {required:true}
				},
			    {title : "执行人", id : "doUserNames",rules : {required:true}},
		        {
			        title : "情况及备注", 
			        id : "remarks",
            		type : "textarea",
            		rules : {required:true,maxChLength:600},
       		        linebreak:true,
       		        wrapXsWidth:12,
       		        wrapMdWidth:8,
       		        height:88
			    }
			];
		
	$(document).ready(function() {
		
		/* form表单初始化 */
		$("#doTaskForm").iForm("init",{"fields":fields,"options":
			{
				validate:true,
				xsWidth:12,
				mdWidth:8,
				labelFixWidth:100
			}});
		$("#doTaskForm").iForm("setVal",{doUserNames:loginUserName});
	});
		
	//校验
	function valid(){
		return $("#doTaskForm").valid();
	}
	</script>
  </head>
  <body>
  	
    <div style="margin-top:8px">
    	 <form id="doTaskForm" class="autoform"></form>
    </div>
  </body>
</html>
