<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<title>维护计划列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />

<script>
var paramsCategoryList = null;
	paramsCategoryList = ["WO_SPEC"];  
	var specJsonParams = null;
	
	var selectTreeId = '<%=request.getParameter("selectTreeId")%>';
	var type = '<%=request.getParameter("type")%>';
	/**
	 * 页面初始化 
	 */
	function initPage(){
		ItcMvcService.getEnumParams(paramsCategoryList, function(data){//查询枚举参数
			specJsonParams = convertEnumToJSONType(data.WO_SPEC);  
			initDataGrid();  //初始化列表
			$("#workorder_table").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(args){
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
		dataGrid = $("#workorder_table").iDatagrid("init",{
	        pageSize:pageSize,//pageSize为全局变量
	        url: basePath + "workorder/workorder/workorderListdata.do?selectTreeId="+selectTreeId,	//basePath为全局变量，自动获取的       
	        columns:[[ 
	        		{field:'ck',width:10,checkbox:'true',fixed:true},
					{field:"workOrderCode",title:"编号",width:120,fixed:true,sortable:true},
					{field:"woSpecCode",title:"专业",width:90,fixed:true,sortable:true,
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
					{field:"description",title:"名称",width:280}, 
					{field:"createuser",title:"报障人",width:80,fixed:true}
				]],
			singleSelect:true,
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
				var tabId = FW.getCurrentTabId();
				var p = _parent().window.document.getElementById(tabId).contentWindow;
				if(type=="woapply"){
					p.$("#workapplyForm").iForm("setVal",{
						 woCode : rowData.workOrderCode,
						 woId : rowData.id
					});
				}else{
					p.$("#workOrderForm").iForm("setVal",{
						parentWOCode : rowData.workOrderCode,
						parentWOId : rowData.id
					});
				}
				
				_parent().$("#itcDlg").dialog("close");
			}
	    });
	}
	
	function getSelected(){
		var obj = $("#workorder_table").datagrid("getChecked");
		if(obj){
			var o = {};
			for(var i=0;i<obj.length;i++){
				o[obj[i].itemcode] = JSON.stringify(obj[i]);
			}
			return o;
		}
		else{
			return null;
		}
	}
	
	function getFullDataSelected(){
		return $("#workorder_table").datagrid("getChecked");	
	}
	
$(document).ready(function() {
		 initPage();
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
	    <table id="workorder_table" pager="#pagination_1">
	       
	    </table>
	</div>
	<!-- 错误信息-->
	<div class="row" id="grid1_error" style="display:none">
	    无法从服务器获取数据，请检查网络是否正常
	</div>
	<!-- 无数据 -->
	<div class="row" id="grid1_empty" style="display:none">
	    	没有工单的数据
	</div>
</body>
</html>