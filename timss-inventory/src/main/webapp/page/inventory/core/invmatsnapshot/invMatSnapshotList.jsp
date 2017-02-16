<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:99%">
<head>
<link rel="shortcut icon" href="${basePath}favicon.ico" type="image/x-icon" />
<title>库存快照列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<link href="${basePath}css/inventory/inventory.css" rel="stylesheet" type="text/css" />
<script src="${basePath}js/inventory/common/inventory.js?ver=${iVersion}"></script>
<script>
	var isSearchLineShow=false;
	var isSearchMode = false;
	$(document).ready(function(){
		initList();
		initBtn();
		InvSnapshotPriv.init();
		FW.fixToolbar("#toolbar1");
	});

	function refCurPage(){
	 	$("#invmatsnapshot_grid").datagrid("reload");
	}
	
	function initList(){
		$("#invmatsnapshot_grid").iDatagrid("init",{
			singleSelect:true,
			pageSize:pageSize,
			url: basePath+"inventory/invmatsnapshot/queryMatSnapshotList.do",
			onLoadSuccess:function(data){
				if(isSearchMode){
	            	 if(data && data.total==0){
	            		 $("#noSearchResult").show();
	            	 }
	            	 else{
	            		 $("#noSearchResult").hide();
	            	 }
	            }
				else{
		            if(data && data.total==0){
		                $("#grid_wrap,#toolbar_wrap").hide();
		                $("#grid_error").show();
		            }else{
		            	$("#grid_wrap,#toolbar_wrap").show();
		                $("#grid_error").hide();
		            }
		            $("#noSearchResult").hide();
				}
				setTimeout(function(){ $("#invmatsnapshot_grid").datagrid("resize"); },200);
				isSearchMode = false;
			},
			onDblClickRow : function(rowIndex, rowData) {
				var url = basePath+ "inventory/invmatsnapshot/invMatSnapshotDetailList.do?imsid="+rowData.imsid;
		    	var prefix = rowData.imsid;
			    FW.addTabWithTree({
			        id : "snapshotDetailForm" + prefix,
			        url : url,
			        name : "库存快照",
			        tabOpt : {
			            closeable : true,
			            afterClose : "FW.deleteTab('$arg');FW.activeTabById('stock');FW.getFrame(FW.getCurrentTabId()).refCurPage();"
			        }
			    });
			}
		});
	}
	
	function initBtn(){
		//查询
		$("#btn_search").click(function(){
		    if(isSearchLineShow){
			    isSearchLineShow=false;
		        $("#invmatsnapshot_grid").iDatagrid("endSearch");
		    }
		    else{
		    	isSearchLineShow=true;
		       	$("#invmatsnapshot_grid").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(arg){
		       		isSearchMode = true;
					return {"search":JSON.stringify(arg)};
				}});
		    }
		});
		
		//生成库存快照
		$("#btn_snopshot,.btn_snopshot").click(function(){
				FW.remark(function(val){
					 $.ajax({
		     			url : basePath+"inventory/invmatsnapshot/saveAsSnapshot.do",
		     			type : 'POST',
		     			data: {
		     					"remark":val,
		     					"type":"M"
		     				  },
		     			dataType : "json",
		     			success : function(data) {
		     				if( data.result == "success" ){
		     					FW.success( "快照生成成功 ！");
		     					refCurPage();
		     				}else{
		     					FW.error( "快照生成失败 ！");
		     				}
		     			}
		     		});
				});
			});
	}
	
	var InvSnapshotPriv={
			init:function(){
				InvSnapshotPriv.set();
				InvSnapshotPriv.apply();
			},
			set:function(){//定义权限
				//生成库存快照
				Priv.map("privMapping.storeHistory_snopshot","storeHistory_snopshot");
			},
			apply:function(){//应用权限
				//应用
				Priv.apply();
			}
		};
</script>
</head>
<body style="height: 100%;" class="bbox list-page">
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
		<!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar1" class="btn-toolbar">
	        <div class="btn-group btn-group-sm">
	        	<button type="button" id="btn_snopshot" class="btn btn-success priv" privilege="storeHistory_snopshot">生成库存快照</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	        	<button type="button" id="btn_search" data-toggle="button" class="btn btn-default">查询</button>
	        </div>	        
	    </div>
	    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
	    <div id="pagination" class="toolbar-pager" bottompager="#bottomPager"></div>
	</div>
	
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div id="grid_wrap" style="width:100%">
	    <table id="invmatsnapshot_grid" pager="#pagination" class="eu-datagrid">
	    	<thead>
				<tr>
					<th data-options="field:'imsid',hidden:true"></th>
					<th data-options="field:'createdate',width:150,sortable:true,fixed:true">快照时间</th>
					<th data-options="field:'opertypeName',width:80,fixed:true,sortable:true">快照类型</th>
					<th data-options="field:'createuserName',width:100,fixed:true">操作人</th>
					<th data-options="field:'remark',width:90">备注</th>
				</tr>
			</thead>
	    </table>
	    <div id="noSearchResult" style="width: 100%;display:none">
			<span>没有查询到相关数据</span>
		</div>
	</div>
	
	<!-- 无数据 -->
	<div id="grid_error" style="display:none;width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">没有查询到相关数据</div>
			    <div class="btn-group btn-group-sm margin-element" style=''>
		                 <button type="button" class="btn btn-success btn_snopshot priv" privilege="storeHistory_snopshot">生成库存快照</button>
			    </div>
			</div>
		</div>
	</div>
	<!--大表需要加下分页器-->
	<div id="bottomPager" style="width:100%"></div>
</body>
</html>