<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>编辑值别</title>
<script>_useLoadingMask = true;</script>

<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src='${basePath}js/operation/common/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/operation/common/commonForm.js?ver=${iVersion}'></script>
<script src='${basePath}js/operation/common/stationCommon.js?ver=${iVersion}'></script>
<script src='${basePath}js/operation/common/pageMode.js?ver=${iVersion}'></script>
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

var g = {users:userArr,relatedorgs:orgArr};
var _g = {users:userArr,relatedorgs:orgArr};
//选择树的数据
var treeData;

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

var fields = [
		{title : "id", id : "id",type:"hidden"},
		{title : "值别编号", id : "num",type:"hidden"},
	  	{title : "值别名称", id : "name",rules : {required:true}},
	  	{title : "排序", id : "sortType",rules : {required:true,digits:true}},
	  	{title : "是否可用", id : "isActive",rules : {required:true},linebreak : true,
				 type : "radio",
             data : [
                 ["Y","是"],
                 ["N","否"]
             ],	
			},
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
	function loadFormData(){
		var data = {
				"id" : rowData.id,
				"num" : rowData.num,
				"name" : rowData.name,
				"sortType" : rowData.sortType,
				"stationId" : rowData.stationId,
				"isActive" : rowData.isActive
			};
			$("#autoform").iForm("setVal",data);
			$("#autoform").iForm("endEdit");
	}
	
	//加载班次
	function setFormData( ){
		var id = '${param.id}';
		var url = basePath + "operation/duty/queryDutyById.do?id=" + id;
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
		PageMode.objs.pageName="值别";
		PageMode.changeMode();
		
		$("#autoform").iForm("init",{"options":opts,"fields":fields});
		//初始化 岗位
		stationOption( basePath, 'f_stationId', setFormData);
		//初始化树
		initUserTree( g );

		
		$( "#editButton" ).click(function(){
			PageMode.changeMode("edit");
			$("#userButtonDiv").show();
		});
		
		$( "#updateButton" ).click(function(){
			if(!$("#autoform").valid()){
				return;
			}
			var f={};
			diffForm(f,"users");
			var formData = getFormData( "autoform" );
			var url = basePath + "operation/duty/updateDuty.do";
			$.ajax({
				url : url,
				type : 'post',
				dataType : "json",
				data:{
					"formData":formData,
					"userDel" : f["usersDel"],
					"userAdd" : f["usersAdd"]
				},
				success : function(data) {
					if (data.result == "success") {
						FW.success("修改值别成功 ！");
						//重新载入页面，避免选人出问题
						FW.navigate(basePath+"operation/duty/updateDutyForm.do?id="+rowData.id);
						/* PageMode.changeMode();
						$("#userButtonDiv").hide(); */
					} else if(data.result == "fail"){
						FW.error("修改值别失败 ！");
					} else {
						var o = data.result.split(",");
						FW.error("["+o[0]+"]["+o[1]+"]已是["+o[2]+"]人员");
					}
				}
			});
		});
		
		$( "#cancelButton" ).click(function(){
			setFormData();
			PageMode.changeMode();
		});
		
		$( "#deleteButton" ).click(function(){
			FW.confirm("确定删除本条数据吗？",function(){
			var formData = getFormData( "autoform" );
			var url = basePath + "operation/duty/deleteDuty.do";
				$.ajax({
					url : url,
					type : 'post',
					dataType : "json",
					data:{"formData":formData},
					success : function(data) {
						if( data.result == "success" ){
							FW.success( "删除值别成功 ！");
							closeTab();
						}else{
							FW.error( "删除值别失败 ！");
						}
					}
				});
			});
		});
		
		//选择用户
		$("#userButton").click(function(){
			var src =  basePath + "page/operation/core/operationlog/select_role_user.jsp";
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
		        width : 350,
		        height: 400,
		        closed : false,
		        title:"选择拥有该值别的用户",
		        modal:true
		    };
		    Notice.dialog(src,dlgOpts,btnOpts);
		});
		
	});
	
	
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
	        <div class="btn-group btn-group-sm" style="display: none;" id="userButtonDiv">
				<button type="button" class="btn btn-default" id="userButton">选择用户</button>
	        </div>
	        
	    </div>
	</div>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	
	<div class="inner-title">
		编辑值别
	</div>
	<form id="autoform"></form>
	<div id="userDiv">
		<div  class="itcui_frm_grp_title" style="width:100%;">
			<span class="itcui_frm_grp_title_txt">拥有该值别的用户</span>
		</div>
		<div class="tree" id="treeDiv">
			<ul id="userTree" style="width: 400px;height: 400px;overflow-y:scroll; overflow-x:visible;margin-top: 5px;">
			</ul>
		</div>
	</div>
	
</body>
</html>