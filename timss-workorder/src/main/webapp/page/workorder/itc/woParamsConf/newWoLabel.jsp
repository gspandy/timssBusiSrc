<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html style="height:99%">
<head>
<title>新建工单标识</title>
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
	var woLabelId = '<%=request.getParameter("woLabelId")%>';  
	var loginUserId = ItcMvcService.user.getUserId();
	var siteId = ItcMvcService.getUser().siteId;
</script>
<script>


	/* 表单字段定义  */
	var fields = [
			{title : "ID", id : "id",type : "hidden"},
		    {title : "编码", id : "labelCode"},
		    {title : "标识",id : "name", rules : {required:true}},
		    {title : "权重", id : "weight", rules : {required:true,number:true}},
	    	{
		        title : "说明", 
		        id : "remarks",
		        type : "textarea",
		        linebreak:true,
		        wrapXsWidth:12,
		        wrapMdWidth:8,
		        height:55
		       
		    }];
		    
		    
	$(document).ready(function() {
		 $("#woLabelForm").iForm("init",{"fields":fields,"options":{validate:true}});
		if(woLabelId == null || woLabelId == 'null'){ 
				$("#woLabelForm").iForm("hide","labelCode");
				$("#btn_edit").hide(); 
				FW.fixRoundButtons("#toolbar1");
			}else{  //查看工作方案
				$.post(basePath + "workorder/woParamsConf/queryWoLabelDataById.do",{"woLabelId":woLabelId},
					function(woLabelData){
						$("#btn_save").hide(); 
						FW.fixRoundButtons("#toolbar1");
						var woLabelFormData = eval("(" +woLabelData.result+ ")")
						$("#woLabelForm").iForm("setVal",woLabelFormData);
						$("#woLabelForm").iForm("endEdit");
					},"json");
			}
	});
	
	
	
	/**
	*保存
	**/
	function saveLabel(){
		/**表单验证*/
		if(!$("#woLabelForm").valid()){
			return ;
		}
	
		var woLabelFormData = $("#woLabelForm").iForm("getVal");  //取表单值
		var woLabelId = woLabelFormData.id;
		if(woLabelId==""){  //如果是新建作业方案的保存，则暂时设置jobPlanId = 0;
			woLabelId = 0;
		};
		 
		$.post(basePath + "workorder/woParamsConf/commitWoLabel.do",
		 		{"woLabelForm":JSON.stringify(woLabelFormData), "woLabelId":woLabelId},
				function(data){
					if(data.result == "success"){
						FW.success("新建成功")
						closeCurPage();
					}else {
						FW.error("新建失败")
					}
		  },"json");
	  
		$("#woLabelForm").iForm("endEdit");  //关闭编辑，
	}
	/**
	*编辑
	**/
	function editLabel(){
		$("#woLabelForm").iForm("beginEdit",["name","weight","remarks"]); 
		$("#btn_save").show(); 
		$("#btn_edit").hide(); 
		FW.fixRoundButtons("#toolbar1");
	}
	/**
	 * 关闭当前tab 页
	 */
	function closeCurPage(){
		FW.deleteTabById(FW.getCurrentTabId());
	}

</script>
</head>
<body style="height: 100%;" class="bbox">
	<div id="mainContent">
		<div class="bbox toolbar-with-pager" id="toolbar_wrap">
		    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
		    <div id="toolbar1" class="btn-toolbar ">
		    	<div class="btn-group btn-group-sm">
					<button id="btn_jp_new" type="button" class="btn btn-default" onclick="closeCurPage()">关闭</button>
				</div>
		        <div class="btn-group btn-group-sm">
		        	<button id="btn_save" type="button" class="btn btn-success" onclick="saveLabel()">保存</button>
		        	<button id="btn_edit" type="button" class="btn btn-default" onclick="editLabel()">编辑</button>
		        </div>
		    </div>
		</div>
		
		<div>
	    	 <form id="woLabelForm" class="autoform"></form>
	    </div>
	
	</div>
		
</body>
</html>