<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>新建班次</title>
<script>_useLoadingMask = true;</script>

<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src='${basePath}js/operation/common/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/operation/common/commonForm.js?ver=${iVersion}'></script>
<script src='${basePath}js/operation/common/pageMode.js?ver=${iVersion}'></script>
<script src='${basePath}js/operation/common/stationCommon.js?ver=${iVersion}'></script>

<script>
//设置排序值
function setSortType( stationId ){
	var url = basePath + "operation/shift/querySortTypeByStationId.do?stationId=" + stationId;
	$.ajax({
		url : url,
		type : 'post',
		dataType : "json",
		success : function(data) {
			$("#f_sortType").val( data.result );
		}
	});
}

var fields = [
				{title : "班次编号", id : "num", type:"hidden"},
	  			{title : "班次名称", id : "name",rules : {required:true} },
	  			{title : "班次别名", id : "abbName",rules : {required:true} },
	  			{title : "班次类型", id : "type",rules : {required:true},
	  				type : "radio",
	  				data : [
		  				      ["normal","正常班",true],
		  				      ["rest","休息"],
		  				      ["study","学习"]
	  				      ]	
	  			},
	  			{title : "开始时间", id : "startTime",rules : {required:true,regex:"(([0-1][0-9])|(2[0-3]))([0-5][0-9])"},messages:{"regex":"请按照时间格式hhmm填写"} },
	  			{title : "上班时长", id : "longTime",rules : {required:true,digits:true,range:[1,24]} },
	  			{title : "是否可用", id : "isActive",rules : {required:true},
	  				 type : "radio",
	                  data : [
	                      ["Y","是",true],
	                      ["N","否"]
	                  ],	
	  			},
	  			{title : "排序", id : "sortType",rules : {required:true,digits:true}},
	  			{title : "工种", id : "stationId",rules : {required:true},
	                  type : "combobox"
	  			}
	  		];
	  		
	var opts={
		validate:true,
		fixLabelWidth:true
	};
	
	$(document).ready(function() {
		PageMode.objs.pageName="班次";
		PageMode.changeTitle("add");
		
		$("#autoform").iForm("init",{"options":opts,"fields":fields});
		//初始化 岗位
		stationOption(basePath, 'f_stationId',function(stationList){
			//setSortType( stationList[0][0] );
		},null,function(val) {
			setSortType(val);
		});
		
		$( "#saveButton" ).click(function(){
			if(!$("#autoform").valid()){
				return;
	      	}
			var formData = getFormData( "autoform" );
			var url = basePath + "operation/shift/insertShift.do";
			$.ajax({
				url : url,
				type : 'post',
				dataType : "json",
				data:{"formData":formData},
				success : function(data) {
					if( data.result == "success" ){
						FW.success( "新增班次成功 ！");
						closeTab();
					}else{
						FW.error( "新增班次失败 ！");
					}
				}
			});
		});
		
		$("#f_type_normal").on('ifChecked', function(event){
			$("#autoform").iForm("beginEdit",["startTime","longTime"]);
			$("#autoform").iForm("setVal",{"startTime" :"","longTime":""});
		});
		$("#f_type_rest").on('ifChecked', function(event){
			$("#autoform").iForm("setVal",{"startTime" :"0000","longTime":"24"});
			$("#autoform").iForm("endEdit",["startTime","longTime"]);
			
		});
		$("#f_type_study").on('ifChecked', function(event){
			//console.log(event);
			$("#autoform").iForm("beginEdit",["startTime","longTime"]);
			$("#autoform").iForm("setVal",{"startTime" :"","longTime":""});
		});
		
	});
		
</script>

</head>
<body>
<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <div id="toolbar" class="btn-toolbar">
	       <div class="btn-group btn-group-sm">
	       		<button type="button" class="btn btn-default" onclick="closeTab()">关闭</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-success" id="saveButton">保存</button>
            </div>
	    </div>
	</div>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div class="inner-title">
		新建班次
	</div>
	<form id="autoform"></form>
</body>
</html>