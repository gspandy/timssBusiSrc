<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% 
	String itemId = request.getParameter("itemId") == null ? "":String.valueOf(request.getParameter("itemId"));
	String pageNum = request.getParameter("pageNum") == null ? "":String.valueOf(request.getParameter("pageNum"));
	String haswh = request.getParameter("haswh") == null ? "":String.valueOf(request.getParameter("haswh"));
	String showWarehouse = request.getAttribute("showWarehouse") == null ? "":String.valueOf(request.getAttribute("showWarehouse"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:100%">
<head>
<link rel="shortcut icon" href="${basePath}favicon.ico" type="image/x-icon" /> 
<title>主项目表单</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src="${basePath}js/inventory/common/inventory.js?ver=${iVersion}"></script>
<script>
	var itemId = '<%=itemId%>';
	var showWarehouse = '<%=showWarehouse%>';
	var haswh = '<%=haswh%>';
	var tmpid=1;
	var pageNum='<%=pageNum%>';
	var selectId = "";
	<%--这个是表示执行了 编辑按钮隐藏的操作，由于之前的逻辑是默认显示编辑按钮，触发changestate时，来触发隐藏，如果被触发隐藏，还会作为返回按钮的判断条件--%>
	var editIsHide = false;
	
	//控制绑定的物资分类双击
	var canDbClick = false;
	
	var new_form = [
			{title : "系统id", id : "itemid", type:"hidden"},
	    	{title : "物资编号", id : "itemcode", type:"hidden"},
		    {title : "物资名称", id : "itemname", rules : {required:true}},
		    {title : "计量单位", id : "unitname", rules : {required:true}},
		    {title : "计量单位id", id : "unit1", type:"hidden"},
		    {title : "隐藏字段", id : "attr1", type:"hidden"},
		    {title : "型号规格", id : "cusmodel"}
	];
	
	var edit_form = [
			{title : "系统id", id : "itemid", type:"hidden"},
	    	{title : "物资编号", id : "itemcode", type:"label"},
		    {title : "物资名称", id : "itemname", rules : {required:true}},
		    {title : "计量单位", id : "unitname", rules : {required:true}},
		    {title : "计量单位id", id : "unit1", type:"hidden"},
		    {title : "隐藏字段", id : "attr1", type:"hidden"},
		    {title : "型号规格", id : "cusmodel"}
	];
	
	//编辑表单加载数据（通用方法）
	function initForm(inForm){
		$("#autoform").iForm("init",{"fields":inForm,"options":{validate:true}});
		//加载用户表单数据
		$.ajax({
			type : "POST",
			async: false,
			url: basePath+"/inventory/invitem/queryInvMainItemDetail.do",
			data: {"itemId":itemId },
			dataType : "json",
			success : function(data) {
				var loaddata = {
					"itemcode" : data.itemcode, 
					"itemname" : data.itemname,
					"unit1" : data.unit1, 
					"unitname" : data.unitname, 
					"cusmodel" : data.cusmodel,
					"itemid" : itemId
					};
				$("#autoform").iForm("setVal",loaddata);
			}
		});
	}
	
	//初始化按钮
	function initBtn(){
		//保存
		//20150104物资一对多不限制仓库绑定按钮
		$("#btn_save").click(function(){
			saveInfo("save");
		});
		
		//添加到仓库
		$("#btn_add2warehouse").click(function(){
			add2WarehouseDialog("new");
		});
		
		//编辑
		$("#btn_edit").click(function(){
			canDbClick = true;
			changeState("edit");
		});
		
		//关闭
		$("#btn_close").click(function(){
			FW.deleteTabById(FW.getCurrentTabId());
		});
	}
	
	//保存页面信息
	function saveInfo(saveType){
		if(!$("#autoform").valid()){
			return ;
		}
		
		var unitname = $("#autoform").iForm("getVal","unitname");
		var unit1 = $("#autoform").iForm("getVal","unit1");
		if("" != unitname && "" == unit1){
			$("#autoform").iForm("setVal",{"attr1":unitname});
		}
		
		var formData =$("#autoform").ITC_Form("getdata");
		
		var listData = $("#warehouse_grid").datagrid("getRows");
		
		 //加载用户表单数据
		$.ajax({
			type : "POST",
			async: false,
			url: basePath+"/inventory/invitem/saveItem.do",
			data: {
				"formData":JSON.stringify(formData),
				"listData":JSON.stringify(listData)
				},
			dataType : "json",
			success : function(data) {
				if( data.result == "success" ){
					canDbClick = false;
					itemId = data.itemid;
					$("#autoform").iForm("setVal",{"itemid" : itemId});
					FW.success( "保存成功 ");
					$("#autoform").ITC_Form("readonly");
					changeState("read");
					initList(); 
				}else{
					FW.error( "保存失败 ");
				}
			}
		}); 
	}
	
	//添加到仓库对话框
	function add2WarehouseDialog(type, rowIndex, rowData){
		FW.dialog("init",{
			src: basePath+"inventory/invitem/addToWarehouse.do?type="+type,
			btnOpts:[{
			            "name" : "取消",
			            "float" : "right",
			            "style" : "btn-default",
			            "onclick" : function(){
			                _parent().$("#itcDlg").dialog("close");
			             }
			        },
			        {
			            "name" : "确定",
			            "float" : "right",
			            "style" : "btn-success",
			            "onclick" : function(){
			                var p = _parent().window.document.getElementById("itcDlgContent").contentWindow;
                			var pFlag = p.saveInfo();
                			if(pFlag == 'validError'){
                				/* FW.error("请选择物资类型"); */
                			}else if(pFlag == 'validErrorSame'){
                				FW.error("不能选择已有的物资类型");
                			}else {
                				haswh = "Y";
                				var warehouseData = pFlag;
                				var warehouseid = warehouseData.warehouseid;
                				var invcateid = warehouseData.invcateid;
                				var binid = warehouseData.defBinid;
                				if(type == "edit" && rowData.warehouseid == warehouseid){
                					var flag = true;
                					if(rowData.active != 'Y' && warehouseData.active==='Y'){
                						flag = isActive(warehouseData, true);
                					}
                					if(flag){
	                					$('#warehouse_grid').datagrid('updateRow', {"index":rowIndex, row:warehouseData});
			                			_parent().$("#itcDlg").dialog("close");
                					}
                				}else{
                					var isExist = isExists(warehouseData);
	                				if(isExist){
		                				$('#warehouse_grid').datagrid('appendRow', warehouseData);
			                			_parent().$("#itcDlg").dialog("close");
	                				}
                				}
                			}
			            }
			        }],
			dlgOpts:{ 
				width:800, 
				height:240, 
				closed:false, 
				title:"绑定物资",
				modal:true 
				}
			});
	}
	
	$(document).ready(function() {
		initBtn();
		$("#warehouse_detail").iFold("init");
		if("" == itemId){
			canDbClick = true;
			changeState("new");
		}else{
			canDbClick = false;
			initForm(edit_form);
			changeState("read");
		}
		initList();
	});
	
	function changeState(state){
		if(state=="read"){
			$("#pageTitle").html("主项目物资详情");
			$("#autoform").ITC_Form("readonly");
			
			//20150104物资一对多不限制仓库绑定按钮
			$("#btn_save").hide();
			$("#btn_add2warehouse").hide();
			$("#btn_edit").show();
			editIsHide = false;
		}else if(state=="edit"){
			$("#pageTitle").html("编辑主项目物资");
			
			//20150104物资一对多不限制仓库绑定按钮
			$("#btn_save").show();
			$("#btn_add2warehouse").show();
			
			$("#btn_edit").hide();
			editIsHide = true;
			initForm(edit_form);
			edit_form[3].render = unitSearch("f_unitname");
		}else if(state=="new"){
			initForm(new_form);
			new_form[3].render = unitSearch("f_unitname");
			
			$("#pageTitle").html("新建主项目物资");
			$("#btn_edit").hide();
			editIsHide = true;
			//20150104物资一对多不限制仓库绑定按钮
			$("#btn_save").show();
		}
		
		FW.fixToolbar("#toolbar");
	}
	
	//查询计量单位
	function unitSearch(id){
	 	//放大镜图标属于基础资源，可以直接引用
        $("#" + id).attr("icon","itcui_btn_mag").removeClass("form-control").ITCUI_Input();
        $("#" + id).next(".itcui_input_icon").on("click",function(){
			FW.dialog("init",{
				src: basePath+"inventory/invunit/invUnitQueryList.do",
				btnOpts:[{
			            "name" : "取消",
			            "float" : "right",
			            "style" : "btn-default",
			            "onclick" : function(){
			                _parent().$("#itcDlg").dialog("close");
			             }
			        },
			        {
			            "name" : "确定",
			            "float" : "right",
			            "style" : "btn-success",
			            "onclick" : function(){
			                var p = _parent().window.document.getElementById("itcDlgContent").contentWindow;
			                var info = p.getUnit();
			                var datas = info.split("||"); 
		                	if(null != info && "" != info){
			                	$("#autoform").iForm("setVal",{"unit1":datas[0],"unitname" :datas[1]});
                            }
                            _parent().$("#itcDlg").dialog("close"); 
			            }
			        }],
				dlgOpts:{ width:800, height:457, closed:false, title:"查询计量单位", modal:true }
				});
		 });
	 }
	
	
	function initList(){
		var columns = [[
			{field:'iwiid',width:10,hidden:true,fixed:true},
			{field:'warehouseid',title:'仓库id',hidden:true},
			{field:'warehousename',title:'仓库',width:120,sortable:true,fixed:true},
			{field:'defBinid',title:'货柜id',hidden:true},
			{field:'binname',title:'货柜',width:120,sortable:true,fixed:true},
			{field:'invcateid',title:'物资分类id',hidden:true},
			{field:'invcatename',title:'物资分类',width:180,sortable:true,fixed:true},
			{field:'canUseQty',title:'可用库存',width:66,fixed:true,
				formatter:function(value,row){
					if(null === value || "" === value){
						return "--";
					}else{
						return value;
					}
				}},
			{field:'qty',title:'实际库存',width:66,fixed:true,
					formatter:function(value,row){
						if(null === value || "" === value){
							return "--";
						}else{
							return value;
						}
					}},
			{field:'qtyLowInv',title:'安全库存量',width:80,fixed:true},
			{field:'qtyEconomic',title:'经济订购量',hidden:true},
			{field:'issafety',title:'启用安全库存',width:96,fixed:true,
				editor:{
					 "type" : "combobox",
 					 "options" : {
 					    "data" : FW.parseEnumData("INV_SPMATERIAL",_enum)
 				     }
		 		},
 				formatter:function(value){
 					var list=FW.parseEnumData("INV_SPMATERIAL",_enum);
 					return getEnumValueByCode(list,value);
 				}
 			},
 			{field:'active',title:'状态',width:80,fixed:true,formatter:function(value,row,index){
 				if(row.active === 'Y'){ 
 					return '<span style="color:#006e12;">启用</span>';
 				}else{ 
 					return '<span style="color:#ff0000;">禁用</span>';
 				}
 			}},
 			//新建的绑定分类列表可以双击编辑仓库分类等信息 编辑的绑定分类列表可以双击编辑新添加的仓库分类等信息 已存在的绑定分类列表不允许编辑
 			{title:'isEdit',field:'是否开启编辑',hidden:true},
 			{title:'',field:'seize',width:1}
		]];
	
		var url = basePath+"inventory/invwarehouseitem/queryInvWarehouseItem.do";
		$("#warehouse_grid").datagrid({
			singleSelect:true,
			columns:columns,
			url : url,
			idField:"iwiid",
			queryParams: {"itemId":$("#autoform").iForm("getVal","itemid")},
			fitColumns : true,
			onLoadSuccess: function(data){
				if(data.rows.length!=0){
	            	$("#warehouse_detail").iFold("show");
	            	$("#warehouse_grid").datagrid("resize"); 
	            }
        	},
        	onDblClickRow:function(rowIndex, rowData){
        		FW.set("warehouseInfo",rowData);
        		if(canDbClick){
	        		var status = "edit";
	        		if(!editIsHide){
	        			status = "read"
	        		}
	        		add2WarehouseDialog(status, rowIndex,rowData);
        		}
			}
		});
	}
	//hasMsg参数 方便isExists调用
	function isActive(warehouseData, hasMsg){
		var warehouseid = warehouseData.warehouseid;
		var gridData  = $('#warehouse_grid').datagrid("getRows");
		for(var i=0;i<gridData.length;i++){
			if(gridData[i].warehouseid == warehouseid){
				if(gridData[i].active === 'Y'){
					if(hasMsg){
						FW.error("同仓库下只能启用一个分类");
					}
					return false;
				}
			}
		}
		return true;
	}
	
	function isExists(warehouseData){
		var flag = isActive(warehouseData, false);
		var warehouseid = warehouseData.warehouseid;
		var invcateid = warehouseData.invcateid;
		var gridData  = $('#warehouse_grid').datagrid("getRows");
		for(var i=0;i<gridData.length;i++){
			if(gridData[i].warehouseid == warehouseid){
				if( gridData[i].invcateid === invcateid ){
					var actFlag = flag ? flag : gridData[i].active === 'Y';
					if( actFlag ){
						gridData[i].active = 'Y';
						gridData[i].defBinid = warehouseData.defBinid;
						gridData[i].binname = warehouseData.binname;
						gridData[i].issafety = warehouseData.issafety;
						gridData[i].qtyLowInv = warehouseData.qtyLowInv;
						gridData[i].qtyEconomic = warehouseData.qtyEconomic;
						$("#warehouse_grid").datagrid("loadData",{total:gridData.length,rows:gridData});
						_parent().$("#itcDlg").dialog("close");
					}else{
						FW.error("同仓库下只能启用一个分类");
					}
					return false;
				}else{
					if(!flag){
						FW.error("同仓库下只能启用一个分类");
						return false;
					}
				}
			}
		}
		return true;
	}
</script>

</head>
<body>
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
		<!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar" class="btn-toolbar ">
	    	<div class="btn-group btn-group-sm">
	        	<button type="button" id="btn_close" class="btn btn-default">关闭</button>
	        </div>
	        
	        <!-- 20150104物资一对多不限制仓库绑定按钮-->
	        <div class="btn-group btn-group-sm">
	        	<button type="button" id="btn_save" class="btn btn-default">保存</button>
	        </div> 
	        
	        <div class="btn-group btn-group-sm">
	        	<button type="button" id="btn_edit" class="btn btn-default priv" privilege="itemList_save">编辑</button>
	        </div>
	    </div>
	</div>
	<div class="inner-title" id="pageTitle">
		主项目物资
	</div>
	<form id="autoform" class="margin-form-title margin-form-foldable autoform"></form>

	<!-- <div id="equipment_detail" grouptitle="所属设备">
		<div id="equipmentGrid" class="margin-title-table">
			<table id="equipment_grid" class="eu-datagrid"></table>
		</div>
	</div> -->
	
	<div id="warehouse_detail" grouptitle="已绑定物资分类信息" >
		<div id="warehouseGrid" class="margin-title-table">
			<table id="warehouse_grid" class="eu-datagrid"></table>
		</div>
	</div>
	<div class="btn-toolbar margin-foldable-button" role="toolbar">
		<div class="btn-group btn-group-xs">
	        <button type="button" class="btn btn-success" id="btn_add2warehouse">添加到仓库</button>
	    </div>
	</div>
</body>
</html>