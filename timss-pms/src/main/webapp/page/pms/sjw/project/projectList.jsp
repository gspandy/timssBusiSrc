<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<%
 	String path = request.getContextPath();
%>
<head>
<title>工作票列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_useLoadingMask = true;</script><jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src="${basePath}js/pms/common/common.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/common/progressBar.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/common/workProgress.js?ver=${iVersion}"></script>
<link rel="stylesheet" type="text/css" href="${basePath}css/pms/common/common.css" media="all"></link>
<script type="text/javascript" src="${basePath}js/pms/itc/project/projectList.js?ver=${iVersion}"></script>
<script>
	var browserType = ( navigator.userAgent.indexOf("Trident/7.0")>-1||navigator.userAgent.indexOf("IE")>-1||navigator.userAgent.indexOf("Edge")>-1)?"IE":"OTHER";
	$(document).ready(function() {
		pmsPager.initListPager();
		//此处一定要给dataGrid赋值。dataGrid为全局变量，不设置会导致不能resize
		dataGrid = $("#test_grid1").iDatagrid("init",{
	        pageSize:pageSize,//pageSize为全局变量，自动获取的
	        url: basePath + "pms/project/projectListData.do",	//basePath为全局变量，自动获取的       
	        singleSelect:true,
	        onLoadSuccess: function(data){
	            //远程无数据需要隐藏整个表格和对应的工具条，然后在无数据信息中指引用户新建
	            if(data && data.total==0){
	            	$('#noResult').show();
	            }else{
	            	$('#noResult').hide();
	            	$("#grid1_wrap").show();
	                $("#grid1_empty").hide();
	            }
	        },
	        onDblClickRow:function(rowIndex, rowData){
	        	var flowid=rowData["flowid"];
	        	var	id=rowData["id"];
	        	openEditProjectTab(id,flowid);
	        },
	        columns:[[		
			 			/* {field:'ck',checkbox:true}, */
			 			{field:'id',hidden:true},
			 			{field:'ptype',hidden:true},
			 			{
			 				field:'pyear',title:'年份',width:50,align:'left',fixed:true	,
	         				sortable:true
			 			},
			 			{
			 				field:'property',title:'性质',width:50,align:'left',fixed:true,
			 				formatter: function(value,row,index){
			 					var result="成本";
			 					if(value=='income'){
			 						return "收入";
			 					}
			 					return result;
			 				},
	         				sortable:true
			 			},
			 			{
			 				field:'projectName',title:'名称',width:1,align:'left',sortable:true
			 			},
			 			{
			 				field:'projectCode',title:'项目编号',width:155,align:'left',sortable:true,fixed:true
			 			},
			 			{
			 				field:'applyBudget',title:'项目金额(元)',width:105,align:'right',fixed:true,
	         				sortable:true
			 			},
			 			{
			 				field:'startTime',title:'计划实施周期',width:215,align:'left',fixed:true,
			 				formatter: function(value,row,index){
			 					var start=FW.long2date(row["startTime"]);
			 					var end=FW.long2date(row["endTime"]);
			 					return start+"~"+end;
			 				}
			 			},
			 			{
			 				field:'companyId',title:'分公司',width:128,align:'left',fixed:true,
			 				formatter: function(value,row,index){
			 					var companyId=row["companyId"];
			 					var companyName = "";
			 					var enumList = FW.parseEnumData("PMS_PROJECT_SUBCOMP",_enum);
			 					for(var i=0;i<enumList.length;i++){
			 						if(companyId==enumList[i][0]){
			 							companyName = enumList[i][1];
			 						}
			 					}
			 					return companyName;
			 				}
			 			},
			 			{
			 				field:'statusValue',title:'状态',width:95,align:'left',fixed:true
	         				
			 			}
			 		]]

	    });
		//表头搜索相关的
	    $("#btn_advSearch").click(function(){
		    if($(this).hasClass("active")){
		        $("#test_grid1").iDatagrid("endSearch");
		    }
		    else{
		        $("#test_grid1").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(args){

				    //强烈建议转为一个string传给后台，方便转Bean或hashMap
				    return {"search":FW.stringify(args)};
				}});
		    }
		});
	    $("#btn_print1").click(function(){
			showExportDialog("signed");
		});
		$("#btn_print2").click(function(){
			showExportDialog("unpayed");
		});
	});
	
	function showExportDialog(type){
		var title="";
		if('unpayed'==type){
			title="导出未结算立项及合同清单";
		}else if('signed'==type){
			title="导出当前已签订合同";
		}
	    var src = basePath + "page/pms/sjw/project/exportCondition.jsp";
	    var btnOpts = [{"name" : "关闭",
			            "onclick" : function(){
			                return true;
			               }
			        	},
			           {
			            "name" : "导出",
			            "style" : "btn-success",
			            "onclick" : function(){
			                var p = _parent().window.document.getElementById("itcDlgContent").contentWindow;
			                var searchDateFrom = p.$("#exportCondition").iForm("getVal");
			                var siteid = Priv.secUser.siteId;
							var beginTime = FW.long2date(searchDateFrom.fromTime);
							var endTime = FW.long2date(searchDateFrom.toTime); 
							if(searchDateFrom.fromTime<=searchDateFrom.toTime){ 
								if(type=='signed'){
									url= fileExportPath+"preview?__report=report/TIMSS2_PMS_SJW_CONTRACT_SIGN_001_xls.rptdesign&__format=xls_spudsoft&siteid="+siteid+
									"&startTime="+beginTime+"&endTime="+endTime+"&cnFileName=当期已签订合同明细&__asattachment=true&browserType="+browserType;
								}else if(type=='unpayed'){
									url= fileExportPath+"preview?__report=report/TIMSS2_PMS_SJW_CONTRACT_UNPAYED_001_xls.rptdesign&__format=xls_spudsoft&siteid="+siteid+
									"&startTime="+beginTime+"&endTime="+endTime+"&cnFileName=未结算立项及合同清单&__asattachment=true&browserType="+browserType;
								}
								window.open (url);
							}else{
								FW.error("查询结束时间必须大于开始时间 ");
							} 
			            }
			           }];
	    var dlgOpts = {width : 400,height:200, title:title,};
	    FW.dialog("init",{"src":src,"dlgOpts":dlgOpts,"btnOpts":btnOpts}); 
	}
</script>
</head>
<body class="list-page">
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar1" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm " >
	            <button type="button" class="btn btn-success pms-privilege" id="pms-b-project-add" onclick="addNewPlanTab();">新建</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" id="btn_advSearch" data-toggle="button" class="btn btn-default">查询</button>
	        </div>
	        <div class="btn-group btn-group-sm" style="margin-left:7px;">
	            <button type="button" class="btn btn-default dropdown-toggle" id="btn_print"  data-toggle="dropdown">
			                            &nbsp;导出&nbsp;
			        <span class="caret"></span>
			    </button>
			    <ul class="dropdown-menu">
			    	<li id="btn_print1"><a href="javascript:void(0)" >当期已签订合同明细</a></li>
			        <li id="btn_print2"><a href="javascript:void(0)" >未结算立项及合同清单</a></li>
			    </ul>
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
	
	<div id="noResult" style="display:none;width:100%;height:62%;margin:10px;">
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
	<div class="row" id="grid1_empty" style="display:none">
	    没有xxxxxxx数据，单击这里创建新的....
	</div>
</body>
</html>