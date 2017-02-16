<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.yudean.itc.util.Constant" %>
<%
	String g = (String)request.getAttribute("g");
	String from = request.getParameter("from");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<link rel="stylesheet" type="text/css"
	href="${basePath}css/public_background.css?ver=${iVersion}" media="all" />	
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>选择审批人员</title>
<script>
	var currSearchUser = null;
	var currExpandNode = null;
	var currNode = null;
	var expandArr = null;
	var chkUsers = {};
	var fromRole = "<%=from%>"=="role";
	var curTabId = "${curTabId}";
	$(document).ready(function(){
		//本文件与itc_authority_front的jsp是基本一致的，就是itc_authority_front中frame确定了必须是编辑角色的Tab，导致其他页面初始化异常。
		var frame = curTabId;
		var p = _parent().window.document.getElementById(frame).contentWindow;
		for(var k in p.users){
			chkUsers[k] = p.users[k];
		}
		initTree();
		initHintList();
		
	});
	
	function initTree(){
		var opts = {
			url : basePath + "tree?method=org&onelevel=true"	
		};
		if(fromRole){
			opts.url += "&ignoresite=1";
		}
		opts["loadFilter"] = function(data,parent){
			for(var i=0;i<data.length;i++){
				var node = data[i];
				if(node.id.indexOf("user_")>=0){
					var uid = node.id.replace('user_',"");
					if(chkUsers[uid]){
						node.checked = true;
					}
				}				
			}
			return data;
		};
		opts["onContextMenu"] = function(e, node){
			e.preventDefault();
			if(node.id.indexOf("org_")>=0){
				$("#menu_tree").css({
					left:e.pageX,
					top:e.pageY
				});
				$("#menu_tree_toggle").dropdown("toggle");
				currNode = node;
			}
		};
		opts["checkbox"] = true;
		opts["onlyLeafCheck"] = true;
		opts["onExpand"] = function(node){
			if(expandArr!=null && expandArr.length>0){
				var id = expandArr.pop();
				var node = $("#userTree").tree("find","org_" + id);
				$("#userTree").tree("expand",node.target);
			}
			else if(currSearchUser!=null){
				var node = $("#userTree").tree("find","user_" + currSearchUser);
				currSearchUser = null;
				$("#userTree").tree("select",node.target);
			}
		};
		opts["onCheck"] = function(node, checked){
			var id = node.id.replace("user_","");
			if(checked){
				chkUsers[id] = node.text;
			}
			else{
				delete(chkUsers[id]);
			}
		};
		$("#userTree").tree(opts);
	}
	
	function getChecked(){
		return chkUsers;
	}
	
	function initHintList(){
		$("#searchUserWrap").show();
		var opts = {
			"datasource":basePath + "user?method=hint",
			"getDataOnKeyPress":true,
			"clickEvent":function(id,name){
				var arr = id.split("_");
				$.ajax({
					url : basePath + "user?method=searchorg&id=" + arr[1],
					dataType:"json",
					success : function(data){
						$("#userTree").tree("collapseAll");
						expandArr = data;
						currSearchUser = arr[0];
						var id = expandArr.pop();
						var node = $("#userTree").tree("find","org_" + id);
						$("#userTree").tree("expand",node.target);
					}
				});
			},
			"showOn":"input",
			"highlight":true,
			"formatter" : function(id,name){
				return name + " / " + id.split("_")[0];
			}
		};
		if(fromRole){
			opts.datasource += "&ignoresite=1";
		}
		$("#hintUser").ITCUI_HintList(opts);
	}
	
	function selectAllNode(select){
		var opts = {
			url : basePath + "tree?method=subusers&id=" + currNode.id.replace("org_",""),
			dataType : "json",
			success : function(data){
				for(var i=0;i<data.length;i++){
					var o = data[i].split(",");
					var uid = o[0];
					var name = o[1];
					//找到真实节点 如果树已经加载了部分
					var realNode = $("#userTree").tree("find","user_" + uid);
					if(realNode){
						if(select){
							$("#userTree").tree("check",realNode.target);
						}
						else{
							$("#userTree").tree("uncheck",realNode.target);
						}
					}
					if(select){
						chkUsers[uid] = name;
					}
					else{
						delete(chkUsers[uid]);
					}
				}
			}
		};
		if(fromRole){
			opts.url += "&ignoresite=1";
		}
		$.ajax(opts);
	}
</script>
<style>
	
</style>
</head>
<body style="padding:6px">
	<span style="font-size:12px">提示：可以使用右键选择某机构下的所有用户</span>
	<div id="searchUserWrap" style="margin-top:4px;width:100%" class="bbox">
		<label class="ctrl-label pull-left" style="width:60px;">搜索用户：</label>
		<div class="input-group-sm pull-left">
		    <input type="text" class="form-control" style="width:200px" id="hintUser">
		</div>
	</div>
	<div style="width:100%;height:260px;margin-top:4px;overflow-y:auto" id="userTree">

	</div>
	<div class="dropdown" id="menu_tree" style="position:absolute">
		<a data-toggle="dropdown" id="menu_tree_toggle"></a>
		<ul class="dropdown-menu" role="menu">
			<li><a class="menuitem" onclick="selectAllNode(true)">选择该节点下所有用户</a></li>
			<li><a class="menuitem" onclick="selectAllNode(false)">删除该节点下所有用户</a></li>
		</ul>
	</div>
</body>
</html>