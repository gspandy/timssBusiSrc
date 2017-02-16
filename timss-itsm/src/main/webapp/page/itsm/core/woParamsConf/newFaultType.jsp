<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html style="height:99%">
<head>
<title>服务目录</title>
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
	var faultTypeId = '<%=request.getParameter("faultTypeId")%>'; 
	var ftRootId = '<%=request.getParameter("ftRootId")%>'; 
	var loginUserId = ItcMvcService.user.getUserId();
	var siteId = ItcMvcService.getUser().siteId;
	var selectFTypeId = '<%=request.getParameter("selectFTypeId")%>';
	var selectFTypeParentId = '<%=request.getParameter("selectFTypeParentId")%>';
	var selectFTypeName = '<%=request.getParameter("selectFTypeName")%>';
</script>
<script>
/* 表单字段定义  */
	var fields = [
			{title : "ID", id : "id",type : "hidden"},
		    {title : "类型编码", id : "faultTypeCode"},
		    {title : "目录/性质",id : "name", rules : {required:true}},
		    {title : "父目录 ID",id : "parentId",type : "hidden"},
		    {title : "父目录/性质",id : "parentTypeName"},
		    {title : "关键字",id : "keywords"},
		    {title : "排序码",id : "defaultScore"},
		    {title : "维护组",id : "principalGroup", type : "combobox",
		    	rules : {required:true},
		    	options : {
			    		url : basePath + "itsm/woUtil/userGroupFilter.do?filterStr=itc_itsm_ft",
			    		allowEmpty : true,
			    	    remoteLoadOn : "init"
			    	}	
		    },
	    	{
		        title : "备注", 
		        id : "remarks",
		        type : "textarea",
		        linebreak:true,
		        wrapXsWidth:12,
		        wrapMdWidth:8,
		        height:55
		       
		    }];
	$(document).ready(function() {	
		$.ajaxSetup({"async":false});  //设置为顺序执行
		 $("#faultTypeForm").iForm("init",{"fields":fields,"options":{validate:true}});
		 if(faultTypeId == null || faultTypeId == 'null'){ 
		 	$("#inPageTitle").html("新建服务目录");
			//$("#faultTypeForm").iForm("hide","faultTypeCode");
			$("#faultTypeForm").iForm("setVal",{"parentId":selectFTypeId,"parentTypeName":selectFTypeName});
			if(selectFTypeId == ftRootId){  //如果选中的是一级服务目录，显示 维护组
				$("#faultTypeForm").iForm("show",["principalGroup"]);  
			}else{  //如果选中的不是一级服务目录，则隐藏 维护组
				$("#faultTypeForm").iForm("hide",["principalGroup"]);  
			}
			$("#btn_edit").hide(); 
			$("#deltetDiv").hide(); 
			FW.fixRoundButtons("#toolbar1");
		}else{  //查看
			$.post(basePath + "itsm/itsmFaultType/queryFaultTypeDataById.do",{"faultTypeId":faultTypeId},
				function(faultTypeData){
					$("#btn_save").hide(); 
					FW.fixRoundButtons("#toolbar1");
				 
					 if(faultTypeData.parentId == ftRootId){
					 	$("#faultTypeForm").iForm("show",["principalGroup"]);  
					 }else{
					 	$("#faultTypeForm").iForm("hide",["principalGroup"]);  
					 }
					 
					 $("#faultTypeForm").iForm("setVal",faultTypeData);
					 $("#faultTypeForm").iForm("endEdit");
					
				},"json");
		}
		$.ajaxSetup({"async":true});  //打开异步有效
	});
	
	/**左边故障类型树选择触发事件*/
	function triggerFaultTypeTreeSelect(data){
		if(data.id == ftRootId){
			$("#faultTypeForm").iForm("show",["principalGroup"]);  
		}else{
			$("#faultTypeForm").iForm("hide",["principalGroup"]);  
		}
		 $("#faultTypeForm").iForm("setVal",{
	        parentId:data.id,
	       	parentTypeName:data.text,
	    });
	}
	/**
	*保存
	**/
	function saveFaultType(){
		/**表单验证*/
		if(!$("#faultTypeForm").valid()){
			return ;
		}
	
		var faultTypeFormData = $("#faultTypeForm").iForm("getVal");  //取表单值
		var faultTypeId = faultTypeFormData.id;
		if(faultTypeId==""){  //如果是新建作业方案的保存，则暂时设置jobPlanId = 0;
			faultTypeId = 0;
		};

		$.post(basePath + "itsm/itsmFaultType/commitFaultType.do",
		 		{"faultTypeForm":JSON.stringify(faultTypeFormData), "faultTypeId":faultTypeId},
				function(data){
					if(data.result == "success"){
						//重新刷新一下树数据
						var faultTypeTreeWinObj=window.parent.document.getElementById("itsmFaultTypeTree").contentWindow.$('#faultType_tree')
						faultTypeTreeWinObj.tree("reload");
						FW.success("保存成功");
						closeCurPage();
					}else {
						FW.error("保存失败");
					}
		  },"json");
	  
		$("#faultTypeForm").iForm("endEdit");  //关闭编辑，
	}
	/**
	*编辑
	**/
	function editFaultType(){
		$("#inPageTitle").html("编辑服务目录");
		var faultTypeFormData = $("#faultTypeForm").iForm("getVal");  //取表单值
		var parentTypeId = faultTypeFormData.parentId;
		if(parentTypeId==""){  //如果是根节点
			$("#faultTypeForm").iForm("beginEdit",["name","keywords","remarks"]); 
		}else if(parentTypeId==ftRootId){  //如果是一级服务目录，则显示 维护组
			$("#faultTypeForm").iForm("beginEdit",["name","keywords","remarks","defaultScore","principalGroup"]); 
		}else{
			$("#faultTypeForm").iForm("beginEdit"); 
			$("#faultTypeForm").iForm("hide",["principalGroup"]);  
		}
		
		$("#btn_save").show(); 
		$("#btn_edit").hide(); 
		FW.fixRoundButtons("#toolbar1");
	}
	/**
	*删除
	*/
	function deleteFaultType(){
		Notice.confirm("确定删除|确定删除此故障类型以及其子类型么？",function(){
			$.post(basePath + "itsm/itsmFaultType/deleteFaultType.do",
			 		{"faultTypeId":faultTypeId},
					function(data){
						if(data.result == "success"){
							FW.success("删除成功")
							//重新刷新一下树数据
							var faultTypeTreeWinObj=window.parent.document.getElementById("faultTypeTree").contentWindow.$('#faultType_tree');
							faultTypeTreeWinObj.tree("reload");
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
					<button id="btn_jp_new" type="button" class="btn btn-default" onclick="closeCurPage()">关闭</button>
				</div>
		    	<div class="btn-group btn-group-sm">
					<button id="btn_save" type="button" class="btn btn-success" onclick="saveFaultType()">保存</button>
					<button id="btn_edit" type="button" class="btn btn-default" onclick="editFaultType()">编辑</button>
				</div>
				<div id="deltetDiv" class="btn-group btn-group-sm">
					<button id="btn_delete" type="button" class="btn btn-default" onclick="deleteFaultType()">删除</button>
				</div>
		    </div>
		    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
		    <div id="pagination_1" class="toolbar-pager" bottompager="#bottomPager">        
		    </div>
		</div>
		
		<div  class="inner-title">
			<span id="inPageTitle">服务目录详情</span>
		</div>
		
		<div>
	    	 <form id="faultTypeForm" class="autoform"></form>
	    </div>
	</div>
		
	
</body>
</html>