<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.yudean.itc.util.FileUploadUtil"%>
<%@page import="com.yudean.itc.util.Constant"%>
<%@page import="com.yudean.itc.dto.sec.SecureUser"%>
<%
	SecureUser operator = (SecureUser) session
			.getAttribute(Constant.secUser);
	String valKey = FileUploadUtil.getValidateStr(operator,
			FileUploadUtil.DEL_OWNED);
	String sessId = request.getSession().getId();
%>
<!DOCTYPE html>
<html>
<head>

<title>合理化建议详情</title>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script type="text/javascript">
_useLoadingMask=true;
</script>
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script type="text/javascript"
	src="${basePath}js/workflow/workflow.js?ver=${iVersion}"></script>
<script type="text/javascript"
	src="${basePath}js/attendance/common/addTab.js?ver=${iVersion}"></script>
<script type="text/javascript"
	src="${basePath}js/attendance/common/pageDetail.js?ver=${iVersion}"></script>
<script type="text/javascript"
	src="${basePath}js/attendance/common/pageMode.js?ver=${iVersion}"></script>
<script type="text/javascript"
	src="${basePath}js/attendance/common/commonForm.js?ver=${iVersion}"></script>
<script type="text/javascript"
	src="${basePath}js/attendance/common/selectPersonWithStructure.js?ver=${iVersion}"></script>
<script type="text/javascript"
	src="${basePath}/itcui/ckeditor/ckeditor.js?ver=${iVersion}"></script>

