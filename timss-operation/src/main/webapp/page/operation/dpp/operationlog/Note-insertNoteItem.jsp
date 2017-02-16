<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>新建运行记事</title>

<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_dialogEmmbed=true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src='${basePath}js/operation/common/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/operation/common/commonForm.js?ver=${iVersion}'></script>
<script src='${basePath}js/operation/common/formatterDate.js?ver=${iVersion}'></script>

<script>
var siteId = '${siteId}';

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

/**
*创建自定义表单项（X时X分）
*by 棋哥
*id 字段ID， flag =0 hour flage=1 min
*/
function getTimeSelector(id,flag){
	var s = "<div style='width:40%;position:relative;float:left;padding-right:8px;'>";
	selId = id + "_" + (flag==0?"hour":"min");
	s += "<select id='" + selId + "' style='width:100%'>";
	var max = flag==0?23:59;
	var date = new Date();
	var hour = date.getHours();
	var min = date.getMinutes();
	for(var i=0;i<=max;i++){
		var sel = ((flag==0 && i==hour) || (flag==1 && i==min))?"selected='selected'":"";
		s += "<option value='" + i + "' " + sel + ">" + i + "</option>";
	}
	s += "</select>";
	var lbl = flag==0?"时":"分";
	//s += "<span style='position:absolute;top:0px;right:8px;width:32px' class='ctrl-label'>" + lbl + "</span>";
	s += "</div>"+"<span class='ctrl-label' style='position:relative;float:left;padding-right:16px;'>"+lbl+"</span>";
	return s;
}

//运行记事
var noteFields = [
        {
			title : "记事时间", 
			id : "writeTimeStr", 
			type : "userdefine",
			rules : {required:true}
        },
		{	
			title : "机组号", 
			id : "crewNum",
			type : "combobox",
			dataType : "enum",
			enumCat : "OPR_CREW_NUM",
			options:{
				allowEmpty:false,
				firstItemEmpty : true
			}
		},
		{	title : "记录类型",
			id : "type",
			type : "combobox",
			dataType : "enum",
			enumCat : "OPR_NOTE_TYPE",
			options:{
				allowEmpty:false,
				firstItemEmpty : true
			}
		},
		{	title : "记事内容", 
			id : "content",
			type : "textarea",
	        linebreak:true,
	        rules : {required:true,maxChLength : parseInt(4000*2/3)},
	        wrapXsWidth:12,
	        wrapMdWidth:8,
	        height : 440
		},
		{
			title : "ID", 
			id : "id", 
			type : "hidden"
        }
	];

//自定义表单项
noteFields[0].getHtml = function(id){
	return getTimeSelector(id,0) + getTimeSelector(id,1); 
};

noteFields[0].render = function(id){
	$("#" + id + "_hour").iCombo("init");
	$("#" + id + "_min").iCombo("init");
};

noteFields[0].getValue = function(id){
	return 	$("#" + id + "_hour").iCombo("getVal") + ":" + $("#" + id + "_min").iCombo("getVal"); 
};

noteFields[0].setValue = function(val,id){
	var _val = val.split(":");
	$("#" + id + "_hour").iCombo("setVal",_val[0]);
	$("#" + id + "_min").iCombo("setVal",_val[1]);
};

	  		
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
		return $("#noteForm").valid()/*  & flag */;
	}
	
	//用户点击一条记事内容时把数据加载到noteForm
	function loardNoteForm( url ){
		$.ajax({
			url : url,
			type : 'post',
			dataType : "json",
			success : function(data) {
				var rowData = data.result;
				var data = {
						"writeTimeStr" : rowData.writeTimeStr,
						"id" : rowData.id,
						"crewNum" : rowData.crewNum,
						"type" : rowData.type,
						"content" : rowData.content,
					};
					$("#noteForm").iForm("setVal",data);
			}
		});
	}
	
	$(document).ready(function() {
		var userJobs=FW.get("opr_note_userJobs");
		if(userJobs.length>0){
			noteFields.push({	
				title : "引用到", 
				id : "jobsId",
				type : "combobox",
				data: userJobs,
				rules : {required:true},
				options:{
					allowEmpty:false,
					firstItemEmpty : true
				}	
			});
		}
		$("#noteForm").iForm("init",{"options":opts,"fields":noteFields});
		$("#noteForm").iForm("hide",["type",'writeTimeStr','crewNum']);
		
		var noteId = '${noteId}';
		if( noteId != '' && noteId > 0 ){
			var url = basePath + "operation/note/queryNoteById.do?id=" + noteId;
	    	loardNoteForm( url );
		}
	});
		
</script>

</head>
<body>
	<div id="noteDiv">
		<form  id="noteForm" class="autoform" ></form>
	</div>
</body>
</html>