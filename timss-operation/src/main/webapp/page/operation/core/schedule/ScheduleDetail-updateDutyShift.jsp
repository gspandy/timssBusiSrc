<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>更新日历</title>
<script>_dialogEmmbed=true;</script>

<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script type="text/javascript" src="${basePath}js/operation/common/pageDetail.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/operation/common/pageMode.js?ver=${iVersion}"></script>
<style type="text/css">
	.schedulePerson{
		width:80px;display:inline-block;
	}
</style>
<script>
var editAble='${param.canEdit}'=='1';

function getDataForSubmit(){
	var tags=$("#showUser").iTags("getVal");
	var persons=[];
	$.each(tags,function(index,obj){
		persons.push(obj.id);
	});
	return persons;
}

	$(document).ready(function() {
		if(editAble){
			$("#search").ITCUI_HintList({
				"datasource":basePath + "operation/duty/queryDutyPersonHint.do",
				"getDataOnKeyPress":true,
				"clickEvent":function(id,name){
					/* if(schedulePersonMap[id]){
						FW.error("该工号已经在值班人员列表中");
					}else{ */
						$("#showUser").iTags("append", {id:id,title:name});
					/* } */
				},
				"showOn":"input",
				"highlight":true,
				"formatter" : function(id,name){
					return name + " / " + id;
				}
			});
		}else{
			$("#showUserLabel").show();
			$("#searchUserWrap").hide();
		}
		
		PageDetail.afterLoadData=function(){
			var tags=[];
			$.each(PageDetail.objs.form.queryData["schedulePerson"]||[],function(index,person){
				tags.push({
					id:person.userId,
					title:person.userName/* ,
					klass:person.isPresent=="Y"?"label-info":"label-default" */
				});
			});
			if(!editAble&&tags.length==0){
				$("#showUser").html("<div style='text-align: center;'>没有值班人员</div>");
			}else{
				$("#showUser").iTags("init", {data:tags,klass:"label-info"});
				if(editAble){
					$("#showUser").iTags("beginEdit");
				}else{
					$("#showUser").iTags("endEdit");
				}
			}
		}
		
		PageDetail.getDataForQuery=function(){
			return {
				date:"${param.date}",
				stationId:"${param.stationId}",
				dutyId:"${param.dutyId}"
			};
		}
		
		PageDetail.init({
			url:{
				query:basePath+"operation/scheduleDetail/getCalendarByDSDWithPerson.do"
			},
			mode:"view",//默认，可选view/create/edit
			form:{
				id:"autoform",
				opts:{
					labelFixWidth:60
				},
				fields:[
					{title:"ID",id:"id", type:"hidden"},
					{title:"日期",id:"dateTime", type:"date",dataType:"date"},
					{title:"工种",id:"deptName",type:"label"},
					{title:"值别",id:"dutyName",type:"label"},
					{title:"班次",id:"shiftName",type:"label",formatter:function(val){
						if(PageDetail.objs.form.bean["startTime"]&&PageDetail.objs.form.bean["longTime"]){
							var sth=PageDetail.objs.form.bean["startTime"].substr(0,2);
							var stm=PageDetail.objs.form.bean["startTime"].substr(2,4);
							var eth=(parseInt(sth)+PageDetail.objs.form.bean["longTime"])%24;
							return val+"("+sth+":"+stm+"-"+(eth<10?"0":"")+eth+":"+stm+")";
						}else{
							return val;
						}
					}}
				],//required
				idField:"id"//required
			}
		});
	});
</script>

</head>
<body>
	<form id="autoform"></form>
	<div id="showUserLabel" style="margin-top:4px;width:100%;display:none" class="bbox">
		<label class="ctrl-label pull-left" style="width:60px;">值班人员：</label>
	</div>
	<div id="searchUserWrap" style="margin-top:4px;width:100%" class="bbox">
		<label class="ctrl-label pull-left" style="width:60px;">添加人员：</label>
		<div class="input-group-sm pull-left">
		    <input type="text" placeholder="输入姓名或工号" class="form-control" style="width:200px" id="search">
		</div>
	</div>
	<div id="showUser" style="margin-top:4px;width:100%;float:left;font-size:12px;" class="bbox"></div>
</body>
</html>