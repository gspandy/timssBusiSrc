<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html  style="height: 99%;">
<head>
<title>加班申请列表</title>
<script>_useLoadingMask = true;</script>

<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src='${basePath}js/attendance/common/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/attendance/common/commonBtnSecurity.js?ver=${iVersion}'></script>

<script>
//第一次打开页面
var modelFlag = false;

	$(document).ready(function() {
		//初始化sec_function 按钮权限
		initBtnSec( );
		
		//此处一定要给dataGrid赋值。dataGrid为全局变量，不设置会导致不能resize
		dataGrid = $("#contentTb").iDatagrid("init",{
	        pageSize:pageSize,//pageSize为全局变量，自动获取的
	        singleSelect :true,
	        url: basePath + "attendance/overtime/queryOvertimeBySiteId.do",	//basePath为全局变量，自动获取的
	        /* queryParams: {
				search: function(){return FW.stringify({itemUserInfo:$("#itemUserInfo").val()})}
			}, */
	        "columns":[[
				{field:'id',title:'id',width:20,fixed:true,hidden:true},
				{field:'num',title:'编号',width:140,fixed:true, sortable: true},
				{field:'userName',title:'申请人',width:65,fixed:true, sortable: true},
				{field:'deptName',title:'申请部门',width:100,fixed:true, sortable: true},
				{field:'createDay',title:'申请时间',fixed:true,width:150,sortable: true,
					formatter:function(val){
						return FW.long2time(val);
					}	
				},
				/* {field:'startDate',title:'加班开始时间',fixed:true,width:150, sortable: true,
					formatter:function(val){
						return FW.long2time(val);
					}	
				},
				{field:'planOverHours',title:'申请加班时长',width:120,fixed:true, sortable: true},
				{field:'realOverHours',title:'核定加班时长',width:120,fixed:true, sortable: true}, */
				{field:'overTimeReason',title:'申请事由',width:250, sortable: true},
				{field:'status',title:'状态',fixed:true,width:150, sortable: true},
				]],
			"onLoadError": function(){
				//加载错误的提示 可以根据需要添加
				$("#grid1_wrap,#toolbar_wrap,#bottomPager").hide();
			    $("#grid1_error").show();
			},
	        onLoadSuccess: function(data){
	            //远程无数据需要隐藏整个表格和对应的工具条，然后在无数据信息中指引用户新建
	            if(data && data.total==0){
	            	 if( modelFlag ){
		                	$("#noResult").show();
		                }else{
			            	$("#grid1_wrap,#toolbar_wrap").hide();
			                $("#grid1_empty").show();
			                $("#noResult").hide();
		                }
	            }else{
	            	$("#grid1_wrap,#toolbar_wrap").show();
	                $("#grid1_empty").hide();
	                $("#noResult").hide();
	            }
	           
	        },
	        onRenderFinish:function(){
	        	$("#contentTb").datagrid("resize");
	        },
	        onDblClickRow : function( rowIndex, rowData ){
		    	var url = "${basePath}/page/attendance/dpp/checkin/Overtime-updateOvertime.jsp?id=" + rowData.id;
		    	
		    	addTabWithTree( "updateOvertime"+rowData.id, "加班申请", url,"attendance", "contentTb" );
		    }

	    });
		
		$("#btn_add,#addBtn").click(function(){
			var url ="${basePath}attendance/overtime/insertOvertimeMenu.do";
			addTabWithTree( "addOvertime", "加班申请", url,"attendance", "contentTb");
		});
		
		$("#itemUserInfo").iInput("init",{onSearch:function(){
			/* if($("#itemUserInfo").val()===""){
				FW.error("请输入要查询的加班人员的姓名或工号");
				return;
			} */
			modelFlag = true;
			if($(".itcui_btn_mag")[1]){
				$(".itcui_btn_mag")[1].click();
			}else{
				//$("#contentTb").datagrid("reload");
				$("#contentTb").iDatagrid("init",{
					queryParams: {
						search: FW.stringify({itemUserInfo:$("#itemUserInfo").val()})
					}
				}); 
			}
		}});
		//高级搜索
		$("#btn_advlocal").click(function() {
			modelFlag = true;
			if($(this).hasClass("active")){
				$("#contentTb").ITCUI_GridSearch("end");
			}
			else{
				$("#contentTb").ITCUI_GridSearch("init",{"remoteSearch":true,"onParseArgs":function(arg){
					arg["itemUserInfo"]=$("#itemUserInfo").val();
					return {"search":JSON.stringify(arg)};
				}});
			}
		});
		
	});
</script>
</head>
<body style="height: 100%;" class="bbox list-page">
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar1" class="btn-toolbar ">
	        <div class="atd_btn_pri atd_ot_create btn-group btn-group-sm">
	           <button type="button" class="btn btn-success" id="btn_add">新建</button>
	        </div>
	        <div class="atd_btn_pri atd_ot_search btn-group btn-group-sm">
	           <button type="button" class="btn btn-default" data-toggle="button" id="btn_advlocal">查询</button>
	    	</div>
	    	<div style="width: 170px;float:left;">
	    		<div class="input-group input-group-sm" style="width:100%;float:left;margin-top:3px;">
					<input type="text" id="itemUserInfo" icon="itcui_btn_mag" placeholder="输入姓名或工号进行查询" style="width:150px"/> 
				</div>
	    	</div>
	    </div>
	    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
	    <div id="pagination_1" class="toolbar-pager" bottompager="#bottomPager">        
	    </div>
	</div>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	
	<div id="grid1_wrap" style="width:100%">
	    <table id="contentTb" pager="#pagination_1" class="eu-datagrid">
	    </table>
	</div>
	<!-- 下页器部分-->
	<div id="bottomPager" style="width:100%;margin-top:6px">
	</div>
	
	<div id="noResult" style="display:none;width:100%;">
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
	<div id="grid1_empty" style="display:none;width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">没有加班申请信息</div>
			    <div class="atd_btn_pri atd_ot_create btn-group btn-group-sm margin-element">
		                 <button type="button" class="btn btn-success" id="addBtn">新建</button>
			    </div>
			</div>
		</div>
	</div>
</body>
</html>