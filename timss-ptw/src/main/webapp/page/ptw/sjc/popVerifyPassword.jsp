<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html style="min-height: 99%;">
<head>
<title>验证密码</title>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
	var _dialogEmmbed = true;
	var userName = ItcMvcService.user.getUserName();
	var desc = '<%=request.getParameter("desc")%>';
	var status = '<%=request.getParameter("status")%>';
	var statType = '<%=request.getParameter("statType")%>';
	$(document).ready(function() {
		$("#descSpan").html(desc);
		var fields = [
			{id:"userName",title : statType,type:"label",value:userName,linebreak:true}
		];
		if(status == 400){
			fields.push({id:"issueSuperNo",title : "签发值长",type:"combobox", linebreak:true,rules : {required:true},
				wrapXsWidth:10,wrapMdWidth:10,
				options:{remoteLoadOn:"init",url:basePath+ "ptw/ptwInfo/queryPtwUsersByGroup.do?role=YXZZ"}});
		}
		if(status == 700){
			fields.push({id:"finElecInfo",title : "已解除接电线数量", linebreak:true,rules : {required:true},wrapXsWidth:10,wrapMdWidth:10
				});
		}
		fields.push({id:"password",title : "密码",dataType:"password", linebreak:true,rules : {required:true},
			wrapXsWidth:10,wrapMdWidth:10,render:function(id){
				$("#" + id).keydown(function(e){
					var ev = e || window.event;
					if(e.keyCode == 13){
						e.preventDefault();
					}
				});
			}});
		$("#bizForm").iForm("init",{"fields" : fields,"options":{validate:true}});
	});
	
	function getFormData(){
		if(! $("#bizForm").valid()){
			return null;
		}
		var formData = $("#bizForm").iForm("getVal");
		if(status == 400){
			formData.issueSuper = $("#f_issueSuperNo").iCombo('getTxt');
		}
		return formData;
	}
</script>
</head>
<body style="height: 100%;" class="bbox">
	<div style="text-align: center;font-size: 14px;font-weight: bold;margin-top: 15px;margin-bottom: 10px;">
		<span id="descSpan">以上安全措施已正确完备，可执行。</span>
	</div>
	<form id="bizForm"></form>
</body>
</html>