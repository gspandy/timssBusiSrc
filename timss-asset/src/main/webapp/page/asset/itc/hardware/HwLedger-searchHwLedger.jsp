<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>硬件台账多条件查询</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src='${basePath}js/asset/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/asset/commonForm.js?ver=${iVersion}'></script>

<script>
//当前选中节点的ID
var hwId = '${ hwId }';
//当前选中节点的名字
var hwName = '${ hwName }';

var data = [
	["","全部",true],
	["HW_L_SERVER","服务器"],
	["HW_L_VM","虚机"],
	["HW_L_ROOM_EQPT","机房设备"],
	["HW_L_NETWORK","网络设备"],
	["HW_L_STORAGE","存储设备"]
 ];

var fields = [
				{title : "类型", id : "hwType",
					type : "combobox",
					data : data					
				},
				{title : "名称", id : "hwName" },
				{title : "IP", id : "ip" },
				/* {title : "型号", id : "model" }, */
	  			{title : "投入运行时间 ", id : "toUseTime",
	  				type : "date"
	  			}
	  		];
	  		
	  		
	var opts={
		validate:true,
		fixLabelWidth:true,
		xsWidth:11,
		mdWidth:11
	};
	

	//为空判断函数
	function isNull( arg1 ){
		return !arg1 && arg1!==0 && typeof arg1!=="boolean"?true:false;
	}
	
	
	//校验
	function valid(){
		
		return $("#hwLedgerItemFrom").valid();
	}
	
	//拿到form data
	function formData(){
		 var data = $("#hwLedgerItemFrom").iForm("getVal");
		 data.hwId = hwId;
		 return data;
	}
	
	$(document).ready(function() {
		$("#hwLedgerItemFrom").iForm("init",{"options":opts,"fields":fields});
		
	});
		
</script>

</head>
<body>
	<div class="inner-title">
		在"${ hwName }"下查询
	</div>
	<div class="margin-title-table">
		<form id="hwLedgerItemFrom"></form>
	</div>
</body>
</html>