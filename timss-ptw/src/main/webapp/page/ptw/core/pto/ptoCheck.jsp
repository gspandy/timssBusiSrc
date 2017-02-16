<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
	<title>工单列表导出</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
	<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
	<!-- js引入的示例,basePath使用EL表达式获取 -->
	<script type="text/javascript">
	var _dialogEmmbed = true;
	var siteId = ItcMvcService.getUser().siteId;
	
	/* 表单字段定义  */
	var fields = [
			    {
			        title : "是否合格",id : "isProper",type : "combobox",
			        options : {allowEmpty : true},
			        data : [
			            ["Y","是"],
			            ["N","否"]
			        ]
			    },
			    {
			        title : "存在的问题", 
			        id : "problem",
			        type : "textarea",
			        linebreak:true,
			        wrapXsWidth:12,
			        wrapMdWidth:8,
			        height:150,
			        rules : {required:true,maxChLength:680}
			    }
			];
		
	$(document).ready(function() {
		
	/* form表单初始化 */
		$("#ptoCheckForm").iForm("init",{"fields":fields,"options":
			{
				validate:true,
				xsWidth:12,
				mdWidth:8,
				labelFixWidth:80
			}});
		 
	});
	
	</script>
  </head>
  <body>
  	
    <div style="margin-top:8px">
    	 <form id="ptoCheckForm" class="autoform"></form>
    </div>
  </body>
</html>
