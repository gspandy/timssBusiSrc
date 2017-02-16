<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>

<title>质检日志详情</title>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script type="text/javascript">
_useLoadingMask=true;
</script>
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script type="text/javascript" src="${basePath}js/operation/common/addTab.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/operation/common/pageDetail.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/operation/common/pageMode.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/operation/common/commonForm.js?ver=${iVersion}"></script>
<script type="text/javascript">
	$(document).ready(function() {	
		PageDetail.afterLoadData=function(){
			if(PageDetail.isMode("create")){
				PageDetail.objs.form.obj.iForm("hide","createdate");
			}else{
				PageDetail.objs.form.obj.iForm("show","createdate");
			}
		};
		
		PageDetail.init({
			namePrefix:"质检日志",
			url:{
				query:basePath+"operation/qualityTest/getDetail.do",
				create:basePath+"operation/qualityTest/insertQualityTest.do",//required
				update:basePath+"operation/qualityTest/updateQualityTest.do",//required
				del:basePath+"operation/qualityTest/deleteQualityTest.do"//required
			},
			mode:"${mode}",//默认，可选view/create/edit
			form:{
				fields:[
					{title:"ID",id:"qtId", type:"hidden"},
					{title:"部门",id:"deptName", type:"hidden"},
					{title:"记录人",id:"userName",type:"label",formatter:function(val){
						return val+"/"+PageDetail.objs.form.bean["deptName"];
					}},
	 				{title:"质检日期",id:"qtDate",type:"date",dataType:"date",rules:{required:true},linebreak:true},
	   				{title:"质检情况",id:"content",linebreak:true,type:'textarea',
	   					wrapXsWidth:12,wrapMdWidth:8,height:240,rules:{required:true,maxChLength:parseInt(1500*2/3)}
	   				},
	   				{title:"记录时间",id:"createdate",type:"label",linebreak:true,formatter:function(val){
						return FW.long2time(val);
					}}
				],//required
				idField:"qtId",//required
				nameField:"content",//required
				beanId:"${qtId}",
				blankBean:{siteid:Priv.secUser.siteId,userName:Priv.secUser.userName,deptName:Priv.secUser.orgs&&Priv.secUser.orgs.length>0?Priv.secUser.orgs[0].name:""},
				bean:${params.bean}
			}
		});
	});
	
</script>
<style type="text/css">
.btn-garbage{
cursor:pointer;
}
html{
	height:95%
}
body{
	height:100%
}
</style>
</head>

<body>
	<div class="toolbar-with-pager bbox">
		<div class="btn-toolbar" role="toolbar">
			<div class="btn-group btn-group-sm" id="closeButtonDiv">
	            <button type="button" class="btn btn-default" onclick="closeTab()">关闭</button>
	        </div>
			<div class="btn-group btn-group-sm" id="btnBack">
				<button class="btn-default btn" onclick="PageDetail.toBack()">取消</button>
			</div>
			<div class="btn-group btn-group-sm" id="btnEdit">
				<button class="btn-default btn priv" privilege="VIRTUAL-EDIT" onclick="PageDetail.toEdit()">编辑</button>
			</div>
			<div class="btn-group btn-group-sm" id="btnSave" style="display:none">
				<button class="btn-success btn" onclick="PageDetail.updateBean()">保存</button>
			</div>
			<div class="btn-group btn-group-sm" id="btnCreate" style="display:none">
				<button class="btn-success btn" onclick="PageDetail.createBean()">保存</button>
			</div>
			<div class="btn-group btn-group-sm" id="btnDel">
				<button class="btn-default btn priv" privilege="VIRTUAL-DELETE" onclick="PageDetail.toDelete()">删除</button>
			</div>	
		</div>
	</div>
	<!-- <div style="clear:both"></div> -->
	
	<div class="inner-title" id="pageTitle">
		此处写页标题
	</div>
	<form id="form_baseinfo" class="margin-form-title margin-form-foldable">

	</form>
	
	<div class="margin-group"></div>
</body>
</html>
