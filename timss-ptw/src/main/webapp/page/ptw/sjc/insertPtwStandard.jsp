<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html style="min-height: 99%;">
<head>
<title>标准树</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src='${basePath}js/ptw/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/ptw/commonForm.js?ver=${iVersion}'></script>
<script src='${basePath}js/ptw/sjc/updateStdTree.js?ver=${iVersion}'></script>
<script type="text/javascript" src="${basePath}js/ptw/commonPtwTree.js?ver=${iVersion}"></script>

<script>
//父节点ID
var parentId = '${ parentId }';
//父节名字
var parentName = '${ parentName }';

//接收树的点击
function toShow( node ){
	parentId = node.id;
	parentName = node.text;
	setForm();
	
}

//设置form表单数据
function setForm( ){
	var data = {
			"parentId" : parentId,
			"parentName" : parentName
	};
	
	$("#autoform").iForm("setVal", data);
	$("#autoform").iForm("endEdit",['parentId', 'parentName']);
}

var fields = [
              {title : "id", id : "id" , type: "hidden"},
				{title : "名称", id : "name" ,rules : {required:true/* ,remote:{
					url: basePath + "ptw/ptwStandardTree/queryCheckStdTreeName.do",
					type: "post",
					data: {
						"paramsMap" : function(){
							var params = {
									"name": $("#f_name").val(),
							};
							 return FW.stringify(params);
						}
						
					}
				} */},
              	 messages:{remote:"名称已存在，请输入其他名称"}
				},
	  			{title : "编码", id : "code",rules : {required:true}},
	  			{title : "父节点", id : "parentId", type: "hidden"},
	  			{title : "父节点", id : "parentName"},
	  			{title : "设备树", id : "equipmentId", type: "hidden"},
	  			{title : "设备树", id : "equipmentName",rules : {required:true},
					render:function(id){
		            	 var ipt = $("#" + id);
		            	 ipt.removeClass("form-control").attr("icon","itcui_btn_mag");
		            	 ipt.ITCUI_Input();
		            	 ipt.next(".itcui_input_icon").on("click",function(){
		                     var src = basePath + "ptw/ptwStandardTree/queryDeviceTreePage.do";
		                     var dlgOpts = {
		                         width : 400,
		                         height:500,
		                         closed : false,
		                         title:"选择设备树",
		                         modal:true
		                     };
		                     var btnOpts = [{
		             			"name" : "取消",
		             			"float" : "right",
		             			"style" : "btn-default",
		             			"onclick" : function() {
		             				return true;
		             			}
		             		},{
		             			"name" : "确定",
		             			"float" : "right",
		             			"style" : "btn-success",
		             			"onclick" : function() {				
		             				var conWin = _parent().window.document.getElementById("itcDlgContent").contentWindow;
		             				var node = conWin.getSelectedNode();
		             				$("#f_equipmentId").val( node.id );
		             				$("#" + id ).val( node.text );
		             				return true;
		             			}
		             		} ];
		                     Notice.dialog(src,dlgOpts,btnOpts);
		                 });
		             }},
	  			{title : "备注", id : "remark",type:"textarea", linebreak:true,
	  				wrapXsWidth:12,
	  				wrapMdWidth:8,
	  				height : 50
	  			}
              ];
	  		
	  		
	var opts={
		validate:true,
		fixLabelWidth:true,
		labelFixWidth:150
	};
	
	
	$(document).ready(function() {
		initStandardTree();
		$("#autoform").iForm("init",{"options":opts,"fields":fields});
		setForm();
		
		$( "#saveButton" ).click(function(){
			if(!$("#autoform").valid()){
				return;
			}
			var formData = getFormData( "autoform" );
			var url = basePath + "ptw/ptwStandardTree/insertOrUpdateStandard.do";
			$.ajax({
				url : url,
				type : 'post',
				dataType : "json",
				data : {
					formData :  formData
				},
				success : function(data) {
					if( data.result == "success" ){
						FW.success( "保存成功 ！");
						addTreeNode( data.ptwStdTreeVo );
						//setForm( data.ptwStdTreeVo );
						closeTab();
					}else{
						if( data.reason != null ){
							FW.error( data.reason );
						}else {
							FW.error( "保存失败 ！");
						}
					}
				}
			});
		});
		
		
	});
		
</script>

</head>
<body>
<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <div id="toolbar" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default" onclick="closeTab()">关闭</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default" id="saveButton">保存</button>
            </div>
	    </div>
	</div>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div class="inner-title">
		新建标准树
	</div>
	<form id="autoform"></form>
</body>
</html>