<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>新增人员值别信息</title>
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
var g = {users:{},relatedorgs:{}};
//选择树的数据
var treeData;

//填充值别
function dutyOption( stationId ){
	var dutyArr = [];
	dutyArr.push( ["0", "请选择值别",true]);
	$("#f_dutyId").iCombo("init", {
		data : dutyArr
	});
	var url = basePath + "operation/duty/queryDutyByStationId.do?stationId=" + stationId ;
	$.ajax({
		url : url,
		type : 'post',
		dataType : "json",
		success : function(data) {
			if( data.total > 0 ){
				var dutyList = data.rows;
				
				for( var index in dutyList ){
					dutyArr.push( [dutyList[index].id,dutyList[index].name]);
				}
				$("#f_dutyId").iCombo("loadData",dutyArr);
			}
		}
	});
}

//填充工种
function jobsOption( stationId ){
	var jobsArr = [];
	jobsArr.push( ["0", "请选择岗位",true]);
	$("#f_jobsId").iCombo("init", {
		data : jobsArr
	});
	var url = basePath + "operation/jobs/queryJobsByStationId.do?stationId=" + stationId ;
	$.ajax({
		url : url,
		type : 'post',
		dataType : "json",
		success : function(data) {
			if( data.total > 0 ){
				var jobsList = data.rows;
				
				for( var index in jobsList ){
					jobsArr.push( [jobsList[index].id,jobsList[index].name]);
				}
				$("#f_jobsId").iCombo("loadData",jobsArr);
			}
		}
	});
}

//填充岗位
function stationOption(){
	
	var url = basePath + "operation/duty/queryStationInfoBySitId.do";
	$.ajax({
		url : url,
		type : 'post',
		dataType : "json",
		success : function(data) {
			var arr = [];
			var stationList = data.result;
			for( var index in stationList ){
				if( index == 0 ){
					arr.push([stationList[index].roleId,stationList[index].name,true]);
				}else{
					arr.push([stationList[index].roleId,stationList[index].name]);
				}
			}
			//$("#f_stationId").iCombo("loadData",arr);
			$("#f_stationId").iCombo("init", {
		      	data : arr,
				"onChange" : function(val) {
					dutyOption( val );
					jobsOption( val );
				}
			});
			
			dutyOption( stationList[0].roleId );
			jobsOption( stationList[0].roleId );
		}
	});
}

var fields = [
	  			{title : "工种", id : "stationId",rules : {required:true},
	                  type : "combobox"
	  			},
	  			{title : "值别", id : "dutyId",rules : {required:true},
	                  type : "combobox"
	  			},
	  			{title : "岗位", id : "jobsId",rules : {required:true},
	                  type : "combobox"
	  			}
	  		];
	  		
	  		
	var opts={
		validate:true,
		fixLabelWidth:true
	};
	
	
	$(document).ready(function() {
		$("#autoform").iForm("init",{"options":opts,"fields":fields});
		//岗位
		stationOption();
		
		
		$( "#saveButton" ).click(function(){
			if(!$("#autoform").valid()){
				return;
			}
			var dutyId = $("#f_dutyId").iCombo("getVal");
			var jobsId = $("#f_jobsId").iCombo("getVal");
			if( dutyId == 0 ){
				FW.error( "请选择值别！");
				return;
			}
			if( jobsId == 0 ){
				FW.error( "请选择工种！");
				return;
			}
			if( treeData == null ){
				FW.error( "请选择用户！");
				return;
			}
			
			var formData = getFormData( "autoform" );
			 var url = basePath + "operation/personJobs/insertPersonJobs.do";
			$.ajax({
				url : url,
				type : 'post',
				dataType : "json",
				data :{
					formData : formData ,
					treeData : treeData
				},
				success : function(data) {
					if( data.result == "success" ){
						FW.success( "新增人员值别成功 ！");
						//resetForm();
						closeTab();
					}else{
						FW.error( "新增人员值别失败 ！");
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
		        width : 450,
		        height: 400,
		        closed : false,
		        title:"选择拥有该角色的用户",
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
	    <div id="toolbar1" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm">
	         <button type="button" class="btn btn-default" onclick="closeTab()">关闭</button>
	         </div>
	         <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-success" id="saveButton">保存</button>
            </div>
            <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default" id="userButton">选择用户</button>
	        </div>
	    </div>
	</div>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div class="inner-title">
		新建人员值别信息
	</div>
	<form id="autoform"></form>
	
	<div id="userDiv">
		<div  class="itcui_frm_grp_title" style="width:100%;">
			<span class="itcui_frm_grp_title_txt">拥有该工种的用户</span>
		</div>
		<div class="tree" id="treeDiv">
			<ul id="userTree" style="width: 400px;height: 300px;overflow-y:scroll; overflow-x:visible;margin-top: 5px;">
			</ul>
		</div>
	</div>
	
</body>
</html>