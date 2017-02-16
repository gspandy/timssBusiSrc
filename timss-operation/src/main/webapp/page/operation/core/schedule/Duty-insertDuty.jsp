<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>新建值别</title>
<script>_useLoadingMask = true;</script>

<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src='${basePath}js/operation/common/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/operation/common/commonForm.js?ver=${iVersion}'></script>
<script src='${basePath}js/operation/common/pageMode.js?ver=${iVersion}'></script>
<script src='${basePath}js/operation/common/stationCommon.js?ver=${iVersion}'></script>
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
var g = {users:{},relatedorgs:{}};
//选择树的数据
var treeData;

//设置排序值
function setSortType( stationId ){
	var url = basePath + "operation/duty/querySortTypeByStationId.do?stationId=" + stationId;
	$.ajax({
		url : url,
		type : 'post',
		dataType : "json",
		success : function(data) {
			$("#f_sortType").val( data.result );
		}
	});
}

var fields = [
				{title : "值别编号", id : "num",type:"hidden"},
	  			{title : "值别名称", id : "name",rules : {required:true}},
	  			{title : "排序", id : "sortType",rules : {required:true,digits:true}},
	  			{title : "是否可用", id : "isActive",rules : {required:true},linebreak : true,
					 type : "radio",
		             data : [
		                 ["Y","是",true],
		                 ["N","否"]
		             ]
				},
	  			{title : "工种", id : "stationId",rules : {required:true},
	                  type : "combobox"
	  			}
	  		];
	var opts={
		validate:true,
		fixLabelWidth:true
	};
	
	//重置插入表单
	function resetForm(){
		var data = {
				"num" : "",
				"name" : "",
				"sortType" : "",
			};
			$("#autoform").iForm("setVal",data);
	}
	
	$(document).ready(function() {
		PageMode.objs.pageName="值别";
		PageMode.changeTitle("add");
		
		$("#autoform").iForm("init",{"options":opts,"fields":fields});
		//岗位
		stationOption(basePath, 'f_stationId');
		
		$( "#saveButton" ).click(function(){
			if(!$("#autoform").valid()){
				return;
			}
			var formData = getFormData( "autoform" );
			var url = basePath + "operation/duty/insertDuty.do";
			$.ajax({
				url : url,
				type : 'post',
				dataType : "json",
				data:{
					"formData":formData,
					"treeData" : treeData
				},
				success : function(data) {
					if( data.result == "success" ){
						FW.success( "新增值别成功 ！");
						//resetForm();
						closeTab();
					}else if( data.result == "fail" ){
						FW.error( "新增值别失败 ！");
					}else{
						var o = data.result.split(",");
						FW.error("["+o[0]+"]["+o[1]+"]已是["+o[2]+"]人员");
					}
				}
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
	    <div id="toolbar" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default" onclick="closeTab()">关闭</button>
	        </div>
	         <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-success" id="saveButton">保存</button>
            </div>
            <div class="btn-group btn-group-sm" id="userButtonDiv">
				<button type="button" class="btn btn-default" id="userButton">选择用户</button>
	        </div>
	    </div>
	</div>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div class="inner-title">
		新建值别
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