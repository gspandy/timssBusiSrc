<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" style="height:99%" xml:lang="en">

<head>
<title>管理费用申请列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script type="text/javascript" src="${basePath}/js/finance/common/eventTab.js?ver=${iVersion}"></script>
<script>
    var isSearchMode=false;
	$(document).ready(function() {
		//此处一定要给dataGrid赋值。dataGrid为全局变量，不设置会导致不能resize
		dataGrid = $("#test_grid1").iDatagrid("init",{
	        pageSize:pageSize,//pageSize为全局变量，自动获取的
	        url: basePath + "finance/fma/queryFinanceManagementApplyListData.do",	//basePath为全局变量，自动获取的       
	        singleSelect:true,
	        onLoadSuccess: function(data){
	            //远程无数据需要隐藏整个表格和对应的工具条，然后在无数据信息中指引用户新建
	        	if(isSearchMode){
			        if(data && data.total==0){
			            $("#noSearchResult").show();
			        }else{
			            $("#noSearchResult").hide();
			        }
			    }else{
			    	$("#noSearchResult").hide();
		            if(data && data.total==0){
	            		$("#main_content").hide();
	                	$("#grid1_empty").show();
		            }else{
		            	$("#main_content").show();
		                $("#grid1_empty").hide();
		            }
			    }
			    isSearchMode = false;
	        },
	        onDblClickRow:function(rowIndex, rowData){
	        	var newId="finEditFMATab"+rowData['id'];
	    		var pageName="出差申请";
	    		var pageUrl=basePath + "finance/fma/editFMAJsp.do?id="+rowData['id']+"&applyType="+"travelapply";
	    		var oldId=FW.getCurrentTabId();
	    		addEventTab( newId, pageName, pageUrl, oldId );
	        },
	        columns : [ [
	         		    {field : 'proInstId',hidden : true},
	         		    {field : 'id',title : '申请单号',width : 140,align : 'left',
	         		   	 	fixed : true,sortable:true},
	         		   	{field : 'description',title : '出差事由',width : 200}, 
	         		   	{field : 'applyType',title : '出差类型',width : 90,align : 'left',
	         				fixed : true,sortable:true,
	         				formatter: function(value,row,index){
								return FW.getEnumMap("FIN_APPLY_TYPE")[value]; 
							},
							"editor" : {
						        "type":"combobox",
						        "options" : {
						            "data" : FW.parseEnumData("FIN_APPLY_TYPE",_enum)	
						        }
						    }
	         			},
	         			{field : 'applyUsername',title : '申请人',width : 65,	align : 'left',	
	         				fixed : true,sortable:true}, 
	         			{field : 'createdate',title : '申请时间',width : 100,align : 'left',
	         				fixed : true,sortable:true,
	         				formatter:function(value,row,index){
	         					var value=FW.long2date(value);
	         					return value;
	         				}
	         			}, 
	         			{field : 'flowStatusName',title : '状态',width : 150,align : 'left',
	         				fixed : true,sortable:true/* ,
	         				formatter:function(value,row,index){
	         					var flowStatus=row["flowStatus"];
	         					var status=row["status"];
	         					value=getStatus(status,flowStatus);
	         					return value;
	         				} */  //将status改为flowStatusName，修复无法按照状态模糊搜索的缺陷
	         			} ] ]

	    });
		//表头搜索相关的
	    $("#btn_advSearch").click(function(){
		    if($(this).hasClass("active")){
		        $("#test_grid1").iDatagrid("endSearch");
		    }
		    else{
		    	
		        $("#test_grid1").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(args){
		        	isSearchMode=true;
				    //强烈建议转为一个string传给后台，方便转Bean或hashMap
				    return {"search":FW.stringify(args)};
				}});
		    }
		});
	});
	
	function refreshFinancePage(){
		dataGrid.datagrid('reload');
	}
	
	function _afterCloseCallBack(){
	}
	
	function getStatus(status,flowStatus){
		var value="";
		if(status=="D"){
			value="草稿";
		}else if(status=='AI'){
			if(flowStatus=='apply'){
				value="提交申请";
			}else if(flowStatus=='bmbzsp'){
				value="部门部长审批";
			}else if(flowStatus=='fgfzsp'){
				value="分管副总审批";
			}else if(flowStatus=='zjlsp'){
				value="总经理审批";
			}else if(flowStatus=='hxbl'){
				value="出差后续办理";
			}else if(flowStatus=='qtldsp'){
				value="任一其他领导审批";
			}
		}else if(status=="AE"){
			value="审批结束";
		}else if(status=="V"){
			value="作废";
		}
		return value;
	}

	function newTravelApplyPage(){
		var applyType = "travelapply";
		var newId="finNewApplyTab"+applyType;
		var pageName="出差申请";
		var pageUrl=basePath + "finance/fma/addFMAJsp.do?applyType="+applyType;
		
		var oldId=FW.getCurrentTabId();
		addEventTab( newId, pageName, pageUrl, oldId );
	}
</script>
</head>
<body style="height: 100%; min-width:850px" class="bbox list-page">
    <div id="main_content">
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar1" class="btn-toolbar ">
	        <!-- <div class="btn-group btn-group-sm " >
	            <button type="button" class="btn btn-success pms-privilege" id="pms-b-br-add" onclick="addNewBidResultTab();">新建</button>
	        </div> -->
	      <div class="btn-group btn-group-sm">
	        	 <button type="button" class="btn btn-success"  onclick="newTravelApplyPage();">新建 </button>
	        </div>
	      <!--   <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default" onclick="refresh(dataGrid);">刷新</button>
	        </div> -->

	        <div class="btn-group btn-group-sm">
	            <button type="button" id="btn_advSearch" data-toggle="button" class="btn btn-default">查询</button>
	        </div>

	    </div>
	    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
	    <div id="pagination_1" class="toolbar-pager" bottompager="#bottomPager">        
	    </div>
	</div>
	
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div id="grid1_wrap" style="width:100%">
	    <table id="test_grid1" pager="#pagination_1" class="eu-datagrid">
	        
	    </table>
	</div>
	<!-- 下页器部分-->
	<div id="bottomPager" style="width:100%;margin-top:6px">
	</div>
	</div>
	<div id="noResult" style="display:none;width:100%;height:62%;">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px"> 没有找到符合条件的结果</div>
			    
			</div>
		</div>
	</div>
	<!-- 错误信息-->
	<div class="row" id="grid1_error" style="display:none">
	    无法从服务器获取数据，请检查网络是否正常
	</div>
	<!-- 无数据 -->
	<div id="grid1_empty" style="display:none;width:100%;height:62%;">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">没有申请信息</div>
			    <div class="btn-group btn-group-sm margin-element">
		        	  <button type="button" class="btn btn-success"  onclick="newTravelApplyPage();">新建 </button>
		        </div>
			</div>
		</div>
	</div>
</body>
</html>