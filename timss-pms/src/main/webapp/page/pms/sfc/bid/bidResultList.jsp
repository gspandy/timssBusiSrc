<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<%
 	String path = request.getContextPath();
 	
%>
<head>
<title>工作票列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_useLoadingMask = true;</script><jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src="${basePath}js/pms/common/common.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/pms/itc/bid/bidResultList.js?ver=${iVersion}"></script>
<script>
	$(document).ready(function() {
		pmsPager.initListPager();
		//此处一定要给dataGrid赋值。dataGrid为全局变量，不设置会导致不能resize
		dataGrid = $("#test_grid1").iDatagrid("init",{
	        pageSize:pageSize,//pageSize为全局变量，自动获取的
	        url: basePath + "pms/bid/bidResultListData.do",	//basePath为全局变量，自动获取的       
	        singleSelect:true,
	        onLoadSuccess: function(data){
	            //远程无数据需要隐藏整个表格和对应的工具条，然后在无数据信息中指引用户新建
	            if(data && data.total==0){
	                //$("#grid1_wrap").hide();
	                //$("#grid1_empty").show();
	            	$('#noResult').show();
	            }else{
	            	$('#noResult').hide();
	            	$("#grid1_wrap").show();
	                $("#grid1_empty").hide();
	               // delete(_itc_grids["test_grid1"]);
	            }
	        },
	        onDblClickRow:function(rowIndex, rowData){
	        	var id=rowData["flowid"];
	        	if(!id){
	        		id='pmsViewBidTab'+rowData["bidResultId"];
	        	}
	        	openTab(basePath+"pms/bid/editBidResultJsp.do?id="+rowData['bidResultId'],'招标',id);
	        },
	        columns : [ [
	         			/* {field:'ck',checkbox:true}, */
	         			{
	         				field : 'bidId',
	         				hidden : true
	         			}, {
	         				field : 'name',
	         				title : '招标名称',
	         				width : 50,
	         				align : 'left',
	         				sortable:true
	         			}, {
	         				field : 'projectName',
	         				title : '所属项目',
	         				width : 50,
	         				align : 'left',
	         				sortable:true
	         			}, {
	         				field : 'type',
	         				title : '招标方式',
	         				width : 110,
	         				align : 'left',
	         				fixed : true,
	         				sortable:true,
	         				'editor':{
	         					 "type" : "combobox",
	         					 "options" : {
	         					    "data" : FW.parseEnumData("PMS_BID_TYPE",_enum)
	         				     }
	         				},
	         				formatter:function(value){
	         					var list=FW.parseEnumData("PMS_BID_TYPE",_enum);
	         					return getEnumValueByCode(list,value);
	         				}
	         			}, {
	         				field : 'budget',
	         				title : '中标价(元)',
	         				width : 105,
	         				align : 'right',
	         				fixed : true,
	         				sortable:true
	         			}, {
	         				field : 'flowStatus',
	         				title : '状态',
	         				width : 120,
	         				align : 'left',
	         				fixed : true,
	         				sortable:true
	         			} ] ]

	    });
		//表头搜索相关的
	    $("#btn_advSearch").click(function(){
		    if($(this).hasClass("active")){
		        $("#test_grid1").iDatagrid("endSearch");
		    }
		    else{
		        $("#test_grid1").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(args){

				    //强烈建议转为一个string传给后台，方便转Bean或hashMap
				    return {"search":FW.stringify(args)};
				}});
		    }
		});
	});
	
	function addNewBidResultTab(){
		openTab( basePath + "pms/bid/addBidResultJsp.do","招标","pmsNewBidResultTab",canOpenMutiTab);
	}
	
	function _afterCloseCallBack(){
		dataGrid.datagrid('reload');
	}

</script>
</head>
<body class="list-page">
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar1" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm " >
	            <button type="button" class="btn btn-success pms-privilege" id="pms-b-br-add" onclick="addNewBidResultTab();">新建</button>
	        </div>

	        <div class="btn-group btn-group-sm">
	            <button type="button" id="btn_advSearch" data-toggle="button" class="btn btn-default">查询</button>
	        </div>

	    </div>
	    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
	    <div id="pagination_1" class="toolbar-pager" bottompager="#bottomPager">        
	    </div>
	</div>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div id="grid1_wrap" style="width:100%">
	    <table id="test_grid1" pager="#pagination_1" class="eu-datagrid">
	        
	    </table>
	</div>
	<!-- 下页器部分-->
	<div id="bottomPager" style="width:100%;margin-top:6px">
	</div>
	
	<div id="noResult" style="display:none;width:100%;height:62%;margin:10px;">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px"> 没有找到符合条件的结果</div>
			    
			</div>
		</div>
	</div>
	<!-- 错误信息-->
	<div class="row" id="grid1_error" style="display:none">
	    无法从服务器获取数据，请检查网络是否正常
	</div>
	<!-- 无数据 -->
	<div class="row" id="grid1_empty" style="display:none">
	    没有xxxxxxx数据，单击这里创建新的....
	</div>
</body>
</html>