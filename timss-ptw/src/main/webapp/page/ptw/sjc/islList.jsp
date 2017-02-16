<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html style="height: 99%;">
<head>
<title>隔离证列表</title>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />

<script type="text/javascript" src="${basePath}js/ptw/OpenTabUtil.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/ptw/PtwUtil.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/ptw/commonPtwTree.js?ver=${iVersion}"></script>

<script>
	//是否为标准隔离证 0 ： 不是， 1：是
	var isStdWt = 0;
	var searchFrom = "wtKind"; // wtKind, wtNo,advSearch,treeSearch
	var searchParams = {};
	//当前datagrid的参数
	var currentData = null;
	
	var columns = [[
		{field:'no',title:'编号',width:140,sortable:true,fixed:true},
		{field:'status',title:'状态',width:85,sortable:true,fixed:true,
			formatter : function(value,row,index){
				return ptwUtil.getStatusNameByWtStatus(value);
			}
		},
		{field:'eqName',title:'设备名称',width:200,sortable:true,fixed:true},
		{field:'workContent',title:'工作内容',width:180,sortable:true}/* ,
		{field:'workPlace',title:'工作地点',width:160,sortable:true,fixed:true} */
	]];
	
	var subGrid = null;
	var subColumns = [[
		{field:'issuer',title:'签发人',width:70,fixed:true},
		{field:'issuedTime',title:'签发时间',width:130,
			formatter: function(value,row,index){
				return FW.long2time(value);
			}},
		{field:'executer',title:'许可人',width:70,fixed:true},
		{field:'executerTime',title:'许可时间',width:130,
			formatter: function(value,row,index){
				return FW.long2time(value);
			}},
		{field:'withDraw',title:'结束人',width:70,fixed:true},
		{field:'withDrawTime',title:'结束时间',width:130,
			formatter: function(value,row,index){
				return FW.long2time(value);
			}},
		{field:'remover',title:'终结人',width:70,fixed:true},
		{field:'removerTime',title:'终结时间',width:130,
			formatter: function(value,row,index){
				return FW.long2time(value);
			}}
	]];
	
	/**隔离证基本信息展示的grid*/
	function loadSubGridData(index){
		if(currentData != undefined && currentData != null && currentData.total > 0 && isStdWt == 0){
			$("#subGridDiv").show();
			var data = {"rows":[currentData.rows[index]],"total":1};
			subGrid.datagrid('loadData',data);
		}else{
			$("#subGridDiv").hide();
		}		
	}
		
	/**查询*/
	function search(){
	    //这里的mytable要换成datagrid对应的id
	    delete(_itc_grids["ptwGrid"]);
	    var pager = $("#ptwGrid").datagrid("getPager"); 
	    pager.pagination("select",1);
	};
	
	/**查询*/
	function searchAfterClosePtw(){
		if(FW.get("PtwIslDoNotRefresh")){
			FW.set("PtwIslDoNotRefresh",false);
			return;
		}
	    search();
	};
	
	/**打开或关闭高级搜索的框*/
	function preAdvSearch(){
	    var btn = $("#btnSearch");
	    if(!btn.hasClass("active")){
	        $("#advSearchBar").show();
	    }else{
	        $("#advSearchBar").hide();
	    }
	}
		
	/**高级搜索*/
	function advSearch(){
		searchFrom = "advSearch";
		var searchDateType = $("#searchDateType").iCombo('getVal');
		var searchDateFrom = $("#sDateFrom").val();
		var searchDateEnd = $("#sDateEnd").val();
		var wtStatus = $("#wtStatus").iCombo('getVal');
		var wtType = $("#wtType").iCombo('getVal');
		var searchPersonType = $("#searchPersonType").iCombo('getVal');
		var searchPersonId = $("#searchPersonName").iCombo('getVal');
		searchParams = {
			searchDateEnd : searchDateEnd,
			searchDateFrom : searchDateFrom,
			searchDateType : searchDateType,
			searchPersonType : searchPersonType,
			searchPersonId :searchPersonId,
			wtStatus : wtStatus,
			wtType : wtType
		};
		search();
	};
	
	/**根据隔离证种类查询*/
	function searchFromKind(wtKind){
		isStdWt = wtKind;
		if(isStdWt  == 1){
			$("#wtTypeDiv").hide();
			$("#searchPerson").hide();
		}else{
			$("#wtTypeDiv").show();
			$("#searchPerson").show();
		}
		searchFrom = "wtKind";
		search();
	}
	
	/**刷新隔离证*/
	function refreshGrid(){
		$("#wtKindMenuBtn1").find("a").click();
		//取消树的选中		
		var p = _parent().window.document.getElementById("ptwAssetTree").contentWindow;
		if(p){
			p.unSelectTree();
		}
	}
	
	/**根据要查询的角色下拉框获取人员列表*/
	function loadPerson(key){
		var role_code = "PTW_work_wpic";
		var actionRole = "queryPtwUsersByRole";
		var actionGroup = "queryPtwUsersByGroup";
		var action = actionRole;
		if(key == 'createuser'){//工作负责人
			role_code = "PTW_work_wpic";
			action = actionGroup;
		}else{
			role_code = "PTW_work_wpic";
			action = actionGroup;
		}
		
		$.post(basePath+ "ptw/ptwInfo/"+action+".do",{"role" : role_code,"hasOther":"all"},function(data){
			$("#searchPersonName").iCombo("loadData",data);
		},"json");
	}
	
	
	
	function onTreeItemClick(node){
		if(node.type == 'root'){
			refreshGrid();
		}else{
			searchFrom = "treeSearch";
			searchParams = {eqId:node.id};
			search();
		}
	}
	
	$(document).ready(function() {
		initAssetTree();
		
		//查看隔离证类型的按钮初始化
		$("#btnView").iMenu("init",{
	    	menu : [{title:"隔离证",select:true,group:"groupView",selected:true,id:"wtKindMenuBtn1",onclick:"searchFromKind(0)"},
	    			 {title:"标准隔离证",select:true,group:"groupView",id:"wtKindMenuBtn2",onclick:"searchFromKind(1)"}]
	    });
		
		//根据隔离证编号查询
		$("#searchPtw").iInput("init",{
	        onSearch : function(key){
	        	searchFrom = "wtNo";
	        	searchParams = {no:$.trim(key.toUpperCase())};
	        	search();
	        }
	    });
	    
	    //高级搜索相关内容的初始化
	    $("#searchDateType").iCombo("init");
		$("#searchPersonType").iCombo("init",{
			onChange : function(val){
				loadPerson(val);
			}
		}); 
		$("#searchPersonName").iCombo("init",{allowSearch:true}); 
		loadPerson($("#searchPersonType").iCombo("getVal"));
		$("#wtType").iCombo("init",{
			url:basePath+ "ptw/ptwType/queryPtwTypes.do?ptwOrIsl=isl",
			displayItemCount:10
		}).iCombo("setTxt","隔离证类型");            
		$("#wtStatus").iCombo("init",{
			displayItemCount:10
		});            
		$("#searchDateFrom").iDate("init",{datepickerOpts:{format:"yyyy-mm-dd",minView:2}});
		$("#searchDateEnd").iDate("init",{datepickerOpts:{format:"yyyy-mm-dd",minView:2}});
		$("#sDateEnd").val(FW.long2date(new Date().getTime()));
    	
		//此处一定要给dataGrid赋值。dataGrid为全局变量，不设置会导致不能resize
		dataGrid = $("#ptwGrid").iDatagrid("init",{
	        columns:columns,
	        pageSize:pageSize,//pageSize为全局变量，自动获取的
		    queryParams : {
			    searchParams : function (){return JSON.stringify(searchParams);},
			    searchFrom : function (){return searchFrom;},
			    isStdWt:function(){return isStdWt;}
			},
	        url: basePath + "ptw/ptwIsolation/queryPtwIslList.do",	//basePath为全局变量，自动获取的 
	        onDblClickRow:function(rowIndex,rowData){
	        	var id = rowData.id;
	        	var tabId = "islInfoDetail"+id;
	        	openNewTab(tabId,"查看隔离证","ptw/ptwIsolation/preQueryIslInfo.do",{id:id,opType:"islDetail"});	        	
	        },
	        rowStyler : rowStyler,
	        singleSelect : true,
	        onSelect:function(rowIndex,rowData){
	        	loadSubGridData(rowIndex);
	        },
	        onLoadSuccess: function(data){
	            //远程无数据需要隐藏整个表格和对应的工具条，然后在无数据信息中指引用户新建
	            if(data && data.total==0){
	                $("#maincontent").hide();
	                $("#grid1_empty").show();
	            }else{
	            	$("#maincontent").show();
	                $("#grid1_empty").hide();
	            }
	            currentData = data;
	            loadSubGridData(0);
	        }
	    });
	    
	    subGrid = $("#subPtwGrid").datagrid({
    		columns:subColumns,
    		fitColumns:true,
    		border:false,
			scrollbarSize:0,
			rowStyler : rowStyler
    	});
	});
	
	function rowStyler(rowIndex, rowData) {
		if (rowData.isStdWt == 1) {
			//标准票
			if (rowData.status == 4000) {
				//已作废
				return 'color:#c0c0c0;';
			} else {
				return 'color:#008000;';
			}
		} else {
			if (rowData.status == 500) {
				//已许可
				return 'color:red;';
			} else if (rowData.status == 800) {
				//已作废
				return 'color:#c0c0c0;';
			}
		}
	}
	
	//新建卡片
	function newIsolation(){
		var params = {opType:"newIsolation"};
		
		var p = _parent().window.document.getElementById("ptwAssetTree").contentWindow;
		var node = undefined;
		if(p){
			node = p.getSelectedNode();
			if(node){
				params.eqId = node.id;
				params.eqName = node.text;
				params.eqNo = node.assetCode;
			}
		}
		openNewTab("islInfoDetail"+new Date().getTime(),"新建隔离证","ptw/ptwIsolation/preQueryIslInfo.do",params);
	}
	
	
	
