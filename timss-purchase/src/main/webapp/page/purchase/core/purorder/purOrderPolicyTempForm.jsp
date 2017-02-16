<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	//获取List页面传送过来的参数，由于没有使用el表达式，所以只能通过java代码获取
	String type = request.getParameter("type")==null?"":String.valueOf(request.getParameter("type"));
	String ppt = request.getAttribute("ppt")==null?"":String.valueOf(request.getAttribute("ppt"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en"" style="width:100%;height:100%">
<head>
<title>采购合同标准条款</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
	var ppt = '<%=ppt%>' ;
	var formfield = [
		{title : "排序", id : "sort",rules : {required:true}},
		{title : "条款内容", id : "policyContent",type : "textarea",linebreak:true,wrapXsWidth:12, wrapMdWidth:8, rules : {required:true}},
		{title : "条款id", id : "policyId", type:"hidden"},
		{title : "站点id", id : "siteid", type:"hidden"}
    ];
	//初始化界面
	$(document).ready(function() {
		var loaddata = ""==ppt?false:JSON.parse(ppt);
		//按钮初始化
		$("#btn-close").click(function(){
			pageClose();
		});
		$("#btn-save").click(function(){
			save();
		});
		$("#btn-del").click(function(){
			//删除按钮的方法
			var url = basePath+ "purchase/purorder/purOrderPolicyTempDelete.do";
			var policyId = $("#f_policyId").val();
			$.post(url,{policyId:policyId},function(result){
				if(result.data.success){
					FW.success("删除成功");
					pageClose();
				}else{
					FW.error("删除失败");
				}
			});
		});
		$("#btn-dtl").click(function(){
			$("#btn-dtl").hide();
			if(Priv.vPriv["policyTemp_save"]){
				$("#btn-save").show();
				$("#autoform").iForm("beginEdit");
			}
			if(Priv.vPriv["policyTemp_del"]){
				$("#btn-del").show();
			}
			FW.fixToolbar("#toolbar1");	
		});
		//权限初始化
		Priv.map("privMapping.policyTemp_save","policyTemp_save");
		Priv.map("privMapping.policyTemp_del","policyTemp_del");
		Priv.apply();
		//新建时，显示保存，隐藏编辑、删除
		if(loaddata==false){
			$("#btn-save").show();
			$("#btn-dtl").hide();
			$("#btn-del").hide();
		}else{
			$("#btn-save").hide();
		}	
		FW.fixToolbar("#toolbar1");
		//表单初始化
		$("#autoform").ITC_Form({validate:true,fixLabelWidth:true},formfield);
		//编辑初始化时表单是只读的	
		if(loaddata!=false){
			$("#autoform").iForm("endEdit");
		}
		$("#autoform").iForm("setVal",loaddata);
		
	});
	//关闭页面
	function pageClose(){
		FW.deleteTabById(FW.getCurrentTabId());
	}
	//保存表单
	function save(){
		var data = $("#autoform").iForm("getVal");
		$.post(basePath+"purchase/purorder/savePurOrderPolicyTemp.do",{policyTemp:FW.stringify(data)},function(result){
			if(result.data.success){
				FW.success("保存成功");
				pageClose();
			}else{
				FW.error("保存失败");
			}
		});
	}
</script>
<style type="text/css">.btn-garbage{cursor:pointer;}</style>
</head>
<body>
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
		<div class="btn-toolbar" role="toolbar" id="toolbar1">
			<div class="btn-group btn-group-sm">
		        <button type="button" class="btn btn-default" id="btn-close">关闭</button>
		    </div>
		    <div class="btn-group btn-group-sm">
		    	<button type="button" class="btn btn-default priv" id="btn-dtl" privilege="policyTemp_save">编辑</button>
		    	<button type="button" class="btn btn-default priv" id="btn-save" privilege="policyTemp_save" >保存</button>
		    </div>
		    <div class="btn-group btn-group-sm">
		    	<button type="button" class="btn btn-default priv" id="btn-del" privilege="policyTemp_del" >删除</button>
		    </div>
		</div>
	</div>
	<div class="inner-title" id="pageTitle">采购合同标准条款</div>
	<form id="autoform" class="margin-form-title margin-form-foldable autoform"></form>
</body>
</html>