<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<title>标准操作票列表</title>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>


	$(document).ready(function() {
		 dataGrid = $("#sptoDlgList_table").iDatagrid("init",{
	        pageSize:pageSize,//pageSize为全局变量
	        url: basePath + "ptw/sptoInfo/sptoRdListData.do",	//basePath为全局变量，自动获取的       
	        queryParams:{"search":JSON.stringify({'_status':'passed','onRunningSpto':'true'})},
	        columns:[[ 
					{field:"code",title:"编号",width:100,fixed:true,sortable:true},
					{field:"type",title:"类型",width:80,fixed:true,sortable:true,
    					formatter: function(value,row,index){
        					return FW.getEnumMap("PTW_SPTO_TYPE")[value];
        				},
        				"editor" : {
        					"type":"combobox",
				            "options" : {
				            	"data" : FW.parseEnumData("PTW_SPTO_TYPE",_enum)
				            }
        				}	
				    }, 
				    {field:"mission",title:"操作任务",width:380,sortable:true}/* ,
				    {field:"createUserName",title:"编写人",width:80,fixed:true,sortable:true},
				    {field:"auditUserName",title:"审批人",width:80,fixed:true,sortable:true},
				    {field:"permitUserName",title:"批准人",width:80,fixed:true,sortable:true} */
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
				 var sptoId = rowData.id ;
				 var tabId = FW.getCurrentTabId();
				 var p = _parent().window.document.getElementById(tabId).contentWindow;
				 p.initDataFromSpto(sptoId);
				 _parent().$("#itcDlg").dialog("close");
			}
	    });
	    $("#sptoDlgList_table").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(args){
		        args["_status"]="passed";
		        args["onRunningSpto"]="true";
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
	    <table id="sptoDlgList_table" pager="#pagination_1">
	    </table>
	</div>
	
	<!-- 无数据 -->
	<div class="row" id="grid1_empty" style="display:none">
	    	没有标准操作票的数据
	</div>
</body>
</html>