<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>编辑上级通知</title>
<script>_useLoadingMask = true;</script>

<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src='${basePath}js/operation/common/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/operation/common/commonForm.js?ver=${iVersion}'></script>

<script>
//对Date的扩展，将 Date 转化为指定格式的String
//月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符， 
//年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字) 
//例子： 
//(new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423 
//(new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
Date.prototype.Format = function (fmt) { //author: meizz 
var o = {
    "M+": this.getMonth() + 1, //月份 
    "d+": this.getDate(), //日 
    "h+": this.getHours(), //小时 
    "m+": this.getMinutes(), //分 
    "s+": this.getSeconds(), //秒 
    "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
    "S": this.getMilliseconds() //毫秒 
};
if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
for (var k in o)
if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
return fmt;
};

	var fields = [
	{title : "id", id : "id",type:"hidden"},
	{title : "siteId", id : "siteId",type:"hidden"},
	{title : "编号", id : "num",type:"hidden"},
	{title : "发布时间", id : "writeDate",type:"label",
		formatter:function(val){
			return new Date(val).Format("yyyy-MM-dd hh:mm");
		}	
	},
	{title : "领导", id : "leaderUserName",rules : {required:true},
			render: function(id){
			$("#" + id).iHint("init",{
				datasource :"${basePath}" + "/user?method=hint",
				highlight : true,
				clickEvent : function(id,name){
					$("#f_leaderUserName").val(name);
					var leaderUserId = id.split("_")[0];
					$("#f_leaderUserId").val(leaderUserId);
				}
			});
		}
	},
	{title : "创建人", id : "writeUserName", type : "label"},
	{title : "领导工号", id : "leaderUserId",rules : {required:true}, type : "hidden"},
	/* {title : "接话人", id : "executeUserName",rules : {required:true} ,
			render: function(id){
			$("#" + id).iHint("init",{
				datasource :"${basePath}" + "/user?method=hint",
				highlight : true,
				clickEvent : function(id,name){
					$("#f_executeUserName").val(name);
					var executeUserId = id.split("_")[0];
					$("#f_executeUser").val(executeUserId);
				}
			});
		}
	}, */
	{title : "接话人", id : "executeUserId",rules : {required:true}, type : "hidden"},
	{title : "通知内容", id : "content", rules : {required:true}, breakAll : true,
		type : "textarea",linebreak:true,wrapXsWidth : 8,wrapMdWidth : 8,height : 50
	},
	{title : "是否落实", id : "isFinished",rules : {required:true},linebreak : true,
			 type : "radio",
         data : [
             ["Y","是"],
             ["N","否"]
         ],	
	},
	{title : "落实时间", id : "finishTime",type:"datetime"},
	{title : "落实情况", id : "feedBackContent", rules : {required:true},breakAll : true,
		type : "textarea",linebreak:true,wrapXsWidth : 8,wrapMdWidth : 8,height : 50
	},
	];
	
	var opts={
			validate:true,
			fixLabelWidth:true
		};
		
	var isFinished=false;
		
	//加载表单数据,在请求岗位数据的ajax返回时调用
	var rowData;
	function loadFormData(){
		var id = '<%=request.getParameter("id") %>';
		var url = basePath + "operation/leaderNotice/queryLeaderNoticeById.do?id=" + id;
		$.ajax({
			url : url,
			type : 'post',
			dataType : "json",
			success : function(data) {
				rowData = data.result;
				
				if (rowData != null ) {
					var data = {
						"id" : rowData == null ? "" : rowData.id,
						"siteId" : rowData == null ? "" : rowData.siteId,
						"finishTime" : rowData == null ? "" : rowData.finishTime,
						"writeDate" : rowData == null ? "" : rowData.writeDate,
						"num" : rowData == null ? "" : rowData.num,
						"executeUserId" : rowData == null ? "" : rowData.executeUserId,
						"executeUserName" : rowData == null ? "" : rowData.executeUserName,
						"leaderUserId" : rowData == null ? "" : rowData.leaderUserId,
						"leaderUserName" : rowData == null ? "" : rowData.leaderUserName,
						"content" : rowData == null ? "" : rowData.content,
						"feedBackContent" : rowData == null ? "" : rowData.feedBackContent,
						"isFinished" : rowData == null ? "N" : rowData.isFinished,
						"writeUserName" : rowData == null ? "" : rowData.writeUserName,
					};
					$("#autoform").iForm("setVal",data);
					isFinished=rowData.isFinished == 'Y';
					changeShow();
				} 
			}
		});
	}

	function changeShow(type){//view/edit/finish
		if(!type){
			type="view";
		}
		$( "#editButtonDiv,#editButton2Div,#cancelButtonDiv,#closeButtonDiv" ).hide();
		if("view"==type){
			$( "#closeButtonDiv" ).show();
			$("#autoform").iForm("endEdit");
			$("#deleteButtonDiv,#updateButtonDiv").hide();
			if(!isFinished){
				$( "#editButtonDiv,#editButton2Div" ).show();
			}
		}else if("edit"==type){
			$("#deleteButtonDiv,#updateButtonDiv,#cancelButtonDiv").show();
			$("#autoform").iForm("beginEdit",["leaderUserId","content"]);
		}else if("finish"==type){
			$("#deleteButtonDiv,#updateButtonDiv,#cancelButtonDiv").show();
			if($("#autoform").iForm("getVal","isFinished") == "Y"){
				$("#autoform").iForm("beginEdit",["feedBackContent","isFinished","finishTime"]);
			}else{
				$("#autoform").iForm("beginEdit",["feedBackContent","isFinished"]);
			}
		}
		FW.fixRoundButtons("#toolbar");
	}
	
	$(document).ready(function() {
		
		$("#autoform").iForm("init",{"options":opts,"fields":fields});
		loadFormData();
		/* //初始化 岗位
		stationOption( basePath, 'f_stationId',loadFormData); */
		
		//修改通知内容
		$( "#editButton" ).click(function(){
			changeShow("edit");
		});
		
		//修改落实情况
		$( "#editButton2" ).click(function(){
			changeShow("finish");
		});

		$( "#cancelButton" ).click(function(){
			loadFormData();
		});
		
		$("#updateButton").click(function() {
			if(!$("#autoform").valid()){
				return;
			}
			if($("#f_executeUser").val()=="" || $("#f_leaderUserId").val()==""){
				FW.error( "您输入的领导或交接人有误");
				return;
			}
			var formData = getFormData("autoform");
			var url = basePath + "operation/leaderNotice/updateLeaderNotice.do";
			$.ajax({
					url : url,
					type : 'post',
					dataType : "json",
					data :{
						formData : formData
					},
					success : function(data) {
									if (data.result == "success") {
											FW.success("修改上级通知成功 ！");
											loadFormData();
									} else {
											FW.error("修改上级通知失败 ！");
									}
								}
							});
						});

				$("#deleteButton").click(function(){
					FW.confirm("确定删除本条数据吗？",function(){
							var formData = getFormData("autoform");
							var url = basePath + "operation/leaderNotice/deleteLeaderNotice.do";
							$.ajax({
								url : url,
								type : 'post',
								dataType : "json",
								data :{
									formData : formData 
								},
								success : function(data) {
									if( data.result == "success" ){
										FW.success( "删除上级通知成功 ！");
										closeTab();
									}else{
										FW.error( "删除上级通知失败 ！");
									}
								}
							});
						});
				});
				
				$("#f_isFinished_N").on('ifChecked', function(event){
					$("#autoform").iForm("setVal",{"finishTime" : null,"feedBackContent" : "尚未落实"});
					$("#autoform").iForm("endEdit",["finishTime"]);
				});
				
				$("#f_isFinished_Y").on('ifChecked', function(event){
					$("#autoform").iForm("beginEdit",["finishTime"]);
					var d = new Date().getTime();
					$("#autoform").iForm("setVal",{"finishTime" : d,"feedBackContent" : "已落实工作"});
				});
				
			});
</script>

</head>
<body>
<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <div id="toolbar" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm" id="closeButtonDiv">
	            <button type="button" class="btn btn-default" id="closeButton" onclick="closeTab()">关闭</button>
	        </div>
	        <div class="btn-group btn-group-sm" id="cancelButtonDiv">
	            <button type="button" class="btn btn-default" id="cancelButton">取消</button>
	        </div>
             <div class="btn-group btn-group-sm" id="deleteButtonDiv">
				<button type="button" class="btn btn-default" id="deleteButton">删除</button>
             </div>
             <div class="btn-group btn-group-sm" id="updateButtonDiv">
				<button type="button" class="btn btn-success" id="updateButton">保存</button>
             </div>
             <div class="btn-group btn-group-sm" id="editButtonDiv">
				<button type="button" class="btn btn-default" id="editButton">编辑</button>
             </div>
             <div class="btn-group btn-group-sm" id="editButton2Div">
				<button type="button" class="btn btn-default" id="editButton2">完工</button>
             </div>
	    </div>
	</div>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div class="inner-title">
		上级通知
	</div>
	<form id="autoform"></form>
</body>
</html>