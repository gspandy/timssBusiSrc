<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html style="height: 99%;">
<head>
<title>工作票列表</title>
<script>
	_useLoadingMask = true;
</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script type="text/javascript" src="${basePath}js/ptw/ptwCommonUtil.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/ptw/OpenTabUtil.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/ptw/zjw/PtwUtil.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/ptw/commonPtwTree.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/ptw/PtwListUtil.js?ver=${iVersion}"></script>
<script>
	
	//是否从menu标准工作票页面过来
	var stdFlagFromMenu = <%=request.getParameter("isStd")%>; 
	if(stdFlagFromMenu){
		isStdWt = 1;
	}
	
	/**根据要查询的角色下拉框获取人员列表*/
	function loadPerson(key){
		var step = "";
		if(key == 'licWpicNo'){//工作负责人
			$.post(basePath+ "ptw/ptwInfo/queryPtwUsersByGroups.do",{"types":"PTW_workpic_DY,PTW_workpic_DE,PTW_workpic_JX,PTW_workpic_WH,PTW_workpic_HY,PTW_workpic_HE", "hasOther" : "all"},function(data){
				$("#searchPersonName").iCombo("loadData",data);
			},"json");
		}else if(key == 'outIssuerNo'){
			step = "310"
		}else if(key == 'issuerNo'){//工作票签发人
			step = "300"
		}else if(key == 'licWlNo'){//工作许可人
			step = "400"
		}
		if(step!=""){
			$.post(basePath+ "ptw/ptwInfo/queryPtwUsersByAuditPri.do",{"step" : step,"type" : "", "hasOther" : "all"},function(data){
				$("#searchPersonName").iCombo("loadData",data);
			},"json");
		}
	}
	
	$(document).ready(function() {
		initAssetTree();
		initStandardTree();
		
		//根据工作票编号查询
		$("#searchPtw").iInput("init",{
	        onSearch : function(key){
	        	searchFrom = "wtNo";
	        	searchParams = {wtNo:$.trim(key.toUpperCase())};
	        	search();
	        }
	    });
	    
	    //高级搜索相关内容的初始化
	    $("#wtType").iCombo("init",{
			url:basePath+ "ptw/ptwType/queryPtwTypes.do",
			displayItemCount:10
		}).iCombo("setTxt","工作票类型");            
		$("#wtStatus").iCombo("init",{
			displayItemCount:10
		});  
	    
	    $("#searchDateType").iCombo("init");
		$("#searchPersonType").iCombo("init",{
			onChange : function(val){
				loadPerson(val);
			}
		}); 
		$("#searchPersonName").iCombo("init",{allowSearch:true}); 
		//loadPerson($("#searchPersonType").iCombo("getVal"));
		          
		$("#searchDateFrom").iDate("init",{datepickerOpts:{format:"yyyy-mm-dd",minView:2}});
		$("#searchDateEnd").iDate("init",{datepickerOpts:{format:"yyyy-mm-dd",minView:2}});
		$("#sDateEnd").val(FW.long2date(new Date().getTime()));
    	
		//此处一定要给dataGrid赋值。dataGrid为全局变量，不设置会导致不能resize
		dataGrid = $("#ptwGrid").iDatagrid("init",{
	        columns:columns,
	        pageSize:pageSize,//pageSize为全局变量，自动获取的
		    queryParams : {
			    searchParams : function (){return FW.stringify(searchParams);},
			    searchFrom : function (){return searchFrom;},
			    isStdWt:function(){return isStdWt;}
			},
	        url: basePath + "ptw/ptwInfo/queryPtwInfoVoList.do",	//basePath为全局变量，自动获取的 
	        onDblClickRow:function(rowIndex,rowData){
	        	var id = rowData.id;
	        	var tabId = id;
	        	if(isStdWt == 1){
					tabId = "ptwInfoDetail" + tabId;
				}else{
					tabId = "ptwStdDetail" + tabId;
				}
	        	openNewTab(tabId,"查看工作票","ptw/ptwInfo/preQueryPtwInfo.do",{opType:"handlePtw",ptwTypeCodes:ptwTypeCodes,id:id});	        	
	        },
	        rowStyler : rowStyler,
	        singleSelect : true,
	        onSelect:function(rowIndex,rowData){
	        	loadSubGridData(rowIndex);
	        },
	        onLoadSuccess: function(data){
	            //远程无数据需要隐藏整个表格和对应的工具条，然后在无数据信息中指引用户新建
	            if(data && data.total==0){
	            	if(modelFlag && $("#grid1_empty").css('display')=="none"){
		                $("#noResult").show();
	            	}else{
	                	$("#maincontent").hide();
	                	$("#grid1_empty").show();
	                	$("#noResult").hide();
	            	}
	            }else{
	            	$("#maincontent").show();
	                $("#grid1_empty").hide();
	                $("#noResult").hide();
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
<body style="height: 100%;" class="bbox list-page">
	<div id="maincontent">
		<div class="bbox toolbar-with-pager" id="toolbar_wrap">
		    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
		    <div id="toolbar1" class="btn-toolbar ">
		        <div class="btn-group btn-group-sm">
		        	 <button type="button" class="btn btn-success" id="ptw_btn_new" onclick="newPtwInfo();">新建</button>
		        </div>
				<div class="btn-group btn-group-sm pull-left">
				    <button type="button" class="btn btn-default" onclick="refreshGrid()">刷新</button>
				</div>
				
				<div class="input-group input-group-sm" style="width:150px;float:left;">
				    <input type="text" id="searchPtw" icon="itcui_btn_mag" placeholder="输入工作票编号查询"/>     
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
				        <option value="licTime">许可时间</option>
				        <option value="finTime">结束时间</option>
				    </select>
				    <span class="ctrl-label pull-left">：</span>
				    <div class="pull-left" id="searchDateFrom" style="width: 100px;" style="margin-left:6px" input-id="sDateFrom"></div>
				    <span class="ctrl-label pull-left">~</span>
				    <div class="pull-left" id="searchDateEnd" style="width: 100px;" input-id="sDateEnd"></div>
			    </div>
			    
			    <div id="wtTypeDiv" style="float:left;margin-left: 6px;">
				    <select id="wtType" style="float:left;width:115px;margin-left: 6px;">
				    </select>
			    </div>
			    <div id="wtStatusDiv" style="float:left;margin-left: 6px;">
			    	<select id="wtStatus" style="float:left;width:100px;margin-left: 6px;">
				    	<option value="0">工作票状态</option>
				    	<option value="310">外委签发</option>
				    	<option value="300">业主签发</option>
				    	<option value="400">待许可</option>
				    	<option value="410,420,430">审核中</option>
				    	<option value="500">待结束</option>
				    	<option value="600">待终结</option>
				    	<option value="700">已终结</option>
				    	<option value="800">已作废</option>
				    </select>
			    </div>		    
			    
			    <div id="searchPerson" style="float: left;margin-left: 6px;">
			    	<select id="searchPersonType" style="float:left;width:90px;">
				        <option value="licWpicNo">工作票负责人</option>
				        <option value="outIssuerNo">外委签发人</option>
				        <option value="issuerNo">业主签发人</option>
				        <option value="licWlNo">许可人</option>
				    </select>		    
				    <span class="ctrl-label pull-left">：</span>
				    <select id="searchPersonName" style="float:left;width:80px">
				    </select>
			    </div>
			    
			    
			    
			    <div class="btn-group btn-group-sm fl" style="margin-left:6px">
			        <button type="button" class="btn btn-default" onclick="advSearch()">查询</button>
			    </div>
			    <div class="btn-group btn-group-sm fl" style="margin-left:6px">
			        <button type="button" class="btn btn-default" onclick="exportPtwRecord()">记录导出</button>
			    </div>
			    <div class="btn-group btn-group-sm fl" style="margin-left:6px">
			        <button type="button" class="btn btn-default" onclick="exportStaInfo()">统计报表</button>
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
	
	<div id="noResult" style="display:none;width:100%;">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px"> 没有找到符合条件的结果</div>
			    
			</div>
		</div>
	</div>
	
	
	<!-- 无数据 -->
	<div id="grid1_empty" style="display:none;width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">未查询到相关的工作票信息</div>
			    <div class="btn-group btn-group-sm margin-element">
				    <button type="button" class="btn btn-success" onclick="newPtwInfo()">新建</button>
				</div>
			</div>
		</div>
	</div>
	
</body>
</html>