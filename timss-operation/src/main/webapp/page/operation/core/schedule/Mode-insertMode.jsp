<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>运行方式设置详情</title>
<script>_useLoadingMask = true;</script>

<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src='${basePath}js/operation/common/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/operation/common/commonForm.js?ver=${iVersion}'></script>
<script src='${basePath}js/operation/schedule/assetTree.js?ver=${iVersion}'></script>

<script>

//工种、岗位
var jobDeptVo = '${jobDeptVo}';
//岗位Id
var jobId ='';
var modeNum=0;

var fields = [
				{title : "工种Id", id : "deptId",type : "hidden"},
				{title : "工种", id : "deptName",type : "label"},
				{title : "岗位Id", id : "jobId",type : "hidden"},
				{title : "岗位", id : "jobName",type : "label"}
	  		];
	  		
	var opts={
		validate:true,
		fixLabelWidth:true
	};
	
	//接收设备树单击事件
	function onTreeItemClick( node ){
	}
	
	//接收设备树双击事件
	function onTreeItemDblClick( node ){
		var url = basePath + "operation/mode/queryModeAssetByAssetId.do?assetId=" + node.id;
		$.ajax({
			url : url,
			type : 'post',
			dataType : "json",
			success : function(data) {
				if( data.result == "success" ){
					var modeAssetVo = JSON.parse( data.modeAssetVo );
					modeAssetVo.jobId = jobId;
					var rowDatas = $("#modeDatagrid").datagrid('getRows');
					for( var index in rowDatas ){
						if( modeAssetVo.assetId == rowDatas[index].assetId ){
							FW.error( "已存在设备：" + modeAssetVo.assetName + "！");
							 $("#modeDatagrid").datagrid('selectRow', index);
							return;
						}
					}
					
					$('#modeDatagrid').datagrid('appendRow', modeAssetVo );
					beginEditorDatagrid();
					$("#grid1_empty").hide();
				}else{
					FW.error( "加载设备树信息失败 ！");
				}
			}
		});
	}
	
	//开启编辑模式
	function beginEditorDatagrid( ){
		var rowSize = $("#modeDatagrid").datagrid('getRows').length;
		for( var i = 0 ; i < rowSize; i++ ){
	    	$("#modeDatagrid").datagrid('beginEdit', i);
		}
	}
	
	//删除一行datagrid数据
	function deleteThisRow(rowIndex, field, value) {
		if (field == 'jobId') {
			FW.confirm("删除？<br/>确定删除所选项吗？该操作无法撤销。", function() {
				$("#modeDatagrid").datagrid("deleteRow",rowIndex);
			});
		}
	}

	
	//初始化设备明细
	function initDatagrid( ){
		var url = basePath + "operation/mode/queryModeListByJobId.do?jobId=" + jobId;
		$("#modeDatagrid").iDatagrid("init",{
	        pageSize:pageSize,//pageSize为全局变量，自动获取的
	        singleSelect :true,
	        url: url,	//basePath为全局变量，自动获取的       
	        "columns":[[
				{field:'assetId',title:'设备树Id',width:180,fixed:true,hidden:true},
				{field:'assetName',title:'名称',width:250,fixed:true, sortable: true},
				{field:'assetType',title:'类型',width:100,fixed:true, sortable: true,formatter:function(val){
					return FW.getEnumMap("AST_ASSET_TYPE")[val];
				}},
				{field:'spec',title:'专业',width:100,fixed:true, sortable: true,formatter:function(val){
					return FW.getEnumMap("AST_ASSET_SPEC")[val];
				}},
				{field:'modeVal',title:'可选项',width:310,fixed:true, sortable: true, editor : {
				 	type:"text","options" : {
			        	rules : {
			        		required:true
						}	
			        }
		    	}},
		    	{field:'nowModeVal',title:'初始值',width:180,fixed:true,hidden:true},
				{field:'team',title:'组别',width:100,fixed:true, sortable: true, editor : {
				 	type:"text","options" : {
			        	rules : {
			        		required:true
						}	
			        }
		    	}},
		    	{field:'sortNum',title:'排序',width:80,fixed:true, sortable: true, editor : {
				 	type:"text","options" : {
				 		dataType:"number",
				 		//value : "0",
			        	rules : {
			        	    number:true,
			        		required:true
						}	
			        }
		    	}},
				{field:'jobId',title:'',width:180, sortable: true, formatter: function(value,row,index){
					 return '<img src="'+basePath+'img/operation/btn_garbage.gif" '+
	  				    +' width="16" height="16" style="cursor:pointer" />';
				}}
				]],
			"onLoadError": function(){
				//加载错误的提示 可以根据需要添加
			    $("#grid1_error").show();
			},
	        onLoadSuccess: function(data){
	            //远程无数据需要隐藏整个表格和对应的工具条，然后在无数据信息中指引用户新建
	            if(data && data.total==0){
	                $("#grid1_empty").show();
	            }else{
	                $("#grid1_empty").hide();
	                modeNum=data.total;
	                beginEditorDatagrid();
	            }
	        },onClickCell : function(rowIndex, field, value) {
				if (field == 'jobId') {
					deleteThisRow(rowIndex, field, value);
				}
			}
	    });
	}
	
	$(document).ready(function() {
		//设备树
		initAssetTree();
		$("#autoform").iForm("init",{"options":opts,"fields":fields});
		if( jobDeptVo != '' ){
			jobDeptVo = FW.parse( jobDeptVo );
			jobId = jobDeptVo.jobId;
			$("#autoform").iForm("setVal", jobDeptVo );
			initDatagrid( );
		}
		
		//$("#sortNum").attr('value','0');
		
		$( "#saveButton" ).click(function(){
			if(!$("#modeFormDatagrid").valid()){
				return;
			}
			var rowData = $("#modeDatagrid").datagrid('getRows');
			for( var i = 0; i < rowData.length; i++ ){
				$("#modeDatagrid").datagrid('endEdit', i);
			}
			rowData = $("#modeDatagrid").datagrid('getRows');
			
			if( rowData.length <= 0 ){
				if(modeNum==0){
					FW.error( "请添加设备 ！");
					return;
				}else{
					FW.confirm("确认删除所有设备检查点？",function(){
						submitMode(rowData);
					});
				}
			}else{
				submitMode(rowData);
			}
		});
		
	});
		
	function submitMode(rowData){
		var url = basePath + "operation/mode/insertOrUpdateMode.do";
		$.ajax({
			url : url,
			type : 'post',
			dataType : "json",
			data : {
				jobId:jobId,
				rowData : JSON.stringify( rowData )
			},
			success : function(data) {
				if( data.result == "success" ){
					FW.success( "保存成功 ！");
					//resetForm();
					beginEditorDatagrid();
					modeNum=rowData.length;
					if(modeNum==0){
						$("#grid1_empty").show();
					}
				}else{
					FW.error( "保存失败 ！");
				}
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
	    </div>
	</div>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div class="inner-title">
		运行方式设置
	</div>
	<div id="formDiv">
		<form id="autoform"></form>
	</div>
	<!-- 资产明细 -->
	<div grouptitle="相关设备" id="detailTitle">
		<div class="margin-title-table">
			<form id="modeFormDatagrid">
			<table id="modeDatagrid" style="" class="eu-datagrid"></table>
			</form>
		</div>
	</div>
	<!-- 错误信息-->
	<div class="row" id="grid1_error" style="display:none">
	    无法从服务器获取数据，请检查网络是否正常
	</div>
	<!-- 无数据 -->
	<div id="grid1_empty" style="display:none;width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">没有设备数据，请从左边设备树双击添加</div>
			</div>
		</div>
	</div>
</body>
</html>