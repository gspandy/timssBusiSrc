<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html style="height:99%">
<head>
<title>新建投诉类别</title>
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
	var complainTypeConfId = '<%=request.getParameter("complainTypeConfId")%>';  
	var loginUserId = ItcMvcService.user.getUserId();
	var siteId = ItcMvcService.getUser().siteId; 
</script>
<script>

	/* 表单字段定义  */
	var fields = [
			{title : "ID", id : "id",type : "hidden"},
		    {title : "投诉类别", id : "typename", rules : {required:true,maxChLength:20},wrapMdWidth:6},
		    {title : "投诉内容",  rules : {required:true,maxChLength:120},
		        id : "remarks",
		        type : "textarea",wrapMdWidth:6,
		        linebreak:true
		    }];
		    	
   
 	$(document).ready(function() {
		 $("#complainTypeConfForm").iForm("init",{"fields":fields,"options":{validate:true}});
		  
		 if(complainTypeConfId == null || complainTypeConfId == 'null' || complainTypeConfId == ""){
			 	$("#inPageTitle").html("新建投诉类别");
				$("#btn_edit").hide(); 
				$("#btn_del").hide();
				FW.fixRoundButtons("#toolbar1");
			}else{  //查看工作方案
				$.post(basePath + "itsm/woParamsConf/queryComplainTypeConfDataById.do",{"complainTypeConfId":complainTypeConfId},
					function(complainTypeConfData){
						$("#btn_save").hide(); 
						FW.fixRoundButtons("#toolbar1");
						var complainTypeConfFormData = JSON.parse( complainTypeConfData.result );
						$("#complainTypeConfForm").iForm("setVal",complainTypeConfFormData);
						$("#complainTypeConfForm").iForm("endEdit");
					},"json");
			}
	}); 
	
	/**
	*保存
	**/
	function saveComplainTypeConf(){
		/**表单验证*/
		if(!$("#complainTypeConfForm").valid()){
			return ;
		}
		var complainTypeConfFormData = $("#complainTypeConfForm").iForm("getVal");//取表单值
		var complainTypeConfId = complainTypeConfFormData.id;
		if(complainTypeConfId==""){  //如果是新建作业方案的保存，则暂时设置0;
			complainTypeConfId = 0;
		};
	 	$.post(basePath + "itsm/woParamsConf/commitComplainTypeConf.do",
		 		{"complainTypeConfForm":JSON.stringify(complainTypeConfFormData),"complainTypeConfId":complainTypeConfId},
				function(data){
					if(data.result == "success"){
						FW.success("保存成功");
						closeCurPage();
					}else {
						FW.error("保存失败")
					}
		  },"json"); 
		  $("#complainTypeConfForm").iForm("endEdit");  //关闭编辑，
	}
	/**
	*编辑
	**/
	function editComplainTypeConf(){
		$("#inPageTitle").html("编辑投诉类别");
		$("#complainTypeConfForm").iForm("beginEdit",["typename","remarks"]); 
		$("#btn_save").show();     
		$("#btn_edit").hide();
		$("#btn_del").hide(); 
		FW.fixRoundButtons("#toolbar1");
	}
	/**
	*删除
	**/
	function delComplainTypeConf(){
		Notice.confirm("确定删除|确定删除此投诉类别么？",function(){
			$.post(basePath + "itsm/woParamsConf/delComplainType.do",
			 		{"complainTypeConfId":complainTypeConfId},
					function(data){
						if(data.result == "success"){
							FW.success("删除成功")
							closeCurPage();
						}else {
							FW.error("删除失败")
						}
			  },"json");
		},null,"info");
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
					<button type="button" class="btn btn-default" onclick="closeCurPage()">关闭</button>
				</div>
		        <div class="btn-group btn-group-sm">
		        	<button id="btn_save" type="button" class="btn btn-success" onclick="saveComplainTypeConf()">保存</button>
		       
		        	<button id="btn_edit" type="button" class="btn btn-default" onclick="editComplainTypeConf()">编辑</button>
		        </div>
		         <div class="btn-group btn-group-sm">
		        	<button id="btn_del" type="button" class="btn btn-default" onclick="delComplainTypeConf()">删除</button>
		        </div>
		         
		    </div>
		</div>
		<div  class="inner-title">
			<span id="inPageTitle">投诉类别详情</span>
		</div>
		<div>
	    	 <form id="complainTypeConfForm" class="autoform"></form>
	    </div>
		
	</div>
		
</body>
</html>