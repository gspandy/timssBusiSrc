<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
 	String path = request.getContextPath();
 	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
 	session.getAttribute("");
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<style type="text/css">
#f_message{
	width:280px!important;
}
.itcui_combo .bbox{
   width:280px!important;
}
</style>
<script type="text/javascript" src="${basePath}/js/homepage/homepageService.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}/js/workflow/showAudit.js?ver=${iVersion}"></script>
<script>
 var _dialogEmmbed = true;
 var userId; 
 var taskId = getUrlParam('taskId');
 var updateDesc = getUrlParam('updateDesc');
 var multiSelect = getUrlParam('multiSelect');
 if(updateDesc == null ||updateDesc == undefined||updateDesc == 'undefined'||updateDesc == 'null'){
     updateDesc = "";
  }
  if(multiSelect == 0){
     multiSelect = false;
  }else{
     multiSelect = true;
  }
  ItcMvcService.getUserInfo(function(Object){
  	userId = Object.userId;
  });
 
 var htAssigneeName;
 var htAssignee;
 var historyTasks;
 var oldmessage = '退回。';
 var nextTasks;
 $(document).ready(function() {
    //修改对话框的标题
	var taskId = getUrlParam('taskId');
	var url = basePath + "/workflow/process_inst/getTaskDetails.do";
	var param = {};
	param['taskId'] = taskId;
	$.post(url, param,function(data) {
		var auditDefault = data.elementInfo.auditDefault != undefined? data.elementInfo.auditDefault:oldmessage;
		nextTasks = data.nextTasks;
		oldmessage = auditDefault;
		var hasParentPorcess = data.hasParentProcess;
		var auditForm1Fields = [{
			id: "processInstId",
			type: "hidden"
		},
		{
			id: "taskId",
			type: "hidden"
		},
		{
			id: "owner",
			type: "hidden"
		},
		{
			title: "审批意见",
			id: "message",
			type: "textarea",
			linebreak: true,
			wrapXsWidth: 11,
			wrapMdWidth: 11,
			height: 55,
			render: function(id) {
				$('#' + id).on('focus',
				function(event) {
					var message = $('#form1').iForm('getVal', 'message');
					if (message == '退回。' || message == auditDefault || message == '终止。') {
						oldmessage = message;
					}
				});
				$('#' + id).on('blur',
				function(event) {
					var message = $('#form1').iForm('getVal', 'message');
					if (message == '') {
						$('#form1').iForm('setVal', {
							'message': oldmessage
						});
					}
				});
			}
		},
		{
			title: "操作",
			id: "operate",
			type: "label",
			linebreak: true,
			wrapXsWidth: 10,
			wrapMdWidth: 10,
			render: function(id) {
				$('#form3').show();
				$('#form1').iForm('setVal', {
					'message': '退回。','operate':'退回'
				});
				//重绘对话框的高度
				_parent().$("#itcDlgShowRevert").dialog("resize",{height:220+70});
			}
		}
		];
		var auditForm3Fields = [];
		var data1s = {
			"processInstId": data.processInstId,
			"taskId": data.taskId,
			"owner": data.owner,
			"message": auditDefault
		};
		$("#form1").iForm("init", {
			"options": {
				labelFixWidth: 100
			},
			"fields": auditForm1Fields
		});
		$("#form1").ITC_Form("loaddata", data1s);

		var data2s = {};
		var usersize = [];
		historyTasks = toComboboxJson(data.historicTasks, "defKey", "name");
		htAssignee = toComboboxJson(data.historicTasks, "defKey", "assignee");
		htAssigneeName = toComboboxJson(data.historicTasks, "assignee", "assigneeName");
		var htLabel = {
			title: "退回环节",
			id: "historyTask",
			type: "combobox",
			linebreak: true,
			wrapXsWidth: 8,
			wrapMdWidth: 8,
			data: historyTasks,
			options: {
				'initOnChange': false,
				'onChange': function(val) {
					$("#form3").iForm('setVal', {
						'htAssignee': htAssignee[val]
					});
				}
			}
		};
		htLabel.render = function(id) {};
		var htaLabel = {
			title: "办理人",
			id: "htAssignee",
			type: "combobox",
			linebreak: true,
			wrapXsWidth: 8,
			wrapMdWidth: 8,
			data: htAssigneeName
		};
		auditForm3Fields.push(htLabel);
		auditForm3Fields.push(htaLabel);
		$("#form3").iForm("init", {
			"options": {
				labelFixWidth: 100
			},
			"fields": auditForm3Fields
		});
		$("#form3").iForm("setVal", {
			"htAssignee": htAssignee[$("#form3").iForm('getVal', 'historyTask')]
		});
		$("#form3").iForm("endEdit", 'htAssignee');
	});
	});

	function submit(businessData, agree, rollback, stop) {
		var data1 = $("#form1").ITC_Form("getdata");
		var data3 = $("#form3").ITC_Form("getdata");
		var rowdata = {};
		var opr = "rollback";
		var url = '';
		rowdata["businessData"] = businessData;
		if (opr == 'rollback') {
			rowdata["processInstId"] = data1["processInstId"];
			rowdata["message"] = data1["message"]=="退回。"?"退回。":data1["message"];
			rowdata["destTaskKey"] = data3["historyTask"];
			rowdata["assignee"] = userId;
			rowdata["owner"] = userId;
			rowdata["userId"] = data3["htAssignee"];
			//组装审批说明:操作+描述+业务修改说明
			//20141121 领导要求将审批说明改为描述+业务修改说明
			rowdata["message"] = rowdata["message"]+" "+ updateDesc;
			url = basePath + "workflow/process_inst/rollback.do";

			$.post(url, rowdata, function(data) {
				var result = data.result;
				if (result == "ok") {
					_parent().$("#itcDlgShowRevert").dialog("close");
					if(_parent().$("#itcDlgRevertInfo:visible").length != 0){
					   _parent().$("#itcDlgRevertInfo").dialog("close"); 
					}
					FW.success("退回成功");
					homepageService.refresh();
					if (typeof (eval(rollback)) == "function") {
						rollback();
					}
				} else {
					FW.success("退回失败");
				}
			});
		} 
	}

	function getForm() {
		var data1 = $("#form1").ITC_Form("getdata");
		var data3 = $("#form3").ITC_Form("getdata");
		var rowdata = {};
		var opr = "rollback";
		if (opr == 'rollback') {
			rowdata["processInstId"] = data1["processInstId"];
			rowdata["message"] = data1["message"];
			rowdata["destTaskKey"] = data3["historyTask"];
		}
		return rowdata;
	}

	function toComboboxJson(data, key1, key2) {
		var result = {};
		for ( var j = 0; j < data.length; j++) {
			result[data[j][key1]] = data[j][key2];
		}
		return result;
	}

	function toComboboxJson1(data, key1, key2,checkall) {
		var result = [];
		var select;
		if(data.length == 1||checkall == '1'){
		    select = true;
		}else{
		    select = false;
		}
		for ( var j = 0; j < data.length; j++) {
			result[j] = [ data[j][key1], data[j][key2], select ];
		}
		return result;
	}

	function delObject(json, key1) {
		for ( var key in json) {
			if (key.indexOf(key1) != -1) {
				delete json[key];
			} else {
				json[key] = $.trim(json[key]).split(' ');
			}
		}
		return json;
	}

    /**
    *校验json是否存在为空的值
    *
    */
    function verifyJSON(json) {
		for ( var key in json) {
			if(json[key] == ""){
			    return false;
			}
		}
		return true;
	}
	/**
	 * 查询url参数值
	 * @param name url参数名称
	 * @returns
	 */
	function getUrlParam(name) {
		var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
		var r = window.location.search.substr(1).match(reg);
		if (r != null){
			return decodeURIComponent(r[2]);
		}	
		return null;
	}
	
</script>
</head>
<body>
<div style="margin-top:33px;"></div>
	<div class="bbox" id="toolbar_wrap" style="margin-left:15px;">
      <form id="form1"></form>
      <form id="form3" style="display:none;"></form>  
</div>
   
</body>
</html>