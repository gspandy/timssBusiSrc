<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>更新人员值别</title>
<script>_useLoadingMask = true;</script>

<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src='${basePath}js/operation/common/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/operation/common/commonForm.js?ver=${iVersion}'></script>
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
//console.log( JSON.stringify( orgArr ) + "----------orgArr-------");
//console.log( JSON.stringify( userArr ) + "-----------------");

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

//加载表单数据,在请求岗位数据的ajax返回时调用
function loadFormData( rowData ){
	var data = {
			"stationId" : rowData.stationId,
			"stationName" : rowData.stationName,
			"dutyId" : rowData.dutyId,
			"dutyName" : rowData.dutyName,
			"jobsId" : rowData.jobsId,
			"jobsName" : rowData.jobsName
		};
		$("#autoform").iForm("setVal",data);
}


var fields = [
	  			{title : "工种", id : "stationId",type:"hidden"},
	  			{title : "工种", id : "stationName",type:"label"},
	  			{title : "值别", id : "dutyId",type:"hidden"},
	  			{title : "值别", id : "dutyName",type:"label"},
	  			{title : "岗位", id : "jobsId",type:"hidden"},
	  			{title : "岗位", id : "jobsName",type:"label"}
	  		];
	  		
	  		
	var opts={
		validate:true,
		fixLabelWidth:true
	};
	
	
	$(document).ready(function() {
		$("#autoform").iForm("init",{"options":opts,"fields":fields});
		//初始化表单
		var personDutyVo = ${ personDutyVo };
		loadFormData(personDutyVo);
		changeTitle();
		//初始化树
		initUserTree( g );
		
		//保存
		$( "#saveButton" ).click(function(){
			var f={};
			diffForm(f,"users");
			
			var formData = getFormData( "autoform" );
			var url = basePath + "operation/personJobs/updatePersonJobs.do";
			$.ajax({
				url : url,
				type : 'post',
				dataType : "json",
				data :{
					formData : formData ,
					userDel : f["usersDel"],
					userAdd : f["usersAdd"]
				},
				success : function(data) {
					if( data.result == "success" ){
						FW.success( "更新人员成功 ！");
						changeTitle();
						//resetForm();
						closeTab();
					}else{
						FW.error( "更新人员失败 ！");
					}
				}
			}); 
		});
		
		//删除
		$( "#deleteButton" ).click(function(){
			FW.confirm("确定删除本条数据吗？",function(){
				var formData = getFormData( "autoform" );
				var url = basePath + "operation/personJobs/deletePersonJobs.do";
				$.ajax({
					url : url,
					type : 'post',
					dataType : "json",
					data :{
						formData : formData 
					},
					success : function(data) {
						if( data.result == "success" ){
							FW.success( "删除人员成功 ！");
							//resetForm();
							closeTab();
						}else{
							FW.error( "删除人员失败 ！");
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
		        width : 450,
		        height: 400,
		        closed : false,
		        title:"选择拥有该角色的用户",
		        modal:true
		    };
		    Notice.dialog(src,dlgOpts,btnOpts);
		});
		
		$("#editButton").click(function(){
			$(this).hide();
			$("#saveButton").show();
			$("#userButton").show();
			changeTitle("edit");
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
		//console.log( "relatedorgs-------" + relatedorgs );
		//console.log( "user-------" + treeData );
		
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
	
		function changeTitle(type){
			var title=$(".inner-title");
			var name="人员值别信息";
			if(type=="add"){
				title.html("新建"+name);
			}else if(type=="edit"){
				title.html("编辑"+name);
			}else{
				title.html(name+"详情");
			}
		}
</script>

</head>
<body>
<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <div id="toolbar" class="btn-toolbar ">
	        <div class="btn-group-sm" style="margin-top: 2px!important;">
	            <button type="button" class="btn btn-default" onclick="closeTab()">关闭</button>
	            <button type="button" class="btn btn-default" id="editButton">编辑</button>
	            <button type="button" class="btn btn-success" id="saveButton" style="display: none;">保存</button>
	            <button type="button" class="btn btn-default" id="deleteButton">删除</button>
	            <button type="button" class="btn btn-default" id="userButton" style="display: none;">选择用户</button>
	        </div>
	    </div>
	</div>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div class="inner-title">
		编辑人员值别信息
	</div>
	<form id="autoform"></form>
	
	<div id="userDiv">
		<div  class="itcui_frm_grp_title" style="width:100%;">
			<span class="itcui_frm_grp_title_txt">拥有该工种的用户</span>
		</div>
		<div class="tree" id="treeDiv">
			<ul id="userTree" style="width: 400px;height: 400px;overflow-y:scroll; overflow-x:visible;margin-top: 5px;">
			</ul>
		</div>
	</div>
	
</body>
</html>