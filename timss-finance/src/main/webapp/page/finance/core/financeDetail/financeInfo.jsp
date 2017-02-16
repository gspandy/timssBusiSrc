<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.yudean.itc.util.FileUploadUtil"%>
<%@page import="com.yudean.itc.util.Constant"%>
<%@page import="com.yudean.itc.dto.sec.SecureUser"%>

<%
	String sessId = request.getSession().getId();
	SecureUser operator = (SecureUser) session
			.getAttribute(Constant.secUser);
	String valKey = FileUploadUtil.getValidateStr(operator,
			FileUploadUtil.DEL_OWNED);
	String curTaskInputInfo = request.getAttribute("curTaskInputInfo")==null?"":String.valueOf(request.getAttribute("curTaskInputInfo"));
%>

<!DOCTYPE html>
<html>
	<head>
		<title>报销详情</title>
		<script type="text/javascript">
			var uploadIds=""; //上传附件编号
			var finNameEn = "${finNameEn}"; //如"travelcost"
			var finNameCn = "${finNameCn}"; //如"差旅费"
			var finTypeEn = "${finTypeEn}"; //如"only"
			var finTypeCn = "${finTypeCn}"; //如"自己"
			var applyId = "${applyId}";
			var allowanceType = "${allowanceType}"
			var applyName = '';
			var apply_budget ='';
			var finFullNameEn = finNameEn + finTypeEn; //如"travelcostonly"
			var finFullNameCn = finNameCn + finTypeCn; //如"差旅费自己"
			var oprModeEn = "${oprModeEn}"; //add或edit
			var oprModeCn = "${oprModeCn}"; //新增或编辑
			var trainAllowancePerDay="${trainAllowancePerDay}";//培训费每日补贴金额
			var allowancePerDay="${allowancePerDay}"; //普通每日补贴金额
			var businessAllowancePerDay ="${businessAllowancePerDay}";  //商务每日补贴金额
			var defKey = ""; //流程定义Key
			var curTaskInputInfo = '<%=curTaskInputInfo%>'; //从工作流配置获取当前流程节点输入信息

			var finPageConfData = ${pageConfData};//页面配置数据信息
			//获取报销明细信息中的一行
			var beneficiary = "${beneficiary}";
			var beneficiaryid = "${beneficiaryid}";
			var description = "${description}";
			var join_boss = "${join_boss}";
			var join_bossid = "${join_bossid}";
			var join_nbr = "${join_nbr}";
			var strdate = +"${strdate}";
			var enddate = +"${enddate}";
		
			var isApprover="false"; //是否为审批者,默认为false
			var checkApprover="${checkApprover}";
			var loginRoleIds="${loginRoleIds}";
			var userId="${userId}";
			var usrName="${userName}";
			var fid="${financeMain.fid}";
			var uploadids='${uploadFiles}';
	
			var creatorname="";  //填单人名
			var createid ="";   //填单人ID
			var formDay="";
			var editDatagrid=""; //是否可以编辑表格
			var taskId="";
			var uploadFiles;
			var flagItem="";
			
			if( oprModeEn == "add" ) {
				creatorname="${creatorname}";
				createid="${createid}";
				formDay="${nowdate}";
				editDatagrid="true";
				taskId="";
				uploadFiles = ${uploadFiles};
			} else if( oprModeEn == "edit" ) {
				creatorname="${financeMain.creatorname}";
				createid = "${financeMain.createid}";
				applyId = "${financeMain.applyId}";
				applyName= "${financeMain.applyName}";
				apply_budget= "${financeMain.budget}";
				formDay="${createdate}";
				editDatagrid="false";
				taskId = "${taskId}";
				pid = "${pid}";
				uploadFiles = ${uploadFiles};
				var finabc = "${financeMain}";
				flagItem="${financeMain.flag_item}";
				
				
			}
			
			var bussinessStatus="${financeMain.status}";
			var fname="${financeMain.fname}";
			var total_amount="${financeMain.total_amount}";
			
			var processInstId = "${pid}";
			var fid = "${financeMain.fid}";
			//TODO 貌似可以删掉flowName
			var flowName = "finance_itc_" + finNameEn + finTypeEn;
	
		</script>
	
		<!-- 公共部分 -->
		<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
		<script type="text/javascript" src="${basePath}js/workflow/workflow.js?ver=${iVersion}"></script>
		
		<!-- 私有部分 -->
		<script type="text/javascript" src="${basePath}js/finance/common/eventTab.js?ver=${iVersion}"></script>
		<script type="text/javascript" src="${basePath}js/finance/common/flowUtilJS.js?ver=${iVersion}"></script>
		<script type="text/javascript" src="${basePath}js/finance/common/financeUtilJS.js?ver=${iVersion}"></script>
		<script type="text/javascript" src="${basePath}js/finance/core/financeInfo.js?ver=${iVersion}"></script>
		<script type="text/javascript" src="${basePath}js/finance/core/financeCommon.js?ver=${iVersion}"></script>
		<script type="text/javascript" src="${basePath}js/finance/core/financeBaseField.js?ver=${iVersion}"></script>
		<script type="text/javascript" src="${basePath}js/finance/core/financeDetail.js?ver=${iVersion}"></script>
		<script type="text/javascript" src="${basePath}js/finance/common/constant.js?ver=${iVersion}"></script>

		<script type="text/javascript">
			var loginUserId = ItcMvcService.user.getUserId();
			var siteId = ItcMvcService.getUser().siteId;
			var pri_postOprUrl = basePath + "finance/financeInfoController/insertFinanceInfo.do";

			$(document).ready(function() {
				getDefKeyByfinTypeEn(finTypeEn);  //获取流程定义KEY
				initPrintBtn(); //初始化打印按钮事件
				showForm(); //显示表单
				createdg(); //显示表格
				uploadform(); //显示附件
				fixPageStyle();
				
				//showFinTypeTabSelStyle(finTypeEn); //显示报销类型Tab选择样式
				
				if( oprModeEn == "add" ) {
					hideFinType2(); //屏蔽补充医疗保险、家属医药费、备用金、探亲路费的建单和审批功能
				}else if( oprModeEn == "edit" ) {
					authInit2(); //权限初始
				}
				
				//移动到js的authInit2()中
				//FW.privilegeOperation("finance_showAccType", "autoform"); //加载记账类型可视权限
				FW.set( "creatorname", creatorname ); //全局设置填单人
				//$("#autoform").iForm("show", "accType");
				
				FW.fixRoundButtons("#btn_toolbar"); //修复工具栏按钮的圆角问题
				FinBtnPriv.init();
			});
			//显示附件
		function uploadform() {
			$("#uploadform").iForm('init', {
				"fields" : [
		        	{
		        		id:"field3", 
		        		title:" ",
		        		type:"fileupload",
		        		linebreak:true,
		        		wrapXsWidth:12,
		        		wrapMdWidth:12,
		        		options:{
		        		    "uploader" : basePath+"upload?method=uploadFile&jsessionid=<%=sessId%>",
		        		    "delFileUrl" : basePath+"upload?method=delFile&key=<%=valKey%>",
		        			"downloadFileUrl" : basePath + "upload?method=downloadFile",
		        			"swf" : basePath + "js/finance/common/uploadify.swf",
		        			//"fileSizeLimit" : 10 * 1024,
		        			//"initFiles" : ${uploadFiles},
		        			"initFiles" : uploadFiles,
		        			"delFileAfterPost" : true
		        		}
		        	}
		        ],
				"options" : {
					"labelFixWidth" : 6,
					"labelColon" : false
				}
			});
		}
		</script>
	</head>

	<body>
		<!-- 1.工具栏层 -->
		<div class="bbox toolbar-with-pager" id="toolbar_wrap">
			<div class="btn-toolbar" role="toolbar" id="btn_toolbar">
				<div class="btn-group btn-group-sm">
					<button type="button" class="btn btn-default"
						onclick="closeTabUnMsg();">关闭</button>
				</div>
				<div class="btn-group btn-group-sm">
					<button type="button" class="btn btn-default" id="editBtn" 
						onclick="beginEdit2();">编辑</button>
					<button type="button" class="btn btn-default" id="saveBtn"
						onclick="saveData(this,'save');">暂存</button>
					<button type="button" class="btn btn-default" id="cancelBtn" 
						onclick="cancelEdit();">取消</button>
					<button type="button" class="btn btn-default" id="submitBtn"
						onclick="saveData(this,'submit');" data-loading-text="提交">提交</button>
					<button type="button" class="btn btn-default" id="firstApproveBtn" 
						onclick="createFirstApprove(finTypeEn);">提交</button>
					<button type="button" class="btn btn-default" id="approveBtn" 
						onclick="createApprove()" >审批</button>
				</div>
				
				<div class="btn-group btn-group-sm">
					<button type="button" class="btn btn-default" id="abolishBtn" onclick="abolish()">作废</button>
					<button type="button" class="btn btn-default" id="deleteBtn" onclick="deleteFinanceMain()">删除</button>
				</div>
				
				<div class="btn-group btn-group-sm">
					<button type="button" class="btn btn-default" id="previewPdfBtn">打印</button>
				</div>
				<div class="btn-group btn-group-sm">
					<button type="button" class="btn btn-default" id="flowInfoWithStatusAndApprove" 
						onclick="showFlowImgInfoWithApprove();">审批信息</button>
					<button type="button" class="btn btn-default" id="flowInfoWithStatus" 
						onclick="showFlowImgInfo();">审批信息</button>
				</div>
				<div class="btn-group btn-group-sm" id="btnGrpInputERP">
					<button class="btn-default btn priv" privilege="VIRTUAL-INPUTERP" onclick="inputERP()" id="btnInputERP">生成凭证</button>
				</div>
			</div>
		</div>
		
		<!-- 2.标题层 -->
		<div class="inner-title" id="innerTitle"></div>
		
		<!-- 4.表单层 -->
		<div>
			<form id="autoform" class="margin-form-title margin-form-foldable"></form>
		</div>
		
		<!-- 5.报销明细层 -->
		<div grouptitle="报销明细" id="detailTitle">
			<div class="margin-title-table">
				<table id="finTable" style="" class="eu-datagrid"></table>
			</div>
			<div class="btn-toolbar margin-foldable-button" role="toolbar" id="addDetailBtn_toolbar">
				<div privilege="F-ROLE-ADD" class="btn-group btn-group-xs">
					<button type="button" class="btn btn-success" id="addDetailBtn" onclick="showDtlIframe('add',null,null)">添加明细</button>
					<button type="button" class="btn btn-success" id="goOnAddDetailBtn" onclick="showDtlIframe('add',null,null)">继续添加明细</button>
				</div>
			</div>
		</div>
		
		<!-- 6.附件层 -->
		<div class="margin-group"></div>
		<div grouptitle="附件" id="uploadfileTitle">
			<div class="margin-title-table" id="uploadfile">
				<form id="uploadform" style=""></form>
			</div>
		</div>
	</body>
</html>
