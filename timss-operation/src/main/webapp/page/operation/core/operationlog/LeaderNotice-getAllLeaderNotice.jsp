<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>上级通知列表</title>
<script>_useLoadingMask = true;</script>

<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src='${basePath}js/operation/common/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/operation/common/map.js?ver=${iVersion}'></script>  	

<script>
/* //岗位Json 需要引入map.js
var stationMap = new Map();

//岗位map
function stationJson(){
	var url = basePath + "operation/duty/queryStationInfoBySitId.do";
	$.ajax({
		url : url,
		type : 'post',
		dataType : "json",
		success : function(data) {
			var stationList = data.result;
			for( var index in stationList ){
				stationMap.put( stationList[index].roleId, stationList[index].name );
			}
		}
	});
}

//formatter 单元格
function formatterData( val ){
	return val != null ? stationMap.get( val ) : "" ;
} */

//对Date的扩展，将 Date 转化为指定格式的String
//月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符， 
//年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字) 
//例子： 
//(new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423 
//(new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
Date.prototype.Format = function (fmt) { //author: meizz 
  var o = {
      "M+": this.getMonth() + 1, //月份 
      "d+": this.getDate(), //日 
      "h+": this.getHours(), //小时 
      "m+": this.getMinutes(), //分 
      "s+": this.getSeconds(), //秒 
      "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
      "S": this.getMilliseconds() //毫秒 
  };
  if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
  for (var k in o)
  if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
  return fmt;
};

	$(document).ready(function() {
		//stationJson();
		//此处一定要给dataGrid赋值。dataGrid为全局变量，不设置会导致不能resize
		dataGrid = $("#contentTb").iDatagrid("init",{
	        pageSize:pageSize,//pageSize为全局变量，自动获取的
	        singleSelect :true,
	        url: basePath + "operation/leaderNotice/queryAllLeaderNoticeList.do",	//basePath为全局变量，自动获取的       
	        nowrap : false,
	        "columns":[[
					{field:'writeDate',title:'发布时间',width:135,fixed:true,
						formatter: function(val,row){
							var d=new Date(val);
							return d.Format("yyyy-MM-dd hh:mm");
						}	
					},
					{field:'leaderUserName',title:'领导',width:80,fixed:true},
					{field:'content',title:'通知内容',width:400,fixed:true},
					{field:'feedBackContent',title:'落实情况',width:400,fixed:true},
					{field:'writeUserName',title:'创建人',width:80,fixed:true},
					{field:'isFinished',title:'是否落实',width:65,
						formatter:function(val){
							return FW.getEnumMap("OPR_YES_NO")[val];
						}	
					}
				]],
			"onLoadError": function(){
				//加载错误的提示 可以根据需要添加
				$("#grid1_wrap").hide();
			    $("#grid1_error").show();
			},
	        onLoadSuccess: function(data){
	            //远程无数据需要隐藏整个表格和对应的工具条，然后在无数据信息中指引用户新建
	            if(data && data.total==0){
	            	$("#grid1_wrap").hide();
	                $("#grid1_empty").show();
	            }else{
	            	$("#grid1_wrap").show();
	                $("#grid1_empty").hide();
	            }
	        },
	        onDblClickRow : function( rowIndex, rowData ){
		    	var url = "${basePath}page/operation/core/operationlog/LeaderNotice-updateLeaderNotice.jsp?id=" + rowData.id;
		    	addTabWithTree( "LeaderNoticeDetail"+rowData.id, "上级通知详情"+rowData.id, url,"opmm", "contentTb" );
		    }

	    });
		
		$("#btn_add").click(function(){
			var url = "${basePath}page/operation/core/operationlog/LeaderNotice-insertLeaderNotice.jsp";
			addTabWithTree( "addLeaderNotice", "新建上级通知", url,"opmm", "contentTb");
		});
		
		//高级搜索
		$("#btn_advlocal").click(function() {
			if($(this).hasClass("active")){
				$("#contentTb").ITCUI_GridSearch("end");
			}
			else{
				$("#contentTb").ITCUI_GridSearch("init",{"remoteSearch":true,"onParseArgs":function(arg){
					return {"search":JSON.stringify(arg)};
				},noSearchColumns:{5:true}});
			}
		});
		
	});
</script>
</head>
<body style="height: 100%;" class="bbox list-page">
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar1" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm">
	           <button type="button" class="btn btn-success" id="btn_add">新建</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	           <button type="button" class="btn btn-default" data-toggle="button" id="btn_advlocal">查询</button>
	         </div>
	    </div>
	    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
	    <div id="pagination_1" class="toolbar-pager" bottompager="#bottomPager">        
	    </div>
	</div>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div id="grid1_wrap" style="width:100%">
	    <table id="contentTb" pager="#pagination_1" class="eu-datagrid">
	    </table>
	</div>
	<!-- 下页器部分-->
	<div id="bottomPager" style="width:100%;margin-top:6px">
	</div>
	<!-- 错误信息-->
	<div class="row" id="grid1_error" style="display:none">
	    无法从服务器获取数据，请检查网络是否正常
	</div>
	<!-- 无数据 -->
	<div class="row" id="grid1_empty" style="display:none">
	    没有查找到上级通知的数据
	</div>
</body>
</html>