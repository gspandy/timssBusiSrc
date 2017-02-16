<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yudean.itc.util.FileUploadUtil"%>
<%@page import="com.yudean.itc.util.Constant"%>
<%@page import="com.yudean.itc.dto.sec.SecureUser"%>
<%
	SecureUser operator = (SecureUser) session.getAttribute(Constant.secUser);
	String sessId = request.getSession().getId();
	String valKey = FileUploadUtil.getValidateStr(operator,FileUploadUtil.DEL_OWNED);
  	String jspPath = Constant.jspPath;
 %>
<!DOCTYPE html>
<html style="height: 99%;">
  <head>
	<title>新建两票审批人员配置记录</title>
	<style>
	.layout-panel-east{
		border-left-color: #C6C6C6;
		border-left-width: 1px;
		border-left-style: solid;
	}
	</style>
	<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
	<script>_useLoadingMask = true;</script>
	<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
	<script type="text/javascript" src="${basePath}/js/workflow/workflow.js?ver=${iVersion}"></script>
	<link rel="stylesheet" type="text/css" href="${basePath}${resBase}css/public_background.css?ver=${iVersion}" media="all" />
	<script>
		var id = "${id}";
		var title= "${title}";
		var relatedorgs = ${relatedorgs};
		var users = ${users};
		var ptwPtoUserInfoVo = ${ptwPtoUserInfoVo};
		var jspPath = "<%=jspPath%>";
		
	</script>
	<script type="text/javascript">
		
		var formRead = false;
		var loginUserId = ItcMvcService.user.getUserId();
		var siteId = ItcMvcService.getUser().siteId;
		var initStep = false;
		function changeTypeFun(val){
			//这里需要关闭异步，否则初始化异常
			$.ajaxSetup({'async':false});
			var category =$("#userInfoConfigForm").iForm("getVal","category"); 
			$.ajax({
				url : basePath + "ptw/userInfo/getStepList.do",
				data : {
					category : category,
					type : val,
					id:ptwPtoUserInfoVo.step
				},
				type:"POST",
				dataType:"JSON",
				success : function(result){
					var map = FW.parse(result.stepList);
					$("#f_step").iCombo('loadData',map);
					if(!initStep){
						$("#userInfoConfigForm").iForm("setVal",{"step":ptwPtoUserInfoVo.step});
						initStep = true;
					}
				}
			});
			$.ajaxSetup({'async':true});
		}
		$(document).ready(function(){
			var fields = [ {
				title : "ID",
				id : "id",
				type : "hidden"
			}, {
				title : "类别",
				id : "category",
				type : "combobox",
				dataType : "enum",
				enumCat : "PTW_TYPE",
				options:{
					firstItemEmpty:true,
					allowEmpty:true,
					onChange: function(val){
						if("spto"==val||"pto"==val){
							var dataSet = FW.parseEnumData("PTW_SPTO_TYPE",_enum);
							$("#f_type").iCombo('loadData',dataSet);
						}/*else if("sptw"==val){
							var dataSet = FW.parseEnumData("ptw_standard_type",_enum);
							$("#f_type").iCombo('loadData',dataSet); 
						}*/else if("ptw"==val||"sptw"==val){
							var dataSet = FW.parseEnumData("PTW_MAJOR",_enum);
							$("#f_type").iCombo('loadData',dataSet); 
						}
						$("#f_step").iCombo('loadData',[]);
					}
				},
				rules:{
					required:true
				}
			},{
				title : "类型",
				id : "type",
				type : "combobox",
				options:{
					initOnChange:false,
					firstItemEmpty:true,
					allowEmpty:true,
					data:[],
					onChange: function(val){
						changeTypeFun(val);	
					}
				},
				rules:{
					required:true
				}
			},{
				title : "环节",
				id : "step",
				type : "combobox",
				options:{
					initOnChange:false,
					firstItemEmpty:true,
					allowEmpty:true,
					data:[]
				},
				rules:{
					required:true
				}
			},{
				title : "生效开始日期",
				id : "beginDate",
				type : "date",
				rules:{
					required:true
				}
			}, {
				title : "生效结束日期",
				id : "endDate",
				type : "date",
				rules:{
					required:true
				}
			}];
			/* form表单初始化 */
			$("#userInfoConfigForm").iForm("init",{"fields":fields,"options":{validate:true,initAsReadonly:formRead}});
			$("#roleUser").ITCUI_Foldable();
			if(id != null && id != "null"){
				$("#inPageTitle").html("编辑两票审批人员配置");
				initUserTree("edit");
				var beginDate = new Date(ptwPtoUserInfoVo.beginDate).format("yyyy-MM-dd");
				var endDate = new Date(ptwPtoUserInfoVo.endDate).format("yyyy-MM-dd");
				$("#userInfoConfigForm").iForm("setVal",{id:id,beginDate:beginDate,endDate:endDate,category:ptwPtoUserInfoVo.category,type:ptwPtoUserInfoVo.type,step:ptwPtoUserInfoVo.step});
			}else if(id == null || id == "null"){  
				//点击新建产生的页面
				$("#inPageTitle").html("新建两票审批人员配置");
				initUserTree("create");
			}
			//控制权限
			Priv.apply();
			FW.fixRoundButtons("#toolbar");
		});
		/**
		* 初始化表单页面用户树
		*/
		function initUserTree(mode){
			if(mode=="edit"){
				if(users.length==0){
					$("#roleUser").ITCUI_Foldable("hide");
				}
				else{
					$("#roleUser").ITCUI_Foldable("show");
				}
			}
			else{
				$("#roleUser").ITCUI_Foldable("show");
			}
			//注意:这里若用JSON.stringify在赋值后会出现无法解析JSON的问题
			$.ajax({
				url : basePath + "tree?method=extendall&ignoresite=1",
				data : {
					orgFilter : FW.stringify(relatedorgs),
					personFilter : FW.stringify(users)
				},
				type:"POST",
				dataType:"JSON",
				success : function(data){
					for(var i=0;i<data.length;i++){
						checkInher(data[i]);
					}
					var opts = {
						"data":data
					};
					$("#userTree").tree(opts);
				}
			});	
		}
		//弹出选择用户树对话框
		function selectuser(){
			var src =  basePath + "ptw/userInfo/userSelect.do?curTabId="+FW.getCurrentTabId();
			var btnOpts = [{
		            "name" : "取消",
		            "float" : "right",
		            "style" : "btn-default",
		            "onclick" : function(){
		                return true;
		            }
		        },{
		            "name" : "确定",
		            "float" : "right",
		            "style" : "btn-success",
		            "onclick" : function(){
		            	var p = _parent().window.document.getElementById("itcDlgContent").contentWindow;
		            	users = p.getChecked();
		            	var lst = [];
		            	for(var k in users){
		            		lst.push(k);
		            	}
		            	initUserTree("create");            			
            			_parent().$("#itcDlg").dialog("close");
		            }
		        }
		    ];
		    var dlgOpts = {
		        width : 450,
		        height: 400,
		        closed : false,
		        title:"选择审批人员",
		        modal:true
		    };
		    FW.dialog("init", {
				"src" : src,
				"dlgOpts" : dlgOpts,
				"btnOpts" : btnOpts
			});
		}
		/**
		* 一个递归的函数
		*/
		function checkInher(node){
			if(node.id.indexOf("user_")>=0){
				var id = node.id.replace("user_","");
				//为在设置的用户弹出对话框显示初始化值
				users[id] = node.text;
				if(users[id]){
					if(users[id].indexOf("(继承)")>0){
						node.text += "(继承)";
					}
				}
			}
			if(node.children){
				for(var i=0;i<node.children.length;i++){
					checkInher(node.children[i]);
				}
			}
		}
		/**
		 * 关闭当前tab 页
		 */
		function closeCurPage(flag) {
			if (flag) {
				FW.set("userlistDoNotRefresh", true);
			}
			FW.deleteTabById(FW.getCurrentTabId());
		}
		
		/**
		 * 保存审批人员配置
		 */
		function saveUserInfo() {
			/** 表单验证 */
			if (!$("#userInfoConfigForm").valid()) {
				return;
			}
			// 表单
			$("#userInfoConfigForm").iForm("endEdit");
			var userInfoConfigFormObj = $("#userInfoConfigForm").iForm("getVal");
			var userInfoConfigFormData = FW.stringify(userInfoConfigFormObj);
			$.post(basePath + "ptw/userInfo/saveUserInfo.do", {
				"userInfoConfigForm" : userInfoConfigFormData,
				"users" : FW.stringify(users)
			}, function(data) {
				if (data.result == "success") {
					FW.success("保存成功");
					closeCurPage(true);
				} else if (data.result == "fail") {
					$("#userInfoConfigForm").iForm("beginEdit");
					FW.error("本时间段内已设置选定环节的审批人员，保存失败");
				} else{
					$("#userInfoConfigForm").iForm("beginEdit");
					FW.error("保存失败");
				}
			}, "json");
		}
	</script>

  </head>
  
  <body style="height: 100%; " class="bbox">
   	 <div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar" class="btn-toolbar ">
	    	<div class="btn-group btn-group-sm">
	        	<button id="btn_close" type="button" class="btn btn-default" onclick="closeCurPage(true);">关闭</button>
	        </div>
	    	<div id="btn_spto_operDiv" class="btn-group btn-group-sm">
	    		<button id="btn_spto_save1" type="button" class="btn btn-default" onclick="saveUserInfo();">保存</button>
			</div>
	        <div id="btn_spto_deleteDiv" class="btn-group btn-group-sm">
	        	<button id="btn_spto_delete" type="button" class="btn btn-default" onclick="selectuser();">设置审批人员</button>
	        </div>
	    </div>
	</div>
	<div  class="inner-title">
		<span id="inPageTitle">两票审批人员配置详情</span>
	</div>
    <div>
    	<form id="userInfoConfigForm" class="autoform"></form>
    </div>
    <div style="clear:both"></div>
	<div id="roleUser" grouptitle="已选择的审批用户">
		<div id="userTreeWrap" class="tree_wrap" style="clear:both">
			<div id="userTree">
			</div>
		</div>
	</div>
  </body>
</html>
