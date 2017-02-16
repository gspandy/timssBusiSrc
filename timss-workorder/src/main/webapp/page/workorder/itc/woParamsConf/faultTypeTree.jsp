<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html style="height:99%">
<head>
<title>故障类型</title>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
	 /* enbbed : 1：嵌入到主页面，0：显示到对话框（弹出框）中 */
	var embbed = '<%=request.getParameter("embbed")%>';
		
</script>
<style>
 .tree-sercharacter{
  background: url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAolJREFUeNp8U81PE1EQn7df3XZbQE2tlAQaKJWiiegBJH6EoyboSePJk4me/Dh68Wb8B0zUE4kXLiYSSRo5oDbQkBATCrVGcbVAAwixlO2H7e727a6zYCvoxkl+mdl58/vNm7fvEcuywLbYHXbHE4IAOIHhSVw5hMsEfR59khqQNHbL4cpTY8dzsN+QCxe9bT2nwxdu9UsdoYCmrlvF5UxuafJlpLi21I7r4wirTmDqgYkp7HLK29Z7ZuDuyPXm8LEIENOt6t95vWm1s+/m/Wvetu5zhgn9NeNPx30CuL2ByKXbg4ZZYUxTrxHCAcuKQKlCt5VZODp8ox/HOI8iDWuMQHeTgeaO7nZKSypqW5QWTE3bBNOsERQ0WkJdQewetMBJYHdbrKbljJ+VL5QQZodcLn/lWFZgeL6FgMVSFOD+J7CxOj+9JIXYaLm8WLU7M4yLcbmCRJI6hbWF5IpukHXLcjgDewTEm9TYqCyJxzVB8POi2MpKUhcjSWFWYDu0xOiTrEYhpjsdom2o/KmsKFMzI89mA4Fht88XZTyeEOPz9bomHj9aUPL5t1iT2stpCBRVAiWNAHYYW/6QzGbn0t8E4TDPcV5Onn2/nEnNZXUKo9UaQEV3EFBrDVDc4sTH6cl1JNsCfHpqcgNzr/AA7UMEx3vwOm1BFZVtqDqkMul5kRCW4F0gmfSCG0nzdfLzGfPfv/Dus4kAuHo2AH6/vzC9SGJ9CfdlWZbh4bi2MthpKXZdQjb33f2/3wK8SGzC0FAUoy2IRntA1wVwIjr9BTsWEQfi8fgRO5HL5aBQ+FFfb0UcRHj28kj9OeOw9nv2/S5qwm8v+geIINbcQ59HlBDbiALmqM37JcAABIc4sUmmya4AAAAASUVORK5CYII=");
  width:16px;
  height:18px;
  display:inline-block;
 }
</style>
<script>
	function initTree(){
		$("#faultType_tree").tree({
		 	url : basePath+ "workorder/woParamsConf/getFaultTypeTree.do",
			dnd : false,
			onSelect : function(node){
			//TODO 此处需要根据不同的情况，调用不同的动作
				if(embbed == '1'){  //嵌入到主页面
					var currFrame = FW.getFrame(FW.getCurrentTabId());
					currFrame.triggerFaultTypeTreeSelect(node);
				}else if(embbed == '0'){  //嵌入到弹出框或者对话框中
					var url = basePath + "workorder/workorder/parentWOList.do?embbed=0";
					url +="&selectTreeId="+node.id; 
					_parent().$("#itcDlgWorkOrderPage").attr("src",url);
				}else{
					var currFrame = FW.getFrame(FW.getCurrentTabId());
					currFrame.triggerFaultTypeTreeSelect(node);
				}
				/* var currFrame = FW.getFrame(FW.getCurrentTabId());
					currFrame.triggerFaultTypeTreeSelect(node); */
					
			}
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
				<ul id="faultType_tree" style="width:100%">
					
				</ul>
			</div>
		</div>
	</body>
	
</html>