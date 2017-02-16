<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html style="height:99%">
<head>
<title>新建工单标识</title>
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
	var woPriorityId = '<%=request.getParameter("woPriorityId")%>';  
	var loginUserId = ItcMvcService.user.getUserId();
	var siteId = ItcMvcService.getUser().siteId;
</script>
<script>

	var hasConfigOtherPri;
	/* 表单字段定义  */
	var fields = [
			{title : "ID", id : "id",type : "hidden"},
		    {title : "编码", id : "priorityCode",type : "hidden",sortable:true},
		    {title : "服务级别名",id : "name", rules : {required:true}},
		    {title : "权重", id : "weight", rules : {required:true,number:true},type : "hidden"},
		    {title : "响应时间(分钟)", id : "respondLength", rules : {required:true,number:true}},
		    {title : "解决时间(小时)", id : "solveLength", rules : {required:true,number:true}},
		    {title : "提醒次数", id : "alertCount", rules : {required:true,number:true},type : "hidden"},
		    {title : "释放时间(小时)", id : "releaseLength", rules : {required:true,number:true}},
	    	{
		        title : "说明", 
		        id : "remarks",
		        type : "textarea",
		        linebreak:true,
		        wrapXsWidth:12,
		        wrapMdWidth:8,
		        height:55
		       
		    }];
		    