<script type="text/javascript">
		 var g = {users:{}};
		 var ss = ${params.bean};
		 $(document).ready(function() {	
		  /*var $form=$("#form1");
		 $form.iForm('init',{"fields":rationalizationFields,options:{validate:true}});  */
		 PageDetail.afterChangeShow=function(){
		 	if ((ss.status=="草稿"||ss.status==null||ss.status=="提交申请")&&(PageDetail.objs.form.bean.curPerson==null&&Priv.secUser.userId!=null)){
		 		PageDetail.objs.form.obj.iForm("beginEdit",["rationalType","recomName","projectName","impCon","benCalc"]); 
		 	}else if(PageDetail.objs.form.bean.curPerson==Priv.secUser.userId||PageDetail.objs.form.bean.curPerson.match(Priv.secUser.userId)!=null){
		 		if (ss.status=="部门建议员审定"){
		 			PageDetail.objs.form.obj.iForm("beginEdit","isFile"); 
				}else if(ss.status=="对口专业评审小组审批"){
			 		PageDetail.objs.form.obj.iForm("beginEdit",["isFile2","counterPartType"]);
			 	}else if(ss.status=="业务相关部门执行"){
			 		PageDetail.objs.form.obj.iForm("beginEdit",["brief","majorDept"]);
			 	}else if(ss.status=="生产经营部专责初评"){
			 		PageDetail.objs.form.obj.iForm("beginEdit",["appraiSal","bonusSplit","impDept"]);
			 	}
		 	}
		 }
		 
		 PageDetail.afterLoadData=function(){
		 	var $form=$("#form1");
		 		PageDetail.objs.form.obj.iForm("hide",["rationalNo","userName","deptName","rationalType","recomName","projectName","impCon","benCalc","isFile","isFile2","counterPartType","brief","majorDept","appraiSal","bonusSplit","impDept"]);
		   if(PageDetail.isMode("create")){
		   		PageDetail.objs.form.obj.iForm("show",["rationalType","recomName","projectName","impCon","benCalc"]);
			}else{
				if (ss.status==null||ss.status=="草稿"){
		 			PageDetail.objs.form.obj.iForm("show",["rationalType","recomName","projectName","impCon","benCalc"]); 
		 		}else if(ss.status=="提交申请"){
		 			PageDetail.objs.form.obj.iForm("show",["rationalType","recomName","projectName","impCon","benCalc"]);
		 		}else if(ss.status=="部门建议员审定"){
			 		if(""==$("#form_baseinfo").iForm("getVal","isFile")&&PageDetail.objs.form.bean.curPerson.match(Priv.secUser.userId)!=null){
			 			$("#form_baseinfo").iForm("setVal",{"isFile":"Y"});
			 		}
		 			PageDetail.objs.form.obj.iForm("show",["rationalNo","userName","deptName","rationalType","recomName","projectName","impCon","benCalc","rationalNo","userName","deptName","isFile"]); 
				}else if(ss.status=="行政部门负责人审批"){
					PageDetail.objs.form.obj.iForm("show",["rationalNo","userName","deptName","rationalType","recomName","projectName","impCon","benCalc","rationalNo","userName","deptName"]);
				}else if(ss.status=="对口专业评审小组审批"){
					if(""==$("#form_baseinfo").iForm("getVal","isFile2")&&PageDetail.objs.form.bean.curPerson.match(Priv.secUser.userId)!=null){
			 			$("#form_baseinfo").iForm("setVal",{"isFile2":"Y"});
			 			PageDetail.objs.form.obj.iForm("show","counterPartType");
			 		}
					PageDetail.objs.form.obj.iForm("show",["rationalNo","userName","deptName","rationalType","recomName","projectName","impCon","benCalc","rationalNo","userName","deptName","isFile2"]);
				}else if(ss.status=="对口专业负责人审批"||ss.status=="业务相关部门指派"){
					PageDetail.objs.form.obj.iForm("show",["rationalNo","userName","deptName","rationalType","recomName","projectName","impCon","benCalc","rationalNo","userName","deptName"]);
				}else if(ss.status=="业务相关部门执行"){
					PageDetail.objs.form.obj.iForm("show",["rationalNo","userName","deptName","rationalType","recomName","projectName","impCon","benCalc","rationalNo","userName","deptName","brief","majorDept"]);
				}else if(ss.status=="生产经营部专责初评"){
					PageDetail.objs.form.obj.iForm("show",["rationalNo","userName","deptName","rationalType","recomName","projectName","impCon","benCalc","rationalNo","userName","deptName","brief","majorDept","appraiSal","bonusSplit","impDept"]);
				}else if(ss.status=="评审委员会评奖"||ss.status=="申请人确认"){
					PageDetail.objs.form.obj.iForm("show",["rationalNo","userName","deptName","rationalType","recomName","projectName","impCon","benCalc","rationalNo","userName","deptName","brief","majorDept","appraiSal","bonusSplit","impDept"]);
				}else if(ss.status=="工会归档"||ss.status=="作废"||ss.status=="已归档"){
					PageDetail.objs.form.obj.iForm("show",["rationalNo","userName","deptName","rationalType","recomName","projectName","impCon","benCalc","brief", "majorDept","appraiSal","bonusSplit","impDept"]);
				}			
			}
			
			//设置工作流的参数
			$.extend(true,PageDetail.objs.workFlow,{
				status:PageDetail.objs.form.bean.status,//工作流状态,required
				isApplicant:PageDetail.objs.form.bean.createUser==Priv.secUser.userId,//是否申请人,required
				isAudit:PageDetail.objs.form.bean.isAudit,//是否审批人,required
				isCommited:PageDetail.objs.form.bean.status!="草稿",//是否提交过了（是否启动了流程）
				taskId:PageDetail.objs.form.bean.taskId,//任务Id
	    		instanceId:PageDetail.objs.form.bean.instanceId//流程实例ID
			});
		};
		
		PageDetail.init({
			namePrefix:"合理化建议",
			url:{
				query:basePath+"attendance/training/getDetail.do",
				create:basePath+"attendance/training/insertTraining.do",//required
				update:basePath+"attendance/training/updateTraining.do",//required
				del:basePath+"attendance/Rationalization/deleteRationalization.do",//required
				invalid:basePath+"attendance/Rationalization/invalidRationalization.do",
				commit:basePath+"attendance/Rationalization/commit.do",
				save:basePath+"attendance/Rationalization/saveRat.do"
			},
			mode:"${mode}",//默认，可选view/create/edit
			form:{
				fields:[
					{id:"rationalId",type:"hidden"},	
		 			{title:"专业分类",id:"rationalType",rules : {required:true},
		 					type:"combobox",
		 					dataType : "enum",
							enumCat : "ATD_RATION_TYPE",
							options:{
								allowEmpty:true
							},
							render : function(id){
								$("#" + id).iCombo("setTxt","");
							},
							formatter:function(value){
									return FW.getEnumMap("【ecat_code】")[value];
	 						}
		 			 },
		 			{title : "建议人", id : "recomIds",type:"hidden"},
		 			{title : "建议人", id : "recomName", 
		 			rules : {required : true},
					options:{
						icon:"itcui_btn_mag",
							onClickIcon:function(val){
								selectParticipantToFrom("form_baseinfo");
							}
						}
		 			},
		 		    {title:"申请单号", id:"rationalNo",rules:{required:true}}, 
		 			{title:"项目名称",id:"projectName",wrapMdWidth:8,breakAll:true,rules:{required:true,maxChLength:parseInt(150*2/3)}},
					{title:"申请人", id:"userName",rules:{required:true},linebreak:true},
					{title:"申请部门", id:"deptName",rules:{required:true}},
		 			{title:"建议(改进)项目",id:"impCon",rules:{required:true},type:'textarea',
		 		 		wrapXsWidth:18,wrapMdWidth:8,height:95,helpMsg:"针对解决问题现状及实施方案、方法或措施 (可在附件中上传调查或原始材料,图纸等证明材料)",
		 		 		rules:{maxChLength:parseInt(3000*2/3)}, render:function(id){
							$("#"+id).attr("placeholder","针对解决问题现状及实施方案、方法或措施(可在附件中上传调查或原始材料,图纸等证明材料)");
						},linebreak:true
					},
					{title:"效益预测计算",
					id:"benCalc",
					type:'textarea',wrapXsWidth:12,wrapMdWidth:8,height:45,
					rules:{maxChLength:parseInt(300*2/3)}, render:function(id){
							$("#"+id).attr("placeholder","经济效益或社会效益");
					}, 
					linebreak:true 
					},
					{title:"是否通过",id:"isFile",type:"radio",data:[["Y","是",true],["N","否"]],rules:{required:true},linebreak:true},	
					/*部门建议人审定*/
					{title:"是否通过",id:"isFile2",type:"radio",data:[["Y","是",true],["N","否"]],rules:{required:true},linebreak:true}, 
					{title:"对口部门",id:"counterPartType", type: "combobox",linebreak:true, rules:{required:true},
				 		options:{
							url : basePath + "attendance/Rationalization/getDeptments.do",
							remoteLoadOn :"init",
							onChange:function(val){
			    				getBmfzrIds(val);
			    			}  
						}
					},
					{title:"实施简况",id:"brief", type:'textarea',rules:{required:true,maxChLength:parseInt(3000*2/3)},
						wrapXsWidth:12,wrapMdWidth:8,height:65, render:function(id){
							$("#"+id).attr("placeholder","开始、竣工时间耗用工料数");
					}, linebreak:true
					},
					{title:"主要实施部门 ",id:"majorDept",wrapMdWidth:8,rules:{required:true,maxChLength:parseInt(150*2/3)},linebreak:true},
				 	{title:"鉴定、评价、评奖或评分",id:"appraiSal", type:'textarea',
						wrapXsWidth:12,wrapMdWidth:8, rules:{required:true,maxChLength:parseInt(1500*2/3)},linebreak:true,
						options:{
							onChange:function(val){
								setPercent(val);
							}
						}
					},
					{title:"奖励分成    建议人(%)",id:"bonusSplit",render:function(id){
							$("#"+id).on("blur",function(){
								refreshVal("bonusSplit");
							});
						},rules:{required:true,range:[0,100]},
						linebreak:true},
					{title:"实施部门(%)", id:"impDept",render:function(id){
						$("#"+id).on("blur",function(){
							refreshVal("impDept");
						});
					} ,
					rules:{required:true,range:[0,100]}} 
				  ],//required
				  opts:{
					labelFixWidth:160
				},
				idField:"rationalId",//required
				nameField:"rationalNo",//required
				beanId:"${rationalId}",
				blankBean:{siteid:Priv.secUser.siteId,userName:Priv.secUser.userName,deptName:Priv.secUser.orgs&&Priv.secUser.orgs.length>0?Priv.secUser.orgs[0].name:""},
				bean:${params.bean}
			},
			withWorkFlow:true,
			workFlow:{
	    		flowDiagram:"atd_?_rationalization"//流程图
			},
			withAttach:true, 
			attach:{
				sessId:"<%=sessId%>",
				valKey:"<%=valKey%>",
				itemType:"ration"
			}
		});
		
		function refreshVal(type){
			if(ss.status=="生产经营部专责初评"){
				if("bonusSplit"==type){
					var bonusSplitValue = $("#form_baseinfo").iForm("getVal",type);
					if (bonusSplitValue.length>5) {
						$("#form_baseinfo").iForm("setVal",Math.round(bonusSplitValue*100)/100);
					}
					$("#form_baseinfo").iForm("setVal",{"impDept":parseFloat((100-bonusSplitValue).toFixed(2))});
				}else if("impDept"==type){
					var impDeptValue = $("#form_baseinfo").iForm("getVal",type);
					$("#form_baseinfo").iForm("setVal",{"bonusSplit":parseFloat((100-impDeptValue).toFixed(2))});
				}
			}
		}
		
		function getBmfzrIds(val){
			 $.post(basePath + "attendance/Rationalization/getBmfzrIds.do",{"orgId":val},
				 function(data){
				 if(data.length>0){
					 bmferId='';
					 for(var i=0;i<data.length;i++){
						 var result = data[i];
						 bmferId += result.Id+',';
					 }
				 }
			},"json");
 		};
 });


