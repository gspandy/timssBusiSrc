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
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src="${basePath}js/pms/common/common.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/common/progressBar.js?ver=${iVersion}"></script>

<script type="text/javascript" src="${basePath}js/pms/itc/project/project.js?ver=${iVersion}"></script>
<script>
	$(document).ready(function() {
		pmsPager.initListPager();
		var data=FW.get('pms-milestones');
		//此处一定要给dataGrid赋值。dataGrid为全局变量，不设置会导致不能resize
		dataGrid = $("#test_grid1").iDatagrid("init",{
	        pageSize:pageSize,//pageSize为全局变量，自动获取的
	        data:{total:9,rows:data},
	        singleSelect:true,
	        onLoadSuccess: function(data){
	            
	        },
	        onClickCell:function(rowIndex, field, value){
				if(field=="garbage-colunms"){
					dataGrid.datagrid('deleteRow',rowIndex);
				}
				
			},
	        
	        columns:[[		
			 			/* {field:'ck',checkbox:true}, */
			 			{
			 				field:"milestoneId",
			 				hidden:true
			 			},
						 {
							field : 'milestoneName',
							title : '名称',
							width : 50,
							align : 'left',
							editor:{ 
								type : 'text'
							}
						}, {
							field : 'originTime',
							title : '原始计划日期',
							width : 105,
							align : 'left',
							fixed : true,
							formatter: function(value,row,index){
									//时间转date的string，还有long2date(value)方法
									return FW.long2date(value);
								}
						}, {
							field : 'expectedTime',
							title : '计划完成日期',
							width : 105,
							align : 'left',
							fixed : true,
							
							editor:{ 
								type : 'datebox',
								
								options : {
									dataType:"date",
									minView:2,
									format:"yyyy-mm-dd"
								}
							},
							formatter: function(value,row,index){
									//时间转date的string，还有long2date(value)方法
									return FW.long2date(value);
								}
						}, {
							field : 'actualTime',
							title : '实际完成日期',
							width : 105,
							align : 'left',
							fixed : true,
							editor:{ 
								type : 'datebox',
								
								options : {
									dataType:"date",
									minView:2,
									format:"yyyy-mm-dd"
								}
							},
							formatter: function(value,row,index){
									//时间转date的string，还有long2date(value)方法
									return FW.long2date(value);
								}
						}, {
							field : 'command',
							title : '备注',
							width : 85,
							align : 'left',
							editor:{
								type:"text"
							}
						},garbageColunms
			 		]]

	    });
		convertToEditStatus(dataGrid);
	});
	function convertToEditStatus(datagrid){
		var rows=datagrid.datagrid("getRows");
		for(var i in rows){
			datagrid.datagrid('beginEdit',i);
		}
	}
    function addMilestone(){
    	var dataGrid=window.dataGrid;
    	
    	var row={};
    	dataGrid.datagrid('appendRow',row);
    	var rowindex=dataGrid.datagrid('getRowIndex',row);
    	dataGrid.datagrid('beginEdit',rowindex);

    	$('#b-add-milestone').html('继续添加里程碑');
    }
</script>
</head>
<body>
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar1" class="btn-toolbar ">
	        

	    </div>
	    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
	    <div id="pagination_1" class="toolbar-pager" bottompager="#bottomPager">        
	    </div>
	</div>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div id="grid1_wrap" style="width:100%">
	    <form id="milestoneList" grouptitle="里程碑信息" class="margin-title-table">
			 <table id="test_grid1" class="eu-datagrid">
		    </table>
		</form>
	  
	    <div class="btn-toolbar margin-foldable-button">
			<div class="btn-group btn-group-xs" >
				 <button type="button" class="btn btn-success" onclick="addMilestone();" id="b-add-milestone">添加里程碑</button>
			</div>
			
		</div>
	</div>
	<!-- 下页器部分-->
	<div id="bottomPager" style="width:100%;margin-top:6px;">
	</div>
	
	
</body>
</html>