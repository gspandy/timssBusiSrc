<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
	<title>录入遗留问题</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
	<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
	<!-- js引入的示例,basePath使用EL表达式获取 -->
	<script type="text/javascript">
	var _dialogEmmbed = true;
	var remainFaultData = '<%=request.getParameter("remainFaultData")%>'; 
		/* 表单字段定义  */
	var fields = [
			    {title : "ID", id : "id",type : "hidden"},
			    {title : "woId", id : "woId",type : "hidden"},
			    {title : "问题描述", id : "description", rules : {required:true}},
			    {title : "维护设备", id : "equipName"},
			    {title : "设备ID", id : "equipId",type : "hidden"},
			    {
			        title : "专业", 
			        id : "specialtyId",
			        type : "combobox",
			        dataType:"enum",
			        enumCat:"ITSM_SPEC",
			        rules : {required:true}
			    },
			    {
			        title : "备注", 
			        id : "remarks",
			        type : "textarea",
			        linebreak:true
			    }
			];
		
	$(document).ready(function() {
		
	/* form表单初始化 */
		$("#remainFaultForm").iForm("init",{"fields":fields,"options":
			{
				validate:true,
				xsWidth:12,
				mdWidth:12,
				labelFixWidth:80
			}});
			
			var remainFaultObj = JSON.parse( remainFaultData );
			
			$("#remainFaultForm").iForm("setVal",remainFaultObj);
			//$("#remainFaultForm").iForm("setVal",{"description":"","remarks":""});
			$("#remainFaultForm").iForm("endEdit","equipName");
		});
	function getRegForm(){
	    if(!$("#remainFaultForm").valid()){
	        return null;
	    }
	    return $("#remainFaultForm").iForm("getVal");
	}
	</script>
  </head>
  <body>
    <div>
    	 <form id="remainFaultForm" class="autoform"></form>
    </div>
  </body>
</html>