function audit(){
		var url = basePath + "workflow/process_inst/setVariables.do";
		var params = {};
		params['processInstId'] = ss.instanceId;
		params['businessId'] = ss.rationalId;
		var variables = [];
		if(ss.status=="部门建议员审定"){
			variables.push({'name':"isFile", 'value':$("#form_baseinfo").iForm("getVal","isFile")});
		}else if(ss.status=="对口专业评审小组审批"){
			variables.push({'name':"isFile2", 'value':$("#form_baseinfo").iForm("getVal","isFile2")},{'name':"majorOrgId",'value':$("#form_baseinfo").iForm("getVal","counterPartType")});
		}
		params['variables'] = FW.stringify(variables);
		$.post(url,params,function(data){
			if(data.result == 'ok'){
				PageDetail.toAudit();
			}
		});
}


function ShowFlow(){
		var url = basePath + "workflow/process_inst/setVariables.do";
		var params = {};
		params['processInstId'] = ss.instanceId;
		params['businessId'] = ss.rationalId;
		var variables = [];
		if(ss.status=="部门建议员审定"){
			variables.push({'name':"isFile", 'value':$("#form_baseinfo").iForm("getVal","isFile")});
		}else if(ss.status=="对口专业评审小组审批"){
			variables.push({'name':"isFile2", 'value':$("#form_baseinfo").iForm("getVal","isFile2")},{'name':"majorOrgId",'value':$("#form_baseinfo").iForm("getVal","counterPartType")});
		}
		params['variables'] = FW.stringify(variables);
		$.post(url,params,function(data){
			if(data.result == 'ok'){
				PageDetail.toShowFlow();
			}
		});
}

