<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>免考勤名单</title>
<script>_useLoadingMask = true;</script>

<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<style type="text/css">
#treeDiv {
	width: 400px;
	height: 300px;
	border-width: 1px;
	border-style: solid;
	border-color: rgb(198, 198, 198);
	overflow: hidden;
	margin-left: 12px;
	margin-top: 4px;
}
</style>
<script>
var userArr = ${ userArr };
var orgArr = ${ orgArr };
var orgs = ${ orgs };

var g = {users:userArr,relatedorgs:orgArr,orgs:orgs};
var _g = {users:userArr,relatedorgs:orgArr,orgs:orgs};
//选择树的数据
var treeData;

var fields = [
              {title : "免考勤部门",type : "label",id : "orgsName",wrapXsWidth : 12,wrapMdWidth : 12,formatter : joinTags}
	  		];
$(document).ready(function() {
	
	$("#autoform").iForm("init",{"options":{fixLabelWidth:true},"fields":fields});
	
	//初始化树
	initUserTree( g );
	
	//初始化免考勤部门
	$("#autoform").iForm("setVal",{orgsName:orgs});
	
	$( "#editButton" ).click(function(){
		$("#editButtonDiv").hide();
		$("#updateButtonDiv,#cancelButtonDiv,#userButtonDiv,#orgButtonDiv").show();
		FW.fixRoundButtons("#toolbar");
	});
	
	$( "#updateButton" ).click(function(){
		if(!$("#autoform").valid()){
			return;
		}
		var f={};
		diffForm(f,"users");
		diffForm(f,"orgs");
		var url = basePath + "attendance/freeAtd/updateFreeAtd.do";
		$.ajax({
			url : url,
			type : 'post',
			dataType : "json",
			data:{
				"userDel" : f["usersDel"],
				"userAdd" : f["usersAdd"],
				"orgDel" : f["orgsDel"],
				"orgAdd" : f["orgsAdd"]
			},
			success : function(data) {
				if (data.result == "success") {
					FW.success("保存成功 ！");
					FW.navigate(basePath+"attendance/freeAtd/queryFreeAtdMenu.do");
				} else {
					FW.error("保存失败 ！");
				}
			}
		});
	});
	
	$( "#cancelButton" ).click(function(){
		FW.navigate(basePath+"attendance/freeAtd/queryFreeAtdMenu.do");
	});
	
	//选择免考勤人员
	$("#userButton").click(function(){
		var src =  basePath + "page/attendance/core/common/select_user.jsp";
	    var btnOpts = [{
	            "name" : "取消",
	            "float" : "right",
	            "style" : "btn-default",
	            "onclick" : function(){
	                return true;
	            }
	        },{
	            "name" : "确定",
	            "float" : "right",
	            "style" : "btn-success",
	            "onclick" : function(){
	            	var p = _parent().window.document.getElementById("itcDlgContent").contentWindow;
	            	g.users = p.getChecked();
	            	var lst = [];
	            	for(var k in g.users){
	            		lst.push(k);
	            	}
	            	$.ajax({
	            		url : basePath + "role?method=userorg",
	            		dataType : "json",
	            		type : "post",
	            		data : {
	            			"ids" : lst.join(",")
	            		},
	            		success : function(data){
	            			g.relatedorgs = data;
	            			initUserTree("create");            			
	            			_parent().$("#itcDlg").dialog("close");
	            		}
	            	});            	            	
	            }
	        }
	    ];
	    var dlgOpts = {
	        width : 450,
	        height: 400,
	        closed : false,
	        title:"选择免考勤人员",
	        modal:true
	    };
	    Notice.dialog(src,dlgOpts,btnOpts);
	});
	
	//选择免考勤部门
	$("#orgButton").click(function(){
		var src =  basePath + "page/attendance/core/common/select_org.jsp";
	    var btnOpts = [{
	            "name" : "取消",
	            "float" : "right",
	            "style" : "btn-default",
	            "onclick" : function(){
	                return true;
	            }
	        },{
	            "name" : "确定",
	            "float" : "right",
	            "style" : "btn-success",
	            "onclick" : function(){
	            	var p = _parent()._parent().window.document.getElementById("itcDlgContent").contentWindow;
	            	g.orgs = p.getChecked();
	            	$("#autoform").iForm("setVal",{orgsName:g.orgs});
	            	return true;
	            }
	        }
	    ];
	    var dlgOpts = {
	        width : 450,
	        height:400,
	        closed : false,
	        title:"选择免考勤组织机构",
	        modal:true
	    };
	    Notice.dialog(src,dlgOpts,btnOpts);
	});
	
});

