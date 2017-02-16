<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.yudean.itc.util.FileUploadUtil"%>
<%@page import="com.yudean.itc.util.Constant"%>
<%@page import="com.yudean.itc.dto.sec.SecureUser"%>
<%
	SecureUser operator = (SecureUser)session.getAttribute(Constant.secUser);
	String valKey = FileUploadUtil.getValidateStr(operator, FileUploadUtil.DEL_OWNED);
	String sessId = request.getSession().getId();
%>
<!DOCTYPE html>
<html>
<head>

<title>培训申请详情</title>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script type="text/javascript">
_useLoadingMask=true;
</script>
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script type="text/javascript" src="${basePath}js/workflow/workflow.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/attendance/common/addTab.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/attendance/common/pageDetail.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/attendance/common/pageMode.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/attendance/common/commonForm.js?ver=${iVersion}"></script>
<script type="text/javascript">
	$(document).ready(function() {	
		PageDetail.afterLoadData=function(){
			if(PageDetail.isMode("create")){
				PageDetail.objs.form.obj.iForm("hide",["createdate","trainingNo"]);
			}else{
				PageDetail.objs.form.obj.iForm("show",["createdate","trainingNo"]);
			}
			
			PageDetail.objs.form.obj.iForm("hide",["grades","certificate","isFile","fileList"]);
			if(PageDetail.objs.form.bean.status=="申请人添加培训结果材料" || PageDetail.objs.form.bean.status=="培训专责确认归档" 
				|| PageDetail.objs.form.bean.status=="已归档"){
				PageDetail.objs.form.obj.iForm("show",["grades","certificate","isFile","fileList"]);
			}
			
			//设置工作流的参数
			$.extend(true,PageDetail.objs.workFlow,{
				status:PageDetail.objs.form.bean.status,//工作流状态,required
				isApplicant:PageDetail.objs.form.bean.createuser==Priv.secUser.userId,//是否申请人,required
				isAudit:PageDetail.objs.form.bean.isAudit,//是否审批人,required
				isCommited:PageDetail.objs.form.bean.status!="草稿",//是否提交过了（是否启动了流程）
				taskId:PageDetail.objs.form.bean.taskId,//任务Id
	    		instanceId:PageDetail.objs.form.bean.instanceId//流程实例ID
			});
		};
		PageDetail.afterChangeShow=function(){//可编辑
			if(PageDetail.objs.form.bean.status=="申请人添加培训结果材料"){
				if(PageDetail.objs.workFlow.isAudit){
				PageDetail.objs.form.obj.iForm("beginEdit",["grades","certificate","isFile","fileList"]);
				PageDetail.objs.attach.isEndEdit=false;
				}
			}
		};
		//对这个状态的附件进行校验
		PageDetail.toValid=function(){
			return PageDetail.objs.form.obj.valid()&&(PageDetail.objs.withItem?PageDetail.toValidItem():true)&&
				(PageDetail.objs.form.bean.status=="申请人添加培训结果材料"?PageDetail.toValidAttach():true);
		};
		PageDetail.toValidAttach=function(){//选择是，要校验附件
			if("Y"==PageDetail.objs.form.obj.iForm("getVal")["isFile"] &&
			    $("#"+PageDetail.objs.attach.formId).iForm("getVal").attachment.length==0){
				FW.error( "请添加评估表至附件");
				return false;
			}else{
				return $("#"+PageDetail.objs.item.formId).valid();
			}
		};
		//添加人员做成弹窗
		PageDetail.toAddItem=function(isToUnify){
			var src = basePath + "attendance/training/insertItemToPage.do?isToUnify="+(isToUnify?"1":"0");
			var btnOpts = [{
				"name" : "取消",
				"float" : "right",
				"style" : "btn-default",
				"onclick" : function() {
					return true;
				}
			},{
				"name" : "确定",
				"float" : "right",
				"style" : "btn-success",
				"onclick" : function() {				
					var conWin = _parent().window.document.getElementById("itcDlgContent").contentWindow;
					if (!conWin.valid()) {
						return false;
					}
					var formdata = conWin.getFormOriginalData("itemForm");
					$("#itemDatagrid").datagrid("appendRow",formdata);
					PageDetail.toEditItem(true,true);
					return true;
				}
			} ];
			FW.dialog("init", {
				"src" : src,
				"dlgOpts" : {
					width : 400,
					height : 180,
					closed : false,
					title : "添加人员",
					modal : true
				},
				"btnOpts" : btnOpts
			});
		};
		PageDetail.init({
			namePrefix:"培训申请",
			url:{
				query:basePath+"attendance/training/getDetail.do",
				create:basePath+"attendance/training/insertTraining.do",//required
				update:basePath+"attendance/training/updateTraining.do",//required
				del:basePath+"attendance/training/deleteTraining.do",//required
				invalid:basePath+"attendance/training/invalidTraining.do",
				commit:basePath+"attendance/training/commit.do",
				save:basePath+"attendance/training/saveTraining.do"
			},
			mode:"${mode}",//默认，可选view/create/edit
			form:{
				fields:[
					{title:"ID",id:"trainingId", type:"hidden"},
					{title:"申请单号",id :"trainingNo",type:"label"},
					{title:"申请人",id:"userName",type:"label",formatter:function(val){
						return val+"/"+(PageDetail.objs.form.bean["deptName"]?PageDetail.objs.form.bean["deptName"]:"");
					}},
					{title:"申请时间",id:"createdate",type:"label",formatter:function(val){
						return FW.long2time(val);
					}},
					{title:"申请理由",id:"reason",type:'textarea',wrapXsWidth:12,wrapMdWidth:8,height:42,rules:{required:true,maxChLength:parseInt(100*2/3)},linebreak:true},
					{title:"开始日期",id:"startDate",type:"date",dataType:"date",rules:{required:true},linebreak:true},
					{title:"结束日期",id:"endDate",type:"date",dataType:"date",rules:{
						required:true,
						greaterEqualThan : '#f_startDate'
					},messages : {
						greaterEqualThan : "结束日期必须大于开始日期"
					}},
					{title:"培训类别",id:"trainingType",type:"combobox",rules : {required:true},
						dataType : "enum",
						enumCat : "ATD_TRAINING_TYPE",
						options:{
							allowEmpty:true
						},
						render : function(id){
							$("#" + id).iCombo("setTxt","");
						}
					},
					{title:"培训学时(个/人)",id:"hour",rules:{required:true,number:true,min:0}},
					{title:"培训费用(元/人)",id:"cost",rules:{required:true,number:true,min:0}},
	   				{title:"培训内容",id:"content",linebreak:true,type:'textarea',
	   					wrapXsWidth:12,wrapMdWidth:8,height:75,rules:{required:true,maxChLength:parseInt(2000*2/3)}
	   				},
	   				{title:"培训效果评价方式",id:"evaluateType",type:"combobox",linebreak:true,rules : {required:true},
	   					dataType : "enum",
						enumCat : "ATD_TRAINING_EVALUATE_TYPE",
						options:{
							allowEmpty:true
						},
						render : function(id){
							$("#" + id).iCombo("setTxt","");
						}
					},
					{title:"主办单位",id:"sponsor",rules:{maxChLength:parseInt(150*2/3)}},
					{title:"地点",id:"address",rules:{maxChLength:parseInt(150*2/3)}},
					{title:"培训成绩",id:"grades",rules:{maxChLength:20}},
					{title:"培训证书",id:"certificate",rules:{maxChLength:parseInt(100*2/3)}},
					{title:"是否附培训评估表",id:"isFile",type:"radio",data:[['Y','是'],['N','否']]},
					{title:"资料清单",id:"fileList", type:'textarea',height:48,
						linebreak:true,wrapXsWidth:18,wrapMdWidth:12,rules:{required:true,maxChLength:1000}}
				],//required
				idField:"trainingId",//required
				nameField:"trainingNo",//required
				beanId:"${trainingId}",
				blankBean:{siteid:Priv.secUser.siteId,userName:Priv.secUser.userName,deptName:Priv.secUser.orgs&&Priv.secUser.orgs.length>0?Priv.secUser.orgs[0].name:""},
				bean:${params.bean}
			},
			withWorkFlow:true,
			workFlow:{
	    		flowDiagram:"atd_?_training"//流程图
			},
			withAttach:true,
			attach:{
				sessId:"<%=sessId%>",
				valKey:"<%=valKey%>"
			},
			withItem:true,
			item:{
				fields:[
					{title:"",field:"userId",hidden:true},
					{title:"姓名",field:"userName",width:100,fixed:true,rules : {required : true,maxChLength:parseInt(50*2/3)}},
					{title:"学历",field:"education",width:100,fixed:true,
						editor:{
					        type:"text",
							"options" : {
								rules : {
									maxChLength:parseInt(150*2/3)
								}
							}
						}
					},
					{title:"现从事工作或岗位",field:"job",width:100,
						editor:{
					        type:"text",
							"options" : {
								rules : {
									maxChLength:parseInt(150*2/3)
								}
							}
						}
					},
					{field:'trainingItemId',title:' ',width:50,fixed:true,
						 formatter:function(value,row,index){
		  				     return '<img src="'+basePath+'img/attendance/btn_garbage.gif" width="16" height="16" style="cursor:pointer" />';
		  				}	
					}  
				],//required
				idField:"trainingItemId",//required
				delField:"trainingItemId",//required
				blankItem:{
					userId:"",
					userName:"",
					education:"",
					job:"",
					trainingItemId:""
				}//required
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
			<div class="btn-group btn-group-sm">
	            <button type="button" class="btn-default btn" id="btnClose" onclick="closeTab()">关闭</button>
	        </div>
			<div class="btn-group btn-group-sm" style="display:none">
				<button type="button" class="btn-default btn" id="btnBack" onclick="PageDetail.toBack()">取消</button>
			</div>
			<div class="btn-group btn-group-sm">
				<button type="button" class="btn-default btn" id="btnSave" onclick="PageDetail.toSave()">暂存</button>
				<button type="button" class="btn-default btn" id="btnCommit" onclick="PageDetail.toCommit()">提交</button>
			</div>
			<div class="btn-group btn-group-sm">
				<button type="button" class="btn-default btn priv" id="btnAudit" privilege="VIRTUAL-AUDIT" onclick="PageDetail.toAudit()">审批</button>
			</div>
			<div class="btn-group btn-group-sm">
				<button type="button" class="btn-default btn priv" id="btnInvalid" privilege="VIRTUAL-INVALID" onclick="PageDetail.toInvalid()">作废</button>
			</div>
			<div class="btn-group btn-group-sm">
				<button type="button" class="btn-default btn priv" id="btnDel" privilege="VIRTUAL-DELETE" onclick="PageDetail.toDelete()">删除</button>
			</div>
			<div class="btn-group btn-group-sm">
				<button type="button" class="btn-default btn" id="btnPrint" onclick="PageDetail.toPrint()">打印</button>
			</div>
	        <div class="btn-group btn-group-sm">
	        	<button type="button" class="btn-default btn" id="btnShowFlow" onclick="PageDetail.toShowFlow()">审批信息</button>
	        </div>	
		</div>
	</div>
	
	<div class="inner-title" id="pageTitle">
		此处写页标题
	</div>
	<form id="form_baseinfo" class="margin-form-title margin-form-foldable">

	</form>
	
	<div class="margin-group"></div>
	
	<!-- 异常明细 -->
	<div grouptitle="培训人员列表 " id="itemDiv">
		<div class="margin-title-table">
			<form id="itemForm">
				<table id="itemDatagrid" class="eu-datagrid"></table>
			</form>
		</div>
		<div class="btn-toolbar margin-foldable-button" role="toolbar" id="addItemToolbar">
			<div class="btn-group btn-group-xs" id="addItemDiv">
				<button type="button" class="btn btn-success" id="addItemBtn" onclick="PageDetail.toAddItem()">添加人员</button>
			</div>
		</div>
	</div>
	
	<div class="margin-group"></div>
	
	<!-- 附件 -->
	<div id="attachDiv" grouptitle="附件">
	    <form id="attachForm" style="width:100%">
	    </form>
	</div>
</body>
</html>