function toPrint(){
 	var src = fileExportPath + "preview?__report=report/TIMSS2_DPP_Rational.rptdesign&__format=pdf"+
						"&Id="+ss.rationalId+"&siteid="+Priv.secUser.siteId+
						"&author="+Priv.secUser.userId  
	var url = encodeURI(encodeURI(src));
	var title ="合理化建议详情单";
	FW.dialog("init",{
		src: url,
		btnOpts:[{
	            "name" : "关闭",
	            "float" : "right",
	            "style" : "btn-default",
	            "onclick" : function(){
	                _parent().$("#itcDlg").dialog("close");
	             }
	        }],
		dlgOpts:{ width:800, height:650, closed:false, title:title, modal:true }
	});
}
 
	
</script>
<style type="text/css">
.btn-garbage {
	cursor: pointer;
}

html {
	height: 95%
}

body {
	height: 100%
}
</style>
</head>

<body>
	<div class="toolbar-with-pager bbox">
		<div class="btn-toolbar" role="toolbar">
			<div class="btn-group btn-group-sm">
				<button type="button" class="btn-default btn" id="btnClose"
					onclick="closeTab()">关闭</button>
			</div>
			<div class="btn-group btn-group-sm" style="display:none">
				<button type="button" class="btn-default btn" id="btnBack"
					onclick="PageDetail.toBack()">取消</button>
			</div>
			<div class="btn-group btn-group-sm">
				<button type="button" class="btn-default btn" id="btnSave"
					onclick="PageDetail.toSave()">暂存</button>
				<button type="button" class="btn-default btn" id="btnCommit"
					onclick="PageDetail.toCommit()">提交</button>
			</div>
			<div class="btn-group btn-group-sm">
				<button type="button" class="btn-default btn priv" id="btnAudit"
					privilege="VIRTUAL-AUDIT" onclick="audit()">审批</button>
			</div>
			<div class="btn-group btn-group-sm">
				<button type="button" class="btn-default btn priv" id="btnInvalid"
					privilege="VIRTUAL-INVALID" onclick="PageDetail.toInvalid()">作废</button>
			</div>
			<div class="btn-group btn-group-sm">
				<button type="button" class="btn-default btn priv" id="btnDel"
					privilege="VIRTUAL-DELETE" onclick="PageDetail.toDelete()">删除</button>
			</div>
			<!-- <div class="btn-group btn-group-sm">
				<button type="button" class="btn-default btn" id="btnPrint"
					onclick="PageDetail.toPrint()">打印</button>
			</div> -->
			<div class="btn-group btn-group-sm">
				<button type="button" class="btn-default btn" id="btnPrint"
					onclick="toPrint()">打印</button>
			</div>
			<div class="btn-group btn-group-sm">
				<button type="button" class="btn-default btn" id="btnShowFlow"
					onclick="ShowFlow()">审批信息</button>
			</div>
		</div>
	</div>

	<div class="inner-title" id="pageTitle">新建合理化建议</div>
	<form id="form_baseinfo" class="margin-form-title margin-form-foldable">

	</form>

	<!--  <form id="form1"  class="margin-form-title margin-form-foldable"></form>  -->

	<div class="margin-group"></div>

	<!-- 异常明细
	<div grouptitle="培训人员列表 " id="itemDiv">
		<div class="margin-title-table">
			<form id="itemForm">
				<table id="itemDatagrid" class="eu-datagrid"></table>
			</form>
		</div>
		<div class="btn-toolbar margin-foldable-button" role="toolbar"
			id="addItemToolbar">
			<!-- <div class="btn-group btn-group-xs" id="addItemDiv">
				<button type="button" class="btn btn-success" id="addItemBtn" onclick="PageDetail.toAddItem()">添加人员</button>
			</div>  
		</div>
	</div>
	 -->
	<div class="margin-group"></div>

	<!-- 附件 -->
	<div id="attachDiv" grouptitle="附件">
		<form id="attachForm" style="width:100%"></form>
	</div>
</body>
</html>