</script>
<style type="text/css">
.afooter {
  position: fixed;
  bottom: 5px;
  height: 60px;
  box-shadow: 2px 2px 2px #888888;
  border: 1px solid #C0C0C0;
  background: #fff;
  width: 100%;
}
</style>
</head>
<body style="height: 100%;" class="bbox">
	<div id="maincontent">
		<div class="bbox toolbar-with-pager" id="toolbar_wrap">
		    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
		    <div id="toolbar1" class="btn-toolbar ">
		        <div class="btn-group btn-group-sm">
		        	 <button type="button" class="btn btn-success" onclick="newIsolation();">新建</button>
		        </div>
				<div class="btn-group btn-group-sm" id="btnView" style="display: none;">
				    <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
				   	 	查看
				   	 	<span class="caret"></span>
				    </button>
				</div>
				<div class="btn-group btn-group-sm pull-left">
				    <button type="button" class="btn btn-default" onclick="refreshGrid()">刷新</button>
				</div>
				
				<div class="input-group input-group-sm" style="width:150px;float:left;">
				    <input type="text" id="searchPtw" icon="itcui_btn_mag" placeholder="输入隔离证编号查询"/>     
				</div>
		        <div class="btn-group btn-group-sm pull-left" style="margin-left:7px">
				    <button type="button" class="btn btn-default" data-toggle="button" id="btnSearch" onclick="preAdvSearch()">高级搜索</button>
				</div>
		    </div>
		    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
		    <div id="pagination_1" class="toolbar-pager" bottompager="#bottomPager">        
		    </div>
		    
		    <div style="clear:both"></div>
		    <div id="advSearchBar" class="row bbox" style="display:none">
			    <div id="searchDate" style="float: left;">
			    	<select id="searchDateType" style="float:left;width:85px">
				        <option value="createDate">创建时间</option>
				        <option value="issuedTime">签发时间</option>
				        <option value="executerTime">许可时间</option>
				        <option value="withDrawTime">结束时间</option>
				        <option value="removerTime">终结时间</option>
				    </select>
				    <span class="ctrl-label pull-left">：</span>
				    <div class="pull-left" id="searchDateFrom" style="width: 100px;" style="margin-left:6px" input-id="sDateFrom"></div>
				    <span class="ctrl-label pull-left">~</span>
				    <div class="pull-left" id="searchDateEnd" style="width: 100px;" input-id="sDateEnd"></div>
			    </div>		    
			    
			    <div id="searchPerson" style="float: left;margin-left: 6px;">
			    	<select id="searchPersonType" style="float:left;width:80px;">
				        <option value="createuser">创建人</option>
				        <option value="issuerNo">签发人</option>
				        <option value="executerNo">许可人</option>
				        <option value="withDrawNo">结束人</option>
				        <option value="removerNo">终结人</option>
				    </select>		    
				    <span class="ctrl-label pull-left">：</span>
				    <select id="searchPersonName" style="float:left;width:80px">
				    </select>
			    </div>
			    
			    <div id="wtTypeDiv" style="float:left;margin-left: 6px;">
				    <select id="wtType" style="float:left;width:125px;margin-left: 6px;">
				    </select>
			    </div>
			    <div id="wtStatusDiv" style="float:left;margin-left: 6px;">
			    	<select id="wtStatus" style="float:left;width:120px;margin-left: 6px;">
				    	<option value="0">隔离证状态</option>
				    	<option value="300">未签发</option>
				    	<option value="400">已签发</option>
				    	<option value="500">已许可</option>
				    	<option value="600">已结束</option>
				    	<option value="700">已终结</option>
				    	<option value="800">已作废</option>
				    </select>
			    </div>
			    
			    <div class="btn-group btn-group-sm fl" style="margin-left:6px">
			        <button type="button" class="btn btn-default" onclick="advSearch()">查询</button>
			    </div>			    
			</div>
		</div>
		<!--这里要清掉分页器的右浮动效果-->	
		<div style="clear:both"></div>
		<div id="grid1_wrap" style="width:100%">
		    <table id="ptwGrid" pager="#pagination_1" class="eu-datagrid">
		    </table>
		</div>
		<!-- 下页器部分-->
		<div id="bottomPager" style="width:100%;margin-top:6px">
		</div>
		<div style="height: 70px;"></div>
		<!-- 扩展信息 -->
		<div id="subGridDiv" class="afooter">
			 <table id="subPtwGrid" class="eu-datagrid">
		    </table>
		</div>
	</div>
	
	<!-- 无数据 -->
	<div id="grid1_empty" style="display:none;width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">未查询到相关的隔离证信息</div>
			    <div class="btn-group btn-group-sm margin-element">
				    <button type="button" class="btn btn-success" onclick="newIsolation()">新建</button>
				</div>
			</div>
		</div>
	</div>
	
</body>
</html>