var woPriConfDelteId = 1;
var	woPriConfGridField = [[
		{field : 'woPriConfDelteId',title : '临时ID用来指定删除row', hidden:true,
			formatter:function(value,row,index){
				row.woPriConfDelteId =woPriConfDelteId;
				return woPriConfDelteId++;
			}
		},  
   	    {field : 'id',title : 'ID', hidden:true},  
		{field : 'influenceScope',title : '影响范围',width:200,fixed:true,
			editor:{ 
					type : 'combobox',
					options : {
						data : FW.parseEnumData("WO_INFLUENCE_SCOPE",_enum),
						rules:{
							required:true
						}
					}
				},
				formatter:function(value){
					return  getEnumName("WO_INFLUENCE_SCOPE",value);
				}
		}, 
		{field : 'urgentDegree',title : '紧急度',width:200,fixed:true,
			editor:{ 
					type : 'combobox',
					options : {
						data : FW.parseEnumData("WO_URGENCY_DEGREE",_enum),
						rules:{
							required:true
						}
					}
				},
				formatter:function(value){
					return  getEnumName("WO_URGENCY_DEGREE",value);
				}
		},
		{field : 'xxxxx',title : '',width:100}, 
		{field : 'oper',title : '',width:55,fixed:true,align:'center', 
			 formatter:function(value,row,index){
				     return '<img src="'+basePath+'img/workorder/btn_garbage.gif" onclick="deleteGridRow('+
				     		row.woPriConfDelteId+')" width="16" height="16" >';
			}
		}
	]];
	
	function getEnumName(enumcate,enumVal){
		var data=FW.parseEnumData(enumcate,_enum);
		var r="";
		for(var i in data){
			if(data[i][0]==enumVal){
				r=data[i][1];
				break;
			}
		}
		return r;
	}
		    
	$(document).ready(function() {
		 $("#woPriorityForm").iForm("init",{"fields":fields,"options":{validate:true}});
		 $("#title_woPriConf").iFold("init");
		 var woPriConfDatagrid =  $("#woPriConfTable").datagrid({
			    columns:woPriConfGridField,
			    idField:'woPriConfDelteId',
			    singleSelect:true,
			    fitColumns:true,
			    scrollbarSize:0
			}); 
		  
		 if(woPriorityId == null || woPriorityId == 'null'){ 
		 		woPriorityId = 0 ;
				$("#woPriorityForm").iForm("hide","priorityCode");
				$("#btn_edit").hide(); 
				$("#btn_delete").hide(); 
				FW.fixRoundButtons("#toolbar1");
			}else{  //查看工作方案
				$.post(basePath + "workorder/woParamsConf/queryWoPriorityDataById.do",{"woPriorityId":woPriorityId},
					function(woPriorityData){
						$("#btn_save").hide(); 
						FW.fixRoundButtons("#toolbar1");
						var woPriorityFormData = eval("(" +woPriorityData.baseData+ ")");
						$("#woPriorityForm").iForm("setVal",woPriorityFormData);
						$("#woPriorityForm").iForm("endEdit");
						var woPriConfigObj = eval("(" +woPriorityData.datagridData+ ")");
						if(woPriConfigObj.length!=0){
							$("#woPriConfTable").datagrid("loadData",woPriConfigObj );
							$("#woPriConfTable").datagrid("hideColumn","oper"); //隐藏某一列
							$("#woPriConfBtnDiv").hide();
						}else{
							$("#title_woPriConf").iFold("hide");
						}
					},"json");
			}
	});
	
	
	/**
	*保存
	**/
	function savePriority(){
		/**表单验证*/
		if(!$("#woPriorityForm").valid()){
			return ;
		}
		endEdit("woPriConfTable");
		var woPriConfData = $("#woPriConfTable").datagrid("getData");
		/* if(woPriConfData.total==0){
			FW.error("请添加对应的紧急度和影响范围");
			return ;
		} */
		if(hasRepeatData(woPriConfData)){
			FW.error("请删掉重复的紧急度和影响范围");
			beginEdit("woPriConfTable");
			return ;
		}
		
		$.ajaxSetup({"async":false});
		checkHasConfigData(woPriConfData);  //查询是否配置给了其他的服务级别
		if(hasConfigOtherPri.woPriId != 0){
			var  influence = getEnumName("WO_INFLUENCE_SCOPE",hasConfigOtherPri.influenceVal);
			var  urgent = getEnumName("WO_URGENCY_DEGREE",hasConfigOtherPri.urgentVal);
			FW.error("“"+influence+" : "+urgent+"”与“"+hasConfigOtherPri.woPriName+"”重复");
			beginEdit("woPriConfTable");
			return ;
		}else{
			var woPriorityFormData = $("#woPriorityForm").iForm("getVal");  //取表单值
			var woPriorityId = woPriorityFormData.id;
			if(woPriorityId==""){  
				woPriorityId = 0;
			};
			 
		 	$.post(basePath + "workorder/woParamsConf/commitWoPriority.do",
			 		{"woPriorityForm":FW.stringify(woPriorityFormData),"woPriConfData":FW.stringify(woPriConfData), "woPriorityId":woPriorityId},
					function(data){
						if(data.result == "success"){
							FW.success("新建成功");
							closeCurPage();
						}else {
							FW.error("新建失败");
						}
			  },"json"); 
			  $("#woPriorityForm").iForm("endEdit");  //关闭编辑，
		}
		$.ajaxSetup({"async":true});  //打开异步有效
	}
	
	/**判断是否有重复的数据*/
	function hasRepeatData(woPriConfData){
		var len = woPriConfData.total;
		for(var i=0; i<len; i++){
			var influenceScope1 = woPriConfData.rows[i].influenceScope;
			var urgentDegree1 = woPriConfData.rows[i].urgentDegree;
			for(var j=i+1; j<len; j++){
				var influenceScope2 = woPriConfData.rows[j].influenceScope;
				var urgentDegree2 = woPriConfData.rows[j].urgentDegree;
				if(influenceScope1==influenceScope2 &&urgentDegree1==urgentDegree2 ){
					return true;
				}
			}
		}
		return false;
	}
	/** 查询新的配置是否配置给了其他的服务级别 */
	function checkHasConfigData(woPriConfData){
		$.post(basePath + "workorder/woParamsConf/checkHasConfigData.do",
				{"woPriConfData":FW.stringify(woPriConfData),"priId":woPriorityId},
			function(data){
				hasConfigOtherPri = data;
			},"json");
    	
		
	}
	/**
	*编辑
	**/
	function editPriority(){
		
		$("#woPriorityForm").iForm("beginEdit",["name","weight","remarks","alertLength","alertCount","alertGap"]); 
		$("#btn_save").show(); 
		$("#btn_edit").hide(); 
		FW.fixRoundButtons("#toolbar1");
		
		$("#title_woPriConf").iFold("show");
		var woPriConfData = $("#woPriConfTable").datagrid("getData");
		if(woPriConfData.total != 0){
			$("#btn_woPriConfTable").html("继续添加")
		}
		beginEdit("woPriConfTable");
		$("#woPriConfTable").datagrid("showColumn","oper"); //显示某一列
		$("#woPriConfBtnDiv").show();
	}
	/**
	* 删除服务级别
	**/
	function deletePriority(){
		Notice.confirm("确定删除|确定删除此服务级别么？",function(){
			$.post(basePath + "workorder/woParamsConf/deletePriority.do",
			 		{"priId":woPriorityId},
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
 * 在datagrid的末尾添加一行数据
 * @param dataGridId  dataGrid的ID
 */
	function appendWoPriConf(){
		var title = $("#btn_woPriConfTable").html();
		var fdStart = title.indexOf("继续");  
		if(fdStart!=0){
	    	$("#btn_woPriConfTable").html("继续"+title);
	    }
	    
	 	$("#woPriConfTable").datagrid("appendRow",{});
	    editIndex = $("#woPriConfTable").datagrid("getRows").length-1;
	    $("#woPriConfTable").datagrid("selectRow", editIndex)
	                .datagrid('beginEdit', editIndex);
	}
/**
 * 删除datagrid的一行数据
 * @param deleteId  dataGrid的行ID
 */
	function deleteGridRow(deleteId){
		 $("#woPriConfTable").datagrid('deleteRow', $("#woPriConfTable").datagrid('getRowIndex',deleteId));
		 var rowsLength = $("#woPriConfTable").datagrid('getRows').length;
		 if(rowsLength == 0){
			 var oldBtnName =  $("#woPriConfTable").html().substring(2);
			 $("#woPriConfTable").html(oldBtnName);
		 }
	}
function beginEdit(id){
   	var rows = $("#"+id).datagrid('getRows');
   	for(var i=0;i<rows.length;i++){
		$("#"+id).datagrid('beginEdit',i);
	}
}
function endEdit(id){
   var rows = $("#"+id).datagrid('getRows');
   for(var i=0;i<rows.length;i++){
	   $("#"+id).datagrid('endEdit',i);
   }
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
		        	<button id="btn_save" type="button" class="btn btn-success" onclick="savePriority()">保存</button>
		        	<button id="btn_edit" type="button" class="btn btn-default" onclick="editPriority()">编辑</button>
		        </div>
		         <div class="btn-group btn-group-sm">
		        	<button id="btn_delete" type="button" class="btn btn-default" onclick="deletePriority()">删除</button>
		        </div>
		    </div>
		</div>
		<div>
	    	 <form id="woPriorityForm" class="autoform"></form>
	    </div>
		 <div id="priConfContentDiv" >
			<div id="title_woPriConf" grouptitle="对应的紧急度和影响范围">
				<div class="margin-title-table">
					<form><table id="woPriConfTable" class="eu-datagrid"></table></form>
					<div id="woPriConfBtnDiv" class="row btn-group-xs" >
						 <button id="btn_woPriConfTable" onclick="appendWoPriConf();" type="button" class="btn btn-success">添加</button>
					</div>
				</div>
			</div>
	    </div>
	</div>
		
</body>
</html>