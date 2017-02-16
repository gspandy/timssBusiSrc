<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html style="height:99%">
<head>
<title>隔离方法列表</title>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
//动态添加返回的结果
var resultReturn = null;

//设置父级datagrid数据
function setDatagridData( data ){
	var tabId = FW.getCurrentTabId();
	var p = _parent().window.document.getElementById(tabId).contentWindow;
	var tableId = p.clickDatagridId;
	
	var datagridId = tableId + "Datagrid";
	var rowData = p.$("#" + datagridId ).datagrid( 'getRows' );
	var size = rowData.length;
	
	var flag = false;
	var msg = "";
	for(var i=0;i<size;i++){
		var dataId = data.pointNo;
		if( dataId == rowData[i].pointNo ){
			flag = true;
			msg += " " + dataId;
		}
	}
	if( !flag ){
			data.safeOrder = p.changeSafeOrder( size, data );
			
			//应接地线，safeType = 4
			if( datagridId == "IsolationElecDatagrid" ){
				data.safeType = 4; 
			}else if(datagridId == "CompSafeDatagrid"){
				data.safeType = 5;
			}else if(datagridId == "IsolationJxDatagrid"){
				data.safeType = 2;
			}else{
				data.safeType = 1;
			}
			//检查补充安全措施是否在安全/地线中
			if( p.checkCompSafe( data.pointNo ) ){
				return;
			}
			p.$("#" + datagridId ).datagrid("appendRow",data );
			FW.success( data.pointNo + "隔离点添加成功！" );
	}else{
		FW.error( msg + "隔离点已经存在！" );
	}
	p.$("#btn" + tableId+"Table" ).text("继续添加");
	p.beginEditor( tableId );
}

//标准隔离证
function setNormalIsl( data ){
	var tabId = FW.getCurrentTabId();
	var p = _parent().window.document.getElementById(tabId).contentWindow;
	
	var rowData = p.$("#isolationPointTable").datagrid( 'getRows' );
	var size = rowData.length;
	
	var flag = false;
	var msg = "";
	for(var i=0;i<size;i++){
		var dataId = data.pointNo;
		if( dataId == rowData[i].pointNo ){
			flag = true;
			msg += " " + dataId;
		}
	}
	if( !flag ){
		 p.$("#isolationPointTable").datagrid("appendRow",data );
		 FW.success( data.pointNo + "隔离点添加成功！" );
	}else{
		FW.error( msg + "隔离点已经存在！" );
	}
	 p.$("#btn_isolationPointTable").html("继续添加");
}

	$(document).ready(function() {
		
		dataGrid = $("#islMethod_table").iDatagrid("init",{
	        pageSize:pageSize,//pageSize为全局变量
	        url: basePath + "ptw/ptwIslMethDef/queryIsMethodItemList.do",	   
	        checkOnSelect: true,
	        columns:[[ 
					{field:"id",title:" ",width:110,fixed:true,checkbox:true}, 
					{field:"pointNo",title:"隔离点编号",width:110,fixed:true}, 
					{field:"pointName",title:"隔离点",width:110,fixed:true}, 
					{field:"stdMethodId",title:"隔离方法编号",width:110,fixed:true}, 
					{field:"methodName",title:"隔离方法名",width:200}
				]],
	        onLoadSuccess: function(data){
	            //远程无数据需要隐藏整个表格和对应的工具条，然后在无数据信息中指引用户新建
	           if(data && data.total==0){
	                $("#mainContent").hide();
	                $("#grid1_empty").show();
	            }else{
	            	$("#mainContent").show();
	                $("#grid1_empty").hide();
	            } 
	        },
	        onCheck : function(rowIndex, rowData) {
	        	//openPtwIslMethDef(rowData);
				var tabId = FW.getCurrentTabId();
				var p = _parent().window.document.getElementById(tabId).contentWindow;
				var tableId = p.clickDatagridId;
				if( tableId != null && tableId != "" ){
					if( tableId == "isolationPointTable"){
						//标准隔离证
						setNormalIsl( rowData );
					}else{
						//隔离证
						setDatagridData( rowData );
					}
				}
			},onBeforeLoad :function( param ){
				_parent().$("#itcDlgIsolationBtn_1 button").hide();
			}
		});
		
		
		  //表头搜索相关的
	    $("#btn_advSearch").click(function(){
		    if($(this).hasClass("active")){
		        $("#islMethod_table").iDatagrid("endSearch");
		    }
		    else{
		        $("#islMethod_table").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(args){
				    return {"search":JSON.stringify(args)};
				}}); 
		    }
		});
		
	});

	//选择勾选或者动态添加的隔离方法
	function getSelected(){
		var data = $("#islMethod_table").datagrid("getSelections");
		if( data == null || data.length <= 0 ){
			data = resultReturn;
		}
		return data;
	}
	
	function refresh(){
	$("#mainContent").show();
	$("#grid1_empty").hide();
	$("#islMethod_table").datagrid("reload");
}
/**
 * 关闭当前tab 页
 */
