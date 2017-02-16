<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>父维护计划列表</title>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
var formParams = null;
	var paramsCategoryList = null;
	paramsCategoryList = ["WO_SPEC","WO_MAINTAINPLAN_FROM"];  
	var specJsonParams = null;
	var mtpFromJsonParams = null;
	var headSearchParams = {}; 
	/**
	 * 页面初始化 
	 */
	function initPage(){
		ItcMvcService.getEnumParams(paramsCategoryList, function(data){//查询枚举参数
			specJsonParams = convertEnumToJSONType(data.WO_SPEC);  
			mtpFromJsonParams = convertEnumToJSONType(data.WO_MAINTAINPLAN_FROM);
			initDataGrid();  //初始化列表
			 $("#maintainPlan_table").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(args){
		        	//人为改变参数
		        	//args["extraParam"] = "WT10001";
				    //强烈建议转为一个string传给后台，方便转Bean或hashMap
				    return {"search":JSON.stringify(args)};
				}});
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
	
	function initDataGrid(){
		 dataGrid = $("#maintainPlan_table").iDatagrid("init",{
	        pageSize:pageSize,//pageSize为全局变量
	        singleSelect:true,
	        queryParams:{
	        	search : function(){
	        		return JSON.stringify(headSearchParams);
	        	}
	        },
	        url: basePath + "workorder/maintainPlan/parentMTPListdata.do",	//basePath为全局变量，自动获取的       
	        columns:[[ 
					{field:"maintainPlanCode",title:"编号",width:120,fixed:true,sortable:true},
					{field:"specialtyId",title:"专业",width:85,fixed:true,sortable:true,
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
					{field:"description",title:"名称",width:200},   
					{field:"equipName",title:"维护设备",width:150}
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
				//alert("工单编号："+rowData.workOrderCode+"   工单ID: "+rowData.id);
				var tabId = FW.getCurrentTabId();
				var p = _parent().window.document.getElementById(tabId).contentWindow;
				p.$("#maintainPlanForm").iForm("setVal",{
					parentMTPCode : rowData.maintainPlanCode,
					parentMTPId : rowData.id
				});
				_parent().$("#itcDlg").dialog("close");
			}
	    });
	}
	
	$(document).ready(function() {
		 initPage();
		/*   //表头搜索相关的
	    $("#btn_advSearch").click(function(){
		    if($(this).hasClass("active")){
		        $("#maintainPlan_table").iDatagrid("endSearch");
		    }
		    else{
		        $("#maintainPlan_table").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(args){
		        	//人为改变参数
		        	//args["extraParam"] = "WT10001";
				    //强烈建议转为一个string传给后台，方便转Bean或hashMap
				    return {"search":JSON.stringify(args)};
				}});
		    }
		}); */
	});



</script>
</head>
<body>
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
		<!-- <div id="toolbar1" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm">
	            <button type="button" id="btn_advSearch" data-toggle="button" class="btn btn-default">高级搜索</button>
	        </div>
	    </div> -->
	    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
	    <div id="pagination_1" class="toolbar-pager">        
	    </div>
	</div>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div id="grid1_wrap" style="width:100%">
	    <table id="maintainPlan_table" pager="#pagination_1"></table>
	</div>
	
	<!-- 无数据 -->
	<div class="row" id="grid1_empty" style="display:none">
	    	没有维护计划的数据 
	</div>
</body>
</html>