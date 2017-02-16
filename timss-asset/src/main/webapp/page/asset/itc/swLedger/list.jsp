<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html style='height:99%'>
<head>
<title>title</title>
<script type="text/javascript">
_useLoadingMask=true;
</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script type="text/javascript" src='${basePath}js/asset/addTab.js?ver=${iVersion}'></script>
<script type="text/javascript" src="${basePath}js/asset/swLedgerList.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/asset/assetPriv.js?ver=${iVersion}"></script>
<script>
	$(document).ready(function(){
		SwLedgerPriv.init();
		SwLedger.init();
	});
	
	function exportSwLedgers(i){
		var exportPath=fileExportPath;
		//exportPath="http://10.133.96.77:8091/itc_report/";//测试用
		//exportPath="http://10.0.17.153:8080/itc_report/";
			
		var list=[//在这里添加要下载的链接
			exportPath+"preview?__report=report/ITSM-03-CM-01-03.rptdesign&__format=xls"
		];
		
		
		if(i<list.length){
			window.open(list[i]);
			if(i!=list.length-1)
				window.setTimeout('exportSwLedgers('+(i+1)+')',50);
		}
	}
	
</script>
<style type="text/css">
</style>
</head>
<body style="height: 100%;" class="bbox list-page">
	<div class="toolbar-with-pager bbox">
	    <div id="toolbar" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm" id="btnCreate">
		        <button class="btn-success btn priv" privilege="VIRTUAL-NEW" onclick="SwLedger.toCreate()">新建</button>
		    </div>
			<div class="btn-group btn-group-sm" id="btnSearch"> 
	        	<button onclick="SwLedger.toShowSearchLine()" class="btn btn-default">查询</button>
	        </div>
	        <div class="btn-group btn-group-sm" id="exportButtonDiv">
	            <button type="button" class="btn btn-default priv" privilege="AST_SWL_EXPORT" id="exportButton" onclick='exportSwLedgers(0);'>导出软件台账</button>
            </div>
	    </div>
	    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
	    <div id="pagination" class="toolbar-pager" bottompager="#bottomPager">        
	    </div>
	</div>
	
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div id="grid_wrap" style="width:100%">
	    <table id="swLedgerList" pager="#pagination" class="eu-datagrid">
	    </table>
	</div>
	<div id="noSearchResult" style="display:none;width:100%;">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">没有查询到相关软件台账</div>
			</div>
		</div>
	</div>
	<!-- 无数据 -->
	<div id="grid_empty" style="display:none;width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">没有查询到相关数据</div>
			    <div class="btn-group btn-group-sm margin-element">
		           <button class="btn-success btn priv" privilege="VIRTUAL-NEW" onclick="SwLedger.toCreate()">新建</button>
			    </div>
			</div>
		</div>
	</div>
	<!--大表需要加下分页器-->
	<div id="bottomPager" style="width:100%">
	</div>
</body>
</html>