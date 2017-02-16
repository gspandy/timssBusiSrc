<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html style="height:99%">
<head>
<title>新建工单标识</title>
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
	var woSkillId = '<%=request.getParameter("woSkillId")%>';  
	var loginUserId = ItcMvcService.user.getUserId();
	var siteId = ItcMvcService.getUser().siteId;
</script>
<script>


	/* 表单字段定义  */
	var fields = [
			{title : "ID", id : "id",type : "hidden"},
		    {title : "编码", id : "skillCode"},
		    {title : "技能名",id : "name", rules : {required:true}},
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
		 $("#skillForm").iForm("init",{"fields":fields,"options":{validate:true}});
		if(woSkillId == null || woSkillId == 'null'){ 
				$("#skillForm").iForm("hide","skillCode");
				$("#btn_edit").hide(); 
				FW.fixRoundButtons("#toolbar1");
			}else{  //查看工作方案
				$.post(basePath + "workorder/woParamsConf/queryWoSkillDataById.do",{"woSkillId":woSkillId},
					function(woSkillData){
						$("#btn_save").hide(); 
						FW.fixRoundButtons("#toolbar1");
						var woSkillFormData = eval("(" +woSkillData.result+ ")")
						$("#skillForm").iForm("setVal",woSkillFormData);
						$("#skillForm").iForm("endEdit");
					},"json");
			}
	});
	
	
	/**
	*保存
	**/
	function saveSkill(){
		/**表单验证*/
		if(!$("#skillForm").valid()){
			return ;
		}
	
		var skillFormData = $("#skillForm").iForm("getVal");  //取表单值
		var woSkillId = skillFormData.id;
		if(woSkillId==""){  //如果是新建作业方案的保存，则暂时设置jobPlanId = 0;
			woSkillId = 0;
		};
		 
		$.post(basePath + "workorder/woParamsConf/commitWoSkill.do",
		 		{"skillForm":JSON.stringify(skillFormData), "woSkillId":woSkillId},
				function(data){
					if(data.result == "success"){
						FW.success("新建成功")
						closeCurPage();
					}else {
						FW.error("新建失败")
					}
		  },"json");
	  
		$("#skillForm").iForm("endEdit");  //关闭编辑，
	}
	/**
	*编辑
	**/
	function editSkill(){
		$("#skillForm").iForm("beginEdit",["name","remarks"]); 
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
		        	<button id="btn_save" type="button" class="btn btn-success" onclick="saveSkill()">保存</button>
		        	<button id="btn_edit" type="button" class="btn btn-default" onclick="editSkill()">编辑</button>
		        </div>
		    </div>
		</div>
		
		<div>
	    	 <form id="skillForm" class="autoform"></form>
	    </div>
	
	</div>
		
</body>
</html>