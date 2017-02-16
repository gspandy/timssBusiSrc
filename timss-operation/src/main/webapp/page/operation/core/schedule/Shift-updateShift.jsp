<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>编辑班次</title>
<script>_useLoadingMask = true;</script>

<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src='${basePath}js/operation/common/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/operation/common/commonForm.js?ver=${iVersion}'></script>
<script src='${basePath}js/operation/common/stationCommon.js?ver=${iVersion}'></script>
<script src='${basePath}js/operation/common/pageMode.js?ver=${iVersion}'></script>

<script>
	var fields = [
				{title : "id", id : "id",type:"hidden"},
				{title : "班次编号", id : "num",type:"hidden" },
	  			{title : "班次名称", id : "name",rules : {required:true}},
	  			{title : "班次别名", id : "abbName",rules : {required:true}},
	  			{title : "班次类型", id : "type",rules : {required:true},
	  				type : "radio",
	  				data : [
		  				      ["normal","正常班",true],
		  				      ["rest","休息"],
		  				      ["study","学习"]
	  				      ]	
	  			},
	  			{title : "开始时间", id : "startTime",rules : {required:true,regex:"(([0-1][0-9])|(2[0-3]))([0-5][0-9])"},messages:{"regex":"请按照时间格式hhmm填写"} },
	  			{title : "上班时长", id : "longTime",rules : {required:true,digits:true,range:[1,24]}},
	  			{title : "是否可用", id : "isActive",rules : {required:true},
	  				 type : "radio",
	                  data : [
	                      ["Y","是"],
	                      ["N","否"]
	                  ],	
	  			},
	  			{title : "排序", id : "sortType",rules : {required:true,digits:true} },
	  			{title : "工种", id : "stationId",rules : {required:true},
	                  type : "combobox"
	  			}
	  			
	  		];
	
	var opts={
			validate:true,
			fixLabelWidth:true
		};
		
	//加载表单数据,在请求岗位数据的ajax返回时调用
	var rowData;
	function loadFormData( ){
		var data = {
				"id" : rowData == null ? "" : rowData.id,
				"num" : rowData == null ? "" : rowData.num,
				"name" : rowData == null ? "" : rowData.name,
				"abbName" : rowData == null ? "" : rowData.abbName,
				"startTime" : rowData == null ? "" : rowData.startTime,
				"longTime" : rowData == null ? "" : rowData.longTime,
				"isActive" : rowData == null ? "Y" : rowData.isActive,
				"sortType" : rowData == null ? "" : rowData.sortType,
				"stationId" : rowData == null ? "1" : rowData.stationId,
				"type" : rowData == null ? "normal" : rowData.type,
			};
			$("#autoform").iForm("setVal",data);
			$("#autoform").iForm("endEdit");
	}
	
	//加载班次
	function setFormData( ){
		var id = '${param.id}';
		var url = basePath + "operation/shift/queryShiftById.do?id=" + id;
		$.ajax({
			url : url,
			type : 'post',
			dataType : "json",
			success : function(data) {
				rowData = data.result;
					if (data.result != null ) {
						loadFormData( );
					} 
				}
			});
		
	}

	$(document).ready(function() {
		PageMode.objs.pageName="班次";
		PageMode.changeMode();
		
		$("#autoform").iForm("init",{"options":opts,"fields":fields});
		//初始化 岗位
		stationOption( basePath, 'f_stationId',setFormData);
		
		$( "#editButton" ).click(function(){
			PageMode.changeMode("edit");
		});
		
		$( "#cancelButton" ).click(function(){
			setFormData();
			PageMode.changeMode();
		});

		$("#updateButton").click(function() {
			if(!$("#autoform").valid()){
				return;
		    }
			var formData = getFormData("autoform");
			var url = basePath + "operation/shift/updateShift.do";
			$.ajax({
					url : url,
					type : 'post',
					dataType : "json",
					data:{"formData":formData},
					success : function(data) {
									if (data.result == "success") {
											FW.success("修改班次成功 ！");
											PageMode.changeMode();
									} else {
											FW.error("修改班次失败 ！");
									}
								}
							});
						});

				$("#deleteButton").click(function(){
					FW.confirm("确定删除本条数据吗？",function(){
							var formData = getFormData("autoform");
							var url = basePath + "operation/shift/deleteShift.do";
							$.ajax({
								url : url,
								type : 'post',
								dataType : "json",
								data:{"formData":formData},
								success : function(data) {
									if( data.result == "success" ){
										FW.success( "删除班次成功 ！");
										closeTab();
									}else{
										FW.error( "删除班次失败 ！");
									}
								}
							});
						});
				});
				
				$("#f_type_normal").on('ifChecked', function(event){
					$("#autoform").iForm("beginEdit",["startTime","longTime"]);
					$("#autoform").iForm("setVal",{"startTime" :rowData.startTime,"longTime":rowData.longTime});
				});
				$("#f_type_rest").on('ifChecked', function(event){
					$("#autoform").iForm("setVal",{"startTime" :"0000","longTime":"24"});
					$("#autoform").iForm("endEdit",["startTime","longTime"]);
					
				});
				$("#f_type_study").on('ifChecked', function(event){
					//console.log(event);
					$("#autoform").iForm("beginEdit",["startTime","longTime"]);
					$("#autoform").iForm("setVal",{"startTime" :rowData.startTime,"longTime":rowData.longTime});
				});

			});
</script>

</head>
<body>
<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <div id="toolbar" class="btn-toolbar">
	        <div class="btn-group btn-group-sm" id="closeButtonDiv">
	            <button type="button" class="btn btn-default" onclick="closeTab()">关闭</button>
	        </div>
	        <div class="btn-group btn-group-sm" id="cancelButtonDiv">
				<button type="button" class="btn-default btn" id="cancelButton">取消</button>
			</div>
	        <div class="btn-group btn-group-sm" id="editButtonDiv">
				<button type="button" class="btn btn-default" id="editButton">编辑</button>
	        </div>
	        <div class="btn-group btn-group-sm" style="display: none;" id="updateButtonDiv">
				<button type="button" class="btn btn-success" id="updateButton" >保存</button>
	        </div>
	        <div class="btn-group btn-group-sm" id="deleteButtonDiv">
				<button type="button" class="btn btn-default" id="deleteButton">删除</button>
	        </div>
	    </div>
	</div>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div class="inner-title">
		编辑班次
	</div>
	<form id="autoform"></form>
</body>
</html>