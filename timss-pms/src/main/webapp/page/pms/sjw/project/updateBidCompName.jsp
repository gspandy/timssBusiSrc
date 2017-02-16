<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>更新邀请/询价候选单位</title>
<script>_dialogEmmbed=true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<style type="text/css">
	#showBidComp .tag-item {
		width:180px;
	}
</style>
<script>
	var id = '${id}';
	var bidComps = '${bidComps}';
	var editable = '${editable}'=="true";
	//获取提交数据
	function getDataForSubmit(){
		var tags=$("#showBidComp").iTags("getVal");
		var comps=[];
		$.each(tags,function(index,obj){
			comps.push({"key":obj.id,"value":obj.title});
		});
		return comps;
	}

	$(document).ready(function() {
		if(editable){
			$("#search").ITCUI_HintList({
				"datasource":basePath + "/pms/supplier/queryFuzzyByName.do",
				"getDataOnKeyPress":true,
				"clickEvent":function(id,name){
					var tags=$("#showBidComp").iTags("getVal");
					if(null==tags||tags.length<5){
						$("#showBidComp").iTags("append", {id:id,title:name});	
					}else{
						FW.error("最多选择5个单位");	
					}
				},
				"showOn":"input",
				"highlight":true,
				"formatter" : function(id,name){
					return name + " / " + id;
				}
			});
		}else{
			$("#showBidCompLabel").show();
			$("#searchBidCompWrap").hide();
		}
		
		var tags=[];		
		if(JSON.parse(bidComps).length>0){
			$.each(JSON.parse(bidComps)||[],function(index,comp){
				tags.push({
					id:comp.id,
					title:comp.title
				});
			});	
		}
		if(!editable&&tags.length==0){
			$("#showBidComp").html("<div style='text-align: center;'>没有邀请/询价候选单位</div>");
		}else{
			$("#showBidComp").iTags("init", {data:tags,klass:"label-info"});
			if(editable){
				$("#showBidComp").iTags("beginEdit");
			}else{
				$("#showBidComp").iTags("endEdit");
			}
		}
	});
		
</script>

</head>
<body>
	<div id="searchBidCompWrap" style="margin-top:4px;width:100%" class="bbox">
		<label class="ctrl-label pull-left" style="width:60px;">公司名称：</label>
		<div class="input-group-sm pull-left">
		    <input type="text" placeholder="输入单位名称" class="form-control" style="width:300px" id="search">
		</div>
	</div>
	<div id="showBidComp" style="margin-top:4px;width:100%;float:left;font-size:12px;" class="bbox"></div>
</body>
</html>