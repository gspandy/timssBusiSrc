<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html style="height:99%">
<head>
<title>选择隔离方法</title>

<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>

	$(document).ready(function() {

		dataGrid = $("#islMethod_table").iDatagrid("init",{
	        pageSize:pageSize,//pageSize为全局变量
	        url: basePath + "ptw/ptwIslMethDef/islMethDefListData.do",	   
	        singleSelect:false,
	        columns:[[ 
	        		{field:"ck",checkbox:true},
					{field:"id",title:"id",width:110,fixed:true,hidden:true}, 
					{field:"no",title:"编号",width:110,fixed:true}, 
					{field:"method",title:"隔离方法名",width:200}
				]],
	        onLoadSuccess: function(data){
	            //远程无数据需要隐藏整个表格和对应的工具条，然后在无数据信息中指引用户新建
	           if(data && data.total==0){
	                $("#mainContent").hide();
	                $("#grid1_empty").show();
	            }else{
	            	$("#mainContent").show();
	                $("#grid1_empty").hide();
	            } 
	        },
		});
		
		
		  //表头搜索相关的
	    $("#btn_advSearch").click(function(){
		    if($(this).hasClass("active")){
		        $("#islMethod_table").iDatagrid("endSearch");
		    }
		    else{
		        $("#islMethod_table").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(args){
				    return {"search":JSON.stringify(args)};
				}}); 
		    }
		});
		
	});
	function getSelected(){
		var rows  =  $("#islMethod_table").datagrid("getChecked");
		var result =  null ;
		if(rows && rows.length>0){
			result={};
			for(var i=1; i<rows.length; i++){
				var row = rows[i];
				result[row.id] = row.no;
			}
		}
		return result;
	}
	
	function getSelectedRetrunId(){
		var rows  =  $("#islMethod_table").datagrid("getSelections");
		var result =  "" ;
		if(rows && rows.length>0){
			for(var i=0; i<rows.length; i++){
				var row = rows[i];
				result += "," + row.id;
			}
			result = result.substring(1);
		}
		return result;
	}
	
	function getFullDataSelected(){
		return 	$("#islMethod_table").datagrid("getChecked");	
	}

</script>
</head>
<body style="height: 100%;" class="bbox">
	<div id="mainContent">
		<div class="bbox toolbar-with-pager" id="toolbar_wrap">
		    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
		    <div id="pagination_1" class="toolbar-pager" bottompager="#bottomPager">        
		    </div>
		</div>
		<!--这里要清掉分页器的右浮动效果-->
		<div style="clear:both"></div>
		<div id="grid1_wrap" style="width:100%">
		    <table id="islMethod_table" pager="#pagination_1" class="eu-datagrid">
		    </table>
		</div>
	</div>
	
	
	<!-- 无数据 -->
	<div id="grid1_empty" style="display:none;width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">未查询到“隔离方法”信息</div>
			    <div class="btn-group btn-group-sm margin-element">
		             <button type="button" class="btn btn-success" onclick="newIslMethod();">新建</button>
			    </div>
			</div>
		</div>
	</div>
</body>
</html>