function closeCurPage(){
	FW.deleteTabById(FW.getCurrentTabId());
}

var headSearchParams = {};
var pointNo = '';
function queryByTree(){
	headSearchParams.pointNo = pointNo;
	//这里的mytable要换成datagrid对应的id
    delete(_itc_grids["islMethod_table"]);
    var pager = $("#islMethod_table").datagrid("getPager"); 
    pager.pagination("select",1);
}

    
/**左边树菜单选择触发事件*/
function onTreeItemClick(data){
	headSearchParams = {};
    $("#islMethod_table").iDatagrid("endSearch");
    pointNo = data.id;
	$("#islMethod_table").datagrid('options').queryParams.search = function(){
    		return JSON.stringify(headSearchParams);
   	};
	if(data.type == "root"){
    	refresh();
    	$("#isolationMethodBtnDiv").hide();
    }else{
    	queryByTree();
    	$("#isolationMethodBtnDiv").show();
    }
}

function refresh(){
	headSearchParams = {};
	$("#grid1_empty").hide();
	$("#islMethod_table").datagrid("reload");
}

//动态隔离方法 
function appendToMethod( methodIds ){
	var url = basePath + "ptw/ptwIsolationPoint/saveIslMethodByMethodIds.do?methodIds=" + methodIds + "&pointNo=" + pointNo;
	$.ajax({
		url : url,
		type : 'post',
		dataType : "json",
		success : function(data) {
			if( data.result == "success"){
				resultReturn = data.rowData;
				
				var tabId = FW.getCurrentTabId();
				var p = _parent().window.document.getElementById(tabId).contentWindow;
				var tableId = p.clickDatagridId;
				if( tableId != null && tableId != "" ){
					if( tableId == "isolationPointTable"){
						for( var index in resultReturn ){
							setNormalIsl( resultReturn[index] );
						}
						//标准隔离证
					}else{
						for( var index in resultReturn ){
							//隔离证
							setDatagridData( resultReturn[index] );
						}
					}
				}
			}else{
				FW.error( data.reason );
			}
			
		}, complete :function(XMLHttpRequest, textStatus){
			 _parent().$("#itcDlgIsolationMethod").dialog("close");
			//_parent().$("#itcDlgIsolationBtn_1 button").click();
			 $("#islMethod_table").datagrid("reload");
		}
	});
}

function appendIsolationMethod(opts){
	opts = opts || {};
	opts.basePath = opts.basePath || window.basePath || "";
	opts.width = opts.width || "50%";
	opts.height = opts.height || "75%";
	//页面地址
	var src = basePath + "ptw/ptwIsolationPoint/selectIslMethodPage.do";
	var dlgOpts = {
		idSuffix : "IsolationMethod",
		width : opts.width,
		height : opts.height,
		title : opts.title || "选择隔离方法"
	};
	var btnOpts = [{
            "name" : "关闭",
            "onclick" : function(){
                return true;
            }
        },{
        	"name" : "确定",
            "style" : "btn-success",
            "onclick" : function(){
            	var pa = _parent();
                var p = pa.window.document.getElementById("itcDlgIsolationMethodContent").contentWindow;
                var result = p.getSelectedRetrunId();
                if(!result){
                	FW.error("未选择任何隔离方法");
                	return;
                }
                
                appendToMethod( result );
            }
        }
    ];
	FW.dialog("init",{btnOpts:btnOpts,dlgOpts:dlgOpts,src:src});
};

</script>
</head>
<body style="height: 100%;" class="bbox">
	<div id="mainContent">
		<div class="bbox toolbar-with-pager" id="toolbar_wrap">
		    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
		    <div id="toolbar1" class="btn-toolbar ">
				<div class="btn-group btn-group-sm">
		            <button id="btn_advSearch" type="button" data-toggle="button" class="btn btn-default">查询</button>
		        </div>
		    </div>
		    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
		    <div id="pagination_1" class="toolbar-pager" bottompager="#bottomPager">        
		    </div>
		</div>
		<!--这里要清掉分页器的右浮动效果-->
		<div style="clear:both"></div>
		<div id="grid1_wrap" style="width:100%">
		    <table id="islMethod_table" pager="#pagination_1" class="eu-datagrid">
		    </table>
		    <div id="isolationMethodBtnDiv" class="row btn-group-xs" style="display: none;">
				 <button id="btn_isolationMethodTable" onclick="appendIsolationMethod();" type="button" class="btn btn-success">添加隔离方法</button>
			</div>
		</div>
		<!-- 下页器部分-->
		<div id="bottomPager" style="width:100%;margin-top:6px"></div>
	</div>
	
	
	<!-- 无数据 -->
	<div id="grid1_empty" style="display:none;width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">未查询到“隔离方法”信息</div>
			    <div class="btn-group btn-group-sm margin-element">
		             <button type="button" class="btn btn-success" onclick="appendIsolationMethod();">添加隔离方法</button>
			    </div>
			</div>
		</div>
	</div>
</body>
</html>