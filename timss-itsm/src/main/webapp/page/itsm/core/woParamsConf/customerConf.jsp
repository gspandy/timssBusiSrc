<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html style="height:99%">
<head>
<title>客户配置</title>
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
	var customerConfId = '<%=request.getParameter("customerConfId")%>';  
	var loginUserId = ItcMvcService.user.getUserId();
	var siteId = ItcMvcService.getUser().siteId; 
</script>
<script>

	/* 表单字段定义  */
	var fields = [
			{title : "ID", id : "id",type : "hidden"},
		    {title : "客户工号", id : "customerCode", rules : {required:true}},
		    {title : "客户姓名", id : "customerName", rules : {required:true}},
		    {title : "客户公司", id : "customerCom"},
		    {title : "服务级别",id : "initPriorityId",type : "combobox",
				options : {
		    		url : basePath + "itsm/woParamsConf/comboboxPriority.do",
		    	    remoteLoadOn : "init"
		    	}			
			}];
		    

//根据工号输入提示
fields[1].render = function(id){
	$("#"+id).iHint('init', getiHintParams('searchByUserId'));
}
//根据姓名输入提示
fields[2].render = function(id){
	$("#"+id).iHint('init', getiHintParams('searchByUserName'));
}
function getiHintParams(searchStyle){
	var iHintParams ={
			datasource : basePath + "itsm/woUtil/userMultiSearch.do?searchStyle="+searchStyle,
			forceParse : function(){},
			clickEvent : function(id,name,rowdata) {
				if(rowdata != null){
					if(rowdata.name!=null){
						$("#f_customerName").val(rowdata.name);
					}
					if(rowdata.id!=null){
						$("#f_customerCode").val(rowdata.id);
						judgeRepeat(rowdata.id);
					}
					if(rowdata.comName!=null){
						$("#f_customerCom").val(rowdata.comName);
					}else{
						$("#f_customerCom").val("");
					}
					
				}
			},
			"highlight":true,
			"formatter" : function(id,name,rowdata){
				var showText = "";
				switch(searchStyle){
					case "searchByUserId": showText=rowdata.name + " / " + rowdata.id;break;
					case "searchByUserName":showText=rowdata.name;break;
					default:break;
				}
				return showText;
			}

		};
	return iHintParams;
}
//判断是否重复
function judgeRepeat(customerCode){
	$.post(basePath + "itsm/woParamsConf/judgeRepeatCustomerConf.do",
		 		{"customerCode":customerCode},
				function(data){
					if(data.result == "fail"){
						FW.error("此客户已经配置了默认服务级别");
						$("#f_customerName").val("");
						$("#f_customerCode").val("");
						$("#f_customerCom").val("");
					}
		  },"json"); 
}		
    
	$(document).ready(function() {
		 $("#customerConfForm").iForm("init",{"fields":fields,"options":{validate:true}});
		  
		 if(customerConfId == null || customerConfId == 'null'){
			 	$("#inPageTitle").html("新建客户配置");
				$("#customerConfForm").iForm("hide","priorityCode");
				$("#btn_edit").hide(); 
				$("#btn_delete").hide(); 
				FW.fixRoundButtons("#toolbar1");
			}else{  //查看工作方案
				$.post(basePath + "itsm/woParamsConf/queryCustomerConfDataById.do",{"customerConfId":customerConfId},
					function(customerConfData){
						$("#btn_save").hide(); 
						FW.fixRoundButtons("#toolbar1");
						var customerConfFormData = JSON.parse( customerConfData.baseData );
						$.ajaxSetup({"async":false});
						$("#customerConfForm").iForm("setVal",customerConfFormData);
						setTimeout(function(){
							$("#customerConfForm").iForm("endEdit");
						},200);
						
					},"json");
			}
	});
	
	
	/**
	*保存
	**/
	function saveCustomerConf(){
		/**表单验证*/
		if(!$("#customerConfForm").valid()){
			return ;
		}
		 
		var customerconfForm = $("#customerConfForm").iForm("getVal");
		var id = customerconfForm.id;
		
	 	$.post(basePath + "itsm/woParamsConf/commitCustomerConf.do",
		 		{"customerConfForm":FW.stringify(customerconfForm),"id":id},
				function(data){
					if(data.result == "success"){
						FW.success("保存成功");
						closeCurPage();
					}else {
						FW.error("保存失败");
					}
		  },"json"); 
		  $("#customerConfForm").iForm("endEdit");  //关闭编辑，
	}
	


	/**
	*编辑
	**/
	function editCustomerConf(){
		$("#inPageTitle").html("编辑客户配置");
		$("#customerConfForm").iForm("beginEdit",["initPriorityId"]); 
		$("#btn_save").show(); 
		$("#btn_edit").hide(); 
		FW.fixRoundButtons("#toolbar1");
	}
	/**
	* 删除服务级别
	**/
	function deleteCustomerConf(){
		Notice.confirm("确定删除|确定删除此 条客服配置么？",function(){
			$.post(basePath + "itsm/woParamsConf/deleteCustomerConf.do",
			 		{"id":customerConfId},
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
		        	<button id="btn_save" type="button" class="btn btn-success" onclick="saveCustomerConf()">保存</button>
		        	<button id="btn_edit" type="button" class="btn btn-default" onclick="editCustomerConf()">编辑</button>
		        </div>
		         <div class="btn-group btn-group-sm">
		        	<button id="btn_delete" type="button" class="btn btn-default" onclick="deleteCustomerConf()">删除</button>
		        </div>
		    </div>
		</div>
		<div  class="inner-title">
			<span id="inPageTitle">客户配置详情</span>
		</div>
		<div>
	    	 <form id="customerConfForm" class="autoform"></form>
	    </div>
		
	</div>
		
</body>
</html>