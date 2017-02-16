<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html  style="height: 99%;">
<head>
<title>硬件台账多条件查询结果</title>
<script>_useLoadingMask = true;</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src='${basePath}js/asset/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/asset/commonForm.js?ver=${iVersion}'></script>

<script>
//第一次打开页面
var modelFlag = false;

	function setData( ){
		var data = {};
		data.hwId = '${ multiSearchVo.hwId }';
		data.hwName = '${ multiSearchVo.hwName }';
		data.hwType = '${ multiSearchVo.hwType }';
		data.ip = '${ multiSearchVo.ip }';
		data.model = '${ multiSearchVo.model }';
		data.toUseTime = '${ multiSearchVo.toUseTime }';
		return data;
	}
	
	$(document).ready(function() {
		var formData = JSON.stringify( setData() );

		var url = basePath + "asset/hwLedger/queryHwLedgerMulti.do?formData=" + formData;
		
		//此处一定要给dataGrid赋值。dataGrid为全局变量，不设置会导致不能resize
		dataGrid = $("#contentTb").iDatagrid("init",{
	        pageSize:pageSize,//pageSize为全局变量，自动获取的
	        singleSelect :true,
	        fitColumns:true,
	        url: url,
	        "columns":[[
				{field:'hwId',title:'hwId',width:20,fixed:true,hidden:true},
				{field:'hwType',title:'类型',width:100,fixed:true, sortable: true,formatter:function(val){
					var str = FW.getEnumMap("AST_HW_TYPE")[val];
					if( str == null || str == "" ){
						str = val;
					}
					return str;
					
				},
				"editor" : {
			        "type":"combobox",
			        "options" : {
			        	"data" : dataType						        	
			        }
			    }

				},
				{field:'hwName',title:'名称',width:140,fixed:true, sortable: true},
				{field:'ip',title:'IP',width:200,fixed:true, sortable: true},
				/* {field:'model',title:'型号',width:200,fixed:true, sortable: true}, */
				{field:'toUseTime',title:'投入运行时间',width:100,
					 formatter:function(val){
						return FW.long2date(val);
					} 
				}
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
	            modelFlag = true;
	        },
	        onDblClickRow : function( rowIndex, rowData ){
	        	FW.activeTabById('equipment');
	        	//asset/assetMenu/queryHardwareMenu.do
	        	//FW.activeTreeItem( "assetHardware");
	        	var p = window.parent.document.getElementById("assetHwTree").contentWindow;
	        	$.ajax({
					url : basePath + "asset/hwLedger/searchHintHwLedgerParentIds.do?id=" + rowData.hwId,
					type:'POST',
					dataType:"json",
					success : function(data){
						p.HardwareTree.objs.expandList = data;
						p.HardwareTree.objs.currExpand = data.length-1;
						p.$('#property_tree').tree("options").onExpand=function(node){
							if(p.HardwareTree.objs.expandList){
								p.HardwareTree.expandForHint();
							}
						};
						p.HardwareTree.expandForHint();
					}
				});
		    }

	    });
		
		
		//高级搜索
		$("#btn_advlocal").click(function() {
			if($(this).hasClass("active")){
				$("#contentTb").ITCUI_GridSearch("end");
			}
			else{
				$("#contentTb").ITCUI_GridSearch("init",{"remoteSearch":true,"onParseArgs":function(arg){
					return {"search":JSON.stringify(arg)};
				}});
			}
		});
		
	});
	
	var dataType = [
	        	["","全部",true],
	        	["HW_L_SERVER","服务器"],
	        	["HW_L_VM","虚机"],
	        	["HW_L_ROOM_EQPT","机房设备"],
	        	["HW_L_NETWORK","网络设备"],
	        	["HW_L_STORAGE","存储设备"]
	         ];
</script>
</head>
<body style="height: 100%;" class="bbox list-page">
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm">
	           <button type="button" class="btn btn-default" data-toggle="button" id="btn_advlocal">查询</button>
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
			    <div style="font-size:14px">没有信息</div>
			</div>
		</div>
	</div>
</body>
</html>