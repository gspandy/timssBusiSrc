<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html style="height:99%">
<head>
<title>故障类型</title>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />

<script>
	function initTree(){
		$("#samplefaultType_tree").tree({
		 	url : basePath+ "workorder/woParamsConf/getFaultTypeTree.do?treeType=SD",
		 	checkbox:true,
			dnd : false,
		});		
	}

	$(document).ready(function() {
		initTree();
	});

	
</script>

</head>
	<body class="bbox" style="height:100%;width:100%">
		<div style="padding-top:10px;width:100%;height:100%">
			<div style="width:100%;height:100%;overflow:auto">
				<ul id="samplefaultType_tree" style="width:100%">
					
				</ul>
			</div>
		</div>
	</body>
	
</html>