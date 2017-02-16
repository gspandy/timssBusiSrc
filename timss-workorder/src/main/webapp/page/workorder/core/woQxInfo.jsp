<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
	<title>缺陷记录</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
	<script>_useLoadingMask = true;</script>
	<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
	<script>
		var woQxId = '<%=request.getParameter("woQxId")%>';
		var formRead = false;
		var  onDutyUserGroup = ${onDutyUserGroup};
		var  defectSolveUserGroup = ${defectSolveUserGroup};
		var  runningUserGroup = ${runningUserGroup};
		var  instructionsUserGroup = ${instructionsUserGroup};
	</script>
	<script type="text/javascript" src="${basePath}js/workorder/common/woConstant.js?ver=${iVersion}"></script>
	<script type="text/javascript" src="${basePath}js/workorder/core/woQx.js?ver=${iVersion}"></script>
	<script type="text/javascript">
		
		var equipId = null;
		var equipName = null;
		var equipCode = null;
		
		var loginUserId = ItcMvcService.user.getUserId();
		var siteId = ItcMvcService.getUser().siteId;
		
		$(document).ready(function(){
			FW.createAssetTree({multiSelect:false,forbidEdit:true}); //左边设备树
			/* form表单初始化 */
			$("#woQxRecordForm").iForm("init",{"fields":fields,"options":{validate:true,initAsReadonly:formRead}});
			//当选择了左边设备树然后新建时，直接将设备信息待过去
			/* if(equipId){
				$("#woQxRecordForm").iForm("setVal",{"equipId":equipId,"equipName":equipName,"equipCode":equipCode});
			} */
			
			if(woQxId != null && woQxId != "null"){
				setFormVal(woQxId);
				formRead = true;
			}
			if(woQxId == null || woQxId == "null"){
				woQxId = "NaN";  //打印空报表时的标识
       			$("#inPageTitle").html("新建缺陷");
       			$("#btn_qx_toWo").hide();
       			$("#btn_woQx_edit1").hide();
       			$("#btn_woQx_delete").hide();
       			//$("#title_relateWo").hide();
			}
			FW.fixRoundButtons("#toolbar");  //解决按钮隐藏后的圆角问题
		});
	function passAssetSelect(data){
		if(formRead == false){
			$("#woQxRecordForm").iForm("setVal",{
		        equipId : data.id,
		        equipCode:data.assetCode,
		        equipName : data.text
		    });
		}
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
	    	<div id="btn_wo_operDiv" class="btn-group btn-group-sm">
				<button id="btn_woQx_commit" type="button" class="btn btn-default" onclick="commitWoQx();">提交</button>
				<button id="btn_woQx_edit1" type="button" class="btn btn-default" onclick="editWoQx();">编辑</button>
				<button id="btn_woQx_delete" type="button" class="btn btn-default" onclick="deleteWoQx();">删除</button>
			</div>
			<div id="btn_qx_toWoDiv" class="btn-group btn-group-sm">
				<button id="btn_qx_toWo" type="button" class="btn btn-success" onclick="qxToWoBtn()">生成工单</button>
			</div>
			<div id="btn_wo_PrintDiv" class="btn-group btn-group-sm">
	        	<button id="btn_woQx_printBdzqx" type="button" class="btn btn-default" onclick="printWoQx()">打印</button>
	        </div>
	        
	    </div>
	</div>
	<div  class="inner-title">
		<span id="inPageTitle">缺陷记录单</span>
	</div>
	
    <div>
    	 <form id="woQxRecordForm" class="autoform"></form>
    </div>
    
    <div id="title_relateWo" grouptitle="关联工单" >
		 <table id="relateWoTable" class="eu-datagrid"></table>
	</div>
  </body>
</html>