//比较数据的差异g _g
function diffForm(f,o){
	f[o + "Add"] = [];
	f[o + "Del"] = [];
	for(var k in _g[o]){
		//原来有现在没有 删除
		if(!g[o][k]){
			f[o + "Del"].push(k);
		}		
	}
	for(var k in g[o]){
		if(!_g[o][k]){
			f[o + "Add"].push(k);
		}
	}
	f[o + "Add"] = f[o + "Add"].join(",");
	f[o + "Del"] = f[o + "Del"].join(",");
}

//object to String 
function objToString (obj) {
    var tabjson=[];
    for (var p in obj) {
        if (obj.hasOwnProperty(p)) {
            tabjson.push('"'+p +'"'+ ':"' + obj[p] + '"');
        }
    }
    return '{'+tabjson.join(',')+'}';
}

function initUserTree(mode){
	var relatedorgs = objToString( g.relatedorgs );
	treeData = objToString( g.users );
		
	$.ajax({
		url : basePath + "tree?method=extendall",
		data : {
			orgFilter : relatedorgs,
			personFilter : treeData
		},
		type:"POST",
		dataType:"JSON",
		success : function(data){
			var opts = {
				"data":data
			};			
			$("#userTree").tree(opts);
		}
	});	
}

function joinTags(o){
	var l = [];
	var lInh = [];
	for(var k in o){
		l.push("<span style='margin-right:12px'>" + o[k] + "</span>");
	}
	if(l.length==0 && lInh.length==0){
		return "暂无";
	}
	return l.join("") + lInh.join("");
}
	
		
</script>
</head>
<body>
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <div id="toolbar" class="btn-toolbar">
	    	<div class="btn-group btn-group-sm" id="editButtonDiv">
				<button type="button" class="btn btn-default" id="editButton">编辑</button>
	        </div>
	        <div class="btn-group btn-group-sm" style="display: none;" id="cancelButtonDiv">
	            <button type="button" class="btn btn-default" id="cancelButton">取消</button>
	        </div>
	        <div class="btn-group btn-group-sm" style="display: none;" id="updateButtonDiv">
				<button type="button" class="btn btn-success" id="updateButton" >保存</button>
	        </div>
	        <div class="btn-group btn-group-sm" style="display: none;" id="userButtonDiv">
				<button type="button" class="btn btn-default" id="userButton">设置免考勤人员</button>
	        </div>
	        <div class="btn-group btn-group-sm" style="display: none;" id="orgButtonDiv">
				<button type="button" class="btn btn-default" id="orgButton">设置免考勤部门</button>
	        </div>
	        
	    </div>
	</div>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div class="inner-title" id="pageTitle">
		免考勤设置
	</div>
	<form id="autoform"></form>
	<div id="userDiv">
		<div  class="itcui_frm_grp_title" style="width:100%;">
			<span class="itcui_frm_grp_title_txt">免考勤人员</span>
		</div>
		<div class="tree" id="treeDiv">
			<ul id="userTree" style="width: 400px;height: 400px;overflow-y:scroll; overflow-x:visible;margin-top: 5px;">
			</ul>
		</div>
	</div>
</body>
</html>