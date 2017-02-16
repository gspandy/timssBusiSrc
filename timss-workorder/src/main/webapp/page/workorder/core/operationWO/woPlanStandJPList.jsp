<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<title>标准作业方案列表</title>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
	var formParams = null;
	var paramsCategoryList = null;
	paramsCategoryList = ["WO_SPEC"];  
	var jsonParams = null;
	 
	/**
	 * 参数初始化 
	 */
	function initGetParams(){
		ItcMvcService.getEnumParams(paramsCategoryList, function(data){
			params = convertEnumToJSONType(data.WO_SPEC);
			jsonParams = params;
		});
	}
	/**
	 * 将后台得到的data数据格式转换为ui组件能够识别的格式
	 * @param data 从后台得到的数据
	 */
	function convertEnumToJSONType(data){
		var result={};
		var res,crr;
		for(var i in data){
			crr=data[i];
	        result[crr['code']]=crr['label'];
		}
		return result;
	}

	$(document).ready(function() {
		 initGetParams();
		 dataGrid = $("#jobPlan_table").iDatagrid("init",{
	        pageSize:pageSize,//pageSize为全局变量
	        url: basePath + "workorder/jobPlan/jobPlanListData.do",	//basePath为全局变量，自动获取的       
	        
	        columns:[[ 
					{field:"jobPlanCode",title:"编号",width:110,fixed:true,sortable:true}, 
					{field:"specialtyId",title:"专业",width:80,fixed:true,sortable:true,
						formatter: function(value,row,index){
							return FW.getEnumMap("WO_SPEC")[value]; 
						},
						"editor" : {
					        "type":"combobox",
					        "options" : {
					            "data" : FW.parseEnumData("WO_SPEC",_enum)
					        }
					    }
					},   
					{field:"description",title:"名称",width:240},   
					{field:"remarks",title:"备注",width:150,fixed:true}
				]],
	        onLoadSuccess: function(data){
	            //远程无数据需要隐藏整个表格和对应的工具条，然后在无数据信息中指引用户新建
	            if(data && data.total==0){
	                $("#grid1_wrap").hide();
	                $("#grid1_empty").show();
	            }else{
	            	$("#grid1_wrap").show();
	                $("#grid1_empty").hide();
	            }
	        },
	        onDblClickRow : function(rowIndex, rowData) {
				 var jpId = rowData.id ;
				 var tabId = FW.getCurrentTabId();
				 var p = _parent().window.document.getElementById(tabId).contentWindow;
				 $.post(basePath + "workorder/jobPlan/queryJPDataById.do",{jobPlanId:jpId},
					function(jpFullData){
						var preHazardDataStr = jpFullData.preHazardData;
						var preHazardDataObj = eval("(" + preHazardDataStr + ")");
						
						var toolDataStr = jpFullData.toolData;
						var toolDataObj = eval("(" + toolDataStr + ")");
					
						var taskDataStr = jpFullData.taskData;
						var taskDataObj = eval("(" + taskDataStr + ")");
						
						p.$("#preHazardTable").datagrid("loadData",preHazardDataObj);
						p.$("#toolTable").datagrid("loadData",toolDataObj);
						p.$("#taskTable").datagrid("loadData",taskDataObj);
						
						p.beginEdit("preHazardTable");
						p.beginEdit("toolTable");
						p.beginEdit("taskTable");
						
						if(preHazardDataObj.length!=0){
							p.$("#btn_preHazardTable").html("继续添加安全注意事项");
						}
						if(toolDataObj.length!=0){
							p.$("#btn_toolTable").html("继续添加工具");
						}
						if(taskDataObj.length!=0){
							p.$("#btn_taskTable").html("继续添加工作内容");
						}
						 
						_parent().$("#itcDlg").dialog("close");
					},"json");
					
				
			}
	    });
	    
	     $("#jobPlan_table").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(args){
				    return {"search":JSON.stringify(args)};
				}}); 
	});
	
	
</script>
</head>
<body>
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
	    <div id="pagination_1" class="toolbar-pager">        
	    </div>
	</div>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div id="grid1_wrap" style="width:100%">
	    <table id="jobPlan_table" pager="#pagination_1">
	    </table>
	</div>
	
	<!-- 无数据 -->
	<div class="row" id="grid1_empty" style="display:none">
	    	没有标准作业方案的数据
	</div>
</body>
</html>